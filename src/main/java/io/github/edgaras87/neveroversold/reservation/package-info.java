/**
 * The reservation ledger — the one area the definition names, holding
 * the per-item on-hand-count, the reservations, and the admit decision.
 * What has behaviour or writes lives here at the root, package-private:
 * the compiler is the boundary (ADR-0008), and nothing outside this
 * package writes the numbers. What is stateless vocabulary lives in the
 * two sub-packages, public — a value or an answer makes no promise a
 * future feature could abuse.
 *
 * <p>Read it in three groups, in this order.
 *
 * <p><b>The door</b> — what a caller touches (ADR-0010):
 * <ul>
 *   <li>{@link ReservationController} — the two paths, reserve and
 *       adjust; the request bodies are records nested inside it</li>
 *   <li>{@link DoorProblems} — every non-success as Problem Details:
 *       invalid 400, unknown 404, refused 409</li>
 * </ul>
 *
 * <p><b>The ledger</b> — the statements to the store, the one entry path
 * to the numbers (slice record, §7):
 * <ul>
 *   <li>{@link Ledger} — reserve and adjust, one transaction each; the
 *       decision is the commit</li>
 *   <li>{@link Item}, {@link Reservation} — what the store holds, as
 *       persisted, mirrored flat into every answer. Plain fields, not
 *       the value types: the store already guarantees these rows, and
 *       the answer's JSON stays flat for free. If a reservation ever
 *       gains behaviour of its own, that is when it earns the types.</li>
 * </ul>
 *
 * <p><b>The vocabulary</b>, in {@code values} and {@code problems}:
 * <ul>
 *   <li>{@link io.github.edgaras87.neveroversold.reservation.values} —
 *       {@code ItemId}, {@code Quantity}, {@code Hold},
 *       {@code OnHandCount}: parsed at the door; a bad value refuses to
 *       exist, so nonsense never reaches the decision (G6)</li>
 *   <li>{@link io.github.edgaras87.neveroversold.reservation.problems} —
 *       {@code InvalidRequest}, {@code UnknownItem}, {@code Refused}:
 *       the three answers besides success, one per status, mapped by
 *       {@link DoorProblems}. {@code values} depends on {@code problems};
 *       nothing points back.</li>
 * </ul>
 *
 * <p>No repository, no service layer: one store, one writer; depth is
 * earned per feature, never stamped. A sub-package for behaviour is
 * earned when a coherent cluster appears — the exits, most likely — and
 * is then a logged revision of ADR-0008, since its seam classes become
 * public and that boundary moves from the compiler to convention.
 */
package io.github.edgaras87.neveroversold.reservation;
