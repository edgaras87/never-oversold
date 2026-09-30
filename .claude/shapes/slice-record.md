# The shape of a slice record

**Governs:** how three parts of a slice record are written — the
guarantees derived by attack; the owners in its plan, which name
what holds each guarantee; and the faces weighed for a guarantee,
the one chosen and the ones not. Their form, never their content:
what a guarantee says comes from the attack, and what holds it and
why comes from the plan.

<!-- A shape: what a kind of output looks like here, its form and
     never its content. How shapes live — where one sits, who moves
     it, what a gate does with one — is
     .claude/rules/shapes-lifecycle.md, and not repeated here. -->

## What this document controls

**The form of three parts, and nothing else.** The guarantees
derived by attack, the owners in the plan that name what holds
each one, and the faces weighed for a guarantee — how each is laid
out on the page, not what it claims. A guarantee this document
would accept can still be the wrong guarantee; that is the
attack's business, and the specification's.

These three because they are the ones that were written, read,
found wanting and rebuilt — not because they matter most. Every
other section is the slice's own, and a record is as long as its
slice needs. When another section turns out to need a shape, it is added
here with a dated line.

## The three shapes

### A guarantee, in the guarantees section

```markdown
**Gn. <the claim, one sentence, no mechanism>**

*What could go wrong (the attack):* <what would let the invariant
hold on paper and break in fact>
*Say:* <real numbers from this system — 8 held, the operator
asserts 7>

*What we guarantee:* <the property that answers it>
*Say:* <the same numbers, carried through>

*Kills:* <n>

---
```

### An owner, in the plan

```markdown
**Gn. <the guarantee, restated in a line>**

*The wall:* <what holds it, named — a constraint, a statement, an
absence with the test that guards it>

*Why it beats this attack:* <against the named adversity, not
adversity in general>
*Say:* <real numbers>

*If the wall were ever wrong:* <what stands behind it, or nothing
and say so>

---
```

### A face, in the faces weighed for a guarantee

```markdown
**<the guarantee the faces are weighed for, in a line>**

**<the face chosen>** — *chosen*

*How it holds Gn:* <how this face holds the guarantee>

*Cost:* <what choosing it costs>

---

**<a face not chosen>**

*How it holds Gn:* <how it would hold the guarantee>

*Why not:* <why it lost, against the named adversity>

---
```

Five things these encode, so a reader checking a record knows what
is being checked:

- **The parts are labelled in plain words, keeping the method's own
  term where one exists** — *What could go wrong (the attack):*,
  not *Attack:* alone and not *What could go wrong* alone.
- **A worked example follows the claim it illustrates,** on its own
  line, marked `*Say:*`, with real numbers from this system rather
  than "some units".
- **Blocks are separated by a rule** (`---`), so where one ends is
  visible without counting blank lines.
- **A `*Say:*` line is owed wherever a mechanism is involved,** and
  not forced where the claim is already concrete without one. A
  face carries none: the chosen face's numbers are in its owner's
  block, and a rejected face was never run.
- **The chosen face comes first and says so,** and every face
  answers the same two questions under the same labels — *How it
  holds* and then *Cost* for the chosen one, *Why not* for the
  rest — so faces are weighed by reading down, not across.

A passage outside these three parts that carries **one claim** is
left alone. The fences in the definition and the intent's
commitments are the examples: short sentences, each standing by
itself, and nothing here asks for them to change.

The line, if one is ever needed elsewhere: if a reader has to hold
two claims in mind to follow a third, the passage carries an
argument.

## What is exempt, and why

- **Lookup tables** — kill ↔ slice, criterion ↔ test, a records
  index. A table row is one line in the file; a four-column row of
  prose measured 435 characters, which an editor cannot show and a
  diff marks whole when one word changes. Tables stay for lookups,
  where cells are short by nature.
- **Bullet lists of single claims** — the evidence criteria, what
  the slice does not claim, the standing guards.

## What to check at a close

Read the new record and ask:

1. Does every argument-carrying passage have labelled parts?
2. Does every mechanism have a `*Say:*` line with real numbers?
3. Are the blocks separated by rules, and does every set of faces
   put the chosen one first?
4. Are the short-claim passages untouched?
5. Where the record diverges from this shape — did it find
   something better? If so, this document changes and the change
   is dated.

## Revisions

<!-- Dated lines: what changed, what taught it, and what was read
     from elsewhere. Whether this shape has stopped teaching is
     read from these — shapes-lifecycle §3. -->

- 2026-09-22 — written, from SL-2's record. Nothing read from
  elsewhere yet: no shape for this kind has been delivered into
  `temp/`. First use: SL-3's close, which is also its first test.
- 2026-10-01 — the faces weighed for a guarantee join, as blocks,
  and leave the exempt list, where they stood undecided. Taught by
  SL-2 §8's G5 table, whose rows ran to 374 characters on one line.
  Weighed against the table as it was, a short table with blocks
  below, and a numbered list (decisions log, this date). Nothing
  read from elsewhere.
