---
name: cbc-bootstrap
description: Bootstrap a framed project's system on an established ground - decide the stack and the bootstrap set at capability-and-constraint grain, compose a requirements document, implement an empty-but-running skeleton wired to the real services, and prove an evidence harness creates one named adversity end to end. Use whenever the user asks to bootstrap the system, stand up the skeleton, "bring the system to life", or to continue a project that has a completed framing and a running ground but no application code yet - even if they just say "let's start building". Requires a completed framing AND an established, verified ground - Stage 0 checks readiness and refuses to proceed if either is missing. Do NOT use for framing (cbc-framing), for implementing invariant slices (cbc-slice), or for setting up databases/infrastructure (that is the ground's own work, done before this).
---

<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     system-bootstrap/.claude/skills/cbc-bootstrap/SKILL.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: none — verbatim below this header.
     Changed 2026-09-03 (ADR-0013): Stage 5 gains the README Run and
     Test projection — the sections (and the stack's Prerequisites
     line) arrive when the harness is real, skeleton in
     templates/readme-run-test.md. -->

# cbc-bootstrap — bring a framed system to life

One run, one exit: ***system bootstrapped*** — stack settled, an
empty-but-running skeleton wired to the real, already-established ground,
and an evidence harness proven able to **create** one named adversity end
to end. After this, cbc-slice has somewhere to land.

## The seam you must keep — WHAT before HOW

This skill was distilled from a two-seat discipline: one seat decides
**what** is being bootstrapped and under which constraints; another decides
**how** it is realized in code. Running as one agent, the seam survives as
a **hard stage boundary**:

- Stages 1–2 are **strategy-free**: capabilities, constraints, identity,
  evidence owed. **No dependency, no class, no file layout is named there.**
- Stage 4 is where implementation judgment lives — dependencies and their
  timing, code shape, commit split — under the stack conventions in
  `references/`.
- Never let a mechanism chosen early masquerade as a requirement. If while
  implementing you discover a missing *what* (a capability or constraint the
  work turns out to need), **stop and return to Stage 1 as a logged
  re-decision** — never absorb it silently.

Every decision is written down with its why **before** the code that applies
it exists. The requirements document (Stage 2) is the contract; on any
conflict between it and the walkthrough reference, **the requirements win**
and the conflict is reported.

## Stage 0 — readiness check (refuse if not ready)

Verify, in the repo as it stands:

1. **Framing outputs committed**: the project intent, the system definition,
   the slice registry. Missing → stop; that is cbc-framing's work.
2. **Established ground, running and verified**: an infrastructure contract
   (identity rule, reach facts, refusals, the one DDL path) and an operator
   manual exist; the ground's stand-up verification is recorded. Missing →
   stop; that is infrastructure work, done before this skill.
3. **No skeleton yet**: if application code already exists, this skill does
   not apply — route to cbc-slice.

Then **verify the ground live** per the operator manual before any wiring —
records prove it *was* verified; the wiring needs it up *now*.

Record the receipt: what stood, what was checked, any observations. Not a
blocker-hunt — observations ride the record; only a missing member stops.

## Stage 1 — decide (strategy-free, human confirms)

Reason out and present for confirmation, each with its why:

- **The stack evaluation.** Deciding inputs are **fluency and audience** —
  which stack the builder/team is proven on, and what the intended reader of
  this codebase reads fluently. The correctness work should be the visible
  substance, not stack novelty. Record the decision **in the project's log
  before any code exists**, including: the migration tool stays outside the
  app; the app knows one identity (the runtime role); the migrations home
  confirmed.
- **The harness's one proven adversity.** Read it out of the framing's own
  words — the promise's named adversity class and the first slice's shape
  (e.g. contention, duplicate delivery). The harness proves the *machinery*
  on that one class alone; every other class is staged at its own slice.
- **The initialization identity**: group (the owner's namespace — ask, never
  invent), artifact (the project's name), base package (hyphens dropped),
  packaging, language version.
- **The capability set — small, typically four**: an HTTP surface (adversity
  arrives through the real door); operational health (every evidence run
  needs a cheap world-is-up check); connectivity to the ground **as the
  runtime identity** (the promise is a property of persisted state); the
  evidence harness proving the chosen adversity end to end, with migrations
  run harness-side from the one home, integration tests under the one
  standard test command, and the migration path proven by a zero-applied
  assertion.
- **The constraints**, cited against the ground's contract: one identity —
  no migrator credentials in any profile; nothing in-app controls schema —
  migration tooling test-scope only; one migrations home, honestly empty;
  plus the standing one — **nothing enters ahead of need** (no ORM/object
  mapping when nothing persists domain state yet; plain SQL access).
- **The exclusions, each with its why**: no business behavior (any probe is
  scaffolding, marked to die at the first slice); no persistence schema (no
  table before its invariant); no other adversity classes; no auth unless a
  slice demands identity at the door; no delivery machinery beyond locally
  runnable and testable.

**Gate: the human confirms the decisions before Stage 2.**

## Stage 2 — compose the requirements document

Write `internal/construction/bootstrap-requirements.md` (or the project's
own equivalent path) in the project's language:

- §1 the initialization identity
- §2 the capabilities, each with its why
- §3 the constraints, each with its why
- §4 the ground's facts **by pointer** — cite the operator manual and
  contract, never restate them; carry only what the code needs in hand: the
  identity the application connects as, and the endpoint facts.
  **Lived rule: state environment facts notation-neutrally** — "env var X,
  default Y" — never in any tool's placeholder syntax. (A compose-style
  `${VAR:-default}` carried into an application config silently resolves to
  a wrong literal.)
- §5 the evidence owed at the end: the step plan as executed with
  deviations, the run proof (app up, health UP with the database component
  UP, the one standard test command green), and the commits standing.
- §6 the exclusions with whys.

**It names no dependency, no class, no file layout** — that gap is
deliberate and Stage 4 closes it. Commit it before implementing.

## Stage 3 — step plan (human confirms)

Produce an ordered step plan: each step one logical change, with its own
verification and its own commit, naming what it gives. The lived shape is
five steps (see the stack walkthrough in `references/`):

1. the stack decision recorded
2. the skeleton standing in the live repo
3. the runtime datasource wired to the real ground
4. the evidence harness standing, harness-side migration proven
5. the harness proven able to create the adversity

The **ordering is load-bearing; the commit split is judgment**. Nothing
outside the requirements enters the plan. **Gate: the human confirms.**

## Stage 4 — execute, step by step

Per step: make the change, **verify it**, commit. A step whose verification
fails is not committed; it is reported. Implementation decisions are yours
here — under the stack conventions:

- Read `references/spring-boot-walkthrough.md` for the outcomes and the
  lived traps (Boot 4 renames, test-scope companions, container-runtime
  setup, the zero-applied migration proof, the concurrency probe shape).
- Write the build file per `references/spring-pom-convention.md` — every
  dependency enters at the step that earns it, with its earning reason as a
  comment, grouped by capability.
- The repo already exists: **extract into it, merge plumbing files, never
  overwrite** (.gitignore grown, .gitattributes merged, README kept).
- Widen the build's test includes so integration tests run under the one
  standard test command — no second command, no test that quietly never runs.

**When something does not fit — report, never absorb:**
- silent on a *what* → back to Stage 1, a logged re-decision;
- a missing service or constraint in the wiring → the ground's own work,
  reported, not patched here;
- something larger than the bootstrap → reported, not solved here;
- the requirements wrong against the repo's reality → reported with what
  was found.

## Stage 5 — certify and close

Certify **against the delivered state, never a report alone**. Check, member
by member:

- §5 evidence: the step plan as executed (deviations honest), the run proof
  (Java version announced; health UP with the db component UP against the
  running ground; the standard test command green end to end — verified
  structurally: the integration tests actually run under it), the commits
  standing.
- §1 identity exact; §2 all capabilities present **and bounded** (probe
  carries no business meaning and is documented to die at the first slice);
  §3 all constraints visible in the delivered files (runtime identity alone
  in config, password from environment, deliberate absences commented,
  nothing structure-controlling on the runtime path, migrations home
  honestly empty, nothing ahead of need); §6 exclusions respected.

Then write the exit records in the project's own docs: the milestone
certified, run and test commands, operator manual linked, an honest "no
business behavior yet", the probe's death scheduled at the first slice.

The certified facts also project into the README (ADR-0013): the
**Run** and **Test** sections, plus the stack's line joining the
Prerequisites section the establishment opened — merged and filled
from `templates/readme-run-test.md`. Run ends in the lived proof of
life; Test is the one standard test command, verified structurally at
certification. The kit's README stub ships without these sections —
container stays, direction goes — and they arrive now because the
harness is real.

**Exit test:** the skeleton runs on the real ground as the runtime identity
alone, and the harness demonstrably creates the chosen adversity through the
real door. Then hand over: growth belongs to cbc-slice.
