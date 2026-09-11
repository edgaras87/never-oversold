package io.github.edgaras87.neveroversold.probe;

import java.util.Map;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SCAFFOLDING — dies when SL-1 lands.
 *
 * <p>One real round-trip to the store as the runtime identity, so the
 * bootstrap's evidence harness has a full HTTP → application → store
 * path to attack. It answers with the identity the store sees, so every
 * asserted response witnesses both the door and the authority, and with
 * the process id, so evidence that races several instances can tell
 * which process served it. Carries no business meaning and must never
 * grow any: the first slice replaces it with its own door, born from its
 * invariant, never extended out of this.
 */
@RestController
class GroundProbeController {

    private final JdbcClient jdbc;

    GroundProbeController(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/probe/ground")
    Map<String, String> groundRoundTrip() {
        String identity = jdbc.sql("SELECT current_user").query(String.class).single();
        return Map.of(
                "identity", identity,
                "pid", Long.toString(ProcessHandle.current().pid()));
    }
}
