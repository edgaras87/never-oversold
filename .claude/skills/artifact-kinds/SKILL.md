---
name: artifact-kinds
description: Vocabulary of document kinds — convention, model, guide, playbook. Use when writing or requesting a document whose kind must be named.
delivery: pushed
---

# Artifact Kinds Convention

A shared vocabulary for the *kinds* of artifacts we create and request —
so that "draft a guide", "this needs a playbook", or "is this a
convention or a model?" resolves the same way for everyone (humans and
AI agents alike). Kinds are defined loosely, by position on a few axes
and by exemplar — never by strict membership criteria.

<!-- This convention is itself a metamodel: a convention about what
     conventions (and their sibling kinds) are. Specific contexts may
     specialize a kind — e.g. the handbook specializes "convention" as
     "a directory under conventions/<name>/ with a CONVENTION.md". -->

---

## 1. The axes (the compass, not a cage)

Every kind is located by its answers to two questions; a third helps
when shaping a document. When a word is in doubt, ask the questions —
the answers point at the word.

1. **Force** — what does it do to the reader?
   - *describes* — says what is; claims nothing about what you must do.
   - *advises* — recommends; deviation needs no justification.
   - *binds* — agreed practice; deviation needs a reason.
   - *executes* — walks you through doing; steps, not statements.

2. **Reuse** — is it a *template* (copied per instance) or the
   *instance itself* (one per project/case, carries live state)?

3. **Reader mode** (optional, from Diátaxis) — is it *studied* once to
   understand, or *consulted* repeatedly mid-work to look things up?

These axes are stolen deliberately: force from the governance
hierarchy (policy → standard → guideline → procedure, RFC 2119's
MUST/SHOULD/MAY), reader mode from Diátaxis. We keep our own kind
names and use the frameworks only as definition machinery (ADR-0009).

## 2. The kinds

Each entry: function, coordinates, the distinguishing question, and an
exemplar from this repo where one exists. Exemplars are prototypes —
"a playbook is like *this*" — not boundaries.

### concept

An idea, pre-artifact. Concepts live *inside* documents (usually as
explanation); a concept is never a document kind itself.
Coordinates: describes; no home of its own.
Question: *is it a thing you could hand someone as a file?* No → concept.
Exemplar: "the distillation pipeline"
(project-recording/CONVENTION.md §11).

### model

A structured description of how things relate — components, records,
flows. Says what *is*, demands nothing.
Coordinates: describes; studied.
Question: *could you disagree with it and violate nothing?* Yes → model.
Exemplar: the record-system diagram
(project-recording/CONVENTION.md §1).

### convention

A normative agreement about how we do things. Deviation is allowed but
needs a reason.
Coordinates: binds; consulted.
Question: *if someone ignores it, do they owe an explanation?*
Yes → convention.
Exemplar: conventions/commit-messages/CONVENTION.md.

### guide

An advisory how-to: teaches a good path without binding to it.
Coordinates: advises; studied or consulted.
Question: *if someone ignores it, is that fine?* Yes → guide.
Exemplar: none in this repo yet.

### playbook

A reusable script for a *type* of undertaking: step sequence, gates,
accumulated warnings. Copied per instance, never executed in place.
Coordinates: executes; template.
Question: *do you copy it to use it?* Yes → playbook.
Exemplar: the handbook's starter/playbooks/default.md — or the one
a concept repo keeps beside its own manual.

### plan

The live instance of an undertaking: current statuses, gates being
closed, notes on divergence. One per project.
Coordinates: executes; instance (carries live state).
Question: *does it lie if not kept current?* Yes → plan.
Exemplar: PLAN.md (this repo's own).

### reference doc

The document *shape* optimized for looking things up: complete on its
subject, structured for random access, not read cover to cover. A
reference doc is a shape, not a force — a convention's CONVENTION.md
*is* a reference doc carrying binding content; a model can be one too.
Coordinates: any force; consulted.
Question: *do readers jump to a section rather than read it through?*
Yes → reference doc.
Exemplar: any CONVENTION.md in this repo.

### template / stub

A document whose content is holes: structure provided, specifics
awaited. The degenerate case of "reusable".
Coordinates: force of whatever it will become; template.
Question: *is it full of `<placeholders>`?* Yes → template.
Exemplar: starter/kit/ stubs.

### specification

A precise, testable description of what something *must be* — complete
enough that conformance can be checked.
Coordinates: binds; consulted.
Question: *could a test verify conformance to it?* Yes → specification.
Exemplar: none in this repo yet (external: Conventional Commits spec,
Keep a Changelog).

### checklist

A procedure reduced to its verifications: no narrative, just the items
that must be true.
Coordinates: executes; consulted.
Question: *is it only boxes to tick?* Yes → checklist.
Exemplar: the gate lists inside PLAN.md steps.

## 3. Rules

- **Definitions are prototypes, not membership tests.** When an
  artifact doesn't fit cleanly, that's a finding to note, not a
  violation to fix. Hybrids are normal — a CONVENTION.md here is
  reference doc (shape) + model (§1) + convention (rules) at once;
  name it by its dominant force.
- **Coordinates live here only.** Actual artifacts carry just the kind
  word (in a request, a filename, a table row) — never a metadata
  block of axis values. This file is the lookup table behind the word,
  the way the commit-type table stands behind `feat:`.
- **New kind = new entry, placed by the axes.** If something's axis
  answers land where no existing word sits, add an entry with the four
  fields above and a changelog line. Prefer reusing an existing word
  with a specialization note over coining a near-synonym.
- **Contexts may specialize, not contradict.** A specific context
  (this handbook, a project type) may narrow a kind ("here, a
  convention is a directory with a CONVENTION.md") but not move it on
  the axes.
- **Scope stays minimal.** A kind earns an entry only if its absence
  has caused, or plausibly will cause, someone to reach for the wrong
  word. The long tail (runbook, RFC, catalog, glossary, …) gets
  entries when first actually needed.

## 4. Origin

Distilled from: the governance document hierarchy
(policy/standard/guideline/procedure), RFC 2119, Diátaxis
(Procida — tutorial/how-to/reference/explanation, kinds-by-function),
DDD's ubiquitous language (formalize the words actually spoken, don't
import a foreign register), and prototype theory for why loose
definitions survive edge cases. See ADR-0009 for why the axes were
adopted and the frameworks' own word lists were not.

---

## Delivery

`pushed` — it fires when a document is written or requested and its
kind has to be named.

A rarer trigger than the others, and one that often passes unnoticed:
the kind is usually settled in a single word early in a conversation,
before anyone thinks to check it.

**What this constrains.** Every kind is anchored by an `Exemplar:`
pointing at another document, and in a project that vendored this
convention alone those point at nothing. They are illustration, not
dependency (ADR-0017), so each definition has to stand without its
exemplar. A kind that only makes sense with the example in hand is
under-defined.
