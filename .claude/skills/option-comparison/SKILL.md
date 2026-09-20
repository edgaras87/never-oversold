---
name: option-comparison
description: Settle a choice by building every option — including the one you expect to lose — and judging each against requirements written down first. Use when more than one option could work, each can be built cheaply enough to look at, and the argument has become about taste. When the choice is how to show a structure, use visual-comparison, which is this method specialised to things you look at.
---

# Option Comparison

A choice is settled by building, not by arguing. Write what the
thing must carry *before* looking at candidates, make each one
real, judge it line by line, and let what you built decide.

Kind: playbook — copied into a fresh `temp/` draft each time, never
executed in place.

## 1. When this fires

More than one option could work, **each can be built cheaply enough
to look at**, and the discussion has become about taste.

**Buildability is the scope.** An option you can only argue about
is out: "should we ship this to runs" has no candidates to build,
and a table of opinions is what this method exists to replace. If
making a candidate real would cost more than living with the wrong
answer for a while, this is not the tool.

It does not fire for a choice with one obvious answer, and it does
not fire twice for the same question — the ADR from last time is
the answer. It does not need a `decide-first` to send it; the
question arrives from wherever it arrives.

## 2. The method

1. **Write the requirements first, in a `temp/` draft.** What the
   thing must *carry*, not what it should look like. Each one
   stated so a candidate can fail it. Name the defect the choice is
   answering, so a later reader can tell whether it was fixed.

2. **Write them as what a reader or user must get, not what a
   candidate must do.** A requirement phrased as "the document
   shows X" has already decided it is a document. This matters
   whenever the candidate set includes something of another kind,
   and the set usually should.

3. **List the candidates, including the plain ones and the one you
   already have.** Leaving the current thing in gives "nothing
   wins" somewhere to land.

4. **Build every candidate for real.** Not a sketch, not a
   description. The findings come from building, and they come
   most often from the candidate you expected to lose.

5. **Put each where it will actually be used, and judge pass or
   fail per requirement with the reason.** For a document, read it
   as its reader would; for a layout, build the tree and list it;
   for a plan, write it against work you already know the shape of.
   A verdict reached by reasoning is a *prediction* — mark it as
   one until building confirms it. Every time this method has run,
   building has changed a verdict that reasoning had got wrong.

6. **Decide, and record it in an ADR** — the requirements, the
   candidates, and why each lost. Then delete the draft; it has
   served, and git history keeps it.

## 3. Gates

- Every requirement is stated before any candidate is built.
- Every candidate is built, including the ones expected to lose.
- Every verdict cites the requirement it turns on.
- A verdict not yet confirmed by building is marked as a prediction.
- The outcome is in an ADR before the draft is deleted.

## 4. What building has caught

Kept because it is the evidence this method works, and because a
list that stops growing is the sign it was written too early.

**These are things to check, not rules to obey.** Each names the
case it came from, so you can judge whether yours is like it. An
entry that would give the wrong answer on a different real case is
too broad — narrow it to what was actually observed. If you cannot
name the case, it is not an entry yet.

- **A candidate can contain something the others cannot express,
  not merely arrange the same parts differently.** Comparing the
  shapes a commit plan's step list could take, the cascade held a
  question none of the other three could ask — and it was the
  question whose absence had cost the set being replanned. Look for
  what a candidate makes *askable*, not only for what it shows
  (CBC ADR-0030).

- **A requirement can turn out to be the consequence of the real
  one.** "Undo one step and the repo is coherent" is what follows
  from a reviewer knowing what is true at a stop, not the thing
  itself. Building the candidate that satisfied it best is what
  exposed the wrong level (CBC ADR-0030).

- **A requirement no candidate can hold is evidence about the
  requirement.** Struck, not failed — and say so in the ADR, so the
  next comparison starts from a corrected spec (CBC ADR-0028, carried
  here because it is not about pictures).

## 5. What this does not do

- It does not decide *which* questions get settled or in what order
  — that is `decide-first`.
- It does not choose for another repo. A run decides its own forms.
- It does not run on a schedule, and it is not a review of choices
  already settled.

---

## Decisions

- CBC ADR-0030 — the method separated from `diagram-comparison`
  when its third run was not about a diagram. The spine is shared;
  what differs is how a candidate is made real and where it is
  looked at, which is why this is a sibling and not a copy
