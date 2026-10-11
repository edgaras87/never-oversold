package io.github.edgaras87.neveroversold;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.edgaras87.neveroversold.testsupport.Body;
import io.github.edgaras87.neveroversold.testsupport.ForkedLedger;
import io.github.edgaras87.neveroversold.testsupport.ThrowawayStore;
import io.github.edgaras87.neveroversold.testsupport.WebDatabaseIT;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SL-3's evidence under contention, E1, E2 and E6 (the slice record:
 * {@code docs/construction/sl-3-a-reservation-exits-once.md}). The
 * sequential cases are {@code ExitDoorIT}'s; here exits race — with each
 * other on one reservation, in one instance and across instances, with
 * time itself at the expiry instant, and with the tidy that every
 * reserve runs first.
 *
 * <p>Replies are not counted as evidence: under ADR-0013 a repeated exit
 * of the same kind also reads {@code 200}, so only the store can say how
 * many took effect. The witness is read first, sampled while the storm
 * runs and read after; the replies are checked last, for what they say
 * about the state.
 *
 * <p>E1's and E6's numbers start from the record's own example (§4):
 * one item, 10 on hand; reservation R holds 3, reservation S holds 5.
 */
class ExitStormIT extends WebDatabaseIT {

    private static final int RACERS = 50;
    private static final int INSTANCES = 3;

    @Autowired
    private TestRestTemplate door;

    /**
     * E1 · G1 — kill 13, consume × consume.
     *
     * <p>Fifty consumes on R, held at a line and released at one instant.
     * Each checks that R is still active and each would see that it is.
     * Exactly one may take effect: R ends once, by consume, and the count
     * falls by R's 3 once — 7 on hand, 5 held. This fails if an exit ever
     * checks R in one step and ends it in another: two then both land, and
     * the count falls by 6.
     */
    @Test
    void racingConsumesEndAReservationOnce() throws Exception {
        Scene scene = theRecordsExample();

        Storm storm = storm(scene.item(), exitsInProcess(scene.r(), RACERS, 0));

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(Witness.receiptsOf(scene.r())).as("R ended once, by consume").containsExactly("consumed");
        assertThat(after.onHandCount()).as("R's 3 left the shelf once: %s", after).isEqualTo(7);
        assertThat(after.held()).as("R's 3 no longer held, once: %s", after).isEqualTo(5);
        assertThat(Witness.violations(storm.samples())).as("every sampled state").isEmpty();

        assertThat(storm.statuses()).as("every consume reads as the one that ended R (ADR-0013)")
                .containsOnly(200).hasSize(RACERS);
    }

    /**
     * E1 · G1 — kill 13, consume × release (F4).
     *
     * <p>Twenty-five consumes and twenty-five releases on R, interleaved,
     * released at one instant. One kind wins: R's receipt says which, and
     * the numbers agree with that kind and no other — 7 on hand if consume
     * won, 10 if release did, 5 held either way. This fails if both kinds
     * ever land: the units would leave the shelf and be freed as well, and
     * the held units would fall below what S still holds.
     */
    @Test
    void racingConsumesAndReleasesEndAReservationOnce() throws Exception {
        Scene scene = theRecordsExample();

        Storm storm = storm(scene.item(), exitsInProcess(scene.r(), RACERS / 2, RACERS / 2));

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        List<String> receipts = Witness.receiptsOf(scene.r());
        assertThat(receipts).as("R ended once, by one kind").hasSize(1);
        String won = receipts.getFirst();
        assertThat(after.onHandCount()).as("the count agrees with %s and no other: %s", won, after)
                .isEqualTo(won.equals("consumed") ? 7 : 10);
        assertThat(after.held()).as("R's 3 no longer held, once: %s", after).isEqualTo(5);
        assertThat(Witness.violations(storm.samples())).as("every sampled state").isEmpty();

        assertThat(storm.statuses()).containsOnly(200, 409);
        assertThat(storm.statuses().stream().filter(status -> status == 200).count())
                .as("the winning kind's exits read 200, the other kind's 409")
                .isEqualTo(RACERS / 2);
    }

    /**
     * E1 · G1 — kills 13 and 14 across instances (F17).
     *
     * <p>The same race split across three instances of the ledger, each
     * its own operating-system process against the one store: sixty
     * exits, consumes and releases, sent round-robin to their doors and
     * released at one instant. No instance can see what another decided;
     * only the store can make one of them win. This fails if an exit ever
     * relies on anything an instance keeps in its own memory.
     */
    @Test
    void racingExitsAcrossInstancesEndAReservationOnce() throws Exception {
        List<ForkedLedger> ledgers = new ArrayList<>();
        try {
            for (int i = 0; i < INSTANCES; i++) {
                ledgers.add(ForkedLedger.start(ThrowawayStore.port()));
            }
            // the race will cross processes
            List<Long> pids = ledgers.stream().map(ForkedLedger::pid).toList();
            assertThat(pids).as("three processes, not one reused: %s", pids)
                    .doesNotHaveDuplicates().hasSize(INSTANCES);
            Scene scene = theRecordsExample();
            int exits = 60;
            HttpClient http = HttpClient.newHttpClient();
            List<Callable<Integer>> racers = new ArrayList<>();
            for (int i = 0; i < exits; i++) {
                String kind = i % 2 == 0 ? "consume" : "release";
                HttpRequest exit = HttpRequest.newBuilder(URI.create(
                                ledgers.get(i % INSTANCES).baseUrl() + "/reservations/" + scene.r() + "/" + kind))
                        .POST(HttpRequest.BodyPublishers.noBody()).build();
                racers.add(() -> http.send(exit, HttpResponse.BodyHandlers.ofString()).statusCode());
            }

            Storm storm = storm(scene.item(), racers);

            Witness.Numbers after = Witness.read(scene.item());
            assertThat(after.holds()).as("the invariant: %s", after).isTrue();
            List<String> receipts = Witness.receiptsOf(scene.r());
            assertThat(receipts).as("R ended once, by one kind, whichever instance").hasSize(1);
            assertThat(after.onHandCount()).as("the count agrees with %s and no other: %s", receipts, after)
                    .isEqualTo(receipts.getFirst().equals("consumed") ? 7 : 10);
            assertThat(after.held()).as("R's 3 no longer held, once: %s", after).isEqualTo(5);
            assertThat(Witness.violations(storm.samples())).as("every sampled state").isEmpty();
            assertThat(storm.statuses()).containsOnly(200, 409).hasSize(exits);
        } finally {
            for (ForkedLedger ledger : ledgers) {
                ledger.close();
            }
        }
    }

    /**
     * E2 · G1, with time — kill 14, consume × expiry (F23): kill 13 with
     * time as one racer.
     *
     * <p>Forty holds of one unit for one second, the shortest the door
     * allows, and a consume for each fired at its own expiry instant, give
     * or take a tenth of a second — some just before, some just after.
     * Each hold ends once: by consume, strictly before its instant by the
     * store's clock, or by expiry, at it. The count falls by exactly the
     * consumed units and nothing stays held. This fails if a consume ever
     * lands at or after the instant, or a hold ever ends both ways.
     */
    @Test
    void consumesAroundTheExpiryInstantEndEachHoldOnce() throws Exception {
        int holds = 40;
        String item = newItem(100);
        List<UUID> ids = new ArrayList<>();
        List<Instant> expiries = new ArrayList<>();
        for (int i = 0; i < holds; i++) {
            Body body = Body.of(reserve(item, 1, "PT1S").getBody());
            ids.add(body.uuidAt("$.id"));
            expiries.add(OffsetDateTime.parse(body.stringAt("$.expiresAt")).toInstant());
        }

        AtomicBoolean over = new AtomicBoolean();
        List<Witness.Numbers> samples = new ArrayList<>();
        Thread sampler = Witness.sampleUntil(item, over, samples);
        ScheduledExecutorService clock = Executors.newScheduledThreadPool(holds);
        List<Integer> statuses = new ArrayList<>();
        try {
            List<Future<Integer>> replies = new ArrayList<>();
            for (int i = 0; i < holds; i++) {
                UUID reservation = ids.get(i);
                // spread evenly from 100 ms before the instant to 100 ms after
                long offsetMillis = -100 + (200L * i) / (holds - 1);
                long delay = Duration.between(Instant.now(), expiries.get(i)).toMillis() + offsetMillis;
                replies.add(clock.schedule(() -> consume(reservation).getStatusCode().value(),
                        Math.max(0, delay), TimeUnit.MILLISECONDS));
            }
            for (Future<Integer> reply : replies) {
                statuses.add(reply.get());
            }
        } finally {
            over.set(true);
            sampler.join();
            clock.shutdownNow();
        }

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        List<Witness.Ending> endings = Witness.endingsOf(item);
        assertThat(endings).as("one receipt per hold, none without").hasSize(holds)
                .allSatisfy(ending -> assertThat(ending.kind()).isIn("consumed", "expired"));
        assertThat(endings).filteredOn(ending -> ending.kind().equals("consumed"))
                .as("every consume landed strictly before its instant, by the store's clock")
                .allSatisfy(ending -> assertThat(ending.beforeExpiry()).isTrue());
        long consumed = endings.stream().filter(ending -> ending.kind().equals("consumed")).count();
        long expired = holds - consumed;
        assertThat(after.onHandCount()).as("the count fell by the consumed units exactly: %s", after)
                .isEqualTo(100 - consumed);
        assertThat(after.held()).as("nothing stays held: %s", after).isZero();
        assertThat(Witness.violations(samples)).as("every sampled state").isEmpty();

        // the race was real: both sides of the instant were hit
        assertThat(consumed).as("some consumes landed before the instant").isPositive();
        assertThat(expired).as("some met the expiry").isPositive();

        assertThat(statuses.stream().filter(status -> status == 200).count()).isEqualTo(consumed);
        assertThat(statuses.stream().filter(status -> status == 409).count()).isEqualTo(expired);
    }

    /**
     * E2 · G1, with time and tidy — consume × expiry by tidy.
     *
     * <p>E2 again, with a second racer at each instant: beside each
     * consume, at the same moment, a reserve for one unit on the same
     * item, whose tidy writes an {@code expired} receipt for every hold
     * that has run out. At the instant a consume and a tidy meet the same
     * hold: the consume writes {@code consumed}, the tidy writes
     * {@code expired}, and only one may land. Each hold ends once; the
     * count falls by exactly the consumed units; the forty new holds are
     * all admitted and are all that stays held. This fails if a hold ever
     * ends both ways: its unit would leave the shelf and be freed as well,
     * and the units held would fall below the new holds.
     */
    @Test
    void consumesAndTidiesAroundTheExpiryInstantEndEachHoldOnce() throws Exception {
        int holds = 40;
        String item = newItem(100);
        List<UUID> ids = new ArrayList<>();
        List<Instant> expiries = new ArrayList<>();
        for (int i = 0; i < holds; i++) {
            Body body = Body.of(reserve(item, 1, "PT1S").getBody());
            ids.add(body.uuidAt("$.id"));
            expiries.add(OffsetDateTime.parse(body.stringAt("$.expiresAt")).toInstant());
        }

        AtomicBoolean over = new AtomicBoolean();
        List<Witness.Numbers> samples = new ArrayList<>();
        Thread sampler = Witness.sampleUntil(item, over, samples);
        ScheduledExecutorService clock = Executors.newScheduledThreadPool(2 * holds);
        List<Integer> consumeStatuses = new ArrayList<>();
        List<Integer> reserveStatuses = new ArrayList<>();
        try {
            List<Future<Integer>> consumes = new ArrayList<>();
            List<Future<Integer>> reserves = new ArrayList<>();
            for (int i = 0; i < holds; i++) {
                UUID reservation = ids.get(i);
                // spread evenly from 100 ms before the instant to 100 ms after
                long offsetMillis = -100 + (200L * i) / (holds - 1);
                long delay = Math.max(0,
                        Duration.between(Instant.now(), expiries.get(i)).toMillis() + offsetMillis);
                consumes.add(clock.schedule(() -> consume(reservation).getStatusCode().value(),
                        delay, TimeUnit.MILLISECONDS));
                reserves.add(clock.schedule(() -> tryReserve(item, 1, "PT15M").getStatusCode().value(),
                        delay, TimeUnit.MILLISECONDS));
            }
            for (Future<Integer> reply : consumes) {
                consumeStatuses.add(reply.get());
            }
            for (Future<Integer> reply : reserves) {
                reserveStatuses.add(reply.get());
            }
        } finally {
            over.set(true);
            sampler.join();
            clock.shutdownNow();
        }

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        Set<UUID> shortHolds = Set.copyOf(ids);
        List<Witness.Ending> endings = Witness.endingsOf(item).stream()
                .filter(ending -> shortHolds.contains(ending.reservation()))
                .toList();
        assertThat(endings).as("one receipt per short hold, none without").hasSize(holds)
                .allSatisfy(ending -> assertThat(ending.kind()).isIn("consumed", "expired"));
        assertThat(endings).filteredOn(ending -> ending.kind().equals("consumed"))
                .as("every consume landed strictly before its instant, by the store's clock")
                .allSatisfy(ending -> assertThat(ending.beforeExpiry()).isTrue());
        long consumed = endings.stream().filter(ending -> ending.kind().equals("consumed")).count();
        long expired = holds - consumed;
        assertThat(after.onHandCount()).as("the count fell by the consumed units exactly: %s", after)
                .isEqualTo(100 - consumed);
        assertThat(after.held()).as("the %s new holds and nothing more: %s", holds, after).isEqualTo(holds);
        assertThat(after.activeSum()).as("the new holds, all active: %s", after).isEqualTo(holds);
        assertThat(Witness.violations(samples)).as("every sampled state").isEmpty();

        // the race was real: both sides of the instant were hit
        assertThat(consumed).as("some consumes landed before the instant").isPositive();
        assertThat(expired).as("some met the expiry").isPositive();

        assertThat(reserveStatuses).as("every new hold fits: %s", reserveStatuses)
                .containsOnly(201).hasSize(holds);
        assertThat(consumeStatuses.stream().filter(status -> status == 200).count()).isEqualTo(consumed);
        assertThat(consumeStatuses.stream().filter(status -> status == 409).count()).isEqualTo(expired);
    }

    /**
     * E6 · G5 — expiry acting with no request of its own.
     *
     * <p>10 on hand; R holds 3 for one second, S holds 5 for fifteen
     * minutes. R runs out. Fifty reserves for one unit are held at a line
     * and released at one instant, and each one's tidy meets R. R's 3 are
     * freed once: 2 free plus R's 3, so exactly five are admitted, and the
     * item reads 10 held of 10. This fails if a tidy ever frees by a sum
     * it read: tidies that each read R before another wrote its receipt
     * all take its 3 off, and more reserves are admitted than fit.
     */
    @Test
    void racingReservesFreeAnExpiredHoldOnce() throws Exception {
        String item = newItem(10);
        UUID r = Body.of(reserve(item, 3, "PT1S").getBody()).uuidAt("$.id");
        reserve(item, 5, "PT15M");
        Thread.sleep(1_500);   // R runs out — by the store's clock, not this JVM's
        Witness.Numbers before = Witness.read(item);
        assertThat(before.held()).as("R's 3 still in the units held, until a decision meets it: %s", before)
                .isEqualTo(8);
        List<Callable<Integer>> racers = new ArrayList<>();
        for (int i = 0; i < RACERS; i++) {
            racers.add(() -> tryReserve(item, 1, "PT15M").getStatusCode().value());
        }

        Storm storm = storm(item, racers);

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(Witness.receiptsOf(r)).as("R ended once, by expiry").containsExactly("expired");
        assertThat(after.onHandCount()).as("nothing left the shelf: %s", after).isEqualTo(10);
        assertThat(after.held()).as("S's 5 and five new units, R's 3 freed once: %s", after).isEqualTo(10);
        assertThat(after.activeSum()).as("S and the five admitted: %s", after).isEqualTo(10);
        assertThat(Witness.violations(storm.samples())).as("every sampled state").isEmpty();

        assertThat(storm.statuses()).containsOnly(201, 409).hasSize(RACERS);
        assertThat(storm.statuses().stream().filter(status -> status == 201).count())
                .as("2 free and R's 3: five reserves for one fit, no more").isEqualTo(5);
    }

    /** One item, 10 on hand, R holding 3 and S holding 5. */
    private record Scene(String item, UUID r, UUID s) {
    }

    /** What a storm leaves behind: every reply's status, and every witness reading taken while it ran. */
    private record Storm(List<Integer> statuses, List<Witness.Numbers> samples) {
    }

    private Scene theRecordsExample() {
        String item = newItem(10);
        UUID r = Body.of(reserve(item, 3, "PT15M").getBody()).uuidAt("$.id");
        UUID s = Body.of(reserve(item, 5, "PT15M").getBody()).uuidAt("$.id");
        Witness.Numbers before = Witness.read(item);
        assertThat(before.onHandCount()).isEqualTo(10);
        assertThat(before.held()).isEqualTo(8);
        return new Scene(item, r, s);
    }

    /** Consumes and releases on one reservation through this instance's door, interleaved. */
    private List<Callable<Integer>> exitsInProcess(UUID reservation, int consumes, int releases) {
        List<Callable<Integer>> racers = new ArrayList<>();
        for (int i = 0; i < Math.max(consumes, releases); i++) {
            if (i < consumes) {
                racers.add(() -> consume(reservation).getStatusCode().value());
            }
            if (i < releases) {
                racers.add(() -> release(reservation).getStatusCode().value());
            }
        }
        return racers;
    }

    /**
     * Holds every racer at a line, releases them at one instant, and
     * samples the witness for the item until the last reply is in.
     */
    private Storm storm(String item, List<Callable<Integer>> racers) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(racers.size());
        CountDownLatch atTheLine = new CountDownLatch(racers.size());
        CountDownLatch go = new CountDownLatch(1);
        AtomicBoolean over = new AtomicBoolean();
        List<Witness.Numbers> samples = new ArrayList<>();
        Thread sampler = Witness.sampleUntil(item, over, samples);
        try {
            List<Future<Integer>> replies = new ArrayList<>();
            for (Callable<Integer> racer : racers) {
                replies.add(pool.submit(() -> {
                    atTheLine.countDown();
                    go.await();
                    return racer.call();
                }));
            }
            atTheLine.await();
            go.countDown();   // the one instant

            List<Integer> statuses = new ArrayList<>();
            for (Future<Integer> reply : replies) {
                statuses.add(reply.get());
            }
            over.set(true);
            sampler.join();
            assertThat(samples).as("the sampler read the store during the storm").isNotEmpty();
            return new Storm(statuses, samples);
        } finally {
            over.set(true);
            pool.shutdownNow();
        }
    }

    private String newItem(int onHand) {
        String item = "exit-storm-" + UUID.randomUUID();
        assertThat(door.postForEntity("/items/" + item + "/adjustments",
                Map.of("onHandCount", onHand), String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
        return item;
    }

    private ResponseEntity<String> reserve(String item, int quantity, String hold) {
        ResponseEntity<String> answer = tryReserve(item, quantity, hold);
        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return answer;
    }

    /** A reserve whose answer is the racer's to report, not asserted here. */
    private ResponseEntity<String> tryReserve(String item, int quantity, String hold) {
        return door.postForEntity("/items/" + item + "/reservations",
                Map.of("quantity", quantity, "hold", hold), String.class);
    }

    private ResponseEntity<String> consume(UUID reservation) {
        return door.postForEntity("/reservations/" + reservation + "/consume", null, String.class);
    }

    private ResponseEntity<String> release(UUID reservation) {
        return door.postForEntity("/reservations/" + reservation + "/release", null, String.class);
    }
}
