# Applying the retrospective: a manual for the reviewer

<!-- The reviewer's document. The agent's instructions are SKILL.md,
     beside it. Written while run 3's Step 10 ran, and no longer
     provisional since its close. -->

## What it is

The retrospective finds what could be better and changes nothing.
This step comes after it. It applies the retrospective to the run
itself, before Release, so the run is not released while its
records read worse than they have to.

Some decisions cannot be applied to a finished run: they change how
a step runs, and the run's steps are done. Those wait for the next
run. This step sorts one kind from the other, then applies the
first kind.

## How it runs

1. **Open.** A branch, and a PLAN step whose gate is written before
   the work.
2. **Triage.** The decisions, one at a time. For each, you choose:
   applied, partly (and which part), or next run only. The answers
   go into RETRO.md, under "Applied to run 3", one row each,
   committed as you agree.
3. **Commit plan.** Built from what is applied here, in the order
   the changes affect each other. You approve it before any change.
4. **Build.** One planned commit at a time. Small tries inside a
   planned commit are squashed into it at its boundary, and you
   see it there. After each, the plan is checked and revised if it
   no longer holds.
5. **Close.** The checks, and a reading against the shapes, whose
   differences you settle a few at a time. The skills the step
   taught are edited. RETRO.md and TODO record what happened. Then
   a look back at all the commits, regrouped by kind, and the branch
   lands on your word.

## How the triage went in run 3

- **27 rows:** 11 applied, 14 partly, 2 next run only.
- **A question can become a new decision.** Three came up during
  the triage, not in the retro: the way to start new work (D25,
  from your notes), the tests' form against the shipped form (D26,
  from your question), and where tests live (D27, from your
  question). Each was written in the same form as the others and
  got its own row.
- **A row can change an earlier decision.** Then the decision gets
  a dated line saying what changed and why. Example: D18 now says
  shapes are read at the gate only, from your point that a test
  written without the shape in view can teach it.
- **A row can change an earlier row.** Example: D11's row gained
  the dropped claim headings when D14 showed them.
- **Each row has a size.** Words, lines, or counts of links. The
  commit plan is built from those sizes.

## How it decides

- **Apply here** what improves what the run hands over without
  changing what it proved. Example: a plainer sentence with the same
  meaning; a fact told twice, kept once and linked.
- **Next run only** what changes how a step runs. Example: gates
  written as goal facts, since run 3's gates are closed.
- **Never** history: devlog entries, closed ADRs, closed gates,
  released CHANGELOG versions, migrations (comments included). The
  exports change only by a dated revision entry.
- **Signed text that a reader still uses** (the slice records) may
  be changed, with one revision line naming the commit where the
  signed text is.
- **New proof is not this step's.** A new test, or a new assertion,
  was never seen red. A gap found here is listed, and you decide
  whether it earns a review step of its own.
- **Delivered skills and rules** may be applied, each with a line
  asking the deliverer to compare it with the bundle. A concept
  chapter only from a mistake lived in the run (D28). A copy the run
  has finished with is marked untried: the deliverer uses its new
  wording first.
- **Read before claiming.** A suspected gap is checked in the files
  before it is shown. In run 3 the agent suspected an untested case
  that a test already covered (D26).

## How changes are kept safe

- **Order by influence.** Remove repeats before rewriting, so no
  text is polished and then deleted. Regroup before rewriting.
  Fix tags before the words around them change. Plain words last.
- **Two passes over the same text become one,** with two questions
  per paragraph: is this its home? then, is it plain? Used for the
  living records (D17 with D2) and for cbc-slice (D21 with D2).
- **A fact's home is found before its copy goes.** A fact with no
  other home moves there first.
- **A move is checked by a script,** not by reading: the same lines
  before and after. A move commit changes no words.
- **Links point at labels, not section numbers,** so a regroup
  cannot break them ("SL-4 G2", not "§8").
- **A check script** for links, paths, cited numbers and the
  version pair runs before the build, after anything that moves
  files, and at the close.
- **Owners before summaries.** A record that summarizes others is
  revised after them. Example: README summarizes the exports and
  the slice records, so it comes last.
- **A full pass repeats until it changes nothing that matters.**
  Each record in the first pass is revised against neighbours not
  yet revised. The second pass reads each against the revised ones
  and fixes only what no longer matches. A third runs only over
  what the second changed.
- **A fresh reader checks each rewrite** against the old text: for
  meaning lost, the writing rule, and home. The writer reads what
  they meant, not what is on the page. Example: README's first
  rewrite dropped "and only" from "first and only", and the review
  caught it.

## How the close went in run 3

- **The tests are counted from this run's reports only.** The
  report folder kept 18 files from before the tests moved into
  packages, and a first count said 195 where the run had 100.
- **The shape reading found differences of three kinds,** and you
  settled them as three decisions: a shape wrong where every record
  agreed against it; outputs that drifted; small corrections to
  other shapes. Records closed before a shape changed were named,
  not reopened.
- **Commit messages are checked when written.** Sixteen small
  commits had subjects over 50 characters, found only at the close.
- **A tool that edits text is checked on what it must not touch.**
  A rewrap joined a heading to its paragraph, a label to the line
  above, and once a skill's front matter into one line. Each was
  caught and fixed, and the tool learned to skip them.

## What you decide

- each triage row;
- each new decision that comes up during the triage;
- the commit plan, before any change;
- each planned commit, at its boundary;
- each revision of the plan that changes what lands;
- when the branch lands.

## What you get

- Run 3's records, improved where the triage said so.
- RETRO.md's "Applied to run 3": what happened to each decision.
- Decisions changed by trying them, marked with the date.
- A skill for applying a retrospective, built from this step:
  `SKILL.md`, beside this manual.

## Ideas not yet applied

- Grouping the triage by the record a decision touches, not by
  decision number. In run 3 many rows met at the same records
  (ARCHITECTURE: D10, D15, D16, D26, D27); the commit plan had to
  find that out after.

## Revisions

- 2026-10-12: started, provisional, when Step 10 opened.
- 2026-10-12: after the triage. What it taught: new decisions can
  arise in it; rows can change decisions and earlier rows; history
  includes migrations; new proof is not this step's; read before
  claiming; the safety practices used by the rows.
- 2026-10-14: during the plain pass (Step 10's group F). What it
  taught: owners before summaries; a full pass repeated until it
  changes nothing that matters; a fresh reader per rewrite. From
  the reviewer's question after README was revised first.
- 2026-10-15: at Step 10's close, no longer provisional. Its own
  skill, `apply-retrospective`, not a part of `retrospective`: the
  retrospective changes nothing in the run, and this step's job is
  to change its records. What the close taught: count only this
  run's test reports; settle the shape reading as a few decisions;
  check commit messages when written; check a text tool on what it
  must not touch. Concept chapters follow D28.
