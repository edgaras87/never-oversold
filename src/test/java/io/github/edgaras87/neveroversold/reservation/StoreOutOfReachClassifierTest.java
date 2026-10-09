package io.github.edgaras87.neveroversold.reservation;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Not evidence — a tripwire for §8's choice in SL-4's record, "The
 * surface": ADR-0016's answer is decided by the store's own error class,
 * not by the framework's exception type. It pins that decision and
 * discharges no kill.
 *
 * <p>A lost connection and a refusal at commit both reach the door as a
 * failed rollback wrapping the first failure. Only the first is "outcome
 * unknown"; the store answered the second, and undid the work. This fails
 * if the ledger ever decides by the wrapper.
 */
class StoreOutOfReachClassifierTest {

    @Test
    void aSessionEndedByTheStoreIsOutOfReach() {
        assertThat(Ledger.lostTheStore(
                new DataAccessResourceFailureException("consume", new SQLException("terminating", "57P01"))))
                .isTrue();
    }

    @Test
    void aLostConnectionBehindAFailedRollbackIsOutOfReach() {
        TransactionSystemException rollback = new TransactionSystemException("JDBC rollback failed",
                new SQLException("Connection is closed", "08003"));
        rollback.initApplicationException(
                new DataAccessResourceFailureException("consume", new SQLException("terminating", "57P01")));
        assertThat(Ledger.lostTheStore(rollback)).isTrue();
    }

    @Test
    void aRefusalAtCommitIsNotOutOfReach() {
        TransactionSystemException commit = new TransactionSystemException("Could not commit JDBC transaction",
                new SQLException("item X: the units held read 5, its reservations with no receipt hold 8", "23000"));
        assertThat(Ledger.lostTheStore(commit)).isFalse();
    }

    @Test
    void aConstraintTheStoreRefusedIsNotOutOfReach() {
        assertThat(Ledger.lostTheStore(
                new DataIntegrityViolationException("reserve", new SQLException("check violated", "23514"))))
                .isFalse();
    }
}
