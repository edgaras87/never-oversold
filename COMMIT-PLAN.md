# Commit plan: the writing pass — the shape, its gate, its hand-offs

<!-- The convention: .claude/skills/commit-plan. Deleted at the close. -->

## Summary — the state after all commits

SL-2's record reads without being unpacked aloud, and the shape it
arrived at is written down as a model of its own — controlling two
sections and saying so, leaving the rest to each slice. Every slice
step from Step 7 on carries a gate item reading its record against
that model before the merge. The slice skill gains the step in
general form, carrying no shape of its own, so the mechanism can
travel while this project's answer stays here. Two hand-offs stand
in TODO: the shape offered to the bundle as a baseline, and a
request that baselines be delivered at a gate rather than shipped.

Written **after** the work rather than before it, which the
convention asks the other way round. The set grew out of a
conversation that kept turning up the next question, and the plan
is being opened now because the reviewer wants each remaining
boundary to be a place to object. Steps 2 onward are unlanded and
may change; step 1 is the only one whose content is settled.

## Commits

**1. `docs: reshape SL-2's guarantees and owners`**
The two sections that could not be read without help. Labelled
parts, a `*Say:*` line with real numbers, a rule between blocks,
and the owners table gone — a four-column row measured 435
characters on one line. This one stands on its own: it is the
material every later step describes.

**2. `docs: model the shape a slice record's two sections take`**
`docs/construction/slice-record-shape.md`, alone. Derived from
step 1's result, not from a guess. Controls the guarantees and the
owners and says why only those two — they are the two that were
written, read, found wanting and rebuilt. Carries the two
comparisons and the baseline lifecycle.

**3. `docs: gate the shape check at every slice close`**
PLAN: the gate item that every slice step from Step 7 carries, and
the retrospective item narrowed to the giving-back direction, since
reading other projects' baselines happens after the first slice
rather than at the end.

**4. `docs: offer the shape as a baseline, ask for gate delivery`**
TODO's two hand-offs. Provisional in one respect: the reviewer has
not yet agreed every line of the lifecycle, and this step's wording
may change at its boundary.

**5. `chore(agent): check a slice record against its project's shape`**
The skill edit, the decisions entry, and the records-table row.
Agent paths only. The step the skill gains is general; the shape it
compares against is the project's own, which is what lets this
travel upstream without handing anyone an answer.

**6. `docs(agent): the drafts behind the writing pass`**
`temp/`: the decide-first questions, the option comparison on the
rule's wording, the visual comparison on the Owners shape, and the
four rendered shapes. Kept because the reasoning is the evidence
that the choices were built rather than argued.

**7. `docs(agent): close the commit plan for the writing pass`**
Deletes this file; the body records what diverged.

## Decisions taken inside this plan

Named here so each can be objected to at its own boundary rather
than all at once.

- **The model controls two sections, not the record.** SL-1 and
  SL-2 do not carry the same sections and should not.
- **The shape stays in this project; only the step travels.** A
  shape shipped with the skill would be inherited rather than
  derived, and would make the step it belongs to decorative.
- **Baselines are read after the first slice, not at the end.**
  Inside a project the records inherit from each other anyway, so
  the first record is the only independent sample and the rest of
  the project may as well have the benefit.
- **A provisional baseline is requested at a gate and deleted
  after.** A file in the working tree is read during the work
  whether or not anyone meant it to be.
- **The temp drafts are committed rather than discarded.** The
  `decide-first` and comparison playbooks both say to delete the
  draft once the answers are recorded; kept here because the
  reviewer asked to be able to diff the reasoning, and because this
  set's answers live in several places rather than one ADR.
