-- infrastructure/postgres/verify-database-model.sql
--
-- The catalog half of the two-way verification: the ground's real state
-- queried against the role-split model's claims (ADR-0006, C1–C5). Run
-- on demand, never at container start:
--
--   podman exec -i never-oversold-postgres \
--     psql -U postgres -d never_oversold < infrastructure/postgres/verify-database-model.sql
--
-- Expected results ride as comments beside each query — this file needs no
-- other document open. The behavioral half (DDL attempted as runtime and
-- refused; an ungranted connection refused; T2's tool shown) lives in the
-- operator manual and the devlog's Step 3 entry. Filled from the
-- infra-establish templates (kit @ af16eb7); the role filter is an explicit
-- IN list because this cluster's role names carry no project prefix.

\echo ''
\echo '=== 1 · Project roles and capabilities ==='
-- expected: exactly migrator and runtime; for both:
--   rolsuper=f, rolcreatedb=f, rolcreaterole=f, rolcanlogin=t
SELECT rolname, rolsuper, rolcreatedb, rolcreaterole, rolcanlogin
FROM pg_roles
WHERE rolname IN ('migrator', 'runtime')
ORDER BY rolname;

\echo ''
\echo '=== 2 · Database ownership — stays above the split ==='
-- expected: never_oversold owned by postgres (the bootstrap identity)
SELECT datname, pg_get_userbyid(datdba) AS owner
FROM pg_database
WHERE datname = 'never_oversold';

\echo ''
\echo '=== 3 · Schema ownership ==='
-- expected: never_oversold owned by migrator;
--           public untouched, owned by pg_database_owner
SELECT nspname, pg_get_userbyid(nspowner) AS owner
FROM pg_namespace
WHERE nspname IN ('never_oversold', 'public')
ORDER BY nspname;

\echo ''
\echo '=== 4 · Schema privileges ==='
-- expected: migrator USAGE=t CREATE=t (owner);
--           runtime  USAGE=t CREATE=f
SELECT r.rolname,
       has_schema_privilege(r.rolname, 'never_oversold', 'USAGE')  AS usage,
       has_schema_privilege(r.rolname, 'never_oversold', 'CREATE') AS "create"
FROM pg_roles r
WHERE r.rolname IN ('migrator', 'runtime')
ORDER BY r.rolname;

\echo ''
\echo '=== 5 · Default privileges — the load-bearing mechanism ==='
-- expected, for owner migrator in schema never_oversold, grantee runtime,
-- is_grantable=f throughout:
--   sequence: USAGE, SELECT
--   table:    SELECT, INSERT, UPDATE, DELETE
SELECT CASE d.defaclobjtype
         WHEN 'r' THEN 'table'
         WHEN 'S' THEN 'sequence'
         ELSE d.defaclobjtype::text
       END                            AS objtype,
       pg_get_userbyid(a.grantee)     AS grantee,
       a.privilege_type,
       a.is_grantable
FROM pg_default_acl d
     CROSS JOIN LATERAL aclexplode(d.defaclacl) a
     JOIN pg_namespace n ON n.oid = d.defaclnamespace
WHERE n.nspname = 'never_oversold'
ORDER BY objtype, privilege_type;

\echo ''
\echo '=== 6 · Connect privilege — access by grant, never by default ==='
-- expected: migrator t, runtime t, and PUBLIC (the empty-string role) f
SELECT 'migrator' AS who, has_database_privilege('migrator', 'never_oversold', 'CONNECT') AS connect
UNION ALL SELECT 'runtime',  has_database_privilege('runtime',  'never_oversold', 'CONNECT')
UNION ALL SELECT 'PUBLIC',   has_database_privilege(0::oid,     'never_oversold', 'CONNECT')
ORDER BY who;
