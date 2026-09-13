package io.github.edgaras87.neveroversold.reservation.values;

import io.github.edgaras87.neveroversold.reservation.problems.InvalidRequest;

/**
 * Units asked for: a positive whole number within the door's stated
 * bound (FC1, folded into SL-1 as G6). A value that cannot exist cannot
 * reach the admit — this type is the door's wall for nonsense; the
 * store's {@code quantity > 0} constraint is the backstop behind it.
 */
public record Quantity(int units) {

    public static final int BOUND = 1_000_000;

    public Quantity {
        if (units < 1 || units > BOUND) {
            throw new InvalidRequest("quantity must be a whole number from 1 to " + BOUND);
        }
    }

    public static Quantity of(Integer units) {
        if (units == null) {
            throw new InvalidRequest("quantity is required");
        }
        return new Quantity(units);
    }
}
