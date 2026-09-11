package io.github.edgaras87.neveroversold;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.github.edgaras87.neveroversold.testsupport.WebDatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SCAFFOLDING — dies with the probe when SL-1 lands.
 *
 * <p>The harness's cheap first check that it can drive contention end to
 * end through the real door: many requests held at a barrier, released at
 * one instant, each a full HTTP → application → real-PostgreSQL
 * round-trip, every response asserted. The release-at-one-instant pattern
 * is the shape later race evidence takes; here it is aimed at nothing.
 *
 * <p>What it proves and what it does not: it proves the harness can drive
 * simultaneity into one instance; it does not assert the requests
 * overlapped in the store, and it would pass on serialized execution.
 * Nor does it prove anything about more than one instance — that is the
 * race across processes, the evidence ADR-0009 requires. A real slice's
 * evidence asserts the witness in persisted state, never a count of
 * successful responses.
 */
class ContentionProbeIT extends WebDatabaseIT {

    private static final int CONCURRENT_REQUESTS = 100;

    @Autowired
    private TestRestTemplate door;

    @Test
    void oneHundredSimultaneousRoundTripsAllSucceed() throws Exception {
        // the pool is sized to the request count so every worker can stand
        // at the line at once; anything smaller weakens the storm
        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);
        CountDownLatch atTheLine = new CountDownLatch(CONCURRENT_REQUESTS);
        CountDownLatch release = new CountDownLatch(1);
        try {
            List<Future<ResponseEntity<String>>> responses = new ArrayList<>();
            for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
                responses.add(pool.submit(() -> {
                    atTheLine.countDown();
                    release.await();
                    return door.getForEntity("/probe/ground", String.class);
                }));
            }
            atTheLine.await();
            release.countDown();     // the one instant

            for (Future<ResponseEntity<String>> response : responses) {
                ResponseEntity<String> entity = response.get();
                assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(entity.getBody()).contains("\"identity\":\"runtime\"");
            }
        } finally {
            pool.shutdownNow();
        }
    }
}
