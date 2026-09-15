---
paths:
  - ".claude/skills/**"
---

# The method skills: changed in place, under guards

<!-- Arrived 2026-09-14, after SL-1 (decisions.md, that date). The
     source owns these skills; this run may correct them from lived
     work, so the next slice runs on the correction and the source
     evaluates a diff, not a note. `docs/concept/` stays pinned:
     theory is not a run's to correct. -->

An edit to a skill here is legal only when all five hold:

1. **Lived, not speculated.** Something happened in this run that
   the skill did not foresee. A possibility stays a TODO note.
2. **A question or an outcome, never this run's answer.** "The plan
   presents owner candidates as options" — not "use a check
   constraint". The edit must hold for a run on another stack, with
   another problem.
3. **Logged in the skill's own header:** a dated line — what changed,
   which step found it, in one or two sentences — appended to the
   provenance comment every skill carries at its top.
4. **Logged in `.claude/decisions.md`:** one entry per edit or per
   skill-per-step — what, why, what was rejected (usually: leave the
   copy and file a note); status *awaiting the source*.
5. **Handed to the source as a diff.** TODO Later carries one line
   per skill: "source: evaluate this run's changes to `<skill>`
   since the pin". The source diffs the copy against its pin, reads
   the log entries, takes or declines each hunk; the verdict comes
   back as one line in the decisions log. A declined edit stays here
   as this run's local version, its decline recorded.

The pin is still the truth of origin: every skill's header names the
source commit it came from, and the diff from that pin is the whole
of what this run changed. "Which version of the method produced this
run" is answered by the pin plus the log, both in git.
