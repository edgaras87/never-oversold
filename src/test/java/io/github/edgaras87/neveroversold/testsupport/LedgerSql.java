package io.github.edgaras87.neveroversold.testsupport;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.delete.ParenthesedDelete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.insert.ParenthesedInsert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.ParenthesedUpdate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * The ledger's SQL, read as statements for the structural tests (SL-3's
 * E7). The ledger writes each statement as a text block; this finds them
 * in its source, parses each with JSqlParser, and answers questions about
 * a statement's parts — what it writes, what it reads, whether it lowers a
 * number — so a test can say what an exit is without spelling it.
 *
 * <p>Source, not a run: a statement assembled at runtime from pieces, or
 * split across a concatenation, is not seen. That is why the wall is the
 * store's (V3); the tests built on this are the tripwire in front of it.
 */
public final class LedgerSql {

    public static final Path LEDGER = Path.of("src", "main", "java",
            "io/github/edgaras87/neveroversold/reservation/Ledger.java");

    private static final Pattern TEXT_BLOCK = Pattern.compile("(?s)\"\"\"(.*?)\"\"\"");
    private static final Pattern STRING = Pattern.compile("\"((?:[^\"\\\\\\n]|\\\\.)*)\"");
    private static final Pattern STARTS_AS_SQL = Pattern.compile(
            "(?i)^\\s*(SELECT|INSERT|UPDATE|DELETE|WITH)\\b");

    private LedgerSql() {
    }

    /** Each text block in the ledger, parsed. One the parser cannot read fails here, named. */
    public static List<Statement> statements() {
        List<Statement> statements = new ArrayList<>();
        Matcher block = TEXT_BLOCK.matcher(source());
        while (block.find()) {
            try {
                statements.add(CCJSqlParserUtil.parse(block.group(1)));
            } catch (JSQLParserException e) {
                throw new AssertionError("the ledger's SQL could not be read: " + block.group(1), e);
            }
        }
        return statements;
    }

    /** Plain string literals in the ledger that begin as SQL — statements {@link #statements} would not see. */
    public static List<String> sqlOutsideTextBlocks() {
        List<String> found = new ArrayList<>();
        Matcher string = STRING.matcher(TEXT_BLOCK.matcher(source()).replaceAll(""));
        while (string.find()) {
            if (STARTS_AS_SQL.matcher(string.group(1)).find()) {
                found.add(string.group(1));
            }
        }
        return found;
    }

    /** A column set to itself minus something: {@code reserved = i.reserved - r.quantity}. */
    public static boolean lowersANumber(Statement sql) {
        return updateSets(sql).stream().anyMatch(set -> {
            for (int i = 0; i < set.getColumns().size(); i++) {
                String column = set.getColumn(i).getColumnName();
                if ((column.equals("reserved") || column.equals("on_hand_count"))
                        && set.getValue(i) instanceof Subtraction minus
                        && minus.getLeftExpression() instanceof Column from
                        && from.getColumnName().equals(column)) {
                    return true;
                }
            }
            return false;
        });
    }

    /** The tables a statement writes: its own target, and each of its {@code WITH}s'. */
    public static Set<String> tablesWritten(Statement sql) {
        Set<String> written = new HashSet<>();
        switch (sql) {
            case Insert insert -> written.add(insert.getTable().getName());
            case Update update -> written.add(update.getTable().getName());
            case Delete delete -> written.add(delete.getTable().getName());
            default -> { }
        }
        for (WithItem<?> with : withItems(sql)) {
            switch (with.getParenthesedStatement()) {
                case ParenthesedInsert inner -> written.add(inner.getInsert().getTable().getName());
                case ParenthesedUpdate inner -> written.add(inner.getUpdate().getTable().getName());
                case ParenthesedDelete inner -> written.add(inner.getDelete().getTable().getName());
                default -> { }
            }
        }
        return written;
    }

    /** The tables an update reads from: its {@code FROM} and its joins, subqueries opened. */
    public static Set<String> tablesRead(Update update) {
        List<FromItem> sources = new ArrayList<>();
        sources.add(update.getFromItem());
        if (update.getJoins() != null) {
            update.getJoins().stream().map(Join::getFromItem).forEach(sources::add);
        }
        Set<String> read = new HashSet<>();
        for (FromItem source : sources) {
            switch (source) {
                case null -> { }
                case Table table -> read.add(table.getName());
                case Select select -> read.addAll(new TablesNamesFinder<>().getTables((Statement) select));
                default -> { }
            }
        }
        return read;
    }

    /** A statement's {@code WITH}s, none when it has none. */
    public static List<WithItem<?>> withItems(Statement sql) {
        List<WithItem<?>> withs = switch (sql) {
            case Insert insert -> insert.getWithItemsList();
            case Update update -> update.getWithItemsList();
            case Delete delete -> delete.getWithItemsList();
            case Select select -> select.getWithItemsList();
            default -> null;
        };
        return withs == null ? List.of() : withs;
    }

    /** Every {@code SET} in the statement: its own, an upsert's, and its {@code WITH}s'. */
    private static List<UpdateSet> updateSets(Statement sql) {
        List<UpdateSet> sets = new ArrayList<>();
        switch (sql) {
            case Update update -> sets.addAll(update.getUpdateSets());
            case Insert insert when insert.getConflictAction() != null
                    && insert.getConflictAction().getUpdateSets() != null ->
                    sets.addAll(insert.getConflictAction().getUpdateSets());
            default -> { }
        }
        for (WithItem<?> with : withItems(sql)) {
            if (with.getParenthesedStatement() instanceof ParenthesedUpdate inner) {
                sets.addAll(inner.getUpdate().getUpdateSets());
            }
        }
        return sets;
    }

    private static String source() {
        try {
            return Files.readString(LEDGER);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
