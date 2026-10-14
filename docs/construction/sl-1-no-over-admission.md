# SL-1 — no over-admission under contention

Words and tags used here: [glossary](../glossary.md).

<!-- The slice's record, grown in three parts. First the correctness
     specification: what must hold, against what, and what proof
     looks like, with no mechanism. Then the plan: one structural
     owner per guarantee, and why it beats the named adversity. Then
     the evidence as delivered: which test creates which adversity,
     and what it read from the store. Invariant and adversity are the
     registry's and the definition's, as written. Guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each part. -->

*Regrouped by guarantee on 2026-10-13: each guarantee's claim,
attack, evidence criterion, owner, options and tests in one block.
No words changed beyond headings and references. A section number
cited before that date refers to the text as signed, at 3443228.*

## Invariant

For every item, in every readable state, the sum of active
reservations ≤ on-hand-count. (`docs/system/registry.md`, SL-1;
also SL-1 in the definition's L4.)

*Active* is FC3's: a reservation not ended by an exit and not past
its expiry, as judged by one clock, not one per instance. *Readable
state* is the intent's: true at every read from outside, not
eventually.

## Adversity

Concurrent admits on one item's last units. Each request fits
alone; together they do not. Taken from the definition's L1 as
written:

- from many callers at one instant (F1);
- from more than one of our own instances (F17), where any number
  an instance keeps in memory about the on-hand-count may already
  be wrong;
- against a store that shows two checks the same count (F21) or a
  count no longer current (F22);
- against an operator's downward change to the count arriving at
  the same instant (F10);
- with an admit decided but not yet recorded (F15);
- under instances whose clocks disagree about which reservations
  are active (F18).

Kills covered: 1, 2, 3, 4, 5, 9, 10. What this slice does not
face:
- an honest downward correction under the reserved sum, with no
  race (SL-2);
- exits and their races (SL-3, SL-4);
- a retried reserve (W2, fenced: it over-holds, it cannot
  oversell).

### How the evidence creates it, for every guarantee

Each guarantee's evidence *creates* its adversity through the real
door. It reads the witness from the store, from outside every
instance. The witness is the invariant itself: for the item, the sum
of active reservations and the on-hand-count, read from persisted
state.

- **In every readable state.** During E1, E2 and E3, a reader
  outside the instances samples the witness while the requests
  race, not only after. Every sample satisfies the invariant.
- **The harness can fail (R5).** Before any wall lands, E1 and E2
  run against the admit written the naive way: read, check, write.
  They go red, the witness showing an oversell, recorded from
  actual output. The same tests go green when the wall lands, with
  nothing in them changed.

All of it runs under the one standard test command, with nothing
exported, and the ground need not be up.

## Guarantees

Each is one answer to: *what would let the invariant hold on paper
yet break in fact?* The attack runs until no new answer comes.
Every guarantee is a property. None names how it is held.

### SL-1 G1. When admits race on one item, each is recorded only if its units still fit at the moment it is written.

*What could go wrong (the attack):* an admit reads the count and
the sum, finds the units fit, then records. Between the finding and
the recording, another admit lands on the same units (F1, F21,
F22).
*Say:* 20 on hand and 19 held. Two requests for one unit each both
read 19, both find a unit free, and both record it: 21 held of 20.
With the admit written this way, the red run printed an active sum
of 22 of 20.

*What we guarantee:* an admit that would take the sum past the
count cannot be recorded. The finding and the recording are one act
against the store's state as it is then, never two acts against a
copy.
*Say:* the second request is judged against the 20 held that stand
when it is written, not the 19 it read, and is refused.

*Kills:* 1, 3, 4.

- **E1 (G1).** One item holds fewer units than a burst of
  concurrent reserve requests together ask for. The requests are
  held back and released at one instant. Witness after: sum ≤
  count. The race is shown to be real: the total asked exceeds the
  count, and at least one request was refused (ADR-0010's status).
  The admitted requests account for exactly the sum the store
  holds.

*The wall:* the store's check constraint `reserved <=
on_hand_count`, with the admit as one conditional statement:
`UPDATE item SET reserved = reserved + q WHERE id = ? AND reserved +
q <= on_hand_count`. Then the reservation is inserted. Both run in
one transaction.

*Why it beats this attack:* two racers update one row. The store
orders writers to a row, and the second re-checks the condition
against the first's result. So exactly the admits that still fit
change a row. The store's answer, one row changed or none, *is* the
decision (F1, F21, F22).
*Say:* 19 held of 20, two requests for one unit. The first changes
the row to 20 held. The second's condition is re-checked against
20, matches no row, and is refused.

*If the wall were ever wrong:* the constraint refuses the row at
write time. An over-admitted state cannot be written, by any path.

*Options weighed:*

The store offers five ways to make "the check and the write are one
act" hold (the contract's inventory). Only two make an over-held
row impossible to write. The other three defend the path, not the
state. Compared against the named adversity:

**Check constraint + conditional `UPDATE`** — *chosen*

*How it holds G1:* one statement, whose count of rows changed is
the decision. The constraint refuses an over-held row by any path.
It is the strongest wall that keeps the decision visible in one SQL
statement.

*Cost:* `reserved` is a counter the entry path must keep true (the
drift case, below).

---

**Row lock, then compute** — `SELECT … FOR UPDATE`, sum the active
rows, decide, insert

*How it holds G1:* orders writers on the item row. It is exact
about expiry, with no counter.

*Why not:* the wall is a lock plus a runtime check. Nothing in the
store refuses a violating row written by another path. And every
racer waits while a sum is computed.

---

**Serializable isolation with retry**

*How it holds G1:* the store aborts one of two conflicting racers,
and the code retries. It is correct.

*Why not:* the decision becomes a loop, and the wall is only a
retry policy. Under a hundred racers, the abort rate is high.

---

**Advisory lock per item around the naive admit**

*How it holds G1:* orders writers by convention, with no schema.

*Why not:* forget the lock on one path, and the race is back. The
ground's probe used it, because it needed no table.

---

**Trigger maintaining `reserved` from the reservation rows**

*How it holds G1:* no code can drift the counter. The strongest
keeper of the counter.

*Why not:* rejected for now, because it moves the entry path's
logic into structure a reader does not see in the application. It
is **the first option to revisit** if a second write path to
`reservation` ever appears.

---

Under the store's default isolation, the store orders two updates
to one row, and the second sees the first's result. That makes the
conditional update's re-check correct.

*Proven by:*

| Guarantee | Owner | Evidence | What it created, what it read |
|---|---|---|---|
| G1 | the check constraint + the conditional `UPDATE` | `ReserveStormIT` (E1) | 100 reserves at one instant on 20 units; witness sampled during, read after: held 20 = admitted 20, 80 refused, no sampled state broken. **Red first:** constraint removed, admit naive — active sum 22 of 20. |

---

### SL-1 G2. When admits for one item arrive at several instances at once, the units held never pass the count, exactly as on one instance.

*What could go wrong (the attack):* an instance decides on a count it
holds in memory (F17).
*Say:* three instances each remember 20 on hand and nothing held,
and each admits ten against its own memory: 30 held of 20.

*What we guarantee:* no state an instance holds outside the store
takes part in the admit. Two instances admitting at once are the
same adversity as two callers on one instance, with the same
outcome.

*Kills:* 2.

- **E2 (G2).** E1's burst, spread across at least three instances
  of the ledger running as separate processes against one store.
  The requests are released at one instant across all of them, and
  the instances that answered are shown to be distinct. Same
  witness, read by none of them.

*The wall:* a single validated entry path: the application holds no
item state. No cache, no counter in memory, no per-instance map.
The only state is the row, and every instance reaches it through
the same statement. Held by a test that reads the main source for
any item numbers kept in memory.

*Why it beats this attack:* the ordering in G1 happens in the
store, which every instance shares and none owns. An instance's
memory cannot take part, because nothing is kept there (F17).
*Say:* three instances, 120 requests, 20 on hand. Every request asks
the same row, so the race across processes is G1's race again, and
20 are held.

*Proven by:*

| Guarantee | Owner | Evidence | What it created, what it read |
|---|---|---|---|
| G2 | no item state in the application | `InstancesStormIT` (E2); `NoInstanceStateOrClockTest` | 120 reserves across 3 forked processes on 20 units, each answering its share; witness read by none of them: held 20, no sampled state broken. **Red first:** 23 of 20. Structurally: no field of a map, collection or atomic type, no mutable static. |

---

### SL-1 G3. When an admit and a change to the count race on one item, each sees the other's result: the admit is judged against the new count, or the change against the new units held.

*What could go wrong (the attack):* an operator lowers the count while
an admit is checking against the old one. Both pass (F10).
*Say:* 20 on hand, 10 held. The operator sets 10 while a request for
one more unit checks against the old 20 and finds it fits. Both
land: 11 held of 10.

*What we guarantee:* on one item, an admit and a change to the
count are ordered. Each sees the other's effect or is seen by it.
No admit is recorded against a count a concurrent change has
already replaced. No change to the count is recorded under a sum an
admit has already raised. A downward change that would set the
count under the reserved sum is not admitted (ADR-0011). SL-2
decided on 2026-09-21 to refuse it, so this wall is the answer.
*Say:* if the 10 lands first, the request finds no unit free. If the
request lands first, 11 are held and the 10 is refused.

*Kills:* 5.

- **E3 (G3).** Reserve requests race a downward change to the same
  item's count. They are released together, and the round is
  repeated. Witness: sum ≤ count in every read. And the change's
  answer agrees with the state. Either it was admitted, and the
  count is the new value with the sum under it. Or it was refused,
  and the count is the old one.

*The wall:* the store: the same row, the same constraint. An
adjustment is one conditional statement on the item row: `INSERT …
ON CONFLICT (id) DO UPDATE SET on_hand_count = ? WHERE
item.reserved <= ?`. It creates the item if unknown (ADR-0011), and
refuses when the new count would sit under `reserved`.

*Why it beats this attack:* an admit and an adjustment are two
writers to one row. The store orders them, so each sees the other's
effect (F10). The statement's condition refuses a count under the
held units in whichever order they land, and the constraint stands
behind it. The refusal is the store's own answer, and no code of
ours decides it. It was provisional when this slice closed, and
SL-2 decided it on 2026-09-21.
*Say:* 20 on hand, thirty requests for one unit and the operator's
10, all at one instant, five rounds. Once the 10 landed first: count
10, 10 held. Four times the requests got there first: the 10 was
refused, and the count stayed 20 with 20 held.

*Proven by:*

| Guarantee | Owner | Evidence | What it created, what it read |
|---|---|---|---|
| G3 | the same row, the same constraint | `AdjustmentRaceIT` (E3) | 30 reserves + one correction 20→10 at one instant, 5 rounds: correction admitted once (count 10, held 10) and refused four times (count 20, held 20); the answer agreed with the state every round; no sampled state broken. |

---

### SL-1 G4. Every reply that says admitted names a reservation the store holds. Nothing is admitted before it is recorded.

*What could go wrong (the attack):* an instance decides "admitted"
and dies before recording, or records later. That is a decision no
other admit can see (F15).
*Say:* an instance decides the last of 20 units is admitted and
dies before recording it. If its caller was already told yes, that
yes names nothing the store holds, and another admit can take the
same unit.

*What we guarantee:* there is no decided-but-unrecorded admit. An
admit is admitted at the instant its reservation is recorded, and
not before. Every reply that says admitted names a reservation the
store holds. The other half, recorded but never replied, is an
orphaned hold, fenced (W2, V5).

*Kills:* 9.

*How kill 9 is proven, decided:* the decision and the record are
one instant. So there is no interval in which to kill an instance,
and no kill-mid-work evidence is owed here. If the plan puts any
act between deciding and recording, this guarantee reopens and
needs kill-mid-work evidence, and the plan must say so.

- **E4 (G4).** Checked in E1 and E2, after each burst: every reply
  that says admitted names a reservation present in the store, and
  the sum the store holds is the sum of exactly those. Owed as
  kill-mid-work evidence only if the plan opens an interval.

*The wall:* a single validated entry path: the decision is the
transaction's commit. The admit's two statements run in one
transaction. The reply "admitted" is produced only from the
committed outcome. No decision variable is set before the write.

*Why it beats this attack:* before commit, nothing is visible to
any other admit and nothing is replied. After commit, the
reservation exists. Death before commit rolls the whole back, so no
decision was taken (F15). Death after commit and before the reply
is the orphan half, fenced. No interval exists to kill, so the
condition above for kill-mid-work evidence is not met.
*Say:* among a hundred racing requests, twenty replies said
admitted. The store held exactly those twenty reservations, each
under the id its reply named.

*Proven by:*

| Guarantee | Owner | Evidence | What it created, what it read |
|---|---|---|---|
| G4 | the transaction's commit | rides on E1, E2 (E4) | every `201` named an id present in the store; the store held exactly the admitted ids; no interval to kill, none killed. |

---

### SL-1 G5. Whether a reservation has expired is judged by the store's clock, the same for every instance. No instance's own clock is read.

*What could go wrong (the attack):* instance A's clock says a
reservation has expired, so its sum is smaller and it admits.
Instance B still counts it (F18).
*Say:* a 15-minute hold on the last unit. A's clock runs a minute
fast, so at minute 14 it counts the hold as gone and admits the
unit again, while B still counts the hold: two holds on one unit.

*What we guarantee:* whether a reservation is active is judged,
for every instance, by the one clock all instances share: the
store's. The store is the one thing on the runtime ground that
every instance shares and that outlives them all. A reservation's
expiry instant is set against that same clock. No instance's own
clock enters either.
*Say:* at minute 14 A asks the store, and the store says the hold
is live, for A as for B.

*Kills:* 10.

*Kill 10's flag, decided:* FC3 removes kill 10. The evidence
does not stage a controlled clock. It shows the one clock is the
one used: no other clock is consulted, and activeness is read from
the store against the store's clock.

- **E5 (G5).** A reservation past its expiry by the store's clock
  is not counted in the sum. One within it is. The persisted expiry
  instant is set by the store. And structurally, the application's
  own code consults no process clock on the admit path or in
  judging activeness. A test that reads the code, not the runtime,
  shows it.

*The wall:* the store's clock. `expires_at` is set in the insert as
`now() + hold`, and activeness is judged as `expires_at > now()` in
the store. The application declares no `Clock`, calls no
`Instant.now()`, and passes no timestamp. Held by a test that reads
the main source and fails on any process-clock call.

*Why it beats this attack:* every instance asks the same clock, the
store's, both to set expiry and to judge it. An instance's clock
error cannot enter what it never supplies (F18). The witness reads
activeness by the same expression.
*Say:* a 15-minute hold's expiry sits exactly `00:15:00` after the
store's own creation stamp. A 1-second hold has left the active sum
a second and a half later, by the store's clock.

*Proven by:*

| Guarantee | Owner | Evidence | What it created, what it read |
|---|---|---|---|
| G5 | the store's `now()`, at insert and at read | `OneClockIT`; `NoInstanceStateOrClockTest` (E5) | a 15-minute hold's expiry exactly `00:15:00` from the store-stamped creation; a 1-second hold left the active sum by the store's clock while the counter kept it. Structurally: no access to any `java.time` `now()`, `Clock`, `Date`, `System.currentTimeMillis` or `nanoTime` — as calls or method references. |

---

### SL-1 G6. A request naming an unknown item, or a quantity that is not a whole number from 1 to the bound, is answered before the ledger decides, `400` invalid or `404` unknown item, and moves nothing.

*What could go wrong (the attack):* a quantity of zero, negative,
or beyond any bound, or an unknown item (F6).
*Say:* a request for −3 units. Added to the held units it lowers
them, and makes room for three units nobody released.

*What we guarantee:* only a request naming a known item and a positive
whole quantity within a stated bound reaches the admit. Anything else
is answered `400`, or `404` for an unknown item, and the numbers do
not move.

*Kills:* 18, folded (FC1).

- **E6 (G6).** Each kind of invalid request is sent: zero,
  negative, beyond the bound, an unknown item, a body that does not
  parse. Each is answered invalid with the status ADR-0010 names,
  and the item's numbers are unchanged after.

*The wall:* the type system at the door. A request is parsed into a
value that cannot be invalid: quantity a whole number in
1..1 000 000, hold a duration in 1 s..7 days, item id not blank.
Otherwise it is answered `400` before any statement runs. An
unknown item is answered `404` (ADR-0010).

*Why it beats this attack:* a value that cannot exist cannot reach
the admit (F6).
*Say:* a request for −3 units never becomes a quantity. It is
answered `400`, and the held units do not move.

*If the wall were ever wrong:* the store's `quantity > 0` and
`>= 0` constraints back it. So even a request that bypassed the
door cannot record an absurd quantity.

*Proven by:*

| Guarantee | Owner | Evidence | What it created, what it read |
|---|---|---|---|
| G6 | the value types at the door; the store's constraints behind | `ReservationDoorIT` (E6) | nine malformed reserves — a quantity of zero, negative, beyond the bound or missing; a hold of zero, beyond seven days, missing or not a duration; a body that is not JSON — each `400` Problem Details, the numbers untouched. An adjustment to a negative count, `400`. A reserve on an unknown item, `404` "unknown item", the status ADR-0010 names. |

---

**The attack found no seventh guarantee.** One more answer belongs
elsewhere: "the reply lies about the outcome" is the caller's view.
L2 refuses it, and G4 covers its edge.

## Folds, consumed here

- **FC1, what a valid request is:** a known item (ADR-0011: one with
  a count), a positive whole quantity within a stated bound. G6.
  FC1's third part, a reservation that exists, is SL-3's (SL-3 G4).
- **FC3, what active means:** not ended by an exit, not past expiry
  as judged by one clock. G5. A reservation carries an expiry
  instant from its admission. How long a hold lasts is the caller's
  to state (L2 refuses the duration policy), positive and within a
  stated bound. Ending expired holds as an exit belongs to
  [SL-3](sl-3-a-reservation-exits-once.md) (SL-3 G3, G5).

## What this slice does not claim

- What the ledger does with an honest downward correction under
  the sum, with no race: SL-2's, which decided to refuse it. G3's
  refusal, provisional until then, is now decided.
- That a retried reserve makes one hold: W2, refused.
- That a reply reaches the caller: the caller's view, refused.
- Who may reserve or adjust: W5, T3.
- Fairness among racers: W3. Which requests win is not asserted.
  That the losers lose is.

## The plan, across guarantees

<!-- The second part: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy: database constraint → type system → single
     validated entry path → runtime check → code review → hope. The
     strongest available is chosen, justified against the named
     adversity, not in general. The store's facility is chosen here
     by weighing options, as the infrastructure contract asks of the
     slice. -->

### What the walls need

Two tables in the one schema, born by this slice's migration, V1:

- **`item`:** `id` (the caller's opaque string, the key),
  `on_hand_count` (whole, ≥ 0), `reserved` (whole, ≥ 0): the units
  held by reservations not yet ended. One check constraint carries
  the promise: **`reserved <= on_hand_count`**. The store refuses
  to hold a row that breaks it, whoever writes it.
- **`reservation`:** `id` (the ledger's, a UUID), `item_id`,
  `quantity` (whole, > 0, constraint), `expires_at` (the store's
  clock plus the hold the caller asked for). No ended state here:
  ending a reservation is an exit, SL-3's. A reservation counts
  until SL-3 gives it a way to stop. (SL-3 did, on 2026-10-07, with
  a receipt per ended reservation in a table of its own.)

`reserved` is the sum of not-yet-ended reservations (SL-3 made this
exact: the units of reservations with no receipt yet). The one entry
path keeps it, in the same transaction as every reservation write.
It can be larger than the active sum, because an expired hold not
yet ended still counts. So the invariant holds with room to spare:
active sum ≤ `reserved` ≤ `on_hand_count`. The cost: units stay
held past expiry until SL-3's exit ends them. That hurts throughput
(W3), not the promise, and is named below, under "Deviations and
provisionals". (Paid by SL-3 on 2026-10-07: its record, "When an
expired hold's units are free". SL-3 G5 frees each once.)

Every guarantee has one owner. None is "all the code being
careful".

### Escape hatches hunted

- **A migration writing rows.** `migrator` owns the schema and
  could insert reservations or set counts without the entry path.
  The rule, stated here in the contract's spirit: migrations carry
  structure, never ledger rows. The constraint still refuses an
  over-held item, whatever a migration writes.
- **The counter drifting from the rows.** Keeping `reserved` equal
  to the sum of not-ended reservations is the entry path's
  bookkeeping, not the store's. The witness recomputes the sum from
  the rows on every read and asserts `reserved` ≥ it. SL-3's exits
  must lower it in the same transaction as they end. That is the
  standing guard, and the trigger above is the answer if it ever
  fails. (Since 2026-10-07 the store checks the counter at every
  commit, SL-3 G6. That check refuses a wrong counter. It is not
  the trigger above, which would move it.)
- **A superuser at a console.** The ground's `postgres`. The
  constraint refuses even it. The superuser can still set the
  counter by hand under the sum: the drift case above.
- **A second door.** None. The bootstrap's probe is deleted here.
  Health reads nothing of the ledger's numbers. No admin path
  exists.
- **The reply path.** Nothing replies "admitted" except the code
  that received the committed outcome: one place, package-private.

### The surface, at its minimum

Only what the guarantees need somewhere to live:

- Migration V1, the first migration, as `migrator`'s. The
  migration-path assertion becomes "applied ≥ 1, none failed".
- `POST /items/{item}/reservations`, body `{"quantity": n,
  "hold": "PT15M"}` (ISO-8601 duration). `201` with the record as
  persisted: `id`, `item`, `quantity`, `expiresAt`. No `Location`:
  a reservation has no reader until a slice needs one, and an
  address that answers `404` is not an address. This deviates from
  ADR-0010's letter. It is logged, and lifted when a reader
  arrives.
- `POST /items/{item}/adjustments`, body `{"onHandCount": n}`.
  `200` with the item as persisted: `id`, `onHandCount`,
  `reserved`. `409` when the count would sit under the held units
  (SL-2's, decided and proved 2026-09-21).
- Refusal and invalid as Problem Details, per ADR-0010.
- Package `reservation` under the base package, package-private
  throughout (ADR-0008): the door, the two statements, the value
  types. No service layer, no repository interface: nothing yet
  needs the extra layer.
- The evidence: E1–E6 under `src/test`. The witness is read as
  `runtime` through the harness's store, from outside every
  instance. The race across instances reuses `ForkedLedger` and
  `ThrowawayStore`. The bootstrap's probe pair is deleted.

### Deviations and provisionals, so the close can see them

- `reserved` can be larger than the active sum until SL-3 (above).
  Paid 2026-10-07 for every decision: tidy frees expired holds
  first. `reserved` still reads high until the next request on the
  item, which no decision sees (SL-3 G5's cost).
- A downward adjustment under the held units is refused by the
  statement's condition, with the constraint behind it. Closed:
  SL-2 weighed "end reservations" and rejected it (its record, "The
  correction's shape — decided"). So this wall stands as decided.
- `Location` omitted until a reader exists.
- The red run for R5: the same tests against the admit written the
  naive way (a read of the row, a check in code, a plain update),
  and migration V1 without its check constraint. It ran on the
  branch and was never committed. The output was recorded in
  `devlog/2026-09.md` before the wall's commit.

## Evidence, as delivered
<!-- The third part: for each guarantee, the test that created its
     adversity, what it read from the store, and the red that
     preceded the green. Every number is from actual output
     (`devlog/2026-09.md`, Step 5). -->

**The harness can fail (R5).** Before the wall, both races ran red
with real oversells, 22 and 23 of 20, on the working tree only.
The same tests went green unchanged once the wall landed. Every
structural rule was shown to fire on a violation planted for it.

**In every readable state.** A reader outside every instance
sampled the witness throughout E1, E2 and E3. Every sample
satisfied the invariant.

**The run on the real ground, 2026-09-14.**
- Migration V1 applied by the compose one-shot, as `migrator`.
- The ledger up as `runtime` on Java 21, health UP with `db` UP.
- An adjustment created an item at 3.
- A reserve of 2 admitted, with the store's expiry.
- A second reserve of 2 refused, `409`, "2 held of 3 on hand".
- A quantity of 0 answered `400`.
- Read back as `runtime`: `proof-item | 3 | 2`, one reservation of
  2, migration V1 in the history as success.

**Under the one standard test command.** 29 tests, `./mvnw test`,
nothing exported, and the ground need not be up.

**Assertion convention, for every later slice.** A body's fields
are asserted by JSON path (`Body` in test support). No substring
assertion remains. The witness is read from the store by plain
JDBC as `runtime`, from outside every instance, never through an
instance's own pool.

**Provisionals carried to the close** (the plan's deviations and
provisionals):
- `reserved` can be larger than the active sum until SL-3 ends
  expired holds. Paid 2026-10-07 for every decision, as above.
- A downward correction under the held units is refused by the
  statement's condition until SL-2 decides. SL-2 decided on
  2026-09-21: refuse.
- `Location` omitted on the reserve reply until a reader exists.

### Read against the test shape, 2026-10-13

SL-1's tests were written before the test shape existed (SL-3's
close). They were read against it on this date (RETRO.md, D13).
Fixed: every first line names its slice ("SL-1 E1 · G1 — kills 1, 3
and 4"), and every label on a number prints the reading it came
from. Not changed, and listed here for the reviewer:

- **The door's answer before the witness.** `ReserveStormIT` and
  `InstancesStormIT` assert "some were refused" before reading the
  store. `AdjustmentRaceIT` asserts each reserve's status as it
  collects it. `ReservationDoorIT`'s three E6 tests assert the
  status and body first, the count and the units held after.
- **The promise after the exact numbers.**
  `OneClockIT.activenessIsJudgedByTheStoresClock` asserts the
  invariant last.
- **No witness.** `ReservationDoorIT.aReserveOnAnUnknownItemIsUnknown`
  reads only the answer. Nothing checks that the store is
  unchanged.
- **A narrower witness.** `ReservationDoorIT`'s E6 tests read the
  count and the units held with queries of their own, not the
  active sum the shared witness reads.

## Standing guards

- **Guarantee erosion.** Any later slice touching `item` or
  `reservation` (SL-2's correction, SL-3's exits, SL-4's consume)
  re-reads this record's guarantees before shipping. New surface is
  new attack surface against this wall.
- **Testing theater.** If `item_never_oversold` is ever dropped or
  weakened, `MigrationPathIT.theWallIsInTheCatalog` goes red before
  any race could pass without it. A race going green with the
  constraint gone means the guarantee moved from structure to
  sampling.
- **Escape hatch watch.** Every new write path to the two tables
  (an admin door, a script, a migration carrying rows, a second
  writer to `reservation`) is checked against the one entry path
  (`Ledger`) and the counter's bookkeeping. The trigger named
  among SL-1 G1's options is the first option if a second writer
  ever appears.
- **The counter and the rows.** `reserved` equals the units of
  reservations with no receipt yet, because the entry path keeps it
  so, not the store. The witness recomputes the sum from the rows on
  every read. SL-3's exits must lower the counter in the same
  transaction as they end. They do, and since 2026-10-07 the store
  checks it at the commit of any transaction that touches the item
  (SL-3 G6).

## Sign-offs

<!-- Dated lines, the reviewer's: the specification before the plan,
     the plan before the build. -->

- 2026-09-12 — the specification (§1–§6) signed by the reviewer.
- 2026-09-13 — the plan (§7) signed by the reviewer.
- 2026-09-14 — the evidence (§8) certified against the delivered
  files and the run on the real ground; SL-1 closed.
- 2026-09-14 — §7's "faces chosen, and the ones not" restated as a
  comparison at the reviewer's ask, after the close; the same five
  faces, the same reasons, no decision changed.
- 2026-10-01 — §7's faces rewritten from a table into blocks, one
  face each, the chosen one first; the same five faces, the same
  reasons, no decision changed.
- 2026-10-01 — §3's guarantees and §7's owners rewritten as blocks,
  each part labelled, with worked numbers where a mechanism is
  involved, as SL-2's record has them; the same guarantees, owners
  and kills, no decision changed.
- 2026-10-01 — §8's E6 row lists what the tests send, the unknown
  item among them as §4 names it; the row had counted its shapes
  loosely and left that one out. Nothing re-run, nothing changed in
  what the evidence showed.
- 2026-10-01 — four corrections, no decision changed: §7's pointer
  to §6 now goes to "Deviations and provisionals", where the cost
  is named; the migrations rule is stated here, the README it
  pointed to never having existed; an adjustment under the held
  units is said to be refused by the statement's condition, the
  constraint behind it, as the code does it; §8's provisionals say
  SL-2 decided the correction.
- 2026-10-13 — the six claims rewritten to say what happens,
  condition first (RETRO.md, D14), each checked against its block
  and its tests; the test tables' short restatements cut. The
  guarantees, owners and evidence are unchanged.
- 2026-10-14 — written plain, no decision changed: one idea per
  sentence, dash and semicolon chains split, lists as lists.
  Metaphors out ("storm", "physically unwritable", "ran dry", "the
  depth is not earned", "movements"). "Kill 9's shape" becomes
  "How kill 9 is proven", and "Kill 10's shape" becomes "Kill 10's
  flag", the registry's word, since a shape is a page under
  `.claude/shapes/`. FC3's note and the plan's notes on SL-3's
  close cite SL-3 G3, G5 and G6 instead of retelling them. A
  glossary line under the title. The text before this pass is at
  adf4ca0. The guarantees, owners and evidence are unchanged.
- 2026-10-14 — read against the definition, the registry and SL-3,
  no decision changed. G6's claim says what is answered: `400`, or
  `404` for an unknown item, as its evidence showed. The attack's
  closing line says no seventh guarantee was found, as SL-3 and
  SL-4 say it. FC1's third part is named as SL-3's. `reserved` is
  given SL-3's exact meaning, units with no receipt. The debt SL-3
  paid is cited by SL-3's section, and "paid" says what still reads
  high. "The shape the walls need" becomes "What the walls need",
  since a shape is a page under `.claude/shapes/`.
- 2026-10-14 — read a third time, no decision changed. G6's claim
  says "answered", not "refused", which here means `409`; its body
  names `400` and `404` too. The store's check runs at the commit
  of any transaction that touches the item, not every commit. The
  G6 row says "malformed reserves", not "shapes".
- 2026-10-14 — read against the slice-record shape at Step 10's
  close: G4's and G5's claims split at their semicolons, words
  unchanged. No decision changed.
