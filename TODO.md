# TODO

<!-- Add items the moment they're discovered — that's what empties your head.
     Triage when closing a step. Prune "Later" ruthlessly: deleting an
     idea you'd re-derive anyway costs nothing.
     Rule: an inline TODO:/FIXME: anywhere in the work must reference an
     item here. -->

## Now (current plan step)

- [ ] Step 9, the retrospective, closing: main moves by
      fast-forward from `retrospective` on the reviewer's word.
      Then everything the deliverer must take is marked under *To
      the deliverer*.

## Next (after the retrospective)

- [ ] Step N, Release. Its gate is derived when it opens.

## Later / someday

<!-- What this run addresses to the bundle is under "To the
     deliverer", at the foot. What stays here is the learner's own,
     or waits on a later trigger. -->

- [ ] Seeing what changed between versions, not between commits.
      Moved here from Next on 2026-10-01, on the reviewer's word. The
      options turned down and the working answer in use since
      2026-09-21 — the index as the checkpoint, word-level diffs —
      are in `.claude/decisions.md` (2026-10-10). Still to decide:
      whether this becomes a written rule here or a line to the
      deliverer, the commit-plan convention being theirs; and
      whether a rejected draft ever needs keeping beyond the devlog
      line that says what it said and why it went. The polish tries
      one option turned down here — a commit per fix, regrouped at
      the end — under the reviewer's explicit exception for its
      branches. Verdict after pass 1 landed (2026-10-11): it worked
      well, in the reviewer's words — commits made freely on the
      branch, regrouped by kind before landing, the report the
      record of each fix.

- [ ] A glossary for this project's reader. Raised 2026-10-08 at the
      review after SL-3: the records say kill, fold, fence, face,
      wall, witness, tripwire and evidence throughout, and nothing
      in this repository defines them in one place — README names
      only two words, the store and the door. The intent's audience
      is a reader judging the construction, who meets those words in
      the registry and the slice records with nowhere to look them
      up. Decide whether README's two grow into a short list, one
      line each, or a page of their own linked from it — and that it
      is derived from the definition and the method, never authored
      apart from them. Trigger: Release's README pass, or sooner if
      a reader asks.

- [ ] A list of an item's reservations, for an operator. Raised
      2026-10-02 at SL-3's opening (its record, §3): an operator
      who counts fewer units than are held can release a hold only
      if they know its identifier, and nothing lists them. No
      guarantee needs it, so no slice builds it. Trigger: a reader
      or the README's stranger meets that operator's case, or a
      re-framing takes up the operator side.

- [ ] Records that describe a moment carry no mark saying so. The
      bootstrap requirements, the operator manual's lived lines, a
      slice record's provisionals: each was true on a date and read
      as if true now, and the error check of 2026-10-01 found three
      of them. Decide whether such records or passages carry a
      "true as of" mark, and in what form. Raised 2026-10-01;
      structure waits for Release or the retrospective, on the
      reviewer's call. Met again in the polish's pass 1: its readers
      took sections headed "as delivered" for claims about now.

- [ ] A fact written in two places gets fixed in one. The contract
      was corrected to "plan" and the operator manual kept
      "specification"; SL-1's record took SL-2's decision in three
      places and missed a fourth. `commit-plan` now sweeps for
      moved names; nothing sweeps for a changed fact. Decide whether
      that is a habit, a gate item, or a line to the deliverer.
      Raised 2026-10-01. Tried in the polish's pass 1: each area's
      reader listed the facts its files state, and the lists were
      compared; that caught one no area saw alone, the pom's version.
      Decide in the walk-through.

- [ ] Should `temp/` be tracked? Today it is neither ignored nor
      committed, so a draft written there never reaches history:
      the table comparison of 2026-10-01 survives only as its
      decisions entry, although `visual-comparison` says "git
      history keeps it". Tracking it would keep drafts; it would
      also commit the bundle's whole staging at every take, on top
      of the take commit that already holds the same files. Raised
      2026-10-01, postponed by the reviewer.

- [ ] Own, no creditor: a personal `cut-a-kata` skill —
      practice exercises cut from live work at the moment the
      learner says "I could not rebuild this": a marker in ten
      seconds, a card at a boundary (skill, problem, oracle,
      checkpoints, reference pinned to a commit, comparison
      questions; the how kept out), done later closed-book, graded
      against the reference. Lived in this run at SL-1 (three cards
      cut, none yet done); on trial in the learner's user-level
      skills; graduates when it has served katas in two projects.

## Known issues (deferred deliberately — each entry: what, why accepted, when to revisit)

- ADR-0005's list of what is not provisioned gives two reasons SL-1
  later overturned: "expiry is the application's clock" (the queue
  bullet), and SL-1's clock evidence as "an application-level
  injection" (the clock bullet). SL-1 put expiry on the store's
  clock, and the application reads none (its G5). The decision —
  PostgreSQL and nothing else — stands, and both bullets'
  conclusions still hold. Accepted because ADRs are immutable and
  the decision did not change, so a new ADR would record nothing
  new. Revisit when a later ADR touches the service set, or if a
  reader is misled by it. Found 2026-10-01.

- SL-3's E7 (`NoSecondWayOutTest`) sees only what is written as a
  text block in the ledger. A statement assembled at runtime from
  pieces, or split across a concatenation, passes all three of its
  rules. Accepted because E7 is the early warning, not the wall: since
  2026-10-05 the store checks the units held at every commit (V3),
  and refuses the numbers moving without a receipt whoever writes,
  however the statement was built. E7 adds what the store cannot
  say — that the ledger is the only writer, and every exit is shaped
  as one — and says it at build time. Revisit if V3 is ever
  weakened or dropped, since E7 would then be the only guard again.
  Found 2026-10-05; rewritten 2026-10-07, when E7 came to read the
  statements by their parts.

- SL-3's check on the units held (V3) still reads every reservation
  an item has ever had, ended ones included, at every commit that
  touches it. Since 2026-10-08 the index on `reservation(item_id)`
  (V4, ADR-0015) keeps that to the one item's history rather than the
  whole table, and the definition sizes the system at up to a few
  thousand reservations on one item over its life. What stays is
  growth itself: nothing removes ended reservations, which is W6's
  fence. Accepted because retention is outside the promise and the
  size it is built for keeps the read small. The further fix, if it
  is ever needed: an ended mark on the reservation with a partial
  index on the open ones, so the sum reads only those. Revisit when
  an item's history passes the stated size, or a decision's speed is
  measured. Found 2026-10-05; narrowed 2026-10-08.

- An interrupted consume can keep its item waiting. A consume whose
  instance is cut off from the store — not killed — stays open at
  the store until the store gives up on that connection, and other
  decisions on that item wait until then. No wrong number is read;
  undone, the consume leaves the item as it was. Accepted because
  waiting is fenced (W3), and the limit would be a setting on the
  store, a ground change no guarantee asks for (SL-4's record, §3).
  Revisit if a deployment puts a network between the instances and
  the store, or if a wait on an item is ever seen. Found 2026-10-09.

- SL-4's one split the store cannot see. Consume's moves split so
  the receipt and the units held become final together and the count
  after — no hold reaches between them, since both touch the item's
  row, and V3 sees nothing wrong, the units held agreeing. Only E4,
  the fifth rule in `NoSecondWayOutTest`, stands there, and it reads
  the ledger's source, as E7 does (the known issue above). Accepted
  because T4 makes the ledger the only writer, and E4 reads every
  statement it has. Revisit if anything but the ledger is trusted to
  write, or if a store check on the count is ever earned. Found
  2026-10-10.

## To the deliverer

<!-- What this run addresses to the bundle, and only that, so a
     reading looks in one place (delivered-copies.md, rule 4). A
     line leaves when a note answers it. Last answered: the note of
     2026-10-10, read through c7073c7. -->

- [ ] Apply RETRO.md's decisions. Each says what to change and
      where it lands; D22 lists what run 4 must meet as gate items
      in its own PLAN.
- [ ] Files this project wrote, offered whole: the writing and
      one-home rules (`.claude/rules/`), and the retrospective
      skill with its manual (`.claude/skills/retrospective/`). Why
      each: RETRO.md, D1, D2 and D23; D23 also drafts the PLAN step.
- [ ] `CLAUDE.local.md`, the reviewer's way of working, is not in
      the repo (it is ignored). The reviewer brings it by hand
      (RETRO.md, D3).
- [ ] Retrospective (playbook): PLAN's Release step was reshaped at
      birth — a Goal line, the gate in the run's step form, the two
      `(CbC)` items no longer naming another run's exclusions. Fold
      back to the cbc-run-pure playbook (its v5 already derives
      Release's gate; check the rest). Still open after the
      retrospective: run 3's Release was not run (RETRO.md, D20).
- [ ] Four arrangement pieces on trial from Step 1: judged in
      RETRO.md, D4 (all kept).
- [ ] Gate items ticked as they come true (on trial from Step 6):
      judged in RETRO.md, D4 (kept).
- [ ] This project's slice-record shape: decided in RETRO.md, D12.
- [ ] `infra-establish`'s silence on the contract's facility
      paragraph: held there, until a second run reaches the same
      gap unprompted. Asked whether SL-2 leaned on it: no. SL-2
      faces no race (its record, "Kills covered"), and its first
      three guarantees reuse SL-1's comparison of faces rather than
      choosing one against the contract.
- [ ] The absence rung: held there, until a second run meets a
      guarantee held by absence. Not that trigger, but said as
      promised: this run met one again in SL-2. Its G5 and G6 are
      both held by an absence, each guarded by a test that reads
      the source, in the spirit of SL-1's no-process-clock rule.
- [ ] This project's evidence-test shape: decided in RETRO.md,
      D13.
- [ ] Should a rule be deliverable on trial? Answered in RETRO.md,
      D4: a trial names the close that judges it.
- [ ] The method has no glossary: decided in RETRO.md, D2 and D6.
