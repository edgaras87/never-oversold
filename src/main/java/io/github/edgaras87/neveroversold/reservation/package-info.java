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
 *   <li>{@link ReservationController} — the four paths: reserve and
 *       adjust, and the two exits, consume and release, which take no
 *       body; the request bodies are records nested inside it</li>
 *   <li>{@link DoorProblems} — every non-success as Problem Details:
 *       invalid 400, unknown item or reservation 404, refused 409, and
 *       the store lost mid-request 503, "outcome unknown" (ADR-0016)</li>
 * </ul>
 *
 * <p><b>The ledger</b> — the statements to the store, the one entry path
 * to the numbers (SL-1's record, "What the walls need"):
 * <ul>
 *   <li>{@link Ledger} — reserve, adjust, consume and release, one
 *       transaction each; the decision is the commit, and a store lost
 *       before it is told apart from one that refused</li>
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
 *       {@code OnHandCount}, {@code ReservationId}: parsed at the door;
 *       a bad value refuses to exist, so it never reaches a decision
 *       (SL-1 G6)</li>
 *   <li>{@link io.github.edgaras87.neveroversold.reservation.problems} —
 *       {@code InvalidRequest}, {@code UnknownItem},
 *       {@code UnknownReservation}, {@code Refused},
 *       {@code StoreOutOfReach}: the answers besides
 *       success, one per kind, mapped by
 *       {@link DoorProblems}. {@code values} depends on {@code problems};
 *       nothing points back.</li>
 * </ul>
 *
 * <p>No repository and no service layer: depth is earned per feature
 * (ADR-0008). A sub-package for behaviour would make its seam classes
 * public, so it waits for a revision of ADR-0008. The exits did not earn
 * one: they are two more statements in {@link Ledger}.
 */
package io.github.edgaras87.neveroversold.reservation;
