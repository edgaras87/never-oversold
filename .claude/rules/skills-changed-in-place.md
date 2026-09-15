---
paths:
  - ".claude/skills/**"
---

# The method skills: pinned copies, edited in place, re-pinned

<!-- Arrived 2026-09-14 after SL-1, settled 2026-09-15 (decisions.md,
     both dates). The source owns these skills; this run may correct
     them from lived work so the next step runs on the correction,
     and the source evaluates a diff, not a note. `docs/concept/`
     stays pinned: theory is not a run's to correct. -->

1. **Every skill is a copy pinned at a source commit** — the hash in
   the decisions log's last entry for that source. The concept
   chapters are copies too, and are never edited here.

2. **A skill may be edited in place during a step when all four
   hold.** Something happened in this project that the skill did not
   foresee — never a speculation. The edit asks a question or demands
   an outcome any project would want — never this project's answer,
   never anything project-specific. A dated line in the skill's own
   header comment says what changed and which step found it. One
   entry in `.claude/decisions.md` per edit, or per skill per step.

3. **What this project needs that no skill should carry goes into
   the project's records** where that kind of thing already lives —
   a gate item in PLAN, an ADR, a slice record, the entry file. The
   skill stays general.

4. **At every step's close, the hand-off.** One TODO line per edited
   skill: "source: evaluate this run's changes to `<skill>` since
   `<pin>`". The line is the intent — without it the source finds a
   changed file and must guess; it is the close's checklist of
   replies owed; and it is done at the re-pin. The edits describe
   themselves in the header and the log; the line only says evaluate.
   The source reads this repo read-only, diffs the copy against its
   own tree at the pin, takes or declines each hunk in its own words,
   and writes its reply.

5. **After the reply, the re-pin.** The source's new version is copied
   whole; one decisions entry carries the new hash; the copy is
   pristine again; the TODO line leaves with the hash. A declined
   edit is gone with the re-pin — never edited back in. If the
   project still needs what was declined, that need goes into
   records per rule 3, and the decisions entry says so.

6. **If a step opens before the reply**, work continues on the edited
   copy, and edits keep landing under rule 2. They stack on this side
   only; the source diffs against the pin either way, and the re-pin
   still lands whole. Behind the source is acceptable; behind this
   run's own lessons is not.

7. **A skill this project has finished with is never edited.** A
   lesson about it is a prose hand-off in TODO, since an edit would
   serve nobody here.

The pin is the truth of origin, the header lines and the log are the
whole of what this run changed, and the diff between them is what
the source reads.
