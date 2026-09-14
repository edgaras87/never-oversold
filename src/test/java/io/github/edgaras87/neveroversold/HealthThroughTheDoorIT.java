package io.github.edgaras87.neveroversold;

import io.github.edgaras87.neveroversold.testsupport.WebDatabaseIT;
import io.github.edgaras87.neveroversold.testsupport.Body;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The world-is-up check every evidence run gates on, taken through the
 * real door against the harness's own store: health answers UP and names
 * the store's component UP. Earns the web-plus-database base.
 */
class HealthThroughTheDoorIT extends WebDatabaseIT {

    @Autowired
    private TestRestTemplate door;

    @Test
    void healthIsUpWithTheStoreUp() {
        ResponseEntity<String> response = door.getForEntity("/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Body body = Body.of(response.getBody());
        assertThat(body.stringAt("$.status")).isEqualTo("UP");
        assertThat(body.stringAt("$.components.db.status")).isEqualTo("UP");
    }
}
