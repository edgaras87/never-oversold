<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     Infrastructure-establishment/.claude/skills/references/postgres-setup-walkthrough.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: moved — from .claude/skills/references/, beside rather
     than inside the skill directory (same normalization as the
     SKILL.md). Content verbatim below this header.
     Re-derived 2026-08-28: embedded file bodies (compose, bootstrap
     SQL, flyway.conf) and the verify-suite section list replaced by
     pointers to the templates/ masters beside this skill's
     references (ADR-0008); whys and traps kept (PLAN Step 6).
     Harvested 2026-08-28: .env carries a fourth key — the runtime
     application password the app reads from the environment — from
     checkout-system's lived .env.example (ADR-0007). -->

# PostgreSQL setup walkthrough — from nothing to a governed, verified ground

The end-to-end *do this* for one service, lived by two projects.
**Masters nothing**: the authority shape — roles, grants, the why — is
mastered in `postgres-role-split.md`; on any conflict the model wins.
Context it assumes (noted where it binds): the Execution Environment is
**podman local containers** driven by a compose file, and **Flyway**
runs as a compose one-shot as the only DDL path.

Placeholders: `<project>` — underscored in SQL identifiers
(`<project>_migrator`), hyphenated where project naming allows
(compose project name).

The files themselves are copy-and-fill masters in `templates/`,
beside this skill's `references/` (ADR-0008) — this walk carries the
whys and the order; the templates carry the bodies and their recall
comments. Fill a template, and the filled file is the run's own.
The templates implement the assumptions above; if the run's decided
constraints differ, you are **off-template**: derive from the model
(`postgres-role-split.md`), record the deviation in the run's log,
and expect it to harvest — never bend a decided constraint to fit a
template.

## 0 · Preconditions

- podman installed, a compose provider present; `podman compose config`
  runs clean (canonical command: `podman compose` — the front door,
  whatever provider stands behind it).
- The decisions already made and logged: PostgreSQL is a required
  Infrastructure Service; its constraints are named (migrations-only
  DDL; role-split authority). This walkthrough *implements* those
  decisions; it does not make them.

## 1 · Declare the service (compose)

`compose.yaml` — a `postgres` service and the Flyway one-shot (a
one-shot behind a profile: the only DDL path, hidden from plain
`up`). Copy-and-fill: `templates/compose.yaml`.

Podman notes: the `:Z` mount flag matters on SELinux hosts; volume and
network names get the compose project prefix automatically — never
write it manually. The `name:` pin is a lived correction: without it,
volume identity depends on the directory the file happens to run from.

## 2 · Split out credentials (`.env`)

`.env` (git-ignored) + committed `.env.example`. Copy-and-fill:
`templates/.env.example`. Keys: the bootstrap password, the migrator
password (**must equal** the literal in the bootstrap SQL), optional
`POSTGRES_PORT`, and the runtime application password (**must
equal** the runtime literal in the bootstrap SQL) — the app reads it
from the environment at bootstrap; the run that lived this walk
added the key. Rule: secrets and machine variance are variables;
**decided identities (db, schema, role names) stay literal** in the
files that use them — changing one is a committed decision, not
configuration.

## 3 · Write the bootstrap SQL (the model instantiated)

`infrastructure/postgres/init/bootstrap.sql` — runs **once**,
automatically, at the container's first start against an empty volume,
as the bootstrap identity, **connected to `POSTGRES_DB`**.
Copy-and-fill: `templates/bootstrap.sql` — its comments are the
recall layer, carried in full; the load-bearing lines are the
default privileges (every table and sequence a future migration
creates arrives already granted to runtime, so the split needs no
per-migration discipline and cannot erode).

Who creates the database: **the container**, from `POSTGRES_DB` —
owned by the bootstrap identity; migrator owns only the schema. No
`CREATE DATABASE` in the script.

## 4 · Land the verification suite

`infrastructure/postgres/verify-database-model.sql` — beside `init/`,
**not in it** (run on demand, never at container start).
Copy-and-fill: `templates/verify-database-model.sql`. It queries the
catalog against the model's claims — roles and capabilities,
database and schema ownership, schema privileges, default privileges
(the model's load-bearing mechanism, invisible to `\dn+`); expected
results ride as comments beside each query so the reader needs no
other doc open.

## 5 · Wire Flyway (config + empty migrations)

`infrastructure/flyway/conf/flyway.conf` — no credentials (env-passed
by compose); the url speaks the compose network's service name.
Copy-and-fill: `templates/flyway.conf`.

`infrastructure/flyway/migrations/` — empty (+`.gitkeep`) until work
earns schema; naming, when it does: `V<n>__<description>.sql`. **No
grant statements in migrations, ever** — default privileges handle it.

## 6 · Bring it up

```sh
podman compose config      # sanity: interpolation resolves, file renders
podman compose up -d       # first start: db created, bootstrap SQL runs
podman compose ps          # expect: <project>-postgres Up (healthy)
```

## 7 · Verify — both ways, always

**Catalog check:**

```sh
podman exec -i <project>-postgres \
  psql -U postgres -d <project_db> < infrastructure/postgres/verify-database-model.sql
```

**Behavioral check** (the constraint attempted and refused, live):

```sh
podman exec -it <project>-postgres \
  psql -U <project>_runtime -d <project_db> -c 'CREATE TABLE t(i int);'
# expected: ERROR: permission denied for schema <project_schema>
```

**Flyway as migrator** (connects, sees the schema, empty history —
correct on a fresh ground):

```sh
podman compose run --rm flyway info
```

## 8 · Know the reset path

The bootstrap SQL runs only against an empty volume. After any change
to it (**destructive — drops all data; explicit confirmation first**):

```sh
podman compose down --volumes
podman compose up -d
```

…then re-run step 7, both checks.

## 9 · Hand off

The connection facts the system's code needs: host
`localhost:${POSTGRES_PORT:-5432}` from outside, `postgres:5432`
container-to-container; database `<project_db>`, schema
`<project_schema>`; **the application connects as `<project>_runtime`
and nothing else** — migrator is the migration tool's identity alone;
the bootstrap identity is an occasional admin lens, never wired in.
Day-to-day IDE/psql inspection: runtime.

These facts land in the project's **living infrastructure contract** —
one section per service, grown at each later addition, the refusals
written as contract terms. Note there that the ground files were
filled from this skill's templates — the trail a later reader needs
to find the masters.
