# SL-1 — no over-admission under contention

<!-- The slice's record, grown in three movements: the correctness
     specification (what must hold, against what, and what proof
     looks like — no mechanism), then the plan (one structural owner
     per guarantee, and why it beats the named adversity), then the
     evidence as delivered (which test creates which adversity, and
     what it read from the store). Invariant and adversity are the
     registry's and the definition's, as written; guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each movement. -->

## §1 Invariant

For every item, in every readable state, the sum of active
reservations ≤ on-hand-count. (`docs/system/registry.md`, SL-1;
concern A in the definition's L4.)

*Active* is FC3's: a reservation not ended by an exit and not past
its expiry, as judged by one clock, not one per instance. *Readable
state* is the intent's: true at every read from outside, not
eventually.

## §2 Adversity

Concurrent admits on one item's last units. Each request fits
alone; together they do not. Pulled from the definition's L1 as
written:

- from many callers at one instant (F1);
- from more than one of our own instances (F17), any belief an
  instance holds in memory about the count being a lie;
- against a store that shows two checks the same count (F21) or a
  count no longer current (F22);
- against an operator's downward change to the count arriving at
  the same instant (F10);
- with an admit decided but not yet recorded (F15);
- under instances whose clocks disagree about which reservations
  are active (F18).

Kills covered: 1, 2, 3, 4, 5, 9, 10. What this slice does not
face: an honest downward correction under the reserved sum with no
race (SL-2); exits and their races (SL-3, SL-4); a retried reserve
(W2, fenced: it over-holds, it cannot oversell).

## §3 Guarantees — derived by attack

Each is one answer to: *what would let §1 hold on paper yet break
in fact?* The attack runs until no new answer comes. Every
guarantee is a property; none names how it is held.

**G1. The admit is decided against the truth at the moment of
recording.**

*What could go wrong (the attack):* read the count and the sum, find
the units fit, then record — and between the finding and the
recording another admit lands on the same units (F1, F21, F22).
*Say:* 20 on hand and 19 held. Two requests for one unit each both
read 19, both find a unit free, and both record it: 21 held of 20.
With the admit written this way, the red run printed an active sum
of 22 of 20.

*What we guarantee:* an admit that would take the sum past the count
cannot be recorded; the finding and the recording are one act
against the store's state as it is then, never two acts against a
copy.
*Say:* the second request is judged against the 20 held that stand
when it is written, not the 19 it read, and is refused.

*Kills:* 1, 3, 4.

---

**G2. The decision holds across instances as it holds within one.**

*What could go wrong (the attack):* an instance decides on a count it
holds in memory (F17).
*Say:* three instances each remember 20 on hand and nothing held,
and each admits ten against its own memory: 30 held of 20.

*What we guarantee:* no state an instance holds outside the store
takes part in the admit; two instances admitting at once are the
same adversity as two callers on one instance, with the same
outcome.

*Kills:* 2.

---

**G3. An admit and a change to the count on one item do not
interleave.**

*What could go wrong (the attack):* an operator lowers the count while
an admit is checking against the old one; both pass (F10).
*Say:* 20 on hand, 10 held. The operator sets 10 while a request for
one more unit checks against the old 20 and finds it fits. Both
land: 11 held of 10.

*What we guarantee:* on one item, an admit and a change to the count
are ordered — each sees the other's effect or is seen by it. No
admit is recorded against a count a concurrent change has already
replaced; no change to the count is recorded under a sum an admit
has already raised. A downward change that would set the count
under the reserved sum is not admitted in this slice (per ADR-0011;
SL-2 decided the shape on 2026-09-21 — refuse it — and this wall is
the answer, no longer provisional).
*Say:* if the 10 lands first, the request finds no unit free. If the
request lands first, 11 are held and the 10 is refused.

*Kills:* 5.

---

**G4. A decision exists only as a record.**

*What could go wrong (the attack):* an instance decides "admitted" and
dies before recording, or records later — a decision no other admit
can see (F15).
*Say:* an instance decides the last of 20 units is admitted and
dies before recording it. If its caller was already told yes, that
yes names nothing the store holds, and another admit can take the
same unit.

*What we guarantee:* there is no decided-but-unrecorded admit. An
admit is admitted at the instant its reservation is recorded and not
before; every reply that says admitted names a reservation the store
holds. The other half — recorded but never replied — is an orphaned
hold, fenced (W2, V5).

*Kills:* 9.

*Kill 9's shape, decided:* because the decision and the record are
one instant, there is no interval to kill, and no kill-mid-work
evidence is owed here. Should the plan put any act between deciding
and recording, this guarantee re-opens as a kill-mid-work evidence,
and the plan must say so.

---

**G5. Active is judged by one clock.**

*What could go wrong (the attack):* instance A's clock says a
reservation has expired, so its sum is smaller and it admits;
instance B still counts it (F18).
*Say:* a 15-minute hold on the last unit. A's clock runs a minute
fast, so at minute 14 it counts the hold as gone and admits the
unit again, while B still counts the hold: two holds on one unit.

*What we guarantee:* whether a reservation is active is judged, for
every instance, by the one clock all instances share — the store's
own, the one thing on the runtime ground that outlives and is
common to every instance — and a reservation's expiry instant is
set against that same clock. No instance's own clock enters either.
*Say:* at minute 14 A asks the store, and the store says the hold
is live, for A as for B.

*Kills:* 10.

*Kill 10's shape, decided:* FC3 removes it. The evidence does not
stage a controlled clock; it shows the one-clock judgment is the one
used by showing no other clock is consulted, and by reading
activeness from the store against the store's clock.

---

**G6. Nonsense never reaches the decision.**

*What could go wrong (the attack):* a quantity of zero, negative, or
beyond any bound; an unknown item (F6).
*Say:* a request for −3 units. Added to the held units it lowers
them, and makes room for three units nobody released.

*What we guarantee:* only a request naming a known item and a
positive whole quantity within a stated bound reaches the admit;
anything else is answered invalid and the numbers do not move.

*Kills:* 18, folded (FC1).

---

The attack ran dry at six: a seventh answer — "the reply lies about
the outcome" — is the caller's view, refused at L2 and covered at
its edge by G4.

## §4 Evidence criteria

Each guarantee's evidence *creates* its adversity through the real
door and reads the witness from the store, from outside every
instance. The witness is §1 itself: for the item, the sum of active
reservations and the on-hand-count, read from persisted state.

- **E1 (G1).** One item holding fewer units than a storm of
  concurrent reserve requests together ask for; the requests held
  at a line and released at one instant. Witness after: sum ≤
  count. The storm shown real: the total asked exceeds the count,
  and at least one request was refused (ADR-0010's status). The
  admitted requests account for exactly the sum the store holds.
- **E2 (G2).** E1's storm spread across at least three instances of
  the ledger running as separate processes against one store, the
  requests released at one instant across all of them; the served
  instances shown distinct. Same witness, read by none of them.
- **E3 (G3).** Reserve requests racing a downward change to the
  same item's count, released together, repeated. Witness: sum ≤
  count in every read; and the change's answer agrees with the
  state — admitted and the count is the new value with the sum
  under it, or refused and the count is the old one.
- **E4 (G4).** Riding on E1 and E2: every reply that says admitted
  names a reservation present in the store, and the sum the store
  holds is the sum of exactly those. Owed as kill-mid-work evidence
  only if the plan opens an interval (§3, G4).
- **E5 (G5).** A reservation past its expiry by the store's clock is
  not counted in the sum; one within it is; the persisted expiry
  instant is the store's assignment. And structurally: the
  application's own code consults no process clock on the admit
  path or in judging activeness — shown by a test that reads the
  code, not the runtime.
- **E6 (G6).** Each nonsense shape sent — zero, negative, beyond the
  bound, an unknown item, a body that does not parse — answered
  invalid with the status ADR-0010 names, and the item's numbers
  unchanged after.
- **In every readable state.** During E1, E2 and E3, a reader
  outside the instances samples the witness while the storm runs,
  not only after; every sample satisfies §1.
- **The harness can fail (R5).** Before any wall lands, E1 and E2
  run against the admit written the naive way — read, check, write
  — and go red, the witness showing an oversell; recorded from
  actual output. The same tests go green when the wall lands and
  nothing in them changes.

All of it under the one standard test command, nothing exported,
the ground not required up.

## §5 Folds, consumed here

- **FC1, what a valid request is:** a known item (ADR-0011: one with
  a count), a positive whole quantity within a stated bound. G6.
- **FC3, what active means:** not ended by an exit, not past expiry
  as judged by one clock. G5. A reservation carries an expiry
  instant from its admission; how long a hold lasts is the caller's
  to state (L2 refuses the duration policy), positive and within a
  stated bound; the ending of expired holds as an exit is SL-3's.

## §6 What this slice does not claim

- What the ledger does with an honest downward correction under the
  sum, absent a race — SL-2, closed 2026-09-21: refuse it. G3's
  refusal was provisional until then and is now the decided shape.
- That a retried reserve makes one hold — W2, refused.
- That a reply reaches the caller — the caller's view, refused.
- Who may reserve or adjust — W5, T3.
- Fairness among racers — W3. Which requests win is not asserted;
  that the losers lose is.

## §7 Plan — one structural owner per guarantee

<!-- The second movement: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy — database constraint → type system →
     single validated entry path → runtime check → code review →
     hope — the strongest available, justified against the named
     adversity, not in general. The store's facility is chosen by
     face here, as the infrastructure contract asks of the slice. -->

### The shape the walls need

Two tables in the one schema, born by this slice's migration, V1:

- **`item`** — `id` (the caller's opaque string, the key),
  `on_hand_count` (whole, ≥ 0), `reserved` (whole, ≥ 0): the units
  held by reservations not yet ended. One check constraint carries
  the promise: **`reserved <= on_hand_count`**. The store refuses
  to hold a row that breaks it, whoever writes it.
- **`reservation`** — `id` (the ledger's, a UUID), `item_id`,
  `quantity` (whole, > 0, constraint), `expires_at` (the store's
  clock plus the hold the caller asked for). No ended state yet:
  ending a reservation is an exit, SL-3's; here a reservation
  counts until SL-3 gives it a way to stop.

`reserved` is the sum of not-yet-ended reservations, kept by the
one entry path in the same transaction as every reservation write.
It is a superset of the active sum (an expired hold not yet ended
still counts), so the invariant holds with room to spare: active
sum ≤ `reserved` ≤ `on_hand_count`. The cost — units held past
expiry until SL-3's exit ends them — is throughput (W3), not the
promise, and is named below, under "Deviations and provisionals".

### Owners

**G1. One act against the truth at recording.**

*The wall:* the store's check constraint `reserved <= on_hand_count`,
with the admit as one conditional statement — `UPDATE item SET
reserved = reserved + q WHERE id = ? AND reserved + q <=
on_hand_count`, then the reservation's insert, both in one
transaction.

*Why it beats this attack:* two racers update one row; the store
serializes writers to a row and the second re-evaluates the
condition against the first's result, so exactly the admits that
still fit change a row. The store's answer — one row changed or
none — *is* the decision (F1, F21, F22).
*Say:* 19 held of 20, two requests for one unit. The first changes
the row to 20 held; the second's condition is re-checked against
20, matches no row, and is refused.

*If the wall were ever wrong:* the constraint refuses the row at
write time. An over-admitted state is physically unwritable, by any
path.

---

**G2. Across instances as within one.**

*The wall:* a single validated entry path — the application holds no
item state. No cache, no counter in memory, no per-instance map; the
only state is the row, and every instance reaches it through the
same statement. Held by a test that reads the main source for any
in-memory keeping of item numbers.

*Why it beats this attack:* the serialization in G1 lives in the
store, which every instance shares and none owns; an instance's
memory cannot take part because nothing is kept there (F17).
*Say:* three instances, 120 requests, 20 on hand. Every request asks
the same row, so the storm across processes is G1's storm again,
and 20 are held.

---

**G3. Admit and adjustment do not interleave.**

*The wall:* the store — the same row, the same constraint. An
adjustment is one conditional statement on the item row — `INSERT …
ON CONFLICT (id) DO UPDATE SET on_hand_count = ? WHERE
item.reserved <= ?` — creating the item if unknown (ADR-0011),
refusing when the new count would sit under `reserved`.

*Why it beats this attack:* an admit and an adjustment are two
writers to one row: serialized by the store, each sees the other's
effect (F10). The statement's condition refuses a count under the
held units whichever order they land in, and the constraint stands
behind it; the refusal (§3 G3) is the store's own answer, and no
code of ours decides it — provisional when this slice closed,
decided by SL-2 on 2026-09-21.
*Say:* 20 on hand, thirty requests for one unit and the operator's
10, all at one instant, five rounds. Once the 10 landed first: count
10, 10 held. Four times the requests got there first: the 10 was
refused, and the count stayed 20 with 20 held.

---

**G4. A decision exists only as a record.**

*The wall:* a single validated entry path — the decision is the
transaction's commit. The admit's two statements run in one
transaction; the reply "admitted" is produced only from the
committed outcome; there is no decision variable set before the
write.

*Why it beats this attack:* before commit nothing is visible to any
other admit and nothing is replied; after commit the reservation
exists. Death before commit rolls the whole back — no decision was
taken (F15). Death after commit before the reply is the orphan half,
fenced. No interval exists to kill: the spec's trigger (§3 G4) is
not pulled.
*Say:* in the storm of a hundred, twenty replies said admitted, and
the store held exactly those twenty reservations, each under the id
its reply named.

---

**G5. Active is judged by one clock.**

*The wall:* the store's clock — `expires_at` assigned in the insert
as `now() + hold`; activeness judged as `expires_at > now()` in the
store. The application declares no `Clock`, calls no
`Instant.now()`, and passes no timestamp. Held by a test that reads
the main source and fails on any process-clock call.

*Why it beats this attack:* every instance asks the same clock, the
store's, for both the setting and the judging; an instance's skew
cannot enter what it never supplies (F18). The witness reads
activeness by the same expression.
*Say:* a 15-minute hold's expiry sits exactly `00:15:00` after the
store's own creation stamp. A 1-second hold has left the active sum
a second and a half later, by the store's clock.

---

**G6. Nonsense never reaches the decision.**

*The wall:* the type system at the door — a request is parsed into a
value that cannot be nonsense: quantity a whole number in
1..1 000 000, hold a duration in 1 s..7 days, item id non-blank. Or
it is answered `400` before any statement runs; an unknown item is
answered `404` (ADR-0010).

*Why it beats this attack:* a value that cannot exist cannot reach
the admit (F6).
*Say:* a request for −3 units never becomes a quantity. It is
answered `400`, and the held units do not move.

*If the wall were ever wrong:* the store's `quantity > 0` and `>= 0`
constraints back it, so even a bypassed door cannot record an
absurd quantity.

---

Every guarantee has one owner. None is "all the code being
careful".

### The faces chosen, and the ones not

The store offers five ways to make "the check and the write are one
act" hold (the contract's inventory). Only two make an over-held
row physically unwritable; the other three defend the path, not the
state. Compared against the named adversity:

**G1 — the admit decided against the truth at the moment of
recording.**

**Check constraint + conditional `UPDATE`** — *chosen*

*How it holds G1:* one statement, the row count the decision; the
constraint refuses an over-held row by any path. The strongest wall
that keeps the decision visible in one SQL sentence.

*Cost:* `reserved` is a counter the entry path must keep true (the
drift case, below).

---

**Row lock, then compute** — `SELECT … FOR UPDATE`, sum the active
rows, decide, insert

*How it holds G1:* serializes writers on the item row. Exact about
expiry, no counter.

*Why not:* the wall is a lock plus a runtime check; nothing in the
store refuses a violating row written by another path; every racer
waits for a sum.

---

**Serializable isolation with retry**

*How it holds G1:* the store aborts one of two conflicting racers;
the code retries. Correct.

*Why not:* the decision becomes a loop and the wall hides in a
retry policy; under a hundred racers the abort rate is high.

---

**Advisory lock per item around the naive admit**

*How it holds G1:* serializes writers by convention, no schema.

*Why not:* forget the lock on one path and the race is back. The
ground's probe, chosen there because it needed no table.

---

**Trigger maintaining `reserved` from the reservation rows**

*How it holds G1:* no code can drift the counter. The strongest
keeper of the counter.

*Why not:* rejected for now because it moves the entry path's logic
into structure a reader does not see in the application. **The
first option to revisit** if a second write path to `reservation`
ever appears.

---

Serialization under the default isolation is what makes the
conditional update's re-evaluation correct: two updates to one row
are ordered by the store, and the second sees the first's result.

### Escape hatches hunted

- **A migration writing rows.** `migrator` owns the schema and
  could insert reservations or set counts without the entry path.
  Rule, stated here in the contract's spirit: migrations carry
  structure, never ledger rows; the constraint still refuses an
  over-held item whatever a migration writes.
- **The counter drifting from the rows.** `reserved` equal to the
  sum of not-ended reservations is the entry path's bookkeeping,
  not the store's. The witness recomputes the sum from the rows on
  every read and asserts `reserved` ≥ it; SL-3's exits must lower
  it in the same transaction as they end — the standing guard, and
  the trigger above is the answer if it ever fails.
- **A superuser at a console.** The ground's `postgres`; the
  constraint refuses even it. The counter can be set by hand under
  the sum — the drift case above.
- **A second door.** None: the probe dies here; health reads
  nothing of the ledger's numbers; no admin path exists.
- **The reply path.** Nothing replies "admitted" except the code
  that received the committed outcome — one place, package-private.

### The surface, at its minimum

Only what the guarantees need somewhere to live:

- V1, the first migration, as `migrator`'s; the migration-path
  assertion turns to "applied ≥ 1, none failed".
- `POST /items/{item}/reservations` — body `{"quantity": n,
  "hold": "PT15M"}` (ISO-8601 duration); `201` with the record as
  persisted: `id`, `item`, `quantity`, `expiresAt`. No `Location`
  yet: a reservation has no reader until a slice needs one, and an
  address that answers `404` is not an address — a deviation from
  ADR-0010's letter, logged, lifted when the reader arrives.
- `POST /items/{item}/adjustments` — body `{"onHandCount": n}`;
  `200` with the item as persisted: `id`, `onHandCount`,
  `reserved`; `409` when the count would sit under the held units
  (SL-2's, decided and proved 2026-09-21).
- Refusal and invalid as Problem Details per ADR-0010.
- Package `reservation` under the base package, package-private
  throughout (ADR-0008); the door, the two statements, the value
  types. No service layer, no repository interface: the depth is
  not earned.
- The evidence: E1–E6 under `src/test`, the witness read as
  `runtime` through the harness's store from outside every
  instance; the race across instances reusing `ForkedLedger` and
  `ThrowawayStore`; the probe pair deleted.

### Deviations and provisionals, so the close can see them

- `reserved` over-approximates the active sum until SL-3 (above).
- A downward adjustment under the held units is refused by the
  statement's condition, the constraint behind it. Closed: SL-2
  weighed "end reservations" and rejected it (its record, §3), so
  this wall stands as the decided shape.
- `Location` omitted until a reader exists.
- The red run for R5: the same tests against the admit written the
  naive way — a read of the row, a check in code, a plain update —
  and V1 without its check constraint, on the branch, never
  committed; the output recorded in the devlog before the wall's
  commit.

## §8 Evidence — as delivered

<!-- The third movement: for each guarantee, the test that created
     its adversity, what it read from the store, and the red that
     preceded the green. Every number is from actual output
     (devlog, Step 5). -->

| Guarantee | Owner (§7) | Evidence | What it created, what it read |
|---|---|---|---|
| G1 one act against the truth | the check constraint + the conditional `UPDATE` | `ReserveStormIT` (E1) | 100 reserves at one instant on 20 units; witness sampled during, read after: held 20 = admitted 20, 80 refused, no sampled state broken. **Red first:** constraint removed, admit naive — active sum 22 of 20. |
| G2 across instances as within one | no item state in the application | `InstancesStormIT` (E2); `NoInstanceStateOrClockTest` | 120 reserves across 3 forked processes on 20 units, each answering its share; witness read by none of them: held 20, no sampled state broken. **Red first:** 23 of 20. Structurally: no field of a map, collection or atomic type, no mutable static. |
| G3 admit and adjustment do not interleave | the same row, the same constraint | `AdjustmentRaceIT` (E3) | 30 reserves + one correction 20→10 at one instant, 5 rounds: correction admitted once (count 10, held 10) and refused four times (count 20, held 20); the answer agreed with the state every round; no sampled state broken. |
| G4 a decision exists only as a record | the transaction's commit | rides on E1, E2 (E4) | every `201` named an id present in the store; the store held exactly the admitted ids; no interval to kill, none killed. |
| G5 active is judged by one clock | the store's `now()`, at insert and at read | `OneClockIT`; `NoInstanceStateOrClockTest` (E5) | a 15-minute hold's expiry exactly `00:15:00` from the store-stamped creation; a 1-second hold left the active sum by the store's clock while the counter kept it. Structurally: no access to any `java.time` `now()`, `Clock`, `Date`, `System.currentTimeMillis` or `nanoTime` — as calls or method references. |
| G6 nonsense never reaches the decision | the value types at the door; the store's constraints behind | `ReservationDoorIT` (E6) | nine reserve shapes — a quantity of zero, negative, beyond the bound or missing; a hold of zero, beyond seven days, missing or not a duration; a body that is not JSON — each `400` Problem Details, the numbers untouched. An adjustment to a negative count, `400`. A reserve on an unknown item, `404` "unknown item", the status ADR-0010 names. |

**The harness can fail (R5).** Before the wall: both storms red
with real oversells, 22 and 23 of 20, on the working tree only;
the same tests green unchanged once the wall landed. Every
structural rule shown to fire on its own plant.

**In every readable state.** A reader outside every instance
sampled the witness throughout E1, E2 and E3; every sample
satisfied §1.

**The run on the real ground, 2026-09-14.** V1 applied by the
compose one-shot as `migrator`; the ledger up as `runtime` on Java
21, health UP with `db` UP; an adjustment created an item at 3; a
reserve of 2 admitted with the store's expiry; a second reserve of
2 refused, `409`, "2 held of 3 on hand"; a quantity of 0 answered
`400`. Read back as `runtime`: `proof-item | 3 | 2`, one
reservation of 2, V1 in the history as success.

**Under the one standard test command.** 29 tests, `./mvnw test`,
nothing exported, the ground not required up.

**Assertion convention, for every later slice.** A body's fields
are asserted by JSON path (`Body` in test support); no substring
assertion remains. The witness is read from the store by plain
JDBC as `runtime`, from outside every instance, never through an
instance's own pool.

**Provisionals carried to the close** (§7): `reserved`
over-approximates the active sum until SL-3 ends expired holds; a
downward correction under the held units is refused by the
statement's condition until SL-2 decides the correction's shape —
decided 2026-09-21: refuse; `Location` omitted on the reserve reply
until a reader exists.

## §9 Standing guards

- **Guarantee erosion.** Any later slice touching `item` or
  `reservation` — SL-2's correction, SL-3's exits, SL-4's consume —
  re-reads §3 before shipping. New surface is new attack surface
  against this wall.
- **Testing theater.** If `item_never_oversold` is ever dropped or
  weakened, `MigrationPathIT.theWallIsInTheCatalog` goes red before
  any storm could pass around its absence. A storm green with the
  constraint gone means the guarantee moved from structure to
  sampling.
- **Escape hatch watch.** Every new write path to the two tables —
  an admin door, a script, a migration carrying rows, a second
  writer to `reservation` — is checked against the one entry path
  (`Ledger`) and the counter's bookkeeping; the trigger named in §7
  is the first option if a second writer ever appears.
- **The counter and the rows.** `reserved` equals the sum of
  not-ended reservations by the entry path's discipline, not the
  store's. The witness recomputes the sum from the rows on every
  read; SL-3's exits must lower the counter in the same transaction
  as they end.

## §10 Sign-offs

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
