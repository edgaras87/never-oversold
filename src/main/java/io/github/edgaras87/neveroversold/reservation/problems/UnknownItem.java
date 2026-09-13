package io.github.edgaras87.neveroversold.reservation.problems;

/** The path names an item the ledger does not know (ADR-0011); answered {@code 404}. */
public class UnknownItem extends RuntimeException {

    public UnknownItem(String item) {
        super("no item " + item + " is known to the ledger");
    }
}
