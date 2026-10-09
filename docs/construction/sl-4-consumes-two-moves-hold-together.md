# SL-4 — consume's two moves hold together

<!-- The slice's record, grown in three movements: the correctness
     specification (what must hold, against what, and what proof
     looks like — no mechanism), then the plan (one structural owner
     per guarantee, and why it beats the named adversity), then the
     evidence as delivered (which test creates which adversity, and
     what it read from the store). Invariant and adversity are the
     registry's and the definition's, as written; guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each movement.

     §3 is this slice's own section: two things the framing does
     not decide, decided before the specification that depends on
     them. -->

## §1 Invariant

No readable state holds one of consume's two moves — ending the
reservation, lowering the on-hand-count — without the other.
(`docs/system/registry.md`, SL-4; concern D in the definition's L4.)

The words, as the truth set has them:

- **Consume** is P4's: ending a reservation and lowering the
  on-hand-count together, once per reservation — the one act that
  moves both numbers. The "once" is SL-3's, closed; this slice
  owns the "together".
- **The two moves.** One ends the reservation, so it no longer
  counts in the units held. The other lowers the on-hand-count by
  what the reservation held.
- **Readable** is the intent's: true in every state readable from
  outside, not eventually.

*Say:* an item has 10 on hand, and reservation R holds 3 of them.
After a consume of R, the item reads 7 on hand with R ended. Before
it, 10 on hand with R holding 3. Nothing else may ever be read:
not 7 on hand with R still holding 3, and not 10 on hand with R
ended.

## §2 Adversity

Our own failure in the middle of a consume. Pulled from the
definition's L1 as written:

- we die between consume's two moves — one move done: the count
  lowered while the reservation still counts, or the reservation
  ended while consumed units still count (F16, kill 15);
- a write's outcome is unknowable — a timeout after sending — and
  we fall silent: the consume neither confirmed nor redone, so
  whatever it left is what stays (F19, kill 16's silence half).

Kills covered: 15, 16 (our silence).

The registry's flag, carried here: the evidence is kill-mid-work and
unknown-outcome injection, not hammering. No race is created; the
adversity is one consume, interrupted.

What this slice does not face: our own retry after an unknown
outcome (kill 16's retry half — SL-3, closed: a repeated consume
moves nothing twice); a reserve whose outcome is unknown (F19 × P2
— the caller's view refused at L2, the possible orphan W2 and V5);
how long anyone waits while a consume is interrupted (W3).

## §3 Decided before the specification

Two questions the framing leaves open, found at this slice's
opening (PLAN, Step 8) and decided by the reviewer on 2026-10-09,
each with its options.

### What a caller is told when the outcome is unknown — ADR-0016

When an instance loses the store mid-consume, the consume may have
committed or not, and the instance cannot tell. The framing refuses
the caller's view (L2), so what the caller reads is the door's
convention, not the promise. Today nothing at the door handles a
lost store, and the framework's default error reads as "failed".

*Say:* 10 on hand, R holding 3. The caller consumes R. The store
commits — 7 on hand, R consumed — and the connection breaks before
the instance hears. Today the caller reads an error, and believes R
is still open.

**Chosen:** the instance answers `503`, Problem Details, "outcome
unknown": the request may or may not have taken effect. The caller
sends the consume again, and ADR-0013 makes that safe — `200` if
the first landed, the consume done now if it did not, refused if
another exit ended R meanwhile.
*Say:* the caller reads `503`, sends consume R again, and reads
`200` with R consumed. The item still reads 7 on hand: one consume,
not two.

**Rejected:** leaving the default error (it says "failed" for a
consume that may have landed); `500` (it would read the same as a
defect in the ledger); and a limit on how long an instance waits
for the store (the numbers are right however long the wait; a limit
brings the unknown sooner but does not remove it; and the number is
a policy the framing never gives — W3). The options and reasons in
full are ADR-0016's.

### Whether anything is left to converge

The registry kept SL-4 apart from SL-3 because the proof
obligations differ: duplicates collapse there, and here "the
half-done converges". That was written at framing, before any
consume existed, when the two moves could have been two writes,
with a half-done state left for something to repair.

SL-3 built consume as one write: the reservation's ending and the
count's fall are committed together, or not at all (SL-3 §8). An
interrupted consume leaves the store as it was before, or as it is
after — never between.

**Chosen: nothing converges, because nothing is half-done.** The
spec says so, and the evidence shows it: an interrupted consume
reads as both moves or neither. Convergence is met at its strongest
— there is never anything to converge from. The registry's
judgment still stands, because the evidence differs from SL-3's:
an interruption, not duplicates.

**Named, outside the promise: the wait an interrupted consume can
leave.** A consume whose instance is cut off from the store — not
killed, cut off — can stay open at the store until the store gives
up on that connection. Until then, other decisions on that item
wait. None reads a wrong number; they only wait, and how long is
W3's, fenced.
*Say:* an instance has run its consume of R on item X, and loses
its network to the store before its commit arrives. A reserve on
item X from another instance waits. When the store gives up on the lost connection, it undoes
the open consume — 10 on hand, R holding 3 — and the reserve
decides against those numbers.

What the decision asks of the evidence: after the interruption, the
next decision on the same item goes through, against the numbers
the witness reads. Nothing is stuck.

**Rejected: limiting that wait in this slice.** It would be a
setting on the store, a change to the ground, made for no
guarantee. Our ground cannot readily stage it either: the long wait
needs a network cut while the instance lives on, and a killed
instance's connection is closed at once. Recorded in the backlog
as a known issue, with when to revisit.

## §4 Guarantees — derived by attack

Each is one answer to: *what would let §1 hold on paper yet break
in fact?* The attack runs until no new answer comes. Every
guarantee is a property; none names how it is held.

The numbers in the examples start from one item unless they say
otherwise: 10 on hand; reservation R holds 3, reservation S holds
5, so 8 are held and 2 are free. A consume of R should leave 7 on
hand, 5 held.

**G1. A consume's two moves become final together, or neither
does.**

*What could go wrong (the attack):* the two moves are made final
one after the other, and we die in between (F16).
*Say:* R's ending is final, and we die before the count falls.
The item reads 10 on hand and 5 held, so 5 look free — but 3 of
the 10 were sold and have left the shelf. Five more are admitted:
10 held against the 7 really there. Or the other way round: the
count falls to 7 and we die before R ends. R still holds 3 and has
no ending, so the caller's retry consumes it again: 4 on hand, one
hold's units taken off the shelf twice.

*What we guarantee:* there is no moment at which one of a consume's
moves is final and the other is not. However the consume is
interrupted, and wherever, what remains is both moves or neither.
*Say:* we die mid-consume. The item reads either 10 on hand, R
holding 3, 8 held — or 7 on hand, R ended by consume, 5 held.
Nothing else.

*Kills:* 15.

---

**G2. While a consume is under way, everyone who reads sees it
whole or not at all.**

*What could go wrong (the attack):* both moves become final
together, but a reader looks while the consume is under way, and
sees one move and not yet the other. "Readable" is every state
readable from outside, not only the states left behind — and the
reader that matters most is another decision on the same item.
*Say:* a consume of R is under way. A reserve of 5 on the same
item reads R as ended, 5 held, but the count still at 10: 5 look
free, and it is admitted. Then the consume finishes: 7 on hand, 10
held. Oversold by 3, though no one died.

*What we guarantee:* a reader — a decision, the witness, anyone —
sees a consume either before it or after it, never in the middle.
A decision taken on the same item while a consume is under way
decides against one whole state, before or after, never a mix.
*Say:* the reserve of 5 decides against 10 on hand and 8 held, and
is refused; or against 7 on hand and 5 held, and is refused. Either
way the item ends at 7 on hand, 5 held.

*Kills:* 15 (the readable half-state, met by a reader rather than
left by a death).

---

**G3. An interrupted consume settles on its own.**

*What could go wrong (the attack):* the consume's outcome is
unknown (F19), and then nothing more happens: the caller does not
send it again, and the instance that sent it is gone or has given
up. If anything were still needed — a retry, a repair, a person —
to bring the consume to both moves or neither, then in our silence
it would stay half-done.
*Say:* the store stops answering mid-consume. The caller's request
times out, and the caller walks away. Nobody ever sends consume R
again.

*What we guarantee:* an interrupted consume reaches both moves or
neither with no further request, retry, repair or person. Once it
has, the next decision on the item goes through against that
state; nothing is left stuck (§3).
*Say:* the store answers again. With no one sending anything, it
reads 10 on hand with R holding 3, or 7 on hand with R consumed.
A reserve of 2 then decides against those numbers, and fits
either way: 10 held of 10 on hand, or 7 held of 7.

*Kills:* 16 (our silence).

---

**G4. Nothing makes one of consume's moves without the other.**

*What could go wrong (the attack):* the consume itself holds
together, but another path makes one move alone — a recovery step
that redoes the count's fall after an unknown outcome, a repair
that ends reservations it thinks were consumed, a later feature
that lowers the count "for a sale".
*Say:* after an unknown outcome, a well-meant retry inside the
ledger lowers the count by R's 3 again, without looking at R. 4 on
hand, R consumed once.

*What we guarantee:* the count falls for a consume only together
with that reservation's ending by consume, and a reservation ends
by consume only together with the count's fall — on every path,
including paths this slice never anticipated.
*Say:* there is one way to consume R, and it does both.

*Kills:* none by number. The attack is on the walls themselves: a
second way in that makes one move alone, bringing kill 15's
half-state back without any death.

---

**Inherited, not re-owned.** The attack's answers that SL-1, SL-2
and SL-3 already hold are named, not re-derived. A consume moves
exactly what its reservation holds, once, and never after the
reservation ended (SL-3's G2 and G4, kills 11, 12, 17). Exits
racing on one reservation collapse to one (SL-3's G1, kills 13,
14). Only the ledger writes the store's data (T4).

**The attack ran dry at four.** A fifth answer — "the store loses
one move after saying both were final" — is below the store's
acknowledgment: trusted (T1), its complement fenced (W4). A sixth —
"the caller is told the wrong thing about an unknown outcome" — is
the caller's view, refused at L2; ADR-0016 decides what the reply
says. A seventh — "an interrupted consume makes others on the item
wait" — is waiting, W3 (§3). An eighth — "a reserve's outcome is
unknown" — is F19 × P2, the orphan W2 and V5.

## §5 Evidence criteria

Each guarantee's evidence *creates* its adversity through the real
door and reads the witness from the store, from outside every
instance. The witness is §1's, read in one reading for the item and
for each reservation on it: the on-hand-count; the units held, and
the sum of active reservations; and how each reservation ended, if
it has. From these the evidence checks two things: the on-hand-count
has fallen by exactly the units of the reservations that ended by
consume, once each; and no more is held than is on hand.

The registry's flag decides the shape: no hammering. The adversity
is one consume, interrupted, and the harness interrupts it on
purpose — it holds the consume in the middle of its work, so the
interruption lands there and not before or after by luck.

- **E1 (G1).** An instance killed outright — not stopped — while
  its consume is held in the middle of its work. Repeated at every
  point the harness can hold it. After each: the witness reads both
  moves or neither; never R ended with the count unmoved, never the
  count lowered with R unended.
- **E2 (G2).** A consume held in the middle of its work. While it
  is held: the witness reads the item, and a reserve on the same
  item is sent from another instance. Then the consume is let go.
  The witness, read while held, shows the before-state whole; the
  reserve decides against a whole state; after, the witness shows
  the after-state whole, and no more held than on hand.
- **E3 (G3).** The store frozen while a consume is in the middle
  of its work. The caller gives up, and nothing else is sent —
  no retry, no repair. The store is let go; with no request since,
  the witness reads both moves or neither. Then a reserve on the
  same item goes through, decided against the numbers the witness
  read. Beside it, not evidence: an instance that loses the store
  mid-consume answers as ADR-0016 says — `503`, "outcome unknown".
  That checks the door's convention, not the promise; it says so
  on itself and discharges no kill.
- **E4 (G4).** A structural check, reading the code: no path lowers
  the count for a consume without ending the reservation in the
  same act, or ends one by consume without lowering the count —
  seen red by planting a path that makes one move alone.
- **SL-1's, SL-2's and SL-3's evidence, unchanged and green.**
- **The harness can fail (R5).** Each of E1–E4 is run with its wall
  absent — the two moves made final apart, on the working tree,
  never in history — and seen red, failing on the witness and not
  on a status or a crash, recorded from actual output; then green,
  unchanged, with the wall standing.

All of it under the one standard test command, nothing exported,
the ground not required up.

## §6 Folds and flags — the written zeros

**Folds: none.** SL-4's row carries none. The three folds are
SL-1's (FC1, FC3) and SL-3's (FC2); a consume relies on FC2 through
SL-3's G4.

**Flags: one, answered.** The registry's flag says the evidence is
kill-mid-work and unknown-outcome injection, not hammering. Both
are staged as their own evidence; neither is removed by a
definition.

- **Kill-mid-work** is E1: an instance killed outright while its
  consume is held mid-work. E2 uses the same hold to put a reader in
  the middle instead of a death.
- **Unknown-outcome injection** is E3: the store frozen mid-consume,
  the caller walking away, the consume left to settle on its own.

## §7 What this slice does not claim

- **That nobody waits.** An interrupted consume can keep its item
  waiting until the store gives up on it (§3). Waiting is W3's.
- **That a network cut with the instance alive is staged.** The
  evidence kills the instance (E1) and freezes the store (E3). A
  cut with the instance alive leaves the store the same thing — an
  unfinished consume no final word reaches — and differs only in
  how long the store takes to notice, which is the wait above.
- **That what the store acknowledged survives the store.** T1,
  W4.
- **That the caller is told what happened.** ADR-0016's `503` says
  only that the outcome is unknown, even when the instance could
  know nothing happened; the caller's view stays refused at L2.
- **That a reserve with an unknown outcome is safe from a second
  hold.** W2, V5.
- **That release and expiry hold together.** Each makes one move —
  the units held fall — and P4 names consume alone as the act that
  moves both numbers.
