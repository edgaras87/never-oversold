# TODO

<!-- Add items the moment they're discovered — that's what empties your head.
     Triage when closing a step. Prune "Later" ruthlessly: deleting an
     idea you'd re-derive anyway costs nothing.
     Rule: an inline TODO:/FIXME: anywhere in the work must reference an
     item here. -->

## Now (current plan step)

- [ ] The review under "Next" below, before Step 8's plan: opened
      2026-10-07 on `housekeeping-review-after-sl-3`, cut from main
      after Step 7's merge, on the reviewer's word. Each question is
      discussed and decided before any edit; then a commit plan for
      what the decisions change. Step 7 closed SL-3 and reached main
      the same day.
- [ ] Step 8 (SL-4, cbc-slice): consume's two moves hold together,
      the registry's chosen-next since 2026-10-07. It inherits by
      name: the two moves are already one statement in one
      transaction, so the invariant is held by structure and owes
      only its proof — our death between the moves, an outcome we
      cannot know, neither created by any harness yet; and the
      exit's identity it presumes exists, one receipt per
      reservation with its kind and instant, a repeated exit
      answering as the first.

## Next (after Step 7)

- [ ] Two questions the slices answer every time without naming
      them — take them up once SL-3 closes, before SL-4's plan.
      Raised 2026-10-05 by the reviewer, at SL-3's units-held check.
      **Where a rule's responsibility lives, application or store.**
      The faces so far have put it in the application alone, in the
      store alone, the application computing and the store only
      refusing (SL-3's units-held check), or both. Each side's cost
      is different: the store's guard reaches every writer but costs
      at every commit and hides from a reader of the code; the
      application's alone holds only while no one else writes. A
      store that recomputes what the application computed is a
      double check; one that only compares is cheaper and cannot
      drift. **What scale a decision assumes.** Performance and
      memory are weighed nowhere on the record; a face is chosen
      for this system's size without saying so, or what would undo
      it. To do: list how SL-1 to SL-3 placed each rule (who
      computes, who refuses, who else writes); decide where that map
      lives (ARCHITECTURE, or the slice record's faces) and whether
      each face gains "assumes" and "revisit when" beside its cost;
      and whether the slice skill's owner ladder (store, type, one
      path) should ask both questions — if so, a finding for the
      deliverer under "To the deliverer", the copies being theirs.
      Not an optimising step: the definition puts throughput and
      latency outside the promise (W3) and growth as seen, not
      handled (W6). The gap is that a face assumes a scale without
      saying so — weighed at the plan, where a cost can change which
      wall is chosen; tuning that changes no meaning (an index) may
      come any time after, the evidence re-run unchanged.
      Two more, raised the same day. "Nothing enters ahead of need"
      (ADR-0007) left `reservation(item_id)` unindexed although
      every decision searches it: is an index a need known from the
      code that searches, or only from a measurement? And where is
      the system's size decided at all? The intent settles it as a
      demonstration nobody operates, and W3 keeps throughput out,
      but no record states an operating envelope a face could cite
      (how many items, reservations, instances), and the method's
      chapters name scale nowhere — a lesson about a chapter is a
      line under "To the deliverer", the chapters never edited here.
      And the lesson under all of it: the framing never said who
      may write the store, so every slice defends against writers
      no one named (G6's "paths never anticipated"). Closed by
      default — the boundary stated in the framing, enforced by the
      ground, opened when a real second writer comes — may be the
      better start; whether the definition gains it, and whether the
      framing chapter should ask for it, is the review's.
      One concrete case for it, raised 2026-10-07: the ledger never
      updates or deletes a reservation row — its ending is a
      separate receipt — yet `runtime` may do both. That door is
      what stays open past the units-held check: an open hold
      deleted together with its units (signed in SL-3's §8 as the
      safe direction), `expires_at` moved, a hold resized or moved
      with its units carried. Reservations made write-once — a
      guard trigger like the receipts', or `runtime` losing update
      and delete on the table — would close all three. A §8
      revision if taken, and a change to `MigrationPathIT`'s
      runtime-writes test and the infrastructure contract's term 4
      if by grants.
      SL-3's close added seven, settled in the same review
      (2026-10-07). In the slice skill, edited in place here under
      `delivered-copies.md` rule 2 — each edit with its decisions
      entry, and the copy's line under "To the deliverer", so the
      deliverer reads the diff and the why: (a) Stage 2's hunt for
      escape hatches, bounded by the definition's trust assumptions
      — a writer the definition trusts is named, not walled; it
      follows question one. (b) Stage 3: a red counts only if it
      fails on the witness, not on a status, a crash or a setup
      step; lived at SL-2 and again at SL-3. (c) A third kind of
      test named beside evidence and tripwire: a wall's own check,
      showing the wall refuse directly, discharging no kill —
      SL-1's catalog test, SL-3's `ReceiptGuardIT` and
      `UnitsHeldCheckIT`. (d) A race whose red needs its window
      widened is widened on the red tree only, and says so; lived
      once, at SL-3's tidy against consume. In this project's
      shapes and records: (e) a wall's stated backstop is seen at
      its red run, or written as unproven — SL-3's G5 named
      `item_never_oversold`, which did not hold; (f) SL-3's record
      calls E7 a tripwire in a looser sense than the skill's, one
      wording to settle. In the framing, the deliverer's: (g) who
      may write the store, and at what scale — the questions above;
      where the review finds the method should ask them, each
      becomes a line under "To the deliverer", the chapters and the
      framing skill being theirs.
      The fuller draft: `temp/app-store-responsibility.md`
      (untracked; this item is what reaches history).

## Later / someday

<!-- What this run addresses to the bundle is under "To the
     deliverer", at the foot. What stays here is the learner's own,
     or waits on a later trigger. -->

- [ ] Seeing what changed between versions, not between commits.
      Moved here from Next on 2026-10-01, on the reviewer's word.
      While one commit's worth of work is being polished, each new
      version can only be diffed against the last commit, so the
      reviewer must re-read the whole thing to find the part that
      answers their last clarification. Worse where a paragraph is
      rewritten and reflows: a line diff marks every line changed
      when three words moved.
      Turned down, with reasons, so the pass need not re-run them:
      a branch per iteration (switching branches to read a
      paragraph); a commit per iteration, squashed or merged at the
      end (it fights two rules already held — the reviewer's word
      before any commit, and the commit-plan convention's "the
      commits that exist are the steps done", so draft commits make
      the history lie about what was finished).
      The working answer, in use from 2026-09-21 and needing no
      decision: the index is the checkpoint. The agent stages each
      version it shows and says which version is staged; the next
      edit then reads as a diff against the version the reviewer
      last read, however many rounds it takes; one commit at the
      end. The reflow problem is answered by word-level diffing —
      `git diff --word-diff`, or the IDE's own word or character
      highlighting inside a changed line.
      In the reviewer's IntelliJ: Settings → Version Control → Git
      → Enable staging area, which splits the Commit window into
      Staged (the version read) and Unstaged (what changed since),
      with the diff of an unstaged file taken against the staged
      one. Clicking the staged entry still shows the whole change
      against the last commit, so the full picture stays one click
      away. Fallback if that feels wrong in practice: the IDE's
      Local History, which records every save without git and can
      be labelled at the moment a version is shown.
      Still to decide: whether this becomes a written rule here or
      a line to the deliverer, the commit-plan convention being
      theirs; and whether a rejected draft ever needs keeping
      beyond the devlog line that says what it said and why it
      went — the pattern used for the writing rule drafted and
      reverted on 2026-09-21.

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
      reviewer's call.

- [ ] A fact written in two places gets fixed in one. The contract
      was corrected to "plan" and the operator manual kept
      "specification"; SL-1's record took SL-2's decision in three
      places and missed a fourth. `commit-plan` now sweeps for
      moved names; nothing sweeps for a changed fact. Decide whether
      that is a habit, a gate item, or a line to the deliverer.
      Raised 2026-10-01; same timing as above.

- [ ] Should `temp/` be tracked? Today it is neither ignored nor
      committed, so a draft written there never reaches history:
      the table comparison of 2026-10-01 survives only as its
      decisions entry, although `visual-comparison` says "git
      history keeps it". Tracking it would keep drafts; it would
      also commit the bundle's whole staging at every take, on top
      of the take commit that already holds the same files. Raised
      2026-10-01, postponed by the reviewer.

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
  rules. Accepted because E7 is the tripwire, not the wall: since
  2026-10-05 the store checks the units held at every commit (V3),
  and refuses the numbers moving without a receipt whoever writes,
  however the statement was built. E7 adds what the store cannot
  say — that the ledger is the only writer, and every exit is shaped
  as one — and says it at build time. Revisit if V3 is ever
  weakened or dropped, since E7 would then be the only guard again.
  Found 2026-10-05; rewritten 2026-10-07, when E7 came to read the
  statements by their parts.

- SL-3's check on the units held (V3) has an unmeasured cost. At
  every commit that touches an item it sums that item's
  reservations and looks up each one's receipt, once per row
  written — a reserve or an exit runs it twice, tidy writing five
  receipts six times. `reservation` has no index on `item_id` (the
  store makes none for a reference), so each run scans the whole
  table, every item's reservations ever made; tidy's own search by
  item scans it the same way, since SL-3's commit 5. Reservations
  and receipts are kept forever (W6). Seen as nothing at the
  evidence's scale (hundreds of rows); not measured. Accepted because the store refusing any writer was
  worth more than a cost no evidence can see yet. The known fixes:
  an index on `reservation(item_id)`; an ended mark on the
  reservation with a partial index on the open ones, so the sum
  reads only those; retention, which is W6's. Revisit when an item's
  history reaches thousands of reservations, or a decision's latency
  is measured. Found 2026-10-05.

## To the deliverer

<!-- What this run addresses to the bundle, and only that, so a
     reading looks in one place (delivered-copies.md, rule 4). A
     line leaves when a note answers it. Last answered: the note of
     2026-10-01, read through c33a996. -->

- [ ] deliverer: evaluate this run's changes to
      `.claude/rules/delivered-copies.md` since a3b6b8c.

- [ ] Retrospective (playbook): PLAN's Release step was reshaped at
      birth — a Goal line, the gate in the run's step form, the two
      `(CbC)` items no longer naming another run's exclusions. Fold
      back to the cbc-run-pure playbook (its v5 already derives
      Release's gate; check the rest). Held until the retrospective.
- [ ] Retrospective: four arrangement pieces on trial from Step 1 —
      the one-branch-per-step rule (PLAN, Standing rules), the kit
      @ af16eb7 with its settings-file gate rejected, the operator's
      CLAUDE.local.md holding the pace, the entry file under
      .claude/. Each that held folds back to the bundle, the kit's
      owner. The fifth, skills edited between two pins, is settled:
      it is `delivered-copies.md`, taken @ 0000855. Held until the
      retrospective.
- [ ] Retrospective (playbook): a fifth arrangement piece on trial,
      from Step 6 — gate items ticked as they come true, the step's
      marker at `[~]` while it runs, a tick recording a verification
      and never that the item is final (PLAN, Standing rules; the
      why and the rejected option in `.claude/decisions.md`). If it
      holds, the fold-back is the run playbook's step form. Held
      until the retrospective.
- [ ] This project's slice-record shape, offered as a finding
      (`.claude/shapes/slice-record.md`): held there, and weighed
      when the bundle first holds a shape of its own. Nothing owed
      here. It has moved since the note: on 2026-10-01 it took a
      third part, the faces weighed for a guarantee, as blocks;
      its dated lines say what taught it.
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
- [ ] This project's evidence-test shape, offered as a finding
      (`.claude/rules/evidence-test.md`, written 2026-10-07 at
      SL-3). Exposed here, because the failure it answers happened
      while writing tests, after three slices had shown what they
      produce without it. Whether it reaches another project, and
      whether hidden first so that project's own tests can be read
      against it, is yours. Its skeleton is marked apart from this
      project's illustration.
- [ ] A question, raised by that shape: should a rule, not only a
      shape, be deliverable hidden or on trial — read at a gate
      rather than loaded while working — until a second project
      arrives at it unprompted? Today only shapes have the two
      places; a rule learned in one run is in force in the next
      from its first step. This run's own standing rules carry "on
      trial" in PLAN until the retrospective; a delivered rule has
      no such state.
- [ ] Framing steps as commit series, and the imperative test in
      commit-messages: both being weighed there now; this run's
      reading of the imperative split is where they start. Nothing
      owed here.
