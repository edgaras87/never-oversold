# Pass 1: Clean-up

<!-- The rules every pass follows are in 00-overview.md; this brief
     holds only what is pass 1's own. -->

Status: closed (2026-10-10)
Branch: `polish-1-clean-up`

## Goal

Every line in the repo is true now, says something once, and sits
in the record that CLAUDE.md's table gives it. The wording stays as it
is: improving it is pass 3's.

## In scope

Everything tracked, about 140 files and 23,500 lines, except what
the overview never touches:
- records: README, PLAN, TODO, CHANGELOG, ARCHITECTURE, devlog,
  ADRs, the slice records, the bootstrap requirements, both
  infrastructure manuals
- the project's own agent files: CLAUDE.md, `.claude/decisions.md`,
  `.claude/shapes/`, `.claude/rules/evidence-test.md`
- code: comments, Javadoc, test headers, messages in assertions

Not in this pass: the delivered skills and rules (pass 2).

## The checks

Each check is something a reader can verify, not a matter of taste.

1. **Stale.** A line says "now", "next", "current", "in progress"
   or "not yet", and the state it describes has changed. Example:
   TODO's *Now* still names Step 8, which is closed.
2. **Repeated.** The same fact is written in two places. It stays
   in the record that owns it, and the other place gets a link.
   When the two copies disagree, that is a finding of its own.
3. **Misplaced.** A line sits in the wrong record. Example: a
   backlog item written in the devlog, or a why written in README
   instead of an ADR.
4. **Missing.** A stranger needs something that no record says.
   Example: README's Test section leaves out the one-time
   testcontainers setup that only the operator manual has.
5. **Dead.** A link, path, class name, ADR number or guarantee name
   points at nothing.
6. **Unneeded.** A line adds nothing a reader can use: a leftover
   placeholder, a note that has been done, or a comment saying what
   the code already says.

## How it works

1. **Inventory.** Every file in scope, grouped into the areas
   below. Each file is read by one agent only, once.
2. **Find.** Read-only agents, one per area. Each returns three
   lists, and no edits:
   - **findings:** file, line, check number, what is wrong, the
     evidence, the proposed fix
   - **facts:** the facts its files state that another record may
     state too — dates, versions, counts, pins, ADR numbers and
     titles, guarantee and kill names, endpoints and status codes,
     limits — each with file and line
   - **ideas:** improvements noticed and not acted on, one line
     each, for pass 3
3. **Compare facts.** The fact lists are set side by side. The same
   fact stated differently in two areas is a finding (check 2), and
   goes to step 4 with the rest. One area alone cannot see it.
4. **Sort.** Merge the findings and remove duplicates. Each finding
   becomes one of:
   - **fix**: true, in scope, and the fix is clear
   - **for the reviewer**: needs a decision, touches the exports or
     history, or the right home is unclear
   - **dropped**: wrong on a second look, with a line saying why
5. **Fix.** One writer, in sequence, one commit per fix.
6. **Re-check.** One read-only reading of only the files that
   changed. New findings go back to step 4, once.
7. **Report,** with the ideas list handed on to pass 3.

## Areas

| Area | Files | About |
|---|---|---|
| A. Plan and shape | PLAN.md (its open parts read closely, its closed steps for dead references only), ARCHITECTURE.md, the devlog's headers | 1,300 lines |
| B. The system | `docs/system/` — intent, definition, registry, framing derivation. Proposals only | 1,900 |
| C1. Early slices | SL-1 and SL-2 records, bootstrap requirements | 1,500 |
| C2. Late slices | SL-3 and SL-4 records | 2,100 |
| D. Decisions and ground | `docs/adr/`, both infrastructure manuals, `infrastructure/`, `compose.yaml`, `.env.example` | 2,200 |
| E. Agent files and build | `.claude/CLAUDE.md`, `.claude/decisions.md` (its living parts closely, its entries for dead references only), `evidence-test.md`, the slice-record shape, `pom.xml`, the dotfiles | 1,700 |
| F. Code | `src/` — comments, Javadoc, test headers, assertion messages | 5,600 |

Not read: the devlog's entries and the decisions log's entries
beyond dead references, since they are history and cannot be
fixed here; the pilot's three files.

## Pilot

README, TODO, CHANGELOG: steps 1 to 4 on those three, then stop and
report the cost.

## Budget

Set from the pilot's cost (its report).
- Commits: 60
- Agents: 7 area agents and 1 re-check, 8 in all, at most 6 at
  once. The pilot's 3 do not count.

## Close

The plan for closing this pass, agreed 2026-10-10.

1. **Decide,** one decision at a time:
   - [x] History that has gone out of date (report items 5, 7, 13,
         14; the pilot's item 7). Decided: 5 gets a correcting entry
         in the decisions log; the rest stay as written.
   - [x] The system exports (items 8–11). Decided: one registry
         revision (the ordering's dates, the store-writer question
         answered, SL-2's row without a working step) and one
         definition revision (the clock's line); T2, the folds and
         the parked mechanisms stay, being rules, not states.
   - [x] CLAUDE.md and the decisions log's header (items 2, 3, 4, 6).
         Decided: the devlog row names both files; a row for
         `.claude/polish/` while it runs; the decisions row promises
         every delivery with its pin; the log's header drops the
         handbook as upstream and the done placeholder instruction.
   - [x] PLAN (item 1). Decided: the slices' placeholder skipped
         with its reason; Release's notes say the polish comes first.
   - [x] SL-3's missing evidence row (item 12). Decided: the row
         added, dated, with a revision line.
   - [x] TODO (the pilot's items 3–6). Decided: the kata item
         loses its handbook history; the versions item keeps only
         its open question, the rest moved to the decisions log; the
         two glossary items wait for pass 2; the two-places and
         moment items record what pass 1 met.
2. [x] **Apply** each decision on this branch as it is made.
3. [x] **Regroup:** keep the small commits on `polish-1-clean-up-detail`
   (local only), reset this branch onto main, and commit again by
   kind — the polish's agent files; the records; the code comments;
   the build; the decisions applied, agent and project apart.
4. [x] **Land:** the devlog's entry, the overview's status closed, and
   a fast-forward into main on the reviewer's word.
