# The retrospective: a manual for the reviewer

<!-- The reviewer's document. The agent's instructions are SKILL.md,
     beside it. This file says what the retro is and how it decides,
     in plain words, and is never read as instructions. -->

## What it is

A look back at a finished run, to decide what the next run does
differently. The run itself is not changed. The decisions change
what the next run starts from: the skills, the rules, the playbook,
the templates, and your `CLAUDE.local.md`.

It runs after the last slice and before Release, so it happens even
if Release is skipped. Release adds its own lessons to the same note
when it closes.

## How it runs

1. The agent cuts a branch, `retrospective`, and opens a note,
   `RETRO.md`. Main is not touched.
2. The agent explains how the run worked, what was good and what was
   costly, with numbers.
3. You walk the run together, part by part, in the order it was
   built: set-up, framing, identity, ground, skeleton, slices, code,
   front door, history, method.
4. For each part, the agent brings a few decisions, one at a time.
5. You agree, correct, ask for a simpler explanation, or drop it.
6. A change worth testing goes on a `try/` branch and is used for
   the rest of the retro. At the end you keep it or drop it.
7. At the close, the run's own lists point to the decisions, the
   next run's checks become its gate items, and the branch lands on
   main, regrouped, on your word.
8. After it, a step of its own applies the retro to this run before
   Release (`apply-retrospective`): what improves the records
   without changing what was proved.

## How it decides

- **Every finding has an example from the run:** a quoted line, a
  file and line number, a count. No example, no finding.
- **Every decision says where it lands.** If it lands nowhere, the
  next run repeats the mistake.
- **No decision without something that went wrong.** In run 3, "one
  kind per document" was drafted and dropped: nothing had gone wrong
  to fix.
- **Choices are shown on real text.** In run 3 the tag names were
  picked from tables of variants applied to real lines.
- **Your corrections are kept.** The agent's first version goes
  under "Considered and rejected", with your reason.

## What you decide

- whether commits are free on the branch;
- each decision: agree, correct, or drop;
- each experiment: keep or drop;
- when the branch lands on main.

## What you get

- `RETRO.md`: every decision, with its example, what was rejected,
  and where it lands.
- TODO's "To the deliverer" pointing at the note.
- The next run's checks, ready to become gate items in its PLAN.

## Ideas not yet applied

- **Release's lessons.** The step before Release is new. How Release
  adds its lessons to the note has not been lived yet.
- **Measure first.** Run 3 measured each part as it reached it. A
  quick measurement of the whole run at the start (sizes, repeated
  facts, commits by kind) might find the big costs sooner.
- **How many decisions.** Run 3 made 23. No limit is set; whether
  one helps is open.
