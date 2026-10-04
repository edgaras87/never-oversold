package io.github.edgaras87.neveroversold.testsupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The witness, read from the store from outside every instance of the
 * ledger, as {@code runtime}: for one item, the on-hand-count, the units
 * held, and the sum of active reservations as the store judges them: no
 * receipt yet, and not past expiry by the store's own clock
 * ({@code expires_at > now()}). A reservation past its expiry with no
 * receipt is ended already (SL-3's record, §3); its receipt is written by
 * the next request that meets it. The invariant is §1 of the slice
 * record; this class reads it, never computes it from replies.
 *
 * <p>Plain JDBC on purpose: no application context, no pool the ledger
 * shares, so a forked-instance test and an in-process test read the same
 * way. {@link #sampleUntil} keeps reading while a storm runs, so "in
 * every readable state" is sampled, not only the state after.
 */
public final class Witness {

    /** One reading. {@code activeSum} is the invariant's left side, {@code onHandCount} its right. */
    public record Numbers(int onHandCount, int held, int activeSum, int reservations) {
        public boolean holds() {
            return activeSum <= onHandCount && held <= onHandCount && activeSum <= held;
        }
    }

    private Witness() {
    }

    public static Numbers read(String item) {
        try (Connection store = connect();
             PreparedStatement statement = store.prepareStatement("""
                     SELECT i.on_hand_count,
                            i.reserved,
                            coalesce((SELECT sum(r.quantity) FROM reservation r
                                      WHERE r.item_id = i.id AND r.expires_at > now()
                                        AND NOT EXISTS (SELECT 1 FROM reservation_exit e
                                                        WHERE e.reservation_id = r.id)), 0) AS active_sum,
                            (SELECT count(*) FROM reservation r WHERE r.item_id = i.id) AS reservations
                     FROM item i WHERE i.id = ?
                     """)) {
            statement.setString(1, item);
            try (ResultSet row = statement.executeQuery()) {
                if (!row.next()) {
                    throw new IllegalStateException("no item " + item + " in the store");
                }
                return new Numbers(row.getInt(1), row.getInt(2), row.getInt(3), row.getInt(4));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("the witness could not be read", e);
        }
    }

    /** Whether a reservation with this id is in the store — E4's "every admitted reply names a record". */
    public static boolean holdsReservation(UUID id) {
        try (Connection store = connect();
             PreparedStatement statement = store.prepareStatement(
                     "SELECT 1 FROM reservation WHERE id = ?")) {
            statement.setObject(1, id);
            try (ResultSet row = statement.executeQuery()) {
                return row.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("the witness could not be read", e);
        }
    }

    /**
     * How a reservation ended, as its receipt says — {@code consumed},
     * {@code released} or {@code expired} — or {@code null} while it has
     * none. Read straight from the receipts, never from a reply.
     */
    public static String endingOf(UUID reservation) {
        try (Connection store = connect();
             PreparedStatement statement = store.prepareStatement(
                     "SELECT kind FROM reservation_exit WHERE reservation_id = ?")) {
            statement.setObject(1, reservation);
            try (ResultSet row = statement.executeQuery()) {
                return row.next() ? row.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("the witness could not be read", e);
        }
    }

    /**
     * Every receipt a reservation has, by kind. One at most while the key
     * stands; a storm run with the key absent shows two here, which is
     * the broken promise E1 looks for.
     */
    public static List<String> receiptsOf(UUID reservation) {
        try (Connection store = connect();
             PreparedStatement statement = store.prepareStatement(
                     "SELECT kind FROM reservation_exit WHERE reservation_id = ? ORDER BY kind")) {
            statement.setObject(1, reservation);
            try (ResultSet row = statement.executeQuery()) {
                List<String> kinds = new ArrayList<>();
                while (row.next()) {
                    kinds.add(row.getString(1));
                }
                return kinds;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("the witness could not be read", e);
        }
    }

    /**
     * How each reservation of an item ended, one line per receipt (a
     * reservation with none reads with a {@code null} kind): its units,
     * and whether the store's ending instant fell before its expiry
     * instant — both instants the store's own.
     */
    public record Ending(UUID reservation, int quantity, String kind, Boolean beforeExpiry) {
    }

    public static List<Ending> endingsOf(String item) {
        try (Connection store = connect();
             PreparedStatement statement = store.prepareStatement("""
                     SELECT r.id, r.quantity, e.kind, e.ended_at < r.expires_at
                     FROM reservation r LEFT JOIN reservation_exit e ON e.reservation_id = r.id
                     WHERE r.item_id = ?
                     """)) {
            statement.setString(1, item);
            try (ResultSet row = statement.executeQuery()) {
                List<Ending> endings = new ArrayList<>();
                while (row.next()) {
                    endings.add(new Ending(row.getObject(1, UUID.class), row.getInt(2), row.getString(3),
                            (Boolean) row.getObject(4)));
                }
                return endings;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("the witness could not be read", e);
        }
    }

    /**
     * Reads the witness again and again on its own thread until {@code stop}
     * is set, returning every reading — the storm's every readable state
     * as this reader saw it. A violating reading is a broken promise.
     */
    public static Thread sampleUntil(String item, AtomicBoolean stop, List<Numbers> samples) {
        Thread sampler = new Thread(() -> {
            while (!stop.get()) {
                samples.add(read(item));
            }
        }, "witness-" + item);
        sampler.setDaemon(true);
        sampler.start();
        return sampler;
    }

    public static List<Numbers> violations(List<Numbers> samples) {
        List<Numbers> broken = new ArrayList<>();
        for (Numbers sample : samples) {
            if (!sample.holds()) {
                broken.add(sample);
            }
        }
        return broken;
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                ThrowawayStore.jdbcUrl(), ThrowawayStore.RUNTIME_IDENTITY, ThrowawayStore.RUNTIME_PASSWORD);
    }
}
