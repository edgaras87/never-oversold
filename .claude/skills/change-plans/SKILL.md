---
name: change-plans
description: How work larger than one commit is planned, reviewed at each boundary, and closed. Use before starting a change set that needs more than one commit.
delivery: pushed
requires: commit-messages, artifact-kinds, project-recording
---

# Change-Plans Convention

How a body of work that needs more than one commit is planned before it
starts, reviewed as it lands, and closed. The unit is one **change
set** — the scope between a single commit (`commit-messages`) and a
whole project (`project-recording`).

<!-- The artifact is a change-plan: agreed up front, committed, revised
     only when reality diverges, deleted at the close. Its content
     survives as the commit messages it produced; the file itself is
     recoverable from history. See ADR-0010. -->

---

## 1. When a change-plan is needed

Write one when the work is **worth splitting into more than one
commit**. A single-commit change gets none — the plan costs two extra
commits, and that only pays for itself across a sequence.

Reach for one especially when the change set is large enough that you
would otherwise discover halfway through that the split was wrong, or
when someone other than the author is reviewing as it lands.

## 2. The artifact

One file, `CHANGE-PLAN.md`, at the repo root. One at a time — a second
concurrent change set means the first should have been finished or
abandoned.

Root placement is deliberate: an in-flight change set is then visible
from a clean clone, so "is work half-landed, and where did it stop?"
is answerable without reading the working tree.

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

Commit subjects follow `commit-messages`. The per-step note is not the
commit body — it is why this step is a step. The body gets written at
commit time.

**The list may roll.** Steps near at hand are firm; steps past the
decision horizon — whatever a not-yet-seen result must shape — are
provisional, and the plan marks them so. A provisional step still
names its intent; what it defers is the wording and the exact split.
Refining it is §5's revision commit doing planned work, not a
recorded failure (ADR-0027).

**Status is not tracked in the file.** No checkboxes, no `[x]`. Git
history is the status: the commits that exist are the steps done.
Tracking status here would couple every work commit to a plan edit,
breaking atomicity, and would reintroduce the staleness that makes a
neglected `PLAN.md` lie.

## 3. How steps are split

**By change, not by artifact.** One step is one coherent thing the repo
needs, whatever number of files that touches. One file may appear in as
many steps as it has distinct changes to make.

The test is **revert**: undo a single step, and the repo must land in a
coherent state.

- A convention document and the README row listing it are *one* step —
  reverting the document alone would leave the table pointing at a
  missing file.
- Adding a plan, revising it on divergence, and deleting it at the
  close are *three* steps on the same file — three different needs.

This is the `commit-messages` rule "one logical change per commit"
applied ahead of time instead of at commit time. The failure mode it
prevents is grouping by file type — "all the doc files," "all the
record files" — which produces commits that look tidy and revert
incoherently.

**Order follows where the decision lives** (ADR-0027). A decision
settled in conversation runs decision-first: record it, then
implement it. A decision only seeable in the material runs
material-first: touch the artifact, let the shape emerge, and write
the durable record from what held. One plan may mix both — the
choice is per-decision, not per-plan. Two constraints bound both
directions: no commit references what does not yet exist, and
durable records are caught up before the close — mid-series
disagreement between artifact and convention is the mode working;
disagreement at the close is a bug.

**The records steps are planned, not remembered.** A change set
batches record-moments: mid-set, a record's trigger can fire before
its truth exists — a changelog entry before the shape has held, an
ADR before the decision is seeable. So when drafting the commit
list, walk the repo's records table (`project-recording`): every
record whose moment this set will create gets a planned step,
placed at the boundary where its truth exists — the same move as a
gate item on the step that made it true (ADR-0025), at set scope.
Which records exist and when each fires stays the table's
knowledge, not this convention's: the walk is the rule here, the
rows are not, and a repo without some row (no architecture to map,
say) simply has nothing to walk there. The final records commit
(§4) is where the last of them usually land. Leaving records to be
noticed at the close works exactly as long as judgment is present,
and nothing catches the run where it is not.

The same walk covers the plan's gate. A change set that will close a
`PLAN.md` gate item names the commit that closes it — the item is a
record-moment like any other, and a close with no step to land in
lands as a divergence.

## 4. Lifecycle

| Step | Commit | Contents |
|---|---|---|
| Open | `docs(agent): add change-plan for <X>` | the approved plan |
| Work | the steps themselves | as planned; stop at each boundary |
| Diverge | `docs(agent): revise change-plan — <what changed>` | only when reality diverged; body says why |
| Close | `docs(agent): close change-plan for <X>` | deletes the file; **body records what diverged** |

The plan is committed **after** it is agreed, before any of the work.
That timestamps the agreement, so the series can be read against its
own plan afterwards.

The closing commit's body is the change set's retrospective: what
diverged from plan, and why. It is the cheapest retro that exists and
it is greppable. If nothing diverged, say so — that is information
too. An abandoned change set closes the same way, with the reason.

**ADRs inside the set default to Status: Proposed** (ADR-0027). A
Proposed ADR is a living document: it lands early so the options are
on the table before anything is built, gathers evidence at the
boundaries, and flips to Accepted in the set's final records
commit — never in the close commit, which is agent-scoped and
touches nothing else — once the retro is the last evidence in.
Committing an ADR Accepted early is the marked case: it carries the
claim that no later boundary can contradict it. An abandoned set
leaves its ADRs Proposed, with the close commit saying why.

## 5. Divergence

When a step turns out to need something other than what was planned:

1. Stop before committing it.
2. Re-evaluate the **remaining** steps — a changed step often
   invalidates later ones, and the whole point of the plan is that the
   tail is no longer trustworthy on its own.
3. Revise `CHANGE-PLAN.md` and commit the revision on its own, with a
   body explaining what forced it.
4. Continue.

The revision commit is what makes a divergence first-class: a diff
between the commits it separates, rather than an invisible mental
adjustment nobody can reconstruct later.

Refining a provisional step (§2) travels the same road — stop,
revise, continue — but is planned refinement, not divergence: the
plan said the wording would arrive at this boundary, and it did.
True divergence remains a firm step going otherwise. The closing
commit's body distinguishes the two.

## 6. Review protocol

Work stops at every commit boundary. The rule is commit-messages' —
stage, show the diff, commit only on the reviewer's word — and it
binds at every commit, not only inside a change set; the arrangement's
settings file makes the stop a gate (ADR-0035). The reviewer inspects
the actual diff before it lands, and may ask for an explanation of any
part of it before agreeing to continue.

Why it matters most here: when an agent is doing the committing,
the boundary is the only place where a misunderstanding is cheap to
catch. Without it, a wrong assumption in step 2 propagates silently
through every later step, and the review becomes an archaeology
exercise.

## 7. Anti-patterns

- **Grouping steps by file type.** Tidy-looking, reverts incoherently.
  See §3.
- **Checkboxes in the plan.** Status belongs to git history; a plan
  that tracks it will go stale and start lying.
- **Writing the plan after the work.** Then it is a summary, not an
  agreement — nothing was reviewable, and divergence is unrecorded.
  (A provisional tail is not this failure: its intent was agreed up
  front and its refinement reviewed at a boundary.)
- **An ADR committed Accepted whose shape a later boundary decides.**
  The record claims settled what the plan itself admits is pending;
  Proposed is the honest status until the shape is seen (§4).
- **Plans for single commits.** Ceremony that outweighs the change.
- **Keeping the file after the close.** It becomes a stale duplicate of
  facts that now live in the commit messages. Delete it; history keeps
  it.
- **Silent divergence.** Quietly doing something other than the plan
  and fixing the plan afterwards, or not at all.

## 8. Relation to other records

**Not a `PLAN.md`.** A change-plan is the artifact-kinds `plan` kind
specialized to change-set scope (permitted by artifact-kinds §3:
contexts may specialize, not contradict). The two differ absolutely in
lifetime — `PLAN.md` is durable and carries live status; a change-plan
is ephemeral and carries none — which is a distinction the artifact-
kinds axes do not currently express.

**Not a record.** It is a scaffold. Everything durable in it survives
as commit messages; nothing about it is meant to be read a year later
except through `git log`. That is why it is not part of
`project-recording`, whose records are all append-or-evolve (ADR-0010).

**Feeds the devlog.** A divergence worth remembering beyond the change
set — a dead end, a wrong assumption about the codebase — gets promoted
to a devlog entry. The closing commit body is per-change-set; the
devlog is per-project.

## 9. Origin

The practice is the kernel and git mailing-list **patch series** —
one logical change per patch, ordered so each applies on the last,
reviewed patch by patch rather than as a lump — carried into a repo
where work is committed directly instead of mailed. What is added here
is the plan agreed *before* the series is written, and its disposal
afterwards.

See ADR-0010 for why this is a separate convention, and for the
options rejected.

---

## Delivery

`pushed` — it fires when work turns out to need more than one commit.

That trigger is weaker than it looks. Recognising that a change set is
large is a judgment, and a convention firing on judgment misses exactly
when the judgment fails. Nothing catches the miss, which is why "the
plan written after the work" is an anti-pattern here (§7) rather than
a lapse.

**One ambient line softens it.** The entry file's records table
carries a row for `CHANGE-PLAN.md` — the moment "work needs more than
one commit", the path — read at the start of every session, before
the judgment is made (ADR-0018: a row names a moment, not a rule, and
that is what the table is for). It is not a second copy of this
convention; it is the trigger placed where the skill's description
alone did not reach.

**What this constrains.** It has to be usable at the moment it fires —
mid-task, before anything is committed — so a cross-reference is a cost
paid then, not while studying the document. And any rule here that
delegates to another convention belongs in `requires` (ADR-0017).
