---
name: retrospective
description: Walk a finished run part by part and decide what the next run does differently. Each decision is backed by a real example from the run and lands in a named place the next run starts from. Use when the run's last slice has closed and before Release, or when the reviewer asks for a retrospective, a look back, or "what would we do differently". Do NOT use to fix the current run's records in place.
requires: commit-messages
---

# Retrospective

The run stays as it is. Its lessons go to the next run, through what
the next run starts from: the skills, the rules, the playbook, the
templates, the reviewer's local file. A lesson that lands nowhere is
repeated.

The reviewer decides everything. The agent reads, measures, finds,
proposes and writes.

## 0. Open

1. **Cut a branch,** `retrospective`, from main. Main is not touched
   until the retro lands. Ask the reviewer whether commits are free
   on it.
2. **Write the note,** `RETRO.md` at the root, with these sections:
   - **Goal:** the run stays as it is; lessons go to the next run.
   - **Rules:** the branch, the `try/` branches, how commits work,
     one decision at a time.
   - **Parts:** a table of the run's parts, in the order the run
     built them, each with its files and a status: not started, in
     progress, done (date).
   - **Do differently:** one block per part.
   - **Delivering the retro:** what reaches the next run, and how.
   - **Experiments:** one line per `try/` branch.
   - **Parked:** questions raised in one part that belong to
     another, each naming that part.
3. **Explain the run to the reviewer first:** how work is done, what
   is read when, what you think is good and what is costly. Use
   numbers: words of records against lines of code, commits by kind.
   The first candidates for decisions come from this.

## 1. Walk each part, in order

1. **Read the part's files in full.** Then measure: sizes, counts,
   how often a thing appears, where one fact is written twice.
2. **Find what went wrong or cost too much.** Each finding needs a
   real example from the run: a quoted line with its file and line
   number, or a count. A finding without an example is dropped.
3. **Say what worked.** "Kept as it is", with the reason, is a valid
   result for a part.
4. **Group the findings into a few decisions.** Then go to section 2.
5. **Mark the part done** in the table when its last decision is in.

## 2. One decision at a time

1. **Show the reviewer one decision:**
   - the problem, with its example;
   - the change;
   - where it lands;
   - a recommendation.

   Where there is a choice, show the variants applied to real text
   from the run, side by side, before asking.
2. **When the reviewer asks for a simpler explanation,** give one,
   with an example, before changing anything.
3. **On agreement, write the decision into the note and stage it.**
   Commit on the reviewer's word. Then show the next one.
4. **A decision block holds, in order:**
   - **D<n>. The decision as a sentence.** (date)
   - What happened in the run, with the example.
   - The change, as bullets.
   - **Considered and rejected:** each option that was on the table,
     with its reason. Only when there was one.
   - **Lands in:** the named places.
5. **When the reviewer corrects you,** the corrected version is the
   decision, and your first version goes under "Considered and
   rejected", with the reviewer's reason.

## Rules for a decision

- **"Lands in" names a place the next run starts from.** "Remember
  to" is not a place.
- **Stay inside the question asked.** A new question goes to Parked,
  with the part that owns it.
- **No decision without something that went wrong.** A good idea
  with no failure behind it is dropped.
- **Judge on real text, never before it exists.** A question that
  needs text the run has not written yet is parked to the moment it
  will exist.
- **Do not fix the run's records.** The exceptions are in section 4:
  the run's own hand-off lists, and lines that say "now" about
  something no longer true.

## 3. Try a change on a branch

A change worth testing during the retro, such as a new rule, goes on
`try/<what>`, cut from `retrospective`, and is used for the rest of
the retro. At the close the reviewer keeps it (`retrospective`
fast-forwards to it) or drops it (the branch is deleted, and the
note's commits move back). The note's Experiments line records which.

## 4. Close

1. **Every part is done,** and every Parked item is judged or still
   names its part.
2. **The run's own hand-off lists close into the note.** Each item
   in TODO's "To the deliverer" that a decision answers becomes one
   line naming that decision. The decisions log gets a "rules in
   force now" list on top, with what each rule replaced.
3. **What only the next run can check becomes its gate items.** List
   them in the note, each with the step that judges it. The
   deliverer writes each into the next run's PLAN at that step, with
   one line of reason and a pointer to the decision.
4. **One TODO line asks the deliverer to apply the note's
   decisions.**
5. **A devlog entry,** ending with a "Resume:" line, and TODO's Now
   updated.
6. **Land it.** Regroup the commits by kind, agent files apart from
   project files. Keep the small commits on a local
   `retrospective-detail`. Merge on the reviewer's word.

## Between sessions

TODO's Now and the devlog's last "Resume:" line say where the retro
stands. The note holds every decision, so a new session reads those
two, then the note.
