# 0009. The evidence harness drives more than one instance, as separate processes

Date: 2026-09-11
Status: Accepted

## Context

The bootstrap proves the evidence harness can **create** one named
adversity end to end before any slice needs it: contention, in
SL-1's shape — concurrent admits on one item's last units
(`docs/system/registry.md`). The intent's own definition of done
says from whom the racers come: "from more than one caller and
more than one instance of the ledger — never a sequential replay
pretending" (`docs/system/intent.md`). The definition's runtime
ground makes plural instances a fact, not a deployment choice (L1,
F17). TODO carried the demand into this step from Step 3: a
single-process pass proves a shape nobody runs.

The method's harness reference, lived twice, stages the race
inside one test process: a hundred requests held at a barrier,
released at one instant, each a full round-trip through the real
door. That shape is proven and cheap. It is also exactly where this
run must leave the reference.

## Options considered

1. **One process, many threads at a barrier** — the reference's
   shape. A lock inside the process, a `synchronized` block or a
   JVM-wide structure, makes the race pass while two real instances
   would still oversell; the harness could not tell the difference.
   Rejected: green on a shape nobody runs.
2. **Several application contexts in one test JVM**, on different
   ports, over one store. Separate bean instances, but one process:
   any static or JVM-wide state crosses the contexts and the same
   false green is possible. Rejected for the same reason, with
   more machinery.
3. **Separate operating-system processes**, two or more instances
   of the system, each addressed through its own door, all against
   one store, the racers released at one instant across them, and
   the witness read from the store directly — never through an
   instance. Only a mechanism in the store, or a protocol every
   process honours, can pass this. Chosen.

## Decision

Option 3. The harness can start **N ≥ 2 instances of the system as
separate processes** against one store, address each instance
through its own HTTP door, release requests at one instant across
all of them, assert every response, and read the witness from the
store from outside. The bootstrap proves this machinery on the
probe; SL-1 aims it at the invariant.

Two constraints bind the realization, which is the bootstrap's
implementation judgment (PLAN Step 4, Stage 4), not this record's:

- **The one standard test command stays self-contained.** The suite
  runs from a clean clone with only the README's prerequisites in
  hand; the store the harness races against is its own throwaway
  instance of the ground's major version, migrated harness-side
  from the one migrations home — the ground does not need to be up.
- **An instance's listen port is set from its environment**, so
  several coexist on one machine; the harness owns the processes'
  lifecycle — started before the race, stopped after, none left
  running on a failed test.

Whether the processes are forked from the build's own output or run
as containers of the system's image is decided at Stage 4 from what
the build makes cheap, and recorded in the devlog with its why.

## Consequences

Good: the evidence's shape is the deployment's shape from day one;
a false pass through in-process locking is impossible by
construction; SL-1 inherits the shape, and F15's kill-mid-work
becomes a natural extension because an instance is a process the
harness can kill. Bad: this is off the harness reference at its
concurrency probe, so the lived traps stop at the process
boundary — process start-up time, port allocation, log capture and
clean shutdown are this run's to learn and to harvest back; the
race test is slower than the in-process one; and a single-process
barrier test may still be worth keeping beside it as the cheap
first check, which is Stage 4's call.
