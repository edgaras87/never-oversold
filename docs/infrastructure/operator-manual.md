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

<!-- Service sections follow as each service is stood up and
     verified. -->
