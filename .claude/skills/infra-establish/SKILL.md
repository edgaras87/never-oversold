---
name: infra-establish
description: Establish a framed backend project's infrastructure ground - decide the execution environment against a lived default (podman local containers, compose-driven), evaluate infrastructure services strictly from the slice registry's adversity needs (the not-provisioned list stated with each exclusion's why), name each service's constraints with database-level enforcement, stand services up, verify both ways (catalog check plus behavioral refusal check), and write the two manuals (infrastructure contract for the builder, operator manual for the human). Use whenever a framed project needs its infrastructure stood up - containers, PostgreSQL, compose files, Flyway/migrations tooling, "set up the database", "stand up the ground", "infrastructure establishment" - even if the user just says "the framing is done, let's get it running". Requires completed framing artifacts (intent, definition, slice registry) - Stage 0 checks and refuses to proceed without them. Do NOT use for framing (cbc-framing), for implementing slices (cbc-slice), or for adding a service to an already-established ground (infra-serve).
---

<!-- Checked against concept v1 of correctness-by-construction
     (CBC ADR-0003, CBC ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     Infrastructure-establishment/.claude/skills/SKILL.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: moved — the archive keeps this file at
     .claude/skills/SKILL.md, disagreeing with its own STATUS.md
     layout diagram and unregisterable as a named skill; normalized
     to the diagram's layout. One addition: the Records-and-outputs
     defaults section at the end, absorbed from the
     agents/groundskeeper.md file left behind (CBC ADR-0006). Content
     otherwise verbatim below this header.
     Re-derived 2026-08-29: the framing exports' paths — they live
     under docs/system/ as intent.md, definition.md, registry.md
     (cbc-framing's layout re-derivation); Stage 0 updated to
     match.
     Changed 2026-09-03 (CBC ADR-0013): records-and-outputs gains the
     README Prerequisites projection — fired at the exit, skeleton
     in templates/readme-prerequisites.md; the walk's step 7 carries
     the direction.
     Harvested 2026-09-11 from never-oversold (run 3 of the pure
     seed) Step 3, read read-only (CBC ADR-0007): the records
     section states the record-keeping-repo shape — no
     establishment log; decisions as ADRs, the walk in the devlog,
     expected results in the verify suite and the operator manual,
     the mapping note in the environment ADR — and the layout both
     lived runs used: compose and env files at the root, the
     runnable ground under infrastructure/, the manuals under
     docs/. The log stays for a repo without records.
     Harvested 2026-09-11, same run (CBC ADR-0007): Stage 0's check
     2 names the return trip — a definition without runtime-ground
     facts gains them by a dated revision entry in L1, one commit —
     instead of "stop"; lived by both runs that hit it. -->

# Infra establish — from no infrastructure to a governed, verified ground

Take one framed project to **ground**: the environment chosen and
running, the services the problem's reasoning chain requires stood up,
constrained to need, verified both ways, with the manuals that make the
ground usable and reproducible. The exit is the guide's own: **the
ground runs, constrained to need, both manuals stand.**

The full walk is `references/establishment-walk.md` — read it before
Stage 1. When PostgreSQL is the decided datastore, two more references
apply at their steps: `references/postgres-role-split.md` (the authority
model — the constraint's realization) and
`references/postgres-setup-walkthrough.md` (the lived end-to-end
sequence, confirmed by two projects).

## Stage 0 — readiness gate (before anything else)

The service decision lives in *this* work, but the **need** is handed
in. Verify against the actual repo:

1. **The framing artifacts exist and are settled**: the project intent,
   the system definition (its environment facts are binding), and the
   slice registry (its slices' adversity requirements are the deciding
   constraints). With cbc-framing these live under `docs/system/`:
   `intent.md`, `definition.md`, `registry.md`.
2. **The environment facts are readable** — the definition states what
   runtime ground the system assumes: the machine, a stranger's clean
   machine, whether more than one instance runs on it, the store as a
   service outliving the instances, the clock. If the definition has
   no runtime-ground facts, this is not "stop": it is the downstream
   trigger the definition's own revision rule names. The facts are
   added by a dated revision entry in L1 — what changed, why,
   triggered by this check — one commit, no possession, refusal or
   verdict touched; then the check passes. Lived twice, the same way
   both times (checkout-system, never-oversold). Framing work is not
   done here; a fact the framing left unstated is written where it
   belongs.
3. **The repo exists and is a git repo** with its plumbing dotfiles
   present or creatable (`.gitignore` at minimum — secrets handling
   depends on it).

**Check 1 or 3 fails → stop.** Report exactly what is missing.
Framing work is never done here — that is the framing job's
territory. An establishment started on an unframed project
provisions from habit,
and habit is exactly what the method exists to refuse.

## The walk and its gates

```
0: ready? → 1: environment → 2: services   → 3: constraints → 4: knowledge
                (decided,       (from need,     (named, with     (applied or
                 stood up)       exclusions)     enforcement)     recorded)
          → 5: stand up + verify BOTH ways → 6: contract → 7: exit test
```

Full detail per step: `references/establishment-walk.md`. The
non-negotiables:

- **Step 1 gate:** the environment decision is logged with its why
  *before* any ground file exists. **No compose file is born at step
  1** — its content is steps 2–5's outcome. The operator manual's
  environment section is written from the lived stand-up, at the
  moment it happens.
- **Step 2 gate:** one logged evaluation, slice by slice: what
  capability does each invariant's evidence actually require? The
  service set is what survives that question — **and the
  not-provisioned list is stated with each exclusion's why.** A cache
  refused, a queue refused, and for what reason. No exclusion list, no
  step 2.
- **Step 3 gate:** every constraint names its **enforcement
  mechanism** — the database's own grant system, a config, a
  structural wall — never convention, code review, or trust. A
  constraint without enforcement is a wish.
- **Step 5 gate:** verification runs **both ways, always**: the
  **catalog check** (real state queried against the model's claims,
  expected results stated beside each query) *and* the **behavioral
  check** (the constraint attempted and watched being refused, live).
  A ground verified one way is not verified.
- **Step 6 gate:** both manuals stand — the **infrastructure
  contract** (builder-facing: identities, reachability, refusals, how
  schema changes are made) and the **operator manual** (stand-up-and-
  use, written from lived work). No manuals, no exit.

## The lived-result discipline (every executing step)

State the command and its expected result → run → read the actual
output → the outcome is what actually happened. Record it in the
ground's record (Records and outputs, below). Destructive acts
(volume drops, resets) need the user's explicit yes; host-level
installs are the user's own acts.

## Deviations — legal, never silent

A different ground (no containers, several services, another
constraint family) may force departures from the walk or the
references. Depart when the ground demands it; log where, what was
done instead, and why. The references are lived captures, not law —
but a silent deviation poisons the record.

## Records and outputs (the repo's own conventions win)

Where the ground's record lives depends on what the repo keeps —
lived by two runs, and the shape below is theirs:

- **A repo with records** (a plan, ADRs, a devlog — every kit-born
  project): **no establishment log.** Decisions are ADRs — the
  environment; the service set with its not-provisioned list; the
  constraints, each with its enforcement. The walk as lived is the
  devlog's entry for the step, growing as the walk runs: for every
  executing step, the command, what was expected, what actually
  happened. Expected results sit in the verify suite and the
  operator manual. The environment ADR carries the mapping from
  this skill's default records to the repo's own — the sentence
  the log's first entry would have held. Run 3 (never-oversold)
  opened the log at the decision and withdrew it one commit later
  at the reviewer's question — what does it hold that the records
  do not? — nothing.
- **A repo without records:**
  `docs/infrastructure/establishment-log.md`, beside the manuals —
  the decision record and the walk's lived outputs, its first entry
  mapping this skill's defaults to whatever the repo does keep.
  Prose about the ground lives with the manuals; `infrastructure/`
  holds only what runs.

The layout, both cases:

- `compose.yaml` and `.env.example` at the root, `.env` ignored —
  the stranger's first command finds them there, and at bootstrap
  the file becomes the whole system's declaration.
- `infrastructure/` — the rest of the runnable ground (bootstrap
  SQL, verify suite, migration tool config), landed as the walk
  produces them.
- The two manuals under the repo's docs directory
  (`docs/infrastructure/`), beside what else is written about the
  system:
  - `infrastructure-contract.md` — the **living** builder-facing
    manual: one section per service, which identity to connect as
    and which never to use, reachability inside vs outside the
    environment's network, what the constraints refuse, how schema
    changes are made. Grown at every re-entry, never rewritten from
    scratch.
  - `operator-manual.md` — the operator's full stand-up-and-use
    truth, written **from the lived setup, contemporaneously** —
    never reconstructed later.
- README `Prerequisites` section — projected at the exit, when the
  ground stands: the environment lines only, merged and filled from
  `templates/readme-prerequisites.md` (CBC ADR-0013). The stack's
  line arrives at bootstrap, not here. Direction in the walk's step
  7.
- Optional, only if the project keeps public docs: a setup guide
  derived from the operator manual — a projection, never a second
  master; re-derived when the manual changes.

