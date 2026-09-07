---
name: cbc-slice
description: Work one slice of a correctness-driven backend - take one invariant from the project's slice registry through specify-correctness, plan, build, and document, until a test that CREATES the adversity (concurrent hammering, injected duplicates, kill mid-transaction) proves the invariant survives. Use this whenever the user asks to work, implement, or close a slice, to implement an invariant or idempotency/contention/recovery guarantee, or to continue a project that has a slice registry (docs/system/registry.md) - even if they just say "let's build the next piece". Requires a completed framing AND a bootstrapped system - this skill's Stage 0 checks readiness first and refuses to proceed if the system is not ready. Do NOT use for framing a new project (that is cbc-framing) or for ordinary feature work outside the registry.
---

<!-- Derives from concept v1 of correctness-by-construction
     (ADR-0003). Provenance — archive/cbc/system-design-method
     birth-materials/.claude/skills/cbc-slice/SKILL.md
     @ fe0075d (imported 2026-08-28, PLAN Step 3). Changes on
     import: none — verbatim below this header.
     Re-derived 2026-08-29: the framing exports' paths — they live
     under docs/system/ as intent.md, definition.md, registry.md
     (cbc-framing's layout re-derivation); the trigger description
     and R1 updated to match. -->

# CbC slice — one invariant made real

Carry **one slice** — one invariant × its adversity — to demonstrated
evidence. The completion test is single and non-negotiable: **the
adversity-creating test exists and passes.** No evidence, no closed slice.

The full procedure is `references/cbc-slice-workflow.md` — read it before
Stage 1; this file adds the operating rules and the readiness gate.
`references/worked-example.md` Part 2 shows one slice worked end to end.

## Stage 0 — readiness gate (before anything else)

Read `references/system-readiness.md` and verify against the actual repo:

1. **R1** — the three framing artifacts exist under `docs/system/`
   (`intent.md`, `definition.md`, `registry.md`), and the registry names
   a chosen-next slice with a fold-reconciliation line.
2. **R2** — the repo builds, runs, and the test suite executes.
3. **R3** — the real store is reachable from tests (not mocked — evidence
   against a mock proves the mock).
4. **R4** — the harness can create **the adversity class this slice
   names**: concurrency for contention, replay/injection for duplicate
   delivery, process-kill for partial failure. Check only what the slice
   needs.
5. **R5** — the harness can fail (a deliberately broken invariant turns it
   red).
6. **R6** — the registry is writable, and the project has an agreed place
   to record deviations and sign-offs (the project decides its own record
   scheme; this skill only requires that one exists).

**Any check fails → stop.** Report exactly what is missing and what would
satisfy it; offer to help with the bootstrap as ordinary project work — but
do not open Stage 1. A slice started on an unready system ends with an
unrunnable completion gate and "done" becomes a feeling.

Readiness sign-off is the **human's** call — ask for it explicitly if not
already recorded.

## The stages and their gates

```
Stage 0: ready?  →  1: specify-correctness  →  2: plan  →  3: build  →  4: document
                        (what — no how)         (how)       (real)       (legible)
```

**Stage 1 — specify-correctness.** Take the invariant and adversity **from
the registry entry and the L1 census, as written** — never re-invent them.
Run the guarantee challenge: attack the invariant ("what would let this
hold on paper yet break in fact?") until each distinct answer is a
strategy-free guarantee. Set evidence criteria per guarantee.
**Gate: zero mechanisms.** If the spec mentions a lock, constraint, queue,
key, or any technology — it leaked; park it and restate as a property.
**Human sign-off on the spec before Stage 2.**

**Stage 2 — plan.** One structural owner per guarantee, strongest wall
available: database constraint → type system → single validated entry path
→ runtime check → code review → hope. Justify each against the *named*
adversity ("a unique constraint defeats duplicate delivery because the
second insert cannot physically succeed" — not "we use a unique
constraint"). Hunt escape hatches: admin paths, raw scripts, migrations
that bypass the wall. **Gate: no unowned guarantee** — "all the code being
careful" is the absence of an owner. **Human sign-off on the plan before
Stage 3.**

**Stage 3 — build.** Implement the enforcement — the code should be boring;
cleverness here usually means Stage 1 or 2 was skipped. Build the evidence
tests: tests that **create** the adversity (hammer concurrently, inject the
duplicate, kill mid-transaction) and show the invariant surviving,
guarantee by guarantee. **Gate: every guarantee has an adversity-creating
test, and all pass. A green happy-path suite closes nothing.**

**Stage 4 — document.** Record compactly: invariant → guarantees → each
one's owner → each one's evidence. Close the slice in the registry as
evidence-closed; the registry re-decides what's next (ordering is
re-decided at each close, never assumed from the original expectation).

## Deviations — legal, never silent

Departing from the spec, the plan, or this workflow is allowed; departing
silently is not. Every deviation is recorded in the project's own record
scheme: where, what was done instead, why. If the build reveals a missed guarantee, add it to the spec
with this slice as provenance — guarantees are discovered in flight too.

## Standing guards (mention at close)

When closing a slice, remind the user of the three rot modes to watch:
later features touching this slice's state re-check its spec; a wall
weakened while only its test remains means the guarantee moved from
structure to sampling; every new admin path or script is checked against
the owners it might bypass.
