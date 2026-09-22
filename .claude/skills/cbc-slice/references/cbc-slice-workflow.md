# CbC slice workflow

*The core loop for one slice: **one invariant made real**. Input: one local
problem (L4) from the framing — an invariant and the adversity that
threatens it. Output: demonstrated evidence that the invariant survives.*

---

## The unit

A **slice** = one invariant × one adversity, carried all the way to passing
evidence. Not a feature, not an endpoint, not a ticket. The completion test
is single and non-negotiable: **the adversity-creating test exists and
passes**. No evidence, no closed slice.

```
specify-correctness  →  plan  →  build  →  document
     (what)             (how       (make      (make it
                        chosen)    it real)   legible)
```

The first boundary is the load-bearing one: **what** must be true is settled
completely before any **how** is allowed into the room. Fusing them is the
classic corruption — a mechanism named early masquerades as a property, and
alternatives are never weighed against the actual requirement.

---

## Stage 1 — specify-correctness

*Question: what must hold, against what, and what would proof look like?*

1. **Take the invariant from the framing's registry entry — as written,
   zero translation.** The kill arrived invariant-shaped from step 3 of the
   framing; consuming it is the handoff. Only when no framing exists (the
   seed-example case) is the invariant named directly here: a property of
   state that must never be violated, one sentence, falsifiable. ("N
   identical requests → exactly one persisted order.")
2. **Take the adversity from the L1 census — pulled, not invented.**
   Precisely what threatens the invariant — contention, duplicate delivery,
   crashes/partial failure, reordering. "Correct" is meaningless until you
   say correct *against what*. Same fallback: named directly only when no
   framing exists.
3. **Name the whats the framing does not carry.** A slice may need
   answers the framing never gave — how the thing the invariant is about
   comes to exist at all, the conventions of the door the adversity
   arrives through, what the first schema holds. The first slice meets
   most of them. Each is a *what*, decided at the opening as a record with
   its options, before the specification that depends on it; absorbing one
   into code is the silent decision this workflow forbids. It is still not
   a mechanism — "an item becomes known by its first adjustment" names no
   table.
4. **Run the guarantee challenge.** Attack your own invariant: *"what would
   let this hold on paper yet be violated in fact?"* Every distinct answer
   becomes a **guarantee** — a strategy-free sub-property that must hold.
   (For the order example: uniqueness of the key; a winner and a loser under
   concurrency; safe replay of the winner's response; correct key scope.)
   Guarantees are **derived by attack, never looked up** — this middle layer
   is the real work; textbook examples skip it.
5. **Set evidence criteria.** For each guarantee: what test would *create*
   its adversity and show survival? Named now, built in stage 3.

**Exit:** a **correctness spec** — invariant → guarantees → evidence
criteria, plus the adversity model — and every flag the registry row
carries answered by name: a flag means the adversity cannot be staged the
normal way, so the spec says what its evidence is instead, staged as its
own evidence or removed by a definition the spec adopts, with the removal
shown. **Gate: zero mechanisms.** If the spec mentions a lock, a
constraint, a queue, or any technology — it has leaked; park the mechanism
and restate as a property.

## Stage 2 — plan

*Question: what structure makes each guarantee impossible to violate?*

1. **Assign one structural owner per guarantee**, choosing the strongest
   wall available:

   ```
   database constraint → type system → single validated entry path
   → runtime check → code review → hope
   ```

   A guarantee owned by "all the code being careful" is a design defect —
   flag it now, before implementation.
2. **Justify each choice against the named adversity.** Not "we use a
   unique constraint" but "a unique constraint defeats duplicate delivery
   because the second insert cannot physically succeed." Where more than
   one face could hold the guarantee, the plan shows the candidates as a
   comparison the signer can weigh — each face, how it holds the
   guarantee, its cost — and recommends one, the way an ADR presents
   options. Faces named in a paragraph after the choice are not weighed;
   the reviewer reads past them and asks afterwards whether alternatives
   existed. The sign-off is only real if the alternatives were in front of
   the signer.
3. **Hunt escape hatches.** Any path that bypasses the wall — an admin
   endpoint, a raw script, a migration — turns the guarantee into fiction.
   Every write path goes through the owner, or the owner is not an owner.
4. **Name the surface at its minimum.** Only what the guarantees need
   somewhere to live — the door they are attacked through, the schema
   the walls stand in, the records the close needs — and nothing beyond.
   A slice is not a feature: the door exists because the invariant needs
   one, and anything entering here that no guarantee needs is scope, to be
   refused or sent back to the registry.

**Exit:** every guarantee has exactly one named owner and a one-line reason
it defeats the adversity; the surface named at its minimum. **Gate: no
unowned guarantees.**

## Stage 3 — build

*Question: does the invariant actually survive the adversity?*

1. **Implement the enforcement.** The code should be boring — the thinking
   already happened. Surprising cleverness here usually means stage 1 or 2
   was skipped.
2. **Build the evidence tests — tests that CREATE the adversity**, not
   sample around it: hammer concurrently, inject duplicate deliveries, kill
   the process mid-transaction, replay out of order. Then watch the
   invariant survive, guarantee by guarantee. A test that runs sequentially
   is not evidence for a contention guarantee — the evidence must generate
   the same adversity the spec names.
3. **Record deviations.** Departing from the spec or plan is legal, silent
   departure is not — every deviation lands in the project's own record
   scheme. If the build reveals a missed guarantee, add it to the spec with
   this slice as provenance — guarantees are discovered in flight too.

4. **See it red.** Run each evidence test with its wall absent — in a
   state that never lands in history — and record the red from actual
   output;
   then, with the wall standing, watch the same test, unchanged, go green.
   How the wall is made absent is the slice's choice: when this slice
   births the wall, the naive version can land first and the wall be its
   own diff; when the wall already stands, remove it and restore it; when
   the wall is a rule over the code, plant the violation it forbids. This
   is also how a slice answers R5 when readiness had no wall to break —
   the usual first slice.

5. **Say on each test what it is for.** Beside the test, not only in the
   record: which evidence criterion, which guarantee, which kill, and then
   in plain words what it checks and what would trip it. The reasons —
   why this face, why this shape — stay in the slice record, or the two
   drift apart and neither can be trusted. A reader who lands on a test
   should not have to reconstruct its purpose from its assertions.
6. **A test that is not evidence says so.** A slice may leave behind a
   test that cannot fail for the invariant, because what it guards is a
   decided face: it goes red only when someone changes, by hand, something
   the slice chose on purpose. That is worth keeping — it speaks to
   whoever stands over it when it reddens — but it says on itself that it
   is a tripwire, which decision it pins, and where the reasons live. And
   the counting rule that keeps this from becoming a hole: **a tripwire
   never discharges a kill.** Every kill still owes a test that was seen
   red, and the red run decides which kind a test is, not its author — a
   test that cannot be reddened by removing the wall it claims to guard is
   not evidence, whatever it is called.

**Exit:** all evidence tests pass. **Gate: every guarantee has a test that
creates its adversity, and was red without the wall — a green suite of
happy-path tests closes nothing, and a test never seen red proves only
that it runs.**

## Stage 4 — document

*Question: can a stranger see what is promised and why to believe it?*

Record, compactly: the invariant → its guarantees → the owner enforcing
each → the evidence proving each. This is the slice's visible product — in
this discipline the guarantee depth, not the feature, is what's on display.

The registry close is more than a status flip. The row went to
`in-progress` when the specification landed — the first project-visible
work — so the registry never read `chosen-next` through a build. At the
close, the row's entry names what this slice left provisional and what it
hands to later slices, by name: a wall that already holds part of a later
invariant, a counter that over-approximates until a later slice ends
something, a shape a later slice must decide. Then the ordering is
re-decided with its reason written. This is the step that lets a built
slice teach that the next expected slice is wrong, split, or unnecessary
— the registry can only re-decide on what the close wrote down.

**Last, read what the slice made against the project's own shapes.** A
project may hold a record of what a kind of its output looks like —
the form of a slice record's sections, of the machinery the evidence
leans on. One may govern each thing this slice produced, or none may.
They are the project's, never this skill's: a shape handed down is an
answer a project did not earn, and one adopted from elsewhere hides
what this project would have arrived at on its own.

- **None for what this slice made?** Nothing is checked, and that is
  correct. Whether this output is worth writing a shape from is a
  judgement the project makes when it makes it, not a step of this
  close.
- **One exists?** Read what the slice made against it and propose, as
  a diff, every place they differ. **Propose, never correct.** Each
  difference ends one of three ways and the human says which: the
  shape was wrong here, so it changes and the output stands; the
  output drifted, so it is brought to the shape; or each has
  something, and both move.

This runs at the close and not before, and the reason is not
independence — inside one project the records inherit from each other
anyway, the writer having read the last one. It is that a record
written from the shape follows the shape, and a record written from
the slice follows the material. Where this slice's material does not
fit — a guarantee with a part the shape has no line for, an owner that
is an absence rather than a thing — writing from the material shows
it, and writing into the shape hides it as a filled-in form. A shape
record that never changes is either finished or unread.

**Exit:** the slice closes in the registry as evidence-closed, with its
provisionals and hand-ons named and the ordering re-decided in writing;
and its record has been read against the project's shape, with each
difference either corrected or taken up into the shape.

---

## Standing guards (after the slice closes)

- **Guarantee erosion:** any later feature touching this slice's state
  re-checks this spec before shipping. New surface = new attack surface
  against your own walls.
- **Testing theater:** if a guarantee's wall weakens over time (constraint
  dropped "temporarily", type loosened) and only the test remains, the
  guarantee has silently moved from structure to sampling. Structure
  forbids; tests only sample.
- **Escape hatch watch:** every new admin path or script is checked against
  the owners it might bypass.

## Working defaults

Two choices are heuristics — working defaults, overridable with a recorded
reason:

- **Prove/verify lives inside build**: evidence tests are stage 3's second
  move, no separate gate between build and document.
- **The spec is prose under fixed headings** — *invariant / adversity /
  guarantees / evidence criteria* — one spec per slice. Structure enough to
  diff, prose enough not to front-load a schema.