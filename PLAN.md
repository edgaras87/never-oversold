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

Each step carries Goal · Gate · Notes, and a gate is derived when
the step opens, never at its close. The form and the items every
step's gate starts from are below, under "The step form".

## Standing rules

<!-- Arrived 2026-09-07, before the briefing; on trial from Step 1.
     Its why and rejected options: .claude/decisions.md. -->

- **One branch per step.** A step starts on its own branch cut
  from main, and ends after its gate closes by fast-forward merge
  into main, on the reviewer's word. A restarted step keeps its
  old branch renamed and cuts a new one.

- **Gate items ticked as they come true.** An item is ticked the
  moment it is verifiably true, not at the step's close, and the
  step's own marker stands at `[~]` from its first tick until its
  last. A gate that reads all-unticked through a step is not
  telling the truth about where the step is. A tick records a
  verification, never that the item is final: an item rewritten
  mid-step keeps its tick only if what was verified still
  satisfies the new words, and loses it in the devlog's words if
  not. On trial from Step 6.

## The step form, and the gate items every step starts from

<!-- Copied into a step when it opens, and filled in.
     Copied, never shared: a step's gate item carries that step's
     own tick, so one checkbox cannot serve six steps. On trial
     from Step 7; earlier steps wrote these lines by hand and are
     not reworded, their ticks being a record of what was verified
     in the words the reviewer approved. Why and rejected options:
     .claude/decisions.md -->

```markdown
## Step <n>: <title>  (<the skill it runs, if any>)    [ ]

Goal: <one sentence: the state that is true when this step is done>
Gate:
<the opening items below, filled in>
<this step's own items, derived from the goal, the named skill and
 the registry>
<the closing items below, filled in>
Notes: <opened <date>; what the reviewer signs and when; what the
step presumes from the steps before it>
```

**At the opening, before any of the step's work.** These are
cheap to satisfy then and impossible to satisfy later — a branch
not cut cannot be cut afterwards, and a gate derived at the close
is a description of what happened rather than a standard the work
was held to.

- [ ] The step's branch `<branch>` is cut from main, and the step
      has not been worked anywhere else.
- [ ] This gate was written before the work started, not
      reconstructed from it afterwards.

**At the close.**

- [ ] *(one pair per shape that governs what this step produced;
      the lifecycle says where shapes live, how to find which ones
      govern this, and what to request from elsewhere)* What the
      step produced is read against `<shape>`.
- [ ] Every difference that reading found is settled: corrected in
      the output, taken up into the shape with a dated line, or
      both.
- [ ] Every commit on `<branch>` follows commit-messages.
- [ ] No commit straddles agent and project paths, so the
      arrangement's history stays separable from the project's.
- [ ] No implementation file is older than the decision it
      realizes: the log reads decision, then code.
- [ ] The step reached main by fast-forward from `<branch>`, on the
      reviewer's word.

A step's own gate is these, filled, plus whatever its goal, its
named skill and the registry demand — derived as they always were.

Shapes are not described here. When a shape is written, what
settling a difference does to it, when it is promoted, and how one
moves between repositories are the shape lifecycle's, in
`.claude/rules/`. This section is the gate: which items a step
starts from, and how they are used.

**A default does not fit this step at all.** It is struck there
with its reason, which is a finding about the default rather than a
step cutting a corner.

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

## Step 4: Skeleton & bootstrap  (cbc-bootstrap)    [x] 2026-09-12

<!-- CbC: the seam is WHAT before HOW. Stages 1–2 name capabilities,
     constraints, identity and evidence owed; no dependency, class
     or file layout until Stage 4. This gate keeps the seam: it names
     no stack — the stack is Stage 1's decision, logged before code. -->

Goal: an empty but buildable, testable, runnable system wired to the
real ground, with the evidence harness proven on one adversity.
Gate:
- [x] Stage 0 passed on the actual repo and recorded: the three
      exports stand; both manuals stand with the identity rule, the
      reach facts, the refusals and the one DDL path; the stand-up
      verification is on record; no application code exists. The
      ground was verified live per the operator manual before any
      wiring — the catalog check run now, its actual output beside
      the expected — and the receipt is in the devlog.
- [x] The decisions are logged with their why before any code
      exists, and confirmed by the reviewer before the requirements
      are composed: the stack, by fluency and audience — the
      correctness work the visible substance, not stack novelty;
      the migration tool outside the app; the app knowing one
      identity, `runtime`; the migrations home confirmed as
      `infrastructure/flyway/migrations`; the application structure
      checked against the skill's lived default and kept or
      deviated by name. The stack is an ADR, listed in this plan's
      decision index.
- [x] The initialization identity is decided, not defaulted: the
      group asked of the reviewer, never invented; the artifact
      `never-oversold`; the base package with hyphens dropped; the
      packaging and language version named.
- [x] The harness's one proven adversity is read from the framing's
      words — contention, in SL-1's shape: concurrent admits on one
      item's last units — and the machinery is proven on that class
      alone. It is created from more than one instance of the ledger
      against the one store (F17; TODO's Step 4 item), the shape
      decided at Stage 1 with its why; a single-process pass does
      not close this item.
- [x] Kill 10's flag is answered in so many words: the harness gains
      a controlled clock now, or the shape is deferred to SL-1 by
      name; either way logged, not left to be met by surprise.
- [x] The capability set stands, small and each with its why: an
      HTTP surface, the adversity arriving through the real door;
      operational health, a cheap world-is-up check naming the store
      as a component; connectivity to the ground as `runtime`; the
      evidence harness — migrations run harness-side from the one
      home, integration tests under the one standard test command,
      the migration path proven by a zero-applied assertion.
- [x] The constraints are cited against the infrastructure contract,
      and the exclusions each carry a why: no business behavior (any
      probe is scaffolding, marked to die at SL-1); no persistence
      schema — no table before its invariant, the migrations home
      honestly empty; no other adversity class; no auth unless a
      slice demands identity at the door; no delivery beyond locally
      runnable and testable; nothing ahead of need.
- [x] The requirements document stands at a path decided for this
      repo and logged, in the project's language: §1 identity, §2
      capabilities, §3 constraints, §4 the ground's facts by pointer
      to the two manuals — only the identity and the endpoint facts
      in hand, every environment fact stated notation-neutrally
      ("env var X, default Y") — §5 the evidence owed, §6 the
      exclusions. It names no dependency, no class, no file layout,
      and is committed before any implementation commit.
- [x] The step plan is confirmed by the reviewer before execution,
      as this run's change-plan: the stack decision recorded, the
      skeleton standing, the datasource wired to the real ground as
      `runtime`, the harness standing with the migration path
      proven, the harness creating the adversity — in that order;
      each step verified before its commit, a failed verification
      reported, never committed. Nothing outside the requirements
      enters; a missing *what* found while building returns to Stage
      1 as a logged re-decision, never absorbed.
- [x] Every constraint is visible in the delivered files: `runtime`
      alone in configuration, its password read from
      `NEVER_OVERSOLD_RUNTIME_PASSWORD` and nowhere else; no
      `migrator` credential in any profile; nothing on the runtime
      path controls structure — no in-app migration, no object
      mapping, no schema generation; migration tooling at test scope
      only; every dependency entered at the step that earned it with
      its earning reason beside it, deliberate absences commented.
- [x] The run proof is recorded from actual output: the system up on
      the real ground as `runtime`, health UP with the store's
      component UP, the runtime's version announced; the one
      standard test command green, the integration tests shown
      structurally to run under it — no second command, no test
      that quietly never runs; the zero-applied migration assertion
      passing against the empty home.
- [x] The exit test answered from the delivered state, member by
      member against §5, §1, §2, §3, §6 — never from a report alone:
      the skeleton runs on the real ground as the runtime identity
      alone, and the harness demonstrably creates SL-1's adversity
      through the real door, every response asserted.
- [x] The repo is grown into, never overwritten: `.gitignore` grown,
      README kept, `.env` still ignored; the test runtime's
      once-per-machine setup lives in the operator manual, not the
      code; host-level installs are the reviewer's own acts, named
      in README's Prerequisites.
- [x] Records: README gains Run and Test and the stack's
      Prerequisites line from the skill's template, Run ending in the
      proof of life; ARCHITECTURE shows the ledger as it now runs
      beside the store; CHANGELOG carries the bootstrap as an Added
      line under Unreleased, the version still 0.0; the devlog's
      entry records the walk, its deviations, the certification and
      the exit test's answers, "no business behavior yet" in so many
      words, the probe's death scheduled at SL-1; hand-offs filed in
      TODO; TODO's Step 4 items close here or move to SL-1 by name.
- [x] Every commit follows commit-messages, none straddles agent and
      project paths; no implementation file older than the decision
      it realizes.
- [x] The step ran on `step-4-bootstrap`, cut from main, and reached
      main by fast-forward on the reviewer's word.
Notes: opened 2026-09-11. The reviewer confirms at Stage 1 and
Stage 3; the group namespace is theirs to give. Every executing
step states the command and its expected result, runs it, and
records what actually happened. The bootstrap is not a slice: no
kill closes here, the version stays 0.0. Growth after the exit
belongs to cbc-slice, SL-1 first.
Closed 2026-09-12: Spring Boot 4.1.1 on Java 21, the lived default
structure, the harness racing three forked instances (ADR-0007–
0009); nine planned commits plus one fix; the requirements certified
member by member from the delivered files, 7 tests green from a
clean build with nothing exported, health UP with the store UP on
the real ground. Deviations: the plain context test carried a
placeholder property for one commit on a false premise, corrected
in its own commit; the walkthrough's Ryuk trap found stale on
Testcontainers 2.x, hand-off filed; the fail-fast-on-missing-secret
question surfaced by the reviewer's own run, decided "at release",
filed. The branch item is ticked on the reviewer's word to merge,
given at this boundary.

## Step 5: SL-1 — no over-admission under contention  (cbc-slice)    [x] 2026-09-14

<!-- CbC: one invariant × its adversity, carried to evidence that
     creates the adversity. The seam is WHAT before HOW: the spec
     names no mechanism; the plan names one owner per guarantee;
     a wall is real only when the attack-creating test is green
     against it and was red without it. -->

Goal: SL-1 closed — for every item, in every readable state, the
sum of active reservations ≤ on-hand-count, shown to survive
concurrent admits on one item's last units, from many callers and
from more than one of our own instances.
Gate:
- [x] Stage 0 passed on the actual repo and recorded in the devlog:
      R1 the three exports stand, SL-1 `chosen-next`, the
      reconciliation line whole; R2 `./mvnw test` green at the
      branch point; R3 the store reachable from tests as the
      miniature; R4 the harness creates contention across instances
      (Step 4's race); R6 the registry writable, the records scheme
      in place. R5, the harness can fail, is answered in this
      step's build: the evidence run red against the admit without
      its wall, recorded from actual output, before the wall lands.
      The readiness sign-off is the reviewer's, one dated line.
- [x] Two decisions taken at opening as ADRs, options and why,
      before the specification: the door's conventions — resource
      naming, the JSON shape, the error format, how a refusal
      differs from an invalid request — entered by every later
      slice; and how an item becomes known to the ledger (the
      catalog refused at L2, yet reserve needs an item with an
      on-hand-count). If either changes the definition, by a dated
      revision entry, never in place.
- [x] Stage 1: the correctness specification stands at a path this
      repo chooses, its row in the entry file: the invariant and
      the adversity taken from SL-1's registry row and the
      definition's L1 and L4 as written, zero translation; the
      guarantees derived by attacking the invariant — "what would
      let this hold on paper yet break in fact" — until the attacks
      run dry, each strategy-free; an evidence criterion per
      guarantee naming the adversity its test creates and the
      witness it reads from the store; FC1 and FC3 folded in as the
      registry says; kill 10's shape decided in so many words — a
      controlled clock, or FC3 removing it by one clock with the
      evidence showing that judgment is the one used; kill 9's
      shape decided — whether a decision can exist unrecorded, and
      what shows it cannot. The spec names no lock, constraint,
      key, queue or technology. Signed off by the reviewer before
      the plan.
- [x] Stage 2: the plan stands beside the spec: one structural
      owner per guarantee, the strongest wall available, each
      justified against the named adversity and not in general;
      the escape hatches hunted — admin paths, scripts, migrations
      that bypass the owner; the surface the guarantees need in
      order to live named at its minimum — the first migration from
      the one home, the door the adversity arrives through, the
      checks FC1 names — and nothing beyond. No unowned guarantee.
      Signed off by the reviewer before code.
- [x] Stage 3 ran as this run's change-plan, review at every commit
      boundary; each commit verified before it landed, a failed
      verification reported, never committed. The first migration
      lands under `infrastructure/flyway/migrations/` as
      `migrator`'s; the migration-path assertion turns to "applied
      ≥ 1, none failed"; the runtime path still controls no
      structure.
- [x] The evidence: for every guarantee a test that creates its
      adversity through the real door and reads the witness from
      the store — the race across more than one instance on one
      item's last units, the storm shown real (more asked than fit,
      some refused); an adjustment racing the admits (kill 5); the
      same-count and stale-read cases (kills 3, 4) covered by the
      wall's evidence, or shown to be the same test with the reason
      written; the run recorded red without the wall and green with
      it; all under `./mvnw test`, nothing exported. A green happy
      path closes nothing.
- [x] The probe pair — `probe/`, `ContentionProbeIT`,
      `InstancesRaceIT` — is deleted when the real door lands; the
      machinery under `testsupport/` stays and is what the evidence
      uses.
- [x] Every deviation from the spec, the plan or the skill is in
      the devlog with what was done instead and why; a guarantee
      found in flight enters the spec with this slice as its
      provenance.
- [x] Stage 4: the spec document closes as invariant → guarantees →
      owner → evidence, readable by a stranger; the registry flips
      SL-1 to `closed (date, evidence)` and re-decides the ordering
      with its reason — whether SL-1's wall already holds SL-2's
      invariant, said in so many words; the standing guards named
      at close.
- [x] Records: CHANGELOG's first user-visible behaviour under
      Unreleased, the version's move decided here as a state of the
      evidence; README shows the door as a stranger would use it;
      ARCHITECTURE shows the ledger with its first table and its
      wall; the devlog carries the walk, the red and the green from
      actual output, the exit; TODO's Step 5 items close here or
      move by name; hand-offs filed.
- [x] Every commit follows commit-messages, none straddles agent and
      project paths; no implementation file older than the decision
      it realizes.
- [x] The step ran on `step-5-sl-1`, cut from main, and reached
      main by fast-forward on the reviewer's word.
Notes: opened 2026-09-12. The reviewer signs at readiness, at the
spec and at the plan; the door's conventions are theirs to
confirm. The slice is not a feature: reserve exists because the
invariant needs a door, and nothing enters the surface the
guarantees do not need.
Closed 2026-09-14: nine planned commits, ten landed (a change-plan
revision at commit 4's boundary added the assertion-style step).
Six guarantees by attack, the store's constraint and one
conditional statement as the wall; both storms run red first
(22 and 23 of 20), green unchanged with the wall; 29 tests under
the one command; the run on the real ground with V1 applied as
`migrator`. Version 0.1, the first evidence-closed state. SL-2
kept next by a dated registry revision. Deviations: vocabulary
sub-packages (ADR-0008's note); a text-search structural test
replaced by ArchUnit before landing; `Location` omitted until a
reader exists; `reserved` over-approximates until SL-3. The
branch item is ticked on the reviewer's word to merge, given at
this boundary.

## Step 6: SL-2 — the correction never undercuts the holds  (cbc-slice)    [x] 2026-09-21

<!-- The second slice on one witness. SL-1's wall already refuses a
     correction under the held units, so what is unproven here is
     the shape of that refusal and the two corrections nothing has
     yet created — resent, and out of order. -->

Goal: SL-2 closed — no admitted change to the on-hand-count leaves
it under the sum of active reservations, shown against an honest
downward correction under the reserved sum, that same correction
resent, and corrections arriving out of order.
Gate:
- [x] Stage 0 passed on the actual repo and recorded in the devlog:
      R1 the three exports stand, SL-2 `chosen-next`, the
      reconciliation line whole; R2 `./mvnw test` green at the
      branch point, from actual output; R3 the store reachable from
      tests as the miniature; R4 the harness creates *this* slice's
      adversity class — an honest request replayed and a pair
      reordered at the real door, sequential, not contention; the
      instance-forking machinery SL-1 built is not what this slice
      needs, and saying so is part of the check; R6 the registry
      writable, the records scheme in place. R5, the harness can
      fail, is answered here and not deferred: a wall already
      stands, so each evidence test is seen red with its wall made
      absent on the working tree — a state that never lands in
      history — before it is seen green. The readiness sign-off is
      the reviewer's, one dated line.
- [x] The registry row goes to `in-progress` when the specification
      lands — the first project-visible work, not at this opening
      and not at the close.
- [x] Stage 1: the correctness specification stands beside SL-1's,
      its row in the entry file: the invariant and the adversity
      taken from SL-2's registry row and the definition's L1 and L4
      (kills 6, 7, 8) as written, zero translation; the guarantees
      derived by attacking the invariant — "what would let this
      hold on paper yet break in fact" — until the attacks run dry,
      each strategy-free; an evidence criterion per guarantee naming
      the adversity its test creates and the witness it reads from
      the store, the same witness as SL-1 and said so. The
      correction's shape is decided here — the two shapes framing
      parked, refuse the correction or let it end reservations,
      both weighed, the loser named with why; if the choice changes
      the door's contract it is an ADR before the spec closes, and
      if it changes the definition, a dated revision entry, never
      in place. The row carries no flag — the written zero, said in
      so many words. The W1 remainder of kills 7 and 8 stays
      fenced: what a resent or reordered correction leaves wrong
      about the world is not this slice's to fix, and the spec says
      which part it owns. The spec names no lock, constraint, key,
      queue or technology. Signed off by the reviewer before the
      plan.
- [x] Stage 2: the plan stands beside the spec: one structural
      owner per guarantee, the strongest wall available, each
      justified against the named adversity and not in general;
      where more than one face could hold a guarantee, the
      candidates in front of the reviewer — each face, how it holds
      the guarantee, its cost, a recommendation — before the
      choice, never a loser named after it; what SL-1's constraint
      already owns said by name, so this slice claims no wall it
      did not build; the escape hatches hunted afresh against the
      owners this slice adds — admin paths, scripts, migrations;
      the surface at its minimum, nothing entering that a guarantee
      does not need. No unowned guarantee. Signed off by the
      reviewer before code.
- [x] Stage 3 ran as a commit plan, the boundary shown at every
      commit and committed on the reviewer's word; each commit
      verified before it landed, a failed verification reported,
      never committed.
- [x] The evidence: for every guarantee a test that creates its
      adversity through the real door and reads the witness from
      the store — the honest correction under the reserved sum
      (F9), that correction resent (F11), two corrections out of
      order (F13); each run red with its wall absent, recorded from
      actual output, then green unchanged with the wall standing;
      all under `./mvnw test`, nothing exported. A green happy path
      closes nothing.
- [x] Every deviation from the spec, the plan or the skill is in
      the devlog with what was done instead and why; a guarantee
      found in flight enters the spec with this slice as its
      provenance.
- [x] Stage 4: the spec document closes as invariant → guarantees →
      owner → evidence, readable by a stranger; the registry flips
      SL-2 to `closed (date, evidence)` by a dated revision entry
      and re-decides the ordering with its reason, naming what this
      slice leaves provisional and what it hands to SL-3 by name;
      the standing guards named at close.
- [x] Records: CHANGELOG carries what a user can now see, the
      version's move decided here as a state of the evidence;
      README true for the correction's shape as a stranger meets
      it; ARCHITECTURE shows any wall this slice adds; the devlog
      carries the walk, the red and the green from actual output,
      the exit; TODO's Step 6 items close here or move by name;
      hand-offs filed — among them the bundle's open question,
      answered by name if this slice's plan leans on the
      infrastructure contract's facility paragraph, and answered
      just as plainly if it does not.
- [x] Every commit follows commit-messages, none straddles agent and
      project paths; no implementation file older than the decision
      it realizes.
- [x] The step ran on `step-6-sl-2`, cut from main, and reached
      main by fast-forward on the reviewer's word.
Notes: opened 2026-09-20. The reviewer signs at readiness, at the
spec and at the plan. SL-1 is presumed — the reserved sum this
invariant is checked against is the one SL-1 keeps true — and its
refusal of a correction under the held units is provisional until
this slice's specification says what the shape is.
Closed 2026-09-21: the shape decided — refuse the correction, the
losing shape and its three reasons in the record's §3 — and no
production code added, the wall standing from SL-1 and justified
here against an honest request rather than a race. Nine commits
against eight planned, one revision at commit 4's boundary adding
the skill's correction. 39 tests from 29; every red recorded from
actual output, the witness reading onHandCount=7 against
activeSum=8 with the wall absent, 19 where 9 was asserted under
delta semantics, and three planted violations for the structural
guards. Deviations: E3's red came from the door's shape rather
than the guard, which could not redden a resend; the hand-off line
moved to the records commit, TODO being a project path. One
finding the specification had not carried: E4 was two things
wearing one name, and the red run separated them — the half that
cannot kill is now a tripwire that says so, and the skill was
corrected in place for the general lesson. Version 0.2. The branch
item is ticked on the reviewer's word to merge, given at this
boundary.

## Steps 7..N-1: Invariant slices  (cbc-slice, one step per stage)

Goal: each remaining registry slice closed by evidence that creates
its adversity; ordering re-decided at each close, never assumed
from the original expectation.
Gate: derived when each stage opens — verifiable facts, from the
goal, the named skill, and the registry; written into the stage
before its work starts. One item is the same in every slice step
from Step 7 on: the record is read against
`docs/construction/slice-record-shape.md` after it is written and
before the merge, and each difference is either corrected in the
record or taken up into the shape with a dated line — never left
unremarked.
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
- ADR-0010: The door's conventions (Step 5)
- ADR-0011: An item becomes known by its first adjustment (Step 5)

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
7. What this project gives back — whether
   `docs/construction/slice-record-shape.md` has held across every
   slice, and whether it is worth offering as a baseline to the
   bundle. Reading *their* baselines does not wait for here: that
   happens once, after the first slice closes, because the first
   slice record is the only independent sample a project produces
   and everything after it inherits anyway (the shape document says
   why). What waits for the retrospective is the other direction —
   what this project found that no other has.

Then fold lessons into the playbook the steps came from — the
"Steps from" line at the top names it — in the repo that owns it,
and bump its version there.
