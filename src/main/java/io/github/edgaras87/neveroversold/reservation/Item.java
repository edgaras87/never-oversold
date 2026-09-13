package io.github.edgaras87.neveroversold.reservation;

/** An item as persisted: its count and the units held by reservations not yet ended. */
record Item(String id, int onHandCount, int reserved) {
}
