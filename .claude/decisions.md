# Agent decisions

<!-- The working arrangement's decision log (HANDBOOK ADR-0020,
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
  already given in chat (HANDBOOK ADR-0035, decision 2's note). The
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

- 2026-09-14 The method skills under .claude/skills/ may be
  changed in place from lived work, under five guards written in
  .claude/rules/skills-changed-in-place.md: lived not speculated;
  a question or outcome, never this run's answer; logged in the
  skill's header and here; handed to the source as a diff, one
  TODO line per skill; the source's verdict logged back. The
  concept chapters stay pinned. Decided with the reviewer after
  SL-1 closed with twenty prose hand-offs in TODO.
  Why: a diff is a precise hand-off a source can evaluate, a note
  is a guess; the next slice runs on the corrected skill instead
  of the one already known thin; the source may still decline, and
  the run keeps its local version with the decline recorded.
  Rejected: the pinned-copy rule as it stood (twenty notes cold by
  the retrospective, the same thin skill run three more times);
  folding back per skill as each finishes (keeps the source the
  only editor, but the slice skill would still run three times
  unimproved); editing the concept too (theory is not a run's to
  correct — a chapter wrong in practice is a hand-off, not an
  edit).

- 2026-09-15 The correctness-by-construction bundle updated
  @ 7bbf49a, from 57cf22f, by copying anew from its checkout at
  ~/PycharmProjects/engineering/concept-garden/correctness-by-
  construction (starter/bundle/): 28 files across the five method
  skills — cbc-framing (SKILL, workflow, the-whole-system-in-plain,
  worked-example, the registry template), cbc-bootstrap (SKILL,
  app-structure, the walkthrough, the harness reference, the pom
  convention, the three templates), cbc-slice (SKILL, workflow,
  system-readiness, worked-example), infra-establish (SKILL, the
  walk, the role split, the setup walkthrough, the six templates),
  infra-serve (SKILL). Compare-first: every copy here was
  byte-identical to the pin, so nothing local is overwritten; no
  header line of this run's is added — the bundle's own harvest
  lines, dated 2026-09-07 to 2026-09-15 and naming this run, are
  the record of what was taken. docs/concept/ untouched: the
  bundle's concept did not change.
  Why: the bundle took seventeen of this run's hand-offs and four
  lessons the run never filed, in twenty-six harvest commits since
  the pin, and none of it had come back; the in-place rule adopted
  2026-09-14 would have forked every one of those files at the
  first edit. The copy lands before any edit, so the run's copies
  are the bundle's and the next slice runs on the harvested skill.
  Rejected: applying the bundle's per-step delivery diffs (five,
  in its temp/, overlapping on the harness reference — each cut
  from a different base; a whole copy is the convention's own
  mechanism); waiting for the retrospective's re-pin (three more
  slices on a skill already known thin); copying only cbc-slice
  (a partial pin answers "which version" with a list).

- 2026-09-15 The kit updated @ 20b1bc8, from af16eb7, through the
  receipt branch kit-20b1bc8 (one commit over kit-af16eb7: the kit
  at 20b1bc8, `git diff kit-af16eb7 kit-20b1bc8` the upstream change
  isolated — six files). Taken: artifact-kinds, change-plans,
  commit-messages, convention-lifecycle — copies overwritten,
  compare-first finding each identical to the kit at af16eb7 (no
  local edit); what changed upstream: every convention rewritten
  from the consumer's seat, every citation tagged HANDBOOK ADR-nnnn
  (HANDBOOK ADR-0037), commit-messages' delivery now `pushed` with
  the gate withdrawn (HANDBOOK ADR-0035 amended 2026-09-10, on this
  run's report), artifact-kinds' exemplars by role (this run's
  finding), convention-lifecycle §8 naming the born-without case.
  Requires-chains unchanged. Installed conventions —
  project-recording, repo-hygiene, agent-arrangement — verified:
  the kit's stubs carry no changed comment since af16eb7; the one
  stub change is the README's decisions row gaining the tag slot
  (HANDBOOK ADR-0037), landing project-side in its own commit with
  this repo's tag. The kit's settings.json deletion lands as
  nothing: rejected here at af16eb7. Two hand-written citations in
  this log retagged HANDBOOK, per the convention received.
  Why: the kit had moved twenty-eight commits past the pin, two of
  them on this run's own hand-offs, and SL-2 opens on these
  conventions at every commit; the receipt branch is the compare
  the handbook asked this run to use once and report on — used,
  it worked: one diff shows the upstream side, one empty diff shows
  no local edit.
  Rejected: copying without the receipt (the handbook's TODO waits
  on exactly this trial); a change-plan for the two-commit landing
  (§8 step 3's letter — the plan costs two commits for a set of
  two; filed as friction for the handbook, not absorbed); retagging
  citations in the project's records (none cite another repo's
  decision; HANDBOOK ADR-0037 rule 4, records that never leave stay
  bare).

- 2026-09-15 The in-place rule of 2026-09-14 settled as seven rules
  in .claude/rules/skills-changed-in-place.md, after the bundle and
  kit re-pins were lived: a skill is a copy pinned at a source
  commit; edited in place only from lived work, as a question or
  outcome any project would want, never project-specific, with a
  header line and a log entry; what the project alone needs goes
  into its records, never the skill; at every step's close one TODO
  line per edited skill asks the source to evaluate since the pin;
  after the reply the source's version is copied whole and the pin
  moves, a declined edit gone with it and never edited back; a step
  opening before the reply continues on the edited copy; a finished
  skill is never edited, its lessons prose hand-offs.
  Why: not editing costs twice — behind the source and behind this
  run's own lessons — while editing and re-pinning costs once; the
  compare stays a diff against one pin because nothing project-
  specific ever enters the copy; a declined edit kept would make
  the copy project-specific in fact and compound at every re-pin.
  Rejected: an overlay file per skill read beside the pinned copy
  (holds project-specific behaviour and survives a slow source, at
  a second file per skill and a prune at every re-pin — machinery
  for a need no step has met; the named fallback if declined-but-
  needed ever becomes a pattern); keeping declined edits with a
  logged reason (compounds); the pinned-copy rule as born (twenty
  cold notes at SL-1's close).
