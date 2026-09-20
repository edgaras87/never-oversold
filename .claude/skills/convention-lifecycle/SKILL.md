---
name: convention-lifecycle
description: How a project holds its conventions — what requires means, the registry, and updating a copy. Use when a convention is injected or updated, or when a copy under .claude/skills/ turns out wrong mid-step.
requires: commit-plan, agent-arrangement
---

# Convention Lifecycle

How a project holds the conventions it was born with, knows which
version it has, and takes a newer one. The decisions behind each
rule are listed at the end.

---

## 1. Requires-chains

`requires` in a convention's frontmatter names the conventions it
delegates rules to. Land a convention together with everything it
requires, and everything those require in turn.

## 2. The registry and the hash

A project's convention registry is its agent decisions log,
`.claude/decisions.md`, as entries:

- The birth entry names the conventions the project was born with
  and the deliverer's commit at copy time. Where the deliverer
  composed its delivery from another source, the entry names that
  hash too: one hash standing for two states would lie.
- Every later injection or update appends an entry with its own
  hash (§3, step 5).
- A convention's version in a project is the hash of the last entry
  that touched it. There are no per-convention version numbers.
- A copy edited since that entry (§3, step 4) is still at that
  version; the edits are the diff against it.
- "Is there newer, and what changed" is answered at the deliverer,
  whose kit is the master of every shipped file:
  `git diff <hash>..HEAD -- <its kit path>`. A project holding no
  checkout of its deliverer reads that diff from the delivery it is
  handed instead (§3, step 1).

## 3. Updating a copy

Injecting a convention into a live project, as a first copy or an
update, is done by the project's agent against the registry.

1. **Position.** Read the registry for the hash the project holds
   the convention at. `git diff <hash>..HEAD -- <its kit path>` at
   the deliverer, and the ADRs since, say what changed. A first
   injection has no position and takes the whole convention. The
   deliverer is a checkout on disk or the payload a handoff
   carries; the protocol is git either way.

2. **Evaluate.** Read the incoming frontmatter. `requires` names the
   chain that must be present and current (§1), including a line
   added since the pin; check the project's copy of a newly required
   convention in the same pass. How a convention lands is where its
   files sit in the delivered set — a skill at
   `.claude/skills/<name>/SKILL.md`, stubs and templates at the
   paths they hold in a project — and the deliverer's
   shipped-conventions table lists which files each ships through. A
   convention the project was born without lands as a first
   injection the same way, in the same pass.

3. **Vehicle.** A skill lands entirely agent-side, copy and registry
   entry, in one commit. Stubs land in project records too, and the
   two sides never share a commit. Two commits, one per side, need
   no commit plan; a landing that is a sequence runs under one
   (never-oversold, 2026-09-15).

4. **Copy, compare first.** The master is the deliverer's kit
   file, `<its kit path>/.claude/skills/<name>/SKILL.md`, at the
   same path the project holds it. Before overwriting, diff the
   project's current copy against the deliverer's file at the
   project's pinned hash. Identical: overwrite. Different: the
   project edited its copy, and each edit is re-applied, dropped or
   promoted by decision, with the registry entry naming what
   happened (the CbC repo's copy was nearly overwritten unread,
   2026-09-03).

   **A receipt branch, when the project holds one, is the compare.**
   A receipt holds every delivered file as it arrived, named by the
   deliverer's commit, `kit-<hash>`, and is never edited; the update
   cuts the next one as a commit on top of it. The upstream change
   is `git diff kit-<old> kit-<new>`. The local layer is `git diff
   kit-<old> -- .claude/skills/<name>` on the working branch, for
   each copy the receipt delivered, empty when nothing was edited.
   Cut a receipt only after the project's ignore lines exist
   (never-oversold, 2026-09-15: a wholesale add swept build output
   into one cut earlier).

   **A project may edit its copy between two pins**, provisionally
   until one such edit has gone through an update. Edit only from
   something that happened in the project, and only as a question
   or outcome any project would want; what this project alone needs
   goes into its own records. An edit has three records and none of
   them is in the file: one entry in the decisions log; one TODO
   line per edited copy, at the step's close, asking the deliverer
   to evaluate since the pin; and the diff of the copy against the
   delivery commit, which exists whether or not anyone writes
   anything. **No dated line in the copy's header comment** — a
   comment in an artifact carries how to use it and what a part is,
   never what changed (CBC ADR-0034, on never-oversold's argument
   at the first edit, 2026-09-20). A step that opens before the
   answer runs on the edited copy. At the update the deliverer's
   file at the new pin overwrites the copy whole; a declined edit is
   gone with it and is never edited back, and a need it served goes
   to the project's records, with the registry entry saying so. The
   deliverer reads the TODO line when it reads the project, at a
   handoff or at the retrospective; a project that needs a faster
   answer sends a handoff.

   **A convention delivered as stubs** has no copy to compare. Diff
   the kit's stubs at the two hashes, `git diff <hash>..HEAD --
   <its kit path>/<stub>`, for each stub the convention ships
   through; the list of stubs is the deliverer's
   shipped-conventions table, named in step 2. Carry the changed
   comment text into the project's record; diff a template file as
   a file. The
   project's content around the comments is the record, never a
   local edit.

   An empty diff still ends in step 5. A change absorbed through a
   reply without a registry entry leaves the pin naming a hash the
   records have moved past, and step 2 reads that as current (the
   CbC repo, 2026-09-08: artifact-kinds one line behind).

5. **Register, one commit.** Land the copy and the registry entry
   together: date, convention, injected or updated `@ <hash>`, why,
   what was rejected. That hash is now the convention's version in
   the project (§2). The registry is the project's only list of its
   conventions; the entry file carries none. Splitting into more
   commits is allowed, never required; every piece is agent-scoped.

6. **Nothing edits the handbook.** Friction met here goes up as a
   promotion-queue entry or a friction list, never as a side effect
   of the landing.

---

## Decisions

- HANDBOOK ADR-0017 — requires-chains
- HANDBOOK ADR-0019 — the agent/project line the landing's commits
  follow
- HANDBOOK ADR-0020 — the decisions log
- HANDBOOK ADR-0022 — the registry is entries; the version is the
  hash
- HANDBOOK ADR-0030 — §3, written from the first lived injection
- HANDBOOK ADR-0034 — the registry is the only list of conventions
- HANDBOOK ADR-0038 — a copy edited between two pins; the receipt
  compare; a two-commit landing without a commit plan
- HANDBOOK ADR-0040 — a convention is a manual and its artifacts;
  this skill is the receiver's half of the kit's protocol
