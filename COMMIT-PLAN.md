# Commit plan: SL-2's build — the evidence and the two guards

<!-- The convention: .claude/skills/commit-plan. The commits that
     exist are the steps done; this file plans them and is deleted
     at the close. -->

## Summary — the state after all commits

SL-2 closed on evidence. The ledger behaves exactly as it does
today — the plan (§8) adds no production code — but three
corrections that nothing has ever sent are now created by tests
through the real door, and each was seen red before it was seen
green, with the wall made absent on the working tree in a state
that never lands in history.

What the repo gains: `CorrectionIT`, creating an honest correction
under the holds (F9), the same correction resent (F11), and two
corrections delivered in the order opposite to their making (F13);
a structural test for the two guarantees held by an absence, so
neither absence can be filled in silently; the slice record closed
as invariant → guarantees → owner → evidence; and the registry
carrying SL-2 as closed with the ordering re-decided.

## Commits

**1. `docs(agent): open the commit plan for SL-2's build`**
This file. The build is five commits of work and two of bookkeeping,
and the split could turn out wrong halfway — the red runs are what
decide whether E1–E4 want one test class or three.

**2. `test: refuse an honest correction under the holds`**
E1 and E2. An item with holds; a correction asserting a count below
the held units, refused at the door with `409` and not `400`; its
mirror, a correction that fits taken exactly, which is what pins G2
against a clamp; and the whole witness — count, held, active sum,
reservation count — identical before and after the refusal. Red
first with the `WHERE` guard removed from `Ledger.adjust` and the
`item_never_oversold` constraint commented out of V1, both on the
working tree only: the correction is then admitted and the witness
shows the active sum above the count, which is the promise's
negation. Restored, then green.

**3. `test: a resent correction asserts the same state`**
E3, both worlds: a correction that fits, sent twice, leaving the
state after the second identical to the state after the first; and
one that does not fit, refused twice, moving nothing either time.
The resend is the same body at the same door, not a second request
written to look alike. Its own commit because it proves a different
kill (7) and its red is a different red — with the guard gone, the
second send lowers the count again.

**4. `test: corrections in either order keep the invariant`**
E4. Two items with identical starting states; the same pair of
corrections delivered in opposite orders. Both orders keep the
invariant in every read; the two surviving counts differ, which is
the adversity shown real — the reorder genuinely changed the
outcome — and the difference is W1's remainder, named in the test's
own words rather than asserted away. A second case where the
late-arriving older value sits under the holds and is refused,
which is kill 8 collapsing into kill 6.

**5. `test: guard the absences the plan names`**
E5. G5: the adjustment request carries exactly one field, the
asserted count, so no version, sequence or "as of" instant can be
added without this test failing and §8's face comparison being
re-opened. G6: only the ledger may reach the store, and exactly one
of its methods takes an `OnHandCount`, so a second writing path for
the count cannot appear quietly. Red by planting the violation each
rule forbids, not by removing a wall.

**6. `chore(agent): correct the slice skill from SL-2's lived work`**
Rule 2's three conditions hold, so the copy is edited rather than
described: the skill asks for a test that creates each adversity and
says nothing about the test saying so on itself, which cost this
step several rounds of the reviewer asking what a test was for. The
edit asks for the outcome any project would want — each evidence
test naming its criterion, guarantee and kill; a test that is not
evidence saying so briefly, with what it guards and where the
reasons live; and the counting rule that keeps the second from being
a hole, a tripwire never discharging a kill, the red run deciding
which a test is rather than its author. One decisions entry with
what changed, which step found it and why. TODO's prose hand-off is
replaced by rule 4's one line, the diff and the log being what the
source reads. Agent paths only, as the entry file requires.

**7. `docs: records catch up on SL-2`**
The slice record's §9 evidence as delivered and §10 standing guards;
the registry closing SL-2 with the ordering re-decided and what it
leaves to SL-3 by name; SL-1's record losing the word *provisional*
from its G3 and §6, one act with its reason; README's invariant
list; CHANGELOG and the version's move, decided there as a state of
the evidence; ARCHITECTURE if the shape changed, which it has not;
the devlog with the red and the green from actual output; TODO's
Step 6 items closed or moved by name; PLAN's remaining gate items
ticked.

**8. `docs(agent): close the commit plan for SL-2's build`**
Deletes this file, the convention's own close.

## Decisions taken inside this plan

- **One test class for E1–E4, not three.** They share one setup —
  an item with holds — and the adversities differ by a line each.
  Provisional: if the red runs show the three want different
  fixtures, commits 3 and 4 split their classes and this file
  records it as a revision.
- **The red is taken by removing both halves of the wall**, the
  `WHERE` guard and the constraint. Removing only the guard leaves
  the constraint to reject the write, which reddens the test on a
  `500` rather than on the invariant — a red that proves the test
  reads status codes, not that it watches the promise.
- **No production code.** If a commit turns out to need any, the
  plan is wrong and §8 is re-opened before the code is written, not
  after.

## Revision — 2026-09-21, at commit 4's boundary

A commit added, now 6 of 8: the slice skill corrected in place.
What forced it: the reviewer asked why a test that cannot fail was
kept, and the answer took several rounds that one line in the file
would have saved — a gap in `cbc-slice`, lived here, not
speculated. The first pass filed it as a prose hand-off in TODO's
Later, which rule 4 of `.claude/rules/skills-changed-in-place.md`
forbids: the hand-off is one line, and the edit itself is what the
source reads. The prose item was removed before commit 4 landed;
the edit takes its place. The remaining commits are unchanged in
content and shift by one.
