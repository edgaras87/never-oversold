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
