# SL-3 — a reservation exits once

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

     "Decided before the specification" is this slice's own section:
     three things the framing does not decide, decided before the
     specification that depends on them. -->

*Regrouped by guarantee on 2026-10-13: each guarantee's claim,
attack, evidence criterion, owner, options and tests in one block.
No words changed beyond headings and references. A section number
cited before that date refers to the text as signed, at 59a7112.*

## Invariant

A reservation moves its item's numbers at most once on exit, and
never after it has ended. (`docs/system/registry.md`, SL-3; also
SL-3 in the definition's L4.)

The words, as the truth set has them:

- **An exit** is a way out of active. There are three:
  - **consume:** the units were sold. The reservation ends and its
    units leave the on-hand-count.
  - **release:** the caller gave up. The reservation ends and its
    units are free again.
  - **expiry:** the hold ran out. The same as release, with time as
    the one who acts.

  L2 names release and expiry. P4 names consume.
- **Ended** is FC3's, as SL-1 uses it: ended by an exit, or past
  its expiry as judged by one clock, not one per instance.
- **The item's numbers** are the on-hand-count and the units held
  by its reservations, the two the promise compares.

## Adversity

Exits that repeat, race, or arrive too late, and a clock that ends
a hold too soon. Taken from the definition's L1 as written:

- a caller resends consume after a lost reply: the on-hand-count
  lowered twice for one reservation (F3, kill 12);
- we retry a consume whose outcome we could not know: the count
  lowered twice (F19, kill 16's retry half);
- a caller sends consume and release for one reservation at the
  same instant, or two consumes: two exits racing on one
  reservation, each moving numbers (F4, kill 13);
- expiry ends a reservation while a consume for it is in flight:
  two exits racing, with time as one partner (F23, kill 14);
- a caller consumes or releases a reservation that already ended
  (expired, consumed or released), even very late (F5, kill 11);
- time jumps, and expiry fires early: holds end before their time,
  and late consumes follow (F24, kill 19's early half).

Kills covered: 11, 12, 13, 14, 16 (our retry), 19 (early). FC2 is
folded in: an exit names its own reservation and moves exactly what
it holds (kill 17).

What this slice does not face:
- our own death between consume's two moves, and our silence after
  an unknowable outcome (kill 15, and kill 16's silence half):
  SL-4;
- who may exit a reservation (kill 17's identity part): W5, T3;
- holds that never expire (kill 19's other half): V5;
- a retried reserve (kill 20): W2.

### How the evidence creates it, for every guarantee

Each guarantee's evidence *creates* its adversity through the real
door. It reads the witness from the store, from outside the
process. The witness is the invariant's, read from persisted state,
for the item and for each reservation on it:
- the on-hand-count;
- the units held, and the sum of active reservations as the store's
  clock judges them;
- how each reservation ended, if it has.

From these, the evidence checks two things. The on-hand-count has
fallen by exactly the units of the reservations that ended by
consume, once each. And no more is held than is on hand.

- **SL-1's and SL-2's evidence, unchanged and green.** The reserve
  and the correction now read expired holds as free ("When an
  expired hold's units are free"), and their guarantees must
  survive it.
- **The harness can fail (R5).** Each of E1–E7 is run with its wall
  absent and seen red, recorded from actual output. That state
  never lands in history. Then each is seen green, unchanged, with
  the wall standing. Where this slice builds the wall, the naive
  version lands first, and the wall is its own diff.

All of it runs under the one standard test command, with nothing
exported, and the ground need not be up.

## Decided before the specification

Three questions the framing leaves open, found at this slice's
opening (PLAN, Step 7) and decided by the reviewer on 2026-10-02,
each with its options.

### What a repeated exit answers — ADR-0013

The paths are ADR-0010's, and an exit carries no body: it names its
reservation and moves exactly what that holds (FC2). What the
framing does not say is what a repeat answers. The numbers are the
invariant's and move once either way. The reply is the door's.

**Chosen:** an exit repeated on a reservation that the same exit
already ended answers `200 OK`, the reservation as persisted. A
different exit on an ended reservation is still refused, `409`.
**Rejected:** `409` for every exit on an ended reservation. That is
true, but the caller who lost the first reply reads an error for a
consume that worked. The options and reasons in full are ADR-0013's.

### When an expired hold's units are free

FC3 already says a reservation past its expiry, by the store's
clock, is no longer active, and F5 says a consume after that is a
request against a reservation with no active state left. What the
framing does not say is when the units it held become free for
the next decision.

Before this slice, they never did. SL-1 left the held units
counting a reservation until an exit ends it (SL-1, "Deviations and
provisionals"), and nothing ended one by expiry.
*Say:* 10 on hand. A holds 3 until 12:00 and walks away. At 12:05,
B asks for 8. The held units still read 3, so only 7 look free, and
B is refused: at 12:05, and at every time after.

**Chosen: free from the expiry instant.** A reservation is ended
the moment its expiry passes, by the store's clock. Nothing has to
happen for that: no request, no sweep. Every decision taken after
the instant, a reserve or a correction, sees its units as free.
*Say:* at 12:05, B's 8 is admitted against 10 on hand and 0 held.

**Rejected: free eventually, within a bound.** Something ends
expired holds now and then. Until it runs, they still block units.
It is safe, since the invariant holds either way. But it holds
more than it must. And the something that runs is a new moving
part that can die or fall behind. Those are adversities this slice
does not name.

**What the choice pays.** Two debts handed here by name:
- SL-1's held units still counting expired holds (SL-1, "Deviations
  and provisionals");
- SL-2's refusals being stricter than needed, against holds already
  past their expiry (SL-2, "That the sum is minimal").

**What it costs.** The reserve and the correction are SL-1's and
SL-2's decisions, proved on their own evidence. This choice changes
what they read. Both slices' evidence must stay green, unchanged.
That is the standing guard SL-1's "Standing guards" names for any
later slice touching its state.

Changes no export: FC3 already defines *active* this way, and V5
already counts on expiry as the exit for abandoned holds.

### Whether an operator can end reservations

SL-2 handed this here (SL-2, "The correction's shape — decided", and
what it does not claim). An operator counts 5 on the shelf while 8 are
held. The correction to 5 is refused. They can only wait for expiry,
or for callers to release.

**Chosen: no operator-side exit in this slice.** The release at
the door does not ask who is calling. The definition trusts a
request's stated identity (T3) and leaves who may act outside (W5).
So an operator who knows a reservation's identifier can release it
like anyone else. What an operator cannot do is find those
identifiers: there is no list of an item's reservations. A list is
a feature no guarantee here needs, so it is not built. It waits in
the backlog.

**Rejected: an exit for the operator.** For example, a correction
that ends enough holds to fit. Something would have to choose which
holds end: the oldest, the largest, the latest. SL-2's decision
rejected that for a reason that stands here too: it is a policy no
possession grants. The system was never given a rule for choosing.

**What the choice costs.** The operator in SL-2's example still has
no practical recourse. The promise holds, but the seller can still
sell units that are not on the shelf, until the holds end. This is
W1's remainder, and it stays fenced.

## Guarantees

Each is one answer to: *what would let the invariant hold on paper
yet break in fact?* The attack runs until no new answer comes.
Every guarantee is a property. None names how it is held.

The numbers in the examples start from one item unless they say
otherwise: 10 on hand; reservation R holds 3, reservation S holds
5, so 8 are held and 2 are free.

### SL-3 G1. When exits race on one reservation, exactly one of them takes effect.

*What could go wrong (the attack):* two exits on R arrive at the
same instant: a consume and a release, or two consumes (F4), or a
consume as R's expiry passes (F23). Each checks that R is still
active, each sees that it is, and each moves the numbers.
*Say:* consume and release on R together. The consume takes R's 3
off the shelf: 7 on hand. The release also frees R's 3, so the
held units drop twice: 8 − 3 − 3 = 2. Only S is left, holding 5,
but the item now reads 2 held of 7, so 5 more units look free.
Five more are admitted, and 10 units are held against 7 on the
shelf.

*What we guarantee:* whether an exit is R's first is decided against
R's state at the moment the exit is recorded, never against a copy
read earlier. Of any exits that race on R, one ends it, and the rest
find it ended. Time is one of the racers: a consume recorded before
R's expiry instant ends R as consumed. One recorded after finds R
already ended by expiry. Never both.
*Say:* consume and release on R together. One of them ends R. If it
is the consume, the item reads 7 on hand and 5 held. If it is the
release, 10 on hand and 5 held. Either fits.

*Kills:* 13, 14.

- **E1 (G1).** A burst of exits on one reservation, held back and
  released at one instant, through the real door: consumes only,
  then consumes and releases mixed, then across more than one of
  our instances (F17, the harness's forked instances). After: the
  reservation ended exactly once, by one kind of exit, and the
  witness agrees with that kind and no other. Replies are not
  counted. Under ADR-0013 a repeated exit of the same kind also
  reads `200`, so only the store can say how many took effect.

- **E2 (G1, with time).** Many reservations with the shortest hold
  the door allows, and a consume for each sent on both sides of the
  expiry instant. After: each reservation ended by consume or by
  expiry, never both. The on-hand-count fell by exactly the
  consumed ones' units. Nothing is held beyond what is on hand.

*The wall:* the primary key on `reservation_exit.reservation_id`.
Every exit (consume, release, and expiry by tidy) ends a
reservation by writing its receipt. It moves numbers only for a
receipt it wrote itself.

*Why it beats this attack:* racing exits are racing inserts of the
same key. The store makes the second wait for the first, then
refuses it: `ON CONFLICT DO NOTHING` returns no row, so the second
moves nothing. Time as a racer is the same race. Consume writes
`consumed`, tidy writes `expired`, and the key admits one. Which
kind can be written at that instant is the trigger's to say, by the
store's clock.
*Say:* consume and release on R together. Both try to write R's
receipt. The consume's lands, and the release's returns no row. The
item reads 7 on hand and 5 held.

*If the wall were ever wrong:* the store's check on the units held
(G6, revised 2026-10-05). A release taking R's 3 a second time
lowers `reserved` below what the reservations with no receipt hold,
and the check refuses that transaction at its commit.
`item_never_oversold` alone cannot see it: it compares the two
stored numbers, not the reservations. That is why the key is the
wall, and the check on the units held is what stands behind it.

*Options weighed, for G1, G2 and G5:* what makes "ends once"
impossible to break.

**A receipt per reservation, one allowed** — *chosen*

*How it holds G1:* a second receipt is a second insert of a key.
The store refuses it, whoever sends it.

*Cost:* a second table, and *active* now reads two tables. Ended
reservations keep their receipts forever (W6 sees the growth).

---

**An ended mark on the reservation, set if not yet set**

*How it holds G1:* `UPDATE reservation SET ended … WHERE ended IS
NULL`. The store orders two updates to one row, and the second
finds it set.

*Why not:* the wall is the statement's condition. Any write that
leaves it out ends a reservation twice and the store does not
object. It defends the path, not the state.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E1 · G1 | `ExitStormIT.racingConsumesEndAReservationOnce` | fifty consumes on R held back and released at one instant (F4, kill 13) |
| E1 · G1 | `ExitStormIT.racingConsumesAndReleasesEndAReservationOnce` | twenty-five consumes and twenty-five releases on R, interleaved, at one instant (F4, kill 13) |
| E1 · G1 | `ExitStormIT.racingExitsAcrossInstancesEndAReservationOnce` | sixty exits split across three instances, each its own process (F17) |
| E2 · G1 | `ExitStormIT.consumesAroundTheExpiryInstantEndEachHoldOnce` | forty one-second holds, each consumed at its own expiry instant ± 0.1 s (F23, kill 14) |
| E2 · G1 | `ExitStormIT.consumesAndTidiesAroundTheExpiryInstantEndEachHoldOnce` | the same, with a reserve beside each consume whose tidy meets the same hold |

---

### SL-3 G2. An exit on a reservation that has ended moves nothing.

*What could go wrong (the attack):* the consume is retried after a
lost reply (F3). Or it arrives long after R ended (expired,
released or consumed), and its units have since gone to someone
else (F5). The exit acts as if R were still active.
*Say:* R expired at 12:00. At 12:05, B reserved 5, so 10 are held
(S's 5 and B's 5) against 10 on hand. At 12:10, a late consume on R
arrives and takes 3 off the shelf: 7 on hand, 10 held. Oversold
by 3.

*What we guarantee:* once R has ended, by any exit, no later exit
on R moves either number, however long after and however many
times. What the reply says is ADR-0013's: the same exit repeated
reads as the first, and a different one is refused. The numbers are
this guarantee's, and they do not move either way.
*Say:* the late consume finds R ended by expiry and is refused.
The item still reads 10 on hand, 10 held.

*Kills:* 11, 12, 16 (our retry).

- **E3 (G2).** One request at a time, the same bytes at the same
  door:
  - a consume, then the same consume again: the second moves
    nothing;
  - a release after a consume, and a consume after a release:
    refused, nothing moved;
  - the full form of kill 11: a reservation expires, its units go
    to a new reserve, and a late consume on it arrives. It is
    refused, the count is unchanged, and the new hold is intact.

*The wall:* the same primary key, for an ended reservation with a
receipt. And the trigger, for one that has expired with no receipt
yet: it refuses a `consumed` or `released` receipt at or after the
expiry instant.

*Why it beats this attack:* a retried consume (F3) is a second
insert of a key that exists. A late consume (F5) on an expired
reservation whose receipt nobody has written yet is refused inside
the store, by the store's clock. The application does not have to
remember to compare.
*Say:* R expired at 12:00 and B took its units at 12:05. At 12:10,
a consume on R arrives. Its path writes R's `expired` receipt. Had
it tried `consumed`, the trigger would have refused it. The item
still reads 10 on hand, 10 held.

*If the wall were ever wrong:* the statement's own condition
(`expires_at > now()`) stands in front of the trigger.

*Options weighed:* what makes "ends once" impossible to break: in SL-3
G1's block.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E3 · G2 | `ExitDoorIT.theSameConsumeTwiceMovesOnce`, `theSameReleaseTwiceMovesOnce` | the same exit sent twice at the same door (F3, kill 12, 16's retry half) |
| E3 · G2 | `ExitDoorIT.aReleaseAfterAConsumeIsRefusedAndMovesNothing`, `aConsumeAfterAReleaseIsRefusedAndMovesNothing` | a different exit after the first (F5) |
| E3 · G2 | `ExitDoorIT.aConsumeAfterExpiryIsRefusedAndTheExpiryRecorded` | a consume after R's hold ran out (F5) |
| E3 · G2 | `ExitDoorIT.aLateConsumeIsRefusedAfterANewHoldTookItsUnits` | kill 11 in full: R expires, T takes its units, R's consume arrives late |

---

### SL-3 G3. A reservation ends by expiry when the store's clock passes its expiry instant, and not before. No instance's own clock decides it.

*What could go wrong (the attack):* time jumps (F24), and holds end
before their time. A late consume then follows on a reservation
that should still have been active (kill 11).
*Say:* R holds until 12:15. One of our instances has a clock that
jumped forward and reads 12:20. If that instance's clock decided,
R would be ended at what is really 12:10, and the caller's consume
at 12:11 would be refused for a hold they were promised until
12:15.

*What we guarantee:* a reservation ends by expiry when the one
clock, the store's (SL-1 G5), passes its expiry instant, and not
before. No instance's own clock decides it. Nothing ends a hold
ahead of that instant, in bulk or by rounding.
*Say:* at 12:11 by the store's clock, R is active, whatever any
instance's clock reads, and the consume ends it.

*Kills:* 19 (early).

- **E4 (G3).** A hold whose expiry is still ahead by the store's
  clock can be consumed. And a structural check reads the code, not
  the runtime: no exit path, and nothing that ends a reservation,
  consults a clock other than the store's. It works like SL-1's
  no-process-clock check, and is seen red by planting the violation
  it forbids.

*The wall:* the trigger refuses an `expired` receipt while the
store's clock reads before the reservation's expiry, and it sets
`ended_at` itself. Behind it stands SL-1's structural test that the
application never reads its own clock. It already covers every
class the application has, the new paths included.

*Why it beats this attack:* the only way a hold ends by expiry is
an `expired` receipt, and the store will not take one early by its
own clock. An instance whose clock jumped never supplies a time to
compare.
*Say:* R holds until 12:15. At 12:11 by the store's clock, a tidy
cannot write R's `expired` receipt, and the consume writes
`consumed`.

*If the wall were ever wrong:* tidy's own condition, `expires_at <=
now()`, in the store's terms.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E4 · G3 | `ExitDoorIT.aHoldStillAheadByTheStoresClockIsConsumable` | a tidy meeting R with two of its three seconds left (F24) |
| E4 · G3 | `NoInstanceStateOrClockTest.theLedgerConsultsNoProcessClock` | SL-1's rule, the code read: no process clock anywhere, tidy and the exits included |

---

### SL-3 G4. An exit moves exactly what its reservation holds, on its reservation's item.

*What could go wrong (the attack):* an exit claims a quantity other
than the reservation's, or names a reservation that does not exist
(F7, F6).
*Say:* a consume on R that says 5. The shelf loses 5 for a hold of
3: 5 on hand. Two units nobody sold are gone from the count.

*What we guarantee:* an exit names one reservation and nothing
else. A consume lowers the on-hand-count by exactly that
reservation's units and lowers the units held by exactly the same.
A release, and an expiry, free exactly that reservation's units.
An exit naming a reservation that does not exist moves nothing and
is answered as unknown.
*Say:* consume on R: 10 on hand becomes 7, 8 held becomes 5.

*Kills:* 17, folded (FC2).

- **E5 (G4).** A consume on a reservation of 3 lowers the count by
  3 and the units held by 3. A release frees exactly 3. An exit
  naming a reservation that does not exist is answered unknown, and
  nothing moves.

*The wall:* what the door accepts. The exit paths take no body, so
the type the controller binds has no quantity to carry. And the
statement takes the units and the item from the reservation's own
row, joined to the receipt it just wrote. The receipt's reference to
the reservation means no receipt names a reservation that does not
exist.

*Why it beats this attack:* there is no number in the request for a
caller to get wrong (F7), and no second place the amount could come
from. A reservation that does not exist is found absent before any
write, and answered `404`.
*Say:* consume on R: the statement reads R's 3 from R's row. 10 on
hand becomes 7, and 8 held becomes 5.

*If the wall were ever wrong:* partly, the store's check on the
units held. A consume that lowered `reserved` by 5 for R's 3 would
leave it unequal to what the open reservations hold, and be refused
at commit. Nothing stands behind the count. The check does not
compare it, so a consume taking 5 off the shelf for R's 3 would be
caught by no wall, only by E5.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E5 · G4 | `ExitDoorIT.aConsumeMovesExactlyItsReservationsUnits`, `aReleaseFreesExactlyItsReservationsUnits` | R's 3 consumed, and released, beside S's 5 |
| E5 · G4 | `ExitDoorIT.anExitTakesNoAmountFromTheRequest` | a consume sent with a quantity in its body (F7, FC2) |
| E5 · G4 | `ExitDoorIT.anExitOnAnUnknownReservationIsUnknownAndMovesNothing` | an exit naming a reservation that does not exist (F6) |

---

### SL-3 G5. An expired reservation frees its units once, however many decisions meet it.

*What could go wrong (the attack):* expiry has no request of its
own. Its units are free from the instant, and nothing has to happen
("When an expired hold's units are free"). So whoever meets an
expired hold first takes it out of the count. And two decisions
meeting it at the same instant each take it out.
*Say:* R expired at 12:00. At 12:05, two reserves for 5 arrive
together. Each sees R expired and frees its 3: 8 − 3 − 3 = 2 held,
8 free. Both reserves are admitted, and 15 are held against 10 on
hand. Really held: S's 5 and the two new 5s.

*What we guarantee:* an expiry is an exit like the others. It moves
the numbers once, whichever decision meets it first and however
many meet it together. The units it frees are free for every
decision after its instant.
*Say:* the two reserves at 12:05 see 5 held and 5 free. One is
admitted and one is refused. The item reads 10 held of 10.

*Kills:* none by number. This is the attack opened by the choice in
"When an expired hold's units are free": expiry acting with no
request of its own. If it lands, the result is kill 1's: more held
than on hand.

- **E6 (G5).** A burst of reserves on an item whose units are partly
  held by a reservation that has just expired. After, and sampled
  during: never more held than on hand, and the expired hold's units
  freed exactly once. Beside it, a tripwire for the choice in "When an
  expired hold's units are free": after the instant, a reserve that
  fits only if the expired units are free is admitted. That guards the
  decided choice (free from the instant), not the promise: holding
  more than needed cannot oversell. It says so on itself, and never
  counts as evidence.

*The wall:* the same primary key. And tidy lowers `reserved` by the
receipts it wrote (its `RETURNING` rows), never by a sum it read.

*Why it beats this attack:* two tidies meeting the same expired
hold race to insert its receipt. One row is written, and only that
statement subtracts. A tidy that read a sum and subtracted it would
subtract twice, since each reads before the other writes. That is
the attack exactly.
*Say:* R expired, and two reserves for 5 arrive at 12:05. Both
tidy. One writes R's receipt and takes 3 off. The other finds the
key taken and takes nothing. 5 held, one reserve admitted, one
refused.

*If the wall were ever wrong:* the store's check on the units held
(G6, revised 2026-10-05). A tidy that subtracted a sum it read
leaves `reserved` below what the open reservations hold, and its
transaction is refused at commit. This block first named
`item_never_oversold` here, and the build showed it does not hold.
With reserves smaller than the units freed twice, the second free
and its admit keep `reserved` within the count while more is held.
It was seen red as 16 units actively held against 10 on hand.

*Options weighed:* what makes "ends once" impossible to break: in SL-3
G1's block.

*Options weighed:* how expired holds leave the held units (decided
before the specification: from the instant).

**Tidy first, on every decision** — *chosen*

*How it holds G5:* each reserve and adjust writes the receipts of
the item's expired holds before deciding, and frees exactly what it
wrote. The key makes the freeing happen once.

*Cost:* a little work on every request. And `reserved` reads high
between an expiry and the next request on that item, which no
decision sees.

---

**No stored held units: sum the active reservations at each
decision**

*How it holds G5:* nothing to free. Expiry is read, not written.

*Why not:* `item_never_oversold` compares two numbers on one row.
Without a stored `reserved`, there is nothing for it to compare.
SL-1's strongest wall would give way to a lock every path must
take. SL-1 weighed exactly this option among SL-1 G1's ("row lock,
then compute") and chose against it. That is SL-1's guarantee
moving from structure to sampling.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E6 · G5 | `ExitStormIT.racingReservesFreeAnExpiredHoldOnce` | fifty reserves at one instant, each one's tidy meeting R just expired |

---

### SL-3 G6. Any path that ends a reservation, or moves the numbers for one, meets the same checks as consume and release.

*What could go wrong (the attack):* a second way in (a script, a
migration, an admin path, a later feature) ends a reservation, or
moves the numbers for one, without meeting G1 to G5.
*Say:* a cleanup script deletes reservations that look old and
gives back their units. It deletes R, which a consume ended a
minute ago, and takes R's 3 off the held units a second time.

*What we guarantee:* no path ends a reservation or moves the numbers
for an exit without facing the same rules, including paths this slice
never foresaw. L2 makes the transitions out of active ours, through
P2's records. This keeps them in one place.

*Kills:* none by number. The attack is on the walls themselves: a
second way in that skips G1 to G5, whose kills it would bring back.

- **E7 (G6).** A structural check that reads the code:
  reservations end, and their numbers move on exit, by one path
  only. A second one added anywhere fails the check, as in SL-2's
  E5.

*The wall:* two parts.
- The trigger, which refuses any change or deletion of a receipt by
  every identity short of the superuser.
- The check on the units held (revised 2026-10-05). At the commit of
  every transaction that touches an item, its reservations or their
  receipts, the store compares the item's `reserved` with the units
  of its reservations that have no receipt. It refuses the
  transaction if they differ.

In front of it stands a structural test that `reservation_exit` and
`reserved` have one writing path, as in SL-2's E5. It is an early
warning that fails at build time.

*Why it beats this attack:* the cleanup script in the attack above
cannot delete R's receipt. The store refuses it, whoever runs it.
So R cannot look active again, and a second receipt cannot be added
either. Nor can the script give back R's units by hand. Lowering
`reserved` with no receipt leaves the units held below what the
open reservations hold, and the store refuses it at commit.
*Say:* a script deleting receipts older than a day meets an error on
the first row. A script setting R's 3 free without a receipt meets
"the units held read 5, its reservations with no receipt hold 8".

*If the wall were ever wrong:* nothing behind it. These are the
walls the others fall back on. What notices them gone is the
catalog test. It names the guard and the check's three triggers,
enabled, so it fails before any race can pass without them.

*Options weighed:* whether a receipt can be changed or deleted.

**A trigger refusing it** — *chosen*

*How it holds G6:* the store refuses `UPDATE` and `DELETE` on
receipts, whoever sends them, short of the superuser.

*Cost:* logic in the store that a reader of the application does
not see. The catalog test names it, so its absence is noticed.

---

**Revoke the runtime identity's update and delete on receipts**

*How it holds G6:* the grant system refuses the application itself.

*Why not:* it rewrites the infrastructure contract, whose runtime may
write every table, including every one a future migration creates
(term 4). A trigger gives the same refusal without changing the
ground.

---

**Code only: one path and the structural test**

*How it holds G6:* nothing in the application writes a second way.

*Why not:* a script outside the application is exactly the attack,
and code does not see it.

*Options weighed:* who moves the numbers when a receipt is written.

**The application's statement** — *chosen*

*How it holds G6:* the same statement that writes the receipt
moves the item's numbers, on the one entry path. The structural test
keeps the path single.

*Cost:* a receipt written by some other path would not move the
numbers. The trigger refuses no insert for that. The structural test
is the guard.

---

**A trigger that moves the numbers whenever a receipt is written**

*How it holds G6:* a receipt and its numbers become one act of the
store. No path could write one without the other.

*Why not:* SL-1 weighed exactly this for `reserved`, and kept it as
the first option to revisit if a second write path ever appears
(among SL-1 G1's options). None has. It would move the arithmetic
the reader needs to see out of the application. This slice's
triggers refuse. They do not compute.

*Options weighed:* what refuses the numbers moving without a receipt
*(added 2026-10-05, during the build)*

**The store checks the units held against the open reservations** —
*chosen 2026-10-05*

*How it holds G6:* a constraint trigger on `item`, `reservation`
and `reservation_exit`, deferred to the commit, compares each
touched item's `reserved` with the units of its reservations that
have no receipt. Each of these leaves the two unequal, and the
transaction is refused, whoever sends it:
- a number moved without its receipt;
- a receipt written without its move;
- a count of units held set by hand;
- a hold added or deleted without its units.

It is deferred because an exit writes its receipt before it moves
the item row, and only the finished transaction is judged.

*Cost:* every commit that touches an item re-reads that item's
reservations, and receipts are kept forever (W6 sees the growth).
Tests and scripts that write rows by hand must keep the two in step,
as the ledger does. The arithmetic stays in the application. The
store only refuses.

---

**The structural test alone** — *as signed on 2026-10-03*

*How it holds G6:* nothing in the application lowers a number
without its receipt.

*Why not, on revision:* it reads the application's SQL as text. A
script outside the application, or a statement assembled at
runtime, passes it, as the attack above says. Kept as an early
warning in front of the check.

---

**The exits as functions, and `runtime` losing its direct writes**

*How it holds G6:* the grant system refuses every write but the
functions'.

*Why not:* it rewrites the infrastructure contract's term 4. And it
moves SL-1's and SL-2's proven statements into the store with SL-3's.
The check refuses the same writes to the numbers without changing the
ground.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E7 · G6 | `NoSecondWayOutTest.receiptsAndTheUnitsHeldAreWrittenByTheLedgerAlone` | the code read: no writer of receipts or of `reserved` outside the ledger |
| E7 · G6 | `NoSecondWayOutTest.theLedgersSqlIsWrittenAsTextBlocks` | the code read: no SQL in the ledger the next rule would not see |
| E7 · G6 | `NoSecondWayOutTest.numbersFallOnlyByTheReceiptsTheSameStatementWrote` | the code parsed: every exit's statement by its parts |
| E7 · G6 | `NoSecondWayOutTest.theLedgerNeverRewritesAReservation` | the code parsed: no statement updates or deletes a reservation (added 2026-10-08, the review after this slice; T4) |

---

**Inherited, not re-owned.** The attack's answers that SL-1 and
SL-2 already hold are named, not derived again:
- an admit is one act against the truth at the moment of recording,
  and racing admits do not interleave (SL-1 G1 to G3, kills 1–5);
- activeness is judged by one clock, the store's (SL-1 G5, FC3),
  which G3 relies on;
- a correction never leaves the count under the sum (SL-2 G1–G6).

The choice that expired units are free from the instant changes
what the reserve and the correction read. Their guarantees are not
re-owned here, but their evidence must stay green unchanged. That is
the guard SL-1's "Standing guards" names for any later slice
touching its state.

**The attack found no seventh guarantee.** Four more answers belong
elsewhere:
- "we die between consume's two moves" is SL-4's (kill 15);
- "the caller is not told whether theirs was the first exit" is the
  caller's view, refused at L2, and ADR-0013 decides what the reply
  says;
- "someone exits a reservation that is not theirs" is W5, with
  identity trusted at the door (T3);
- "a hold that never expires" is V5.

## Folds and flags — the written zeros

**Folds: one, FC2.** An exit names its own reservation and moves
exactly what it holds. G4 uses it, and its evidence is E5. The other
two folds are SL-1's, FC1 and FC3. This slice relies on FC3 for G3,
and checks FC1's third part, a reservation that exists, in G4 (E5).

**Flags: none.** SL-3's row carries none, and it is worth saying
why. Every adversity here is staged the normal way: the same
request sent twice, exits held back and released together, holds
that run out by the store's clock while the evidence waits. The one
that looks as if it should be flagged is kill 19's early expiry,
since hammering cannot make a time jump. It needs no flag, because
FC3 already removed it for instances, as SL-1's flag on kill 10 was
removed. No instance's clock decides, so an instance's jump ends
nothing, and E4 shows it. What FC3 does not reach is said under
what this slice does not claim.

## What this slice does not claim

- **That a jump of the one clock is caught.** The store's clock is
  the machine's (the runtime ground: one machine, one clock). If
  that clock itself is stepped forward, by a correction or a resume
  from pause, holds end early in real time, though on time by the
  clock. The ledger has no second clock to notice with, and FC3
  chose one. Telling a stepped clock from time that really passed
  is the question the definition leaves unprobed, "monotonic vs
  wall". G3 holds against every clock but that one.
- **That consume's two moves hold together if we die between
  them,** or after an outcome we cannot know. SL-4: kill 15, and
  kill 16's silence half.
- **That a reply tells a first exit from a repeat.** ADR-0013's
  cost, and the caller's view, refused at L2.
- **That an operator can end holds.** "Whether an operator can end
  reservations". W1's remainder stands.
- **Who may exit a reservation.** W5, T3: identity is trusted at
  the door.
- **That every hold ends.** A hold lasts as long as the caller
  asked, up to the door's bound. That it ends at all is V5's.
- **How long ended reservations are kept.** W6.

## The plan, across guarantees

<!-- The second part: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy: database constraint → type system →
     single validated entry path → runtime check → code review →
     hope. The strongest available is chosen, justified against the
     named adversity, not in general. -->

### What the walls need

One new table, created by this slice's migration, V2:

- **`reservation_exit`:** one row per ended reservation, its
  receipt. `reservation_id` (the key, and a reference to the
  reservation), `kind` (`consumed`, `released` or `expired`),
  `ended_at` (the store's clock). The primary key on
  `reservation_id` is the slice's main wall: **a reservation can
  have one receipt, and a second cannot be written by any path.**
- **One trigger on it,** a function that refuses three things,
  whoever writes:
  - changing or deleting a receipt;
  - an `expired` receipt written before the reservation's expiry
    instant, by the store's clock;
  - a `consumed` or `released` receipt written at or after it.

  It also sets `ended_at` itself: the expiry instant for `expired`,
  the store's now for the others. So no path supplies a time.

Nothing in migration V1 changes. `item` keeps its three
constraints, among them `item_never_oversold CHECK (reserved <=
on_hand_count)`. `reserved` keeps its meaning, made exact: the
units of reservations with no receipt yet.

*Active*, as the witness reads it: no receipt, and `expires_at >
now()`. A reservation past its expiry with no receipt yet is
already ended ("When an expired hold's units are free"). Its
receipt is written by the next request that meets it.

### How each path runs

All in one transaction each, on the one entry path (`Ledger`).

- **Tidy:** the first thing reserve and adjust do on an item. One
  statement writes an `expired` receipt for every reservation of the
  item that is past its expiry and has none (`INSERT … ON CONFLICT
  DO NOTHING RETURNING`). It lowers `reserved` by the units of
  exactly the receipts it wrote, not by a sum it read.
- **Reserve and adjust:** tidy, then the statement SL-1 or SL-2
  already proved, unchanged.
- **Consume:** writes a `consumed` receipt for the reservation if it
  has none and has not expired. In the same statement, it lowers
  `on_hand_count` and `reserved` by that reservation's units, only
  if the receipt was written. If the reservation has expired with
  no receipt, it writes the `expired` receipt instead and frees the
  units, and the consume is refused. The refused request has still
  written the receipt. The expiry it met had already happened
  ("When an expired hold's units are free"), and this records it.
- **Release:** the same, with a `released` receipt, lowering
  `reserved` only.
- **The answer** (ADR-0013): if no receipt was written, and the one
  that stands is the same kind → `200` with the reservation as
  persisted. Any other kind → `409`. No such reservation → `404`.

**One order for every path: receipts first, then the item row.** Each
path writes its receipts before it touches the item. It writes several
receipts in one fixed order (by identifier). That way, no two requests
can each hold one row while waiting for the other's. If a later change
broke the order, the store would end such a wait by failing one of the
two requests with an error: a refused request, never a wrong number.
The order is a matter of requests getting through (W3), not of the
promise.

Every guarantee has one owner, and each owner is the store's: the
key, the triggers, the checks. The paths are the application's, and
the walls behind them are not.

### The options chosen, and the ones not

Three choices were put to the reviewer on 2026-10-03, before this
plan was written, and decided:
- what makes "ends once" impossible to break (SL-3 G1, G2 and G5);
- how expired holds leave the held units (SL-3 G5);
- whether a receipt can be changed or deleted (SL-3 G6).

The fourth, who moves the numbers when a receipt is written (SL-3
G6), was put to the reviewer with the drafted plan. It was decided
on 2026-10-03, before the plan was signed. Each is in its
guarantee's block.

### Escape hatches hunted, afresh

- **A migration writing receipts or rows.** The rule from SL-1
  stands: migrations carry structure, never ledger rows. The key and
  the trigger refuse a second or a late receipt from a migration
  too.
- **The superuser, or `migrator`, disabling the trigger.** Both can.
  The catalog test (`MigrationPathIT`) gains the trigger, the key and
  the reference, and (revised 2026-10-05) the check on the units held.
  So an evidence run on a store without them fails before any race
  could pass.
- **Deleting a reservation.** One with a receipt cannot be deleted:
  the receipt's reference stops it. One without a receipt can be
  deleted only in the same transaction as its units leave
  `reserved`. Alone, the check on the units held refuses it
  (revised 2026-10-05; signed as open, the safe direction). Closed
  on 2026-10-08 for the one writer the definition trusts (T4): E7's
  fourth rule fails the build if the ledger ever updates or deletes
  a reservation.
- **`reserved` set by hand.** `item_never_oversold` refuses it above
  the count. The check on the units held refuses any value but the
  units the open reservations hold (revised 2026-10-05; signed as
  SL-1's drift case, seen only by the witness).
- **A new path that skips tidy.** It sees expired holds as held:
  the safe direction, a W3 cost. The structural test names every
  writer of `reserved`.
- **The operator's way out.** None is added ("Whether an operator can
  end reservations").

### The surface, at its minimum

- **The door:** `POST /reservations/{reservation}/consume` and
  `POST /reservations/{reservation}/release`, no body (ADR-0010,
  ADR-0013). The reservation in a response gains two fields from
  the definition's terms, `endedBy` and `endedAt`, absent while it
  is active.
- **The store:**
  - migration V2: the receipts table, its key, its reference, its
    kind check, the trigger;
  - migration V3 (revised 2026-10-05): the check on the units held,
    one function and its deferred constraint trigger on `item`,
    `reservation` and `reservation_exit`.

  As planned, no index beyond the key: nothing needed one at the sizes
  the evidence runs. Since 2026-10-08, migration V4 indexes
  `reservation(item_id)`, the need read from tidy's and the check's
  searches (ADR-0015).
- **The ledger:** `consume` and `release`, and tidy, called first by
  `reserve` and `adjust`. One new problem, an unknown reservation
  (`404`). The refusal (`409`) is SL-1's, reused.
- **Under test:**
  - the witness reads *active* with receipts;
  - the evidence for E1–E7;
  - the catalog test's new names;
  - the structural test's new writer rules;
  - the check on the units held, shown refusing directly as
    `runtime`;
  - the tests that write rows by hand, keeping the units held in
    step.
- **Not added:** a list of reservations ("Whether an operator can
  end reservations", backlog), a sweep, a read endpoint. And no
  index, until migration V4 (2026-10-08, above).

### Deviations and provisionals, so the close can see them

- **SL-4's invariant, held by structure here.** Consume's two moves,
  the receipt and the count, are one statement in one transaction.
  SL-4's invariant is that no readable state holds one without the
  other. This slice builds that wall but proves nothing about it:
  our death between the moves, and an unknowable outcome, are
  SL-4's adversity. Handed to SL-4 by name at the close.
- **SL-1's and SL-2's records,** which name SL-3 as the slice that
  ends expired holds and fixes the stricter-refusal debt, were
  corrected at this slice's close, 2026-10-07. Migration V1's
  comment "No ended state yet" stays as written, since migrations
  are history.
- **The guarantees are held by structure in the store, not by an
  absence,** this time. Absence as a level of the enforcement
  hierarchy is a hand-off already filed with the bundle (SL-2,
  "Deviations and provisionals"). This run met it twice: SL-1's clock
  and no-state tests, and SL-2's ordering and one-writer tests. Here
  it appears only as SL-1's clock test, reused.
- **The plan revised during the build, 2026-10-05.** G6's guard for
  the numbers was signed as the structural test alone. The build
  showed it reads text, and a writer outside the application passes
  it. G6 gains the store's check on the units held, in migration V3,
  and the structural test stays as an early warning in front of it.
  G1's and G5's "if the wall were ever wrong" now name the check.
  G5's first answer, `item_never_oversold`, was seen not to hold.
  The options signed on 2026-10-03 stand: the arithmetic in the
  application, and triggers that refuse and never compute.

## Evidence, as delivered
All of it runs under `./mvnw test`: 81 tests (39 at the branch point),
0 failures, nothing exported, and the ground need not be up. The
witness is read from the store as `runtime`, from outside every
instance. It is read first in every test, and sampled while the races
run. The door's answer is checked after it.

Beside them, and not evidence, each saying so on itself:
- `ExitDoorIT.aReserveAfterTheInstantFindsTheExpiredUnitsFree` and
  `aCorrectionAfterTheInstantFindsTheExpiredUnitsFree`, tripwires
  on the choice in "When an expired hold's units are free";
- `anActiveReservationCarriesNoEnding`, a tripwire on the answer's
  form;
- `anExitNamingNoReservationIsInvalid`, SL-1 G6's rule on invalid
  requests, reaching the new doors.

And the walls' own checks, straight against the store as `runtime`:
- `ReceiptGuardIT` (seven tests: the guard, the key and the
  reference refusing, and the store setting the ending instant);
- `UnitsHeldCheckIT` (nine tests: the check on the units held
  refusing and accepting);
- `MigrationPathIT`'s catalog tests, naming the key, the reference,
  the guard and the three triggers.

SL-1's and SL-2's evidence ran green unchanged beside all of it,
with tidy in the reserve and the correction.

**On the real ground,** 2026-10-07:
- rows read first, none out of step;
- the ground dumped;
- migrations V2 and V3 applied by Flyway as `migrator`, and the
  walls read from its catalog.

Through the door:
- R's 3 consumed (7 on hand, 5 held);
- the same consume again: `200`, and nothing moved;
- a release after it: `409`;
- S released (0 held);
- an unknown reservation: `404`;
- a one-second hold run out, its 7 units taken by the next reserve,
  and its late consume: `409`.

As `runtime`, a script lowering the units held met "the units held
read 0, its reservations with no receipt hold 7". One deleting a
receipt met "a receipt is never deleted".

### Red before green, from actual output

Every wall was made absent on the working tree for one run, then
restored. None landed in history. Where the wall is a rule over the
code, the violation it forbids was planted.

- **E1, the key and every `ON CONFLICT` removed,** the exit reading
  its receipt by a naive check, then insert. Fifty consumes read
  `onHandCount=4, held=2, activeSum=5`: R's 3 off the shelf twice,
  the invariant broken. Mixed: 7 on hand, 2 held, 5 active. Across
  three instances: 4, 2, 5.
- **E2,** with the guard's late check and the consume's
  `expires_at > now()` both removed: every one of forty holds
  consumed, twenty after their instant by the store's clock.
- **E2 with tidy,** the key removed: red three times out of three
  (81 on hand, 36 held, 40 active, and the like). But every double
  ending was `[expired, expired]`. The interleaving the test is named
  for, a consume uncommitted across the instant, is a window of a
  few milliseconds. Held open on the red tree only, by a 50 ms pause
  in the consume's transaction, it showed `[consumed, expired]`
  twice out of two.
- **E3,** the key removed: consume twice read 4 on hand, 2 held, 5
  active. Release twice: 10 and 2. Release after consume: 7 and 2.
  Kill 11 in full, the key and the conflict clauses removed: 10 on
  hand, 7 held, 10 active. The late consume's fallback wrote a
  second `expired` receipt and freed R's 3 again.
- **E4,** tidy's condition loosened to five seconds ahead: the guard
  refused the early receipt, and the reserve that ran tidy answered
  `500`. The wall held, so that was no red. With the guard's early
  check removed as well: *the tidy left R alone … expected: null but
  was: "expired"*. SL-1's clock rule, with `OffsetDateTime.now()`
  planted in tidy: *Method `Ledger.tidy(ItemId)` calls method
  `java.time.OffsetDateTime.now()`*.
- **E5,** the consume made to take its amount from an optional body:
  9 on hand and 7 held, where 7 and 5 were owed.
- **E6,** tidy subtracting a sum it read instead of the receipts it
  wrote: *the invariant: Numbers[onHandCount=10, held=10,
  activeSum=16, reservations=13]*. Eleven reserves were admitted
  where five fit, and `item_never_oversold` stayed silent (below).
- **E7,** each rule's violation planted:
  - a receipt writer in the controller: writers
    `[ReservationController.java, Ledger.java]` where only the
    ledger is allowed;
  - a force release in the ledger, in a text block: *one WITH,
    writing the receipts: UPDATE item SET reserved = reserved -
    :units WHERE id = :id*; and in a plain string: *Expecting empty
    but was: ["UPDATE item SET reserved = reserved - :units WHERE id
    = :id"]*;
  - a bare receipt: *an exit moves the item row*;
  - a consume moving by its reservation, not by its receipt:
    *Expecting HashSet ["reservation"] to contain ["receipt"]*.

  Green, as it must be, under a reformat of the consume, in lower
  case and with its `WITH` renamed.
- **The tripwires.** Tidy out of the reserve: *S's 5 and the new 5,
  R's 3 freed … expected 10 but was 8*. Tidy out of the correction:
  *the count the operator asserted … expected 5 but was 10*. The
  `@JsonInclude` on the answer removed: *absent, not null*.
- **The walls' own checks.**
  - The guard dropped from migration V2: five of seven refusals red,
    and the catalog test empty.
  - The key dropped: the second receipt taken.
  - The reference dropped: an ended reservation deleted.
  - Migration V3's three triggers dropped: every refusal red,
    *Expecting code to raise a throwable*, and the two that are
    accepted still green.
  - The trigger on `reservation` alone dropped: the four that add,
    resize, move and delete a hold.
- **The check on the units held, behind the walls.** With migration V3
  standing and E6's naive tidy planted again, the invariant held in
  every reading: `held` equal to `activeSum`, 23 of 23, and 37 of 37.
  The store refused the double frees at commit, 38 and 6 times, each a
  `500` at the door.

All green, unchanged, with every wall standing: three runs at
commit 7, then the full suite again at commit 8 and before this
close. 81 tests.

### What the build found that the specification had not

- **G5's first "if the wall were ever wrong" did not hold.** It
  named `item_never_oversold`. E6's red showed why not. With reserves
  smaller than the units freed twice, the second free and its admit
  keep `reserved` within the count while more is held: 16 actively
  held against 10 on hand, the constraint satisfied throughout. The
  constraint compares two stored numbers. The drift was between a
  stored number and the rows.
- **G6's guard for the numbers read text.** It was signed as the
  structural test alone. Its own commit's review showed it passes
  any writer outside the application. The plan was revised on
  2026-10-05: the store checks the units held at every commit
  (migration V3), and the test stays as the early warning in front
  of it. The same check stands behind G1 and G5. Then, on
  2026-10-07, the test itself was made to read the statements it
  checks, not their spelling: parsed, each exit checked by its
  parts. A third rule keeps every statement where the parser reads
  it.
- **The first red of the build proved nothing, as SL-2's did.** With
  the key removed, the four one-at-a-time tests failed on `500`s: the
  answer's read found two receipts, threw, and rolled the double move
  back. The red proved the test reads status codes. Redone with the
  read taking one row, the reds carried the numbers above. SL-2's
  record had this lesson, but it was not in front of the writer. It
  was then made a shape every test was read against (decisions log,
  2026-10-07).
- **The planned red for E3 could not show one request at a time.** A
  naive check-then-insert is only wrong when two exits interleave,
  which one request at a time never does. That red moved to E1's
  races.
- **Kill 11 needed its full form.** A consume after expiry, alone,
  left the invariant holding even with its walls removed: no one
  else had taken the units. The harm needs a new hold on them first.
  That test was added.
- **A tripwire at the correction.** The plan named only the
  reserve's. Without one at the correction, tidy's call in adjust
  had no red.
- **One branch of the check had no test.** A reservation updated
  (resized, or moved to another item with its units carried) was
  refused by migration V3 and shown by nothing. Two tests were
  added. The move one was rewritten until only the item the hold
  left could refuse it. The check's branch for a deleted receipt
  cannot be reached while the guard stands, because the guard
  refuses the delete first.
- **What stays open past every wall.** `runtime` may update and
  delete reservation rows, which the ledger never does. An open hold
  deleted with its units in one transaction passes the check, since
  the numbers agree, and leaves no receipt. The plan's escape
  hatches signed it as the safe direction. Moving a hold's expiry is
  not refused. Neither can oversell. Making reservations write-once
  was left to the review after this slice, with the question of who
  may write the store at all. Answered on 2026-10-08: the ledger
  alone writes (T4), and E7's fourth rule fails the build if it ever
  rewrites a reservation.
- **R5 against the plan.** R5's criterion says that where this slice
  builds a wall, the naive version lands first, and the wall is its
  own diff. The plan chose otherwise: one migration carrying the
  walls, each red taken on the working tree. The reason: a migration
  written to be wrong would be applied by every store forever. The
  skill allows either, and the criterion stands as signed.

## Standing guards

What could break this slice later, and what watches for it:

- **A second way out.** Any new path that ends a reservation or
  moves an item's numbers (an admin door, a cleanup job, SL-4's
  recovery) re-reads this record's guarantees first. `E7 · G6` fails
  at build time if it writes outside the ledger, hides SQL from the
  parser, or lowers a number without its receipt. Whatever passes it
  meets the check on the units held at commit.
- **A wall dropped "for a while".** `MigrationPathIT` names the
  receipts' key, reference and guard, and the three triggers of the
  check, enabled. A store without them fails before any race could
  pass without them.
- **Tidy forgotten.** A new decision path that skips tidy sees
  expired holds as held: the safe direction, a cost to W3. The two
  tripwires on the choice in "When an expired hold's units are free"
  fail if the reserve or the correction ever loses it. A new path
  has no such tripwire, only this line.
- **The check made faster by seeing less.** A `WHEN` clause, a
  skipped branch, a trigger left off one table: each is a faster
  check that no longer sees one way the numbers can drift.
  `UnitsHeldCheckIT` is what fails. Its cost is real and unmeasured
  (TODO). The fix is an index or an ended mark, never a narrower
  check.
- **Rows written by hand.** Every test or script that writes
  reservations, receipts or the units held must move them together,
  as the ledger does, or the store refuses the transaction. That is
  the check working, not a nuisance to switch off.
- **The reservation rows.** The ledger never changes or deletes
  them, and since 2026-10-08 `E7 · G6` fails the build if it ever
  does. Anyone else writing with its identity is outside, by T4. A
  slice that means to change reservations, such as extending a
  hold, meets that rule first and decides.

## Sign-offs

<!-- Dated lines, the reviewer's: the specification before the plan,
     the plan before the build. -->

- 2026-10-02 — §3's three decisions taken by the reviewer.
- 2026-10-02 — the specification (§1–§7) signed by the reviewer.
- 2026-10-03 — the plan (§8) signed by the reviewer, its four face
  choices taken: a receipt per reservation, tidy first, a trigger
  that refuses, the arithmetic in the application's statement.
- 2026-10-05 — §8 revised by the reviewer: the store checks the
  units held against the open reservations (G6), the arithmetic
  staying in the application.
- 2026-10-07 — the evidence (§9) certified against the delivered
  files, the suite and the run on the real ground; SL-3 closed.
- 2026-10-07 — read against the slice-record shape at the close: §4's
  G6 gains its kills line, none by number; §8's owners for G4 and G6
  gain what stands behind each wall — partly, and nothing. The same
  guarantees and owners; no decision changed.
- 2026-10-08 — the review after this slice. §8, §9 and §10 call E7
  an early warning where they had called it a tripwire: the skill's
  tripwire guards a decided face and can never go red for the
  promise, and E7 goes red when the violation it forbids is planted.
  The escape hatch for an open hold deleted with its units, closed
  for the one writer the definition now trusts (T4), by E7's fourth
  rule. The surface's "no index beyond the key" answered by V4
  (ADR-0015). The cost of the check on the units held, sized now
  that the definition states a size: it assumes up to a few thousand
  reservations on one item over its life, each check reading that
  item's history through the index; revisit when an item's history
  passes that, or a decision's speed is measured. No guarantee,
  owner or evidence changed.
- 2026-10-10 — §9's table gains E7's fourth rule,
  `theLedgerNeverRewritesAReservation`: added on 2026-10-08 and
  named in §8 and §10, but missing from the table. No guarantee,
  owner or evidence changed.
- 2026-10-13 — G3's and G6's claims rewritten to say what happens
  (RETRO.md, D14), each checked against its block and its tests;
  the other four already did, and stand. The guarantees, owners and
  evidence are unchanged.
- 2026-10-14 — written plain, no decision changed: one idea per
  sentence, dash and semicolon chains split, lists as lists. Metaphors
  out ("storm", "held at a line", "ran dry", "births", "worthless",
  "rot", "made blind", "movements", "faces", "the absence rung"). "The
  faces chosen" becomes "The options chosen", as in SL-2. "Concern C"
  becomes SL-3, the definition's name since the letters were retired.
  "Discharges no kill" becomes "never counts as evidence", the
  glossary's words. A glossary line under the title. The text before
  this pass is at 6e20e9c. The guarantees, owners and evidence are
  unchanged.
- 2026-10-14 — read against the definition, SL-1 and SL-2, no
  decision changed. G6 says L2, not P2, makes the transitions out
  of active ours: P2 names the ones into active. The inherited
  admit line cites SL-1 G1 to G3, which hold kills 1 to 5 between
  them. The folds name FC1's third part, checked in G4. The absence
  level names the four tests SL-2 counts. A line that said the test
  shape "now is" read against every test says it was then; since
  2026-10-13 shapes are read at the gate. "The shape the walls
  need" becomes "What the walls need".
- 2026-10-14 — read a third time, no decision changed: the
  adversity names our own retry after an unknown outcome as its own
  line (F19), as the registry and the definition now do.
- 2026-10-14 — read against the slice-record shape at Step 10's
  close: G3's claim split at its semicolon, words unchanged. No
  decision changed.
