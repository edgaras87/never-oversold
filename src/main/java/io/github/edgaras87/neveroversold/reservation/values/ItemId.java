package io.github.edgaras87.neveroversold.reservation.values;

import io.github.edgaras87.neveroversold.reservation.problems.InvalidRequest;

/**
 * An item's identifier as the outside gives it: the catalog is not ours,
 * so the ledger knows an item only by the opaque string a caller or
 * operator names (ADR-0010). Cannot be blank — a request naming no item
 * is nonsense at the door (G6).
 */
public record ItemId(String value) {

    public ItemId {
        if (value == null || value.isBlank()) {
            throw new InvalidRequest("an item must be named");
        }
    }
}
