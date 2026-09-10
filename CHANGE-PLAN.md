# Change-plan: Step 2, the identity

## Summary — the state after all commits

The project has a public identity, decided and recorded: a name
derived from the promise and tested against the naming bar, a
one-line description derived from the intent, and a remote under
that name carrying main. ADR-0003 holds the decision with its
rejected candidates and says in so many words whether the working
name safe-reservations stood. No record says "working name" any
more; each export's revision log carries a dated entry for the
heading change; CHANGELOG says the project was named; the devlog
records the step; PLAN's Step 2 gate is ticked.

## Commits

**1. `docs(agent): add change-plan for the identity`**
This plan, agreed.

**2. `docs(adr): name the project`**
ADR-0003, Status: Proposed. Candidates derived from the intent's
sentence, each against the four-point bar; the chosen name; the
one-line description; safe-reservations confirmed or overturned
explicitly. The decision is the reviewer's, as a series: a draft,
revisions per question, the verdict — the same rhythm as framing.

**3. `docs: carry the name into the records`**
The heading and every "working name" in README, PLAN, the three
exports (each with its dated revision-log entry), and the devlog's
briefing line. CHANGELOG's Changed line. One commit: the rename is
one change across many files, and reverting it must revert whole.

**4. `docs(agent): carry the name into the entry file`**
The entry file's title. Agent-scoped, so it cannot share commit 3.

**5. The remote** *(no commit — an outward action)*
Created under the name with the description, main pushed, both on
the reviewer's word at this boundary; the remote's name recorded
in the working tree for commit 6 by `git remote -v`, nothing else.

**6. `docs: records catch up on the identity`**
Devlog entry (decision, rejected candidates in one line, the
remote); TODO triaged, the step's retitle added to the playbook
fold-back item; PLAN's gate ticked and the step marked done, the
decision index gaining ADR-0003; ADR-0003 flipped to Accepted.

**7. `docs(agent): close change-plan for the identity`**
Deletes this file; the body records what diverged.

Then, outside the set: the branch fast-forwarded into main on the
reviewer's word, and main pushed again so the remote carries the
close.

**Touch-ups from the reviewer's reading** — zero or more, one
commit each, refinement not divergence; the close body lists them.

## Decisions taken inside this plan

- **The remote is not a commit but is a planned step,** because
  the gate names it and the close must say whether it happened. It
  runs between the rename and the records so the devlog can record
  it as done.
- **The rename is one project commit,** not one per file: the
  revert test — undoing the name in README alone would leave the
  exports disagreeing with it.
- **The exports' heading change is a dated revision entry,** not a
  silent edit: they are living records and this is the first change
  since close.
- **ADR-0003 lands Proposed and flips in the records commit,** per
  change-plans §4.
- **The second push after the merge is the reviewer's word again,**
  not implied by the first.
