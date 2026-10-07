-- V3 — the check on the units held: SL-3's G6, revised 2026-10-05
-- (docs/construction/sl-3-a-reservation-exits-once.md, §8). Runs as
-- migrator; nothing here changes what runtime may write (infrastructure
-- contract, term 4).

-- `reserved` is the units of an item's reservations with no receipt
-- yet (V1, sharpened by V2). The ledger keeps it so, moving it in the
-- same statement that writes the reservation or its receipt; this
-- makes the store refuse any transaction that leaves it otherwise,
-- whoever sends it: units freed without a receipt, a receipt written
-- without its units freed, the units held set by hand, a hold added or
-- deleted without its units. The arithmetic stays the application's;
-- this computes nothing into the rows, it only compares and refuses.
--
-- Deferred to the commit: an exit writes its receipt before it moves
-- the item row, and only the finished transaction is judged. Every
-- honest path moves the item row, so it holds that row's lock when its
-- check runs, and a concurrent path on the same item waits behind it.
CREATE FUNCTION units_held_agree() RETURNS trigger
LANGUAGE plpgsql AS $$
DECLARE
    items   text[];
    checked text;
    counted integer;
    open    integer;
BEGIN
    IF TG_TABLE_NAME = 'item' THEN
        items := ARRAY[NEW.id];
    ELSIF TG_TABLE_NAME = 'reservation' THEN
        items := CASE TG_OP WHEN 'INSERT' THEN ARRAY[NEW.item_id]
                            WHEN 'DELETE' THEN ARRAY[OLD.item_id]
                            ELSE ARRAY[OLD.item_id, NEW.item_id] END;
    ELSE
        SELECT array_agg(r.item_id) INTO items FROM reservation r
         WHERE r.id = CASE TG_OP WHEN 'DELETE' THEN OLD.reservation_id
                                 ELSE NEW.reservation_id END;
    END IF;

    FOREACH checked IN ARRAY coalesce(items, ARRAY[]::text[]) LOOP
        SELECT i.reserved INTO counted FROM item i WHERE i.id = checked;
        -- an item deleted in the same transaction has nothing left to count
        CONTINUE WHEN NOT FOUND;
        SELECT coalesce(sum(r.quantity), 0) INTO open FROM reservation r
         WHERE r.item_id = checked
           AND NOT EXISTS (SELECT 1 FROM reservation_exit e WHERE e.reservation_id = r.id);
        IF counted <> open THEN
            RAISE EXCEPTION 'item %: the units held read %, its reservations with no receipt hold %',
                checked, counted, open
                USING ERRCODE = 'integrity_constraint_violation';
        END IF;
    END LOOP;
    RETURN NULL;
END;
$$;

CREATE CONSTRAINT TRIGGER units_held_agree
    AFTER INSERT OR UPDATE ON item
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW EXECUTE FUNCTION units_held_agree();

CREATE CONSTRAINT TRIGGER units_held_agree
    AFTER INSERT OR UPDATE OR DELETE ON reservation
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW EXECUTE FUNCTION units_held_agree();

CREATE CONSTRAINT TRIGGER units_held_agree
    AFTER INSERT OR UPDATE OR DELETE ON reservation_exit
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW EXECUTE FUNCTION units_held_agree();
