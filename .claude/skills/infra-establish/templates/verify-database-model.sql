-- Template — master copy in correctness-by-construction (CBC ADR-0008);
-- checked against concept v1 (CBC ADR-0003, CBC ADR-0005 — practice-born).
-- Copy into a run at the same path and fill; the filled file is the
-- run's own (CBC ADR-0008).

-- infrastructure/postgres/verify-database-model.sql
--
-- The catalog half of the two-way verification (walk step 5): the ground's
-- real state queried against the role-split model's claims. Run on demand,
-- never at container start:
--
--   podman exec -i <project>-postgres \
--     psql -U postgres -d <project_db> < infrastructure/postgres/verify-database-model.sql
--
-- Expected results ride as comments beside each query — this file needs no
-- other document open. The behavioral half (DDL attempted as runtime and
-- refused) lives in the operator manual and the ground's record — the
-- establishment log, or the devlog in a repo with records.

\echo ''
\echo '=== 1 · Project roles and capabilities ==='
-- the LIKE filter assumes prefixed roles (the shared-cluster case) and
-- also surfaces stray roles; on a dedicated cluster with bare names it
-- becomes an explicit IN list: WHERE rolname IN ('migrator', 'runtime')
-- expected: exactly <project>_migrator and <project>_runtime; for both:
--   rolsuper=f, rolcreatedb=f, rolcreaterole=f, rolcanlogin=t
SELECT rolname, rolsuper, rolcreatedb, rolcreaterole, rolcanlogin
FROM pg_roles
WHERE rolname LIKE '<project>\_%'
ORDER BY rolname;

\echo ''
\echo '=== 2 · Database ownership — stays above the split ==='
-- expected: <project_db> owned by postgres (the bootstrap identity)
SELECT datname, pg_get_userbyid(datdba) AS owner
FROM pg_database
WHERE datname = '<project_db>';

\echo ''
\echo '=== 3 · Schema ownership ==='
-- expected: <project_schema> owned by <project>_migrator;
--           public untouched, owned by pg_database_owner
SELECT nspname, pg_get_userbyid(nspowner) AS owner
FROM pg_namespace
WHERE nspname IN ('<project_schema>', 'public')
ORDER BY nspname;

\echo ''
\echo '=== 4 · Schema privileges ==='
-- expected: <project>_migrator USAGE=t CREATE=t (owner);
--           <project>_runtime  USAGE=t CREATE=f
SELECT r.rolname,
       has_schema_privilege(r.rolname, '<project_schema>', 'USAGE')  AS usage,
       has_schema_privilege(r.rolname, '<project_schema>', 'CREATE') AS "create"
FROM pg_roles r
WHERE r.rolname LIKE '<project>\_%'
ORDER BY r.rolname;

\echo ''
\echo '=== 5 · Default privileges — the load-bearing mechanism ==='
-- expected, for owner <project>_migrator in schema <project_schema>, grantee
-- <project>_runtime, is_grantable=f throughout:
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
WHERE n.nspname = '<project_schema>'
ORDER BY objtype, privilege_type;

\echo ''
\echo '=== 6 · Connect privilege — access by grant, never by default ==='
-- expected: <project>_migrator t, <project>_runtime t, and PUBLIC (the
-- empty-string role, oid 0) f — the bootstrap's REVOKE CONNECT, checked
SELECT '<project>_migrator' AS who, has_database_privilege('<project>_migrator', '<project_db>', 'CONNECT') AS connect
UNION ALL SELECT '<project>_runtime',  has_database_privilege('<project>_runtime',  '<project_db>', 'CONNECT')
UNION ALL SELECT 'PUBLIC',             has_database_privilege(0::oid,               '<project_db>', 'CONNECT')
ORDER BY who;
