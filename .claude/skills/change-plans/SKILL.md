---
name: change-plans
description: How work larger than one commit is planned, reviewed at each boundary, and closed. Use before starting a change set that needs more than one commit.
requires: commit-messages, artifact-kinds, project-recording
---

# Change Plans

A **change set** is a body of work that needs more than one commit.
It is planned before it starts, reviewed as it lands, and closed.

## 1. When a change-plan is needed

Write one when the work is worth splitting into more than one
commit. A single-commit change gets none: the plan costs two extra
commits, and pays for itself only across a sequence. Reach for one
especially when the split could turn out wrong halfway through, or
when someone other than the author reviews as it lands.

## 2. The artifact

One file, `CHANGE-PLAN.md`, at the repo root, one at a time:

```markdown
# Change-plan: <what this change set does>

## Summary — the state after all commits
<Prose. The end state, not the steps. What the repo looks like once
this is done, and what it gives us that it did not have before.>

## Commits

**1. `<type>(<scope>): <subject>`**
<What this commit does, or what it gives us, or both — whatever tells
the reader why this step exists as its own step.>

**2. `<type>(<scope>): <subject>`**
<…>

## Decisions taken inside this plan
<Judgment calls made while planning that a reviewer should see and
could reasonably object to. Optional, but usually the most useful
section.>
```

- Commit subjects follow commit-messages. The per-step note says
  why the step is a step; the commit body is written at commit time.
- **The list may roll.** Steps near at hand are firm. Steps past
  the decision horizon, whatever a not-yet-seen result must shape,
  are provisional and marked so; a provisional step names its
  intent and defers its wording and exact split.
- **No status in the file.** No checkboxes. The commits that exist
  are the steps done.

## 3. How steps are split

- **By change, not by artifact.** One step is one coherent thing
  the repo needs, whatever files that touches; one file may appear
  in as many steps as it has distinct changes.
- **The test is revert:** undo one step, and the repo lands in a
  coherent state. A module and the ARCHITECTURE paragraph mapping
  it are one step; adding a plan, revising it and deleting it are
  three.
- **Order follows where the decision lives.** A decision settled in
  conversation runs decision-first: record it, then implement it. A
  decision only seeable in the material runs material-first: touch
  the artifact, let the shape emerge, write the durable record from
  what held. One plan may mix both, per decision. Two constraints
  either way: no commit references what does not yet exist, and
  durable records are caught up before the close.
- **Plan the records steps.** Walk the entry file's records table:
  every record whose moment this set will create gets a step, at
  the boundary where its truth exists. A set that closes a
  `PLAN.md` gate item names the commit that closes it.

## 4. Lifecycle

| Step | Commit | Contents |
|---|---|---|
| Open | `docs(agent): add change-plan for <X>` | the approved plan |
| Work | the steps themselves | as planned; stop at each boundary |
| Diverge | `docs(agent): revise change-plan — <what changed>` | only when reality diverged; body says why |
| Close | `docs(agent): close change-plan for <X>` | deletes the file; **body records what diverged** |

- Commit the plan after it is agreed and before any of the work.
- The close commit's body is the set's retrospective: what diverged
  and why. If nothing diverged, say so. An abandoned set closes the
  same way, with the reason.
- **An ADR inside the set opens as Proposed** and flips to Accepted
  in the set's final records commit, never in the close commit. An
  ADR committed Accepted early claims that no later boundary can
  contradict it. An abandoned set leaves its ADRs Proposed.

## 5. Divergence

When a step needs something other than what was planned:

1. Stop before committing it.
2. Re-evaluate the remaining steps.
3. Revise `CHANGE-PLAN.md` and commit the revision on its own, with
   a body saying what forced it.
4. Continue.

Refining a provisional step travels the same road and is planned
refinement, not divergence; the close body distinguishes the two.

## 6. Review protocol

Work stops at every commit boundary: stage, show the diff, commit
only on the reviewer's word. The rule is commit-messages' and binds
at every commit, not only inside a change set.

## 7. Anti-patterns

- **Steps grouped by file type.** Tidy-looking, reverts incoherently.
- **Checkboxes in the plan.** Status belongs to git history.
- **The plan written after the work.** A summary, not an agreement;
  nothing was reviewable and divergence is unrecorded. A provisional
  tail refined at its boundary is not this.
- **An ADR committed Accepted whose shape a later boundary decides.**
- **A plan for a single commit.**
- **The file kept after the close.** History keeps it.
- **Silent divergence.** Doing something other than the plan and
  fixing the plan afterwards, or not at all.

---

## Decisions

- HANDBOOK ADR-0010 — a separate convention; the plan is a scaffold,
  not a record
- HANDBOOK ADR-0025 — a set that closes a gate item names the commit
- HANDBOOK ADR-0027 — order follows where the decision lives;
  provisional tails; ADRs open Proposed
- HANDBOOK ADR-0035 — the stop at every boundary is commit-messages'
