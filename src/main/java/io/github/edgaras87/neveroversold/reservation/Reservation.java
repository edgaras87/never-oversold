package io.github.edgaras87.neveroversold.reservation;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A reservation as persisted — the record, never a derived view
 * (ADR-0010). {@code endedBy} is the exit that ended it — consume,
 * release or expiry — and {@code endedAt} the store's instant for it;
 * both are absent from the answer while it has not ended, so an
 * active reservation reads as it did before exits existed.
 */
record Reservation(UUID id, String item, int quantity, OffsetDateTime expiresAt,
                   @JsonInclude(JsonInclude.Include.NON_NULL) String endedBy,
                   @JsonInclude(JsonInclude.Include.NON_NULL) OffsetDateTime endedAt) {
}
