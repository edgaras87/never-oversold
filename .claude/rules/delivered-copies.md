---
paths:
  - ".claude/skills/**"
  - ".claude/rules/**"
  - "docs/concept/**"
  - "temp/**"
foundation: the exchange convention
---

# Delivered copies: pinned, edited in place, re-pinned

<!-- What this run holds from its deliverer, and what it may do to
     it. The deliverer's side of the same exchange is not here; the
     run never needs it. -->

1. **Every file delivered here is a copy pinned at the deliverer's
   commit.** The decisions log's last delivery entry carries two
   numbers:
   - the **pin:** the deliverer's commit these copies equal, taken
     from the staging's name;
   - the **read-through:** this run's own commit the deliverer last
     read up to, taken from the note.

   One pin covers everything delivered: the skills and rules under
   `.claude/`, and `docs/concept/`. The concept chapters are copies
   too, edited like any copy, but only from a mistake lived here.
   Skills are written from them and readers learn the method from
   them, so lived work can show one wrong. A preference for other
   wording is never the reason. The records (`PLAN`, `TODO`, the
   devlog) were delivered once as stubs, and the entry file was
   composed once. All of them are this run's own, not copies, and
   this file does not govern them.

   **A copy is never renamed or deleted here.** Its delivered path
   is its identity for the take. A renamed copy becomes two files at
   the next delivery, and a deleted one comes back. A copy this run
   has no use for gets a line under *To the deliverer*: *we do not
   use this*. The deliverer decides.

2. **A copy may be edited in place during a step when all three
   hold:**
   - something happened in this project that the copy did not
     foresee, never a speculation;
   - the edit asks a question or demands an outcome any project
     would want, never this project's answer, never anything
     project-specific;
   - one entry goes in `.claude/decisions.md` per edit, or per copy
     per step, saying what changed, which step found it, and why.

   Nothing is written into the copy but the edit itself. A comment
   in an artifact says how to use it or what a part is, never what
   changed: that is history, and the diff against the pin and the
   log already hold it.

3. **What this project needs that no copy should carry goes into
   the project's records,** where that kind of thing already lives:
   a gate item in PLAN, an ADR, a slice record, the entry file. The
   copy stays general.

4. **At every step's close, the hand-off.** One TODO line per edited
   copy: "deliverer: evaluate this run's changes to `<copy>` since
   `<pin>`". The lines go under one heading kept for the purpose,
   `## To the deliverer`. A rewrite of another section then cannot
   take them. The deliverer reads one section instead of the whole
   backlog. A copy edited after this run finished with it (rule 7)
   adds "— untried here: written from this run's evidence, never
   run", so the deliverer reads those hunks hardest.

   The line is the request. Without it, the deliverer finds a
   changed file and must guess. The edits describe themselves in the
   diff and the log.

   The deliverer reads this repo read-only when it reads it: at a
   hand-off, or at the retrospective, not at every step's close. A
   faster answer needs a hand-off document. The deliverer diffs the
   copy against its own tree at the pin, takes, reshapes or declines
   each hunk in its own words, and writes its reply. The line is done
   at the re-pin.

5. **After the reply, the re-pin: the take.** The deliverer's new
   version arrives staged in this repo's own `temp/`, in a directory
   named for the hash, with the note beside it. **Nothing in `temp/`
   is in force until it is copied into place.** A staged skill is a
   file, not a skill, and a step that opens meanwhile runs on the held
   copy.

   In this order, before anything moves:
   1. **Check the note against the staging.** Diff every delivered
      file against what this run holds. The note says how many
      differ, and the diff says how many. A mismatch is reported
      first.
   2. **Find this run's own edits.** Diff each held copy against the
      last delivery entry's commit, and read the note's verdict on
      each.
   3. **An edit with no verdict was made after the read-through the
      note names.** The copy lands whole, and that edit is re-applied
      on top. Its log entry and its TODO line stand until a later
      note answers the edit. If the edit was made to a path the note
      names as gone, there is no copy to land on, and nothing is
      re-applied. The log entry and the TODO line stand, the diff is
      in this run's history, and the next note answers them.
   4. **Copy whole.**
   5. **Remove what the note says is gone.** Each path it names as
      deleted is deleted, and each it names as renamed is moved. A
      copy cannot carry an absence, so the note carries it and this
      step does it.
   6. **Remove the staging,** the directory named for the hash and
      its note, and nothing else. `temp/` may hold the project's own
      drafts, which are not the delivery's to clear.
   7. **Write one decisions entry** carrying both numbers: the new
      pin, and the read-through the note names. The copy is pristine
      again, and the TODO line leaves with the pin.

   The take's commits follow `.claude/skills/commit-messages/`. When
   there is more than one, the chapters' commit comes first, and the
   entry goes in the last, where every copy equals the pin. A
   declined edit is gone with the re-pin, and never edited back in.
   If the project still needs what was declined, that need goes into
   the records under rule 3, and the decisions entry says so.

   **A note may arrive alone,** with no files: *read through
   `<commit>`, nothing to answer.* The entry records the
   read-through, the pin does not move, and nothing else happens.

6. **If a step opens before the reply,** work continues on the
   edited copy, and edits keep landing under rule 2. They stack on
   this side only. The deliverer diffs against the pin either way,
   and the re-pin still lands whole. Being behind the deliverer is
   acceptable. Being behind this run's own lessons is not.

7. **A copy this project has finished with is edited the same way,
   whenever the evidence is here.** It is never kept as a note for a
   later run, for three reasons:
   - a restarted step may re-run on it;
   - the deliverer takes or declines an exact hunk, not a translated
     note;
   - one process serves every copy.

   The one difference: the problem was lived either way, but a
   finished copy's fix is never used by a later step here. A running
   skill's fix is exercised by the next step. A finished skill's fix
   is first used by the deliverer. Say so in the log entry, and in its
   hand-off line (rule 4).

The pin is the truth of origin, and the log is the why. The diff
between the pin and the copy is the whole of what this run changed,
and that is what the deliverer reads.

---

## Decisions

- CBC ADR-0036 — the exchange. This rule is the run's half of it,
  built from the seven rules never-oversold wrote for its own method
  skills and widened to every delivered copy; the pin and the
  read-through; what a copy cannot carry; the check at the take
- CBC ADR-0046 — the take's commits: the chapters first, the entry
  in the last
