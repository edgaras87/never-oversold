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
 * that does not exist, and an exit after expiry.
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
     * taking R's units first, needs the reserve to free expired holds
     * itself, and lands with that.
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
        assertThat(door.postForEntity("/items/" + item + "/adjustments",
                Map.of("onHandCount", onHand), String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
        return item;
    }

    private ResponseEntity<String> reserve(String item, int quantity, String hold) {
        ResponseEntity<String> answer = door.postForEntity("/items/" + item + "/reservations",
                Map.of("quantity", quantity, "hold", hold), String.class);
        assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return answer;
    }

    private ResponseEntity<String> consume(UUID reservation) {
        return door.postForEntity("/reservations/" + reservation + "/consume", null, String.class);
    }

    private ResponseEntity<String> release(UUID reservation) {
        return door.postForEntity("/reservations/" + reservation + "/release", null, String.class);
    }
}
