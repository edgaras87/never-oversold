---
paths:
  - ".claude/shapes/**"
foundation: the shapes convention
---

# Shapes: what they are, and how they live

A **shape** says what a kind of output looks like in this project:
its form, never its content. It opens with one line naming what it
covers:

> **Governs:** how `<which parts>` of `<which artifact>` are
> written. Their form, never their content.

A step finds the shapes that apply to its work by that line. A file
without it is not a shape. A shape says nothing about when it is
written or changed; that is this file's.

## 1. Written from work that exists

A shape is written from outputs that already exist: a pair that
recurred, or one piece worth keeping. Never from an idea of how the
work should go. If no shape covers what a step made, its gate checks
nothing for it.

Before a shape is written, its examples are read side by side, with
two questions:

- **Where do they differ?** Each difference ends as one form for
  all, a variant the shape names and allows, or a better form
  neither example had.
- **What could they all do better?** Read against the writing rule,
  the project's decisions, and what went wrong. A weakness every
  example shares never shows up by comparing them. Example: twenty
  entries that all lack a worked example with numbers.

The reviewer settles what the look finds; anything the writer adds
beyond it is named as theirs. Then the shape is tried on the
examples it came from, and on an earlier version where a miss is
known. A shape catches a part that is missing, not a part that kept
its place but lost half its meaning: only the source shows that.

## 2. One place, read at the gate

Every shape lives in `.claude/shapes/`. Nothing loads it while the
work is written. It is read at the step's closing gate.

Why: work written without the shape in view can show that the shape
is wrong, or that a new one is needed. What must hold while the work
is written is not a shape. It belongs to the skill or the rule that
runs the work.

## 3. A difference at the gate

The gate reads what the step made against every shape that governs
it. The comparison is by judgment, not by text: does the output
have the parts the shape names, in its order, reading as it says?
Each difference is named, with the line from the output and the
line from the shape. It ends one of four ways, judged one
difference at a time. Any change it leads to, in the output or in
the shape, is shown before it is made, never applied silently:

- **The output drifted.** The shape stands; the output is brought
  to it.
- **The shape was wrong here.** The output stands; the shape
  changes, with a dated line.
- **Each had something.** The shape takes what the output found,
  and the output is brought to the changed shape.
- **The output is a different kind.** Both stand, and a new shape
  starts from the output (§1). Either of the last three can name
  new work (§4).

Outputs closed before a change are not reopened.

## 4. When a shape names work

A shape mostly names form: the order of parts, how they read. Some
parts need work done during the step. Example: a backstop "seen at
the red run" can only be known by looking during the red run.

When a shape is written or changed so that it names such a part,
the skill that runs the work is checked. If the skill does not ask
for that part, it is changed too, under `delivered-copies.md`.
Otherwise the gate finds the part missing when it is too late to
produce, and the work must be redone.

## 5. Offering a shape back

The deliverer reads this project's shapes when it reads the
repository. A shape marks what any project would want (its
skeleton) apart from this project's example (its illustration), so
the deliverer can take one and leave the other. How an edit reaches
the deliverer is `delivered-copies.md`'s.

## 6. What this does not govern

The gate items themselves, and how a step carries them.

---

## Decisions

- CBC ADR-0035 — written in never-oversold from its own lived work
  and taken here reshaped: the holder is named as the deliverer,
  which the run's `delivered-copies.md` names, and the role-name it
  arrived under is not adopted
- CBC ADR-0037 — shapes are a convention; this rule derives from its
  manual, keeps the definition because a run cannot open the manual,
  and says that a shape states form and never its lifecycle
