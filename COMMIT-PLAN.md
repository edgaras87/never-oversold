# Change-plan: the conventions re-pin @ 6f2be1d, one renamed, three new

## Summary — the state after all commits

The seven convention skills are pinned at the bundle's `6f2be1d`,
taken from the staging in `temp/`, and the staging is gone. Four of
them we held before: `commit-messages` changes one line, the artifact's
name; `convention-lifecycle` says "the deliverer" wherever it said
"the handbook", which is our third reported gap answered; `artifact-
kinds` is unchanged; `change-plans` is gone by name and `commit-plan`
stands in its place, opening on a rule the old one did not carry — it
plans the commits, not the change. Three arrive new — `decide-first`,
`option-comparison`, `visual-comparison` — methods to reach for, not
rules over anything we hold. The five method skills and the concept
chapters are byte-identical to the staging and are not re-copied.

Four of the seven copies carry this run's first in-place edit: the
five lines where the delivered files still say "change-plan" after
the rename, each file with a dated header line, one decisions entry
for the edit, and one TODO line asking the bundle to evaluate it.
This is the first edit that will go through a re-pin, so the report
owed to the handbook is armed, not fired.

One decisions entry carries the new hash and names the handbook's
`ba7eaa4` in words as the provenance of the four that came from
there, not as a second pin. The records table in `.claude/CLAUDE.md`
names `COMMIT-PLAN.md`; this plan's own file carries that name from
the take onward. `TODO.md` carries the note's section 5 in its own
words, since the note is deleted with the staging: two items
discharged, one accepted by the bundle, one rule withdrawn, two items
held at the bundle with a named trigger.

What this gives us that we did not have: SL-2 opens on a convention
whose name says what it does, a procedure that names the deliverer
we actually read from, copies that do not contradict their own
rename, and a Later list that owes nothing already paid.

## Commits

**1. `docs(agent): add change-plan for the convention take`**
The plan, agreed before any of the work. It opens under the old
convention's name and artifact, because the new one has not landed —
step 2 renames the file with the convention that governs it.

**2. `chore(agent): update the conventions @ 6f2be1d`**
The take itself, agent-side and whole: the seven directories copied
from `temp/bundle-6f2be1d/conventions/` over `.claude/skills/`, the
`change-plans` directory deleted by name, `CHANGE-PLAN.md` renamed
`COMMIT-PLAN.md`, the records-table row in `.claude/CLAUDE.md`, and
the decisions entry. Compare-first is empty: `git diff aeef417 --
.claude/skills` returns nothing, so no local edit is overwritten;
`diff -rq` on the method half and on `docs/concept/` is empty too.

The deletion rides here and nowhere else — it is the one step the
note says a copy cannot do, and the repo must never hold two skills
stating the same rules. The rename and the table row ride here
because the copy makes the old names false: revert this step alone
and the convention, the artifact, and the table agree again.

This commit is the pristine copy. The next re-pin's compare is a
diff against it, which is why step 3 is not folded in.

**3. `docs(agent): four copies drop the change-plan noun`**
The in-place edit, under `convention-lifecycle` §3 step 4 as
received: five lines in four files — the template heading inside
`commit-plan`, one line in `commit-messages`, two in
`convention-lifecycle`, one in `option-comparison` — say
"commit plan" where they still said "change-plan". Each file gains
a dated header line naming the change and this step; one decisions
entry records the edit as this run's first, provisional in the
convention's own word until it has gone through an update. Its own
step so the take stays the pristine compare and the edit is one
exact hunk per file for the bundle to take or decline.

**4. `docs: TODO carries the note's verdicts on the Later list`**
Project-side, one change: the Later list read against section 5,
which is a set of decisions the bundle takes in the note itself,
with nothing behind them to check — so each TODO entry states the
verdict in its own words, dated, because the note leaves with the
staging. The three-gaps hand-off and the playbook fold-back are
discharged and go. The entry-file item goes: the bundle accepted
the half we held open against the handbook. The Spring slice
reference sentence is rewritten: the hand-it-after-the-build rule is
withdrawn in the note's own sentence, nothing is handed at a slice
close, and the absence-rung half stands. The framing-as-commit-
series item and the imperative-test item move into the held-at-the-
bundle entry, each with the bundle's trigger: the next time that
convention's file is opened for any reason, or a retrospective. The
first names `commit-plan` now, which is the citation the note asked
us to fix. `cut-a-kata` and the facility paragraph are untouched.

**5. `docs: hand the four edited copies to the bundle`**
The convention's own hand-off form: one TODO line asking the bundle
to evaluate this run's edits to the four copies since `6f2be1d`,
naming the five lines and why — nouns the rename left behind, missed
by the very lesson section 4 records from our first gap. The line
also answers their closing ask, since this is where the delivery
fell short. Beside it, the line owed to the handbook is updated: the
first edit of a copy now exists, and its report fires at the next
re-pin. Its own step, not folded into 4: triage is bookkeeping, a
hand-off is a request outward.

**6. `docs(agent): close commit plan for the convention take`**
Deletes `COMMIT-PLAN.md`. Body records what diverged. The subject
follows the new convention's table, since that is the one in force
at the close.

Then the branch `housekeeping-bundle-6f2be1d` fast-forwards into
main, on the reviewer's word.

## Decisions taken inside this plan

**One pin, the bundle's.** Three of the seven have no handbook
ancestor, so a handbook hash can no longer stand for the set. The
entry names `6f2be1d`; the handbook's `ba7eaa4` appears in words,
scoped to the four that came from there, as provenance. The note
names no newer handbook hash and none is invented.

**The plan file is renamed mid-set, in step 2.** The note said this
is a table row for us and not a file to move, which was true when
the bundle looked: nothing was in flight. Opening this plan puts one
in flight. Renaming it where the convention lands keeps every
boundary coherent — the alternative leaves four boundaries where the
convention in force names a file the root does not hold. Decided
with the reviewer.

**The five stale nouns are fixed here, as an in-place edit, and
sent back as one line.** Decided with the reviewer. The convention
permits an edit only from something that happened in the project
and only as an outcome any project would want; reading the
delivered files and finding them contradict their own rename is the
happening, and a copy that agrees with its own name is what any
project wants. The cost is the convention's full machinery for five
words — a header line per file, a decisions entry, a hand-off line
— and the arming of the report owed to the handbook. Accepted
because the machinery is the trial the handbook's rule is
provisional on, and five words are the cheapest possible first run
of it. One TODO line names all four copies rather than one line per
copy; the deliverer evaluates each hunk either way.

**The method half and `docs/concept/` are not re-copied.** `diff
-rq` is empty on both, verified rather than taken on the note's
word. Copying them would put unchanged files in the diff.

**No receipt branch**, as decided 2026-09-18: the compare for a
pristine copy is the delivery commit, and the delivery commit is
step 2.

**`decide-first` does not fire for this set.** Its diagnostic is
whether the commit count is sayable, and it is: six. The set is
decision-first throughout, the what settled in the note and this
conversation before the plan opened, which is exactly the case the
renamed convention's new opening paragraph names.

**Section 5 is taken as what it says it is: decisions, not
reports.** The note's first draft read as claims about the bundle's
records, which this repo cannot check, and the reviewer sent that
back. The rewrite makes each line a verdict whose taking is the
whole event. Nothing is verified because nothing is verifiable; the
TODO entries carry the verdicts in their own words, dated, so the
record survives the staging's deletion.

**Subjects follow this repo's lived declarative style**, as the
retrospective line filed 2026-09-18 already records; nothing new to
file.
