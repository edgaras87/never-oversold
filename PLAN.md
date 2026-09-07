# Plan: cbc-pure-run-3

<!-- The stub ships the pure shape: the steps below are placeholders
     showing the form — a goal, a gate of verifiable facts, the records
     expected. At birth the install manual replaces the region between
     the STEPS markers with a playbook's full sequence — the handbook's
     or a concept's; the playbook itself stays where it came from. Born
     without one, fill the placeholders in place. Either way the two
     STEPS markers stay: they are what makes the swap re-runnable. -->

<!-- Steps from: cbc-run-pure v4 at 57cf22f,
     copied at birth. Filled in place at birth; this comment stays —
     the retrospective folds lessons back to what it names. -->

## Legend

`[ ]` planned  ·  `[~]` in progress  ·  `[x]` done (+date)  ·  `[!]` blocked (+what unblocks)  ·  `[-]` skipped (+why)

**Gate** = exit criteria: verifiable facts, not intentions. A step is done only when every gate item is true.
Detail only the next 1–2 steps finely; keep later steps coarse (rolling wave).

Each step carries Goal · Gate · Notes. A gate is derived when the
step opens — verifiable facts from the goal, the named skill, and
the run's records — and written in before the step's work starts.

---

<!-- STEPS-BEGIN — steps between the markers; the markers stay -->

## Step 0: Bootstrap                                [x] 2026-09-07

Goal: the container exists — repo, records, arrangement — before content.
Gate:
- [x] Every delivered file is tracked on main: `git status
      --porcelain` is empty, and the pinned copies match the receipt
      (`git diff birth-seed -- docs/concept .claude/skills` is empty).
- [x] The nine skills under `.claude/skills/` load; each is
      registered in `.claude/decisions.md` at its pin (kit @ c670fe5,
      bundle @ 57cf22f); no placeholder remains in the log.
- [x] Every record exists and is true or honestly stubbed: PLAN
      titled and this gate written; the devlog carries the birth
      session with a Resume line; TODO triaged; every link in
      README's records table resolves; CHANGELOG and ARCHITECTURE
      stubs name their own fill moment.
- [x] CLAUDE.md passes its three tests, line by line.
- [x] Nothing names the problem: no `docs/system/`, no source, no
      build file; the devlog's briefing line says so explicitly.
- [x] Every commit on main follows commit-messages: subject ≤ 50,
      imperative; no commit straddles agent and project paths.
Notes: the title is a working name (Step 2 decides the public
identity). The birth ran as one change set — see the
`docs(agent): … change-plan for the birth` commits.

## Step 1: Framing  (cbc-framing)                   [ ]

<!-- CbC: this step opens on the briefing — its starting input,
     the first prompt of project work. The README purpose
     paragraph and the devlog's briefing line land here; names
     given before it are working names. -->

Goal: know what we're building and why, before code.
Gate: derived when this step opens — verifiable facts, from the
goal, the named skill, and the briefing; written into this step
before its work starts.
Notes:

## Step 2: Define (naming)                          [ ]

Goal: the project's public identity decided, not defaulted.
Gate: derived when this step opens — verifiable facts, from the
goal and the run's own records; written into this step before
its work starts.
Notes:

## Step 3: Ground / infrastructure  (infra-establish)    [ ]

Goal: services stood up, constrained to need, verified both ways.
Gate: derived when this step opens — verifiable facts, from the
goal, the named skill, and the registry; written into this step
before its work starts.
Notes:

## Step 4: Skeleton & bootstrap  (cbc-bootstrap)    [ ]

Goal: an empty but buildable, testable, runnable system wired to the
real ground, with the evidence harness proven on one adversity.
Gate: derived when this step opens — verifiable facts, from the
goal, the named skill, and the registry; written into this step
before its work starts.
Notes:

## Steps 5..N-1: Invariant slices  (cbc-slice, one step per stage)

Goal: each registry slice closed by evidence that creates its
adversity; ordering re-decided at each close, never assumed from
the original expectation.
Gate: derived when each stage opens — verifiable facts, from the
goal, the named skill, and the registry; written into the stage
before its work starts.
Notes:

## Step N: Release                                  [ ]

<!-- Kit step — vendored from starter/playbooks/default.md
     @ c670fe5; reshaped at birth to the run's step form. -->

Goal: the system handed to its audience — the promise shipped,
observable, and reversible wherever it deploys.
Gate: derived when this step opens — verifiable facts, from the
goal, the run's records, and the exclusions framing recorded.
Known already: a CHANGELOG entry for the release; README true for
a stranger, its commands verified on a clean machine; known
issues filed in TODO.md. Decided at framing, checked here:
monitoring and alerts in place; deploy and rollback documented and
tried once — each unless this run's own recorded exclusion.
Notes:

<!-- STEPS-END -->

---

## Discovered along the way

<!-- Non-blocking findings. Triage each into TODO.md: assign to a step,
     park in Later, or drop. Then delete the line here. -->

## Decision index

- ADR-0001: Record architecture decisions (birth)

---

## Retrospective  (fill at project end)

Ran: <start> → <end>

1. Estimate vs reality — which steps took much longer/shorter, why?
2. Wrong order — what needed to happen earlier?
3. Dead ends — approaches tried and abandoned (→ playbook warnings).
4. Missing steps — work that had no home in the plan.
5. Useless gates — ceremony that caught nothing.
6. The entry file — read CLAUDE.md top to bottom; every line still
   passes its three tests, or leaves (agent-arrangement §2).

Then fold lessons into the playbook the steps came from — the
"Steps from" line at the top names it — in the repo that owns it,
and bump its version there.
