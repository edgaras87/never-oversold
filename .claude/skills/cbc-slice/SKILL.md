---
name: cbc-slice
description: Work one slice of a correctness-driven backend - take one invariant from the project's slice registry through specify-correctness, plan, build, and document, until a test that CREATES the adversity (concurrent hammering, injected duplicates, kill mid-transaction) proves the invariant survives. Use this whenever the user asks to work, implement, or close a slice, to implement an invariant or idempotency/contention/recovery guarantee, or to continue a project that has a slice registry (docs/system/registry.md) - even if they just say "let's build the next piece". Requires a completed framing AND a bootstrapped system - this skill's Stage 0 checks readiness first and refuses to proceed if the system is not ready. Do NOT use for framing a new project (that is cbc-framing) or for ordinary feature work outside the registry.
foundation: concept v1
---

# CbC slice — one invariant made real

Carry **one slice**, one invariant × its adversity, to demonstrated
evidence. The completion test is single and non-negotiable: **the
adversity-creating test exists and passes.** No evidence, no closed
slice.

This file says what to do at each stage, and each gate.
`references/cbc-slice-workflow.md` holds what this file does not:
the record's outline, why each stage runs as it does, and examples.
Read it before Stage 1. `references/worked-example.md` Part 2 shows
one slice worked end to end.

## Stage 0 — readiness gate (before anything else)

Read `references/system-readiness.md` and verify against the actual
repo:

1. **READINESS-1:** the three framing artifacts exist under
   `docs/system/` (`intent.md`, `definition.md`, `registry.md`), and
   the registry names a chosen-next slice with a fold-reconciliation
   line.
2. **READINESS-2:** the repo builds, runs, and the test suite
   executes.
3. **READINESS-3:** the real store is reachable from tests, not
   mocked. Evidence against a mock proves the mock.
4. **READINESS-4:** the harness can create **the adversity class
   this slice names**: concurrency for contention, replay or
   injection for duplicate delivery, process-kill for partial
   failure. Check only what the slice needs.
5. **READINESS-5:** the harness can fail: a deliberately broken
   invariant turns it red. When no wall exists yet to break (the
   usual first slice), READINESS-5 is answered in this slice's
   Stage 3, and the gate says so. It is not a blocker here.
6. **READINESS-6:** the registry is writable, and the project has an
   agreed place to record deviations and sign-offs. The project
   decides its own record scheme; this skill only requires that one
   exists.

**Any check fails → stop.** Report exactly what is missing and what
would satisfy it. Offer to help with the bootstrap as ordinary project
work, but do not open Stage 1. A slice started on an unready system
ends with an unrunnable completion gate, and "done" cannot be checked.

Readiness sign-off is the **human's** call. Ask for it explicitly if
it is not already recorded.

## The stages and their gates

```
Stage 0: ready?  →  1: specify-correctness  →  2: plan  →  3: build  →  4: document
                        (what — no how)         (how)       (real)       (legible)
```

The record is written as the stages go, in one outline, one block
per guarantee. Which stage writes which part:
`references/cbc-slice-workflow.md`, "The slice's record".

### Stage 1 — specify-correctness

1. **Take the invariant from the registry entry, and the adversity
   from the L1 census, as written.** Never re-invent them. Only when
   no framing exists (the seed-example case) are both named here:
   the invariant as one falsifiable sentence about state that must
   never be violated, and the adversity it must survive.
2. **Name what the framing does not carry.** A slice may meet
   questions the framing never answered: how the thing the invariant
   is about comes to exist, the conventions of the door the adversity
   arrives through, what the first schema holds. The first slice meets
   most of them. Each is decided at the opening, as a record with its
   options, before the specification that depends on it. It is never
   absorbed into code, and never a mechanism: it is still a *what*.
   One recorded as an ADR is **Proposed** until the slice's closing
   records, then **Accepted**.
3. **Run the guarantee challenge.** Attack the invariant: "what
   would let this hold on paper yet break in fact?" Each distinct
   answer becomes a strategy-free guarantee. Keep attacking until no
   new answer comes.
4. **Set an evidence criterion per guarantee:** what test would
   *create* its adversity and show the invariant surviving. Named
   now, built in Stage 3.
5. **Tag them** GUARANTEE-n and EVIDENCE-n, numbered once through the
   record. Outside the record, each carries its slice, owner first:
   "SLICE-4 GUARANTEE-1", "SLICE-4 EVIDENCE-3 · GUARANTEE-2 ·
   KILL-12". The framing's tags (KILL, FOLD, FACT, NOT-OURS, …) are
   used as the framing wrote them.

**Gate: zero mechanisms.** If the spec mentions a lock, a
constraint, a queue, or any technology, it has leaked. Park it and
restate it as a property. **And every flag on the registry row is
answered by name.** A flag says the adversity cannot be staged the
normal way. The spec says what its evidence is instead: staged as
its own evidence, or removed by a definition the spec adopts, with
the removal shown. **Human sign-off on the spec before Stage 2.**

### Stage 2 — plan

1. **One structural owner per guarantee,** the strongest wall
   available: the store's refusal (a constraint, a right withheld)
   → type system → single validated entry path → runtime check →
   code review → hope.
2. **Justify each against the named adversity:** "a unique
   constraint defeats duplicate delivery because the second insert
   cannot physically succeed", not "we use a unique constraint".
3. **Where more than one option could hold a guarantee, put them in
   front of the signer as a comparison,** with a recommendation, the
   way an ADR presents options. Each option says how it holds the
   guarantee, its cost, the size that cost assumes, and what would
   make it worth revisiting.
4. **Hunt escape hatches** (admin paths, raw scripts, migrations that
   bypass the wall) against the definition's trust list. A writer the
   definition trusts is named as trusted, not walled. A writer it
   never mentions is a question for the definition, answered there by
   a dated revision, before it is a wall to build.
5. **Name the surface at its minimum:** only what the guarantees
   need somewhere to live (the door, the schema, the records), and
   nothing beyond. What enters here without a guarantee needing it
   is scope: refuse it, or send it back to the registry.

**Gate: no unowned guarantee.** Each has exactly one named owner and a
one-line reason it defeats the adversity, and the surface is named.
"All the code being careful" is the absence of an owner. **Human
sign-off on the plan before Stage 3.**

### Stage 3 — build

1. **Implement the enforcement.** The code should be boring.
2. **Build the evidence tests:** tests that **create** the adversity
   (hammer concurrently, inject the duplicate, kill mid-transaction,
   replay out of order) and show the invariant surviving, guarantee by
   guarantee. Every reading of the witness after the adversity checks
   the promise first, then the slice's invariant, and only then any
   exact number.
3. **See each one red.** Run it with its wall absent, in a state
   that never lands in history, and record the red from actual
   output. Then watch the same test, unchanged, go green with the
   wall standing.
   - How the wall is made absent is the slice's choice. When this
     slice builds the wall, the naive version can land first and
     the wall be its own diff. When the wall already stands, remove
     it on the working tree. When the wall is a rule over the code,
     plant the violation it forbids.
   - **A red counts only if it fails on the witness.** A test that
     stops on a status, a crash or a setup step before reading the
     state proves it reads statuses. Reorder it, witness first, and
     take the red again.
   - A race whose red needs its window held open (a pause added so
     the interleaving the test is named for actually happens) gets
     that pause on the red tree only, and the record says so.
   - For each wall removed, note whether its backstop was seen
     catching the attack, or is unproven.
   - This is also how READINESS-5 is answered when readiness had no
     wall to break.
4. **Label each test beside itself:** its evidence criterion, its
   guarantee and its kill, with the slice ("SLICE-4 EVIDENCE-3 ·
   GUARANTEE-2 · KILL-12"), and in plain words what it checks and
   what would trip it. The reasons stay in the slice's record.
5. **A test that is not evidence says so on itself.**
   - A **tripwire** guards a decided option, not the promise, and
     cannot fail for the invariant. It names the decision it pins,
     and where the reasons live.
   - **The wall's own check** sends a wall its refusal directly (the
     second key inserted, the forbidden write made), or reads the
     wall where it is defined. It shows the wall stands.
   - Neither discharges a kill. **Every kill still owes a test seen
     red**, and the red run decides which kind a test is, not its
     author.
6. **Record deviations** (below). A guarantee the build reveals is
   added to the spec, with this slice as provenance.

**Gate: every guarantee has an adversity-creating test, all pass,
and each was seen red on the witness with its wall absent, in a
state that never lands in history, recorded from actual output, then
green unchanged.** A green happy-path suite closes nothing. A test
never seen red is not known to be watching.

### Stage 4 — document

1. **Complete the record in its outline:** each guarantee's block
   gains the tests that prove it, and the sections across
   guarantees are finished.
2. **Close the slice in the registry** as evidence-closed. The row
   went to `in-progress` when the specification landed, not at the
   close. The entry gets one line per item under *Answered at
   close*: what the slice left provisional, what it hands to later
   slices by name, and each flag or judgment it settled. Each line
   is the answer in a sentence and where in the record; the reasons
   stay in the record. Then the ordering is re-decided, with its
   reason written.
3. **Where this slice changed how the system is built, add or change
   rows in ARCHITECTURE's table of rules:** one row per rule, never a
   paragraph. How the slice got there stays in its record.
4. **Last, read what the slice made against the project's own
   shapes,** never this skill's, which carries none. Where no shape
   governs what the slice made, nothing is checked. Where one does,
   each difference is **proposed as a diff, never corrected**. How
   each ends is the shapes rule's
   (`.claude/rules/shapes-lifecycle.md`), and the human says which.

**Gate:** the slice is closed in the registry with its answers,
provisionals and hand-ons one line each and the ordering re-decided in
writing; ARCHITECTURE's rows match what the slice built; and the
record has been read against the project's shapes, each difference
ended as the shapes rule says.

## Deviations — legal, never silent

Departing from the spec, the plan, or this workflow is allowed.
Departing silently is not. Every deviation is recorded in the
project's own record scheme: where, what was done instead, and why.

## Standing guards (mention at close)

When closing a slice, remind the user of the three standing guards:
guarantee erosion, testing theater, escape-hatch watch
(`references/cbc-slice-workflow.md`, "Standing guards").
