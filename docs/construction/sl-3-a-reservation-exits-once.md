# SL-3 — a reservation exits once

<!-- The slice's record, grown in three movements: the correctness
     specification (what must hold, against what, and what proof
     looks like — no mechanism), then the plan (one structural owner
     per guarantee, and why it beats the named adversity), then the
     evidence as delivered (which test creates which adversity, and
     what it read from the store). Invariant and adversity are the
     registry's and the definition's, as written; guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each movement.

     §3 is this slice's own section: three things the framing does
     not decide, decided before the specification that depends on
     them. -->

## §1 Invariant

A reservation moves its item's numbers at most once on exit, and
never after it has ended. (`docs/system/registry.md`, SL-3; concern
C in the definition's L4.)

The words, as the truth set has them:

- **An exit** is a way out of active. There are three: **consume**
  (the units were sold — the reservation ends and its units leave
  the on-hand-count), **release** (the caller gave up — the
  reservation ends and its units are free again), and **expiry**
  (the hold ran out — the same as release, with time as the one
  who acts). L2 names release and expiry; P4 names consume.
- **Ended** is FC3's, as SL-1 consumed it: ended by an exit, or
  past its expiry as judged by one clock, not one per instance.
- **The item's numbers** are the on-hand-count and the units held
  by its reservations, the two the promise compares.

## §2 Adversity

Exits that repeat, race, or arrive too late, and a clock that ends
a hold too soon. Pulled from the definition's L1 as written:

- a caller resends consume after a lost reply — the on-hand-count
  lowered twice for one reservation (F3, kill 12, and kill 16's
  retry half);
- a caller sends consume and release for one reservation at the
  same instant, or two consumes — two exits racing on one
  reservation, each moving numbers (F4, kill 13);
- expiry ends a reservation while a consume for it is in flight —
  two exits racing, time being one partner (F23, kill 14);
- a caller consumes or releases a reservation that already ended —
  expired, consumed, released — including very late (F5, kill 11);
- time jumps, and expiry fires early — holds end before their time,
  and late consumes follow (F24, kill 19's early half).

Kills covered: 11, 12, 13, 14, 16 (our retry), 19 (early). FC2 is
folded in: an exit names its own reservation and moves exactly what
it holds (kill 17).

What this slice does not face: our own death between consume's two
moves, and our silence after an unknowable outcome (kills 15 and
16's silence half — SL-4); who may exit a reservation (kill 17's
identity part — W5, T3); holds that never expire (kill 19's other
half — V5); a retried reserve (kill 20 — W2).

## §3 Decided before the specification

Three questions the framing leaves open, found at this slice's
opening (PLAN, Step 7) and decided by the reviewer on 2026-10-02,
each with its options.

### What a repeated exit answers — ADR-0013

The paths are ADR-0010's, and an exit carries no body: it names its
reservation and moves exactly what that holds (FC2). What the
framing does not say is what a repeat answers. The numbers are the
invariant's and move once either way; the reply is the door's.

**Chosen:** an exit repeated on a reservation that the same exit
already ended answers `200 OK`, the reservation as persisted. A
different exit on an ended reservation is still refused, `409`.
**Rejected:** `409` for every exit on an ended reservation — true,
but the caller who lost the first reply reads an error for a
consume that worked. The options and reasons in full are ADR-0013's.

### When an expired hold's units are free

FC3 already says a reservation past its expiry, by the store's
clock, is no longer active, and F5 says a consume after that is a
request against a reservation with no active state left. What the
framing does not say is when the units it held become free for
the next decision.

Today they never do. SL-1 left the held units counting a
reservation until an exit ends it (SL-1 §7), and nothing ends one
by expiry.
*Say:* 10 on hand. A holds 3 until 12:00 and walks away. At 12:05
B asks for 8. The held units still read 3, so only 7 look free and
B is refused — at 12:05, and at every time after.

**Chosen: free from the expiry instant.** A reservation is ended
the moment its expiry passes, by the store's clock. Nothing has to
happen for that: no request, no sweep. Every decision taken after
the instant — a reserve, a correction — sees its units as free.
*Say:* at 12:05 B's 8 is admitted against 10 on hand and 0 held.

**Rejected: free eventually, within a bound.** Something ends
expired holds now and then; until it runs, they still block units.
It is safe — the invariant holds either way — but it holds more
than it must, and the something that runs is a new moving part
that can die or fall behind. Those are adversities this slice does
not name.

**What the choice pays.** Two debts handed here by name: SL-1's
held units over-counting expired holds (SL-1 §7, "Deviations and
provisionals"), and SL-2's refusals being conservative against
holds the clock has already ended (SL-2 §7, "That the sum is
minimal").

**What it costs.** The reserve and the correction are SL-1's and
SL-2's decisions, proved on their own evidence. This choice changes
what they read. Both slices' evidence must stay green, unchanged —
the standing guard SL-1 §9 names for any later slice touching its
state.

Changes no export: FC3 already defines *active* this way, and V5
already counts on expiry as the exit for abandoned holds.

### Whether an operator can end reservations

SL-2 handed this here (SL-2 §3 and §7). An operator counts 5 on the
shelf while 8 are held; the correction to 5 is refused; they can
only wait for expiry or for callers to release.

**Chosen: no operator-side exit in this slice.** The release at the
door does not ask who is calling — the definition trusts a
request's stated identity (T3) and leaves who may act outside (W5).
So an operator who knows a reservation's identifier can release it
like anyone else. What an operator cannot do is find those
identifiers: there is no list of an item's reservations. A list is
a feature no guarantee here needs, so it is not built; it waits in
the backlog.

**Rejected: an exit for the operator** — for example, a correction
that ends enough holds to fit. Something would have to choose which
holds end: the oldest, the largest, the latest. SL-2 §3 rejected
that for the reason that stands here too: it is a policy no
possession grants. The system was never given a rule for choosing.

**What the choice costs.** The operator in SL-2's example still has
no practical recourse. The promise holds; the seller can still sell
units that are not on the shelf, until the holds end. This is W1's
remainder, and it stays fenced.

## §4 Guarantees — derived by attack

Each is one answer to: *what would let §1 hold on paper yet break
in fact?* The attack runs until no new answer comes. Every
guarantee is a property; none names how it is held.

The numbers in the examples start from one item unless they say
otherwise: 10 on hand; reservation R holds 3, reservation S holds
5, so 8 are held and 2 are free.

**G1. When exits race on one reservation, exactly one of them
takes effect.**

*What could go wrong (the attack):* two exits on R arrive at the
same instant — a consume and a release, or two consumes (F4), or a
consume as R's expiry passes (F23). Each checks that R is still
active, each sees that it is, and each moves the numbers.
*Say:* consume and release on R together. The consume takes R's 3
off the shelf: 7 on hand. The release also frees R's 3, so the
held units drop twice: 8 − 3 − 3 = 2. Only S is left, holding 5,
but the item now reads 2 held of 7, so 5 more units look free.
Five more are admitted, and 10 units are held against 7 on the
shelf.

*What we guarantee:* whether an exit is R's first is decided
against R's state at the moment the exit is recorded, never
against a copy read earlier. Of any exits that race on R, one
ends it, and the rest find it ended. Time is one of the racers: a
consume recorded before R's expiry instant ends R as consumed; one
recorded after finds R already ended by expiry. Never both.
*Say:* consume and release on R together. One of them ends R. If
it is the consume, the item reads 7 on hand and 5 held; if the
release, 10 on hand and 5 held. Either fits.

*Kills:* 13, 14.

---

**G2. An exit on a reservation that has ended moves nothing.**

*What could go wrong (the attack):* the consume is retried after a
lost reply (F3), or arrives long after R ended — expired, released,
consumed — and its units have since gone to someone else (F5). The
exit acts as if R were still active.
*Say:* R expired at 12:00; at 12:05 B reserved 5, so 10 are held
(S's 5 and B's 5) against 10 on hand. At 12:10 a late consume on R
arrives and takes 3 off the shelf: 7 on hand, 10 held. Oversold
by 3.

*What we guarantee:* once R has ended, by any exit, no later exit
on R moves either number, however long after and however many
times. What the reply says is ADR-0013's: the same exit repeated
reads as the first, a different one is refused. The numbers are
this guarantee's, and they do not move either way.
*Say:* the late consume finds R ended by expiry and is refused.
The item still reads 10 on hand, 10 held.

*Kills:* 11, 12, 16 (our retry).

---

**G3. Nothing ends a reservation by expiry before its expiry
instant, as the one clock reads it.**

*What could go wrong (the attack):* time jumps (F24), and holds end
before their time. A late consume then follows on a reservation
that should still have been active (kill 11).
*Say:* R holds until 12:15. One of our instances has a clock that
jumped forward and reads 12:20. If that instance's clock decided,
R would be ended at what is really 12:10, and the caller's consume
at 12:11 would be refused for a hold they were promised until
12:15.

*What we guarantee:* a reservation ends by expiry when the one
clock — the store's, SL-1's G5 — passes its expiry instant, and
not before. No instance's own clock decides it, and nothing ends
a hold ahead of that instant in bulk or by rounding.
*Say:* at 12:11 by the store's clock R is active, whatever any
instance's clock reads, and the consume ends it.

*Kills:* 19 (early).

---

**G4. An exit moves exactly what its reservation holds, on its
reservation's item.**

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

---

**G5. An expired reservation frees its units once, however many
decisions meet it.**

*What could go wrong (the attack):* expiry has no request of its
own (§3 — its units are free from the instant, nothing has to
happen). So whoever meets an expired hold first takes it out of the
count — and two decisions meeting it at the same instant each take
it out.
*Say:* R expired at 12:00. At 12:05 two reserves for 5 arrive
together. Each sees R expired and frees its 3: 8 − 3 − 3 = 2 held,
8 free. Both reserves are admitted, and 15 are held against 10 on
hand. Really held: S's 5 and the two new 5s.

*What we guarantee:* an expiry is an exit like the others. It
moves the numbers once, whichever decision meets it first and
however many meet it together, and the units it frees are freed
for every decision after its instant.
*Say:* the two reserves at 12:05 see 5 held and 5 free. One is
admitted and one is refused; the item reads 10 held of 10.

*Kills:* none by number. This is the attack §3's choice opens:
expiry acting with no request of its own. What dies if it lands is
kill 1's death — more held than on hand.

---

**G6. Every way a reservation ends faces these rules.**

*What could go wrong (the attack):* a second way in — a script, a
migration, an admin path, a later feature — ends a reservation, or
moves the numbers for one, without meeting G1 to G5.
*Say:* a cleanup script deletes reservations that look old and
gives back their units. It deletes R, which a consume ended a
minute ago, and takes R's 3 off the held units a second time.

*What we guarantee:* no path ends a reservation or moves the
numbers for an exit without facing the same rules, including paths
this slice never anticipated. P2 makes the transitions out of
active ours; this keeps them in one place.

---

**Inherited, not re-owned.** The attack's answers that SL-1 and
SL-2 already hold are named, not re-derived. An admit is one act
against the truth at the moment of recording, and racing admits do
not interleave (SL-1's G1, kills 1–5). Activeness is judged by one
clock, the store's (SL-1's G5, FC3), which G3 relies on. A
correction never leaves the count under the sum (SL-2's G1–G6).

§3's choice that expired units are free from the instant changes
what the reserve and the correction read. Their guarantees are not
re-owned here, but their evidence must stay green unchanged — the
guard SL-1 §9 names for any later slice touching its state.

**The attack ran dry at six.** A seventh answer — "we die between
consume's two moves" — is SL-4's (kill 15). An eighth — "the caller
is not told whether theirs was the first exit" — is the caller's
view, refused at L2, and ADR-0013 decides what the reply says. A
ninth — "someone exits a reservation that is not theirs" — is W5,
with identity trusted at the door (T3). A tenth — "a hold that
never expires" — is V5.

## §5 Evidence criteria

Each guarantee's evidence *creates* its adversity through the real
door and reads the witness from the store, from outside the
process. The witness is §1's, read from persisted state, for the
item and for each reservation on it: the on-hand-count; the units
held, and the sum of active reservations as the store's clock
judges them; and how each reservation ended, if it has. From these
the evidence checks two things: the on-hand-count has fallen by
exactly the units of the reservations that ended by consume, once
each; and no more is held than is on hand.

- **E1 (G1).** A storm on one reservation: many exits held at a
  line and released at one instant — consumes only; consumes and
  releases mixed — through the real door, and across more than one
  of our instances (F17, the harness's forked instances). After:
  the reservation ended exactly once, by one exit kind, and the
  witness agrees with that kind and no other. Replies are not
  counted: under ADR-0013 a repeated exit of the same kind also
  reads `200`, so only the store can say how many took effect.
- **E2 (G1, with time).** Many reservations with the shortest hold
  the door allows, and a consume for each fired on both sides of
  the expiry instant. After: each reservation ended by consume or
  by expiry, never both; the on-hand-count fell by exactly the
  consumed ones' units; nothing held beyond what is on hand.
- **E3 (G2).** Sequential, the same bytes at the same door. A
  consume, then the same consume again: the second moves nothing.
  A release after a consume, and a consume after a release: refused,
  nothing moved. The full form of kill 11: a reservation expires,
  its units go to a new reserve, and a late consume on it arrives —
  refused, the count unchanged, the new hold intact.
- **E4 (G3).** A hold whose expiry is still ahead by the store's
  clock is consumable, and a structural check reads the code rather
  than the runtime: no exit path, and nothing that ends a
  reservation, consults a clock other than the store's — in the
  spirit of SL-1's no-process-clock check, and seen red by planting
  the violation it forbids.
- **E5 (G4).** A consume on a reservation of 3 lowers the count by
  3 and the units held by 3; a release frees exactly 3; an exit
  naming a reservation that does not exist is answered unknown and
  nothing moves.
- **E6 (G5).** A storm of reserves on an item whose units are
  partly held by a reservation that has just expired. After, and
  sampled during: never more held than on hand, and the expired
  hold's units freed exactly once. Beside it, a tripwire for §3's
  choice: after the instant, a reserve that fits only if the
  expired units are free is admitted. That guards the decided face
  — free from the instant — not the promise, since holding more
  than needed cannot oversell; it says so on itself and discharges
  no kill.
- **E7 (G6).** A structural check, reading the code: reservations
  end, and their numbers move on exit, by one path only, and a
  second one introduced anywhere fails the check — in the spirit of
  SL-2's E5.
- **SL-1's and SL-2's evidence, unchanged and green** — the reserve
  and the correction now read expired holds as free (§3), and their
  guarantees must survive it.
- **The harness can fail (R5).** Each of E1–E7 is run with its wall
  absent — a state that never lands in history — and seen red,
  recorded from actual output; then green, unchanged, with the wall
  standing. Where this slice births the wall, the naive version
  lands first and the wall is its own diff.

All of it under the one standard test command, nothing exported,
the ground not required up.

## §6 Folds and flags — the written zeros

**Folds: one, FC2** — an exit names its own reservation and moves
exactly what it holds. Consumed by G4, its evidence E5. The other
two folds are SL-1's, FC1 and FC3; this slice relies on FC3 for G3.

**Flags: none.** SL-3's row carries none, and it is worth saying
why. Every adversity here stages the normal way: the same request
sent twice, exits held at a line and released together, holds that
run out by the store's clock while the evidence waits. The one
that looks like it should be flagged is the early expiry of kill
19, since a time jump is not something hammering makes. It needs
no flag because FC3 already removed it for instances, the way
SL-1's flag on kill 10 was removed: no instance's clock decides,
so an instance's jump ends nothing, and E4 shows it. What FC3 does
not reach is said in §7.

## §7 What this slice does not claim

- **That a jump of the one clock is caught.** The store's clock is
  the machine's (the runtime ground: one machine, one clock). If
  that clock itself is stepped forward — a correction, a resume
  from pause — holds end early in real time, though on time by the
  clock. The ledger has no second clock to notice with, and FC3
  chose one. Telling a stepped clock from time that really passed
  is the question the definition leaves unprobed, "monotonic vs
  wall". G3 holds against every clock but that one.
- **That consume's two moves hold together if we die between
  them,** or after an outcome we cannot know. SL-4 — kills 15 and
  16's silence half.
- **That a reply tells a first exit from a repeat.** ADR-0013's
  cost, and the caller's view refused at L2.
- **That an operator can end holds.** §3; W1's remainder stands.
- **Who may exit a reservation.** W5, T3 — identity is trusted at
  the door.
- **That every hold ends.** A hold lasts as long as the caller
  asked, up to the door's bound; that it ends at all is V5's.
- **How long ended reservations are kept.** W6.
