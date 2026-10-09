package io.github.edgaras87.neveroversold;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.github.edgaras87.neveroversold.testsupport.ForkedLedger;
import io.github.edgaras87.neveroversold.testsupport.Hold;
import io.github.edgaras87.neveroversold.testsupport.InterruptedConsumeScene;
import io.github.edgaras87.neveroversold.testsupport.ThrowawayStore;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SL-4's evidence for a consume interrupted mid-work (the slice record:
 * {@code docs/construction/sl-4-consumes-two-moves-hold-together.md}, §5).
 * Each test starts from {@link InterruptedConsumeScene} — 10 on hand, R
 * holding 3, S holding 5 — and holds R's consume at one of two points
 * with a {@link Hold}, so the interruption lands mid-work on purpose and
 * not before or after by luck.
 */
class InterruptedConsumeIT {

    private static final Duration GONE_WITHIN = Duration.ofSeconds(10);

    /**
     * E1 · G1 — kill 15: we die between consume's two moves.
     *
     * <p>R's consume is held with its receipt written and the item's row
     * not yet moved; its instance is killed outright; then the hold lets
     * go. The store reads both moves or neither — and since no commit ever
     * arrived, neither: 10 on hand, 8 held, R with no ending. This fails
     * if R's ending is final while the count still reads 10, or the
     * count reads 7 while R has none.
     */
    @Test
    void aConsumeKilledBetweenItsMovesLeavesNeither() throws Exception {
        killedWhileHeld(scene -> Hold.itemRow(scene.item()));
    }

    /**
     * E1 · G1 — kill 15, at the second point: both moves written, neither
     * committed.
     *
     * <p>R's consume is held at the end of its statement, the item's row
     * already moved to 7 on hand and 5 held; its instance is killed
     * outright; the hold lets go. The store reads neither — 10 on hand, 8
     * held, R with no ending. This fails if a move written but never
     * committed survives the kill.
     */
    @Test
    void aConsumeKilledAfterBothMovesBeforeItsCommitLeavesNeither() throws Exception {
        killedWhileHeld(scene -> Hold.reservationRow(scene.r()));
    }

    /**
     * E2 · G2 — kill 15, met by a reader: a consume under way is seen
     * whole or not at all.
     *
     * <p>R's consume is held with both moves written, uncommitted. While
     * it is held, the witness reads, and a reserve of 2 on the same item
     * arrives at a second instance. The witness sees the item before the
     * consume, whole: 10 on hand, 8 held, R with no ending. The reserve
     * waits behind the consume. Let go, the consume commits, and the
     * reserve decides against the state after it — 2 free of 7 — and is
     * admitted: 7 on hand, 7 held. This fails if anyone reads one move
     * without the other — the witness seeing the count fallen while R
     * still holds, or a reserve deciding on half a consume.
     */
    @Test
    void aReaderBesideAHeldConsumeSeesItWholeWhenItCommits() throws Exception {
        readerBesideHeldConsume(false);
    }

    /**
     * E2 · G2 — kill 15, met by a reader, the consume then dying.
     *
     * <p>As above, but the consume's instance is killed while held. The
     * reserve then decides against the state before the consume — 2 free
     * of 10 — and is admitted: 10 on hand, 10 held, R still holding.
     * This fails if anyone reads one move without the other.
     */
    @Test
    void aReaderBesideAHeldConsumeSeesItWholeWhenItDies() throws Exception {
        readerBesideHeldConsume(true);
    }

    private static void readerBesideHeldConsume(boolean kill) throws Exception {
        try (ForkedLedger consuming = ForkedLedger.start(ThrowawayStore.port());
             ForkedLedger reserving = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(consuming);
            CompletableFuture<HttpResponse<String>> reserved;
            int consumer;
            try (Hold hold = Hold.reservationRow(scene.r())) {
                scene.consumeR(consuming);
                consumer = hold.awaitWaiter().pid();

                Witness.Reading during = Witness.read(scene.item(), scene.r());
                assertThat(during.numbers().holds()).as("the invariant, mid-consume: %s", during).isTrue();
                assertThat(during.numbers().onHandCount()).as("on hand, mid-consume: %s", during).isEqualTo(10);
                assertThat(during.numbers().held()).as("held, mid-consume: %s", during).isEqualTo(8);
                assertThat(during.ending()).as("R's ending, mid-consume: %s", during).isNull();

                reserved = scene.reserveOnItem(reserving, 2);
                Hold.Waiter reserve = Hold.awaitWaiterOn(consumer);
                assertThat(reserve.query()).as("the reserve waits behind the consume").contains("reserved + ");
                if (kill) {
                    consuming.kill();
                }
            }
            HttpResponse<String> reserveAnswer = reserved.get(30, TimeUnit.SECONDS);
            if (kill) {
                ThrowawayStore.awaitSessionGone(consumer, GONE_WITHIN);
            }

            Witness.Reading after = Witness.read(scene.item(), scene.r());
            assertThat(after.numbers().holds()).as("the invariant: %s", after).isTrue();
            boolean ended = "consumed".equals(after.ending());
            assertThat(ended).as("the consume committed, or it died: %s", after).isEqualTo(!kill);
            assertThat(after.numbers().onHandCount()).as("on hand: %s", after).isEqualTo(kill ? 10 : 7);
            assertThat(after.numbers().held()).as("held, the reserve's 2 among them: %s", after)
                    .isEqualTo(kill ? 10 : 7);

            assertThat(reserveAnswer.statusCode()).as("the reserve fit a whole state").isEqualTo(201);
        }
    }

    private static void killedWhileHeld(Function<InterruptedConsumeScene, Hold> holdAt) throws Exception {
        try (ForkedLedger ledger = ForkedLedger.start(ThrowawayStore.port())) {
            InterruptedConsumeScene scene = InterruptedConsumeScene.set(ledger);
            CompletableFuture<HttpResponse<String>> answer;
            int consumer;
            try (Hold hold = holdAt.apply(scene)) {
                answer = scene.consumeR(ledger);
                consumer = hold.awaitWaiter().pid();
                ledger.kill();
            }
            ThrowawayStore.awaitSessionGone(consumer, GONE_WITHIN);

            Witness.Reading after = Witness.read(scene.item(), scene.r());
            assertThat(after.numbers().holds()).as("the invariant: %s", after).isTrue();
            boolean ended = "consumed".equals(after.ending());
            assertThat(after.numbers().onHandCount() == 7)
                    .as("R ended by consume exactly when the count fell by its 3: %s", after).isEqualTo(ended);
            assertThat(after.numbers().held() == 5)
                    .as("R ended by consume exactly when the units held fell by its 3: %s", after).isEqualTo(ended);
            assertThat(after.numbers().onHandCount()).as("no commit arrived — on hand: %s", after).isEqualTo(10);
            assertThat(after.numbers().held()).as("no commit arrived — held: %s", after).isEqualTo(8);
            assertThat(after.ending()).as("no commit arrived — R's ending: %s", after).isNull();

            assertThat(answer).as("the killed instance answered nothing")
                    .failsWithin(Duration.ofSeconds(30));
        }
    }
}
