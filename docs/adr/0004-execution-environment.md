# 0004. Execution environment: podman local containers, compose-driven

Date: 2026-09-10
Status: Proposed

## Context

The ground must let the evidence create the registry's adversities
(`docs/system/registry.md`) on the runtime ground the definition
states (`docs/system/definition.md`, L1): one local machine, a
stranger's clean machine by the README, several instances of our
process at once, the store as a service outliving them, one clock.

What the slices demand the evidence be able to *do*, derived slice
by slice:

- **SL-1:** run several instances of the ledger at once against one
  store and race them (F17); make a read stale (F22); race an
  operator's adjustment against admits (F10); read the witness from
  the store from outside the system.
- **SL-2:** send sequential requests only; nothing beyond SL-1's
  ground.
- **SL-3:** duplicate, race and delay requests; move the clock the
  instances see, or control it (F18, F23, F24) — a matter for the
  evidence and the application, not a ground service.
- **SL-4:** kill an instance mid-work (F16); make a write's outcome
  unknowable by cutting the instance from the store mid-write
  (F19).

So the ground must: start N instances of one image; kill one at
will; pause or partition the store from an instance; expose the
store to the host for the witness read; and stand up from nothing
by a stranger's commands.

## Options considered

1. **podman local containers, compose-driven** — the lived default.
   Checked against the demands above: `podman compose` scales one
   service to N; `podman kill` lands SIGKILL mid-work; `podman
   pause` freezes the store so an in-flight write's outcome is
   unknowable to the caller; a published host port exposes the
   store for the outside read; the whole stands up from a compose
   file on a clean machine with podman installed. Rootless on this
   host, so ports stay above 1024 and volumes on SELinux need `:Z`.
   No demand and no runtime fact defeats it.
2. **Bare processes on the host, no containers.** Weighed only
   because option 1 must be checked, not assumed: it defeats the
   stranger's clean machine — the store would be a host install
   with its own manual per OS — and makes "cut the instance from
   the store" a firewall exercise. Rejected.
3. **A hosted environment.** Rejected by the intent: no hosted
   deployment; the audience reads evidence on a machine, not a
   service.

## Decision

Option 1. The execution environment is podman local containers,
compose-driven, rootless on the reviewer's host. Proven at the
engine level before any ground file exists: version, compose
provider, rootless and cgroup facts, an image pulled, a container
run end to end, two containers alive at once, one killed, one
paused and resumed — the commands and their actual output in the
establishment log's first entry.

No compose file is born with this decision; its content is the
service evaluation's and the constraints' outcome.

## Consequences

Good: every adversity the registry names has a lever the evidence
can pull from outside the application; a stranger stands the ground
up with one tool installed. Bad: rootless podman's quirks — port
range, SELinux volume labels, the compose provider being a separate
binary — are facts the operator manual must carry, and a stranger
on another OS meets different ones.
