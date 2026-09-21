package io.github.edgaras87.neveroversold;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import io.github.edgaras87.neveroversold.reservation.values.OnHandCount;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.support.TransactionTemplate;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * E5 for SL-2 — the structural half, a test that reads the code rather
 * than the runtime. Two of this slice's guarantees are held by what the
 * application does <em>not</em> contain, and an absence survives only
 * while something watches for it being filled in:
 *
 * <ul>
 *   <li>G5, no ordering state: the ledger keeps no order of its own, so
 *       no arrival order can put the count under the held units (kill
 *       8). The face was chosen against two others in §8 — an
 *       operator-supplied instant and a ledger-assigned sequence — and
 *       either would show up first as a second thing carried at the
 *       door.</li>
 *   <li>G6, one writing path: every change to the on-hand-count faces
 *       the same comparison, by whatever path it arrives (P1). The
 *       store's constraint keeps the <em>state</em> safe from any
 *       writer; this keeps the <em>decision</em> from being made
 *       somewhere else.</li>
 * </ul>
 *
 * <p>If a later slice earns an exception, it is written here as a
 * narrower rule with its why — never deleted quietly.
 */
class NoOrderingStateOrSecondWriterTest {

    private static final JavaClasses LEDGER = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("io.github.edgaras87.neveroversold");

    /**
     * E5 · G5 — kill 8.
     *
     * <p>The adjustment request carries one field: the count the operator
     * asserts. Add a timestamp, a version or a sequence number and this
     * fails — that is an order for the ledger to judge by, and keeping none
     * is the face §8 chose over two others.
     */
    @Test
    void anAdjustmentCarriesNothingButTheAssertedCount() throws Exception {
        Class<?> request = Class.forName(
                "io.github.edgaras87.neveroversold.reservation.ReservationController$AdjustRequest");

        List<String> carried = Arrays.stream(request.getRecordComponents())
                .map(RecordComponent::getName)
                .toList();

        assertThat(carried)
                .as("a version, a sequence or an 'as of' instant here is an order the "
                        + "ledger would judge by — §8's G5 chose to keep none")
                .containsExactly("onHandCount");
    }

    /**
     * E5 · G6 — P1, every change to the count faces the same comparison.
     *
     * <p>Two halves. Only the ledger class reaches the store, and only one
     * of its methods takes a count. A second way in — a force-adjust, a
     * bulk importer, an admin endpoint — trips one half or the other.
     *
     * <p>The store's constraint already keeps the <em>state</em> safe from
     * any writer at all. What this keeps safe is the <em>decision</em>: a
     * path that writes the count without the comparison refuses nothing,
     * and the constraint turns its mistake into an error rather than an
     * orderly refusal.
     */
    @Test
    void theCountHasOneWritingPath() {
        noClasses().that().haveSimpleNameNotEndingWith("Ledger")
                .should().dependOnClassesThat().belongToAnyOf(JdbcClient.class, TransactionTemplate.class)
                .because("a second class reaching the store is a second decision path for the count (G6)")
                .check(LEDGER);

        List<Method> takeACount = Arrays.stream(
                        classOf("io.github.edgaras87.neveroversold.reservation.Ledger").getDeclaredMethods())
                .filter(method -> !method.isSynthetic())   // the compiler's lambdas carry it too
                .filter(method -> Arrays.asList(method.getParameterTypes()).contains(OnHandCount.class))
                .toList();

        assertThat(takeACount)
                .as("one method changes the count; a second is a second path, and the "
                        + "constraint would turn its mistake into an error, not a refusal")
                .hasSize(1);
    }

    private static Class<?> classOf(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException absent) {
            throw new IllegalStateException(name + " is not on the classpath", absent);
        }
    }
}
