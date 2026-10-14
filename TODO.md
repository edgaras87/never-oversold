# TODO

Words and tags used here: [glossary](docs/glossary.md).

<!-- Add items the moment they're discovered — that's what empties your head.
     Triage when closing a step. Prune "Later" ruthlessly: deleting an
     idea you'd re-derive anyway costs nothing. Rule: an inline
     TODO:/FIXME: anywhere in the work must reference an item here.
     -->

## Now (current plan step)

- [ ] The deliverer's re-check of this run from main, once: RETRO.md
      and *To the deliverer*, below.

## Next (after the re-check)

- [ ] Step N, Release. Its gate is derived when it opens.

## Later / someday

<!-- What this run addresses to the bundle is under "To the
     deliverer", at the foot. What stays here is the learner's own,
     or waits on a later trigger. -->

- [ ] Seeing what changed between versions, not between commits.
      Moved here from Next on 2026-10-01, on the reviewer's word. The
      options turned down, and the working answer in use since
      2026-09-21 (the index as the checkpoint, word-level diffs), are
      in `.claude/decisions.md` (2026-10-10). Still to decide:
      - whether this becomes a written rule here or a line to the
        deliverer, since the commit-plan convention is theirs;
      - whether a rejected draft ever needs keeping beyond the
        devlog line that says what it said and why it went.

      The polish tried one option turned down here: a commit per
      fix, regrouped at the end, under the reviewer's explicit
      exception for its branches. Verdict after pass 1 landed
      (2026-10-11), in the reviewer's words: it worked well. Commits
      were made freely on the branch, regrouped by kind before
      landing, and the report was the record of each fix.

- [ ] A list of an item's reservations, for an operator. Raised
      2026-10-02 at SL-3's opening (its record, "Whether an operator
      can end reservations"). An operator who counts fewer units than
      are held can release a hold only if they know its identifier,
      and nothing lists them. No guarantee needs it, so no slice
      builds it. Trigger: a reader, or the README's stranger, meets
      that operator's case, or a re-framing takes up the operator
      side.

- [ ] Records that describe a moment carry no mark saying so. The
      bootstrap requirements, the operator manual's lived lines, a
      slice record's provisionals: each was true on a date and read
      as if true now. Raised 2026-10-01. Answered in part by the
      retrospective: a closing check for living lines that say
      "now", "today" or "not yet" (RETRO.md, D17), run at Step 10's
      close. Whether passages also carry a "true as of" mark waits
      for Release, on the reviewer's call.

- [ ] Should `temp/` be tracked? It is neither ignored nor
      committed, so a draft written there never reaches history.
      The table comparison of 2026-10-01 survives only as its
      decisions entry, although `visual-comparison` says "git
      history keeps it". Tracking it would keep drafts. It would
      also commit the bundle's whole staging at every take, on top
      of the take commit that already holds the same files. Raised
      2026-10-01, postponed by the reviewer.

- [ ] The learner's own, not for the bundle: a personal
      `cut-a-kata` skill. It cuts practice exercises from live work
      at the moment the learner says "I could not rebuild this".
      - A marker takes ten seconds.
      - A card is written at a boundary: skill, problem, oracle,
        checkpoints, a reference pinned to a commit, comparison
        questions, with the how kept out.
      - The card is done later, closed-book, and graded against the
        reference.

      Lived in this run at SL-1: three cards cut, none yet done. On
      trial in the learner's user-level skills. It moves to the
      handbook when it has served katas in two projects.

## Known issues (deferred deliberately — each entry: what, why accepted, when to revisit)

- ADR-0005's list of what is not provisioned gives two reasons SL-1
  later overturned: "expiry is the application's clock" (the queue
  bullet), and SL-1's clock evidence as "an application-level
  injection" (the clock bullet). SL-1 put expiry on the store's
  clock, and the application reads none (SL-1 G5). The decision,
  PostgreSQL and nothing else, stands, and both bullets'
  conclusions still hold. Accepted because ADRs are never edited,
  and the decision did not change, so a new ADR would record nothing
  new. Revisit when a later ADR touches the service set, or if a
  reader is misled by it. Found 2026-10-01.

- SL-3's E7 (`NoSecondWayOutTest`) sees only what is written as a text
  block in the ledger. A statement assembled at runtime from pieces,
  or split across a concatenation, passes every rule in that test:
  E7's four, and SL-4 E4's fifth. Accepted because E7 is the early
  warning, not the wall. Since 2026-10-05 the store checks the units
  held at the commit of any transaction that touches an item
  (migration V3). It refuses the numbers moving without a receipt,
  whoever writes, however the statement was built. E7 adds what the
  store cannot say, and says it at build time: that the ledger is the
  only writer, and that every exit is written as one statement.
  Revisit if migration V3 is ever weakened or dropped, since E7 would
  then be the only guard again. Found 2026-10-05; rewritten
  2026-10-07, when E7 came to read the statements by their parts.

- SL-3's check on the units held (migration V3) still reads every
  reservation an item has ever had, ended ones included, at every
  commit that touches it. Since 2026-10-08 the index on
  `reservation(item_id)` (migration V4, ADR-0015) keeps that to the
  one item's history rather than the whole table, and the definition
  sizes the system at up to a few thousand reservations on one item
  over its life. What stays is growth itself: nothing removes ended
  reservations, which is W6's fence. Accepted because retention is
  outside the promise and the size it is built for keeps the read
  small. The further fix, if it is ever needed: an ended mark on the
  reservation with a partial index on the open ones, so the sum reads
  only those. Revisit when an item's history passes the stated size,
  or a decision's speed is measured. Found 2026-10-05; narrowed
  2026-10-08.

- An interrupted consume can keep its item waiting. A consume whose
  instance is cut off from the store (not killed) stays open at the
  store until the store gives up on that connection. Other
  decisions on that item wait until then. No wrong number is read.
  Once undone, the consume leaves the item as it was. Accepted
  because waiting is fenced (W3), and the limit would be a setting
  on the store, a change to the ground no guarantee asks for (SL-4's
  record, "Whether anything is left to converge").
  Revisit if a deployment puts a network between the instances and
  the store, or if a wait on an item is ever seen. Found 2026-10-09.

- SL-4's one split the store cannot see. Consume's moves are split:
  the receipt and the units held are made final together, then the
  count is lowered. No hold reaches between them, since both touch the
  item's row. And migration V3 sees nothing wrong, since the units
  held agree. Only SL-4 E4, the fifth rule in `NoSecondWayOutTest`,
  stands there, and it reads the ledger's source, as SL-3 E7 does (the
  known issue above). Accepted because T4 makes the ledger the only
  writer, and E4 reads every statement it has. Revisit if anything but
  the ledger is trusted to write, or if a store check on the count is
  ever earned. Found 2026-10-10.

## To the deliverer

<!-- What this run addresses to the bundle, and only that, so a
     reading looks in one place (delivered-copies.md, rule 4). A
     line leaves when a note answers it. Last answered: the note of
     2026-10-10, read through c7073c7. -->

- [ ] Apply RETRO.md's decisions. Each says what to change and
      where it lands; D22 lists what run 4 must meet as gate items
      in its own PLAN.
- [ ] Files this project wrote, offered whole: the writing and
      one-home rules (`.claude/rules/`), the check script
      (`.claude/checks/records.py`), the retrospective skill with
      its manual (`.claude/skills/retrospective/`), and the skill
      that applies it, with its manual
      (`.claude/skills/apply-retrospective/`). Why each: RETRO.md,
      D1, D2, D17, D23 and D24; D23 and D24 also draft their PLAN
      steps.
- [ ] `CLAUDE.local.md`, the reviewer's way of working, is not in
      the repo (it is ignored). The reviewer brings it by hand
      (RETRO.md, D3).
- [ ] Retrospective (playbook): PLAN's Release step was reshaped at
      birth: a Goal line, the gate in the run's step form, and the
      two `(CbC)` items no longer naming another run's exclusions.
      Fold back to the cbc-run-pure playbook (its v5 already derives
      Release's gate; check the rest). Still open after the
      retrospective: run 3's Release was not run (RETRO.md, D20).
- [ ] Four arrangement pieces on trial from Step 1: judged in
      RETRO.md, D4 (all kept).
- [ ] Gate items ticked as they come true (on trial from Step 6):
      judged in RETRO.md, D4 (kept).
- [ ] `infra-establish` says nothing of the contract's facility
      paragraph. Held there until a second run reaches the same gap
      unprompted. Asked whether SL-2 relied on it: no. SL-2 faces no
      race (its record, "Kills covered"), and its first three
      guarantees reuse SL-1's comparison of options, instead of
      choosing one against the contract.
- [ ] The absence rung (absence as a level of the hierarchy in
      chapter 02): held there until a second run meets a guarantee
      held by an absence. Not that trigger, but said as promised:
      this run met one again in SL-2. Its G5 and G6 are both held
      by an absence, each guarded by a test that reads the source,
      as SL-1's no-process-clock rule is.
- [ ] This project's four shapes, offered (`.claude/shapes/`): the
      slice record (RETRO.md, D12), the evidence test (D13), the
      kill and the registry entry (D29). Each marks its skeleton,
      what any project would want, apart from this project's
      illustration (`shapes-lifecycle.md` §5).
- [ ] Should a rule be deliverable on trial? Answered in RETRO.md,
      D4: a trial names the close that judges it.
- [ ] The method has no glossary: decided in RETRO.md, D2 and D6.
- [ ] deliverer: evaluate this run's changes to `cbc-framing` since
      5361213 — untried here: written from this run's evidence,
      never run.
- [ ] deliverer: evaluate this run's changes to `cbc-slice` since
      5361213 — untried here: written from this run's evidence,
      never run.
- [ ] deliverer: evaluate this run's changes to `cbc-bootstrap`
      since 5361213 — untried here: written from this run's
      evidence, never run.
- [ ] deliverer: evaluate this run's changes to `infra-establish`
      since 5361213 — untried here: written from this run's
      evidence, never run.
- [ ] deliverer: evaluate this run's changes to
      `.claude/rules/delivered-copies.md` since 5361213.
- [ ] deliverer: evaluate this run's changes to
      `.claude/rules/shapes-lifecycle.md` since 5361213.
- [ ] deliverer: evaluate this run's changes to `commit-plan` since
      5361213.
- [ ] `delivered-copies.md` rule 1 now lets a concept chapter be
      edited from a mistake lived here (RETRO.md, D28). That
      reverses the chapter clause of CBC ADR-0036. Accept it, or
      decline it and say why.
- [ ] `cbc-framing` calls its own parts "Step 0" to "Step 4". A
      project's PLAN calls its parts steps too, so "step 3" can
      mean either. Run 3's glossary keeps "step" for PLAN's steps;
      the skill's parts need another word.

