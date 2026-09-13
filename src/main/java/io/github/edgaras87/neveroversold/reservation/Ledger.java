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
     * Admits a reservation if its units still fit, else refuses — G1's one
     * act against the truth at the moment of recording.
     *
     * <p>The check and the write are one statement: the units are added
     * only where they still fit, and the store's answer — one row changed
     * or none — is the decision. Two racers update one row; the store
     * serializes them and the second re-evaluates the condition against
     * the first's result, so exactly the admits that still fit change a
     * row (F1, F21, F22, and F17 — the row is the only state, every
     * instance reaches it the same way). Behind the condition stands the
     * constraint {@code reserved <= on_hand_count}: an over-held row is
     * unwritable by any path. The reservation is recorded in the same
     * transaction; nothing is replied until it commits (G4).
     *
     * <p>None changed means either the item is unknown or the units do
     * not fit; one read tells which. That read decides nothing about
     * admission — the admission was decided by the store's answer above.
     */
    Reservation reserve(ItemId item, Quantity quantity, Hold hold) {
        return transaction.execute(status -> {
            int admitted = jdbc.sql("""
                    UPDATE item
                       SET reserved = reserved + :units
                     WHERE id = :id
                       AND reserved + :units <= on_hand_count
                    """)
                    .param("units", quantity.units())
                    .param("id", item.value())
                    .update();
            if (admitted == 0) {
                Item current = jdbc.sql("SELECT id, on_hand_count, reserved FROM item WHERE id = :id")
                        .param("id", item.value())
                        .query(Item.class)
                        .optional()
                        .orElseThrow(() -> new UnknownItem(item.value()));
                throw new Refused(quantity.units() + " units of " + item.value()
                        + " do not fit: " + current.reserved() + " held of "
                        + current.onHandCount() + " on hand");
            }
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
