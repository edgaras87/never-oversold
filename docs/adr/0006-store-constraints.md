# 0006. Constraints on the store, each enforced by the store itself

Date: 2026-09-11
Status: Accepted

## Context

PostgreSQL is the whole service set (ADR-0005). A constraint on a
service is governance of the ground — a requirement or limitation
the ground imposes on the service — stated as a principle and
**enforced by a mechanism**, never by convention, review, or trust.
A constraint without enforcement is a wish. These are not registry
invariants: none is a property of persisted state under adversity.

Standing knowledge exists for PostgreSQL's governance: the
role-split model in the infra-establish skill's references
(`postgres-role-split.md`), lived by two projects. It is the
rebuttable default here; this record checks it against this
project's facts and deviates only where a fact defeats it, naming
the defeater.

## Options considered

1. **The role-split model as it stands:** structure authority and
   data authority as two database roles, ownership scoped to a
   project schema, access by grant only, `public` stripped; the
   bootstrap identity runs the setup once and never appears in the
   running system. Checked against this project: one dedicated
   cluster in one container, one application, one schema. Nothing
   defeats it.
2. **One identity, application owns everything.** The principle's
   direct violation: any stray DDL from the running ledger
   restructures the store. Refused, as the model refuses it.
3. **Convention only.** "The application does not run DDL" as a
   rule in a document. Unenforced; decays. Refused.
4. **More roles** — a dedicated database owner, a read-only witness
   identity, per-slice identities. Real needs at larger scope; not
   this ground's. The witness read is done as `runtime` (SELECT is
   within its grant) from the host. Not taken; may re-enter by need.

## Decision

Option 1, the model as it stands, with this project's names. The
constraints, each with its enforcement:

| # | Constraint | Enforcement |
|---|---|---|
| C1 | The running application must not control database structure. | The grant system: `runtime` holds DML on the project schema and nothing else — no CREATE, ALTER, DROP, no ownership; DDL from it is refused by the server. |
| C2 | Structure exists only through versioned migrations. | Only `migrator` owns the schema and its objects; only the migration tool connects as `migrator`; the application's configuration never carries `migrator`'s credentials. |
| C3 | Access exists by grant, never by default. | `REVOKE CONNECT ... FROM PUBLIC` on the database; `GRANT CONNECT` to the two roles only; `public` schema stripped of PUBLIC privileges. |
| C4 | Future objects arrive already usable by the application. | `ALTER DEFAULT PRIVILEGES FOR ROLE migrator IN SCHEMA never_oversold` granting DML on tables and usage on sequences to `runtime` — the split cannot erode migration by migration. |
| C5 | The bootstrap identity runs once and is not part of the running system. | The stock image's `postgres` superuser applies the bootstrap SQL at first start (the image's init hook) and appears in no application or migration configuration. |
| C6 | The store's data outlives every instance of the ledger. | A named volume declared in `compose.yaml` for the data directory; the ledger's containers hold no data. |
| C7 | The store is reachable from the host for the witness read, and from the instances by name. | A published port above 1024 in `compose.yaml` (rootless, ADR-0004); the compose network resolves the service name for the instances. |
| C8 | Secrets never enter the repository. | Passwords live in `.env`, ignored by `.gitignore`; `.env.example` carries placeholders; `compose.yaml` interpolates. Identities stay literal, secrets are variables. |
| C9 | The store's version is pinned. | The image tag pins the major (`postgres:17`); the digest actually pulled is recorded in the operator manual. |

**Names.** One dedicated cluster, so the model's bare role names
stand: `migrator`, `runtime`. The database and the application
schema are `never_oversold` — the project name, underscored,
because a hyphen is not legal in an unquoted identifier and a quoted
name is a permanent tax. The application's `search_path` names the
schema.

**Verification, both ways** (the next commits): the catalog check
queries role attributes, ownership, grants and default privileges
against the claims above, expected results beside each query; the
behavioral check attempts DDL as `runtime` and watches the refusal,
and attempts a connection as an ungranted role and watches it
refused. Beside the constraints, one capability is shown, not
governed: T2's tool — two sessions writing one row, one made to
wait or fail by the server — because SL-1 needs it to exist and the
ground must prove it does.

## Consequences

Good: structural drift is impossible rather than forbidden; the
ledger's instances cannot alter what they run on; every door is a
grant a reader can query. Bad: two credentials to manage instead of
one; migrations are a separate act with a separate identity, which
the operator manual must make routine; and a future need — a
witness identity, a second application — re-enters this decision
rather than being added by hand.
