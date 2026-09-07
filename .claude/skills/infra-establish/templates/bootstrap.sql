-- Template — master copy in correctness-by-construction (ADR-0008);
-- checked against concept v1 (ADR-0003, ADR-0005 — practice-born).
-- Extracted 2026-08-28 from checkout-system's lived
-- infrastructure/postgres/init/bootstrap.sql (PLAN Step 6). Changes
-- on extraction: identities and local-dev passwords generalized to
-- placeholders (<project>, <project_db>, <project_schema>); the
-- run-local model path and ADR references generalized to neutral
-- wording. Copy into a run at the same path and fill; the filled
-- file is the run's own (ADR-0008).

-- infrastructure/postgres/init/bootstrap.sql
--
-- Purpose:
--   Instantiate the postgres role model for this project: the two working
--   identities, the application schema, and the privilege boundaries.
--   Model: postgres-role-split.md beside this skill's walkthrough —
--   this file instantiates, never re-derives.
--
-- Run as:
--   the bootstrap identity (postgres) — runs automatically, ONCE, at the
--   container's first start against an empty volume (docker-entrypoint-initdb.d;
--   files there run in lexical order — irrelevant while this is the only one).
--   Re-run = full reset: podman compose down --volumes, then up.
--
-- Database context:
--   <project_db> — the container's entrypoint creates POSTGRES_DB (compose.yaml)
--   at first start, then runs this script CONNECTED TO IT: schema and grant
--   statements below land in this database. CREATE ROLE is the exception —
--   roles are cluster-wide, in no database.
--
-- Local development note:
--   Role passwords are explicit local-dev placeholders, coupled to .env
--   (FLYWAY_MIGRATOR_PASSWORD must equal the migrator password here).
--
-- Boundary:
--   Identities, schema, and privilege boundaries only — no tables, no
--   application objects; those arrive solely through the migration tool.

-- the two identities of the authority split
CREATE ROLE <project>_migrator LOGIN PASSWORD '<project>_migrator_localdev';
CREATE ROLE <project>_runtime  LOGIN PASSWORD '<project>_runtime_localdev';

-- PUBLIC (the grant target: every role, present and future) loses the default
-- right to connect; access to this database exists only by the named grants
-- below. The bootstrap identity needs no grant — superusers bypass checks.
REVOKE CONNECT ON DATABASE <project_db> FROM PUBLIC;
GRANT  CONNECT ON DATABASE <project_db> TO <project>_migrator;
GRANT  CONNECT ON DATABASE <project_db> TO <project>_runtime;

-- the application schema: owned by migrator, consumable by runtime
CREATE SCHEMA <project_schema> AUTHORIZATION <project>_migrator;
GRANT USAGE ON SCHEMA <project_schema> TO <project>_runtime;

-- the DEFAULT SCHEMA named "public" (unrelated to the PUBLIC grant target
-- above) stops being usable by anyone — no side door beside the governed
-- schema. (PG15+ already revokes CREATE; this makes the whole stance explicit.)
REVOKE ALL ON SCHEMA public FROM PUBLIC;

-- the load-bearing lines: every table and sequence a future migration creates
-- arrives already granted to runtime — the split needs no per-migration
-- discipline, so it cannot erode
ALTER DEFAULT PRIVILEGES FOR ROLE <project>_migrator IN SCHEMA <project_schema>
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO <project>_runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE <project>_migrator IN SCHEMA <project_schema>
  GRANT USAGE, SELECT ON SEQUENCES TO <project>_runtime;

-- search_path: where unqualified names resolve — pinned so migrations and
-- application statements land in the governed schema, never in public. A
-- default for convenience; the authority boundary is the grants above.
ALTER ROLE <project>_migrator SET search_path = <project_schema>;
ALTER ROLE <project>_runtime  SET search_path = <project_schema>;
