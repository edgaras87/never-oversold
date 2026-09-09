# Agent decisions

<!-- The working arrangement's decision log (handbook ADR-0020,
     provisional). Append-only, newest last. One entry per
     arrangement decision — a skill added or changed, a rule tuned,
     a workflow adopted. Three lines: what, why, what was rejected.

     Division of labor: the standing rule rides as a comment in the
     artifact it governs — this log keeps the why and the rejected
     options, and neither repeats the other. Commit bodies stay
     ordinary commit bodies.

     This file is agent-side: a commit touching it is scoped `agent`
     and touches nothing else (the commit-messages skill carries
     that rule).

     At the project retrospective, read top to bottom: each entry
     graduates to the handbook, stays local, or dies.

     The two placeholders in the birth entry below — the date and
     the "@" hash — are replaced at copy time by the install block
     in the handbook's manual. The hash pins which handbook state —
     and so which version of every convention — this project was
     born from (convention-lifecycle §7). If either still shows a
     placeholder, the install block was not run from the handbook;
     fix it before the bootstrap commit. -->

- 2026-09-07 Born from the engineering-handbook starter kit
  @ c670fe5.
  Conventions: project-recording, commit-messages, repo-hygiene,
  artifact-kinds, change-plans, convention-lifecycle,
  agent-arrangement.
  Why: handbook defaults.
  Rejected: none — see the handbook's ADRs.

- 2026-09-07 The correctness-by-construction bundle installed
  @ 57cf22f: docs/concept/ (five chapters), the five method
  skills (cbc-framing, cbc-slice, cbc-bootstrap, infra-establish,
  infra-serve, with their references and templates), PLAN's steps
  (cbc-run-pure v4), the entry-file fills. Pinned copies: changed
  only by copying anew from the source, each copy logged here.
  Why: this run practices the method; the kit knows nothing of it.
  Rejected: the handbook's default playbook as PLAN's step source
  (the concept's run playbook carries the method's order; only
  its Release step is vendored in); editing any copy in place
  (the source owns them — lessons fold back at the retrospective).

- 2026-09-07 Standing rule added to PLAN, above the steps: one
  branch per step, cut from main, fast-forward merged after the
  gate closes on the reviewer's word; a restart renames the old
  branch and cuts a new one. On trial from Step 1; fold-back
  filed in TODO Later.
  Why: a step's work lands on main only as a reviewed, gated
  unit; fast-forward keeps main linear and the step's own commits.
  Rejected: committing on main as Step 0 did (the birth had no
  reviewer between it and main); merge or squash commits (either
  hides the step's commit trail from main).

- 2026-09-09 The kit updated @ af16eb7, from c670fe5, through the
  receipt branch kit-af16eb7 (one commit against the seed: the kit
  at the two pins). Taken: commit-messages, change-plans,
  convention-lifecycle — skill copies overwritten, compare-first
  finding each identical to c670fe5; agent-arrangement — the entry
  file's guard comment, tests 2 and 3; repo-hygiene — the ignore
  line for CLAUDE.local.md, landed in aa9b3ec, the settings comment
  keeping "(if any)" since the file below is rejected. Unchanged:
  artifact-kinds, the project-recording stubs (placeholders only).
  Why: the pace rule and §8's installed path reached the handbook
  after the birth pin; the registry names the hash the records hold.
  Rejected: the kit's tracked .claude/settings.json holding the
  commit ask rule — the reviewer holds the pace through
  CLAUDE.local.md and the commit-messages rule, and the handbook
  withdrew the same rule in its own checkout for asking a word
  already given in chat (its ADR-0035, decision 2's note). The
  receipt branch keeps the file so the compare stays faithful.

- 2026-09-09 The entry file moved to .claude/CLAUDE.md, content
  untouched; the harness reads either address as one file. On
  trial from Step 1.
  Why: every agent-side file then sits under .claude/, and the
  handbook's kit keeps the root address until a born project
  reports on the move — this run is that report.
  Rejected: leaving it at the root as the kit's default (agent-
  arrangement now permits either; tidiness alone earned no change
  in the handbook, so the trial is what earns it here); renaming or
  restating any mention — records name the file, none its path.

- 2026-09-09 CLAUDE.local.md exists at the root, ignored, holds the
  reviewer's pace. The operator's, not the project's: its words
  enter no record, and no project file derives from it. On trial
  from Step 1.
  Why: the harness loads it beside the entry file and reads it the
  same way; one person, one checkout, so the arrangement's reply to
  a repeated request lives there rather than in a tracked rule.
  Rejected: the kit's settings-file gate for the same pace (the
  entry above); restating its text in a record (a record that
  quotes it lets one operator's preference into the project's
  truth).
