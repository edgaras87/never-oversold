package io.github.edgaras87.neveroversold.testsupport;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Database-only test base: an application context in this JVM over the
 * shared {@link ThrowawayStore}, connected as {@code runtime} alone —
 * the contract's identity rule, mirrored in evidence runs. Migration has
 * already run, harness-side, before this context boots: the store's own
 * initializer guarantees the order structurally.
 */
@SpringBootTest
public abstract class DatabaseIT {

    @DynamicPropertySource
    static void datasourceAsRuntimeIdentity(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ThrowawayStore::jdbcUrl);
        registry.add("spring.datasource.username", () -> ThrowawayStore.RUNTIME_IDENTITY);
        registry.add("spring.datasource.password", () -> ThrowawayStore.RUNTIME_PASSWORD);
    }
}
