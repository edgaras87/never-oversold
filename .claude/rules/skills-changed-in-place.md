---
paths:
  - ".claude/skills/**"
---

# The skills — method and convention alike: pinned copies, edited in place, re-pinned

<!-- Arrived 2026-09-14 after SL-1, its seven rules written
     2026-09-15 (decisions.md, both dates). In force here and on
     trial: the lifecycle of copies is the handbook's, the rule is
     handed to it as a proposal (TODO Later), and the handbook's
     answer governs at the next kit update — taken, this file goes
     redundant; declined or reshaped, this file follows the answer
     or stands as a logged local layer until the retrospective.
     The source owns these skills; this run may correct them from
     lived work so the next step runs on the correction, and the
     source evaluates a diff, not a note.
     Scope: every copy under .claude/skills/ — the bundle's five
     method skills with their references and templates, and the
     kit's four conventions — each against its own source. Outside
     it: `docs/concept/` (theory; the skills are derived from it
     upstream, no step here runs a chapter, its lessons are prose
     hand-offs); the stubs and fills — the entry file, the records'
     stub comments, the ignore files, PLAN's vendored steps — which
     are this run's own records, edited under project-recording and
     folded back by name at the retrospective. -->

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

7. **A skill this project has finished with is edited the same
   way.** A restarted step may re-run on it, the source takes or
   declines an exact hunk rather than a translated note, and one
   process serves every skill. The one difference: the problem was
   lived either way, but the fix's wording is never used by a later
   step here — a running skill's fix is exercised by the next step,
   a finished skill's fix is first used by the source. Say so in the
   header line.

The pin is the truth of origin, the header lines and the log are the
whole of what this run changed, and the diff between them is what
the source reads.
