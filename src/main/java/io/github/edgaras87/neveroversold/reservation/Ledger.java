package io.github.edgaras87.neveroversold.reservation;

import io.github.edgaras87.neveroversold.reservation.problems.Refused;
import io.github.edgaras87.neveroversold.reservation.problems.StoreOutOfReach;
import io.github.edgaras87.neveroversold.reservation.problems.UnknownItem;
import io.github.edgaras87.neveroversold.reservation.problems.UnknownReservation;
import io.github.edgaras87.neveroversold.reservation.values.Hold;
import io.github.edgaras87.neveroversold.reservation.values.ItemId;
import io.github.edgaras87.neveroversold.reservation.values.OnHandCount;
import io.github.edgaras87.neveroversold.reservation.values.Quantity;
import io.github.edgaras87.neveroversold.reservation.values.ReservationId;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * The ledger's writes: reserve, adjust, consume, release, and the tidy
 * before each decision. The one entry path to the numbers: nothing else
 * writes {@code item}, {@code reservation} or {@code reservation_exit},
 * and no item state is kept here, so every instance reads the same rows
 * (SL-1 G2). Each operation is one transaction, and nothing is answered
 * before it commits (SL-1 G4).
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
     * Admits a reservation if its units still fit, else refuses. The check
     * and the write are one {@code UPDATE}, so the store runs racing admits
     * on one item one after another, and only those that still fit change
     * the row; the store's check constraint stands behind it. Holds SL-1 G1.
     *
     * <p>Tidy runs first, so an expired hold's units are free before the
     * admit decides. When no row changed, one read tells an unknown item
     * from a refusal; it decides nothing.
     */
    Reservation reserve(ItemId item, Quantity quantity, Hold hold) {
        return inOneTransaction(status -> {
            tidy(item);
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
                Item current = jdbc.sql("""
                                SELECT id, on_hand_count, reserved FROM item WHERE id = :id
                                """)
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
     * Sets an item's count to what the operator asserts, creating the item
     * if the ledger does not know it (ADR-0011). One conditional statement
     * on the item's row: a count under the units held changes nothing, and
     * is refused. Holds SL-2 G1 to G3; the row orders it against admits
     * (SL-1 G3).
     *
     * <p>Tidy runs first, so expired holds do not count against the
     * asserted number.
     */
    Item adjust(ItemId item, OnHandCount count) {
        return inOneTransaction(status -> {
            tidy(item);
            return jdbc.sql("""
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
                            + ": more units than that are held by reservations"));
        });
    }

    /**
     * Ends by expiry every hold of the item that has run out by the store's
     * clock, and frees its units. Runs first in every decision on the item.
     * The units held fall by the receipts this statement wrote, never by a
     * sum it read: two decisions meeting one expired hold race on its
     * receipt's key, and only the one that wrote it subtracts. Holds SL-3 G5.
     *
     * <p>{@code NOT EXISTS} only skips holds already ended; the key makes the
     * freeing once. Receipts go in identifier order. The store refuses an
     * expiry receipt written before the instant (SL-3 G3).
     */
    private void tidy(ItemId item) {
        jdbc.sql("""
                WITH receipt AS (
                    INSERT INTO reservation_exit (reservation_id, kind)
                    SELECT r.id, 'expired' FROM reservation r
                     WHERE r.item_id = :id AND r.expires_at <= now()
                       AND NOT EXISTS (SELECT 1 FROM reservation_exit e WHERE e.reservation_id = r.id)
                     ORDER BY r.id
                    ON CONFLICT (reservation_id) DO NOTHING
                    RETURNING reservation_id
                )
                UPDATE item i
                   SET reserved = i.reserved - freed.units
                  FROM (SELECT sum(r.quantity) AS units
                          FROM receipt e JOIN reservation r ON r.id = e.reservation_id) freed
                 WHERE i.id = :id AND freed.units IS NOT NULL
                """)
                .param("id", item.value())
                .update();
    }

    /**
     * Ends a reservation by consume: the count and the units held fall by
     * exactly what it holds (FC2), in one statement (SL-4 G1). Answered as
     * {@link #exit} says.
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
     * Ends a reservation by release: the units held fall by exactly what it
     * holds; the count stays. Answered as {@link #exit} says.
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
     * One exit, in one transaction. The statement writes the reservation's
     * receipt, then moves the numbers by the reservation's own units, and
     * only for a receipt it wrote. The store refuses a consume or release
     * receipt at or after expiry. Of two racing exits one writes and the
     * other moves nothing (SL-3 G1); a repeat moves nothing (SL-3 G2).
     * Receipt first, item row after: the one order every path keeps.
     *
     * <p>When nothing was written and the hold is past its expiry, its
     * expiry receipt is written here (SL-3 G5). Then the answer, from the
     * reservation as stored (ADR-0013). A refusal is thrown after the
     * commit, so an expiry receipt written here stands.
     */
    private Reservation exit(ReservationId reservation, String exit, String statement) {
        Reservation persisted = inOneTransaction(status -> {
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

    /**
     * Every decision's one transaction. A store lost mid-way becomes
     * {@link StoreOutOfReach} (ADR-0016): the commit may or may not have
     * arrived, and nothing here can say which. Any other failure is thrown
     * as it came — a refusal at commit included, which the store answered,
     * and undid.
     */
    private <T> T inOneTransaction(TransactionCallback<T> work) {
        try {
            return transaction.execute(work);
        } catch (DataAccessException | TransactionException failure) {
            if (lostTheStore(failure)) {
                throw new StoreOutOfReach(failure);
            }
            throw failure;
        }
    }

    /**
     * Decided by the store's own error class, never by the framework's
     * exception type (SL-4's record, "The surface"): a lost connection
     * and a refusal at commit can arrive in the same wrapper. Read through
     * the whole chain of causes, and through a failed rollback to the
     * failure it replaced — on a lost connection the rollback fails too,
     * and the framework throws that instead.
     */
    static boolean lostTheStore(Throwable failure) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sql && outOfReach(sql.getSQLState())) {
                return true;
            }
            if (cause instanceof TransactionSystemException rollback && rollback.getApplicationException() != null
                    && lostTheStore(rollback.getApplicationException())) {
                return true;
            }
        }
        return false;
    }

    /**
     * SQLSTATE class {@code 08}, the connection failed or was lost; and
     * {@code 57P01}–{@code 57P03}, the store ending the session or not
     * accepting it.
     */
    private static boolean outOfReach(String sqlState) {
        return sqlState != null && (sqlState.startsWith("08")
                || sqlState.equals("57P01") || sqlState.equals("57P02") || sqlState.equals("57P03"));
    }
}
