# Commit plan: apply the retrospective to run 3

## Summary — the state after all commits

Run 3 hands over records that read the way its retrospective said
they could. Each guarantee in a slice record is one block. The
tests sit in folders by kind and say which slice they serve. The
exports hold the system, not how framing ran, and the definition
no longer lost meaning against the derivation. ARCHITECTURE says
what is true now, by rule, and answers what a slice may rely on.
Each fact has one home, and the living records read plainly, with
a glossary and a worked example per kill. A check script finds
broken links and numbers. The shapes live in one folder and are
read at the gate. The delivered copies Step 10 changed each carry
a log entry and a TODO line for the deliverer. A skill for applying
a retrospective is built from this step. Nothing proven changes:
the same tests, the same assertions, the same count, green.

Which decision each commit serves is RETRO.md's "Applied to run
3"; its last column is filled at the close.

**How this plan is built (D19).** A planned commit may be built as
several small commits: tries, fixes, a way abandoned. At the
planned commit's boundary they are squashed into the one commit
named here, the small ones kept on `step-10-apply-retro-detail`,
and the reviewer sees the planned commit before the next starts.
After the last, a look back at the whole: a better grouping is
proposed, and done on the reviewer's word.

## Commits

### A. Tools and arrangement first, since later commits use them

**1. `docs(agent): add commit plan for Step 10`**
This plan, approved.

**2. `chore(agent): add a check script for the records`**
D17. `.claude/checks/records.py`: links and paths resolve; every
cited ADR, slice, kill and test name exists; the version in
`pom.xml` and CHANGELOG agree. Run once here, and its findings
shown before anything is fixed: a baseline, not a fix.

**3. `docs: PLAN's step form asks for goal facts`**
D4, D18. The step form asks of the goal "how would I check that's
true?", gains the "Now:" line, and loses the `temp/` closing item.
The "on trial" notes say kept. Release opens from this form.

**4. `chore(agent): one folder for shapes, read at gate`**
D18. `shapes-lifecycle.md` shortened and written plain: one folder,
read at the gate, four ways a difference ends. The test file
split: its shape moves to `.claude/shapes/evidence-test.md`, the
red-run rule it repeated is left to cbc-slice and linked,
`.claude/rules/evidence-test.md` goes.

**5. `chore(agent): the slice-record shape by guarantee`**
D12, D14. The shape becomes one block per guarantee, parts in
order; its rules for the parts kept; what the writing rule says,
removed; the claim's form (condition first, what a test sees)
added. The regroup in commits 10 to 13 follows it.

**6. `chore(agent): log the step form and the shapes`**
The decisions-log entries for commits 3 to 5: the step form;
`shapes-lifecycle.md` edited (a delivered copy); the test file
split; "Rules in force now" updated for both.

### B. The exports, which the other records link to

**7. `docs: the definition regains what it lost`**
D7. Provisional: its content is the comparison's result. The
definition is compared with the derivation's results (kills,
facts, assumptions, things not ours, verdicts, possessions); the
list of differences goes to the reviewer before any edit. Each
difference where the definition lost meaning, as kill 17 did, is
restored by a dated revision entry.

**8. `docs: the exports hold the system, linked`**
D5, D8. The registry's ordering section shortened to the order and
links to the slice records; its "Divergences" line says the
briefing was not kept. The definition's "How saturation was
earned" and its derivation-order note become links. Each fact
found in its home first. Dated revision entries.

### C. The tests, moved before anything names their place

**9. `test: place the tests by kind`**
D27. `adversity/`, `structure/`, `store/`, `door/`; each test's
kind confirmed by reading it first. Only the package line and the
folder change. `./mvnw test` green, with the same count. The check
script runs after.

### D. The slice records

**10. `docs: SL-1's record by guarantee`**
**11. `docs: SL-2's record by guarantee`**
**12. `docs: SL-3's record by guarantee`**
**13. `docs: SL-4's record by guarantee`**
D11. One per record, so each reverts alone. Moved, not reworded:
a script checks the same lines before and after, allowing only the
plan's restated claim headings to go. The living links into that
record (in the record, the code, ARCHITECTURE, TODO, the exports)
switch from section numbers to labels in the same commit. One
revision line names the signed commit, which covers history's
section numbers. The check script runs after each.

**14. `docs: guarantee claims say what happens`**
D14. The claims that read as titles (about 8 of 22) rewritten,
condition first, each checked against its block's body and tests.
Shown to the reviewer one slice at a time, as small commits.

**15. `test: labels name their slice`**
D13. The 53 first lines read "SL-1 E3 · G3 — kill 5". SL-1's and
SL-2's tests read against the test shape's close check: first lines
and labels fixed; an assertion missing or out of order is not
changed, but listed in its slice record for the reviewer.
Comments only. Provisional: the listing's size is not yet known.

**16. `docs: code comments link to the argument`**
D15. The main code's comments cut to what the code relies on and
cannot show, each linking its guarantee ("Holds SL-1 G1"); what a
removed comment said is found in the record first. Bare tags carry
their owner. Comments only; migrations untouched. `./mvnw test`
green.

### E. The front door and the manuals

**17. `docs: ARCHITECTURE describes the system by rule`**
D16, D10, D26, D27. The invariants stories go; the table becomes
the invariants section, each row linking its slice. The components
answer what a slice may rely on (the store, migrations V1 to V4,
the ledger, the harness, and why it forks processes). The codemap
shows the test folders.

**18. `docs: the bootstrap requirements are history`**
D10. README's records table names them history and sends "what a
slice may rely on" to ARCHITECTURE; their banner becomes a plain
history marker. The certified text is untouched.

**19. `chore(agent): entry file points to ARCHITECTURE`**
D10. CLAUDE.md's records table, the same change as commit 18: an
agent file, so a commit of its own.

**20. `docs: the operator manual's levers, as they are`**
D9, D26. The levers section says the harness pulls its own: plain
processes, killed with SIGKILL, and the store's container paused.
Step 3's container proofs stay in the devlog, linked.

**21. `docs: one owner per local tag in living text`**
D6. What the commits above did not already reach: "migration V3"
wherever V3 is a migration; a guarantee or evidence number outside
its record carries its slice; concerns A to D become their slice
tags. The check script runs after.

### F. The plain pass, with one home checked first

Each commit below reads its record once, with two questions per
paragraph: is this the fact's home (if not, link)? Then: is it
plain? (D17 with D2.) The exports change by dated revision; each
slice record's revision line names the signed commit.

**22. `docs: a glossary for the reader`**
D2, D6. `docs/glossary.md`: each coined word and tag kind in one
plain sentence, with its run-4 name beside it. The records tables
(README, and CLAUDE.md in its own commit) gain its row.

**23. `docs: a worked example for each kill`**
D2. The definition gains, by revision, one example with numbers
for each of its 20 kills.

**24 onward. One commit per record, provisional:**
README; ARCHITECTURE; intent; definition; registry; the four slice
records; the operator manual; the infrastructure contract; TODO;
PLAN's living parts. CLAUDE.md in a `chore(agent)` commit of its
own. The split is refined at commit 23's boundary, when the
records' state after D to E is known.

**Then, agent files, provisional:**
- `chore(agent): cbc-slice says each thing once` (D21, D2): SKILL.md
  and its workflow reference, home first, then plain; the red-run
  rule and every gate kept in meaning.
- `chore(agent): delivered-copies rule, plain` (D2).
- `chore(agent): log the delivered copies changed`: one entry per
  copy, each saying a finished copy's new wording is first used by
  the deliverer (delivered-copies rule 7).

### G. The close

**Close 1. `docs: records pass the checks`**
The check script, and the judgment check: no living line still
says "now", "today" or "not yet" about something since changed.
What they find is fixed. Provisional: may be empty, and then the
plan says so.

**Close 2. `chore(agent): commit-plan gains small steps`**
D19, D25. The skill edited from how this plan actually ran, with
its log entry.

**Close 3. `chore(agent): a skill to apply a retrospective`**
D24, D25. Built from the manual and from what happened; its own
skill or a part of `retrospective`, decided here by its size. The
`retrospective` skill and manual name the step after. The manual
stops being provisional. Log entry.

**Close 4. `docs: RETRO.md records what Step 10 applied`**
The commits column filled; lessons written back, dated; D22's
table read once more against every decision; a draft of the apply
step for run 4's PLAN, beside D23's.

**Close 5. `docs: TODO hands the changed copies over`**
One line under "To the deliverer" per edited copy: cbc-slice,
`delivered-copies.md`, `shapes-lifecycle.md`, `commit-plan`.

**Close 6. `docs: close Step 10, the retrospective applied`**
The devlog entry; PLAN's Step 10 gate ticked and closed, Release
next.

**Close 7. `docs(agent): close commit plan for Step 10`**
Deletes this file; the body records what diverged.

## Decisions taken inside this plan

- **The check script lives in `.claude/checks/`,** as an agent
  file: it checks the records, and is offered to the deliverer with
  the one-home rule (D17).
- **Each slice record is regrouped in its own commit,** with the
  living links into it. Four commits revert alone; one commit of
  ~27,700 moved words would not be reviewable.
- **D6 comes after ARCHITECTURE's rewrite (17), not before.** Tags
  fixed in text that commit 17 removes would be wasted work. It
  still comes before the plain pass, as D24 orders.
- **The glossary is a record of its own, `docs/glossary.md`,** not
  part of the exports: the coined words span the exports, the slice
  records and the tests.
- **No CHANGELOG entry.** Nothing a user of the ledger sees
  changes: no behaviour, no answer, no command.
- **A new decision found during the build** is shown to the
  reviewer as in the triage, gets its RETRO row, and revises this
  plan (§5).
- **Step 10's gate gains one item** at commit 3, for D17's judgment
  check ("no living line says now, today or not yet about something
  since changed"), marked as added mid-step.
