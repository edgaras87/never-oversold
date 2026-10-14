# Writing

**Governs:** everything written here: chat answers, records,
commit messages, comments. How it reads, never what it says.

<!-- This project's own rule, not a delivered copy. It has no
     `paths:` line, so it loads at the start of every session. -->

## The rules

1. **Lead with the answer.** The first line says the point. The
   reasons follow.
2. **Short sentences. One idea per sentence.**
3. **Label the parts.** When an argument has parts, give each its
   own line or label. Don't chain them with dashes and semicolons.
4. **Show it with numbers.** A worked example with real numbers
   beats a definition. "10 on hand, 4 held, a reserve of 7 is
   refused" beats "a reserve exceeding available units is refused."
5. **Plain words, not metaphor.** Say what happens. Not "kill 6 in
   a second costume", but "the same failure as kill 6, caused a
   second way."
6. **Names are labels.** Keep defined names and references exactly:
   kill 7, W1, G2, SL-3, ADR-0014. Define each coined word once, in
   plain words, in the glossary (`docs/glossary.md`). Coin a word
   only when plain English cannot say it.
   A number local to one record carries its owner outside it:
   "SL-4 G1", never a bare "G1".
7. **Cut what adds nothing.** If a sentence can go without losing
   meaning, it goes.

## Before and after

Before:

> R5, the harness can fail: the wall this slice leans on stands
> already, so it is made absent on the working tree — the two moves
> split apart — and the evidence seen red there, in the build, owed
> there by name.

After:

> R5: the test must be able to fail. The protection already exists,
> because SL-3 built it. So we remove it on the working tree,
> splitting consume into two writes, and check that the test goes
> red. That red run is owed in the build stage.

Same meaning, same references. It can be read once.

## Where it applies

- **Chat:** every answer.
- **Records:** every new or changed line. History keeps its words.
- **Other texts:** a skill or rule may be written densely. Follow
  what it says, but not how it says it. This rule sets the voice.

## Other styles

Plain is the default for every kind of text. A kind of text gets
another style only when plain has shown it falls short there, and
this rule then names that kind and its style.

## Revisions

- 2026-10-11: written on `try/writing-rule`, from the
  retrospective's D2. On trial for the rest of the retro.
- 2026-10-11: plain named the default; another style only where
  plain has shown it falls short.
- 2026-10-11: a local number carries its owner outside its record
  (the retrospective's D6).
- 2026-10-12: kept, at the retrospective's close. No longer on
  trial.
- 2026-10-14: a coined word is defined in the glossary, its one
  place (the retrospective's D2 and D6; Step 10's commit 22).
