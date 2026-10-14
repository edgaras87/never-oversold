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
is derived from it. Nothing is derived from a feature list.

### What the words commit to

- *for any item* — the claim holds per item, never across items. An
  item is whatever the ledger counts stock of.
- *the reserved quantity* — the sum of active reservations: those
  not yet released, expired, or consumed.
- *the on-hand-count* — always this one term. It is the ledger's
  own recorded count of an item, not what a warehouse holds. Every
  change to it arrives as a request to the ledger.
- *race for the same units* — concurrent requests overlapping in
  time against one item. The adversity is per item.
- *never exceeds* — true in every state readable from outside, not
  eventually.

## Who this is for

Written for a reader judging whether the author can build a backend
correctness-first. That means one claim, stated before any code. The
system is derived from it, and the evidence creates the adversity
for real. The claim is sold to you.

The system's callers and operators are not sold anything. Callers
reserve, consume and release. Operators restock, correct and
recount. They are part of the world the claim must hold in, so the
system definition lists them as its environment, never here as the
audience.

**Who the promise protects,** without being its audience: a seller
running such a ledger, to whom an oversell costs money and trust.
The seller is real, appears only through the callers, and is
the reason the claim is worth anything.

## Why it is worth proving

**It can be false.** The negation is one event, an oversell: a
persisted state in which an item's reserved quantity exceeds its
on-hand-count. Naive code produces it. It reads the count, checks,
then writes. Two racing requests both pass the check on the last
unit.

**Proving it matters.** Oversell under contention is the classic
correctness adversity. Contention is what this demonstration is judged
on. And the event the claim rules out is the one that costs the
seller.

## What done demonstrably means

- The adversity genuinely created: many concurrent reservations
  against one item holding fewer units than they ask for, from more
  than one caller and more than one instance of the ledger. Never
  requests replayed one after another and called a race.
- The witness never firing: reserved greater than on-hand-count,
  read from persisted state, checkable from outside the system.
- The path from claim to proof followable by the reader: this
  intent, the system definition, the slice registry, the evidence.

**What done excludes,** decided here so the release can check it:
- Monitoring and alerts: excluded. Nobody operates this system for
  real. Its audience reads evidence, not dashboards.
- Deploy: in scope in one form only. The system and its evidence
  run on a clean machine by the README's commands. No hosted
  deployment.
- Rollback: excluded, since there is no hosted deployment to roll
  back from.

## The tests the claim was held to

Six, set before the sentence was chosen. Four are settled by the
sentence itself. Two were argued here, and later proven.

1. One claim, one sentence, a timeless state property. Settled.
2. The adversity class is in the sentence: "race for the same
   units" is contention. Settled.
3. Trivially false: a read, a check and a write that another
   request can slip between break it. The negation is one event,
   visible from outside the system. Settled.
4. Stageable: send N concurrent reservations to an item holding
   fewer than N units, then read the witness from state. Argued
   here, and proven by SL-1's evidence.
5. Matters to the audience. Settled.
6. Narrow: one owner, and a full lifecycle (reserve, release,
   expire, consume) inside one system. No orders, no payments.
   Argued here; confirmed by the system definition's ownership
   layer, which found one area.

## Rejected candidates, kept

Each considered, each refused for a written reason. None is lost.

- *"No unit of stock is ever held by two reservations at once."*
  The same truth, but it presumes each unit of stock has its own
  identity, a model choice the problem does not demand. It names
  structure, not state.
- *"A confirmed reservation is never lost."* Real, but its negation
  is a reservation that disappears, not a collision: the wrong
  adversity class. It appears instead in the system definition's
  environment, as a trust assumption (T1), its loss fenced (W4).
- *"A retried reservation request creates at most one
  reservation."* Idempotency, whose adversity class is duplication.
  Under the chosen promise a duplicate reservation over-holds but
  cannot oversell. So it is a second claim, and is refused. Its cost
  is accepted and written in the system definition's fences (W2).
- *"Released stock returns exactly once."* A second purpose, at the
  lifecycle's far end. An intent carrying it too is overloaded. Its
  substance lives on as a slice under the chosen promise, not as a
  claim of its own.
- *"Available stock reported to callers is always current."* The
  negation is a stale number, whose adversity class is staleness. And
  the claim reads as a feature wish.

**Widening the promise,** by adding a second claim such as request
identity, is not a small check. It means deriving the definition
and the registry again. A second claim goes to the next project, or
into a dated revision here after release, never added silently.

## Revision log

<!-- Dated entries only: what changed, why, what triggered it. -->

- 2026-09-10 — heading: the working name safe-reservations
  replaced by the project's name, never-oversold. Why: the naming
  step decided it (ADR-0003) — the name is the promise's negation,
  ruled out. Triggered by PLAN Step 2. No claim changed.
- 2026-10-14 — written plain, meaning unchanged: one idea per
  sentence, dash and semicolon chains split, metaphors out ("the
  weather the claim stands in", "a sequential replay pretending").
  Why: the reader the intent is for had to untangle it. Triggered by
  Step 10 (the retrospective's D2). No claim, audience, test or
  exclusion changed.
- 2026-10-14 — read against the definition and the slice records.
  Test 4 says it was proven by SL-1's evidence, where it said
  "proven when the evidence harness creates it". The rejected claim
  "A confirmed reservation is never lost" is placed where the
  definition has it, a trust assumption (T1) with its loss fenced
  (W4), not among the facts that can hurt. Why: both lines had gone
  out of step with the records they name. Triggered by Step 10's
  second reading of the records. No claim, audience, test or
  exclusion changed.
