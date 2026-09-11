# Operator manual — standing the ground up and using it

<!-- The operator's full stand-up-and-use truth, written from the
     lived setup at the moment it happened, never reconstructed.
     One section per part of the ground, grown as each is stood
     up. What any machine must be is the definition's (L1, the
     runtime ground); what this machine is, is here. -->

## The execution environment

**Decided:** podman local containers, compose-driven, rootless
(ADR-0004). One machine runs everything: the store, the ledger's
instances, the evidence.

### What a machine needs in hand

- **podman**, 5.x. On Fedora: `sudo dnf install podman`. Host
  installs are the operator's own act; nothing here installs for
  you.
- **A compose provider** answering `podman compose`. Podman does
  not ship one; it delegates to whatever provider it finds —
  `podman-compose` (Python) or Docker's `docker-compose` plugin —
  and says which at the top of every `podman compose` call. Either
  works for this ground; the canonical command throughout this
  manual is `podman compose`, never the provider's own name.
- **Rootless podman**, the default on Fedora: a user with a
  subordinate uid range (`/etc/subuid` has a line for you, which
  the Fedora installer writes).

### The machine this was stood up on

Verified by execution on 2026-09-10; the commands are the check a
stranger runs on their own machine, the expected results beside
them.

| Check | Command | Expected | Seen here |
|---|---|---|---|
| engine | `podman version` | `Version: 5.x`, client and API the same | 5.8.2, API 5.8.2, linux/amd64 |
| compose front door | `podman compose version` | a provider announces itself, then its version | external provider `docker-compose`, Docker Compose v2.39.4 (a second provider, `podman-compose` 1.5.0, is installed but not the one podman chose) |
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
- **Volumes on SELinux hosts.** A bind mount needs the `:Z` label
  or the container cannot read it; named volumes need nothing.
  This ground uses named volumes for the store's data and `:Z` on
  any bind mount it declares.
- **User namespaces.** Inside a container, root is your user
  outside; files a container writes to a bind mount are owned by
  you. Nothing here runs as host root.
- **Two providers.** If `podman compose` announces a provider you
  did not expect, both work; to pin one, set
  `compose_providers` in `containers.conf` — not needed for this
  ground.

### The levers the evidence pulls

Proven at the engine level on 2026-09-10 (the devlog's Step 3
entry has the outputs); the compose-level shapes arrive with the
services:

- **Several instances at once:** two containers of one image ran
  simultaneously — `podman compose up --scale <service>=N` at the
  compose level.
- **Kill mid-work:** `podman kill <container>` lands SIGKILL; the
  container is gone from `podman ps -a`.
- **Freeze a service:** `podman pause <container>` stops it
  responding — an in-flight write's outcome becomes unknowable to
  its caller — and `podman unpause` resumes it.

### The test runtime — once per machine

The evidence harness (the application's test code, under the one
standard test command) starts its own throwaway PostgreSQL through
Testcontainers, so the compose ground does not need to be up for
tests. Testcontainers needs a container-runtime socket; on rootless
podman that is the user's own socket unit.

```sh
systemctl --user enable --now podman.socket
ls -l "$XDG_RUNTIME_DIR/podman/podman.sock"     # expected: the socket file exists
```

Then point the library at it — **in the home directory, never the
project root**; the file binds only from `$HOME`:

```
# ~/.testcontainers.properties
docker.host=unix:///run/user/<uid>/podman/podman.sock
```

`<uid>` is `id -u`. Check with the standard test command:
`./mvnw test` — expected: a `postgres:17` container starts, the
tests run, and within seconds of the JVM's exit `podman ps` shows
no throwaway left.

Seen here, 2026-09-12: podman 5.8.2 rootless, the socket unit
`active`, the socket file present; Testcontainers 2.0.5 (the
version the build manages) started `postgres:17` in 6 s and the
suite ran green; Ryuk, the library's reaper, ran as a container
of its own and removed both throwaways within about ten seconds
of the JVM's exit.

**Traps, lived:**

- The socket unit can report *active* with the socket file
  missing. Active is not enough: stop socket and service user
  units, start the socket again, confirm the file exists.
- Older guidance disables Ryuk under rootless podman with a
  `ryuk.disabled=true` line in the properties file. Testcontainers
  2.x does not read that key — it is inert — and Ryuk worked on
  this host as is. If a hard-killed test JVM ever strands a
  throwaway, `podman ps` shows it by image (`postgres:17` with a
  random name, and `testcontainers/ryuk`); remove those and only
  those with `podman rm -f <name>`.

## PostgreSQL — the store

**Decided:** PostgreSQL 17, one instance, the whole service set
(ADR-0005); its constraints and their enforcement in ADR-0006. The
ground files: `compose.yaml` and `.env.example` at the root,
`infrastructure/postgres/init/bootstrap.sql` (runs once, at first
start), `infrastructure/postgres/verify-database-model.sql` (on
demand), `infrastructure/flyway/` (the only DDL path).

### Stand it up

```sh
cp .env.example .env          # once; local-dev values work as they are
podman compose config         # sanity: renders clean, no warning
podman compose up -d          # first start: database created, bootstrap runs
podman compose ps             # expect: never-oversold-postgres Up (healthy)
```

Stood up here on 2026-09-11: image `postgres:17` pulled at digest
`sha256:67f41722…` (PostgreSQL 17.11); the container reported
healthy after 6 s; the log shows
`running /docker-entrypoint-initdb.d/bootstrap.sql`, then
`CREATE ROLE`, `CREATE ROLE`, `CREATE SCHEMA`.

**A trap, lived:** the image runs the bootstrap against a
*temporary* server and then restarts. The health check can report
healthy during that temporary server, and a query in that window
fails with `the database system is shutting down`. Wait a second
and retry; `pg_isready` plus one real query is the honest "up".

### Verify — both ways, always

**Catalog check** — the ground's real state against the model's
claims; expected results are comments beside each query:

```sh
podman exec -i never-oversold-postgres \
  psql -U postgres -d never_oversold < infrastructure/postgres/verify-database-model.sql
```

Seen here, all six as the file states: two roles, neither super,
neither creates databases or roles, both log in; the database owned
by `postgres`; the schema `never_oversold` owned by `migrator`,
`public` by `pg_database_owner`; `runtime` USAGE yes, CREATE no;
default privileges for `runtime`: tables SELECT, INSERT, UPDATE,
DELETE and sequences USAGE, SELECT, none grantable; CONNECT
`migrator` t, `runtime` t, PUBLIC f.

**Behavioral check** — the constraint attempted and refused, live:

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

**T2's tool, shown** — the store makes two concurrent writers to
one thing disagree; the promise's evidence needs the facility, so
the ground proves it exists before any schema. The probe uses an
advisory lock because it needs no table — it is not the face a
slice should prefer. Two sessions as `runtime`, one holding a lock,
the second made to fail by the server:

```sh
podman exec -d never-oversold-postgres \
  psql -U runtime -d never_oversold -c "SELECT pg_advisory_lock(42), pg_sleep(6);"
podman exec never-oversold-postgres \
  psql -U runtime -d never_oversold -c "SET lock_timeout = '1500ms'; SELECT pg_advisory_lock(42);"
# expected: ERROR:  canceling statement due to lock timeout
```

Seen here: the second session refused after 1.5 s. Advisory locks
are one of the server's serializing tools; row locks, unique and
check constraints, and serializable isolation are the same
facility's other faces. Which one a slice uses is that slice's
decision, made in its specification; the ground proves only that
the facility refuses.

**Flyway as `migrator`** — connects, sees the schema, empty history,
which is correct on a fresh ground:

```sh
podman compose run --rm flyway info
# expected: Database: ...never_oversold (PostgreSQL 17.x);
#           Schema version: << Empty Schema >>; No migrations found
```

Seen here: Flyway 11.20.3, exactly that, exit 0.

### Read the witness from outside

The evidence reads persisted state from the host, as `runtime`,
through the published port. This host has no `psql` client; a
client container on the host network does the job:

```sh
podman run --rm --network host docker.io/library/postgres:17 \
  psql "postgresql://runtime:runtime_localdev@localhost:${POSTGRES_PORT:-5432}/never_oversold" -c 'select 1'
# expected: 1
```

Seen here: `1`. With a host `psql`, the same URL works directly.

### Migrate

Schema arrives only through Flyway, as `migrator`:
`infrastructure/flyway/migrations/V<n>__<description>.sql`, then
`podman compose run --rm flyway migrate`. No grant statements in
migrations, ever — the default privileges hand every new table and
sequence to `runtime`.

### Reset — destructive

The bootstrap runs only against an empty volume. After any change
to it, or to start over — **this drops all data; confirm first**:

```sh
podman compose down --volumes
podman compose up -d
```

…then verify both ways again.

### Day to day

`podman compose up -d` / `podman compose down` (data survives in
the named volume `never-oversold_postgres-data`); inspect as
`runtime` — never wire `migrator` or `postgres` into anything that
runs.
