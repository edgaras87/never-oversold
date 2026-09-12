# Architecture

<!-- Describes the system AS IT IS NOW — not the aspiration. 1–2 pages max.
     Update trigger: a plan step's gate closes and this no longer matches
     reality. For the WHY behind any shape, link the ADR. -->

## Overview

The ledger runs on the ground, empty: instances of one build
output, each connecting to the store as `runtime` and nothing
else, each answering HTTP — a health check that names the store
as a component, and one probe that does a round-trip and dies at
the first slice. No business behavior, no schema: the promise's
numbers have no table yet. Beside it, at test scope only, the
evidence harness: its own throwaway store of the ground's major
version, migrated from the one home, and the machinery to race
real instances of the ledger against it.

```
┌──────────────────────────┐        ┌──────────────────────────────┐
│  never-oversold-postgres │        │  the ledger                  │
│  PostgreSQL 17           │ ◀───── │  N instances, one build      │
│  db never_oversold       │  5432  │  HTTP door · health · probe  │
│  schema never_oversold   │        │  connects as `runtime` only  │
└──────────────────────────┘        └──────────────────────────────┘
        ▲  published ${POSTGRES_PORT}: the witness read, Flyway as `migrator`

  test scope ─ the harness: a throwaway postgres:17 (Testcontainers),
  migrated harness-side; forked instances of the ledger raced through
  their doors; the witness read from the store directly
```

## Components

### PostgreSQL — the store

Responsibility: holds the two numbers the promise is about — each
item's on-hand-count and its reservations — and nothing else holds
them; makes two concurrent writers to one thing disagree, and
commits multi-statement transactions atomically.
Why shaped this way: ADR-0005 (why this service and no other),
ADR-0006 (the authority split and every constraint's enforcement),
ADR-0004 (the environment it runs in).

### The ledger — the application

Responsibility: today, nothing of the promise — it starts, connects
as `runtime`, answers health with the store's state, and answers
the probe. The reservation ledger, the one area the definition
names (L3), arrives with SL-1 as a feature package beside the
probe's grave.
Why shaped this way: ADR-0007 (the stack; the migration tool
outside the app; one identity), ADR-0008 (package by feature,
package-private, depth earned per feature).

### The evidence harness — test scope

Responsibility: create adversity for real and read the witness
from persisted state. Proven at bootstrap on contention: requests
held at a barrier and released at one instant, across three
instances of the ledger running as separate processes against one
store, every response asserted, the store read from outside.
Why shaped this way: ADR-0009 (plural instances as processes; the
suite self-contained, the ground not required up).

## Invariants

<!-- What must NEVER happen to the data / system, and where each rule
     is enforced (DB constraint, module boundary, ...). -->
- The promise's four invariants are the registry's
  (`docs/system/registry.md`), none yet enforced anywhere — no
  schema, no business code.
- The running ledger knows one database identity, `runtime`, its
  password from the environment — enforced by the configuration
  carrying no other and the build carrying no migration or
  object-mapping library at runtime scope (ADR-0007; bootstrap
  requirements §3).
- The running application cannot change structure — enforced by
  the store's grant system (`runtime` has no CREATE, ALTER, DROP;
  ADR-0006 C1).
- Nothing connects to `never_oversold` but `migrator` and
  `runtime` — enforced by CONNECT revoked from PUBLIC (ADR-0006 C3).

## Codemap

<!-- Where to find things. Directory → what lives there. -->
| Path | What lives there |
|---|---|
| `compose.yaml`, `.env.example` | the ground's declaration and its secrets' shape |
| `infrastructure/postgres/` | the bootstrap SQL (runs once) and the verify suite (on demand) |
| `infrastructure/flyway/` | the only DDL path: config and migrations (none yet) |
| `docs/infrastructure/` | the operator manual and the infrastructure contract |
| `docs/system/` | the truth set: intent, definition, registry |
| `docs/construction/` | the bootstrap requirements — what the skeleton delivers and refuses |
| `pom.xml`, `mvnw` | the build: every dependency with its earning reason; the wrapper |
| `src/main/java/…/neveroversold/` | the entry point; `probe/` is scaffolding that dies at SL-1 |
| `src/main/resources/application.yaml` | the one identity, the password from the environment, the absences commented |
| `src/test/java/…/neveroversold/` | the evidence: `testsupport/` is the harness (the throwaway store, the test bases, the forked instance); `*IT` are the integration tests |
