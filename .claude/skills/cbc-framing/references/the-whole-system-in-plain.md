<!-- Derives from concept v1 of correctness-by-construction
     (ADR-0003). Provenance — archive/cbc/system-design-method
     birth-materials/.claude/skills/cbc-framing/references/the-whole-system-in-plain.md
     @ fe0075d (imported 2026-08-28, PLAN Step 3). Changes on
     import: none — verbatim below this header. -->

# The whole system in plain words

*One page. No jargon. If this page and the detailed docs ever disagree, one
of them is wrong — find out which.*

**What this is, in one sentence:** a design discipline that derives a
backend from one falsifiable promise — asking *"what must never happen?"*
before *"what should it do?"* — and answers each never-event with a
structural wall, proven by a test that creates the attack.

## The one idea

Most software is designed by asking "what should it do?" and listing
features. This method flips it: ask **"what must never happen?"** first.
Money counted twice. An order created twice. A stale answer shown as fresh.
Then build the smallest system that makes those disasters *impossible by
structure* — not caught by tests, not fixed after alerts. Features come
last, as the minimum needed to keep the promises real. Every extra feature
is a new way to break your own guarantees, so staying small is discipline,
not taste.

## The map: five nested boxes

- **L1 — The world.** Everything outside your control: users, networks,
  other systems, your own process crashing. It is hostile: things vanish,
  duplicate, and lie.
- **L2 — Your system.** One promise: which facts you are the authority on,
  and what you commit to about them. A real promise can be broken — you can
  describe the exact scenario where it fails. Also the boundary: everything
  crossing from outside gets checked right at the door.
- **L3 — The parts inside.** Split by one rule: every fact has exactly one
  owner. Often the answer is "one part" — that's fine.
- **L4 — The small problems.** Inside each part: what must hold true, and
  what attacks it. Small on purpose — the boxes above made them small.
- **L5 — Between the parts.** Correctness no single part can own. Where
  most real incidents live. Empty if there's only one part.

## The order of work (not the same as the map's order)

0. **Choose the promise** (intent). One sentence, worth proving, for a
   named audience.
1. **What must be ours?** (L2). For each candidate: "if someone else owned
   this, could they break our promise?" Yes → ours. No → refused, in
   writing.
2. **List the enemies** (L1). Follow one request through its whole life;
   everything beyond your control is an actor. For each: can it vanish,
   duplicate, or lie? Write facts you can attack with ("responses get
   lost"), not vibes ("networks are unreliable"). Stop when two fresh
   attacks on the list find nothing new.
3. **Run the collisions** (L4). Each enemy fact × each thing you own: what
   dies? Name the kill, never the cure — mechanisms come later.
4. **Sort into owners** (L3) — only if the collisions actually force
   separate owners. One owner is a confirmed answer, not a gap.
5. **Check between owners** (L5) — only if step 4 made more than one.
6. **Cut into slices.** A slice = one invariant made real: proven to
   survive its specific attack, end to end.

*(Numbered 0–6 to match the detailed workflows step for step.)*

## Working one slice

**Specify** — write what must hold and what attacks it; no solutions yet.
**Plan** — pick the mechanism and say why it beats *that* attack.
**Build** — the mechanism, plus a test that *creates* the attack (hammer it
concurrently, inject duplicates, kill it mid-write) and shows the invariant
surviving. **Document** — the promise, the guarantees, the proof, readable
later. The attack-creating test is the deliverable most projects skip.

## Enforcement: who guards each guarantee

Every guarantee gets exactly one structural guard, strongest available:

database constraint → type system → single checked entry path → runtime
check → code review → hope.

"All the code being careful" is not a guard — it's the absence of one.

## How it rots (watch for these)

New features shipping without re-checking the guarantees. An admin script
that bypasses the one enforced path — the guarantee quietly becomes
fiction. Guarantees drifting out of structure and back into test suites.
And over-theorizing before the boxes have shrunk the problem.

## When NOT to use this

Throwaway code, experiments, low-stakes tools: iterate-and-fix is cheaper
and not wrong. This method pays off when breaking the promise is expensive
or irreversible (money, safety, trust), or the system will live long.

