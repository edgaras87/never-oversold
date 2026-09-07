<!-- Derives from concept v1 of correctness-by-construction
     (ADR-0003). Provenance — archive/cbc/system-design-method
     birth-materials/.claude/skills/cbc-framing/references/cbc-framing-workflow.md
     @ fe0075d (imported 2026-08-28, PLAN Step 3). Changes on
     import: none — verbatim below this header.
     Harvested 2026-08-29 from checkout-system's lived framing,
     read read-only (ADR-0007): step 2 records its saturation
     probe log in L1 — saturation checkable, not just claimed.
     Harvested 2026-08-29, same run: the census may carry a short
     labeled trust-assumptions list beside the facts, never
     instead of them.
     Harvested 2026-08-29 from the safe-reservations framing node,
     read read-only (ADR-0007), its vocabulary never adopted:
     step 0 carries the audience distinction, worth-proving, and
     what done demonstrably means.
     Harvested 2026-08-29, same node: step 2 carries the probe
     machinery as runnable procedure — the three lenses, the
     three-stamp rule, the audit checklist — taught in the lived
     walk at the human's own demand.
     Harvested 2026-08-29, same node: step 2's exit leaves three
     ledgers — the numbered fence list, the not-probed ledger,
     and scope verdicts with recommendation and reason.
     Harvested 2026-08-29, same node: step 6 named as three
     passes — sort, dedupe, folds — each with its test and its
     record. -->

# CbC framing workflow — layering the system

*The first part of the method: how a raw idea becomes a layered system
definition and a slice surface. Ends exactly where `cbc-slice-workflow.md`
begins — its output is that workflow's input.*

---

## The unit

A **framing** = one promise worked into a slice surface. Input: a standing
idea (an itch, a need, a candidate problem). Output: three artifacts — the
**intent** (the promise), the **system definition** (L1–L5 filled in), and
the **slice registry** (the work, cut and ready). A framing may honestly end
in **no project** — that is a valid exit, not a failure.

## The order — and why it isn't the map's order

The layers are *presented* L1→L5 (outside-in containment). They are
*derived* in a different order, and the difference is deliberate:

```
step 0   choose the promise        (intent)   — one claim worth proving
step 1   what must be ours?        (L2)       — hostage test
step 2   name the enemies          (L1)       — the census
step 3   run the collisions        (L4)       — facts × possessions
step 4   sort into owners          (L3)       — only if collisions force it
step 5   check between owners      (L5)       — only if owners exist
step 6   cut the slice surface                — theorems vs definitions
```

Two inversions, both tested: **L2 before L1** — the promise is derivable on
rough enemy *sketches*; the full census comes after and pays off the
sketches. **L4 before L3** — internal areas are *discovered* by sorting
collisions, never drawn first on intuition.

## How the layers communicate

Four rules govern every handoff:

1. **Downward, each step consumes exactly the previous step's output.** The
   promise feeds the hostage test; the possessions feed the census scope;
   facts × possessions feed the collisions; the kills feed the ownership
   sort; the owners feed the seams; everything feeds the cut. A step whose
   input isn't the prior step's output is out of order.
2. **Upward only through logged return trips, never scheduled ones.** The
   census (step 2) may reveal a missed possession or an unwritten refusal —
   then L2 gets a *logged revision*. If not, L2 stands. No routine
   "polish pass" back up: that reopens settled layers forever.
3. **Reasoning may cross layers; statements may not.** Enemy-talk is
   allowed inside a *derivation* ("ours *because* a retrying caller could
   break the promise") — but the resulting L2 statement stays pure L2.
   Every recorded statement sits in exactly one layer.
4. **Every "outside" is written.** A refusal, a deferral, a rejected
   candidate promise — decided out loud and recorded, never dropped in
   silence. Silence is the only forbidden channel.

---

## Step 0 — Choose the promise (intent)

*Question: what claim is this project selling, and to whom?*

Name the capability; name who values it; name what "done" demonstrably
means. Filter out promises that are really features: *could this claim be
false, and would proving it true matter to the named audience?* A second
real purpose is not a second promise — an intent carrying two is overloaded;
force the choice.

Three sharpenings, each written into the intent:

- **The audience is who the claim is sold to** — the party for whom
  the proof matters. Consumers of the running system (callers,
  services) cannot be sold a claim; they generate adversity and
  enter the census as actors, never the intent as audience.
- **Worth proving is two questions, both answered concretely:**
  could it be false — the negation named as an event, not a mood;
  and would proving it true matter to the named audience.
- **What "done" demonstrably means, spelled out:** the named
  adversity genuinely created (never sequential replay pretending),
  the witness — checkable from outside, on state — never firing,
  and the path from claim to proof followable by the audience.

**Exit:** one promise, one sentence, falsifiable, worth proving.
**Hands down:** the promise. **Residue (written):** rejected candidates and
un-chosen purposes, banked in the log.

## Step 1 — What must be ours? (L2)

*Question: to keep that promise, what must we own — and what do we refuse?*

The engine is the **hostage test**: for each candidate possession — *"if
somebody else owned this, could they break the promise without us being
able to stop them?"* Yes → ours. No → run the mirror: *"can the promise
stay true even if this goes wrong elsewhere?"* Yes → refused, in writing.
The test runs on **sketch-enemies** (a retrying caller, a lying store) —
informal, just concrete enough to power the test. Those sketches are
**debt**; step 2 pays it.

**Exit:** owned responsibilities stated; refusals stated.
**Hands down:** the possessions (what the census must cover) and the
sketch-debt. **Boundary note:** this is also where the edge lives — every
crossing from outside is where untrusted becomes trusted; check at the door.

## Step 2 — Name the enemies (L1)

*Question: who and what touches us, and how does each fail, lie, or repeat?*

Enumerate actors by following one request through its whole life —
everything it touches beyond our control is an actor (typically: callers,
network, our own process, the store). For each, the three negations:
**vanishes, duplicates, lies**. Keep only *facts*, concrete enough to attack
with ("responses get lost", not "networks are unreliable"), stated
consequence-first — what each fact leaves *us* facing.

**The trap:** a census is not an assumption inventory. Listing what you
*trust* ("the store is atomic") instead of what can *hurt* ("a write's
outcome can be unknowable") passes casual reading and fails the standard.
The store is an actor that can hurt you, not an ally with guaranteed tools.

What *is* allowed — the opposite of hidden trust: a short list under
its own label, **"Trust assumptions — accepted deliberately, not
defended"**, sitting *beside* the facts, never replacing them. Each
line is a deliberate acceptance a reviewer can challenge ("the store
durably holds a commit it acknowledged"). Kept short; the moment it
grows easier to extend than the fact list, it has become the trap.

**The probe machinery — how saturation is earned.** The enumeration
ran on one lens (actors × three negations), which only finds facts
shaped like "someone misbehaves." A probe deliberately picks up a
**different lens** and re-scans the same territory; what a new lens
catches, the old one was blind to. Three lenses, each mechanical:

1. **The assumption hunt.** Per census line, finish *"this line is
   only true/complete if ___"* — the blank is the hidden assumption;
   then ask *what if that's false?* Hunt **silent singulars** (every
   "the X" — could there be two?) and **silent successes** (every
   action taken for granted — what's the other branch?).
2. **The timeline stretched.** Run the film long, three questions:
   *what accumulates?* (anything that only grows) · *what expires or
   is forgotten?* (forgetting collides with late arrivals) · *what
   arrives very late?* Shortest form: take every noun and ask *"what
   does this look like after a year?"*
3. **The resource lens.** List every *quantity*, set each to
   **zero / many / huge**, watch what breaks — a literal grid works.
   Boring cells are evidence, not failure.

**The three-stamp rule.** Every probe result gets one stamp, out
loud: **new fact** (the census grows) · **nothing new** (naming which
existing line covers it — forcing the actual check) · **out of
scope** (written out in ink). A silent probe is no probe; the stamps
are what make "did we miss something?" answerable.

**The audit checklist** — the human verifies the framer without
out-deriving him: did each line get the assumption question? was
every quantity in the grid? did anything accumulate un-asked? does
every probe result carry a stamp?

**Exit — saturation by probes:** run the lenses until **two
consecutive lenses yield zero new facts** — a number, not a feeling.
**The probe log is recorded in L1** — each probe's lens, its stamps,
what it surfaced (or "nothing new") — so the saturation claim is
checkable by a later reader, not just made. Out-of-scope facts get a
written refusal or deferral line, never silence.

**What the exit leaves behind — three ledgers, in ink:**

- **The fence list.** Every written-out scope exclusion as a
  numbered line (W1, W2, …). A fence is a working boundary, not a
  disclaimer — it earns its ink by *visibly stopping facts* at
  step 3, where a fenced fact collides with nothing.
- **The not-probed ledger.** What was *not* examined, named honestly
  (clock skew, deployment transitions, …) — so a later reader knows
  the census's edge, not just its content. An unprobed door flagged
  here is raised at the step's close, never silently carried.
- **The scope verdicts.** Each boundary question the census surfaced
  is put as a named verdict with a recommendation and its reason —
  *in* (more of the same named kind, richer demonstration) · *out*
  (a new kind of difficulty, another promise's territory) ·
  *censused-and-fenced* (the facts stay, the boundary holds). The
  verdicts follow the mode's rule on who decides.

**Hands down:** the fact list and the three ledgers. **Hands up
(conditional, logged):** a missed possession or unwritten refusal →
L2 revision.

## Step 3 — Run the collisions (L4)

*Question: for each enemy fact × each possession — what distinct kill?*

Collide one possession at a time against every fact that can reach it.
State each kill as **what dies, never how it's saved** — mechanisms are
parked the instant they surface. Split two kills into two concerns only
when their *proof obligations* differ; dedupe by attack surface (what a
fact does to us, not whose fault it is). Wording holes inside L2's own
possessions — concerns with no enemy — get marked as fold-candidates, not
slices. A possession that lends all its facts to other kills and keeps none
is the stage, not a victim — normal.

**Exit:** a full pass across all facts × possessions yields nothing new.
**Hands down:** kills stated invariant-shaped, adversity named — consumable
by the slice workflow's specify-correctness with zero translation.

## Step 4 — Sort into owners (L3)

*Question: do the collisions force distinct owners, or does one area own
everything?*

For each concern, name the state and decisions it touches. A division is
real only if it would hold **separate state AND separate decision
authority**. Concern-count is not area-count. Where a plausible seam fails
the bar, name it and refuse to draw it — a noted-not-drawn seam is better
record than silence.

**Exit:** ownership stated — even if the statement is "one area." A thin or
empty L3, reached by sorting real kills, is a *confirmed* answer, not a gap.
**Hands down:** the owners (or the one owner).

## Step 5 — Check between owners (L5)

*Question: did step 4 create seams?*

If no: record **empty, with its reasons** — each emptiness a consequence of
a prior decision (one area; siblings refused at L2), never a blank. If yes:
name what must hold across the seams that no local guarantee covers —
cross-boundary ordering, coordinated failure, consistency at the joints.
L5 takes L3's boundaries and L4's local guarantees as given; it never
redefines them.

**Exit:** L5 empty-with-reason, or its cross-area constraints stated.

## Step 6 — Cut the slice surface

*Question: which concerns are theorems, which are definitions?*

A **slice** = one promise × one enemy needing its own proof. The step
runs as **three passes**, each with its test and its record:

1. **The sort.** Every concern stamped **theorem** (carries an
   invariant-under-adversity of its own — proof means *creating*
   that adversity and witnessing the invariant survive) or
   **definition** (contract-shaped — declared and checked, no
   adversity stages against it), with a written *because* per stamp
   so a near-miss's doubt is answered in the record, not left
   lingering. When step 3's discipline held, kills arrive already
   theorem-shaped and this pass is a confirmation sweep — that is
   the discipline working, not the pass being skipped.
2. **The dedupe.** Pairwise on the theorems, the operational test:
   two kills are two slices only when their evidence must *create
   different adversity* (a proof that sequential replay generates is
   not the proof contention demands). Same stageable adversity ⇒ one
   slice, merged visibly; different ⇒ both stand. Splits parked
   earlier as visible choices are settled here.
3. **The folds.** Each definition folds into the first slice that
   consumes it, its check riding along. The registry is composed,
   carrying the **fold-reconciliation line** (concerns ↔ slices +
   folds) so nothing is silently dropped.

Ordering is an *expectation*, not a commitment: derive a presumption order
(what each proof takes as already standing), but re-decide the actual next
slice at each close. Step 6's deliverable is the **shape**, not a sequence.

**Exit:** the registry stands — first slice chosen-next.
**Hands off to:** `cbc-slice-workflow.md`, one slice at a time.

---

## What this workflow refuses to contain

Implementation, technology, repo structure, environment setup, and work
tracking all live downstream — naming a mechanism here is the same leak as
naming one in a correctness spec. And idea *generation* lives upstream: this
workflow sharpens a standing idea; it does not invent one.

## Open (to be settled by living)

- Step 0 from a genuinely raw itch (all lived framings started from an
  already-shaped idea).
- How the promise generalizes for systems that compute or relay rather than
  own facts (renderer, proxy).
- The multi-owner case end to end: every lived framing so far produced one
  area and an empty L5.