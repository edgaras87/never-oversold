package io.github.edgaras87.neveroversold.reservation;

import io.github.edgaras87.neveroversold.reservation.problems.Refused;
import io.github.edgaras87.neveroversold.reservation.problems.UnknownItem;
import io.github.edgaras87.neveroversold.reservation.problems.UnknownReservation;
import io.github.edgaras87.neveroversold.reservation.values.Hold;
import io.github.edgaras87.neveroversold.reservation.values.ItemId;
import io.github.edgaras87.neveroversold.reservation.values.OnHandCount;
import io.github.edgaras87.neveroversold.reservation.values.Quantity;
import io.github.edgaras87.neveroversold.reservation.values.ReservationId;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * The reservation ledger's writes: the admit of a reservation, the
 * change of an item's count, and the two exits. The one entry path to
 * the numbers (SL-1's record, §7): nothing else in the application
 * writes {@code item}, {@code reservation} or {@code reservation_exit},
 * and the application keeps no item state of its own — every instance
 * reaches the same rows through these statements (G2).
 *
 * <p>Every operation is one transaction; the decision is the commit
 * (G4): nothing is replied until the store has the record.
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
                    RETURNING id, item_id AS item, quantity, expires_at,
                              NULL AS ended_by, NULL::timestamptz AS ended_at
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
     * units held changes nothing — refused, which is the shape SL-2
     * decided and proved (its record, §3).
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

    /**
     * Ends a reservation by consume: its units leave the shelf — the count
     * and the units held both fall by exactly what it holds (FC2). Answered
     * as {@link #exit} says.
     */
    Reservation consume(ReservationId reservation) {
        return exit(reservation, "consume", """
                WITH receipt AS (
                    INSERT INTO reservation_exit (reservation_id, kind)
                    SELECT r.id, 'consumed' FROM reservation r
                     WHERE r.id = :id AND r.expires_at > now()
                    ON CONFLICT (reservation_id) DO NOTHING
                    RETURNING reservation_id
                )
                UPDATE item i
                   SET on_hand_count = i.on_hand_count - r.quantity,
                       reserved      = i.reserved - r.quantity
                  FROM receipt e JOIN reservation r ON r.id = e.reservation_id
                 WHERE i.id = r.item_id
                """);
    }

    /**
     * Ends a reservation by release: its units are free again — the units
     * held fall by exactly what it holds, the count stays. Answered as
     * {@link #exit} says.
     */
    Reservation release(ReservationId reservation) {
        return exit(reservation, "release", """
                WITH receipt AS (
                    INSERT INTO reservation_exit (reservation_id, kind)
                    SELECT r.id, 'released' FROM reservation r
                     WHERE r.id = :id AND r.expires_at > now()
                    ON CONFLICT (reservation_id) DO NOTHING
                    RETURNING reservation_id
                )
                UPDATE item i
                   SET reserved = i.reserved - r.quantity
                  FROM receipt e JOIN reservation r ON r.id = e.reservation_id
                 WHERE i.id = r.item_id
                """);
    }

    /**
     * One exit, in one transaction (SL-3's record, §8).
     *
     * <p>The exit's statement writes the reservation's receipt and moves
     * the item's numbers by the units on the reservation's own row — and
     * moves them only for a receipt it wrote itself. A reservation can
     * have one receipt (the key), so of two exits racing on it one writes
     * and the other's insert returns no row and moves nothing (G1); a
     * repeat finds the receipt standing and moves nothing (G2). The
     * receipt comes first and the item row after, the one order every
     * path keeps.
     *
     * <p>When nothing was written, a reservation past its expiry may have
     * no receipt yet: it ended at that instant (§3), and its expiry
     * receipt is written here, freeing its units once — the same key
     * again. The store refuses a consume or release receipt at or after
     * expiry whoever sends it; the statement's own condition stands in
     * front of that.
     *
     * <p>Then the answer, from the reservation as persisted (ADR-0013):
     * ended by this same exit — first or repeated — is {@code 200} with
     * the record; ended any other way is refused; no such reservation is
     * unknown. The refusal is thrown after the commit, so an expiry
     * receipt this request wrote stands.
     */
    private Reservation exit(ReservationId reservation, String exit, String statement) {
        Reservation persisted = transaction.execute(status -> {
            int moved = jdbc.sql(statement).param("id", reservation.value()).update();
            if (moved == 0) {
                jdbc.sql("""
                        WITH receipt AS (
                            INSERT INTO reservation_exit (reservation_id, kind)
                            SELECT r.id, 'expired' FROM reservation r
                             WHERE r.id = :id AND r.expires_at <= now()
                            ON CONFLICT (reservation_id) DO NOTHING
                            RETURNING reservation_id
                        )
                        UPDATE item i
                           SET reserved = i.reserved - r.quantity
                          FROM receipt e JOIN reservation r ON r.id = e.reservation_id
                         WHERE i.id = r.item_id
                        """).param("id", reservation.value()).update();
            }
            return jdbc.sql("""
                    SELECT r.id, r.item_id AS item, r.quantity, r.expires_at,
                           CASE e.kind WHEN 'consumed' THEN 'consume'
                                       WHEN 'released' THEN 'release'
                                       WHEN 'expired'  THEN 'expiry' END AS ended_by,
                           e.ended_at
                      FROM reservation r LEFT JOIN reservation_exit e ON e.reservation_id = r.id
                     WHERE r.id = :id
                    """)
                    .param("id", reservation.value())
                    .query(Reservation.class)
                    .optional()
                    .orElse(null);
        });
        if (persisted == null) {
            throw new UnknownReservation(reservation.value());
        }
        if (persisted.endedBy() == null) {
            throw new IllegalStateException("reservation " + reservation.value()
                    + " was neither ended by this " + exit + " nor ended before it");
        }
        if (!persisted.endedBy().equals(exit)) {
            throw new Refused("reservation " + reservation.value() + " has already ended by "
                    + persisted.endedBy() + ", at " + persisted.endedAt());
        }
        return persisted;
    }
}
