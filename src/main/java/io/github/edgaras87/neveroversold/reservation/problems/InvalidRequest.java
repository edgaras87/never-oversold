package io.github.edgaras87.neveroversold.reservation.problems;

/** The request means nothing (FC1); answered {@code 400}, the numbers untouched. */
public class InvalidRequest extends RuntimeException {

    public InvalidRequest(String message) {
        super(message);
    }
}
