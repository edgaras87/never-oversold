package io.github.edgaras87.neveroversold.reservation.values;

import java.time.Duration;

import io.github.edgaras87.neveroversold.reservation.problems.InvalidRequest;

/**
 * How long a reservation holds its units: the caller's to state (the
 * duration policy is refused at L2), positive and within the door's
 * stated bound. Carried to the store as seconds; the expiry instant
 * itself is the store's clock plus this (G5) — the ledger never reads a
 * clock.
 */
public record Hold(Duration duration) {

    public static final Duration MIN = Duration.ofSeconds(1);
    public static final Duration MAX = Duration.ofDays(7);

    public Hold {
        if (duration == null) {
            throw new InvalidRequest("hold is required, an ISO-8601 duration such as PT15M");
        }
        if (duration.compareTo(MIN) < 0 || duration.compareTo(MAX) > 0) {
            throw new InvalidRequest("hold must be from " + MIN + " to " + MAX);
        }
    }

    public long seconds() {
        return duration.toSeconds();
    }
}
