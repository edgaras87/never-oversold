<!-- Provenance — archive/cbc/system-design-method
     birth-materials/concept/04-cbc-slice.md @ fe0075d (imported
     2026-08-28, PLAN Step 2). Changes on import: none — verbatim
     below this header. The authoritative copy lives in
     correctness-by-construction's concept/ — a run's copy
     (docs/concept/) is pinned, changed only by copying anew
     (harvest, never edits); the archive copy is a historical
     snapshot. -->

# The slice

The second movement of the work, and the unit of construction: **one
invariant made real, end to end.** Not a feature, not a screen, not a
sprint's worth of tickets — one never-event from the guarantee inventory,
taken from specification to a structural wall, proven to survive its
specific attack. This chapter describes what a slice is; the procedural
detail of working one is left outside the concept.

## Why the invariant is the unit

Cutting work by feature reproduces the inversion the discipline exists to
avoid: features first, correctness later. Cutting by invariant keeps the
derivation direction intact all the way into code — every unit of work is
a guarantee becoming enforceable, and the feature surface grows only as a
side effect of guarantees needing somewhere to live.

A slice is *closed* when its invariant demonstrably survives the adversity
that attacks it. Closed means the question is over: the wall stands, the
proof exists, and later work is allowed to build on it without re-arguing
it.

## The four phases

**Specify.** State what must hold and what attacks it — the invariant and
its named adversity, taken from the framing. No mechanisms. A specification
that says "use a unique constraint" has answered the next phase's question
in this one, which means the mechanism was chosen before the requirement
was fully stated.

**Plan.** Choose the mechanism, and say why it beats *that* attack — not
why it is good in general. The wall is picked from the enforcement
hierarchy, strongest available; choosing a weaker wall than possible is a
decision that needs a stated reason.

**Build.** The mechanism, plus the proof: a test that ***creates* the
attack** — hammers the endpoint concurrently, injects the duplicate, kills
the process mid-write — and shows the invariant surviving. This test is the
defining deliverable of the slice, and the one most projects skip. An
attack-creating test is different in kind from a correctness test: it
doesn't check that the code does the right thing, it manufactures the
hostile world and checks that the wrong thing has become impossible.

**Document.** The claim and its proof, kept readable: what is promised,
what wall holds it, and how it was shown to survive — so that the slice
stays closed for a reader months later, not just for the person who built
it.

## The proof standard

Structure forbids; tests sample. The slice does not pretend otherwise —
the attack-creating test is not the wall, it is the *demonstration that
the wall exists*. If the wall is ever weakened and only the test remains,
the guarantee has silently moved from structure to sampling, and the slice
is no longer closed, whatever the test suite says.

## Slices and the whole

The registry orders the slices, but the ordering is re-decided as evidence
arrives — a built slice is allowed to teach that the next expected slice is
wrong, split, or unnecessary. And a closed slice is not immune to the
future: a later change touching the state it governs re-opens the question
of whether its wall still holds. Closed means proven, not forgotten.
