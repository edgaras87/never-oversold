<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     system-bootstrap/.claude/skills/cbc-bootstrap/references/spring-boot-walkthrough.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: none — verbatim below this header.
     Harvested 2026-08-28: the Boot 4.1 TestRestTemplate trap
     (RANDOM_PORT alone provides no bean;
     @AutoConfigureTestRestTemplate required) — from
     checkout-system's decision record of 2026-08-27, in the run's
     own wording (ADR-0007).
     Re-derived 2026-08-28: stage 3's config skeleton and stage 4's
     test-runtime properties now point at copy-and-fill masters in
     templates/ beside this skill's references (ADR-0008); whys and
     traps kept (PLAN Step 6).
     Re-derived 2026-08-28: stages 4 and 5 point at
     spring-harness-reference.md — the recurring harness artifacts
     as code, confirmed by the third lived pass; a reference on
     ADR-0008's imitated side, never pasted. Outcomes and traps
     stay mastered here.
     Re-derived 2026-08-29: stage 1 carries the application-
     structure decision beside the stack decision, routed to
     app-structure.md — the decision surface: lived default,
     decision rule, vocabulary of unlived alternatives. -->

# Spring Boot bootstrap walkthrough — outcomes and lived traps

Stack line: **Spring Boot 4.x · Java 21 · Maven**, on an established
PostgreSQL ground under a role split (`<project>_migrator` /
`<project>_runtime`), migrations home `infrastructure/flyway/migrations/`
(empty), rootless podman as the container runtime. Boot 4's renamed modules
are load-bearing throughout; on Boot 3 the names differ.

This doc carries **required outcomes** (what must be true, the how is
yours) and **lived traps** (exact facts about this stack and environment).
It carries no code to copy. Its config files, though, are
copy-and-fill masters in `templates/` beside this skill's
`references/` (ADR-0008); they implement the stack line above, and
if the run's decided stack differs you are **off-template**: derive
from the outcomes here, record the deviation in the run's log, and
expect it to harvest. **The project's requirements document wins over
this doc on any conflict.** Governing principle at every tier: *something
outside the app migrates; the app runs.*

## 1. Record the stack decision

Before any code, the project's log carries: the stack; the
application structure, decided against the lived default —
`app-structure.md` beside this walkthrough is the decision surface,
and deviating from its default is a logged off-default decision;
Flyway staying outside the app (no migrator credentials in any
profile); the migrations home confirmed.

**Trap — the standard recipe reverses the ground's constraint.** The usual
Spring path (in-app Flyway with dual credentials, JPA with
`ddl-auto: validate`) hands the running app control over database
structure. No JPA, no in-app Flyway, no `-data-` starters.

## 2. Stand the skeleton in the live repo

- Initialize to the identity fields and **extract into the existing repo
  root** — beside the infrastructure dirs. Merge, never overwrite:
  `.gitignore` (gaining build output, IDE, OS, log sets), `README.md`,
  `.gitattributes` (wrapper line-ending lines merged in: shell wrapper LF,
  cmd wrapper CRLF).
- Capabilities at this stage and no more: an HTTP surface and operational
  health.
- Generated cruft removed: help files, empty static/template dirs;
  properties file replaced by a YAML carrying the application name only;
  the pom's empty metadata blocks deleted, description filled from intent.
- Pom written per the pom convention (sibling reference).

**Verified:** context test green (no database); the app starts and
announces **Java 21** (align IDE and terminal JDK if not); health UP.

**Traps:**
- Boot 4 renames the web starter to the web-MVC name, and pairs runtime
  starters with `-test` companions (state the pairing rule once as a pom
  comment).
- A `4.x.y.RELEASE`-style coordinate does not resolve on this line — the
  version is plain `4.x.y`.

## 3. Wire the runtime datasource

Config skeleton copy-and-fill: `templates/application.yaml` — the
absences below ride in it as comments; the run's business config
grows under its own key, never in the template.

- Connect **as the runtime identity only**, using the connection facts the
  requirements carry; details in the operator manual.
- **Three deliberate absences, each commented in the config with its why:**
  no schema pinning in the app (the ground pins the search path
  server-side); no migrator credentials in any profile; no test profile
  (the harness overrides the datasource itself).
- Access at capability grain: a plain SQL client (`JdbcClient`), not an
  ORM; the driver at runtime scope.
- Password from the environment. **Environment facts notation-neutral in
  any authored doc** — compose's `${VAR:-default}` syntax pasted into
  `application.yaml` does not error; it silently resolves to a literal
  like `-5432`.

**Verified:** ground up → app runs, health UP **with the db component UP**.
Fact: the test run stays green with the ground down — the pool connects
lazily; degradation shows only in health. Correct, not a gap.

## 4. Stand up the evidence harness

### The test runtime (once per machine — operator-manual territory)

Testcontainers needs a reachable container-runtime socket. Rootless podman:
enable the user socket unit, point the library at it.

**Traps, exact:**
1. `~/.testcontainers.properties` binds **only from `$HOME`** — never the
   project root (lived as a long "no valid Docker environment" hunt).
   Copy-and-fill: `templates/testcontainers.properties`.
2. Ryuk misbehaves under rootless podman; disabling it is the accepted
   trade — a hard-killed test JVM can strand a throwaway container,
   cleaned with `podman ps` / `rm -f`.
3. The socket unit can report *active (listening)* with the socket file
   missing. Active is not enough: stop socket+service user units, start
   the socket again, confirm the file exists.
4. This setup belongs in the project's operator manual, not the code.

### The harness in the codebase

- **Test-scope capabilities only**: drive a real PostgreSQL container from
  tests; integrate with the Spring test context; run migrations
  **harness-side**. Flyway is a harness tool — at runtime scope it would
  undo step 3's claim.
- **One container per test JVM**, same major version as the ground,
  started once and shared, lifecycle explicitly the harness's own.
- **Migration runs immediately after container start, before any app
  context boots**, pointed at **the one migrations home on the
  filesystem** (`filesystem:infrastructure/flyway/migrations`), never a
  classpath copy.
- A small **layered test-support base**: a database-only base, and a
  web-plus-database base extending it.
- **Integration tests run under the one standard test command** — widen
  Surefire's includes to cover `*IT`; no second command, no test that
  quietly never runs.
- **The migration path proven, not assumed**: a test asserting the
  history table exists with **zero applied migrations** (the table is born
  even by a zero-migration run; zero is correct for an earned-migrations
  ground). The first real migration turns the count positive without
  changing what the test proves.

The recurring artifacts these outcomes converge on — the two bases and
the migration-path test — live as confirmed code in
`spring-harness-reference.md` beside this walkthrough: **imitated,
never pasted**; read its variation points before writing a line.

**Traps, exact:**
1. `spring-boot-restclient` must be present **at test scope** on Boot 4,
   or the web test context dies with
   `NoClassDefFoundError: RestTemplateBuilder` — the failure names
   something else.
2. Boot 4 moved the test REST client to
   `org.springframework.boot.resttestclient.*` — and `RANDOM_PORT`
   alone **no longer provides the `TestRestTemplate` bean**: the test
   (or base) needs `@AutoConfigureTestRestTemplate` from
   `org.springframework.boot.resttestclient.autoconfigure`, or the
   autowire fails with "No qualifying bean". (Lived on Boot 4.1.1,
   checkout-system bootstrap.)
3. Testcontainers 2.x renamed artifacts and packages:
   `testcontainers-postgresql`, `org.testcontainers.postgresql`.
4. **No dummy baseline migration** — keep the home honestly empty until a
   migration is earned. (Git cannot track an empty dir; a keep-file is the
   ground's own decision, not the app's.)

**Verified:** the standard test command green — image pulled, all passing.

## 5. Prove the harness creates the adversity

- **A probe endpoint** in main source: one real round-trip to the database
  as the runtime identity, documented in its own javadoc as **scaffolding
  that dies when the first real slice lands**. Never grows business
  meaning.
- **A concurrency probe test**: many requests (the lived pass used a
  hundred) held at a barrier, **released at one instant**, each a full
  HTTP → application → real-PostgreSQL round-trip through the real door;
  every response asserted. The release-at-one-instant pattern *is* the
  shape later race evidence takes — aimed at nothing yet.
- The probe pair as lived code — endpoint, barrier test, and the
  scheduled-death javadoc — is in `spring-harness-reference.md`, same
  imitate-don't-paste rule.

**Verified:** the standard test command green.

## The exit

Project records grown: stack, run and test commands, operator manual
linked, an honest "no business behavior yet", the probe's death scheduled
at the first slice.

## Recall list — the traps compressed

1. Boot 4 renames: web starter → web-MVC name; `-test` companions per
   starter; test REST client in `org.springframework.boot.resttestclient.*`.
2. `spring-boot-restclient` at test scope or the web test context fails on
   `RestTemplateBuilder`; `@AutoConfigureTestRestTemplate` required —
   `RANDOM_PORT` alone provides no `TestRestTemplate` bean.
3. Testcontainers 2.x: `testcontainers-postgresql`,
   `org.testcontainers.postgresql`.
4. `~/.testcontainers.properties` binds only from `$HOME`.
5. Podman socket can be *active* with the file missing — restart user
   units, confirm the file.
6. Ryuk off under rootless podman; accept occasional stranded throwaways.
7. No dummy baseline migration; the history table is born by a
   zero-migration run.
8. Flyway is harness-only; at runtime scope it undoes the ground's
   constraint.
9. Boot version coordinate plain `4.x.y` — the `.RELEASE` suffix does not
   resolve.
10. Environment facts notation-neutral — never compose placeholder syntax
    in application config or authored requirement text.
