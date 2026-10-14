# SL-2 — the correction never undercuts the holds

Words and tags used here: [glossary](../glossary.md).

<!-- The slice's record, grown in three parts. First the correctness
     specification: what must hold, against what, and what proof
     looks like, with no mechanism. Then the plan: one structural
     owner per guarantee, and why it beats the named adversity. Then
     the evidence as delivered: which test creates which adversity,
     and what it read from the store. Invariant and adversity are the
     registry's and the definition's, as written. Guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each part.

     "The correction's shape — decided" is this slice's own section.
     SL-1 had no such choice to make. This slice exists because no
     structure makes that choice by itself. -->

*Regrouped by guarantee on 2026-10-13: each guarantee's claim,
attack, evidence criterion, owner, options and tests in one block.
No words changed beyond headings and references. A section number
cited before that date refers to the text as signed, at 23573c2.*

## Invariant

No admitted change to the on-hand-count leaves it under the sum of
active reservations. (`docs/system/registry.md`, SL-2; also SL-2 in
the definition's L4.)

*Active* is FC3's, as SL-1 uses it: a reservation not ended by
an exit and not past its expiry, as judged by one clock, not one
per instance. *Readable state* is the intent's: true at every read
from outside, not eventually. *A change to the on-hand-count* is
whatever an adjustment asserts at the door: a value, never a
difference (ADR-0011).

## Adversity

An honest request the ledger cannot honour as asked. There is no
race to create. Requests one at a time are enough, and that is what
makes this a slice of its own: hammering never sends an honest
correction. Taken from the definition's L1 as written:

- an operator lowers the count under the reserved sum, after a
  loss, breakage or a recount: the promise's negation, arriving as
  a legitimate fact from the world (F9, kill 6);
- that downward correction is resent, the count asked to fall twice
  for one loss (F11, kill 7);
- two adjustments to one item arrive in the other order than they
  were made, so which value is current is not known (F13, kill 8).

Kills covered: 6, 7, 8. What this slice does not face:
- any race (an adjustment against concurrent admits is kill 5,
  closed in SL-1);
- exits and their races (SL-3, SL-4);
- a retried reserve (W2, fenced).

### How the evidence creates it, for every guarantee

Each guarantee's evidence *creates* its adversity through the real
door. It reads the witness from the store, from outside the
process. The witness is the invariant's: for the item, the
on-hand-count and the sum of active reservations, read from
persisted state. It is the same witness SL-1 read, with a different
adversity creating it.

- **The harness can fail (R5).** Each of E1–E4 is run with the wall
  absent and seen red, recorded from actual output. The wall is
  removed on the working tree and on the throwaway store, and that
  state never lands in history. Then each is seen green, unchanged,
  with the wall standing. A green suite that was never red is not
  known to be watching.

All of it runs under the one standard test command, with nothing
exported, and the ground need not be up.

## The correction's shape — decided

Framing parked two options and chose neither (registry, SL-2's
judgment logged; ADR-0011's third bullet; L4's SL-2). The
decision is this specification's, and it is taken here.

**Chosen: refuse.** A change to the on-hand-count that would leave
it under the sum of active reservations is not admitted. The
reservations stand. The ledger's count keeps its old value. The
operator is answered with a refusal, which is not an invalid
request (ADR-0010).

**Rejected: let the correction end reservations.** That is, admit
the operator's number and end enough active reservations to fit
under it. It was not rejected for breaking the invariant: under
either choice, the count is never under the sum. It was rejected
for three reasons, in the truth set's own words:

1. It needs a policy no possession grants. L2 makes the transitions
   out of active ours, through P2's records, so the ledger *may* end a
   hold. But *which* hold ends is a choice, and the framing refuses
   that kind of choice by name: expiry duration is "the caller's or
   the seller's policy". Ending holds at random, by age or by size is
   a policy this system was never given.
2. It would have enlarged SL-3, not yet worked then. A correction that
   ends reservations is a new kind of exit. SL-3's invariant is that a
   reservation moves its item's numbers at most once on exit, and
   never after it has ended. The new exit would have to be built into
   SL-3 before it was understood here.
3. What it would fix was already accepted as a cost, in writing. Its
   gain is a count that agrees with the shelf. But the intent defines
   the on-hand-count as "the ledger's own recorded count of an item,
   not what a warehouse holds". L2 refuses physical stock truth with
   the words "the count can be wrong about the world and the promise
   still holds". And the registry's fold-reconciliation line sends the
   rest of kills 7 and 8 to the W1 fence. Shrinking W1 takes a dated
   revision of the definition. A slice does not take it in passing.

**What the choice costs, said plainly.** Say an operator counted the
shelf and found fewer units than are held. They have no practical
recourse in this system. The surface has no operator-side exit, so
they wait for expiry or for the callers to release. Meanwhile, the
ledger goes on admitting reservations against units that are not
there. The promise holds, but the seller can still lose. This is what
W1's remainder looks like in practice. Whether an operator should have
an exit is a question about exits, so it belongs to SL-3, not to this
slice. It is recorded under what this slice does not claim.

**What the choice confirms.** SL-1's wall already refuses such a
change, provisionally, deciding nothing in advance (SL-1 G3;
ADR-0011). This section removes the word *provisional*: the
refusal is now decided. This slice owes the evidence that it
survives the resent and the reordered correction, which nothing has
yet created.

## Guarantees

Each is one answer to: *what would let the invariant hold on paper
yet break in fact?* The attack runs until no new answer comes.
Every guarantee is a property. None names how it is held.

### SL-2 G1. A correction is admitted only if the count it sets is not under the units held at the moment it is written.

*What could go wrong (the attack):* the count is set to a number
that was above the sum when the request was read, and below it when
the write lands.
*Say:* 10 on hand and 6 held when the operator's 7 is read, so it
fits. Two reservations land. Now 8 are held, and the 7 is written
anyway.

*What we guarantee:* the comparison and the change are one act
against the store's state as it is then, never two acts against a
copy.
*Say:* the 7 is compared against the 8 that stands when it is
written, not the 6 that stood when it was read.

*Kills:* 6.

- **E1 (G1, G2).** An item has active reservations holding some of
  its units. An honest downward correction is sent through the real
  door, asserting a count below that sum. Witness after: the count
  is the old value, and the sum is unchanged. The answer is a
  refusal, not an invalid request (ADR-0010's statuses tell them
  apart). And its mirror: a correction that fits, below the old
  count but not below the sum, is admitted. The count after is
  exactly the number asserted, with no clamping.

*The wall:* the store's check constraint `reserved <=
on_hand_count`, with the adjustment as one conditional statement:
`INSERT … ON CONFLICT (id) DO UPDATE SET on_hand_count = :count
WHERE item.reserved <= :count`. SL-1 built it as its G3. It is
adopted here, not rebuilt.

*Why it beats this attack:* SL-1 justified it against a race. F9
needs no race, and the same statement answers it for a different
reason. The `WHERE` is checked against the row's committed state at
the instant of writing. So the door's answer is the store's answer,
and no code of ours decides it.
*Say:* 8 held, the operator asserts 7. No row changes, and the door
turns "no row" into a refusal.

*If the wall were ever wrong:* the constraint refuses the row at
write time. So a count under the held units cannot be written by
any path.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E1 · G1 | `CorrectionIT.anHonestCorrectionUnderTheHoldsIsRefused` | 10 on hand, 8 held, the operator asserts 7 (F9, kill 6) |
| E1 · G1 | `CorrectionIT.aRefusalIsNotAnInvalidRequest` | the same, beside a nonsense count, to keep `409` and `400` apart (ADR-0010) |

---

### SL-2 G2. After a correction, the count is exactly the number the operator sent, or exactly what it was before.

*What could go wrong (the attack):* the ledger meets a number it
cannot honour and takes part of it, so that something is written.
*Say:* 8 held, and the operator asserts 5. The ledger writes 8
instead: a count that fits, that nobody sent, and that no one
counted on any shelf.

*What we guarantee:* the count after an adjustment is either
exactly the number the operator asserted, or exactly what it was
before.

*Kills:* 6's other half.

*Evidence:* E1, in SL-2 G1's block.

*The wall:* the store's own assignment, in the same statement:
`SET on_hand_count = :count`, the operator's asserted value as
sent. No arithmetic, no `LEAST`, no clamp.

*Why it beats this attack:* clamping is not caught by a check at
runtime. It cannot be expressed in the one statement that writes
the count.
*Say:* 8 held and the operator asserts 5. There is nowhere in that
statement to compute 8 and write it instead.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E1 · G2 | `CorrectionIT.aCorrectionThatFitsIsTakenAtExactlyTheNumberAsserted` | 8 asserted against 8 held: it fits, so it is taken — at 8, never clamped |

---

### SL-2 G3. A refused change moves nothing.

*What could go wrong (the attack):* the refusal is decided, but
the attempt has already touched the numbers, or ends a hold on its
way out.
*Say:* 10 on hand, 8 held, the operator's 7 refused. But the held
count reads 9 afterwards, so the next honest correction is refused
for a unit nobody holds.

*What we guarantee:* after a refused adjustment, the item's count,
its reservations and their active sum are what they were before the
request arrived.

*Kills:* 6, and it is what makes the chosen refusal checkable.

- **E2 (G3).** Checked on E1's refusal: every reservation that was
  active before is active after, with the same units. The count,
  the sum and the reservation records are identical to the state
  read before the request.

*The wall:* one statement, and no second write on the path. The
adjust path writes once. A `WHERE` that does not match writes
nothing and returns no row, and the door turns that into a refusal
(ADR-0010's `409`).

*Why it beats this attack:* there is no interval in which something
is moved and then undone, and no reservation write on this path at
all. So "refused" and "unchanged" are one event, not two that must
agree.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E2 · G3 | `CorrectionIT.aRefusedCorrectionMovesNothing` | the refusal of E1, with every number the store keeps compared before and after |

---

### SL-2 G4. When the same correction arrives twice, the state after is the state after one.

*What could go wrong (the attack):* a downward correction is
resent, and the count falls twice for one loss (F11).
*Say:* 10 on hand. The operator asserts 9, the reply is lost, and
they send 9 again. A door that read the number as an amount to add
would leave 19, then 28. That is what this slice's red run actually
printed.

*What we guarantee:* an adjustment asserts a value, not a
difference. The state after two identical assertions is the state
after one.

*Kills:* 7.

- **E3 (G4).** The same correction sent twice, in two cases:
  - one that fits: the state after the second assertion is
    identical to the state after the first;
  - one that does not: refused both times, and nothing moved
    either time.

  The resend is the same bytes at the same door, not a second
  request written to look alike.

*The wall:* the door's contract (ADR-0011): an adjustment asserts a
value, never a difference. G2's assignment backs it.

*Why it beats this attack:* a resend cannot lower the count twice
for one loss, because no statement on this path adds or subtracts.
The second assertion assigns the same number to the same row. Kill
7 is stopped by what the door accepts, not by a duplicate check. So
no request identity is needed, and none is claimed. W2 stays
fenced, and the claim about retried requests stays among the
intent's rejected candidates.
*Say:* the operator asserts 9, the reply is lost, they send 9
again. The row reads 9 after the first and 9 after the second.

*Options weighed:* how a resend is made harmless.

**Value semantics at the door** — *chosen*

*How it holds G4:* a second assertion of the same number assigns
the same number. Nothing accumulates.

*Cost:* none new. ADR-0011 already decided values over deltas, and
this slice uses that decision instead of taking one. It cannot tell
a resend from two honest corrections that agree, and it does not
need to.

---

**A request identity per adjustment:** an idempotency key, stored,
and refused on repeat

*How it holds G4:* the ledger recognises the resend as *the same
request* and answers the first outcome again.

*Why not:* it is a second claim, not this promise's. The intent
kept "a retried reservation request creates at most one
reservation" among its rejected
candidates, and the definition fenced it as W2. It would give the
operator a truthful "you already sent this", which nobody has asked
for. The cost is a key, a store of keys, and their expiry.

---

**Deltas with identity:** `{"delta": -3}` plus a key

*How it holds G4:* the count moves once per identified request.

*Why not:* strictly worse here. It needs the key, *and* it reopens
the reordering question: deltas give different results in different
orders when one can be refused. 8 on hand, 6 held: −3 then +2 ends
at 10, because the −3 is refused. +2 then −3 ends at 7. ADR-0011
rejected deltas already.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E3 · G4 | `CorrectionIT.aCorrectionThatFitsResentAssertsTheSameState` | the same body sent twice at the same door (F11, kill 7) |
| E3 · G4 | `CorrectionIT.aCorrectionThatDoesNotFitResentIsRefusedTwiceAndMovesNothing` | the refused correction resent |

---

### SL-2 G5. When two corrections arrive in the other order than they were made, each is judged alone against the numbers it meets, and no admitted one leaves the count under the units held.

*What could go wrong (the attack):* two corrections arrive in the
other order than they were made. The ledger, assuming an order,
admits one because the other came before it (F13).
*Say:* 9 made first, then 7, delivered 7 then 9. Which arrived
first must not change whether either is admitted. Only the count
and the holds when each lands decide it.

*What we guarantee:* each adjustment is judged alone, against the
state it meets. The ledger asserts no order among corrections, and
needs none for the invariant to hold. Whichever order they arrive
in, no admitted one leaves the count under the sum.

*Kills:* 8.

- **E4 (G5).** Two corrections for one item, delivered in the
  opposite order to the one they were made in, against identical
  starting states. The invariant is read in every state between
  them and after. Where the surviving value is the older one, the
  record says so, and calls it W1's remainder, not a violation.
  The evidence shows the invariant held, not that the world was
  right.

*The wall:* an absence, made structural. No ordering state exists
on the adjust path: no version, no sequence number, no supplied or
stored "as of" instant. Nothing is consulted but the row as found.
Held by a test that reads the source and fails if any appears.

*Why it beats this attack:* kill 8 arrives as two assertions in the
wrong order. Each is judged alone against the state it meets, so no
order can put the count under the held units.

*What the test is for:* the danger is not the code as written, but
a later change. An absence that nothing enforces can be filled by
one well-meant commit. So the structural test is the enforcement,
as with SL-1's no-process-clock rule.

*Options weighed:* whether the ledger should know the order.

**No ordering at all** — *chosen*

*How it holds G5:* each assertion is judged alone against the
state it meets. No order can break the invariant.

*Cost:* the count can end at the older value. That is W1's
remainder, which W1 accepts. The ledger claims nothing about which
correction was made last.

---

**An operator-supplied instant, last-write-wins**

*How it holds G5:* a late-arriving older correction is ignored, so
the count tends to the operator's latest truth.

*Why not:* it trusts the operator's clock, and T3 trusts their
*identity*, not their timekeeping. And it is a claim about the
world, which L2 refuses and W1 fences. Shrinking W1 takes a dated
revision of the definition, not a slice's plan.

---

**A ledger-assigned sequence per item**

*How it holds G5:* the ledger orders what it received.

*Why not:* it orders *arrival*, which is exactly what F13
scrambles. It would answer a question nobody asked, and leave the
real one untouched.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E4 · G5 | `CorrectionIT.aLateOlderCorrectionUnderTheHoldsIsRefused` | 9 then 7 swapped in delivery, the late 7 arriving against 8 held (F13, kill 8) |
| E5 · G5 | `NoOrderingStateOrSecondWriterTest.anAdjustmentCarriesNothingButTheAssertedCount` | the code read, not run: the door carries one field |

---

### SL-2 G6. Any path that changes the on-hand-count meets the same check as the operator's correction.

*What could go wrong (the attack):* a second way in (a script, a
migration, an admin path, a later feature) lowers the count without
meeting the comparison.
*Say:* a bulk importer sets 5 while 8 are held. The store refuses
the row, so the state survives. But the importer meets an error
where the door would have given it a refusal, and nothing told it
what it did wrong.

*What we guarantee:* P1 makes the count and every change to it ours:
"Restock, loss and recount reach it only as requests at our door."
That holds for changes this slice never foresaw. No path writes the
count without facing the invariant.

- **E5 (G6).** A structural check that reads the code, not the
  runtime: the on-hand-count has one writing path, and a second one
  added anywhere fails the check. Like SL-1's structural test, it
  is not a text search.

*The wall:* the store's check constraint as the backstop, and a
structural test that the count has one writing path.

*Why it beats this attack:* the constraint refuses an under-held
row whoever writes it: a script, a migration, a console, a future
feature. So the *state* is safe by structure. The test guards the
other half, the *decision*.
*Say:* a bulk importer sets 5 while 8 are held. Without the test,
nothing stops the importer from being written. The constraint still
saves the state, but the importer meets an error where the door
would have given it a refusal.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E5 · G6 | `NoOrderingStateOrSecondWriterTest.theCountHasOneWritingPath` | the code read: one class reaches the store, one method takes a count |

---

**Inherited, not re-owned.** The attack's answers that SL-1
already holds are named, not derived again:
- the comparison is one act, not two, and an adjustment and a
  concurrent admit do not interleave (SL-1 G1 and G3, kill 5);
- an invalid request (a negative or absurd count, an unknown item)
  never reaches the decision (SL-1 G6, FC1);
- activeness is judged by one clock (SL-1 G5, FC3).

This slice relies on them and adds no claim about them.

**The attack found no seventh guarantee.** Two more answers belong
elsewhere:
- "the operator is not told which happened" is the caller's view,
  refused at L2. The part of it that concerns the state is G3.
- "the count now disagrees with the shelf" is W1, fenced. "The
  correction's shape — decided" names it as the cost of the choice.

## Folds and flags — the written zeros

**Folds: none.** SL-2's registry row names no fold. The framing's
three folds are used elsewhere: FC1 and FC3 by SL-1, FC2 by SL-3.
Writing the zero keeps the count checkable.

**Flags: none.** SL-1's row carried one: kill 10, an adversity no
hammering could stage, and its specification owed an answer.
SL-2's row carries none, and it is worth saying why. Every
adversity here is staged the normal way: an honest request at the
door, the same request sent twice, and a pair delivered in the
other order. No warning is handed down, and nothing is owed an
answer this specification does not give.

## What this slice does not claim

- **That the count agrees with the world.** W1, fenced. A refused
  correction leaves the ledger knowingly disagreeing with the
  shelf. The operator can only wait for expiry, or for the callers'
  own releases. That is the cost of the choice, stated in "The
  correction's shape — decided".
- **That an operator can make the ledger true.** No operator-side
  exit exists. Whether one should is a question about exits, so
  this slice hands it to SL-3 by name instead of inventing it here.
  SL-3 added no operator-side exit (its record, "Whether an
  operator can end reservations").
- **That a resent correction gets the same answer.** The world
  moves between the two: a correction refused while holds stood may
  be admitted once they have gone. What holds across the resend is
  the invariant, not the answer.
- **That corrections are applied in the order they were made.**
  The order is the network's (F13), and the remainder is W1's. The
  ledger claims only that no arrival order can put the count under
  the sum.
- **That the sum is minimal.** A hold past its expiry still counted
  until an exit ended it (SL-1, "Deviations and provisionals"). So
  a refusal here could be stricter than needed, refusing against
  holds already past their expiry. That was SL-3's to fix, not this
  slice's. SL-3 fixed it (its record, "When an expired hold's units
  are free"), and this slice's evidence ran green unchanged on it.
- **Who may adjust.** W5, T3: identity is trusted at the door.
- **That a reply reaches the operator.** The caller's view, refused
  at L2.

## The plan, across guarantees

<!-- The second part: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy: database constraint → type system →
     single validated entry path → runtime check → code review →
     hope. The strongest available is chosen, justified against the
     named adversity, not in general. -->

### What the walls need

Nothing new. SL-1's migration V1 already carries both tables and
the check constraint `reserved <= on_hand_count`. ADR-0011 already
fixed the adjustment as a value at the door. This slice adds no
table, no column and no migration. It adds evidence, and two
structural tests where a guarantee is held by an absence. Without
those tests, nothing would notice the absence being filled.

The plan's main point: **the walls this slice needs are standing,
built by SL-1 against a different adversity.** The registry
predicted it at SL-1's close, in words since revised: "most of
SL-2's invariant holds by structure today". The work here has three
parts:
- justify each wall against *this* adversity;
- prove it against the two corrections nothing had yet sent;
- stop the absences from being quietly filled in later.

Every guarantee has one owner. Each of the two absences (G5, G6) is
held by a test that reads the code, not by a habit.

### The options chosen, and the ones not

G1, G2 and G3 inherit SL-1's comparison (the options weighed under
SL-1 G1, five options against one adversity). Nothing in this
slice's adversity changes that verdict, and running it again would
add nothing. Two guarantees here do have a real choice. Both were
put to the reviewer before the choice was taken. Their options are
in SL-2 G4's and G5's blocks.

### Escape hatches hunted, afresh

- **A migration or a console writing the count.** The constraint
  refuses an under-held row from any of them. The rule that
  migrations carry structure and never ledger rows stands from
  SL-1, unchanged here.
- **The counter drifting from the rows.** `reserved` is the entry
  path's bookkeeping (SL-1's standing guard). If it drifted low, a
  correction that should be refused would be admitted. So this
  slice's witness recomputes the active sum from the reservation
  rows and never trusts `reserved` alone, exactly as SL-1's witness
  does.
- **A second decision path for the count.** None. G6's structural
  test keeps it that way.
- **A "force" flag or an admin correction.** None exists, and none
  is added. It would bring back the rejected choice by another
  route: forcing the count under the holds is exactly what ending
  reservations was proposed to allow.
- **The refusal turned into a clamp by a later hand.** G2's owner
  is how the statement is written. The evidence pins it with the
  mirror case: a correction that fits is taken exactly.

### The surface, at its minimum

Nothing is added to the application. The door already carries
`POST /items/{item}/adjustments` with its value body (ADR-0011).
The refusal already answers `409`, distinct from `400` (ADR-0010).
The store already carries the constraint. What this slice adds
lives entirely under test: the evidence for E1–E4, and the two
structural tests for E5 (G5's no ordering state, G6's one writing
path). A slice is not a feature, and this one delivers no feature
at all.

### Deviations and provisionals, so the close can see them

- SL-1's provisional refusal is no longer provisional ("The
  correction's shape — decided"). SL-1's record called it
  provisional until this slice's close. Then SL-1 G3 and its folds
  were corrected, so the correction and its reason landed together.
- G5 and G6 are guarantees held by an absence, plus a test that the
  absence stays. This run has now met that pattern twice: SL-1's
  no-process-clock and no-instance-state tests, and these two. The
  hand-off already filed with the bundle (absence as a level of the
  enforcement hierarchy) gains a second example. It is named here
  so the close can carry it.

## Evidence, as delivered
All of it runs under `./mvnw test`: 39 tests, 0 failures, nothing
exported, and the ground need not be up. The requests go one at a
time throughout, from an honest operator at the real door. The
witness is read from the store as `runtime`, from outside the
application.

E5 was signed for G6 alone (its criterion, in SL-2 G6's block). The
plan gave G5's absence a structural test as well (SL-2 G5's owner).
So E5 as delivered covers both. The criterion stands as signed.

Beside them, and not evidence:
`CorrectionIT.theArrivalOrderDecidesWhichNumberSurvives`. It says
so on itself: it is a tripwire on the decision to keep no order.
This section's own finding, below, is how it came to be marked.

### Red before green, from actual output

- **E1, E2 and E4's evidence half.** The wall was removed on the
  working tree: the `WHERE` guard out of `Ledger.adjust`, the
  `item_never_oversold` constraint out of migration V1. The witness
  read `Numbers[onHandCount=7, held=8, activeSum=8,
  reservations=1]`: eight units held against seven on hand, the
  promise's negation, created. Restored, and the same tests went
  green unchanged.
- **E3.** Its wall is not a guard but what the door accepts. So the
  absence to create is delta semantics: the statement made to add
  the asserted number instead of assigning it. The count read 19
  where 9 was asserted: F11 itself, the count moving twice for one
  loss. Restored, green.
- **E5.** There is no wall to remove, so each forbidden thing was
  planted in turn:
  - an `asOf` beside the count at the door, which gave
    `["onHandCount", "asOf"]` where exactly `["onHandCount"]` is
    required;
  - a second method taking an `OnHandCount`, which gave "expected
    size 1 but was 2";
  - a `JdbcClient` field on the controller, which the rule named:
    *Field `ReservationController.store` has type `JdbcClient`*.

  All three were restored.

No removal or plant ever landed in history. Each lived on the
working tree for one run, and `git status` showed only the new test
file afterwards.

### What the build found that the specification had not

- **E1's first red proved nothing.** It failed on the status code,
  *expected 409 CONFLICT but was 200 OK*, and stopped before
  reading the store at all. That red proves the test reads status
  codes, not that it watches the promise. The test was reordered to
  assert the witness first, and its second red carried the numbers
  above. The order of assertions is part of the evidence, not a
  matter of taste.
- **E4 was two cases under one name.** With the wall absent, the
  late-correction case went red, and the either-order case could not.
  With 4 units held, both corrections sit above the holds, so no
  implementation could break the invariant there. L4 had already said
  as much, in words since revised: kill 8 is *"kill 6 if the older is
  lower; otherwise W1"*. The specification's E4 did not carry the
  split. The case a missing wall can break is the evidence. The case
  nothing can break is a tripwire, and now says so. The guarantee and
  the criterion stand as signed. This is how the criterion divided
  when it met the code.
- **The skill had no name for the second kind.** Corrected in place at
  `cbc-slice` (decisions log, 2026-09-21), with the counting rule that
  keeps it honest: a tripwire never counts as evidence, and the red
  run, not the author, decides which kind a test is.

### Read against the test shape, 2026-10-13

SL-2's tests were written before the test shape existed (SL-3's
close). They were read against it on this date (RETRO.md, D13).
Fixed: every first line names its slice ("SL-2 E1 · G1 — kill 6"),
and every label on a number prints the reading it came from. Not
changed, and listed here for the reviewer:

- **The door's answer before the witness.**
  `aCorrectionThatFitsIsTakenAtExactlyTheNumberAsserted` asserts the
  status and the body, then reads the store.
  `aRefusedCorrectionMovesNothing`, both resend tests and the
  arrival-order tripwire assert each status as they send.
- **The promise after the exact numbers.**
  `aCorrectionThatFitsIsTakenAtExactlyTheNumberAsserted` asserts the
  invariant after the count.
- **No promise on the reading after the adversity.**
  `aRefusedCorrectionMovesNothing` and both resend tests compare the
  reading after with the one before, and never assert the invariant
  on it.
- **No witness.** `CorrectionIT.aRefusalIsNotAnInvalidRequest`
  reads only the two statuses.

## Standing guards

What could break this slice later, and what watches for it:

- **A later feature touching the count.** Any new path that writes
  `on_hand_count` re-reads this specification first. `E5 · G6`
  fails the moment a second writing path appears. That is the
  reminder, not the rule. The rule is that the comparison and the
  write stay one act.
- **The refusal turned helpful.** A later hand may decide that
  clamping the count down to the held units is kinder than
  refusing. `E1 · G2` is what fails. "The correction's shape —
  decided" says why it must not be reopened lightly: the ledger
  would then assert a number no operator sent.
- **An order arriving quietly.** A timestamp on the request, a
  sequence, a last-write-wins rule: `E5 · G5` fails, and the
  tripwire beside it fails too. Both point at SL-2 G5's options
  weighed, and at W1's fence, which needs a dated revision to move.
- **The held-units counter drifting.** Inherited from SL-1's own
  guard: `reserved` is the entry path's bookkeeping. If it drifts
  upward, honest corrections are refused for units nobody holds.
  This slice's witness recomputes the active sum from the
  reservation rows instead of trusting the counter, and `E2 · G3`
  compares both.
- **The door growing a force flag.** An admin correction that
  bypasses the comparison would bring back the rejected choice by
  another route. Nothing structural stops a new endpoint from being
  written. Only `E5 · G6`'s one-writing-path rule and this
  paragraph stand in the way.

## Sign-offs

<!-- Dated lines, the reviewer's: the specification before the plan,
     the plan before the build. -->

- 2026-09-20 — the specification (§1–§7) signed by the reviewer.
- 2026-09-21 — the plan (§8) signed by the reviewer.
- 2026-10-01 — §8's faces for G4 and G5 rewritten from tables into
  blocks, one face each, the chosen one first; the same faces, the
  same reasons, no decision changed.
- 2026-10-01 — §9 says E5 grew to cover G5 as well as G6, which §5
  had not carried; §5 is unchanged.
- 2026-10-08 — `E5 · G6`'s one-writing-path rule widened by the
  review after SL-3: "reaches the store" is any database API —
  `java.sql`, `javax.sql`, Spring's JDBC and its transactions — and
  "the ledger" is the one class by its full name. As first written,
  a class holding a `DataSource`, or one named `CleanupLedger`,
  passed it; both were planted and now fail. The guarantee and its
  owner are unchanged.
- 2026-10-13 — five claims rewritten to say what happens, condition
  first (RETRO.md, D14), each checked against its block and its
  tests; G3's already did, and stands. The guarantees, owners and
  evidence are unchanged.
- 2026-10-14 — written plain, no decision changed: one idea per
  sentence, dash and semicolon chains split, lists as lists. Metaphors
  out ("in ink", "ran dry", "dies of the door's shape", "two things
  wearing one name", "rot", "movements", "faces"). "Banked" becomes
  "kept among the intent's rejected candidates", the intent's own
  heading. "The faces chosen" becomes "The options chosen". SL-3's
  close is cited by its sections ("When an expired hold's units are
  free", "Whether an operator can end reservations"), no longer
  retold. A glossary line under the title. The text before this pass
  is at efc6ba3. The guarantees, owners and evidence are unchanged.
- 2026-10-14 — read against the definition, the glossary and SL-3,
  no decision changed. The attack's closing line says no seventh
  guarantee was found, as SL-3 and SL-4 say it. Reason 1 says L2,
  not P2, makes the transitions out of active ours: P2 names the
  ones into active. A tripwire "never counts as evidence", the
  glossary's words. "Two shapes" and "The shape the walls need"
  become "two options" and "What the walls need", since a shape is
  a page under `.claude/shapes/`; this record's heading "The
  correction's shape — decided" keeps its name, which others cite.
- 2026-10-14 — read a third time, no decision changed: G3's kills
  line says "the chosen refusal", not "the correction's shape".
