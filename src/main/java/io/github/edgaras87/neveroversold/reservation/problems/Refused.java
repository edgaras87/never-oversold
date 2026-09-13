package io.github.edgaras87.neveroversold.reservation.problems;

/**
 * A valid request the ledger cannot honour as asked (ADR-0010): the
 * units do not fit, or the count would sit under the units held.
 * Answered {@code 409}. Not an error of the caller's making — the world
 * is simply smaller than they hoped.
 */
public class Refused extends RuntimeException {

    public Refused(String message) {
        super(message);
    }
}
