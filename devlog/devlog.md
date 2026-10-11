# Devlog

<!-- Newest entries on top. 2–5 minutes at the end of each session.
     For future-you: fragments fine, honesty mandatory. Never clean up.
     Mark dead ends loudly with "DEAD END:" so they're greppable.
     End every session with a "Resume:" line — cheapest save-point there is.
     This file holds the current month. A finished month moves out
     whole, word for word, to devlog/<YYYY-MM>.md: September 2026
     is in devlog/2026-09.md. -->

## 2026-10-10  (the polish, pass 1: clean-up)

- Before Release, a polish in three passes, on the reviewer's
  idea: clean-up, skills and rules, improve. Not a PLAN step; its
  rules, briefs and reports are in `.claude/polish/`. Each pass on
  its own branch, the agent committing freely there, never merging.
- Pass 1 made every line true and put it in its record. A pilot on
  README, TODO and CHANGELOG first, then seven areas, one read-only
  reader each (Sonnet), then one re-check of the changes. Stopped
  on "re-check dry".
- 20 fixes. The biggest: README missed the tests' once-per-machine
  setup; the pom still said 0.3 (caught only by comparing each
  area's facts); ARCHITECTURE said three of four invariants; kills
  13 and 14 swapped in SL-3 and ExitStormIT; September's devlog
  entries cited as "the devlog" after the split. 100 tests green.
- Fourteen groups for the reviewer came down to six decisions,
  taken one at a time: history corrected only where it misleads
  (temp/ was never ignored); two export revisions; the entry file
  and the log's header (the handbook no longer upstream); PLAN's
  slices slot skipped; SL-3's missing E7 row; TODO trimmed to
  backlog.
- Cost: about 720,000 agent tokens, 11 agents with the pilot's
  three; the code area a third of it.
- A read-only reader ran `git checkout`. Nothing moved (already on
  the branch); the report proposes naming the forbidden commands.
- Landed regrouped: 36 small commits kept on the local
  `polish-1-clean-up-detail`, the branch committed again by kind.
  That is the option TODO's versions item turned down on 2026-10-01,
  tried here under the reviewer's exception; its verdict goes there.
- Resume: once this lands, pass 2 (skills and rules): turn its
  sketch into a brief, with pass 1's five rule proposals decided
  first, then cut `polish-2-skills-and-rules`.

## 2026-10-10  (housekeeping: the take @ 5361213, the devlog split)

- On `housekeeping-take-5361213`, cut from main.
- The deliverer's note arrived in `temp/`, read through c7073c7.
  Checked against the staging before anything moved: 2 copies
  differ (`commit-messages`, `commit-plan`), none new, none gone,
  nothing under `docs/concept/`. That is the note's count. Our one
  edit since f801fd0, to `cbc-slice`, was already in the staging.
- Taken whole in one commit (c07b57e); the staging and its note
  removed. Pin now 5361213, in `.claude/decisions.md`.
- What changes for us: a commit subject is in the present tense,
  either a command or what became true; a commit plan says
  "commit", and "step" means only PLAN's steps.
- TODO's *To the deliverer* drops the two answered lines (65dec93).
- The devlog split (0dadfd0): September, 1,084 lines, moved word
  for word to `devlog/2026-09.md`. This file keeps the current
  month. The note offered it; the reviewer chose this shape over a
  file per month for October too, which would move the
  session-ending file every month.
- Resume: once this branch reaches main, open Step N, Release: cut
  its branch from main, derive its gate into PLAN before any work.

## 2026-10-10  (after Step 8: the hand-off)

- Pushed by the reviewer; `step-8-sl-4` deleted, merged and never
  pushed.
- TODO's line for the evidence-test shape now says it moved at
  SL-4's close (968abae), as the slice-record shape's line tracks its
  own moves.
- A lesson drafted for `cbc-slice` and dropped, on the reviewer's
  reasoning: "a plan's claims about behaviour, tried before it is
  signed" — from the two §8 revisions. A plan is a best guess and the
  build is where it meets the store; both claims were found within
  the hour and revised openly, which is the method's own rule —
  deviations legal, never silent. Testing before signing would turn
  planning into building. Not to be raised again without a cost
  bigger than a dated revision.
- Ready for the deliverer: since the read-through at 10437cd, two
  changes under `.claude/` besides the take itself — `cbc-slice`
  (0069200) and the evidence-test shape (a47f129) — each with its
  record and its line under *To the deliverer*.
- Resume: wait for the deliverer's note in `temp/`; take it by
  `delivered-copies.md` rule 5. Then Step N, Release — its gate
  derived into PLAN when it opens.

## 2026-10-10  (Step 8: SL-4, E1 red and green)

- E1 written (`InterruptedConsumeIT`): R's consume held at each
  point, its instance killed outright, the hold let go, the witness
  read in one statement. Green against the standing wall at both
  points: 10 on hand, 8 held, R with no ending.
- DEAD END: the red as §8 signed it — consume split into the receipt
  with the units held, then the count — stayed green at both points.
  Both holds sit inside the first transaction, which moves the item's
  row and has its receipt checked against R's row; the hold caught
  the consume before anything was final.
- Four runs instead, each on the working tree and restored from git
  after, from actual output:
  - receipt first, then the numbers, V3 standing: the store refused
    the first transaction; the consume never reached the hold —
    `nothing came to wait on the row of item … within PT30S`.
  - receipt first, V3 absent, held at the item's row: red, on the
    witness — `R ended by consume exactly when the count fell by
    its 3: Numbers[onHandCount=10, held=8, activeSum=5,
    reservations=2], ending=consumed`.
  - numbers first, then the receipt, V3 standing: refused the same
    way — `nothing came to wait on the row of reservation …`.
  - numbers first, V3 absent, held at R's row: red, on the promise
    itself — `the invariant: Numbers[onHandCount=7, held=5,
    activeSum=8, reservations=2], ending=null`.
- So V3 is G1's backstop, seen catching both orders; and E1 shows
  F16's two half-states, one per hold point, once V3 is gone too.
  The split V3 lets through — the receipt with the units held, then
  the count — no hold can reach: both touch the item's row. Left to
  E4. §8 revised for it (1fc0b69), on the reviewer's word.
- A first attempt at taking V3 away commented half its statements
  and broke the migration; the store did not start. Emptied whole
  instead.
- 90 tests green with E1.
- E2 written, in the same class: R's consume held at R's row, both
  moves written; the witness reads mid-consume, then a reserve of 2
  arrives at a second instance and waits behind the consume. Let go,
  the reserve fits the state after — 7 on hand, 7 held, R consumed;
  killed, the state before — 10 on hand, 10 held, R holding. Green.
  The harness gained `Hold.awaitWaiterOn`, to see the reserve wait
  on the consume rather than on the hold, and the scene a reserve
  that returns at once.
- E2's red, numbers first with V3 absent, from actual output, both
  runs failing on the mid-consume reading: `the invariant,
  mid-consume: Numbers[onHandCount=7, held=5, activeSum=8,
  reservations=2], ending=null`. The test stops there, so on the red
  tree the reserve's own half is not reached: with the item's row
  already free, it would not wait at all, and the check that it
  waits would fail first. Said, not hidden: the reader that breaks
  first on the red tree is the witness, not the reserve.
- 92 tests green with E2.
- The 503 (ADR-0016). Its check first (`StoreOutOfReachIT`): R's
  consume held, its session ended at the store. Red against today's
  door, from actual output: `expected: 503 but was: 500`, the body
  Spring's default — `{"timestamp":…,"status":500,"error":"Internal
  Server Error",…}`, not Problem Details. The instance's log showed
  the shape to classify: the statement failed with SQLSTATE 57P01,
  the rollback on the dead connection then failed too ("Connection
  is closed"), and the framework threw that, the first failure kept
  inside it.
- First placed in `DoorProblems`, as §8 signed. Two standing
  structural rules went red on it, and were right: SL-2's — no class
  but `Ledger` touches `java.sql` or Spring's transactions — and
  SL-1's — no field holds a collection. Moved: `Ledger` wraps its
  three transactions in one method that turns a lost store into
  `StoreOutOfReach`; the door maps that to 503. §8's surface revised
  for it, in the same commit, on the reviewer's word to follow.
- A tripwire for §8's face (`StoreOutOfReachClassifierTest`): a lost
  connection, a lost connection behind a failed rollback, a refusal
  at commit, a refused constraint. Seen red by planting the rejected
  face — deciding by the wrapper's type —
  `aRefusalAtCommitIsNotOutOfReach: Expecting value to be false but
  was true`.
- 97 tests green.
- E3 written, beside the 503 check: R's consume held between its
  moves, the store frozen, the caller gone, the instance killed while
  the store could not see it; thawed and let go, nothing sent since.
  The store settled alone — 10 on hand, 8 held, R with no ending —
  and a reserve of 2 at another instance then went through, 10 held
  of 10. Green.
- E3's red, receipt first with V3 absent, from actual output, on the
  witness: `R ended by consume exactly when the count fell by its 3:
  Numbers[onHandCount=10, held=8, activeSum=5, reservations=2],
  ending=consumed`.
- Beside it, the freeze with the instance alive: thawed and let go,
  the live instance carries its consume to the commit — 7 on hand, 5
  held, R consumed — and a reserve fits after. Green on the red tree
  too: a live instance finishes what it began, one transaction or
  two, so nothing is owed. It says on itself that it is not evidence
  and discharges no kill; kept to show a freeze loses nothing.
- The harness, again: E3's freeze failed every run with "Broken pipe"
  on podman's socket — it came after another test's freeze, and the
  client reused a connection the engine had closed while idle. The
  request broke on the way out and never arrived, so `ThrowawayStore`
  now sends it once more when that happens. Green twice after.
- 99 tests green.
- E4, a fifth rule in `NoSecondWayOutTest`: a statement writes a
  `consumed` receipt if and only if it lowers `on_hand_count`, and
  then by what it lowers the units held by. Rule three ties every
  fall to a receipt; this ties the receipt's kind to which number
  falls. `LedgerSql` gained `loweredBy` and `writesReceiptsOfKind`,
  read by parts.
- E4's reds, each planted in `Ledger` on the working tree and
  restored, from actual output — the rule naming the statement each
  time:
  - a consume that frees R's units and leaves the count: `the count
    falls exactly where a consumed receipt is written: WITH receipt
    AS (INSERT … 'consumed' …`;
  - a release that also lowers the count: the same label, on the
    `'released'` statement;
  - a retry lowering the count alone, `UPDATE item SET on_hand_count
    = on_hand_count - :units …`: the same label, and rule three's too;
  - a consume lowering the count by 1, not the hold's units: `the
    count falls by what the units held fall by … expected:
    Optional[r.quantity]`.
- 100 tests green.
- The records caught up: SL-4's record gains §9 (the evidence as
  delivered, every red from actual output, what the build found) and
  §10 (standing guards), and the evidence certified on its sign-off
  line; the registry closes SL-4 by a dated entry — nothing left to
  order, Release next, "the half-done converges" answered; ADR-0016
  Accepted; README and CHANGELOG at 0.4, the 503 shown as a stranger
  meets it; ARCHITECTURE's SL-4, the ledger's one wrapper, the
  harness that interrupts; the ledger's package map names the 503
  and `StoreOutOfReach` — a comment the 503's own commit should have
  carried; TODO's known issue for the one split only E4 sees, and
  the line to the deliverer for `cbc-slice`.
- Not run on the real ground: no migration, and nothing the ground
  could show that the harness did not. It stays down.
- The close's readings. `temp/` empty. SL-4's record against the
  slice-record shape: every block carries its labels; two backstops
  — G2's, G4's — did not say whether the red run saw them, and now
  do (the output drifted). The tests against the evidence-test
  shape: every first line says what the test is, the witness comes
  first, every red failed on it; E1–E3 assert SL-4's invariant after
  the promise, and read the item with R's ending in one statement —
  neither in the shape, both taken into it (each had something).
  Four differences, each settled by the reviewer.
- The commit plan closed (6fcb5d0): fifteen steps, three revisions,
  what diverged in its body. 27 commits on the branch before the
  last, each read against commit-messages: none over, none
  straddling; the last, 28th, counted as written.
- Step 8 reaches main by fast-forward, on the reviewer's word.
- Resume: Step N, Release — derive its gate into PLAN when it opens,
  from the goal, the run's records and the exclusions framing
  recorded.

## 2026-10-09  (Step 8: SL-4, specified, planned, the harness built)

- Stage 1 and 2 on the reviewer's word, each signed the same day.
  §3's two decisions: an unknown outcome answers 503 (ADR-0016);
  nothing converges, because nothing is half-done. The spec's four
  guarantees, G2 among them — a reader mid-consume, past the
  registry's adversity, signed as written. The plan builds almost
  no wall: SL-3's one statement and SL-1's guarded update already
  stand.
- ADR-0016 was committed Accepted at the opening, then held as
  Proposed (f142873) on the reviewer's question: why Accepted before
  the build that tests it? `cbc-slice` now says so for every slice
  (0069200). It is Accepted at this slice's close.
- The harness (ce8b911): a hold, a kill outright, a freeze, one
  session ended; 88 tests green. The hold at R's row is confirmed:
  the consume waits there with the item's row already moved.
- Found by the harness, and §8 revised for it: a killed instance's
  session that is waiting on a row is not undone at once. The store
  learns a connection is dead only when it next speaks to it; the
  check `aKilledInstancesHeldSessionLeavesTheStore` saw the session
  stay until the hold let go. The outcome is unchanged; the timing
  is W3's.
- Asked along the way, and answered in chat: whether all this is
  over-engineering. The tests prove our consume stays one
  transaction, not that the store's commit works; the freeze (E3)
  adds the least, and was kept on the reviewer's word.
- Resume: E1, a consume killed midway (commit 6 of the plan).

## 2026-10-09  (Step 8 opens: SL-4, Stage 0)

- Step 8 opened on `step-8-sl-4`, cut from main at de29d9e, on the
  bundle's copies @ f801fd0. The gate was written and committed
  (05dd952) before any work.
- Stage 0, checked against the repo, not from memory.
  - R1: the three exports stand; SL-4 is the only `chosen-next`
    row; the reconciliation table has its 20 kill rows.
  - R2: `./mvnw test` at the branch point: exit 0, 83 tests in 16
    classes, 0 failures, 0 errors, 0 skipped. The review's last
    count was 82; the one more is E7's fourth rule (24e1214).
  - R3: the suite's store is `ThrowawayStore`, a real postgres:17
    per test JVM, built from the ground's bootstrap.sql and
    migrated from the one migrations home. No mock.
  - R4, one line per kind of adversity SL-4 names.
    - An instance killed mid-consume (F16). What the harness has:
      `ForkedLedger` runs each instance as its own process and
      knows its pid. What it owes: it only stops an instance
      gracefully (destroy, then forcibly if it lingers), so a hard
      kill on demand is missing; and nothing yet lands the kill
      while a consume is inside its transaction. How to land it is
      a plan question.
    - A consume's outcome unknowable (F19). What the harness has:
      nothing. What it owes: freezing the throwaway store, or
      cutting an instance from it, from inside a test. The ground's
      way is `podman pause` (ADR-0004, ADR-0005); checked on this
      machine with a throwaway container — rootless podman, cgroups
      v2, pause then unpause both worked, the container removed.
  - R6: the registry writable, the record scheme in place.
  - R5: the wall SL-4 leans on stands already, so it is made absent
    on the working tree — the two moves split apart — and the
    evidence seen red there, in the build.
- What Stage 0 surfaced for Stage 1, not for the harness:
  - Consume is one statement today: it writes the reservation's
    receipt and lowers the count and the units held, in one
    transaction (`Ledger.consume`, through `Ledger.exit`). There
    is no moment in our code between the two moves — only inside
    the store's transaction, before its commit.
  - V3 checks at every commit that an item's units held equal its
    unreceipted reservations' units. A receipt written without the
    units falling would be refused there. Whether that is an owner
    of SL-4's invariant or only a second guard is Stage 2's.
  - A retry after an unknown outcome already gets an answer:
    ADR-0013 says a repeated exit answers as the first did. What a
    caller is told while the outcome is unknown is the first of
    the two questions before the spec.
- The ground is down: `never-oversold-postgres` exited 11 hours
  ago. The suite does not need it, so it is left down.
- Readiness signed by the reviewer: *2026-10-09 — "lets do it"*,
  given on reading this record.
- Resume: Stage 1. First the two questions, each a record with its
  options: what a caller is told when its consume's outcome is
  unknown, and whether anything is left to converge. Then the
  specification, the row to `in-progress` when it lands; the
  reviewer signs it before the plan.

## 2026-10-07 → 2026-10-08  (housekeeping: the review after SL-3)

- Opened on `housekeeping-review-after-sl-3`, cut from main after
  Step 7's merge. Four questions SL-3 had answered without naming,
  each discussed and decided before any edit, then a commit plan of
  thirteen.
- Who writes the store. Talked through from "why do we have these
  triggers at all": most of SL-3's walls defend against our own
  races, retries, clock and bugs — V3 caught a double free in our
  own tidy — and only part against scripts, which nothing had said
  were trusted or not. A superuser can remove any wall, so no system
  walls against everything; it draws a line. Locking with roles was
  weighed and declined for a system nobody operates, where the
  person running the ledger holds its password anyway. Decided: T4,
  a trust line — only the ledger writes data, its identity its
  alone, Flyway writes structure, the superuser trusted — with W7
  fencing the rest (ADR-0014, with what a real deployment would
  enforce instead). The store's walls stay: they refuse a wrong
  result from anyone, the ledger included.
- Write-once reservations. Deleting or editing a hold cannot
  oversell, but it can leave history that lies — a hold gone with no
  receipt. A store trigger and a revoked right were both weighed;
  under T4 the only writer is the ledger, so the cheapest whole
  answer is a build rule: E7's fourth, the ledger never updates or
  deletes a reservation. Reading SL-2's one-writer rule beside it
  found two holes — a class holding a `DataSource`, or one merely
  named `CleanupLedger`, passed. Both planted, both green under the
  old rule, both red under the widened one.
- Where a rule lives: ARCHITECTURE's map — the application computes,
  the store refuses and never computes. What scale: one line in the
  definition's runtime ground, the evidence's size, so a face can say
  what its cost assumes. When an index is a need: read from the code
  that searches on every decision, not from a measurement nothing
  makes (ADR-0015); V4 indexes `reservation(item_id)`, 82 tests green
  unchanged, its catalog test red with V4 absent.
- The copies. The reviewer asked that every delivered copy the
  findings touch be corrected here and handed back, the deliverer to
  filter: `cbc-slice` (the hunt aims at the trust list; a cost says
  its size; a red fails on the witness; a race's held-open window is
  said; the wall's own check named), `cbc-framing` and
  `infra-establish` (finished copies, first used by the deliverer:
  who besides the system writes what it stores; the size; the
  inspection identity that writes), `commit-plan` (subjects counted
  when planned; agent files their own step). Each edit written as a
  question, not this project's answer; one decisions entry and one
  TODO line per copy; the chapters' lessons as prose.
- Small things caught on the way: forward references cut twice
  before staging (a definition entry naming an ADR not yet written,
  a decisions entry naming manuals not yet changed); a stray
  `{@linkc` in the ledger's Javadoc, restored by the reviewer; every
  planned subject counted before the plan was committed.
- The temp draft, `temp/app-store-responsibility.md`, has done its
  work: everything it argued is decided in the records above. It
  stays untracked; deleting it is the reviewer's call.
- The ground, 2026-10-08: down since the day before, brought up and
  verified both ways, dumped, migrated to V4; the index read from its
  catalog, every trigger still enabled. Through the door on V4: a
  reserve of 2 on `sl3-ground`, whose last hold of 7 had run out
  overnight, wrote that hold's expired receipt first and was
  admitted — expiry ending a hold a day late, by the store's clock,
  the first time on the real ground; a fresh item adjusted to 5,
  reserved 2 and consumed, leaving 3 on hand and nothing held.
- Resume: merge `housekeeping-review-after-sl-3` on the reviewer's
  word, then Step 8, SL-4 — its plan the first to aim its escape
  hatches at T4 and size its faces.

## 2026-10-03 → 2026-10-07  (Step 7: SL-3 built and closed)

- The build ran as a commit plan on `step-7-sl-3`: eight steps at
  the opening, six of work and two of bookkeeping; three revisions,
  and §8 itself revised once; twelve steps at the close. 39 tests
  at the branch point, 81 at the close; every one green, SL-1's and
  SL-2's unchanged beside SL-3's.
- What it built. V2: one receipt per ended reservation, its key the
  slice's main wall, its guard refusing a changed, deleted, early or
  late receipt and stamping the instant itself. The two doors,
  consume and release, each one statement that writes the receipt
  and moves the numbers only for a receipt it wrote. Tidy, run first
  by reserve and adjust, freeing exactly the receipts it wrote. V3,
  added by revision: the store checks at every commit that an item's
  units held equal what its unreceipted reservations hold.
- The reds, from actual output; the full list with the messages is
  the record's §9. The key and `ON CONFLICT` removed: fifty racing
  consumes read `onHandCount=4, held=2, activeSum=5`, R's 3 off the
  shelf twice. Tidy subtracting a sum it read: `Numbers[onHandCount=10,
  held=10, activeSum=16, reservations=13]`, eleven admitted where
  five fit. Kill 11 in full with the walls out: 10 on hand, 7 held,
  10 active. The guard's late check out: twenty of forty holds
  consumed after their instant. E7's planted violations each named
  by its rule; SL-1's clock rule named `Ledger.tidy` when
  `OffsetDateTime.now()` was planted there.
- DEAD END: the first red of the build was worthless, again. Key
  removed, four sequential tests failed on `500`s — the answer's
  read met two receipts, threw, and rolled the double move back.
  SL-2's devlog had this exact lesson, "the order of assertions is
  part of the evidence". It was in a record nobody opens while
  writing a test. Redone with the read taking one row; the reds
  carried the numbers. Taken up on 2026-10-03 as a shape for
  evidence tests, written and exposed at the close (decisions log,
  2026-10-07).
- The finding that changed the plan. E6's red showed G5's backstop
  wrong: `item_never_oversold` stayed satisfied while 16 units were
  actively held against 10 on hand — the counter drifted from the
  rows, and the constraint compares only the two stored numbers.
  Then E7, signed as G6's guard for the numbers, was seen at its
  own review to read text that a script outside the application
  never passes through. The reviewer asked whether the store could
  refuse the numbers moving without a receipt as it already refused
  a changed receipt. It could: §8 revised first (7c8cb83), the plan
  next (cc0e5e3), then V3 (81dbef2). With commit 5's naive tidy
  planted again, the store refused the double frees 38 and 6 times
  and the invariant held in every reading.
- The review at V3 found its reservation-update branch untested;
  two tests added, the move one rewritten after its first red
  failed only on a message — the store still refused, on the other
  item. And E7 rewritten to read SQL (JSqlParser, test scope), a
  third rule refusing SQL outside text blocks so every statement is
  read; the ledger's one plain-string read became a text block.
- Deviations from the plan, for the close. Commit 3's planned red,
  the naive check-then-insert, cannot show one request at a time;
  it moved to commit 4's storms. Commit 3 gained a late-consume test
  when kill 11's harm turned out to need a new hold on the expired
  units. Commit 5 gained, on the reviewer's word, a race of tidy
  against consume at the instant, and a correction tripwire beside
  the reserve's; that race's red hit only `[expired, expired]` until
  a 50 ms pause in consume, on the red tree only, held the window
  open — recorded as such. E4's first red was refused by the guard,
  not a red; the second removed the guard's early check too. §5's
  sentence that the naive wall lands first stands against the
  plan's working-tree reds; the skill allows either. Three plan
  revisions: V3; E7 read as SQL; the records step split, since it
  carried agent paths beside project ones.
- The questions this slice raised and did not answer, all in TODO
  for a review before SL-4's plan: where a rule's responsibility
  lives, store or application; what scale a decision assumes, with
  `reservation(item_id)` unindexed and V3's cost unmeasured; who
  may write the store at all — reservations write-once as the
  concrete case, since a hold deleted with its units still passes
  every wall; and, to the deliverer, whether a rule could arrive on
  trial the way a shape can.
- The scratchpad notes holding every red were lost to a cleared
  `/tmp` on 2026-10-06; rebuilt from the session's transcript, where
  each was written from the tool's output at the time.
- The ground, 2026-10-07: verified both ways, the rows read before
  V3 (none out of step), dumped, migrated to V3. Real exits through
  the door against it, and two script writes as `runtime` refused:
  "the units held read 0, its reservations with no receipt hold 7",
  "a receipt is never deleted". The operator manual carries the
  before-V3 check.
- The close's shape readings, each shape's first: four differences,
  settled by the reviewer one by one. The record had drifted twice
  (G6's kills line; G4's and G6's backstops, the honest answers
  being "partly" and "nothing") and was brought to the shape. The
  dated labels on §8's revised faces were something the shape
  lacked, and it took them. The evidence-test shape asked the
  promise of setup readings, which are not evidence; it was wrong
  there, and changed. The reviewer took the records commit first so
  each settlement reads as its own diff.
- Two commit subjects found past 50 characters, one from Stage 1
  and one from today; kept rather than reworded, since a rebase
  would change the hashes the records cite. The planned subjects
  were counted after that, and three shortened before committing.
- Version 0.3, three of four invariants evidence-closed. The
  registry re-decided the ordering and kept it: SL-4 next and last,
  its walls standing already, its adversity — death mid-work,
  unknown outcomes — the one no harness has created yet.
- Resume: Step 7's gate is closed but for its branch item, ticked on
  the reviewer's word to merge. Then the review under TODO's Next,
  before or beside Step 8's opening, as the reviewer sets.

## 2026-10-02 → 2026-10-03  (Step 7: SL-3 specified and planned)

- Written at the close from the commits and the record; the
  sessions left no entry of their own.
- 2026-10-02: the three questions the gate put before the
  specification, decided by the reviewer. A repeated exit answers as
  the first (ADR-0013). An expired hold's units are free from its
  instant, not eventually — which paid SL-1's over-counting and
  SL-2's conservative refusals, at the price of changing what their
  decisions read. No operator-side exit: release asks no identity,
  and an exit that chose whose hold ends was SL-2's rejected policy
  again. The specification signed the same day: six guarantees by
  attack, the seventh to tenth answers SL-4's, the caller's view,
  W5 and V5; FC2 folded in; no flag, said why.
- 2026-10-03: the plan, four faces put to the reviewer and decided —
  a receipt per reservation over an ended mark; tidy first over no
  stored counter; a trigger refusing changed receipts over revoked
  grants or code alone; the arithmetic in the application's
  statement over a trigger that computes. Signed before code.

## 2026-10-02  (Step 7 opens: SL-3, Stage 0)

- The bundle's delivery @ a3b6b8c was taken first (fa9a402), on the
  note's advice that SL-3 opens on two of its changes. It was
  committed straight onto main, not on a housekeeping branch like
  the takes before it. Same result on main; the reviewer said to
  leave it.
- Step 7 opened on `step-7-sl-3`, cut from main at fa9a402. The
  gate was written and committed (06c2aee) before any work. It is
  the first step written from PLAN's step form.
- Stage 0, checked against the repo, not from memory.
  - R1: the three exports stand; SL-3 is the only `chosen-next`
    row; the reconciliation table has its 20 kill rows.
  - R2: `./mvnw test` at the branch point: exit 0, 39 tests, 0
    failures, 0 errors, 0 skipped. The same 39 SL-2 closed on.
  - R3: the suite's store is `ThrowawayStore`, a real postgres:17
    per test JVM, built from the ground's own bootstrap.sql and
    migrated from the one migrations home. No mock.
  - R4, one line per kind of adversity SL-3 names. A request sent
    twice (F3, F5): the door is driven by plain HTTP, as
    `CorrectionIT` already does for a resend. Requests at the same
    instant (F4, F23): `ReserveStormIT` holds requests at a line
    and releases them together, and `InstancesStormIT` does it
    across forked instances; both work on any endpoint. A hold run
    out, or not yet, by the store's clock (F23, F24): `OneClockIT`
    makes a one-second hold run out by waiting, and a ten-minute
    hold is "not yet". The shortest hold is one second, so a race
    exactly at expiry is staged by firing on both sides of the
    instant; how is a plan question, not harness work. No harness
    work owed.
  - R6: the registry writable, the record scheme in place.
  - R5, split as the gate says. Two of SL-1's constraints bear on
    exits and stand already: `item_reserved_not_negative` (a
    reservation's units given back twice can drive `reserved`
    down) and `item_never_oversold`. They can be made absent on
    the working tree. The walls SL-3 owns do not exist yet, so
    their evidence is seen red in the build, before each wall
    stands.
- What Stage 0 surfaced for Stage 1, not for the harness:
  - Nothing ends a reservation today. The `reservation` table has
    no ended state, and the door has two endpoints, reserve and
    adjust.
  - The admit already returns the reservation's identifier, which
    is the obvious way for an exit to name its reservation (FC2).
    Whether it should is Stage 1's to decide.
  - An expired hold still counts in `reserved`. Whether expiry
    ends a reservation is one of the three questions the gate
    puts before the specification.
- The ground was down: `never-oversold-postgres` stopped 11 days.
  The suite does not need it, so it was left down.
- Readiness signed by the reviewer: *2026-10-02 — ready for SL-3,
  signed off.*
- Resume: Stage 1. First the three questions, each a record with
  its options: what an exit is at the door, what expiry does, and
  whether an operator-side exit exists. Then the specification,
  the row to `in-progress` when it lands; the reviewer signs it
  before the plan.

## 2026-10-01  (housekeeping: the error check's four decisions)

- The four findings the error check left open, settled on the
  reviewer's picks after they were put again in plain words.
- ADR-0010's "201 for a new record" against the adjustment that
  creates its item and answers 200: the code was right, and
  ADR-0012 says why — the same assertion gets the same answer, and
  a resent first adjustment would otherwise read 201 then 200. It
  narrows one line of ADR-0010, which stays Accepted. No code, test
  or README line changed.
- ADR-0005's two overturned reasons: TODO's Known issues, its first
  real entry. The decision stands, so a new ADR would say nothing.
- The bootstrap requirements carry a dated note on top, the body
  kept as certified. PLAN's retrospective points at the three tests
  where they are written, CLAUDE.md's own comment.
- The two structure ideas are in TODO's Later, for Release or the
  retrospective.
- Resume: once this branch reaches main, nothing stands before Step
  7 on this side. The reviewer checks the bundle's side first, then
  SL-3.

## 2026-10-01  (housekeeping: the documentation error check)

- Asked before SL-3: do the records contradict each other, are there
  errors, would a better structure help, and now or later. Split in
  two: errors now, because three had already turned up today by
  accident and SL-3 and the bundle both read these records next;
  structure later, at Release or the retrospective, with only two of
  four slices written.
- One read, alone, of all 23 records (4,800 lines), with every claim
  about code checked against the code and the tests. Eighteen
  findings: seven wrong, five stale, three order or format, three
  small gaps. Clean: constraint names, README's refusal message, the
  39-test count, the decision index, the kill mapping across
  registry, definition and records.
- Fourteen fixed on `housekeeping-doc-errors`, one commit per
  document kind. The truth set changed only by dated revision
  entries; each slice record got a dated line.
- The kind of error that recurred: a fix made in one document and
  not in its twin. The contract was corrected to "plan" on
  2026-09-11 and the operator manual kept "specification"; SL-1's
  record updated three places for SL-2's decision and missed a
  fourth. Wrong the day it was written, also: a pointer to a §6
  that never held the cost, and one to a README that never existed.
- Tried and abandoned: standing up a second, throwaway ground to
  see a fresh Flyway `info` as lived. The compose file pins the
  container name, and the stopped real container holds it. The
  empty volume and network the attempt made were removed by name;
  the real volume was not touched. The manual's new expected line
  is Flyway's documented behaviour, marked so by keeping the old
  lived line dated.
- Waiting on the reviewer: ADR-0005's stale reasons and ADR-0010's
  201 against the code's 200, both immutable records; whether the
  bootstrap requirements stay a snapshot; PLAN's pointer to a
  convention this repo does not hold; and two structure ideas for
  TODO — a mark for "true as of a date", and a habit of sweeping a
  fact's other copies when one changes.
- Resume: once this branch reaches main, the four decisions above,
  then Step 7, SL-3.

## 2026-10-01  (housekeeping: the writing sweep's remainder)

- The second item before Step 7, on `housekeeping-writing-sweep`.
  Three decisions first, all the recommended ones: reshape SL-1's
  record although it closed before the shape existed; settle the
  face tables by comparison; touch README and the contract only
  where a passage carries an argument, and leave the operator
  manual, which is procedure.
- The face tables were the shape's one undecided part. Four
  candidates built on SL-2's G5 table, the words the same in each.
  The table failed the diff test outright, with 374-character rows.
  Blocks were the only candidate with no fail. The reviewer chose
  them on the argument without opening the render, and asked first
  what was being decided and why the shape did not already say —
  it had left this part open on purpose, because columns are good
  at weighing options side by side. Recorded in the decisions log
  as it happened, render skipped.
- SL-1's guarantees and owners now read like SL-2's. The *Say:*
  lines are the one new content: SL-1's own run numbers where a
  test made them, the attack played out where none did. SL-1's §8
  now counts the unknown item §4 always named; its old row had
  also swapped a missing hold for the negative count.
- Found on the way, not in the plan: the contract said check
  constraints are checked at commit. PostgreSQL checks them at
  every write and cannot defer them, which is exactly why SL-1's
  wall holds. Fixed in a commit of its own after a plan revision.
  ADR-0005's "constraints checked at commit" is true of the
  deferrable kinds and stays.
- SL-2's sign-offs have no line for its close or for the writing
  pass's reshape. Only today's line was added, on the reviewer's
  word; the gap is left visible.
- Resume: once this branch reaches main, the last item under
  TODO's Next — seeing changes between versions — on its own
  branch. Then Step 7, SL-3, the first slice to meet the shape's
  close-time check with all three parts in it.

## 2026-10-01  (housekeeping: SL-1's test headers)

- The first of the three items before Step 7, on
  `housekeeping-sl-1-test-headers`. Fifteen headers across SL-1's
  six test files, in the form SL-2's carry: criterion, guarantee,
  kill — or "not evidence" and what it pins — then what the test
  checks and what would make it fail. Comments only; the tests
  compile and the structural test passes.
- The question the item used to carry, whether to write the
  convention down, was answered before the work started: the take
  @ 0000855 brought it in `cbc-slice`.
- Writing them sorted the door tests. Three of `ReservationDoorIT`'s
  pin a decision (ADR-0010's answers, ADR-0011's first adjustment)
  and cannot fail for the invariant, so they say they are
  tripwires. One is SL-2's decision seen at the door and points at
  `CorrectionIT`.
- Found, not fixed: SL-1 §4 names an unknown item among E6's
  shapes, and §8's row for E6 counts nine shapes without it. The
  unknown-item test's header follows §4. The record is left as it
  is; the writing sweep touches SL-1's record next, and can square
  it there.
- The line owed to the bundle since the take is in TODO: which rule
  gives way when a take covers `.claude/` and `docs/concept/`.
- Resume: once this branch reaches main, two items under TODO's
  Next — the writing sweep's remainder, and seeing changes between
  versions — each its own branch from main. Then Step 7, SL-3.

## 2026-10-01  (housekeeping: the bundle's delivery @ 0000855)

- The bundle's note of 2026-09-30 was waiting in `temp/` with its
  staging, read through 9869798. It answered everything this run
  had addressed to the bundle since 4c3ac99.
- Checked before anything moved, as the note asked: its counts held
  (24 differ, 1 new, 5 gone), and each difference was what it said.
  All four of this run's edits since the last pin had a verdict, so
  nothing was re-applied. Both `cbc-slice` edits came back in this
  run's wording.
- Taken whole on `housekeeping-bundle-0000855`. Conventions held as
  copies went from seven to three: `decide-first`,
  `option-comparison`, `artifact-kinds` and `convention-lifecycle`
  are gone, and our `skills-changed-in-place.md` with them.
  `delivered-copies.md` does that rule's job, and covers the concept
  chapters under the same pin.
- The reviewer's call: no private copies of the withdrawn skills —
  do as the note says. TODO tells the bundle they were used here,
  because `visual-comparison` now calls the general method unused.
- TODO gained `## To the deliverer`, and Now stopped repeating Next.
  Two answers the bundle asked for went there: SL-2 did not lean on
  the facility paragraph, and SL-2's G5 and G6 are held by absence.
- Found on the way, not in the note: PLAN named
  `docs/construction/slice-record-shape.md` twice, a file that has
  not existed since the writing pass moved the shape. The kind of
  miss `commit-plan`'s new rule — sweep every moved name — is
  there to catch. Plan revised; one commit added.
- Careful at the next take: `cp -a` from the staging's `.` copied
  that folder's owner-only mode onto the repo root, 755 to 700. Put
  back by hand; git does not track folder modes, so nothing showed
  in the diff. Copy the files, not the folder's own attributes.
- Resume: once this branch reaches main, the three items under
  TODO's Next, each its own branch from main, in the order the
  reviewer picks. Then Step 7, SL-3.
