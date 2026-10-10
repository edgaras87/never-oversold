package io.github.edgaras87.neveroversold;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import io.github.edgaras87.neveroversold.testsupport.LedgerSql;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.ConflictActionType;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.insert.ParenthesedInsert;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;
import org.junit.jupiter.api.Test;

import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.LEDGER;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.loweredBy;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.lowersANumber;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.tablesRead;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.tablesRewritten;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.tablesWritten;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.withItems;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.writesReceiptsOfKind;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * E7 for SL-3 — G6, every way a reservation ends faces the same rules
 * (the slice record: {@code docs/construction/sl-3-a-reservation-exits-once.md}).
 * A structural test: it reads the application's SQL as written, not a
 * run of it, in the spirit of SL-2's
 * {@link NoOrderingStateOrSecondWriterTest}, which already keeps every
 * statement inside the ledger class.
 *
 * <p>The store refuses a changed, deleted, second or untimely receipt
 * from any writer, and — its check on the units held (V3) — the numbers
 * moving without a receipt, whoever writes. That is the wall; this is
 * the early warning in front of it, failing at build time and naming
 * the file. Four rules: receipts and the units held are written by the
 * ledger alone; the ledger writes its SQL as text blocks, so each
 * statement is read; inside it, numbers fall only in the statement
 * that wrote the receipt, by the receipts that statement wrote; and no
 * statement of its updates or deletes a reservation. A fifth, SL-4's
 * E4: a consume's two moves are one statement's — a {@code consumed}
 * receipt is written exactly where the count falls, by the same units.
 *
 * <p>It reads source, not bytecode: SQL is text either way, and a
 * statement assembled at runtime from pieces would pass both. The first
 * rule casts a wide net over every file's text; the third parses each of
 * the ledger's statements ({@link LedgerSql}) and checks its parts. If a
 * later slice earns an exception, it is written here as a narrower rule
 * with its why.
 */
class NoSecondWayOutTest {

    private static final Path APPLICATION = Path.of("src", "main", "java");

    private static final Pattern WRITES_A_RECEIPT = Pattern.compile(
            "(?i)(INSERT\\s+INTO|UPDATE|DELETE\\s+FROM)\\s+reservation_exit\\b");
    private static final Pattern WRITES_THE_UNITS_HELD = Pattern.compile("(?i)\\breserved\\s*=");

    /**
     * E7 · G6 — a second writer of receipts or of the units held, outside
     * the ledger, fails it: a cleanup job, an admin path, a later feature
     * with SQL of its own.
     */
    @Test
    void receiptsAndTheUnitsHeldAreWrittenByTheLedgerAlone() {
        List<Path> writers = new ArrayList<>();
        try (Stream<Path> sources = Files.walk(APPLICATION)) {
            sources.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                String source = read(path);
                if (WRITES_A_RECEIPT.matcher(source).find() || WRITES_THE_UNITS_HELD.matcher(source).find()) {
                    writers.add(path);
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        assertThat(writers)
                .as("a second writer of reservation_exit or of reserved is a second way out (G6)")
                .containsExactly(LEDGER);
    }

    /**
     * E7 · G6 — the ledger writes SQL only as text blocks, the form the
     * next rule reads. A statement in a plain string would go unread: a
     * force release written {@code "UPDATE item SET reserved = …"} fails
     * here.
     */
    @Test
    void theLedgersSqlIsWrittenAsTextBlocks() {
        assertThat(LedgerSql.sqlOutsideTextBlocks())
                .as("SQL in a plain string is not read by the rule on exits; write it as a text block")
                .isEmpty();
    }

    /**
     * E7 · G6 — inside the ledger, every statement that lowers the units
     * held or the count writes a receipt in the same statement, and every
     * statement that writes a receipt moves the numbers by the receipts it
     * wrote — its own returned rows, the key deciding which. A statement
     * lowering a number with no receipt, or a receipt written without its
     * move, fails it. Each statement is parsed, so it is checked by its
     * parts, not its spelling.
     */
    @Test
    void numbersFallOnlyByTheReceiptsTheSameStatementWrote() {
        List<Statement> statements = LedgerSql.statements();
        assertThat(statements).as("the ledger's statements were found").isNotEmpty();

        List<Statement> exits = statements.stream()
                .filter(sql -> lowersANumber(sql) || tablesWritten(sql).contains("reservation_exit"))
                .toList();
        assertThat(exits).as("the exits' statements were found").isNotEmpty();
        assertThat(exits)
                .as("a number falls only beside its receipt, and only by what that statement wrote")
                .allSatisfy(NoSecondWayOutTest::movesTheItemByTheReceiptsItWrote);
    }

    /**
     * The one shape an exit has: an {@code UPDATE item} whose {@code WITH}
     * inserts the receipts — a second one skipped by the key, not raised —
     * returns the reservations it wrote them for, and is what the update
     * reads its units from.
     */
    private static void movesTheItemByTheReceiptsItWrote(Statement sql) {
        assertThat(sql).as("an exit moves the item row: %s", sql).isInstanceOf(Update.class);
        Update update = (Update) sql;
        assertThat(update.getTable().getName()).as("the row it moves").isEqualTo("item");
        assertThat(withItems(update)).as("one WITH, writing the receipts: %s", sql).hasSize(1);

        WithItem<?> with = withItems(update).getFirst();
        assertThat(with.getParenthesedStatement()).as("the WITH writes a row").isInstanceOf(ParenthesedInsert.class);
        Insert receipt = with.getInsert().getInsert();
        assertThat(receipt.getTable().getName()).as("the row it writes").isEqualTo("reservation_exit");
        assertThat(receipt.getConflictTarget().getIndexColumnNames()).as("the key it meets")
                .containsExactly("reservation_id");
        assertThat(receipt.getConflictAction().getConflictActionType()).as("a second receipt is skipped")
                .isEqualTo(ConflictActionType.DO_NOTHING);
        assertThat(receipt.getReturningClause()).as("it returns what it wrote").isNotNull();
        assertThat(receipt.getReturningClause().stream().map(SelectItem::toString))
                .as("it returns the reservations it wrote receipts for")
                .containsExactly("reservation_id");

        assertThat(tablesRead(update)).as("the update reads its units from the receipts it wrote")
                .contains(with.getAliasName());
    }

    /**
     * E7 · G6 — the ledger never updates or deletes a reservation. A
     * reservation ends by its receipt; removed with its units, or edited,
     * it would end with none, and the check on the units held would not
     * see it, the numbers still agreeing. Under T4 the ledger is the only
     * writer, so this is the whole of that door. A future feature that
     * means to change reservations — extending a hold — fails here, and
     * is decided rather than slipped in (the review after SL-3,
     * 2026-10-08).
     */
    @Test
    void theLedgerNeverRewritesAReservation() {
        assertThat(LedgerSql.statements())
                .as("a reservation's ending is a receipt; none of the ledger's statements may change or remove one")
                .allSatisfy(sql -> assertThat(tablesRewritten(sql)).as("%s", sql).doesNotContain("reservation"));
    }

    /**
     * E4 · G4 for SL-4 — nothing makes one of consume's moves without the
     * other (the slice record:
     * {@code docs/construction/sl-4-consumes-two-moves-hold-together.md}).
     *
     * <p>A statement writes a {@code consumed} receipt if and only if it
     * lowers {@code on_hand_count}, and then by exactly what it lowers the
     * units held by — R's 3 off the shelf and out of the held units, never
     * one without the other. Rule three already ties every fall to a
     * receipt; this ties the receipt's kind to which number falls, which
     * rule three cannot see. It fails on a consume that frees R's 3 but
     * leaves the count at 10, a release that takes R's 3 off the shelf, a
     * retry that lowers the count alone, or a count lowered by other units
     * than the hold's. Each is planted, and the rule names it.
     */
    @Test
    void aConsumedReceiptIsWrittenExactlyWhereTheCountFalls() {
        List<Statement> statements = LedgerSql.statements();
        assertThat(statements).as("the ledger's statements were found").isNotEmpty();
        assertThat(statements).as("a consume's statement was found")
                .anySatisfy(sql -> assertThat(writesReceiptsOfKind(sql, "consumed")).isTrue());

        assertThat(statements).allSatisfy(sql -> {
            boolean consumes = writesReceiptsOfKind(sql, "consumed");
            assertThat(loweredBy(sql, "on_hand_count").isPresent())
                    .as("the count falls exactly where a consumed receipt is written: %s", sql)
                    .isEqualTo(consumes);
            if (consumes) {
                assertThat(loweredBy(sql, "on_hand_count").map(Object::toString))
                        .as("the count falls by what the units held fall by: %s", sql)
                        .isPresent()
                        .isEqualTo(loweredBy(sql, "reserved").map(Object::toString));
            }
        });
    }

    private static String read(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
