# Devlog

<!-- Newest entries on top. 2–5 minutes at the end of each session.
     For future-you: fragments fine, honesty mandatory. Never clean up.
     Mark dead ends loudly with "DEAD END:" so they're greppable.
     End every session with a "Resume:" line — cheapest save-point there is.
     When this file gets long, split into devlog/<YYYY-MM>.md per month. -->

## 2026-09-12  (Step 5: SL-1, no over-admission under contention)

<!-- Grows as the stages run — the lived-result record: for every
     executing step, the command, what was expected, what actually
     happened. Decisions with their options are ADRs. -->

- Opened on `step-5-sl-1`, cut from main at `be61f60`; the gate
  derived from the slice skill's five stages, SL-1's registry row
  and TODO's three Step 5 items, committed before any work
  (`6d8f373`). The gate names no mechanism: WHAT before HOW, the
  wall is Stage 2's.
- Stage 0, readiness on the actual repo, each check, expected,
  actual:

  | Check | Expected | Actual |
  |---|---|---|
  | R1 the three exports | intent, definition, registry under `docs/system/`; SL-1 `chosen-next`; the reconciliation line whole | all three present; SL-1 `chosen-next` (2026-09-10); 20 kills ↔ 4 slices + 3 folds, 20 rows in the table |
  | R2 the suite | `./mvnw test` green at the branch point | 7 tests, 0 failures, exit 0: context 1, migration path 3, health through the door 1, in-process burst 1, race across 3 instances 1 — nothing exported, the ground down |
  | R3 the real store from tests | the miniature, not a mock | `ThrowawayStore`: `postgres:17`, the ground's own `bootstrap.sql`, migrated as `migrator`, connected as `runtime` (Step 4) |
  | R4 contention across instances | the harness creates SL-1's adversity class | `InstancesRaceIT`: 3 forked instances, 120 requests at one instant, every pid among the 3 (34 s) |
  | R5 the harness can fail | — | not answerable yet: no wall exists to break. Answered in this step's build, the evidence run red against the admit without its wall, before the wall lands (the gate says so) |
  | R6 the record surfaces | registry writable; a place for deviations and sign-offs | writable; the devlog for deviations and the walk, ADRs for decisions, the entry file's table naming both |

  Observation, not a blocker: the ground's container
  `never-oversold-postgres` is `Exited (0) 6 hours ago`; the suite
  needs it down or up alike, and the run proof at close will need
  it up. The migrations home holds `.gitkeep` alone — the first
  migration is this slice's.
- 2026-09-12, ready for SL-1 — the reviewer's sign-off, given in
  so many words after the table above.
- Two decisions at opening, before the specification: ADR-0010, the
  door's conventions (resource paths, invalid 400 / unknown 404 /
  refused 409, Problem Details, the record as persisted); ADR-0011,
  an item becomes known by its first adjustment, the adjustment
  stating the count. Both confirmed by the reviewer.
- Stage 1, the specification at `docs/construction/sl-1-no-over-
  admission.md`: six guarantees by attack, zero mechanisms
  (checked by grep for lock, constraint, transaction, row, table,
  cache, queue: no hit); kill 9 shaped as "no interval to kill",
  kill 10 as "FC3 removes it". Signed 2026-09-12. Stage 2, the plan
  as §7 of the same record: the store's check constraint and one
  conditional statement as the wall; faces not chosen and escape
  hatches written; three provisionals. Signed 2026-09-13. A
  numbering slip (the plan landed as §8 before §7) fixed before
  the plan's commit.
- Stage 3 as the change-plan (`79a7239`), eight commits. Commit 2,
  V1 — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw test`, the migration-path tests | V1 applied in the miniature as `migrator`; applied ≥ 1, none failed; the wall in the catalog; `runtime` writes both tables | `Successfully applied 1 migration … now at version v1`; 5 of 5 green; the constraint read back as `CHECK ((reserved <= on_hand_count))` |
  | the whole suite | green | 9 tests, exit 0 |

- Commit 3, the doors — the reviewer asked for navigation inside
  the feature package and the layout was decided before the
  commit: values and answers in public sub-packages, behaviour and
  writes at the root, package-private; a cycle between the two
  sub-packages found and removed (`UnknownItem` takes a string);
  ADR-0008 gains the dated note; the package carries its map in
  `package-info.java`. The change-plan said "one package-private
  package" — a divergence for the close. Verified: 22 tests green
  from clean, the door's fifteen among them, nine nonsense shapes
  each `400` with the numbers untouched.
- **The red run**, before commit 4, on the working tree only:
  V1's `item_never_oversold` constraint removed, the admit still
  the naive read-check-write of commit 3, E1 and E2 run:

  | Test | Expected | Actual |
  |---|---|---|
  | `ReserveStormIT` — 100 requests, one instance, 20 on hand | red: the witness shows an oversell | **active sum 22, on-hand-count 20** — `[active sum ≤ on-hand-count] Expecting 22 to be less than or equal to 20` |
  | `InstancesStormIT` — 120 requests across 3 instances, 20 on hand | red | **active sum 23, on-hand-count 20** |

  Two of two red; the harness can fail (R5). V1 restored from the
  index; `git status` showed only the three new test files.
- Commit 4, the wall — the admit as one conditional `UPDATE`,
  the store's row count the decision, the reservation inserted in
  the same transaction; the tests unchanged:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw clean test` | the same two storms green, all else green | 24 tests, 0 failures, exit 0; `ReserveStormIT` 1.5 s, `InstancesStormIT` 23 s |

  Every reply in both storms was `201` or `409` — none of the
  constraint errors commit 3's naive admit would have produced;
  the admitted count equalled the units held; every admitted id was
  in the store; the sampler read the witness throughout and saw no
  violating state.

## 2026-09-11  (Step 4: skeleton & bootstrap)

<!-- Grows as the stages run — the lived-result record: for every
     executing step, the command, what was expected, what actually
     happened. Decisions with their options are ADRs. -->

- Opened on `step-4-bootstrap`, cut from main at `143d00e`; the
  gate derived from the skill's five stages, the registry's SL-1
  and TODO's Step 4 items, committed before any work (`b70e792`).
  The gate names no stack: WHAT before HOW, the stack is Stage 1's.
- Stage 0, readiness on the actual repo: the three exports stand,
  exactly one slice `chosen-next` (SL-1); both manuals stand — the
  contract with the identity table, the reach table, four refusals
  and the one DDL path, the manual with six "seen here" results
  from 09-11; no source file, no build file anywhere in the tree;
  `infrastructure/flyway/migrations/` empty; `.env` present and
  ignored, `.env.example` committed. Passed.
- Stage 0, the ground live — found **down** at opening: the
  container `never-oversold-postgres` was `Exited (0) 18 hours
  ago`, the volume `never-oversold_postgres-data` in place. An
  observation, not a blocker: `podman compose up -d` per the
  manual, healthy after 6 s, data intact (roles, schema, grants as
  bootstrapped). Then each check, expected, actual:

  | Check | Expected | Actual |
  |---|---|---|
  | `pg_isready` | accepting connections, exit 0 | `accepting connections`, exit 0 |
  | one real query as `runtime` | `runtime`, `never_oversold`, 17.x | `runtime\|never_oversold\|PostgreSQL 17.11` |
  | catalog check, 6 queries | as the file's comments | all six as stated: two roles, neither super, both login; db owned by `postgres`; schema by `migrator`; `runtime` USAGE t CREATE f; default privileges tables SIUD, sequences US, none grantable; CONNECT `migrator` t, `runtime` t, PUBLIC f |
  | `podman compose run --rm flyway info` | `No migrations found` | `No migrations found` |
  | witness read from the host, published port, as `runtime` | `1` | `1` |
  | C1: `CREATE TABLE` as `runtime` | `permission denied for schema never_oversold` | exactly that |
  | tables in `never_oversold` | 0 | 0 |

  Seven of seven. The ground is up now, which is what the wiring
  needs. Nothing to hand off from Stage 0.
- Stage 1, decided with the reviewer, before any code: the stack
  (ADR-0007 — Spring Boot 4, Java 21, Maven; fluency and audience;
  the host already carries JDK 21.0.11, Maven 3.9.9, the podman
  socket and a Testcontainers properties file from the earlier
  project); the group `io.github.edgaras87`, given, not invented;
  the structure (ADR-0008 — the lived default, after the reviewer
  asked for the alternatives and their fit: classic layered is the
  same layering with the compiler removed from the boundary,
  hexagonal would centre the naive check-then-write shape,
  Modulith has one module to verify); the harness shape (ADR-0009
  — plural instances as separate processes, the one place this
  run leaves the harness reference by decision). Kill 10's clock
  deferred to SL-1 by name. The requirements document's home:
  `docs/construction/`, not the skill's `internal/` — hand-off
  filed in TODO.
- Stage 2: `docs/construction/bootstrap-requirements.md` composed —
  §1–§6, no dependency, class or file layout named; the ground by
  pointer; the port and password as "env var X, default Y".
- Stage 3: the step plan as this run's change-plan, confirmed and
  committed (`ee558e5`); nine commits, the plural-instance race
  provisional.
- Commit 2, the skeleton — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | Initializr, `bootVersion=4.1.1`, `web,actuator` | a tarball | first pull truncated (gzip: unexpected end of file); second pull `http 200`, 10 037 bytes, extracted |
  | `./mvnw -q -B test` | context test green, no store | `Tests run: 1, Failures: 0`, Boot `v4.1.1`, `using Java 21.0.11` |
  | `./mvnw spring-boot:run`, then `curl localhost:8080/actuator/health` | Java 21 announced, health UP | `Starting … using Java 21.0.11`; `Tomcat started on port 8080`; `HTTP/1.1 200` `{"status":"UP"}` after 9 s |

  Extracted into the root beside `infrastructure/`: wrapper and
  sources copied; the pom rewritten per the convention — two
  capability groups, each with its why, the Boot minor pinned
  once, the plugin commented, the empty metadata blocks gone; the
  version `0.0-SNAPSHOT` because a version is a state of the
  evidence; `application.properties` replaced by a YAML carrying
  the name only; `HELP.md` and Initializr's `.gitignore` dropped,
  their useful lines merged below the overlay markers in this
  repo's own `.gitignore` and `.gitattributes`. Boot 4's
  Initializr already names the web starter `webmvc` and pairs the
  `-test` companions — the walkthrough's rename trap did not bite.
- Commit 3, the datasource as `runtime` — each command, expected,
  actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw -q -B test`, nothing exported, ground up | green — the pool connects lazily | `Tests run: 1, Failures: 0` |
  | `.env` exported, `./mvnw spring-boot:run`, `curl …/actuator/health` | health UP, `db` UP | `200`, `"db":{"status":"UP"}` among the components, after 8 s |
  | `pg_stat_activity` while it ran | sessions as `runtime` only | `runtime \| PostgreSQL JDBC Driver \| 4`; none as any other identity |

  One thing the reference leaves open: the plain context test starts
  a context whose password placeholder has no default, so it must
  resolve from somewhere. Given as a test property with a value
  that says what it is (`unused-nothing-connects`) — not a test
  profile, and not a default in the config. The companion
  `spring-boot-starter-jdbc-test` enters with its starter under the
  pairing rule; the driver at runtime scope; no object mapping.
- Commit 4, the harness — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `podman compose stop`, then `./mvnw -q -B test`, nothing exported | green with the ground down; `*IT` run under the one command | `postgres:17` started in 6.5 s; history table created, `No migrations found`; **1 failure of 5** — mine, see below; `MigrationPathIT` and `HealthThroughTheDoorIT` listed in the surefire reports |
  | `./mvnw -q -B test` after the fix | 5 green | `MigrationPathIT` 3/3, `HealthThroughTheDoorIT` 1/1, context test 1/1, exit 0 |
  | `podman ps` ~12 s after the JVM exit | no throwaway left | none running |
  | `podman compose start` | healthy | healthy after 2 s |

  The failure: a third test I added beyond the reference —
  `runtime` attempting `CREATE TABLE` in the miniature, the
  ground's refusal 1 held in evidence runs. It asserted the message
  on the top-level exception; Spring wraps the driver's error, so
  the store's `permission denied for schema never_oversold` sits at
  the root cause. Fixed by asserting on the root cause. Kept: three
  lines that catch a miniature quietly wired without the split.
- The walkthrough's Ryuk trap is stale on this line: Testcontainers
  2.0.5 (Boot-managed) reads no `ryuk.disabled` key — the config
  keys are image, privileged, timeout — so the line in
  `~/.testcontainers.properties` is inert; and Ryuk ran fine under
  rootless podman 5.8, reaping both throwaways within seconds. Kept
  Ryuk on; the manual says so; hand-off filed.
- The web base lands here with a user, not as an empty class:
  `HealthThroughTheDoorIT` takes the world-is-up check through the
  real door against the harness's store — the check every evidence
  run gates on — and that is what earns `spring-boot-restclient` at
  test scope. Boot 4 with only `flyway-core` on the test classpath
  runs no Flyway auto-configuration (the module split), so the
  harness's own call is the only migration path in tests.
- Commit 5, the probe pair — each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw -q -B test` | 6 green, the hundred released at one instant all `200` with `runtime` | `ContentionProbeIT` 1/1 in 18 s, the rest as before, exit 0 |
  | `.env` exported, run, `curl localhost:8080/probe/ground` | `200`, identity `runtime`, a pid | `200`, `{"identity":"runtime","pid":"…"}` |

  Imitated from the reference, not pasted: the probe answers the
  identity the store sees and, beyond the reference, the process
  id — so commit 6 can assert that the race was served by distinct
  processes. Both files say in their own javadoc that they are
  scaffolding and die with SL-1, and the test says what it does not
  prove: overlap in the store, and anything about more than one
  instance.
- **Correction to commit 3's note, 2026-09-12.** The claim that the
  context "must resolve the placeholder or refuse to start" was
  wrong, and I stated it without running the check. Found through
  the reviewer's own run: `./mvnw spring-boot:run` from a terminal
  with nothing exported *started*, health `DOWN` with `db` `DOWN`,
  the probe `500`, the store's log `password authentication failed
  for user "runtime"` — Boot's configuration binding leaves an
  unresolvable placeholder as the literal string, so the literal
  became the password. Verified the other way: the context test
  with the property removed and the variable unset, exit 0. The
  test property is dead; removed, with the javadoc corrected. The
  five options the reviewer chose from rested on a false premise;
  none was needed.
- The lived trap that came out of it: **a missing secret does not
  stop the ledger**. It starts, and only health says the store is
  down. The README's Run section must say "health `db` DOWN means
  the environment is not exported"; whether the ledger should
  refuse to start without its secret is a *what* not in the
  requirements — filed in TODO for the reviewer, not decided here.
- Commit 6, the race across instances — the realization decided at
  this boundary, as the plan reserved: **forked JVM processes from
  the build's own output**, not containers. Why: the test phase
  runs before packaging, so an image would need a second command,
  which §3.5 forbids; a fork of `target/classes` with the runtime
  classpath — written to `target/runtime-classpath.txt` by the
  dependency plugin at `process-test-classes` — is the real
  application with no test-scope code inside it, started with the
  three environment facts a real instance gets and nothing else.
  Each command, expected, actual:

  | Command | Expected | Actual |
  |---|---|---|
  | `./mvnw -q -B test` | 7 green; 3 instances up, 120 requests at one instant, every pid among the 3 | all green, `InstancesRaceIT` in 33 s, exit 0 |
  | `pgrep` after the run | no instance left | none |
  | an instance's log | Java 21, its own port, health UP with `db` UP | `using Java 21.0.11`, `Tomcat started on port 37739`, started in ~3 s |
  | the forked classpath | no test-scope artifact | 0 matches for flyway, testcontainers, junit, assertj, `-test`; 58 entries |

  Shape: `ThrowawayStore` now holds the container and the migration
  (lifted out of `DatabaseIT`, which keeps only the datasource
  override), so tests that boot a context and tests that fork
  instances share one store; `ForkedLedger` starts an instance on
  a free port, waits for its door to answer UP with the store UP,
  and stops it — destroy, then forcibly — in `close()`;
  `InstancesRaceIT` runs no Spring context of its own. Beyond the
  reference's shape: the pids served are asserted to be exactly
  the instances started, and the witness path is exercised — a
  plain JDBC read of `pg_stat_activity` as `runtime` from outside
  every instance, while they run. Traps met: none that bit; free
  ports from `ServerSocket(0)` held for the instances without a
  collision across three runs. What it does not prove is written
  in the test: overlap in the store.
- Commit 7, certification — against the delivered files, never a
  report. §5's evidence, from actual output:

  | Evidence | Expected | Actual |
  |---|---|---|
  | `./mvnw clean test`, nothing exported, ground up but unused | green; `*IT` under the one command | 7 tests, 0 failures: context 1, migration path 3, health through the door 1, in-process burst 1, race across 3 instances 1; exit 0 |
  | `.env` exported, `./mvnw spring-boot:run`, health, probe | Java 21 announced; UP with `db` UP; the probe as `runtime` | `using Java 21.0.11`; `"db":{"status":"UP"}`; `{"pid":"174077","identity":"runtime"}` |
  | the commits standing | the plan's 2–6, one fix | `5336563 066c113 53e4f76 49e7453 e5c61dd 0abbcdc`, each verified before it landed |

  §1 identity exact: group `io.github.edgaras87`, artifact
  `never-oversold`, package `io.github.edgaras87.neveroversold`,
  jar, Java 21; packages `neveroversold` and `probe` only. §2 all
  four capabilities present and bounded: the door (webmvc), health
  naming `db`, connectivity as `runtime`, the harness with every
  sub-item — throwaway store, harness-side migration, zero-applied
  assertion, `*IT` under `./mvnw test`, forked instances raced,
  witness read from the store — proven on contention alone; the
  probe carries no meaning and says it dies at SL-1. §3 every
  constraint visible in the files: `runtime` the only identity in
  `application.yaml`, password `${NEVER_OVERSOLD_RUNTIME_PASSWORD}`
  with no default; the two mentions of `migrator` under main are
  the comment lines naming its absence; 0 of flyway, hibernate,
  jpa, liquibase on the runtime classpath; the migrations home
  holds `.gitkeep` alone; every dependency with its reason above
  it; the suite green with the ground stopped (commit 4's run);
  the forked instances stopped in `finally`. §6 respected: no
  item, reservation or admit anywhere; no table; no other
  adversity; no auth; no image, descriptor or pipeline.
- Exit test: the skeleton runs on the real ground as the runtime
  identity alone — yes, the store saw sessions as `runtime` and no
  other; the harness demonstrably creates the chosen adversity
  through the real door — yes, 120 requests at one instant across
  three processes, every response asserted. **No business behavior
  yet.** The probe pair's death is scheduled at SL-1, in their own
  javadoc and in TODO.
- Decided with the reviewer, 2026-09-12: the fail-fast question
  goes to Release, not here — the only operator today is the
  reviewer, the harness already accepts an instance only on
  health with `db` UP, and the release gate's stranger test is
  where a forgotten export becomes a real need. README's Run
  section names the symptom now. The reviewer's own reproduction
  notes ride in the TODO item.
- Records: README gains Run, Test, the JDK line, the requirements
  row, a two-word vocabulary line (store, door — the reviewer asked
  whether "store" reads as a shop; it is the definition's word,
  chosen before any technology, and kept), and a true status;
  ARCHITECTURE shows the ledger and the harness beside the store;
  CHANGELOG's two Added lines, the version still 0.0 — no slice
  closed; TODO triaged — Now points at Step 5, the plural-instance
  demand closed here, the clock and the fail-fast items moved by
  name; PLAN's gate ticked. Version: 0.0.
- Hand-offs filed this step, all in TODO: the requirements
  document's path (`internal/` → the project's records); the
  Testcontainers 2.x Ryuk trap and template. Not filed as a
  hand-off but worth the skill's notice at the retrospective: the
  reference's concurrency probe is single-process, and this run's
  gate needed more — `ForkedLedger` and the classpath-file plugin
  are the shape that answered it.
- Resume: the entry file's records row (agent-scoped), the
  change-plan's close, then fast-forward `step-4-bootstrap` into
  main on the word; the reviewer pushes. Then Step 5 (SL-1, no
  over-admission under contention): cut its branch, derive its
  gate, run cbc-slice — the door's conventions at its opening, the
  probe pair dying when reserve lands, the race machinery aimed at
  the item's row.
- 2026-09-12, after the merge: the skill re-read against the lived
  step on the reviewer's ask, and four more hand-offs filed in TODO
  — the plural-instance harness shape (promoted from "notice at the
  retrospective" above to a filed hand-off, as ADR-0009 already
  said it should be), the missing-secret trap with its three homes,
  the port key disagreeing between two templates, and the refusal
  test in the miniature. Not filed: the walkthrough's rename trap
  (true, did not bite — Initializr does the pairing) and the
  reference's prefixed role placeholders (this ground's bare names
  are ADR-0006's decision). Also found and fixed on main: the
  entry file's opening paragraph still said "no code yet"; the
  state clause dropped, the lesson filed for the retrospective.

## 2026-09-10 → 2026-09-11  (Step 3: the ground)

<!-- Grows as the walk runs — the lived-result record: for every
     executing step, the command, what was expected, what actually
     happened. Decisions with their options are ADRs. -->

- Opened on `step-3-ground`; the gate derived from the walk's own
  step gates. Readiness tripped on one thing: the definition had no
  runtime-ground facts. Added by a dated revision entry in L1
  (`44f256d`) — the framing's census never asks for them; hand-off
  filed in TODO.
- Readiness, on the actual repo: the three exports stand (ADR-0002),
  four slices, SL-1 chosen-next; the runtime ground now readable;
  the repo is git with `.gitignore`. Passed.
- The environment: ADR-0004, podman local containers, compose-
  driven, kept against every demand the slices make. Proven at the
  engine level before any ground file, each command with its
  expected result, then what happened:

  | Command | Expected | Actual |
  |---|---|---|
  | `podman version` | engine 5.x, client and API the same | 5.8.2 / API 5.8.2, linux/amd64 |
  | `podman compose version` | a provider answers | external provider `docker-compose`, Docker Compose v2.39.4-desktop.1 |
  | `podman info` | rootless, cgroups v2, crun, Fedora | rootless=true, cgroups v2, crun, fedora 42, amd64, kernel 6.19.14, SELinux enabled |
  | `podman pull docker.io/library/alpine:3.20` | digest, exit 0 | digest `bf8527eb…`, exit 0 |
  | `podman run --rm alpine:3.20 sh -c 'echo ground ok'` | `ground ok`, exit 0 | `ground ok`, exit 0 |
  | two `podman run -d` at once, `podman ps` | both Up | `ng-a Up`, `ng-b Up` |
  | `podman kill` on a running container | gone from `podman ps -a` | gone |
  | `podman pause`, then `podman unpause` | paused, then running | `paused`, then `running` |

  Eight of eight as expected; test containers removed. For the
  operator manual: rootless, ports above 1024; SELinux, `:Z` on
  bind mounts; the front door is `podman compose`, answered here by
  an external docker-compose binary.
- The skill's establishment log was opened at the decision and
  withdrawn one commit later, at the reviewer's question: what does
  it hold that the records do not? Nothing — this entry is the walk,
  ADRs are the decisions. Layout settled with it: `compose.yaml`
  and the env files at the root, the runnable ground under
  `infrastructure/`, the manuals under `docs/infrastructure/`.
  Hand-off filed: the skill should treat this as its normal shape
  in a repo with records, not a deviation.
- Operator manual begun (environment section) from the stand-up;
  found two compose providers on the host, podman choosing Docker's
  plugin — noted as "either works, the front door is `podman
  compose`".
- Services, slice by slice (ADR-0005, 09-11): every slice needs
  one shared store with a way to make two writers disagree, atomic
  commit of two writes, and a witness readable from the host;
  nothing needs anything else. PostgreSQL, one instance. Eight
  things not provisioned, each with its why; Flyway as tooling, not
  a service.
- Constraints (ADR-0006): the role-split model checked against the
  project's facts, nothing defeats it; nine constraints, each with
  the mechanism that enforces it — the grant system, the image's
  init hook, a named volume, a published port, the ignore file, the
  image tag. Names: `migrator`, `runtime`, database and schema
  `never_oversold`. T2's tool goes into the behavioral check as a
  capability shown, not a constraint governed.
- Ground files filled from the templates, this repo's names, one
  addition to the verify suite (query 6, the connect privilege,
  because C3 claims it). `podman compose config` clean; every bind
  mount labelled Z; Flyway only under its profile.
- Stand-up and verification, 2026-09-11, each with its expected
  result, then what happened:

  | Step | Expected | Actual |
  |---|---|---|
  | `podman pull postgres:17` | digest, exit 0 | `sha256:67f41722…`, PostgreSQL 17.11 |
  | `podman compose up -d` | created, started, bootstrap runs | started; log: `running …/bootstrap.sql`, `CREATE ROLE` ×2, `CREATE SCHEMA`; healthy after 6 s |
  | first query | answers | **failed once**: `the database system is shutting down` — the image's init restarts the server after the bootstrap and the health check saw the temporary one; answered 1 s later. Trap recorded in the manual |
  | catalog check, 6 queries | as the file's comments | all six as stated |
  | DDL as `runtime` | refused | `ERROR: permission denied for schema never_oversold` |
  | ungranted role connects | refused | `FATAL: permission denied for database "never_oversold"`; probe role dropped |
  | T2's tool: two `runtime` sessions on one advisory lock, `lock_timeout` 1.5 s | second refused | `ERROR: canceling statement due to lock timeout` |
  | `podman compose run --rm flyway info` | connects as migrator, empty schema, exit 0 | Flyway 11.20.3, `<< Empty Schema >>`, `No migrations found`, exit 0 |
  | witness read from the host via the published port | `1` | `1`, through a client container on the host network — the host has no `psql` |

  Both verifications passed; the manual's PostgreSQL section
  written from this run.
- The contract written; then the reviewer asked whether naming the
  server's serializing faces pre-empts the slice — it read that way
  in two phrases; both manuals now say inventory, not a choice, and
  that the advisory-lock probe was picked for needing no schema.
- Clean re-stand from the operator manual alone, on the reviewer's
  yes: `down --volumes` removed container, volume, network; `up -d`
  ran the bootstrap again; honest up after 7 s; all 16 catalog rows
  as stated; DDL as `runtime` refused; Flyway `<< Empty Schema >>`;
  witness read from the host `1`. Reproduced.
- Exit test: the ground runs — yes; every service tied to a stated
  need and every exclusion to a stated why — ADR-0005; both
  verifications passed from actual output — this entry; both
  manuals stand, written from lived work — yes; a stranger could
  stand it up from the operator manual alone — the re-stand followed
  it and nothing else. Passed. README gains Prerequisites;
  ARCHITECTURE names the ground and nothing that does not run.
- Version: still 0.0 — no slice closed; the ground is not a slice.
- Resume: the entry file's records rows, the plan's close, then
  fast-forward into main on the word and the reviewer pushes. Then
  Step 4 (bootstrap): cut its branch, derive its gate, run
  cbc-bootstrap — the stack at capability grain, the skeleton as
  `runtime` on this ground, the harness creating SL-1's adversity
  across instances (TODO's Step 4 items).

## 2026-09-10  (Step 2: identity)

- Named **never-oversold** (ADR-0003): the promise's negation,
  ruled out. Seven candidates from the sentence against a four-point
  bar; safe-reservations overturned — it named the territory and
  overclaimed; rejected beside it: no-oversell, oversell-proof,
  reserved-within-count, reservations-under-contention,
  reservation-ledger. The reviewer took the recommendation as it
  stood.
- The step was retitled from "Define (naming)" when it opened:
  a public identity is a name, a description, and a remote. The
  description is one line derived from the intent's why.
- The rename landed as one project commit across README, PLAN,
  the three exports (each with its first dated revision entry —
  the registry gained the log its header promised), CHANGELOG; the
  derivation record keeps the name it ran under, with a note; the
  entry file's title in its own agent commit. Closed steps' notes
  and earlier devlog entries stay as written: dated facts.
- The remote: github.com/edgaras87/never-oversold, created by the
  reviewer by hand with the ADR's description, main pushed by the
  reviewer at 0de75df (Step 1's close). This branch was not
  pushed; the reviewer pushes main again after the fast-forward.
- The folder on disk keeps `cbc-pure-run-3`; records name the
  project, not the path. The agent's memory is keyed to the path.
- Resume: close the plan, fast-forward `step-2-define` into main
  on the word, the reviewer pushes main. Then Step 3 (ground): cut
  its branch, derive its gate into PLAN, run infra-establish from
  the registry's adversity needs — T2's tool named and verified by
  refusal is the first thing the ground must show.

## 2026-09-09 → 2026-09-10  (Step 1: framing)

- Briefing: arrived 2026-09-09 in `temp/`, the reviewer's file,
  untracked. Inventory reservation under contention, for the
  portfolio reader; six bars a promise must clear; the sentence
  left open. The problem is named from this session on.
- Step 1 ran on `step-1-framing` under a change-plan. The seven
  framing steps each landed as a commit series — draft, one
  revision per reviewer question that changed something, the
  verdict — the reviewer's request before step 0's first commit,
  so a question's effect is a diff between commits. It held: 40
  commits from the gate to the ADR, every verdict the reviewer's.
- What the questions changed: the seller entered as who the
  promise protects, not the audience; the six bars split into
  settled-here and proven-later; the candidate sweep got written
  and each word of the sentence its commitment; refusal was
  restated as by-falsifiability, never by scope; the actors got
  one-line definitions; W2 states its cost; the reference number
  became the one token on-hand-count (a return trip through four
  frozen sections); possession tags kept P, probes became PL.
- Outcome: one promise; four possessions, six refusals; twenty-two
  facts, three trust lines, six fences, five scope verdicts,
  saturation by two empty lenses; twenty kills into four concerns
  and three folds; one area, five seams refused; L5 empty with
  reasons; four slices, SL-1 chosen-next. ADR-0002 adopts it.
- Two slips, both fixed at the reviewer's reading: the definition
  was appended in derivation order and had to be reordered L1→L5
  (the export section's "growing" read as append — hand-off filed
  in TODO); the registry's opening carried the template's own
  words — a skill name, a step number, the delegation slot — into
  a project artifact. The reviewer's rule from that: exports carry
  no agent language. Hand-off filed for the template's line.
- Review-driven touch-ups to committed exports each cost a plan
  revision until a provisional step named them; three plan
  revisions before that, one after. Worth a retrospective line:
  the plan wants a "touch-ups on reading" step from the start.
- DEAD END: none. The near one was concern B — the downward
  correction looked like a definition until the because was
  written (naive code accepts the honest request and the promise
  dies).
- Version decided: a state of the evidence, 0.N by slices closed,
  1.0.0 at all closed and released (CHANGELOG header).
- Resume: land the entry-file row and the plan's close, delete
  `temp/`, fast-forward `step-1-framing` into main on the word.
  Then Step 2 (Define): cut its branch, derive its gate into PLAN,
  decide the public name — safe-reservations to confirm or
  overturn.

## 2026-09-09  (pre-briefing: the working arrangement)

- Four arrangement pieces went in before the briefing, all on
  trial from Step 1. The branch rule landed on main on 09-07, as a
  Standing rules section in PLAN. The other three landed on the
  branch `prebriefing-arrangement` under a change-plan, seven
  commits, fast-forwarded to main on the reviewer's word.
- Kit updated c670fe5 → af16eb7 through the receipt branch
  `kit-af16eb7`: one commit cut from the seed, the kit copied over
  it. What the branch gave the compare: the whole kit diff at the
  two pins in one place, file by file, and compare-first became
  `git diff 27db35e main -- <file>` — every copy identical, so
  every overwrite was clean. Where it fell short: the four
  placeholder reversions are noise to read past; the receipt
  cannot say which convention a stub change belongs to (that is
  the starter README's table, read in the handbook checkout); the
  why is not in it either — ADR-0035, ADR-0036 and the
  agent-arrangement convention text are not kit files, and the
  rejection and the move both needed them; the entry file's stub
  diff had to be carried into the living file by hand, comment
  only; and the script's bare `git add -A` would have swept the
  untracked `temp/` into the receipt (excluded by hand).
- The kit's settings-file commit gate: rejected, not deferred. The
  reviewer gives the word once in chat; a prompt asking for it
  again is a second word. The handbook withdrew the same rule in
  its own checkout for the same reason. The receipt keeps the file
  so the compare stays honest.
- CLAUDE.md moved under .claude/; content untouched; the harness
  picks up the new address from the next session. CLAUDE.local.md
  exists at the root, ignored — the operator's, not quoted.
- Briefing: still not brought. Nothing names the problem.
- Resume: wait for the briefing. On its arrival cut Step 1's branch
  from main, derive Step 1's gate into PLAN first (the branch item
  among them), then run cbc-framing jointly.

## 2026-09-07  (Step 0: bootstrap)

- Project started. Seeded, not born whole: the kit's hygiene commit
  on main, then the branch `birth-seed` as the receipt — kit
  remainder @ c670fe5, the CbC bundle @ 57cf22f (concept, five
  skills, playbook steps, entry-file fills). Main held the same
  files untracked; the receipt is never merged.
- The birth ran as one change set, eight commits: the plan, kit
  conventions, concept, records with Step 0's gate, CbC skills
  with their registry entry, the entry file, this close, the
  plan's close. Every pinned copy byte-identical to the receipt.
- Briefing: not yet brought. It arrives as the prompt that opens
  Step 1; nothing before it names the problem. The title
  `cbc-pure-run-3` is a working name.
- Open: what a version is here (CHANGELOG header) — Framing
  decides.
- Resume: wait for the briefing. On its arrival open Step 1 with
  cbc-framing: derive Step 1's gate into PLAN first, then run the
  framing jointly.
