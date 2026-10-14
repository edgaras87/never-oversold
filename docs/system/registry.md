# Slice registry — the work, cut and ready

<!-- The work the promise cuts into: one slice per invariant that
     needs its own proof, each with the adversity its evidence must
     create. Filled at framing close from the derivation record
     beside it (framing/derivation.md). Kill numbers are the
     definition's L4; facts are its L1. Stands
     alone. A living record: statuses change at every slice close;
     anything else changes only through a dated revision entry. -->

Framed 2026-09-10. Every verdict is the reviewer's. This is the source
of truth for what to work next. The order is an expectation. It is
decided again at each slice close, never assumed from what this
file first said.

A slice = one invariant × the adversity its evidence must *create*.
Status values: `open` · `chosen-next` · `in-progress` ·
`closed (date, evidence)`.

---

## Contention on one item's numbers

The heading groups the slices for the reader. It is not a
boundary. All four slices live in one area, the reservation ledger,
and share one witness: the promise's own.

### SL-1 — no over-admission under contention  `closed (2026-09-14, evidence)`

- **Invariant:** for every item, in every readable state, the sum
  of active reservations ≤ on-hand-count.
- **Adversity to create:** concurrent admits on one item's last
  units:
  - from many callers (F1);
  - from more than one of our own instances (F17);
  - against a store that shows two checks the same count (F21), or
    a stale read (F22);
  - against a downward adjustment racing them (F10);
  - with an admit decided but not yet recorded (F15);
  - under clocks that disagree about activeness (F18).
- **Area:** the reservation ledger. **Kills covered:** 1, 2, 3, 4,
  5, 9, 10.
- **Folds in:** FC1 (what a valid request is) and FC3 (what
  *active* means, judged by one clock, not one per instance).
- **Flag riding:** kill 10, clocks disagreeing about activeness,
  cannot be staged by hammering. Its evidence needs a controlled
  clock, unless FC3 removes it by judging activeness from one
  clock. Then the evidence shows that clock is the one used.
  Named here so the slice knows of it before it starts.
- **Presumes:** nothing.
- **Judgment logged:** kill 5 sits here and not in SL-2 because it
  is a race, and SL-2's evidence would not create it. Kill 9 sits
  here because a decision invisible to other decisions is
  over-admission from inside.
- **Record:** [SL-1](../construction/sl-1-no-over-admission.md)
- **Answered at close:**
  - The flag: FC3 removes kill 10. Whether a reservation is active
    is judged by one clock, the store's. The evidence shows no
    other clock is read (record, SL-1 G5).
  - The judgment stands. Kill 5 is proven here (SL-1 G3). Kill 9
    has no interval to stage, because the decision and its record
    are one commit (SL-1 G4).

### SL-2 — the correction never undercuts the holds  `closed (2026-09-21, evidence)`

- **Invariant:** no admitted change to the on-hand-count leaves it
  under the sum of active reservations.
- **Adversity to create:** an operator's downward correction under
  the reserved sum (F9), resent (F11), or out of order (F13).
  The evidence can send them one at a time. No concurrency is
  needed. The adversity is an honest request the ledger cannot
  honour as asked.
- **Area:** the reservation ledger. **Kills covered:** 6, 7, 8.
- **Folds in:** none.
- **Flag riding:** none.
- **Presumes:** SL-1. The reserved sum a correction is checked
  against is the one SL-1 keeps true.
- **Judgment logged:** two options were seen during framing and
  parked, not chosen: refuse the correction, or let it end
  reservations. The slice decides. The invariant and the witness
  are the same under either. SL-2 has the same witness as SL-1 but
  a different adversity. Hammering never sends an honest
  correction, and a correction never creates a race. So they are
  two slices.
- **Record:** [SL-2](../construction/sl-2-correction-never-undercuts.md)
- **Answered at close:** the judgment. The slice chose to refuse. A
  correction that would leave the on-hand-count under the sum of
  active reservations is refused, and ends no hold (record, "The
  correction's shape — decided").

### SL-3 — a reservation exits once  `closed (2026-10-07, evidence)`

- **Invariant:** a reservation moves its item's numbers at most
  once on exit, and never after it has ended.
- **Adversity to create:**
  - a retried consume (F3);
  - consume racing release, or two consumes racing (F4);
  - consume racing expiry (F23);
  - a late consume on an ended reservation (F5);
  - our own retry after an unknown outcome (F19);
  - expiry fired early (F24).
- **Area:** the reservation ledger. **Kills covered:** 11, 12, 13,
  14, 16 (our own retry), 19 (early).
- **Folds in:** FC2 (an exit names its own reservation and moves
  exactly what it holds).
- **Flag riding:** none.
- **Presumes:** SL-1. The reservations that exit were admitted
  correctly.
- **Judgment logged:** none.
- **Record:** [SL-3](../construction/sl-3-a-reservation-exits-once.md)
- **Answered at close:** none.

### SL-4 — consume's two moves hold together  `closed (2026-10-10, evidence)`

- **Invariant:** no readable state holds one of consume's two moves
  without the other. The two moves are ending the reservation and
  lowering the on-hand-count.
- **Adversity to create:** our own death between the two moves
  (F16); an unknowable outcome of the consume write (F19).
- **Area:** the reservation ledger. **Kills covered:** 15, 16
  (silence).
- **Folds in:** none.
- **Flag riding:** the evidence is not hammering. It kills an
  instance mid-work and injects an unknown outcome. Named here so
  the slice plans for it.
- **Presumes:** SL-3. Each exit leaves its own record, its receipt,
  so a half-done consume can be told from a done one.
- **Judgment logged:** kept apart from SL-3 because the proof
  obligations differ: "duplicates collapse" (SL-3) against "the
  half-done converges" (SL-4). The evidence differs with them.
- **Record:** [SL-4](../construction/sl-4-consumes-two-moves-hold-together.md)
- **Answered at close:**
  - The judgment: nothing converges. One commit leaves nothing
    half-done, and the evidence shows both moves or neither
    (record, "Whether anything is left to converge").
  - The flag: both were staged. An instance was killed mid-consume,
    and the store was frozen (record, the evidence as delivered).

---

## Registry state

4 slices closed (SL-1, 2026-09-14; SL-2, 2026-09-21; SL-3,
2026-10-07; SL-4, 2026-10-10), none open. New work enters by
re-framing or as a new slice through this registry, never around
it.

## Fold-reconciliation line

Every kill the definition's L4 names is accounted for: 20 kills ↔
4 slices + 3 folds + 0 deferrals + the fence remainders. There is
one row per kill, so counting the rows checks that none was
dropped. The definition's L4 owns every kill: what dies, against
which fact, from which possession. The middle column here is a
summary for counting, never to be read in its place.

| Kill | What dies | Lands in |
|---|---|---|
| 1 | racing admits both succeed on the last units | SL-1 |
| 2 | our own instances race, each on its memory | SL-1 |
| 3 | the store shows both checks the same count | SL-1 |
| 4 | an admit passes on a stale count | SL-1 |
| 5 | an admit races a downward adjustment | SL-1 |
| 6 | an honest correction sets the count under the sum | SL-2 |
| 7 | a downward correction resent | SL-2; its world-truth part W1 |
| 8 | corrections reordered | SL-2; its world-truth part W1 |
| 9 | an admit decided but not yet recorded | SL-1; its orphan half W2, V5 |
| 10 | instances disagree on what is active | SL-1, its flag answered |
| 11 | a consume lands on an ended reservation | SL-3 |
| 12 | a consume retried | SL-3 |
| 13 | consume races release, or two consumes race | SL-3 |
| 14 | consume races expiry | SL-3 |
| 15 | we die between consume's two moves | SL-4 |
| 16 | consume's outcome unknowable | SL-3 (our retry); SL-4 (our silence); the orphan V5 |
| 17 | a consume moves units its reservation does not hold | FC2, folded into SL-3; identity W5 |
| 18 | a request that means nothing reaches the decision | FC1, folded into SL-1 |
| 19 | time jumps: expiry early, or never | SL-3 (early); V5 (never) |
| 20 | a retried or abandoned reserve over-holds | fenced: W2, V5 |

Folds: FC1 and FC3 into SL-1, the first slice that needs them. FC2
into SL-3, because only the exit path uses it.

The written zeros: no deferrals, no kill dropped, no two
invariants merged into one slice, and no flag beyond the two above.

## Ordering expectation (re-decided at each close)

SL-1 → SL-2 → SL-3 → SL-4, all closed. The order was re-decided at
each close and kept; the last re-decision was at SL-4's close,
2026-10-10. What each close settled, and what it handed to the
slices after it, is in that slice's record:
[SL-1](../construction/sl-1-no-over-admission.md),
[SL-2](../construction/sl-2-correction-never-undercuts.md),
[SL-3](../construction/sl-3-a-reservation-exits-once.md),
[SL-4](../construction/sl-4-consumes-two-moves-hold-together.md).
Nothing is left to order: what follows is the release, not a
slice.

## Divergences from the briefing (derivation wins, recorded)

None. The working name safe-reservations was overturned at the
naming step (ADR-0003). That was the naming step's own decision,
not the derivation overriding the briefing.

The briefing itself was not kept in the repository, so this line
cannot be checked against it.

## Revision log

<!-- Dated entries only, for anything but a status change: what
     changed, why, what triggered it. -->

- 2026-09-10 — the divergences section: the working name's fate
  recorded (ADR-0003). Triggered by PLAN Step 2. No slice changed.
- 2026-09-14 — SL-1 closed on evidence (its record:
  `docs/construction/sl-1-no-over-admission.md`); the ordering
  expectation re-decided and kept, with what SL-1 leaves to SL-2
  and SL-3 by name. SL-1's flag (kill 10) resolved as FC3 removing
  it: activeness is judged by the store's clock, shown by evidence
  and by structure. Triggered by PLAN Step 5's close. No invariant,
  adversity or fold changed.
- 2026-09-21 — SL-2 closed on evidence (its record:
  `docs/construction/sl-2-correction-never-undercuts.md`); the
  ordering expectation re-decided and kept, with what SL-2 leaves
  to SL-3 by name. The correction's shape, parked at framing as two
  and left to this slice's specification, is decided: refuse it.
  The row's judgment-logged line stands as the record of the
  parking; the choice itself lives in the slice record's §3, with
  the losing shape and its three reasons. Triggered by PLAN Step
  6's close. No invariant, adversity or fold changed.
- 2026-10-01 — SL-1's adversity line names F21 and F18, which it
  always covered: the definition places both in concern A, and SL-1
  closed on evidence for both (kills 3 and 10). The revision log put
  in date order. Triggered by a documentation error check before
  Step 7. No invariant, kill or fold changed.
- 2026-10-07 — SL-3 closed on evidence (its record:
  `docs/construction/sl-3-a-reservation-exits-once.md`); the
  ordering expectation re-decided and kept, with what SL-3 settles
  from SL-1 and SL-2 and what it leaves to SL-4 by name. SL-4 moves
  to `chosen-next`. Triggered by PLAN Step 7's close. No invariant,
  adversity or fold changed.
- 2026-10-10 — SL-4 closed on evidence (its record:
  `docs/construction/sl-4-consumes-two-moves-hold-together.md`); the
  ordering re-decided with nothing left to order, and what SL-4
  leaves named. The row's "the half-done converges" answered: one
  commit leaves nothing half-done, so nothing converges; the row's
  judgment stands for the evidence. Its flag answered by staging
  both: kill-mid-work and unknown-outcome injection. Triggered by
  PLAN Step 8's close. No invariant, adversity or fold changed.
- 2026-10-10 — The ordering expectation's two latest re-decisions,
  at SL-3's and SL-4's closes, each labelled with its date, so the
  SL-3 text no longer reads as current; its open question of who
  may write the store marked answered (T4, W7, ADR-0014); SL-2's
  row says only that the slice decides. Triggered by a records
  clean-up before Release. No invariant, adversity or fold changed.
- 2026-10-13 — The ordering expectation shortened to the order and a
  link to each slice record: the re-decisions at SL-3's and SL-4's
  closes told again what those records hold. Each fact was found in
  its record, an ADR or the backlog before its text went. The
  divergences line says the briefing was not kept. Why: the
  registry holds the work, not the story of each close. Triggered
  by Step 10 (the retrospective's D5 and D8). No invariant,
  adversity or fold changed.
- 2026-10-13 — Each slice's entry gains the fields it lacked, "none"
  where empty, a link to its record, and one line answering each
  flag and judgment its close settled, taken from the record:
  SL-1's flag (one clock, the store's) and judgment (kills 5 and 9
  proven here), SL-2's judgment (refuse), SL-4's judgment (nothing
  converges) and flag (both staged). Framing's words stand beside
  the answers. Why: once the ordering section's stories went, a
  closed entry still read its framing guesses as open. Triggered by
  Step 10 (the retrospective's D29). No invariant, adversity or
  fold changed.
- 2026-10-14 — written plain, meaning unchanged: one idea per
  sentence, dash and semicolon chains split, the adversity lists as
  lists. Words the glossary gives another meaning replaced: "the
  ground" (SL-1 presumes nothing), "the first door" (SL-1 is the
  first slice that needs FC1 and FC3). "Theorem" and "rider" said
  plainly. The header comment no longer names concern letters,
  which the definition retired. Why: the reader had to untangle it.
  Triggered by Step 10 (the retrospective's D2). No invariant,
  adversity, kill or fold changed.
- 2026-10-14 — read against the definition and the slice records.
  Kill 16's orphan is stopped by V5 alone, as the definition's kill
  16 and its fences say; the reconciliation row had W2 too. SL-3's
  adversity names F19, our own retry after an unknown outcome,
  which its kill 16 needs. SL-2's judgment says "two options", since
  a shape is a page under `.claude/shapes/`; its record's heading
  keeps its name. Kill 10's row says its flag was answered. Why:
  lines out of step with the records they name. Triggered by Step
  10's second reading of the records. No invariant, kill or fold
  changed; SL-3's adversity names a fact its kills already covered.
