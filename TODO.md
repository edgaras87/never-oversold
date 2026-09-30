# TODO

<!-- Add items the moment they're discovered — that's what empties your head.
     Triage when closing a step. Prune "Later" ruthlessly: deleting an
     idea you'd re-derive anyway costs nothing.
     Rule: an inline TODO:/FIXME: anywhere in the work must reference an
     item here. -->

## Now (current plan step)

- [ ] Between steps. Step 6 closed SL-2 on 2026-09-21; the writing
      pass ran 2026-09-21..23, and the bundle's delivery @ 0000855
      was taken 2026-10-01. Two things stand before Step 7 opens,
      listed under Next, each its own branch cut from main, in the
      order the reviewer picks.
- [ ] Step 7 (SL-3, cbc-slice): a reservation exits once, the
      registry's chosen-next since 2026-09-21. It inherits by name:
      an expired hold still counts in the held units until an exit
      ends it (SL-1 §7), and an operator who has counted the shelf
      and found fewer units than are held has no recourse — whether
      an operator-side exit should exist is this slice's question,
      handed over by SL-2 §7 rather than invented there. It is also
      the first slice to meet the close-time shape check, and its
      gate is the first derived from PLAN's step form.

## Next (before Step 7)

- [ ] The writing sweep's remainder. The pass produced the rule and
      applied it to one record: SL-2's guarantees and its owners.
      Untouched: SL-1's record, README, the manuals, and the two
      comparison tables in SL-2's §8, which the shape marks as
      undecided rather than exempt. The rule is
      `.claude/shapes/slice-record.md`; how shapes live is
      `.claude/rules/shapes-lifecycle.md`.

- [ ] Seeing what changed between versions, not between commits.
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

## Later / someday

<!-- What this run addresses to the bundle is under "To the
     deliverer", at the foot. What stays here is the learner's own,
     or waits on a later trigger. -->

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

- <issue>. Accepted because <reason>. Revisit at <step / condition>.

## To the deliverer

<!-- What this run addresses to the bundle, and only that, so a
     reading looks in one place (delivered-copies.md, rule 4). A
     line leaves when a note answers it. Last answered: the note of
     2026-09-30, read through 9869798. -->

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
      here.
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
- [ ] Information, not a request: `decide-first` and
      `option-comparison` were used here before they were withdrawn.
      `visual-comparison` now calls the general method "discarded
      2026-09-24, unused". In this run's writing pass
      (2026-09-21..23), `decide-first` showed the commit count was
      not yet sayable, and `option-comparison` built five wordings
      of SL-2's G1 and caught that its labels were already there —
      the finding the pass turned on.
