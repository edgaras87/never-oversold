package io.github.edgaras87.neveroversold.reservation;

import io.github.edgaras87.neveroversold.reservation.problems.Refused;
import io.github.edgaras87.neveroversold.reservation.problems.UnknownItem;
import io.github.edgaras87.neveroversold.reservation.values.Hold;
import io.github.edgaras87.neveroversold.reservation.values.ItemId;
import io.github.edgaras87.neveroversold.reservation.values.OnHandCount;
import io.github.edgaras87.neveroversold.reservation.values.Quantity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * The reservation ledger's writes: the admit of a reservation and the
 * change of an item's count. The one entry path to the numbers (slice
 * record, §7): nothing else in the application writes {@code item} or
 * {@code reservation}, and the application keeps no item state of its
 * own — every instance reaches the same rows through these statements
 * (G2).
 *
 * <p>Both operations are one transaction each; the decision is the
 * commit (G4): nothing is replied until the store has the record.
 */
@Component
class Ledger {

    private final JdbcClient jdbc;
    private final TransactionTemplate transaction;

    Ledger(JdbcClient jdbc, TransactionTemplate transaction) {
        this.jdbc = jdbc;
        this.transaction = transaction;
    }

    /**
     * Admits a reservation if its units still fit, else refuses.
     *
     * <p>Written the naive way for now — read the row, check in code,
     * then write — so the next change is the wall and nothing else. The
     * store's constraint already forbids an over-held row, so a racer that
     * loses here meets the constraint's error rather than a refusal.
     */
    Reservation reserve(ItemId item, Quantity quantity, Hold hold) {
        return transaction.execute(status -> {
            Item current = jdbc.sql("SELECT id, on_hand_count, reserved FROM item WHERE id = :id")
                    .param("id", item.value())
                    .query(Item.class)
                    .optional()
                    .orElseThrow(() -> new UnknownItem(item.value()));
            if (current.reserved() + quantity.units() > current.onHandCount()) {
                throw new Refused(quantity.units() + " units of " + item.value()
                        + " do not fit: " + current.reserved() + " held of "
                        + current.onHandCount() + " on hand");
            }
            jdbc.sql("UPDATE item SET reserved = reserved + :units WHERE id = :id")
                    .param("units", quantity.units())
                    .param("id", item.value())
                    .update();
            return jdbc.sql("""
                    INSERT INTO reservation (item_id, quantity, expires_at)
                    VALUES (:id, :units, now() + make_interval(secs => :seconds))
                    RETURNING id, item_id AS item, quantity, expires_at
                    """)
                    .param("id", item.value())
                    .param("units", quantity.units())
                    .param("seconds", hold.seconds())
                    .query(Reservation.class)
                    .single();
        });
    }

    /**
     * Sets an item's on-hand-count to what the operator asserts, creating
     * the item if the ledger did not know it (ADR-0011). One conditional
     * statement on the item's row: the store serializes it against any
     * admit on the same row (G3), and a count that would sit under the
     * units held changes nothing — refused, provisionally, until SL-2
     * decides the correction's shape.
     */
    Item adjust(ItemId item, OnHandCount count) {
        return transaction.execute(status -> jdbc.sql("""
                        INSERT INTO item (id, on_hand_count)
                        VALUES (:id, :count)
                        ON CONFLICT (id) DO UPDATE SET on_hand_count = EXCLUDED.on_hand_count
                            WHERE item.reserved <= EXCLUDED.on_hand_count
                        RETURNING id, on_hand_count, reserved
                        """)
                .param("id", item.value())
                .param("count", count.units())
                .query(Item.class)
                .optional()
                .orElseThrow(() -> new Refused("the on-hand-count of " + item.value()
                        + " cannot be set to " + count.units()
                        + ": more units than that are held by reservations")));
    }
}
