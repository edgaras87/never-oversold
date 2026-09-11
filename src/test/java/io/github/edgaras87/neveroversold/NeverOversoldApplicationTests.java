package io.github.edgaras87.neveroversold;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * The application context assembles. No store is involved: this is the
 * cheapest proof that the skeleton is wired, and it stays green with the
 * ground down and nothing exported — the pool connects lazily, and Boot's
 * configuration binding leaves an unresolvable placeholder as a literal,
 * so a missing password shows only in health, never here.
 */
@SpringBootTest
class NeverOversoldApplicationTests {

    @Test
    void contextLoads() {
    }
}
