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

## §8 Plan — one structural owner per guarantee

<!-- The second movement: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy — database constraint → type system →
     single validated entry path → runtime check → code review →
     hope — the strongest available, justified against the named
     adversity, not in general. -->

### The shape the walls need

One new table, born by this slice's migration, V2:

- **`reservation_exit`** — one row per ended reservation, its
  receipt: `reservation_id` (the key, and a reference to the
  reservation), `kind` (`consumed`, `released` or `expired`),
  `ended_at` (the store's clock). The primary key on
  `reservation_id` is the slice's main wall: **a reservation can
  have one receipt, and a second cannot be written by any path.**
- **One trigger on it,** a function that refuses three things,
  whoever writes: changing or deleting a receipt; an `expired`
  receipt written before the reservation's expiry instant by the
  store's clock; a `consumed` or `released` receipt written at or
  after it. It also sets `ended_at` itself — the expiry instant for
  `expired`, the store's now for the others — so no path supplies a
  time.

Nothing in V1 changes. `item` keeps its three constraints, among
them `item_never_oversold CHECK (reserved <= on_hand_count)`, and
`reserved` keeps its meaning, sharpened: the units of reservations
with no receipt yet.

*Active*, as the witness reads it: no receipt, and `expires_at >
now()`. A reservation past its expiry with no receipt yet is ended
already (§3) — its receipt is written by the next request that
meets it.

### How each path runs

All in one transaction each, on the one entry path (`Ledger`).

- **Tidy** — the first thing reserve and adjust do on an item. One
  statement writes an `expired` receipt for every reservation of the
  item that is past its expiry and has none (`INSERT … ON CONFLICT
  DO NOTHING RETURNING`), and lowers `reserved` by the units of
  exactly the receipts it wrote — not by a sum it read.
- **Reserve and adjust** — tidy, then the statement SL-1 or SL-2
  already proved, unchanged.
- **Consume** — writes a `consumed` receipt for the reservation if
  it has none and has not expired, and in the same statement lowers
  `on_hand_count` and `reserved` by that reservation's units — only
  if the receipt was written. If the reservation has expired with
  no receipt, it writes the `expired` receipt instead and frees the
  units, and the consume is refused. The refused request has still
  written the receipt — the expiry it met had already happened
  (§3), and this records it.
- **Release** — the same, with a `released` receipt, lowering
  `reserved` only.
- **The answer** (ADR-0013): no receipt written, and the one that
  stands is the same kind → `200` with the reservation as
  persisted; any other kind → `409`; no such reservation → `404`.

**One order for every path: receipts first, then the item row.**
Each path writes its receipts before it touches the item, and
several receipts in one fixed order (by identifier). That way no
two requests can each hold one row while waiting for the other's.
If a later change broke the order, the store would end such a wait
by failing one of the two requests with an error — a refused
request, never a wrong number. The order is a matter of liveness
(W3), not of the promise.

### Owners

One block per guarantee: what holds it, why that beats *this*
adversity rather than adversity in general, and what stands behind
it if the wall itself is ever wrong.

**G1. When exits race on one reservation, exactly one takes
effect.**

*The wall:* the primary key on `reservation_exit.reservation_id`.
Every exit — consume, release, and expiry by tidy — ends a
reservation by writing its receipt, and moves numbers only for a
receipt it wrote itself.

*Why it beats this attack:* racing exits are racing inserts of the
same key. The store makes the second wait for the first, then
refuses it: `ON CONFLICT DO NOTHING` returns no row, so the second
moves nothing. Time as a racer is the same race — consume writes
`consumed`, tidy writes `expired`, and the key admits one. Which
kind can be written at that instant is the trigger's, by the
store's clock.
*Say:* consume and release on R together. Both try to write R's
receipt. The consume's lands; the release's returns no row. The
item reads 7 on hand and 5 held.

*If the wall were ever wrong:* the store's check on the units held
(G6, revised 2026-10-05). A release taking R's 3 a second time
lowers `reserved` below what the reservations with no receipt hold,
and the check refuses that transaction at its commit.
`item_never_oversold` alone cannot see it — it compares the two
stored numbers, not the reservations. That is why the key is the
wall, and the check on the units held what stands behind it.

---

**G2. An exit on a reservation that has ended moves nothing.**

*The wall:* the same primary key, for an ended one with a receipt;
and the trigger, for one that has expired with no receipt yet: it
refuses a `consumed` or `released` receipt at or after the expiry
instant.

*Why it beats this attack:* a retried consume (F3) is a second
insert of a key that exists. A late consume (F5) on an expired
reservation whose receipt nobody has written yet is refused by the
store's clock inside the store — not by the application remembering
to compare.
*Say:* R expired at 12:00 and B took its units at 12:05. At 12:10 a
consume on R arrives. Its path writes R's `expired` receipt, and
had it tried `consumed` the trigger would have refused it. The item
still reads 10 on hand, 10 held.

*If the wall were ever wrong:* the statement's own condition
(`expires_at > now()`) stands in front of the trigger.

---

**G3. Nothing ends a reservation by expiry before its expiry
instant.**

*The wall:* the trigger refuses an `expired` receipt while the
store's clock reads before the reservation's expiry, and it sets
`ended_at` itself. Behind it, SL-1's structural test that the
application never reads its own clock, which already covers every
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

---

**G4. An exit moves exactly what its reservation holds.**

*The wall:* the door's shape — the exit paths take no body, so the
type the controller binds has no quantity to carry — and the
statement takes the units and the item from the reservation's own
row, joined to the receipt it just wrote. The receipt's reference to
the reservation means no receipt names a reservation that does not
exist.

*Why it beats this attack:* there is no number in the request for a
caller to get wrong (F7), and no second place the amount could come
from. A reservation that does not exist is found absent before any
write, and answered `404`.
*Say:* consume on R: the statement reads R's 3 from R's row; 10 on
hand becomes 7, 8 held becomes 5.

---

**G5. An expired reservation frees its units once.**

*The wall:* the same primary key, and tidy lowering `reserved` by
the receipts it wrote — its `RETURNING` rows — never by a sum it
read.

*Why it beats this attack:* two tidies meeting the same expired
hold race to insert its receipt; one row is written, and only that
statement subtracts. A tidy that read a sum and subtracted it would
subtract twice, since each reads before the other writes — the
attack exactly.
*Say:* R expired; two reserves for 5 at 12:05. Both tidy; one
writes R's receipt and takes 3 off; the other finds the key taken
and takes nothing. 5 held, one reserve admitted, one refused.

*If the wall were ever wrong:* the store's check on the units held
(G6, revised 2026-10-05). A tidy that subtracted a sum it read
leaves `reserved` below what the open reservations hold, and its
transaction is refused at commit. This block first named
`item_never_oversold` here, and the build showed it does not hold:
with reserves smaller than the units freed twice, the second free
and its admit keep `reserved` within the count while more is held —
seen red as 16 units actively held against 10 on hand.

---

**G6. Every way a reservation ends faces these rules.**

*The wall:* the trigger, which refuses any change or deletion of a
receipt by every identity short of the superuser; and the check on
the units held (revised 2026-10-05): at the commit of every
transaction that touches an item, its reservations or their
receipts, the store compares the item's `reserved` with the units
of its reservations that have no receipt, and refuses the
transaction if they differ. In front of it, a structural test that
`reservation_exit` and `reserved` have one writing path, in the
spirit of SL-2's E5 — a tripwire that fails at build time.

*Why it beats this attack:* the cleanup script of §4 cannot delete
R's receipt — the store refuses it, whoever runs it — so R cannot
look active again, and a second receipt cannot be added either. Nor
can it give back R's units by hand: lowering `reserved` with no
receipt leaves the units held below what the open reservations
hold, and the store refuses it at commit.
*Say:* a script deleting receipts older than a day meets an error on
the first row. A script setting R's 3 free without a receipt meets
"the units held read 5, its reservations with no receipt hold 8".

Every guarantee has one owner, and each owner is the store's: the
key, the triggers, the checks. The paths are the application's, and
the walls behind them are not.

### The faces chosen, and the ones not

Three were put to the reviewer on 2026-10-03 before this plan was
written, and decided: one, two and three below. The fourth was put
to the reviewer with the drafted plan, and decided on 2026-10-03
before the plan was signed.

**G1, G2, G5 — what makes "ends once" impossible to break.**

**A receipt per reservation, one allowed** — *chosen*

*How it holds G1:* a second receipt is a second insert of a key;
the store refuses it whoever sends it.

*Cost:* a second table, and *active* now reads two tables. Ended
reservations keep their receipts forever (W6 sees the growth).

---

**An ended mark on the reservation, set if not yet set**

*How it holds G1:* `UPDATE reservation SET ended … WHERE ended IS
NULL` — the store serializes two updates to one row, and the second
finds it set.

*Why not:* the wall is the statement's condition. Any write that
leaves it out ends a reservation twice and the store does not
object. It defends the path, not the state.

---

**G5 — how expired holds leave the held units (§3: from the
instant).**

**Tidy first, on every decision** — *chosen*

*How it holds G5:* each reserve and adjust writes the receipts of
the item's expired holds before deciding, and frees exactly what it
wrote; the key makes the freeing once.

*Cost:* a little work on every request; `reserved` reads high
between an expiry and the next request on that item, which no
decision sees.

---

**No stored held units: sum the active reservations at each
decision**

*How it holds G5:* nothing to free; expiry is read, not written.

*Why not:* `item_never_oversold` compares two numbers on one row.
Without a stored `reserved` there is nothing for it to compare, and
SL-1's strongest wall — weighed in its §7 against this very face,
"row lock, then compute" — falls to a lock every path must take.
That is SL-1's guarantee moving from structure to sampling.

---

**G6 — whether a receipt can be changed or deleted.**

**A trigger refusing it** — *chosen*

*How it holds G6:* the store refuses `UPDATE` and `DELETE` on
receipts, whoever sends them, short of the superuser.

*Cost:* logic in the store that a reader of the application does not
see; the catalog test names it so its absence is noticed.

---

**Revoke the runtime identity's update and delete on receipts**

*How it holds G6:* the grant system refuses the application itself.

*Why not:* it rewrites the infrastructure contract, whose runtime
may write every table, including every one a future migration
creates (term 4). A trigger holds the same line without touching
the ground.

---

**Code only — one path and the structural test**

*How it holds G6:* nothing in the application writes a second way.

*Why not:* a script outside the application is exactly the attack,
and code does not see it.

---

**G6 — who moves the numbers when a receipt is written.**

**The application's statement** — *chosen*

*How it holds G6:* the same statement that writes the receipt moves
the item's numbers, on the one entry path; the structural test keeps
the path single.

*Cost:* a receipt written by some other path would not move the
numbers. The trigger refuses no insert for that; the structural test
is the guard.

---

**A trigger that moves the numbers whenever a receipt is written**

*How it holds G6:* a receipt and its numbers become one act of the
store; no path could write one without the other.

*Why not:* SL-1 weighed exactly this for `reserved` and kept it as
the first option to revisit if a second write path ever appears
(its §7). None has. It would move the arithmetic the reader needs to
see out of the application; this slice's triggers refuse, they do
not compute.

---

**G6 — what refuses the numbers moving without a receipt** *(added
2026-10-05, during the build)*

**The store checks the units held against the open reservations** —
*chosen 2026-10-05*

*How it holds G6:* a constraint trigger on `item`, `reservation` and
`reservation_exit`, deferred to the commit, compares each touched
item's `reserved` with the units of its reservations that have no
receipt. A number moved without its receipt, a receipt written
without its move, a count of units held set by hand, a hold added or
deleted without its units — each leaves the two unequal, and the
transaction is refused, whoever sends it. Deferred, because an exit
writes its receipt before it moves the item row, and only the
finished transaction is judged.

*Cost:* every commit that touches an item re-reads that item's
reservations, and receipts are kept forever (W6 sees the growth).
Tests and scripts that write rows by hand must keep the two in step,
as the ledger does. The arithmetic stays in the application; the
store only refuses.

---

**The structural test alone** — *as signed on 2026-10-03*

*How it holds G6:* nothing in the application lowers a number
without its receipt.

*Why not, on revision:* it reads the application's SQL as text. A
script outside the application, or a statement assembled at
runtime, passes it — the attack §4 names. Kept as a tripwire in
front of the check.

---

**The exits as functions, and `runtime` losing its direct writes**

*How it holds G6:* the grant system refuses every write but the
functions'.

*Why not:* it rewrites the infrastructure contract's term 4, and
moves SL-1's and SL-2's proven statements into the store with
SL-3's. The check holds the same line for the numbers without
touching the ground.

---

### Escape hatches hunted, afresh

- **A migration writing receipts or rows.** The rule from SL-1
  stands: migrations carry structure, never ledger rows. The key and
  the trigger refuse a second or a late receipt from a migration
  too.
- **The superuser, or `migrator`, disabling the trigger.** Both
  can. The catalog test (`MigrationPathIT`) gains the trigger, the
  key and the reference — and, revised 2026-10-05, the check on the
  units held — so an evidence run on a store without them fails
  before any storm could pass around their absence.
- **Deleting a reservation.** One with a receipt cannot be deleted —
  the receipt's reference stops it. One without a receipt can be
  deleted only in the same transaction as its units leave
  `reserved`; alone, the check on the units held refuses it
  (revised 2026-10-05 — signed as open, the safe direction).
- **`reserved` set by hand.** `item_never_oversold` refuses it above
  the count; the check on the units held refuses any value but the
  units the open reservations hold (revised 2026-10-05 — signed as
  SL-1's drift case, seen only by the witness).
- **A new path that skips tidy.** It sees expired holds as held — the
  safe direction, a W3 cost. The structural test names every writer
  of `reserved`.
- **The operator's way out.** None is added (§3).

### The surface, at its minimum

- **The door:** `POST /reservations/{reservation}/consume` and
  `POST /reservations/{reservation}/release`, no body (ADR-0010,
  ADR-0013). The reservation in a response gains two fields from
  the definition's terms, `endedBy` and `endedAt`, absent while it
  is active.
- **The store:** V2 — the receipts table, its key, its reference,
  its kind check, the trigger. V3 (revised 2026-10-05) — the check on
  the units held, one function and its deferred constraint trigger
  on `item`, `reservation` and `reservation_exit`. No index beyond
  the key: nothing here needs one at the scale of the evidence.
- **The ledger:** `consume` and `release`; tidy, called first by
  `reserve` and `adjust`. One new problem, an unknown reservation
  (`404`); the refusal (`409`) is SL-1's, reused.
- **Under test:** the witness reads *active* with receipts; the
  evidence for E1–E7; the catalog test's new names; the structural
  test's new writer rules; the check on the units held, shown
  refusing directly as `runtime`, and the tests that write rows by
  hand keeping the units held in step.
- **Not added:** a list of reservations (§3, backlog), a sweep, a
  read endpoint, an index.

### Deviations and provisionals, so the close can see them

- **SL-4's invariant, held by structure here.** Consume's two moves
  — the receipt and the count — are one statement in one
  transaction. SL-4's invariant is that no readable state holds one
  without the other. This slice builds that wall but proves nothing
  about it: our death between the moves, and an unknowable outcome,
  are SL-4's adversity. Handed to SL-4 by name at the close.
- **SL-1's and SL-2's records,** which name SL-3 as the slice that
  ends expired holds and pays the conservative-refusal debt, are
  corrected at this slice's close, not before — and V1's comment
  "No ended state yet" stays as written, migrations being history.
- **The guarantees held by structure in the store, not by an
  absence,** this time. The absence rung this run has met twice
  (SL-1's clock, SL-2's ordering) appears once more only as SL-1's
  clock test, reused.

- **§8 revised during the build, 2026-10-05.** G6's guard for the
  numbers was signed as the structural test alone; the build showed
  it reads text, and a writer outside the application passes it. G6
  gains the store's check on the units held, in V3, and the
  structural test stays as a tripwire in front of it. G1's and G5's
  "if the wall were ever wrong" now name the check; G5's first
  answer, `item_never_oversold`, was seen not to hold. The faces
  signed on 2026-10-03 stand — the arithmetic in the application,
  triggers that refuse and never compute.

## §9 Evidence — as delivered

All of it under `./mvnw test`: 81 tests, 0 failures, nothing
exported, the ground not required up — 39 at the branch point. The
witness is read from the store as `runtime`, from outside every
instance, first in every test and sampled while the storms run; the
door's answer is checked after it.

| Criterion | Test | The adversity it creates |
|---|---|---|
| E1 · G1 | `ExitStormIT.racingConsumesEndAReservationOnce` | fifty consumes on R held at a line and released at one instant (F4, kill 13) |
| E1 · G1 | `ExitStormIT.racingConsumesAndReleasesEndAReservationOnce` | twenty-five consumes and twenty-five releases on R, interleaved, at one instant (F4, kill 14) |
| E1 · G1 | `ExitStormIT.racingExitsAcrossInstancesEndAReservationOnce` | sixty exits split across three instances, each its own process (F17) |
| E2 · G1 | `ExitStormIT.consumesAroundTheExpiryInstantEndEachHoldOnce` | forty one-second holds, each consumed at its own expiry instant ± 0.1 s (F23, kill 14) |
| E2 · G1 | `ExitStormIT.consumesAndTidiesAroundTheExpiryInstantEndEachHoldOnce` | the same, with a reserve beside each consume whose tidy meets the same hold |
| E3 · G2 | `ExitDoorIT.theSameConsumeTwiceMovesOnce`, `theSameReleaseTwiceMovesOnce` | the same exit sent twice at the same door (F3, kill 12, 16's retry half) |
| E3 · G2 | `ExitDoorIT.aReleaseAfterAConsumeIsRefusedAndMovesNothing`, `aConsumeAfterAReleaseIsRefusedAndMovesNothing` | a different exit after the first (F5) |
| E3 · G2 | `ExitDoorIT.aConsumeAfterExpiryIsRefusedAndTheExpiryRecorded` | a consume after R's hold ran out (F5) |
| E3 · G2 | `ExitDoorIT.aLateConsumeIsRefusedAfterANewHoldTookItsUnits` | kill 11 in full: R expires, T takes its units, R's consume arrives late |
| E4 · G3 | `ExitDoorIT.aHoldStillAheadByTheStoresClockIsConsumable` | a tidy meeting R with two of its three seconds left (F24) |
| E4 · G3 | `NoInstanceStateOrClockTest.theLedgerConsultsNoProcessClock` | SL-1's rule, the code read: no process clock anywhere, tidy and the exits included |
| E5 · G4 | `ExitDoorIT.aConsumeMovesExactlyItsReservationsUnits`, `aReleaseFreesExactlyItsReservationsUnits` | R's 3 consumed, and released, beside S's 5 |
| E5 · G4 | `ExitDoorIT.anExitTakesNoAmountFromTheRequest` | a consume sent with a quantity in its body (F7, FC2) |
| E5 · G4 | `ExitDoorIT.anExitOnAnUnknownReservationIsUnknownAndMovesNothing` | an exit naming a reservation that does not exist (F6) |
| E6 · G5 | `ExitStormIT.racingReservesFreeAnExpiredHoldOnce` | fifty reserves at one instant, each one's tidy meeting R just expired |
| E7 · G6 | `NoSecondWayOutTest.receiptsAndTheUnitsHeldAreWrittenByTheLedgerAlone` | the code read: no writer of receipts or of `reserved` outside the ledger |
| E7 · G6 | `NoSecondWayOutTest.theLedgersSqlIsWrittenAsTextBlocks` | the code read: no SQL in the ledger the next rule would not see |
| E7 · G6 | `NoSecondWayOutTest.numbersFallOnlyByTheReceiptsTheSameStatementWrote` | the code parsed: every exit's statement by its parts |

Beside them, and not evidence, each saying so on itself:
`ExitDoorIT.aReserveAfterTheInstantFindsTheExpiredUnitsFree` and
`aCorrectionAfterTheInstantFindsTheExpiredUnitsFree`, tripwires on
§3's choice; `anActiveReservationCarriesNoEnding`, a tripwire on the
answer's shape; `anExitNamingNoReservationIsInvalid`, SL-1's nonsense
rule reaching the new doors. And the walls' own checks, straight
against the store as `runtime`: `ReceiptGuardIT` (seven, the guard,
the key and the reference refusing, and the store setting the ending
instant), `UnitsHeldCheckIT` (nine, the check on the units held
refusing and taking), and `MigrationPathIT`'s catalog
tests naming the key, the reference, the guard and the three
triggers.

SL-1's and SL-2's evidence ran green unchanged beside all of it,
with tidy in the reserve and the correction.

**On the real ground,** 2026-10-07: rows read first, none out of
step; the ground dumped; V2 and V3 applied by Flyway as `migrator`;
the walls read from its catalog. Through the door: R's 3 consumed (7
on hand, 5 held), the same consume again `200` and nothing moved, a
release after it `409`, S released (0 held), an unknown reservation
`404`, a one-second hold run out and its 7 units taken by the next
reserve, its late consume `409`. As `runtime`, a script lowering the
units held met "the units held read 0, its reservations with no
receipt hold 7", and one deleting a receipt met "a receipt is never
deleted".

### Red before green, from actual output

Every wall was made absent on the working tree for one run and
restored; none landed in history. Where the wall is a rule over the
code, the violation it forbids was planted.

- **E1, the key and every `ON CONFLICT` removed,** the exit reading
  its receipt by a naive check then insert: fifty consumes read
  `onHandCount=4, held=2, activeSum=5` — R's 3 off the shelf twice,
  the invariant broken; mixed, 7 on hand, 2 held, 5 active; across
  three instances, 4, 2, 5.
- **E2,** with the guard's late check and the consume's
  `expires_at > now()` both removed: every one of forty holds
  consumed, twenty after their instant by the store's clock.
- **E2 with tidy,** the key removed: red three times out of three
  (81 on hand, 36 held, 40 active, and the like), but every double
  ending was `[expired, expired]`. The interleaving the test is named
  for — a consume uncommitted across the instant — is a window of a
  few milliseconds; held open on the red tree only, by a 50 ms pause
  in the consume's transaction, it showed `[consumed, expired]`
  twice out of two.
- **E3,** the key removed: consume twice read 4 on hand, 2 held, 5
  active; release twice 10 and 2; release after consume 7 and 2.
  Kill 11 in full, the key and the conflict clauses removed: 10 on
  hand, 7 held, 10 active — the late consume's fallback wrote a
  second `expired` receipt and freed R's 3 again.
- **E4,** tidy's condition loosened to five seconds ahead: the guard
  refused the early receipt, and the reserve that ran tidy answered
  `500` — the wall held, so that was no red. With the guard's early
  check removed as well: *the tidy left R alone … expected: null but
  was: "expired"*. SL-1's clock rule, `OffsetDateTime.now()` planted
  in tidy: *Method `Ledger.tidy(ItemId)` calls method
  `java.time.OffsetDateTime.now()`*.
- **E5,** the consume made to take its amount from an optional body:
  9 on hand and 7 held, where 7 and 5 were owed.
- **E6,** tidy subtracting a sum it read rather than the receipts it
  wrote: *the invariant: Numbers[onHandCount=10, held=10,
  activeSum=16, reservations=13]* — eleven reserves admitted where
  five fit, and `item_never_oversold` silent (below).
- **E7,** each rule's violation planted. A receipt writer in the
  controller: writers `[ReservationController.java, Ledger.java]`
  where only the ledger is allowed. A force release in the ledger,
  in a text block: *one WITH, writing the receipts: UPDATE item SET
  reserved = reserved - :units WHERE id = :id*; in a plain string:
  *Expecting empty but was: ["UPDATE item SET reserved = reserved -
  :units WHERE id = :id"]*. A bare receipt: *an exit moves the item
  row*. A consume moving by its reservation, not by its receipt:
  *Expecting HashSet ["reservation"] to contain ["receipt"]*. Green,
  as it must be, under a reformat of the consume, lower case and its
  `WITH` renamed.
- **The tripwires.** Tidy out of the reserve: *S's 5 and the new 5,
  R's 3 freed … expected 10 but was 8*. Tidy out of the correction:
  *the count the operator asserted … expected 5 but was 10*. The
  `@JsonInclude` on the answer removed: *absent, not null*.
- **The walls' own checks.** The guard dropped from V2: five of
  seven refusals red, and the catalog test empty; the key dropped:
  the second receipt taken; the reference dropped: an ended
  reservation deleted. V3's three triggers dropped: every refusal
  red, *Expecting code to raise a throwable*, the two that are
  taken still green; the trigger on `reservation` alone dropped:
  the four that add, resize, move and delete a hold.
- **The check on the units held, behind the walls.** With V3
  standing and E6's naive tidy planted again, the invariant held in
  every reading — `held` equal to `activeSum`, 23 of 23 and 37 of 37
  — the store refusing the double frees at commit, 38 and 6 times,
  each a `500` at the door.

All green, unchanged, with every wall standing: three runs at
commit 7, the full suite again at commit 8 and before this close —
81 tests.

### What the build found that the specification had not

- **G5's first "if the wall were ever wrong" did not hold.** It named
  `item_never_oversold`. E6's red showed why not: with reserves
  smaller than the units freed twice, the second free and its admit
  keep `reserved` within the count while more is held — 16 actively
  held against 10 on hand, the constraint satisfied throughout. The
  constraint compares two stored numbers; the drift was between a
  stored number and the rows.
- **G6's guard for the numbers read text.** Signed as the structural
  test alone, it was shown at its own commit's review to pass any
  writer outside the application. §8 was revised on 2026-10-05: the
  store checks the units held at every commit (V3), and the test
  stays as the tripwire in front of it. The same check stands behind
  G1 and G5. Then, on 2026-10-07, the test itself was made to read
  the statements it checks rather than their spelling — parsed, each
  exit checked by its parts — and a third rule keeps every statement
  where the parser reads it.
- **The first red of the build was worthless, as SL-2's was.** The
  key removed, the four sequential tests failed — on `500`s: the
  answer's read found two receipts, threw, and rolled the double move
  back. The red proved the test reads status codes. Redone with the
  read taking one row, the reds carried the numbers above. SL-2's
  record had this lesson; it was not in front of the writer. It now
  is, as a shape exposed to every test (decisions log, 2026-10-07).
- **The planned red for E3 could not show sequentially.** A naive
  check-then-insert is only wrong when two exits interleave, which
  one request at a time never does; that red moved to E1's storms.
- **Kill 11 needed its full form.** A consume after expiry, alone,
  left the invariant holding even with its walls removed — no one
  else had taken the units. The harm needs a new hold on them first;
  that test was added.
- **A tripwire at the correction.** The plan named the reserve's
  only; without one at the correction, tidy's call in adjust had no
  red.
- **One branch of the check had no test.** A reservation updated —
  resized, or moved to another item with its units carried — was
  refused by V3 and shown by nothing; two tests were added, the move
  one rewritten until only the item the hold left could refuse it.
  The check's branch for a deleted receipt cannot be reached while
  the guard stands, which refuses the delete first.
- **What stays open past every wall.** `runtime` may update and
  delete reservation rows, which the ledger never does. An open hold
  deleted with its units in one transaction passes the check — the
  numbers agree — and leaves no receipt; §8's escape hatches signed
  it as the safe direction. Moving a hold's expiry is not refused.
  Neither can oversell. Making reservations write-once is a question
  for the review after this slice, with who may write the store at
  all (TODO).
- **§5 against the plan.** §5 says that where this slice births a
  wall, the naive version lands first and the wall is its own diff.
  The plan chose otherwise — one migration carrying the walls, each
  red taken on the working tree — because a migration written to be
  wrong would be applied by every store forever. The skill allows
  either; §5 stands as signed.

## §10 Standing guards

What would rot this slice, and what watches:

- **A second way out.** Any new path that ends a reservation or
  moves an item's numbers — an admin door, a cleanup job, SL-4's
  recovery — re-reads §4 first. `E7 · G6` fails at build time if it
  writes outside the ledger, hides SQL from the parser, or lowers a
  number without its receipt; whatever passes it meets the check on
  the units held at commit.
- **A wall dropped "for a while".** `MigrationPathIT` names the
  receipts' key, reference and guard, and the three triggers of the
  check, enabled; a store without them fails before any storm could
  pass around their absence.
- **Tidy forgotten.** A new decision path that skips tidy sees
  expired holds as held — the safe direction, a cost to W3. The two
  tripwires on §3's choice fail if the reserve or the correction
  ever loses it; a new path has no such tripwire, only this line.
- **The check made cheap by being made blind.** A `WHEN` clause, a
  skipped branch, a trigger left off one table — each a faster check
  that no longer sees one way the numbers can drift. `UnitsHeldCheckIT`
  is what fails. Its cost is real and unmeasured (TODO); the fix is
  an index or an ended mark, never a narrower check.
- **Rows written by hand.** Every test or script that writes
  reservations, receipts or the units held must move them together,
  as the ledger does, or the store refuses the transaction. That is
  the check working, not a nuisance to switch off.
- **The reservation rows.** `runtime` may still change or delete
  them; the review after this slice decides whether they become
  write-once (TODO).

## §11 Sign-offs

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
