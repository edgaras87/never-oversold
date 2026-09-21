package io.github.edgaras87.neveroversold;

import java.util.Map;
import java.util.UUID;

import io.github.edgaras87.neveroversold.testsupport.Body;
import io.github.edgaras87.neveroversold.testsupport.WebDatabaseIT;
import io.github.edgaras87.neveroversold.testsupport.Witness;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SL-2's evidence, E1 and E2 (the slice record:
 * {@code docs/construction/sl-2-correction-never-undercuts.md}).
 *
 * <p>The adversity is not a race and cannot be made by hammering: it is
 * one honest request the ledger cannot honour as asked — an operator
 * who counted the shelf and found fewer units than reservations hold
 * (F9, kill 6). Sequential, one request at a time, through the real
 * door, with the witness read from the store from outside.
 *
 * <p>What is here: E1 and E2 (the refusal, its mirror, and nothing moved),
 * E3 (the resend, in both worlds), and E4's evidence half (a late older
 * correction under the holds). One method is not evidence at all and says
 * so on itself — the arrival-order tripwire.
 *
 * <p>E1 holds G1 and G2: the correction is refused, and its mirror — a
 * correction that fits — is taken at exactly the number asserted, which
 * is what says the ledger never clamps the count down to the held units
 * to make something writable. E2 holds G3: after a refusal every number
 * is what it was.
 */
class CorrectionIT extends WebDatabaseIT {

    @Autowired
    private TestRestTemplate door;

    /** E1 · G1 — kill 6: the honest correction that does not fit, refused. */
    @Test
    void anHonestCorrectionUnderTheHoldsIsRefused() {
        String item = anItemHolding(10, 8);

        ResponseEntity<String> answer = adjust(item, 7);

        // the witness first: the promise is a property of the store's state,
        // and the door's manners are what it says about it afterwards
        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant, read from the store: %s", after).isTrue();
        assertThat(after.onHandCount()).as("the count keeps its old value: %s", after).isEqualTo(10);
        assertThat(after.activeSum()).as("every hold stands: %s", after).isEqualTo(8);

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(answer.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(Body.of(answer.getBody()).stringAt("$.title")).isEqualTo("refused");
    }

    /** E1 · G1 — ADR-0010: a refusal and an invalid request are different answers. */
    @Test
    void aRefusalIsNotAnInvalidRequest() {
        String item = anItemHolding(10, 8);

        assertThat(adjust(item, 7).getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(adjust(item, -1).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /** E1 · G2 — the mirror: taken whole, never clamped down to the held units. */
    @Test
    void aCorrectionThatFitsIsTakenAtExactlyTheNumberAsserted() {
        String item = anItemHolding(10, 8);

        ResponseEntity<String> answer = adjust(item, 8);

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Body.of(answer.getBody()).intAt("$.onHandCount")).isEqualTo(8);

        Witness.Numbers after = Witness.read(item);
        assertThat(after.onHandCount()).as("taken whole, never clamped: %s", after).isEqualTo(8);
        assertThat(after.holds()).isTrue();
    }

    /** E2 · G3 — every number the store holds is what it was before the refusal. */
    @Test
    void aRefusedCorrectionMovesNothing() {
        String item = anItemHolding(10, 8);
        Witness.Numbers before = Witness.read(item);

        assertThat(adjust(item, 7).getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        assertThat(Witness.read(item))
                .as("every number the store holds for the item, after a refusal")
                .isEqualTo(before);
    }

    /** E3 · G4 — kill 7: a resent correction asserts a state, so twice is once. */
    @Test
    void aCorrectionThatFitsResentAssertsTheSameState() {
        String item = anItemHolding(10, 4);
        Map<String, Object> correction = Map.of("onHandCount", 9);

        assertThat(adjust(item, correction).getStatusCode()).isEqualTo(HttpStatus.OK);
        Witness.Numbers afterFirst = Witness.read(item);

        // the same body at the same door, not a second request made to look alike
        assertThat(adjust(item, correction).getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(afterFirst.onHandCount()).as("the count moved once: %s", afterFirst).isEqualTo(9);
        assertThat(Witness.read(item))
                .as("an adjustment asserts a state, so twice is the same as once")
                .isEqualTo(afterFirst);
    }

    /** E3 · G4 — kill 7 in the refused world: refused twice, nothing moved twice. */
    @Test
    void aCorrectionThatDoesNotFitResentIsRefusedTwiceAndMovesNothing() {
        String item = anItemHolding(10, 8);
        Map<String, Object> correction = Map.of("onHandCount", 7);
        Witness.Numbers before = Witness.read(item);

        assertThat(adjust(item, correction).getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(adjust(item, correction).getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        assertThat(Witness.read(item))
                .as("a refusal resent is still a refusal, and still moves nothing")
                .isEqualTo(before);
    }

    /**
     * Not evidence — a tripwire: the invariant cannot fail here, both
     * corrections sitting above the held units. It pins a decision, that the
     * ledger keeps no order of its own, so adding any goes red on purpose.
     * Why that was chosen, and why the older number surviving is W1's
     * remainder rather than a broken promise: §3 and §8's G5 in the slice
     * record. Kill 8's evidence is {@link #aLateOlderCorrectionUnderTheHoldsIsRefused}.
     */
    @Test
    void theArrivalOrderDecidesWhichNumberSurvives() {
        // the operator makes 9 first, then 7; the network delivers one item's
        // pair as made and the other's the other way round (F13, kill 8)
        String asMade = anItemHolding(10, 4);
        String reordered = anItemHolding(10, 4);

        assertThat(adjust(asMade, 9).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Witness.read(asMade).holds()).isTrue();
        assertThat(adjust(asMade, 7).getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(adjust(reordered, 7).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Witness.read(reordered).holds()).isTrue();
        assertThat(adjust(reordered, 9).getStatusCode()).isEqualTo(HttpStatus.OK);

        Witness.Numbers madeOrder = Witness.read(asMade);
        Witness.Numbers swappedOrder = Witness.read(reordered);
        assertThat(madeOrder.holds()).as("delivered as made: %s", madeOrder).isTrue();
        assertThat(swappedOrder.holds()).as("delivered reordered: %s", swappedOrder).isTrue();

        // the swap changed the outcome, and what survives is the older
        // correction's number: the count is then wrong about the world —
        // W1's remainder, not a broken promise. This slice claims no
        // ordering and fixes none.
        assertThat(madeOrder.onHandCount()).isEqualTo(7);
        assertThat(swappedOrder.onHandCount()).isEqualTo(9);
    }

    /** E4 · G5 — kill 8's evidence half: the late older correction, refused as kill 6. */
    @Test
    void aLateOlderCorrectionUnderTheHoldsIsRefused() {
        // kill 8 collapsing into kill 6: the operator makes 7 first, then 9;
        // the swap lands 9 first, so 7 arrives late against units it no
        // longer fits under
        String item = anItemHolding(10, 8);

        assertThat(adjust(item, 9).getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> late = adjust(item, 7);

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant after the late correction: %s", after).isTrue();
        assertThat(after.onHandCount()).as("the late correction moved nothing: %s", after).isEqualTo(9);
        assertThat(late.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    /** An item known to the ledger at {@code onHand}, with {@code held} units under reservation. */
    private String anItemHolding(int onHand, int held) {
        String item = "correction-" + UUID.randomUUID();
        adjust(item, onHand);
        door.postForEntity("/items/" + item + "/reservations",
                Map.of("quantity", held, "hold", "PT15M"), String.class);
        return item;
    }

    private ResponseEntity<String> adjust(String item, int onHandCount) {
        return adjust(item, Map.of("onHandCount", onHandCount));
    }

    private ResponseEntity<String> adjust(String item, Map<String, Object> body) {
        return door.postForEntity("/items/" + item + "/adjustments", body, String.class);
    }
}
