# CbC slice workflow — the outline, the why, the examples

*SKILL.md says what to do at each stage, and each gate. This file
holds only what SKILL.md does not: the record's outline, why each
stage runs as it does, and worked examples. Each section names the
stage it serves.*

---

## The unit

A **slice** is one invariant × one adversity, carried all the way to
passing evidence. Not a feature, not an endpoint, not a ticket.

```
specify-correctness  →  plan  →  build  →  document
     (what)             (how       (make      (make it
                        chosen)    it real)   legible)
```

The first boundary matters most: **what** must be true is settled
completely before any **how** is allowed in. Mixing them is the usual
failure. A mechanism named early is taken for a property, and the
alternatives are never weighed against the actual requirement.

---

## The slice's record — its outline

One record per slice, written as the stages go. This is the minimum
every record needs: where each stage's output goes, and one block
per guarantee. A project may keep a shape that adds to it and says
how each part reads; the shape is read at the close, never while
writing. Beside each part, the stage that writes it.

```
# SLICE-<n> — <the invariant in a few words>

## Invariant                                  specify
## Adversity                                  specify
   what attacks it, and how the evidence creates it, for every
   guarantee alike
## Decided before the specification           the opening, where something was
## Guarantees
### SLICE-<n> GUARANTEE-<k>. <the claim>       one block per guarantee:
   the attack, and what we guarantee          specify
   its evidence criterion                     specify
   what holds it, and what stands behind it   plan
   the options weighed                        plan
   the tests that prove it                    the close
## Folds and flags                            specify: each one the registry handed over, answered
## What this slice does not claim             specify
## The plan, across guarantees                plan: what the walls need, the escape
                                              hatches hunted, the surface, the red as planned,
                                              the deviations and provisionals
## Evidence, as delivered                     build and close: the suite, the tests beside
                                              the evidence, the red runs, what the build found
## Standing guards                            the close
## Sign-offs                                  the human's, one per stage
```

What spans every guarantee has its own section. What belongs to one
guarantee is in its block, and nowhere else: the claim is written
once, at the top of its block. A stage that adds to a block adds to
that block. It does not open a section of its own.

---

## Stage 1 — why, and an example

*Question: what must hold, against what, and what would proof look
like?*

**Taken as written, from the framing.** The kill arrived shaped as an
invariant from the framing's step 3. Using it is the handoff. The seed
example's invariant: "N identical requests → exactly one persisted
order." The adversity is pulled from the L1 census, not invented:
contention, duplicate delivery, crashes and partial failure,
reordering. "Correct" means nothing until it says correct *against
what*.

**What the framing does not carry is still a what.** "An item
becomes known by its first adjustment" names no table. Absorbing
such a decision into code is the silent decision this workflow
forbids. An ADR for it stays Proposed until the close because the
build can still reshape it: a Proposed record is corrected in place,
where an Accepted one could only be superseded.

**Why every flag is answered.** A flag left unanswered is a warning
the slice inherited and dropped.

**Guarantees are derived by attack, never looked up.** This middle
layer is the real work, and textbook examples skip it. For the order
example, the attack yields: uniqueness of the key; a winner and a
loser under concurrency; safe replay of the winner's response;
correct key scope. Each is strategy-free: it says what must hold,
not how.

The output is a **correctness spec**: invariant → guarantees →
evidence criteria, plus the adversity model.

## Stage 2 — why

*Question: what structure makes each guarantee impossible to
violate?*

**"All the code being careful" is a design defect,** flagged before
implementation, not discovered after.

**Why the comparison, and why the size.** Options named in a
paragraph after the choice are not weighed: the reviewer reads past
them and asks afterwards whether alternatives existed. The sign-off
is only real if the alternatives were in front of the signer. And a
cost is true at some size. Read it from the size the definition says
the system is built for, where it says one: an option chosen at one
size can lose at another.

**Why a trusted writer is not walled.** Any path that bypasses the
wall makes the guarantee false, so every write path goes through the
owner. But a guarantee cannot wall against a writer who can remove the
wall, such as the store's superuser or the identity the definition
names as the system's own. Trying turns every slice into an open-ended
defence.

**Why the surface is minimal.** The door exists because the
invariant needs one, and a slice is not a feature.

## Stage 3 — why

*Question: does the invariant actually survive the adversity?*

**Boring code.** The thinking already happened. Surprising
cleverness here usually means Stage 1 or 2 was skipped.

**Create, don't sample.** A test that runs one request at a time is
not evidence for a contention guarantee. The evidence must generate
the adversity the spec names.

**Why the promise is read first.** A test that checks only the
numbers it expected passes when the promise breaks in a way it did
not foresee.

**Why the race's pause stays on the red tree.** The evidence as
committed runs without it, so what is committed is what runs.

**Why the backstop is noted at the red run.** The red run is the one
moment the backstop meets the attack. A backstop not seen there is
recorded as unproven, never assumed.

**Why each test is labelled beside itself.** A reader who lands on a
test should not have to rebuild its purpose from its assertions. The
reasons (why this option, why this form) stay in the slice record,
or the two drift apart and neither can be trusted.

**Why a tripwire is kept, and why it counts for nothing.** It goes red
only when someone changes, by hand, something the slice chose on
purpose. That is worth keeping: when it goes red, it tells whoever
changed that choice that it was made on purpose. But a test that
cannot be made red by removing the wall it claims to guard is not
evidence, whatever it is called.

**Why the wall's own check.** It proves the wall stands before any
path leans on it, and it notices a wall dropped "for a while" before
any evidence test samples around the gap. It creates no adversity, so
it is not evidence.

## Stage 4 — why

*Question: can a stranger see what is promised and why to believe
it?*

**The record is the slice's visible product.** In this discipline
the guarantee depth, not the feature, is what is on display.

**Why the close writes its answers.** A built slice can teach that
the next expected slice is wrong, split or unnecessary, but the
registry can only re-decide on what the close wrote down. Examples
of what a close hands on: a wall that already holds part of a later
invariant; a counter that over-approximates until a later slice ends
something; a choice a later slice must make. And a
registry that reads `chosen-next` through a whole build is not the
source of truth for what is being worked.

**Why ARCHITECTURE gains rows, not paragraphs.** It answers what a
later slice may rely on now, so it stays short and current. Each row
names what computes the rule, what refuses a wrong result, the test
that goes red, and the slice that proved it.

**Why the shapes are the project's own.** A shape handed down is an
answer a project did not earn, and one adopted from elsewhere hides
what this project would have arrived at itself. Whether an output
is worth writing a shape from is a judgement the project makes when
it makes it, not a step of this close. Why a shape is read only at
the close: the shapes rule, "One place, read at the gate".

---

## Standing guards (after the slice closes)

- **Guarantee erosion:** any later feature touching this slice's
  state re-checks this spec before shipping. New surface is new
  attack surface against your own walls.
- **Testing theater:** if a guarantee's wall weakens over time (a
  constraint dropped "temporarily", a type loosened) and only the
  test remains, the guarantee has silently moved from structure to
  sampling. Structure forbids; tests only sample.
- **Escape hatch watch:** every new admin path or script is checked
  against the owners it might bypass.

## Working defaults

Two choices are working defaults, overridable with a recorded
reason:

- **Prove and verify live inside build:** evidence tests are Stage
  3's second move, with no separate gate between build and document.
- **The spec is prose under fixed headings** (*invariant /
  adversity / guarantees / evidence criteria*), one spec per slice.
  Structure enough to diff, prose enough not to front-load a schema.
