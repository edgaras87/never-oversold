package io.github.edgaras87.neveroversold;

import java.time.OffsetDateTime;
import java.util.UUID;

import io.github.edgaras87.neveroversold.testsupport.DatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * SL-3's walls, checked on their own, straight against the store as
 * {@code runtime} — no ledger, no door (the slice record:
 * {@code docs/construction/sl-3-a-reservation-exits-once.md}, §8). Each
 * statement here is one a script outside the application could send,
 * and the store refuses it whoever sends it.
 *
 * <p>Not evidence: these are the walls' own checks, in the spirit of
 * {@code MigrationPathIT.theWallIsInTheCatalog}, so the walls are known
 * to refuse before any path leans on them. They discharge no kill. The
 * evidence travels the door.
 *
 * <p>The reservations and receipts here are written by hand, each in
 * one statement with the units held moved to match, as V3's check on
 * the units held requires of every writer; nothing reads them but
 * these tests.
 */
class ReceiptGuardIT extends DatabaseIT {

    @Autowired
    private JdbcClient store;

    /** The key: a reservation has one receipt, and a second is refused. */
    @Test
    void aSecondReceiptIsRefused() {
        UUID reservation = aReservationExpiringIn("1 hour");
        writeReceipt(reservation, "consumed");

        assertThatThrownBy(() -> writeReceipt(reservation, "released"))
                .rootCause()
                .hasMessageContaining("reservation_exit_pk");
        assertThat(kindOf(reservation)).isEqualTo("consumed");
    }

    /** G6: the cleanup script cannot make an ended reservation look active again. */
    @Test
    void aReceiptIsNeverDeleted() {
        UUID reservation = aReservationExpiringIn("1 hour");
        writeReceipt(reservation, "consumed");

        assertThatThrownBy(() -> store.sql("DELETE FROM reservation_exit WHERE reservation_id = :id")
                .param("id", reservation).update())
                .rootCause()
                .hasMessageContaining("a receipt is never deleted");
        assertThat(kindOf(reservation)).isEqualTo("consumed");
    }

    /** G6: nor turn one ending into another. */
    @Test
    void aReceiptIsNeverChanged() {
        UUID reservation = aReservationExpiringIn("1 hour");
        writeReceipt(reservation, "consumed");

        assertThatThrownBy(() -> store.sql("UPDATE reservation_exit SET kind = 'released' WHERE reservation_id = :id")
                .param("id", reservation).update())
                .rootCause()
                .hasMessageContaining("a receipt is never changed");
        assertThat(kindOf(reservation)).isEqualTo("consumed");
    }

    /** G3: nothing ends a reservation by expiry before its instant, by the store's clock. */
    @Test
    void anExpiryReceiptIsNotTakenEarly() {
        UUID reservation = aReservationExpiringIn("1 minute");

        assertThatThrownBy(() -> writeReceipt(reservation, "expired"))
                .rootCause()
                .hasMessageContaining("has not expired");
        assertThat(hasReceipt(reservation)).isFalse();
    }

    /** G2: a reservation that has run out can no longer be consumed or released. */
    @Test
    void aConsumeOrReleaseReceiptIsNotTakenLate() {
        UUID reservation = aReservationThatExpired("1 minute");

        assertThatThrownBy(() -> writeReceipt(reservation, "consumed"))
                .rootCause()
                .hasMessageContaining("cannot be consumed");
        assertThatThrownBy(() -> writeReceipt(reservation, "released"))
                .rootCause()
                .hasMessageContaining("cannot be released");
        assertThat(hasReceipt(reservation)).isFalse();
    }

    /**
     * No path supplies a time: whatever ending instant a writer sends, the
     * store puts its own — the expiry instant for an expiry, its now for
     * the others.
     */
    @Test
    void theStoreSetsTheEndingInstant() {
        OffsetDateTime aLie = OffsetDateTime.parse("2000-01-01T00:00:00Z");

        UUID expired = aReservationThatExpired("1 minute");
        writeReceipt(expired, "expired", aLie);
        assertThat(store.sql("""
                SELECT e.ended_at = r.expires_at
                FROM reservation_exit e JOIN reservation r ON r.id = e.reservation_id
                WHERE e.reservation_id = :id
                """).param("id", expired).query(Boolean.class).single()).isTrue();

        UUID consumed = aReservationExpiringIn("1 hour");
        writeReceipt(consumed, "consumed", aLie);
        assertThat(store.sql("""
                SELECT e.ended_at > r.created_at AND e.ended_at <= now()
                FROM reservation_exit e JOIN reservation r ON r.id = e.reservation_id
                WHERE e.reservation_id = :id
                """).param("id", consumed).query(Boolean.class).single()).isTrue();
    }

    /** The reference: a reservation with a receipt cannot be deleted from under it. */
    @Test
    void anEndedReservationCannotBeDeleted() {
        UUID reservation = aReservationExpiringIn("1 hour");
        writeReceipt(reservation, "released");

        assertThatThrownBy(() -> store.sql("DELETE FROM reservation WHERE id = :id")
                .param("id", reservation).update())
                .rootCause()
                .hasMessageContaining("reservation_exit_reservation_fk");
    }

    private UUID aReservationExpiringIn(String interval) {
        return aReservation("now()", "now() + interval '" + interval + "'");
    }

    private UUID aReservationThatExpired(String ago) {
        return aReservation("now() - interval '1 hour'", "now() - interval '" + ago + "'");
    }

    /** An item of 10 holding the one unit of one reservation, written as one statement. */
    private UUID aReservation(String createdAt, String expiresAt) {
        String item = "receipt-guard-" + UUID.randomUUID();
        return store.sql("WITH held AS (INSERT INTO item (id, on_hand_count, reserved) VALUES (:id, 10, 1)) "
                        + "INSERT INTO reservation (item_id, quantity, created_at, expires_at) "
                        + "VALUES (:id, 1, " + createdAt + ", " + expiresAt + ") RETURNING id")
                .param("id", item).query(UUID.class).single();
    }

    private void writeReceipt(UUID reservation, String kind) {
        writeReceipt(reservation, kind, OffsetDateTime.parse("2000-01-01T00:00:00Z"));
    }

    /** A receipt and its units freed, as one statement — the store refuses either alone. */
    private void writeReceipt(UUID reservation, String kind, OffsetDateTime endedAt) {
        store.sql("""
                        WITH receipt AS (
                            INSERT INTO reservation_exit (reservation_id, kind, ended_at)
                            VALUES (:id, :kind, :at)
                            RETURNING reservation_id
                        )
                        UPDATE item i SET reserved = i.reserved - r.quantity
                          FROM receipt e JOIN reservation r ON r.id = e.reservation_id
                         WHERE i.id = r.item_id
                        """)
                .param("id", reservation).param("kind", kind).param("at", endedAt).update();
    }

    private String kindOf(UUID reservation) {
        return store.sql("SELECT kind FROM reservation_exit WHERE reservation_id = :id")
                .param("id", reservation).query(String.class).single();
    }

    private boolean hasReceipt(UUID reservation) {
        return store.sql("SELECT count(*) FROM reservation_exit WHERE reservation_id = :id")
                .param("id", reservation).query(Integer.class).single() > 0;
    }
}
