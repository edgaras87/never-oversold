<!-- Derives from concept v1 of correctness-by-construction
     (ADR-0003). Provenance — archive/cbc/system-design-method
     birth-materials/.claude/skills/cbc-slice/references/worked-example.md
     @ fe0075d (imported 2026-08-28, PLAN Step 3). Changes on
     import: none — verbatim below this header.
     Twin: byte-identical copy in both skills' references/ — the
     bundle duplicates it so each installed skill is self-contained;
     a change to one lands in both. -->

# Worked example: a tiny order service through both workflows

*A demonstration run, invented for teaching — not a lived framing. It shows
part 1 (framing, steps 0–6), the three artifacts it leaves behind, and how
part 2 (the slice workflow) consumes them. Kept deliberately small: one
promise, one area.*

---

# Part 1 — the framing

## Step 0 — Choose the promise

Idea brought to the walk: "a backend where shops submit customer orders."

Candidate promises considered and banked:
- "Fast order processing" — rejected: a feature-wish, not falsifiable.
- "Orders are never lost AND payment always completes" — rejected:
  overloaded; payment is a second purpose, forced to choose.

**Chosen promise:** *One customer submission becomes exactly one recorded
order — no matter how many times it arrives.*
Audience: the shop owner (money) and integrating clients (retry safely).
Falsifiable: the breaking scenario is concrete — a timeout retry creates
two orders, a customer is charged twice.

## Step 1 — What must be ours? (L2)

Hostage test on each candidate:

| Candidate | If someone else owned it, could they break the promise? | Verdict |
|---|---|---|
| The order records | Yes — they could double-record | **ours** |
| The new-vs-repeat decision | Yes — a retrying caller deciding "I'm new" breaks it | **ours** |
| The answer to a repeated submission | Yes — a wrong replay creates a second order | **ours** |
| Payment processing | No — promise stays true even if payment fails | refused, written |
| Product catalog correctness | No — someone else's fact | refused, written |
| Notifying the customer | No | refused, written |

Sketch-enemies used (debt for step 2): a retrying caller, a store that
answers strangely. Edge note: submissions cross from outside → parsed and
checked at the door.

## Step 2 — Name the enemies (L1)

Follow one submission through its life: caller → network → our process →
store → network → caller. Everything beyond our control is an actor. Facts,
consequence-first:

- Callers resend the same submission after a timeout → we face the same
  order twice.
- Two identical submissions can arrive at the same instant → we face a
  race, not just a repeat.
- A response can be lost *after* we recorded the order → the caller doesn't
  know, will retry, and we must answer again without creating again.
- Our process can die between accepting and recording, or between recording
  and responding → we face half-done work at every boundary.
- A caller can reuse an idempotency key with a *different* order body →
  we face a lie: "this is a repeat" when it isn't.
- A write's outcome can be unknowable (store timeout) → we face uncertainty
  about our own state.

Census check: no trust-lines ("the store is atomic" does not appear — the
store appears only as something that can hurt us). Saturation: probe 1
(assumption hunt — "what does 'recorded' quietly assume?") surfaced the
unknowable-write fact; probe 2 (timeline stretched — a retry arriving a day
later) surfaced nothing new; probe 3 (quantities at huge — a key reused
thousands of times) surfaced nothing new. Two dry probes → saturated.

Return trip check: did the census reveal a missed possession? No — L2
stands, no revision logged.

## Step 3 — Run the collisions (L4)

Each fact × each possession — what dies (never how it's saved):

1. Retry after timeout × order records → **a second order exists for one
   submission.**
2. Simultaneous duplicates × new-vs-repeat decision → **both are judged
   "new"; two orders exist.**
3. Lost response × the repeat-answer → **the retry is answered as new; a
   second order exists.**
4. Death mid-work × order records → **an order half-exists: recorded but
   unconfirmable, or confirmed but unrecorded.**
5. Key reused with different body × new-vs-repeat decision → **the wrong
   order is silently confirmed as a "repeat."**
6. Unknowable write outcome × order records → **we ourselves don't know if
   the order exists.**

Dedup by attack surface: kills 1–3 are one concern (duplicate delivery in
three costumes — same thing dies the same way). Kills 4 and 6 are one
concern (partial failure — the proof obligation is "recovery converges,"
different from "duplicates collapse"). Kill 5 stands alone (a lie, not a
repeat). Fold-candidate found with no enemy: "what a valid submission is"
(the contract) — contract-shaped, no invariant-under-adversity → folds, not
a slice.

## Step 4 — Sort into owners (L3)

All concerns touch one state (the order records) and one decision
(new-vs-repeat). Candidate seam probed: "key management" as its own area —
fails the bar: no separate state, no separate decision authority. Named,
not drawn. **Ownership: one area — order intake.** Confirmed by sorting,
not assumed.

## Step 5 — Between owners (L5)

**Empty, with reasons:** one internal area (step 4); no sibling systems to
coordinate (payment refused at L2). Each emptiness traces to a decision.

## Step 6 — Cut the slice surface

Operational test — two kills are two slices only if their evidence must
create *different adversity*:

- **Slice 1 — "exactly once under duplicate delivery."** Invariant: N
  arrivals of one submission → exactly one recorded order. Covers kills
  1–3 and 5 (its evidence must create replays, races, AND the reused-key
  lie). Judgment logged: races fold in here because duplicate delivery is
  the one adversity class, concurrent being its hardest costume.
- **Slice 2 — "recovery converges under partial failure."** Invariant: a
  crash at any point leaves the system where a retry ends in exactly one
  order and one truthful answer. Covers kills 4 and 6. Different evidence:
  kill-mid-transaction, not hammering — hence its own slice.

Folds, reconciled: the submission contract → folds into slice 1's spec
(its checks ride along). Nothing dropped: 6 kills → 2 slices + 1 fold.

**Registry:** slice 1 chosen-next (slice 2 presumes 1's uniqueness ground —
an expectation, re-decided at close).

---

## What now exists (the three artifacts)

1. **Intent:** the promise, one sentence, plus banked rejections.
2. **System definition:** L1 the six facts · L2 three possessions, three
   written refusals · L3 one area, one seam named-not-drawn · L4 the
   concerns · L5 empty-with-reasons.
3. **Slice registry:** 2 slices + 1 fold, reconciliation line, slice 1
   chosen-next.

---

# Part 2 — slice 1 enters the slice workflow

**specify-correctness** consumes the registry entry *as written* — zero
translation:

- Invariant: N arrivals of one submission → exactly one recorded order.
- Adversity (pulled from the L1 census, not invented): sequential retries,
  simultaneous duplicates, lost responses, key reuse with different body.
- Guarantee challenge — "what would let this hold on paper yet break in
  fact?": (a) **uniqueness** — a key can never map to two orders; (b)
  **winner/loser** — of simultaneous duplicates, exactly one creates, the
  rest observe it; (c) **safe replay** — a repeat receives the original
  outcome, changing nothing; (d) **key scope** — a repeat is only a repeat
  if the body matches; a mismatch is an error, never a silent success.
- Evidence criteria: a hammering test (many concurrent identical
  submissions), an injected-replay test (retry after a swallowed
  response), a mismatched-body test. Gate passed: zero mechanisms named.

**plan** — one structural owner per guarantee, strongest wall available:
(a) database unique constraint on the key — *defeats duplicates because
the second insert cannot physically succeed*; (b) the same constraint's
winner + losers reading the winner's row; (c) the stored original response,
returned on conflict; (d) a body-hash stored with the key, compared on
conflict. Escape-hatch hunt: any admin insert path must go through the same
constraint-guarded table — no side door. Gate passed: no unowned guarantee.

**build** — boring code plus the evidence: hammer it, inject the replay,
send the mismatched body — watch the invariant survive, guarantee by
guarantee. Gate: green happy-path tests close nothing; only
adversity-creating tests do.

**document** — invariant → four guarantees → their owners → their evidence.
Slice 1 closes in the registry as evidence-closed; the registry re-decides
what's next.

---

## What the example shows about the seams

- The census (step 2) is where part 2's adversity models come from — the
  spec *pulled* its threats from L1.
- The kills (step 3) arrived in the spec invariant-shaped — the
  zero-translation handoff.
- The refusals (step 1) are why the spec never mentions payment.
- The thin L3 is why there is exactly one place the code and its walls live.
- The fold carried the contract checks into slice 1 instead of losing them.