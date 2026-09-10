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

## Revision log

<!-- Dated entries only: what changed, why, what triggered it. -->

- (none since framing close, 2026-09-10)
