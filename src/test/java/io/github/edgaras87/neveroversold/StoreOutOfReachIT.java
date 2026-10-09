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
