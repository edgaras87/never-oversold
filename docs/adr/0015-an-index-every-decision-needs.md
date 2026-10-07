# 0015. An index every decision needs is not ahead of need

Date: 2026-10-08
Status: Proposed

## Context

ADR-0007 decided that nothing enters ahead of need. It was written
about the build — no object mapping, no schema generation while
nothing persists domain state — and the slices have read it for the
schema as well: SL-3's plan added "no index beyond the key: nothing
here needs one at the scale of the evidence" (its record, §8).

PostgreSQL indexes a table's key, and makes no index for a column
that refers to another table. So `reservation.item_id` has none, and
since SL-3 nearly every request searches the reservations by it:

- **tidy**, the first thing every reserve and every correction does,
  finds the item's reservations that have run out;
- **the check on the units held**, at the commit of every transaction
  that touches an item, sums that item's reservations with no
  receipt — once for each row the transaction wrote.

Without an index each of those searches reads the whole table: every
reservation of every item, ended ones included, and ended ones are
kept for ever (W6). The definition now states the size the system is
built for — up to a few thousand reservations on one item over its
life — and the table holds every item's.

Say: three items, each with three thousand reservations over its
life. A consume on one of them reads nine thousand rows at commit to
sum the few still open on its own item, and the reserve after it
reads them all again in tidy. Nothing is wrong, and nothing has been
measured slow; the cost only grows.

## Options considered

1. **A need is measured.** An index enters when a decision is
   measured slow. ADR-0007's letter, read strictly. Rejected: nothing
   in the system measures speed (W3 keeps it out of the promise), so
   "when measured" means never, while the search grows with every
   reservation ever made.
2. **A need is read from the code.** A column that every decision
   searches by is indexed when the search is written: the need is in
   the statement, not a guess about the future. Chosen.
3. **Every column that refers to another table, up front.** A common
   habit. Rejected: it indexes what no statement searches, which is
   ahead of need in exactly ADR-0007's sense.

## Decision

- **An index on a column enters with the statement that searches by
  it on every decision**, or as soon as such a statement is found
  without one. A search that only some requests make waits for the
  size the definition states to say it matters.
- **`reservation(item_id)` is indexed** (V4): tidy and the check on
  the units held search by it on nearly every request.
- An index changes no answer and no wall. Adding one is tuning: the
  evidence runs again, unchanged.

ADR-0007 stands: nothing enters ahead of need. This record says what
"need" is for an index.

## Consequences

Good: the cost of the two searches follows one item's history, not
the whole table's, so the check on the units held stays cheap as the
system ages within the size it is built for. The rule is one a slice
applies when it writes a query, not one it waits to be told by a
measurement that will not come.

Bad: every insert into `reservation` also updates the index — a small
cost on each reserve. And the table still grows for ever: an item's
own history is what the searches read now, ended reservations among
it; keeping that short is retention, which W6 fences.

Changes no export.
