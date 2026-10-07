package io.github.edgaras87.neveroversold;

import java.util.UUID;

import io.github.edgaras87.neveroversold.testsupport.DatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * V3's check on the units held, on its own, straight against the store
 * as {@code runtime} — no ledger, no door (the slice record:
 * {@code docs/construction/sl-3-a-reservation-exits-once.md}, §8, G6,
 * revised 2026-10-05). Each statement is one a script outside the
 * application could send: at its commit the store compares the item's
 * units held with the units of its reservations that have no receipt,
 * and refuses the transaction if they differ.
 *
 * <p>Not evidence: these are the wall's own checks, like
 * {@link ReceiptGuardIT}, so the wall is known to refuse before any
 * path leans on it. They discharge no kill. The evidence travels the
 * door, and stays green with the check in place.
 *
 * <p>Each test starts from one item of 10 on hand holding the 3 units
 * of one reservation, R, written in step.
 */
class UnitsHeldCheckIT extends DatabaseIT {

    private static final String REFUSAL = "the units held read";

    @Autowired
    private JdbcClient store;

    /** One item, 10 on hand, R holding 3 of them. */
    private record Scene(String item, UUID r) {
    }

    /** The honest move is taken: R's receipt and its 3 units freed, in one statement. */
    @Test
    void aReceiptWithItsUnitsFreedIsTaken() {
        Scene scene = anItemHoldingR();

        store.sql("""
                WITH receipt AS (
                    INSERT INTO reservation_exit (reservation_id, kind) VALUES (:id, 'released')
                    RETURNING reservation_id
                )
                UPDATE item i SET reserved = i.reserved - r.quantity
                  FROM receipt e JOIN reservation r ON r.id = e.reservation_id
                 WHERE i.id = r.item_id
                """).param("id", scene.r()).update();

        assertThat(unitsHeld(scene)).isZero();
        assertThat(receipts(scene)).isEqualTo(1);
    }

    /** G6: the cleanup script cannot give back R's units without ending R. */
    @Test
    void unitsFreedWithoutAReceiptAreRefused() {
        Scene scene = anItemHoldingR();

        assertThatThrownBy(() -> store.sql("UPDATE item SET reserved = reserved - 3 WHERE id = :item")
                .param("item", scene.item()).update())
                .rootCause()
                .hasMessageContaining(REFUSAL + " 0, its reservations with no receipt hold 3");
        assertThat(unitsHeld(scene)).isEqualTo(3);
    }

    /** G6: nor end R and leave its units held. */
    @Test
    void aReceiptWithoutItsUnitsFreedIsRefused() {
        Scene scene = anItemHoldingR();

        assertThatThrownBy(() -> store.sql(
                        "INSERT INTO reservation_exit (reservation_id, kind) VALUES (:id, 'released')")
                .param("id", scene.r()).update())
                .rootCause()
                .hasMessageContaining(REFUSAL + " 3, its reservations with no receipt hold 0");
        assertThat(receipts(scene)).isZero();
        assertThat(unitsHeld(scene)).isEqualTo(3);
    }

    /** The units held are set by no hand: above the open holds as well as below them. */
    @Test
    void theUnitsHeldSetByHandAreRefused() {
        Scene scene = anItemHoldingR();

        assertThatThrownBy(() -> store.sql("UPDATE item SET reserved = 5 WHERE id = :item")
                .param("item", scene.item()).update())
                .rootCause()
                .hasMessageContaining(REFUSAL + " 5, its reservations with no receipt hold 3");
        assertThat(unitsHeld(scene)).isEqualTo(3);
    }

    /** A hold added without its units: the next decision would see them free. */
    @Test
    void aHoldAddedWithoutItsUnitsIsRefused() {
        Scene scene = anItemHoldingR();

        assertThatThrownBy(() -> store.sql("""
                        INSERT INTO reservation (item_id, quantity, expires_at)
                        VALUES (:item, 2, now() + interval '15 minutes')
                        """).param("item", scene.item()).update())
                .rootCause()
                .hasMessageContaining(REFUSAL + " 3, its reservations with no receipt hold 5");
        assertThat(reservations(scene)).isEqualTo(1);
    }

    /** A hold grown without its units: R would hold 5 while the item counts 3. */
    @Test
    void aHoldResizedWithoutItsUnitsIsRefused() {
        Scene scene = anItemHoldingR();

        assertThatThrownBy(() -> store.sql("UPDATE reservation SET quantity = 5 WHERE id = :id")
                .param("id", scene.r()).update())
                .rootCause()
                .hasMessageContaining(REFUSAL + " 3, its reservations with no receipt hold 5");
        assertThat(store.sql("SELECT quantity FROM reservation WHERE id = :id")
                .param("id", scene.r()).query(Integer.class).single()).isEqualTo(3);
    }

    /**
     * A hold moved to another item, its units carried there: the item it went to agrees, so only the
     * item it left — 3 held, nothing open — refuses it. Both items are checked, not only the new one.
     */
    @Test
    void aHoldMovedToAnotherItemIsRefused() {
        Scene scene = anItemHoldingR();
        String other = "units-held-" + UUID.randomUUID();
        store.sql("INSERT INTO item (id, on_hand_count, reserved) VALUES (:item, 10, 0)")
                .param("item", other).update();

        assertThatThrownBy(() -> store.sql("""
                        WITH moved AS (
                            UPDATE reservation SET item_id = :other WHERE id = :id RETURNING quantity
                        )
                        UPDATE item i SET reserved = i.reserved + m.quantity
                          FROM moved m
                         WHERE i.id = :other
                        """).param("other", other).param("id", scene.r()).update())
                .rootCause()
                .hasMessageContaining("item " + scene.item() + ": "
                        + REFUSAL + " 3, its reservations with no receipt hold 0");
        assertThat(reservations(scene)).isEqualTo(1);
    }

    /** An open hold deleted alone would leave its units held for ever. */
    @Test
    void anOpenHoldDeletedWithoutItsUnitsIsRefused() {
        Scene scene = anItemHoldingR();

        assertThatThrownBy(() -> store.sql("DELETE FROM reservation WHERE id = :id")
                .param("id", scene.r()).update())
                .rootCause()
                .hasMessageContaining(REFUSAL + " 3, its reservations with no receipt hold 0");
        assertThat(reservations(scene)).isEqualTo(1);
    }

    /** Deleted together with its units, an open hold goes: the check compares, it does not forbid. */
    @Test
    void anOpenHoldDeletedWithItsUnitsIsTaken() {
        Scene scene = anItemHoldingR();

        store.sql("""
                WITH gone AS (DELETE FROM reservation WHERE id = :id RETURNING item_id, quantity)
                UPDATE item i SET reserved = i.reserved - g.quantity
                  FROM gone g
                 WHERE i.id = g.item_id
                """).param("id", scene.r()).update();

        assertThat(reservations(scene)).isZero();
        assertThat(unitsHeld(scene)).isZero();
    }

    private Scene anItemHoldingR() {
        String item = "units-held-" + UUID.randomUUID();
        UUID r = store.sql("""
                        WITH held AS (INSERT INTO item (id, on_hand_count, reserved) VALUES (:item, 10, 3))
                        INSERT INTO reservation (item_id, quantity, expires_at)
                        VALUES (:item, 3, now() + interval '15 minutes')
                        RETURNING id
                        """).param("item", item).query(UUID.class).single();
        assertThat(store.sql("SELECT reserved FROM item WHERE id = :item")
                .param("item", item).query(Integer.class).single()).isEqualTo(3);
        return new Scene(item, r);
    }

    private int unitsHeld(Scene scene) {
        return store.sql("SELECT reserved FROM item WHERE id = :item")
                .param("item", scene.item()).query(Integer.class).single();
    }

    private int receipts(Scene scene) {
        return store.sql("SELECT count(*) FROM reservation_exit WHERE reservation_id = :id")
                .param("id", scene.r()).query(Integer.class).single();
    }

    private int reservations(Scene scene) {
        return store.sql("SELECT count(*) FROM reservation WHERE item_id = :item")
                .param("item", scene.item()).query(Integer.class).single();
    }
}
