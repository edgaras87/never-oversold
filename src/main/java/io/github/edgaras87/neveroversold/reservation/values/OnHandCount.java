package io.github.edgaras87.neveroversold.reservation.values;

import io.github.edgaras87.neveroversold.reservation.problems.InvalidRequest;

/**
 * The count an operator asserts for an item (ADR-0011: an adjustment
 * states the on-hand-count, never a difference). A whole number that is
 * not negative; nonsense stops here (G6).
 */
public record OnHandCount(int units) {

    public OnHandCount {
        if (units < 0) {
            throw new InvalidRequest("onHandCount must be a whole number that is not negative");
        }
    }

    public static OnHandCount of(Integer units) {
        if (units == null) {
            throw new InvalidRequest("onHandCount is required");
        }
        return new OnHandCount(units);
    }
}
