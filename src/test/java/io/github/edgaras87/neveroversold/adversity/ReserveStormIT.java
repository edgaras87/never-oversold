package io.github.edgaras87.neveroversold.adversity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.edgaras87.neveroversold.testsupport.WebDatabaseIT;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import io.github.edgaras87.neveroversold.testsupport.Body;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SL-1 E1 — the storm on one item's last units, in one instance: a
 * hundred reserve requests held at a line and released at one instant
 * against an item holding twenty units, each a full HTTP → application →
 * store round-trip through the real door. The witness (SL-1's invariant)
 * is read from the store — sampled while the storm runs and read after —
 * never computed from replies. SL-1 E4 rides on it: every reply that says
 * admitted names a reservation the store holds, and the store holds
 * exactly those.
 *
 * <p>The storm is shown real: more is asked than fits, and some are
 * refused. What this does not prove: the same across instances — that
 * is {@link InstancesStormIT}.
 */
class ReserveStormIT extends WebDatabaseIT {

    private static final int ON_HAND = 20;
    private static final int REQUESTS = 100;

    @Autowired
    private TestRestTemplate door;

    /**
     * SL-1 E1 · G1 — kills 1, 3 and 4. SL-1 E4 · G4 — kill 9, riding on the
     * same storm.
     *
     * <p>Twenty units on hand, a hundred requests for one unit each, all
     * released at the same instant. At most twenty may be admitted, and the
     * store must hold exactly the reservations the admitted replies name —
     * no more, no fewer. This fails if the admit ever checks the numbers in
     * one step and writes in another: two requests then see the same free
     * unit, and both take it.
     */
    @Test
    void aHundredSimultaneousReservesNeverOversell() throws Exception {
        String item = "storm-" + UUID.randomUUID();
        assertThat(door.postForEntity("/items/" + item + "/adjustments",
                Map.of("onHandCount", ON_HAND), String.class).getStatusCode()).isEqualTo(HttpStatus.OK);

        ExecutorService pool = Executors.newFixedThreadPool(REQUESTS);
        CountDownLatch atTheLine = new CountDownLatch(REQUESTS);
        CountDownLatch release = new CountDownLatch(1);
        AtomicBoolean stormOver = new AtomicBoolean();
        List<Witness.Numbers> samples = new ArrayList<>();
        Thread sampler = Witness.sampleUntil(item, stormOver, samples);
        try {
            List<Future<ResponseEntity<String>>> replies = new ArrayList<>();
            for (int i = 0; i < REQUESTS; i++) {
                replies.add(pool.submit(() -> {
                    atTheLine.countDown();
                    release.await();
                    return door.postForEntity("/items/" + item + "/reservations",
                            Map.of("quantity", 1, "hold", "PT10M"), String.class);
                }));
            }
            atTheLine.await();
            release.countDown();   // the one instant

            int admitted = 0;
            int refused = 0;
            List<UUID> admittedIds = new ArrayList<>();
            for (Future<ResponseEntity<String>> reply : replies) {
                ResponseEntity<String> response = reply.get();
                if (response.getStatusCode() == HttpStatus.CREATED) {
                    admitted++;
                    admittedIds.add(Body.of(response.getBody()).uuidAt("$.id"));
                } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                    refused++;
                } else {
                    throw new AssertionError("a reply that is neither admitted nor refused: "
                            + response.getStatusCode() + " " + response.getBody());
                }
            }
            stormOver.set(true);
            sampler.join();

            // the storm was real
            assertThat(REQUESTS).isGreaterThan(ON_HAND);
            assertThat(refused).as("some were refused: %d of %d", refused, REQUESTS).isGreaterThan(0);

            // the witness, after
            Witness.Numbers after = Witness.read(item);
            assertThat(after.activeSum()).as("active sum ≤ on-hand-count: %s", after)
                    .isLessThanOrEqualTo(after.onHandCount());
            assertThat(after.held()).as("held ≤ on-hand-count: %s", after)
                    .isLessThanOrEqualTo(after.onHandCount());
            assertThat(admitted)
                    .as("admitted replies account for exactly the units held: %d admitted, %s", admitted, after)
                    .isEqualTo(after.held());
            assertThat(admitted + refused)
                    .as("every reply admitted or refused: %d admitted, %d refused", admitted, refused)
                    .isEqualTo(REQUESTS);

            // SL-1 E4: every admitted reply names a record; the store holds exactly those
            assertThat(after.reservations()).as("the store holds exactly the admitted reservations: %s", after)
                    .isEqualTo(admittedIds.size());
            for (UUID id : admittedIds) {
                assertThat(Witness.holdsReservation(id)).as("reservation %s is in the store", id).isTrue();
            }

            // in every readable state: the sampler saw no violation while the storm ran
            assertThat(samples).as("the sampler read the store during the storm").isNotEmpty();
            assertThat(Witness.violations(samples)).as("every sampled state satisfies the invariant").isEmpty();
        } finally {
            stormOver.set(true);
            pool.shutdownNow();
        }
    }
}
