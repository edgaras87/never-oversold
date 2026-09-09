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
the lifecycle moments and the stock-on-hand ownership named in the
reading.

### Earned

**Promise:** For any item, the reserved quantity never exceeds the
stock on hand, however many reservations race for the same units.

**Reading of the sentence** — what each word commits to, and which
step confirms it:
- *for any item*: the claim holds per item, never across items;
  an item is whatever the ledger counts stock of.
- *the reserved quantity*: the sum of active reservations — not
  yet released, expired, or consumed. The lifecycle enters the
  sentence here, unstated; step 1 decides which of its moments are
  ours.
- *the stock on hand*: the ledger's own recorded count, not a
  warehouse's truth. Whether restock and corrections are ours or
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
  stock on hand. Naive code produces it: read the stock, check,
  write; two racers both pass the check on the last unit.
- Would proving it matter: yes — oversell under contention is the
  canonical correctness adversity, contention is what the reader is
  judging the author on, and the event it rules out is the one that
  costs the seller.

**Done demonstrably means:**
- the adversity genuinely created — many concurrent reservations
  against one item holding fewer units than they ask for, never a
  sequential replay pretending;
- the witness never firing — reserved greater than on hand, read
  from persisted state, checkable from outside the system;
- the path from claim to proof followable by the reader.

**Against the six bars** (the briefing's exit test for the
sentence; four settle here, two are argued here and proven later):
1. One claim, one sentence, a timeless state property: for every
   item, reserved ≤ on hand, at every instant. Settled.
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

**Debt handed to step 1:** stock on hand can move — restock,
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
in the chosen sentence, made deliberately: "stock on hand" makes the
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
reserved exceed on hand (a retried consume, a crash between its two
moves, an operator's correction) is the promise's enemy too, and
step 2 lists them all.

| Candidate | If someone else owned it, could they break the promise? | Verdict |
|---|---|---|
| The stock-on-hand count, per item | Yes — an outside owner lowering it under the reserved sum breaks it, and we could not stop them | **ours**: the ledger's own number; every change to it enters through our door |
| The reservation records — which exist, for which item, how many units | Yes — an outside owner could hold two for the last unit | **ours** |
| A reservation's active state — the transitions into it | Yes — an outside owner reviving a released reservation raises the reserved sum past on hand | **ours** |
| The admit-or-refuse decision on a reservation request | Yes — this is the moment the race is won or lost | **ours** |
| Consume — ending a reservation and lowering on hand together | Yes — an outside owner lowering on hand without ending the reservation, or lowering it twice for one reservation, breaks it | **ours** |
| The once-ness of consume under retry | Yes — a retried consume lowering on hand twice breaks it for every other reservation on the item | **ours** |
| The once-ness of reserve under retry | No — a duplicated reservation is admitted against on hand like any other; the sum still fits. Mirror: the promise stays true; the caller over-holds | refused, written — a quality, not this promise's |
| Release and expiry — transitions out of active | Cannot break it: they only lower the reserved sum | ours by ownership of the record, but not promise-bearing; the expiry *policy* (how long) refused — the promise holds for any duration |
| The item catalog — which items exist | No — someone else's fact. Mirror: the promise holds per item the ledger knows; an unknown item is refused at the door | refused, written |
| Physical stock truth — what the warehouse actually holds | No — the promise is about the ledger's recorded count (step 0's reading). Mirror: the count can be wrong about the world and the promise still holds | refused, written; consequence below |
| The answer given to the caller matching the persisted outcome | No — a wrong answer lies to the caller; the state property holds. Mirror: yes | refused, written — the census may still find it as an enemy of something else |
| Orders, payment, pricing, who may reserve | No — outside the territory | refused, written |

**Possessions, stated:** the per-item on-hand count and every
change to it; the reservation records and the transitions into
active; the admit-or-refuse decision under concurrent requests;
consume, once per reservation, moving both numbers together.

**Refusals, stated:** reserve idempotency; expiry duration; the item
catalog; physical stock truth; the caller's view; everything beyond
the reservation ledger.

**Consequence of refusing physical truth:** a downward correction —
stock lost, broken, miscounted — arrives at our door as a request to
lower on hand. Lowered under the reserved sum, the promise dies. So
the correction is a collision, on hand × an operator's fact, for
step 3 to run; step 1 only fixes that the number is ours to admit or
refuse. Two shapes are visible already and parked, not chosen:
refuse the correction below reserved, or let it cancel reservations.

**Sketch-enemies used** (debt, step 2 pays): many callers racing for
one item's last units; a retrying caller; a store that loses,
duplicates, or reorders a write; an operator lowering on hand; a
crash between ending a reservation and lowering on hand; a clock
that jumps, firing expiry early or late.

**The edge:** every request crosses at the door — reserve(item,
quantity), release, consume, adjust on hand — parsed and checked
there; an unknown item, a non-positive quantity, a reservation not
ours: refused at the door, never inside.

**Step 0's reading, confirmed or moved:** *stock on hand* is ours,
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
ours for exactly the reason reserve's is not — it moves on hand. The
downward correction is the first real tension between the promise
and the world, parked as a collision rather than settled here. The
reviewer's questions and what they changed:
- Q1, on refusing reserve once-ness: refused for correctness, not
  scope? Yes — a duplicate reservation is admitted against on hand
  like any other, so it cannot push the sum past on hand; the
  promise does not need it owned. Owning it would guard a second
  claim, banked at step 0. A duplicate consume can push on hand
  under the sum, so that once-ness is ours. Added to the earned
  half: refusal is never by scope, and the promise's enemies are
  everything that can falsify it, contention being the headline.
