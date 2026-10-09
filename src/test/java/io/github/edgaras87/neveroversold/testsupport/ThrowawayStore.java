package io.github.edgaras87.neveroversold.testsupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

import org.flywaydb.core.Flyway;
import org.testcontainers.DockerClientFactory;
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
 *
 * <p>For SL-4's interruptions the harness also acts on the store itself,
 * as its superuser — trusted under the definition's T4, and writing no
 * data: it freezes and thaws the container, ends one session, and sees
 * when a session has left.
 */
public final class ThrowawayStore {

    private static final String GROUND_BOOTSTRAP_SQL =
            "infrastructure/postgres/init/bootstrap.sql";
    private static final String MIGRATIONS_HOME =
            "filesystem:infrastructure/flyway/migrations";

    public static final String RUNTIME_IDENTITY = "runtime";
    public static final String RUNTIME_PASSWORD = "runtime_localdev";
    private static final String SUPERUSER = "postgres";
    private static final String SUPERUSER_PASSWORD = "postgres_localdev";

    private static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer(DockerImageName.parse("postgres:17"))
                    .withDatabaseName("never_oversold")
                    .withUsername(SUPERUSER)
                    .withPassword(SUPERUSER_PASSWORD)
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

    /**
     * A connection as the store's superuser, for the harness's own acts on
     * the store. The ledger's schema is named here: the ground pins it for
     * {@code runtime} alone, and the superuser would not find the tables.
     */
    static Connection superuser() throws SQLException {
        Properties login = new Properties();
        login.setProperty("user", SUPERUSER);
        login.setProperty("password", SUPERUSER_PASSWORD);
        login.setProperty("currentSchema", "never_oversold");
        return DriverManager.getConnection(jdbcUrl(), login);
    }

    /**
     * Freezes the store: its container paused, every process in it
     * stopped mid-step (ADR-0004's way to make a write's outcome
     * unknowable). Nothing it holds is lost or undone; it answers no one
     * until {@link #thaw()}.
     */
    public static void freeze() {
        DockerClientFactory.instance().client().pauseContainerCmd(POSTGRES.getContainerId()).exec();
    }

    /** Lets a frozen store go on from exactly where it stopped. */
    public static void thaw() {
        DockerClientFactory.instance().client().unpauseContainerCmd(POSTGRES.getContainerId()).exec();
    }

    /**
     * Ends one session at the store, as the superuser — the instance on
     * the other end loses the store mid-request, and the store undoes
     * whatever that session had not committed.
     */
    public static void endSession(int pid) {
        try (Connection store = superuser();
             PreparedStatement end = store.prepareStatement("SELECT pg_terminate_backend(?)")) {
            end.setInt(1, pid);
            end.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("could not end session " + pid, e);
        }
    }

    /**
     * Whether the store answers a fresh connection and one query within
     * {@code seconds} — false for a frozen store, which accepts nothing.
     */
    public static boolean answersWithin(int seconds) {
        Properties login = new Properties();
        login.setProperty("user", SUPERUSER);
        login.setProperty("password", SUPERUSER_PASSWORD);
        login.setProperty("connectTimeout", Integer.toString(seconds));
        login.setProperty("socketTimeout", Integer.toString(seconds));
        login.setProperty("loginTimeout", Integer.toString(seconds));
        try (Connection store = DriverManager.getConnection(jdbcUrl(), login);
             PreparedStatement statement = store.prepareStatement("SELECT 1");
             ResultSet row = statement.executeQuery()) {
            return row.next();
        } catch (SQLException noAnswer) {
            return false;
        }
    }

    /** Whether the store still has a session with this process id. */
    public static boolean hasSession(int pid) {
        try (Connection store = superuser();
             PreparedStatement statement = store.prepareStatement(
                     "SELECT 1 FROM pg_stat_activity WHERE pid = ?")) {
            statement.setInt(1, pid);
            try (ResultSet row = statement.executeQuery()) {
                return row.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("could not read the store's sessions", e);
        }
    }

    /** Waits until the store has no session with this process id, reading its own list. */
    public static void awaitSessionGone(int pid, Duration timeout) throws InterruptedException {
        Instant deadline = Instant.now().plus(timeout);
        while (hasSession(pid)) {
            if (Instant.now().isAfter(deadline)) {
                throw new IllegalStateException("session " + pid + " still at the store after " + timeout);
            }
            Thread.sleep(10);
        }
    }
}
