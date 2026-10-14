# System definition — never-oversold

<!-- The layered system the promise derives: what we own (L2), the
     environment that attacks it (L1), the collisions and what
     dies (L4), the owners (L3), what holds between owners (L5).
     Composed at framing close from the derivation record beside
     it (framing/derivation.md), and ordered as the map reads,
     L1→L5. Stands alone. A living record: after close it
     changes only through a dated entry in the revision log at the
     end. -->

The promise this definition serves is in `intent.md`:
*for any item, the reserved quantity never exceeds the
on-hand-count, however many reservations race for the same units.*

Layers, in the map's order, outside-in: L1 the environment · L2
what must be ours · L3 the owners · L4 the collisions · L5 between
owners.

## L1 — The environment

Everything beyond our control, named by what it does to us. The
census lists what can *hurt*, never what we trust. The short trust
list below it is the one deliberate exception, and each of its
lines can be challenged.

### Actors

- **Callers:** the services that reserve, consume and release on
  behalf of a seller's customers. They move the reserved side.
- **Operators:** whoever changes the on-hand-count from the
  world's side, by a restock, a loss or a recount. An operator
  may be a person at a screen or a stock system. Either way, the
  ledger sees an adjust request at the door. They move the
  on-hand-count side.
- **The network:** carries requests and replies. It loses,
  duplicates, delays and reorders them.
- **Our own process:** the ledger's running instances. They die
  mid-work, and there can be more than one.
- **The store:** where the ledger's numbers persist. It answers
  reads and takes writes. It can be slow, stale or silent.
- **The clock:** ends reservations by expiry. It differs between
  instances, and it jumps.

### Facts, consequence-first

Each fact reads: what the world does → what that does to us.

Callers
- F1. Many callers send reserve for one item at the same instant,
  together asking more than the on-hand-count → the race for the
  last units. Each request fits alone; together they do not.
- F2. A caller resends reserve after a lost reply → the same
  request twice. A second hold takes units others need.
- F3. A caller resends consume after a lost reply → the
  on-hand-count lowered twice for one reservation.
- F4. A caller sends consume and release for one reservation at the
  same instant, or two consumes → two exits racing on one
  reservation, each moving numbers.
- F5. A caller consumes or releases a reservation that already
  ended (expired, consumed or released), even very late → a
  request against a reservation with no active state left.
- F6. A caller asks for zero, a negative or absurd quantity, or an
  unknown item → a request that means nothing, at the door.
- F7. A caller consumes a reservation not theirs, or claims a
  quantity other than the reservation's → a false claim at the
  door.
- F8. A caller never returns → a reservation held forever unless
  something ends it. Holds accumulate.

Operators
- F9. An operator lowers the on-hand-count under the reserved sum,
  after a loss, breakage or a recount → the promise's negation,
  arriving as a legitimate fact from the world.
- F10. An operator's adjustment and callers' reservations hit one
  item at the same instant → the race with a different partner.
- F11. A downward adjustment is resent → the on-hand-count lowered
  twice for one loss. (An upward one resent overstates the count.
  The promise is untouched, but the count is now wrong about the
  world: W1.)
- F13. Two adjustments to one item arrive in the other order than
  they were made → not knowing which value is current.

Network
- F12. A reply is lost after we admitted a reservation → the caller
  does not know. They retry (F2) or abandon (F8).

Our own process
- F15. We die between admitting a reservation and recording it, or
  between recording and replying → half-done work at every
  boundary. The caller retries against a state they cannot see.
- F16. We die between consume's two moves, ending the reservation
  and lowering the on-hand-count → one move done. Either the count
  is lowered while the reservation still counts, so reserved can
  exceed the on-hand-count. Or the reservation has ended while its
  consumed units still count.
- F17. Two of our processes run at once, in a deploy overlap or a
  scale-out → the race between our own instances. Any number an
  instance keeps in memory about the on-hand-count may already be
  wrong.
- F18. Our instances' clocks disagree → a reservation expired for
  one instance and active for another.

Store
- F19. A write's outcome is unknowable, as with a timeout after
  sending → not knowing whether the reservation exists.
- F21. The store answers two concurrent readers with the same count
  → two checks that both pass on units that fit only once. A read
  is not a reservation.
- F22. A read returns a count no longer current, from a lagging
  replica or a cache → a check passing on a number already gone.

Clock
- F23. Expiry ends a reservation while a consume for it is in
  flight → two exits racing, with time as one partner.
- F24. Time jumps, after a paused machine or a corrected clock →
  expiry firing for everything at once, or never.

(Numbering skips F14 and F20. They were merged during the census
into F5 and W4, and the gaps keep every reference true.)

### The runtime ground

What the system assumes about where it runs. The ground must honour
these facts. They are stated here so the ground is derived from
need, not habit.

- **One local machine.** The system and its evidence run on the
  reviewer's own machine, a Linux workstation. There is no hosted
  deployment (the intent excludes it) and no second machine.
- **A stranger's clean machine.** The same system and evidence must
  stand up on a clean machine by the README's commands alone. What
  such a machine needs is the README's to say.
- **More than one instance on that machine.** F17 is a fact, not a
  deployment choice. Our process must be able to run as several
  instances at once on the one machine, so the evidence can race
  them.
- **The store is a service on that machine.** Every instance can
  reach it, and it outlives any of them. Which store it is, is the
  ground's decision, not this definition's.
- **The clock is the machine's,** shared by the instances. Whether
  a reservation is active is judged by one clock, the store's
  (FC3, settled at SL-1's close).
- **The size it is built for is the evidence's:** a handful of
  items, up to a few thousand reservations on one item over its
  life, three instances, about a hundred requests racing on one
  item. Beyond it, nothing is promised about speed (W3). A decision
  that leans on a size says which size, and when to revisit it.

### Trust assumptions, accepted on purpose and not defended

- T1. The store durably holds a write it acknowledged. Its
  complement, loss below acknowledgment, is fenced: W4.
- T2. The store offers at least one way to make two concurrent
  writers disagree: something to serialize them or refuse one. This
  is a floor, not a nicety. Without it, no store can carry the
  promise. Which way is a slice's decision.
- T3. A request's stated caller and operator identity is what it
  claims to be. Authentication is outside: W5.
- T4. Only the ledger writes the store's data, and the identity it
  writes with is its alone. Only the migration tool writes the
  store's structure. The store's superuser is trusted, as in any
  store. The identity could do more than the ledger does, and a
  real deployment would restrict who holds it. This system assumes
  nobody else writes. Its complement is fenced: W7.

### How the census was shown complete

The census stopped by a count, not a feeling. Five lenses each
searched for facts. Each result was marked one of three ways:
- *new*;
- *nothing new*, naming the fact that already covered it;
- *out*, written down as a fence.

The census ended when two lenses in a row found no new fact.

| Lens | New facts |
|---|---|
| actors × vanishes / duplicates / lies | 17 |
| the assumption hunt — silent singulars and silent successes | 6 (F11, F13, F17, F18, F22, F24) |
| the timeline stretched to a year | 1 (F8), 1 out (W6) |
| every quantity at zero / many / huge | 0 |
| the assumption hunt again, over the additions | 0 |

The reviewer called the census complete on 2026-09-10, after an
audit. Every fact was asked what it assumes. Every quantity was
tried at zero, many and huge. Nothing that grows over time went
unasked. Every lens result carries its mark.

### Fences: what we write down and do not defend

- W1. Physical stock truth. The world's count is not ours. It
  reaches us only as an operator's adjustment at the door.
- W2. Reserve once-ness. The ledger does not check whether a
  reserve request repeats an earlier one. A retry after a lost
  reply makes a second hold, known to no one, orphaned until expiry
  ends it (F8). It over-holds; it cannot oversell. The cost
  accepted: the seller sells less than they could for the hold's
  duration. The claim that would remove it is kept in the intent,
  among the rejected candidates.
- W3. Throughput, latency, fairness between racers. Who wins a race
  is not ours. That the losers lose correctly is.
- W4. Loss below the store's acknowledgment: restores from an older
  backup, corrupted volumes. T1's complement. It belongs to another
  promise.
- W5. Who may reserve, consume, adjust. T3's complement.
- W6. Retention and growth of ended reservations' records. They
  cannot move the reserved sum. Written down so the growth is seen,
  not dropped.
- W7. Writers that bypass the ledger: a console, a script, another
  service, the superuser. T4's complement. What the store's own
  rules refuse, they refuse from these writers too, but no wall is
  owed against them.

### Not probed: where the census stopped

- Deployment transitions beyond overlap: schema changes, data
  migrations.
- Replication topologies beyond "a read can be stale" (F22).
- A malicious operator or caller, as opposed to a mistaken or
  retrying one. T3 stands in front of it.
- The clock beyond skew and jumps (leap seconds, monotonic vs
  wall).

### Scope verdicts

Boundary questions the census raised, each decided by the
reviewer, 2026-09-10:

- V1. The downward correction under the reserved sum (F9): **in.**
  It is the promise's own negation, arriving legitimately. It is the
  same kind of danger: the on-hand-count against the units held. The
  reader needs to see the promise survive the world contradicting
  the ledger.
- V2. Expiry (F8, F18, F23, F24): **in.** A timed exit racing a
  consume is contention, with time as the partner. The duration
  policy stays refused (L2).
- V3. Our own instances racing (F17): **in.** It is the same class,
  and the evidence must create it. A design allowed to assume one
  instance would be proven for a system nobody runs.
- V4. Store loss below acknowledgment: **out,** W4. It is a new kind
  of difficulty, recovery, which belongs to another promise.
- V5. Over-holding and starvation by duplicates or abandoned
  reservations (F2, F8): **censused and fenced.** The facts stay,
  because expiry is their exit. The boundary (W2) holds.

## L2 — What must be ours

Each possession passed one test: *if someone else owned this, could
they break the promise without us being able to stop them?* Yes →
ours. No → the mirror question: *can the promise stay true even if
this goes wrong elsewhere?* Yes → refused, in writing. Nothing is
refused for scope. What is refused is what cannot make the promise
false.

### Possessions

- **P1. The per-item on-hand-count, and every change to it.** An
  outside owner could lower it under the reserved sum, breaking the
  promise, and we could not stop them. It is the ledger's own
  number. Restock, loss and recount reach it only as requests at
  our door.
- **P2. The reservation records, and the transitions into active.**
  Which reservations exist, for which item, how many units. An
  outside owner reviving a released reservation raises the sum
  past the on-hand-count.
- **P3. The admit-or-refuse decision on a reservation request,
  under concurrent requests.** The moment the race is won or lost.
  Owning both numbers and letting someone else compare them is
  exactly how naive systems oversell.
- **P4. Consume, which ends a reservation and lowers the
  on-hand-count together, once per reservation.** The one act that
  moves both numbers. An outside owner could lower the count
  without ending the hold, or lower it twice for one hold. Either
  breaks the promise for everyone else on the item.

Release and expiry, the transitions out of active, are ours because
we own the records. But they carry no promise: they only lower the
sum.

### Refusals, each with its mirror

- **Reserve once-ness.** A retried reserve makes a second hold. It
  is admitted against the on-hand-count like any other, so the sum
  still fits. The promise stays true; the caller over-holds. This
  is a quality of the surface, not this promise's. The claim that
  would own it is kept in the intent, among the rejected
  candidates.
- **Expiry duration.** How long a hold lasts is the caller's or the
  seller's policy. The promise holds for any duration.
- **The item catalog.** Which items exist is someone else's fact.
  The promise holds for each item the ledger knows, and an unknown
  item is refused at the door.
- **Physical stock truth.** What the warehouse actually holds is
  not the on-hand-count. The count can be wrong about the world and
  the promise still holds. But a downward correction arrives as a
  request to lower the count. If that puts the count under the units
  held, the promise breaks. So the correction is a collision, worked
  in L4, not a refusal.
- **The caller's view.** An answer that does not match the persisted
  outcome misleads the caller, but the state property holds.
- **Orders, payment, pricing, who may reserve.** Outside what the
  ledger covers.

### The edge

Every request crosses at the door: reserve(item, quantity),
release, consume, adjust the on-hand-count. It is parsed and
checked there. An unknown item, a quantity not above zero, a
reservation the ledger does not know: each is refused at the door,
never inside.

## L3 — The owners

A division is real only if it would hold separate state *and*
separate decision authority. Four concerns do not make four areas.

### What each concern touches

| Concern | State touched | Decision touched |
|---|---|---|
| SL-1 — over-admission | the on-hand-count; the active reservations of the item | admit or refuse a reservation, against both |
| SL-2 — the downward correction | the on-hand-count; the active reservations of the item | admit or refuse a change to the count, against the sum |
| SL-3 — a reservation exits once | one reservation's active state; the on-hand-count (on consume) | whether this exit is the reservation's first and only |
| SL-4 — consume's two moves | one reservation's active state; the on-hand-count | none of its own: it holds SL-1's and SL-3's outcomes together |

Every concern reads the same two numbers of the same item and
decides against their comparison.

### Seams probed and refused

- *Stock adjustments as their own area.* Its state would be the
  on-hand-count. But SL-1's admit decision reads the count, and
  SL-2's reads the reserved sum. One number cannot be owned by an
  area whose decisions are taken by another. Fails on state.
- *Expiry, or the clock, as its own area.* Its state would be each
  reservation's expiry moment, which is the reservation record
  itself. Ending a hold is SL-3's exit. No separate state.
- *The door (request validation) as its own area.* FC1 and FC2 are
  checks with no state of their own. The door is a step every
  request passes, not an owner.
- *Consume, or fulfilment, as its own area.* It moves both numbers
  in one act. Separate authority over a number is what SL-4
  forbids.
- *One area per item.* The same decision over the same kind of
  state, repeated. That divides the load, not the ownership: a
  mechanism, parked.

### Ownership: one area, the reservation ledger

It owns the per-item on-hand-count, the reservations with their
active state, and three decisions: admit a reservation, admit a
change to the count, allow an exit. This is confirmed, not
assumed: four real concerns were sorted, and five seams refused in
writing. The intent's sixth test (one owner, a full lifecycle
inside one system) is settled here.

## L4 — The collisions, and what dies

Each possession (L2) was collided against every fact (L1) that can
reach it. A kill states what dies, never how it is saved. Every
mechanism that came up is parked at the end, unchosen at framing.
Kills are grouped by what a fact does to us, never by whose fault it
is. Each group is a concern, and the concerns differ in what their
proof must show.

### Kills

1. F1 × P3 → both racing admits succeed on units that fit only
   once: **reserved exceeds the on-hand-count.**
   *Say:* 10 on hand, 8 held. Two reserves of 2 arrive together;
   both see 2 free, both are admitted: 12 held of 10.
2. F17 × P3 → two of our instances admit at once, each on a count
   it holds in memory: **reserved exceeds the on-hand-count.** Kill
   1's death, the racers our own instances.
   *Say:* 10 on hand, 8 held, remembered by both instances. Each
   admits a reserve of 2 against its memory: 12 held of 10.
3. F21 × P3 → the store answers two concurrent checks with the same
   count, and both pass: **reserved exceeds the on-hand-count.**
   Kill 1's death, as the store shows it.
   *Say:* 10 on hand, 8 held. Two checks both read 8 held; each
   reserve of 2 fits alone; both are written: 12 held of 10.
4. F22 × P3 → an admit passes on a count already gone: **reserved
   exceeds the on-hand-count** on a number that was never the truth.
   Kill 1's death, by a stale read.
   *Say:* the count fell from 10 to 6, and 6 are held. A lagging
   replica still reads 10; a reserve of 2 is admitted: 8 held of 6.
5. F10 × P3, P1 → an admit passes while a concurrent adjustment is
   lowering the count: **reserved exceeds the on-hand-count.** Kill
   1's death, the partner an operator, not a caller.
   *Say:* 10 on hand, 6 held. A reserve of 2 checks against 10 while
   an operator sets the count to 7; both land: 8 held of 7.
6. F9 × P1 → a legitimate downward correction sets the on-hand-count
   under the reserved sum: **the promise dies by one honest
   request,** no race needed.
   *Say:* 10 on hand, 8 held. Three units break, and the operator
   asserts 7: 8 held of 7.
7. F11 × P1 → a resent downward correction lowers the count twice
   for one loss, two ways:
   (a) the second lowering goes under the reserved sum: **the count
   set under the units held,** kill 6's death caused by a resend.
   (b) it stays above the sum: the count is wrong about the world.
   Stopped by W1.
   *Say:* (a) 10 on hand, 9 held. One unit is lost; the "lower by
   1" is sent twice: 8 on hand, 9 held.
8. F13 × P1 → reordered adjustments leave the count at the older
   value, two ways:
   (a) the older value is the lower: **the count set under the
   units held,** kill 6's death caused by the order.
   (b) the older is the higher: the count is wrong about the world.
   Stopped by W1.
   *Say:* (a) 5 held. The operator asserts 6, then 9 after a
   restock, and callers reserve 3 more against 9: 8 held. The 6
   arrives last: 8 held of 6.
9. F15 × P2, P3 → we die between deciding, recording and
   replying, two ways:
   (a) an admit decided but not yet recorded is a decision no
   other admit can see: **two decisions on one unit**, kill 1's
   death from our own half-done work.
   (b) recorded but never replied: an orphaned hold. Stopped by W2
   and V5: expiry is its exit.
   *Say:* (a) 10 on hand, 9 held. A reserve of 1 is decided, not
   yet recorded; a second reserve of 1 sees 1 free and is admitted;
   the first is recorded: 11 held of 10.
10. F18 × P2, P3 → two instances judge one reservation's activeness
    differently; the one seeing the smaller sum admits: **reserved
    exceeds the on-hand-count by the store's own record.** Kill 1's
    death, by clocks that disagree.
    *Say:* 10 on hand, 10 held, a hold of 2 expiring at 12:00:00. At
    11:59:59, an instance whose clock reads 12:00:01 counts it
    ended, sees 2 free, and admits 2: the store holds 12 of 10.
11. F5 × P2, P4 → a consume lands on a reservation that already
    ended, its units since reserved by someone else: **the count
    lowered for units nobody holds; reserved exceeds it for the
    others.**
    *Say:* 10 on hand. R held 2 and expired, and its 2 units were
    reserved again: 10 held by others. R's late consume lowers the
    count to 8: the others hold 10 of 8.
12. F3 × P4 → a retried consume lowers the count twice for one
    reservation: **the count lowered for units nobody holds;
    reserved exceeds it for the others.** Kill 11's death, by a
    retry.
    *Say:* 10 on hand, 10 held, R holds 2. R's consume lands: 8 on
    hand. Its reply is lost and the retry lands too: 6 on hand,
    while the others hold 8.
13. F4 × P4 → a consume and a release, or two consumes, race on one
    reservation: **both exits act, so units are returned and
    removed, or removed twice.**
    *Say:* 10 on hand, 4 held, R holds 2. A consume and a release of
    R both act: 8 on hand and 0 held, while another reservation
    still holds 2. A reserve of 8 now fits: 10 held of 8.
14. F23 × P4 → expiry and consume race on one reservation: **both
    exits act,** kill 13's death with time as one partner.
    *Say:* 10 on hand, 4 held, R holds 2 until 12:00. At 12:00 its
    expiry frees 2 as its consume lowers both numbers by 2: 8 on
    hand and 0 held, while another reservation still holds 2.
15. F16 × P4 → we die between consume's two moves: **a readable
    state with the count lowered while the reservation still
    counts, or the reservation ended while consumed units still
    count.**
    *Say:* 10 on hand, 10 held, R holds 2. Consume lowers the count
    to 8; we die before R ends: 10 held of 8.
16. F19 × P4, P2 → a write's outcome is unknowable, three ways:
    (a) consume's, and we retry: **the count lowered for units
    nobody holds,** kill 12's death.
    (b) consume's, and we stay silent: **one of consume's two moves
    readable without the other,** kill 15's death.
    (c) a reservation's, which we cannot confirm: nothing
    promise-bearing dies. The caller's view is refused (L2); the
    possible orphan is stopped by V5.
    *Say:* (a) 10 on hand, 10 held, R holds 2. The consume lands,
    its answer times out, and our retry lands too: 6 on hand while
    the others hold 8. (b) The count falls to 8 and R still
    counts: 10 held of 8.
17. F7 × P4 → a consume makes a false claim at the door, two ways:
    (a) it names a quantity its reservation does not hold: **the
    count lowered by units the caller never held; reserved exceeds
    it for the others.**
    (b) it names a reservation not its own: stopped by W5 (T3).
    *Say:* (a) 10 on hand, 10 held, R holds 2. A consume of R claims
    5: the count falls to 5 while the others hold 8.
18. F6 × P3 → a request that means nothing (zero, absurd, an
    unknown item) reaches the decision: **the numbers move by an
    amount that means nothing.**
    *Say:* 10 on hand, 8 held. A reserve of −5 is admitted: 3 held.
    Reserves of 7 more now fit, and 15 units are held of 10.
19. F24 × P2 → time jumps, two ways:
    (a) expiry fires for everything at once: holds end early, and
    their late consumes follow: **the count lowered for units
    nobody holds,** kill 11's death.
    (b) expiry never fires: holds last forever. Stopped by V5.
    *Say:* (a) 10 on hand, 10 held. The clock jumps an hour: every
    hold ends, and callers reserve 10 again. The first holders'
    consumes land late and lower the count to 0 while 10 are held.
20. F2, F12 × P2 → a retried or abandoned reserve makes a second or
    orphaned hold: **nothing dies.** It over-holds; it cannot
    oversell. Stopped by W2 and V5.

### Concerns: each an invariant, its adversity named

- **SL-1 — over-admission under concurrent writers.** Kills 1, 2, 3,
  4, 5, 9, 10. One death: more units admitted than fit, because the
  decision read a number that was not the truth at the moment of
  writing. *Invariant: for every item, in every readable state, the
  sum of active reservations ≤ on-hand-count.* Adversity: concurrent
  admits on one item's last units, from many callers and from our own
  instances. Some see the same count in two checks. Some decide on a
  stale read. Some meet a concurrent downward adjustment. Some admits
  are decided but not yet recorded. Some run under clocks that
  disagree about activeness. Proof obligation: create the contention,
  read the witness from state.
- **SL-2 — the downward correction.** Kills 6, 7, 8. The promise's
  negation arriving as a legitimate request. Requests one after
  another are enough to stage it. *Invariant: no admitted change to
  the on-hand-count leaves it under the sum of active reservations.*
  Adversity: an operator lowering the count under the reserved sum,
  once, twice, or out of order. Proof obligation differs from
  SL-1's. There is no race to create. The question is what the
  ledger does with an honest request it cannot honour as asked. Two
  options were seen and parked, not chosen at framing: refuse it, or
  let it end reservations. SL-2 chose to refuse (its record, "The
  correction's shape — decided").
- **SL-3 — a reservation exits once.** Kills 11, 12, 13, 14, 16
  (retry), 19 (early). *Invariant: a reservation moves its item's
  numbers at most once on exit, and never after it has ended.*
  Adversity: duplicated exits, ours among them after an unknown
  outcome (F19); racing exits (consume × release, consume ×
  consume, consume × expiry); late exits on ended reservations;
  expiry fired early. Proof obligation: duplicates and races
  collapse to one exit.
- **SL-4 — consume's two moves hold together.** Kills 15, 16
  (silence). *Invariant: no readable state holds one of consume's
  two moves without the other.* Adversity: our death, or an
  unknowable outcome, between ending the reservation and lowering
  the count. Proof obligation differs from SL-3's. Not "duplicates
  collapse", but "the half-done converges": a consume cut off
  midway ends with both moves or neither.

### Folds: rules checked at the door

No adversity can be staged against these beyond sending the
request. Each is checked in the first slice that needs it.

- **FC1. What a valid request is** (kill 18): a known item, a
  positive bounded quantity, a reservation that exists.
- **FC2. An exit names its own reservation and moves exactly what
  it holds** (kill 17). A lying caller can break it. Its proof is a
  plain check: the false request is sent, and no adversity is
  staged.
  The caller's identity is W5 and T3.
- **FC3. What *active* means:** not ended by an exit, and not past
  its expiry as judged by one clock, not one per instance. The
  clock's facts (F18, F23, F24) reach the ledger through SL-1 and
  SL-3, which use this fold.

### What the fences stopped here

W1 stopped the world-truth parts of kills 7 and 8. W2 stopped kill
20 and half of kill 9. W3 stopped the fairness question inside
kill 1. W5 stopped the identity part of kill 17. V5 stopped the
other half of kill 9, kill 16's orphan, kill 19's never, kill 20.
W4 and W6 stopped nothing, since no censused fact reaches them.
They are kept because the lenses raised them, and each was written
down.

### Coverage: every fact placed

| Fact | Lands in |
|---|---|
| F1, F10, F17, F21, F22 | SL-1 |
| F15 | SL-1 (the decision half), W2/V5 (the orphan half) |
| F18 | SL-1, using FC3 |
| F9, F11, F13 | SL-2; their world-truth remainder W1 |
| F3, F4, F5, F23 | SL-3 |
| F24 | SL-3 (early), V5 (never) |
| F16 | SL-4 |
| F19 | SL-4 (consume), SL-3 (our retry), L2's refused view + V5 (reserve) |
| F6 | FC1 |
| F7 | FC2, W5 |
| F2, F8, F12 | W2, V5 |

P2, the reservation records, lends its facts to SL-1, SL-3 and SL-4.
It keeps no kill of its own. Every kill that meets it is placed in
one of those slices or stopped by a fence. P3 is SL-1's whole
surface. P4 is SL-3's and SL-4's. P1 meets the adjustments and the
races.

### Mechanisms parked

Each came up while stating a kill and was stopped there. None was
chosen at framing, and none is named in a kill. Each slice's record
says what it chose:
- something that makes two writers to one item disagree (SL-1);
- the form of an adjustment: an amount to add or subtract (a
  delta), or a new total (SL-2);
- a single clock source for activeness (FC3);
- one act for consume's two moves (SL-4);
- a marker that makes a retried exit recognisable (SL-3).

## L5 — Between owners

**Empty, with its reasons.** L5 holds what must be true across
seams that no local guarantee covers. There are no seams, and each
absence traces to a decision already taken:

- *No internal seams.* L3 sorted four real concerns into one area
  and refused five candidate divisions in writing. Nothing crosses
  a boundary inside the system, because there is no boundary inside
  it.
- *No sibling systems to coordinate with.* The catalog, physical
  stock truth, orders, payment, and who may act were refused at L2
  and fenced at L1 (W1, W5). The ledger owes them nothing, and they
  owe it nothing the promise depends on.
- *Callers and operators are environment, not owners.* They are
  actors in L1. What they do reaches the ledger as facts at the
  door, never as an agreement between peers.
- *Our own instances are not areas.* Two of us running is a fact
  (F17) inside one area, handled by SL-1. Instances share state;
  they do not divide it.
- *Per-item partition is not a seam.* L3 refused it as a mechanism
  of scale. If ever chosen, it divides the same decision over
  separate items. It creates no guarantee across items, and the
  promise needs none, because it holds per item.

**What L5 would have held, had a seam been drawn.** It is recorded
so the emptiness is checked, not assumed. Had the operator side
been its own area, L5 would have had to state two things across the
seam: the order between an adjustment and a concurrent admit
(kill 5), and how a correction that ends reservations fails as a
whole (the option SL-2 rejected). Both live inside one area, as SL-1
and SL-2, with no seam between them.

## Revision log

<!-- Dated entries only: what changed, why, what triggered it. -->

- 2026-09-10 — heading: the working name safe-reservations
  replaced by never-oversold (ADR-0003). Triggered by PLAN Step 2.
  No layer changed.
- 2026-09-10 — L1 gains "The runtime ground": the machine, the
  stranger's machine, plural instances, the store as a service, the
  clock. Why: the framing left the runtime facts unstated, and the
  ground must be derived from them. Triggered by PLAN Step 3's
  readiness check. No fact, fence, or verdict changed.
- 2026-10-01 — L1's census table: the second lens's count corrected
  from 5 to 6. Why: the line listed six facts and counted five; the
  twenty-four numbers F1–F24 need six. Triggered by a documentation
  error check before Step 7. No fact, fence, or verdict changed.
- 2026-10-08 — L1's trust list gains T4, and the fences W7, its
  complement: who writes the store. Why: the framing never said, so
  each slice defended against writers no one had named — SL-3 built
  part of its walls against scripts holding the ledger's identity —
  while every other part of the system already pictured the door as
  the only way in. Triggered by the review after SL-3's close. No
  fact, kill or verdict changed.
- 2026-10-08 — L1's runtime ground gains the size the system is
  built for. Why: no record said it, so every wall assumed one
  silently — SL-3's check on the units held reads an item's
  reservations at every commit, ended ones included, fine at the
  evidence's size, with nothing saying how far beyond it that
  stays true. Triggered by the same review.
  No fact, fence or verdict changed.
- 2026-10-10 — L1's runtime ground: the clock's line no longer
  points at SL-1's flag as open. Why: SL-1 resolved it at its
  close — FC3, one clock, the store's (registry, 2026-09-14) — and
  the line still read as if the evidence might skew the clock.
  Triggered by a records clean-up before Release. No fact, fence
  or verdict changed.
- 2026-10-13 — kills 4, 5, 17 and 18, F16, and L2's edge regain what
  composing them from the derivation lost: four kills their death
  (kill 18 also its examples of nonsense), F16 its link to the
  promise, and the edge its meaning ("a reservation not the
  caller's own" said the door checks identity, which W5 fences and
  kill 17 places outside). Why: a reader of the definition got the
  weaker version, or a false one. Triggered by Step 10's comparison
  against the derivation (the retrospective's D7). No fact, kill,
  fence or verdict changed.
- 2026-10-13 — The note on the order the layers were derived in
  dropped: it told how framing ran, not what the system is. The
  census's saturation record stays in L1, as the evidence its fact
  list is complete. Triggered by Step 10 (the retrospective's D5).
  No fact, kill, fence or verdict changed.
- 2026-10-14 — L4's four concerns are called by the slice each
  became, SL-1 to SL-4, where they were letters A to D, through L3,
  L4 and L5. Each concern became exactly one slice, so the letter
  was a second name for it. L4's "Definitions" heading becomes
  "Folds", the name the registry and the slice records use for
  FC1 to FC3. Triggered by Step 10 (the retrospective's D6). No
  fact, kill, fence, verdict or fold changed.
- 2026-10-14 — L4's kills brought to one form, meaning unchanged.
  Each states its own death in bold: kills 2, 3, 7, 8, 12, 14, 16
  and 19 pointed to another kill's. A kill that meets one
  possession in more than one way labels its cases (a), (b); a
  case a fence stops says "stopped by" and the fence; kill 20 says
  "nothing dies". Every kill that dies gains a worked example with
  real numbers. Why: a reader landing on a kill had to read another
  to see what breaks, and no kill showed it happen. Triggered by
  Step 10 (the retrospective's D2 and D29). No fact, kill, fence or
  verdict changed.
- 2026-10-14 — written plain, meaning unchanged: one idea per
  sentence, dash and semicolon chains split, metaphors out ("in
  ink", "the census's edge", "a check, not a shrug", "enemies",
  "the stage, not a victim"). The refused claims are "kept in the
  intent, among the rejected candidates", the intent's own heading.
  FC3's wording slip corrected: it described a reservation that is
  not active, and now reads as SL-1's record does. Why: the reader
  had to untangle it. Triggered by Step 10 (the
  retrospective's D2). No fact, kill, fence, verdict or fold
  changed.
- 2026-10-14 — read against the registry and the slice records.
  L4's opening and "Mechanisms parked" say nothing was chosen at
  framing, and that each slice's record says what it chose; SL-2's
  concern names its choice, refuse. SL-1's adversity names two
  facts its kills need (F21, F15). F14 and F20 were "merged", not
  "folded", since a fold is FC1 to FC3. "Shape" used for a choice
  becomes "option" or "form", since a shape is a page under
  `.claude/shapes/`. The header comment drops the derivation order,
  as the 2026-10-13 entry said. Why: lines out of step with the
  records they name. Triggered by Step 10's second reading of the
  records. No fact, kill, fence, verdict or fold changed.
- 2026-10-14 — SL-3's adversity names F19, our own retry after an
  unknown outcome, as the registry's does; pass 2 had added it to
  the registry only. Triggered by Step 10's third reading of the
  records. No fact, kill, fence, verdict or fold changed.
- 2026-10-14 — read against the kill shape at Step 10's close.
  Kills 4, 5 and 10 say their death is kill 1's, as kills 2, 3 and 9
  do. Kill 18 no longer says "Stopped at the door": it dies, the
  door is not a fence, and FC1 covers the door. No fact, kill,
  fence, verdict or fold changed.
