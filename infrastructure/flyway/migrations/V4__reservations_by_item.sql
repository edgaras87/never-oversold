-- V4 — reservations indexed by item: ADR-0015. Runs as migrator; no
-- GRANT (infrastructure contract, term 4).

-- Tidy, at the start of every reserve and correction, and the check on
-- the units held (V3), at the commit of every transaction that touches
-- an item, both search an item's reservations. The key indexes `id`
-- and nothing else, so without this each search read the whole table —
-- every item's reservations, ended ones kept for ever (W6). With it, a
-- search reads one item's own.
--
-- Tuning, not a wall: it changes no answer and refuses nothing. Built
-- plainly, inside the migration's transaction, which holds writes to
-- `reservation` while it runs — a moment at the size the system is
-- built for (the definition's runtime ground).
CREATE INDEX reservation_by_item ON reservation (item_id);
