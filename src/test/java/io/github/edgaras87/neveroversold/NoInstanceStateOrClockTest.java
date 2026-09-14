package io.github.edgaras87.neveroversold;

import java.time.Clock;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.JavaAccess.Predicates.target;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.name;
import static com.tngtech.archunit.core.domain.properties.HasOwner.Predicates.With.owner;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;

/**
 * E5, the structural half, and G2's — a test that reads the compiled
 * code, not the runtime. Two guarantees are held by what the application
 * does <em>not</em> contain, and an absence cannot be seen by running
 * anything:
 *
 * <ul>
 *   <li>G5, one clock: the ledger never asks its own process what time
 *       it is. Expiry is set and judged by the store's clock; an
 *       instance's skew cannot enter what it never supplies. So no call
 *       to any {@code now()} in {@code java.time}, no dependency on
 *       {@code Clock} or {@code Date}, no {@code System.currentTimeMillis}
 *       or {@code nanoTime} — as calls or as method references.</li>
 *   <li>G2, no state outside the store: the ledger keeps no item
 *       numbers in memory, so two instances cannot disagree. So no field
 *       of a map, collection or atomic type, and no mutable static
 *       field.</li>
 * </ul>
 *
 * <p>Rules on bytecode (ArchUnit): a static import, a method reference,
 * a wrapper of our own around a forbidden call, or a reformat cannot
 * slip past. Only the application's classes are imported — never the
 * tests, which may do all of this. If a later slice earns an exception,
 * it is written here as a narrower rule with its why.
 */
class NoInstanceStateOrClockTest {

    private static final JavaClasses LEDGER = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("io.github.edgaras87.neveroversold");

    @Test
    void theLedgerConsultsNoProcessClock() {
        // accesses, not only calls: a method reference such as Instant::now is an access too
        noClasses().should().accessTargetWhere(target(name("now")).and(target(owner(resideInAPackage("java.time..")))))
                .because("expiry is set and judged by the store's clock, never an instance's (G5)")
                .check(LEDGER);
        noClasses().should().dependOnClassesThat().belongToAnyOf(Clock.class, Date.class)
                .because("a Clock, or a Date, is a process clock by another name (G5)")
                .check(LEDGER);
        noClasses().should().accessTargetWhere(
                        target(name("currentTimeMillis").or(name("nanoTime"))).and(target(owner(type(System.class)))))
                .because("the JVM's time is an instance's own (G5)")
                .check(LEDGER);
    }

    @Test
    void theLedgerKeepsNoStateOutsideTheStore() {
        noFields().should().haveRawType(assignableTo(Map.class)
                        .or(assignableTo(Collection.class))
                        .or(resideInAPackage("java.util.concurrent.atomic")))
                .because("the row is the only state; nothing kept in memory can take part in an admit (G2)")
                .check(LEDGER);
        fields().that().areStatic().should().beFinal()
                .because("a mutable static is shared state an instance holds outside the store (G2)")
                .check(LEDGER);
    }
}
