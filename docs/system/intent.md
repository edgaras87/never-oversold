# Intent — never-oversold

<!-- The promise this system is built to keep, and what it refuses
     to claim. Composed at framing close from the derivation record
     beside it (framing/derivation.md); stands alone. A living
     record: after close it changes only through a dated entry in
     the revision log at the end — never a silent edit. -->

## The promise

**For any item, the reserved quantity never exceeds the
on-hand-count, however many reservations race for the same units.**

One claim, a timeless property of state. Every structure below it
is derived from it; nothing is derived from a feature list.

### What the words commit to

- *for any item* — the claim holds per item, never across items; an
  item is whatever the ledger counts stock of.
- *the reserved quantity* — the sum of active reservations: those
  not yet released, expired, or consumed.
- *the on-hand-count* — one term, one token: the ledger's own
  recorded count of an item, not a warehouse's truth. Every change
  to it enters through the ledger's door.
- *race for the same units* — concurrent requests overlapping in
  time against one item. The adversity is per item.
- *never exceeds* — true in every state readable from outside, not
  eventually.

## Who this is for

Written for a reader judging whether the author can build a backend
correctness-first: one claim, stated before any code, the system
derived from it, the evidence staging the adversity for real. The
claim is sold to you.

The system's callers and operators are not sold anything. Callers
reserve, consume and release; operators restock, correct, recount.
They are the weather the claim stands in, and they appear in the
system definition as the environment, never here as the audience.

**Who the promise protects,** without being its audience: a seller
running such a ledger, to whom an oversell costs money and trust.
Real, present through the callers, and the reason the claim is
worth anything.

## Why it is worth proving

**It can be false.** The negation is one event, an oversell: a
persisted state in which an item's reserved quantity exceeds its
on-hand-count. Naive code produces it — read the count, check,
write — and two racers both pass the check on the last unit.

**Proving it matters.** Oversell under contention is the canonical
correctness adversity; contention is what this demonstration is
judged on; and the event it rules out is the one that costs the
seller.

## What done demonstrably means

- The adversity genuinely created: many concurrent reservations
  against one item holding fewer units than they ask for, from more
  than one caller and more than one instance of the ledger — never
  a sequential replay pretending.
- The witness never firing: reserved greater than on-hand-count,
  read from persisted state, checkable from outside the system.
- The path from claim to proof followable by the reader: this
  intent, the system definition, the slice registry, the evidence.

**What done excludes,** decided here so the release can check it:
- Monitoring and alerts: excluded. Nobody operates this system for
  real; its audience reads evidence, not dashboards.
- Deploy: in scope in one shape only — the system and its evidence
  run from a clean machine by the README's commands. No hosted
  deployment.
- Rollback: excluded with deploy's hosted shape; nothing to roll
  back from.

## The tests the claim was held to

Six, set before the sentence was chosen. Four settle on the
sentence itself; two are argued here and proven downstream.

1. One claim, one sentence, a timeless state property. Settled.
2. The adversity class in the sentence — "race for the same units"
   is contention. Settled.
3. Trivially false: unserialized read-check-write breaks it; the
   negation is one observable event. Settled.
4. Stageable: fire N concurrent reservations at an item holding
   fewer than N units; read the witness from state afterwards.
   Argued here; proven when the evidence harness creates it.
5. Matters to the audience. Settled.
6. Narrow: one owner, a full lifecycle — reserve, release, expire,
   consume — inside one system's walls; no orders, no payments.
   Argued here; confirmed by the system definition's ownership
   layer, which found one area.

## Rejected candidates, banked

Each considered, each refused for a written reason; none is lost.

- *"No unit of stock is ever held by two reservations at once."*
  The same truth, but it presumes stock is serialized units — a
  model choice the territory does not demand. Names structure, not
  state.
- *"A confirmed reservation is never lost."* Real, but its negation
  is a vanishing, not a collision: the wrong adversity class. It
  appears in the environment census as an enemy instead.
- *"A retried reservation request creates at most one
  reservation."* Idempotency; adversity class duplication. Under
  the chosen promise a duplicate reservation over-holds but cannot
  oversell, so this is a second claim, refused — its cost is
  accepted and written in the system definition's fences.
- *"Released stock returns exactly once."* A second purpose at the
  lifecycle's far end; an intent carrying it too is overloaded.
  Its substance lives on as a slice under the chosen promise, not
  as a claim of its own.
- *"Available stock reported to callers is always current."* The
  negation is a stale number — class staleness — and the claim
  reads as a feature wish.

**Widening the promise** — adding a second claim such as request
identity — is not a small check. It re-runs the derivation below
it. The route for a second claim is the next project, or a dated
revision here after release, never a silent extension.

## Revision log

<!-- Dated entries only: what changed, why, what triggered it. -->

- 2026-09-10 — heading: the working name safe-reservations
  replaced by the project's name, never-oversold. Why: the naming
  step decided it (ADR-0003) — the name is the promise's negation,
  ruled out. Triggered by PLAN Step 2. No claim changed.
