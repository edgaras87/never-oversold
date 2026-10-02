# SL-3 — a reservation exits once

<!-- The slice's record, grown in three movements: the correctness
     specification (what must hold, against what, and what proof
     looks like — no mechanism), then the plan (one structural owner
     per guarantee, and why it beats the named adversity), then the
     evidence as delivered (which test creates which adversity, and
     what it read from the store). Invariant and adversity are the
     registry's and the definition's, as written; guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each movement.

     §3 is this slice's own section: three things the framing does
     not decide, decided before the specification that depends on
     them. -->

## §1 Invariant

A reservation moves its item's numbers at most once on exit, and
never after it has ended. (`docs/system/registry.md`, SL-3; concern
C in the definition's L4.)

The words, as the truth set has them:

- **An exit** is a way out of active. There are three: **consume**
  (the units were sold — the reservation ends and its units leave
  the on-hand-count), **release** (the caller gave up — the
  reservation ends and its units are free again), and **expiry**
  (the hold ran out — the same as release, with time as the one
  who acts). L2 names release and expiry; P4 names consume.
- **Ended** is FC3's, as SL-1 consumed it: ended by an exit, or
  past its expiry as judged by one clock, not one per instance.
- **The item's numbers** are the on-hand-count and the units held
  by its reservations, the two the promise compares.

## §2 Adversity

Exits that repeat, race, or arrive too late, and a clock that ends
a hold too soon. Pulled from the definition's L1 as written:

- a caller resends consume after a lost reply — the on-hand-count
  lowered twice for one reservation (F3, kill 12, and kill 16's
  retry half);
- a caller sends consume and release for one reservation at the
  same instant, or two consumes — two exits racing on one
  reservation, each moving numbers (F4, kill 13);
- expiry ends a reservation while a consume for it is in flight —
  two exits racing, time being one partner (F23, kill 14);
- a caller consumes or releases a reservation that already ended —
  expired, consumed, released — including very late (F5, kill 11);
- time jumps, and expiry fires early — holds end before their time,
  and late consumes follow (F24, kill 19's early half).

Kills covered: 11, 12, 13, 14, 16 (our retry), 19 (early). FC2 is
folded in: an exit names its own reservation and moves exactly what
it holds (kill 17).

What this slice does not face: our own death between consume's two
moves, and our silence after an unknowable outcome (kills 15 and
16's silence half — SL-4); who may exit a reservation (kill 17's
identity part — W5, T3); holds that never expire (kill 19's other
half — V5); a retried reserve (kill 20 — W2).

## §3 Decided before the specification

Three questions the framing leaves open, found at this slice's
opening (PLAN, Step 7) and decided by the reviewer on 2026-10-02,
each with its options.

### What a repeated exit answers — ADR-0013

The paths are ADR-0010's, and an exit carries no body: it names its
reservation and moves exactly what that holds (FC2). What the
framing does not say is what a repeat answers. The numbers are the
invariant's and move once either way; the reply is the door's.

**Chosen:** an exit repeated on a reservation that the same exit
already ended answers `200 OK`, the reservation as persisted. A
different exit on an ended reservation is still refused, `409`.
**Rejected:** `409` for every exit on an ended reservation — true,
but the caller who lost the first reply reads an error for a
consume that worked. The options and reasons in full are ADR-0013's.

### When an expired hold's units are free

FC3 already says a reservation past its expiry, by the store's
clock, is no longer active, and F5 says a consume after that is a
request against a reservation with no active state left. What the
framing does not say is when the units it held become free for
the next decision.

Today they never do. SL-1 left the held units counting a
reservation until an exit ends it (SL-1 §7), and nothing ends one
by expiry.
*Say:* 10 on hand. A holds 3 until 12:00 and walks away. At 12:05
B asks for 8. The held units still read 3, so only 7 look free and
B is refused — at 12:05, and at every time after.

**Chosen: free from the expiry instant.** A reservation is ended
the moment its expiry passes, by the store's clock. Nothing has to
happen for that: no request, no sweep. Every decision taken after
the instant — a reserve, a correction — sees its units as free.
*Say:* at 12:05 B's 8 is admitted against 10 on hand and 0 held.

**Rejected: free eventually, within a bound.** Something ends
expired holds now and then; until it runs, they still block units.
It is safe — the invariant holds either way — but it holds more
than it must, and the something that runs is a new moving part
that can die or fall behind. Those are adversities this slice does
not name.

**What the choice pays.** Two debts handed here by name: SL-1's
held units over-counting expired holds (SL-1 §7, "Deviations and
provisionals"), and SL-2's refusals being conservative against
holds the clock has already ended (SL-2 §7, "That the sum is
minimal").

**What it costs.** The reserve and the correction are SL-1's and
SL-2's decisions, proved on their own evidence. This choice changes
what they read. Both slices' evidence must stay green, unchanged —
the standing guard SL-1 §9 names for any later slice touching its
state.

Changes no export: FC3 already defines *active* this way, and V5
already counts on expiry as the exit for abandoned holds.

### Whether an operator can end reservations

SL-2 handed this here (SL-2 §3 and §7). An operator counts 5 on the
shelf while 8 are held; the correction to 5 is refused; they can
only wait for expiry or for callers to release.

**Chosen: no operator-side exit in this slice.** The release at the
door does not ask who is calling — the definition trusts a
request's stated identity (T3) and leaves who may act outside (W5).
So an operator who knows a reservation's identifier can release it
like anyone else. What an operator cannot do is find those
identifiers: there is no list of an item's reservations. A list is
a feature no guarantee here needs, so it is not built; it waits in
the backlog.

**Rejected: an exit for the operator** — for example, a correction
that ends enough holds to fit. Something would have to choose which
holds end: the oldest, the largest, the latest. SL-2 §3 rejected
that for the reason that stands here too: it is a policy no
possession grants. The system was never given a rule for choosing.

**What the choice costs.** The operator in SL-2's example still has
no practical recourse. The promise holds; the seller can still sell
units that are not on the shelf, until the holds end. This is W1's
remainder, and it stays fenced.
