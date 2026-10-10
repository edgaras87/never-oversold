# Architecture

<!-- Describes the system AS IT IS NOW — not the aspiration. 1–2 pages max.
     Update trigger: a plan step's gate closes and this no longer matches
     reality. For the WHY behind any shape, link the ADR. -->

## Overview

The ledger runs on the ground and holds three of its four
invariants: instances of one build output, each connecting to the
store as `runtime` and nothing else, each answering HTTP — reserve,
adjust, consume, release, and a health check that names the store.
The store holds the two numbers the promise is about, per item, and
the wall between them: a check constraint that no writer can pass,
and an admit that is one conditional statement against the row. It
holds each ended reservation's receipt, one at most, guarded so that
no writer changes, deletes or mistimes one, and it checks at every
commit that an item's held units equal what its unreceipted
reservations hold. The application keeps no item state and reads no
clock; expiry is the store's clock, and every decision first ends
the holds that clock has run out.
Beside it, at test scope only, the evidence harness: its own
throwaway store of the ground's major version, migrated from the
one home, the machinery to race real instances, and the witness
that reads the invariant from the store from outside them all.

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
        │             the units held checked at every commit
        │  published ${POSTGRES_PORT}: the witness read, Flyway as `migrator`

  test scope ─ the harness: a throwaway postgres:17 (Testcontainers),
  migrated harness-side; forked instances raced through their doors;
  the witness read from the store by plain JDBC; a consume held
  mid-work by a row lock, its instance killed, the store frozen;
  ArchUnit rules on
  the compiled classes for what the ledger must not contain, and a
  SQL parser (JSqlParser) reading the ledger's statements by their
  parts
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
`values` (what a request may say) and `problems` (the answers
besides success). Reserve is one conditional statement whose row
count is the decision; adjust is one insert-or-update that creates
an unknown item and refuses a count under the held units. Each
first runs tidy, one statement writing an `expired` receipt for
every hold of the item that has run out and freeing exactly the
units of the receipts it wrote. Consume and release are one
statement each: the receipt and the item's numbers move together,
and only for a receipt that statement wrote. Every decision's
transaction goes through one method, which turns a store lost
mid-request into `StoreOutOfReach` — read from the store's own error
class — and the door answers it `503`, "outcome unknown". No service
layer, no repository, no ORM, no clock, no in-memory state.
Why shaped this way: ADR-0007 (the stack; the migration tool
outside the app; one identity), ADR-0008 (package by feature,
package-private, depth earned per feature; its note on
vocabulary sub-packages), ADR-0010 (the door's conventions),
ADR-0011 (an item becomes known by its first adjustment),
ADR-0012 (an adjustment answers 200, creating or not), ADR-0013
(a repeated exit answers as the first), ADR-0016 (an outcome the
ledger cannot know answers 503); the walls' owners per
guarantee in the slice records (`docs/construction/sl-1-…`, §7;
`sl-3-…`, §8).

### The evidence harness — test scope

Responsibility: create adversity for real and read the witness
from persisted state. Proven at bootstrap on contention: requests
held at a barrier and released at one instant, across three
instances of the ledger running as separate processes against one
store, every response asserted, the store read from outside. Since
SL-4 it also interrupts on purpose: a row held at the store as its
superuser, so a consume stops mid-work where the test chose; an
instance killed outright; the store's container frozen and thawed;
one session ended.
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
- **SL-3, closed:** a reservation moves its item's numbers at most
  once on exit, and never after it has ended — enforced by the
  store: the primary key on `reservation_exit.reservation_id` (one
  receipt per reservation, so of racing or repeated exits one
  writes and the rest move nothing); the guard trigger
  `reservation_exit_guard` (no receipt changed or deleted, no
  `expired` one before its instant, no `consumed` or `released` one
  at or after it, the ending instant the store's own); and the check
  on the units held, `units_held_agree` (V3: a constraint trigger on
  `item`, `reservation` and `reservation_exit`, deferred to commit,
  refusing any transaction that leaves `reserved` unequal to the
  units of the item's unreceipted reservations, whoever writes). The
  arithmetic stays in the ledger's statements; the store refuses,
  it never computes. One way out is guarded at build time
  (`NoSecondWayOutTest`): writers outside the ledger, SQL the parser
  cannot see, an exit not shaped as one.
- **SL-4, closed:** no readable state holds one of consume's two
  moves without the other — enforced by SL-3's structure, now
  proven: one statement in one transaction, so a death before the
  commit leaves neither move and the store undoes it alone; the
  item's row, so a decision on the item waits for a consume under
  way and then reads it whole; and V3 behind it, refusing any split
  of the ending from the units held. The one split V3 cannot see —
  the count apart from the rest — is guarded at build time
  (`NoSecondWayOutTest`, the fifth rule). No structure was added.
- **Who writes the store:** the ledger alone (the definition's T4,
  ADR-0014), trusted rather than enforced by grants. Inside the
  application it is enforced at build time: only the `Ledger` class
  reaches the store, by any database API, and none of its statements
  updates or deletes a reservation (`NoOrderingStateOrSecondWriterTest`,
  `NoSecondWayOutTest`). Outside it — a console, a script, the
  superuser — is fenced (W7); the store's walls still refuse a wrong
  result from them.
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

## Responsibilities — who computes, who refuses

<!-- One row per rule the system keeps: where the arithmetic or the
     decision is made, what refuses a wrong result, and what test
     would go red if either went. The pattern, deliberately: the
     application computes, the store refuses and never computes. -->

| Rule | Computed by | Refused by | Guarded by |
|---|---|---|---|
| never more held than on hand | the ledger: reserve's conditional update | the store: `item_never_oversold` | the reserve storms, across instances |
| a correction never under the held units | the ledger: adjust's conditional upsert | the store: `item_never_oversold` | `CorrectionIT`, `AdjustmentRaceIT` |
| one ending per reservation | — | the store: `reservation_exit_pk` | `ExitStormIT`, `ExitDoorIT` |
| an ending on time, never changed or deleted | — | the store: `reservation_exit_guard`, by its own clock | `ExitDoorIT`, `ReceiptGuardIT` |
| an exit moves exactly its reservation's units | the ledger: the exit's statement, from the reservation's row | the store, for the units held: `units_held_agree`; nothing, for the count | `ExitDoorIT` |
| expired units freed once, from the instant | the ledger: tidy, by the receipts it wrote | the store: the key, and `units_held_agree` | `ExitStormIT`; the tripwires on the instant |
| units held equal the unended reservations' | the ledger, in each statement | the store: `units_held_agree` (V3) | `UnitsHeldCheckIT` |
| activeness judged by one clock | the store's `now()` | the application: no process clock | `OneClockIT`, `NoInstanceStateOrClockTest` |
| no item state outside the store | — | the application's structure | `NoInstanceStateOrClockTest` |
| the ledger the one writer; no reservation rewritten | — | the application's structure; T4 beyond it | `NoOrderingStateOrSecondWriterTest`, `NoSecondWayOutTest` |
| nonsense never reaches a decision | the door's value types | the store's constraints | the door tests |

## Codemap

<!-- Where to find things. Directory → what lives there. -->
| Path | What lives there |
|---|---|
| `compose.yaml`, `.env.example` | the ground's declaration and its secrets' shape |
| `infrastructure/postgres/` | the bootstrap SQL (runs once) and the verify suite (on demand) |
| `infrastructure/flyway/` | the only DDL path: config and migrations — V1, `item` and `reservation` with the wall; V2, the receipts and their guard; V3, the check on the units held; V4, reservations indexed by item |
| `docs/infrastructure/` | the operator manual and the infrastructure contract |
| `docs/system/` | the truth set: intent, definition, registry |
| `docs/construction/` | the bootstrap requirements, and one record per slice: specification, plan, evidence |
| `pom.xml`, `mvnw` | the build: every dependency with its earning reason; the wrapper |
| `src/main/java/…/neveroversold/` | the entry point; `reservation/` is the ledger — its `package-info` is the map |
| `src/main/resources/application.yaml` | the one identity, the password from the environment, the absences commented |
| `src/test/java/…/neveroversold/` | the evidence: `testsupport/` is the harness (the throwaway store, the test bases, the forked instance, the witness, the body reader, and for SL-4 the hold and the interrupted consume's scene); `*IT` are the integration tests — `*StormIT`/`*RaceIT` create SL-1's contention, `CorrectionIT` SL-2's honest corrections, `ExitDoorIT` and `ExitStormIT` SL-3's exits, `InterruptedConsumeIT` and `StoreOutOfReachIT` SL-4's interruptions, `InterruptionHarnessIT` checks the harness that makes them; `ReceiptGuardIT` and `UnitsHeldCheckIT` show the store's refusals directly; `NoInstanceStateOrClockTest`, `NoOrderingStateOrSecondWriterTest` and `NoSecondWayOutTest` the structural rules, the last reading SQL through `testsupport/LedgerSql` |
