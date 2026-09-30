# Commit plan: take the bundle's delivery @ 0000855

## Summary — the state after all commits

The bundle's delivery of 2026-09-30 is in force. Every copy under
`.claude/skills/`, `.claude/rules/` and `docs/concept/` equals the
bundle at `0000855`, and the decisions log records that pin with the
read-through `9869798`. The five files the bundle no longer ships are
gone: `artifact-kinds`, `convention-lifecycle`, `decide-first`,
`option-comparison`, and our rule `skills-changed-in-place.md`, whose
job `delivered-copies.md` now does.

Nothing live points at a removed name. `TODO.md` has a
`## To the deliverer` section holding only what is still open with
the bundle, and the lines the note answered are gone. The devlog
says what happened.

What this gives us: SL-3 and the three queued items run on the
current rules, not the ones the bundle has since replaced.

## Commits

**1. `chore(agent): update the bundle's copies @ 0000855`**
The take on its own: copy the staging over whole, delete the five
files the note names, and add one decisions entry with the pin and
the read-through. Reverting it puts back exactly what we held before.

**2. `docs(agent): drop the names the take removed`**
The sweep `commit-plan` now asks for, on the agent side.
`CLAUDE.md` still sends copy edits to "convention-lifecycle §3", and
`slice-record.md` still cites `artifact-kinds` as its kind. The same
commit trims `slice-record.md`'s opening on how it is used, which
the shapes rule now says a shape must not carry. History in
`decisions.md` stays as written.

**3. `docs: TODO answers the bundle's note`**
The project side. A `## To the deliverer` section at the foot of
`TODO.md`. The lines the note answered leave. The lines it holds
stay, moved under the new heading. Two lines are added: SL-2 did not
lean on the facility paragraph, and `decide-first` and
`option-comparison` were used here in the writing pass, although the
note calls the second one unused. "Now" stops repeating the items
that "Next" already lists.

**4. `docs: devlog carries the take @ 0000855`**
The session's entry and its Resume line.

## Decisions taken inside this plan

- **Five files deleted, although `delivered-copies.md` says a copy is
  never deleted here.** That rule is about us deleting on our own.
  Its rule 5 has the take remove what the note names, and the note
  names these five. The rule we hold today says the same thing.
- **`temp/` is kept until the close.** Commit 3 still works from the
  note. It is emptied after the close commit, and it is untracked,
  so no commit shows that.
- **The note's optional TODO suggestion is taken in commit 3, not in
  a commit of its own.** Commit 3 rewrites that part of the file
  anyway.
