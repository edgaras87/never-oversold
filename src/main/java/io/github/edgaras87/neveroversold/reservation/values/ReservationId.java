package io.github.edgaras87.neveroversold.reservation.values;

import java.util.UUID;

import io.github.edgaras87.neveroversold.reservation.problems.InvalidRequest;

/**
 * A reservation's identifier, as the store assigned it and the reserve
 * answered it. An exit names its reservation by this and nothing else
 * (FC2); a path that is not one is nonsense at the door (G6), answered
 * before any statement runs.
 */
public record ReservationId(UUID value) {

    public static ReservationId of(String raw) {
        try {
            return new ReservationId(UUID.fromString(raw));
        } catch (IllegalArgumentException notAnIdentifier) {
            throw new InvalidRequest("a reservation is named by the identifier its reserve answered");
        }
    }
}
