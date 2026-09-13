package io.github.edgaras87.neveroversold.reservation;

import java.time.OffsetDateTime;
import java.util.UUID;

/** A reservation as persisted — the record, never a derived view (ADR-0010). */
record Reservation(UUID id, String item, int quantity, OffsetDateTime expiresAt) {
}
