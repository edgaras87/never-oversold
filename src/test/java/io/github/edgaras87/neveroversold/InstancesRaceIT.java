package io.github.edgaras87.neveroversold;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.edgaras87.neveroversold.testsupport.ForkedLedger;
import io.github.edgaras87.neveroversold.testsupport.ThrowawayStore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SCAFFOLDING — dies with the probe when SL-1 lands; the machinery it
 * proves stays.
 *
 * <p>The evidence ADR-0009 requires, aimed at nothing yet: more than one
 * instance of the ledger, each its own operating-system process, all
 * against one store; requests held at a barrier and released at one
 * instant across all of them, every response asserted; and the witness
 * read from the store directly, never through an instance. An in-process
 * lock cannot make this pass — only the store, or a protocol every
 * process honours, can.
 *
 * <p>What it proves and what it does not: it proves the harness can start,
 * address, race and stop real instances, and can read the store beside
 * them; it does not assert that requests overlapped in the store, and
 * would pass on serialized execution. That is the correct claim for a
 * machinery proof. A slice's evidence asserts the witness in persisted
 * state — the violation that must never be there.
 */
class InstancesRaceIT {

    private static final int INSTANCES = 3;
    private static final int CONCURRENT_REQUESTS = 120;
    private static final Pattern PID = Pattern.compile("\"pid\":\"(\\d+)\"");

    @Test
    void requestsReleasedAtOneInstantAcrossInstancesAllSucceed() throws Exception {
        List<ForkedLedger> ledgers = new ArrayList<>();
        try {
            for (int i = 0; i < INSTANCES; i++) {
                ledgers.add(ForkedLedger.start(ThrowawayStore.port()));
            }

            Set<Long> served = race(ledgers);

            // every instance served, and nothing but the instances did
            Set<Long> started = new HashSet<>();
            for (ForkedLedger ledger : ledgers) {
                started.add(ledger.pid());
            }
            assertThat(served).containsExactlyInAnyOrderElementsOf(started);

            // the witness path: the store read directly, as runtime, from
            // outside every instance — while the instances are still up
            try (Connection store = DriverManager.getConnection(ThrowawayStore.jdbcUrl(),
                    ThrowawayStore.RUNTIME_IDENTITY, ThrowawayStore.RUNTIME_PASSWORD);
                 Statement query = store.createStatement();
                 ResultSet row = query.executeQuery("""
                         SELECT count(DISTINCT client_port)
                         FROM pg_stat_activity
                         WHERE usename = 'runtime' AND pid <> pg_backend_pid()
                         """)) {
                assertThat(row.next()).isTrue();
                assertThat(row.getInt(1))
                        .as("distinct client connections as runtime, one or more per instance")
                        .isGreaterThanOrEqualTo(INSTANCES);
            }
        } finally {
            for (ForkedLedger ledger : ledgers) {
                ledger.close();
            }
        }
    }

    /** Releases the requests at one instant, round-robin across the instances; returns the pids that served them. */
    private static Set<Long> race(List<ForkedLedger> ledgers) throws Exception {
        HttpClient http = HttpClient.newHttpClient();
        // the pool is sized to the request count so every worker can stand
        // at the line at once; anything smaller weakens the storm
        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);
        CountDownLatch atTheLine = new CountDownLatch(CONCURRENT_REQUESTS);
        CountDownLatch release = new CountDownLatch(1);
        try {
            List<Future<HttpResponse<String>>> responses = new ArrayList<>();
            for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
                ForkedLedger target = ledgers.get(i % ledgers.size());
                HttpRequest probe = HttpRequest.newBuilder(URI.create(target.baseUrl() + "/probe/ground")).build();
                responses.add(pool.submit(() -> {
                    atTheLine.countDown();
                    release.await();
                    return http.send(probe, HttpResponse.BodyHandlers.ofString());
                }));
            }
            atTheLine.await();
            release.countDown();     // the one instant

            Set<Long> served = new HashSet<>();
            for (Future<HttpResponse<String>> response : responses) {
                HttpResponse<String> entity = response.get();
                assertThat(entity.statusCode()).isEqualTo(200);
                assertThat(entity.body()).contains("\"identity\":\"runtime\"");
                Matcher pid = PID.matcher(entity.body());
                assertThat(pid.find()).isTrue();
                served.add(Long.parseLong(pid.group(1)));
            }
            return served;
        } finally {
            pool.shutdownNow();
        }
    }
}
