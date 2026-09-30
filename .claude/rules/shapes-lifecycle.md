---
paths:
  - ".claude/shapes/**"
foundation: the shapes convention
---

# Shapes: what they are, and how they live

A **shape** says what a kind of output looks like in this project —
its form, never its content. It opens with one line naming what it
covers:

> **Governs:** how `<which parts>` of `<which artifact>` are
> written. Their form, never their content.

That line is how a step finds which shapes apply to what it made. A
file here without it is not a shape.

A shape says its form and nothing of any lifecycle. When the output
it governs opens, extends and closes is the owning convention's to
say; when the shape itself is written, moved or changed is this
file's. A shape that restates either is a copy of it, and a copy
that repeats is what drifts.

## 1. Written from work that exists, never in advance

Write a shape from a pair that recurred — what they share is the
shape — or from one piece judged worth keeping beyond here. Never
from an idea of how the work should go. If no shape covers what a
step made, its gate carries no check for it; do not invent one.

## 2. Two places, and the place is the force

**Unexposed — `.claude/shapes/`.** Nothing loads it; it is opened
at a gate. Keep a shape here while what the work produces *without*
it still matters — above all before this project's first output of
that kind.

**Exposed — `.claude/rules/`, with its own `paths:`.** Loads
whenever a matching path is touched. Move a shape here when
conformance is what is wanted.

**Held at the deliverer.** An unexposed shape reaches this project
only as a delivery staged in `temp/`, read at a gate and not before
(§4). An exposed one arrives as a delivered copy, under
`delivered-copies.md`.

## 3. Moving a shape

The reviewer moves a shape, in both directions; no count does. A
count only prompts the question, and the shape's own dated lines
are what it is asked against. Changing an exposed shape does not
withdraw it; it returns to `.claude/shapes/` only when someone
decides the question is open again.

## 4. At a step's close: what arrives in `temp/`

This project fetches nothing; a shape is there because a person
staged it. At the close:

1. **Look in `temp/` for shapes staged for this step.** Nothing
   there: tick the item with one line saying so — a complete answer,
   not a failure. Something there: read each against what the step
   produced, settle each difference (§5), and tick with a note naming
   what was checked against.
2. **Keep it.** The result moves into `.claude/shapes/` as this
   project's shape for that kind — unchanged, changed by what this
   project found, or merged with what was already here — with dated
   lines saying what arrived, what was taken and what was refused.
   The staging leaves.
3. **Offer, never push.** This project's shapes sit here and are read
   when the deliverer reads this repository. Mark what is this
   project's illustration apart from the skeleton, so a reader can
   take one and leave the other.

Only exposed shapes ship. An unexposed one is offered as a finding.

## 5. A difference

A gate reads what a step made against every shape governing it.
Propose each difference as a diff; never correct it. It ends one of
three ways, judged per difference and never by a policy:

- **the shape was wrong here** — the output stands; the shape
  changes to match it.
- **the output drifted** — the shape stands; the output is brought
  to it.
- **each has something** — the shape takes what this output found,
  and the output is brought to the shape as changed.

Outputs closed before a change are not reopened. An exposed shape
that changes stays exposed.

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
