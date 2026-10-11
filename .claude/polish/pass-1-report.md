# Pass 1 report

<!-- Written by the pass, read by the reviewer before the branch.
     The pilot's part first; the rest of the pass adds below it. -->

## The pilot: README, TODO, CHANGELOG (2026-10-10)

Steps 1 to 4 of the brief, then stopped, as the brief says.

### Changed

| Commit | What and why |
|---|---|
| c7cb292 | README names the tests' once-per-machine setup. `./mvnw test` needs podman's user socket and `~/.testcontainers.properties`; only the operator manual said so. Prerequisites and Test now link to that section, without copying its steps. (check 4, missing) |
| 9c75a65 | README's status block loses its dated timeline and "Next is the release gate". The dates were already beside each invariant; the early steps are PLAN's and the devlog's; "next" was stale. It now links to CHANGELOG and PLAN. (checks 1, 2, 3) |
| 692fbdf | TODO's *Now* still held Step 8 as in progress. Now names the polish; *Next* names Release. (check 1, stale) |

### For the reviewer

Each needs your decision, or touches a file the pass may not edit.

1. **PLAN does not mention the polish.** CLAUDE.md gives the
   current state to PLAN, but the polish is not a PLAN step, so
   only TODO's *Now* says it is happening. Options: a line in PLAN
   (for example in the empty "Steps 9..N-1" section, which now
   holds no slice), or leave it to TODO. PLAN is outside the
   pilot; the full pass will reach it either way.
2. **A wrong fact in the decisions log.** The entry of 2026-09-17
   says "temp/ is ignored". Checked: it is not (`git check-ignore`
   finds no rule). TODO's own temp/ item says so correctly. The
   entry is history, so the fix is a new dated line correcting it.
3. **TODO's kata item carries history.** Two parentheses and a
   sentence tell how the item moved between the handbook and the
   bundle. The backlog part is one line: the `cut-a-kata` skill is
   on trial and graduates after serving two projects. Cut the
   history, or keep it?
4. **TODO's "Seeing what changed between versions" item is mostly a
   decision.** Options turned down with reasons, a working answer
   in use since 2026-09-21, and IDE setup steps. Only "Still to
   decide" is backlog. Move the rest to a new decisions entry and
   leave a short item?
5. **Two glossary items in TODO.** One under *Later* (this
   project's reader), one under *To the deliverer* (the method).
   Different owners, but the same word list and date written
   twice. A cross-reference instead of the repeated list? Pass 2
   may settle the deliverer's one.
6. **TODO's "A fact written in two places gets fixed in one"** says
   nothing sweeps for a changed fact. This pass is such a sweep.
   Re-decide the item at the pass's close, not now.
7. **CHANGELOG 0.1 reads as build history** (the harness, the
   ground, the role split) rather than what a user sees, and the
   version intros use slice IDs. Released versions are history, so
   no edit. The finder recommends leaving it; so do I.

### Dropped

- README: the "cited from other repos as `NEVER-OVERSOLD
  ADR-nnnn`" line was flagged as unsupported. It is a real
  convention: this repo cites the bundle's ADRs the same way
  ("CBC ADR-0038").
- README: "version 0.4" in the status line. True now; Release
  changes it.
- README: the quantity and duration limits repeat SL-1's record.
  A caller needs them where they use the door; SL-1's record still
  owns the guarantee.
- CHANGELOG: the adjust rule stated in 0.1 and again in 0.2. The
  second is a dated correction of the first, not a drift.
- TODO: "the kit @ af16eb7" in a deliverer line. It names what was
  on trial at Step 1, which is still accurate.
- TODO: the template comment at the top. Still in use; harmless.
- TODO: the three "held until the retrospective" lines. Still
  true: the retrospective has not run.
- TODO: whether a rule can be delivered on trial. The deliverer's
  question, held by its note; not for this pass.

### Cost

- Agents: 3 finding agents (Sonnet), read-only, one per file.
- Their tokens: 25,905 (CHANGELOG), 28,176 (README), 35,430 (TODO):
  89,511 in all, for about 600 lines. How much of that is fixed
  cost per agent, and how much grows with the file, the pilot
  cannot tell.
- The main model's own share (sorting, checking, writing four
  commits and this report) is not reported by the tools.
- Time: under a minute per agent.

### What the pilot says about the rules

Proposals, not applied: the overview's rules change only with your
word, dated under its *Changes*.

1. **The budget's agent count does not fit the inventory.** "8
   agents in all" was set before counting files: one agent per
   file would need about 140. One agent per *area* fits: about 6
   areas (records; the system exports; the slice records and ADRs;
   the manuals and bootstrap requirements; the project's agent
   files; the code's comments), plus one for the re-check. Propose:
   the pilot's 3 do not count against the 8.
2. **A finding outside the pilot's files has nowhere to go but the
   reviewer.** Finding 1 above is one. In the full pass that is
   fine, since every file is in some area.
3. **Finders report history they may not change.** Two of the
   seven items above are history. That is useful, and the brief
   already routes them to the reviewer; no change needed.
4. **Estimated cost of the rest:** about 6 area agents at 60,000 to
   150,000 tokens each, since the areas are 10 to 20 times the
   pilot's files. Roughly 0.5 to 1 million subagent tokens, plus
   the main model's share. A guess from three files; the first
   area will say more. The pilot made 3 fixes in 600 lines; at that
   rate the 60-commit cap is not the limit that bites.

## The full pass (2026-10-10)

Seven areas, one reader each, then one re-check of the changes. It
stopped on **re-check dry**: the re-check found one small thing and
nothing new. `./mvnw test`: 100 tests, 0 failures, after the code
comments changed.

### Changed

| Commit | What and why |
|---|---|
| 55428bf | ARCHITECTURE said the ledger holds "three of its four" invariants. All four are closed. |
| 6341247 | `pom.xml` was still `0.3-SNAPSHOT`, its comment "No slice has closed". The version moved at every close but SL-4's. Caught only by comparing facts across areas. |
| 3e3ebd8 | Five living references to September's devlog entries said "the devlog", which now opens on October. They name `devlog/2026-09.md`. |
| 448ea26 | SL-2's record: a quote from the registry that the registry has since revised; "SL-1's record still calls it so", corrected long ago. |
| 4ae081d | SL-3's evidence table called consume × release kill 14. It is kill 13. |
| 2ff3304 | SL-3's record: three lines still read as during the slice ("Today they never do", "Not added: an index", "are corrected at this slice's close"). |
| 6e17ff6 | SL-4's record: "Today nothing at the door handles a lost store" — the 503 is built. |
| d98c75f | Operator manual: the ledger never became a compose service (the harness forks processes); Flyway's info lists V1–V4, not V1. |
| b264fc8 | The contract's header pointed the why at ADRs 0004–0006 only; its body leans on 0014 and 0015 too. |
| 1b2c9f9 | `pom.xml` comments still spoke from the bootstrap ("once the store is wired", "nothing persists domain state yet"). |
| 6 commits | Code comments: ExitStormIT's kill numbers; OneClockIT forgetting the exit writes expiry receipts; CorrectionIT carrying E1–E4, not E1–E2; package-info and Hold naming which slice they cite; a pointer to the 503's check; MigrationPathIT reading every migration's walls. |
| cd0b81c | One line the pass itself made too wide. |

### For the reviewer

Grouped by where the fix would go. The pilot's seven items are
above; two of them are repeated here because the full pass found
more of the same.

**PLAN**
1. PLAN does not mention the polish (pilot item 1), and its "Steps
   9..N-1: Invariant slices" holds no slice now. Strike it `[-]`
   with a reason, delete it, or use it to name the polish?

**CLAUDE.md** (the entry file: left for you, not edited by the pass)

2. The devlog row names `devlog/devlog.md` only. September is in
   `devlog/2026-09.md`. Name the folder, or both files?
3. No row for `.claude/polish/`. It is temporary; a row now, removed
   at pass 3's close, or none?
4. The decisions row promises "the conventions held, with
   versions", but `decisions.md` has no such list: the held set and
   pin must be rebuilt from dated entries. Add a short "held now"
   block at its top, or change the row's words?

**The decisions log**

5. The 2026-09-17 entry says "temp/ is ignored". It is not (pilot
   item 2). A new dated line correcting it.
6. Its header comment still tells the reader to fill two
   placeholders "before the bootstrap commit". Done long ago. Delete
   it?

**History that points at moved things**

7. ADR-0004 cites "the devlog's Step 3 entry", now in
   `devlog/2026-09.md`. The ADR body is history: a correcting line
   elsewhere, or leave it, since the devlog folder still holds it?

**The system exports** (each a dated revision entry, on your word)

8. The registry's "Ordering expectation" still carries the SL-3
   close's text as if current: "SL-4 is next and last … the harness
   has not yet created" its adversity.
9. The registry's "Left open: who may write the store … waits for a
   review" — answered by the definition's T4 and W7 on 2026-10-08.
10. The registry's SL-2 row says "The slice's specify step
    decides". That names a workflow step, which the local rule
    keeps out of the exports.
11. The definition: "SL-1's flag" is gone (FC3 removed it); T2's
    "Which way is a slice's decision" has no pointer to the deciding
    record; L4's "parked, not chosen" mechanisms and "folds into the
    first slice" read as pending. One revision entry could mark them
    all as framing-time.

**Closed slice records** (certified evidence: changed only on your
word)

12. SL-3's evidence table has no row for
    `NoSecondWayOutTest.theLedgerNeverRewritesAReservation`, the
    rule the review after SL-3 added on 2026-10-08. Its §10 names it.
    Add the row, dated?
13. The bootstrap requirements say the migrations home "holds V1".
    It holds V1–V4. A dated line, or leave the certified record as
    it was?
14. SL-4's plan keeps a bullet marked "provisional" that was
    confirmed the next day, and a conditional ("if it does not
    hold…") that the build answered. Leave as signed?

**TODO** (pilot items 3–6, unchanged; item 6 can be answered now:
this pass did sweep for a fact written in two places, and the fact
comparison caught one the areas alone missed)

### Dropped

- Test counts in "Evidence — as delivered" sections (29, 39, 81):
  the heading makes them a snapshot of the close, not a claim
  about now.
- ADR index titles in PLAN are shortened, not wrong.
- The operator manual and the contract both give the migrate
  command: the operator's command where the operator runs it.
- The contract's list of what the schema refuses is a summary that
  defers to ARCHITECTURE, as it says.
- SL-2's "no recourse today" for an operator: the next bullet
  records SL-3's answer beside it.
- Bare G-numbers inside `Ledger`'s SL-3 methods: each method's doc
  names SL-3's record first, so the context says whose G it is.
- ADR-0009's "F15's kill-mid-work" against ADR-0004's F16: both are
  ways of dying mid-work, not a contradiction.
- Template comments in the manuals: still guidance.

### Ideas for pass 3

Not acted on; handed on as found.

- **Explanation:** a "what is true now" line in the bootstrap
  requirements; a "state of the ground now" line in the operator
  manual (schema version 4); the stand-up and migrate order in one
  place; SL-4's "as signed / as revised" given subheads.
- **Structure:** the registry's "Ordering expectation" as one
  current line, its history to the log; SL-3's revision narrative
  told once, not in §8 and §9; the 10 / R 3 / S 5 scene told once
  (InterruptedConsumeScene) and pointed at; test counts once per
  record; "Seen here" history in the manual split from the living
  steps; a "held now" block on top of the decisions log.
- **References:** each wall's owner named as a method
  (`Ledger.reserve`), not only as SQL; "migration V3" against the
  definition's verdict V3, which collide (the glossary item);
  ADR links from the Decision index; ARCHITECTURE's test codemap,
  which leaves out seven test classes, and its "the reserve storms"
  naming no class.
- **Repetition with drift risk:** the intent's tests and rejected
  candidates restated in the derivation; "kit @ af16eb7" in six
  files; compose.yaml's header retelling ADRs 0004–0006.
- **Comments:** StoreOutOfReachIT's "seen green on the red tree";
  ReservationDoorIT's "no adversity yet"; the door's "three kinds
  of answer" against DoorProblems' fourth.
- **A doc risk:** the operator manual puts `runtime_localdev` in a
  URL while the contract says no password appears in docs.

### Cost

| Agent | Tokens |
|---|---|
| A. Plan and shape | 43,474 |
| B. The system | 64,005 |
| C1. Early slices | 68,435 |
| C2. Late slices | 81,756 |
| D. Decisions and ground | 82,936 |
| E. Agent files and build | 54,787 |
| F. Code | 192,907 |
| Re-check | 41,721 |
| **Full pass** | **630,021** |
| Pilot (above) | 89,511 |
| **Pass 1 in all** | **719,532** |

8 agents in the full pass, at the cap. 20 fix commits in pass 1
(3 in the pilot, 17 after), well under the 60-commit cap. The main
model's share (sorting, checking each finding, writing) is not
reported by the tools.

### What the pass says about the rules

Proposals, not applied.

1. **The code area cost a third of the pass alone** (193,000 of
   630,000): 5,600 lines and many citations to verify one by one.
   For pass 3, split code from the start, or give it its own pilot.
2. **A read-only agent ran `git checkout`.** It changed nothing (it
   was already on the branch), but "read-only" was a request, not a
   guarantee. Name the forbidden commands in the shared rules: no
   checkout, switch, stash, reset, or anything that writes.
3. **The fact comparison earned its place.** It caught the pom's
   version, which no single area could see.
4. **Snapshot sections confuse the finders.** "As delivered" and
   "as signed" sections were reported as stale. Tell finders those
   headings mark a moment, not a claim about now.
5. **CLAUDE.md was routed to the reviewer** although the brief put
   it in scope: the entry file governs every session, so its edits
   are better made with you. Say so in the shared rules.

## The close (2026-10-10)

The fourteen groups came down to six decisions, taken one at a
time; each is ticked, with what was decided, in the brief's
*Close*. Applied on the branch:

- **History:** one correcting entry in the decisions log (temp/
  was never ignored); the other out-of-date facts in history stay
  as written.
- **The exports:** one registry revision (the ordering's two
  re-decisions dated, the store-writer question answered, SL-2's row
  without a working step) and one definition revision (the clock's
  line).
- **CLAUDE.md and the log's header:** the devlog row names both
  files; a row for `.claude/polish/`; the decisions row promises
  every delivery with its pin; the header no longer names the
  handbook as upstream.
- **PLAN:** the slices' slot skipped with its reason; Release's
  notes say the polish comes first.
- **SL-3:** the evidence table gains E7's fourth rule, dated.
- **TODO:** the kata item loses its handbook history (that wording
  now survives only in git history before this pass); the versions
  item keeps its open question, the rest moved to the decisions
  log; the two-places and moment items record what this pass met.

Then the branch was regrouped: its 36 small commits are kept on the
local branch `polish-1-clean-up-detail`, and the branch itself was
committed again as a few commits by kind. Every fix above and in
the tables before is in them; this report is the record of each.

Found while deciding: TODO's versions item had turned down "a
commit per iteration, squashed at the end" on 2026-10-01. The
regroup is that option, tried under the reviewer's explicit
exception for the polish's branches. Its verdict goes to that TODO
item once this pass lands.
