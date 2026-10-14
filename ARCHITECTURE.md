# Architecture

Words and tags used here: [glossary](docs/glossary.md).

<!-- Describes the system AS IT IS NOW — not the aspiration. 1–2 pages max.
     Update trigger: a plan step's gate closes and this no longer
     matches reality. For the WHY behind any choice, link the ADR. By
     rule, not by slice: a slice's close adds or changes rows in
     "Invariants"; how the slice got there is its record's. -->

## Overview

The system as it stands, and what a slice may rely on. The bootstrap
requirements are history: they say what was certified at the
bootstrap's close, not what holds now.

The ledger is one build, run as any number of instances. Each
connects to the store as `runtime` and nothing else, keeps no item
state, reads no clock, and answers HTTP: reserve, adjust, consume,
release, and a health check that names the store.

The store holds the two numbers the promise is about, per item: the
on-hand-count and the units held. It holds each reservation and, once
it has ended, its one receipt. It refuses a wrong result from any
writer: a row with more held than on hand, a second receipt, a
receipt changed or mistimed, units held that disagree with the open
reservations. The ledger computes. The store refuses, and never
computes.

At test scope, the evidence harness runs the ledger as it ships:
plain processes, against a throwaway store. It reads the witness
from the store, outside every instance.

```
┌───────────────────────────┐        ┌──────────────────────────────┐
│  never-oversold-postgres  │        │  the ledger                  │
│  PostgreSQL 17            │ ◀───── │  N instances, one build      │
│  db never_oversold        │  5432  │  HTTP door: reserve, adjust, │
│  item ── the wall ──┐     │        │  consume, release; health    │
│  reservation        │     │        │  connects as `runtime` only  │
│  reservation_exit ┐ │     │        └──────────────────────────────┘
└───────────────────┼─┼─────┘
        ▲           │ └ CHECK reserved <= on_hand_count
        │           └ one receipt per reservation (key); its guard;
        │             the units held checked at each commit touching it
        │  published ${POSTGRES_PORT}: the ledger and inspection, as `runtime`

  test scope ─ the harness: a throwaway postgres:17 (Testcontainers),
  migrated from the one home; the ledger forked as plain processes and
  raced through their doors; the witness read by plain JDBC; a consume
  held mid-work by a row lock, its instance killed, the store frozen;
  rules on the compiled classes and a SQL parser for what the ledger
  must not contain
```

## Components

### PostgreSQL — the store

Holds each item's on-hand-count and units held, the reservations, and
their receipts. Nothing else holds them. A slice may rely on:
- **Migration V1:** `item` and `reservation`, with
  `item_never_oversold` (`reserved <= on_hand_count`), counts that are
  not negative, a positive quantity, and an expiry after creation.
- **Migration V2:** `reservation_exit`, one receipt per reservation by
  its key. Its foreign key: a receipt names a reservation that exists,
  and a reservation with a receipt cannot be deleted. Its guard,
  `reservation_exit_guard`, refuses a changed or deleted receipt, an
  expiry receipt before the hold runs out, and a consume or release
  receipt at or after it. The guard sets the ending instant itself:
  the hold's expiry instant for an expiry, the store's `now()` for the
  others.
- **Migration V3:** `units_held_agree`, checked at the commit of any
  transaction that touches an item: the item's `reserved` equals the
  units of its reservations with no receipt.
- **Migration V4:** `reservation_by_item`, an index. It refuses
  nothing (ADR-0015).

Migrations are never edited once committed. A change is a new
migration with the next number. Why it is built this way: ADR-0005
(this service and no other), ADR-0006 (the authority split, every
constraint's enforcement), ADR-0004 (the environment), ADR-0015 (the
index).

### The ledger — the application

One feature package, `reservation`. Its `package-info` is the map.
A slice may rely on:
- **One entry path:** `Ledger` alone writes the store. Each operation
  is one transaction, and nothing is answered before it commits.
- **Reserve and adjust tidy first:** each writes the expiry receipts
  of the item's run-out holds and frees their units. A consume or
  release on a run-out hold writes that hold's expiry receipt instead,
  and the consume or release is refused.
- **One statement per decision:** reserve and adjust are each one
  conditional statement on the item's row. Consume and release each
  write their receipt and move the numbers in one statement.
- **A lost store** is answered `503`, "outcome unknown", told apart
  from a refusal by the store's own error class.
- **No service layer, repository, ORM, clock, or in-memory state.**

Why it is built this way: ADR-0007 (the stack, the migration tool
outside the app, one identity), ADR-0008 (package by feature, a layer
added inside a feature only when that feature needs it),
ADR-0010 (the door), ADR-0011 (an item known by its first
adjustment), ADR-0012 (an adjustment answers 200), ADR-0013 (a
repeated exit answers as the first), ADR-0016 (an unknown outcome
answers 503).

### The evidence harness — test scope

Creates each adversity for real and reads the witness from the
store. It starts the ledger in the form it ships: the build's
classes, forked as plain operating-system processes, with the
environment a real instance gets and no test code inside. What each
adversity is in the world, and in the evidence:

| Adversity | In the world | In the evidence |
|---|---|---|
| Instances with no shared memory | separate JVMs | forked JVMs |
| Death mid-work | the process killed | `SIGKILL` (`ForkedLedger.kill`) |
| The store hangs | no answer | the store's container paused |
| The connection drops | an error at once | the session ended at the store |
| A network cut, the instance alive | the store waits to notice | not staged; a known issue in `TODO.md` |

The suite is self-contained: one command, and the ground need not be
up. Why it is built this way: ADR-0009 (instances as processes, the
suite self-contained).

## Invariants

<!-- One row per rule the system keeps: where the arithmetic or the
     decision is made, what refuses a wrong result, the test that goes
     red if either went, and the slice that proved it. -->

| Rule | Computed by | Refused by | Guarded by | Proved by |
|---|---|---|---|---|
| never more held than on hand | reserve's conditional update | `item_never_oversold` | `ReserveStormIT`, `InstancesStormIT` | [SL-1](docs/construction/sl-1-no-over-admission.md) G1, G2 |
| an admit and a correction on one item do not interleave | the item's row | `item_never_oversold` | `AdjustmentRaceIT` | [SL-1](docs/construction/sl-1-no-over-admission.md) G3 |
| an invalid request moves nothing | the door's value types | the store's constraints | `ReservationDoorIT` | [SL-1](docs/construction/sl-1-no-over-admission.md) G6 |
| activeness judged by one clock | the store's `now()` | no process clock in the application | `OneClockIT`, `NoInstanceStateOrClockTest` | [SL-1](docs/construction/sl-1-no-over-admission.md) G5 |
| no item state outside the store | — | the application's structure | `NoInstanceStateOrClockTest` | [SL-1](docs/construction/sl-1-no-over-admission.md) G2 |
| a correction never under the units held, taken whole | adjust's conditional upsert | `item_never_oversold` | `CorrectionIT` | [SL-2](docs/construction/sl-2-correction-never-undercuts.md) G1–G3 |
| a correction sent twice, or out of order, does no harm | the asserted value; no order kept | — | `CorrectionIT`, `NoOrderingStateOrSecondWriterTest` | [SL-2](docs/construction/sl-2-correction-never-undercuts.md) G4, G5 |
| one ending per reservation | — | `reservation_exit_pk` | `ExitStormIT`, `ExitDoorIT` | [SL-3](docs/construction/sl-3-a-reservation-exits-once.md) G1, G2 |
| an ending on time, never changed or deleted | — | `reservation_exit_guard`, by its own clock | `ExitDoorIT`, `ReceiptGuardIT` | [SL-3](docs/construction/sl-3-a-reservation-exits-once.md) G3, G6 |
| an exit moves exactly its reservation's units | the exit's statement, from the reservation's row | `units_held_agree`, for the units held; nothing, for the count | `ExitDoorIT` | [SL-3](docs/construction/sl-3-a-reservation-exits-once.md) G4 |
| expired units freed once, from the instant | tidy, by the receipts it wrote | the key, and `units_held_agree` | `ExitStormIT`; the tripwires on the instant | [SL-3](docs/construction/sl-3-a-reservation-exits-once.md) G5 |
| units held equal the open reservations' | each statement | `units_held_agree` | `UnitsHeldCheckIT` | [SL-3](docs/construction/sl-3-a-reservation-exits-once.md) G6 |
| consume's two moves together, or neither | one statement, one transaction | `units_held_agree`, for the units held | `InterruptedConsumeIT`, `StoreOutOfReachIT`, `NoSecondWayOutTest` | [SL-4](docs/construction/sl-4-consumes-two-moves-hold-together.md) G1–G4 |
| the ledger the one writer; no reservation rewritten | — | the application's structure; T4 beyond it | `NoOrderingStateOrSecondWriterTest`, `NoSecondWayOutTest` | [SL-2](docs/construction/sl-2-correction-never-undercuts.md) G6, [SL-3](docs/construction/sl-3-a-reservation-exits-once.md) G6 |
| the application knows one identity, `runtime` | the configuration | no migration or mapping library at runtime | — | ADR-0007 |
| the application cannot change structure | — | the grants: `runtime` has no CREATE, ALTER, DROP | the ground's verify suite | ADR-0006 C1 |
| only `migrator`, `runtime` and the superuser connect | — | CONNECT revoked from PUBLIC | the ground's verify suite | ADR-0006 C3 |

Writers outside the application are fenced: a console, a script,
another service, the superuser (W7, ADR-0014). No wall is owed against
them. The store's walls still refuse a wrong result from them.

## Codemap

<!-- Where to find things. Directory → what lives there. -->
| Path | What lives there |
|---|---|
| `compose.yaml`, `.env.example` | the ground's declaration, and the template for `.env`: passwords and the published port |
| `infrastructure/postgres/` | the bootstrap SQL (runs once) and the verify suite (on demand) |
| `infrastructure/flyway/` | the only DDL path: config and migrations V1 to V4 |
| `docs/infrastructure/` | the operator manual and the infrastructure contract |
| `docs/system/` | the truth set: intent, definition, registry |
| `docs/construction/` | one record per slice; the bootstrap requirements, as history |
| `docs/adr/` | the decisions, one per file |
| `pom.xml`, `mvnw` | the build: every dependency with the reason it was added; the wrapper |
| `src/main/java/…/neveroversold/` | the entry point; `reservation/` is the ledger, and its `package-info` is the map |
| `src/main/resources/application.yaml` | the one identity, the password from the environment, the absences commented |
| `src/test/java/…/neveroversold/adversity/` | tests that create the danger for real: bursts of requests, races, kills, a frozen store |
| `…/folds/` | a fold's fact shown by running the system, with no attack: one clock |
| `…/structure/` | tests that read the code: what the ledger must not contain |
| `…/store/` | the store's own refusals, and the migration path |
| `…/door/` | the HTTP contract: each answer and its status; health |
| `…/testsupport/` | the harness: the throwaway store, the forked instance, the witness, the hold, the SQL reader, and the harness's own test |
| `…/reservation/` | a unit test that needs the ledger's package access |
