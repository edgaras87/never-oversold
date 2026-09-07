<!-- Derives from concept v1 of correctness-by-construction
     (ADR-0003). Provenance — archive/cbc/system-design-method
     birth-materials/.claude/skills/cbc-slice/references/cbc-slice-workflow.md
     @ fe0075d (imported 2026-08-28, PLAN Step 3). Changes on
     import: none — verbatim below this header. -->

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
3. **Run the guarantee challenge.** Attack your own invariant: *"what would
   let this hold on paper yet be violated in fact?"* Every distinct answer
   becomes a **guarantee** — a strategy-free sub-property that must hold.
   (For the order example: uniqueness of the key; a winner and a loser under
   concurrency; safe replay of the winner's response; correct key scope.)
   Guarantees are **derived by attack, never looked up** — this middle layer
   is the real work; textbook examples skip it.
4. **Set evidence criteria.** For each guarantee: what test would *create*
   its adversity and show survival? Named now, built in stage 3.

**Exit:** a **correctness spec** — invariant → guarantees → evidence
criteria, plus the adversity model. **Gate: zero mechanisms.** If the spec
mentions a lock, a constraint, a queue, or any technology — it has leaked;
park the mechanism and restate as a property.

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
   because the second insert cannot physically succeed."
3. **Hunt escape hatches.** Any path that bypasses the wall — an admin
   endpoint, a raw script, a migration — turns the guarantee into fiction.
   Every write path goes through the owner, or the owner is not an owner.

**Exit:** every guarantee has exactly one named owner and a one-line reason
it defeats the adversity. **Gate: no unowned guarantees.**

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

**Exit:** all evidence tests pass. **Gate: every guarantee has a test that
creates its adversity — a green suite of happy-path tests closes nothing.**

## Stage 4 — document

*Question: can a stranger see what is promised and why to believe it?*

Record, compactly: the invariant → its guarantees → the owner enforcing
each → the evidence proving each. This is the slice's visible product — in
this discipline the guarantee depth, not the feature, is what's on display.

**Exit:** the slice closes in the registry as evidence-closed.

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