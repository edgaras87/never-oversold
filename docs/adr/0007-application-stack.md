# 0007. Application stack: Spring Boot 4, Java 21, Maven

Date: 2026-09-11
Status: Accepted

## Context

The bootstrap (PLAN Step 4) brings the system to life on the ground
ADR-0004–0006 established: PostgreSQL 17 under a two-role split,
schema changes only through Flyway as `migrator`
(`docs/infrastructure/infrastructure-contract.md`), rootless podman
as the container runtime. The stack is decided before any code
exists, and the deciding inputs are **fluency and audience**: which
stack the builder is proven on, and what the intended reader of
this codebase reads fluently. The correctness work must be the
visible substance; stack novelty would compete with the claim for
the reader's attention (`docs/system/intent.md`, who this is for).

What the slices demand of a stack is little and general: an HTTP
door, a connection to the store as one identity, transactions,
plain SQL, and a test harness that can run more than one instance
of the system at once (ADR-0009). Every serious backend stack
offers these; the slices do not discriminate.

## Options considered

1. **Spring Boot 4.x · Java 21 · Maven.** The line the method's
   references were lived on (the bootstrap skill's walkthrough and
   harness reference), with their traps recorded; the builder is
   proven on it from the earlier project this one is spun off from;
   the host already carries JDK 21.0.11 and Maven 3.9.9; the
   portfolio reader of a Java backend reads it without translation.
2. **Another JVM stack** — Kotlin/Ktor, Quarkus, Micronaut. Same
   language family, but no lived references and no recorded traps;
   the novelty would be the visible substance. Rejected.
3. **A non-JVM stack** — Go, Node, Python, Rust. The builder is not
   proven on any for this work; the reader's fluency is uncertain;
   every reference in the method becomes advice to translate.
   Rejected.
4. **Java 25 instead of 21.** A newer long-term release exists. The
   host carries 21, the references are lived on 21, and no slice
   needs a newer language feature. Rejected for now; a later
   upgrade is a dated decision of its own, not a bootstrap choice.

## Decision

Option 1. The stack line is **Spring Boot 4.x, Java 21, Maven**.
The exact Boot minor is pinned where it lands, in the build file,
at the bootstrap step that earns it — this record fixes the major.

With it, as the bootstrap requires:

- **The migration tool stays outside the app.** Flyway remains the
  ground's tool, run as `migrator` from the compose profile
  (contract, *How schema changes are made*). The application never
  migrates; the evidence harness runs migrations harness-side, at
  test scope only, against its own throwaway store.
- **The app knows one identity.** `runtime`, its password from
  `NEVER_OVERSOLD_RUNTIME_PASSWORD`; no other database identity in
  any configuration or profile.
- **The migrations home is confirmed:**
  `infrastructure/flyway/migrations/`, honestly empty until a slice
  earns a table.
- **Nothing enters ahead of need.** Plain SQL access; no object
  mapping and no schema generation while nothing persists domain
  state — the standard Spring recipe (in-app Flyway with two
  credentials, JPA with `ddl-auto`) reverses the ground's
  constraint and is refused by name in the build file.

**Initialization identity:** group `io.github.edgaras87` — the
reviewer's namespace, given 2026-09-11, never invented; artifact
`never-oversold`; base package `io.github.edgaras87.neveroversold`;
packaging jar; Java 21. Maven rides in through the committed
wrapper, so a stranger's machine needs a JDK and podman, nothing
else.

## Consequences

Good: the reader sees the invariant work, not a tour of a
framework; the walkthrough's lived traps apply as written; the
harness reference's confirmed code can be imitated rather than
derived. Bad: Spring Boot 4's renamed modules are load-bearing and
the failures name something other than the cause — the walkthrough
carries the recall list, and every dependency enters the build
file with its earning reason so the deliberate absences read as
decisions, not omissions.
