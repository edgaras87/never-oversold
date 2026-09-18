---
paths:
  - ".claude/skills/cbc-*/**"
  - ".claude/skills/infra-*/**"
---

# The bundle's method skills: pinned copies, edited in place, re-pinned

<!-- Arrived 2026-09-14 after SL-1, its seven rules written
     2026-09-15, narrowed 2026-09-16 (decisions.md, those dates).
     The four conventions are no longer this file's: the handbook
     took the rules into convention-lifecycle §3 step 4 (HANDBOOK
     ADR-0038, at handbook ba7eaa4, delivered here by the bundle
     @ c3a3d97), and that copy governs them — read it there. This
     file stands only for the bundle's five method skills, which
     the handbook left to the bundle; the bundle answered
     2026-09-17 (decisions.md, that date) by taking these seven
     rules as they stand here, so the file stays and
     this text is the one that governs them. In force here and on
     trial; provisional in the same sense the handbook's text is:
     no edit has yet gone through a re-pin.
     Outside it: `docs/concept/` (theory; the skills are derived from
     it upstream, no step here runs a chapter, its lessons are prose
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
   header comment says what changed and which step found it. There
   is no header block to join: the bundle deleted its own, and the
   copy without them is the one this run holds (taken 2026-09-18).
   So the block is this run's alone, with no counterpart upstream,
   and rule 5 takes it away as it always did. One entry in
   `.claude/decisions.md` per edit, or per skill per step.

3. **What this project needs that no skill should carry goes into
   the project's records** where that kind of thing already lives —
   a gate item in PLAN, an ADR, a slice record, the entry file. The
   skill stays general.

4. **At every step's close, the hand-off.** One TODO line per edited
   skill: "source: evaluate this run's changes to `<skill>` since
   `<pin>`". The line is the request — without it the source finds a
   changed file and must guess; the edits describe themselves in the
   header and the log. The source reads this repo read-only when it
   reads it — at a hand-off, or at the retrospective — not at every
   step's close; a faster answer needs a hand-off document. It diffs
   the copy against its own tree at the pin, takes, reshapes or
   declines each hunk in its own words, and writes its reply. The
   line is done at the re-pin.

5. **After the reply, the re-pin.** The source's new version arrives
   staged in this repo's own `temp/` — a directory named for the
   hash, the reply beside it — is read and diffed there against
   this run's own delivery commit, then copied whole and the
   staging deleted; one decisions entry carries the new hash; the
   copy is pristine again; the TODO line leaves with the hash. A
   declined edit is gone with the re-pin — never edited back in. If the
   project still needs what was declined, that need goes into
   records per rule 3, and the decisions entry says so.

6. **If a step opens before the reply**, work continues on the edited
   copy, and edits keep landing under rule 2. They stack on this side
   only; the source diffs against the pin either way, and the re-pin
   still lands whole. Behind the source is acceptable; behind this
   run's own lessons is not. The first edit that goes through a
   re-pin is reported to the handbook as one TODO line — its text is
   provisional on that report.

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
