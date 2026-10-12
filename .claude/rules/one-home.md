# One home per fact

**Governs:** where a fact is written in records, comments and
config. Where it lives, never how it reads.

## The rules

1. **Each fact has one home.** The records table in CLAUDE.md says
   which record owns which kind of fact.
2. **Elsewhere, link; don't retell.** "The size it is built for:
   definition, L1." Not the size again.
3. **A summary says it is one.** It names its owner, and nobody
   reads it in the owner's place. The registry's kill table does
   this already.
4. **A fact changes at its home.** The links don't change.
5. **When two copies are unavoidable,** one is the owner and the
   other follows it, and a check compares them. Example: the
   version lives in the pom; the CHANGELOG follows it.

## Not a second home

History: the devlog, closed ADRs, commit bodies. They record what
was true then. They are never updated, so they cannot drift.

## Revisions

- 2026-10-11: written on `try/writing-rule`, from the
  retrospective's D1. On trial for the rest of the retro.
- 2026-10-12: kept, at the retrospective's close. No longer on
  trial.
