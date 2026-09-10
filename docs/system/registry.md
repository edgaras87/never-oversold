# Slice registry — the work, cut and ready

<!-- On the cbc-framing skill's registry template (its
     templates/registry.md), filled at framing close from the
     derivation record beside it (framing/derivation.md). Kill
     numbers and concern letters are the definition's L4; facts
     are its L1. Stands alone. -->

Framed 2026-09-10 (cbc-framing step 6; verdicts the reviewer's,
every one, no delegation). Source of truth for what to work next.
Ordering is an expectation, re-decided at each slice close — never
assumed from this file's original state.

A slice = one invariant × the adversity its evidence must *create*.
Status values: `open` · `chosen-next` · `in-progress` ·
`closed (date, evidence)`.

---

## Contention on one item's numbers

Grouping is a heading for the reader, never a boundary: all four
slices live in one area, the reservation ledger, and share one
witness — the promise's own.

### SL-1 — no over-admission under contention  `chosen-next`

- **Invariant:** for every item, in every readable state, the sum
  of active reservations ≤ on-hand-count.
- **Adversity to create:** concurrent admits on one item's last
  units — from many callers (F1); from more than one of our own
  instances (F17); against a stale read (F22); against a downward
  adjustment racing them (F10); an admit decided but not yet
  recorded (F15).
- **Area:** the reservation ledger. **Kills covered:** 1, 2, 3, 4,
  5, 9, 10.
- **Folds in:** FC1 (what a valid request is); FC3 (what *active*
  means — one clock, not one per instance).
- **Flag riding:** kill 10 — clocks disagreeing about activeness —
  cannot be staged by hammering. Its evidence shape is a
  controlled clock; or FC3 removes it by judging activeness from
  one clock, in which case the evidence shows that judgment is the
  one used. Named here so the slice inherits the warning, not the
  surprise.
- **Presumes:** nothing — this is the ground.
- **Judgment logged:** kill 5 sits here and not in SL-2 because it
  is a race, and SL-2's evidence would not create it. Kill 9 sits
  here because a decision invisible to other decisions is
  over-admission from inside.

### SL-2 — the correction never undercuts the holds  `open`

- **Invariant:** no admitted change to the on-hand-count leaves it
  under the sum of active reservations.
- **Adversity to create:** an operator's downward correction under
  the reserved sum (F9); resent (F11); out of order (F13).
  Sequential suffices — the adversity is an honest request the
  ledger cannot honour as asked.
- **Area:** the reservation ledger. **Kills covered:** 6, 7, 8.
- **Presumes:** SL-1 — the reserved sum it is checked against is
  the one SL-1 keeps true.
- **Judgment logged:** two shapes were seen during framing and
  parked, not chosen — refuse the correction, or let it end
  reservations. The slice's specify step decides; the invariant
  and the witness are the same under either. Same witness as SL-1,
  different adversity: hammering never sends an honest correction,
  and a correction never creates a race — hence two slices.

### SL-3 — a reservation exits once  `open`

- **Invariant:** a reservation moves its item's numbers at most
  once on exit, and never after it has ended.
- **Adversity to create:** a retried consume (F3); consume racing
  release, two consumes (F4); consume racing expiry (F23); a late
  consume on an ended reservation (F5); expiry fired early (F24).
- **Area:** the reservation ledger. **Kills covered:** 11, 12, 13,
  14, 16 (our own retry), 19 (early).
- **Folds in:** FC2 (an exit names its own reservation and moves
  exactly what it holds).
- **Presumes:** SL-1 — the reservations being exited were admitted
  truly.

### SL-4 — consume's two moves hold together  `open`

- **Invariant:** no readable state holds one of consume's two moves
  — ending the reservation, lowering the on-hand-count — without
  the other.
- **Adversity to create:** our own death between the two moves
  (F16); an unknowable outcome of the consume write (F19).
- **Area:** the reservation ledger. **Kills covered:** 15, 16
  (silence).
- **Flag riding:** the evidence is kill-mid-work and unknown-outcome
  injection, not hammering — the unusual shape named so the
  consumer inherits it.
- **Presumes:** SL-3 — an exit's identity, so the half-done can be
  told from the done.
- **Judgment logged:** kept apart from SL-3 because the proof
  obligations differ — duplicates collapse (SL-3) versus the
  half-done converges (SL-4) — and the evidence differs with them.

---

## Registry state

4 slices open, SL-1 chosen-next (2026-09-10). New work enters by
re-framing or as a new slice through this registry, never around
it.

## Fold-reconciliation line

20 kills (L4's 1–20, derivation trace in framing/derivation.md,
step 3) ↔ 4 slices + 3 folds + 0 deferrals + the fence remainders:

- 1, 2, 3, 4, 5, 9, 10 → SL-1 · 6, 7, 8 → SL-2 · 11, 12, 13, 14 →
  SL-3 · 15 → SL-4 · 16 → SL-3 (our retry) and SL-4 (our silence)
  · 17 → FC2 in SL-3 · 18 → FC1 in SL-1 · 19 → SL-3 (early) · 20 →
  fenced (W2, V5).
- Folds: FC1 → SL-1 · FC3 → SL-1 · FC2 → SL-3.
- Fence remainders, stopped at L4: the world-truth parts of 7 and
  8 (W1); the orphan halves of 9 and 16 and the never of 19 (W2,
  V5); the identity part of 17 (W5).
- The written zero: no deferrals; no kill dropped; no theorem
  merged; no rider needed beyond the two flags.

## Ordering expectation (re-decided at each close)

SL-1 → SL-2 → SL-3 → SL-4. SL-1 is the promise's headline and the
evidence harness's first adversity (bootstrap proves the harness on
it); SL-2 stands on SL-1 alone and is the promise's other face, the
on-hand-count side; SL-3 needs truly admitted reservations to exit;
SL-4 needs an exit's identity to tell half-done from done.

## Divergences from the briefing (derivation wins, recorded)

None. The working name is untouched — the naming step decides it,
which is not the derivation overriding the briefing.
