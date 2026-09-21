# Changelog

All notable changes to this project are documented here.
Format: [Keep a Changelog](https://keepachangelog.com/) ·
Versioning: a version is a state of the evidence, in
[SemVer](https://semver.org/) form. 0.N while the registry's
slices close — N counts the slices evidence-closed, 0.0 being the
framed system with none; 1.0.0 when every registry slice is closed
and the release gate has passed. Later claims, added by dated
revision, start a new minor.

<!-- Write entries WHEN the change lands, in user-speak
     ("order creation is 3x faster"), not developer-speak
     ("refactored OrderService") — users may be future-you, or
     another repo pinning this one. Categories:
     Added · Changed · Deprecated · Removed · Fixed · Security —
     use the ones this repo's versions can need.
     Releasing = rename [Unreleased] to a version + date, open a
     fresh one. -->

## [Unreleased]

## [0.2] — 2026-09-21

The second invariant evidence-closed: SL-2, the correction never
undercuts the holds. Nothing you can do with the service changed;
what changed is that the behaviour is now decided, and proven
against the three ways it could have gone wrong.

### Changed

- Adjust: a count under the units held is refused — settled now,
  not "for now". The alternative, letting a correction end
  reservations until the number fits, was weighed and rejected:
  deciding whose hold dies is the seller's policy, not this
  ledger's. What it costs you, said plainly: an operator who counts
  the shelf and finds fewer units than customers hold cannot make
  the ledger agree with the shelf. They wait for holds to expire or
  be released.
- Adjust, resent: sending the same correction twice does exactly
  what sending it once does. The count is a number you assert, not
  an amount to subtract, so a retry after a lost reply cannot lower
  it twice.
- Adjust, out of order: when two corrections arrive swapped, the
  count can keep the older number, and the ledger does not fix
  that. What it does guarantee is that neither one can drop the
  count under the units held — your count may disagree with the
  shelf, never with your customers' holds.

## [0.1] — 2026-09-14

The first invariant evidence-closed: SL-1, no over-admission under
contention.

### Added

- Reserve: `POST /items/{item}/reservations` with a quantity and a
  hold admits a reservation if the units still fit and answers the
  record as stored, or refuses with `409` and says what did not
  fit. For any item, the units held by reservations never exceed
  the on-hand-count — held by the store itself, shown to survive a
  hundred simultaneous requests and three instances of the ledger
  racing for the same last units.
- Adjust: `POST /items/{item}/adjustments` with an on-hand-count
  sets an item's count, creating the item if the ledger did not
  know it. A count under the units held is refused for now; what a
  correction should do instead is the next invariant's question.
- Every request that means nothing — a zero or absurd quantity, a
  missing field, an unknown item — is answered `400` or `404` as
  Problem Details and moves no number.
- A reservation carries an expiry set from the caller's hold by the
  store's clock, the one clock every instance shares.

### Changed

- Named `never-oversold`: the name is the promise's negation, ruled
  out. The working name safe-reservations is overturned (ADR-0003).

### Added

- The ledger runs: an empty skeleton on the ground, connecting as
  the runtime identity alone, with an HTTP door and a health check
  that names the store. No business behavior yet — one probe,
  scaffolding that dies at the first slice. Run and test commands
  in the README.
- The evidence harness, under the one test command: a throwaway
  PostgreSQL of the ground's version, migrations proven from the
  empty home, and a race across three real instances of the
  ledger — the machinery every invariant's evidence will use.
- The infrastructure ground: PostgreSQL 17 under podman compose,
  governed by a two-role authority split, verified both ways; stood
  up from a clean machine by the operator manual's commands.
- The promise, the system definition and the slice registry under
  `docs/system/`: what this ledger will guarantee, what it owns
  and refuses, and the four invariants to be proven — written
  before any code.
