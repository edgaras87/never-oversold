package io.github.edgaras87.neveroversold;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.github.edgaras87.neveroversold.testsupport.Body;
import io.github.edgaras87.neveroversold.testsupport.ForkedLedger;
import io.github.edgaras87.neveroversold.testsupport.Hold;
import io.github.edgaras87.neveroversold.testsupport.InterruptedConsumeScene;
import io.github.edgaras87.neveroversold.testsupport.ThrowawayStore;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A consume whose instance loses the store mid-work (the slice record:
 * {@code docs/construction/sl-4-consumes-two-moves-hold-together.md}, §5,
 * E3). Each test starts from {@link InterruptedConsumeScene} — 10 on
 * hand, R holding 3, S holding 5.
 */
class StoreOutOfReachIT {

    private static final Duration GONE_WITHIN = Duration.ofSeconds(10);

    /**
     * E3 · G3 — kill 16, our silence: an interrupted consume settles on
     * its own.
     *
     * <p>R's consume is held between its moves — its receipt written, the
     * item's row not yet moved. The store is frozen, the caller gives up,
     * and the consume's instance is killed while the store cannot see it.
     * The store is thawed and the hold let go; nothing is sent since — no
     * retry, no repair. The store settles alone: neither move, 10 on hand,
     * 8 held, R with no ending. A reserve of 2 then goes through against
     * those numbers: 10 held of 10. This fails if anything were still owed
     * after the silence — R's ending final with the count unmoved, or the
     * item stuck so the reserve never decides.
     */
    @Test
    void aConsumeFrozenOutAndAbandonedSettlesWithNoOneSendingAnything() throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port());
             ForkedLedger next = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            int consumer;
            try (Hold hold = Hold.itemRow(scene.item())) {
                CompletableFuture<HttpResponse<String>> answer = scene.consumeR(ledger);
                consumer = hold.awaitWaiter().pid();
                ThrowawayStore.freeze();
                try {
                    answer.cancel(true);   // the caller gives up
                    ledger.kill();         // and so does the instance, unseen by the store
                } finally {
                    ThrowawayStore.thaw();
                }
            }
            ThrowawayStore.awaitSessionGone(consumer, GONE_WITHIN);

            Witness.Reading settled = Witness.read(scene.item(), scene.r());
            assertThat(settled.numbers().holds()).as("the invariant, settled: %s", settled).isTrue();
            boolean ended = "consumed".equals(settled.ending());
            assertThat(settled.numbers().onHandCount() == 7)
                    .as("R ended by consume exactly when the count fell by its 3: %s", settled).isEqualTo(ended);
            assertThat(settled.numbers().held() == 5)
                    .as("R ended by consume exactly when the units held fell by its 3: %s", settled).isEqualTo(ended);
            assertThat(settled.numbers().onHandCount()).as("no commit arrived — on hand: %s", settled).isEqualTo(10);
            assertThat(settled.numbers().held()).as("no commit arrived — held: %s", settled).isEqualTo(8);
            assertThat(settled.ending()).as("no commit arrived — R's ending: %s", settled).isNull();

            HttpResponse<String> reserved = scene.reserveOnItem(next, 2).get(30, TimeUnit.SECONDS);
            Witness.Reading after = Witness.read(scene.item(), scene.r());
            assertThat(after.numbers().holds()).as("the invariant, after the next decision: %s", after).isTrue();
            assertThat(after.numbers().held()).as("the next decision went through: %s", after).isEqualTo(10);
            assertThat(reserved.statusCode()).as("the reserve, against the settled numbers").isEqualTo(201);
        }
    }

    /**
     * Not evidence — the freeze alone, with the consume's instance alive:
     * nothing is lost, and the consume finishes on its own.
     *
     * <p>R's consume is held between its moves, the store frozen, the
     * caller gone. Thawed and let go, the instance — still alive — carries
     * its consume to its commit: 7 on hand, 5 held, R consumed, with
     * nothing sent since; a reserve of 2 then fits, 7 held of 7. It cannot
     * go red for the invariant: an instance that lives finishes what it
     * began, whether the moves are one transaction or two, so nothing is
     * owed after the freeze. Seen green on the red tree, and kept to show
     * a freeze loses nothing. It discharges no kill.
     */
    @Test
    void aConsumeFrozenOutWithItsInstanceAliveFinishesOnItsOwn() throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port());
             ForkedLedger next = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            int consumer;
            try (Hold hold = Hold.itemRow(scene.item())) {
                CompletableFuture<HttpResponse<String>> answer = scene.consumeR(ledger);
                consumer = hold.awaitWaiter().pid();
                ThrowawayStore.freeze();
                try {
                    answer.cancel(true);   // the caller gives up
                } finally {
                    ThrowawayStore.thaw();
                }
            }
            awaitIdle(consumer);

            Witness.Reading settled = Witness.read(scene.item(), scene.r());
            assertThat(settled.numbers().holds()).as("the invariant, settled: %s", settled).isTrue();
            assertThat(settled.numbers().onHandCount()).as("on hand: %s", settled).isEqualTo(7);
            assertThat(settled.numbers().held()).as("held: %s", settled).isEqualTo(5);
            assertThat(settled.ending()).as("R's ending: %s", settled).isEqualTo("consumed");

            HttpResponse<String> reserved = scene.reserveOnItem(next, 2).get(30, TimeUnit.SECONDS);
            Witness.Reading after = Witness.read(scene.item(), scene.r());
            assertThat(after.numbers().holds()).as("the invariant, after the next decision: %s", after).isTrue();
            assertThat(after.numbers().held()).as("the next decision went through: %s", after).isEqualTo(7);
            assertThat(reserved.statusCode()).as("the reserve, against the settled numbers").isEqualTo(201);
        }
    }

    /** Waits until the live instance's session has finished its consume — no longer waiting, no longer in it. */
    private static void awaitIdle(int session) throws InterruptedException {
        long deadline = System.nanoTime() + GONE_WITHIN.toNanos();
        while (ThrowawayStore.isBusy(session)) {
            if (System.nanoTime() > deadline) {
                throw new IllegalStateException("session " + session + " still busy after " + GONE_WITHIN);
            }
            Thread.sleep(10);
        }
    }

    /**
     * Not evidence — a check of ADR-0016, the door's convention, not the
     * promise: it discharges no kill.
     *
     * <p>R's consume is held mid-work, and its session is ended at the
     * store: the instance has lost the store and cannot know whether the
     * consume committed. It answers {@code 503} as Problem Details, title
     * "outcome unknown", saying the request may or may not have taken
     * effect. This fails if the instance answers anything else — the
     * framework's default error, or a {@code 500} that reads as a defect.
     */
    @Test
    void anInstanceThatLosesTheStoreMidConsumeAnswersOutcomeUnknown() throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            CompletableFuture<HttpResponse<String>> answer;
            try (Hold hold = Hold.itemRow(scene.item())) {
                answer = scene.consumeR(ledger);
                int consumer = hold.awaitWaiter().pid();
                ThrowawayStore.endSession(consumer);
                ThrowawayStore.awaitSessionGone(consumer, GONE_WITHIN);
            }
            HttpResponse<String> lost = answer.get(30, TimeUnit.SECONDS);

            assertThat(lost.statusCode()).as("the answer: %s", lost.body()).isEqualTo(503);
            assertThat(lost.headers().firstValue("Content-Type")).as("Problem Details")
                    .hasValueSatisfying(type -> assertThat(type).startsWith("application/problem+json"));
            Body body = Body.of(lost.body());
            assertThat(body.stringAt("$.title")).isEqualTo("outcome unknown");
            assertThat(body.intAt("$.status")).isEqualTo(503);
            assertThat(body.stringAt("$.detail")).contains("may or may not have taken effect");
        }
    }
}
