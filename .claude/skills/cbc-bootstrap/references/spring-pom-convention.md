<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     system-bootstrap/.claude/skills/cbc-bootstrap/references/spring-pom-convention.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: none — verbatim below this header. -->

# Spring pom convention — how the pom is written and kept

Scope: Spring Boot on Maven, the Boot 4 line. Governs the pom's **shape**
only — which dependencies are chosen is the bootstrap decision's; when they
enter is the implementer's judgment under the governing rule.

## The governing rule

**A dependency enters only at the step or slice that earns it — nothing
ahead of need.** The pom is a record of what the system has proven it
requires, not a provisioning wishlist. The bootstrap's initial set is
deliberately small; the harness, the driver, and every later library arrive
at their own commits, each with its earning reason live in the file.

## Every entry carries its why

The inheriting reader's question is never *what* — the coordinates say
that — but ***why is this here***:

- **Every runtime dependency carries a comment above it** stating what
  capability it buys in the project's own terms — what the system does with
  it, and, where a decision hangs on it, which boundary or guarantee it
  serves. Capability, not requirement provenance.
- **Every test companion carries its own line.** Where a companion exists
  only to satisfy another module's auto-configuration, the comment says so
  plainly — that comment is the only thing between the next reader and a
  mystifying removal.
- **Every plugin carries a comment** on why it is configured and what its
  configuration changes; a plugin with default behavior and no reason to be
  there is removed, not commented.
- **Comments state facts and reasons, never instructions to a reader.**
- **No dependency whose earning reason cannot be written honestly** — if
  the comment can't be written, the dependency hasn't been earned.

## Grouping and order

Group by **the capability served**, never by scope; blank line between
groups. Within a group: runtime dependency first, its test companion
immediately after — the pairing visible at a glance. Scope-sorted poms
(all compile, then all test) scatter each capability's story.

The Boot 4 pairing rule is stated **once**, as a block comment above the
dependency section: runtime starters pair with their `-test` companions;
where no companion exists, the runtime dependency stands alone and the gap
is left uncommented (its absence is not a decision).

## What the file does not carry

- No generated empty metadata: `<url/>`, `<licenses>`, `<developers>`,
  `<scm>` deleted unless actually populated.
- `<description>` filled from the project's intent — one line, the
  problem's language, not the artifact's name restated.
- Versions are the parent's wherever the parent manages them; an explicit
  version is itself a decision and carries a comment saying why.

## Afterwards

Every slice that adds a dependency writes it the same way: same group
discipline, same earning reason.
