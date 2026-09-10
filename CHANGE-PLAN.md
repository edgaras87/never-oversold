# Change-plan: Step 1, the framing

## Summary — the state after all commits

The framing stands and Step 1's gate is closed. Under
`docs/system/` the three exports — `intent.md`, `definition.md`,
`registry.md` — each stand alone, composed from the derivation
record at `docs/system/framing/derivation.md`, which holds every
step's earned block and every verdict inline, the reviewer's. One
promise, one sentence, clearing the briefing's six bars; the
rejected candidates banked. The definition filled or empty with
reasons across L1–L5. The registry with one slice chosen-next and
its reconciliation line. The adoption ADR Accepted. README
re-derived from the exports; the records tables list
`docs/system/`; CHANGELOG says what a version is here; the devlog
carries the briefing line. PLAN's Step 1 gate ticked, ready for
the fast-forward.

## Commits

**1. `docs(agent): add change-plan for the framing`**
This plan, agreed.

**2. `docs: frame step 0, the promise`**
Creates the derivation record with its header and the step 0
section: candidates against the six bars, the chosen sentence, the
audience, what done demonstrably means, every rejection banked.
The first thing in the repo that names the problem.

**3. `docs: frame step 1, what must be ours`**
L2: possessions earned by the hostage test, refusals with their
mirror, sketch-enemies noted as step 2's debt.

**4. `docs: frame step 2, the enemy census`**
L1: concrete facts, consequence-first, the probe log recorded,
closed only on two empty probes.

**5. `docs: frame step 3, the collisions`**
L4: facts × possessions, each kill stating what dies and never how
it is saved, invariant-shaped with its adversity named; parked
mechanisms listed as parked.

**6. `docs: frame step 4, the owners`**
L3: divisions only where state and decision authority both
separate; one area is a confirmed answer, refused seams named.

**7. `docs: frame step 5, between owners`**
L5: filled if owners exist, else empty with each reason traced.

**8. `docs: frame step 6, the slice surface`**
The sort, the dedupe, the folds; the slice shape with a presumption
order and a chosen-next.

Steps 2–8 are firm in intent and number; their wording arrives at
each verdict. **Each is a series, not one commit:** a draft
(`docs: draft step N, <name>`), then one revision per reviewer
question that changes something (`docs: revise step N — <what
changed>`, the question in the body), then the verdict commit
carrying the planned subject, which freezes the section. A question
that changes nothing lands as a line in the section's "how it ran"
half, in the next commit that touches it. Between any two of
them, **return trips** may land — zero or more, each a logged
revision to a frozen section, one commit each (`docs: return trip,
<what> revised from <step>`), inserted where they fall. They are provisional by design: this plan
names them so their landing is refinement, not divergence.

**9. `docs: export the intent`**
`docs/system/intent.md`, composed from step 0's earned block under
the residue filter.

**10–14. `docs: export the definition, L2` … `L1`, `L4`, `L3`, `L5`**
`docs/system/definition.md` growing one lived state per commit in
derivation order; an empty-with-reasons layer is still a state and
still a commit.

**14a. `docs: order the definition L1→L5`** *(added 2026-09-10,
divergence)*
The five layer commits appended each layer in derivation order,
leaving the file in that order. The workflow presents L1→L5,
outside-in: the history keeps the derivation, the file gets the
map. Content unchanged, sections moved, the header's order note
rewritten.

**15. `docs: export the registry`**
`docs/system/registry.md` on the skill's template.

**15a. `docs: registry in project voice`** *(added 2026-09-10,
divergence)*
The registry's opening carried the template's own words — the
skill's name, its step number, the delegation slot — and a header
comment naming the template. Project truth reads as project
truth: the reviewer refused any line a reader would need the
agent's arrangement to decipher. Rewritten in the repo's voice;
content unchanged.

**15b. `docs: registry reconciliation as a table`** *(added
2026-09-10, divergence)*
The reconciliation line was a run-on of arrows; the reviewer found
it weak. One row per kill — what dies, where it lands — so
"nothing dropped" is checked by counting rows. Content unchanged.

**16. `docs(adr): adopt the framing`**
ADR-0002, Status: Proposed — the framing adopted as the project's
truth set, the alternatives it closed (the run-repo layout, the
one-doc record) named.

**17. `docs: records catch up on the framing`**
README re-derived from the exports per the projection table, its
records table gaining the `docs/system/` row; CHANGELOG's version
placeholder replaced; devlog entry with the briefing line and this
step's dead ends; TODO triaged; PLAN's Step 1 gate ticked and
marked done, the decision index gaining ADR-0002; ADR-0002 flipped
to Accepted.

**18. `docs(agent): list docs/system/ in the entry file`**
The records-table row in `.claude/CLAUDE.md`. Agent-scoped, so it
cannot share commit 17.

**19. `docs(agent): close change-plan for the framing`**
Deletes this file; the body records what diverged, and lists the
return trips that landed.

Outside the set, after the close: `temp/` deleted (untracked, never
committed) and the branch fast-forwarded into main, on the
reviewer's word.

A session ending mid-set lands a devlog entry as its own commit
(`docs: devlog for the session`) where it falls — a record-moment
the set cannot schedule; provisional, not divergence.

## Decisions taken inside this plan

- **One series per verdict** (revised 2026-09-09 from one commit
  per verdict, before step 0's first commit). The derivation record
  is one file whose sections freeze at the reviewer's verdict; the
  verdict is the last commit of a step's series, the drafts and
  revisions before it are kept, never amended or squashed. The
  reviewer asked for it: a question's effect on the derivation is
  then a diff between two commits, and whether the second try is
  what they want is readable. The history reads as the derivation
  at the grain the derivation actually ran.
- **Exports follow the skill's derivation order,** the default it
  names; five definition commits even where a layer is empty with
  reasons, because the skill counts an empty layer as a lived
  state.
- **The ADR lands Proposed at its export step and flips Accepted in
  the records commit,** per change-plans §4 — the skill puts the
  adoption record after the registry, the convention says Proposed
  until the last boundary; both hold.
- **The gate closes on the branch.** Commit 17 ticks every gate
  item including the branch item, on the reviewer's word to merge
  given at that boundary; the fast-forward follows the close
  immediately, so the tick is true by the time main carries it.
- **The briefing stays in `temp/`, untracked, until the merge.** The
  derivation record's step 0 takes from it what the intent needs
  and names it as the input; the file itself is the reviewer's, like
  the pre-briefing was, and goes the same way.
- **The definition is reordered after its growth** (added
  2026-09-10). "Growing one lived state per commit" was read as
  appending; the presentation order is the map's, L1→L5. One extra
  commit rather than rewriting the five that landed — history is
  the derivation and stays.
- **Exports carry no agent language** (added 2026-09-10, the
  reviewer's rule). The three artifacts under `docs/system/` are
  project truth: nothing in them names a skill, a step of the
  agent's workflow, or the delegation arrangement. What the
  template put there is a fold-back for the bundle, filed in TODO.
- **Return trips are named provisional steps, not divergence.** The
  method calls them its normal working; a plan revision per return
  trip would cost a commit for what the close body can list.
