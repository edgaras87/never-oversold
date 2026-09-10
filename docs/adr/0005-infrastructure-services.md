# 0005. Infrastructure services: PostgreSQL, and nothing else

Date: 2026-09-11
Status: Accepted

## Context

The ground provides services to the system; the service set is
decided strictly by what the registry's slices need their evidence
to be able to do (`docs/system/registry.md`), on the environment
ADR-0004 decided. Habit provisions caches, queues and dashboards;
need is the only admission here, and every refusal is written with
its why.

## The evaluation, slice by slice

**SL-1 — no over-admission under contention.** The evidence races
many admits, from several instances, against one item's last units
and reads the witness from persisted state afterwards. It requires:
a store shared by every instance and outliving them; the store's
own way to make two concurrent writers to one item disagree (the
definition's trust line T2) — a serializing lock, a conditional
write, a constraint the store enforces on commit; and the state
readable from outside the system by a plain query. A stale read
(F22) is created by the race itself — a check read before a
competing write commits — and needs no separately lagging service.

**SL-2 — the correction never undercuts the holds.** Sequential
requests against the same store; nothing beyond SL-1's.

**SL-3 — a reservation exits once.** Duplicated, racing and late
exits against the same store; expiry is the application's reading
of the machine's clock, not a scheduled job in a service.

**SL-4 — consume's two moves hold together.** The store must offer
atomic commit of two writes, so that a kill mid-work leaves either
both or neither; and an unknowable outcome is created by freezing
the store (`podman pause`, ADR-0004), not by a service.

**Every slice** reads the witness the same way: a query against the
store from the host, outside the application.

## Options considered

1. **PostgreSQL**, one instance, the official image pinned to a
   major version. Offers every tool T2 can name — row locks,
   serializable isolation, constraints checked at commit, unique
   indexes, advisory locks — and leaves the choice to the slice;
   atomic multi-statement transactions for SL-4; a grant system that
   enforces the ground's constraints itself (ADR-0006 realizes
   them); reachable from the host by `psql` for the witness read.
   Standing knowledge exists for its governance.
2. **An embedded or file store** (SQLite and kin). Defeated by the
   runtime ground: several instances must share one store that
   outlives them, and the witness must be read from outside while
   instances run. Rejected.
3. **A key-value store with conditional writes** (Redis-like).
   Offers a conditional write but not atomic commit of two writes
   with the same guarantees, and the witness read is a scan, not a
   query; SL-4's converge obligation would be built in the
   application. Rejected: more application, less ground.

## Decision

Option 1: PostgreSQL, one instance, is the whole service set.

**Not provisioned, and why:**

- *A cache.* No slice's evidence needs one; a cache adds the
  stale-read enemy the promise already fights, for nothing.
- *A queue or broker.* No asynchronous work exists: every request
  is admitted or refused at the door, synchronously; expiry is the
  application's clock, not a delayed message.
- *A read replica.* F22's adversity is created by the race itself;
  a lagging replica is a topology the census listed as not probed.
- *A second datastore.* One owner, one area (definition L3); the
  two numbers live in one place by construction.
- *A clock service.* The clock is the machine's (definition L1,
  runtime ground); SL-1's flagged evidence controls the clock the
  application reads, an application-level injection.
- *A load generator or evidence service.* The evidence harness is
  the application's own test code, born at bootstrap; not ground.
- *A reverse proxy or load balancer* in front of the instances. The
  evidence addresses instances directly and must know which one
  served; a balancer would hide exactly that.
- *Monitoring, alerting, dashboards.* Excluded by the intent: nobody
  operates this system for real.

**Tooling, not a service:** a migration tool (Flyway) runs at
migration time as the structure authority; it is part of how the
store's constraints are realized (ADR-0006), not a service the
system talks to.

## Consequences

Good: one service to stand up, verify, and explain; every lever the
evidence needs is either the store's own or the environment's; a
stranger's machine needs podman and nothing else. Bad: PostgreSQL
carries governance that must be done right or the ground lies —
roles, ownership, the public schema — which is the next decision's
whole job; and the choice of *which* of its tools SL-1 uses is
deferred to the slice, so the ground must prove that at least one
exists and refuses, not which is best.
