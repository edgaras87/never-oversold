# Pass 1: the manual

<!-- For the reviewer: what pass 1 is, how it is defined now, and
     the ideas not yet applied. The agent's instructions are the
     brief beside it, pass-1-clean-up.md, with the shared rules in
     00-overview.md. This manual says how they work; it adds no
     rule of its own. -->

## What this pass is

A clean-up of the whole repo for truth and place. Readers go
through every file once and look for six kinds of fault: a line
gone stale, a fact written twice, a line in the wrong record,
something a stranger needs and cannot find, a reference that points
at nothing, and a line that adds nothing. Each fault is checkable:
a line is stale or it is not.

**When to use it:** the records have grown over many steps, and a
stranger is about to read them. Here: before 1.0.0.

**What it does not do:** improve wording or explanations (pass 3);
correct the skills and rules (pass 2); change what the code does;
edit history.

**What it leaves:** small fixes, regrouped by kind before they
land; and a report — each fix with its reason, the items only you
can decide, the findings dropped and why, the ideas handed to pass
3, and the cost.

**The problem it solves.** Each record was written at a moment.
When a step closes, it updates the records it touches, but a fact
written elsewhere, earlier, is never revisited. Over nine steps and
140 files, those lines go wrong quietly. Pass 1 found, for example:
README missing the tests' one-time setup, so a stranger's tests
fail; the pom saying 0.3 while README said 0.4; kills 13 and 14
swapped in the table that says which test proves which danger.

## How it works, in nine steps

1. **You start it.** The agent cuts the branch and commits the
   briefs.
2. **Pilot.** Three readers on three small files; the agent fixes
   what is clearly wrong, then **stops and tells you the cost**.
3. **Read everything,** on your word: one read-only reader per group
   of files. Each returns three lists — mistakes with proof, the
   facts its files state, ideas for later.
4. **Compare the fact lists.** "0.3" in one group and "0.4" in
   another is a mistake no single reader can see.
5. **Sort** each mistake: fix it, ask you, or drop it with a reason.
6. **Fix,** one small commit each, the tests after any code change.
7. **One last look** at what changed only. Fewer than three new
   problems, and it stops.
8. **You decide** what was asked, one decision at a time.
9. **Regroup and land:** the small commits kept on a `-detail`
   branch, the branch rebuilt as a few commits by kind; you say
   "merge".

**Your part:** "go" after the pilot, the decisions, the merge.

## How a reader knows something is a mistake

Every reader gets the same short sheet, and checks every line
against it and against the repo itself.

**What the sheet holds:**
- what is true right now (all four slices closed, version 0.4,
  Step 8 done, the devlog split)
- who owns which fact (CLAUDE.md's table: the registry owns slice
  status, CHANGELOG owns versions)
- the six mistakes, each with an example

**The rule that matters most: no proof, no mistake.** A reader may
only report what it can point at — another file and line, or a
command's output. "This looks old" does not count; "this says
three, and the registry says four" does.

**The three grounds a line is checked against:**

| Mistake | Checked against |
|---|---|
| Out of date | the sheet |
| Same fact twice | the files compared with each other |
| Wrong place | the ownership table |
| Missing for a newcomer | could a stranger do it with only what is written? |
| Link to nowhere | the repo: does that file, test or section exist? |
| Line that says nothing | judgement, with a reason |

**Then every mistake is checked again** — by the agent before
fixing (about fifteen were dropped as wrong in pass 1), by the
tests, by the last look, and by you.

**What it cannot catch:** a mistake in the sheet itself, which
every reader would inherit; the same wrong fact in two places that
agree; facts no reader listed; what nobody noticed. The pass makes
the repo more correct; it does not prove it correct.

## How it is defined now: three layers

Three kinds of instruction define the pass. The last two are
written as rules today, in the brief and the shared rules.

1. **What it is** — the section at the top of this manual. The
   brief starts from its goal.
2. **Hard limits** — in the overview's shared rules:
   - never touched: history, the system documents (proposals
     only), the concept chapters, what the code does
   - always to you: anything needing a decision, the exports,
     history, the entry file
   - when it stops: done, the last look dry, the budget, or the
     tests red
   - the cost: an agent cap, the pilot first, read-only readers on
     a cheaper model
   - no proof, no mistake
3. **The way to do it** — in the brief: the seven steps, the seven
   groups of files, the budget's numbers.

**What each layer did in pass 1:**

| Layer | What it did |
|---|---|
| Hard limits | Prevented damage and runaway cost. Nothing was blocked: what the limits stopped came to you, as your decisions should. |
| No proof, no mistake | Kept the findings honest. |
| The way to do it | Protected nothing. Its guess of "8 agents" was wrong and had to be changed mid-pass. |

## Ideas, not yet applied

Proposals from pass 1. None is in force until you say so; each,
once taken, becomes a dated line in the overview's *Changes*.

**Firm on what and never, free on how.** Keep layers 1 and 2 as
rules. Turn layer 3 into "a way that worked": the agent may split
the work differently, use more or fewer readers, change the order,
but says what and why in the report. It can adapt to what it finds
without a rule change, and still never cross a hard limit. The same
idea as the project's own method: the promise and what must never
happen are fixed; the mechanism is free.

**The sheet first, checked, and yours to review.** In pass 1 the
sheet was small, written by the agent into the readers'
instructions, and seen by no one. The better order:
1. Check the owners first — the registry, definition, CHANGELOG,
   PLAN. If they are wrong, everything checked against them
   inherits it; pass 1 found stale lines in the registry itself.
2. Build the sheet from the checked owners and the machine, each
   line with its source, and a command where one proves it —
   "version 0.4: CHANGELOG line 23; `grep '<version>' pom.xml`
   agrees"; "100 tests: `./mvnw test`, 2026-10-10".
3. You review it — thirty or forty lines.
4. Then the readers check everything else against it.

In one line: fix the originals first, then the copies. The readers'
fact lists grow the sheet, so it can be kept and only updated at
the next pass or release.

**The other proposals** — splitting the code group, naming the
commands a read-only reader must not run, telling readers that "as
delivered" sections are snapshots — are in the report, under *What
the pass says about the rules*.
