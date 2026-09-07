---
name: infra-establish
description: Establish a framed backend project's infrastructure ground - decide the execution environment against a lived default (podman local containers, compose-driven), evaluate infrastructure services strictly from the slice registry's adversity needs (the not-provisioned list stated with each exclusion's why), name each service's constraints with database-level enforcement, stand services up, verify both ways (catalog check plus behavioral refusal check), and write the two manuals (infrastructure contract for the builder, operator manual for the human). Use whenever a framed project needs its infrastructure stood up - containers, PostgreSQL, compose files, Flyway/migrations tooling, "set up the database", "stand up the ground", "infrastructure establishment" - even if the user just says "the framing is done, let's get it running". Requires completed framing artifacts (intent, definition, slice registry) - Stage 0 checks and refuses to proceed without them. Do NOT use for framing (cbc-framing), for implementing slices (cbc-slice), or for adding a service to an already-established ground (infra-serve).
---

<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     Infrastructure-establishment/.claude/skills/SKILL.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: moved — the archive keeps this file at
     .claude/skills/SKILL.md, disagreeing with its own STATUS.md
     layout diagram and unregisterable as a named skill; normalized
     to the diagram's layout. One addition: the Records-and-outputs
     defaults section at the end, absorbed from the
     agents/groundskeeper.md file left behind (ADR-0006). Content
     otherwise verbatim below this header.
     Re-derived 2026-08-29: the framing exports' paths — they live
     under docs/system/ as intent.md, definition.md, registry.md
     (cbc-framing's layout re-derivation); Stage 0 updated to
     match.
     Changed 2026-09-03 (ADR-0013): records-and-outputs gains the
     README Prerequisites projection — fired at the exit, skeleton
     in templates/readme-prerequisites.md; the walk's step 7 carries
     the direction. -->

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
   runtime ground the system assumes (local, single machine, OS-level
   facts). If the definition has no environment facts, the framing
   isn't finished.
3. **The repo exists and is a git repo** with its plumbing dotfiles
   present or creatable (`.gitignore` at minimum — secrets handling
   depends on it).

**Any check fails → stop.** Report exactly what is missing. Framing
work is never done here — that is the framing job's territory. An
establishment started on an unframed project provisions from habit,
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
establishment log. Destructive acts (volume drops, resets) need the
user's explicit yes; host-level installs are the user's own acts.

## Deviations — legal, never silent

A different ground (no containers, several services, another
constraint family) may force departures from the walk or the
references. Depart when the ground demands it; log where, what was
done instead, and why. The references are lived captures, not law —
but a silent deviation poisons the record.

## Records and outputs (defaults — the repo's own conventions win)

- `infrastructure/establishment-log.md` — the decision record.
- `infrastructure/` — ground files (compose, bootstrap SQL, verify
  suite, migration tool config), landed as the walk produces them.
- `infrastructure-contract.md` — the **living** builder-facing manual:
  one section per service, which identity to connect as and which never
  to use, reachability inside vs outside the environment's network, what
  the constraints refuse, how schema changes are made. Grown at every
  re-entry, never rewritten from scratch.
- `operator-manual.md` — the operator's full stand-up-and-use truth,
  written **from the lived setup, contemporaneously** — never
  reconstructed later.
- README `Prerequisites` section — projected at the exit, when the
  ground stands: the environment lines only, merged and filled from
  `templates/readme-prerequisites.md` (ADR-0013). The stack's line
  arrives at bootstrap, not here. Direction in the walk's step 7.
- Optional, only if the project keeps public docs: a setup guide
  derived from the operator manual — a projection, never a second
  master; re-derived when the manual changes.

If the repo already has record/doc conventions, follow them and note the
mapping in the log's first entry.

