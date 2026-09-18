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

<!-- Hand-offs the bundle has taken are gone from here; this repo's
     decisions log is the record (2026-09-15 @ 7bbf49a for the SL-1
     harvest, 2026-09-17 for the in-place answer) — the bundle's
     harvest lines in each skill's header end with the next copy,
     which opens on instruction only. What stays is for the
     playbook, the handbook, the kit, or a later trigger. -->

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
      ADR-0035 waits on this run's report). The fifth, skills
      edited between two pins (2026-09-14), the handbook took for
      its conventions 2026-09-15 (HANDBOOK ADR-0038, provisional on
      the first edit through a re-pin); the bundle answered for the
      method skills 2026-09-17, taking the seven rules as this run
      wrote them, so the rules file stays as the text that governs
      them (decisions.md, that date).
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
- [ ] Retrospective (commit-messages): the convention's imperative
      test ("if applied, this commit will …") has not been followed
      here since Step 1. Record commits are written as statements
      instead — "the bundle takes the in-place rule", "the rules
      file narrows to the bundle's skills" — and read better that
      way, a record commit reporting what became true rather than
      ordering it. Work commits stayed imperative ("update the
      bundle's skills"). Nobody decided this; it settled. Either
      the convention names the split or the log gives it up —
      noticed at the re-pin of 2026-09-18, where six more commits
      followed the log rather than the text.
- [ ] Owed to the handbook, whenever it happens: the first edit of a
      skill copy that goes through a re-pin, reported as one TODO
      line per convention-lifecycle §3 step 4 — HANDBOOK ADR-0038 is
      provisional on that report (its decision 5), and names beside
      it as open: work done by a step that ran on an edit the source
      later declined.
- [ ] Hand-off to the handbook: a personal `cut-a-kata` skill —
      practice exercises cut from live work at the moment the
      learner says "I could not rebuild this": a marker in ten
      seconds, a card at a boundary (skill, problem, oracle,
      checkpoints, reference pinned to a commit, comparison
      questions; the how kept out), done later closed-book, graded
      against the reference. Lived in this run at SL-1 (three cards
      cut, none yet done); on trial in the learner's user-level
      skills; graduates when it has served katas in two projects.
      Handbook's answer 2026-09-15 (kit 9e28143): parked in its
      Later until it graduates — no place in the tiers model for a
      person's own practice, no artifact kind for a skill yet; the
      likely shape then a guide or a pointer; it wants to hear when
      two projects have been served.
- [ ] Hand-off to the CbC bundle (infra-establish): the skill is
      silent on the contract's paragraph about the store's facility
      — the ways it makes two writers disagree — so this run wrote
      one at Step 3 and sent a slice's choice of face to "its own
      specification"; a face is a mechanism and belongs to the
      slice's plan (fixed here, 6c986bd). If the contract should
      carry a facility paragraph, the skill says so and where the
      face is chosen. Bundle's answer 2026-09-17: held, not
      declined — the wording fix was this run's and correct, but one
      instance is not a shape; it watches for a second run reaching
      the same gap unprompted, and reads SL-2 leaning on this
      paragraph as evidence the first slice needed it. Say so if
      SL-2 does.
- [ ] Hand-off to the CbC bundle: they asked to hear where the
      update procedure of 2026-09-18 fell short, it being the first
      run of it as a note and a copy together. Two places, both of
      them things no diff could have told us. The note named two
      citations of convention-lifecycle §8 step 4 going stale and
      there were three — it did not see this repo's own CLAUDE.md,
      which carries the same pointer in its own prose. And moving
      the conventions onto the bundle's channel makes the receipt
      branch a live question the note leaves unasked: the kit's
      `kit-<hash>` receipt was the compare for those four files.
      This run answered it for itself — no receipt for a pristine
      copy, its compare being the delivery commit, and the kit's
      next receipt simply carrying four fewer files (decisions.md,
      2026-09-18) — and says so in case the bundle wants an answer
      of its own in the procedure. Everything else in the note held:
      the strip was the strip, and the one structural change was
      called out exactly where a diff would have hidden it.
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
