-- V2 — the receipts: what SL-3's walls need, and nothing else
-- (docs/construction/sl-3-a-reservation-exits-once.md, §8). Runs as
-- migrator; runtime can write the new table by the ground's default
-- privileges, so no GRANT appears here (infrastructure contract,
-- term 4).

-- A receipt: one row per ended reservation, saying how it ended and
-- when, by the store's clock. The key is the slice's main wall: a
-- reservation has one receipt, and a second cannot be written by any
-- path. The reference means no receipt names a reservation that does
-- not exist, and no reservation with a receipt can be deleted.
-- `reserved` on the item keeps its meaning, sharpened: the units of
-- reservations with no receipt yet.
CREATE TABLE reservation_exit (
    reservation_id uuid        NOT NULL,
    kind           text        NOT NULL,
    ended_at       timestamptz NOT NULL,
    CONSTRAINT reservation_exit_pk             PRIMARY KEY (reservation_id),
    CONSTRAINT reservation_exit_reservation_fk FOREIGN KEY (reservation_id) REFERENCES reservation (id),
    CONSTRAINT reservation_exit_kind_known     CHECK (kind IN ('consumed', 'released', 'expired'))
);

-- The guard: refuses, whoever writes, short of the superuser — a
-- changed or deleted receipt; an `expired` receipt before the
-- reservation's expiry instant by the store's clock; a `consumed` or
-- `released` one at or after it. It sets `ended_at` itself, so no path
-- supplies a time: the expiry instant for `expired`, the store's now
-- for the others. It refuses; it never moves the item's numbers — the
-- application's statement does that, on the one entry path. runtime
-- holds no TRUNCATE, so a delete is the only way to remove a row, and
-- this refuses it.
CREATE FUNCTION reservation_exit_guard() RETURNS trigger
LANGUAGE plpgsql AS $$
DECLARE
    expiry timestamptz;
BEGIN
    IF TG_OP = 'UPDATE' THEN
        RAISE EXCEPTION 'a receipt is never changed'
            USING ERRCODE = 'integrity_constraint_violation';
    END IF;
    IF TG_OP = 'DELETE' THEN
        RAISE EXCEPTION 'a receipt is never deleted'
            USING ERRCODE = 'integrity_constraint_violation';
    END IF;

    SELECT r.expires_at INTO expiry FROM reservation r WHERE r.id = NEW.reservation_id;
    IF NOT FOUND THEN
        -- the reference refuses it, with its own message
        RETURN NEW;
    END IF;

    IF NEW.kind = 'expired' THEN
        IF now() < expiry THEN
            RAISE EXCEPTION 'reservation % has not expired: it holds until %', NEW.reservation_id, expiry
                USING ERRCODE = 'integrity_constraint_violation';
        END IF;
        NEW.ended_at := expiry;
    ELSE
        IF now() >= expiry THEN
            RAISE EXCEPTION 'reservation % expired at %: it cannot be %', NEW.reservation_id, expiry, NEW.kind
                USING ERRCODE = 'integrity_constraint_violation';
        END IF;
        NEW.ended_at := now();
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER reservation_exit_guard
    BEFORE INSERT OR UPDATE OR DELETE ON reservation_exit
    FOR EACH ROW EXECUTE FUNCTION reservation_exit_guard();
