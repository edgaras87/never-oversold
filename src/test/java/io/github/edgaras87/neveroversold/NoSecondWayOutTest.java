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
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.lowersANumber;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.tablesRead;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.tablesWritten;
import static io.github.edgaras87.neveroversold.testsupport.LedgerSql.withItems;
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
 * the tripwire in front of it, failing at build time and naming the
 * file. Three rules: receipts and the units held are written by the
 * ledger alone; the ledger writes its SQL as text blocks, so each
 * statement is read; and inside it, numbers fall only in the statement
 * that wrote the receipt, by the receipts that statement wrote.
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

    private static String read(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
