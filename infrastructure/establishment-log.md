# Establishment log — the ground, as it was walked

<!-- The lived-result record of standing the ground up: for every
     executing step, the command, its expected result, and what
     actually happened. Decisions with their options live in
     docs/adr/; this log holds the walk. Append-only. -->

## 2026-09-10 — records, readiness, the environment

**Record mapping.** The walk's default records land as follows in
this repo: this log at `infrastructure/establishment-log.md`; the
ground files under `infrastructure/`; the infrastructure contract
at `infrastructure-contract.md` and the operator manual at
`operator-manual.md`, both at the root beside the other records;
the ground's decisions as ADRs in `docs/adr/` (the records table
sends decisions there), this log keeping the commands and outcomes.

**Readiness.** Checked on the actual repo:
- The framing artifacts exist and are settled: `docs/system/`
  holds `intent.md`, `definition.md`, `registry.md`, adopted by
  ADR-0002; the registry has four slices, SL-1 chosen-next.
- The definition's runtime facts: absent at first check — the
  framing had left them unstated. Added by a dated revision entry
  (L1, "The runtime ground", commit `44f256d`) before this walk
  moved. Now readable: one local machine, a stranger's clean
  machine, plural instances, the store as a service, one clock.
- The repo is a git repository with `.gitignore` present.
Readiness passed.

**The environment decision.** ADR-0004: podman local containers,
compose-driven; the slices' demands derived there; the default
checked and kept, no defeater found.

**Engine-level proof of the default**, before any ground file.
Each line: what was expected → what actually happened.

| Command | Expected | Actual |
|---|---|---|
| `podman version` | engine 5.x, client and API the same | 5.8.2 / API 5.8.2, linux/amd64 |
| `podman compose version` | a provider answers | external provider `docker-compose`, Docker Compose v2.39.4-desktop.1 |
| `podman info` (rootless, cgroups, runtime, distro, arch, SELinux) | rootless, v2, crun, Fedora | rootless=true, cgroups v2, crun, fedora 42, amd64, kernel 6.19.14, SELinux enabled |
| `podman pull docker.io/library/alpine:3.20` | digest, exit 0 | digest `bf8527eb…`, exit 0 |
| `podman run --rm alpine:3.20 sh -c 'echo ground ok'` | prints `ground ok`, exit 0 | `ground ok`, exit 0 |
| two `podman run -d` at once, then `podman ps` | both Up simultaneously | `ng-a Up`, `ng-b Up` |
| `podman kill` on a running container | gone from `podman ps -a` | 0 matches after kill |
| `podman pause` then `podman unpause` | state paused, then running | `paused`, then `running` |

All eight as expected. Test containers removed; no leftovers.

**Implications recorded for the operator manual:** rootless, so
published ports must be above 1024; SELinux enforcing, so bind
mounts need the `:Z` label; the compose front door is
`podman compose`, answered here by an external docker-compose
binary — the canonical command is what the manual states.
