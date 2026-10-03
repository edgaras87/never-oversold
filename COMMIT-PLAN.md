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
application's statements, on the one entry path.

What the repo gains: migration V2 (the receipts table, its key, its
reference, its kind check, the guard trigger); the two exit doors
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

**7. `docs: records catch up on SL-3`**
The slice record's evidence as delivered, standing guards and its
sign-off line; the registry closing SL-3 by a dated revision entry,
re-deciding the ordering, and handing SL-4 consume's two moves held
in one statement; SL-1's and SL-2's records where they name SL-3 as
the payer of a debt now paid; README's exits as a stranger meets
them; CHANGELOG and the version's move; ARCHITECTURE's new table
and trigger; the devlog with every red and green from actual
output; TODO's Step 7 items closed or moved; PLAN's evidence,
deviations, Stage 4 and records items ticked. A sweep for
"SL-3" in live text that still speaks of it as future.

**8. `docs(agent): close the commit plan for SL-3's build`**
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
