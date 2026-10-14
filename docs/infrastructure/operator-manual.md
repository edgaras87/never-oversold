# Operator manual — standing the ground up and using it

Words and tags used here: [glossary](../glossary.md).

<!-- The operator's full stand-up-and-use truth, written from the
     lived setup at the moment it happened, never reconstructed.
     One section per part of the ground, grown as each is stood
     up. What any machine must be is the definition's (L1, the
     runtime ground). What this machine is, is here. -->

## The execution environment

**Decided:** podman local containers, compose-driven, rootless
(ADR-0004). One machine runs everything: the store, the ledger's
instances, the evidence.

### What a machine needs in hand

- **podman**, 5.x. On Fedora: `sudo dnf install podman`. Host
  installs are the operator's own act. Nothing here installs for
  you.
- **A compose provider** answering `podman compose`. Podman does
  not ship one. It hands the call to whatever provider it finds,
  `podman-compose` (Python) or Docker's `docker-compose` plugin,
  and says which at the top of every `podman compose` call. Either
  works for this ground. The command throughout this manual is
  `podman compose`, never the provider's own name.
- **Rootless podman**, the default on Fedora: a user with a
  subordinate uid range (`/etc/subuid` has a line for you, which
  the Fedora installer writes).

### The machine this was stood up on

Verified by running the commands below on 2026-09-10. They are the
check a stranger runs on their own machine, with the expected results
beside them.

| Check | Command | Expected | Seen here |
|---|---|---|---|
| engine | `podman version` | `Version: 5.x`, client and API the same | 5.8.2, API 5.8.2, linux/amd64 |
| compose command | `podman compose version` | a provider announces itself, then its version | external provider `docker-compose`, Docker Compose v2.39.4 (a second provider, `podman-compose` 1.5.0, is installed but not the one podman chose) |
| rootless | `podman info --format '{{.Host.Security.Rootless}}'` | `true` | `true` |
| cgroups | `podman info --format '{{.Host.CgroupsVersion}}'` | `v2` | `v2` |
| runtime, host | `podman info --format '{{.Host.OCIRuntime.Name}} {{.Host.Distribution.Distribution}} {{.Host.Distribution.Version}} {{.Host.Arch}}'` | an OCI runtime, your distro | `crun fedora 42 amd64`, kernel 6.19.14 |
| SELinux | `podman info --format '{{.Host.Security.SELinuxEnabled}}'` | `true` on Fedora, `false` elsewhere | `true` |
| end to end | `podman run --rm docker.io/library/alpine:3.20 sh -c 'echo ground ok'` | prints `ground ok`, exit 0 | `ground ok` |

### What rootless means here

- **Ports.** Unprivileged processes may bind ports from 1024 up
  (`/proc/sys/net/ipv4/ip_unprivileged_port_start` is `1024` on
  this host). Every published port in `compose.yaml` is above
  1024.
- **Volumes on SELinux hosts.** A bind mount needs the `:Z` label,
  or the container cannot read it. Named volumes need nothing. This
  ground uses named volumes for the store's data, and `:Z` on any
  bind mount it declares.
- **User namespaces.** Inside a container, root is your user
  outside. Files a container writes to a bind mount are owned by
  you. Nothing here runs as host root.
- **Two providers.** If `podman compose` announces a provider you
  did not expect, both work. To pin one, set `compose_providers` in
  `containers.conf`. This ground does not need it.

### The levers the evidence pulls

The harness pulls its own, inside `./mvnw test`, and the compose
ground need not be up. The ledger runs in the form it ships in, as
plain processes. The list below is a summary. Its owner is
ARCHITECTURE's "The evidence harness — test scope".

- **Several instances at once:** the ledger's build forked as plain
  operating-system processes, not containers.
- **Kill mid-work:** the process killed with SIGKILL
  (`ForkedLedger.kill`).
- **Freeze the store:** the throwaway store's container paused, then
  resumed.
- **Drop a connection:** the session ended at the store, as its
  superuser.

The engine's own levers on compose containers (`--scale`,
`podman kill`, `podman pause`) were proven when the ground was stood
up, 2026-09-10. The evidence does not use them. Their outputs are in
`devlog/2026-09.md`, the Step 3 entry.

### The test runtime — once per machine

The evidence harness (the application's test code, under the one
standard test command) starts its own throwaway PostgreSQL through
Testcontainers. So the compose ground does not need to be up for
tests. Testcontainers needs a container-runtime socket. On rootless
podman, that is the user's own socket unit.

```sh
systemctl --user enable --now podman.socket
ls -l "$XDG_RUNTIME_DIR/podman/podman.sock"     # expected: the socket file exists
```

Then point the library at it with a file **in the home directory,
never the project root**. The library takes `docker.host` only from
that file:

```
# ~/.testcontainers.properties
docker.host=unix:///run/user/<uid>/podman/podman.sock
```

`<uid>` is `id -u`. Check with the standard test command,
`./mvnw test`. Expected: a `postgres:17` container starts, the
tests run, and within seconds of the JVM's exit, `podman ps` shows
no throwaway left.

Seen here, 2026-09-12:
- podman 5.8.2 rootless, the socket unit `active`, the socket file
  present;
- Testcontainers 2.0.5 (the version the build manages) started
  `postgres:17` in 6 s, and the suite ran green;
- Ryuk, the library's clean-up container, removed both throwaways
  within about ten seconds of the JVM's exit.

**Traps, lived:**

- The socket unit can report *active* with the socket file
  missing. Active is not enough. Stop the socket and service user
  units, start the socket again, and confirm the file exists.
- Older guidance disables Ryuk under rootless podman with a
  `ryuk.disabled=true` line in the properties file. Testcontainers 2.x
  does not read that key, so the line does nothing, and Ryuk worked on
  this host as is. If a hard-killed test JVM ever leaves a throwaway
  behind, `podman ps` shows it by image (`postgres:17` with a random
  name, and `testcontainers/ryuk`). Remove those, and only those, with
  `podman rm -f <name>`.

## PostgreSQL — the store

**Decided:** PostgreSQL 17, one instance, the whole service set
(ADR-0005). Its constraints and their enforcement are in ADR-0006.
The ground files:
- `compose.yaml` and `.env.example` at the root;
- `infrastructure/postgres/init/bootstrap.sql` (runs once, at first
  start);
- `infrastructure/postgres/verify-database-model.sql` (on demand);
- `infrastructure/flyway/` (the only DDL path).

### Stand it up

```sh
cp .env.example .env          # once; local-dev values work as they are
podman compose config         # sanity: renders clean, no warning
podman compose up -d          # first start: database created, bootstrap runs
podman compose ps             # expect: never-oversold-postgres Up (healthy)
```

Stood up here on 2026-09-11:
- image `postgres:17` pulled at digest `sha256:67f41722…`
  (PostgreSQL 17.11);
- the container reported healthy after 6 s;
- the log shows `running /docker-entrypoint-initdb.d/bootstrap.sql`,
  then `CREATE ROLE`, `CREATE ROLE`, `CREATE SCHEMA`.

**A trap, lived:** the image runs the bootstrap against a
*temporary* server, then restarts. The health check can report
healthy during that temporary server, and a query in that window
fails with `the database system is shutting down`. Wait a second
and retry. `pg_isready` plus one real query is the reliable sign
that it is up.

### Verify — both ways, always

**Catalog check:** the ground's real state against the model's
claims. Expected results are comments beside each query:

```sh
podman exec -i never-oversold-postgres \
  psql -U postgres -d never_oversold < infrastructure/postgres/verify-database-model.sql
```

Seen here, all six as the file states:
- two roles, neither a superuser, neither able to create databases
  or roles, both able to log in;
- the database owned by `postgres`;
- the schema `never_oversold` owned by `migrator`, and `public` by
  `pg_database_owner`;
- `runtime` on the schema: USAGE yes, CREATE no;
- default privileges for `runtime`: tables: SELECT, INSERT, UPDATE,
  DELETE; sequences: USAGE, SELECT; none grantable;
- CONNECT: `migrator` t, `runtime` t, PUBLIC f.

**Behavioral check:** two constraints, C1 and C3, attempted and
refused, live:

```sh
# C1: the application cannot change structure
podman exec never-oversold-postgres \
  psql -U runtime -d never_oversold -c 'CREATE TABLE t(i int);'
# expected: ERROR:  permission denied for schema never_oversold

# C3: an ungranted role cannot connect (a probe role, dropped after)
podman exec never-oversold-postgres psql -U postgres -d never_oversold -c "CREATE ROLE probe LOGIN;"
podman exec never-oversold-postgres psql -U probe -d never_oversold -c 'select 1'
# expected: FATAL:  permission denied for database "never_oversold"
podman exec never-oversold-postgres psql -U postgres -d never_oversold -c "DROP ROLE probe;"
```

Seen here: both refused with exactly those messages.

**T2's tool, shown:** the store makes two concurrent writers to
one thing disagree. The promise's evidence needs that facility, so
the ground proves it exists before any schema. The probe uses an
advisory lock because it needs no table. It is not the option a
slice should prefer. Two sessions as `runtime`, one holding a lock,
the second made to fail by the server:

```sh
podman exec -d never-oversold-postgres \
  psql -U runtime -d never_oversold -c "SELECT pg_advisory_lock(42), pg_sleep(6);"
podman exec never-oversold-postgres \
  psql -U runtime -d never_oversold -c "SET lock_timeout = '1500ms'; SELECT pg_advisory_lock(42);"
# expected: ERROR:  canceling statement due to lock timeout
```

Seen here: the second session was refused after 1.5 s. Advisory locks
are one form of the facility the server offers to serialize writers
or refuse one (T2). The full list is the infrastructure contract's
inventory. Which form a slice uses is that slice's decision, made in
its plan. The ground proves only that the facility refuses.

**Flyway as `migrator`:** it connects and sees the schema. On a
fresh ground nothing is applied yet, and every migration in the
folder is listed as pending:

```sh
podman compose run --rm flyway info
# expected on a fresh ground: Database: ...never_oversold (PostgreSQL 17.x);
#           Schema version: << Empty Schema >>; every migration
#           listed as Pending
```

Then apply them (*Migrate*, below), and `info` lists each as Success.

Seen here, 2026-09-11, before migration V1 existed: Flyway 11.20.3,
`No migrations found`, exit 0.

### Read the store from outside

Inspection reads persisted state from the host, as `runtime`,
through the published port. The evidence reads its own throwaway
store instead (*The test runtime*, above). This host has no `psql`
client, so a client container on the host network does the job:

```sh
podman run --rm --network host docker.io/library/postgres:17 \
  psql "postgresql://runtime:runtime_localdev@localhost:${POSTGRES_PORT:-5432}/never_oversold" -c 'select 1'
# expected: 1
```

Seen here: `1`. With a host `psql`, the same URL works directly.

### Migrate

Schema arrives only through Flyway, as `migrator`:
`infrastructure/flyway/migrations/V<n>__<description>.sql`, then
`podman compose run --rm flyway migrate`. Never put grant
statements in migrations. The default privileges hand every new
table and sequence to `runtime`.

**Before migration V3, read the rows first.** Migration V3's check
judges an item only when a later transaction touches it, so an item
already out of step would refuse every valid write to it afterwards.
On a ground below migration V2 (this ground took V2 and V3
together), as `runtime`:

```sh
podman exec never-oversold-postgres psql -U runtime -d never_oversold -c "
SELECT i.id, i.reserved, coalesce(sum(r.quantity), 0) AS held_by_reservations
  FROM item i LEFT JOIN reservation r ON r.item_id = i.id
 GROUP BY i.id, i.reserved
HAVING i.reserved <> coalesce(sum(r.quantity), 0);"
# expected: (0 rows)
```

The query counts every reservation as unended, which is true below
migration V2, where nothing has a receipt. If a row appears, stop.
Putting it right is a decision, not a command. A dump first costs a
second, and is the only way back once Flyway has moved forward:

```sh
podman exec never-oversold-postgres \
  pg_dump -U postgres -d never_oversold --format=custom > ground.dump
```

Seen here, 2026-10-07, on a ground at migration V1:
- 0 rows (one item, `proof-item`, 2 held by one reservation of 2);
- dumped;
- `migrate` applied migrations V2 and V3. The catalog showed the
  receipts' key, reference and kind check, the guard, and the
  check's three triggers, enabled and deferred.

Migration V4 adds an index and nothing else, so it needs no row
check before it. The dump is still taken. Seen here, 2026-10-08:
verified both ways, dumped, and `migrate` applied migration V4. The
catalog showed `reservation_by_item` on `reservation (item_id)`,
and every trigger still enabled.

### Reset — destructive

The bootstrap runs only against an empty volume. So after any change
to the bootstrap, or to start over, empty the volume. **This drops all
data. Confirm first:**

```sh
podman compose down --volumes
podman compose up -d
```

…then verify both ways again.

### Day to day

Start with `podman compose up -d`. Stop with `podman compose down`.
Data survives in the named volume `never-oversold_postgres-data`.

Inspect as `runtime`, and only read. The ledger is the only writer of
the store's data, by the definition's T4. The grants do not enforce
that assumption: the password in `.env` can write every table. Never
wire `migrator` or `postgres` into anything that runs.
