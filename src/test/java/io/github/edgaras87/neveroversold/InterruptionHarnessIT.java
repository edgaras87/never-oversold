package io.github.edgaras87.neveroversold;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.github.edgaras87.neveroversold.testsupport.InterruptedConsumeScene;
import io.github.edgaras87.neveroversold.testsupport.ForkedLedger;
import io.github.edgaras87.neveroversold.testsupport.Hold;
import io.github.edgaras87.neveroversold.testsupport.ThrowawayStore;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Not evidence — the harness's own checks, before any evidence leans on
 * it (SL-4's record, §8, "The surface"). They show that the harness can
 * do what SL-4's evidence needs: hold a consume at each of two points
 * mid-work, kill its instance outright, freeze and thaw the store, end
 * one session. They discharge no kill and read the witness only to show
 * a held consume still finishes when let go.
 *
 * <p>Each starts from {@link InterruptedConsumeScene}: 10 on hand, R holding 3, S
 * holding 5, set through one forked instance's real door.
 */
class InterruptionHarnessIT {

    private static final Duration GONE_WITHIN = Duration.ofSeconds(10);

    /**
     * Not evidence: a hold on the item's row stops R's consume there —
     * its receipt written, the item's row not yet moved — and the consume
     * finishes, 7 on hand and 5 held, when the hold lets go.
     */
    @Test
    void aConsumeHeldAtTheItemsRowWaitsThereAndFinishesWhenLetGo() throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            CompletableFuture<HttpResponse<String>> answer;
            try (Hold hold = Hold.itemRow(scene.item())) {
                answer = scene.consumeR(ledger);
                Hold.Waiter waiter = hold.awaitWaiter();
                assertThat(waiter.query()).as("the consume is what waits").contains("'consumed'");
                assertThat(answer).as("no answer while held").isNotDone();
            }
            HttpResponse<String> done = answer.get(30, TimeUnit.SECONDS);

            Witness.Reading after = Witness.read(scene.item(), scene.r());
            assertThat(after.numbers().holds()).as("the invariant: %s", after).isTrue();
            assertThat(after.numbers().onHandCount()).as("on hand after the consume: %s", after).isEqualTo(7);
            assertThat(after.numbers().held()).as("held after the consume: %s", after).isEqualTo(5);
            assertThat(after.ending()).isEqualTo("consumed");
            assertThat(done.statusCode()).isEqualTo(200);
        }
    }

    /**
     * Not evidence: a hold on R's row stops R's consume after both moves —
     * the item's row already taken by the consume, so moved — and before
     * its statement ends; let go, it finishes. This settles the record's
     * provisional on the second hold point.
     */
    @Test
    void aConsumeHeldAtItsReservationsRowHasMovedTheItemAlready() throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            CompletableFuture<HttpResponse<String>> answer;
            try (Hold hold = Hold.reservationRow(scene.r())) {
                answer = scene.consumeR(ledger);
                Hold.Waiter waiter = hold.awaitWaiter();
                assertThat(waiter.query()).as("the consume is what waits").contains("'consumed'");
                assertThat(Hold.itemRowTaken(scene.item()))
                        .as("the consume holds the item's row: it has moved it already").isTrue();
                assertThat(answer).as("no answer while held").isNotDone();
            }
            HttpResponse<String> done = answer.get(30, TimeUnit.SECONDS);

            Witness.Reading after = Witness.read(scene.item(), scene.r());
            assertThat(after.numbers().holds()).as("the invariant: %s", after).isTrue();
            assertThat(after.numbers().onHandCount()).as("on hand after the consume: %s", after).isEqualTo(7);
            assertThat(after.numbers().held()).as("held after the consume: %s", after).isEqualTo(5);
            assertThat(after.ending()).isEqualTo("consumed");
            assertThat(done.statusCode()).isEqualTo(200);
        }
    }

    /**
     * Not evidence: a kill is outright — the process is gone — and the
     * killed instance's held session leaves the store once the store next
     * speaks to it, which is when the hold lets go.
     */
    @Test
    void aKilledInstancesHeldSessionLeavesTheStore() throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            int consumer;
            try (Hold hold = Hold.itemRow(scene.item())) {
                scene.consumeR(ledger);
                consumer = hold.awaitWaiter().pid();
                ledger.kill();
                assertThat(ProcessHandle.of(ledger.pid())).as("the process is gone").isEmpty();
            }
            ThrowawayStore.awaitSessionGone(consumer, GONE_WITHIN);
        }
    }

    /**
     * Not evidence: a frozen store answers nothing, and a thawed one
     * answers again.
     */
    @Test
    void aFrozenStoreAnswersNothingUntilThawed() {
        assertThat(ThrowawayStore.answersWithin(5)).as("the store answers before the freeze").isTrue();
        ThrowawayStore.freeze();
        try {
            assertThat(ThrowawayStore.answersWithin(2)).as("a frozen store answers nothing").isFalse();
        } finally {
            ThrowawayStore.thaw();
        }
        assertThat(ThrowawayStore.answersWithin(5)).as("the thawed store answers").isTrue();
    }

    /**
     * Not evidence: ending a held consume's session at the store removes
     * it — the way an instance is made to lose the store mid-request —
     * and the instance's caller gets an answer of some kind. Which answer
     * is ADR-0016's, checked by {@code StoreOutOfReachIT}.
     */
    @Test
    void anEndedSessionLeavesTheStoreAndTheCallerIsAnswered() throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            CompletableFuture<HttpResponse<String>> answer;
            try (Hold hold = Hold.itemRow(scene.item())) {
                answer = scene.consumeR(ledger);
                int consumer = hold.awaitWaiter().pid();
                ThrowawayStore.endSession(consumer);
                ThrowawayStore.awaitSessionGone(consumer, GONE_WITHIN);
            }
            assertThat(answer.get(30, TimeUnit.SECONDS).statusCode()).as("the caller is answered").isPositive();
        }
    }
}
