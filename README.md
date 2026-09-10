# never-oversold

An inventory-reservation ledger built to keep one promise: **for
any item, the reserved quantity never exceeds the on-hand-count,
however many reservations race for the same units.** It exists as
a demonstration for a reader judging correctness-first
construction — the claim stated before any code, the system
derived from it, and evidence that stages the contention for real.
Why this claim, what it excludes, and what done means:
[docs/system/intent.md](docs/system/intent.md).

The ledger owns, per item, its on-hand-count and its reservations,
and decides three things: whether a reservation is admitted,
whether a change to the count is admitted, and whether an exit —
release, expiry, consume — is a reservation's first and only. It
refuses what cannot make the promise false: whether a repeated
request is the same one, how long a hold lasts, which items exist,
what a warehouse actually holds, who may act. The environment it
stands in, what dies without it, and who owns what:
[docs/system/definition.md](docs/system/definition.md).

## The invariants, planned

- No over-admission under contention — the sum of active
  reservations never exceeds the on-hand-count, however many admits
  race. *(chosen next)*
- The correction never undercuts the holds — no admitted change to
  the on-hand-count leaves it under the reserved sum.
- A reservation exits once — its numbers move at most once on
  exit, and never after it has ended.
- Consume's two moves hold together — no readable state has the
  reservation ended without the count lowered, or the reverse.

Each with the adversity its evidence must create, and its status:
[docs/system/registry.md](docs/system/registry.md).

Built by correctness-by-construction: what must never happen first,
features last. The method: [docs/concept/](docs/concept/), start
with [00-cbc.md](docs/concept/00-cbc.md).

**Status:** framed and named 2026-09-10; the ground stands,
verified, 2026-09-11. No code yet; next is the skeleton and the
evidence harness.

## Prerequisites

- podman with a compose provider (`podman compose` answers) — the
  infrastructure ground; details in
  [docs/infrastructure/operator-manual.md](docs/infrastructure/operator-manual.md)

## Project records

| Record | Where | What it answers |
|---|---|---|
| System | [docs/system/](docs/system/) | What it promises, owns, refuses; what to work next |
| Operator manual | [docs/infrastructure/operator-manual.md](docs/infrastructure/operator-manual.md) | How to stand the ground up, verify it, reset it |
| Infrastructure contract | [docs/infrastructure/infrastructure-contract.md](docs/infrastructure/infrastructure-contract.md) | What the builder may rely on: identities, reachability, refusals |
| Plan | [PLAN.md](PLAN.md) | Where are we, what's next, what does *done* mean |
| Decisions | [docs/adr/](docs/adr/) | Why is it built this way |
| Architecture | [ARCHITECTURE.md](ARCHITECTURE.md) | What is the current shape of the system |
| Backlog | [TODO.md](TODO.md) | What's known but not done |
| Changelog | [CHANGELOG.md](CHANGELOG.md) | What changed per version (for users) |
| Devlog | [devlog/](devlog/) | Day-to-day work, dead ends, open questions |

<!-- A line here is true now, and meant for someone arriving from
     outside. What changes weekly is PLAN.md's; why is the ADRs'; how
     it went is the devlog's. A missing section is not an omission: it
     arrives when a step's gate makes it true — projection follows
     truth. The paragraphs above are derived from docs/system/ and
     never authored here: a fact the reader needs that is not in an
     export marks a gap in the export — fix it there, re-derive. -->
