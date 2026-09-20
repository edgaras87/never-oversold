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
     playbook, the bundle — which owns the kit — or a later
     trigger. -->

- [ ] Retrospective (playbook): PLAN's Release step was reshaped at
      birth — a Goal line, the gate in the run's step form, the two
      `(CbC)` items no longer naming another run's exclusions. Fold
      back to the cbc-run-pure playbook (its v5 already derives
      Release's gate; check the rest). The other half this item
      carried, Step 2's retitle to "Identity (name, description,
      remote)", the bundle discharged 2026-09-20 in its note: taken,
      nothing further owed.
- [ ] Retrospective (bundle, playbook): four arrangement pieces
      are on trial from Step 1 — the one-branch-per-step rule
      (PLAN, Standing rules), the kit @ af16eb7 with its
      settings-file gate rejected, the operator's CLAUDE.local.md
      holding the pace, the entry file under .claude/. Each that
      held folds back to its source, and the source of the kit is
      the bundle since 2026-09-18 (its second note of 2026-09-20:
      the handbook is no longer this project's upstream, its kit
      and conventions the bundle's; HANDBOOK ADR-0035 waited on
      this run's report, and the report goes where the kit went).
      The rule's fold-back to the playbook the bundle discharged
      2026-09-20 — taken, nothing further owed, the trial here runs
      on. The fifth, skills edited between two pins (2026-09-14),
      is settled: it went into convention-lifecycle §3 step 4
      (HANDBOOK ADR-0038), the bundle took the seven rules for the
      method skills 2026-09-17, and the first edit went through a
      re-pin 2026-09-20 and was taken (decisions.md, that date) —
      no longer provisional on either side.
- [ ] Retrospective (playbook): a fifth arrangement piece on trial,
      from Step 6 — gate items ticked as they come true, the step's
      marker at `[~]` while it runs, a tick recording a verification
      and never that the item is final (PLAN, Standing rules; the
      why and the rejected option in `.claude/decisions.md`). It
      came from SL-1's registry lesson, one step out: a record that
      reads unchanged through a whole step is not the source of
      truth for where the step is. If it holds, the fold-back is the
      run playbook's step form, which the bundle owns.

- [ ] Own, no creditor (was a hand-off to the handbook, which the
      bundle's second note of 2026-09-20 says is no longer
      reachable from here; kept as the learner's own item, same
      trigger): a personal `cut-a-kata` skill —
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
- [ ] Hand-off to the CbC bundle: where the second note of
      2026-09-20 fell short, since they asked. Its map correction
      named two Later items that pointed at the handbook and there
      were three: the four-arrangement-pieces item folded three
      pieces back "to the handbook" and called ADR-0038 provisional
      — re-addressed here to the bundle, as the kit's owner, and
      the provisional mark dropped. The same grep-the-whole-span
      lesson, third time: a map correction moves every arrow that
      pointed at the old party, not the two the sender has in mind.
      Both asks of the morning's hand-off were taken and that line
      is closed; the five-noun edit is in the masters, and the
      header-line clause is gone from §3 step 4.
- [ ] Held at the bundle, not here — recorded so the trail is
      whole, each verdict in the bundle's note of 2026-09-20 being
      the whole of the event, nothing behind it to check.
      The absence rung in the enforcement hierarchy: a concept
      question, in the bundle's Later with a trigger, a second run
      meeting a guarantee held by absence. Unchanged.
      The Spring slice reference: the bundle keeps it as a baseline
      design under its docs/baselines/, the skill carries no
      pointer. Its rule "handed to a run after its build is on
      record" the bundle withdrew 2026-09-20 — it never fired, it
      generalised from one slice, and a shape adopted from a
      reference is inherited by the next slice rather than derived.
      Nothing is handed at a slice close, ever; SL-2 and everything
      after it is derived with no reference in hand, as SL-1 was.
      Framing steps as commit series (cbc-framing, commit-plan):
      draft, one commit per reviewer question, verdict — each
      question's effect a diff. Asked whether commit-plan should
      name the shape. Held 2026-09-20, not decided: it collides with
      that skill's own assertion that the commits which exist are
      the steps done, so naming it means saying what a step is — a
      change to the convention, not a sentence added. cbc-framing's
      record section (the mode) is still this run's to fold back at
      the retrospective.
      The imperative test (commit-messages): not followed here since
      Step 1 — record commits are statements ("the bundle takes the
      in-place rule"), work commits imperative ("update the bundle's
      skills"); nobody decided it, it settled, and the re-pin of
      2026-09-18 followed the log rather than the text. Asked
      whether the convention names the split. Held 2026-09-20, not
      decided; this run's reading of where the line falls is the
      one the bundle starts from.
      Trigger for the two held items: the next time that file is
      opened for any reason, or a retrospective, whichever first.
      Nothing owed meanwhile; either moves sooner on request.
      Nothing to do here unless SL-2 meets the first two.

## Known issues (deferred deliberately — each entry: what, why accepted, when to revisit)

- <issue>. Accepted because <reason>. Revisit at <step / condition>.
