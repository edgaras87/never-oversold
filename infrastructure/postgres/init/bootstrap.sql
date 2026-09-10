-- infrastructure/postgres/init/bootstrap.sql
--
-- Purpose:
--   Instantiate the postgres role model for this project: the two working
--   identities, the application schema, and the privilege boundaries.
--   Model: the role-split reference beside the infra-establish walkthrough
--   (kit @ af16eb7); this file instantiates, never re-derives. The
--   constraints it enforces are ADR-0006's C1–C5.
--
-- Run as:
--   the bootstrap identity (postgres) — runs automatically, ONCE, at the
--   container's first start against an empty volume (docker-entrypoint-initdb.d;
--   files there run in lexical order — irrelevant while this is the only one).
--   Re-run = full reset: podman compose down --volumes, then up.
--
-- Database context:
--   never_oversold — the container's entrypoint creates POSTGRES_DB (compose.yaml)
--   at first start, then runs this script CONNECTED TO IT: schema and grant
--   statements below land in this database. CREATE ROLE is the exception —
--   roles are cluster-wide, in no database.
--
-- Names:
--   one dedicated cluster, so the model's bare role names stand — migrator,
--   runtime (ADR-0006); database and schema never_oversold, underscored
--   because a hyphen is not a legal unquoted identifier.
--
-- Local development note:
--   Role passwords are explicit local-dev placeholders, coupled to .env
--   (FLYWAY_MIGRATOR_PASSWORD must equal the migrator password here;
--   NEVER_OVERSOLD_RUNTIME_PASSWORD the runtime one).
--
-- Boundary:
--   Identities, schema, and privilege boundaries only — no tables, no
--   application objects; those arrive solely through the migration tool.

-- the two identities of the authority split
CREATE ROLE migrator LOGIN PASSWORD 'migrator_localdev';
CREATE ROLE runtime  LOGIN PASSWORD 'runtime_localdev';

-- PUBLIC (the grant target: every role, present and future) loses the default
-- right to connect; access to this database exists only by the named grants
-- below. The bootstrap identity needs no grant — superusers bypass checks.
REVOKE CONNECT ON DATABASE never_oversold FROM PUBLIC;
GRANT  CONNECT ON DATABASE never_oversold TO migrator;
GRANT  CONNECT ON DATABASE never_oversold TO runtime;

-- the application schema: owned by migrator, consumable by runtime
CREATE SCHEMA never_oversold AUTHORIZATION migrator;
GRANT USAGE ON SCHEMA never_oversold TO runtime;

-- the DEFAULT SCHEMA named "public" (unrelated to the PUBLIC grant target
-- above) stops being usable by anyone — no side door beside the governed
-- schema. (PG15+ already revokes CREATE; this makes the whole stance explicit.)
REVOKE ALL ON SCHEMA public FROM PUBLIC;

-- the load-bearing lines: every table and sequence a future migration creates
-- arrives already granted to runtime — the split needs no per-migration
-- discipline, so it cannot erode
ALTER DEFAULT PRIVILEGES FOR ROLE migrator IN SCHEMA never_oversold
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE migrator IN SCHEMA never_oversold
  GRANT USAGE, SELECT ON SEQUENCES TO runtime;

-- search_path: where unqualified names resolve — pinned so migrations and
-- application statements land in the governed schema, never in public. A
-- default for convenience; the authority boundary is the grants above.
ALTER ROLE migrator SET search_path = never_oversold;
ALTER ROLE runtime  SET search_path = never_oversold;
