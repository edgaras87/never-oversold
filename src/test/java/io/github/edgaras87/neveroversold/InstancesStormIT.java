package io.github.edgaras87.neveroversold;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.edgaras87.neveroversold.testsupport.ForkedLedger;
import io.github.edgaras87.neveroversold.testsupport.ThrowawayStore;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import io.github.edgaras87.neveroversold.testsupport.Body;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * E2 — the storm across instances (ADR-0009): three instances of the
 * ledger as separate operating-system processes against one store, a
 * hundred and twenty reserve requests spread round-robin across their
 * doors and released at one instant, against an item holding twenty
 * units. No application context runs in this test; the witness is read
 * by none of the instances, from outside all of them. Every instance is
 * shown to have answered its share, so the storm did cross processes.
 *
 * <p>What this proves that {@link ReserveStormIT} cannot: no belief an
 * instance holds in memory can save the invariant — the racers do not
 * share a process (G2, F17).
 */
class InstancesStormIT {

    private static final int INSTANCES = 3;
    private static final int ON_HAND = 20;
    private static final int REQUESTS = 120;

    @Test
    void threeInstancesRacingForTheLastUnitsNeverOversell() throws Exception {
        List<ForkedLedger> ledgers = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(REQUESTS);
        AtomicBoolean stormOver = new AtomicBoolean();
        String item = "instances-" + UUID.randomUUID();
        List<Witness.Numbers> samples = new ArrayList<>();
        Thread sampler = null;
        try {
            for (int i = 0; i < INSTANCES; i++) {
                ledgers.add(ForkedLedger.start(ThrowawayStore.port()));
            }
            HttpClient http = HttpClient.newHttpClient();
            HttpResponse<String> created = http.send(post(ledgers.get(0).baseUrl() + "/items/" + item + "/adjustments",
                    "{\"onHandCount\":" + ON_HAND + "}"), HttpResponse.BodyHandlers.ofString());
            assertThat(created.statusCode()).isEqualTo(200);

            sampler = Witness.sampleUntil(item, stormOver, samples);
            CountDownLatch atTheLine = new CountDownLatch(REQUESTS);
            CountDownLatch release = new CountDownLatch(1);
            List<Future<HttpResponse<String>>> replies = new ArrayList<>();
            int[] sentTo = new int[INSTANCES];
            for (int i = 0; i < REQUESTS; i++) {
                ForkedLedger target = ledgers.get(i % INSTANCES);
                sentTo[i % INSTANCES]++;
                HttpRequest reserve = post(target.baseUrl() + "/items/" + item + "/reservations",
                        "{\"quantity\":1,\"hold\":\"PT10M\"}");
                replies.add(pool.submit(() -> {
                    atTheLine.countDown();
                    release.await();
                    return http.send(reserve, HttpResponse.BodyHandlers.ofString());
                }));
            }
            atTheLine.await();
            release.countDown();   // the one instant, across all three doors

            int admitted = 0;
            int refused = 0;
            int[] answeredBy = new int[INSTANCES];
            List<UUID> admittedIds = new ArrayList<>();
            for (int i = 0; i < REQUESTS; i++) {
                HttpResponse<String> response = replies.get(i).get();
                answeredBy[i % INSTANCES]++;
                if (response.statusCode() == 201) {
                    admitted++;
                    admittedIds.add(Body.of(response.body()).uuidAt("$.id"));
                } else if (response.statusCode() == 409) {
                    refused++;
                } else {
                    throw new AssertionError("a reply that is neither admitted nor refused: "
                            + response.statusCode() + " " + response.body());
                }
            }
            stormOver.set(true);
            sampler.join();

            // the storm crossed processes: each instance answered every request sent to it
            for (int i = 0; i < INSTANCES; i++) {
                assertThat(answeredBy[i]).as("instance %d (pid %d) answered its share", i, ledgers.get(i).pid())
                        .isEqualTo(sentTo[i]);
            }
            assertThat(ledgers.stream().map(ForkedLedger::pid).distinct().count()).isEqualTo(INSTANCES);

            // the storm was real
            assertThat(refused).as("some were refused").isGreaterThan(0);

            // the witness, read by none of them
            Witness.Numbers after = Witness.read(item);
            assertThat(after.activeSum()).as("active sum ≤ on-hand-count").isLessThanOrEqualTo(after.onHandCount());
            assertThat(after.held()).as("held ≤ on-hand-count").isLessThanOrEqualTo(after.onHandCount());
            assertThat(admitted).isEqualTo(after.held());
            assertThat(after.reservations()).isEqualTo(admittedIds.size());
            for (UUID id : admittedIds) {
                assertThat(Witness.holdsReservation(id)).isTrue();
            }
            assertThat(samples).isNotEmpty();
            assertThat(Witness.violations(samples)).as("every sampled state satisfies the invariant").isEmpty();
        } finally {
            stormOver.set(true);
            pool.shutdownNow();
            for (ForkedLedger ledger : ledgers) {
                ledger.close();
            }
        }
    }

    private static HttpRequest post(String url, String json) {
        return HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }
}
