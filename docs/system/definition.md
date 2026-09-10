# System definition — safe-reservations (working name)

<!-- The layered system the promise derives: what we own (L2), the
     environment that attacks it (L1), the collisions and what
     dies (L4), the owners (L3), what holds between owners (L5).
     Composed at framing close from the derivation record beside
     it (framing/derivation.md), in derivation order — L2, L1, L4,
     L3, L5 — one layer per commit, so the history reads as the
     derivation. Stands alone. A living record: after close it
     changes only through a dated entry in the revision log at the
     end. -->

The promise this definition serves is in `intent.md`:
*for any item, the reserved quantity never exceeds the
on-hand-count, however many reservations race for the same units.*

Layers, in the order they were derived (the map's order is L1
outermost to L5 innermost; the derivation order is not the map's):
L2 what must be ours · L1 the environment · L4 the collisions ·
L3 the owners · L5 between owners.

## L2 — What must be ours

The test that earned every possession: *if someone else owned
this, could they break the promise without us being able to stop
them?* Yes → ours. No → the mirror: *can the promise stay true
even if this goes wrong elsewhere?* Yes → refused, in writing.
Refusal is never by scope: what is refused is what cannot make the
promise false.

### Possessions

- **P1. The per-item on-hand-count, and every change to it.** An
  outside owner lowering it under the reserved sum breaks the
  promise and we could not stop them. The ledger's own number;
  restock, loss and recount reach it only as requests at our door.
- **P2. The reservation records, and the transitions into active.**
  Which reservations exist, for which item, how many units; an
  outside owner reviving a released reservation raises the sum
  past the on-hand-count.
- **P3. The admit-or-refuse decision on a reservation request,
  under concurrent requests.** The moment the race is won or lost.
  Owning both numbers and letting someone else compare them is
  exactly how naive systems oversell.
- **P4. Consume — ending a reservation and lowering the
  on-hand-count together — once per reservation.** The one act
  that moves both numbers; an outside owner lowering the count
  without ending the hold, or lowering it twice for one hold,
  breaks the promise for everyone else on the item.

Release and expiry — the transitions out of active — are ours by
owning the records, but they carry no promise: they only lower the
sum.

### Refusals, each with its mirror

- **Reserve once-ness.** A retried reserve makes a second hold; it
  is admitted against the on-hand-count like any other, so the sum
  still fits. The promise stays true; the caller over-holds. A
  quality of the surface, not this promise's — the claim that
  would own it is banked in the intent.
- **Expiry duration.** How long a hold lasts is the caller's or the
  seller's policy; the promise holds for any duration.
- **The item catalog.** Which items exist is someone else's fact;
  the promise holds per item the ledger knows, and an unknown item
  is refused at the door.
- **Physical stock truth.** What the warehouse actually holds is
  not the on-hand-count; the count can be wrong about the world
  and the promise still holds. Consequence: a downward correction
  arrives as a request to lower the count, and lowered under the
  sum the promise would die — so it is a collision, run below, not
  a refusal.
- **The caller's view.** An answer that mismatches the persisted
  outcome lies to the caller; the state property holds.
- **Orders, payment, pricing, who may reserve.** Outside the
  territory.

### The edge

Every request crosses at the door — reserve(item, quantity),
release, consume, adjust the on-hand-count — parsed and checked
there. An unknown item, a non-positive quantity, a reservation not
the caller's own: refused at the door, never inside.

## L1 — The environment

Everything beyond our control, named by what it does to us. The
census lists what can *hurt*, never what we trust; the short trust
list beside it is the one deliberate exception, each line
challengeable.

### Actors

- **Callers** — the services that reserve, consume and release on
  behalf of a seller's customers; they move the reserved side.
- **Operators** — whoever changes the on-hand-count from the
  world's side: a restock, a loss, a recount. A person at a screen
  or a stock system; the ledger sees an adjust request at the door
  either way. They move the on-hand-count side.
- **The network** — carries requests and replies; loses,
  duplicates, delays, reorders them.
- **Our own process** — the ledger's running instances, which die
  mid-work and can be more than one.
- **The store** — where the ledger's numbers persist; answers reads
  and takes writes; can be slow, stale, or silent.
- **The clock** — ends reservations by expiry; skews between
  instances and jumps.

### Facts, consequence-first

Callers
- F1. Many callers send reserve for one item at the same instant,
  together asking more than the on-hand-count → the race for the
  last units: each request fits alone, together they do not.
- F2. A caller resends reserve after a lost reply → the same
  request twice; a second hold takes units others need.
- F3. A caller resends consume after a lost reply → the
  on-hand-count lowered twice for one reservation.
- F4. A caller sends consume and release for one reservation at the
  same instant, or two consumes → two exits racing on one
  reservation, each moving numbers.
- F5. A caller consumes or releases a reservation that already
  ended — expired, consumed, released — including very late → a
  request against a reservation with no active state left.
- F6. A caller asks for zero, a negative or absurd quantity, or an
  unknown item → nonsense at the door.
- F7. A caller consumes a reservation not theirs, or claims a
  quantity other than the reservation's → a lie at the door.
- F8. A caller never returns → a reservation held forever unless
  something ends it; holds accumulate.

Operators
- F9. An operator lowers the on-hand-count under the reserved sum —
  loss, breakage, a recount → the promise's negation arriving as a
  legitimate fact from the world.
- F10. An operator's adjustment and callers' reservations hit one
  item at the same instant → the race with a different partner.
- F11. A downward adjustment is resent → the on-hand-count lowered
  twice for one loss. (An upward one resent overstates the count;
  the promise is untouched; the world is lied to — W1.)
- F13. Two adjustments to one item arrive in the other order than
  they were made → not knowing which value is current.

Network
- F12. A reply is lost after we admitted a reservation → the caller
  does not know; they retry (F2) or abandon (F8).

Our own process
- F15. We die between admitting a reservation and recording it, or
  between recording and replying → half-done work at every
  boundary; the caller retries against a state they cannot see.
- F16. We die between consume's two moves — ending the reservation,
  lowering the on-hand-count → one move done: the count lowered
  while the reservation still counts, or the reservation ended
  while consumed units still count.
- F17. Two of our processes run at once — a deploy overlap, a
  scale-out → the race between our own instances; any belief an
  instance holds in memory about the on-hand-count is a lie.
- F18. Our instances' clocks disagree → a reservation expired for
  one instance and active for another.

Store
- F19. A write's outcome is unknowable — a timeout after sending →
  not knowing whether the reservation exists.
- F21. The store answers two concurrent readers with the same count
  → two checks that both pass on units that fit only once. A read
  is not a reservation.
- F22. A read returns a count no longer current — a lagging
  replica, a cache → a check passing on a number already gone.

Clock
- F23. Expiry ends a reservation while a consume for it is in
  flight → two exits racing, time being one partner.
- F24. Time jumps — a paused machine, a corrected clock → expiry
  firing for everything at once, or never.

(Numbering skips F14 and F20: folded during the census into F5 and
W4; the gaps keep every reference true.)

### Trust assumptions — accepted deliberately, not defended

- T1. The store durably holds a write it acknowledged. Its
  complement, loss below acknowledgment, is fenced: W4.
- T2. The store offers at least one way to make two concurrent
  writers disagree — something to serialize or refuse on. A floor,
  not a nicety: without it no store can carry the promise. Which
  way is a slice's decision.
- T3. A request's stated caller and operator identity is what it
  claims to be. Authentication is outside: W5.

### How saturation was earned

The census stopped growing by a number, not a feeling: five lenses
run over the same territory, each result stamped new, nothing new
(naming the covering line), or out (fenced in ink); the exit was
two consecutive lenses with zero new facts.

| Lens | New facts |
|---|---|
| actors × vanishes / duplicates / lies | 17 |
| the assumption hunt — silent singulars and silent successes | 5 (F11, F13, F17, F18, F22, F24) |
| the timeline stretched to a year | 1 (F8), 1 out (W6) |
| every quantity at zero / many / huge | 0 |
| the assumption hunt again, over the additions | 0 |

Saturation called by the reviewer, 2026-09-10, after the audit:
every line got the assumption question, every quantity sat in the
grid, nothing accumulated un-asked, every probe result carries a
stamp.

### Fences — exclusions in ink

- W1. Physical stock truth. The world's count is not ours; it
  reaches us only as an operator's adjustment at the door.
- W2. Reserve once-ness. The ledger does not check whether a
  reserve request repeats an earlier one: a retry after a lost
  reply makes a second hold, known to no one, orphaned until expiry
  ends it (F8). It over-holds; it cannot oversell. The cost
  accepted: the seller sells less than they could for the hold's
  duration. The claim that would remove it is banked in the intent.
- W3. Throughput, latency, fairness between racers. Who wins a race
  is not ours; that the losers lose correctly is.
- W4. Loss below the store's acknowledgment — restores from an older
  backup, corrupted volumes. T1's complement; another promise's
  territory.
- W5. Who may reserve, consume, adjust. T3's complement.
- W6. Retention and growth of ended reservations' records. They
  cannot move the reserved sum; written so the growth is seen, not
  dropped.

### Not probed — the census's edge

- Deployment transitions beyond overlap: schema changes, data
  migrations.
- Replication topologies beyond "a read can be stale" (F22).
- A malicious operator or caller, as opposed to a mistaken or
  retrying one; T3 stands in front of it.
- The clock beyond skew and jumps (leap seconds, monotonic vs
  wall).

### Scope verdicts

Boundary questions the census raised, each decided by the
reviewer, 2026-09-10:

- V1. The downward correction under the reserved sum (F9): **in** —
  the promise's own negation arriving legitimately, the same kind,
  and the reader needs to see the promise survive the world
  contradicting the ledger.
- V2. Expiry (F8, F18, F23, F24): **in** — a timed exit racing a
  consume is contention with time as the partner; the duration
  policy stays refused (L2).
- V3. Our own instances racing (F17): **in** — the same class, and
  the evidence must create it: a design allowed to assume one
  instance would be proven for a shape nobody runs.
- V4. Store loss below acknowledgment: **out**, W4 — a new kind of
  difficulty, recovery, another promise's territory.
- V5. Over-holding and starvation by duplicates or abandoned
  reservations (F2, F8): **censused and fenced** — the facts stay
  because expiry is their exit; the boundary (W2) holds.

## L4 — The collisions, and what dies

Each possession (L2) collided against every fact (L1) that can
reach it. A kill states what dies, never how it is saved; every
mechanism that surfaced is parked at the end, unchosen. Kills are
grouped by attack surface — what a fact does to us, never whose
fault — into concerns whose proof obligations differ.

### Kills

1. F1 × P3 → both racing admits succeed on units that fit only
   once: **reserved exceeds the on-hand-count.**
2. F17 × P3 → the same death, the racers being our own instances,
   each admitting on a count it holds in memory.
3. F21 × P3 → the same death as the store shows it: two checks pass
   on one count.
4. F22 × P3 → an admit passes on a count already gone.
5. F10 × P3, P1 → an admit passes while a concurrent adjustment is
   lowering the count: the partner an operator, not a caller.
6. F9 × P1 → a legitimate downward correction sets the on-hand-count
   under the reserved sum: **the promise dies by one honest
   request,** no race needed.
7. F11 × P1 → a resent downward correction lowers the count twice
   for one loss: kill 6 in a second costume; the part that fits
   under the sum leaves the count wrong about the world — W1.
8. F13 × P1 → reordered adjustments leave the count at the older
   value: kill 6 if the older is lower; otherwise W1.
9. F15 × P2, P3 → an admit decided but not yet recorded is a
   decision no other admit can see: **two decisions on one unit.**
   The other half — recorded but never replied — is an orphaned
   hold: W2, V5, expiry its exit.
10. F18 × P2, P3 → two instances judge one reservation's activeness
    differently; the one seeing the smaller sum admits: **reserved
    exceeds the on-hand-count by the store's own record.**
11. F5 × P2, P4 → a consume lands on a reservation that already
    ended, its units since reserved by someone else: **the count
    lowered for units nobody holds; reserved exceeds it for the
    others.**
12. F3 × P4 → a retried consume lowers the count twice for one
    reservation: the same death.
13. F4 × P4 → a consume and a release, or two consumes, race on one
    reservation: **both exits act — units returned and removed, or
    removed twice.**
14. F23 × P4 → expiry and consume race on one reservation: kill 13
    with time as one partner.
15. F16 × P4 → we die between consume's two moves: **a readable
    state with the count lowered while the reservation still
    counts, or the reservation ended while consumed units still
    count.**
16. F19 × P4 → consume's outcome unknowable: our own retry is kill
    12; our silence is kill 15's half-state. F19 × P2 → a
    reservation we cannot confirm: nothing promise-bearing dies —
    the caller's view is refused (L2), the possible orphan is V5.
17. F7 × P4 → a consume names a quantity its reservation does not
    hold, or a reservation not its own: **the count lowered by
    units the caller never held.** A lie at the door; identity is
    W5/T3.
18. F6 × P3 → a nonsense request reaches the decision: **the
    numbers move by an amount that means nothing.** At the door.
19. F24 × P2 → time jumps: expiry fires for everything at once —
    holds end early, late consumes follow (kill 11); or never —
    holds last forever, V5.
20. F2, F12 × P2 → a retried or abandoned reserve makes a second or
    orphaned hold: over-holds, cannot oversell — W2, V5.

### Concerns — invariant-shaped, adversity named

- **A — over-admission under concurrent writers.** Kills 1, 2, 3,
  4, 5, 9, 10. One death: more units admitted than fit, because the
  decision read a number that was not the truth at the moment of
  writing. *Invariant: for every item, in every readable state, the
  sum of active reservations ≤ on-hand-count.* Adversity: concurrent
  admits on one item's last units — from many callers, from our own
  instances, against a stale read, against a concurrent downward
  adjustment, under clocks that disagree about activeness. Proof
  obligation: create the contention, read the witness from state.
- **B — the downward correction.** Kills 6, 7, 8. The promise's
  negation arriving as a legitimate request; sequential suffices to
  stage it. *Invariant: no admitted change to the on-hand-count
  leaves it under the sum of active reservations.* Adversity: an
  operator lowering the count under the reserved sum — once, twice,
  out of order. Proof obligation differs from A's: no race to
  create; the question is what the ledger does with an honest
  request it cannot honour as asked. Two shapes seen and parked,
  not chosen: refuse it, or let it end reservations.
- **C — a reservation exits once.** Kills 11, 12, 13, 14, 16
  (retry), 19 (early). *Invariant: a reservation moves its item's
  numbers at most once on exit, and never after it has ended.*
  Adversity: duplicated exits, racing exits (consume × release,
  consume × consume, consume × expiry), late exits on ended
  reservations, expiry fired early. Proof obligation: duplicates
  and races collapse to one exit.
- **D — consume's two moves hold together.** Kills 15, 16
  (silence). *Invariant: no readable state holds one of consume's
  two moves without the other.* Adversity: our death, or an
  unknowable outcome, between ending the reservation and lowering
  the count. Proof obligation differs from C's: not "duplicates
  collapse" but "the half-done converges".

### Definitions — contract-shaped, checked at the door

No adversity stages against these beyond sending the request; each
folds into the first slice that consumes it.

- **FC1. What a valid request is** (kill 18): a known item, a
  positive bounded quantity, a reservation that exists.
- **FC2. An exit names its own reservation and moves exactly what
  it holds** (kill 17). Enemy-touched — a lying caller — but its
  proof is a check, not a staged adversity; identity is W5/T3.
- **FC3. What *active* means:** ended by an exit, or past its
  expiry as judged by one clock, not one per instance. The clock's
  enemies act through A and C, which consume this definition.

### What the fences stopped here

W1 stopped the world-truth parts of kills 7 and 8. W2 stopped kill
20 and half of kill 9. W3 stopped the fairness question inside
kill 1. W5 stopped the identity part of kill 17. V5 stopped the
other half of kill 9, kill 16's orphan, kill 19's never, kill 20.
W4 and W6 stopped nothing: no censused fact reaches them; kept
because the probes raised them in ink.

### Coverage — every fact placed

| Fact | Lands in |
|---|---|
| F1, F10, F17, F21, F22 | A |
| F15 | A (the decision half), W2/V5 (the orphan half) |
| F18 | A, consuming FC3 |
| F9, F11, F13 | B; their world-truth remainder W1 |
| F3, F4, F5, F23 | C |
| F24 | C (early), V5 (never) |
| F16 | D |
| F19 | D (consume), C (our retry), L2's refused view + V5 (reserve) |
| F6 | FC1 |
| F7 | FC2, W5 |
| F2, F8, F12 | W2, V5 |

P2, the reservation records, lends its facts to A, C and D and
keeps no kill of its own — the stage, not a victim. P3 is A's
whole surface; P4 is C's and D's; P1 meets the adjustments and the
races.

### Mechanisms parked

Each surfaced while stating a kill and was stopped there; none is
chosen, none is named in a kill: something that makes two writers
to one item disagree (A); the shape of an adjustment, delta or
absolute (B); a single clock source for activeness (FC3); one act
for consume's two moves (D); a marker that makes a retried exit
recognisable (C).

## Revision log

<!-- Dated entries only: what changed, why, what triggered it. -->

- (none since framing close, 2026-09-10)
