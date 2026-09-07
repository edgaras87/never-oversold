<!-- Provenance — archive/cbc/system-design-method
     birth-materials/concept/02-guarantees-and-walls.md @ fe0075d (imported
     2026-08-28, PLAN Step 2). Changes on import: none — verbatim
     below this header. The authoritative copy lives in
     correctness-by-construction's concept/ — a run's copy
     (docs/concept/) is pinned, changed only by copying anew
     (harvest, never edits); the archive copy is a historical
     snapshot. -->

# Guarantees and walls

What the discipline actually produces. Not documents — documents are
packaging — but *objects*, whatever their recorded form. Five of them, in
dependency order, and then the hierarchy that decides whether a guarantee
is real.

## The promise

A short, human-readable, **falsifiable** statement of what the system is
answerable for. One sentence, one claim, for a named audience.

Falsifiable is the load-bearing word: you can construct, concretely, the
scenario in which the promise is broken. *"One submission becomes exactly
one recorded order, no matter how many times it arrives"* is a promise —
you can picture the duplicate slipping through. *"A reliable order
platform"* is a mission statement — nothing could ever count as breaking
it, so nothing can be derived from it.

## The described adversary

The environment's ways of being hostile, written as attackable facts:
what can vanish, duplicate, or lie — plus the assumptions explicitly
accepted as trust rather than defended against. This is the promise's
opponent; without it the guarantees below have nothing to be tested
against.

## The guarantee inventory

The promise's negation space: the small set of **never-events**, each
meaningful against a named adversity. "The same submission is never
recorded twice — *against* retries and duplicated deliveries." A
never-event with no named adversary is a slogan; one with no relation to
the promise is scope creep wearing a safety costume.

Small is a feature. The inventory is the few invariants whose violation is
expensive or irreversible — that is where the rigor budget goes.

## The ownership map

For every fact, one owner; for every guarantee, one **structural wall**.
The walls, ranked strongest to weakest:

    database constraint
    → type system
    → single validated entry path
    → runtime check
    → code review
    → hope

Each guarantee is assigned exactly one wall, the strongest available. The
ranking is a hierarchy of *who can violate it*: a database constraint binds
every code path ever written, including future ones; a type error binds
everything that compiles; a single checked entry path binds everything that
goes through the door; a runtime check binds only the paths that reach it;
review and hope bind no one.

"All the code being careful" is not a wall — it is the absence of one. A
guarantee whose owner is carefulness is a design defect, flagged before
implementation, not discovered after.

## The derived surface

The feature set — as an *output*. Each feature carries its justification
against the promise: it exists because some commitment cannot be kept
without it. A feature that cannot say which guarantee it serves is a
candidate for refusal, and refusals are decisions worth stating, not
omissions.

## The stance underneath: prevented, not detected

Running through all five objects is one inherited conviction: errors should
be *prevented by structure*, not found by search. Tests sample; structure
forbids. A test can show the invariant surviving one attack; only a wall
makes the attack meaningless. This is why a guarantee migrating out of the
schema and into a test suite is a loss even when the test is green — the
guarantee has moved from structure to sampling.
