---
name: artifact-kinds
description: Vocabulary of document kinds — convention, model, guide, playbook. Use when writing or requesting a document whose kind must be named.
---

# Artifact Kinds

A shared vocabulary for the kinds of documents we create and
request, so that "draft a guide" or "is this a convention or a
model?" resolves the same way for everyone. Kinds are located by
axes and anchored by exemplars, never by membership tests.

## 1. The axes

1. **Force** — what it does to the reader: *describes* (says what
   is), *advises* (recommends; deviation needs no reason), *binds*
   (agreed practice; deviation needs a reason), *executes* (walks
   you through doing).
2. **Reuse** — a *template*, copied per instance, or the *instance
   itself*, one per project or case, carrying live state.
3. **Reader mode** (optional) — *studied* once to understand, or
   *consulted* repeatedly mid-work.

When a word is in doubt, ask the questions; the answers point at
the word.

## 2. The kinds

**concept** — an idea, pre-artifact; lives inside documents, never
a document kind itself. Describes; no home of its own. *Is it a
thing you could hand someone as a file?* No → concept. Exemplar:
"the distillation pipeline" in project-recording.

**model** — a structured description of how things relate; says
what is, demands nothing. Describes; studied. *Could you disagree
with it and violate nothing?* Yes → model. Exemplar: the
record-system diagram in project-recording.

**convention** — a normative agreement about how we do things;
deviation is allowed but needs a reason. Binds; consulted. *If
someone ignores it, do they owe an explanation?* Yes → convention.
Exemplar: the commit-messages convention, wherever this repo holds
it.

**guide** — an advisory how-to; teaches a good path without binding
to it. Advises; studied or consulted. *If someone ignores it, is
that fine?* Yes → guide. Exemplar: none named yet.

**playbook** — a reusable script for a type of undertaking: step
sequence, gates, accumulated warnings; copied per instance, never
executed in place. Executes; template. *Do you copy it to use it?*
Yes → playbook. Exemplar: the playbook this repo's PLAN names in
its Steps-from line.

**plan** — the live instance of an undertaking: statuses, gates
being closed, notes on divergence; one per project. Executes;
instance. *Does it lie if not kept current?* Yes → plan. Exemplar:
this repo's PLAN.md.

**reference doc** — the shape optimised for looking things up:
complete on its subject, structured for random access. A shape,
not a force. Any force; consulted. *Do readers jump to a section
rather than read it through?* Yes → reference doc. Exemplar: any
skill copy under `.claude/skills/`.

**template / stub** — a document whose content is holes: structure
provided, specifics awaited. Force of whatever it will become;
template. *Is it full of `<placeholders>`?* Yes → template.
Exemplar: the stubs this repo's records were born from.

**shape** — what a kind of output looks like here: its form, never
its content. Force is positional, which no other kind's is: it
binds where the project puts it in front of the writer, and
describes where it does not. Instance, one per kind of output;
consulted. *Does it say how an output should look, and leave what
it says to someone else?* Yes → shape. Exemplar: never-oversold's
`.claude/shapes/slice-record.md`, the form of a slice record's
guarantees and owners.

**specification** — a precise, testable description of what
something must be. Binds; consulted. *Could a test verify
conformance to it?* Yes → specification. Exemplar: none named yet
(external: Conventional Commits, Keep a Changelog).

**checklist** — a procedure reduced to its verifications. Executes;
consulted. *Is it only boxes to tick?* Yes → checklist. Exemplar:
the gate lists inside PLAN.md steps.

## 3. Rules

- **Definitions are prototypes, not membership tests.** An artifact
  that does not fit cleanly is a finding to note, not a violation.
  Hybrids are normal; name one by its dominant force.
- **Coordinates live here only.** An artifact carries just the kind
  word, in a request, a filename, a table row, never a block of
  axis values.
- **New kind = new entry, placed by the axes**, with a changelog
  line. Prefer an existing word with a specialisation note over a
  near-synonym.
- **Contexts may specialise, not contradict.** A context may narrow
  a kind but not move it on the axes.
- **Scope stays minimal.** A kind earns an entry only when its
  absence has caused, or plausibly will cause, someone to reach for
  the wrong word.

---

## Decisions

- HANDBOOK ADR-0009 — own words, stolen axes: the kinds are located
  by force and reuse, and the frameworks' word lists are not
  imported
