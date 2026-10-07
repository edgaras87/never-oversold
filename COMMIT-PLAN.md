# Commit plan: SL-3's build — the exits, their walls, their evidence

<!-- The convention: .claude/skills/commit-plan. The commits that
     exist are the steps done; this file plans them and is deleted
     at the close. -->

## Summary — the state after all commits

SL-3 closed on evidence. A reservation can now be ended through the
door — consumed or released — and ends by itself when its hold runs
out, its units free from that instant. Every ending writes one
receipt, and the store refuses a second receipt, a changed or
deleted one, an expiry receipt written early, and a consume or
release receipt written late. The arithmetic stays in the
application's statements, on the one entry path, and the store
checks at every commit that an item's units held equal what its
reservations with no receipt hold.

What the repo gains: migration V2 (the receipts table, its key, its
reference, its kind check, the guard trigger); migration V3 (the
check on the units held); the two exit doors
and the ledger's `consume` and `release`; tidy, run first by
reserve and adjust; evidence E1–E7, each seen red with its wall
absent and then green unchanged; SL-1's and SL-2's evidence green
unchanged beside it; the slice record closed as invariant →
guarantees → owner → evidence; the registry carrying SL-3 as closed
with the ordering re-decided and what it hands SL-4 by name.

## Commits

**1. `docs(agent): open the commit plan for SL-3's build`**
This file. The build is six commits of work and two of bookkeeping,
and the red runs may still reshape the test split.

**2. `feat: add receipts for ended reservations`**
V2: `reservation_exit`, its primary key on `reservation_id`, its
reference to `reservation`, its kind check, and the guard trigger —
refusing a changed or deleted receipt, an `expired` receipt before
its instant, a `consumed` or `released` one at or after it, and
setting `ended_at` itself. Nothing writes a receipt yet. With it:
the catalog test names the key, the reference and the trigger; the
store's refusals are shown directly as `runtime` — update, delete,
an early expiry receipt, a late consume receipt — each red with the
trigger dropped from V2 on the working tree; the witness reads
*active* as no receipt and not past expiry. The 39 tests stay
green. Its own step because the walls are the store's and stand
before any path leans on them.

**3. `feat: add the consume and release doors`**
`POST /reservations/{reservation}/consume` and `/release`, no body;
the ledger's `consume` and `release`, each writing its receipt and
moving the numbers in one statement, and writing the `expired`
receipt instead when its reservation has run out; the unknown
reservation as `404`; ADR-0013's answers; the reservation's
`endedBy` and `endedAt`, absent while it is active. Evidence beside
it, sequential at the real door: E5 (a consume moves exactly the
reservation's units, a release frees exactly them, an unknown one
moves nothing) and E3's first half (the same exit twice reads `200`
and moves once; a different exit after it reads `409` and moves
nothing). Red with the key removed and the receipt written by a
naive read-then-insert, on the working tree.

**4. `test: end a racing reservation once`**
E1 and E2. Exits held at a line and released together on one
reservation — consumes only, consumes and releases mixed — in one
instance and across forked instances; and consumes fired on both
sides of the expiry instant of many short holds. The witness after,
and sampled during. Red with the key removed and the naive
read-then-insert, on the working tree: two exits both land.

**5. `feat: free expired holds before each decision`**
Tidy, run first by reserve and adjust, freeing exactly the receipts
it wrote. Evidence: E6, a storm of reserves on an item whose units
an expired hold partly holds, freed once — red with tidy
subtracting a sum it read instead of what it wrote; the tripwire
for §3's choice, a reserve that fits only if the expired units are
free, saying on itself that it pins a decision and discharges no
kill; E3's second half, kill 11 in full — a hold expires, a reserve
takes its units, a late consume is refused and moves nothing. SL-1's
and SL-2's evidence green unchanged.

**6. `test: guard the one way out and the one clock`** *(provisional
split)*
E7: `reservation_exit` and `reserved` are written by the ledger
alone, a second writer failing the check. E4: a hold still ahead by
the store's clock is consumable, and SL-1's no-process-clock rule
is confirmed to reach the new paths. Red by planting the violation
each rule forbids. May fold into earlier steps if a rule turns out
to belong beside the code it guards; a revision says so.

**7. `feat: check the units held in the store`** *(added by
revision, 2026-10-05)*
V3: one function and its constraint trigger on `item`,
`reservation` and `reservation_exit`, deferred to the commit,
refusing any transaction that leaves an item's `reserved` unequal
to the units of its reservations with no receipt (§8, G6, revised
2026-10-05). With it: the catalog test names the three triggers,
enabled; the store's refusals shown directly as `runtime` — a
number lowered without a receipt, a receipt without its move, the
units held set by hand, a hold added or deleted without its units —
and a receipt with its move taken; the tests that write rows by
hand (`ReceiptGuardIT`'s helpers, `MigrationPathIT`'s runtime-writes
test) keeping the units held in step. Each refusal red with V3's
triggers dropped on the working tree. All evidence green unchanged
— E1–E7, SL-1's and SL-2's — and, with commit 5's naive tidy
planted, the store refusing the double free that the
never-oversold constraint let through.

**8. `test: read the ledger's exits as SQL, not text`** *(added by
revision, 2026-10-07)*
E7's rule that inside the ledger a number falls only beside the
receipt its own statement wrote, parsed rather than matched: each of
the ledger's statements read by a SQL parser at test scope
(JSqlParser, entering the build file with its earning reason, as
every dependency does), and an exit checked by its parts — an
`UPDATE item` whose one `WITH` inserts into `reservation_exit`, skips
a second receipt on the key, returns the reservations it wrote, and
is what the update reads its units from. The parsing lives in a test
support class, so the test reads as its rules. A new rule beside it:
the ledger writes SQL only as text blocks, the form the parser is
given — the ledger's one read in a plain string becomes a text
block. The rule on writers outside the ledger keeps its wide net
over every file's text. Red by planting what each rule forbids: a
force release, in a text block and in a plain string; a bare
receipt; a consume moving by its reservation instead of its
receipt; green under a reformat the text check would have failed.

**9. `chore(agent): keep temp/'s drafts at a take`** *(added by
revision, 2026-10-07)*
`delivered-copies.md`, edited in place on 2026-10-05: the take
removes its own staging and nothing else, where it said to empty
`temp/`, which now holds a draft of the project's own. Its decisions
entry with it. An agent path, so its own commit, ahead of the
records that name it.

**10. `chore(agent): expose the evidence-test conventions`**
*(moved here by revision, 2026-10-07)*
The conventions every evidence test has followed since commit 3's
review — the witness asserted first, the invariant checked in every
test that moves numbers, labels carrying the numbers, the record's
example numbers, each test saying what it is — as a rule exposed to
every test (`.claude/rules/`, paths `src/test/**`), with its
decisions entry. Decided at commit 3's review; planned inside the
records step, moved out because it is an agent path.

**11. `docs: records catch up on SL-3`**
The slice record's evidence as delivered, standing guards and its
sign-off line; the registry closing SL-3 by a dated revision entry,
re-deciding the ordering, and handing SL-4 consume's two moves held
in one statement; SL-1's and SL-2's records where they name SL-3 as
the payer of a debt now paid; README's exits as a stranger meets
them; CHANGELOG and the version's move; ARCHITECTURE's new table,
its triggers and the check on the units held, and the harness's SQL
parser beside ArchUnit; the infrastructure contract naming the
check where it lists the store's refusals; the operator manual if
the ground's rows need a check before V3; the devlog with every red
and green from actual output; TODO's Step 7 items closed or moved,
its known issue on E7 rewritten now that the store holds the line,
and the line to the deliverer for the copy edited in place; PLAN's
evidence, deviations, Stage 4 and records items ticked. A sweep for
"SL-3" in live text that still speaks of it as future.

**12. `docs(agent): revise plan — shapes settled at close`**
*(added by revision, 2026-10-07)* This file, gaining the two steps
below.

**13. `chore(agent): shapes take what SL-3's close found`**
*(added by revision, 2026-10-07)*
Two differences the close's reading settled on the shapes' side.
`slice-record.md`: a face revised after the plan was signed carries
its date — `*chosen <date>*`, `*Why not, on revision:*` — as SL-3's
§8 wrote them when V3 replaced the structural test. `evidence-test.md`:
the promise is checked first on every reading taken after the
adversity; a reading that only confirms the scene is set is exempt.
Each with a dated revision line.

**14. `docs: settle SL-3's shape check`** *(added by revision,
2026-10-07)*
Two differences settled on the record's side: §4's G6 gains its
kills line, none by number and why; §8's owners for G4 and G6 gain
what stands behind each wall — partly, and nothing. PLAN: the
differences settled, each named with its ending; the commit-messages
item struck, two subjects past 50 characters named and kept, since
rewriting reviewed commits would break every hash the records cite.
A devlog line.

**15. `docs(agent): close commit plan for SL-3's build`**
Deletes this file.

## Decisions taken inside this plan

- **One migration carrying the walls; the reds on the working
  tree.** The alternative was a naive V2 without key or trigger and
  a V3 adding them, so the wall lands as its own diff in history.
  Rejected: migrations are permanent, and a migration written to be
  wrong would be applied by every store forever. The skill allows
  either; here the wall stands from the first commit that creates
  its table, and each test is reddened by removing it on the
  working tree, a state that never lands.
- **The store's refusals are tested twice.** Directly against the
  store as `runtime` in commit 2, so the trigger is known to refuse
  before any door relies on it; and through the door in commits
  3–5, where the evidence lives. The direct tests are the walls'
  own checks, like SL-1's `theWallIsInTheCatalog`; they discharge no
  kill on their own.
- **The reservation in a response gains `endedBy` and `endedAt`,
  absent while it is active,** so reserve's answer reads as it does
  today and README's reserve example stays true.
- **Test classes by adversity, not by guarantee:** a sequential exit
  class (commits 3, 5), a storm class (commit 4), the structural
  checks with SL-1's and SL-2's (commit 6). Provisional: the red
  runs decide, and a revision records a different split.
- **No commit reaches outside the plan.** If the build needs a
  change to §8 — a wall that does not hold as planned — the work
  stops, §8 is revised with the reviewer, and then this file.

## Revisions

- **2026-10-05 — a seventh step of work: the store's check on the
  units held.** Forced at commit 6's review: E7 reads the
  application's SQL as text, and a writer outside the application
  passes it. §8 was revised first, on the reviewer's word
  (`7c8cb83`); this file follows. The records step and the close
  move to 8 and 9. Folded into the records step, decided earlier at
  commit 3's review: the evidence-test conventions as an exposed
  rule. Lived so far, for the close: commit 3's planned red, the
  naive read-then-insert, cannot show in sequential tests and moved
  to commit 4; commit 3 gained a simple late-consume test; the
  specification's §5 says the naive version lands first and the
  wall is its own diff, against this plan's working-tree reds —
  this plan's choice stands, and the skill allows either; commit 5
  gained a tidy × consume race, on the reviewer's word, and a
  correction tripwire beside the reserve's, and that race's red
  needed its window widened; commit 6 landed unfolded, E7 reading
  source, as Java 21 has no class-file reader.
- **2026-10-07 — an eighth step of work: E7's second rule read as
  SQL.** Forced at commit 7's review: the rule matched the ledger's
  statements as text — `contains("WITH receipt AS (")` — so a
  reformat or a renamed alias failed it while saying nothing of
  what the statement does. The reviewer asked for it to read SQL;
  a parser was tried on the ledger's statements first and reads all
  of them, the data-modifying `WITH`, `ON CONFLICT` and `RETURNING`
  included. At the same review, the parser was found to see only text
  blocks, and the reviewer chose to refuse SQL written any other way
  rather than read both forms. No wall moves, the evidence stays
  green unchanged. The records step and the close move to 9 and 10.
- **2026-10-07 — the records step split by path.** Found opening it:
  it carried the evidence-test rule and the edit to
  `delivered-copies.md`, both under `.claude/`, beside the project's
  records, and an agent path never shares a commit
  (`commit-messages`). Each becomes its own `chore(agent)` commit,
  ahead of the records, which name them. The records step and the
  close move to 11 and 12.
- **2026-10-07 — the shape check settled after the records.** The
  close read SL-3's record against `slice-record.md` and its tests
  against `evidence-test.md`, and found four differences and two
  commit subjects past 50 characters. The reviewer took the records
  commit first, as staged, so each settlement reads as its own diff;
  the shape edits go first of the two, an agent path, since the
  record's settlement names them. The close moves to 15, its
  subject shortened to fit 50 characters, as are the two new ones.
