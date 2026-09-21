# Architecture

<!-- Describes the system AS IT IS NOW — not the aspiration. 1–2 pages max.
     Update trigger: a plan step's gate closes and this no longer matches
     reality. For the WHY behind any shape, link the ADR. -->

## Overview

The ledger runs on the ground and holds its first invariant:
instances of one build output, each connecting to the store as
`runtime` and nothing else, each answering HTTP — reserve, adjust,
and a health check that names the store. The store holds the two
numbers the promise is about, per item, and the wall between them:
a check constraint that no writer can pass, and an admit that is
one conditional statement against the row. The application keeps
no item state and reads no clock; expiry is the store's clock.
Beside it, at test scope only, the evidence harness: its own
throwaway store of the ground's major version, migrated from the
one home, the machinery to race real instances, and the witness
that reads the invariant from the store from outside them all.

```
┌──────────────────────────┐        ┌──────────────────────────────┐
│  never-oversold-postgres │        │  the ledger                  │
│  PostgreSQL 17           │ ◀───── │  N instances, one build      │
│  db never_oversold       │  5432  │  HTTP door: reserve, adjust  │
│  item ── the wall ──┐    │        │  health                      │
│  reservation        │    │        │  connects as `runtime` only  │
└─────────────────────┼────┘        └──────────────────────────────┘
        ▲             └ CHECK reserved <= on_hand_count
        │  published ${POSTGRES_PORT}: the witness read, Flyway as `migrator`

  test scope ─ the harness: a throwaway postgres:17 (Testcontainers),
  migrated harness-side; forked instances raced through their doors;
  the witness read from the store by plain JDBC; ArchUnit rules on
  the compiled classes for what the ledger must not contain
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

Responsibility: the reservation ledger, the one area the definition
names (L3), as one feature package `reservation`: the door
(`ReservationController`, `DoorProblems`), the one entry path to
the numbers (`Ledger`), the rows as persisted (`Item`,
`Reservation`), and the vocabulary in two public sub-packages —
`values` (what a request may say) and `problems` (the three
answers besides success). Reserve is one conditional statement
whose row count is the decision; adjust is one insert-or-update
that creates an unknown item and refuses a count under the held
units. No service layer, no repository, no ORM, no clock, no
in-memory state.
Why shaped this way: ADR-0007 (the stack; the migration tool
outside the app; one identity), ADR-0008 (package by feature,
package-private, depth earned per feature; its note on
vocabulary sub-packages), ADR-0010 (the door's conventions),
ADR-0011 (an item becomes known by its first adjustment); the
wall's owners per guarantee in the slice record
(`docs/construction/sl-1-no-over-admission.md`, §7).

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
- **SL-1, closed:** for every item, the sum of active reservations
  ≤ on-hand-count — enforced by the store: `item_never_oversold`
  (`CHECK (reserved <= on_hand_count)`) refuses any over-held row
  by any path, and the admit's conditional `UPDATE` makes the
  check and the write one act (slice record §7, G1/G3). The
  application holds no item state (G2) and reads no clock (G5) —
  enforced by ArchUnit rules on the compiled classes
  (`NoInstanceStateOrClockTest`). Nonsense never reaches the
  decision (G6) — enforced by the value types at the door, the
  store's constraints behind them.
- **SL-2, closed:** no admitted change to the on-hand-count leaves
  it under the sum of active reservations — enforced by the same
  two owners SL-1 built, justified here against a different
  adversity: an honest request, no race. The conditional statement
  assigns the asserted value or writes nothing, so a correction
  that does not fit is refused and nothing is clamped; the door's
  value shape (ADR-0011) makes a resend assert the same state
  rather than move the count again. Two guarantees are held by an
  absence — no ordering state at the door, one writing path for the
  count — and enforced by rules on the compiled code
  (`NoOrderingStateOrSecondWriterTest`). No structure was added:
  the slice's record §8 says why.
- The promise's other two invariants are the registry's, not yet
  enforced: SL-3 and SL-4 not at all — a reservation has no exit
  yet.
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
| `infrastructure/flyway/` | the only DDL path: config and migrations — V1, `item` and `reservation` with the wall |
| `docs/infrastructure/` | the operator manual and the infrastructure contract |
| `docs/system/` | the truth set: intent, definition, registry |
| `docs/construction/` | the bootstrap requirements, and one record per slice: specification, plan, evidence |
| `pom.xml`, `mvnw` | the build: every dependency with its earning reason; the wrapper |
| `src/main/java/…/neveroversold/` | the entry point; `reservation/` is the ledger — its `package-info` is the map |
| `src/main/resources/application.yaml` | the one identity, the password from the environment, the absences commented |
| `src/test/java/…/neveroversold/` | the evidence: `testsupport/` is the harness (the throwaway store, the test bases, the forked instance, the witness, the body reader); `*IT` are the integration tests, `*StormIT`/`*RaceIT` the adversity-creating ones; `NoInstanceStateOrClockTest` the structural rules |
