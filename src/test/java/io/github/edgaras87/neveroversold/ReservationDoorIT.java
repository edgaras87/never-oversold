package io.github.edgaras87.neveroversold;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import io.github.edgaras87.neveroversold.testsupport.WebDatabaseIT;
import io.github.edgaras87.neveroversold.testsupport.Body;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The door under ADR-0010, one request at a time — no adversity yet, the
 * storms are the evidence tests' (E1–E5). What this holds: the three
 * kinds of answer with their statuses and Problem Details bodies; an
 * item becoming known by its first adjustment (ADR-0011); every answer
 * carrying the record as persisted; and E6 — nonsense never reaching
 * the decision, the numbers untouched (G6, FC1).
 */
class ReservationDoorIT extends WebDatabaseIT {

    @Autowired
    private TestRestTemplate door;

    @Autowired
    private JdbcClient store;

    @Test
    void anItemBecomesKnownByItsFirstAdjustment() {
        String item = newItemId();

        ResponseEntity<String> response = adjust(item, Map.of("onHandCount", 10));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Body body = Body.of(response.getBody());
        assertThat(body.stringAt("$.id")).isEqualTo(item);
        assertThat(body.intAt("$.onHandCount")).isEqualTo(10);
        assertThat(body.intAt("$.reserved")).isZero();
        assertThat(count(item)).isEqualTo(10);
        assertThat(held(item)).isZero();
    }

    @Test
    void aReserveThatFitsIsAdmittedAndRecorded() {
        String item = newItemId();
        adjust(item, Map.of("onHandCount", 10));

        ResponseEntity<String> response = reserve(item, Map.of("quantity", 3, "hold", "PT15M"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Body body = Body.of(response.getBody());
        assertThat(body.stringAt("$.item")).isEqualTo(item);
        assertThat(body.intAt("$.quantity")).isEqualTo(3);
        assertThat(body.has("$.expiresAt")).as("the persisted expiry is in the answer").isTrue();
        assertThat(body.uuidAt("$.id")).as("the reservation's identifier is the ledger's own").isNotNull();
        assertThat(held(item)).isEqualTo(3);
        Integer recorded = store.sql(
                        "SELECT count(*) FROM reservation WHERE item_id = :item AND quantity = 3")
                .param("item", item).query(Integer.class).single();
        assertThat(recorded).isEqualTo(1);
    }

    @Test
    void aReserveThatDoesNotFitIsRefusedAndMovesNothing() {
        String item = newItemId();
        adjust(item, Map.of("onHandCount", 5));
        reserve(item, Map.of("quantity", 4, "hold", "PT15M"));

        ResponseEntity<String> response = reserve(item, Map.of("quantity", 2, "hold", "PT15M"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        Body problem = Body.of(response.getBody());
        assertThat(problem.stringAt("$.title")).isEqualTo("refused");
        assertThat(problem.intAt("$.status")).isEqualTo(409);
        assertThat(held(item)).isEqualTo(4);
        assertThat(count(item)).isEqualTo(5);
    }

    @Test
    void aReserveOnAnUnknownItemIsUnknown() {
        ResponseEntity<String> response = reserve(newItemId(), Map.of("quantity", 1, "hold", "PT1M"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(Body.of(response.getBody()).stringAt("$.title")).isEqualTo("unknown item");
    }

    @Test
    void anAdjustmentUnderTheHeldUnitsIsRefused() {
        // provisional: SL-2 decides the correction's shape; until then the
        // count never drops under what reservations hold
        String item = newItemId();
        adjust(item, Map.of("onHandCount", 10));
        reserve(item, Map.of("quantity", 7, "hold", "PT15M"));

        ResponseEntity<String> response = adjust(item, Map.of("onHandCount", 6));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Body.of(response.getBody()).stringAt("$.title")).isEqualTo("refused");
        assertThat(count(item)).isEqualTo(10);
        assertThat(held(item)).isEqualTo(7);
    }

    static Stream<Arguments> nonsense() {
        return Stream.of(
                Arguments.of("quantity zero", "{\"quantity\":0,\"hold\":\"PT1M\"}"),
                Arguments.of("quantity negative", "{\"quantity\":-3,\"hold\":\"PT1M\"}"),
                Arguments.of("quantity beyond the bound", "{\"quantity\":1000001,\"hold\":\"PT1M\"}"),
                Arguments.of("quantity missing", "{\"hold\":\"PT1M\"}"),
                Arguments.of("hold zero", "{\"quantity\":1,\"hold\":\"PT0S\"}"),
                Arguments.of("hold beyond the bound", "{\"quantity\":1,\"hold\":\"P8D\"}"),
                Arguments.of("hold missing", "{\"quantity\":1}"),
                Arguments.of("hold not a duration", "{\"quantity\":1,\"hold\":\"soon\"}"),
                Arguments.of("body not JSON", "this is not a request"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("nonsense")
    void nonsenseIsInvalidAndMovesNothing(String shape, String body) {
        String item = newItemId();
        adjust(item, Map.of("onHandCount", 10));

        ResponseEntity<String> response = post("/items/" + item + "/reservations", body);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(Body.of(response.getBody()).stringAt("$.title")).isEqualTo("invalid request");
        assertThat(held(item)).isZero();
        assertThat(count(item)).isEqualTo(10);
    }

    @Test
    void aNegativeCountIsInvalid() {
        ResponseEntity<String> response = post("/items/" + newItemId() + "/adjustments", "{\"onHandCount\":-1}");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Body.of(response.getBody()).stringAt("$.title")).isEqualTo("invalid request");
    }

    private ResponseEntity<String> reserve(String item, Map<String, Object> body) {
        return door.postForEntity("/items/" + item + "/reservations", body, String.class);
    }

    private ResponseEntity<String> adjust(String item, Map<String, Object> body) {
        return door.postForEntity("/items/" + item + "/adjustments", body, String.class);
    }

    private ResponseEntity<String> post(String path, String rawJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return door.postForEntity(path, new HttpEntity<>(rawJson, headers), String.class);
    }

    private static String newItemId() {
        return "door-" + UUID.randomUUID();
    }

    private int count(String item) {
        return store.sql("SELECT on_hand_count FROM item WHERE id = :item").param("item", item)
                .query(Integer.class).single();
    }

    private int held(String item) {
        return store.sql("SELECT reserved FROM item WHERE id = :item").param("item", item)
                .query(Integer.class).single();
    }
}
