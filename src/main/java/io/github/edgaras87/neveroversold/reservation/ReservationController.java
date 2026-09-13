package io.github.edgaras87.neveroversold.reservation;

import io.github.edgaras87.neveroversold.reservation.values.Hold;
import io.github.edgaras87.neveroversold.reservation.values.ItemId;
import io.github.edgaras87.neveroversold.reservation.values.OnHandCount;
import io.github.edgaras87.neveroversold.reservation.values.Quantity;
import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The door (ADR-0010): resource-shaped paths, bodies with the
 * definition's terms, the record as persisted in every answer. Parses
 * each request into values that cannot be nonsense before any statement
 * runs (G6); the answers for invalid, unknown and refused are
 * {@link DoorProblems}'.
 */
@RestController
class ReservationController {

    private final Ledger ledger;

    ReservationController(Ledger ledger) {
        this.ledger = ledger;
    }

    /** {@code {"quantity": 3, "hold": "PT15M"}} — the hold an ISO-8601 duration. */
    record ReserveRequest(Integer quantity, Duration hold) {
    }

    /** {@code {"onHandCount": 10}} — the count the operator asserts (ADR-0011). */
    record AdjustRequest(Integer onHandCount) {
    }

    @PostMapping("/items/{item}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    Reservation reserve(@PathVariable String item, @RequestBody ReserveRequest request) {
        return ledger.reserve(new ItemId(item), Quantity.of(request.quantity()), new Hold(request.hold()));
    }

    @PostMapping("/items/{item}/adjustments")
    Item adjust(@PathVariable String item, @RequestBody AdjustRequest request) {
        return ledger.adjust(new ItemId(item), OnHandCount.of(request.onHandCount()));
    }
}
