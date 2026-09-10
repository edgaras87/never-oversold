# Architecture

<!-- Describes the system AS IT IS NOW — not the aspiration. 1–2 pages max.
     Update trigger: a plan step's gate closes and this no longer matches
     reality. For the WHY behind any shape, link the ADR. -->

## Overview

The ground stands and nothing runs on it yet: one PostgreSQL
instance under podman compose, governed by a two-role authority
split, holding an empty application schema. The ledger — one area
by the system definition (L3) — is not built; it arrives at
bootstrap as instances of one image beside the store.

```
┌──────────────────────────┐        ┌──────────────────────────┐
│  never-oversold-postgres │        │  the ledger (not yet)    │
│  PostgreSQL 17           │ ◀───── │  N instances, one image  │
│  db never_oversold       │  5432  │  connects as `runtime`   │
│  schema never_oversold   │        └──────────────────────────┘
└──────────────────────────┘
        ▲  published ${POSTGRES_PORT}: the witness read, Flyway as `migrator`
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

## Invariants

<!-- What must NEVER happen to the data / system, and where each rule
     is enforced (DB constraint, module boundary, ...). -->
- The promise's four invariants are the registry's
  (`docs/system/registry.md`), none yet enforced anywhere — no
  schema, no code.
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
