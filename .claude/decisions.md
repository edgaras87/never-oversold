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
