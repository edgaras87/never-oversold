# Infrastructure contract — what the builder may rely on

<!-- The builder-facing manual: one section per service, grown at
     every later addition, never rewritten from scratch. It masters
     the ground's vocabulary and states the refusals as contract
     terms. How to stand the ground up is the operator manual's;
     why it is shaped this way is the ADRs' (0004–0006). -->

## Vocabulary

- **Execution environment** — the local runtime host for
  infrastructure: podman local containers, compose-driven, rootless
  (ADR-0004). Front door: `podman compose` at the repository root.
- **Infrastructure service** — a capability provided to the system.
  There is one: PostgreSQL (ADR-0005). Everything not listed below
  is not provided, and the not-provisioned list in ADR-0005 says why.
- **Infrastructure service constraint** — a requirement or
  limitation imposed on a service, enforced by the service itself
  (ADR-0006). The refusals below are those constraints seen from
  the builder's side.

## PostgreSQL

**What it is.** PostgreSQL 17 (image `docker.io/library/postgres:17`,
one instance, container `never-oversold-postgres`), holding one
database, `never_oversold`, with one application schema,
`never_oversold`. The two numbers the promise is about — an item's
on-hand-count and its reservations — live here and nowhere else.

**How to reach it.**

| From | Host | Port | Notes |
|---|---|---|---|
| inside the compose network (the ledger's instances, Flyway) | `postgres` | `5432` | the service name resolves; no published port involved |
| outside (the host: the evidence's witness read, inspection) | `localhost` | `${POSTGRES_PORT}` from `.env`, default `5432` | the published port; above 1024 because rootless |

Connection URL shape: `postgresql://<identity>:<password>@<host>:<port>/never_oversold`.
Passwords come from `.env` (`.env.example` is the committed
shape); they never appear in code, config, or this document.

**Which identity to connect as.**

| Identity | Who uses it | May | May not |
|---|---|---|---|
| `runtime` | **the running ledger, and only it**; the evidence's witness read; day-to-day inspection | SELECT, INSERT, UPDATE, DELETE on every table and USAGE, SELECT on every sequence in `never_oversold` — including every one a future migration creates | CREATE, ALTER, DROP anything; own anything; TRUNCATE; REFERENCES; touch `public` |
| `migrator` | **Flyway, and only it** | DDL inside `never_oversold`, which it owns | own the database; create schemas; anything outside its schema; log in from application code or config |
| `postgres` | the bootstrap, once, at first start | everything — it is the superuser | appear in any application or migration configuration; be used for routine work |

The password the ledger reads is `NEVER_OVERSOLD_RUNTIME_PASSWORD`
from the environment; nothing else in its environment names a
database identity.

**What the store refuses — contract terms.** Each is enforced by
the server's grant system and was watched being refused on
2026-09-11 (operator manual, devlog):

1. **The running application cannot change structure.** A DDL
   statement as `runtime` fails with `permission denied for schema
   never_oversold`. Do not design around it; there is no way
   around it.
2. **No identity but the two named ones can connect** to
   `never_oversold`. PUBLIC has no CONNECT; a role without the
   grant fails with `permission denied for database`.
3. **`public` is no application surface.** Nothing may be created
   there and nothing should be looked for there; both roles'
   `search_path` is `never_oversold`.
4. **New objects arrive already usable by `runtime`.** A migration
   never writes a GRANT; the default privileges do it. A migration
   that writes one is wrong, not helpful.

**What the store offers — inventory, not a choice.** The server
can make two concurrent writers to one thing disagree; its faces
of that facility are row-level locks, unique and check constraints
checked at commit, serializable isolation, advisory locks. The
ground proved the facility refuses — an advisory lock held by one
session, a second cancelled by `lock_timeout` — and chose that
probe because it needs no schema, not because it is preferred. No
face is endorsed here: a slice that needs the facility chooses its
face in its own plan, with its why. The contract promises
only that the facility exists and that a refused writer sees an
error, never a silent success. Multi-statement transactions commit
atomically; that too is inventory.

**How schema changes are made.** Only through Flyway, as
`migrator`: a file `infrastructure/flyway/migrations/V<n>__<description>.sql`,
then `podman compose run --rm flyway migrate`. Flyway's history
table lives in `never_oversold` as `flyway_schema_history`, created
on the first migrate. The ledger's code never issues DDL and never
carries `migrator`'s credentials.

**What survives what.** Data lives in the named volume
`never-oversold_postgres-data` and survives `podman compose down`
and any instance's death; only `down --volumes` destroys it, and
that also re-runs the bootstrap on the next start. Nothing in a
ledger container is state.

**Provenance.** The ground files were filled from the
infra-establish skill's templates (kit @ af16eb7), this repo's
names substituted and one verify query added; the role model they
instantiate is that skill's `postgres-role-split.md`.
