# Commit plan: SL-4's build — a consume interrupted, and proven whole

<!-- The convention: .claude/skills/commit-plan. The commits that
     exist are the steps done; this file plans them and is deleted
     at the close. -->

## Summary — the state after all commits

SL-4 closed on evidence. Nothing a user does has changed, except
one answer: an instance that loses the store mid-request now says
`503`, "outcome unknown", as Problem Details (ADR-0016), where it
used to fall to the framework's default error.

What the repo gains: a harness that can interrupt a consume on
purpose — hold it mid-work from outside every instance, kill the
instance outright, freeze and thaw the store, end one session —
and a witness that reads the item's numbers and a reservation's
ending in one reading; evidence E1–E4, each seen red with consume
split into two transactions (or, for E4, a half-consume planted)
and then green unchanged; SL-1's, SL-2's and SL-3's evidence green
unchanged beside it; one handler at the door; the slice record
closed as invariant → guarantees → owner → evidence; the registry
carrying SL-4 as closed, and saying whether anything stands before
Release. No migration, and no change to the ledger. ADR-0016,
decided at the step's opening, is Accepted only once the build has
tested it; and `cbc-slice` says so for every slice after this one.

## Commits

**1. `docs(agent): add commit plan for SL-4's build`**
This file. Six steps of work, two of bookkeeping, and two that set
the status of a decision taken before the build; the harness step
may still reshape what follows it.

**2. `chore(agent): a slice's ADRs open Proposed`**
`cbc-slice`, edited in place under `delivered-copies.md` rule 2,
with its decisions entry: a decision a slice records as an ADR
before its specification opens Proposed, and is Accepted in the
slice's closing records, once the build has tested it — the build
can still reshape it, and a Proposed record is corrected in place
where an Accepted one could only be superseded. In SKILL.md's
Stage 1 and the workflow's step 3. Its own step, never folded into
a records step. The rule first, then its first use.

**3. `docs: hold ADR-0016 as Proposed`**
ADR-0016's status line only, Accepted → Proposed, and nothing else
in it. It was committed Accepted at the opening (5299d3a), but
commits 8 and 9 build and test the answer it decides, and either
could reshape it. Still on this step's branch, never on main, so no
record outside the step ever read it as Accepted.

**4. `test: hold, kill and freeze a consume`**
The harness, before any evidence leans on it. `ForkedLedger` gains
a kill outright. A hold: the store's superuser takes the item's row
or a reservation's row and keeps it until let go, and the test sees
the consume waiting there by reading the store's own list of who
waits on whom, never by sleeping. `ThrowawayStore` gains a freeze
and a thaw, and ending one session. The witness gains one reading
of an item's numbers with one reservation's ending. With it, the
harness's own checks: a consume held at each point is seen waiting
there, and finishes when let go. This step settles §8's
provisional — whether the hold at the reservation's row stops a
consume after both moves; if not, E1 holds at one point and a
revision says so. 83 tests stay green.

**5. `docs: revise SL-4's plan — when a kill is seen`**
Added by revision, after the harness. §8's G3 said a killed
instance's consume is undone "at once"; the harness showed the store
notices a dead connection only when it next speaks to it, so a
killed instance's consume that is waiting on a row stays at the
store until its wait ends, and is undone then. The outcome is the
same — nothing committed, nothing half-done readable — and only the
timing differs, which is W3's. Two sentences of §8 corrected with
their date, the provisional on the hold at R's row settled as
confirmed, and the devlog's line. No wall, face or test changes.

**6. `test: kill a consume midway`**
E1 (G1, kill 15). A consume held at each point, its instance
killed outright, the witness read after: both moves or neither.
Red with consume split into two transactions on the working tree,
the hold landing between them: R consumed, the count unmoved.

**7. `test: read and decide beside a held consume`**
E2 (G2, kill 15's reader). A consume held after both moves; while
held, the witness reads and a reserve on the same item is sent from
another instance; then the consume is let go, or its instance
killed. The reserve decides against one whole state, before or
after. Red with the same split: the reserve reads half a consume
and is admitted against units that leave the shelf.

**8. `feat: answer 503 when the store is out of reach`**
ADR-0016 at the door: one handler in `DoorProblems`, deciding by the
store's own error class — SQLSTATE `08…`, `57P01`–`57P03` — and
nothing else. Its check beside it, saying on itself that it checks
the door's convention and discharges no kill: a consume held, the
instance's session ended by the superuser, the answer `503` as
Problem Details, "outcome unknown". Seen red without the handler,
recorded from actual output.

**9. `test: freeze the store mid-consume`**
E3 (G3, kill 16's silence). A consume held, the store frozen, the
caller giving up, nothing sent after; the store thawed — with the
instance alive, and with it killed while frozen. The witness reads
both moves or neither with no request since, and a reserve on the
same item then goes through against those numbers. Red with the
same split, the second transaction never arriving.

**10. `test: a consumed receipt moves the count with it`**
E4 (G4): a fifth rule in `NoSecondWayOutTest` — a statement writes a
`consumed` receipt if and only if it lowers `on_hand_count` by the
same units, and nothing else lowers it relative to itself. Red by
planting each violation it forbids: a consume that lowers only the
units held; a release that lowers the count; the count lowered
alone.

**11. `docs: records catch up on SL-4`** *(provisional split)*
The slice record's evidence as delivered (§9), standing guards
(§10) and its sign-off line; ADR-0016 to Accepted, or revised in
place first if the build reshaped it; the registry closing SL-4 by a dated
revision entry and saying what follows; README's `503` as a
stranger meets it; CHANGELOG and the version's move; ARCHITECTURE;
the devlog with every red and green from actual output; TODO's
Step 8 items closed or moved, the known issue on the wait kept;
PLAN's evidence, deviations, Stage 4 and records items ticked. A
sweep for "SL-4" in live text that still speaks of it as future.
The close's shape readings follow here, and a step of their own is
added by revision if they find anything; so is any agent-path
change, never folded into this step.

**12. `docs(agent): close commit plan for SL-4's build`**
Deletes this file.

## Decisions taken inside this plan

- **The reds on the working tree, never in history.** The wall
  stands already (SL-3), so there is no naive version to land
  first. Each of E1–E3 is reddened by splitting consume into two
  transactions on the working tree; E4 by planting. Nothing red is
  committed.
- **The harness acts as the store's superuser,** trusted under T4.
  It holds rows, ends a session and reads who waits; it writes no
  data. No test code runs inside an instance (ADR-0009).
- **The harness's own checks are not evidence.** They show a hold
  stops a consume where it says, before any evidence leans on it —
  the wall's-own-check kind, exempt from the evidence-test shape,
  saying so at their class.
- **The `503` lands after E1 and E2, before E3.** E3's check of
  ADR-0016 needs the handler; E1 and E2 do not, and read only the
  store.
- **ADR-0016 goes back to Proposed, at the reviewer's ask.** It was
  committed Accepted at the opening, as ADR-0013 was at SL-3's, on
  the reading that a decision taken before the set is not one the
  set can reshape. That reading was wrong here: commits 8 and 9
  build and test the very answer it decides. Moving a status back
  is unusual; it is done because the record has not left this
  step's branch. ADR-0001's immutability binds an Accepted record,
  and this one is not Accepted again until the build has run.
- **The rule goes in the skill, not in TODO.** TODO holds what is
  noticed and not done now; this is done now, and its next use is
  SL-4's own records step. `cbc-slice` is where the opening's
  decisions are made, so it is where the status is set;
  `commit-plan` already says an ADR inside a set opens Proposed,
  and is not touched. The deliverer is told by the one TODO line
  rule 4 owes at this step's close.
- **Test classes by adversity, not by guarantee** — provisional: a
  class for a consume held and killed or read beside (E1, E2), one
  for the frozen store (E3 and the `503` check), the structural rule
  in SL-3's class. The red runs decide; a revision records a
  different split.
- **No commit reaches outside the plan.** If the build needs a
  change to §8 — a hold point that does not hold, a wall that does
  not stand as planned — the work stops, §8 is revised with the
  reviewer, and then this file.
