# 0011. An item becomes known by its first adjustment

Date: 2026-09-12
Status: Accepted

## Context

The definition refuses the item catalog: which items exist is
someone else's fact, "the promise holds per item the ledger knows,
and an unknown item is refused at the door" (L2). It also says the
on-hand-count and every change to it are ours (P1), reaching the
ledger only as requests at the door — a restock, a loss, a
recount. Nothing in the framing says how an item comes to be known
in the first place; reserve needs an item with an on-hand-count
before any race can be staged, and the evidence needs a real path
to give it one. This is a *what* the first slice cannot build
without, found at its opening, decided here, not absorbed.

## Options considered

1. **A registration door** — `PUT /items/{item}` with an initial
   count. A catalog-shaped surface the definition refused, and an
   adjustment in disguise: it sets the count. Rejected.
2. **Items seeded by SQL** as evidence setup only. The running
   system would then have no way to gain an item; the README's
   stranger could never reserve; and the evidence would enter
   state through a side door the plan must hunt as an escape
   hatch. Rejected.
3. **Reserve creates the item at zero.** Every reserve on an
   unknown item is then refused as "does not fit" instead of
   "unknown" — nonsense (FC1) and refusal collapse into one
   answer, the distinction ADR-0010 keeps. Rejected.
4. **The first adjustment creates the item.** An operator states
   the item's on-hand-count; if the ledger does not know the item,
   it now does, at that count. The catalog stays refused — the
   ledger learns of an item only as the operator's number arrives,
   which is exactly P1's "every change enters through the door".
   Chosen.

## Decision

- **An item is known to the ledger when it has an on-hand-count,
  and it gains one by an operator's adjustment.** The first
  adjustment for an unknown item creates it at the stated count.
  Reserve, consume and release on an unknown item are refused at
  the door as unknown (ADR-0010: `404`).
- **An adjustment states the on-hand-count** — the operator asserts
  the ledger's recorded count, a restock or a recount arriving as
  the number the world now shows, `{"onHandCount": n}`, never a
  difference to add. Why: P1 calls it "the ledger's own recorded
  count", the intent's term is one token, and the census's
  reordering fact (F13, "not knowing which value is current") is
  written as values, not deltas. The resend fact (F11) then
  arrives as the same number twice, and SL-2's evidence creates it
  as such.
- **SL-1 births this door in the shapes its guarantees need:**
  creation and an upward change (so an item exists to race for),
  and a downward change racing admits (kill 5). What the ledger
  does with a downward change that would set the count under the
  reserved sum is SL-2's specification (the registry parks two
  shapes: refuse it, or let it end reservations). Until SL-2
  decides, SL-1's wall holds the invariant the only way that
  pre-decides nothing: such a change is not admitted, and the
  refusal is recorded as provisional in SL-1's plan.

## Consequences

Good: no catalog surface; one door for the count, as the
definition says; the evidence stages items through the real door
and the README's stranger can too; SL-2 inherits an adjustment
whose shape is decided and whose facts (resent, reordered) it can
still create.

Bad: "unknown item" and "item with count zero" are different
states a reader must learn; a delta-shaped adjustment, had it been
wanted, is now a logged revision here rather than a free choice
in SL-2; and SL-1 carries a small piece of SL-2's territory — a
downward change refused under the sum — flagged provisional so the
registry's re-decision at SL-1's close can see it.

Changes no export: L2's refusal of the catalog stands, an unknown
item is still refused at the door, P1's "every change enters
through the door" is what this decision applies.
