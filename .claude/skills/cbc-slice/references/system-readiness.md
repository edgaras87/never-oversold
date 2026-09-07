<!-- Derives from concept v1 of correctness-by-construction
     (ADR-0003). Provenance — archive/cbc/system-design-method
     birth-materials/.claude/skills/cbc-slice/references/system-readiness.md
     @ fe0075d (imported 2026-08-28, PLAN Step 3). Changes on
     import: none — verbatim below this header.
     Re-derived 2026-08-29: the framing exports' paths — they live
     under docs/system/ as intent.md, definition.md, registry.md
     (cbc-framing's layout re-derivation); R1 updated to match. -->

# System readiness — the contract between framing and slicing

*What must exist before the first slice can be worked. The framing hands off
TO this checklist; the slice workflow's Stage 0 checks AGAINST it. How each
item gets satisfied — which store, which language, which harness tooling —
is deliberately unspecified: bootstrap is project work, not method work.*

## R1 — The framing artifacts are in the repo

- `docs/system/intent.md` — the one promise, with banked rejections.
- `docs/system/definition.md` — L1–L5, each layer filled or
  empty-with-reasons.
- `docs/system/registry.md` — slices + folds, the fold-reconciliation line
  (concerns ↔ slices + folds, nothing silently dropped), and a
  **chosen-next slice**.

Why: the slice workflow's Stage 1 consumes the registry entry *as written*
— no artifacts, nothing to consume.

## R2 — The repo builds, runs, and tests run

A skeleton service exists: it compiles, starts, and an empty/trivial test
suite executes green. No features required — the surface is derived later,
slice by slice.

Why: a slice's build stage must land in a place where code and tests
already run; fighting the toolchain mid-slice pollutes the record with
noise that isn't method feedback.

## R3 — The real store is reachable from tests

The store the enforcement will live in (the one whose constraints are
candidate walls) is provisioned and reachable from the test suite — not
mocked. Mocks are acceptable elsewhere; never for the store.

Why: the strongest walls (unique constraints, atomic conditional updates)
are *store behavior*. Evidence against a mock proves the mock, not the
invariant.

## R4 — The harness can create each adversity class the registry names

Check the registry's slices; for **each adversity class named there**, the
test harness must be able to generate it:

- **contention** → fire N genuinely concurrent requests at one endpoint
  (parallel, not sequential-fast).
- **duplicate delivery** → replay a captured request; inject a duplicate
  after a swallowed response; send a same-key-different-body request.
- **partial failure / crashes** → kill the process (or the transaction)
  mid-write, then restart and re-drive.

Only the classes the registry names are required — a checklist item for an
adversity no slice faces is ceremony.

Why: a slice closes only on a test that *creates* its adversity. If the
harness can't create it, the completion gate is physically unrunnable and
"done" becomes a feeling again.

## R5 — The harness can fail

Self-test: break an invariant deliberately (drop the constraint, comment
the guard — on a branch) and confirm the adversity test goes red, then
restore. A harness that has never failed proves nothing when it passes.

## R6 — The run's record surfaces exist

The registry is writable, and the project's own record scheme (decided at
bootstrap, not by this method) has a place for deviations and sign-offs —
slice closes and re-decisions land there.

## Sign-off

Readiness is a human verdict, not a script's: walk R1–R6, check each, and
record the sign-off (one line in the registry or the project's records:
date + "ready for slice N"). The slice workflow's Stage 0 asks for this before
Stage 1 opens.
