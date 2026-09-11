package io.github.edgaras87.neveroversold;

import io.github.edgaras87.neveroversold.testsupport.DatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Proves the migration path rather than assuming it: even a
 * zero-migration run births the history table, so its existence shows the
 * harness-side migrator path works end to end, and a count of zero is the
 * honest state of an earned-migrations ground — no table before its
 * invariant. The first real migration turns the count positive without
 * changing what this proves.
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
    void migrationHistoryExistsWithZeroApplied() {
        Boolean historyTableExists = jdbc.sql("""
                SELECT EXISTS (
                  SELECT 1 FROM information_schema.tables
                  WHERE table_schema = 'never_oversold'
                    AND table_name = 'flyway_schema_history')
                """).query(Boolean.class).single();
        assertThat(historyTableExists).isTrue();

        Integer applied = jdbc.sql("SELECT count(*) FROM never_oversold.flyway_schema_history")
                .query(Integer.class).single();
        assertThat(applied).isZero();
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
