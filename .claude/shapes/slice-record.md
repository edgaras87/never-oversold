# The shape of a slice record

**Governs:** how two sections of a slice record are written — the
guarantees derived by attack, and the owners in its plan, which
name what holds each guarantee. Their form, never their content:
what a guarantee says comes from the attack, and what holds it
comes from the plan.

<!-- Kind: model (artifact-kinds). It describes how a finished slice
     record is put together; it demands nothing while a slice is
     being worked. The hybrid, noted rather than forced: at one
     moment — the slice's close — it does bind, in that a record
     that diverges is either corrected or its divergence recorded.
     That is convention force at a single point, which the kinds
     vocabulary has no word for.

     Derived 2026-09-22 from SL-2's record after it was written,
     read and corrected; not from a guess about what would read
     well. What it is not: an instruction to the next slice. -->

## How this is used

**A slice is written without this document open**, and read against
it at the close. `.claude/rules/shapes-lifecycle.md` says why, and
everything else about how shapes live: when one is written, what
its place does, who moves one between places, and what a gate does
with a shape delivered into `temp/`.

What belongs here and nowhere else is the shape itself — the two
sections it governs, and what they look like.

The cost, stated so nobody is surprised by it: every slice record
is written once and then reconciled. That is a rewrite per slice.

## What this document controls

**The form of two sections, and nothing else.** The guarantees
derived by attack, and the owners in the plan that name what holds
each one — how each is laid out on the page, not what it claims. A guarantee this document would
accept can still be the wrong guarantee; that is the attack's
business, and the specification's.

These two because they are the two that were written, read, found
wanting and rebuilt — not because they matter most. Every other
section is the slice's own, and a record is as long as its slice
needs. When another section turns out to need a shape, it is added
here with a dated line.

## The two shapes

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

Four things these encode, so a reader checking a record knows what
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
  not forced where the claim is already concrete without one.

A passage outside these two sections that carries **one claim** is
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
- **Comparison tables** — the faces weighed for a guarantee. Not
  yet decided either way; SL-2 left them as tables and said so.
- **Bullet lists of single claims** — the evidence criteria, what
  the slice does not claim, the standing guards.

## What to check at a close

Read the new record and ask:

1. Does every argument-carrying passage have labelled parts?
2. Does every mechanism have a `*Say:*` line with real numbers?
3. Are the blocks separated by rules?
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
