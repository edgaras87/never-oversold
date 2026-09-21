# Decide first — the writing pass

Draft, `temp/`, deleted when the answers are in their records.

## What this is about

**The work.** The writing pass, one of three things queued while
SL-2 was being built and deliberately not done during it, so the
slice's record stayed in one voice. They now run as one
housekeeping branch, before SL-3.

The records read as if they must be decoded, and so do the chat
answers explaining them. The case that showed it: SL-2's owners
table, where each cell argued three things at once and the reviewer
could not read it without unpacking it aloud. It is not the only
one.

**Why this draft exists.** The commit count is not sayable. Item 1
could be one commit or fifteen depending on answers nobody has
given: which reader the rule serves, whose rule it is, whether it
governs the chat at all, and whether the records already written
are swept now or fixed when next opened. That is the sign a shape
question is still open, so the questions come before the plan.

**What this draft does not cover, checked rather than forgotten.**
The other two queued items were held against this method's own
trigger and do not meet it — the commit count is sayable for both,
so their shape is not in question and a draft would be ceremony.
*Review between versions*: the practice is already in use and
works — stage each version shown, word-diff the next — and the one
open question, whether it is written down here or handed to the
bundle whose `commit-plan` convention it touches, is a plain ask at
planning time. *SL-1's per-test references*: no decision at all,
only work; SL-2's tests say what each is for beside it, SL-1's say
it at class level, and the count is taken when the work starts.
One draft per unsettled shape, never per pile of queued work.

**What is already decided and is not re-opened here.** Two options
were weighed and rejected during SL-2, with reasons in the devlog:
a second, plainer set of records beside these (two masters for one
fact drift), and marking the current records agent-only with a
human version alongside (there is no dialect the agent reads
better). The style rule drafted mid-slice was reverted unstaged, on
purpose; its text is in the devlog entry of 2026-09-21 so this pass
starts from something rather than from nothing.

## The questions, written before any is answered

**Q1. Is the fault the voice, or the shape of certain passages?**
Asked first as "which reader", and that was the wrong cut. The
evidence says the dense voice works in some places and collapses in
others: `intent.md`, the fences and the registry are the same voice
and read fine, because each sentence is short and stands alone;
SL-2's owners table is that voice with three claims packed into one
cell and no separators, and it failed every reader including the
portfolio one the intent names. So the two answers are:
*the voice* — every record is rewritten plainer, for a named
reader, and the project's style changes;
*the shape* — the voice stays, and any passage carrying a
multi-part argument is unpacked into labelled parts with a worked
example, wherever it appears.
*What would make the second wrong:* if the reviewer finds
`intent.md` hard as well. Then it is the voice, the sample below is
not a fair test, and the pass is much larger.
*Settled by:* **ask**, against two real passages — one short-claim,
one argument-carrying — rather than in the abstract.

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

## Order, by what rests on each

1. **Q1** — every other answer changes with it. If the fault is the
   voice, Q5's measurement is a different count and Q4 has a
   different answer.
2. **Q2** — decides which files the pass touches at all.
3. **Q3** — may split the rule in two, which changes Q2's answer
   for one half.
4. **Q5** — a measurement, cheap, and Q4 cannot be asked without it.
5. **Q4** — the commit count.

## Answers

<!-- Filled as each is settled, top first. -->
