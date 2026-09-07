---
name: commit-messages
description: Conventional Commits format and the 50/72 rules. Use before writing any commit message.
delivery: pushed + gate
---

# Commit Convention

<!-- Format: Conventional Commits (conventionalcommits.org) on top of the
     classic 50/72 rules. Enforceable later with a commitlint hook.
     A good commit body is a micro-ADR: the diff shows WHAT, the body
     preserves WHY. -->

## Format

```
<type>(<scope>): <description>          ← subject: ≤50 chars, imperative, no period
                                        ← blank line (mandatory)
<body>                                  ← the WHY, wrapped at 72 chars, optional
                                        ← blank line
<footer>                                ← issue refs, breaking changes, optional
```

**Imperative test:** the subject must complete the sentence
*"If applied, this commit will …"* — "…Fix race in order creation" ✓,
"…Fixed race" ✗.

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

**Scope** is optional: the module/area touched — `feat(auth):`,
`fix(orders):`. Use it when the repo has more than one area; skip it while
everything is one area.

## Breaking changes

Flag with `!` after the type, and explain in a footer:

```
feat(api)!: rename fields in /orders response

BREAKING CHANGE: `created` is now `created_at` (ISO 8601).
Clients must update field names.
```

SemVer effect: major.

## Footers

```
Closes #42            ← auto-closes the issue on merge
Refs #17, TODO.md     ← relates without closing
See ADR-0006          ← link the decision behind the change
```

<!-- Connect the trails: a commit that implements a decision links the ADR;
     a commit that resolves a TODO item names it. -->

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

## Scope note: the agent's own records

The working arrangement's files — `CLAUDE.md`, `.claude/`,
`CHANGE-PLAN.md` — are not project records (ADR-0019). A commit that
touches them is scoped `agent` — `chore(agent)` to install or update,
`feat(agent)` for a new skill, `docs(agent)` for a change-plan's
lifecycle — and touches nothing else: no commit mixes those paths
with project changes.

This is "one logical change per commit" with a reason on top. The two
sides are two histories sharing one repo, and they stay separable —
a filtered log, a portfolio export that drops the arrangement and
keeps the work — only if no commit ever straddles them.

## Rules of thumb

- **Atomic commits.** One logical change per commit. "Fix X and update
  deps and rename file" is three commits wearing a trenchcoat — split it.
- **Subject alone must make sense** in `git log --oneline`. That listing
  is the index of the project's history; keep it readable.
- **Body answers why**, and anything the diff can't say: rejected
  alternatives, non-obvious constraints, "looks wrong but is right
  because…".
- **No `wip` / `fixes` / `asdf`** on the shared branch — squash locally
  first. (Fine on private branches; they're your devlog's scratch space.)
- When a change is too small for an ADR but the reasoning matters, the
  commit body IS the record — spend the two sentences.

---

## Delivery

`pushed`, with a `gate` that does not exist yet.

Pushed because the rule applies at one moment and is dead weight
otherwise; in Claude Code that means a skill, which is an ambient
trigger over a pulled body rather than true pushed (ADR-0015).

**What this constrains.** The entry file gets a path to this file,
never a summary of it (ADR-0014). And no text channel changes an
outcome, only the odds — until a commit hook exists the limit is
enforced nowhere, which is what 15 of the first 20 commits looked
like.
