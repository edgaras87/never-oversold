# Plan: never-oversold

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

## Standing rules

<!-- Arrived 2026-09-07, before the briefing; on trial from Step 1.
     Its why and rejected options: .claude/decisions.md. -->

- **One branch per step.** A step starts on its own branch cut
  from main, and ends after its gate closes by fast-forward merge
  into main, on the reviewer's word. A restarted step keeps its
  old branch renamed and cuts a new one.

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

## Step 1: Framing  (cbc-framing)                   [x] 2026-09-10

<!-- CbC: this step opens on the briefing — its starting input,
     the first prompt of project work. The README purpose
     paragraph and the devlog's briefing line land here; names
     given before it are working names. -->

Goal: know what we're building and why, before code.
Gate:
- [x] The three exports stand under `docs/system/` — `intent.md`,
      `definition.md`, `registry.md` — composed from
      `docs/system/framing/derivation.md`; no export references
      the derivation doc (residue filter).
- [x] `intent.md` carries exactly one promise, one sentence, and
      it clears the briefing's six bars: one timeless state claim;
      its adversity class (contention) named in the sentence; a
      concrete negation naive code actually produces; the negation
      stageable, its witness read from persisted state from
      outside; proving it matters to the audience; one owner, a
      full lifecycle inside one system's walls. The audience is
      the portfolio reader; what done demonstrably means is spelled
      out; every rejected candidate is banked.
- [x] `definition.md`: every L2 possession earned by the hostage
      test and every refusal written with its mirror; L1 closed by
      saturation, its probe log recorded, the last two probes
      empty; every L4 kill invariant-shaped, adversity named, no
      mechanism; L3 and L5 filled, or empty with reasons traced to
      prior decisions.
- [x] `registry.md` on the skill's template: every slice states
      its invariant, the adversity its evidence must create, its
      owner, the kills it covers, what it presumes; the
      reconciliation line accounts for every kill; exactly one
      slice is chosen-next.
- [x] Every step verdict in `derivation.md` is the reviewer's,
      written inline where it fell; `.claude/decisions.md` holds
      no delegation entry.
- [x] The framing's adoption is one ADR in `docs/adr/`, listed in
      this plan's decision index.
- [x] Release's framing-time decisions are written in the exports:
      monitoring and alerts, deploy and rollback — each in scope,
      or excluded with its why.
- [x] README re-derived from the exports per the skill's projection
      table — why, what, one line per invariant, a method pointer,
      a status line — nothing on it without a master in
      `docs/system/`; the `docs/system/` row added to the records
      tables in `.claude/CLAUDE.md` and README.
- [x] CHANGELOG's versioning placeholder replaced by what a version
      is here.
- [x] The devlog's briefing line says the briefing arrived on
      2026-09-09 and what it brought.
- [x] The exports landed in derivation order — intent; definition
      L2 → L1 → L4 → L3 → L5; registry; ADR; README last — or the
      devlog logs why not; every commit follows commit-messages,
      none straddles agent and project paths.
- [x] The step ran on `step-1-framing`, cut from main, and reached
      main by fast-forward on the reviewer's word.
Notes: opened 2026-09-09 on the briefing — inventory reservation
under contention, sold to the portfolio reader; spun off from
checkout-system's territory by lineage, not decomposition, and
nothing of that repo is read here. Working name from the briefing:
safe-reservations; Step 2 confirms or overturns it. Closed
2026-09-10: the derivation ran as seven verdict series on
`step-1-framing`, with two return trips and four later fixes to
the exports at the reviewer's reading; ADR-0002 adopts the
framing; the change-plan's close commit carries the divergences.
The branch item is ticked on the reviewer's word to merge, given
at this boundary.

## Step 2: Identity  (name, description, remote)     [x] 2026-09-10

Goal: the project's public identity decided, not defaulted.
Gate:
- [x] The name is decided by an ADR: candidates derived from the
      intent's sentence, each tested against the naming bar — it
      points at the claim, not a feature; it does not overclaim; a
      stranger can guess what the project proves from it; it works
      as a repository name — the rejected ones banked with reasons,
      and the working name safe-reservations confirmed or overturned
      in so many words.
- [x] The description is one line derived from the intent's why,
      recorded in the same ADR, and used verbatim on the remote.
- [x] No "working name" remains outside history: the entry file,
      README, PLAN, the three exports and the devlog carry the name;
      each export's revision log has a dated entry for the change.
- [x] CHANGELOG carries the naming as a Changed line under
      Unreleased.
- [x] The remote exists under the name, with the description, and
      main is on it — created and pushed on the reviewer's word at
      that boundary; `git remote -v` names it.
- [x] README is true for a stranger arriving from the remote: the
      title, the purpose, every link resolving.
- [x] The devlog's entry records the decision, its rejected
      candidates in one line, and the remote's creation.
- [x] The step ran on `step-2-define`, cut from main, and reached
      main by fast-forward on the reviewer's word.
Notes: opened 2026-09-10. The hosting is the reviewer's choice.
Renaming the folder on disk is outside git and the reviewer's
move; the agent's project memory is keyed to the folder path.
Closed 2026-09-10: never-oversold, by ADR-0003; the remote
github.com/edgaras87/never-oversold created by the reviewer with
the ADR's description, main pushed by the reviewer at 0de75df; the
step's own commits reach it with the fast-forward. The branch item
is ticked on the reviewer's word to merge, given at this boundary.

## Step 3: Ground / infrastructure  (infra-establish)    [x] 2026-09-11

Goal: services stood up, constrained to need, verified both ways.
Gate:
- [x] Stage 0 passed on the actual repo and recorded: the definition
      carries the runtime ground the system assumes — one local
      machine, the reviewer's; a stranger's clean machine by the
      README; more than one instance of our process runnable on it —
      added by a dated revision entry, since framing left it
      unstated; the registry's adversities read as the deciding
      constraints.
- [x] The environment decision is logged with its why before any
      ground file exists: the lived default, podman local containers
      compose-driven, checked against the slices' demands (race
      plural instances, kill mid-write, inject an unknowable
      outcome, control a clock) and defeated or kept by name.
- [x] The operator manual's environment section is written from
      the lived stand-up, contemporaneously: engine version, the
      compose provider answering `podman compose`, host OS and arch,
      cgroups version, rootless or not with its implications.
- [x] One logged evaluation, slice by slice, of what capability each
      invariant's evidence requires; the service set is what
      survives; the not-provisioned list states each exclusion's
      why.
- [x] Every constraint on every service names its enforcement
      mechanism — the store's own grants, a config, a structural
      wall — never trust; where standing knowledge exists it is the
      rebuttable default, deviations naming their defeater.
- [x] Ground files land under `infrastructure/` as project truth:
      the compose declaration, bootstrap scripts, the verify suite,
      tool configs; secrets split out — `.env` ignored, `.env.example`
      committed; no ground file older than the decision it realizes.
- [x] The services run, and verification ran both ways, recorded
      from actual output: the catalog check with expected results
      beside each query; the behavioral check with each constraint
      attempted and watched being refused live — among them T2's
      tool, the one way two writers to one item are made to
      disagree, named and its refusal seen.
- [x] Both manuals stand: the infrastructure contract (identities to
      connect as and never to use, reachability inside and outside
      the network, refusals as contract terms, how schema changes
      are made) and the operator manual; a clean re-stand from the
      manual alone — down, volumes dropped on the reviewer's yes,
      up — reproduces the verified state.
- [x] The ground's decisions are ADRs (the environment; the service
      set with its exclusions), and the establishment log's first
      entry maps the skill's default records to this repo's.
- [x] README gains Prerequisites from the skill's template, the
      environment lines only; ARCHITECTURE names the ground's
      services and nothing that does not run; the records tables in
      the entry file and README gain rows for the log and the two
      manuals; every commit follows commit-messages, none straddles
      agent and project paths.
- [x] The devlog's entry records the walk, its deviations, and the
      exit test's answers; TODO's T2 item closes here.
- [x] The step ran on `step-3-ground`, cut from main, and reached
      main by fast-forward on the reviewer's word.
Notes: opened 2026-09-10. Host-level installs are the reviewer's
own acts; destructive acts — volume drops, resets — on the
reviewer's explicit yes; every executing step states the command
and its expected result, runs it, and records what actually
happened. The host at opening: podman 5.8.2, a compose provider
answering, Fedora 42, cgroup v2.
Closed 2026-09-11: PostgreSQL 17 under podman compose, the whole
service set (ADR-0004–0006); both verifications from actual
output, T2's facility refused live; the clean re-stand from the
manual alone reproduced the verified state; the walk lived in the
devlog, no establishment log (deviation, hand-off filed). The
branch item is ticked on the reviewer's word to merge, given at
this boundary.

## Step 4: Skeleton & bootstrap  (cbc-bootstrap)    [~]

<!-- CbC: the seam is WHAT before HOW. Stages 1–2 name capabilities,
     constraints, identity and evidence owed; no dependency, class
     or file layout until Stage 4. This gate keeps the seam: it names
     no stack — the stack is Stage 1's decision, logged before code. -->

Goal: an empty but buildable, testable, runnable system wired to the
real ground, with the evidence harness proven on one adversity.
Gate:
- [ ] Stage 0 passed on the actual repo and recorded: the three
      exports stand; both manuals stand with the identity rule, the
      reach facts, the refusals and the one DDL path; the stand-up
      verification is on record; no application code exists. The
      ground was verified live per the operator manual before any
      wiring — the catalog check run now, its actual output beside
      the expected — and the receipt is in the devlog.
- [ ] The decisions are logged with their why before any code
      exists, and confirmed by the reviewer before the requirements
      are composed: the stack, by fluency and audience — the
      correctness work the visible substance, not stack novelty;
      the migration tool outside the app; the app knowing one
      identity, `runtime`; the migrations home confirmed as
      `infrastructure/flyway/migrations`; the application structure
      checked against the skill's lived default and kept or
      deviated by name. The stack is an ADR, listed in this plan's
      decision index.
- [ ] The initialization identity is decided, not defaulted: the
      group asked of the reviewer, never invented; the artifact
      `never-oversold`; the base package with hyphens dropped; the
      packaging and language version named.
- [ ] The harness's one proven adversity is read from the framing's
      words — contention, in SL-1's shape: concurrent admits on one
      item's last units — and the machinery is proven on that class
      alone. It is created from more than one instance of the ledger
      against the one store (F17; TODO's Step 4 item), the shape
      decided at Stage 1 with its why; a single-process pass does
      not close this item.
- [ ] Kill 10's flag is answered in so many words: the harness gains
      a controlled clock now, or the shape is deferred to SL-1 by
      name; either way logged, not left to be met by surprise.
- [ ] The capability set stands, small and each with its why: an
      HTTP surface, the adversity arriving through the real door;
      operational health, a cheap world-is-up check naming the store
      as a component; connectivity to the ground as `runtime`; the
      evidence harness — migrations run harness-side from the one
      home, integration tests under the one standard test command,
      the migration path proven by a zero-applied assertion.
- [ ] The constraints are cited against the infrastructure contract,
      and the exclusions each carry a why: no business behavior (any
      probe is scaffolding, marked to die at SL-1); no persistence
      schema — no table before its invariant, the migrations home
      honestly empty; no other adversity class; no auth unless a
      slice demands identity at the door; no delivery beyond locally
      runnable and testable; nothing ahead of need.
- [ ] The requirements document stands at a path decided for this
      repo and logged, in the project's language: §1 identity, §2
      capabilities, §3 constraints, §4 the ground's facts by pointer
      to the two manuals — only the identity and the endpoint facts
      in hand, every environment fact stated notation-neutrally
      ("env var X, default Y") — §5 the evidence owed, §6 the
      exclusions. It names no dependency, no class, no file layout,
      and is committed before any implementation commit.
- [ ] The step plan is confirmed by the reviewer before execution,
      as this run's change-plan: the stack decision recorded, the
      skeleton standing, the datasource wired to the real ground as
      `runtime`, the harness standing with the migration path
      proven, the harness creating the adversity — in that order;
      each step verified before its commit, a failed verification
      reported, never committed. Nothing outside the requirements
      enters; a missing *what* found while building returns to Stage
      1 as a logged re-decision, never absorbed.
- [ ] Every constraint is visible in the delivered files: `runtime`
      alone in configuration, its password read from
      `NEVER_OVERSOLD_RUNTIME_PASSWORD` and nowhere else; no
      `migrator` credential in any profile; nothing on the runtime
      path controls structure — no in-app migration, no object
      mapping, no schema generation; migration tooling at test scope
      only; every dependency entered at the step that earned it with
      its earning reason beside it, deliberate absences commented.
- [ ] The run proof is recorded from actual output: the system up on
      the real ground as `runtime`, health UP with the store's
      component UP, the runtime's version announced; the one
      standard test command green, the integration tests shown
      structurally to run under it — no second command, no test
      that quietly never runs; the zero-applied migration assertion
      passing against the empty home.
- [ ] The exit test answered from the delivered state, member by
      member against §5, §1, §2, §3, §6 — never from a report alone:
      the skeleton runs on the real ground as the runtime identity
      alone, and the harness demonstrably creates SL-1's adversity
      through the real door, every response asserted.
- [ ] The repo is grown into, never overwritten: `.gitignore` grown,
      README kept, `.env` still ignored; the test runtime's
      once-per-machine setup lives in the operator manual, not the
      code; host-level installs are the reviewer's own acts, named
      in README's Prerequisites.
- [ ] Records: README gains Run and Test and the stack's
      Prerequisites line from the skill's template, Run ending in the
      proof of life; ARCHITECTURE shows the ledger as it now runs
      beside the store; CHANGELOG carries the bootstrap as an Added
      line under Unreleased, the version still 0.0; the devlog's
      entry records the walk, its deviations, the certification and
      the exit test's answers, "no business behavior yet" in so many
      words, the probe's death scheduled at SL-1; hand-offs filed in
      TODO; TODO's Step 4 items close here or move to SL-1 by name.
- [ ] Every commit follows commit-messages, none straddles agent and
      project paths; no implementation file older than the decision
      it realizes.
- [ ] The step ran on `step-4-bootstrap`, cut from main, and reached
      main by fast-forward on the reviewer's word.
Notes: opened 2026-09-11. The reviewer confirms at Stage 1 and
Stage 3; the group namespace is theirs to give. Every executing
step states the command and its expected result, runs it, and
records what actually happened. The bootstrap is not a slice: no
kill closes here, the version stays 0.0. Growth after the exit
belongs to cbc-slice, SL-1 first.

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
- ADR-0002: Adopt the framing as the project's truth set (Step 1)
- ADR-0003: Name the project (Step 2)
- ADR-0004: Execution environment: podman local containers (Step 3)
- ADR-0005: Infrastructure services: PostgreSQL, and nothing else (Step 3)
- ADR-0006: Constraints on the store, each enforced by the store (Step 3)
- ADR-0007: Application stack: Spring Boot 4, Java 21, Maven (Step 4)
- ADR-0008: Application structure: package by feature (Step 4)
- ADR-0009: The harness drives plural instances as processes (Step 4)

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
