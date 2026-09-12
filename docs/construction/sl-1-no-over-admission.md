# SL-1 — no over-admission under contention

<!-- The slice's record, grown in three movements: the correctness
     specification (what must hold, against what, and what proof
     looks like — no mechanism), then the plan (one structural owner
     per guarantee, and why it beats the named adversity), then the
     evidence as delivered (which test creates which adversity, and
     what it read from the store). Invariant and adversity are the
     registry's and the definition's, as written; guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each movement. -->

## §1 Invariant

For every item, in every readable state, the sum of active
reservations ≤ on-hand-count. (`docs/system/registry.md`, SL-1;
concern A in the definition's L4.)

*Active* is FC3's: a reservation not ended by an exit and not past
its expiry, as judged by one clock, not one per instance. *Readable
state* is the intent's: true at every read from outside, not
eventually.

## §2 Adversity

Concurrent admits on one item's last units. Each request fits
alone; together they do not. Pulled from the definition's L1 as
written:

- from many callers at one instant (F1);
- from more than one of our own instances (F17), any belief an
  instance holds in memory about the count being a lie;
- against a store that shows two checks the same count (F21) or a
  count no longer current (F22);
- against an operator's downward change to the count arriving at
  the same instant (F10);
- with an admit decided but not yet recorded (F15);
- under instances whose clocks disagree about which reservations
  are active (F18).

Kills covered: 1, 2, 3, 4, 5, 9, 10. What this slice does not
face: an honest downward correction under the reserved sum with no
race (SL-2); exits and their races (SL-3, SL-4); a retried reserve
(W2, fenced: it over-holds, it cannot oversell).

## §3 Guarantees — derived by attack

Each is one answer to: *what would let §1 hold on paper yet break
in fact?* The attack runs until no new answer comes. Every
guarantee is a property; none names how it is held.

- **G1. The admit is decided against the truth at the moment of
  recording.** Attack: read the count and the sum, find the units
  fit, then record — and between the finding and the recording
  another admit lands on the same units (F1, F21, F22). Guarantee:
  an admit that would take the sum past the count cannot be
  recorded; the finding and the recording are one act against the
  store's state as it is then, never two acts against a copy.
  Kills 1, 3, 4.
- **G2. The decision holds across instances as it holds within
  one.** Attack: an instance decides on a count it holds in memory
  (F17). Guarantee: no state an instance holds outside the store
  takes part in the admit; two instances admitting at once are the
  same adversity as two callers on one instance, with the same
  outcome. Kill 2.
- **G3. An admit and a change to the count on one item do not
  interleave.** Attack: an operator lowers the count while an admit
  is checking against the old one; both pass (F10). Guarantee: on
  one item, an admit and a change to the count are ordered — each
  sees the other's effect or is seen by it. No admit is recorded
  against a count a concurrent change has already replaced; no
  change to the count is recorded under a sum an admit has already
  raised. A downward change that would set the count under the
  reserved sum is not admitted in this slice (provisional, per
  ADR-0011; SL-2 decides its shape). Kill 5.
- **G4. A decision exists only as a record.** Attack: an instance
  decides "admitted" and dies before recording, or records later —
  a decision no other admit can see (F15). Guarantee: there is no
  decided-but-unrecorded admit. An admit is admitted at the instant
  its reservation is recorded and not before; every reply that says
  admitted names a reservation the store holds. The other half —
  recorded but never replied — is an orphaned hold, fenced (W2,
  V5). Kill 9. *Kill 9's shape, decided:* because the decision and
  the record are one instant, there is no interval to kill, and no
  kill-mid-work evidence is owed here. Should the plan put any act
  between deciding and recording, this guarantee re-opens as a
  kill-mid-work evidence, and the plan must say so.
- **G5. Active is judged by one clock.** Attack: instance A's clock
  says a reservation has expired, so its sum is smaller and it
  admits; instance B still counts it (F18). Guarantee: whether a
  reservation is active is judged, for every instance, by the one
  clock all instances share — the store's own, the one thing on the
  runtime ground that outlives and is common to every instance —
  and a reservation's expiry instant is set against that same
  clock. No instance's own clock enters either. Kill 10. *Kill 10's
  shape, decided:* FC3 removes it. The evidence does not stage a
  controlled clock; it shows the one-clock judgment is the one used
  by showing no other clock is consulted, and by reading activeness
  from the store against the store's clock.
- **G6. Nonsense never reaches the decision.** Attack: a quantity of
  zero, negative, or beyond any bound; an unknown item (F6).
  Guarantee: only a request naming a known item and a positive
  whole quantity within a stated bound reaches the admit; anything
  else is answered invalid and the numbers do not move. Kill 18,
  folded (FC1).

The attack ran dry at six: a seventh answer — "the reply lies about
the outcome" — is the caller's view, refused at L2 and covered at
its edge by G4.

## §4 Evidence criteria

Each guarantee's evidence *creates* its adversity through the real
door and reads the witness from the store, from outside every
instance. The witness is §1 itself: for the item, the sum of active
reservations and the on-hand-count, read from persisted state.

- **E1 (G1).** One item holding fewer units than a storm of
  concurrent reserve requests together ask for; the requests held
  at a line and released at one instant. Witness after: sum ≤
  count. The storm shown real: the total asked exceeds the count,
  and at least one request was refused (ADR-0010's status). The
  admitted requests account for exactly the sum the store holds.
- **E2 (G2).** E1's storm spread across at least three instances of
  the ledger running as separate processes against one store, the
  requests released at one instant across all of them; the served
  instances shown distinct. Same witness, read by none of them.
- **E3 (G3).** Reserve requests racing a downward change to the
  same item's count, released together, repeated. Witness: sum ≤
  count in every read; and the change's answer agrees with the
  state — admitted and the count is the new value with the sum
  under it, or refused and the count is the old one.
- **E4 (G4).** Riding on E1 and E2: every reply that says admitted
  names a reservation present in the store, and the sum the store
  holds is the sum of exactly those. Owed as kill-mid-work evidence
  only if the plan opens an interval (§3, G4).
- **E5 (G5).** A reservation past its expiry by the store's clock is
  not counted in the sum; one within it is; the persisted expiry
  instant is the store's assignment. And structurally: the
  application's own code consults no process clock on the admit
  path or in judging activeness — shown by a test that reads the
  code, not the runtime.
- **E6 (G6).** Each nonsense shape sent — zero, negative, beyond the
  bound, an unknown item, a body that does not parse — answered
  invalid with the status ADR-0010 names, and the item's numbers
  unchanged after.
- **In every readable state.** During E1, E2 and E3, a reader
  outside the instances samples the witness while the storm runs,
  not only after; every sample satisfies §1.
- **The harness can fail (R5).** Before any wall lands, E1 and E2
  run against the admit written the naive way — read, check, write
  — and go red, the witness showing an oversell; recorded from
  actual output. The same tests go green when the wall lands and
  nothing in them changes.

All of it under the one standard test command, nothing exported,
the ground not required up.

## §5 Folds, consumed here

- **FC1, what a valid request is:** a known item (ADR-0011: one with
  a count), a positive whole quantity within a stated bound. G6.
- **FC3, what active means:** not ended by an exit, not past expiry
  as judged by one clock. G5. A reservation carries an expiry
  instant from its admission; how long a hold lasts is the caller's
  to state (L2 refuses the duration policy), positive and within a
  stated bound; the ending of expired holds as an exit is SL-3's.

## §6 What this slice does not claim

- What the ledger does with an honest downward correction under the
  sum, absent a race — SL-2. G3's refusal is provisional.
- That a retried reserve makes one hold — W2, refused.
- That a reply reaches the caller — the caller's view, refused.
- Who may reserve or adjust — W5, T3.
- Fairness among racers — W3. Which requests win is not asserted;
  that the losers lose is.

## §7 Sign-offs

<!-- Dated lines, the reviewer's: the specification before the plan,
     the plan before the build. -->

- 2026-09-12 — the specification (§1–§6) signed by the reviewer.
