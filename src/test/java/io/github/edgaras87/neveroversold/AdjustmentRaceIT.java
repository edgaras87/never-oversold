package io.github.edgaras87.neveroversold;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * E3 — the race with a different partner (F10, kill 5): reserve requests
 * and an operator's downward adjustment on one item released at one
 * instant, repeated over fresh items. The witness (§1) is sampled while
 * each round runs and read after. Beyond the invariant, the adjustment's
 * answer must agree with the state: admitted means the count is the new
 * value with the held units under it; refused means the count is still
 * the old one. Which outcome a round gets is the race's to decide (W3:
 * who wins is not ours); that neither outcome ever leaves the invariant
 * broken is.
 */
class AdjustmentRaceIT extends WebDatabaseIT {

    private static final int ROUNDS = 5;
    private static final int ON_HAND = 20;
    private static final int LOWERED_TO = 10;
    private static final int RESERVES = 30;

    @Autowired
    private TestRestTemplate door;

    @Test
    void aDownwardAdjustmentRacingTheAdmitsNeverLeavesTheCountUnderTheHeldUnits() throws Exception {
        for (int round = 1; round <= ROUNDS; round++) {
            oneRound(round);
        }
    }

    private void oneRound(int round) throws Exception {
        String item = "adjust-race-" + round + "-" + UUID.randomUUID();
        assertThat(door.postForEntity("/items/" + item + "/adjustments",
                Map.of("onHandCount", ON_HAND), String.class).getStatusCode()).isEqualTo(HttpStatus.OK);

        int racers = RESERVES + 1;
        ExecutorService pool = Executors.newFixedThreadPool(racers);
        CountDownLatch atTheLine = new CountDownLatch(racers);
        CountDownLatch release = new CountDownLatch(1);
        AtomicBoolean over = new AtomicBoolean();
        List<Witness.Numbers> samples = new ArrayList<>();
        Thread sampler = Witness.sampleUntil(item, over, samples);
        try {
            List<Future<ResponseEntity<String>>> reserves = new ArrayList<>();
            for (int i = 0; i < RESERVES; i++) {
                reserves.add(pool.submit(() -> {
                    atTheLine.countDown();
                    release.await();
                    return door.postForEntity("/items/" + item + "/reservations",
                            Map.of("quantity", 1, "hold", "PT10M"), String.class);
                }));
            }
            Future<ResponseEntity<String>> adjustment = pool.submit(() -> {
                atTheLine.countDown();
                release.await();
                return door.postForEntity("/items/" + item + "/adjustments",
                        Map.of("onHandCount", LOWERED_TO), String.class);
            });
            atTheLine.await();
            release.countDown();   // the one instant: thirty admits and one correction

            int admitted = 0;
            for (Future<ResponseEntity<String>> reply : reserves) {
                HttpStatus status = (HttpStatus) reply.get().getStatusCode();
                assertThat(status).as("round %d: a reserve is admitted or refused", round)
                        .isIn(HttpStatus.CREATED, HttpStatus.CONFLICT);
                if (status == HttpStatus.CREATED) {
                    admitted++;
                }
            }
            ResponseEntity<String> corrected = adjustment.get();
            over.set(true);
            sampler.join();

            Witness.Numbers after = Witness.read(item);
            System.out.printf("AdjustmentRaceIT round %d: correction %s, admitted %d, after %s%n",
                    round, corrected.getStatusCode(), admitted, after);
            assertThat(after.activeSum()).as("round %d: active sum ≤ on-hand-count", round)
                    .isLessThanOrEqualTo(after.onHandCount());
            assertThat(after.held()).as("round %d: held ≤ on-hand-count", round)
                    .isLessThanOrEqualTo(after.onHandCount());
            assertThat(admitted).as("round %d: admitted replies account for the units held", round)
                    .isEqualTo(after.held());

            // the adjustment's answer agrees with the state
            if (corrected.getStatusCode() == HttpStatus.OK) {
                assertThat(after.onHandCount()).as("round %d: admitted correction set the count", round)
                        .isEqualTo(LOWERED_TO);
                assertThat(after.held()).as("round %d: the held units sit under the new count", round)
                        .isLessThanOrEqualTo(LOWERED_TO);
            } else {
                assertThat(corrected.getStatusCode()).as("round %d: a correction is admitted or refused", round)
                        .isEqualTo(HttpStatus.CONFLICT);
                assertThat(after.onHandCount()).as("round %d: refused correction left the count", round)
                        .isEqualTo(ON_HAND);
            }

            assertThat(samples).as("round %d: the sampler read during the race", round).isNotEmpty();
            assertThat(Witness.violations(samples)).as("round %d: every sampled state satisfies the invariant", round)
                    .isEmpty();
        } finally {
            over.set(true);
            pool.shutdownNow();
        }
    }
}
