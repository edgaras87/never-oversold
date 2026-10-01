# 0012. An adjustment answers 200, whether or not it creates the item

Date: 2026-10-01
Status: Proposed

## Context

ADR-0010 gives every success one of two statuses: `201 Created` for
a new record, `200 OK` for a transition on an existing one. ADR-0011
then made an item known by its first adjustment: the adjustment that
meets an unknown item creates it.

The two meet at that first adjustment. By ADR-0010's letter it
creates a record and should answer `201`. The door answers `200`, as
it does for every adjustment, and has since SL-1: README shows it,
`ReservationDoorIT` asserts it, SL-1's record lists the surface with
it. Nothing recorded the difference. An error check of the records,
before Step 7, found it.

## Options considered

1. **`201` when the adjustment creates the item, `200` otherwise.**
   ADR-0010's letter. The status would then depend on whether the
   ledger already knew the item, which the operator neither asks nor
   needs. A first adjustment resent after a lost reply would be
   answered `201`, then `200`: the same assertion, two answers. And
   the one statement that writes the count would have to report
   which of its two branches ran. Rejected.
2. **`200` for every adjustment.** An adjustment asserts a count
   (ADR-0011); the item coming into being is how the ledger learns
   of it, not what the operator asked for. The same request gets the
   same answer however often it arrives. Chosen.

## Decision

An adjustment answers `200 OK` with the item as persisted, whether
the item existed before or this adjustment created it.

This narrows one line of ADR-0010 — "`201 Created` for a new
record" — for the adjustment alone. Everything else ADR-0010
decides stands, and it stays Accepted. Reserve still answers `201`:
each reserve creates a reservation the caller asked for.

## Consequences

Good: an operator handles one success status for one kind of
request; a resend reads the same as the first send; the door, its
tests and README already behave this way, so nothing changes but
the record.

Bad: a reader of ADR-0010 alone would expect `201` for the item an
adjustment creates; this record is where they find why not, and
PLAN's decision index lists both. Whether an adjustment created its
item cannot be read from the answer; no slice has needed to know.
