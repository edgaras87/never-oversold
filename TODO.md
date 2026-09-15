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

<!-- Hand-offs the bundle has taken are gone from here; the bundle's
     harvest lines in each skill's header and this repo's decisions
     log (2026-09-15, @ 7bbf49a) are the record. What stays is for
     the playbook, the handbook, the kit, or a later trigger. -->

- [ ] Retrospective (playbook): PLAN's Release step was reshaped at
      birth — a Goal line, the gate in the run's step form, the two
      `(CbC)` items no longer naming another run's exclusions — and
      Step 2 was retitled from "Define (naming)" to "Identity (name,
      description, remote)" when it opened, the remote being part
      of a public identity. Fold both back to the cbc-run-pure
      playbook (its v5 already derives Release's gate; check the
      rest).
- [ ] Retrospective (handbook, playbook): four arrangement pieces
      are on trial from Step 1 — the one-branch-per-step rule
      (PLAN, Standing rules), the kit @ af16eb7 with its
      settings-file gate rejected, the operator's CLAUDE.local.md
      holding the pace, the entry file under .claude/. Each that
      held folds back to its source: the rule to the playbook the
      steps came from, the other three to the handbook (its
      ADR-0035 waits on this run's report). Add the fifth on trial
      from 2026-09-14: skills changed in place under guards
      (.claude/rules/skills-changed-in-place.md).
- [ ] Retrospective (cbc-framing, change-plans): framing steps ran
      as commit series — draft, one revision per reviewer question,
      verdict — so each question's effect is a diff. If it held,
      fold back to the source: cbc-framing's record section (the
      mode), and change-plans if the series-per-step shape wants
      naming there.
- [ ] Retrospective (kit): the entry file's opening paragraph
      carried a state clause from the kit's stub ("nothing to
      build, no tests, no runtime") and staled at Step 4's close;
      dropped here 2026-09-12, the paragraph now stating only what
      never changes. The bundle fixed its own fill (cde0e97); the
      kit's stub and agent-arrangement's test 2 (a line with a
      moment goes where the moment is) are the handbook's half,
      still open.
- [ ] Hand-off to the handbook (convention-lifecycle §8), from the
      kit update @ 20b1bc8 on 2026-09-15: the receipt-branch
      compare its TODO waits on was used — `kit-20b1bc8` one commit
      over `kit-af16eb7`, `git diff` between them the upstream
      change isolated, `git diff kit-af16eb7 -- <copies>` empty for
      no local edit — and it worked; one trap: a receipt branch cut
      before the project's ignore lines swallows build output on
      `git add -A`. And one friction: step 3's letter puts a
      two-commit landing (skills agent-side, one stub row
      project-side) under a change-plan, which costs two commits
      for a set of two; this run landed them as two plain commits
      and says so in its registry entry.
- [ ] Hand-off to the handbook (tiers model §3, convention-lifecycle
      §8): a copy may carry a local layer between two pins. This run
      settled it 2026-09-15 as seven rules
      (.claude/rules/skills-changed-in-place.md, decisions.md
      2026-09-14 and 2026-09-15): a skill is a copy pinned at a
      source commit; edited in place only from lived work, as a
      question or outcome any project would want, never project-
      specific, with a header line and a log entry; what the project
      alone needs goes into its records; at every step's close one
      TODO line per edited skill asks the source to evaluate since
      the pin; after the reply the source's version is copied whole
      and the pin moves, declined edits gone and never edited back;
      a step opening before the reply continues on the edited copy;
      a finished skill is never edited. The model's flow already
      fits — the layer is a record, harvested by diffing against the
      pin, the run sends nothing — and §8 step 4 already re-applies,
      drops or promotes an edited copy; what neither says is that a
      run may make the edit on purpose, what it must carry, and that
      the re-pin lands whole. The concept chapters stay pinned. The
      overlay-file form was weighed and rejected for now, named as
      the fallback if declined-but-needed edits ever become a
      pattern.
- [ ] Hand-off to the CbC bundle (CBC ADR-0007, the harvest
      section): a run's copy may already carry the change, with its
      own dated header line, under the run's in-place rule (above).
      The harvest then diffs the copy against the pin and keeps the
      run's provenance in the master's harvest line, taking or
      declining each hunk; the verdict returns as a document or as
      the header line the run sees at its next copy. ADR-0007's
      mechanics already do this — its first lived case was a run
      editing its local walkthrough — the rule does not yet say so.
- [ ] Hand-off to the handbook: a personal `cut-a-kata` skill —
      practice exercises cut from live work at the moment the
      learner says "I could not rebuild this": a marker in ten
      seconds, a card at a boundary (skill, problem, oracle,
      checkpoints, reference pinned to a commit, comparison
      questions; the how kept out), done later closed-book, graded
      against the reference. Lived in this run at SL-1 (three cards
      cut, none yet done); on trial in the learner's user-level
      skills; graduates when it has served katas in two projects.
- [ ] Hand-off to the CbC bundle (infra-establish): the skill is
      silent on the contract's paragraph about the store's facility
      — the ways it makes two writers disagree — so this run wrote
      one at Step 3 and sent a slice's choice of face to "its own
      specification"; a face is a mechanism and belongs to the
      slice's plan (fixed here, 6c986bd). If the contract should
      carry a facility paragraph, the skill says so and where the
      face is chosen.
- [ ] Held at the bundle, not here — recorded so the trail is
      whole: the absence rung in the enforcement hierarchy (a
      concept question, in the bundle's Later with a trigger: a
      second run meeting a guarantee held by absence); the Spring
      slice reference (the bundle keeps it as a baseline design
      under its docs/baselines/, handed to a run after its build is
      on record; the skill carries no pointer). Nothing to do here
      unless SL-2 meets either.

## Known issues (deferred deliberately — each entry: what, why accepted, when to revisit)

- <issue>. Accepted because <reason>. Revisit at <step / condition>.
