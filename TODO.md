# TODO

<!-- Add items the moment they're discovered — that's what empties your head.
     Triage when closing a step. Prune "Later" ruthlessly: deleting an
     idea you'd re-derive anyway costs nothing.
     Rule: an inline TODO:/FIXME: anywhere in the work must reference an
     item here. -->

## Now (current plan step)

- [ ] Step 5 (SL-1, cbc-slice): cut its branch from main, derive
      its gate into PLAN first; the door's conventions decided at
      its opening; kill 10's clock shape decided in its
      specification; the probe pair (`probe/`, `ContentionProbeIT`,
      `InstancesRaceIT`) dies when the real door lands — the
      machinery under `testsupport/` stays.

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
- [ ] Step 5 (SL-1), at its opening: the door's conventions —
      resource naming, JSON shape, the error format, how a refusal
      differs from an invalid request — decided once as a logged
      decision when the first real endpoint (reserve) is specified;
      every later slice enters them. Not the bootstrap's: its probe
      carries no meaning and dies here.
- [ ] Step 5 (SL-1), in its specification: kill 10's evidence shape
      — clocks disagreeing about activeness cannot be hammered; a
      controlled clock, or FC3's one-clock judgment shown to be the
      one used. Deferred here by name at the bootstrap's Stage 1
      (2026-09-11): nothing entered ahead of that need.

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
