package io.github.edgaras87.neveroversold.reservation.problems;

/**
 * The store was lost mid-request, and the ledger cannot know whether its
 * write committed (ADR-0016, F19). Answered {@code 503}, "outcome
 * unknown". Not a refusal — the store never said no — and not a defect:
 * the request may or may not have taken effect, and only the store knows.
 */
public class StoreOutOfReach extends RuntimeException {

    public StoreOutOfReach(Throwable lost) {
        super("the store was out of reach mid-request: the request may or may not have taken effect", lost);
    }
}
