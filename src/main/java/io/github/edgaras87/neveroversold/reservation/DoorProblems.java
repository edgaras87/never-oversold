package io.github.edgaras87.neveroversold.reservation;

import io.github.edgaras87.neveroversold.reservation.problems.InvalidRequest;
import io.github.edgaras87.neveroversold.reservation.problems.Refused;
import io.github.edgaras87.neveroversold.reservation.problems.StoreOutOfReach;
import io.github.edgaras87.neveroversold.reservation.problems.UnknownItem;
import io.github.edgaras87.neveroversold.reservation.problems.UnknownReservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Every non-success answer as Problem Details, RFC 9457 (ADR-0010):
 * invalid, unknown and refused, the title in the definition's words and
 * the detail saying what did not fit or what was wrong. A refusal is not
 * an invalid request: the first is a valid ask the ledger cannot honour,
 * the second means nothing and moves nothing.
 *
 * <p>A fourth, ADR-0016: an instance that lost the store mid-request
 * cannot know whether its write committed, and says so — {@code 503},
 * "outcome unknown". Which failures those are is the ledger's to say, the
 * one class that knows the store.
 */
@RestControllerAdvice
class DoorProblems {

    @ExceptionHandler(InvalidRequest.class)
    ProblemDetail invalid(InvalidRequest e) {
        return problem(HttpStatus.BAD_REQUEST, "invalid request", e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail unreadable(HttpMessageNotReadableException e) {
        return problem(HttpStatus.BAD_REQUEST, "invalid request", "the body could not be read as the request it should be");
    }

    @ExceptionHandler(UnknownItem.class)
    ProblemDetail unknown(UnknownItem e) {
        return problem(HttpStatus.NOT_FOUND, "unknown item", e.getMessage());
    }

    @ExceptionHandler(UnknownReservation.class)
    ProblemDetail unknown(UnknownReservation e) {
        return problem(HttpStatus.NOT_FOUND, "unknown reservation", e.getMessage());
    }

    @ExceptionHandler(Refused.class)
    ProblemDetail refused(Refused e) {
        return problem(HttpStatus.CONFLICT, "refused", e.getMessage());
    }

    @ExceptionHandler(StoreOutOfReach.class)
    ProblemDetail outcomeUnknown(StoreOutOfReach e) {
        return problem(HttpStatus.SERVICE_UNAVAILABLE, "outcome unknown", e.getMessage());
    }

    private static ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return problem;
    }
}
