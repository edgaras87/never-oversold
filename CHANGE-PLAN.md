# Change-plan: Step 5, SL-1 — no over-admission under contention

## Summary — the state after all commits

The ledger holds its first invariant by structure and shows it:
for every item, the sum of active reservations never exceeds the
on-hand-count, under a storm of concurrent admits from many callers
and from three instances of the ledger running as separate
processes, and under admits racing a downward adjustment. The
schema exists — `item` and `reservation`, born by the first
migration as `migrator`'s, the item row carrying the check
constraint that is the wall (`docs/construction/sl-1-no-over-
admission.md`, §7). Two doors stand under ADR-0010's conventions:
reserve and adjust, an item becoming known by its first adjustment
(ADR-0011). The application keeps no item state and declares no
clock; the store's clock sets and judges expiry. The evidence
creates every adversity the specification names and reads the
witness from the store from outside every instance; it was run red
against the naive admit before the wall landed, recorded, and went
green unchanged. The probe pair is gone; the harness machinery
stays. The slice record carries invariant → guarantees → owner →
evidence; the registry closes SL-1 and re-decides the order; the
records are caught up; PLAN's Step 5 gate is ticked.

## Commits

**1. `docs(agent): add change-plan for SL-1`**
This plan, agreed.

**2. `feat: the first migration, item and reservation`**
V1 under `infrastructure/flyway/migrations/`, as `migrator`'s, per
§7's shape: `item` with `on_hand_count`, `reserved`, the check
`reserved <= on_hand_count` and both non-negative; `reservation`
with `quantity > 0`, `expires_at`, its item. No grant — the ground's
default privileges make it `runtime`'s (contract, term 4). The
migration-path test turns from "zero applied" to "applied ≥ 1, none
failed", what it proves unchanged; a new assertion reads the
constraint from the catalog so a dropped wall is seen. Verified:
`./mvnw test` green; the miniature migrated V1 as `migrator`;
`runtime` can write both tables and still cannot create one.

**3. `feat: the reserve and adjust doors`**
ADR-0010 made real: `POST /items/{item}/reservations` and
`POST /items/{item}/adjustments` in one package-private
`reservation` package; requests parsed into values that cannot be
nonsense (G6) or answered `400`; unknown item `404`; refusal `409`;
every non-success as Problem Details. The adjust is already its
final shape — one conditional statement, creating the unknown item
(ADR-0011), refusing a count under the held units. **The reserve
admit is written the naive way here** — read the row, check in
code, update, insert, in one transaction — so the next commit's
diff is the wall and nothing else. The check constraint from
commit 2 already holds the invariant against it; a racer that
loses under the naive admit sees the constraint's error, not a
clean refusal. The probe pair dies here: `probe/`,
`ContentionProbeIT`, `InstancesRaceIT` deleted, their scaffolding
purpose served; `testsupport/` stays. Verified: the door tests —
E6's nonsense shapes, an admitted reserve, a refused one, an
adjustment creating an item — green; health still UP.

**4. `feat: the admit as one act against the store`**
The wall's second half and the storm that proves it. The reserve
admit becomes §7 G1's one conditional statement — units added only
if they still fit, the store's row count the decision — the reply
"admitted" produced from the committed outcome alone (G4). The
evidence lands with it: E1, the storm on one item's last units
released at one instant, the witness sampled during and read
after, the storm shown real (asked > count, some refused), every
admitted reply present in the store (E4); E2, the same storm across
three forked instances, served pids distinct, the witness read by
none of them. **Before this commit lands, the red run:** on the
working tree, with V1's check constraint removed and the admit
still naive, E1 and E2 run and go red — the witness shows an
oversell — recorded in the devlog from actual output; the working
tree restored; then the wall, and the same tests green. Verified:
`./mvnw test` green from clean, nothing exported.

**5. `test: the adjustment racing the admits`**
E3: reserve requests and a downward adjustment on one item released
together, repeated; the witness in every read; the adjustment's
answer agreeing with the state. Verified: green; the refusal under
the held units seen as `409` from the constraint's own answer.

**6. `test: one clock — expiry set and judged by the store`**
E5: a reservation past its expiry by the store's clock not counted
in the active sum and one within it counted, the persisted expiry
the store's assignment; and the structural test that reads the main
source and fails on any process-clock call or any in-memory keeping
of item numbers (G2, G5). Verified: green; the structural test
shown to fail on a planted `Instant.now()`, then the plant removed.

**7. `test: assert bodies by path`**
The evidence's assertion style, decided with the reviewer at commit
4's boundary and applied before the slice closes: a body's fields
are asserted by JSON path (`$.quantity` equal to 3), not by
substring — `"quantity":3` also matches `"quantity":30`, and a
substring cannot say "a field exists" without a regex. The door
tests and both storms switch; the storms' regex for the id goes;
the storms' proof still comes from the store. One convention,
project-wide, written in the slice record's evidence movement: a
word may stay a `contains` (an error title); a shape is a path.
Verified: green, no behaviour change.

**8. `docs: records catch up on SL-1`**
The slice record's third movement: §9 evidence as delivered — each
test, the adversity it created, what it read, the red and the
green from actual output — and the standing guards. The registry:
SL-1 `closed (2026-09-xx, evidence)`; the ordering re-decided by a
dated revision entry with its reason. CHANGELOG under Unreleased:
reserve and adjust, the promise's first held invariant; the version
moved to 0.1, the first evidence-closed state (decided with the
reviewer here). README: the two doors as a stranger would use them,
with the refusal shown. ARCHITECTURE: the ledger with its first
table and its wall. The operator manual: unchanged unless the walk
found something. The run proof on the real ground, recorded from
actual output: the ground up, V1 applied by the compose one-shot as
`migrator` (the reviewer's ground, on their word), the ledger up as
`runtime`, an adjustment creating an item, a reserve admitted, one
refused. TODO: the Step 5 items closed or moved by name; hand-offs
filed. Devlog: the walk, the deviations, the exit test's answers.
PLAN: Step 5's gate ticked from the delivered state.

**9. `docs(agent): close change-plan for SL-1`**
The plan deleted; the commit body records where the set diverged.

## Decisions taken inside this plan

- **The naive admit is committed (3) and the wall is its own diff
  (4).** So the history shows what the wall is, not only that it
  exists. Under commit 3 alone the invariant already holds — the
  constraint is the wall — but losers see an error rather than a
  refusal. The red run needs the constraint gone too, so it happens
  on the working tree between 3 and 4, never committed: a commit
  that can oversell has no place on the branch.
- **Commit 3 births the adjust door in its final shape** while the
  reserve admit is naive. The adjust has no naive version worth
  showing; the reserve does, because it is where naive code
  oversells.
- **The evidence tests share one throwaway store** per test JVM, so
  each test uses its own item ids; nothing is reset between tests.
- **E1 runs in-process against one context; E2 forks three.** Both
  stay: E1 is the cheap first check, E2 the shape nobody can fake
  (ADR-0009). The in-process pool is sized to the request count;
  the connection pool stays the default — requests queue at the
  pool and still race at the row, which is the adversity, not the
  pool.
- **The version moves to 0.1 at commit 8**, because a version is a
  state of the evidence and the first invariant is now evidence-
  closed; the reviewer confirms at that boundary.
- **Applying V1 on the real ground is the reviewer's act**, run at
  commit 8's proof of life on their word; the miniature proves the
  migration on every test run before that.
- **A step added at commit 4's boundary (revision, not divergence of
  a firm step):** the assertion style. The door tests landed with
  substring assertions; the reviewer asked whether that is the way
  to assert, and it is not the way to keep. Refactored inside this
  slice rather than at SL-2's opening so the evidence closes in the
  style every later slice enters.
- **`Location` omitted** on the reserve reply until a reader exists
  (§7's deviation from ADR-0010's letter), lifted when SL-3 gives a
  reservation a reader.
