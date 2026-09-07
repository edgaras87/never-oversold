<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     Infrastructure-establishment/.claude/skills/references/postgres-role-split.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: moved — from .claude/skills/references/, beside rather
     than inside the skill directory (same normalization as the
     SKILL.md). Content verbatim below this header. -->

# PostgreSQL role split — database authority as a grant-enforced property

How database authority is split into roles on PostgreSQL. Generic
throughout — a using project's concrete names, passwords, and wiring
live in that project's own manuals, never here. On any conflict with
the setup walkthrough, **this model wins**.

## The principle

Making a database available to a system is not one grant but two
decisions: *who may change what the database is* (structure — DDL),
and *who may change what it holds* (data — DML). The rule:

> **The running application must not control database structure.**

Schema exists solely through versioned migrations; the application
operates on data inside a structure it cannot alter. The split is
enforced by the **database's own grant system** — never by convention,
code review, or trust: an application identity that *cannot* issue DDL
makes structural drift impossible rather than forbidden.

## The roles

Three identities, two of them the model's own:

- **`migrator`** — the structure authority. Owns the application
  schema and every object in it; the **only identity migrations run
  as** (Flyway or equivalent connects as `migrator`, nothing else
  does). DDL within that schema — and only there: it does not own the
  database, cannot drop it, creates no schemas beyond its own. Not a
  superuser.
- **`runtime`** — the data authority; **what the running application
  connects as.** DML only: read and write rows, use sequences — no
  create, no alter, no drop, no ownership. If the application attempts
  DDL, the database refuses.
- *(the bootstrap identity)* — the server's existing admin role
  (`postgres` in a stock container; never created by this model) runs
  the setup **once**: creates the database (and owns it), the two
  roles, the application schema, and the grants. Not part of the
  running system; never appears in application or migration config.

**Database ownership stays above the split** — with the bootstrap
identity by default. A dedicated database-owner tier is real on shared
or managed clusters — an extension by need, not the base model.

## The application schema — own it, don't use `public`

The mechanism is **schema-scoped ownership**: `migrator` owns a
**project-named schema**; everything hangs off that ownership. The
default `public` schema is the wrong home — a shared legacy surface
whose ownership is not the migrator's to have. The application's
`search_path` names the project schema — and `public` is stripped of
PUBLIC privileges so "unused" is enforced, not assumed.

## The grant boundaries (run once by the bootstrap identity)

```sql
CREATE ROLE migrator LOGIN;
CREATE ROLE runtime  LOGIN;
CREATE DATABASE app;                    -- owned by the bootstrap identity

-- access is explicit: nobody by default, the working identities by grant
REVOKE CONNECT ON DATABASE app FROM PUBLIC;
GRANT  CONNECT ON DATABASE app TO migrator;
GRANT  CONNECT ON DATABASE app TO runtime;

-- connected to app, as the bootstrap identity
CREATE SCHEMA app AUTHORIZATION migrator;
GRANT USAGE ON SCHEMA app TO runtime;
REVOKE ALL ON SCHEMA public FROM PUBLIC;   -- public is no application surface

-- future objects migrator creates arrive consumable by runtime automatically
ALTER DEFAULT PRIVILEGES FOR ROLE migrator IN SCHEMA app
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE migrator IN SCHEMA app
  GRANT USAGE, SELECT ON SEQUENCES TO runtime;
```

The load-bearing lines are the **default privileges** pair: every
table and sequence a future migration creates arrives already granted
to `runtime` — the split needs no per-migration grant discipline, so
it cannot erode migration by migration. The connect revoke closes the
outermost door the same way: access at every level exists by grant,
never by default.

`runtime` deliberately receives no `TRUNCATE`, no `REFERENCES`, no
`CREATE` anywhere; `migrator` deliberately owns one schema, not the
database and not the server.

## Naming across a cluster

Roles are **cluster-wide**. On any cluster serving more than one
project, role names carry the project as a prefix
(`<project>_migrator`, `<project>_runtime`), underscored — hyphens are
not legal in unquoted SQL identifiers, and quoted names are a
permanent tax. The schema, living inside the project's database, may
carry the bare project name.

## Why this split and not less

- **One identity (app == owner):** the common default, and the
  principle's direct violation — any ORM auto-DDL, any stray migration
  call, any injected statement can restructure the database. Refused.
- **Convention only ("the app just doesn't run DDL"):** unenforced
  rules decay; the grant system makes the rule a property.
- **More roles** (a dedicated database owner, readers, per-service
  identities, row-level security): real needs at larger scope —
  extensions by need, not the base model.

## What a using project supplies

Concrete role names under the cluster naming rule and their
credentials (secrets handling is the project's), the schema name and
the application's `search_path`, which migration tool connects as
`migrator`, where the bootstrap SQL lives and how it is applied, and
how the model is verified on the ground (catalog checks plus a
behavioral DDL-refusal check) — all in the project's manuals and
ground files, referencing this model, never restating it.
