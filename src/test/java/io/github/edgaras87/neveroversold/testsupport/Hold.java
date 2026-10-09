package io.github.edgaras87.neveroversold.testsupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * A row held at the store from outside every instance, so a consume that
 * needs it stops there, mid-work, until the test lets go (SL-4's record,
 * §8, "The surface"). No test code runs inside an instance (ADR-0009):
 * the consume stops because the store makes it wait, as it would for any
 * other transaction holding the row.
 *
 * <p>Two points a consume can be held at, each by the row it needs next:
 * <ul>
 *   <li>{@link #itemRow} — the consume has written its receipt and waits
 *       to move the item's row: one move written, the other not.</li>
 *   <li>{@link #reservationRow} — the consume has written its receipt and
 *       moved the item's row, and waits at the end of its statement, where
 *       the store checks the receipt's reference to the reservation: both
 *       moves written, neither committed.</li>
 * </ul>
 *
 * <p>Taken as the store's superuser, trusted under T4; it locks a row and
 * writes nothing. {@link #awaitWaiter} reads the store's own list of who
 * waits on whom, so a test knows the consume is held, never guesses it by
 * sleeping.
 */
public final class Hold implements AutoCloseable {

    /** A session the hold is keeping waiting, and what it was running. */
    public record Waiter(int pid, String query) {
    }

    private static final Duration WAIT_FOR_WAITER = Duration.ofSeconds(30);

    private final Connection store;
    private final int pid;
    private final String what;
    private boolean held = true;

    private Hold(Connection store, int pid, String what) {
        this.store = store;
        this.pid = pid;
        this.what = what;
    }

    /** Holds an item's row: a consume of one of its reservations stops between its two moves. */
    public static Hold itemRow(String item) {
        return take("the row of item " + item, "SELECT 1 FROM item WHERE id = ? FOR UPDATE", item);
    }

    /** Holds a reservation's row: its consume stops with both moves written, before its statement ends. */
    public static Hold reservationRow(UUID reservation) {
        return take("the row of reservation " + reservation,
                "SELECT 1 FROM reservation WHERE id = ? FOR UPDATE", reservation);
    }

    private static Hold take(String what, String sql, Object key) {
        try {
            Connection store = ThrowawayStore.superuser();
            store.setAutoCommit(false);
            try (PreparedStatement lock = store.prepareStatement(sql)) {
                lock.setObject(1, key);
                try (ResultSet row = lock.executeQuery()) {
                    if (!row.next()) {
                        store.rollback();
                        store.close();
                        throw new IllegalStateException("nothing to hold: " + what + " is not in the store");
                    }
                }
            }
            int pid;
            try (PreparedStatement self = store.prepareStatement("SELECT pg_backend_pid()");
                 ResultSet row = self.executeQuery()) {
                row.next();
                pid = row.getInt(1);
            }
            return new Hold(store, pid, what);
        } catch (SQLException e) {
            throw new IllegalStateException("could not hold " + what, e);
        }
    }

    /**
     * Waits until some session is waiting on this hold, and returns it.
     * Read from a separate connection each time: the store's list of
     * sessions is fixed for the length of a transaction, and the hold's
     * own stays open.
     */
    public Waiter awaitWaiter() throws InterruptedException {
        Instant deadline = Instant.now().plus(WAIT_FOR_WAITER);
        while (true) {
            try (Connection reader = ThrowawayStore.superuser();
                 PreparedStatement waiting = reader.prepareStatement("""
                         SELECT pid, query FROM pg_stat_activity
                          WHERE ? = ANY(pg_blocking_pids(pid))
                         """)) {
                waiting.setInt(1, pid);
                try (ResultSet row = waiting.executeQuery()) {
                    if (row.next()) {
                        return new Waiter(row.getInt(1), row.getString(2));
                    }
                }
            } catch (SQLException e) {
                throw new IllegalStateException("could not read who waits on " + what, e);
            }
            if (Instant.now().isAfter(deadline)) {
                throw new IllegalStateException("nothing came to wait on " + what + " within " + WAIT_FOR_WAITER);
            }
            Thread.sleep(10);
        }
    }

    /**
     * Whether another session holds an item's row right now — asked
     * without waiting, so the answer is about this instant. A consume held
     * at its reservation's row answers yes: it has moved the item's row
     * already.
     */
    public static boolean itemRowTaken(String item) {
        try (Connection probe = ThrowawayStore.superuser()) {
            probe.setAutoCommit(false);
            try (PreparedStatement lock = probe.prepareStatement(
                    "SELECT 1 FROM item WHERE id = ? FOR UPDATE NOWAIT")) {
                lock.setString(1, item);
                lock.executeQuery().close();
                return false;
            } catch (SQLException e) {
                if ("55P03".equals(e.getSQLState())) {   // lock_not_available
                    return true;
                }
                throw e;
            } finally {
                probe.rollback();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("could not ask whether the row of item " + item + " is taken", e);
        }
    }

    /** Lets go: the hold's transaction ends, having written nothing, and the waiter goes on. */
    public void letGo() {
        if (!held) {
            return;
        }
        held = false;
        try (store) {
            store.rollback();
        } catch (SQLException e) {
            throw new IllegalStateException("could not let go of " + what, e);
        }
    }

    @Override
    public void close() {
        letGo();
    }
}
