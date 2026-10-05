package io.github.edgaras87.neveroversold;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

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
 * from any writer. What it cannot refuse is the application moving the
 * numbers without one: a "force release" lowering the units held by
 * hand ends a reservation in the numbers while its receipt never says
 * so, and nothing stops the next exit taking the same units again. So
 * two rules: receipts and the units held are written by the ledger
 * alone; and inside it, numbers fall only in the statement that wrote
 * the receipt, by the receipts that statement wrote.
 *
 * <p>It reads source, not bytecode: SQL is text either way, and a
 * statement assembled at runtime from pieces would pass both. That is
 * why the walls are the store's; this keeps the decision on one path.
 * If a later slice earns an exception, it is written here as a narrower
 * rule with its why.
 */
class NoSecondWayOutTest {

    private static final Path APPLICATION = Path.of("src", "main", "java");
    private static final Path LEDGER = APPLICATION.resolve(
            "io/github/edgaras87/neveroversold/reservation/Ledger.java");

    private static final Pattern WRITES_A_RECEIPT = Pattern.compile(
            "(?i)(INSERT\\s+INTO|UPDATE|DELETE\\s+FROM)\\s+reservation_exit\\b");
    private static final Pattern WRITES_THE_UNITS_HELD = Pattern.compile("(?i)\\breserved\\s*=");
    private static final Pattern LOWERS_A_NUMBER = Pattern.compile(
            "(?i)\\b(reserved|on_hand_count)\\s*=\\s*(\\w+\\.)?\\1\\s*-");
    private static final Pattern TEXT_BLOCK = Pattern.compile("(?s)\"\"\"(.*?)\"\"\"");

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
     * E7 · G6 — inside the ledger, every statement that lowers the units
     * held or the count writes a receipt in the same statement, and every
     * statement that writes a receipt moves the numbers by the receipts it
     * wrote — its own returned rows, the key deciding which. A statement
     * lowering a number with no receipt, or a receipt written without its
     * move, fails it.
     */
    @Test
    void numbersFallOnlyByTheReceiptsTheSameStatementWrote() {
        List<String> statements = new ArrayList<>();
        Matcher block = TEXT_BLOCK.matcher(read(LEDGER));
        while (block.find()) {
            statements.add(block.group(1));
        }
        assertThat(statements).as("the ledger's statements were found").isNotEmpty();

        List<String> exits = statements.stream()
                .filter(sql -> LOWERS_A_NUMBER.matcher(sql).find() || WRITES_A_RECEIPT.matcher(sql).find())
                .toList();
        assertThat(exits).as("the exits' statements were found").isNotEmpty();
        assertThat(exits)
                .as("a number falls only beside its receipt, and only by what that statement wrote")
                .allSatisfy(sql -> assertThat(sql)
                        .contains("WITH receipt AS (")
                        .contains("INSERT INTO reservation_exit")
                        .contains("ON CONFLICT (reservation_id) DO NOTHING")
                        .contains("RETURNING reservation_id")
                        .contains("UPDATE item")
                        .contains("FROM receipt"));
    }

    private static String read(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
