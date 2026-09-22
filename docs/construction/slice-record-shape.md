# The shape of a slice record

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

## How this is used, and why not sooner

**A slice is written without this document open.** At its close the
record is read against this shape, and then one of two things
happens: the record is corrected, or this document changes because
the slice found something better.

The reason is that a shape imposed at the start hides what the next
slice would have invented. SL-2 was written freely and then
corrected, and the correction is what taught us that labelling the
parts of an argument was not the missing piece and a worked example
with real numbers was — a lesson a template used from the start
would have buried. The same holds at the scale of a project: a
record of what this project arrived at on its own is worth more,
compared with another project's, than a rule handed to both.

The cost, stated so nobody is surprised by it: every slice record
is written once and then reconciled. That is a rewrite per slice.

## What a record contains, and what this document controls

The sections are the slice's own. SL-1 and SL-2 do not carry the
same ones and should not: SL-2 has a §3 for the correction's shape,
which SL-1 had no decision to make; SL-1 carries provisionals that
SL-2 closed. A record is as long as its slice needs.

**Two sections are controlled, and only two:** the guarantees
derived by attack, and the plan's owners. They are controlled
because they are the two that were written, read, found wanting and
rebuilt — not because they matter most. When another section turns
out to need a shape, it is added here with a dated line, and until
then it is the slice's own business.

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

## What this is compared against, and when

Two comparisons, and they are not the same exercise.

**Against this document, at every slice's close.** Its subject is
consistency: does the new record read as the last one did. Nothing
outside the project takes part, so nothing is inherited from
elsewhere.

**Against other projects' baselines, once, after the first slice
closes.** Its subject is learning: has someone else's project found
a better shape. It happens after the first slice and not later,
because the first slice record is the only independent sample a
project ever produces — every record after it is written by someone
who has read the one before, so the influence has already happened
and delaying the comparison protects nothing while costing the rest
of the project the benefit.

In this project that moment has passed in the right order: SL-1 was
written with no reference in hand, and SL-2 inherited from it. So a
baseline read now revises this document without touching the
evidence.

### How a provisional baseline reaches a gate

Not as a file in this repo. A baseline sitting in the working tree
is read during the work whether or not anyone meant it to be — a
grep finds it, and the influence is invisible to the one being
influenced. "Read only at the gate" has to be a fact about when the
file exists, not a promise about discipline. So while baselines are
still being polished, one arrives the way the bundle's deliveries
already arrive: staged into `temp/`, read and diffed there, and the
staging deleted after.

### When a baseline stops being provisional

When several projects have compared against it and stopped finding
anything new — the shape holding across different slices and
different material — it is no longer an experiment and can ship
permanently with the bundle. Two things change then and one does
not. It may sit in the working tree, because there is no longer an
invention to protect. It need not be requested. And the gate still
runs: the check stops asking "did this project find something
better" and starts asking "did this record drift", which is worth
doing against a settled standard too.

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

- 2026-09-22 — written, from SL-2's record. First use: SL-3's
  close, which is also its first test.
