package io.github.edgaras87.neveroversold;

import io.github.edgaras87.neveroversold.testsupport.DatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Proves the migration path rather than assuming it: the history table
 * shows the harness-side migrator path works end to end, and its rows show
 * what was applied and that nothing failed. Born with a count of zero (no
 * table before its invariant); SL-1's V1 turned the count positive without
 * changing what this proves. Riding on it: the wall that V1 carries is
 * read back from the catalog, so a constraint dropped "temporarily" is
 * seen here before any evidence test samples around its absence.
 */
class MigrationPathIT extends DatabaseIT {

    @Autowired
    private JdbcClient jdbc;

    @Test
    void applicationConnectsAsTheRuntimeIdentity() {
        String identity = jdbc.sql("SELECT current_user").query(String.class).single();
        assertThat(identity).isEqualTo("runtime");
    }

    @Test
    void migrationHistoryExistsWithEveryMigrationApplied() {
        Boolean historyTableExists = jdbc.sql("""
                SELECT EXISTS (
                  SELECT 1 FROM information_schema.tables
                  WHERE table_schema = 'never_oversold'
                    AND table_name = 'flyway_schema_history')
                """).query(Boolean.class).single();
        assertThat(historyTableExists).isTrue();

        Integer applied = jdbc.sql("SELECT count(*) FROM never_oversold.flyway_schema_history")
                .query(Integer.class).single();
        assertThat(applied).isGreaterThanOrEqualTo(1);

        Integer failed = jdbc.sql(
                        "SELECT count(*) FROM never_oversold.flyway_schema_history WHERE NOT success")
                .query(Integer.class).single();
        assertThat(failed).isZero();
    }

    @Test
    void theWallIsInTheCatalog() {
        // SL-1's owner for G1 and G3 is this constraint (slice record, §7);
        // structure forbids, tests only sample — so its presence is asserted,
        // not inferred from a green storm
        String wall = jdbc.sql("""
                SELECT pg_get_constraintdef(c.oid)
                FROM pg_constraint c
                JOIN pg_class t ON t.oid = c.conrelid
                JOIN pg_namespace n ON n.oid = t.relnamespace
                WHERE n.nspname = 'never_oversold'
                  AND t.relname = 'item'
                  AND c.conname = 'item_never_oversold'
                """).query(String.class).single();
        assertThat(wall).isEqualTo("CHECK ((reserved <= on_hand_count))");
    }

    @Test
    void theRuntimeIdentityCanWriteTheNewTables() {
        // the ground's fourth term — new objects arrive already usable by
        // runtime, with no GRANT in the migration — held in the miniature
        String item = "migration-path-" + java.util.UUID.randomUUID();
        jdbc.sql("INSERT INTO item (id, on_hand_count) VALUES (:id, 1)")
                .param("id", item).update();
        jdbc.sql("""
                INSERT INTO reservation (item_id, quantity, expires_at)
                VALUES (:id, 1, now() + interval '1 minute')
                """).param("id", item).update();
        assertThat(jdbc.sql("DELETE FROM reservation WHERE item_id = :id").param("id", item).update())
                .isEqualTo(1);
        assertThat(jdbc.sql("DELETE FROM item WHERE id = :id").param("id", item).update())
                .isEqualTo(1);
    }

    @Test
    void theRuntimeIdentityCannotChangeStructure() {
        // the ground's first refusal (contract, term 1), held in the miniature;
        // the store's message sits at the root of Spring's translated exception
        assertThatThrownBy(() -> jdbc.sql("CREATE TABLE never_oversold.t (i int)").update())
                .rootCause()
                .hasMessageContaining("permission denied for schema never_oversold");
    }
}
