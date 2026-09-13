# 0008. Application structure: package by feature, boundaries package-private

Date: 2026-09-11
Status: Accepted

## Context

How the application's packages are shaped is a bootstrap decision,
recorded before the code that lives under it, and it governs all
later code: slices enter the structure decided here, they do not
re-decide it. The slices themselves rarely discriminate between
structures, so the deciding inputs are the run's own — fluency,
audience, and any stated intent to practice a pattern.

This system has **one problem area**: the reservation ledger
(`docs/system/definition.md`, L3). Four slices land in it. Its
claim is that an invariant holds in persisted state under
contention; the mechanism that keeps it will be a transaction plus
the store's own facility for making two writers disagree
(ADR-0006, T2). The reviewer's stated intent, 2026-09-11: learn
what a professional shape is here — considered explicitly, not
defaulted into.

## Options considered

1. **Package by feature, package-private boundaries, depth earned
   per feature** — the lived default of the method (the bootstrap
   skill's app-structure reference). One package per problem area;
   classes package-private unless another feature genuinely calls
   them, so the compiler enforces the boundary; a controller,
   service or persistence split appears *inside* a feature when
   that feature's own complexity earns it, never as a repo-wide
   template.
2. **Classic layered** — packages by technical role (controller,
   service, repository). The long-standing enterprise default. The
   package list names the technology, not the problem; every class
   is public across layer packages, so the boundaries are
   convention only; one feature's change touches three packages.
   Rejected: it is the same layering as option 1 with the
   problem's name removed from the folder and the compiler removed
   from the boundary.
3. **Hexagonal (ports and adapters).** Domain logic in the center
   with no framework imports; store and HTTP behind port interfaces
   with adapters at the edge. Pays when many integrations must be
   swapped or faked. Here it would put the invariant in a pure
   in-memory domain class and treat the store as a detail behind a
   port — the naive check-then-write shape the promise exists to
   rule out; the real mechanism lives in the transaction at the
   edge. Rejected for this system: the pattern's center would be
   empty or dishonest. Named as the pattern worth practising on a
   project with several areas and an external integration.
4. **Modular monolith (Spring Modulith).** Option 1 plus tooling
   that verifies module boundaries. Meaningful with several
   modules; there is one. Rejected: nothing to verify yet. It is
   the step to consider if areas multiply — a dated revision, not a
   bootstrap choice.

## Decision

Option 1, the lived default: **package by feature, package-private
by default, depth earned per feature.** The base package
`io.github.edgaras87.neveroversold` holds one feature package per
problem area — the ledger, when SL-1 lands — plus the harness's
test-support package and, at bootstrap only, the probe that dies
at the first slice. Layers inside the ledger are earned slice by
slice: each slice's specification says what its complexity
demands, and the structure grows there and nowhere else. Deviating
from this shape later is an off-default decision with its own
dated record.

## Consequences

Good: the package list reads as the problem's table of contents;
the boundary is enforced by the compiler, the same stance as the
ground's grant split; the shape is what a modular monolith grows
from, so it is not a dead end. Bad: the professional skill on show
is restraint — deciding structure as need arrives — which is less
visible than a pattern's scaffolding; the ADR is the visibility,
and each slice's own structural choice is written in its
specification.

## Notes

- 2026-09-13 — inside a feature, sub-packages are allowed for
  stateless vocabulary — value types and the answers a door gives —
  which are then public, since a value or an exception makes no
  promise a future feature could abuse; everything with behaviour or
  a write stays at the feature's root, package-private, so the
  compiler still guards the only boundary that matters. Sub-packages
  must not depend on each other in a cycle. Triggered by SL-1's first
  thirteen files: the reviewer found a flat feature folder hard to
  navigate, a legitimate deciding input (the reader's fluency). The
  default above stands; a sub-package for *behaviour* is still a
  revision here, since its seam classes become public.
