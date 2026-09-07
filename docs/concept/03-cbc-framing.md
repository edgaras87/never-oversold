<!-- Provenance — archive/cbc/system-design-method
     birth-materials/concept/03-cbc-framing.md @ fe0075d (imported
     2026-08-28, PLAN Step 2). Changes on import: none — verbatim
     below this header, until: 2026-09-06, the technology-timing
     sentence corrected against the lived runs ("with the first
     slice" → "after framing, answerable to the registry") — the
     runs stand up ground and skeleton before the first slice,
     and that is where technology lands, traced to registry
     needs. The authoritative copy lives in
     correctness-by-construction's concept/ — a run's copy
     (docs/concept/) is pinned, changed only by copying anew
     (harvest, never edits); the archive copy is a historical
     snapshot. -->

# Framing

The first movement of the work: turning an idea into a defined system — on
paper, before any code and before any technology choice. Framing ends when
the promise is stated, the world is described, ownership is settled, and
the work is cut into slices. This chapter describes *what framing is*; the
procedural detail of running it is deliberately left outside the concept.

## Why paper first

The cheapest place to find a design flaw is a description, not a codebase.
But framing is not "write a design doc" — a blank page prescribes nothing.
Framing is a fixed sequence of questions, each of which manufactures the
input the next one needs. Skipping ahead doesn't save time; it produces
answers to questions that weren't well-posed yet.

## The derivation order

The layered system presents outside-in: environment first, then the system,
then its interior. Framing does **not** derive in that order. It starts
from the promise, because the promise is the filter every other question
needs:

**Choose the promise.** One sentence, one claim, falsifiable, worth
proving, for a named audience. Candidate promises that are really features
in disguise get rejected; a second genuine purpose means the intent is
overloaded, and the choice is forced. This is the hardest step and
everything else is downstream of it.

**Decide what must be ours.** For each candidate fact or responsibility,
one test: *if someone else owned this, could they break our promise?* Yes
means it's ours. No means it's refused — deliberately, as a decision, not
a leftover. This fixes the system's authority and boundary.

**Take the census of enemies.** Follow one request through its whole life;
everything beyond the system's control is an actor. For each: can it
vanish, duplicate, or lie? The census collects attackable facts, not
vibes. It is done not when it feels complete but when fresh attempts to
attack the list stop finding anything new.

**Run the collisions.** Each enemy fact against each thing we own: what
dies? The output names the *kill*, never the cure — "a retry lands twice
and the order exists twice," not "add an idempotency key." Mechanisms
chosen here would be mechanisms chosen before the problem is understood.

**Sort into owners — only if forced.** If the collisions genuinely demand
separate owners, partition; if one owner covers everything, that is a
confirmed answer. Then, and only if there is more than one owner, examine
the seams between them for the correctness no single owner can hold.

**Cut into slices.** The collisions become a registry of slices — each
slice one invariant to be made real. The registry's ordering is an
expectation, not a commitment; evidence from built slices is allowed to
reorder what remains.

## Why this order and not the map's

The promise comes first because "ours or not ours" is unanswerable without
it. The enemy census comes before the interior partition because the
partition should be *forced by collisions*, not sketched from intuition —
areas invented before the adversity is known are org charts, not ownership.
The two inversions (promise before environment, collisions before areas)
are what make framing a derivation instead of a documentation exercise.

## What framing produces

Three things, whatever their recorded form: the **intent** (the promise and
the purposes refused), the **system definition** (authority, boundary,
adversary, owners), and the **slice registry** (the work, cut into
invariants). Nothing in them names a language, a store, or a framework —
technology arrives only after framing, each choice answerable to the
registry it must serve, never the other way around.
