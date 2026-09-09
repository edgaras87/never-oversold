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

**Verdict:** open — draft awaiting the reviewer's questions.

### Earned

**Promise:** For any item, the reserved quantity never exceeds the
stock on hand, however many reservations race for the same units.

**Audience:** the portfolio reader — the party judging whether the
author can do correctness-driven backend construction. Callers that
reserve stock and operators that restock or correct it are
consumers: they generate adversity and enter the census (step 2) as
actors, never the intent as audience.

**Worth proving:**
- Could it be false: yes — the negation is an event, an oversell:
  persisted state where an item's reserved quantity exceeds its
  stock on hand. Naive code produces it: read the stock, check,
  write; two racers both pass the check on the last unit.
- Would proving it matter: yes — oversell under contention is the
  canonical correctness adversity, and contention is what the reader
  is judging the author on.

**Done demonstrably means:**
- the adversity genuinely created — many concurrent reservations
  against one item holding fewer units than they ask for, never a
  sequential replay pretending;
- the witness never firing — reserved greater than on hand, read
  from persisted state, checkable from outside the system;
- the path from claim to proof followable by the reader.

**Against the six bars:**
1. One claim, one sentence, a timeless state property: for every
   item, reserved ≤ on hand, at every instant.
2. The adversity class is in the sentence: "race for the same units"
   is contention.
3. Trivially false: the unserialized read-check-write breaks it; the
   negation is one observable event, the oversell.
4. Stageable: fire N concurrent reservations at an item with fewer
   than N units; read the witness from persisted state afterwards.
5. Matters to the audience: see worth proving.
6. Narrow: one owner, the reservation ledger; the full lifecycle —
   reserve, release, expire, consume — inside one system's walls;
   no orders, no payments.

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

Six candidates derived from the territory, each tested against the
six bars; five banked with the bar they failed. Two wording choices
in the chosen sentence, made deliberately: "stock on hand" makes the
ledger's own count the reference rather than a warehouse's truth,
keeping the claim inside one system's walls; "race for the same
units" carries the adversity class. The reviewer's questions and
what they changed: (pending).
