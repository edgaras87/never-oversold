# Framing derivation — safe-reservations (working name)

<!-- The framing's working record: one section per step, each in
     two halves — what the step earned (on top; the exports compose
     from it) and how it ran (below: candidates, probes, dead ends,
     the reviewer's questions). A section freezes at its verdict;
     after that it changes only by a logged return trip appended
     below. Kept forever, never deleted. Every verdict here is the
     reviewer's, written where it fell. -->

Opened 2026-09-09 on the briefing (`temp/briefing-run-3.md`,
untracked, the reviewer's). Its fixed points: the territory is
inventory reservation, spun off from checkout-system's territory by
lineage, not decomposition — nothing of that repo is read here; the
adversity class is contention; the audience is the portfolio reader;
the promise must clear six bars, restated under step 0. Method:
cbc-framing as pinned, every verdict the reviewer's.

## Step 0 — Choose the promise

**Verdict:** taken, 2026-09-09 — the reviewer accepts the promise
as worded, with its reading. Section frozen; four questions ran
before the call (below). Hands down: the promise. Debt to step 1:
the lifecycle moments and the on-hand-count ownership named in the
reading.

### Earned

**Promise:** For any item, the reserved quantity never exceeds the
on-hand-count, however many reservations race for the same units.

**Reading of the sentence** — what each word commits to, and which
step confirms it:
- *for any item*: the claim holds per item, never across items;
  an item is whatever the ledger counts stock of.
- *the reserved quantity*: the sum of active reservations — not
  yet released, expired, or consumed. The lifecycle enters the
  sentence here, unstated; step 1 decides which of its moments are
  ours.
- *the on-hand-count*: one term, one token — the ledger's own
  recorded count of an item, not a warehouse's truth. Whether restock and corrections are ours or
  arrive from outside is step 1's first question, and its answer
  can change this word.
- *race for the same units*: concurrent requests overlapping in
  time against one item; the adversity is per item.
- *never exceeds*: true in every state readable from outside, not
  eventually.

**Audience:** the portfolio reader — the party judging whether the
author can do correctness-driven backend construction. Callers that
reserve stock and operators that restock or correct it are
consumers: they generate adversity and enter the census (step 2) as
actors, never the intent as audience.

**Who the promise protects** (not who it is sold to): a seller
running such a ledger, to whom an oversell costs money and trust.
Real in the world model — the caller's principal, present in the
census through the callers — and the reason the claim is worth
anything. Named so the intent does not pretend to serve them and
does not lose them either.

**Worth proving:**
- Could it be false: yes — the negation is an event, an oversell:
  persisted state where an item's reserved quantity exceeds its
  on-hand-count. Naive code produces it: read the stock, check,
  write; two racers both pass the check on the last unit.
- Would proving it matter: yes — oversell under contention is the
  canonical correctness adversity, contention is what the reader is
  judging the author on, and the event it rules out is the one that
  costs the seller.

**Done demonstrably means:**
- the adversity genuinely created — many concurrent reservations
  against one item holding fewer units than they ask for, never a
  sequential replay pretending;
- the witness never firing — reserved greater than on-hand-count, read
  from persisted state, checkable from outside the system;
- the path from claim to proof followable by the reader.

**Against the six bars** (the briefing's exit test for the
sentence; four settle here, two are argued here and proven later):
1. One claim, one sentence, a timeless state property: for every
   item, reserved ≤ on-hand-count, at every instant. Settled.
2. The adversity class is in the sentence: "race for the same units"
   is contention. Settled.
3. Trivially false: the unserialized read-check-write breaks it; the
   negation is one observable event, the oversell. Settled.
4. Stageable: fire N concurrent reservations at an item with fewer
   than N units; read the witness from persisted state afterwards.
   Argued here; proven when the evidence harness creates it (PLAN
   Step 4, then each slice).
5. Matters to the audience: see worth proving. Settled.
6. Narrow: one owner, the reservation ledger; the full lifecycle —
   reserve, release, expire, consume — inside one system's walls;
   no orders, no payments. Argued here; confirmed at step 4 (L3),
   which reopens this bar if the collisions force a division.

**Banked rejections:**
- "No unit of stock is ever held by two reservations at once" —
  same truth, but it presumes stock is serialized units: a model
  choice the territory does not demand. Names structure, not state.
- "A confirmed reservation is never lost" — real, but its negation
  is a vanishing, not a collision: wrong adversity class. Expected
  back as an enemy in the census.
- "A retried reservation request creates at most one reservation" —
  idempotency; adversity class duplication. Expected back as a kill
  in step 3, not the promise.
- "Released stock returns exactly once" — a second purpose at the
  lifecycle's other end; an intent carrying it too is overloaded.
  The census decides whether it is a kill under the chosen promise.
- "Available stock reported to callers is always current" — the
  negation is a stale number (class: staleness) and the claim reads
  as a feature wish.

**Debt handed to step 1:** on-hand-count can move — restock,
corrections. Whether that adjustment is ours is the hostage test's
first question.

### How it ran

The candidates came from a sweep, not a pick: the ledger's
never-events, one per adversity class the method names — contention
(an oversell), duplication (a retry creates two), vanishing (a
reservation lost), staleness (a stale count shown as fresh) — plus
the lifecycle's far end (a release counted twice) and the sentence
that names structure instead of state (units held twice). The
briefing fixed the class as contention, which leaves the oversell
nearly alone; the six bars then confirmed it and banked the rest
with the bar each failed. The bars are the briefing's, copied, not
derived here. Two wording choices
in the chosen sentence, made deliberately: "on-hand-count" makes the
ledger's own count the reference rather than a warehouse's truth,
keeping the claim inside one system's walls; "race for the same
units" carries the adversity class. The reviewer's questions and
what they changed:
- Q1, on the audience line: is it worded for the portfolio reader,
  or for us? Answer: for us here — this record is the workshop.
  The line reaches the reader through `intent.md`, composed at
  export, and there it is worded as addressed to them ("the claim
  is sold to you; callers and operators are the weather"), same
  meaning. Changes nothing here; binds the intent's export.
- Q2, on naming the reader at all: should the intent address
  someone who needs safe reservations instead, the reader kept
  behind the scenes? Answer: no — the audience decides what done
  means, and a seller's done (runs, stays up, operable) would derive
  a surface nobody asked for; an unstated real purpose is the
  forbidden silent channel; the reader sees a staged intent for
  what it is; and the briefing fixed the audience. The grain kept:
  the seller is who the promise protects, added to the earned block
  as its own line, and worth-proving now names their stake.
- Q3, on the six-bar block: what is it, and is it too soon? It is
  the briefing's exit test run on the chosen sentence, in its place.
  Too soon for two bars: stageable and one-owner cannot be settled
  by a sentence; they are argued at step 0 and proven downstream.
  Each bar now says whether it settles here or where it is proven.
- Q4, final or polished, and how derived: final at the verdict,
  changed after only by logged return trips — no polish passes. The
  sweep behind the candidates was not written; it is now, above.
  Depth at step 0 is in the words, not in a cleverer sentence: the
  reading block states what each word commits to, so steps 1–3
  test a stated meaning rather than a guessed one.

**Return trip, 2026-09-10, from step 4:** the reviewer renamed the
reference number: "stock on hand" / "on hand" became the one term
*on-hand-count*, so it reads as a name, not three words. The promise
now reads: *For any item, the reserved quantity never exceeds the
on-hand-count, however many reservations race for the same units.*
Wording only; no reading, bar, or rejection changed. Applied through
steps 1–4.

## Step 1 — What must be ours? (L2)

**Verdict:** taken, 2026-09-09 — the reviewer accepts the
possessions and refusals as stated. Section frozen; one question
ran before the call (below). Hands down: the four possessions the
census must cover, the sketch-enemies as debt, the downward
correction as a parked collision.

### Earned

The hostage test on each candidate possession. Yes → ours. No → the
mirror: can the promise stay true even if this goes wrong elsewhere?
Yes → refused, written. Refusal is never by scope: what is refused
is what cannot make the promise false. And the promise's enemies
are not only the class its sentence names — contention is what the
evidence stages and the reader judges; anything that can make
reserved exceed on-hand-count (a retried consume, a crash between its two
moves, an operator's correction) is the promise's enemy too, and
step 2 lists them all.

| Candidate | If someone else owned it, could they break the promise? | Verdict |
|---|---|---|
| The on-hand-count, per item | Yes — an outside owner lowering it under the reserved sum breaks it, and we could not stop them | **ours**: the ledger's own number; every change to it enters through our door |
| The reservation records — which exist, for which item, how many units | Yes — an outside owner could hold two for the last unit | **ours** |
| A reservation's active state — the transitions into it | Yes — an outside owner reviving a released reservation raises the reserved sum past on-hand-count | **ours** |
| The admit-or-refuse decision on a reservation request | Yes — this is the moment the race is won or lost | **ours** |
| Consume — ending a reservation and lowering on-hand-count together | Yes — an outside owner lowering on-hand-count without ending the reservation, or lowering it twice for one reservation, breaks it | **ours** |
| The once-ness of consume under retry | Yes — a retried consume lowering on-hand-count twice breaks it for every other reservation on the item | **ours** |
| The once-ness of reserve under retry | No — a duplicated reservation is admitted against on-hand-count like any other; the sum still fits. Mirror: the promise stays true; the caller over-holds | refused, written — a quality, not this promise's |
| Release and expiry — transitions out of active | Cannot break it: they only lower the reserved sum | ours by ownership of the record, but not promise-bearing; the expiry *policy* (how long) refused — the promise holds for any duration |
| The item catalog — which items exist | No — someone else's fact. Mirror: the promise holds per item the ledger knows; an unknown item is refused at the door | refused, written |
| Physical stock truth — what the warehouse actually holds | No — the promise is about the ledger's recorded count (step 0's reading). Mirror: the count can be wrong about the world and the promise still holds | refused, written; consequence below |
| The answer given to the caller matching the persisted outcome | No — a wrong answer lies to the caller; the state property holds. Mirror: yes | refused, written — the census may still find it as an enemy of something else |
| Orders, payment, pricing, who may reserve | No — outside the territory | refused, written |

**Possessions, stated:** the per-item on-hand-count and every
change to it; the reservation records and the transitions into
active; the admit-or-refuse decision under concurrent requests;
consume, once per reservation, moving both numbers together.

**Refusals, stated:** reserve idempotency; expiry duration; the item
catalog; physical stock truth; the caller's view; everything beyond
the reservation ledger.

**Consequence of refusing physical truth:** a downward correction —
stock lost, broken, miscounted — arrives at our door as a request to
lower on-hand-count. Lowered under the reserved sum, the promise dies. So
the correction is a collision, on-hand-count × an operator's fact, for
step 3 to run; step 1 only fixes that the number is ours to admit or
refuse. Two shapes are visible already and parked, not chosen:
refuse the correction below reserved, or let it cancel reservations.

**Sketch-enemies used** (debt, step 2 pays): many callers racing for
one item's last units; a retrying caller; a store that loses,
duplicates, or reorders a write; an operator lowering on-hand-count; a
crash between ending a reservation and lowering on-hand-count; a clock
that jumps, firing expiry early or late.

**The edge:** every request crosses at the door — reserve(item,
quantity), release, consume, adjust on-hand-count — parsed and checked
there; an unknown item, a non-positive quantity, a reservation not
ours: refused at the door, never inside.

**Step 0's reading, confirmed or moved:** *on-hand-count* is ours,
the reading holds; *reserved quantity* = active reservations, and
active is ours to enter, the exits promise-neutral.

### How it ran

Candidates gathered from the sentence's words (the two numbers, the
race) and the lifecycle (reserve, release, expire, consume, adjust),
then the neighbours the territory touches (catalog, warehouse,
orders, callers). Two outcomes worth the reviewer's eye: reserve
idempotency is refused by the mirror, though step 0 expected it back
as a kill — under this promise a duplicate reservation over-holds
but never oversells, so it is a quality of the surface, not a
guarantee this promise carries; and the once-ness of *consume* is
ours for exactly the reason reserve's is not — it moves on-hand-count. The
downward correction is the first real tension between the promise
and the world, parked as a collision rather than settled here. The
reviewer's questions and what they changed:
- Q1, on refusing reserve once-ness: refused for correctness, not
  scope? Yes — a duplicate reservation is admitted against on-hand-count
  like any other, so it cannot push the sum past on-hand-count; the
  promise does not need it owned. Owning it would guard a second
  claim, banked at step 0. A duplicate consume can push on-hand-count
  under the sum, so that once-ness is ours. Added to the earned
  half: refusal is never by scope, and the promise's enemies are
  everything that can falsify it, contention being the headline.

**Return trip, 2026-09-10, from step 4:** the term *on-hand-count*
applied throughout (see step 0's return trip). Wording only.

## Step 2 — Name the enemies (L1)

**Verdict:** taken, 2026-09-10 — the reviewer accepts the census:
saturated by the audit, five scope verdicts as recommended, the
three ledgers as written. Section frozen; six questions ran before
the call (below). Hands down: the fact list F1–F24 and the three
ledgers. No L2 revision.

### Earned

**Actors,** found by following one reservation through its life:
reserve request → network → our process → store → network → reply;
later a consume or release by the same or another caller; beside it
an operator adjusting on-hand-count; underneath, the clock that ends
reservations by expiry. Everything beyond our control is an actor,
each named by what it does to us:
- *Callers* — the services that reserve, consume and release on
  behalf of a seller's customers; they move the reserved side.
- *Operators* — whoever changes on-hand-count from the world's
  side: a restock, a loss, a recount; a person at a screen or a
  stock system, the ledger cannot tell and does not need to — it
  sees an adjust request at the door. They move the on-hand-count side.
- *The network* — carries requests and replies, and loses,
  duplicates, delays, reorders them.
- *Our own process* — the ledger's running instances, which die
  mid-work and can be more than one.
- *The store* — where the ledger's numbers persist; it answers
  reads and takes writes, and can be slow, stale, or silent.
- *The clock* — what ends reservations by expiry; it skews between
  instances and jumps.

**Facts,** each stated consequence-first — what it leaves us facing:

Callers
- F1. Many callers send reserve for one item at the same instant,
  together asking more than on-hand-count → we face the race for the last
  units: each request fits alone, together they do not.
- F2. A caller resends reserve after a lost reply → we face the same
  request twice; a second reservation holds units others need.
- F3. A caller resends consume after a lost reply → we face lowering
  on-hand-count twice for one reservation.
- F4. A caller sends consume and release for one reservation at the
  same instant, or two consumes → we face two exits racing on one
  reservation, each moving numbers.
- F5. A caller consumes or releases a reservation that already ended
  — expired, consumed, released — including very late → we face a
  request against a reservation with no active state left.
- F6. A caller asks for zero, a negative or absurd quantity, or an
  unknown item → we face nonsense at the door.
- F7. A caller consumes a reservation that is not theirs, or claims
  a quantity other than the reservation's → we face a lie at the
  door.
- F8. A caller never returns → we face a reservation held forever
  unless something ends it; holds accumulate.

Operators
- F9. An operator lowers on-hand-count under the reserved sum — loss,
  breakage, a recount → we face the promise's negation arriving as
  a legitimate fact from the world.
- F10. An operator's adjustment and callers' reservations hit one
  item at the same instant → we face the race with a different
  partner: adjust × reserve.
- F11. An operator's downward adjustment is resent → we face on-hand-count
  lowered twice for one loss. (An upward one resent overstates on
  hand: the promise as worded is untouched; the world is lied to —
  W1.)
- F13. Two adjustments to one item arrive in the other order than
  they were made → we face not knowing which value is current.

Network
- F12. A reply is lost after we admitted a reservation → the caller
  does not know; they retry (F2) or abandon (F8).
- (Reorder and lateness of caller requests: F5, F13.)

Our own process
- F15. We die between admitting a reservation and recording it, or
  between recording and replying → we face half-done work at every
  boundary; the caller retries against a state they cannot see.
- F16. We die between consume's two moves — ending the reservation,
  lowering on-hand-count → we face one move done: on-hand-count lowered while
  the reservation still counts (reserved can exceed on-hand-count), or
  the reservation ended while on-hand-count still counts consumed units.
- F17. Two of our processes run at once — a deploy overlap, a
  scale-out → we face the race between our own instances; any
  belief an instance holds in memory about on-hand-count is a lie.
- F18. Our instances' clocks disagree → we face a reservation that
  is expired for one instance and active for another.

Store
- F19. A write's outcome is unknowable — a timeout after sending →
  we face not knowing whether the reservation exists.
- F21. The store answers two concurrent readers with the same count
  → we face two checks that both pass on units that fit only once.
  (This is F1 as the store shows it to us.)
- F22. A read returns a count that is no longer current — a lagging
  replica, a cache → we face a check passing on a number already
  gone.

Clock
- F23. Expiry ends a reservation while a consume for it is in flight
  → we face two exits racing, time being one partner (F4's shape).
- F24. Time jumps — a paused machine, a corrected clock → we face
  expiry firing for everything at once, or never.

**Trust assumptions — accepted deliberately, not defended:**
- T1. The store durably holds a write it acknowledged. (Its
  complement, loss below acknowledgment, is fenced: W4.)
- T2. The store offers at least one way to make two concurrent
  writers disagree — something to serialize or refuse on.
- T3. A request's stated caller and operator identity is what it
  claims to be. (Authentication is outside: W5.)

**Probe log** — how saturation was earned. Stamps: *new* (the
census grew) · *nothing new* (which line covers it) · *out* (fenced,
in ink).
- PL0, the enumeration lens (actors × vanishes/duplicates/lies):
  F1–F7, F9–F12, F15, F16, F19, F21, F23.
- PL1, the assumption hunt. Silent singulars: "our process" → could
  be two: **new** F17. "The clock" → could disagree: **new** F18.
  "The adjustment" → could be two, reordered: **new** F13, F11.
  Silent successes: "the check reads the current count" → the other
  branch is a stale read: **new** F22. "Expiry fires" → or never, or
  all at once: **new** F24. "Consume's two moves both happen" →
  nothing new, F16 covers. Result: five new.
- PL2, the timeline stretched. What accumulates: reservations nobody
  returns for → **new** F8; records of ended reservations → **out**,
  W6. What is forgotten: an expired reservation, then its consume
  arrives → nothing new, F5. What arrives very late: a consume a
  day later → nothing new, F5; a reply that arrives after the
  caller retried → nothing new, F2/F12. Result: one new, one out.
- PL3, the resource lens, every quantity at zero / many / huge.
  Requested quantity: zero → F6; huge → F6 (the door bounds it).
  On hand: zero → every reservation refused, nothing new, F1's
  check; huge → nothing new. Callers on one item: many → F1.
  Active reservations on one item: huge → the reserved sum must be
  known at every admit — a cost, not an enemy: **out**, W3. Items:
  many → the race is per item, nothing new. Adjustments: many at
  once → F10, F13. Instances: many → F17. Result: zero new.
- PL4, the assumption hunt again, over the facts PL1–PL2 added. F17
  "two instances" → could be one that restarts mid-work: F15
  covers. F18 → could a single instance's clock be wrong: F24
  covers. F22 → could a write go to a lagging place: T1 with W4
  covers. F8 → could a hold end without anyone asking: that is
  expiry, F23/F24. Result: zero new.

Two consecutive lenses (PL3, PL4) with zero new facts → **saturated**,
the reviewer's call, 2026-09-10, after the audit's four checks.

**Fence list** — exclusions in ink; each earns its place at step 3
by stopping a fact from colliding:
- W1. Physical stock truth. The world's count is not ours (L2
  refusal); it reaches us only as an operator's adjustment at the
  door.
- W2. Reserve once-ness. The ledger does not check whether a
  reserve request repeats an earlier one: a retry after a lost
  reply makes a second hold, known to no one, orphaned until expiry
  ends it (F8). It over-holds; it cannot oversell (L2 refusal). The
  cost accepted: the seller sells less than they could for the
  hold's duration. The claim that would remove it, one request →
  one reservation, is banked at step 0 as another promise's.
- W3. Throughput, latency, fairness between racers. Who wins a race
  is not ours; that the losers lose correctly is.
- W4. Loss below the store's acknowledgment — restores from an older
  backup, corrupted volumes. T1's complement; the banked "never
  lost" promise's territory.
- W5. Who may reserve, consume, adjust. T3's complement.
- W6. Retention and growth of ended reservations' records.

**Not-probed ledger** — the census's edge, named:
- Deployment transitions beyond overlap: schema changes, data
  migrations.
- Replication topologies beyond "a read can be stale" (F22).
- A malicious operator or caller, as opposed to a mistaken or
  retrying one; T3 stands in front of it.
- The clock beyond skew and jumps (leap seconds, monotonic vs wall).

**Scope verdicts** — boundary questions the census raised, each
with a recommendation; all five taken as recommended by the
reviewer, 2026-09-10:
- V1. The downward correction under the reserved sum (F9). Recommend
  **in**: it is the promise's own negation arriving legitimately,
  the same kind — on-hand-count × reserved — and the reader needs to see
  the promise survive the world contradicting the ledger.
- V2. Expiry (F8, F18, F23, F24). Recommend **in**: a timed exit
  racing a consume is contention with time as the partner, the same
  class; the duration policy stays refused (L2).
- V3. Our own instances racing (F17). Recommend **in**: the same
  class, and the evidence harness will run it.
- V4. Store loss below acknowledgment (behind F19/F22). Recommend
  **out**, W4: a new kind of difficulty, recovery, another
  promise's territory.
- V5. Over-holding and starvation by duplicates or abandoned
  reservations (F2, F8). Recommend **censused-and-fenced**: the
  facts stay because expiry is their exit; the boundary (W2)
  holds.

**Return trip check:** did the census reveal a missed possession or
an unwritten refusal? F9 confirms the on-hand number is ours to
admit or refuse; F16 confirms consume's two moves are one
possession; F17 adds no possession, only a partner. No L2 revision.

### How it ran

The enumeration lens alone found the caller-shaped facts and the
store's unknowable write; the assumption hunt found everything with
a silent "the": our process, the clock, the adjustment, the current
count. The timeline lens found the abandoned hold and one exclusion.
The resource lens found nothing, which is the point — every cell
named its covering line. The reviewer asked who the operators
are (Q1): the word had entered at step 0 undefined; each actor now
carries a one-line definition. Numbering skips F14 and F20 — F14 (very
late requests) folded into F5 and F20 (store loss) into W4 during
the sort; the gaps are kept so the log's references stay true. The
reviewer's other questions and what they changed:
- Q2, T2 explained (the store must offer one way to make two
  writers disagree; a floor, not an enemy): nothing changed.
- Q3, why ended records are "out": they cannot move the reserved
  sum; written as W6 so the growth is seen, not dropped. Nothing
  changed.
- Q4, what saturation is and what the call is: two consecutive
  empty lenses, audited by four checks. Called saturated.
- Q5, on W2: is the ledger really not checking for a repeated
  request? Yes; the fence now states the consequence and the cost
  accepted, not only the reasoning.
- Q6, what if the reviewer decided to cover request identity after
  all: a return trip to step 0 with a logged intent revision; the
  sentence would carry a second claim under a second class, which
  the briefing's bars refuse and the method calls overloaded;
  overriding that re-runs every step downstream. Under this
  promise it has no home — not a slice (the enemy cannot break the
  promise), not a fold (it is adversity). The route is the next
  project, or a logged revision after release. Nothing changed.

**Return trip, 2026-09-10, from step 3:** the probe tags P0–P4
renamed PL0–PL4 throughout this section so that P is free for the
possessions step 3 collides. Labels only; no fact, stamp, or
verdict changed.

**Return trip, 2026-09-10, from step 4:** the term *on-hand-count*
applied throughout (see step 0's return trip). Wording only.

## Step 3 — Run the collisions (L4)

**Verdict:** taken, 2026-09-10 — the reviewer accepts the kills,
the four concerns, the three fold-candidates, and the coverage.
Section frozen; one question ran before the call (below), and one
return trip to step 2 (labels only). Hands down: concerns A–D
invariant-shaped with adversity named, FC1–FC3, the parked
mechanisms.

### Earned

The four possessions from step 1, collided one at a time against
every fact that can reach them. Each kill says what dies; every
mechanism that surfaced is parked at the end, unchosen.

- P1 — the per-item on-hand-count and every change to it
- P2 — the reservation records and the transitions into active
- P3 — the admit-or-refuse decision under concurrent requests
- P4 — consume, once per reservation, moving both numbers together

**Kills** (fact × possession → what dies):

1. F1 × P3 → both racing admits succeed on units that fit only
   once: **reserved exceeds on-hand-count.**
2. F17 × P3 → the same death, the racers being our own instances,
   each admitting on a count it holds in memory.
3. F21 × P3 → the same death as the store shows it: two checks pass
   on one count.
4. F22 × P3 → an admit passes on a count already gone: **reserved
   exceeds on-hand-count** on a number that was never the truth.
5. F10 × P3, P1 → an admit passes against on-hand-count while a
   concurrent adjustment is lowering it: **reserved exceeds on
   hand,** the partner being an operator, not another caller.
6. F9 × P1 → a legitimate downward correction sets on-hand-count under
   the reserved sum: **the promise dies by one honest request,**
   no race needed.
7. F11 × P1 → a resent downward correction lowers on-hand-count twice for
   one loss: kill 6's death in a second costume; the part that fits
   under the sum leaves on-hand-count wrong about the world — stops at
   W1.
8. F13 × P1 → reordered adjustments leave on-hand-count at the older
   value: kill 6's death if the older is lower; otherwise wrong
   about the world — stops at W1.
9. F15 × P2, P3 → an admit decided but not yet recorded is a
   decision no other admit can see: **two decisions on one unit,**
   kill 1's death from our own half-done work. The other half —
   recorded but never replied — is an orphaned hold: stops at W2
   and V5, expiry its exit.
10. F18 × P2, P3 → two instances judge one reservation's activeness
    differently; the one that sees the smaller reserved sum admits:
    **reserved exceeds on-hand-count by the store's own record.**
11. F5 × P2, P4 → a consume lands on a reservation that already
    ended, its units since reserved by someone else: **on-hand-count is
    lowered for units nobody holds; reserved exceeds on-hand-count for
    the others.**
12. F3 × P4 → a retried consume lowers on-hand-count twice for one
    reservation: the same death.
13. F4 × P4 → a consume and a release, or two consumes, race on one
    reservation: **both exits act — units returned and removed, or
    removed twice.**
14. F23 × P4 → expiry and consume race on one reservation: kill 13
    with time as one partner.
15. F16 × P4 → we die between consume's two moves: **a readable
    state with on-hand-count lowered while the reservation still counts,
    or the reservation ended while consumed units still count.**
16. F19 × P4 → consume's outcome unknowable: our own retry is kill
    12; our silence is kill 15's half-state. F19 × P2 → a
    reservation whose existence we cannot confirm: nothing
    promise-bearing dies — the caller's view is refused (L2), the
    possible orphan stops at V5.
17. F7 × P4 → a consume names a quantity its reservation does not
    hold, or a reservation not its own: **on-hand-count lowered by units
    the caller never held; reserved exceeds on-hand-count for the
    others.** A lie at the door; identity stops at W5/T3.
18. F6 × P3 → a nonsense request — zero, absurd, unknown item —
    reaches the decision: **the numbers move by an amount that
    means nothing.** At the door.
19. F24 × P2 → time jumps: expiry fires for everything at once —
    holds end early, late consumes follow (kill 11); or never —
    holds last forever, stops at V5.
20. F2, F12 × P2 → a retried or abandoned reserve makes a second or
    orphaned hold: over-holds, cannot oversell — stops at W2, V5.

**Dedupe by attack surface** — what a fact does to us, never whose
fault — into concerns whose proof obligations differ:

- **Concern A — over-admission under concurrent writers.** Kills
  1, 2, 3, 4, 5, 9, 10. One death: more units admitted than fit,
  because the decision read a number that was not the truth at the
  moment of writing. Invariant-shaped: *for every item, in every
  readable state, the sum of active reservations ≤ on-hand-count.*
  Adversity, named: concurrent admits on one item's last units —
  from many callers, from our own instances, against a stale
  read, against a concurrent downward adjustment, under clocks
  that disagree about activeness. Proof obligation: create the
  contention, read the witness from state.
- **Concern B — the downward correction.** Kills 6, 7, 8. The
  promise's negation arriving as a legitimate request; sequential
  suffices to stage it. Invariant-shaped: *no admitted change to on
  hand leaves it under the sum of active reservations.* Adversity:
  an operator lowering on-hand-count under the reserved sum, once,
  twice, out of order. Proof obligation differs from A's: no race
  to create; the question is what the ledger does with an honest
  request it cannot honour as asked. Two shapes seen and parked,
  not chosen: refuse it; or let it end reservations.
- **Concern C — a reservation exits once.** Kills 11, 12, 13, 14,
  16 (retry), 19 (early). Invariant-shaped: *a reservation moves
  its item's numbers at most once on exit, and never after it has
  ended.* Adversity: duplicated exits, racing exits (consume ×
  release, consume × consume, consume × expiry), late exits on
  ended reservations, expiry fired early. Proof obligation:
  duplicates and races collapse to one exit.
- **Concern D — consume's two moves hold together.** Kills 15, 16
  (silence). Invariant-shaped: *no readable state holds one of
  consume's two moves without the other.* Adversity: our death, or
  an unknowable outcome, between ending the reservation and
  lowering on-hand-count. Proof obligation differs from C's: not
  "duplicates collapse" but "the half-done converges" — the
  worked example's partial-failure distinction.

**Fold-candidates** — contract-shaped, checked at the door, no
adversity to stage beyond sending the request:
- FC1. What a valid request is (kill 18): a known item, a positive
  bounded quantity, a reservation that exists. Enemy-less: nonsense
  is not an attack, it is a malformed request.
- FC2. A consume or release names its own reservation and moves
  exactly what that reservation holds (kill 17). Enemy-touched — a
  lying caller — but its proof is a check, not a staged adversity;
  identity itself is W5/T3. Step 6 stamps it.
- FC3. What *active* means — a wording hole in L2's own
  possessions: ended by an exit, or past its expiry as judged by
  one clock, not one per instance. No enemy of its own; the clock's
  enemies act through concerns A and C, which consume this
  definition.

**Fences, and what each stopped at this step:** W1 stopped the
world-truth part of kills 7 and 8. W2 stopped kill 20 and half of
kill 9. W3 stopped the fairness question inside kill 1 (who wins is
not ours). W5 stopped the identity part of kill 17. V5 (expiry as
the orphan's exit) stopped the other half of kill 9, kill 16's
orphan, kill 19's never, kill 20. W4 and W6 stopped nothing here —
no censused fact reaches them; kept because the probes raised them
in ink, and noted as fences that earned no ink at step 3.

**Coverage — the full pass, every fact placed:**

| Fact | Lands in |
|---|---|
| F1, F10, F17, F21, F22 | A |
| F15 | A (decision half), W2/V5 (orphan half) |
| F18 | A, consuming FC3 |
| F9, F11, F13 | B; their world-truth remainder W1 |
| F3, F4, F5, F23 | C |
| F24 | C (early), V5 (never) |
| F16 | D |
| F19 | D (consume), C (our retry), L2's refused view + V5 (reserve) |
| F6 | FC1 |
| F7 | FC2, W5 |
| F2, F8, F12 | W2, V5 |

A second pass, possession by possession against the full list,
yielded nothing new: P1 meets only the adjustment facts and the
races (A, B); P2 lends its facts to A, C and D and keeps no kill of
its own — the stage, not a victim; P3 is A's whole surface; P4 is
C's and D's.

**Mechanisms parked** — each surfaced while stating a kill and was
stopped there; none is chosen, none is named in a kill: something
that makes two writers to one item disagree (A); the shape of an
adjustment, delta or absolute (B); a single clock source for
activeness (FC3); one act for consume's two moves (D); a marker
that makes a retried exit recognisable (C).

**Step 0's bar 6, checked here:** every kill lands on one ledger's
numbers; no kill needed a second owner to state. Step 4 confirms.

### How it ran

Possession by possession, then the coverage table as the exit
check. Three findings shaped the concerns: the downward correction
is not a race — sequential stages it — so it is not concern A's
adversity though it shares the witness; exit-once and
consume-atomic looked like one concern until the proof obligations
were compared (collapse vs converge), the worked example's own
split; and "active" turned out undefined in L2, a wording hole
that the clock facts had been colliding with unnamed — FC3 names
it, and if step 4 or 6 finds it needs an owner's decision it goes
back to L2 as a logged return trip. The reviewer's questions and
what they changed:
- Q1, the possession tags P1–P4: are they the probe log's P0–P4?
  No — an accidental shared letter. The reviewer chose to keep P
  for possessions; the probes became PL0–PL4 by a logged return
  trip to step 2.

**Return trip, 2026-09-10, from step 4:** the term *on-hand-count*
applied throughout (see step 0's return trip). Wording only.

## Step 4 — Sort into owners (L3)

**Verdict:** taken, 2026-09-10 — the reviewer accepts one area,
the reservation ledger, with the five seams refused in writing.
Section frozen; one question ran before the call (below), and one
return trip to steps 0–3 (the term on-hand-count). Hands down: the
one owner.

### Earned

**What each concern touches:**

| Concern | State touched | Decision touched |
|---|---|---|
| A — over-admission | on-hand-count; the active reservations of the item | admit or refuse a reservation, against both |
| B — the downward correction | on-hand-count; the active reservations of the item | admit or refuse a change to on-hand-count, against the sum |
| C — a reservation exits once | one reservation's active state; on-hand-count (on consume) | whether this exit is the reservation's first and only |
| D — consume's two moves | one reservation's active state; on-hand-count | none of its own — it holds A's and C's outcomes together |

Every concern reads the same two numbers of the same item and
decides against their comparison. No concern touches state the
others do not; no concern decides something the others must not.

**Seams probed** — each a plausible division, tested against the
bar: separate state *and* separate decision authority.

- *Stock adjustments as their own area* (the operator side). Its
  state would be on-hand-count; but concern A's admit decision reads on
  hand and concern B's admit decision reads the reserved sum. One
  number cannot be owned by an area whose decisions are taken by
  another. Fails on state. Named, not drawn.
- *Expiry, or the clock, as its own area.* Its decision would be
  when a hold ends; its state, the expiry moment on each
  reservation — which is the reservation record itself, and ending
  a hold is concern C's exit. No separate state. Named, not drawn.
- *The door — request validation — as its own area.* FC1 and FC2
  are checks with no state of their own; a stage, not an owner.
  Fails on state. Named, not drawn.
- *Consume, or fulfilment, as its own area.* It moves both numbers
  in one act; separate decision authority over a number is exactly
  what concern D forbids. Fails on authority. Named, not drawn.
- *One area per item.* The same decision over the same kind of
  state, repeated; a partition of scale, not of ownership — a
  mechanism, parked with the others. Not a seam.

**Ownership: one area — the reservation ledger.** It owns the
per-item on-hand-count, the reservations with their active state,
and the three decisions: admit a reservation, admit a change to on
hand, allow an exit. Confirmed by sorting four real concerns, not
assumed. Step 0's bar 6 — one owner, a full lifecycle inside one
system's walls — is settled here, no longer argued.

### How it ran

The table came first, and it was already the answer: four rows,
two columns, the same entries. The seams were then probed one by
one so the one-area result is a refusal of each, in writing, rather
than an absence. The strongest candidate was the operator side —
it has its own actor, its own facts, its own concern — and it
fails on the plainest ground: the two numbers are compared in one
decision, so they must have one owner. FC3 (what active means) was
watched for an owner's decision it might need: it needs a
definition, not an owner, so it stays a fold; no return trip to
L2. The reviewer's questions and what they changed:
- Q1, the reference number's name: "on hand" read as loose words.
  "available" refused — it is the other number, on-hand-count minus
  reserved; "in-stock" blurs into it. The reviewer chose the one
  token *on-hand-count*; applied by return trips to steps 0–3.

## Step 5 — Check between owners (L5)

**Verdict:** taken, 2026-09-10 — the reviewer accepts L5 empty
with its reasons. Section frozen; no questions ran. Hands down:
nothing to hold across seams; step 6 cuts within one area.

### Earned

**Empty, with its reasons.** L5 holds what must be true across
seams that no local guarantee covers. There are no seams, and each
absence traces to a decision already taken:

- *No internal seams:* step 4 sorted four real concerns into one
  area, the reservation ledger, and refused five candidate
  divisions in writing. Nothing crosses a boundary inside the
  system because there is no boundary inside it.
- *No sibling systems to coordinate with:* the catalog, physical
  stock truth, orders, payment, and who-may-act were refused at
  step 1 (L2) and fenced at step 2 (W1, W5). The ledger owes them
  nothing across a joint, and they owe it nothing the promise
  depends on.
- *Callers and operators are environment, not owners:* they enter
  the census as actors (L1) — what they do reaches the ledger as
  facts at the door, never as an agreement between peers.
- *Our own instances are not areas:* two of us running is a fact
  (F17) inside one area, handled by concern A; instances share
  state, they do not divide it.
- *Per-item partition is not a seam:* refused at step 4 as a
  mechanism of scale; if it is ever chosen, it divides the same
  decision over disjoint state and creates no cross-item guarantee
  the promise needs, the promise being per item.

**What L5 would have held, had a seam been drawn** — recorded so
the emptiness is a check, not a shrug: had the operator side been
its own area, L5 would have had to state the ordering between an
adjustment and a concurrent admit across the seam (kill 5) and
the coordinated failure of a correction that ends reservations
(concern B's parked shape). Both live inside one area now, as
concerns A and B, with no joint to hold them together.

### How it ran

Each emptiness was written by asking which prior decision made it
empty, and the two facts that could have become cross-area
constraints (kill 5, concern B's second shape) were named as the
constraints they would have been. Nothing here redefines L3's one
area or L4's concerns; L5 takes them as given. The reviewer's
questions and what they changed: (pending).

## Step 6 — Cut the slice surface

**Verdict:** open — draft awaiting the reviewer's questions.

### Earned

**Pass 1 — the sort.** Every concern and fold-candidate stamped
theorem (an invariant under adversity; proof creates the adversity
and witnesses survival) or definition (contract-shaped; declared
and checked; nothing stages against it), with its because.

| Concern | Stamp | Because |
|---|---|---|
| A — over-admission under concurrent writers | **theorem** | the adversity is created — many admits racing for one item's last units — and the witness, reserved ≤ on-hand-count, is read from state after |
| B — the downward correction | **theorem** | near-miss answered: the adversity is sequential, but it is adversity — naive code accepts the honest request and the promise dies; the evidence creates it (a correction under the sum, resent, out of order) and reads the same witness |
| C — a reservation exits once | **theorem** | duplicated, racing and late exits are injected; the witness is one move per exit and none after ending |
| D — consume's two moves hold together | **theorem** | the adversity is death between the moves, created by killing mid-work; the witness is no half-state readable |
| FC1 — what a valid request is | **definition** | a malformed request is not an attack; declared at the door, checked there |
| FC2 — an exit names its own reservation and moves what it holds | **definition** | enemy-touched (a lying caller) but its proof is a check, not a staged adversity; the lie is sent, not created under load |
| FC3 — what *active* means | **definition** | a wording; no enemy of its own — the clock's enemies act through A and C, which consume it |

Four theorems, three definitions. Step 3's discipline held: the
kills arrived theorem-shaped and this pass confirmed rather than
sorted.

**Pass 2 — the dedupe.** Pairwise on the theorems, one test: must
their evidence create *different adversity*?

- A × B: the same witness, different adversity — contention among
  admits versus an honest request that cannot be honoured as
  asked. A's evidence hammers; B's sends one correction (and its
  resends and reorders). **Two slices.** Kill 5 — an adjustment
  racing a reserve — stays in A: it is a race, and B's evidence
  would not create it.
- A × C: both contention-class, different adversity — racing
  admits on one item's last units versus racing and duplicated
  exits on one reservation. **Two slices.**
- C × D: the worked example's own split — duplicates collapse
  versus the half-done converges; injected duplicates and races
  versus kill-mid-work. **Two slices.**
- A × D, B × C, B × D: different adversity on every pair. **Two
  slices each.**

No merge. Four theorems → four slices.

**Pass 3 — the folds.** Each definition into the first slice that
consumes it, its check riding along:

- FC1 (valid request) → **SL-1**: the admit is the first door a
  request reaches.
- FC3 (what active means) → **SL-1**: the admit reads the sum of
  active reservations; SL-3 consumes it second.
- FC2 (an exit moves what it holds) → **SL-3**: the exit path is
  its only consumer.

**The registry shape** (composed at export on the skill's
template):

- **SL-1 — no over-admission under contention** · `chosen-next`
  - Invariant: for every item, in every readable state, the sum of
    active reservations ≤ on-hand-count.
  - Adversity to create: concurrent admits on one item's last
    units — many callers (F1); more than one of our instances
    (F17); against a stale read (F22); against a downward
    adjustment racing them (F10); an admit decided but not yet
    recorded (F15).
  - Area: the reservation ledger. Kills covered: 1, 2, 3, 4, 5, 9,
    10.
  - Folds in: FC1, FC3.
  - Flag riding: kill 10 — clocks disagreeing about activeness —
    cannot be staged by hammering; its evidence shape is a
    controlled clock, or FC3 removes it by judging activeness from
    one clock, in which case the evidence shows that judgment is
    the one used. Named here so the slice inherits the warning.
  - Presumes: nothing — this is the ground.
  - Judgment logged: kill 5 here, not in SL-2, because it is a
    race; kill 9 here because a decision invisible to other
    decisions is A's death from inside.
- **SL-2 — the correction never undercuts the holds** · `open`
  - Invariant: no admitted change to on-hand-count leaves it under
    the sum of active reservations.
  - Adversity to create: an operator's downward correction under
    the reserved sum (F9), resent (F11), out of order (F13).
  - Area: the reservation ledger. Kills covered: 6, 7, 8.
  - Presumes: SL-1 — the reserved sum it is checked against is
    the one SL-1 keeps true.
  - Judgment logged: two shapes were parked at step 1 and step 3 —
    refuse the correction, or let it end reservations. The slice's
    specify step decides; the invariant and the witness are the
    same under either.
- **SL-3 — a reservation exits once** · `open`
  - Invariant: a reservation moves its item's numbers at most once
    on exit, and never after it has ended.
  - Adversity to create: a retried consume (F3); consume racing
    release, two consumes (F4); consume racing expiry (F23); a
    late consume on an ended reservation (F5); expiry fired early
    (F24).
  - Area: the reservation ledger. Kills covered: 11, 12, 13, 14,
    16 (our own retry), 19 (early).
  - Folds in: FC2.
  - Presumes: SL-1 — the reservations being exited were admitted
    truly.
- **SL-4 — consume's two moves hold together** · `open`
  - Invariant: no readable state holds one of consume's two moves
    without the other.
  - Adversity to create: our death between ending the reservation
    and lowering on-hand-count (F16); an unknowable outcome of the
    consume write (F19).
  - Area: the reservation ledger. Kills covered: 15, 16 (silence).
  - Flag riding: the evidence is kill-mid-work and unknown-outcome
    injection, not hammering — the unusual shape named so the
    consumer inherits it.
  - Presumes: SL-3 — an exit's identity, so the half-done can be
    told from the done.

**Fold-reconciliation line:** 20 kills (steps 3's 1–20) ↔ 4 slices
+ 3 folds + 0 deferrals + the fence remainders:
- 1, 2, 3, 4, 5, 9, 10 → SL-1 · 6, 7, 8 → SL-2 · 11, 12, 13, 14 →
  SL-3 · 15 → SL-4 · 16 → SL-3 (retry) and SL-4 (silence) · 17 →
  FC2 in SL-3 · 18 → FC1 in SL-1 · 19 → SL-3 (early) · 20 → W2/V5.
- Folds: FC1 → SL-1 · FC3 → SL-1 · FC2 → SL-3.
- Fence remainders, already stopped at step 3: the world-truth
  parts of 7, 8 (W1); the orphan halves of 9, 16 and the never of
  19 (W2, V5); the identity part of 17 (W5).
- The written zero: no deferrals; no kill dropped; no theorem
  merged.

**Ordering expectation** (re-decided at each close): SL-1 → SL-2 →
SL-3 → SL-4. SL-1 is the promise's headline and the harness's first
adversity (PLAN Step 4 proves the harness on it); SL-2 stands on
SL-1 alone and is the promise's other face, the on-hand-count
side; SL-3 needs truly admitted reservations to exit; SL-4 needs
an exit's identity to tell half-done from done.

**Divergences from the briefing:** none. The working name
safe-reservations is untouched — PLAN's Step 2 decides it, and
that is not the derivation overriding the briefing.

### How it ran

The sort was a confirmation sweep, as step 3's discipline intended;
B was the one near-miss, and the because is written so the doubt
does not linger. The dedupe's only real question was A × B — same
witness, and a reader could ask why not one slice; the answer is
the evidence: hammering never sends an honest correction, and a
correction never creates a race. Kill 5 was the tie-breaker's
proof: the racing adjustment belongs with the races. Kill 16 lands
in two slices by its two halves (our retry, our silence), stated
in both. The reviewer's questions and what they changed: (pending).
