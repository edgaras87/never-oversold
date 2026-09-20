---
name: decide-first
description: Find the questions a piece of work has not asked yet, order them by what rests on each, and settle the top one before touching the next. Use before opening a commit plan, when something is undecided and the shape of the work depends on it — the sign being that you cannot say roughly how many commits it will take.
---

# Decide First

Work that has not settled its shape produces a plan that is a
guess. Name what is undecided, order the questions by what rests on
each, settle the top one, and only then ask how the work is cut
into commits.

Kind: playbook — worked in a `temp/` draft, never in place.

## 1. When this fires

Something is undecided **and the shape of the work depends on it**.
That is the whole scope, and the line that draws it:

> A question whose answer changes the **shape of the work** is
> settled first. A question whose answer changes **a step** is
> discovered inside it.

**The diagnostic is free and it is reliable: if you cannot say
roughly how many commits the work will take, a shape question is
still open.** Confidence about the count is not evidence that there
is none — it is what a missing question feels like from inside.

It does not fire for a decision already taken, in conversation or
in a record — `commit-plan` carries those, decision-first. It does
not fire for work whose surprises will be found in the material;
that is material-first, and it is what a commit plan is for. It is
not a gate on every change set.

## 2. The method

1. **Write the questions down, in a `temp/` draft.** Not the
   answers, not the plan — the things that are not settled. Include
   the ones that feel already answered; write what would make each
   one wrong.

2. **Look for the question nobody has phrased.** The questions on
   the table are rarely the dangerous ones. Ask what the work
   *assumes* — a word in the goal that could be read two ways is
   usually it.

3. **Order by what rests on each, not by how hard each is.** The
   cost of a wrong answer is everything built on top of it. A cheap
   question that everything depends on goes above an expensive one
   that nothing does.

4. **Say how each one gets settled, by name.** There are three:
   - **ask** — the answer is in someone's head, and a sentence
     gets it. Cheapest and most often skipped.
   - **measure** — the answer is in the material. A sort, a grep,
     a count, written into the draft.
   - **compare** — more than one answer could work and each can be
     built: `option-comparison`, or `visual-comparison` when the
     question is how something is shown.

   A question with no method named is not ready to be settled.

5. **Settle the top one before touching the next.** An answer
   changes the questions below it, and often deletes some. Working
   two at once is what produces an order that has to be redone.

6. **Record the answers where they belong** — an ADR for a decision
   with rejected options, the draft for a measurement, a line in
   the plan for the rest. Then delete the draft.

7. **Only now ask: one commit, or a plan?** If you still cannot say
   roughly how many, go back to step 2; a question is still open.

## 3. Gates

- Every question is written before any is answered.
- Every question names how it gets settled, or is marked not ready.
- The top question is settled before the next is touched.
- The count of commits is sayable before a plan is opened.
- Decisions are in their records before the draft is deleted.

## 4. What this has caught

**These are things to check, not rules to obey.** Each names the
case it came from, so you can judge whether yours is like it. An
entry that would give the wrong answer on a different real case is
too broad — narrow it to what was actually observed. If you cannot
name the case, it is not an entry yet.

The first three are **retrospective**: they come from the change
set whose failure prompted this skill, read backwards, not from a
run of it. The first entry that is not marked retrospective is the
evidence this method works; a list that never gets one is the sign
it was written too early (CBC ADR-0030).

- **The question nobody phrased was the one that changed the
  shape.** *(retrospective — CBC ADR-0029, the groups set.)* The gate
  said the groups must be "named". Everyone read that as prose;
  the user read it as directories. Nobody asked which, and the
  answer cost four commits and a mid-set revision. A word in the
  goal that reads two ways is where to look first.

- **Confidence about the commit count was the tell.** *(same set.)*
  Six steps were written firmly and it landed at twelve. The two
  written with most confidence were the ones undone. The
  confidence was not evidence of a settled shape; it was the
  missing question, felt from inside.

- **A measurement cannot answer a shape question, and running one
  feels like progress.** *(same set.)* The sort was run first and
  was good work — it found that a grep over-reports, that thirteen
  files were stack-bound, that the gate's own arithmetic was
  wrong. None of it touched *are groups directories*, which was
  the question that mattered. Check that the method you named
  answers the question you asked.

## 5. What this does not do

- It does not sequence commits — that is `commit-plan`, and it
  starts where this ends.
- It does not settle a question of form on its own; it routes to
  `visual-comparison`.
- It does not fire for a decision already taken, and it is not a
  review of decisions already made.

---

## Decisions

- CBC ADR-0030 — built on one clear instance, which is thinner
  evidence than this repo usually accepts. The claim that makes it
  defensible is that this is a gap nameable in advance rather than
  a rule distilled from a pattern, and the cost of being wrong is
  one file. The trigger recorded there: if this runs three times
  and never routes anywhere but a comparison skill, it is a wrapper
  and should merge into one
