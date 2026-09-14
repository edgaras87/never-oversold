# TODO

<!-- Add items the moment they're discovered — that's what empties your head.
     Triage when closing a step. Prune "Later" ruthlessly: deleting an
     idea you'd re-derive anyway costs nothing.
     Rule: an inline TODO:/FIXME: anywhere in the work must reference an
     item here. -->

## Now (current plan step)

- [ ] Step 6 (SL-2, cbc-slice): cut its branch from main, derive
      its gate into PLAN first. Its specification decides the
      correction's shape — refuse it, or let it end reservations —
      which SL-1 left provisional: today a count under the held
      units is refused by the constraint's own answer
      (`Ledger.adjust`, `ReservationDoorIT`,
      `AdjustmentRaceIT`); whichever shape SL-2 chooses keeps
      SL-1's wall. Its evidence creates the resent (F11) and
      reordered (F13) corrections, uncreated so far.

## Next (upcoming steps — assign each to a step when triaged)

- [ ] Each step while the branch trial runs: its gate carries one
      item — the step ran on its own branch cut from main and
      reached main by fast-forward on the reviewer's word.
- [ ] Step N (Release): fail fast on a missing secret — decided
      2026-09-12 as "not at bootstrap, at release": today the ledger
      starts with the literal placeholder as its password and only
      health (`db` DOWN) tells; the store logs `password
      authentication failed`; README's Run section names the
      symptom. The need becomes real when the README's commands are
      verified on a clean machine by a stranger, which is the
      release gate — decide there, with the trigger recorded.
      Reproduced 2026-09-12: boot reports Started in ~2.5s, the
      probe answers 500 and health 503 on first request, because
      the pool opens lazily and nothing in the app borrows a
      connection at boot (Flyway is test-scoped). So the check must
      either borrow one connection eagerly at startup (Hikari's own
      fail-fast then fires) or reject the unresolved placeholder at
      bind time. Either way the store-free context test then needs
      the variable supplied, for a true reason this time.
- [ ] Step 7 (SL-3): an expired hold still counts in `reserved`
      until an exit ends it — SL-1's counter over-approximates on
      the safe side (slice record §7). SL-3's exits lower the
      counter in the same transaction as they end a reservation;
      the trigger-maintained counter named in §7 is the first
      option if a second writer to `reservation` ever appears.
- [ ] Step 7 (SL-3): the reserve reply carries no `Location`
      header until a reservation has a reader — a deviation from
      ADR-0010's letter, logged in SL-1's plan; lift it when the
      reader lands.

## Later / someday

- [ ] Retrospective: PLAN's Release step was reshaped at birth —
      a Goal line, the gate in the run's step form, the two `(CbC)`
      items no longer naming another run's exclusions — and Step 2
      was retitled from "Define (naming)" to "Identity (name,
      description, remote)" when it opened, the remote being part
      of a public identity. Fold both back to the cbc-run-pure
      playbook.
- [ ] Retrospective: four arrangement pieces are on trial from
      Step 1 — the one-branch-per-step rule (PLAN, Standing rules),
      the kit @ af16eb7 with its settings-file gate rejected, the
      operator's CLAUDE.local.md holding the pace, the entry file
      under .claude/. Each that held folds back to its source: the
      rule to the playbook the steps came from, the other three to
      the handbook (its ADR-0035 waits on this run's report).
- [ ] Retrospective: framing steps ran as commit series — draft,
      one revision per reviewer question, verdict — so each
      question's effect is a diff. If it held, fold back to the
      source: cbc-framing's record section (the mode), and
      change-plans if the series-per-step shape wants naming there.
- [ ] Hand-off to the CbC bundle (cbc-framing): the registry
      template's opening line puts the skill's name, its step
      number and the delegation slot into a project artifact —
      rephrase it in project voice ("Framed <date>; every verdict
      the reviewer's" or the delegation record's pointer). Same
      for any template line a reader would need the agent's
      arrangement to decipher.
- [ ] Hand-off to the CbC bundle (cbc-framing): the export section
      says the definition grows "L2 → L1 → L4 → L3 → L5, one lived
      state per commit" while the workflow presents L1→L5; say
      that the file ends in the map's order and the commits carry
      the derivation, so the next run does not append.
- [ ] Hand-off to the CbC bundle (cbc-framing ↔ infra-establish):
      the framing's census never asks for the runtime ground —
      machine, plural instances, the store as a service, the clock —
      while the ground's readiness check requires the definition to
      carry it. This run added the block by a dated revision at
      Step 3's opening; the census (or its export) should ask for it
      at framing.
- [ ] Hand-off to the CbC bundle (infra-establish): in a repo with
      records, the normal shape is no establishment log — decisions
      as ADRs, the walk as lived in the devlog, expected results in
      the verify suite and the operator manual — with `compose.yaml`
      and the env files at the root, the runnable ground under
      `infrastructure/`, the manuals under `docs/infrastructure/`.
      This run did it as a logged deviation; the skill's
      records-and-outputs section should carry it as the default
      for record-keeping repos, so the next run does not re-decide.
- [ ] Hand-off to the CbC bundle (cbc-bootstrap): Stage 2 names
      `internal/construction/bootstrap-requirements.md` as the
      requirements document's path, a directory a record-keeping
      repo does not have; this run put it at
      `docs/construction/`, beside the builder's other manuals.
      The skill should say "at the path the project's records
      choose" and name no directory of its own.
- [ ] Hand-off to the CbC bundle (cbc-bootstrap): the walkthrough's
      Ryuk trap and `templates/testcontainers.properties` are stale
      for Testcontainers 2.x — the `ryuk.disabled` key is not read
      (the keys are `ryuk.container.image|privileged|timeout`), and
      Ryuk ran unmodified under rootless podman 5.8 on this host,
      reaping the throwaways within seconds. The template should
      carry `docker.host` alone; the trap should say "if Ryuk fails
      on your host, `TESTCONTAINERS_RYUK_DISABLED=true` in the
      environment", not a properties line.
- [ ] Hand-off to the CbC bundle (cbc-bootstrap): the harness
      reference's concurrency probe is single-process, and Stage 1
      never asks whether the definition's ground names more than
      one instance — this run derived it into its own gate from
      F17 and left the reference by decision (ADR-0009). The lived
      shape to harvest: the store singleton lifted out of the
      database base so a test with no application context shares
      it; an instance forked from the build's own output with a
      runtime classpath file the dependency plugin writes before
      the tests (test-scope code stays out of the instance); the
      probe answering its process id so served-by is asserted;
      health with the store's component UP as the up signal; the
      instances closed in `finally`, destroy then forcibly. Traps
      met: none bit — free ports from a bound-and-released socket
      held across runs. Stage 1 should ask the plurality question
      by name; the reference should carry the shape as the plural
      variant beside the in-process probe.
- [ ] Hand-off to the CbC bundle (cbc-bootstrap): the walkthrough's
      stage 3 records that the suite stays green with the ground
      down, not the flip side lived here — Boot's binding keeps an
      unresolvable `${VAR}` as the literal, the application starts
      with it as its password, and only health (`db` DOWN) tells;
      the plain context test therefore needs no environment and no
      test property. Three homes: the stage 3 trap; the README
      template's Run section carrying the symptom line; Stage 1's
      exclusions naming "fail fast on a missing secret: not at
      bootstrap, decided at release", so the next run does not
      re-decide it mid-set.
- [ ] Hand-off to the CbC bundle (cbc-bootstrap ↔ infra-establish):
      the bootstrap's `templates/application.yaml` reads the port
      from `<PROJECT>_DB_PORT` while infra-establish's
      `.env.example` and compose template name it `POSTGRES_PORT`.
      A run copying both gets an application that never reads the
      port the ground publishes — silent on 5432, wrong on any
      other. This run used the ground's name. One key, in both.
- [ ] Hand-off to the CbC bundle (cbc-bootstrap): the harness
      reference argues the authority split must hold in evidence
      runs but witnesses it only through `current_user`. Lived
      here as a third migration-path test: `runtime` attempting
      `CREATE TABLE` in the miniature, refused with the ground's
      own message — three lines that catch a miniature quietly
      wired without the split. Its trap: Spring wraps the driver's
      error, so the assertion goes on the root cause. Same section
      could state the Boot 4 fact relied on here: with only
      `flyway-core` and the database module at test scope, no
      Flyway auto-configuration runs, so the harness's explicit
      call is the only migration path in tests.
- [ ] Hand-off to the CbC bundle (cbc-slice): R5, "the harness can
      fail", cannot be answered at Stage 0 of the *first* slice —
      no wall exists to break. Lived answer: the naive version is
      committed first, the wall is its own diff, and the evidence
      is run red on the working tree (wall and constraint removed)
      before the wall's commit, recorded from actual output. The
      skill should say so for the first slice and make "red before
      green" the build stage's own gate.
- [ ] Hand-off to the CbC bundle (cbc-slice): the first slice
      births far more than its invariant — the schema, the first
      migration, the door and its conventions, how the aggregate
      comes to exist at all (here: an item by its first adjustment,
      ADR-0011). Stage 1 should name the "birth whats" the framing
      cannot carry, and the plan stage should own "the surface at
      its minimum". The worked example is duplicate-delivery; a
      contention twin would show the counter-over-approximation
      trade this run met.
- [ ] Hand-off to the CbC bundle (cbc-slice, plan stage): a
      guarantee held by *absence* — no process clock, no state
      outside the store — has no runtime evidence; its wall is a
      rule on the compiled classes (ArchUnit at test scope, each
      rule with a `because` naming its guarantee, each shown to
      fire on a plant). A text search over the source was tried
      first and replaced: it misses a static import, a method
      reference, a reformat. The enforcement hierarchy should name
      this rung between "single validated entry path" and "code
      review".
- [ ] Hand-off to the CbC bundle (cbc-bootstrap, harness
      reference): how the evidence asserts on a body is a stack
      convention the reference leaves unsaid, so each run decides
      by habit. Lived: substring first, replaced mid-slice by JSON
      path (a substring cannot tell 3 from 30 or say a field
      exists). A variation point: by path for a shape, by type
      when a shared API contract exists; never by substring.
- [ ] Hand-off to the CbC bundle (cbc-slice, Stage 1): a registry
      row may carry a flag ("named here so the slice inherits the
      warning") — SL-1's kill 10, and kill 9's process-death shape
      inside a contention slice. The skill never says what a flag
      demands. Lived: every flag is answered by name in the
      specification — staged as its own evidence, or removed by a
      definition with the removal shown (FC3 removed kill 10; G4
      showed kill 9 has no interval). Make it a Stage 1 exit item.
- [ ] Hand-off to the CbC bundle (cbc-slice, Stage 4): the close is
      more than a status. Lived: the row flips to `in-progress` when
      the specification lands (the first project-visible work; this
      run never used the value and went `chosen-next` → `closed` in
      one commit), and the close names the provisionals and what
      the slice hands to later slices by name — SL-1's wall
      pre-decided part of SL-2, its counter left a debt to SL-3.
      The concept's "a built slice may teach that the next is
      wrong or split" needs this step to be true in the registry.
- [ ] Hand-off to the CbC bundle (infra-establish ↔ cbc-slice): the
      contract template says a slice "chooses its face [of the
      store's facility] in its own specification, with its why";
      cbc-slice's specification forbids mechanisms, and the face is
      one. One word: "in its plan". Found when SL-1's plan chose
      check constraints and row serialization and the spec could
      not have.
- [ ] Hand-off to the CbC bundle (cbc-slice, shape): the skill
      carries its WHAT/HOW seam in words ("zero mechanisms", then
      "implementation judgment is yours") but no stack reference
      beside it, where cbc-bootstrap has three. Not a split into two
      skills — the unit is one invariant end to end and the
      completion test is single — but a `references/spring-slice-
      reference.md` on the bootstrap's model: imitated, never
      pasted; each artifact stating the outcome it realizes, with
      variation points, so a reader can reject the artifact and
      keep the outcome. What this run lived for it: the naive-then-
      wall commit split and the red run; the witness over plain JDBC
      from outside every instance, with the sampler for "in every
      readable state"; the one-statement admit with the row count
      as the decision; value types at the door; bodies by path; the
      absence rung as bytecode rules. A run on another stack writes
      its own reference; the SKILL stays stack-free.
- [ ] Hand-off to the handbook: a personal `cut-a-kata` skill —
      practice exercises cut from live work at the moment the
      learner says "I could not rebuild this": a marker in ten
      seconds, a card at a boundary (skill, problem, oracle,
      checkpoints, reference pinned to a commit, comparison
      questions; the how kept out), done later closed-book, graded
      against the reference. Lived in this run at SL-1 (three cards
      cut, none yet done); on trial in the learner's user-level
      skills; graduates when it has served katas in two projects.
- [ ] Retrospective: the entry file's opening paragraph carried a
      state clause from the kit's stub ("nothing to build, no tests,
      no runtime"), rewritten at Step 1 and Step 3's closes and
      missed at Step 4's — "no code yet" stood on main with seven
      tests green until a re-read on 2026-09-12. Dropped here: the
      paragraph now states only what never changes, the problem and
      its framing date; current state is PLAN's by the records
      table. Fold back to the kit's stub, and to agent-arrangement
      as an instance of its test 2 (a line with a moment goes where
      the moment is).
- [ ] Retrospective: the worked-example twin in cbc-framing and
      cbc-slice claims a byte-identical copy but differs in its
      provenance path line — fold back to the source, never edit
      the pinned copies here.

## Known issues (deferred deliberately — each entry: what, why accepted, when to revisit)

- <issue>. Accepted because <reason>. Revisit at <step / condition>.
