---
name: commit-plan
description: How work larger than one commit is sequenced into commits, reviewed at each boundary, and closed. Use before starting a change set that needs more than one commit.
requires: commit-messages, project-recording
foundation: the commit-plan convention
---

# Commit Plan

A **change set** is a body of work that needs more than one commit.
Its commits are planned before it starts, reviewed as they land,
and the plan is closed.

**It plans the commits, not the change.** What is being changed is
settled before the plan is opened — in discussion, or in a record.
When it turns out not to have been, §5 revises the plan and the
set's ADR is still Proposed.

## 1. When a commit plan is needed

Write one when the work is worth splitting into more than one
commit. A single-commit change gets none: the plan costs two extra
commits, and pays for itself only across a sequence. Reach for one
especially when the split could turn out wrong halfway through, or
when someone other than the author reviews as it lands.

## 2. The artifact

One file, `COMMIT-PLAN.md`, at the repo root, one at a time:

```markdown
# Commit plan: <what this change set does>

## Summary — the state after all commits
<Prose. The end state, not the commits. What the repo looks like once
this is done, and what it gives us that it did not have before.>

## Commits

**1. `<type>(<scope>): <subject>`**
<What this commit does, or what it gives us, or both — whatever tells
the reader why this commit exists as its own commit.>

**2. `<type>(<scope>): <subject>`**
<…>

## Decisions taken inside this plan
<Judgment calls made while planning that a reviewer should see and
could reasonably object to. Optional, but usually the most useful
section.>
```

- Commit subjects follow commit-messages, and are counted against
  its limit when the plan is written, not when each commit lands:
  a subject planned too long lands too long, or is rewritten at its
  boundary with nothing in the plan saying why. The note under each
  commit says why it is a commit of its own; the commit body is
  written at commit time.
- **The list may roll.** Commits near at hand are firm. Commits
  past the decision horizon, whatever a not-yet-seen result must
  shape, are provisional and marked so; a provisional commit names
  its intent and defers its wording and exact split.
- **No status in the file.** No checkboxes. The commits that exist
  are the plan done so far.

## 3. How the commits are split

- **By change, not by artifact.** One commit is one coherent thing
  the repo needs, whatever files that touches; one file may appear
  in as many commits as it has distinct changes.
- **The test is revert:** undo one commit, and the repo lands in a
  coherent state. A module and the ARCHITECTURE paragraph mapping
  it are one commit; adding a plan, revising it and deleting it are
  three.
- **Order follows where the decision lives.** A decision settled in
  conversation runs decision-first: record it, then implement it. A
  decision only seeable in the material runs material-first: touch
  the artifact, let the shape emerge, write the durable record from
  what held. One plan may mix both, per decision. Two constraints
  either way: no commit references what does not yet exist, and
  durable records are caught up before the close.
- **Plan the records commits.** Walk the entry file's records
  table: every record whose moment this set will create gets a
  commit, at the boundary where its truth exists. A set that closes
  a `PLAN.md` gate item names the commit that closes it. A change to
  the agent's own files — a skill or rule corrected, a convention
  exposed, its decisions entry — is a commit of its own, never
  folded into a records commit: commit-messages keeps the two
  apart, and a records commit planned to carry one has to be split
  when it opens.

## 4. Lifecycle

| When | Commit | Contents |
|---|---|---|
| Open | `docs(agent): add commit plan for <X>` | the approved plan |
| Work | the planned commits | as planned; stop at each boundary |
| Diverge | `docs(agent): revise commit plan — <what changed>` | only when reality diverged; body says why |
| Close | `docs(agent): close commit plan for <X>` | deletes the file; **body records what diverged** |

- Commit the plan after it is agreed and before any of the work.
- The close commit's body is the set's retrospective: what diverged
  and why. If nothing diverged, say so. An abandoned set closes the
  same way, with the reason.
- **An ADR inside the set opens as Proposed** and flips to Accepted
  in the set's final records commit, never in the close commit. An
  ADR committed Accepted early claims that no later boundary can
  contradict it. An abandoned set leaves its ADRs Proposed.
- **Before the close, sweep for every name the set moved.** When a
  commit renamed or renumbered anything, grep live text for each old
  identifier — all of them, not the one being described. A
  renumbering moves every number, so the search is the whole span;
  the cheapest form is every old value at once. Grep the text
  unwrapped, its lines joined and its spaces squeezed —
  `tr '\n' ' ' | tr -s ' '` — since a name the file breaks across
  two lines is invisible to a line-by-line grep. What is history
  stays; what points at the old name from live text is the defect.

## 5. Divergence

When a commit needs something other than what was planned:

1. Stop before committing it.
2. Re-evaluate the remaining commits.
3. Revise `COMMIT-PLAN.md` and commit the revision on its own, with
   a body saying what forced it.
4. Continue.

Refining a provisional commit travels the same road and is planned
refinement, not divergence; the close body distinguishes the two.

## 6. Review protocol

Work stops at every commit boundary: stage, show the diff, commit
only on the reviewer's word. The rule is commit-messages' and binds
at every commit, not only inside a change set.

## 7. Anti-patterns

- **Commits grouped by file type.** Tidy-looking, reverts incoherently.
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

- CBC ADR-0038, 1b — a separate convention; the plan is a scaffold,
  not a record
- CBC ADR-0038, 1d — a set that closes a gate item names the commit
- CBC ADR-0038, 1e — order follows where the decision lives;
  provisional tails; ADRs open Proposed
- CBC ADR-0038, 1f — the stop at every boundary is commit-messages'
- CBC ADR-0049, 1 — a commit plan counts commits; "step" is
  `PLAN.md`'s
- CBC ADR-0049, 3 — the sweep greps the text unwrapped
