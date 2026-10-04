package io.github.edgaras87.neveroversold.reservation.problems;

import java.util.UUID;

/** The path names a reservation the ledger has no record of; answered {@code 404}, nothing moved. */
public class UnknownReservation extends RuntimeException {

    public UnknownReservation(UUID reservation) {
        super("no reservation " + reservation + " is known to the ledger");
    }
}
