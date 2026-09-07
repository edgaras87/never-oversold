# cbc-pure-run-3

A backend built by [correctness-by-construction](docs/concept/):
a system whose design is derived from one falsifiable promise —
asking what must never happen before what it should do — and
whose every invariant is closed by a test that creates its
adversity. The problem is not yet chosen. It arrives with the
briefing that opens framing, and this paragraph is then
re-derived from the framed intent ([PLAN.md](PLAN.md), Step 1).
Until then the repository holds the method, the plan, and the
records — no code.

The method: [docs/concept/](docs/concept/), a pinned copy of the
concept in five chapters; start with [00-cbc.md](docs/concept/00-cbc.md).

## Project records

| Record | Where | What it answers |
|---|---|---|
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
     truth. -->
