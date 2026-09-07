<!-- Provenance — archive/cbc/system-design-method
     birth-materials/concept/01-layered-system.md @ fe0075d (imported
     2026-08-28, PLAN Step 2). Changes on import: none — verbatim
     below this header. The authoritative copy lives in
     correctness-by-construction's concept/ — a run's copy
     (docs/concept/) is pinned, changed only by copying anew
     (harvest, never edits); the archive copy is a historical
     snapshot. -->

# The layered system

The world model half of the discipline: a containment description of
reality, five nested boxes. The layering is not administrative — each layer
manufactures the precondition the next one needs to be a well-posed
question at all.

```
L1  Environment            the world the system lives in — outside its control
L2  System of Interest     the thing being built: responsibility, boundary, authority
L3  Responsibility Areas   the system's internal divisions by ownership
L4  Local Problems         bounded problems inside one area: invariants under adversity
L5  Interaction            what emerges when areas interact; correctness across them

containment:  L1 surrounds L2 · L2 contains L3 · each L3 area contains its L4 problems
              L5 is not "below" L4 — it lives between L3 areas
```

## L1 — The environment

Everything outside the system's control: users, networks, other systems,
the system's own process crashing. It is described as *hostile*: things
vanish, duplicate, and lie. The description must be made of facts you can
attack with ("responses get lost"), not vibes ("networks are unreliable").

The environment description also records what is deliberately *not*
defended against — assumptions explicitly accepted as trust. An assumption
written down is a decision; an assumption left implicit is a surprise
waiting.

Why the layer exists: without a described environment, guarantees have no
adversary and collapse into slogans. "Never lose an order" means nothing
until you can say what tries to lose it.

## L2 — The system of interest

The thing being built, defined by three properties:

- **Authority**: which facts this system is the single authority on.
- **Promise**: one falsifiable commitment about those facts, made to a
  named audience. Falsifiable means the breaking scenario can be imagined
  concretely — that property is what separates a promise from a mission
  statement.
- **Boundary**: everything crossing from outside gets checked at the door.

Why the layer exists: without a boundary, you defend against everything —
an unbounded job that ends in the theory swamp. The boundary gives defense
a finite task; the promise gives the whole design something to be derived
*from*.

## L3 — Responsibility areas

The interior, partitioned by one rule: **every fact has exactly one
owner.** Often the honest answer is a single area — that is a confirmed
answer, not a gap. Keeping the partition small is a design decision, not a
limitation.

Why the layer exists: an invariant is only enforceable if exactly one place
answers for it. Without the ownership partition, every invariant is
everyone's job — which means no one's.

## L4 — Local problems

Inside each area: bounded problems small enough to be *closed* — an
invariant that must hold, the specific adversity that attacks it, and a
structure that ends the question. Small on purpose: the layers above made
them small.

Why the layer exists: without problems this size, there is nothing concrete
to construct correctly. Local correctness becomes checkable only when the
problem fits in one head.

## L5 — Interaction

The correctness no single area can own: what emerges *between* the parts —
ordering across areas, agreement between owners, the whole being right when
every part is. Empty by construction when L3 is a single area.

Why the layer exists: skipping it is the signature failure of teams who did
L4 well — correct parts, wrong whole. Naming the between-space gives
emergent wrongness somewhere to be owned. It is where most real incidents
live.

## Two readings to keep apart

**Containment is not work order.** The boxes nest L1 → L5, and that is the
right order for *presenting* a system. It is not the order the design work
runs in — deriving the layers has its own order, starting from the promise.
That order belongs to framing and is described there.

**Layers are not architecture.** The layered system says what must be
answerable and by whom. It does not say what must be built: the same layer
description can honestly become a monolith or a set of services.
