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
 * held, and the sum of active reservations as the store judges them by
 * its own clock ({@code expires_at > now()}). The invariant is §1 of the
 * slice record; this class reads it, never computes it from replies.
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
                                      WHERE r.item_id = i.id AND r.expires_at > now()), 0) AS active_sum,
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
