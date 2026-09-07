---
name: cbc-framing
description: Frame a new backend system with correctness-driven design - turn a raw project idea into one falsifiable promise, a layered system definition (L1-L5), and a registry of provable slices, BEFORE any code or tech choices. Use this whenever the user wants to start, design, or plan a backend/service where being wrong is expensive (orders, payments, inventory, bookings, anything owning facts or money), mentions framing, a promise, invariants, idempotency, "what must never happen", CbC, or correctness-driven design - even if they just say "I have an idea for a service" or "help me design a backend". Do NOT use for throwaway prototypes, experiments, or low-stakes tools, and do not use for implementing code (that is cbc-slice, which runs only after framing AND bootstrap).
---

<!-- Derives from concept v1 of correctness-by-construction
     (ADR-0003). Provenance — archive/cbc/system-design-method
     birth-materials/.claude/skills/cbc-framing/SKILL.md
     @ fe0075d (imported 2026-08-28, PLAN Step 3). Changes on
     import: none — verbatim below this header.
     Harvested 2026-08-29: the registry export's lived format from
     checkout-system's nine-slice run, read read-only (ADR-0007) —
     outcomes stated in the export section, copy-and-fill master in
     templates/registry.md (ADR-0008).
     Harvested 2026-08-29, same run: the exports are living records
     — post-close changes only as logged revision entries (the run
     updated its intent at a breach's close and revised its system
     definition by logged return trips from downstream stages).
     Harvested 2026-08-29, same run: step 2's gate names the
     recorded probe log (details in the workflow's step 2).
     Harvested 2026-08-29, same run: the mode section names the
     delegated-verdict mode — human default unchanged, delegation
     only by a decision recorded in the run's own records.
     Re-derived 2026-08-29: the record shape decided — one
     derivation doc as the framing's working record, composed at
     close into the three exports under a residue filter, committed
     in derivation order; harvested from the safe-reservations
     framing node read read-only (ADR-0007), its own vocabulary
     never adopted; the shape's one-doc form is this repo's
     decision, both lived runs its evidence.
     Harvested 2026-08-29, same node: step 0's gate names the
     audience and done-means (details in the workflow's step 0).
     Harvested 2026-08-29, same node: the registry outcomes gain
     riders, flags, headings-never-boundaries, the written zero.
     Re-derived 2026-08-29: the run-repo doc layout — the three
     exports live under docs/system/ as intent.md, definition.md,
     registry.md; the derivation record nests beside them at
     docs/system/framing/. Root placement was an auto run's
     unverdicted default, never a decision; the directory makes the
     truth set one nameable path and dissolves the prefix-carried
     names (the dotted exports, the framing- appendix prefix).
     Harvested 2026-08-30: the README projection law — the export
     section's projected-surface block — from the safe-reservations
     node's projection model and guide, read read-only (ADR-0007),
     their vocabulary never adopted; the lived core only, the
     deeper lifecycle left unharvested until a run lives it. -->

# CbC framing — one promise worked into a slice surface

Turn a standing project idea into three artifacts: **intent** (the promise),
**system definition** (L1–L5), **slice registry** (the work, cut and ready).
All paper — no code, no tech choices; the run's stub-born repo is the
paper's home. A framing may honestly end in "no project"; that is a
valid exit.

The full procedure is `references/cbc-framing-workflow.md` — read it before
step 0 and keep it open; this file is the operating rules, not a substitute.
`references/worked-example.md` shows a complete framing of a tiny order
service — consult it whenever unsure what a step's output looks like.
`references/the-whole-system-in-plain.md` is the one-page orientation if the
user (or you) needs the whole method reloaded first.

## The mode — non-negotiable

Framing is **joint work**. You open each question and derive visibly; the
human works it with you. The hard rule, from the method itself:

- **You never close a step, never call saturation, never resolve a fork
  alone.** Every exit is the human's verdict. Present your derivation, then
  ask for the call.
- A first-time walker may ask for **demonstration mode** — derive with
  reasoning fully visible, teaching the moves. Even then, every verdict
  stays human.
- **Every "outside" is written.** Rejected candidate promises, refused
  responsibilities, out-of-scope facts — recorded, never silently dropped.
  Silence is the only forbidden channel.

**Delegated-verdict mode.** A run may hand these verdicts to the
agent — but only through an explicit decision recorded in the run's
own records (its `.claude/decisions.md`), naming what is delegated,
why, and the cost accepted: the written derivation trail becomes
the only review, so its legibility is part of the product. Every
gate is still presented, decided, and recorded where the record
scheme puts sign-offs — delegation changes who calls the verdict,
never whether it is written. Absent that recorded decision, the
default above stands: every verdict is the human's.

## The derivation order (not the presentation order)

```
step 0  choose the promise      (intent)  one claim, falsifiable, worth proving
step 1  what must be ours?      (L2)      hostage test; refusals in writing
step 2  name the enemies        (L1)      census: vanishes / duplicates / lies
step 3  run the collisions      (L4)      facts × possessions → kills
step 4  sort into owners        (L3)      only if collisions force divisions
step 5  check between owners    (L5)      only if owners exist; else empty-with-reasons
step 6  cut the slice surface             slices vs folds; registry
```

Run them in this order — each step consumes exactly the previous step's
output. Upward corrections only as logged return trips (a census finding
that reveals a missed possession → logged L2 revision), never scheduled
polish passes.

## Per-step gates (check before asking the human to close a step)

- **Step 0:** exactly one promise, one sentence, falsifiable — the breaking
  scenario can be described concretely. The audience named as who the
  claim is sold to (system consumers belong in the census); what done
  demonstrably means stated. A second real purpose means the
  intent is overloaded: force the choice. Bank rejected candidates.
- **Step 1:** every possession earned by the hostage test ("if someone else
  owned this, could they break the promise?"); every refusal written with
  the mirror test. Sketch-enemies are fine here — they are debt step 2 pays.
- **Step 2:** facts only, concrete enough to attack with ("responses get
  lost", never "networks are unreliable"), consequence-first. **A census is
  not an assumption inventory** — list what can hurt, never what you trust.
  Exit only by saturation: two consecutive fresh probes finding nothing
  new — the probe log recorded in L1.
- **Step 3:** each kill states **what dies, never how it's saved** — park
  any mechanism the instant it surfaces. Land kills invariant-shaped with
  adversity named, consumable by the slice workflow with zero translation.
  Enemy-less contract-shaped concerns → fold-candidates, not slices.
- **Step 4:** a division is real only with separate state AND separate
  decision authority. One area is a *confirmed* answer, not a gap. Name
  seams that fail the bar; refuse to draw them.
- **Step 5:** empty is fine — but empty **with its reasons**, each traced to
  a prior decision.
- **Step 6:** two kills are two slices only if their evidence must create
  *different adversity*. Folds ride into the spec of the slice consuming
  them. The registry carries the fold-reconciliation line (concerns ↔
  slices + folds — nothing silently dropped) and a chosen-next slice.
  Ordering is an expectation, re-decided at each slice close.

## The record — one derivation doc

The framing's working record is **one file,
`docs/system/framing/derivation.md`** — nested beside the truth it
earns, kept as the archive forever, never deleted. Three rules give
it multi-doc's virtues without the file sprawl:

1. **One section per step**, each split in two: *what this step
   earned* (clean statements, on top — export composes from these)
   and *how it ran* (reasoning, probes, dead ends, below). Verdicts
   are written inline, where they happen.
2. **A section freezes at its verdict.** Append-only after that;
   revisions touch it only as logged return trips.
3. **The escape valve:** a genuinely bulky mechanical sweep may
   live as a referenced appendix file beside it in the same
   directory, plain-named (`collision-grid.md`, say), carrying
   sweep output only, never decisions. The doc stays the single
   record.

Where each record kind lives: framing verdicts — inline here; a
delegation arrangement — the run's `.claude/decisions.md` (agent
setup, not project truth); the framing's adoption — one ADR in the
run's `docs/adr/` at close.

## Export and handoff

At close, **compose** the three artifacts from the derivation doc's
earned blocks, under the **residue filter**: an export never
references the derivation doc's machinery — conclusions travel
re-grounded in the exports themselves, which must stand alone.
Default export practice: **commit in derivation order** — the
intent; the system definition growing L2 → L1 → L4 → L3 → L5, one
lived state per commit; the registry; the adoption record; the
README last (the surface derives from committed internals) — so
the run repo's own history tells the derivation story. Skipping the
sequence is off-default: log why.

The three artifacts, under `docs/system/` — internal truth as one
nameable path, the directory carrying the context the old dotted
prefixes carried:

- `intent.md` — the promise + banked rejections
- `definition.md` — L1–L5, filled or empty-with-reasons
- `registry.md` — slices + folds + reconciliation line + chosen-next

**The registry's outcomes** (what the file must carry, whatever its
shape): every slice entry states its invariant, the adversity its
evidence must *create*, its owning area, the kills it covers, and
what it presumes already stands; statuses are trustworthy
(`open · chosen-next · in-progress · closed (date, evidence)`);
riders and evidence-shape flags ride the entry they guard; any
grouping is headings for the reader, never a boundary; the
reconciliation line accounts for every kill and states even its
absences (the written zero); exactly one slice is chosen-next
while open work remains. The default shape is the
copy-and-fill master `templates/registry.md` beside this
skill's references (ADR-0008). Declining it is **off-template**:
derive your own shape from the outcomes above and record the
deviation in the run's log.

**The exports are living records, not write-once.** The registry
already lives (statuses change at every slice close); the other two
stay true the same way: when later work changes what an export
claims — a named breach closes (intent's staging honesty), a
downstream stage surfaces facts the definition must carry — the
export is updated **only through a logged revision entry** (dated:
what changed, why, what triggered it), the framing's own
return-trip discipline extended past close. Silent edits and stale
claims are the same forbidden channel.

**The README — the projected surface.** The run repo's README is
**derived from the three exports, never authored on its own**:
every claim on it has a master in `docs/system/`. A fact the
reader needs that lives only in the derivation record marks a gap
in the exports — fix the export first, then re-derive; a surface
patched independently is a second master, and two masters diverge.
What each piece derives from:

| From | Becomes | How much |
|---|---|---|
| the intent | why this exists | one paragraph |
| the definition's owned-and-refused | what this is | one paragraph, no parts named |
| the registry | the planned invariants | one line per invariant |
| the method | a pointer note | two lines, never a restatement |
| the repo's observable state | an honest status line | one line |

Depth is a routing link into the exports, never more prose. At
framing close the thin README is the whole public surface — with
the masters one click away, a public restatement fails: the reader
clicks into the master instead. A run whose conditions differ may
rebut this, off-default, logged. The README refreshes the way it
was born: event-driven at milestones (a slice close moves the
status line), re-derived from the masters, never patched in place.
The residue filter applies with full force: the surface tells what
the system is and guarantees, never how the work ran.

Then say this explicitly to the user: **the handoff is to bootstrap, not to
slicing.** Between framing and the first slice sits real project work —
repo skeleton, store, adversity harness — defined by the readiness
checklist (`system-readiness.md`, bundled with the cbc-slice skill). Slicing
begins only when the human signs off readiness; the cbc-slice skill will
check.
