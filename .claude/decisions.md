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
     born from (convention-lifecycle §2). If either still shows a
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

- 2026-09-15, later Rule 7 of the seven withdrawn as a prohibition
  on the reviewer's argument, rewritten as a caveat: a finished
  skill is edited the same way. Why: leaving it unedited buys a
  pristine copy the whole re-pin gives anyway; editing buys a
  possible re-run of a restarted step on the corrected text, an
  exact hunk for the source to take or decline instead of a
  translated note, and one process for every skill. What differs
  is only that the fix's wording is never used by a later step here
  — the problem was lived either way — said in the rule. Rejected: two processes by kind of skill (a rule people
  forget).

- 2026-09-15, later The in-place rule's scope stated: every copy
  under .claude/skills/, the kit's four conventions as much as the
  bundle's five method skills, each against its own source. Outside
  it: docs/concept/, the stubs and the fills.
  Why: a convention copy is the same object as a method skill — a
  pinned copy read at a moment, re-applied or dropped at update by
  the handbook's own §8 — and two processes by kind of skill is what
  rule 7 rejected; the concept is theory the skills derive from
  upstream and nothing here runs it; the stubs and fills are already
  this run's records under project-recording, folded back by name.
  Expected: the handbook declines convention edits more often than
  the bundle declines skill edits — it reads for a rule across runs
  — and rule 5 absorbs that.
  Rejected: method skills only (a second process for the
  conventions); the concept in scope (a fork of the theory the
  skills come from, exercised by nothing here).

- 2026-09-15 The kit updated @ 9e28143, from 20b1bc8, through the
  receipt branch kit-9e28143 (one commit over kit-20b1bc8, the
  upstream change one file). Taken: convention-lifecycle — the
  copy overwritten whole, compare-first empty (no local edit on any
  of the four since 20b1bc8); what changed upstream, the handbook's
  answer to this run's hand-off of 2026-09-15 (HANDBOOK ADR-0038):
  §8 step 4 gains "a project may edit its copy between two pins" —
  this run's rules 1–3, 5, 6 and 7 in the handbook's own text,
  provisional until one such edit has gone through an update; the
  receipt branch adopted as the compare when a project holds one,
  with this run's trap; step 3's letter bent for a two-commit
  landing; the skill's description gains the trigger "when a copy
  under .claude/skills/ turns out wrong mid-step". Reshaped, not
  taken: rule 4's cadence — the handbook reads the TODO line at a
  handoff or the retrospective, not at every step's close; a run
  that needs a faster answer sends a handoff. The other three
  conventions unchanged; the installed three verified, no stub
  changed.
  The rules file's fate, as the handbook asked: for the four
  conventions .claude/rules/skills-changed-in-place.md is redundant
  from this pin — the convention itself governs them, and the
  handbook ships no rules file (ADR-0038, option 6). The file
  narrows to the bundle's five method skills, which the handbook
  left to the bundle, until the bundle answers; its rule 4 reworded
  to the handbook's cadence so the two do not disagree. Owed to the
  handbook whenever it happens: the first edit of a copy that goes
  through a re-pin, one TODO line — ADR-0038's decision 5 waits on
  it.
  Why: the answer arrived in the kit, and a pin that stays behind
  it leaves the registry lying (§8 step 4's own warning).
  Rejected: keeping the rules file in force for the conventions
  beside the convention (two texts for one rule); deleting it
  outright (the bundle's skills still have no source text for it).

- 2026-09-17 The bundle answered this run's in-place hand-off (CBC
  ADR-0007, the harvest section), read against the pin this run
  still holds @ 7bbf49a. Taken as written: the seven rules of
  .claude/rules/skills-changed-in-place.md stand in this run's own
  words, rule 7's reversal included, and the bundle's harvest now
  says a change may arrive already made — it diffs the copy against
  the pin and takes, reshapes or declines each hunk. Nothing this
  run holds changes; no edit has fired yet.
  Declined, and replaced: half the channel this run offered — the
  verdict as the header line the run sees at its next copy. Every
  provenance and harvest header in the bundle's skills, references
  and templates is deleted; from the next copy on a file opens on
  instruction only. The verdict arrives instead as a document,
  written after reading this repo and staged beside the files it
  governs: not a path into the bundle's tree — this run cannot read
  it and should not, since it holds the bundle's readings of this
  run's work — but the next version staged in this repo's own
  temp/, in a directory named for the hash, with the note beside
  it; read and diffed there against this run's delivery commit,
  then taken whole and the staging deleted.
  Held, not declined: the infra-establish facility paragraph (TODO
  Later). One instance is not a shape; the trigger the bundle
  watches for is a second run reaching the same gap unprompted, and
  SL-2 leaning on this one counts as evidence.
  This run's own two calls: the rules file keeps its seven rules
  unreworded, gaining only what the answer made true — the file's
  fate settled, rule 2's line now having no counterpart upstream,
  rule 5's copy arriving staged; and the note itself stays out of
  history (temp/ is ignored), its verdict carried here and in TODO.
  Why: the bundle took the rules because they were the clearer
  statement, so restating them in the answer's words would trade an
  exact text for a translation — the same argument rule 7 was
  reversed on; and a verdict absorbed into the records it changes
  is read, while a verdict filed whole is a second text that ages.
  Rejected: rewriting the rules file now for the header-less master
  (that copy has not arrived — the pin is still 7bbf49a, and a rule
  describing files this run does not hold would lie until the
  re-pin); committing the note under docs/ as the record (a second
  text for a verdict already absorbed, and the bundle's own record
  of it is better).

- 2026-09-18 The bundle updated @ c3a3d97, from 7bbf49a — both
  halves in one copy, the four conventions now arriving through this
  channel rather than the handbook's, at handbook ba7eaa4. Staged in
  temp/ with its note per rule 5, read and diffed there against the
  delivery commit 0e3ea72, then taken whole and the staging deleted.
  Compare-first empty on both halves: no local edit on any of the
  nine copies since its pin, so nothing here was overwritten.
  The method half: 28 files, every provenance and harvest header
  deleted, a file now opening on its first instruction. No rule
  changed — checked rather than taken on the note's word, by
  stripping comments from both sides: every non-comment line in the
  five skills is identical. docs/concept/ is unchanged and was not
  re-copied.
  The convention half: rules kept, the explanation behind each moved
  into a manual that never ships to a project; four files, each about
  a third shorter. convention-lifecycle is renumbered from nine
  numbered sections to three, its step numbers unchanged, so the
  procedure this repo cites as §8 step 4 is §3 step 4 — the
  handbook-authoring sections are the ones gone, and its description
  and requires narrow with them. Three citations went stale, not the
  two the note named: the rules file's header, this repo's CLAUDE.md,
  and a TODO line.
  Byte-identity to the handbook @ ba7eaa4 is the bundle's claim and
  is not checked here — this repo holds no handbook checkout, and the
  receipt kit-9e28143 proves only that this repo has not edited its
  copies. The entry names both hashes so the claim is attributed.
  No receipt branch cut for this delivery. The receipt exists for the
  kit, which ships stubs whose local content is the record and cannot
  be diffed against a master; a skill copy is pristine by rule 1 and
  its compare is the delivery commit, which is what rule 5 names and
  what was used here. The kit's next receipt carries four fewer
  files — the conventions having moved channel, not been deleted.
  Why: one source, one procedure and one hash to carry, which the
  four conventions' own convention already permits — the handbook may
  be "a checkout on disk or the payload a handoff carries", and this
  is that payload. And a pin that stays behind leaves the registry
  lying (§3 step 4's own warning).
  Rejected: keeping the conventions on the handbook's channel (two
  channels, two procedures and two hashes for files that arrive in
  one directory); cutting a bundle-c3a3d97 receipt (a compare this
  repo already has in its own history, and a second receipt line to
  keep); re-copying docs/concept/ unchanged (five files in the diff
  that the delivery did not change).

- 2026-09-20 The bundle updated @ 6f2be1d, from c3a3d97 — the
  convention half only. Staged in temp/ with its note, read and
  diffed there against the delivery commit aeef417, then taken whole
  and the staging deleted at the set's close. Compare-first empty:
  `git diff aeef417 -- .claude/skills` returns nothing, so no local
  edit is overwritten. The method half and docs/concept/ are
  byte-identical to the staging and are not re-copied.
  Taken: seven conventions. Four held before — artifact-kinds
  unchanged; commit-messages one line, the plan artifact's name;
  convention-lifecycle's requires line, §2's registry bullets and
  six places in §3 saying "the deliverer" where they said "the
  handbook" — this run's third gap of 2026-09-18 answered, and the
  birth-entry bullet now asking for a composing source's hash, this
  run's practice made rule; change-plans renamed commit-plan, its
  artifact COMMIT-PLAN.md, and a new opening paragraph: it plans the
  commits, not the change, the what settled before the plan opens.
  Three new, the bundle's own with no handbook ancestor: decide-
  first, option-comparison, visual-comparison — methods, not rules
  over anything held here. The change-plans directory deleted by
  name in the same commit, the one step a copy cannot do. The plan
  in flight renamed with its convention; the entry file's records
  table names the new artifact.
  The pin is the bundle's 6f2be1d, one hash for the seven. The
  handbook's ba7eaa4 stands as provenance for the four that came
  from there, in words, not as a second pin: three of the seven have
  no handbook ancestor, and the bundle owns all seven now.
  Why: two channels and two hashes cannot name a set that one
  deliverer composes; and a pin that stays behind leaves the registry
  lying (§3 step 4's own warning). SL-2 opens on these conventions at
  every commit.
  Rejected: recording ba7eaa4 as a second pin (a hash for four files
  standing beside a hash for seven, with three files under neither);
  leaving change-plans in place for the harness to disambiguate (it
  loads whichever it meets first); keeping the in-flight plan under
  the old artifact name until its close (four boundaries where the
  convention in force names a file the root does not hold).

- 2026-09-20 Four convention copies edited in place, this run's
  first: commit-plan, commit-messages, convention-lifecycle,
  option-comparison, five lines saying "change-plan" after the
  rename to commit-plan — the template heading, the agent-files
  section, §3 step 3 and the ADR-0038 line, §4's first entry. One
  TODO line asks the bundle to evaluate since 6f2be1d. Under
  convention-lifecycle §3 step 4 as received, provisional until it
  has gone through an update; the report owed to the handbook
  (ADR-0038, decision 5) is armed by this edit and fires at the next
  re-pin. Decided with the reviewer.
  The dated header line the rule asks for is not written, on the
  reviewer's argument: a comment in an artifact carries how to use
  it or what a part is, never what changed — that is history, and
  history here has two homes already, `git diff 1b0859a --
  .claude/skills/<name>` for the exact hunks and this entry for the
  why. A third copy in the file is the explanation-in-the-artifact
  both sources have been stripping out since 2026-09-17. The same
  line in rule 2 of .claude/rules/skills-changed-in-place.md, this
  run's own text, is dropped in the same set so one process serves
  every skill; the hand-off asks the bundle to drop it from both.
  Why: a copy that contradicts its own rename is an outcome any
  project would want fixed, and five words are the cheapest first
  run of the machinery the rule is provisional on. The edit lands
  after the take so the delivery commit stays the pristine compare
  and each file's edit is one exact hunk.
  Rejected: sending the line without the edit (leaves the copies
  contradicting the convention's own table until the next re-pin, a
  cost the reviewer chose not to carry); folding the edit into the
  take (the compare would then never be empty against a pristine
  delivery); writing the header line as the rule says and reporting
  it as noise afterwards (a deviation lived is a finding, a rule
  followed against the reviewer's argument is not).

- 2026-09-20, later The bundle updated @ 4c3ac99, from 6f2be1d —
  one file, convention-lifecycle, its §3 step 4 paragraph on editing
  a copy: the dated header line gone, this run's argument written in
  as the rule's reason (CBC ADR-0034), the three records that remain
  named — the decisions entry, the TODO line, the diff against the
  delivery commit. Staged whole in temp/ with its note; checked, not
  taken: the method half, docs/concept/ and six conventions are
  byte-identical to what main held, so `diff -rq` returned that one
  file, as the note said it would. The five-noun edit of 67e913e is
  in the bundle's masters in this run's wording — the three other
  edited copies match the staging byte for byte — so this run's
  first in-place edit went through a re-pin and was taken whole,
  which is the trial both texts were provisional on. All seven
  copied whole; git shows one.
  The map corrected, on the bundle's word, which this repo cannot
  check: the handbook has not been this project's upstream since
  2026-09-18 — the bundle owns the kit, the conventions and the
  models, and nothing here tracks the handbook by reference, pin or
  owed report. The rules file's header and rule 6 said otherwise
  and now do not; the TODO's three handbook items are cleared in
  the project-side commit. Nothing changes in practice: no file
  here came from the handbook after the birth kit, and the
  conventions have come through the bundle since 2026-09-18.
  Why: a copy carrying a clause this run refused, beside a master
  that dropped it on this run's argument, is the pin lying in the
  other direction; and a record that names a channel that no
  longer exists sends the next report nowhere.
  Rejected: taking only the one file (rule 5 copies whole; the six
  identical copies cost nothing in the diff); keeping the rules
  file's "provisional" (the trial it waited on has run, and its
  finding is in the rule).

- 2026-09-20 Second standing rule added to PLAN, beside the branch
  rule: a gate item is ticked the moment it is verifiably true, the
  step's marker stands at `[~]` from the first tick to the last,
  and a tick records a verification rather than a finality — an
  item rewritten mid-step keeps its tick only if what was verified
  still satisfies the new words, and loses it in the devlog's words
  if not. On trial from Step 6; fold-back filed in TODO Later.
  Why: through SL-1 the gate read all-unticked while the step ran,
  which is the same lie the registry told by reading `chosen-next`
  through that whole build — the lesson this run harvested and the
  bundle wrote back into the slice skill at 7bbf49a. A record that
  cannot say where the work is, is not the source of truth for it.
  The clause exists because ticking invites "settled, don't
  reopen": it names the tick as a record of one verification, so
  rewriting an item stays ordinary and unticking stays sayable.
  Rejected: ticking at the close as Steps 1-5 did (the gate is then
  a record of the past and never of the present); ticking with a
  date per item (the devlog carries when, and the step's own date
  is the one that matters); leaving the rule unwritten and just
  doing it (an arrangement nobody can read is not on trial, it is a
  habit).

- 2026-09-21 `cbc-slice` edited in place, pinned @ 4c3ac99: Stage 3
  gains two demands, in SKILL.md and as items 5 and 6 of the
  workflow reference's Stage 3. One, each test says beside itself
  what it is for — its evidence criterion, its guarantee, its kill,
  and in plain words what it checks and what would trip it, the
  reasons staying in the slice record so the two cannot drift. Two,
  a test that cannot fail for the invariant says on itself that it
  is a tripwire on a decided face, with the counting rule that
  keeps that from being a hole: a tripwire never discharges a kill,
  every kill still owes a test seen red, and the red run decides
  which kind a test is rather than its author.
  Found at Step 6, SL-2. The reviewer asked what one test was for
  and could not tell from the file; the answer took several rounds
  of conversation that a few lines beside the test would have
  saved. The same run then found the skill has no notion that a
  test which cannot fail may still be worth keeping — so such a
  test is either mislabelled as evidence, which is what happened
  here until the red run exposed it, or deleted. The red run is
  also the mechanical part of the fix: with the wall absent one
  test reddened at onHandCount=7 against activeSum=8 and the other
  stayed green, and that is what told them apart, not a reading.
  Rejected: a prose hand-off describing the gap in TODO (rule 4 —
  the hand-off is one line, the diff and this entry are what the
  source reads, and prose makes it read a translation); a single
  reference line per test, tried first and found insufficient —
  `E5 · G5 — kill 8` says where to look, not what the test means,
  and the reviewer still had to ask; putting any of it in the
  project's own records instead (rule 3 — the demand is one any
  project would want, and nothing in it is this project's answer).

- 2026-09-22 Shapes, and the rule that governs them. A shape says
  how a kind of this project's output is written — the first is
  `.claude/shapes/slice-record.md`, the form of a slice record's
  guarantees and owners — and `.claude/rules/shapes-lifecycle.md`,
  scoped to `.claude/shapes/**`, says how shapes live: born from
  work that exists and never designed in advance; unexposed in
  `shapes/` where nothing loads them, exposed in `rules/` where
  they load while the matching artifact is written; moved between
  the two by the reviewer's judgement, which a shape's dated lines
  inform and no count decides.
  Found in the housekeeping branch after Step 6. SL-2's record was
  unreadable in two sections, the rebuild produced a shape worth
  keeping, and every question after that was about where such a
  thing lives and when it may be seen.
  Why the place rather than a note: a rules file loads whenever a
  matching path is touched and a shapes file loads never, so the
  directory decides whether a shape is in front of whoever writes,
  and nobody has to remember the rule. Provisional means the
  question is still open, and showing a shape to the writer being
  asked destroys the answer — which this run proved on itself by
  reading SL-1's record before writing SL-2's and matching its form
  without asking whether the material wanted another.
  What the comparison caught, kept because the shape's own text
  cannot say it: the failing passage **already carried labels** —
  *Attack:* and *Guarantee:* — so labelling was never the missing
  piece, and a candidate that only labelled the parts left the
  reader simulating the situation in their head. The worked example
  with real numbers is what made it readable, which is why the
  skeletons demand one where a mechanism is involved. Also built
  and rejected there: one claim per sentence, which is longer,
  choppier, and cannot tell a fence from an argument, so it
  flattens the short-claim passages this project wants left alone.
  Rejected: the shape under `docs/` (its reader is whoever writes
  the next record, and it uses words a docs reader cannot decode);
  the lifecycle inside the one shape that exists (it governs every
  shape and describes movement between places, so no file inside
  one of them has the scope); a shape written at a kind's second
  instance as the rule (it would not describe how this project's
  own first shape was born — from a rebuild judged worth keeping,
  with no pair compared); a promotion counter (two closes taking
  nothing up is a prompt to ask the question, never the answer —
  exposing a shape and withdrawing it are the reviewer's);
  deleting a delivered copy after its gate (deleting only hides
  what has already been read, and keeping it is what lets the
  collector reconcile — it reads this project's version and its
  dated lines against what it sent); the rule speaking as if this
  project could ask the collector for anything (it holds no address
  and reaches no repository but its own — a delivery arrives
  because a person asked for it there, so the gate's act is to look
  in `temp/` and to say in a line what it found, including
  nothing).

- 2026-09-22 `artifact-kinds` edited in place, pinned @ 4c3ac99:
  a **shape** entry added beside specification — what a kind of
  output looks like here, its form and never its content, written
  from work that exists and never designed in advance. Its force is
  positional rather than fixed: it binds while it is exposed, which
  is a placement decision, and describes while it is not; who
  consults it follows the same placement.
  Found in the housekeeping branch after Step 6, by the symptom the
  convention itself names as earning an entry. Naming this
  project's first shape took "model" plus a paragraph saying model
  is wrong — a model demands nothing, and this one binds at the
  close — and that paragraph stood in the file for a day. The
  vocabulary has "binds" and "describes" and nothing for a document
  whose force depends on where it is put.
  Why an entry rather than the paragraph: the paragraph would have
  been copied into the second shape and the third, each explaining
  the same gap in its own words, and the vocabulary exists so that
  "is this a convention or a model?" resolves the same way for
  everyone.
  Rejected: model with the misfit noted (it is the symptom, not a
  resolution); specification (a test can verify conformance to one,
  and nothing can test that a passage reads well); template (its
  content is holes awaiting specifics, and a shape is read against
  finished work rather than filled in); convention (deviation from
  one owes an explanation, and a shape wants divergence — that is
  how it learns).
  The three records convention-lifecycle §3 asks for: this entry,
  the TODO line asking the bundle to evaluate the copy since the
  pin, and the diff itself.

- 2026-09-22 `cbc-slice` edited in place, pinned @ 4c3ac99: Stage 4
  gains a last step, in SKILL.md and in the workflow reference. At a
  slice's close, what the slice made is read against the project's
  own shapes, and every difference is **proposed as a diff, never
  corrected** — each ending one of three ways, the human saying
  which: the shape was wrong here and changes, the output drifted
  and is brought to the shape, or each has something and both move.
  Where no shape governs what the slice made, nothing is checked;
  whether that output is worth a shape is the project's judgement
  and not a step of the close.
  Found in the housekeeping branch after Step 6. SL-2's record was
  written, read, found unreadable in two sections and rebuilt; the
  rebuild produced a shape worth keeping, and every question after
  it was about where such a thing lives and when it may be seen.
  Why the skill carries the step and not the shape: the demand —
  that a project's outputs of a kind read alike, and that a
  divergence is a question rather than a fault — is one any project
  would want, while a shape shipped with the skill would be
  inherited rather than derived and would make the step it belongs
  to decorative.
  One correction before it landed, the reviewer's: the step's first
  wording said it runs at the close so the output is written
  independently of the shape. That reason is wrong inside a project
  — the writer reads the previous output anyway, so SL-2 inherited
  SL-1's form and was never an independent sample. The reason that
  survives: what is written from a shape follows the shape, and what
  is written from the slice follows the material, so only the second
  shows where this slice's material does not fit.
  Rejected: the skill carrying a shape of its own (`cbc-slice` ships
  no template today, and `cbc-framing`'s one template is a registry,
  whose form is the method's rather than a project's); the check as
  a gate that corrects rather than proposes (a divergence would
  become a fault, and the loop only works while the shape can lose);
  the close writing a shape when none exists (a shape is born from
  work judged worth keeping, which is the project's call and not a
  thing a close does by rote).
