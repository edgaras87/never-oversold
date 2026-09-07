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
