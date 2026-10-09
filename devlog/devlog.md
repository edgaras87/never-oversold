# Devlog

<!-- Newest entries on top. 2–5 minutes at the end of each session.
     For future-you: fragments fine, honesty mandatory. Never clean up.
     Mark dead ends loudly with "DEAD END:" so they're greppable.
     End every session with a "Resume:" line — cheapest save-point there is.
     When this file gets long, split into devlog/<YYYY-MM>.md per month. -->

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
- Resume: E2, a reader and a reserve beside a held consume (plan
  step 8).

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

## 2026-09-21..23  (housekeeping: the writing pass, and shapes)

- Three things were queued when SL-2 closed: the writing pass, the
  review-between-versions question, and SL-1's per-test references.
  One branch, `housekeeping-writing-and-review`, took the first
  only; the other two are untouched and still in TODO's Next.
- It started as "the records are hard to read" and ended with two
  artifacts nobody had asked for: `.claude/shapes/slice-record.md`
  and `.claude/rules/shapes-lifecycle.md`. Nineteen commits, a
  commit plan of seven steps that landed as eleven, five revisions.
  Every revision came from the reviewer reading what had just been
  written and finding it claimed more than it could do.
- `decide-first` ran first and was worth it: the commit count was
  not sayable. Its draft was trimmed to one item after the reviewer
  pointed out that two of the three queued items had no shape
  question at all — the count was sayable for both, so the draft
  was covering a pile of work rather than an unsettled shape. The
  lesson is in the draft's own words, now in history: one draft per
  unsettled shape, never per pile of queued work.
- Q1 turned out to be miscut. Asked as "which reader is readable
  about", it was answered by looking at two real passages: the
  definition's fences read fine, SL-2's G1 had to be worked at.
  Same voice, same week, same author. So the fault is not the voice
  but the passages that pack an argument into one block, and the
  pass is small and targeted rather than a rewrite of everything.
- `option-comparison` built five wordings against that same G1.
  What building caught, which arguing would not have: the failing
  passage **already carried labels** — *Attack:*, *Guarantee:* — so
  labelling was never the missing piece; one claim per sentence is
  longer, choppier and flattens the fences; and a requirement
  turned out to be the consequence of the real one. The winner was
  labels plus a worked example with real numbers, marked `*Say:*`
  on its own line, which is the reviewer's refinement.
- `visual-comparison` settled the Owners section by rendering. The
  table died on a measurement: a four-column row is 435 characters
  on one line, which an editor cannot show and a line diff marks
  whole when one word changes — the review-between-versions problem
  made worse. Blocks with a rule between them won.
- Then the question that ate the rest of the session: where does a
  thing like that live, and when may it be read. It landed as a
  mechanism rather than a note — a shape sits in `.claude/shapes/`
  where nothing loads it, or in `.claude/rules/` with its `paths:`
  where it loads while the matching artifact is written, and the
  directory is what decides whether it is in front of the writer.
  `artifact-kinds` gained a **shape** entry for it: force that is
  positional, which no other kind has.
- What the reviewer overturned, all four of them mine: a shape
  written at a kind's second instance (it would not have described
  how this project's own first shape was born — from a rebuild
  judged worth keeping, nothing compared); a promotion counter
  (exposing a shape is a judgement, and a count only prompts the
  question); deleting a delivered copy after its gate (keeping it
  is what lets the collector reconcile); and "keep it concrete so
  it cannot travel", which confused a status with a wording — the
  protection is that a shape is offered as a finding, not that it
  is phrased vaguely.
- One correction worth keeping for its own sake: the first version
  of the close-time rule said it runs at the close so the output is
  written independently of the shape. That reason is false inside a
  project. This run read SL-1's record before writing SL-2's and
  matched its form without once asking whether SL-2's material
  wanted another. The reason that survives is different: what is
  written from a shape follows the shape, and what is written from
  the slice follows the material.
- PLAN gained a step form to copy and default gate items — two at
  the opening, seven at the close — after the reviewer noticed the
  same lines were being retyped into every step's gate. Earlier
  steps are not reworded: their ticks are a record of what was
  verified in the words the reviewer approved.
- The drafts were discarded at the end, as `decide-first` and
  `option-comparison` both say to. The one finding that lived
  nowhere else — labels were already there and the numbers did the
  work — went into the decisions log first.
- Unfinished, and named so it is not mistaken for done: the sweep
  itself. SL-1's record, README, the manuals and the two comparison
  tables in SL-2's §8 are untouched. The pass produced the rule and
  applied it to one record.
- Resume: three things before SL-3, each on its own branch — the
  sweep's remainder, the review-between-versions question, SL-1's
  per-test references. Then Step 7, SL-3, which inherits an
  operator with no recourse and is the first slice to meet the
  close-time shape check. Owed to the bundle: four hand-offs in
  TODO, one of them asking it to be the collector.

## 2026-09-21  (Step 6: SL-2 built and closed)

- The build ran as a commit plan on `step-6-sl-2`, eight commits
  planned and one revision at commit 4's boundary. What it proved:
  nothing in the application changed. The conditional statement and
  the `item_never_oversold` constraint stand from SL-1, built there
  against a race and justified here against an honest request; the
  door's value shape (ADR-0011) kills the resend with no duplicate
  check anywhere. The slice's whole delivery is evidence and two
  structural guards. 29 tests at the branch point, 39 at the close.
- The reds, from actual output. E1, E2 and E4's evidence half: with
  the `WHERE` guard out of `Ledger.adjust` and the constraint out
  of V1, the witness read `Numbers[onHandCount=7, held=8,
  activeSum=8, reservations=1]` — eight held against seven on hand,
  the negation created. E3: its wall is the door's shape, not a
  guard, so the absence was delta semantics — the statement made to
  add rather than assign — and the count read 19 where 9 was
  asserted. E5: nothing to remove, so each forbidden thing was
  planted — an `asOf` at the door gave `["onHandCount", "asOf"]`, a
  second method taking a count gave "expected size 1 but was 2", a
  `JdbcClient` on the controller was named by the rule itself.
  Every removal and plant lived on the working tree for one run.
- E1's first red was worthless and was thrown away: it failed on
  the status code, *expected 409 CONFLICT but was 200 OK*, and
  stopped before reading the store. A red that proves the test
  reads status codes is not a red that proves it watches the
  promise. The assertions were reordered — witness first, door's
  answer after — and the second red carried the numbers. The order
  of assertions turned out to be part of the evidence.
- E4 was two things wearing one name, and the red run is what
  separated them: with the wall absent, the late-correction case
  reddened and the either-order case could not. L4 had said so
  already — kill 8 is "kill 6 if the older is lower; otherwise W1"
  — and the signed specification's E4 had not carried the split.
  The half that cannot kill is now a tripwire that says so on
  itself. Nothing in §4 or §5 was changed: the guarantee and the
  criterion stand as signed, and this is how the criterion divided
  when it met the code.
- The reviewer could not tell what that test was for, and the
  answer took several rounds of chat. Two lessons, both taken. In
  this repo: every test in both files now carries what it means in
  plain words beside its reference — the id line points at the why,
  the prose says what it checks and what would trip it, and the why
  itself stays in the record so the two cannot drift. In the skill:
  `cbc-slice` corrected in place under rule 2 (decisions log, this
  date), with the counting rule that keeps the second lesson from
  being a hole — a tripwire never discharges a kill, every kill
  still owes a test seen red, and the red run decides which kind a
  test is rather than its author.
- The plan's one revision: a commit added for that skill edit. The
  first pass had filed the finding as a prose hand-off in TODO's
  Later, which rule 4 forbids — the hand-off is one line, and the
  diff and the log are what the source reads. The prose was removed
  before the commit it sat in landed.
- Deviations from the spec and the plan: none in substance. The
  plan said the red for E3 would come from the guard's absence;
  it could not, the guard having nothing to do with a resend, and
  delta semantics was the honest absence instead. The plan said
  commit 6 would carry the TODO hand-off line; it could not, TODO
  being a project path and that commit agent-scoped, so the line
  went with the records.
- Version 0.2, two of four invariants evidence-closed. The registry
  re-decided the ordering and kept it: SL-3 next, carrying what
  SL-2 hands it by name — an operator who has counted the shelf and
  found fewer units than are held has no recourse in this system,
  and whether an exit should exist for them is the exits slice's
  question, not one this slice would invent.
- Resume: Step 6's gate is closed but for its branch item, which is
  ticked on the reviewer's word to merge. After the merge, two
  passes wait in TODO before SL-3 — the writing one and the
  review-between-versions one — and their order is the reviewer's
  to set when Step 7 opens.

## 2026-09-21  (Step 6: SL-2 specified and planned; the language finding)

- The slice moved: the specification written and committed unsigned
  first, so any change would read as a diff (662e97d); the registry
  row flipped to `in-progress` the moment it landed, the first time
  in this project's life a row has said so (7f3860d); the flag zero
  added — SL-2's row carries none, said out loud because the gate
  asks for it and the first draft passed over it — and the
  specification signed 2026-09-20 (60291b4); the plan written and
  committed unsigned (be14e7c), its sign-off still open.
- What the plan found: SL-2 adds no production code. The
  conditional statement and the check constraint stand from SL-1,
  built against a race and justified here against an honest
  request; ADR-0011's value-not-delta door kills the resend with no
  duplicate check anywhere. What the slice adds is evidence, plus
  two structural tests where a guarantee is held by an absence —
  no ordering state, one writing path — that nothing today would
  notice disappearing. Two faces were put up before the choice, as
  the skill asks: how a resend is made harmless (value semantics,
  an idempotency key, deltas-plus-key) and whether the ledger
  should know the order (no ordering, an operator-supplied instant,
  a ledger-assigned sequence).

### The language finding, and what is deferred

- The finding has two faces and they are one problem. First, the
  records: the reviewer could not read the owners table without
  decoding it. Second, this session's chat: the answers that
  explain the records need decoding too, and the reviewer has had
  to ask for a simpler version of nearly every one. An artifact a
  reader must decipher and a reply a reader must decipher are the
  same defect in two places, so the pass decides the rule for
  both — the written records and how the work is explained while
  it happens. The diagnosis, from looking at the cells: three
  causes, stacked. One, the house voice
  this run has written in since SL-1 — terse and literary, em-dashes
  for connectives, abstract nouns as subjects — which reads well in
  the intent's short standalone sentences and badly in a dense
  table. Two, every cell argues three things at once (what the wall
  is, why it beats this adversity, what happens if the wall is
  wrong) with no separator, so the reader must split before they
  can read. Three, every noun is definite and assumes the reader is
  holding it in memory — "the statement", "the row", "the held
  units" — three per sentence, with no numbers anywhere to anchor.
- Asked whether plainer records would cost the agent anything. The
  answer given: no. Precision lives in the vocabulary and the
  references, not in sentence length; three claims in one sentence
  let a qualifier attach to the wrong claim for any reader. What
  would cost something is deleting the defined terms, the fence and
  kill numbers, or the reasons — that is not simplification.
- Turned down, so the pass need not re-run them: a second, plainer
  set of records beside these (two masters for one fact drift, and
  the project holds one master per fact); marking these as
  agent-only and writing a human version (there is no dialect the
  agent reads better). The argument that settled it: the intent
  names a human reader as this system's audience, so a record that
  must be deciphered fails the job it was written for.
- A writing rule was drafted into the entry file's local rules and
  a decisions entry written for it, then reverted unstaged on the
  reviewer's call: nothing changes mid-slice, or SL-2 ends half in
  one voice and half in the other, and the decision deserves its
  own pass rather than a stopgap written in the very voice it is
  trying to fix. The draft's own text proved the point — "never
  stacked into one period" needs decoding.
- What the rule would have said, kept here so the pass starts from
  something: one idea per sentence; the parts of an argument
  labelled (what the wall is, why it beats this attack, what
  happens if it fails) rather than joined; every defined term and
  every reference kept whole; one worked example with real numbers
  per mechanism; argument prose and argument-carrying tables
  governed, lookup tables — kill ↔ slice, a records index — left
  alone.
- Where recall lives, since the question was asked: TODO's item
  under "Next (after SL-2 closes)" is the index and this entry is
  the detail. `temp/` was considered and refused — it is untracked
  rather than ignored, it has been deleted at step boundaries
  before, and a bare `git add -A` has nearly swept it into a commit
  once already.
- Resume: the plan's sign-off (§8) is the only thing between here
  and Stage 3. Then the build: no production code expected, the
  evidence for E1-E4 each seen red with the wall made absent on the
  throwaway store before green, and the two structural tests for
  the absences. The writing pass comes after the slice closes, not
  during it.

## 2026-09-20  (Step 6 opens: SL-2, Stage 0)

- Step 6 opened on `step-6-sl-2`, cut from main at adc90f6. The
  gate was derived and committed (ccf7b6a) before any work, from
  the slice skill's stages, SL-2's registry row and TODO's Step 6
  item. Three things make it unlike Step 5's: R5 is answered here
  rather than deferred, a wall already standing; the row's flip to
  `in-progress` at the specification is a gate item, the rule this
  run harvested from SL-1 and the bundle wrote back at 7bbf49a;
  and no ADR is owed at the opening, because the registry says the
  correction's shape is the specify step's to decide.
- Stage 0, against the actual repo, not recall. R1: the three
  exports stand, SL-2 the only `chosen-next` row, the
  reconciliation line whole — 20 kill rows for the 20 it claims.
  R2: `./mvnw test` at the branch point, BUILD SUCCESS, exit 0, 29
  tests, 0 failures, 0 errors, 0 skipped — the same 29 SL-1 closed
  on. R3: the suite's store is `ThrowawayStore`, a real postgres:17
  per JVM off the ground's own bootstrap.sql, migrated as
  `migrator` from the one migrations home; no mock. R4: the door
  already carries `POST /items/{item}/adjustments`, driven
  sequentially by `ReservationDoorIT` with `Witness.read` reading
  the persisted numbers — F9 is one call, F11 the same bytes twice,
  F13 two calls in a chosen order, no harness work owed; SL-1's
  instance-forking machinery is not what this slice needs. R6: the
  registry writable, the record scheme in place.
- The ground was down when the session opened — the container
  stopped, the named volume intact. Brought up per the operator
  manual: healthy, `select 1` as `runtime`, `flyway info` showing
  V1 applied 2026-09-14. The suite never needed it; it is up so the
  door can be met the manual's way.
- What Stage 0 surfaced for Stage 1, not for the harness: an
  adjustment carries no identity — `AdjustRequest(Integer
  onHandCount)` and nothing else — so nothing at the door tells a
  resent correction from two honest corrections that agree. That
  is F11's question itself, and the specification owes the answer.
- R5 is owed to the build, the wall named: `item_never_oversold
  CHECK (reserved <= on_hand_count)` in V1, with
  `ReservationDoorIT.anAdjustmentUnderTheHeldUnitsIsRefused` the
  provisional refusal SL-1 left. Each evidence test is seen red
  with it absent on the throwaway store, from actual output, and
  that state never lands in history.
- Readiness signed by the reviewer: *2026-09-20 — ready for SL-2,
  signed off.*
- Resume: Stage 1 — the correctness specification for SL-2, its
  invariant and adversity taken from the registry row and L4's
  kills 6, 7 and 8 as written; the correction's shape decided
  there, both parked shapes weighed; the row to `in-progress` when
  it lands; the reviewer signs the spec before the plan.

## 2026-09-20  (housekeeping: the conventions re-pin @ 6f2be1d)

<!-- Between steps: SL-1 closed 2026-09-14, SL-2's branch not cut.
     The three housekeeping sessions before this one — the bundle
     re-pin of 2026-09-15, the in-place answer of 2026-09-17, the
     re-pin of 2026-09-18 — have no entry here; the decisions log
     is their record, and the gap is noted rather than back-filled. -->

- The bundle's second delivery as a note and a copy, staged in
  `temp/`. Read the note, then checked its claims rather than took
  them: `diff -rq` on the five method skills and `docs/concept/`
  empty, as it said; the four held conventions diffed against the
  staging, as it said — one line in commit-messages, "the
  deliverer" through convention-lifecycle §2 and §3, change-plans
  renamed commit-plan with a new opening paragraph; three new,
  decide-first, option-comparison, visual-comparison. Found what
  the note had not: five lines in four delivered files still saying
  "change-plan" — the very grep-the-whole-span lesson its §4
  records from our first gap.
- The note's first §5 read as claims about the bundle's records —
  "already in the playbook", "we hold the same finding" — none
  checkable here. Said so; the reviewer sent it back; the note came
  again with §5 as verdicts, each line a decision taken in the
  note itself with nothing behind it to verify, and the staging
  re-hashed 9041d00 → 6f2be1d with every file byte-identical. The
  branch renamed to match before anything committed.
- Ran as a commit plan on `housekeeping-bundle-6f2be1d`, eight
  commits, one revision, each boundary shown and committed on the
  word: the plan; the take (seven copies, change-plans deleted by
  name, the in-flight plan file renamed with its convention, the
  records-table row, the pin entry — 6f2be1d, ba7eaa4 as
  provenance in words); the five-noun edit; rule 2; the TODO's
  verdicts; the hand-off; the close. Fast-forwarded into main.
- The revision, reversed before the step committed: step 3 had
  each edited copy gaining a dated header comment, as the received
  convention and our own rule 2 ask. The reviewer: a comment in an
  artifact says how to use it or what a part is, never what changed
  — that is history, and the diff against the take and the
  decisions entry already hold it. Taken. The comments were written,
  looked at, and removed unstaged; rule 2 dropped its line in a
  step of its own so one process serves every copy; the hand-off
  asks the bundle to drop it from both texts. This run's first
  in-place edit of a copy, and the first finding it produces is
  against the rule it runs under — the report owed to the handbook
  is armed on it.
- What the note's §5 did to the Later list, in its own words there:
  three gaps and the playbook fold-back discharged; the entry-file
  half accepted by the bundle; the Spring reference's
  hand-after-the-build rule withdrawn, nothing handed at a slice
  close, ever; framing-as-commit-series and the imperative test
  held at the bundle with one trigger, the next time that file is
  opened or a retrospective.
- `decide-first` did not fire: the commit count was sayable, six
  then seven. `commit-plan`'s new paragraph — it plans the commits,
  not the change — was exactly this set's case: everything settled
  in the note and the conversation before the plan opened, and the
  one thing that was not settled forced the one revision.
- Same day, later: a second note and a bundle at 4c3ac99, a few
  hours after the first, answering the morning's hand-off. Checked
  before taken: one file differs, convention-lifecycle, as the note
  said — the header-line clause gone, this run's argument in the
  rule as its reason; the five-noun edit in the masters in our
  wording, the three other edited copies byte-identical to the
  staging. So this run's first in-place edit went through a re-pin
  and was taken whole, the trial both texts were provisional on.
  Taken on `housekeeping-bundle-4c3ac99` as two commits, agent and
  project, no plan: the take with the rules file's header and rule
  6 corrected, then the TODO.
- The note's other half, a map correction: the handbook has not
  been this project's upstream since 2026-09-18 — the bundle owns
  the kit and the conventions, told the handbook, and nothing here
  reaches it any more. Unverifiable from here, and the note says
  so; the bundle is the only party this repo can reach, so its word
  on the channel is what the records now hold. Cleared: the report
  owed on ADR-0038 (discharged — the edit was the report), the
  cut-a-kata debt (kept, as the learner's own, same trigger), the
  morning's hand-off (both asks taken). The note named two items
  and there were three — the four-pieces item still folded three
  trials back to the handbook; re-addressed to the bundle, and one
  line back says so. Third time the whole-span lesson has fired on
  a note from them; each time the missed one was in our prose, not
  their files.
- Resume: fast-forward `housekeeping-bundle-4c3ac99` into main on
  the word, delete the staging. Then Step 6 (SL-2, the correction
  never undercuts the holds): cut `step-6-sl-2` from main, derive
  its gate into PLAN first, run cbc-slice on the conventions now
  held — its specification decides the correction's shape, refuse
  or let end, which SL-1 left provisional. No reference in hand, as
  SL-1. Say in TODO if SL-2 leans on the facility paragraph. The
  reviewer pushes main when wanted.

## 2026-09-12  (Step 5: SL-1, no over-admission under contention)

<!-- Grows as the stages run — the lived-result record: for every
     executing step, the command, what was expected, what actually
     happened. Decisions with their options are ADRs. -->

- Opened on `step-5-sl-1`, cut from main at `be61f60`; the gate
  derived from the slice skill's five stages, SL-1's registry row
  and TODO's three Step 5 items, committed before any work
  (`6d8f373`). The gate names no mechanism: WHAT before HOW, the
  wall is Stage 2's.
- Stage 0, readiness on the actual repo, each check, expected,
  actual:

  | Check | Expected | Actual |
  |---|---|---|
  | R1 the three exports | intent, definition, registry under `docs/system/`; SL-1 `chosen-next`; the reconciliation line whole | all three present; SL-1 `chosen-next` (2026-09-10); 20 kills ↔ 4 slices + 3 folds, 20 rows in the table |
  | R2 the suite | `./mvnw test` green at the branch point | 7 tests, 0 failures, exit 0: context 1, migration path 3, health through the door 1, in-process burst 1, race across 3 instances 1 — nothing exported, the ground down |
  | R3 the real store from tests | the miniature, not a mock | `ThrowawayStore`: `postgres:17`, the ground's own `bootstrap.sql`, migrated as `migrator`, connected as `runtime` (Step 4) |
  | R4 contention across instances | the harness creates SL-1's adversity class | `InstancesRaceIT`: 3 forked instances, 120 requests at one instant, every pid among the 3 (34 s) |
  | R5 the harness can fail | — | not answerable yet: no wall exists to break. Answered in this step's build, the evidence run red against the admit without its wall, before the wall lands (the gate says so) |
  | R6 the record surfaces | registry writable; a place for deviations and sign-offs | writable; the devlog for deviations and the walk, ADRs for decisions, the entry file's table naming both |

  Observation, not a blocker: the ground's container
  `never-oversold-postgres` is `Exited (0) 6 hours ago`; the suite
  needs it down or up alike, and the run proof at close will need
  it up. The migrations home holds `.gitkeep` alone — the first
  migration is this slice's.
- 2026-09-12, ready for SL-1 — the reviewer's sign-off, given in
  so many words after the table above.
- Two decisions at opening, before the specification: ADR-0010, the
  door's conventions (resource paths, invalid 400 / unknown 404 /
  refused 409, Problem Details, the record as persisted); ADR-0011,
  an item becomes known by its first adjustment, the adjustment
  stating the count. Both confirmed by the reviewer.
- Stage 1, the specification at `docs/construction/sl-1-no-over-
  admission.md`: six guarantees by attack, zero mechanisms
  (checked by grep for lock, constraint, transaction, row, table,
  cache, queue: no hit); kill 9 shaped as "no interval to kill",
  kill 10 as "FC3 removes it". Signed 2026-09-12. Stage 2, the plan
  as §7 of the same record: the store's check constraint and one
  conditional statement as the wall; faces not chosen and escape
  hatches written; three provisionals. Signed 2026-09-13. A
  numbering slip (the plan landed as §8 before §7) fixed before
  the plan's commit.
- Stage 3 as the change-plan (`79a7239`), eight commits. Commit 2,
  V1 — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw test`, the migration-path tests | V1 applied in the miniature as `migrator`; applied ≥ 1, none failed; the wall in the catalog; `runtime` writes both tables | `Successfully applied 1 migration … now at version v1`; 5 of 5 green; the constraint read back as `CHECK ((reserved <= on_hand_count))` |
  | the whole suite | green | 9 tests, exit 0 |

- Commit 3, the doors — the reviewer asked for navigation inside
  the feature package and the layout was decided before the
  commit: values and answers in public sub-packages, behaviour and
  writes at the root, package-private; a cycle between the two
  sub-packages found and removed (`UnknownItem` takes a string);
  ADR-0008 gains the dated note; the package carries its map in
  `package-info.java`. The change-plan said "one package-private
  package" — a divergence for the close. Verified: 22 tests green
  from clean, the door's fifteen among them, nine nonsense shapes
  each `400` with the numbers untouched.
- **The red run**, before commit 4, on the working tree only:
  V1's `item_never_oversold` constraint removed, the admit still
  the naive read-check-write of commit 3, E1 and E2 run:

  | Test | Expected | Actual |
  |---|---|---|
  | `ReserveStormIT` — 100 requests, one instance, 20 on hand | red: the witness shows an oversell | **active sum 22, on-hand-count 20** — `[active sum ≤ on-hand-count] Expecting 22 to be less than or equal to 20` |
  | `InstancesStormIT` — 120 requests across 3 instances, 20 on hand | red | **active sum 23, on-hand-count 20** |

  Two of two red; the harness can fail (R5). V1 restored from the
  index; `git status` showed only the three new test files.
- Commit 4, the wall — the admit as one conditional `UPDATE`,
  the store's row count the decision, the reservation inserted in
  the same transaction; the tests unchanged:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw clean test` | the same two storms green, all else green | 24 tests, 0 failures, exit 0; `ReserveStormIT` 1.5 s, `InstancesStormIT` 23 s |

  Every reply in both storms was `201` or `409` — none of the
  constraint errors commit 3's naive admit would have produced;
  the admitted count equalled the units held; every admitted id was
  in the store; the sampler read the witness throughout and saw no
  violating state.
- Between commits 4 and 5, on the reviewer's questions: the
  assertion style (substring cannot tell 3 from 30) decided as a
  step added to the change-plan (`ba227aa`), applied after commit
  6; the feature's layout question answered at commit 3 (ADR-0008's
  note). The reviewer's practice notes (kata cards) live outside the
  project; a hand-off for the handbook rides with commit 8.
- Commit 5, the adjustment racing the admits — E3, five rounds on
  fresh items, thirty reserves and one downward correction (20 → 10)
  released at one instant, the witness sampled during:

  | Round | The correction | Admitted | After |
  |---|---|---|---|
  | 1 | `200` admitted | 10 | count 10, held 10 |
  | 2–5 | `409` refused | 20 | count 20, held 20 |

  Both outcomes seen; in each the correction's answer agreed with
  the state and no sampled state broke the invariant. The race
  decided who won (W3); the constraint decided that the loser lost
  correctly. Whole suite from clean: 25 tests, exit 0.
- Commit 6, one clock — E5 in two halves. Behavioural,
  `OneClockIT`: a 15-minute hold's persisted expiry sits exactly
  `00:15:00` from the store-stamped creation instant (the store's
  assignment); a one-second hold and a ten-minute hold on one item
  read as active sum 2, and 1.5 s later as active sum 1 with the
  counter still 2 — the store's clock judged, the counter over-
  approximates on the safe side until SL-3 ends the hold.
  Structural, `NoInstanceStateOrClockTest`, on ArchUnit 1.5.0 at
  test scope (earning reason in the pom): rules on the compiled
  application classes — no access to any `now()` in `java.time`, no
  dependency on `Clock` or `Date`, no `System.currentTimeMillis` or
  `nanoTime`; no field of a map, collection or atomic type; no
  mutable static. **A dead end on the way:** the first version was
  a regex over the source text under `src/main`. It caught a planted
  `Instant.now()` and missed a planted `HashMap` field (the pattern
  refused a `(` before the `;`); re-anchored on indentation it caught
  both, and the reviewer said it did not feel like something
  developers write. It was not: a text rule misses a static import,
  a method reference and any reformat. Replaced by ArchUnit before
  the commit. Its first draft used `callMethodWhere`, which sees
  calls only — a planted `Instant::now` method reference walked
  through; switched to `accessTargetWhere`, which sees both. Then
  four plants one at a time, each caught by its own rule: the
  `Instant::now` reference, a `System.nanoTime()` call, a
  `static int`, a `HashMap` field; plants removed, `src/main` clean.
  Whole suite from clean: 29 tests, exit 0.
- Commit 7, assert bodies by path — the step added at commit 4's
  boundary. `json-path` declared at test scope with its reason (it
  already rode in through the Boot test starter; declared so the
  use is visible and survives a starter change). A `Body` helper in
  test support parses once and reads by path; every substring
  assertion in the door test, the health test and both storms
  replaced — twelve `contains` and two id regexes gone; the storms
  take the reservation id from `$.id`. No behaviour change: 29
  tests from clean, exit 0. The convention, for the slice record:
  a shape is a path; a word (an error title) is a path too now, so
  no `contains` remains to make the exception.
- Commit 8, the records — the run on the real ground first, on the
  reviewer's word to run the compose commands (2026-09-14), each
  command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `podman compose up -d`, wait for healthy | the ground up | `never-oversold-postgres` healthy after 1 s |
  | `podman compose run --rm flyway migrate` | V1 applied as `migrator` | `Migrating schema "never_oversold" to version "1 - item and reservation"`, `Successfully applied 1 migration … now at version v1` |
  | `.env` exported, `./mvnw spring-boot:run` | Java 21 announced; UP with `db` UP | `using Java 21.0.11`, `Started … in 3.945 seconds`; health `UP db UP` |
  | adjust `proof-item` to 3 | `200`, the item as stored | `{"id":"proof-item","onHandCount":3,"reserved":0}` |
  | reserve 2 for `PT5M` | `201`, the reservation with the store's expiry | `201`, `"expiresAt":"2026-09-14T16:50:12.033145Z"` |
  | reserve 2 more | `409` refused, the reason | `409`, `"2 units of proof-item do not fit: 2 held of 3 on hand"` |
  | quantity 0 | `400` invalid | `400`, `"quantity must be a whole number from 1 to 1000000"` |
  | read as `runtime` (via the container: no `psql` on the host) | count 3, held 2, one reservation of 2, V1 success | `proof-item\|3\|2`; `1\|2`; `1\|item and reservation\|t`; both tables present |

  The ledger stopped; port 8080 free. Then the records: the slice
  record's §8 evidence (each guarantee, its owner, its test, what
  it created and read, the red before the green) and §9 standing
  guards; the registry — SL-1 closed, SL-2 chosen-next, the
  ordering re-decided and kept by a dated revision with what SL-1
  leaves to SL-2 and SL-3 by name, kill 10's flag resolved;
  CHANGELOG `[0.1] — 2026-09-14` with the doors in user-speak, the
  pom at `0.1-SNAPSHOT` (a version is a state of the evidence; the
  reviewer confirmed 0.1); README — the invariant closed with its
  record linked, a Use section with the three curls and their
  actual answers, the migrate command in Run, the slice-records
  row; ARCHITECTURE with the wall in the diagram and SL-1 in the
  invariants; TODO — Now points at Step 6, the two Step 5 items
  closed, two SL-3 items and five hand-offs filed (cbc-slice ×3,
  cbc-bootstrap, the handbook's kata skill); PLAN's gate ticked
  from the delivered state.
- Certification, member by member against the delivered files:
  §1–§2 as the registry writes them; §3 six guarantees each with an
  owner in §7 and evidence in §8; §4 E1–E6 each a test that
  creates its adversity and reads the store — E4 riding on E1/E2
  with no interval to kill, E5 in two halves; the red run on
  record; the sampler's "in every readable state" on E1, E2, E3;
  §5 FC1 at the door (nine shapes), FC3 by the store's clock; §6
  respected — no correction shape decided, no exit, no identity,
  no fairness claimed. Constraints visible: `runtime` alone in
  configuration; `migrator` only in the harness and the compose
  one-shot; no ORM, no in-app migration; V1 the only DDL, no
  GRANT in it; every dependency with its reason. 29 tests under
  `./mvnw test`, nothing exported, the ground not required up.
- Exit test: the adversity-creating tests exist and pass — yes,
  E1 and E2 storms, E3 race, and each was red without the wall.
  The invariant demonstrably survives concurrent admits from many
  callers and from three instances — yes, the witness read from
  the store from outside them all. **The standing guards named at
  close** (slice record §9): erosion by later slices touching the
  two tables, the constraint dropped while the storm stays green,
  a second write path, the counter drifting from the rows.
- Resume: the change-plan's close (agent-scoped), then fast-forward
  `step-5-sl-1` into main on the word; the reviewer pushes; the
  reviewer's questions held back until the merge are due here.
  Then Step 6 (SL-2, the correction never undercuts the holds):
  cut its branch, derive its gate, run cbc-slice — its
  specification decides the correction's shape.

## 2026-09-11  (Step 4: skeleton & bootstrap)

<!-- Grows as the stages run — the lived-result record: for every
     executing step, the command, what was expected, what actually
     happened. Decisions with their options are ADRs. -->

- Opened on `step-4-bootstrap`, cut from main at `143d00e`; the
  gate derived from the skill's five stages, the registry's SL-1
  and TODO's Step 4 items, committed before any work (`b70e792`).
  The gate names no stack: WHAT before HOW, the stack is Stage 1's.
- Stage 0, readiness on the actual repo: the three exports stand,
  exactly one slice `chosen-next` (SL-1); both manuals stand — the
  contract with the identity table, the reach table, four refusals
  and the one DDL path, the manual with six "seen here" results
  from 09-11; no source file, no build file anywhere in the tree;
  `infrastructure/flyway/migrations/` empty; `.env` present and
  ignored, `.env.example` committed. Passed.
- Stage 0, the ground live — found **down** at opening: the
  container `never-oversold-postgres` was `Exited (0) 18 hours
  ago`, the volume `never-oversold_postgres-data` in place. An
  observation, not a blocker: `podman compose up -d` per the
  manual, healthy after 6 s, data intact (roles, schema, grants as
  bootstrapped). Then each check, expected, actual:

  | Check | Expected | Actual |
  |---|---|---|
  | `pg_isready` | accepting connections, exit 0 | `accepting connections`, exit 0 |
  | one real query as `runtime` | `runtime`, `never_oversold`, 17.x | `runtime\|never_oversold\|PostgreSQL 17.11` |
  | catalog check, 6 queries | as the file's comments | all six as stated: two roles, neither super, both login; db owned by `postgres`; schema by `migrator`; `runtime` USAGE t CREATE f; default privileges tables SIUD, sequences US, none grantable; CONNECT `migrator` t, `runtime` t, PUBLIC f |
  | `podman compose run --rm flyway info` | `No migrations found` | `No migrations found` |
  | witness read from the host, published port, as `runtime` | `1` | `1` |
  | C1: `CREATE TABLE` as `runtime` | `permission denied for schema never_oversold` | exactly that |
  | tables in `never_oversold` | 0 | 0 |

  Seven of seven. The ground is up now, which is what the wiring
  needs. Nothing to hand off from Stage 0.
- Stage 1, decided with the reviewer, before any code: the stack
  (ADR-0007 — Spring Boot 4, Java 21, Maven; fluency and audience;
  the host already carries JDK 21.0.11, Maven 3.9.9, the podman
  socket and a Testcontainers properties file from the earlier
  project); the group `io.github.edgaras87`, given, not invented;
  the structure (ADR-0008 — the lived default, after the reviewer
  asked for the alternatives and their fit: classic layered is the
  same layering with the compiler removed from the boundary,
  hexagonal would centre the naive check-then-write shape,
  Modulith has one module to verify); the harness shape (ADR-0009
  — plural instances as separate processes, the one place this
  run leaves the harness reference by decision). Kill 10's clock
  deferred to SL-1 by name. The requirements document's home:
  `docs/construction/`, not the skill's `internal/` — hand-off
  filed in TODO.
- Stage 2: `docs/construction/bootstrap-requirements.md` composed —
  §1–§6, no dependency, class or file layout named; the ground by
  pointer; the port and password as "env var X, default Y".
- Stage 3: the step plan as this run's change-plan, confirmed and
  committed (`ee558e5`); nine commits, the plural-instance race
  provisional.
- Commit 2, the skeleton — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | Initializr, `bootVersion=4.1.1`, `web,actuator` | a tarball | first pull truncated (gzip: unexpected end of file); second pull `http 200`, 10 037 bytes, extracted |
  | `./mvnw -q -B test` | context test green, no store | `Tests run: 1, Failures: 0`, Boot `v4.1.1`, `using Java 21.0.11` |
  | `./mvnw spring-boot:run`, then `curl localhost:8080/actuator/health` | Java 21 announced, health UP | `Starting … using Java 21.0.11`; `Tomcat started on port 8080`; `HTTP/1.1 200` `{"status":"UP"}` after 9 s |

  Extracted into the root beside `infrastructure/`: wrapper and
  sources copied; the pom rewritten per the convention — two
  capability groups, each with its why, the Boot minor pinned
  once, the plugin commented, the empty metadata blocks gone; the
  version `0.0-SNAPSHOT` because a version is a state of the
  evidence; `application.properties` replaced by a YAML carrying
  the name only; `HELP.md` and Initializr's `.gitignore` dropped,
  their useful lines merged below the overlay markers in this
  repo's own `.gitignore` and `.gitattributes`. Boot 4's
  Initializr already names the web starter `webmvc` and pairs the
  `-test` companions — the walkthrough's rename trap did not bite.
- Commit 3, the datasource as `runtime` — each command, expected,
  actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw -q -B test`, nothing exported, ground up | green — the pool connects lazily | `Tests run: 1, Failures: 0` |
  | `.env` exported, `./mvnw spring-boot:run`, `curl …/actuator/health` | health UP, `db` UP | `200`, `"db":{"status":"UP"}` among the components, after 8 s |
  | `pg_stat_activity` while it ran | sessions as `runtime` only | `runtime \| PostgreSQL JDBC Driver \| 4`; none as any other identity |

  One thing the reference leaves open: the plain context test starts
  a context whose password placeholder has no default, so it must
  resolve from somewhere. Given as a test property with a value
  that says what it is (`unused-nothing-connects`) — not a test
  profile, and not a default in the config. The companion
  `spring-boot-starter-jdbc-test` enters with its starter under the
  pairing rule; the driver at runtime scope; no object mapping.
- Commit 4, the harness — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `podman compose stop`, then `./mvnw -q -B test`, nothing exported | green with the ground down; `*IT` run under the one command | `postgres:17` started in 6.5 s; history table created, `No migrations found`; **1 failure of 5** — mine, see below; `MigrationPathIT` and `HealthThroughTheDoorIT` listed in the surefire reports |
  | `./mvnw -q -B test` after the fix | 5 green | `MigrationPathIT` 3/3, `HealthThroughTheDoorIT` 1/1, context test 1/1, exit 0 |
  | `podman ps` ~12 s after the JVM exit | no throwaway left | none running |
  | `podman compose start` | healthy | healthy after 2 s |

  The failure: a third test I added beyond the reference —
  `runtime` attempting `CREATE TABLE` in the miniature, the
  ground's refusal 1 held in evidence runs. It asserted the message
  on the top-level exception; Spring wraps the driver's error, so
  the store's `permission denied for schema never_oversold` sits at
  the root cause. Fixed by asserting on the root cause. Kept: three
  lines that catch a miniature quietly wired without the split.
- The walkthrough's Ryuk trap is stale on this line: Testcontainers
  2.0.5 (Boot-managed) reads no `ryuk.disabled` key — the config
  keys are image, privileged, timeout — so the line in
  `~/.testcontainers.properties` is inert; and Ryuk ran fine under
  rootless podman 5.8, reaping both throwaways within seconds. Kept
  Ryuk on; the manual says so; hand-off filed.
- The web base lands here with a user, not as an empty class:
  `HealthThroughTheDoorIT` takes the world-is-up check through the
  real door against the harness's store — the check every evidence
  run gates on — and that is what earns `spring-boot-restclient` at
  test scope. Boot 4 with only `flyway-core` on the test classpath
  runs no Flyway auto-configuration (the module split), so the
  harness's own call is the only migration path in tests.
- Commit 5, the probe pair — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw -q -B test` | 6 green, the hundred released at one instant all `200` with `runtime` | `ContentionProbeIT` 1/1 in 18 s, the rest as before, exit 0 |
  | `.env` exported, run, `curl localhost:8080/probe/ground` | `200`, identity `runtime`, a pid | `200`, `{"identity":"runtime","pid":"…"}` |

  Imitated from the reference, not pasted: the probe answers the
  identity the store sees and, beyond the reference, the process
  id — so commit 6 can assert that the race was served by distinct
  processes. Both files say in their own javadoc that they are
  scaffolding and die with SL-1, and the test says what it does not
  prove: overlap in the store, and anything about more than one
  instance.
- **Correction to commit 3's note, 2026-09-12.** The claim that the
  context "must resolve the placeholder or refuse to start" was
  wrong, and I stated it without running the check. Found through
  the reviewer's own run: `./mvnw spring-boot:run` from a terminal
  with nothing exported *started*, health `DOWN` with `db` `DOWN`,
  the probe `500`, the store's log `password authentication failed
  for user "runtime"` — Boot's configuration binding leaves an
  unresolvable placeholder as the literal string, so the literal
  became the password. Verified the other way: the context test
  with the property removed and the variable unset, exit 0. The
  test property is dead; removed, with the javadoc corrected. The
  five options the reviewer chose from rested on a false premise;
  none was needed.
- The lived trap that came out of it: **a missing secret does not
  stop the ledger**. It starts, and only health says the store is
  down. The README's Run section must say "health `db` DOWN means
  the environment is not exported"; whether the ledger should
  refuse to start without its secret is a *what* not in the
  requirements — filed in TODO for the reviewer, not decided here.
- Commit 6, the race across instances — the realization decided at
  this boundary, as the plan reserved: **forked JVM processes from
  the build's own output**, not containers. Why: the test phase
  runs before packaging, so an image would need a second command,
  which §3.5 forbids; a fork of `target/classes` with the runtime
  classpath — written to `target/runtime-classpath.txt` by the
  dependency plugin at `process-test-classes` — is the real
  application with no test-scope code inside it, started with the
  three environment facts a real instance gets and nothing else.
  Each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw -q -B test` | 7 green; 3 instances up, 120 requests at one instant, every pid among the 3 | all green, `InstancesRaceIT` in 33 s, exit 0 |
  | `pgrep` after the run | no instance left | none |
  | an instance's log | Java 21, its own port, health UP with `db` UP | `using Java 21.0.11`, `Tomcat started on port 37739`, started in ~3 s |
  | the forked classpath | no test-scope artifact | 0 matches for flyway, testcontainers, junit, assertj, `-test`; 58 entries |

  Shape: `ThrowawayStore` now holds the container and the migration
  (lifted out of `DatabaseIT`, which keeps only the datasource
  override), so tests that boot a context and tests that fork
  instances share one store; `ForkedLedger` starts an instance on
  a free port, waits for its door to answer UP with the store UP,
  and stops it — destroy, then forcibly — in `close()`;
  `InstancesRaceIT` runs no Spring context of its own. Beyond the
  reference's shape: the pids served are asserted to be exactly
  the instances started, and the witness path is exercised — a
  plain JDBC read of `pg_stat_activity` as `runtime` from outside
  every instance, while they run. Traps met: none that bit; free
  ports from `ServerSocket(0)` held for the instances without a
  collision across three runs. What it does not prove is written
  in the test: overlap in the store.
- Commit 7, certification — against the delivered files, never a
  report. §5's evidence, from actual output:

  | Evidence | Expected | Actual |
  |---|---|---|
  | `./mvnw clean test`, nothing exported, ground up but unused | green; `*IT` under the one command | 7 tests, 0 failures: context 1, migration path 3, health through the door 1, in-process burst 1, race across 3 instances 1; exit 0 |
  | `.env` exported, `./mvnw spring-boot:run`, health, probe | Java 21 announced; UP with `db` UP; the probe as `runtime` | `using Java 21.0.11`; `"db":{"status":"UP"}`; `{"pid":"174077","identity":"runtime"}` |
  | the commits standing | the plan's 2–6, one fix | `5336563 066c113 53e4f76 49e7453 e5c61dd 0abbcdc`, each verified before it landed |

  §1 identity exact: group `io.github.edgaras87`, artifact
  `never-oversold`, package `io.github.edgaras87.neveroversold`,
  jar, Java 21; packages `neveroversold` and `probe` only. §2 all
  four capabilities present and bounded: the door (webmvc), health
  naming `db`, connectivity as `runtime`, the harness with every
  sub-item — throwaway store, harness-side migration, zero-applied
  assertion, `*IT` under `./mvnw test`, forked instances raced,
  witness read from the store — proven on contention alone; the
  probe carries no meaning and says it dies at SL-1. §3 every
  constraint visible in the files: `runtime` the only identity in
  `application.yaml`, password `${NEVER_OVERSOLD_RUNTIME_PASSWORD}`
  with no default; the two mentions of `migrator` under main are
  the comment lines naming its absence; 0 of flyway, hibernate,
  jpa, liquibase on the runtime classpath; the migrations home
  holds `.gitkeep` alone; every dependency with its reason above
  it; the suite green with the ground stopped (commit 4's run);
  the forked instances stopped in `finally`. §6 respected: no
  item, reservation or admit anywhere; no table; no other
  adversity; no auth; no image, descriptor or pipeline.
- Exit test: the skeleton runs on the real ground as the runtime
  identity alone — yes, the store saw sessions as `runtime` and no
  other; the harness demonstrably creates the chosen adversity
  through the real door — yes, 120 requests at one instant across
  three processes, every response asserted. **No business behavior
  yet.** The probe pair's death is scheduled at SL-1, in their own
  javadoc and in TODO.
- Decided with the reviewer, 2026-09-12: the fail-fast question
  goes to Release, not here — the only operator today is the
  reviewer, the harness already accepts an instance only on
  health with `db` UP, and the release gate's stranger test is
  where a forgotten export becomes a real need. README's Run
  section names the symptom now. The reviewer's own reproduction
  notes ride in the TODO item.
- Records: README gains Run, Test, the JDK line, the requirements
  row, a two-word vocabulary line (store, door — the reviewer asked
  whether "store" reads as a shop; it is the definition's word,
  chosen before any technology, and kept), and a true status;
  ARCHITECTURE shows the ledger and the harness beside the store;
  CHANGELOG's two Added lines, the version still 0.0 — no slice
  closed; TODO triaged — Now points at Step 5, the plural-instance
  demand closed here, the clock and the fail-fast items moved by
  name; PLAN's gate ticked. Version: 0.0.
- Hand-offs filed this step, all in TODO: the requirements
  document's path (`internal/` → the project's records); the
  Testcontainers 2.x Ryuk trap and template. Not filed as a
  hand-off but worth the skill's notice at the retrospective: the
  reference's concurrency probe is single-process, and this run's
  gate needed more — `ForkedLedger` and the classpath-file plugin
  are the shape that answered it.
- Resume: the entry file's records row (agent-scoped), the
  change-plan's close, then fast-forward `step-4-bootstrap` into
  main on the word; the reviewer pushes. Then Step 5 (SL-1, no
  over-admission under contention): cut its branch, derive its
  gate, run cbc-slice — the door's conventions at its opening, the
  probe pair dying when reserve lands, the race machinery aimed at
  the item's row.
- 2026-09-12, after the merge: the skill re-read against the lived
  step on the reviewer's ask, and four more hand-offs filed in TODO
  — the plural-instance harness shape (promoted from "notice at the
  retrospective" above to a filed hand-off, as ADR-0009 already
  said it should be), the missing-secret trap with its three homes,
  the port key disagreeing between two templates, and the refusal
  test in the miniature. Not filed: the walkthrough's rename trap
  (true, did not bite — Initializr does the pairing) and the
  reference's prefixed role placeholders (this ground's bare names
  are ADR-0006's decision). Also found and fixed on main: the
  entry file's opening paragraph still said "no code yet"; the
  state clause dropped, the lesson filed for the retrospective.

## 2026-09-10 → 2026-09-11  (Step 3: the ground)

<!-- Grows as the walk runs — the lived-result record: for every
     executing step, the command, what was expected, what actually
     happened. Decisions with their options are ADRs. -->

- Opened on `step-3-ground`; the gate derived from the walk's own
  step gates. Readiness tripped on one thing: the definition had no
  runtime-ground facts. Added by a dated revision entry in L1
  (`44f256d`) — the framing's census never asks for them; hand-off
  filed in TODO.
- Readiness, on the actual repo: the three exports stand (ADR-0002),
  four slices, SL-1 chosen-next; the runtime ground now readable;
  the repo is git with `.gitignore`. Passed.
- The environment: ADR-0004, podman local containers, compose-
  driven, kept against every demand the slices make. Proven at the
  engine level before any ground file, each command with its
  expected result, then what happened:

  | Command | Expected | Actual |
  |---|---|---|
  | `podman version` | engine 5.x, client and API the same | 5.8.2 / API 5.8.2, linux/amd64 |
  | `podman compose version` | a provider answers | external provider `docker-compose`, Docker Compose v2.39.4-desktop.1 |
  | `podman info` | rootless, cgroups v2, crun, Fedora | rootless=true, cgroups v2, crun, fedora 42, amd64, kernel 6.19.14, SELinux enabled |
  | `podman pull docker.io/library/alpine:3.20` | digest, exit 0 | digest `bf8527eb…`, exit 0 |
  | `podman run --rm alpine:3.20 sh -c 'echo ground ok'` | `ground ok`, exit 0 | `ground ok`, exit 0 |
  | two `podman run -d` at once, `podman ps` | both Up | `ng-a Up`, `ng-b Up` |
  | `podman kill` on a running container | gone from `podman ps -a` | gone |
  | `podman pause`, then `podman unpause` | paused, then running | `paused`, then `running` |

  Eight of eight as expected; test containers removed. For the
  operator manual: rootless, ports above 1024; SELinux, `:Z` on
  bind mounts; the front door is `podman compose`, answered here by
  an external docker-compose binary.
- The skill's establishment log was opened at the decision and
  withdrawn one commit later, at the reviewer's question: what does
  it hold that the records do not? Nothing — this entry is the walk,
  ADRs are the decisions. Layout settled with it: `compose.yaml`
  and the env files at the root, the runnable ground under
  `infrastructure/`, the manuals under `docs/infrastructure/`.
  Hand-off filed: the skill should treat this as its normal shape
  in a repo with records, not a deviation.
- Operator manual begun (environment section) from the stand-up;
  found two compose providers on the host, podman choosing Docker's
  plugin — noted as "either works, the front door is `podman
  compose`".
- Services, slice by slice (ADR-0005, 09-11): every slice needs
  one shared store with a way to make two writers disagree, atomic
  commit of two writes, and a witness readable from the host;
  nothing needs anything else. PostgreSQL, one instance. Eight
  things not provisioned, each with its why; Flyway as tooling, not
  a service.
- Constraints (ADR-0006): the role-split model checked against the
  project's facts, nothing defeats it; nine constraints, each with
  the mechanism that enforces it — the grant system, the image's
  init hook, a named volume, a published port, the ignore file, the
  image tag. Names: `migrator`, `runtime`, database and schema
  `never_oversold`. T2's tool goes into the behavioral check as a
  capability shown, not a constraint governed.
- Ground files filled from the templates, this repo's names, one
  addition to the verify suite (query 6, the connect privilege,
  because C3 claims it). `podman compose config` clean; every bind
  mount labelled Z; Flyway only under its profile.
- Stand-up and verification, 2026-09-11, each with its expected
  result, then what happened:

  | Step | Expected | Actual |
  |---|---|---|
  | `podman pull postgres:17` | digest, exit 0 | `sha256:67f41722…`, PostgreSQL 17.11 |
  | `podman compose up -d` | created, started, bootstrap runs | started; log: `running …/bootstrap.sql`, `CREATE ROLE` ×2, `CREATE SCHEMA`; healthy after 6 s |
  | first query | answers | **failed once**: `the database system is shutting down` — the image's init restarts the server after the bootstrap and the health check saw the temporary one; answered 1 s later. Trap recorded in the manual |
  | catalog check, 6 queries | as the file's comments | all six as stated |
  | DDL as `runtime` | refused | `ERROR: permission denied for schema never_oversold` |
  | ungranted role connects | refused | `FATAL: permission denied for database "never_oversold"`; probe role dropped |
  | T2's tool: two `runtime` sessions on one advisory lock, `lock_timeout` 1.5 s | second refused | `ERROR: canceling statement due to lock timeout` |
  | `podman compose run --rm flyway info` | connects as migrator, empty schema, exit 0 | Flyway 11.20.3, `<< Empty Schema >>`, `No migrations found`, exit 0 |
  | witness read from the host via the published port | `1` | `1`, through a client container on the host network — the host has no `psql` |

  Both verifications passed; the manual's PostgreSQL section
  written from this run.
- The contract written; then the reviewer asked whether naming the
  server's serializing faces pre-empts the slice — it read that way
  in two phrases; both manuals now say inventory, not a choice, and
  that the advisory-lock probe was picked for needing no schema.
- Clean re-stand from the operator manual alone, on the reviewer's
  yes: `down --volumes` removed container, volume, network; `up -d`
  ran the bootstrap again; honest up after 7 s; all 16 catalog rows
  as stated; DDL as `runtime` refused; Flyway `<< Empty Schema >>`;
  witness read from the host `1`. Reproduced.
- Exit test: the ground runs — yes; every service tied to a stated
  need and every exclusion to a stated why — ADR-0005; both
  verifications passed from actual output — this entry; both
  manuals stand, written from lived work — yes; a stranger could
  stand it up from the operator manual alone — the re-stand followed
  it and nothing else. Passed. README gains Prerequisites;
  ARCHITECTURE names the ground and nothing that does not run.
- Version: still 0.0 — no slice closed; the ground is not a slice.
- Resume: the entry file's records rows, the plan's close, then
  fast-forward into main on the word and the reviewer pushes. Then
  Step 4 (bootstrap): cut its branch, derive its gate, run
  cbc-bootstrap — the stack at capability grain, the skeleton as
  `runtime` on this ground, the harness creating SL-1's adversity
  across instances (TODO's Step 4 items).

## 2026-09-10  (Step 2: identity)

- Named **never-oversold** (ADR-0003): the promise's negation,
  ruled out. Seven candidates from the sentence against a four-point
  bar; safe-reservations overturned — it named the territory and
  overclaimed; rejected beside it: no-oversell, oversell-proof,
  reserved-within-count, reservations-under-contention,
  reservation-ledger. The reviewer took the recommendation as it
  stood.
- The step was retitled from "Define (naming)" when it opened:
  a public identity is a name, a description, and a remote. The
  description is one line derived from the intent's why.
- The rename landed as one project commit across README, PLAN,
  the three exports (each with its first dated revision entry —
  the registry gained the log its header promised), CHANGELOG; the
  derivation record keeps the name it ran under, with a note; the
  entry file's title in its own agent commit. Closed steps' notes
  and earlier devlog entries stay as written: dated facts.
- The remote: github.com/edgaras87/never-oversold, created by the
  reviewer by hand with the ADR's description, main pushed by the
  reviewer at 0de75df (Step 1's close). This branch was not
  pushed; the reviewer pushes main again after the fast-forward.
- The folder on disk keeps `cbc-pure-run-3`; records name the
  project, not the path. The agent's memory is keyed to the path.
- Resume: close the plan, fast-forward `step-2-define` into main
  on the word, the reviewer pushes main. Then Step 3 (ground): cut
  its branch, derive its gate into PLAN, run infra-establish from
  the registry's adversity needs — T2's tool named and verified by
  refusal is the first thing the ground must show.

## 2026-09-09 → 2026-09-10  (Step 1: framing)

- Briefing: arrived 2026-09-09 in `temp/`, the reviewer's file,
  untracked. Inventory reservation under contention, for the
  portfolio reader; six bars a promise must clear; the sentence
  left open. The problem is named from this session on.
- Step 1 ran on `step-1-framing` under a change-plan. The seven
  framing steps each landed as a commit series — draft, one
  revision per reviewer question that changed something, the
  verdict — the reviewer's request before step 0's first commit,
  so a question's effect is a diff between commits. It held: 40
  commits from the gate to the ADR, every verdict the reviewer's.
- What the questions changed: the seller entered as who the
  promise protects, not the audience; the six bars split into
  settled-here and proven-later; the candidate sweep got written
  and each word of the sentence its commitment; refusal was
  restated as by-falsifiability, never by scope; the actors got
  one-line definitions; W2 states its cost; the reference number
  became the one token on-hand-count (a return trip through four
  frozen sections); possession tags kept P, probes became PL.
- Outcome: one promise; four possessions, six refusals; twenty-two
  facts, three trust lines, six fences, five scope verdicts,
  saturation by two empty lenses; twenty kills into four concerns
  and three folds; one area, five seams refused; L5 empty with
  reasons; four slices, SL-1 chosen-next. ADR-0002 adopts it.
- Two slips, both fixed at the reviewer's reading: the definition
  was appended in derivation order and had to be reordered L1→L5
  (the export section's "growing" read as append — hand-off filed
  in TODO); the registry's opening carried the template's own
  words — a skill name, a step number, the delegation slot — into
  a project artifact. The reviewer's rule from that: exports carry
  no agent language. Hand-off filed for the template's line.
- Review-driven touch-ups to committed exports each cost a plan
  revision until a provisional step named them; three plan
  revisions before that, one after. Worth a retrospective line:
  the plan wants a "touch-ups on reading" step from the start.
- DEAD END: none. The near one was concern B — the downward
  correction looked like a definition until the because was
  written (naive code accepts the honest request and the promise
  dies).
- Version decided: a state of the evidence, 0.N by slices closed,
  1.0.0 at all closed and released (CHANGELOG header).
- Resume: land the entry-file row and the plan's close, delete
  `temp/`, fast-forward `step-1-framing` into main on the word.
  Then Step 2 (Define): cut its branch, derive its gate into PLAN,
  decide the public name — safe-reservations to confirm or
  overturn.

## 2026-09-09  (pre-briefing: the working arrangement)

- Four arrangement pieces went in before the briefing, all on
  trial from Step 1. The branch rule landed on main on 09-07, as a
  Standing rules section in PLAN. The other three landed on the
  branch `prebriefing-arrangement` under a change-plan, seven
  commits, fast-forwarded to main on the reviewer's word.
- Kit updated c670fe5 → af16eb7 through the receipt branch
  `kit-af16eb7`: one commit cut from the seed, the kit copied over
  it. What the branch gave the compare: the whole kit diff at the
  two pins in one place, file by file, and compare-first became
  `git diff 27db35e main -- <file>` — every copy identical, so
  every overwrite was clean. Where it fell short: the four
  placeholder reversions are noise to read past; the receipt
  cannot say which convention a stub change belongs to (that is
  the starter README's table, read in the handbook checkout); the
  why is not in it either — ADR-0035, ADR-0036 and the
  agent-arrangement convention text are not kit files, and the
  rejection and the move both needed them; the entry file's stub
  diff had to be carried into the living file by hand, comment
  only; and the script's bare `git add -A` would have swept the
  untracked `temp/` into the receipt (excluded by hand).
- The kit's settings-file commit gate: rejected, not deferred. The
  reviewer gives the word once in chat; a prompt asking for it
  again is a second word. The handbook withdrew the same rule in
  its own checkout for the same reason. The receipt keeps the file
  so the compare stays honest.
- CLAUDE.md moved under .claude/; content untouched; the harness
  picks up the new address from the next session. CLAUDE.local.md
  exists at the root, ignored — the operator's, not quoted.
- Briefing: still not brought. Nothing names the problem.
- Resume: wait for the briefing. On its arrival cut Step 1's branch
  from main, derive Step 1's gate into PLAN first (the branch item
  among them), then run cbc-framing jointly.

## 2026-09-07  (Step 0: bootstrap)

- Project started. Seeded, not born whole: the kit's hygiene commit
  on main, then the branch `birth-seed` as the receipt — kit
  remainder @ c670fe5, the CbC bundle @ 57cf22f (concept, five
  skills, playbook steps, entry-file fills). Main held the same
  files untracked; the receipt is never merged.
- The birth ran as one change set, eight commits: the plan, kit
  conventions, concept, records with Step 0's gate, CbC skills
  with their registry entry, the entry file, this close, the
  plan's close. Every pinned copy byte-identical to the receipt.
- Briefing: not yet brought. It arrives as the prompt that opens
  Step 1; nothing before it names the problem. The title
  `cbc-pure-run-3` is a working name.
- Open: what a version is here (CHANGELOG header) — Framing
  decides.
- Resume: wait for the briefing. On its arrival open Step 1 with
  cbc-framing: derive Step 1's gate into PLAN first, then run the
  framing jointly.
