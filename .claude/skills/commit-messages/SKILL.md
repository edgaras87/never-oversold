---
name: commit-messages
description: Conventional Commits format and the 50/72 rules. Use before writing any commit message.
---

# Commit Messages

## Format

```
<type>(<scope>): <description>          ← subject: ≤50 chars, imperative, no period
                                        ← blank line (mandatory)
<body>                                  ← the WHY, wrapped at 72 chars, optional
                                        ← blank line
<footer>                                ← issue refs, breaking changes, optional
```

The subject completes the sentence *"If applied, this commit will …"*:
"…Fix race in order creation" passes, "…Fixed race" fails.

## Types

| Type | Meaning | SemVer effect |
|---|---|---|
| `feat` | New user-facing capability | minor |
| `fix` | Bug fix | patch |
| `docs` | Documentation only (incl. PLAN, ADRs, devlog) | — |
| `refactor` | Restructuring, no behavior change | — |
| `perf` | Performance improvement | patch |
| `test` | Adding or fixing tests | — |
| `style` | Formatting, whitespace — no logic change | — |
| `build` | Build system, dependencies | — |
| `ci` | CI configuration | — |
| `chore` | Maintenance that fits nowhere above | — |
| `revert` | Reverts a previous commit (name it in body) | — |

Use a scope, the module or area touched, once the repo has more
than one area: `feat(auth):`, `fix(orders):`.

## Breaking changes

Flag with `!` after the type and explain in a footer; SemVer effect
is major.

```
feat(api)!: rename fields in /orders response

BREAKING CHANGE: `created` is now `created_at` (ISO 8601).
Clients must update field names.
```

## Footers

```
Closes #42            ← auto-closes the issue on merge
Refs #17, TODO.md     ← relates without closing
See ADR-0006          ← link the decision behind the change
```

A commit that implements a decision links the ADR; a commit that
resolves a TODO item names it.

## Examples

```
feat(auth): add token expiry

Tokens previously lived forever; any leaked token was permanent.
Expiry is 24h, configurable via AUTH_TOKEN_TTL. See ADR-0005 for
why sliding sessions were rejected.

Closes #31
```

```
fix(orders): prevent duplicate creation under race

Two concurrent POSTs could both pass the app-level uniqueness
check. Moved the guarantee to a partial unique index; the app now
maps the constraint violation to a 409.

See ADR-0006
Closes #42
```

```
docs: close Step 2 in PLAN, update ARCHITECTURE codemap
```

```
revert: feat(auth): add token expiry

Reverts commit abc1234 — expiry broke the mobile client's silent
refresh. Re-land after #58.
```

## The agent's own files

`CLAUDE.md`, `.claude/` and `CHANGE-PLAN.md` are the working
arrangement, not project records. A commit that touches them is
scoped `agent` — `chore(agent)` to install or update, `feat(agent)`
for a new skill, `docs(agent)` for a change-plan's lifecycle — and
touches nothing else.

## Rules

- **Commit on the word.** Stage, show the reviewer the diff, and
  commit only when they have said so; one commit at a time, and no
  push without the same word.
- **One logical change per commit.** "Fix X and update deps and
  rename file" is three commits.
- **The subject alone makes sense** in `git log --oneline`.
- **The body answers why**, and anything the diff cannot say:
  rejected alternatives, non-obvious constraints, "looks wrong but
  is right because…".
- **No `wip` / `fixes` / `asdf`** on the shared branch; squash
  locally first. Fine on a private branch.
- **Too small for an ADR but the reasoning matters:** the commit
  body is the record. Spend the two sentences.

---

## Decisions

- HANDBOOK ADR-0005 — Conventional Commits over plain 50/72
- HANDBOOK ADR-0019 — the agent's files and the project's records
  never share a commit
- HANDBOOK ADR-0035 — the stop is this file's sentence, gated
  nowhere
