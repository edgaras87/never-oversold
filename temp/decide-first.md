# Decide first — the writing and review pass

Draft, `temp/`, deleted when the answers are in their records.

## What this is about

**The work.** Three things were queued while SL-2 was being built
and deliberately not done during it, so the slice's record stayed
in one voice. They now run as one housekeeping branch, before SL-3:

1. **The writing pass.** The records read as if they must be
   decoded, and so do the chat answers explaining them. The case
   that showed it: SL-2's owners table, where each cell argued
   three things at once and the reviewer could not read it without
   unpacking it aloud. It is not the only one.
2. **Review between versions.** While one commit's worth of work is
   polished, each new version can only be diffed against the last
   commit, so the reviewer re-reads the whole thing to find the
   part that answers their last question. A working answer is
   already in use — stage each version shown, word-diff the next —
   and what is open is only whether it is written down, and where.
3. **SL-1's per-test references.** SL-2's tests now say what each
   one is for beside it; SL-1's say it only at class level.

**Why this draft exists.** The commit count is not sayable. Item 1
could be one commit or fifteen depending on answers nobody has
given: which reader the rule serves, whose rule it is, whether it
governs the chat at all, and whether the records already written
are swept now or fixed when next opened. That is the sign a shape
question is still open, so the questions come before the plan.

**Which questions belong to which item.** Q1–Q5 are item 1. Q6 is
item 2. Q7 is item 3. Items 2 and 3 are cheap and independent, and
sit at the bottom of the order for that reason.

**What is already decided and is not re-opened here.** Two options
were weighed and rejected during SL-2, with reasons in the devlog:
a second, plainer set of records beside these (two masters for one
fact drift), and marking the current records agent-only with a
human version alongside (there is no dialect the agent reads
better). The style rule drafted mid-slice was reverted unstaged, on
purpose; its text is in the devlog entry of 2026-09-21 so this pass
starts from something rather than from nothing.

## The questions, written before any is answered

**Q1. Which reader is "readable" about?** The word reads two ways
and the whole pass rests on which. The intent names the audience as
a portfolio reader judging whether the author can build
correctness-first — that reader wants the argument tight and wants
to be impressed by its density. The reviewer mid-work wants to read
a paragraph once, at speed, without decoding. A third exists: a
maintainer six months out, who wants to find one fact fast. The
three want different things from the same sentence.
*What would make this wrong:* if all three turn out to want the
same thing, the question is empty and the pass is just "write
better".
*Settled by:* **ask**. The answer is in the reviewer's head and one
sentence gets it.

**Q2. Whose rule is it, and where does it live?** Three homes, and
they are not interchangeable. A project-local rule in the entry
file's local rules — ours, no hand-off. A convention — the bundle
owns conventions since 2026-09-18, so it goes to them and we hold a
trial copy. A new artifact of our own, a guide (`artifact-kinds`
names the kinds: convention, model, guide, playbook).
*What would make this wrong:* if the rule turns out to be one line,
no home question exists — it goes in the entry file and nothing
else happens.
*Settled by:* **ask**, then `artifact-kinds` for the naming if the
answer is "an artifact".

**Q3. Does the rule govern the chat, and can it?** The finding has
two faces, and one of them — the agent's answers while the work
happens — is not an artifact and not in the repo. A convention
governs artifacts. An agent-arrangement rule governs the agent.
They are different homes, so the answer may be two rules rather
than one.
*What would make this wrong:* if the chat half is already handled
by the agent's own memory and needs nothing written here.
*Settled by:* **ask**.

**Q4. Sweep now, or fix each record the next time it is opened?**
This is the commit count. Sweeping now is a large diff that proves
nothing and risks touching records mid-sentence; fixing on touch
leaves the project in two voices for a while, which is the thing
that was rejected mid-slice.
*What would make this wrong:* if the measurement (Q5) shows the
sweep is three files, in which case there is no question.
*Settled by:* **ask**, after Q5's measurement.

**Q5. How much is actually in scope?** Which records carry an
argument rather than a lookup — the distinction already drawn:
argument prose and argument-carrying tables are governed, lookup
tables are not.
*Settled by:* **measure**. Count the tables and the multi-claim
paragraphs across `docs/`, README and the manuals; write the count
here.

**Q6. Is the review-between-versions item a rule at all?** The
working answer is already in use and needed no decision: stage each
version shown, word-diff or the IDE's staging area for the next.
What is open is only whether it is written down, and where — here,
or handed to the bundle, whose `commit-plan` convention it touches.
*What would make this wrong:* if it turns out the practice fails in
use, in which case it is not a rule but a problem.
*Settled by:* **ask**.

**Q7. How many tests need SL-1's headers?**
*Settled by:* **measure**.

## Order, by what rests on each

1. **Q1** — every other answer changes with it.
2. **Q2** — decides which files the pass touches at all.
3. **Q3** — may split the rule in two, which changes Q2's answer
   for one half.
4. **Q5** — a measurement, cheap, and Q4 cannot be asked without it.
5. **Q4** — the commit count.
6. **Q6**, **Q7** — independent of the rest, cheap, settle last.

## Answers

<!-- Filled as each is settled, top first. -->
