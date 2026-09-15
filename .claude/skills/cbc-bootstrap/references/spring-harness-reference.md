<!-- Checked against concept v1 of correctness-by-construction
     (CBC ADR-0003, CBC ADR-0005 — practice-born). Provenance — the
     workbench-era doc system-bootstrap-spring-harness.reference.md
     (ai-context-system, born from the second lived pass,
     safe-reservations), handed over 2026-08-28 as an uncommitted
     working copy. Changes on import: the old maintenance language
     and lifecycle (workbench mastering, flow-back, genre labels)
     replaced by this repo's records; cross-references renamed to
     this skill's files; run-specific citations generalized to the
     walkthroughs' placeholder notation.
     Confirmed and corrected 2026-08-28 against the third lived
     pass — checkout-system's bootstrap (commits d732b53, 83262b5),
     read read-only (CBC ADR-0007). The third pass's corrections, taken:
     the container is a faithful miniature carrying the ground's
     authority split (the second pass ran the harness as the
     Testcontainers superuser); the layering is two bases, not
     three; the migration-path test joins the set; the probe
     round-trips the identity, not `select 1`; the contention pool
     is sized to the count. The second pass's virtual-threads claim
     and failOnMissingLocations guard demoted to variation points.
     Harvested 2026-09-12 from never-oversold (run 3 of the pure
     seed) Step 4, read read-only (CBC ADR-0007): variation point 8,
     the plural-instance shape — lived once, carried as prose until
     a second run lives it. The "two bases, not three" line above
     holds for the single-process shape; when the definition names
     plural instances the store leaves the base for a holder of its
     own, and the layering is the two bases plus the holder.
     Harvested 2026-09-12, same run (CBC ADR-0007): §1's bound
     facts state the Boot 4 Flyway split — the engine alone on the
     test classpath runs no auto-configuration, so the harness's
     own call is the only migration path in tests.
     Harvested 2026-09-12, same run (CBC ADR-0007): §3 gains a
     third test, lived once — the runtime identity attempting DDL
     in the miniature, refused with the ground's own message; the
     assertion on the root cause, since Spring wraps the driver's
     error. Code, not prose: the trap is in the three lines'
     shape.
     Harvested 2026-09-14 from never-oversold (run 3 of the pure
     seed) Step 5, read read-only (CBC ADR-0007): variation point
     9 states how the evidence asserts on a body — by path for a
     shape, by type under a shared contract, never by substring;
     the probe's `.contains` line annotated as the identity
     witness, not the pattern. -->

# Spring harness reference — the recurring artifacts, as code

The evidence harness's five recurring artifacts for the *system
bootstrapped* step on the Spring line: the two test bases, the
migration-path test, and the probe pair. **Stack-scoped by name**:
Spring Boot on Maven, the Boot 4 line, Testcontainers 2.x,
PostgreSQL. Lived three times; the shape below is the third pass's,
the most corrected.

**Masters nothing.** The *outcomes* these files satisfy are
`spring-boot-walkthrough.md`'s, stages 4 and 5; what enters the build
and how each entry is written is `spring-pom-convention.md`'s; the
machine setup that makes containers reachable is the project's
operator manual's. **The run's requirements document wins over all of
them** — where it forbids something this file shows, the file is
wrong for that run.

**Why code exists here at all.** The walkthrough deliberately carries
none: two projects may end with different code for the same thing.
That holds for the application and does not hold for these five,
which are shaped by the stack and the harness outcomes rather than by
the problem — three projects produced them near-identically, and each
executor's only route was reading the previous project's source. This
file exists to make that read unnecessary. **It is a reference, not a
template** (CBC ADR-0008): imitated, never pasted-and-filled — copied
thoughtlessly it will be wrong in the variation points named at the
end.

Placeholders: `<base-package>`, `<project>` (underscored in SQL
identifiers), `<project_db>`, `<project_schema>`, and the ground's
major version.

---

## What these files are, in one line each

| File | Scope | The outcome it realizes |
|---|---|---|
| `DatabaseIT` | test | one container per test JVM as a faithful miniature of the ground; migrations harness-side as migrator; the context connects as runtime |
| `WebDatabaseIT` | test | the evidence tier: random-port HTTP server over the same database |
| `MigrationPathIT` | test | the migration path proven, not assumed; the connection identity asserted; the authority split witnessed by a refused DDL |
| probe endpoint | **main** | one identity round-trip through the real door — scaffolding, dies at the first slice |
| contention probe | test | many requests released at one instant, all asserted |

## 1 · The database base — a faithful miniature of the ground

**Outcomes it realizes** (walkthrough, stage 4): one PostgreSQL
container of the ground's major version per test JVM, started once
and shared, its lifecycle explicitly the harness's own; migration run
**harness-side, immediately after the container starts and before any
application context boots**, pointed at the **one migrations home on
the filesystem**, never a classpath copy — and, the third pass's
correction, **the ground's authority split held in evidence runs**:
the container is initialized by the SAME bootstrap SQL the ground
uses, migration runs as the migrator identity, and the application
context connects as runtime alone.

```java
package <base-package>.testsupport;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

/**
 * Database-only test base: one real PostgreSQL container per test JVM,
 * started once and shared; lifecycle is the harness's own, deliberately
 * outside any framework-managed teardown.
 *
 * The container is a faithful miniature of the ground: the same image
 * major as the established ground, initialized by the SAME bootstrap
 * SQL file (single source: infrastructure/postgres/init/), so the role
 * split and grant boundaries hold in evidence runs exactly as they do
 * on the ground. Migrations run harness-side, immediately after
 * container start and before any application context boots, as the
 * migrator identity, from the one migrations home on the filesystem —
 * never a classpath copy. The application context then connects as the
 * runtime identity alone, mirroring production authority.
 */
@SpringBootTest
public abstract class DatabaseIT {

    private static final String GROUND_BOOTSTRAP_SQL =
            "infrastructure/postgres/init/bootstrap.sql";
    private static final String MIGRATIONS_HOME =
            "filesystem:infrastructure/flyway/migrations";

    private static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer(DockerImageName.parse("postgres:17"))
                    .withDatabaseName("<project_db>")
                    .withUsername("postgres")
                    .withPassword("postgres_localdev")
                    .withCopyFileToContainer(
                            MountableFile.forHostPath(GROUND_BOOTSTRAP_SQL),
                            "/docker-entrypoint-initdb.d/bootstrap.sql");

    static {
        POSTGRES.start();
        Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(),
                        "<project>_migrator", "<project>_migrator_localdev")
                .schemas("<project_schema>")
                .locations(MIGRATIONS_HOME)
                .load()
                .migrate();
    }

    @DynamicPropertySource
    static void datasourceAsRuntimeIdentity(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "<project>_runtime");
        registry.add("spring.datasource.password", () -> "<project>_runtime_localdev");
    }
}
```

**Bound facts.**

- The **static initializer** is what "the lifecycle is the harness's
  own" means concretely: no per-class container annotation, no
  JUnit-managed restart. It runs once per test JVM, and the migration
  runs inside it — the ordering the outcome demands (migrate *then*
  boot) is structural, not a convention someone must remember.
- **The mounted bootstrap SQL is the single source.** The harness
  copies the ground's own `bootstrap.sql` into
  `docker-entrypoint-initdb.d`, so the miniature cannot drift from
  the ground's role model; the localdev literals here are the same
  coupling the establishment's `.env.example` documents.
- **The identity split is live in tests**: migrate as
  `<project>_migrator`, connect as `<project>_runtime`. A harness run
  as the container's superuser (the second pass's shape) proves the
  round-trip but not the authority — DDL attempted from application
  code would succeed in tests and fail on the ground.
- **Testcontainers 2.x names**: artifact `testcontainers-postgresql`,
  package `org.testcontainers.postgresql`. The 1.x names differ and
  the failure is a missing class, not a helpful message.
- The migrate call plays the **compose one-shot's role** against the
  throwaway database. Flyway is a **test-scope harness tool**; at
  runtime scope it would undo the datasource's claim that the
  application cannot migrate. `flyway-core` alone is not enough on
  Flyway ≥ 10 — `flyway-database-postgresql` is required for
  PostgreSQL.
- **The harness's Flyway is the build's** (Boot-managed) and may be a
  different major than the ground's one-shot image — the third pass
  lived 12 against the ground's 11. They never meet: separate
  databases, separate history tables. Noted, accepted.
- **On Boot 4 the engine enters alone, and that is load-bearing.**
  Boot 4 moved Flyway's auto-configuration into its own module; with
  only `flyway-core` and `flyway-database-postgresql` on the test
  classpath, no auto-configuration runs inside the application
  context, so the `static` call above is the only migration path in
  tests — nothing can migrate from inside a context, at any scope.
  Entering Boot's Flyway module instead would reopen that door.
- **The filesystem location is deliberate**: `filesystem:` and the
  project's one home, so nothing can drift from a classpath copy.

## 2 · The evidence tier

**Outcome** (stage 4): a small, **layered** test-support base — the
database base above, and a web-plus-database base over it — so a test
declares what it needs and inherits the rest.

```java
package <base-package>.testsupport;

import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Web-plus-database test base: the full application on a random real
 * port over the shared per-JVM PostgreSQL container. Evidence that must
 * travel the real door (HTTP → application → store) extends this; the
 * door's client (TestRestTemplate) needs the explicit auto-configure
 * annotation on Boot 4 — RANDOM_PORT alone no longer provides the bean.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public abstract class WebDatabaseIT extends DatabaseIT {
}
```

**Bound facts — the lived traps live here.**

1. **`spring-boot-restclient` must be present at test scope**, or
   this context dies with `NoClassDefFoundError: RestTemplateBuilder`
   — the test REST client module's auto-configuration introspects a
   builder from that *separate* module. The failure names something
   else entirely, which is why the pom convention requires the
   dependency's comment to say plainly that it exists only to satisfy
   another module's auto-configuration.
2. **Boot 4 moved the test REST client** to
   `org.springframework.boot.resttestclient.*` — and `RANDOM_PORT`
   alone **no longer provides the `TestRestTemplate` bean**:
   `@AutoConfigureTestRestTemplate` is required, or the autowire
   fails with "No qualifying bean".
3. The bases stay lean — no shared protected fields; a test autowires
   what it uses (`JdbcClient`, `TestRestTemplate`), so attacking
   through the door and inspecting persisted state directly are both
   one line away. The bases grow only what every extending test needs
   (the third pass added a clock configuration here at a later slice
   — slice-era growth, not bootstrap shape).

## 3 · The migration-path test

**Outcome** (stage 4): the migration path **proven, not assumed** —
and, riding on it, the connection identity asserted and the
authority split witnessed: the runtime identity cannot change
structure in the miniature, refused with the ground's own message.

```java
package <base-package>;

import <base-package>.testsupport.DatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Proves the migration path rather than assuming it: even a
 * zero-migration run births the history table, so its existence shows
 * the harness-side migrator path works end to end, and a count of zero
 * is the honest state of an earned-migrations ground. The first real
 * migration turns the count positive without changing what this proves.
 */
class MigrationPathIT extends DatabaseIT {

    @Autowired
    private JdbcClient jdbc;

    @Test
    void applicationConnectsAsTheRuntimeIdentity() {
        String identity = jdbc.sql("SELECT current_user")
                .query(String.class).single();
        assertThat(identity).isEqualTo("<project>_runtime");
    }

    @Test
    void migrationHistoryExistsWithZeroApplied() {
        Boolean historyTableExists = jdbc.sql("""
                SELECT EXISTS (
                  SELECT 1 FROM information_schema.tables
                  WHERE table_schema = '<project_schema>'
                    AND table_name = 'flyway_schema_history')
                """).query(Boolean.class).single();
        assertThat(historyTableExists).isTrue();

        Integer applied = jdbc.sql(
                        "SELECT count(*) FROM <project_schema>.flyway_schema_history")
                .query(Integer.class).single();
        assertThat(applied).isZero();
    }

    @Test
    void theRuntimeIdentityCannotChangeStructure() {
        // the ground's first refusal, held in the miniature; the store's
        // message sits at the root of Spring's translated exception
        assertThatThrownBy(() ->
                jdbc.sql("CREATE TABLE <project_schema>.t (i int)").update())
                .rootCause()
                .hasMessageContaining("permission denied for schema <project_schema>");
    }
}
```

**Bound facts.**

- **Zero is the honest bootstrap state**: the history table is born
  even by a zero-migration run, so existence proves the path and zero
  proves the home is honestly empty. When the first slice earns V1,
  the third pass evolved this to "count ≥ 1 and none failed" — the
  count changed, the proof did not.
- **The identity assertion is the split's cheapest witness**:
  `current_user` seen by application-context SQL must be the runtime
  identity. It costs three lines and catches a harness quietly wired
  as the wrong identity — the exact failure the miniature exists to
  prevent.
- **The refused DDL witnesses the authority, not only the identity**
  (lived once, run 3): a miniature wired as the right name but
  without the split — the grants missing, the bootstrap SQL not
  mounted — passes the identity assertion and fails here, with the
  ground's own message. Its trap is in the shape: Spring translates
  the driver's error and wraps it, so the message sits on the **root
  cause**, not the thrown exception — assert there, or the test is
  red for the wrong reason.

## 4 · The probe pair

**Outcomes** (stage 5): one **probe endpoint** in the main source
doing a real round-trip as the runtime identity, documented as
scaffolding that dies when the first real slice lands; and a
**contention probe test** — many requests held at a barrier and
released at one instant, each a full HTTP → application →
real-PostgreSQL round-trip through the same door a real caller uses,
every response asserted.

```java
package <base-package>.probe;

import java.util.Map;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SCAFFOLDING — dies when the first real slice lands.
 *
 * One real round-trip to the database as the runtime identity, so the
 * bootstrap's evidence harness has a full HTTP → application → store
 * path to attack. Carries no business meaning and must never grow any:
 * the first slice replaces it with its own door.
 */
@RestController
class GroundProbeController {

    private final JdbcClient jdbc;

    GroundProbeController(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/probe/ground")
    Map<String, String> groundRoundTrip() {
        String identity = jdbc.sql("SELECT current_user")
                .query(String.class).single();
        return Map.of("identity", identity);
    }
}
```

```java
package <base-package>;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import <base-package>.testsupport.WebDatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Proves the harness can CREATE contention — the adversity class the
 * first slice needs — end to end through the real door: many requests
 * held at a barrier, released at one instant, each a full HTTP →
 * application → real-PostgreSQL round-trip, every response asserted.
 * The release-at-one-instant pattern is the shape later race evidence
 * takes; here it is aimed at nothing yet.
 */
class ContentionProbeIT extends WebDatabaseIT {

    private static final int CONCURRENT_REQUESTS = 100;

    @Autowired
    private TestRestTemplate http;

    @Test
    void oneHundredSimultaneousRoundTripsAllSucceed() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);
        CountDownLatch ready = new CountDownLatch(CONCURRENT_REQUESTS);
        CountDownLatch release = new CountDownLatch(1);
        try {
            List<Future<ResponseEntity<String>>> responses = new ArrayList<>();
            for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
                responses.add(pool.submit(() -> {
                    ready.countDown();
                    release.await();
                    return http.getForEntity("/probe/ground", String.class);
                }));
            }
            ready.await();
            release.countDown();     // the one instant

            for (Future<ResponseEntity<String>> response : responses) {
                ResponseEntity<String> entity = response.get();
                assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
                // a substring is enough here: the body IS the identity
                // string, not a shape. A real body is asserted by path
                // (variation point 9), never by substring.
                assertThat(entity.getBody()).contains("<project>_runtime");
            }
        } finally {
            pool.shutdownNow();
        }
    }
}
```

**Bound facts.**

- **The two latches are the pattern**, not decoration: one counts
  every worker to the line, one releases them together. Submitting a
  hundred tasks without the barrier tests the thread pool's ramp-up,
  not simultaneity.
- **The probe round-trips the identity**, not `select 1`: every
  asserted response then witnesses both the door and the authority —
  a hundred simultaneous confirmations that the application speaks as
  runtime.
- **The pool is sized to the request count**, so every worker can
  block at the barrier at once — that is the law here; a fixed pool
  of that size (the third pass) and virtual threads (the second) both
  satisfy it. What weakens the storm is any pool smaller than the
  count.
- **Both files are scaffolding and both die together** at the first
  slice. The endpoint must never grow business meaning — the real
  surface is born from its invariant, not extended out of a probe.
  Say this in the code, not only in the log: the javadoc is what the
  person deleting it will read.
- **What this proves and what it does not.** It proves the harness
  can *drive* simultaneity end to end; it does **not** assert the
  requests overlapped in the database, and would pass on serialized
  execution. That is correct for a machinery proof and must be
  stated, because the first real slice's evidence has to assert the
  **witness** — persisted state showing the violation — not a count
  of successful responses.

## 5 · What is not in this file, and why

- **The application's own code** — the executor's, always. Only the
  probe crosses into `main/`, because the method demands it and the
  method deletes it.
- **The pom entries.** Which artifacts realize these capabilities,
  when they enter, and how each comment is written is
  `spring-pom-convention.md`'s. This file names artifacts only where
  a *name* is a trap (`testcontainers-postgresql`,
  `spring-boot-restclient`, `flyway-database-postgresql`).
- **The machine setup.** The container-runtime socket, the properties
  file that binds only from `$HOME`, Ryuk under rootless podman — the
  walkthrough's stage 4 and the project's operator manual.
- **The test-command wiring.** Widening the build's test includes to
  cover the integration naming is a build decision, stated as an
  outcome in the walkthrough.

## 6 · Variation points — read before copying

1. **The image version follows the ground's major version.**
   `postgres:17` here because the lived grounds were 17. This is the
   one line that is wrong by default in any project whose ground
   differs.
2. **The migrations home path** is the project's one home as its own
   records name it; the `filesystem:` prefix is the invariant, the
   path is not.
3. **The credential rule decides the wiring.** The localdev literals
   here are the establishment's coupling (`bootstrap.sql` ↔
   `.env.example`) carried into the harness, and they keep the suite
   self-contained — no environment set, no ground up. A run whose
   requirements forbid committed test credentials is off this
   reference at exactly this point: derive its wiring, record the
   deviation, expect it to harvest.
4. **The missing-home hole is closed somewhere, deliberately.** Git
   carries no empty directory, so a fresh clone can lack the
   migrations home while Flyway's default skips a missing location
   with a warning. The second pass closed it in Flyway config
   (`failOnMissingLocations(true)`); the third relied on the
   migration-path test plus a tracked home. Either works; closing it
   nowhere leaves the suite green on a clone that cannot migrate.
5. **The data-access grain** follows the run's requirements, not this
   file: `JdbcClient` appears because plain SQL access was required.
6. **The request count** is a knob, not a law. A hundred is what the
   lived passes used; what matters is that they are released
   together.
7. **Package layout** (`testsupport/`, `probe/`) is the executor's —
   the layering is the outcome, the names are not.
8. **Plural instances, when the definition names them.** The shape
   above stages the race inside one test process. If the framing's
   runtime ground says the system runs as more than one instance,
   the machinery proof must cross the process boundary (walkthrough,
   stage 5): a single-process pass proves a shape nobody runs, and a
   lock inside the process could make it pass. Lived once (run 3,
   never-oversold), carried here as prose until a second run lives
   it: the container and the migration lifted out of the database
   base into a **store holder** of their own, so a test with no
   application context shares the store — the base keeps only the
   datasource override, and the layering is the two bases plus the
   holder; a **process helper** that starts one instance of the
   build's own output on a free port against the holder's store,
   waits for its door to answer UP with the store's component UP,
   and stops it on close; the **probe answering its process id**
   beside the identity, so the race asserts the pids served are
   exactly the instances started; and the **race test running no
   Spring context of its own**, the requests released at one
   instant round-robin across the instances, the witness read from
   the store as the runtime identity while the instances are still
   up. The in-process burst stays beside it as the cheap first
   check.
9. **How the evidence asserts on a body** is a stack convention this
   file left unsaid, so each run decided by habit. Lived once (run 3,
   never-oversold): substring first, replaced mid-slice by JSON path
   — a substring cannot tell `3` from `30`, cannot say a field exists,
   and cannot say one is absent. The convention, decided before the
   first slice and held after: **by path for a shape** (a small
   test-support reader that parses a body once and answers a path —
   `$.id`, `$.onHandCount` — so a field's value, presence and absence
   are each one assertion); **by type when a shared API contract
   exists** (a client type both sides compile against — none exists
   in a system born from its invariant, so path is the default);
   **never by substring**, even for a word — an error title is a
   path too. The probe above is the one exception, and says why in
   its comment: its body is the identity string itself, not a shape.
