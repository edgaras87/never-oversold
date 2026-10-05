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
 * SL-3's evidence at the real door, one request at a time (the slice
 * record: {@code docs/construction/sl-3-a-reservation-exits-once.md}).
 * The races are {@code ExitStormIT}'s; what is here needs no race to
 * happen: a repeated exit, a different exit after the first, an exit
 * that moves exactly its reservation's units, an exit on a reservation
 * that does not exist, and an exit after expiry — on its own, and after
 * a new reserve has taken the expired hold's units.
 *
 * <p>The numbers start from the record's own example (§4): one item,
 * 10 on hand; reservation R holds 3, reservation S holds 5, so 8 are
 * held and 2 are free. The witness is read from the store first in
 * every test, the door's answer after: the promise is a property of
 * the state, and the status is what the door says about it.
 */
class ExitDoorIT extends WebDatabaseIT {

    @Autowired
    private TestRestTemplate door;

    /** One item, 10 on hand, R holding 3 and S holding 5. */
    private record Scene(String item, UUID r, UUID s) {
    }

    /**
     * E5 · G4 — a consume moves exactly what its reservation holds (FC2).
     *
     * <p>Consume R: 10 on hand becomes 7, 8 held becomes 5, and S is
     * untouched. The answer is R as persisted, now saying how and when it
     * ended.
     */
    @Test
    void aConsumeMovesExactlyItsReservationsUnits() {
        Scene scene = theRecordsExample();

        ResponseEntity<String> answer = consume(scene.r());

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("3 units left the shelf: %s", after).isEqualTo(7);
        assertThat(after.held()).as("R's 3 no longer held: %s", after).isEqualTo(5);
        assertThat(after.activeSum()).as("S alone is active: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(scene.r())).isEqualTo("consumed");
        assertThat(Witness.endingOf(scene.s())).isNull();

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.OK);
        Body body = Body.of(answer.getBody());
        assertThat(body.uuidAt("$.id")).isEqualTo(scene.r());
        assertThat(body.intAt("$.quantity")).isEqualTo(3);
        assertThat(body.stringAt("$.endedBy")).isEqualTo("consume");
        assertThat(body.has("$.endedAt")).isTrue();
    }

    /** E5 · G4 — a release frees exactly what its reservation holds; the count stays. */
    @Test
    void aReleaseFreesExactlyItsReservationsUnits() {
        Scene scene = theRecordsExample();

        ResponseEntity<String> answer = release(scene.r());

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("nothing left the shelf: %s", after).isEqualTo(10);
        assertThat(after.held()).as("R's 3 are free again: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(scene.r())).isEqualTo("released");

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Body.of(answer.getBody()).stringAt("$.endedBy")).isEqualTo("release");
    }

    /**
     * E5 · G4 — an exit carries no quantity (FC2, folded here).
     *
     * <p>A caller sends a consume with a body asking for 1 unit. The door
     * binds no body, so the consume moves R's 3 all the same. This fails if
     * an exit ever takes its amount from the request.
     */
    @Test
    void anExitTakesNoAmountFromTheRequest() {
        Scene scene = theRecordsExample();

        ResponseEntity<String> answer = door.postForEntity(
                "/reservations/" + scene.r() + "/consume", Map.of("quantity", 1), String.class);

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("R's 3, not the 1 asked: %s", after).isEqualTo(7);
        assertThat(after.held()).as("R's 3, not the 1 asked: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(scene.r())).isEqualTo("consumed");
        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /** E5 · G4 — an exit naming a reservation that does not exist is unknown, and nothing moves. */
    @Test
    void anExitOnAnUnknownReservationIsUnknownAndMovesNothing() {
        Scene scene = theRecordsExample();

        ResponseEntity<String> answer = consume(UUID.randomUUID());

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).isEqualTo(10);
        assertThat(after.held()).isEqualTo(8);

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(answer.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(Body.of(answer.getBody()).stringAt("$.title")).isEqualTo("unknown reservation");
    }

    /** Not evidence — the door's nonsense rule (G6, SL-1) reaching the new paths. */
    @Test
    void anExitNamingNoReservationIsInvalid() {
        ResponseEntity<String> answer = door.postForEntity(
                "/reservations/not-an-identifier/release", null, String.class);

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Body.of(answer.getBody()).stringAt("$.title")).isEqualTo("invalid request");
    }

    /**
     * Not evidence — a tripwire on the answer's shape: an active
     * reservation carries no ending, so reserve's answer reads as it did
     * before exits existed (and README's example stays true).
     */
    @Test
    void anActiveReservationCarriesNoEnding() {
        String item = newItem(10);

        Body body = Body.of(reserve(item, 3, "PT15M").getBody());

        assertThat(body.names("endedBy")).as("absent, not null").isFalse();
        assertThat(body.names("endedAt")).as("absent, not null").isFalse();
    }

    /**
     * E3 · G2 — kill 12: the reply is lost, the caller consumes again.
     *
     * <p>Consume R, then the same consume again. The count moves once: 7
     * on hand, 5 held, after both. The second answer is the first's
     * (ADR-0013) — 200 with R as persisted, the same instant.
     */
    @Test
    void theSameConsumeTwiceMovesOnce() {
        Scene scene = theRecordsExample();

        ResponseEntity<String> first = consume(scene.r());
        ResponseEntity<String> again = consume(scene.r());

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("moved once, not twice: %s", after).isEqualTo(7);
        assertThat(after.held()).as("moved once, not twice: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(scene.r())).isEqualTo("consumed");

        assertThat(again.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Body.of(again.getBody()).stringAt("$.endedAt"))
                .isEqualTo(Body.of(first.getBody()).stringAt("$.endedAt"));
    }

    /** E3 · G2 — kill 12, the release side: released twice, freed once. */
    @Test
    void theSameReleaseTwiceMovesOnce() {
        Scene scene = theRecordsExample();

        release(scene.r());
        ResponseEntity<String> again = release(scene.r());

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).isEqualTo(10);
        assertThat(after.held()).as("freed once, not twice: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(scene.r())).isEqualTo("released");
        assertThat(again.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * E3 · G2 — a different exit after the first is refused and moves
     * nothing (ADR-0010, as ADR-0013 narrowed it).
     *
     * <p>Consume R, then release R. Without the wall the release would
     * take R's 3 off the held units a second time — 2 held, though S
     * still holds 5. It stays 7 on hand, 5 held.
     */
    @Test
    void aReleaseAfterAConsumeIsRefusedAndMovesNothing() {
        Scene scene = theRecordsExample();
        consume(scene.r());

        ResponseEntity<String> answer = release(scene.r());

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).isEqualTo(7);
        assertThat(after.held()).as("R's 3 not freed a second time: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(scene.r())).isEqualTo("consumed");

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Body.of(answer.getBody()).stringAt("$.title")).isEqualTo("refused");
    }

    /** E3 · G2 — and the other way round: consume after release, refused, nothing left the shelf. */
    @Test
    void aConsumeAfterAReleaseIsRefusedAndMovesNothing() {
        Scene scene = theRecordsExample();
        release(scene.r());

        ResponseEntity<String> answer = consume(scene.r());

        Witness.Numbers after = Witness.read(scene.item());
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("nothing left the shelf: %s", after).isEqualTo(10);
        assertThat(after.held()).isEqualTo(5);
        assertThat(Witness.endingOf(scene.r())).isEqualTo("released");
        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    /**
     * E3 · G2 — a consume after expiry is refused; the expiry it met is
     * recorded.
     *
     * <p>R holds 3 for one second and runs out. A consume on it then
     * arrives: nothing leaves the shelf, R's receipt says expired, and its
     * 3 units are free — the request that met the expiry wrote it down,
     * though it was refused. Kill 11's full form, with a new reserve
     * taking R's units first, is the next test.
     */
    @Test
    void aConsumeAfterExpiryIsRefusedAndTheExpiryRecorded() throws InterruptedException {
        String item = newItem(10);
        UUID r = Body.of(reserve(item, 3, "PT1S").getBody()).uuidAt("$.id");
        reserve(item, 5, "PT15M");

        Thread.sleep(1_500);   // R runs out — by the store's clock, not this JVM's

        ResponseEntity<String> answer = consume(r);

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("nothing left the shelf: %s", after).isEqualTo(10);
        assertThat(after.held()).as("R's 3 freed by its expiry: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(r)).isEqualTo("expired");

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Body.of(answer.getBody()).stringAt("$.title")).isEqualTo("refused");
    }

    /**
     * E3 · G2 — kill 11 in full.
     *
     * <p>R holds 3 for one second, S holds 5 for fifteen minutes. R runs
     * out, and a new reserve for 5, T, takes the units R's expiry freed:
     * 10 held of 10. Then R's consume arrives, late. It is refused and
     * moves nothing: 10 on hand, 10 held, R ended by expiry, S and T
     * still active. This fails if a late consume ever lands on an expired
     * reservation: R's 3 would leave the shelf a second time, under T's
     * hold — 7 on hand while 10 stay held.
     */
    @Test
    void aLateConsumeIsRefusedAfterANewHoldTookItsUnits() throws InterruptedException {
        String item = newItem(10);
        UUID r = Body.of(reserve(item, 3, "PT1S").getBody()).uuidAt("$.id");
        UUID s = Body.of(reserve(item, 5, "PT15M").getBody()).uuidAt("$.id");
        Thread.sleep(1_500);   // R runs out — by the store's clock, not this JVM's
        UUID t = Body.of(reserve(item, 5, "PT15M").getBody()).uuidAt("$.id");
        Witness.Numbers before = Witness.read(item);
        assertThat(before.held()).as("T took the units R's expiry freed: %s", before).isEqualTo(10);

        ResponseEntity<String> answer = consume(r);

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("nothing left the shelf: %s", after).isEqualTo(10);
        assertThat(after.held()).as("S's 5 and T's 5, and nothing more: %s", after).isEqualTo(10);
        assertThat(after.activeSum()).as("S and T still active: %s", after).isEqualTo(10);
        assertThat(Witness.endingOf(r)).isEqualTo("expired");
        assertThat(Witness.endingOf(s)).isNull();
        assertThat(Witness.endingOf(t)).isNull();

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Body.of(answer.getBody()).stringAt("$.title")).isEqualTo("refused");
    }

    /**
     * Not evidence — a tripwire for §3's choice, at the reserve: an
     * expired hold's units are free from the instant. It guards the
     * decided face, not the promise — holding more than needed cannot
     * oversell — and discharges no kill.
     *
     * <p>R holds 3 for one second, S holds 5: 2 free. R runs out, and a
     * reserve for 5 is admitted, which fits only if R's 3 are free. This
     * fails if a reserve ever leaves expired units held until something
     * else ends them.
     */
    @Test
    void aReserveAfterTheInstantFindsTheExpiredUnitsFree() throws InterruptedException {
        String item = newItem(10);
        UUID r = Body.of(reserve(item, 3, "PT1S").getBody()).uuidAt("$.id");
        reserve(item, 5, "PT15M");
        Thread.sleep(1_500);   // R runs out — by the store's clock, not this JVM's

        ResponseEntity<String> answer = tryReserve(item, 5, "PT15M");

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.held()).as("S's 5 and the new 5, R's 3 freed: %s", after).isEqualTo(10);
        assertThat(after.activeSum()).as("S and the new hold: %s", after).isEqualTo(10);
        assertThat(Witness.endingOf(r)).isEqualTo("expired");

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    /**
     * Not evidence — a tripwire for §3's choice, at the correction: the
     * same decided face, on SL-2's path. It discharges no kill.
     *
     * <p>R holds 3 for one second, S holds 5: 8 held. R runs out, and the
     * operator sets the count to 5, which is accepted: only S's 5 stand
     * under it. This fails if a correction ever counts an expired hold's
     * units as held — it would refuse 5 against 8.
     */
    @Test
    void aCorrectionAfterTheInstantFindsTheExpiredUnitsFree() throws InterruptedException {
        String item = newItem(10);
        UUID r = Body.of(reserve(item, 3, "PT1S").getBody()).uuidAt("$.id");
        reserve(item, 5, "PT15M");
        Thread.sleep(1_500);   // R runs out — by the store's clock, not this JVM's

        ResponseEntity<String> answer = tryAdjust(item, 5);

        Witness.Numbers after = Witness.read(item);
        assertThat(after.holds()).as("the invariant: %s", after).isTrue();
        assertThat(after.onHandCount()).as("the count the operator asserted: %s", after).isEqualTo(5);
        assertThat(after.held()).as("S's 5 alone, R's 3 freed: %s", after).isEqualTo(5);
        assertThat(Witness.endingOf(r)).isEqualTo("expired");

        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Body.of(answer.getBody()).intAt("$.onHandCount")).isEqualTo(5);
    }

    private Scene theRecordsExample() {
        String item = newItem(10);
        UUID r = Body.of(reserve(item, 3, "PT15M").getBody()).uuidAt("$.id");
        UUID s = Body.of(reserve(item, 5, "PT15M").getBody()).uuidAt("$.id");
        Witness.Numbers before = Witness.read(item);
        assertThat(before.onHandCount()).isEqualTo(10);
        assertThat(before.held()).isEqualTo(8);
        return new Scene(item, r, s);
    }

    private String newItem(int onHand) {
        String item = "exit-" + UUID.randomUUID();
        assertThat(tryAdjust(item, onHand).getStatusCode()).isEqualTo(HttpStatus.OK);
        return item;
    }

    private ResponseEntity<String> reserve(String item, int quantity, String hold) {
        ResponseEntity<String> answer = tryReserve(item, quantity, hold);
        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return answer;
    }

    /** A reserve under test: its answer is checked after the witness, not here. */
    private ResponseEntity<String> tryReserve(String item, int quantity, String hold) {
        return door.postForEntity("/items/" + item + "/reservations",
                Map.of("quantity", quantity, "hold", hold), String.class);
    }

    /** A correction under test: its answer is checked after the witness, not here. */
    private ResponseEntity<String> tryAdjust(String item, int onHand) {
        return door.postForEntity("/items/" + item + "/adjustments",
                Map.of("onHandCount", onHand), String.class);
    }

    private ResponseEntity<String> consume(UUID reservation) {
        return door.postForEntity("/reservations/" + reservation + "/consume", null, String.class);
    }

    private ResponseEntity<String> release(UUID reservation) {
        return door.postForEntity("/reservations/" + reservation + "/release", null, String.class);
    }
}
