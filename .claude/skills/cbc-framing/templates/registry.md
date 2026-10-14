<!-- Copy-and-fill master for the slice-registry export (CBC ADR-0008).
     Fill every <angle> slot; delete
     the guidance lines. Declining this template is off-template:
     the outcomes in the skill's export section still bind — derive
     your own shape from them and record the deviation in the run's
     log. -->

# Slice registry — the work, cut and ready

Framed <date>; every verdict <the reviewer's — or: delegated, per
<the run's record of that decision>>. Source of truth for what to
work next. Ordering is an expectation, re-decided at each slice
close — never assumed from this file's original state.

A slice = one invariant × the adversity its evidence must *create*.
Status values: `open` · `chosen-next` · `in-progress` ·
`closed (date, evidence)`.

---

## <Group heading — a version stage or an adversity class; else delete.
    Groupings are headings for the reader, NEVER a boundary: slices in
    one group share a kind of storm (or a stage), nothing more.>

### SLICE-1 — <the invariant in a few words>  `chosen-next`

- **Invariant:** <what must hold, stated so it can concretely fail>
- **Adversity to create:** <the attack the evidence must generate —
  from the L1 census, not invented>
- **Area:** <owner from L3>. **Kills covered:** <KILL-ids from L4>.
- **Folds in:** <definitions this slice consumes first; or "none">
- **Rider:** <a structural condition checked, never battle-tested,
  whose silent violation voids this slice's evidence; or "none">
- **Flag riding:** <what cannot be staged the normal way — name the
  unusual evidence shape here, so the consumer inherits the warning,
  not the surprise; the slice answers it by name in its
  specification, staged or removed with the removal shown; or
  "none">
- **Presumes:** <the slices this one stands on, and what it takes
  from each; or "nothing">
- **Judgment logged:** <why the slice is cut this way, when the cut
  could be questioned — e.g. why this and SLICE-n are two slices
  sharing one adversity class; or "none">
- **Record:** <a link to the slice's record, once it has one>
- **Answered at close:** <added by the slice's close: one line per
  flag or judgment it settled — the answer in a sentence, and where
  in the record. How the slice got there is the record's, never
  told here. Framing's words above stay as written.>

Every field is present; an empty one says "none", so a reader can
tell empty from forgotten.

### SLICE-2 — <…>  `open`

- **Invariant:** <…>
- **Adversity to create:** <…>
- **Area:** <…>. **Kills covered:** <…>.
- **Folds in:** <…>
- **Rider:** <…>
- **Flag riding:** <…>
- **Presumes:** <…>
- **Judgment logged:** <…>
- **Record:** <…>
- **Answered at close:** <…>

---

## Registry state

<One line, kept current: which slices stand open — or "all N slices
evidence-closed (date)". New work enters by re-framing or as a new
slice through this registry, never around it.>

## Fold-reconciliation line

Every kill the definition's L4 names, accounted for: <N> kills ↔
<n> slices + <m> folds + <deferrals> + <refusals>. One row per
kill; nothing dropped is checked by counting. The definition's L4
is the master for every kill — what dies, against which fact, from
which possession; the middle column here is a summary for
counting, never to be read in its place.

| Kill | What dies | Lands in |
|---|---|---|
| KILL-1 | <a few words> | SLICE-1 |
| KILL-2 | <…> | <SLICE-n; or FOLD-n; or "fenced: NOT-OURS-n"; or "deferred: <why>"> |
| <…every kill, one row> | | |

Folds: <each definition → the first slice consuming it>.

The written zero: <state absences too — "no deferrals; no kill
dropped" is a line, never a silence.>

## Ordering expectation (re-decided at each close)

<SLICE-1 → SLICE-2 → … , with a one-line rationale: what each proof
takes as already standing. Each close re-decides the next and says
why in its own record; here, the order as it stands and a link to
each slice's record — never the story again.>

## Divergences from the briefing (derivation wins, recorded)

<Only if a briefing existed and the derivation overrode it — each
divergence with its because; else delete this section.>
