# 0013. A repeated exit answers as the first did

Date: 2026-10-02
Status: Accepted

## Context

ADR-0010 gives the two exits their paths —
`POST /reservations/{reservation}/consume` and
`POST /reservations/{reservation}/release` — and says an exit on an
ended reservation is refused: `409 Conflict`.

SL-3 builds the exits. Its first fact is F3: a caller sends consume,
the reply is lost, and the caller sends consume again. The
invariant decides what the numbers do: the count moves once, never
twice (kill 12). Nothing in the framing decides what the second
reply says. The definition refuses the caller's view (L2), so the
reply is a convention of the door, not a promise. It is decided
here, at SL-3's opening, before the specification that depends on
it.

Say: an item has 10 on hand, and reservation R holds 3 of them. The
caller consumes R. The count becomes 7 and R is ended. The reply is
lost. The caller sends consume on R again. The count stays 7 either
way. The question is only what the caller reads.

## Options considered

1. **`409 Conflict`, "already ended".** ADR-0010's letter. True,
   but the caller whose first reply was lost reads an error for a
   consume that worked. To learn that it worked, they must read the
   detail text. Rejected.
2. **`200 OK` with the reservation as persisted, showing it ended
   by that same exit.** The repeat gets the answer the first send
   got. This is the reasoning of ADR-0012: the same request gets the
   same answer, however often it arrives. Chosen.

## Decision

- **An exit repeated on a reservation that the same exit already
  ended answers `200 OK`** with the reservation as persisted. A
  consume on a consumed reservation, or a release on a released
  one. The numbers do not move again.
- **A different exit on an ended reservation is still refused,
  `409 Conflict`**, as ADR-0010 says: release after consume, consume
  after release, either exit after expiry.
- **An exit carries no body.** It names its reservation in the path
  and moves exactly what that reservation holds (FC2), so there is
  no quantity for a caller to get wrong.

This narrows one line of ADR-0010, "refused — an exit on an ended
reservation", for the case where the exit repeats the one that
ended it. Everything else ADR-0010 decides stands, and it stays
Accepted.

## Consequences

Good: the caller who retries after a lost reply reads success,
which is the truth; a repeated exit reads the same as the first;
the door treats exits the way ADR-0012 treats adjustments.

Bad: the reply cannot tell a first exit from a repeat. Nothing the
promise needs depends on that. Two callers who both consume one
reservation both read `200`; the definition trusts a request's
stated identity (T3), so a reservation has one caller and two
consumes are that caller's repeat. And a reader of ADR-0010 alone
would expect `409`; this record is where they find why not, and
PLAN's decision index lists both.

Changes no export. The invariant, kill 12 and FC2 stand as the
definition has them; this decides only what the door says back.
