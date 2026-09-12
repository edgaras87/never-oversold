package io.github.edgaras87.neveroversold.testsupport;

import org.flywaydb.core.Flyway;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

/**
 * The harness's own store: one real PostgreSQL container per test JVM,
 * started once on first use and shared by every test in the JVM — those
 * that boot an application context and those that fork instances alike.
 * Its lifecycle is the harness's own, outside any framework-managed
 * teardown; the library's reaper removes it when the JVM exits.
 *
 * <p>A faithful miniature of the ground: the same image major as the
 * compose ground, initialized by the same bootstrap SQL file (single
 * source: {@code infrastructure/postgres/init/bootstrap.sql}), so the role
 * split and the grant boundaries hold in evidence runs exactly as they do
 * on the ground. Migrations run harness-side, immediately after the
 * container starts and before anything connects, as {@code migrator},
 * from the one migrations home on the filesystem — never a classpath
 * copy.
 *
 * <p>The local-dev passwords are the ground's own coupling
 * ({@code bootstrap.sql} ↔ {@code .env.example}) carried into the harness,
 * which is what keeps the suite self-contained: nothing exported, no
 * ground up.
 */
public final class ThrowawayStore {

    private static final String GROUND_BOOTSTRAP_SQL =
            "infrastructure/postgres/init/bootstrap.sql";
    private static final String MIGRATIONS_HOME =
            "filesystem:infrastructure/flyway/migrations";

    public static final String RUNTIME_IDENTITY = "runtime";
    public static final String RUNTIME_PASSWORD = "runtime_localdev";

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

    private ThrowawayStore() {
    }

    /** The JDBC URL of the store; the evidence reads its witness here. */
    public static String jdbcUrl() {
        return POSTGRES.getJdbcUrl();
    }

    /** The host port the store is published on — what a forked instance is told. */
    public static int port() {
        return POSTGRES.getMappedPort(5432);
    }
}
