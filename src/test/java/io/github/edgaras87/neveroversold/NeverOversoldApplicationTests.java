package io.github.edgaras87.neveroversold;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * The application context assembles. No store is involved: this is the
 * cheapest proof that the skeleton is wired, and it stays green with the
 * ground down — the pool connects lazily, so only health would notice.
 * The password placeholder must still resolve for the context to start;
 * the value is unused because nothing here connects.
 */
@SpringBootTest(properties = "NEVER_OVERSOLD_RUNTIME_PASSWORD=unused-nothing-connects")
class NeverOversoldApplicationTests {

    @Test
    void contextLoads() {
    }
}
