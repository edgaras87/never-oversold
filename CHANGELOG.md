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

### Changed

- Named `never-oversold`: the name is the promise's negation, ruled
  out. The working name safe-reservations is overturned (ADR-0003).

### Added

- The infrastructure ground: PostgreSQL 17 under podman compose,
  governed by a two-role authority split, verified both ways; stood
  up from a clean machine by the operator manual's commands.
- The promise, the system definition and the slice registry under
  `docs/system/`: what this ledger will guarantee, what it owns
  and refuses, and the four invariants to be proven — written
  before any code.
