# cbc-pure-run-3

A backend service to be built by correctness-by-construction —
the design derived from one falsifiable promise, what must never
happen first, features last. Until the briefing brings the
problem, this repo is method and records, ready to start:
nothing to build, no tests, no runtime.

## Correctness by construction

The method is in `docs/concept/` — read `00-cbc.md` first; the
other chapters deepen it. It is not restated here: the order of
questions (promise → guarantees → structure → features → code,
never backwards) lives there, and the method's reading of each
record lives in that record's own comments.

Until the framing artifacts exist (cbc-framing creates them, under
`docs/system/`), the project is pre-framing: the only method work
is running cbc-framing jointly with the human — never invent the
artifacts to fill the gap.

`docs/concept/` and the method skills under `.claude/skills/` are
pinned copies: never edited in place — a change is a new copy
from the source, logged in `.claude/decisions.md`.

## Records

<!-- When to open which record. The record teaches the rest, but only
     once opened, and nothing else says when. Three things per row —
     the moment, what it holds, the path — never the rule itself: what
     an ADR contains is inside the ADR. Adding a record means adding
     its row. -->

| When | What's in it | Record |
|---|---|---|
| Starting work, or closing a step's gate | Current state, next steps, gates | PLAN.md |
| A decision taken, options rejected | Decisions and why | docs/adr/ |
| Noticed something, not doing it now | Backlog | TODO.md |
| Session ending, or a dead end hit | Work history, dead ends | devlog/devlog.md |
| Shipped something users can see | What changed, for users | CHANGELOG.md |
| The system's shape changed | Shape of the system | ARCHITECTURE.md |
| Something became true the outside should see | The front door: what this is, how to use it | README.md |
| Agent setup changed, or a convention arrives | Decision, why, rejected options; the conventions held, with versions | .claude/decisions.md |
| Work needs more than one commit | In-flight change set | CHANGE-PLAN.md (when present) |

<!-- This file is loaded in full on every task, relevant or not, so
     every line below passes three tests or leaves (agent-arrangement):
     1. True of this project and nowhere else — else it is a
        convention, stated once, there.
     2. No moment — else it goes where the moment is: the record's
        stub, README, a project skill.
     3. Nothing else would deliver it — a stance, or a fact whose
        failure is not noticing it, like a generated directory nobody
        may edit by hand.
     Longer than a screen means a line is failing. Shrinking it is
     maintenance, not tidying. -->

## Local rules

- Nothing before the briefing names the problem.
