# Polish: the overview

<!-- The polish's briefs, tracked here from pass 1's opening (they
     were drafted in temp/). The polish stopped after pass 1
     (2026-10-11); the folder stays as pass 1's record and the
     ideas of the passes not run. Whether it stays past Release is
     decided then; git history keeps every brief and report. -->

## Why

1.0.0 is what a stranger reads. Before it is cut, the repo should
read true, and read once. Release waits until the polish is done.

The polish stopped after pass 1, on the reviewer's word
(2026-10-11). What passes 2 and 3 meant to do, the reviewer goes
through by hand instead: the repo part by part, the agent guiding
and suggesting. Their sketches stay below as ideas, not plans.

## The passes

| Pass | Brief | Job | Status |
|---|---|---|---|
| 1. Clean-up | `pass-1-clean-up.md` | Make every line true and put it in the right place | closed (2026-10-10) |
| 2. Skills and rules | `pass-2-skills-and-rules.md` | Correct the skills and rules from what this project lived | stopped (2026-10-11) |
| 3. Improve | `pass-3-improve.md` | Make the repo read well: wording, explanations, structure, references, test comments | stopped (2026-10-11) |

Status moves: sketch → drafting → open (date) → closed (date); a
pass not run is stopped (date).

Why this order:
- Improving a stale line is wasted work, so clean-up goes first.
- The skills and rules govern every pass after them: how commits
  are planned and written, how records are shaped. Fixed second,
  they are in force for the improve pass and for Release.
- Improving goes last, because it touches the most text, and it
  should touch text that is already true and governed by corrected
  rules.

## Where these files live

- **Now:** `temp/polish/`, untracked drafts.
- **From pass 1's opening:** `.claude/polish/`, tracked. The first
  commit on pass 1's branch adds the folder (`docs(agent)`), so a
  brief never points at a file git does not hold.
- **At each pass's opening:** that pass's brief is finished and its
  status set to open, in the pass's first commit.
- **At pass 3's close:** the folder is deleted. Git history keeps
  every brief and report.

## Rules every pass follows

What a pass's brief says overrides this section only where it says
so by name.

These rules are on trial: written before any pass, so guesses. They
change only from what a pilot or a pass showed, each change with a
dated line under *Changes* below saying what showed it.

### Freedom

- Each pass works on its own branch, `polish-<n>-<name>`, cut from
  main after the pass before it has merged.
- Commit freely, without stopping. commit-messages still holds: one
  change per commit, no commit touching both agent and project
  paths.
- One commit per finding, or per group of findings of one kind in
  one file. The reviewer can then drop any single commit.
- Never merge, never push.

### Never touched, in any pass

- `docs/concept/`. A lesson about a chapter is a line under TODO's
  *To the deliverer*.
- History: devlog entries, `.claude/decisions.md` entries, the body
  of an Accepted ADR, a closed PLAN step's gate, CHANGELOG's
  released versions. A wrong fact in history is fixed by a new
  dated line that corrects it, never by editing the old one.
- `docs/system/`: proposals only. The exports change by a dated
  revision entry, on the reviewer's word.
- Behaviour: no change to what the code does, and no test removed
  or weakened. `./mvnw test` runs after every commit that touches
  code.

### When a pass stops

At the first of these:

- **Done:** every file in the pass's inventory is read, and every
  finding is fixed, listed for the reviewer, or dropped with a
  reason.
- **Re-check dry:** the one re-check of the changed files finds
  fewer than 3 new items. Those go in the report; there is no
  second re-check.
- **Budget:** the commit and agent caps in the pass's brief. What
  remains goes in the report as not done.
- **Red:** a change turns `./mvnw test` red and one fix attempt does
  not bring it back. The commit is reverted, the pass stops, and the
  report says where.

A pass never stops to ask. A question becomes a "for the reviewer"
line, and the work moves on.

### Guarding tokens

- Each file is read once, by one agent. The re-check reads only the
  files that changed.
- Finding agents are read-only and run on a cheaper model (Sonnet).
  The sort and the writing run on the main model.
- An agent returns its findings list, never the files it read.
- Each pass's agent cap is in its brief. A pilot's agents do not
  count against it.
- **Pilot first:** each pass starts on one small area, then stops and
  reports the cost. The rest runs only on the reviewer's word. The
  pilot area is named in the pass's brief.

### The report

`pass-<n>-report.md`, beside the brief, read by the reviewer before
the branch is:
- **Changed:** each commit, one line: what and why.
- **For the reviewer:** each finding left open, with the proposed
  fix.
- **Dropped:** each finding dropped, with the reason.
- **Not done:** whatever the budget cut off.
- **Cost:** agents run, and tokens if the workflow reports them.

### Review and close

The reviewer reads the report, then the branch, and drops any
commit they do not want. The "for the reviewer" items are decided
together, one decision at a time, each decision covering every item
of its kind. The decisions are applied on the branch.

Then the branch is regrouped before it lands: its many small
commits are kept on a local branch, `<branch>-detail`, and the
branch itself is reset onto main and committed again as a few
commits by kind — agent files apart from project files, as
commit-messages requires. The report is the record of each small
fix. The devlog gets one entry for the pass. The overview's status
moves to closed, and the branch fast-forwards into main on the
reviewer's word.


## Changes

<!-- Dated lines only: what changed in these rules, and what in a
     pilot or a pass showed it. -->

- 2026-10-10 — The pilot's agents do not count against a pass's
  agent cap. Pass 1's pilot showed the cap was set before the files
  were counted. Pass 1's brief changed with it: one agent per area,
  not per file; each agent also returns the facts its files state,
  compared across areas, so a fact repeated in two areas is caught;
  and an ideas list, not acted on, handed to pass 3.
- 2026-10-10 — A pass's branch is regrouped into a few commits by
  kind before it lands, its small commits kept on a local
  `-detail` branch. Pass 1 showed why: 25 commits, each useful
  while working and reviewing, mostly noise on main, where the
  report already says what each fix was. Its open items are
  decided one decision at a time, each covering every item of its
  kind: pass 1's fourteen groups came down to about six.
- 2026-10-11 — The polish stops after pass 1, on the reviewer's
  word. Passes 2 and 3 are not run; the reviewer goes through the
  repo by hand, part by part, with the agent guiding — what the
  passes meant to do, done as a conversation rather than a sweep.
  Pass 1's open proposals (its report, and its manual's ideas) are
  not applied; they stay as ideas. The regroup worked well, in the
  reviewer's words.
