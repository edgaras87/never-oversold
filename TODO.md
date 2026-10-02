# TODO

<!-- Add items the moment they're discovered — that's what empties your head.
     Triage when closing a step. Prune "Later" ruthlessly: deleting an
     idea you'd re-derive anyway costs nothing.
     Rule: an inline TODO:/FIXME: anywhere in the work must reference an
     item here. -->

## Now (current plan step)

- [ ] Step 7 (SL-3, cbc-slice): a reservation exits once. Opened
      2026-10-02 on the bundle's copies @ a3b6b8c; readiness signed
      the same day; Stage 1 next. It inherits by name:
      an expired hold still counts in the held units until an exit
      ends it (SL-1 §7), and an operator who has counted the shelf
      and found fewer units than are held has no recourse — whether
      an operator-side exit should exist is this slice's question,
      handed over by SL-2 §7 rather than invented there. It is also
      the first slice to meet the close-time shape check, and its
      gate is the first derived from PLAN's step form.

## Next (after Step 7)

Nothing yet.

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

## To the deliverer

<!-- What this run addresses to the bundle, and only that, so a
     reading looks in one place (delivered-copies.md, rule 4). A
     line leaves when a note answers it. Last answered: the note of
     2026-10-01, read through c33a996. -->

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
- [ ] Framing steps as commit series, and the imperative test in
      commit-messages: both being weighed there now; this run's
      reading of the imperative split is where they start. Nothing
      owed here.
