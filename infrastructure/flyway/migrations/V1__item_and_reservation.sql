-- V1 — the first structure: what SL-1's wall needs, and nothing else
-- (docs/construction/sl-1-no-over-admission.md, §7). Runs as migrator;
-- runtime can write both tables by the ground's default privileges, so
-- no GRANT appears here (infrastructure contract, term 4).

-- An item is known to the ledger when it has an on-hand-count
-- (ADR-0011). `reserved` is the units held by reservations not yet
-- ended, kept by the ledger's one entry path in the same transaction
-- as every reservation write. The last constraint is the promise:
-- the store refuses to hold a row where more is held than is on hand,
-- whoever writes it.
CREATE TABLE item (
    id            text    NOT NULL,
    on_hand_count integer NOT NULL,
    reserved      integer NOT NULL DEFAULT 0,
    CONSTRAINT item_pk               PRIMARY KEY (id),
    CONSTRAINT item_count_not_negative    CHECK (on_hand_count >= 0),
    CONSTRAINT item_reserved_not_negative CHECK (reserved >= 0),
    CONSTRAINT item_never_oversold        CHECK (reserved <= on_hand_count)
);

-- A reservation: which item, how many units, until when. The
-- identifier and the timestamps are the store's own (G5: one clock —
-- `created_at` is the store's now at insert, `expires_at` is set from
-- it by the insert, and the evidence reads both). No ended state yet:
-- ending a reservation is an exit, SL-3's.
CREATE TABLE reservation (
    id         uuid        NOT NULL DEFAULT gen_random_uuid(),
    item_id    text        NOT NULL,
    quantity   integer     NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    expires_at timestamptz NOT NULL,
    CONSTRAINT reservation_pk             PRIMARY KEY (id),
    CONSTRAINT reservation_item_fk        FOREIGN KEY (item_id) REFERENCES item (id),
    CONSTRAINT reservation_quantity_positive CHECK (quantity > 0),
    CONSTRAINT reservation_expires_after_creation CHECK (expires_at > created_at)
);
