<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     Infrastructure-establishment/.claude/skills/references/establishment-walk.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: moved — from .claude/skills/references/, beside rather
     than inside the skill directory (same normalization as the
     SKILL.md). Content verbatim below this header.
     Changed 2026-09-03 (ADR-0013): step 7 gains the README
     Prerequisites projection — the section arrives when the ground
     stands, skeleton in templates/readme-prerequisites.md. -->

# The establishment walk — step by step

The method, end to end. Generic throughout; the walking project's
concrete choices land in its own record and manuals, never here. This
capture has been lived by two projects; deviations a different ground
forces are legal when logged.

## Inputs, before the first move

The framing's outputs, standing: the definition's environment facts
(the ground must honor them) and the registry's slices (whose
adversities are the deciding constraints — **evidence needs drive
infrastructure, not habit**). The intent is consulted at scope and
tradeoff questions. If these aren't settled, the step isn't ready —
stop (Stage 0 in SKILL.md).

## 1 · Decide the Execution Environment — demands first, against the lived default

Derive what the slices' adversities demand the evidence be able to
*do*: kill processes mid-write, race plural instances, break a commit
boundary, survive restarts — each project's own list. This derivation
is the step's living half — it feeds verification design and the
manuals.

Check the **lived default — podman local containers, compose-driven —**
against those demands and the definition's runtime facts. Weigh
alternatives **only when a demand or an environment fact defeats the
default**, and name the defeater. Need governs: the default is
checked, never assumed. Log the decision with its why.

Then stand the environment up, and **write the operator manual's
environment section from the lived setup, contemporaneously** — never
reconstructed later. Standing the default up captures, at minimum,
verified by execution:

- the engine version (`podman version`);
- the compose provider actually answering the front door
  (`podman compose version` — the canonical command is
  `podman compose`, whatever provider stands behind it);
- host OS and arch; cgroups version;
- rootless or not, with its operating implications (ports, volumes,
  mount flags — `:Z` on SELinux hosts).

This checklist keeps every project's environment section structurally
comparable while its facts stay each machine's own.

**No ground file is born at this step.** `compose.yaml` and its kin
are steps 2–5's outcomes — their content *is* the service evaluation's
and the constraints' result; a file born here would exist before the
decisions that define it. The environment's proof is engine-level
(image pulled, container run, end to end); the file-level path is
covered at step 5's opening sanity check (`podman compose config`).
An ephemeral throwaway compose smoke test outside the repo is allowed
when the operator wants the earlier signal; it is never owed and never
committed.

## 2 · Evaluate Infrastructure Services — constrained by need

Slice by slice: what capability does each invariant's evidence
actually require? The service set is what survives that question — and
**the not-provisioned list is stated with each exclusion's why** (a
cache refused, a queue refused, and for what reason). One log entry.
Anything joining later re-enters this evaluation as a logged decision
(the infra-serve skill).

## 3 · Name each service's constraints — against standing knowledge first

Constraints are requirements and limitations imposed on a service —
governance of the ground, stated as principles (e.g. *the running
application must not control database structure*). Deliberately **not**
registry invariants: they are no properties of persisted state under
adversity. **Each constraint names its enforcement mechanism.**

Where governing knowledge for a decided service already stands (for
PostgreSQL: `postgres-role-split.md` in this skill's references), its
constraint set is the **rebuttable default**: check it against this
project's facts; deviate only when a project fact defeats it, naming
the defeater. The project's log entry stays — its own constraints are
its own truth — but references the standing knowledge as the
realization, never re-deriving it. A service without standing
knowledge gets its constraints derived fresh.

## 4 · Apply — or record — the governing knowledge

Where a constraint's realization is reusable across projects, it is
knowledge, not project truth: keep it generic, keep the project's
concrete wiring in its manuals. When this walk derives a fresh
reusable realization (a new service family), write it up as a new
reference beside `postgres-role-split.md` so no later project
re-derives it.

## 5 · Stand the services up, and verify both ways

Ground files land as project truth: declarations (`compose.yaml`),
bootstrap scripts, tool configs — **secrets split out** per the
project's credential handling (`.env` git-ignored + committed
`.env.example`; decided identities stay literal, secrets and machine
variance are variables). The services come up on the environment.

Then **two complementary verifications, always both**:

- **Catalog check** — the ground's real state queried against the
  governing knowledge's claims, expected results stated as comments
  beside each query so the reader needs no other doc open.
- **Behavioral check** — the constraint *attempted* and watched being
  **refused**, live. The governing rule demonstrated, not assumed.

The operator manual grows its service sections from this lived work.

For PostgreSQL, the whole of steps 3–6 has a lived end-to-end
sequence: `postgres-setup-walkthrough.md`. Consult it when PostgreSQL
recurs; never force its shape on a different service.

## 6 · Write the infrastructure contract

The builder-facing manual — a **living document, one section per
service**, grown at every later re-entry: what services exist, how to
reach them (inside vs. outside the environment's network), **which
identity to connect as and which never to use**, what the constraints
allow and refuse (the refusals written as contract terms), how schema
changes are made. It masters the ground's vocabulary. The exit needs
both manuals standing — this one and the operator manual.

## 7 · Run the exit test

Against the lived walk: does the ground run? Is every service tied to
a stated need, every exclusion to a stated why? Do both verifications
pass, recorded from actual output? Do both manuals stand, written
from lived work? Could a reader stand the same ground up on their own
machine from the operator manual alone? Any no → the walk isn't done.

When the exit test passes, the ground's truth projects outward: the
run's README gains its **Prerequisites** section — the environment
lines a stranger's machine needs before this ground can stand,
derived from the operator manual, carrying only what a stranger
needs in hand and linking the manual for the rest. Skeleton:
`templates/readme-prerequisites.md` in this skill — merge, fill,
and the section is the run's own (ADR-0008). The stack's own
prerequisite line is **not** written here: its fact is born at
bootstrap, whose skill carries it (ADR-0013). The kit's README stub
ships without the section — container stays, direction goes — and
the section arrives now because this gate made it true.

If the project keeps public docs: derive the setup guide from the
operator manual now — a projection, never a second master.

## Vocabulary (mastered by the project's infrastructure contract)

- **Execution Environment** — the local runtime host for
  infrastructure.
- **Infrastructure Service** — a capability provided to the system.
- **Infrastructure Service Constraint** — a requirement or limitation
  imposed on an infrastructure service.
