package io.github.edgaras87.neveroversold.testsupport;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

/**
 * Database-only test base: one real PostgreSQL container per test JVM,
 * started once and shared; its lifecycle is the harness's own, outside
 * any framework-managed teardown.
 *
 * <p>The container is a faithful miniature of the ground: the same image
 * major as the compose ground, initialized by the same bootstrap SQL file
 * (single source: {@code infrastructure/postgres/init/bootstrap.sql}), so
 * the role split and the grant boundaries hold in evidence runs exactly
 * as they do on the ground. Migrations run harness-side, immediately after
 * the container starts and before any application context boots, as
 * {@code migrator}, from the one migrations home on the filesystem —
 * never a classpath copy. The application context then connects as
 * {@code runtime} alone, mirroring the contract's identity rule.
 *
 * <p>The local-dev passwords are the ground's own coupling
 * ({@code bootstrap.sql} ↔ {@code .env.example}) carried into the harness,
 * which is what keeps the suite self-contained: nothing exported, no
 * ground up.
 */
@SpringBootTest
public abstract class DatabaseIT {

    private static final String GROUND_BOOTSTRAP_SQL =
            "infrastructure/postgres/init/bootstrap.sql";
    private static final String MIGRATIONS_HOME =
            "filesystem:infrastructure/flyway/migrations";

    private static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer(DockerImageName.parse("postgres:17"))
                    .withDatabaseName("never_oversold")
                    .withUsername("postgres")
                    .withPassword("postgres_localdev")
                    .withCopyFileToContainer(
                            MountableFile.forHostPath(GROUND_BOOTSTRAP_SQL),
                            "/docker-entrypoint-initdb.d/bootstrap.sql");

    static {
        POSTGRES.start();
        Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), "migrator", "migrator_localdev")
                .schemas("never_oversold")
                .locations(MIGRATIONS_HOME)
                // a clone without the home must fail here, loudly, not pass
                // a suite that cannot migrate
                .failOnMissingLocations(true)
                .load()
                .migrate();
    }

    /** The store this JVM's tests share; the evidence reads its witness here. */
    protected static String jdbcUrl() {
        return POSTGRES.getJdbcUrl();
    }

    @DynamicPropertySource
    static void datasourceAsRuntimeIdentity(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "runtime");
        registry.add("spring.datasource.password", () -> "runtime_localdev");
    }
}
