# SL-2 — the correction never undercuts the holds

<!-- The slice's record, grown in three movements: the correctness
     specification (what must hold, against what, and what proof
     looks like — no mechanism), then the plan (one structural owner
     per guarantee, and why it beats the named adversity), then the
     evidence as delivered (which test creates which adversity, and
     what it read from the store). Invariant and adversity are the
     registry's and the definition's, as written; guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each movement.

     §3 is this slice's own section: SL-1 had no shape to choose,
     and this slice exists because no structure decides such a
     thing by itself. -->

## §1 Invariant

No admitted change to the on-hand-count leaves it under the sum of
active reservations. (`docs/system/registry.md`, SL-2; concern B in
the definition's L4.)

*Active* is FC3's, as SL-1 consumed it: a reservation not ended by
an exit and not past its expiry, as judged by one clock, not one
per instance. *Readable state* is the intent's: true at every read
from outside, not eventually. *A change to the on-hand-count* is
whatever an adjustment asserts at the door — a value, never a
difference (ADR-0011).

## §2 Adversity

An honest request the ledger cannot honour as asked. No race to
create; sequential suffices, and that is what makes this a slice of
its own — hammering never sends an honest correction. Pulled from
the definition's L1 as written:

- an operator lowers the count under the reserved sum — loss,
  breakage, a recount — the promise's negation arriving as a
  legitimate fact from the world (F9, kill 6);
- that downward correction is resent, the count asked to fall twice
  for one loss (F11, kill 7);
- two adjustments to one item arrive in the other order than they
  were made, so which value is current is not known (F13, kill 8).

Kills covered: 6, 7, 8. What this slice does not face: any race —
an adjustment against concurrent admits is kill 5, closed in SL-1;
exits and their races (SL-3, SL-4); a retried reserve (W2, fenced).

## §3 The correction's shape — decided

Framing parked two shapes and chose neither (registry, SL-2's
judgment logged; ADR-0011's third bullet; L4's concern B). The
decision is this specification's, and it is taken here:

**Chosen: refuse.** A change to the on-hand-count that would leave
it under the sum of active reservations is not admitted. The
reservations stand; the ledger's count keeps its old value; the
operator is answered with a refusal, which is not an invalid
request (ADR-0010).

**Rejected: let the correction end reservations** — admit the
operator's number and end enough active reservations to fit under
it. Not because it would break the invariant; under either shape
the count is never under the sum. It was rejected for three
reasons, in the truth set's own words:

1. It needs a policy no possession grants. L2's P2 makes the
   transitions out of active ours, so the ledger *may* end a hold;
   but *which* hold dies is a choice, and the framing refuses that
   kind of choice by name — expiry duration is "the caller's or the
   seller's policy". Ending holds by lottery, by age, or by size is
   a policy this system was never given.
2. It enlarges a slice that has not been worked. A correction that
   ends reservations is a new kind of exit, and SL-3's invariant is
   that a reservation moves its item's numbers at most once on
   exit, and never after it has ended. The exit would have to be
   carried there before it is understood here.
3. The cost it buys was already accepted in ink. Its gain is a
   count that agrees with the shelf; but the intent defines the
   on-hand-count as "the ledger's own recorded count of an item,
   not a warehouse's truth", L2 refuses physical stock truth with
   the words "the count can be wrong about the world and the
   promise still holds", and the reconciliation line sends the
   remainder of kills 7 and 8 to the W1 fence. Shrinking W1 is a
   dated revision of the definition, not a slice's to take in
   passing.

**What the choice costs, said plainly.** An operator who has
counted the shelf and found fewer units than are held has no
recourse in this system today: the surface has no operator-side
exit, so they wait for expiry or for the callers to release, while
the ledger goes on admitting reservations against units that are
not there. The promise holds; the seller can still lose. This is
W1's remainder made concrete, and the question of an operator-side
exit belongs to SL-3's territory — exits — not to this slice by the
back door. Recorded in §7 as what this slice does not claim.

**What the choice confirms.** SL-1's wall already refuses such a
change, provisionally, pre-deciding nothing (SL-1 §3, G3; ADR-0011).
This section removes the word *provisional*: the refusal is now the
decided shape, and this slice owes the evidence that it survives the
resent and the reordered correction, which nothing has yet created.

## §4 Guarantees — derived by attack

Each is one answer to: *what would let §1 hold on paper yet break
in fact?* The attack runs until no new answer comes. Every
guarantee is a property; none names how it is held.

- **G1. An admitted change leaves the count not under the sum, as
  the sum stands at the moment of recording.** Attack: the count is
  set to a number that was above the sum when the request was read
  and below it when the write lands. Guarantee: the comparison and
  the change are one act against the store's state as it is then,
  never two acts against a copy. Kill 6.
- **G2. A change is taken whole or not at all.** Attack: the ledger
  meets a number it cannot honour and takes part of it — clamping
  the count down to the sum, say, so that something is written.
  Guarantee: the count after an adjustment is either exactly the
  number the operator asserted or exactly what it was before; the
  ledger never asserts a number nobody sent. Kill 6's other half.
- **G3. A refused change moves nothing.** Attack: the refusal is
  decided, but the attempt has already touched the numbers, or ends
  a hold on its way out. Guarantee: after a refused adjustment the
  item's count, its reservations and their active sum are what they
  were before the request arrived. Kill 6; and the shape of §3 made
  checkable.
- **G4. A correction's effect depends only on the number it
  asserts, never on how many times it arrives.** Attack: a
  downward correction is resent and the count falls twice for one
  loss (F11). Guarantee: an adjustment asserts a state, not a
  movement; the state after two identical assertions is the state
  after one. Kill 7.
- **G5. No decision depends on a correction's position in a
  sequence.** Attack: two corrections arrive in the other order
  than they were made, and the ledger, assuming an order, admits
  one because the other preceded it (F13). Guarantee: each
  adjustment is judged alone, against the state it meets; the
  ledger asserts no ordering among corrections and needs none for
  §1 to hold. Whichever order they arrive in, no admitted one
  leaves the count under the sum. Kill 8.
- **G6. Every change to the on-hand-count faces this rule, by
  whatever path it arrives.** Attack: a second way in — a script, a
  migration, an admin path, a later feature — lowers the count
  without meeting the comparison. Guarantee: P1's "every change to
  it enters through the ledger's door" holds for changes this
  slice never anticipated; no path writes the count without
  facing §1.

**Inherited, not re-owned.** The attack's answers that SL-1 already
holds are named, not re-derived: that the comparison is one act and
not two, and that an adjustment and a concurrent admit do not
interleave (SL-1's G1 and G3, kill 5); that nonsense — a negative
or absurd count, an unknown item — never reaches the decision
(SL-1's G6, FC1); that activeness is judged by one clock (SL-1's
G5, FC3). This slice relies on them and adds no claim about them.

**The attack ran dry at six.** A seventh answer — "the operator is
not told which happened" — is the caller's view, refused at L2, and
its state-side half is G3. An eighth — "the count now disagrees
with the shelf" — is W1, fenced in ink, and §3 names it as the
chosen shape's cost.

## §5 Evidence criteria

Each guarantee's evidence *creates* its adversity through the real
door and reads the witness from the store, from outside the
process. The witness is §1's: for the item, the on-hand-count and
the sum of active reservations, read from persisted state — the
same witness SL-1 read, a different adversity creating it.

- **E1 (G1, G2).** An item with active reservations holding some of
  its units; an honest downward correction sent through the real
  door asserting a count below that sum. Witness after: the count
  is the old value, unchanged, and the sum is unchanged; the answer
  is a refusal, not an invalid request (ADR-0010's statuses
  distinguish them). And its mirror: a correction that fits — below
  the old count but not below the sum — is admitted, and the count
  after is exactly the number asserted, no clamping.
- **E2 (G3).** Riding on E1's refusal: every reservation that was
  active before is active after, with the same units; the count,
  the sum and the reservation records are identical to the state
  read before the request.
- **E3 (G4).** The same correction sent twice, in both worlds: one
  that fits — the state after the second assertion identical to the
  state after the first; and one that does not — refused both
  times, nothing moved either time. The resend is the same bytes at
  the same door, not a second request written to look alike.
- **E4 (G5).** Two corrections for one item, delivered in the order
  opposite to the one they were made in, against identical starting
  states; the invariant read in every state between them and after.
  Where the surviving value is the older one, the record says so
  and names it W1's remainder rather than a violation — the
  evidence shows the invariant held, not that the world was right.
- **E5 (G6).** A structural check, reading the code rather than the
  runtime: the on-hand-count has one writing path, and a second one
  introduced anywhere fails the check. In the spirit of SL-1's
  structural test, not a text search.
- **The harness can fail (R5).** Each of E1–E4 is run with the wall
  absent — removed on the working tree and on the throwaway store,
  a state that never lands in history — and seen red, recorded from
  actual output; then seen green, unchanged, with the wall standing.
  A green suite that was never red is not known to be watching.

All of it under the one standard test command, nothing exported,
the ground not required up.

## §6 Folds, consumed here

None. SL-2's registry row names no fold, and the three the framing
cut — FC1, FC2, FC3 — are consumed elsewhere: FC1 and FC3 by SL-1,
FC2 by SL-3. The written zero, so the count is checkable.

## §7 What this slice does not claim

- **That the count agrees with the world.** W1, in ink. A refused
  correction leaves the ledger knowingly disagreeing with the
  shelf, and the operator's only recourse today is time — expiry —
  or the callers' own releases. The cost of §3's shape, stated
  there.
- **That an operator can make the ledger true.** No operator-side
  exit exists; whether one should is SL-3's territory, and this
  slice hands it there by name rather than inventing it here.
- **That a resent correction gets the same answer.** The world
  moves between the two: a correction refused while holds stood may
  be admitted once they have gone. What holds across the resend is
  §1, not the answer.
- **That corrections are applied in the order they were made.**
  Ordering is the network's (F13) and the remainder is W1's; the
  ledger claims only that no arrival order can put the count under
  the sum.
- **That the sum is minimal.** A hold past its expiry still counts
  until an exit ends it (SL-1 §7), so a refusal here may be
  conservative — refusing against holds the clock has already
  killed. SL-3's debt, not this slice's to pay.
- **Who may adjust.** W5, T3 — identity is trusted at the door.
- **That a reply reaches the operator.** The caller's view, refused
  at L2.

## §8 Sign-offs

<!-- Dated lines, the reviewer's: the specification before the plan,
     the plan before the build. -->

- <pending> — the specification (§1–§7).
