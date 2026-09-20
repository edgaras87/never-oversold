---
name: visual-comparison
description: Settle how a structure is shown — a picture, a table, a plain list — by writing down what the reader must get and rendering every candidate against it. Use when a diagram might be the answer, including deciding it should not be. For a choice that is not about showing something, use option-comparison; this is that method specialised to things you look at, and what rendering them has cost us.
---

# Visual Comparison

How a structure is shown is settled by rendering, not by argument.
Write what the reader must get *before* looking at candidates,
build each one, judge it line by line, and let the render decide.

**This is `option-comparison` specialised to things you look at.**
The spine is the same — requirements first, every candidate built,
judged per requirement, recorded in an ADR. What is here and not
there is the failure mode only a picture has: *a notation that
asserts something you did not mean*, and what that has cost.

Kind: playbook — copied into a fresh draft each time, never
executed in place.

## 1. When this fires

A structure is hard to see and more than one way of showing it
could work. A Mermaid dialect against another, a table against a
picture, a numbered list against both.

**The candidate set must contain at least one thing that is not a
picture** — a table, a numbered list, the prose you already have.
Otherwise a picture wins by construction and the comparison cannot
return *no picture*, which is a real answer. In CBC ADR-0028 the table
was the best answer to one requirement and lost on another; a set
without it would have hidden that.

Under this repo's constraint — plain text, rendering on GitHub and
in the IDE with no build step — the buildable notations are Mermaid
and Unicode box drawing. PlantUML, Graphviz and D2 all need a
render step or a plugin and are out; a committed SVG renders but is
not text anyone can read in a diff.

A choice that is not about showing something goes to
`option-comparison`.

It does not fire for a form with one obvious answer, and it does
not fire twice for the same question — the ADR from last time is
the answer.

## 2. The method

1. **Write the requirements first, in a `temp/` draft.** What the
   artifact must *carry*, not what it should look like. Each one
   stated so a candidate can fail it. Name the defect the artifact
   is answering, so a later reader can tell whether it was fixed.

2. **Write them as what a reader must get, not what a form must
   show.** A requirement phrased as "the picture shows X" has
   already decided that it is a picture. This matters whenever the
   candidate set includes a non-picture, and the set usually
   should.

3. **List the candidates, including the plain ones.** A numbered
   list and a table are candidates. So is the form you already
   have — leaving it in gives "nothing wins" somewhere to land.

4. **Build every candidate for real.** Not a sketch, not a
   description. The findings come from building.

5. **Render them where they will actually be read**, and judge
   pass or fail per requirement with the reason. A verdict reached
   by reasoning is a *prediction*; mark it as one until the render
   confirms it. Both times this method has run, the render changed
   a verdict that reasoning had got wrong.

6. **Decide, and record it in an ADR** — the requirements, the
   candidates, and why each lost. Then delete the draft; it has
   served, and git history keeps it.

## 3. Gates

- Every requirement is stated before any candidate is built.
- Every candidate is built and rendered, including the ones
  expected to lose.
- Every verdict cites the requirement it turns on.
- A verdict not yet rendered is marked as a prediction.
- The outcome is in an ADR before the draft is deleted.

## 4. What the render has caught

Kept because it is the evidence this method works, and because a
list that stops growing is the sign it was written too early.

**These are things to check, not rules to obey.** Each names the
case it came from, so you can judge whether yours is like it. An
entry that would give the wrong answer on a different real case is
too broad — narrow it to what was actually observed. If you cannot
name the case, it is not an entry yet.

- **The dialect built for the job can be the one that fails.**
  Mermaid `block-beta` is meant for stacked blocks and lost both
  the arrow labels and the vertical order. `sequenceDiagram` is
  meant for handoffs between actors and had to draw a read-only
  reading as an arrow into the other repo — asserting the opposite
  of the rule the procedure existed to keep. A picture that states
  the opposite of the truth is worse than no picture (CBC ADR-0027,
  CBC ADR-0028).

- **A requirement no candidate can hold is evidence about the
  requirement.** Struck, not failed — and say so in the ADR, so
  the next comparison starts from a corrected spec (CBC ADR-0028).

- **Building is what finds the modelling error.** The two-subgraph
  flowchart could not place the operator, who works in both repos.
  The fix was not a third box but the recognition that the
  operator is transport, not a place (CBC ADR-0028).

- **Using a dialect is not fighting it.** Ordinary syntax is
  ordinary. Invisible links, spacer nodes and nodes declared out
  of meaning order are the fight, and a candidate needing them has
  lost (CBC ADR-0027 decision 2).

## 5. What this does not do

- It does not choose a format for another repo. This repo's
  Mermaid trial is provisional and does not travel (CBC ADR-0027
  decision 3); a run decides its own forms.
- It does not run on a schedule, and it is not a review of forms
  already settled.

---

## Decisions

- CBC ADR-0027 — the comparison is a method that worked once;
  recorded, not adopted, with the second format question as the
  trigger for asking whether it becomes a rule
- CBC ADR-0028 — the trigger fired and was answered here. The
  recommendation was to wait for a third instance: two uses by one
  author in one week is thin evidence. If that objection was
  right, §4 is where it shows, by not growing
- CBC ADR-0030 — the third run was not about showing anything,
  which separated the general method out as `option-comparison` and
  left this one specialised. §4's discipline line is from there,
  and so is §1's rule that the set must hold a non-picture
