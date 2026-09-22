---
paths:
  - ".claude/shapes/**"
---

# Shapes: what they are, and how they live

A **shape** says what a kind of output looks like in this project —
the guarantees section of a slice record, the test machinery a
slice leans on, whatever else turns out to repeat.

It opens with one line naming what it covers, in this form:

> **Governs:** how `<which parts>` of `<which artifact>` are
> written. Their form, never their content.

That line is how a step finds which shapes apply to what it made,
and the form-not-content half is what stops a shape check being
mistaken for a review of the work. A file here without such a line
is not a shape.

## 1. A shape is written from work that exists, never in advance

What is ruled out is one thing only: a shape designed before
anything has been built, from an idea of how the work should go. It
would be a guess dressed as a standard, and everything measured
against it afterwards would be measuring the guess.

Two ways one is born, and neither is better:

**It recurred.** A second instance of a kind appears, and the pair
is the evidence — what they share is the shape, what differs is
either the material's doing or a mistake, and both are visible.

**It was judged worth keeping.** Someone looks at one piece and
says this should hold beyond here — a section rebuilt until it
could be read, machinery a slice leans on that another project
would need too. The shape is written from that piece, and the next
output of its kind is what tests it.

This project's first shape came that way. SL-2's record was hard to
read in two sections, they were rebuilt until they were not, and
the result was worth keeping. Nothing was compared; a piece was
judged.

**If no shape covers what a step made, its gate carries no check
for it.** That is not a gap to be filled. There is nothing to check
against yet, and inventing something to check against is the guess
this section rules out.

## 2. Exposed or not, and the place is the mechanism

One axis, and it is not how settled a shape is. It is whether the
shape is in front of whoever does the work. Both kinds go on
changing; changing is not what separates them.

**Unexposed — `.claude/shapes/`.** Inert: nothing loads it, and it
is opened at a gate. Chosen while it still matters what the work
produces *without* it — most of all for the first output of a kind
this project makes, which is the only one nothing here has yet
influenced.

**Exposed — `.claude/rules/`, with its own `paths:`.** Loads
whenever a matching path is touched, which is the point: conformance
is now what is wanted, and keeping a shape out of sight only makes
the next output re-derive it badly.

**Held by the collector — one repository, not a peer project.**
Unexposed shapes from every project are held in one place, the
repository this project takes its method from; projects do not read
each other's directly. Having one in the tree before this project
has made its own first output of that kind spends the only
independence there is — so it reaches this project only as a
delivery staged in `temp/`, read at a gate and not before (§4).
Exposed shapes are the opposite: they ship, because being in front
of a writer is what they are for.

## 3. Moving a shape between the two

**The reviewer decides, in both directions.** Exposing a shape and
withdrawing it again are judgements, and no count makes them. What
a count can do is prompt the question: a shape that has taken
nothing up across several closes has probably stopped teaching, and
one that keeps changing probably still is. Both readings live in
the shape's own dated lines, which is what the question is asked
against.

**Changing an exposed shape does not withdraw it.** A shape in
`.claude/rules/` may gain a dated line like any other; it is still
the thing the work is held to. It returns to `.claude/shapes/` only
when someone decides the question is open again — that what the
next output invents without it is worth seeing once more.

## 4. What arrives in `temp/`, and what the gate does with it

This project cannot fetch anything. It holds no address for the
collector and reaches no repository but its own; what comes, comes
because a person asked for it there and it was staged here. So the
gate's act is local and always the same: **look in `temp/` for
shapes staged for this step.**

**Nothing there.** The item is ticked with one line saying so —
nothing was delivered for this step's kinds of output. That is a
complete answer, not a failure: what the item asks is that the
question was put to the folder, and "none" is what the folder said.
A tick with no note is what would be wrong.

**Something there.** Each is read against what the step produced,
each difference settled (§5), and the item ticked with a note
naming what was checked against — so a later reader knows which
shapes this output has met and which it has not.

**Afterwards.** The result moves into `.claude/shapes/` as this
project's shape for that kind, whether it came back unchanged,
changed by what this project found, or merged with what was already
here. The staging leaves; the shape stays, with dated lines saying
what arrived, what was taken from it and what was refused.

**Why kept rather than deleted.** Deleting would only hide what has
already been read. Keeping it makes the next gate cheap and the
reconciliation possible: the collector reads this project's version
and its dated lines against what it sent, and decides whether to
take the change or leave its own standing.

**Staleness, honestly.** A kept copy is a snapshot, current only as
of the delivery it came from, and nothing here can tell when the
collector's has moved on. A fresh delivery is the only refresh, and
asking for one is a person's act in the collector's repository, not
this project's.

**What travels the other way.** This project's shapes are offered,
never pushed, and by the same route in reverse: they sit here and
are read when the collector reads this repository. What is offered
is a finding, not a proposal — it says what one project arrived at,
and the collector decides where such things live on its side, how
it notices a new or changed one here, and how it reconciles them.

**A shape is written in two separable layers, and only one of them
travels well.** The skeleton and what it encodes are general by
construction: they say *a worked example with real numbers from
this system*, not which numbers. The illustrations filling the
placeholders are this project's, and are marked as such. A
collector takes the first and leaves the second, and nothing has to
be rewritten at the hand-off to make that possible.

What must not travel is not a wording but a status: a shape handed
down as a standard is inherited rather than derived, and the next
project's own answer is lost before it is written. That is why an
unexposed shape is offered as a finding — the protection is in how
it moves, not in how vaguely it is phrased.

**What may be shipped.** Exposed shapes only, as rules with their
`paths:`, because being in front of a writer is what they are for.

## 5. What a difference does, when a gate finds one

A gate reads what a step made against every shape that governs it.
Each difference ends one of three ways, and which one is a
judgement about *this* difference — not a policy of always
preferring one side.

**The shape was wrong here.** The output stands as it is and the
shape changes to match it. This is the case when the material had
something the shape did not anticipate — a guarantee with a part
the skeleton has no line for, an owner that is an absence rather
than a thing. The shape was a guess about what outputs look like,
and this output is the better evidence.

**The output drifted.** The shape stands and the output is brought
to it. This is the case when the difference is nothing but
variation — a label reworded, a part left out because the writer
forgot it rather than because the material refused it.

**Each has something.** The shape takes what this output found,
and the output is then brought to the shape as changed. Nothing
says a difference resolves to one side whole.

Two things follow, whichever way a difference went.

**Outputs closed before the change are not reopened.** Each looks
like what the shape asked for when it was written, and the
difference between them is the record of when the shape changed.

**An exposed shape that changes stays exposed.** A dated line is
not a withdrawal; §3 says who decides that, and when.

## 6. What this does not govern

The gate items themselves, and how a step carries them, are PLAN's.
This file is the shapes: when one is written, where it lives, what
that place does, who moves one between them, and how one is asked
of the collector.
