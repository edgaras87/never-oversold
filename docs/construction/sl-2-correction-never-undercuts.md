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

## §6 Folds and flags — the written zeros

**Folds: none.** SL-2's registry row names no fold, and the three
the framing cut — FC1, FC2, FC3 — are consumed elsewhere: FC1 and
FC3 by SL-1, FC2 by SL-3. The count stays checkable.

**Flags: none.** SL-1's row carried one — kill 10, an adversity no
hammering could stage — and its specification owed an answer.
SL-2's row carries none, and it is worth saying why rather than
passing over it: every adversity here stages the normal way, as an
honest request at the door, the same request sent twice, and a
pair delivered in the other order. Nothing is inherited as a
warning, and nothing is owed an answer this specification does not
give.

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

## §8 Plan — one structural owner per guarantee

<!-- The second movement: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy — database constraint → type system →
     single validated entry path → runtime check → code review →
     hope — the strongest available, justified against the named
     adversity, not in general. -->

### The shape the walls need

Nothing new. SL-1's V1 migration already carries both tables and
the check constraint `reserved <= on_hand_count`, and ADR-0011
already fixed the adjustment as a value at the door. This slice
adds no table, no column and no migration; what it adds is
evidence, and two structural tests where a guarantee is held by an
absence that nothing today would notice disappearing.

That is the honest headline of this plan: **the walls this slice
needs are standing, built by SL-1 against a different adversity.**
The registry predicted it at SL-1's close — "most of SL-2's
invariant holds by structure today" — and the work here is to
justify each wall against *this* adversity, to prove it against
the two corrections nothing has ever sent, and to stop the
absences from being silently filled in later.

### Owners

| Guarantee | Owner, wall level | Why it defeats *this* adversity |
|---|---|---|
| **G1** an admitted change is compared at the moment of recording | **The store: the check constraint `reserved <= on_hand_count`, with the adjustment as one conditional statement** — `INSERT … ON CONFLICT (id) DO UPDATE SET on_hand_count = :count WHERE item.reserved <= :count`. Built by SL-1 (its G3); adopted here as this guarantee's owner. | SL-1 justified it against a race; F9 needs no race, and the same statement answers it for a different reason. The `WHERE` is evaluated against the row's committed state at the instant of writing, so an honest correction under the held units changes no row, and the door's answer is the store's answer — no code of ours decides it. Should the condition ever be wrong, the constraint refuses the row at write time: a count under the held units is physically unwritable, by any path. |
| **G2** taken whole or not at all | **The store's own assignment, in the same statement:** `SET on_hand_count = :count`, the operator's asserted value verbatim. No arithmetic, no `LEAST`, no clamp — the statement has nowhere to compute a different number. | Clamping is not refused by a check at runtime; it is impossible to express in the one statement that writes the count. The count after an adjustment is the asserted value or the old one, and nothing else can be written. |
| **G3** a refused change moves nothing | **The store: one statement, and no second write on the path.** The adjust path writes once; a `WHERE` that does not match writes nothing and returns no row, which is what the door turns into a refusal (ADR-0010's `409`). | There is no interval in which something is moved and then undone, and no reservation write on this path at all — so "refused" and "unchanged" are the same event, not two that must agree. |
| **G4** effect depends only on the number asserted | **The door's contract (ADR-0011): an adjustment asserts a value, never a difference** — backed by G2's assignment. | A resend cannot lower the count twice for one loss because no statement on this path adds or subtracts: the second assertion assigns the same number to the same row. Kill 7 dies of the door's shape, not of a duplicate check — which is why no request identity is needed and none is claimed (W2 stays fenced, the idempotency claim stays banked). |
| **G5** no decision depends on position in a sequence | **Absence, made structural: no ordering state exists on the adjust path** — no version, no sequence number, no supplied or stored "as of" instant, nothing consulted but the row as found. Held by a test that reads the main source and fails if any appears. | Kill 8 arrives as two assertions in the wrong order; each is judged alone against the state it meets, so no order can put the count under the held units. The danger is not today's code but tomorrow's: an absence nothing enforces is one helpful commit from being filled. The structural test is the enforcement, in the spirit of SL-1's no-process-clock test. |
| **G6** every change faces the rule, by whatever path | **The store's check constraint as the backstop; a structural test that the count has one writing path.** | The constraint refuses an under-held row whoever writes it — a script, a migration, a console, a future feature — so the *state* is safe by structure. The structural test guards the other half, the *decision*: a second path that writes the count without the conditional statement would refuse nothing, and the constraint would turn its mistake into an error rather than a refusal. |

Every guarantee has one owner, and the two absences (G5, G6) are
each held by a test that reads the code rather than by a habit.

### The faces chosen, and the ones not

G1, G2 and G3 inherit SL-1's face comparison (its §7, five faces
against one adversity); nothing in this slice's adversity changes
that verdict, and re-running it would be ceremony. Two guarantees
here do have a real choice, and both are put in front of the
reviewer before the choice is taken:

**G4 — how a resend is made harmless.**

| Face | How it holds G4 | Cost |
|---|---|---|
| **Value semantics at the door** — chosen | a second assertion of the same number assigns the same number; nothing accumulates | none new: ADR-0011 already decided values over deltas, and this slice consumes that decision rather than taking one. Does not tell a resend from two honest corrections that agree — and does not need to |
| A request identity per adjustment — an idempotency key, stored and refused on repeat | the ledger recognises the resend as *the same request* and answers the first outcome again | a second claim, not this promise's: the intent banked "a retried request creates at most one" and fenced it as W2. It buys the operator a truthful "you already sent this", which nobody has asked for, at the cost of a key, a store of keys, and their expiry |
| Deltas with identity — `{"delta": -3}` plus a key | the count moves once per identified request | strictly worse here: it needs the key *and* re-opens the reordering question, since deltas do not commute with a refusal. ADR-0011 rejected deltas already |

**G5 — whether the ledger should know the order.**

| Face | How it holds G5 | Cost |
|---|---|---|
| **No ordering at all** — chosen | each assertion judged alone against the state it meets; no order can break §1 | the count can end at the older value, which is W1's remainder in ink. The ledger claims nothing about which correction was made last |
| An operator-supplied instant, last-write-wins | a late-arriving older correction is ignored, so the count tends to the operator's latest truth | it trusts the operator's clock — T3 trusts their *identity*, not their timekeeping — and it is a claim about the world, which L2 refuses and W1 fences. Shrinking W1 takes a dated revision of the definition, not a slice's plan |
| A ledger-assigned sequence per item | the ledger orders what it received | orders *arrival*, which is exactly what F13 scrambles; it would answer a question nobody asked while leaving the real one untouched |

### Escape hatches hunted, afresh

- **A migration or a console writing the count.** The constraint
  refuses an under-held row from any of them; the rule that
  migrations carry structure and never ledger rows stands from
  SL-1 and is unchanged here.
- **The counter drifting from the rows.** `reserved` is the entry
  path's bookkeeping (SL-1's standing guard). A drift low would let
  a correction be admitted that should be refused — so this slice's
  witness recomputes the active sum from the reservation rows and
  never trusts `reserved` alone, exactly as SL-1's witness does.
- **A second decision path for the count.** None today; G6's
  structural test is what keeps it that way.
- **A "force" flag or an admin correction.** None exists, and none
  is added: it would be §3's rejected shape arriving through the
  back door, since forcing the count under the holds is precisely
  what ending reservations was proposed to make possible.
- **The refusal turned into a clamp by a later hand.** G2's owner
  is the statement's shape; the evidence pins it with the mirror
  case (a correction that fits is taken exactly).

### The surface, at its minimum

Nothing is added to the application. The door already carries
`POST /items/{item}/adjustments` with its value body (ADR-0011),
the refusal already answers `409` distinct from `400` (ADR-0010),
and the store already carries the constraint. What this slice adds
lives entirely under test: the evidence for E1–E4, the two
structural tests for E5 (G5's no-ordering-state, G6's one-writing-
path), and nothing else. A slice is not a feature, and this one
delivers no feature at all.

### Deviations and provisionals, so the close can see them

- SL-1's provisional refusal is no longer provisional (§3). SL-1's
  own record still calls it so; its §3 G3 and §6 are corrected at
  this slice's close, not before, so the correction is one act with
  its reason.
- G5 and G6 are guarantees held by an absence plus a test that the
  absence persists. This run has now met that shape twice — SL-1's
  no-process-clock and no-instance-state tests, and these two. The
  hand-off already filed with the bundle (the absence rung in the
  enforcement hierarchy) gains a second instance; it is named here
  so the close can carry it.

## §9 Evidence — as delivered

All of it under `./mvnw test`: 39 tests, 0 failures, nothing
exported, the ground not required up. The adversity is sequential
throughout — an honest operator at the real door — and the witness
is read from the store as `runtime`, from outside the application.

| Criterion | Test | The adversity it creates |
|---|---|---|
| E1 · G1 | `CorrectionIT.anHonestCorrectionUnderTheHoldsIsRefused` | 10 on hand, 8 held, the operator asserts 7 (F9, kill 6) |
| E1 · G1 | `CorrectionIT.aRefusalIsNotAnInvalidRequest` | the same, beside a nonsense count, to keep `409` and `400` apart (ADR-0010) |
| E1 · G2 | `CorrectionIT.aCorrectionThatFitsIsTakenAtExactlyTheNumberAsserted` | 8 asserted against 8 held: it fits, so it is taken — at 8, never clamped |
| E2 · G3 | `CorrectionIT.aRefusedCorrectionMovesNothing` | the refusal of E1, with every number the store keeps compared before and after |
| E3 · G4 | `CorrectionIT.aCorrectionThatFitsResentAssertsTheSameState` | the same body sent twice at the same door (F11, kill 7) |
| E3 · G4 | `CorrectionIT.aCorrectionThatDoesNotFitResentIsRefusedTwiceAndMovesNothing` | the refused correction resent |
| E4 · G5 | `CorrectionIT.aLateOlderCorrectionUnderTheHoldsIsRefused` | 9 then 7 swapped in delivery, the late 7 arriving against 8 held (F13, kill 8) |
| E5 · G5 | `NoOrderingStateOrSecondWriterTest.anAdjustmentCarriesNothingButTheAssertedCount` | the code read, not run: the door carries one field |
| E5 · G6 | `NoOrderingStateOrSecondWriterTest.theCountHasOneWritingPath` | the code read: one class reaches the store, one method takes a count |

Beside them, and not evidence:
`CorrectionIT.theArrivalOrderDecidesWhichNumberSurvives`, which says
so on itself — a tripwire on the decision to keep no ordering. §9's
own finding, below, is how it came to be marked.

### Red before green, from actual output

- **E1, E2 and E4's evidence half.** The wall made absent on the
  working tree — the `WHERE` guard out of `Ledger.adjust`, the
  `item_never_oversold` constraint out of V1 — and the witness read
  `Numbers[onHandCount=7, held=8, activeSum=8, reservations=1]`:
  eight units held against seven on hand, the promise's negation,
  created. Restored, and the same tests green unchanged.
- **E3.** Its wall is not a guard but the door's shape, so the
  absence to create is delta semantics: the statement made to add
  the asserted number rather than assign it. The count read 19
  where 9 was asserted — F11's own shape, the count moving twice
  for one loss. Restored, green.
- **E5.** No wall to remove, so each forbidden thing was planted in
  turn: an `asOf` beside the count at the door, which gave
  `["onHandCount", "asOf"]` where exactly `["onHandCount"]` is
  required; a second method taking an `OnHandCount`, which gave
  "expected size 1 but was 2"; and a `JdbcClient` field on the
  controller, which the rule named — *Field
  `ReservationController.store` has type `JdbcClient`*. All three
  restored.

No removal or plant ever landed in history; each lived on the
working tree for one run, and `git status` showed only the new test
file afterwards.

### What the build found that the specification had not

- **E1's first red was worthless.** It failed on the status code —
  *expected 409 CONFLICT but was 200 OK* — and stopped before
  reading the store at all. That red proves the test reads status
  codes, not that it watches the promise. The test was reordered to
  assert the witness first, and its second red carried the numbers
  above. The order of assertions is part of the evidence, not a
  matter of taste.
- **E4 was two things wearing one name.** With the wall absent the
  late-correction case reddened and the either-order case could
  not: with 4 units held, both corrections sit above the holds, so
  no implementation could break §1 there. L4 had already said as
  much — kill 8 is *"kill 6 if the older is lower; otherwise W1"* —
  and the specification's E4 did not carry the split. The half that
  can kill is the evidence; the half that cannot is a tripwire and
  now says so. Nothing in §4 or §5 changed: the guarantee and the
  criterion stand as signed, and this is how the criterion divided
  when it met the code.
- **The skill had no name for the second kind.** Corrected in place
  at `cbc-slice` (decisions log, 2026-09-21), with the counting
  rule that keeps it honest: a tripwire never discharges a kill,
  and the red run — not the author — decides which kind a test is.

## §10 Standing guards

What would rot this slice, and what watches:

- **A later feature touching the count.** Any new path that writes
  `on_hand_count` re-reads this specification first. `E5 · G6`
  fails the moment a second writing path appears, which is the
  reminder rather than the rule; the rule is that the comparison
  and the write stay one act.
- **The refusal turned helpful.** A later hand may decide that
  clamping the count down to the held units is kinder than
  refusing. `E1 · G2` is what fails; §3 is why it must not be
  reopened lightly — the ledger would then assert a number no
  operator sent.
- **Ordering arriving quietly.** A timestamp on the request, a
  sequence, a last-write-wins rule: `E5 · G5` fails, and the
  tripwire beside it fails too. Both are pointing at §8's G5 face
  comparison and at W1's fence, which needs a dated revision to
  move.
- **The held-units counter drifting.** Inherited from SL-1's own
  guard: `reserved` is the entry path's bookkeeping, and a drift
  upward starts refusing honest corrections for units nobody holds.
  This slice's witness recomputes the active sum from the
  reservation rows rather than trusting the counter, and `E2 · G3`
  compares both.
- **The door growing a force flag.** An admin correction that
  bypasses the comparison is §3's rejected shape arriving by the
  back door, and nothing structural stops a new endpoint from being
  written — only `E5 · G6`'s one-writing-path rule and this
  paragraph.

## §11 Sign-offs

<!-- Dated lines, the reviewer's: the specification before the plan,
     the plan before the build. -->

- 2026-09-20 — the specification (§1–§7) signed by the reviewer.
- 2026-09-21 — the plan (§8) signed by the reviewer.
