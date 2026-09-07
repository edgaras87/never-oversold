<!-- Provenance — archive/cbc/system-design-method
     birth-materials/concept/00-cbc.md @ fe0075d (imported
     2026-08-28, PLAN Step 2). Changes on import: none — verbatim
     below this header. The authoritative copy lives in
     correctness-by-construction's concept/ — a run's copy
     (docs/concept/) is pinned, changed only by copying anew
     (harvest, never edits); the archive copy is a historical
     snapshot. -->

# Correctness-by-construction

*The main document. Read this first; the other chapters deepen it —
the layered system, guarantees and walls, framing, and the slice.*

## In one sentence

A system is what it promises about a hostile world; design is the
derivation, in dependency order, of the structures that make breaking that
promise impossible — and of nothing else.

## The inversion

Most software is designed by asking *"what should it do?"* and listing
features. Correctness, if it appears at all, is bolted on afterwards as a
quality attribute — tests, monitoring, bug fixes.

This discipline flips the first question. It asks ***"what must never
happen?"*** first. Money counted twice. An order created twice. A stale
answer shown as fresh. From those never-events, everything else is derived:

- **Guarantees** are the never-events, each stated against a named source
  of adversity.
- **Structure** is derived from the guarantees: each never-event gets one
  structural owner that makes it *impossible*, not merely detected.
- **Features** come last, as the minimal surface needed to keep the
  promises real.

The direction of derivation is the whole point:

    world → promise → guarantees → structure → features → code

never the reverse. A feature list is an *output* of the design, not its
input.

One corollary follows immediately: the surface stays deliberately
**feature-thin**. Every feature added is new attack surface against the
system's own guarantees. Guarantee depth is the visible product; staying
small is discipline, not taste.

## What kind of thing this is

It is a *design discipline* — not a process framework, and not a technique.
The distinction decides what counts as following it:

- A **technique** ("use database constraints") can be applied locally
  without changing how you think. The discipline cannot: its unit is the
  *ordering of questions*, and it is violated by asking them out of order
  even when every individual answer is good.
- A **process framework** prescribes activities, roles, artifacts, cadence.
  The discipline prescribes none of these. Any form that records the
  answers in dependency order satisfies it; any that doesn't, doesn't.

Its closest kin in kind is test-first development: a small inversion of
question order that, held consistently, reorganizes everything downstream
of it.

## The two components and their joint

The discipline has exactly two components, and it lives in their joint:

**The world model — the layered system.** A containment description of
reality: a hostile environment, inside it a system making one falsifiable
promise, inside that a partition by ownership, inside each part problems
small enough to close, and between the parts the correctness no single part
can own. Described fully in its own chapter.

**The way of thinking — the inversion.** At every layer, the first question
is negative: what must never happen here, given what the surrounding layer
throws at it. Positive behavior is whatever minimal surface makes the
negative commitments hold.

The joint: the inversion without the layers has nowhere to stand — a
never-event is only meaningful relative to a described world; asked in a
vacuum it produces either slogans or endless theorizing. The layers without
the inversion are just an architecture documentation format. The thing
being defined here is the pair.

## The shape of the work

The work has two movements, each with its own chapter:

- **Framing** turns an idea into a defined system on paper: one promise,
  a described adversary, settled ownership, and the work cut into slices.
  No code, no technology choices.
- **The slice** is the unit of construction: one invariant taken from
  specification to a structural wall, proven by a test that *creates* the
  attack and shows the invariant surviving.

## Where it comes from

None of the parts are new. Bounded contexts (domain-driven design) built
the same ownership walls against language drift; SLOs (site reliability
engineering) made promises explicit and falsifiable; threat modeling
already thinks in never-events first; design-before-code culture found that
the cheapest place to catch a flaw is a document; the academic
correctness-by-construction lineage owns "errors prevented by structure,
not found by search."

Each of those practices covers a region and stops — threat modeling
produces adversity but no boundary, bounded contexts produce boundaries but
no guarantees, SLOs state guarantees but no enforcement, formal methods
enforce but don't say where specifications come from. What is claimed as
original here is the **composition**: these are not five practices but five
phases of one derivation, with a single organizing question — *what must
never happen, and what structure makes it impossible* — asked at each phase
in that phase's vocabulary.

## What it is not

- **Not universal.** It prices correctness above speed of exploration. For
  throwaway code, experiments, and low-stakes tools, iterate-and-fix is
  cheaper and not wrong. The method earns its cost when breaking the
  promise is expensive or irreversible — money, safety, trust — or when the
  system will live long. A method that does not know its limits is a slogan.
- **Not formal methods.** It keeps the stance of the
  correctness-by-construction lineage while dropping universal proof. Rigor
  is *spent*, not applied: the budget goes to the few invariants whose
  violation is expensive or irreversible.
- **Not architecture.** Two teams following it could produce a monolith and
  a set of services from the same design. It constrains what must be
  *answerable*, not what must be *built*.

## How it rots

The known failure modes, named so they can be caught:

- **Guarantee erosion by feature.** New features ship without re-checking
  the guarantees they might now violate.
- **The escape hatch.** An admin path or raw script bypasses the single
  enforced code path; the guarantee quietly becomes fiction.
- **Testing theater.** Guarantees drift out of structure and back into test
  suites. Tests sample; structure forbids.
- **The theory swamp.** Attempting deep rigor before the layers have shrunk
  the problem — over-theorizing a world that hasn't been bounded yet.
