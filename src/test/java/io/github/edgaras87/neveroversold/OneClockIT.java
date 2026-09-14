package io.github.edgaras87.neveroversold;

import java.util.Map;
import java.util.UUID;

import io.github.edgaras87.neveroversold.testsupport.WebDatabaseIT;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * E5, the behavioural half — active is judged by one clock, the store's
 * (G5, FC3; kill 10 removed rather than staged). Two facts read from the
 * store: a reservation's expiry instant is the store's own assignment
 * (its distance from the store-stamped creation instant is exactly the
 * hold asked for), and activeness is judged against the store's clock —
 * a hold that has run out by that clock leaves the active sum, one that
 * has not stays in it. The ledger supplied no timestamp in either case;
 * {@link NoInstanceStateOrClockTest} shows it could not have.
 *
 * <p>The counter {@code reserved} keeps counting the expired hold: an
 * expired reservation is not yet <em>ended</em> — ending is an exit,
 * SL-3's — and the counter over-approximates on the safe side (slice
 * record, §7). Stated here so the asymmetry is read as designed.
 */
class OneClockIT extends WebDatabaseIT {

    @Autowired
    private TestRestTemplate door;

    @Autowired
    private JdbcClient store;

    @Test
    void theExpiryInstantIsTheStoresAssignment() {
        String item = newItem(5);

        assertThat(reserve(item, "PT15M")).isEqualTo(HttpStatus.CREATED);

        String distance = store.sql("""
                SELECT (expires_at - created_at)::text FROM reservation WHERE item_id = :item
                """).param("item", item).query(String.class).single();
        assertThat(distance).isEqualTo("00:15:00");
    }

    @Test
    void activenessIsJudgedByTheStoresClock() throws InterruptedException {
        String item = newItem(5);
        assertThat(reserve(item, "PT1S")).isEqualTo(HttpStatus.CREATED);
        assertThat(reserve(item, "PT10M")).isEqualTo(HttpStatus.CREATED);

        Witness.Numbers both = Witness.read(item);
        assertThat(both.activeSum()).as("both holds active at first").isEqualTo(2);
        assertThat(both.held()).isEqualTo(2);

        Thread.sleep(1_500);   // the one-second hold runs out — by the store's clock, not this JVM's

        Witness.Numbers later = Witness.read(item);
        assertThat(later.activeSum()).as("the run-out hold left the active sum").isEqualTo(1);
        assertThat(later.held()).as("the counter still counts it until an exit ends it").isEqualTo(2);
        assertThat(later.holds()).isTrue();
    }

    private HttpStatus reserve(String item, String hold) {
        return (HttpStatus) door.postForEntity("/items/" + item + "/reservations",
                Map.of("quantity", 1, "hold", hold), String.class).getStatusCode();
    }

    private String newItem(int onHand) {
        String item = "clock-" + UUID.randomUUID();
        assertThat(door.postForEntity("/items/" + item + "/adjustments",
                Map.of("onHandCount", onHand), String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
        return item;
    }
}
