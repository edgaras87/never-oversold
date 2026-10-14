# SL-4 — consume's two moves hold together

Words and tags used here: [glossary](../glossary.md).

<!-- The slice's record, grown in three parts. First the correctness
     specification: what must hold, against what, and what proof
     looks like, with no mechanism. Then the plan: one structural
     owner per guarantee, and why it beats the named adversity. Then
     the evidence as delivered: which test creates which adversity,
     and what it read from the store. Invariant and adversity are the
     registry's and the definition's, as written. Guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each part.

     "Decided before the specification" is this slice's own section:
     two things the framing does not decide, decided before the
     specification that depends on them. -->

*Regrouped by guarantee on 2026-10-13: each guarantee's claim,
attack, evidence criterion, owner, options and tests in one block.
No words changed beyond headings and references. A section number
cited before that date refers to the text as signed, at c6ae21a.*

## Invariant

No readable state holds one of consume's two moves without the
other. The two moves are ending the reservation and lowering the
on-hand-count.
(`docs/system/registry.md`, SL-4; also SL-4 in the definition's L4.)

The words, as the truth set has them:

- **Consume** is P4's: ending a reservation and lowering the
  on-hand-count together, once per reservation. It is the one act
  that moves both numbers. The "once" is SL-3's, closed. This slice
  owns the "together".
- **The two moves.** One ends the reservation, so it no longer
  counts in the units held. The other lowers the on-hand-count by
  what the reservation held.
- **Readable** is the intent's: true in every state readable from
  outside, not eventually.

*Say:* an item has 10 on hand, and reservation R holds 3 of them.
After a consume of R, the item reads 7 on hand with R ended. Before
it, 10 on hand with R holding 3. Nothing else may ever be read:
not 7 on hand with R still holding 3, and not 10 on hand with R
ended.

## Adversity

Our own failure in the middle of a consume. Taken from the
definition's L1 as written:

- we die between consume's two moves, with one move done: the count
  lowered while the reservation still counts, or the reservation
  ended while consumed units still count (F16, kill 15);
- a write's outcome is unknowable, as with a timeout after sending,
  and we fall silent. The consume is neither confirmed nor redone,
  so whatever it left is what stays (F19, kill 16's silence half).

Kills covered: 15, 16 (our silence).

The registry's flag, carried here: the evidence is not hammering. It
kills an instance mid-work and injects an unknown outcome. No race is
created. The adversity is one consume, interrupted.

What this slice does not face:
- our own retry after an unknown outcome (kill 16's retry half):
  SL-3, closed, where a repeated consume moves nothing twice;
- a reserve whose outcome is unknown (F19 × P2): the caller's view,
  refused at L2, and the possible orphan (V5);
- how long anyone waits while a consume is interrupted: W3.

### How the evidence creates it, for every guarantee

Each guarantee's evidence *creates* its adversity through the real
door. It reads the witness from the store, from outside every
instance. The witness is the invariant's, taken in one reading, for
the item and for each reservation on it:
- the on-hand-count;
- the units held, and the sum of active reservations;
- how each reservation ended, if it has.

From these, the evidence checks two things. The on-hand-count has
fallen by exactly the units of the reservations that ended by
consume, once each. And no more is held than is on hand.

The registry's flag decides how the evidence works: no hammering.
The adversity is one consume, interrupted, and the harness
interrupts it on purpose. It holds the consume in the middle of its
work, so the interruption lands there, and not before or after by
luck.

- **SL-1's, SL-2's and SL-3's evidence, unchanged and green.**
- **The harness can fail (R5).** Each of E1–E4 is run with its wall
  absent: the two moves made final apart, on the working tree,
  never in history. Each is seen red, failing on the witness and
  not on a status or a crash, recorded from actual output. Then
  each is seen green, unchanged, with the wall standing.

All of it runs under the one standard test command, with nothing
exported, and the ground need not be up.

## Decided before the specification

Two questions the framing leaves open, found at this slice's
opening (PLAN, Step 8) and decided by the reviewer on 2026-10-09,
each with its options.

### What a caller is told when the outcome is unknown — ADR-0016

When an instance loses the store mid-consume, the consume may have
committed or not, and the instance cannot tell. The framing refuses
the caller's view (L2), so what the caller reads is the door's
convention, not the promise. Before this slice nothing at the door
handled a lost store, and the framework's default error read as
"failed".

*Say:* 10 on hand, R holding 3. The caller consumes R. The store
commits (7 on hand, R consumed), and the connection breaks before
the instance hears. Before this slice, the caller read an error and
believed R was still open.

**Chosen:** the instance answers `503`, Problem Details, "outcome
unknown": the request may or may not have taken effect. The caller
sends the consume again, and ADR-0013 makes that safe:
- `200` if the first landed;
- the consume done now, if it did not;
- refused, if another exit ended R meanwhile.

*Say:* the caller reads `503`, sends consume R again, and reads
`200` with R consumed. The item still reads 7 on hand: one consume,
not two.

**Rejected:**
- leaving the default error: it says "failed" for a consume that
  may have landed;
- `500`: it would read the same as a defect in the ledger;
- a limit on how long an instance waits for the store: the numbers are
  right however long the wait. A limit brings the unknown sooner, but
  does not remove it. And the number is a policy the framing never
  gives (W3).

The options and reasons in full are ADR-0016's.

### Whether anything is left to converge

The registry kept SL-4 apart from SL-3 because the proof
obligations differ: duplicates collapse there, and here "the
half-done converges". That was written at framing, before any
consume existed, when the two moves could have been two writes,
with a half-done state left for something to repair.

SL-3 built consume as one write: the reservation's ending and the
count's fall are committed together, or not at all (SL-3, "How each
path runs"). An interrupted consume leaves the store as it was
before, or as it is after, never between.

**Chosen: nothing converges, because nothing is half-done.** The
specification says so, and the evidence shows it: an interrupted
consume reads as both moves or neither. Convergence is met in the
strongest way: there is never anything to converge from. The
registry's judgment still stands, because the evidence differs from
SL-3's: an interruption, not duplicates.

**Named, outside the promise: the wait an interrupted consume can
leave.** A consume whose instance is cut off from the store (not
killed, cut off) can stay open at the store until the store gives
up on that connection. Until then, other decisions on that item
wait. None reads a wrong number. They only wait, and how long is
W3's, fenced.
*Say:* an instance has run its consume of R on item X, and loses
its network to the store before its commit arrives. A reserve on
item X from another instance waits. When the store gives up on the
lost connection, it undoes the open consume (10 on hand, R holding
3), and the reserve decides against those numbers.

What the decision asks of the evidence: after the interruption, the
next decision on the same item goes through, against the numbers
the witness reads. Nothing is stuck.

**Rejected: limiting that wait in this slice.** It would be a
setting on the store, a change to the ground, made for no
guarantee. Our ground cannot readily stage it either: the long wait
needs a network cut while the instance lives on, and a killed
instance's connection is closed at once. Recorded in the backlog
as a known issue, with when to revisit.

## Guarantees

Each is one answer to: *what would let the invariant hold on paper
yet break in fact?* The attack runs until no new answer comes.
Every guarantee is a property. None names how it is held.

The numbers in the examples start from one item unless they say
otherwise: 10 on hand; reservation R holds 3, reservation S holds
5, so 8 are held and 2 are free. A consume of R should leave 7 on
hand, 5 held.

### SL-4 G1. When a consume is interrupted, the store holds both its moves, the reservation ended and the count lowered, or neither.

*What could go wrong (the attack):* the two moves are made final
one after the other, and we die in between (F16).
*Say:* R's ending is final, and we die before the count falls.
The item reads 10 on hand and 5 held, so 5 look free. But 3 of the
10 were sold and have left the shelf. Five more are admitted:
10 held against the 7 really there. Or the other way round: the
count falls to 7 and we die before R ends. R still holds 3 and has
no ending, so the caller's retry consumes it again: 4 on hand, one
hold's units taken off the shelf twice.

*What we guarantee:* there is no moment at which one of a consume's
moves is final and the other is not. However the consume is
interrupted, and wherever, what remains is both moves or neither.
*Say:* we die mid-consume. The item reads either 10 on hand, R
holding 3, 8 held. Or 7 on hand, R ended by consume, 5 held.
Nothing else.

*Kills:* 15.

- **E1 (G1).** An instance killed outright, not stopped, while its
  consume is held in the middle of its work. Repeated at every
  point the harness can hold it. After each, the witness reads both
  moves or neither. Never R ended with the count unmoved. Never the
  count lowered with R unended.

*The wall:* the transaction: one statement, one commit. The store
makes all of a transaction's writes final at its commit, and a
transaction that never commits leaves nothing (T1; ADR-0005 chose
the store for exactly this).

*Why it beats this attack:* the attack needs a moment at which one
move is final and the other is not. Inside one transaction there
is no such moment. Before the commit, neither is final. After it,
both are. A death at any point before the commit (mid-statement,
or between the statement and the commit) ends the session, and the
store undoes every write in it.
*Say:* the instance is killed while the receipt is written and the
item row still waits. The store undoes the receipt: 10 on hand, R
holding 3, 8 held.

*If the wall were ever wrong:* partly. The check on the units held
(migration V3) refuses any commit where the units held disagree with
the reservations that have no ending. So it refuses a split that
separates R's ending from the units held falling, in either order.
Seen at E1's red (revised 2026-10-10, below): with the moves split
and migration V3 standing, the store refused the first transaction
each way. What passes it is the count's fall kept apart from the
other two. Either the receipt and the units held are made final,
then the count. Or the count is lowered alone. Nothing in the store
stands behind that. E4's rule is the early warning there (G4).

*Options weighed:* the two moves, made final.

**One statement in one transaction** — *chosen*

*How it holds G1:* both moves are one update of one row, beside
the receipt, all made final by one commit.

*Cost:* nothing new: SL-3 built it. A consume holds its item's row
from its update to its commit: one statement, one read, and
migration V3's read of the item's history.
*Assumes:* the definition's size: up to a few thousand reservations on
one item over its life, which migration V3 reads at each commit that
touches the item through migration V4's index. Revisit with migration
V3's known issue: when an item's history passes that size, or a
decision's speed is measured.

---

**Two statements in one transaction**

*How it holds G1:* the same commit makes both final.

*Why not:* no gain, and it breaks SL-3's wall: rule 3 of E7 fails
any statement that lowers a number without writing the receipt.
And the row is held longer.

---

**Two transactions, and a repair that finishes the half-done**

*How it holds G1:* it does not. It converges afterwards.

*Why not:* the half-done state is readable between the two (G2
fails). And the repair is a second path that makes one move alone
(G4). "Whether anything is left to converge" decided there is
nothing to converge.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E1 · G1 | `InterruptedConsumeIT.aConsumeKilledBetweenItsMovesLeavesNeither` | R's consume held at the item's row — its receipt written, the count not yet moved — and its instance killed outright (F16, kill 15) |
| E1 · G1 | `InterruptedConsumeIT.aConsumeKilledAfterBothMovesBeforeItsCommitLeavesNeither` | held at R's row — both moves written, neither committed — and its instance killed outright (F16, kill 15) |

---

### SL-4 G2. While a consume is under way, everyone who reads sees it whole or not at all.

*What could go wrong (the attack):* both moves become final
together, but a reader looks while the consume is under way, and
sees one move and not yet the other. "Readable" is every state
readable from outside, not only the states left behind. And the
reader that matters most is another decision on the same item.
*Say:* a consume of R is under way. A reserve of 5 on the same
item reads R as ended, 5 held, but the count still at 10. So 5
look free, and it is admitted. Then the consume finishes: 7 on
hand, 10 held. Oversold by 3, though no one died.

*What we guarantee:* a reader (a decision, the witness, anyone)
sees a consume either before it or after it, never in the middle.
A decision taken on the same item while a consume is under way
decides against one whole state, before or after, never a mix.
*Say:* the reserve of 5 decides against 10 on hand and 8 held, and
is refused. Or it decides against 7 on hand and 5 held, and is
refused. Either way, the item ends at 7 on hand, 5 held.

*Kills:* 15 (the readable half-state, met by a reader rather than
left by a death).

- **E2 (G2).** A consume held in the middle of its work. While it
  is held, the witness reads the item, and a reserve on the same
  item is sent from another instance. Then the consume is let go.
  - The witness, read while held, shows the before-state whole.
  - The reserve decides against a whole state.
  - After, the witness shows the after-state whole, and no more
    held than on hand.

*The wall:* the store shows no reader a write before its commit,
and a decision on an item waits for the item's row while a consume
holds it. The decision reads the two numbers it compares from that
one row, in one update (SL-1's guarded update).

*Why it beats this attack:* the attack needs a reader to see one
move and not the other. A reader that is not deciding sees the
store as of before the consume's commit, or after it. A reserve on
the same item cannot read past a consume at all. Its update waits
for the row. When the row is free, the store re-reads it, and the
reserve decides against what is there then: the before-state if the
consume was undone, the after-state if it committed.
*Say:* a consume of R is under way, holding the row at 7 on hand,
5 held, uncommitted. A reserve of 5 waits. If the consume commits,
the reserve reads 7 and 5, 2 free, and is refused. If the consume
is undone, the reserve reads 10 and 8, 2 free, and is refused.

*If the wall were ever wrong:* `item_never_oversold`, the store's
check that the units held never pass the count, refuses a decision
that would hold more than is on hand. That covers the promise's own
half, not the consume's. A reserve that saw half a consume could
fit and pass it. Unproven: at the red run, the witness broke first,
mid-consume, and no reserve reached it (the evidence as delivered).

*Options weighed:* what a reader sees mid-consume.

**The store's default isolation, and the item's row** — *chosen*

*How it holds G2:* no uncommitted write is seen. A decision waits
for the row and re-reads it.

*Cost:* nothing new. A reserve on an item waits while a consume of
the same item is under way, for as long as the consume runs.
*Assumes:* a consume's run is short: one statement and a commit.
An interrupted one can keep the row longer ("Whether anything is
left to converge", W3, the known issue).

---

**Serializable isolation on every decision**

*How it holds G2:* the store refuses any decision whose reads a
concurrent commit has made stale.

*Why not:* it holds nothing more here, since the numbers a decision
compares are one row. And a refused decision has to be sent again,
which is a retry path in the ledger (G4's attack).

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E2 · G2 | `InterruptedConsumeIT.aReaderBesideAHeldConsumeSeesItWholeWhenItCommits` | the witness reading, and a reserve at a second instance deciding, while the consume is held; then let go |
| E2 · G2 | `InterruptedConsumeIT.aReaderBesideAHeldConsumeSeesItWholeWhenItDies` | the same, the consume's instance then killed |

---

### SL-4 G3. When a consume is interrupted and nothing more is sent, the store still ends it with both moves or neither, and the next decision on the item goes through.

*What could go wrong (the attack):* the consume's outcome is unknown
(F19), and then nothing more happens. The caller does not send it
again, and the instance that sent it is gone or has given up. Suppose
something were still needed to bring the consume to both moves or
neither, such as a retry, a repair or a person. Then in our silence it
would stay half-done.
*Say:* the store stops answering mid-consume. The caller's request
times out, and the caller walks away. Nobody ever sends consume R
again.

*What we guarantee:* an interrupted consume reaches both moves or
neither with no further request, retry, repair or person. Once it
has, the next decision on the item goes through against that
state. Nothing is left stuck ("Whether anything is left to
converge").
*Say:* the store answers again. With no one sending anything, it
reads 10 on hand with R holding 3, or 7 on hand with R consumed.
A reserve of 2 then decides against those numbers, and fits
either way: 10 held of 10 on hand, or 7 held of 7.

*Kills:* 16 (our silence).

- **E3 (G3).** The store frozen while a consume is in the middle of
  its work. The caller gives up, and nothing else is sent: no retry,
  no repair. The store is let go. With no request since, the witness
  reads both moves or neither. Then a reserve on the same item goes
  through, decided against the numbers the witness read. Beside it,
  not evidence: an instance that loses the store mid-consume answers
  as ADR-0016 says, `503`, "outcome unknown". That checks the door's
  convention, not the promise, and says so on itself.

*The wall:* the store itself. A session that ends without a commit
is undone by the store. A commit the store acknowledged stays (T1).
No path in the ledger finishes, repeats or repairs a consume, and
G4's rule keeps it so.

*Why it beats this attack:* the attack needs something to be owed
after the interruption: a retry, a repair, a person. Nothing is.
The store decides alone, and only between two answers: the commit
arrived, or it did not. When the instance is killed, the store
undoes the consume the next time it speaks to that session and
finds it gone. That is at once if the session was mid-statement or
idle, and when its wait ends if it was waiting on a row (revised
2026-10-09, below). When the store was frozen, it resumes the
moment it is let go, and does the same.
*Say:* the store freezes mid-consume, and the caller walks away.
The store is let go. Whatever it decides, no request since has been
needed. Either the commit arrived: 7 on hand and R consumed. Or the
instance is gone: 10 on hand and R holding 3.

*If the wall were ever wrong:* nothing. This is T1, trusted.

*Options weighed:* an interrupted consume settles.

**The store undoes an ended session** — *chosen*

*How it holds G3:* nothing has to happen after the interruption.
The store decides between committed and undone by itself.

*Cost:* nothing to build. The cost is the wait until the store
notices the session has ended.
*Assumes:* the end reaches the store promptly. For a killed process
on the same machine, as on this ground, that is as soon as the
store next speaks to its session. A session waiting on a row is
found gone when its wait ends (revised 2026-10-09, below). Revisit
when a network sits between the instances and the store (the known
issue: a cut connection can stay open for hours).

---

**A limit at the store on a session left open mid-transaction**

*How it holds G3:* the same, with the wait bounded.

*Why not:* "Whether anything is left to converge" rejected it: a
change to the ground made for no guarantee. Waiting is W3's.

---

**A repair in the ledger**

*How it holds G3:* something finds half-done consumes and finishes
them.

*Why not:* there are none to find, and it would be a second path
(G4).

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E3 · G3 | `StoreOutOfReachIT.aConsumeFrozenOutAndAbandonedSettlesWithNoOneSendingAnything` | the store frozen mid-consume, the caller gone, the instance killed unseen; nothing sent since (F19, kill 16's silence) |

---

### SL-4 G4. Any path that lowers the count for a consume also ends the reservation, and any path that ends one by consume also lowers the count.

*What could go wrong (the attack):* the consume itself holds
together, but another path makes one move alone. For example:
- a recovery step that redoes the count's fall after an unknown
  outcome;
- a repair that ends reservations it thinks were consumed;
- a later feature that lowers the count "for a sale".

*Say:* after an unknown outcome, a well-meant retry inside the
ledger lowers the count by R's 3 again, without looking at R. 4 on
hand, R consumed once.

*What we guarantee:* the count falls for a consume only together
with that reservation's ending by consume. And a reservation ends
by consume only together with the count's fall. This holds on every
path, including paths this slice never foresaw.
*Say:* there is one way to consume R, and it does both.

*Kills:* none by number. The attack is on the walls themselves: a
second way in that makes one move alone, bringing kill 15's
half-state back without any death.

- **E4 (G4).** A structural check that reads the code. No path
  lowers the count for a consume without ending the reservation in
  the same act. No path ends one by consume without lowering the
  count. Seen red by planting a path that makes one move alone.

*The wall:* T4 and a rule over the ledger's code. Only
the ledger writes the store's data (T4). SL-3's structural check
(`NoSecondWayOutTest`, E7) already fails the build if a number
falls in a statement that writes no receipt. A fifth rule, this
slice's E4: a statement writes a `consumed` receipt if and only if
it lowers `on_hand_count` by the same units. And nothing else
lowers `on_hand_count` relative to itself.

*Why it beats this attack:* the attack is a second path written
into the ledger: a retry that resends only the count, a repair that
writes only receipts. Under T4, every writer the system trusts is
the ledger. So a rule over the ledger's statements reaches every
path the definition allows. A path that makes one move alone fails
the build, naming the file, before it can run.
*Say:* a planted retry lowers the count by R's 3 with no receipt:
rule 3 names it. A planted consume lowers only `reserved`: the new
rule names it, a `consumed` receipt with the count unmoved.

*If the wall were ever wrong:* migration V3, for the units-held
half only, as under G1. It was seen at E1's red, refusing each
split it could see.

*Options weighed:* one move never alone.

**A rule over the ledger's statements, under T4** — *chosen*

*How it holds G4:* every writer the definition trusts is the
ledger. The rule reads every statement it has.

*Cost:* one more rule in E7, and the build parsing the ledger's
statements, as it already does.
*Assumes:* T4. Revisit if anything but the ledger is ever trusted
to write data.

---

**A check in the store at every commit**

*How it holds G4:* the store would refuse a `consumed` receipt
whose item's count did not fall by its units, whoever wrote it.

*Why not:* the store keeps no history of the count to compare
against, because an adjustment sets the count outright. The check
would need a new record of every change to the count, written by
every path, against writers T4 already trusts.

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E4 · G4 | `NoSecondWayOutTest.aConsumedReceiptIsWrittenExactlyWhereTheCountFalls` | the code parsed: a consumed receipt written exactly where the count falls, by the same units |

---

**Inherited, not re-owned.** The attack's answers that SL-1, SL-2
and SL-3 already hold are named, not derived again:
- a consume moves exactly what its reservation holds, once, and
  never after the reservation ended (SL-3 G2 and G4, kills 11, 12,
  17);
- exits racing on one reservation collapse to one (SL-3 G1, kills
  13, 14);
- only the ledger writes the store's data (T4).

**The attack found no fifth guarantee.** Four more answers belong
elsewhere:
- "the store loses one move after saying both were final" is below
  the store's acknowledgment: trusted (T1), its complement fenced
  (W4);
- "the caller is told the wrong thing about an unknown outcome" is
  the caller's view, refused at L2, and ADR-0016 decides what the
  reply says;
- "an interrupted consume makes others on the item wait" is
  waiting, W3 ("Whether anything is left to converge");
- "a reserve's outcome is unknown" is F19 × P2: the orphan (V5).

## Folds and flags — the written zeros

**Folds: none.** SL-4's row carries none. The three folds are
SL-1's (FC1, FC3) and SL-3's (FC2). A consume relies on FC2 through
SL-3 G4.

**Flags: one, answered.** The registry's flag says the evidence is not
hammering: it kills an instance mid-work and injects an unknown
outcome. Both are staged as their own evidence. Neither is removed by
a definition.

- **Kill-mid-work** is E1: an instance killed outright while its
  consume is held mid-work. E2 uses the same hold to put a reader in
  the middle instead of a death.
- **Unknown-outcome injection** is E3: the store frozen mid-consume,
  the caller walking away, the consume left to settle on its own.

## What this slice does not claim

- **That nobody waits.** An interrupted consume can keep its item
  waiting until the store gives up on it ("Whether anything is left to
  converge"). Waiting is W3's.
- **That a network cut with the instance alive is staged.** The
  evidence kills the instance (E1) and freezes the store (E3). A cut
  with the instance alive leaves the store the same thing: an
  unfinished consume that no final word reaches. It differs only in
  how long the store takes to notice, which is the wait above.
- **That what the store acknowledged survives the store.** T1,
  W4.
- **That the caller is told what happened.** ADR-0016's `503` says
  only that the outcome is unknown, even when the instance could
  know nothing happened. The caller's view stays refused at L2.
- **That a reserve with an unknown outcome is safe from a second
  hold.** W2, V5.
- **That release and expiry hold together.** Each makes one move,
  the units held falling. P4 names consume alone as the act that
  moves both numbers.

## The plan, across guarantees

<!-- The second part: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy: database constraint → type system →
     single validated entry path → runtime check → code review →
     hope. The strongest available is chosen, justified against the
     named adversity, not in general. -->

### What stands already

This slice builds almost no wall. SL-3 made consume one statement
in one transaction (SL-3, "How each path runs"), and SL-1 made
every decision on an item one guarded update of its row. The plan's
work is to say which of those walls owns which guarantee here, and
to build the harness that can interrupt a consume on purpose.

What a consume is, as the store sees it, all in one transaction on
the one entry path (`Ledger.consume`, through `Ledger.exit`):

1. **One statement.** Its `WITH` writes R's `consumed` receipt. The
   same statement's `UPDATE item` lowers `on_hand_count` and
   `reserved` by R's units, for the receipt it wrote. Move one is
   the receipt and the units held falling. Move two is the count
   falling. Both of the item's numbers are columns of one row, and
   one update moves them.
2. **The answer's read:** the reservation as persisted.
3. **The commit.** The store's deferred check on the units held
   (migration V3) runs here, and then every write of the transaction
   becomes final at one instant.

*Say:* 10 on hand, R holding 3, 8 held. The statement writes R's
receipt and turns the item row from 10 on hand and 8 held into 7
on hand and 5 held. Until the commit, nobody but this transaction
sees either.


### Escape hatches hunted, against the trust list

- **The ledger's own retries.** None exist. There is no retry in
  the application, and the driver and the pool resend nothing. A
  consume whose statement fails is answered (ADR-0016), never
  redone. E4 names any retry ever written with half a consume.
- **The expiry branch inside an exit.** When a consume meets an
  expired hold, it writes the `expired` receipt and frees the
  units: one move. That is expiry, not consume, so P4 is not
  involved.
- **An adjustment.** It sets the count outright, under SL-2's
  rules, and writes no receipt. It is the operator's count, not a
  consume, and E4's rule names relative lowering only.
- **A migration.** `migrator` writes structure only (T4).
  Migrations V1 to V4 write no rows. A migration that wrote data
  would be outside T4: a question for the definition before it is a
  wall.
- **The store's superuser.** Trusted (T4). The harness uses it to
  hold a row, to end a session, and to read who waits. It writes no
  data.
- **Anyone else holding `runtime`'s password.** W7, fenced: T4
  assumes nobody else writes.

### The surface, at its minimum

- **The door: ADR-0016's answer.** The store out of reach answers
  `503`, "outcome unknown".
  - *As signed:* one handler in `DoorProblems`, classifying the
    failure itself.
  - *As revised, 2026-10-10:* the ledger classifies. Every
    decision's transaction goes through one wrapper in `Ledger`,
    which turns a lost store into a plain problem,
    `StoreOutOfReach`. `DoorProblems` maps that to `503`, as it maps
    `Refused` to `409`.

  SL-1's and SL-2's structural rules refused the signed design. No
  class but `Ledger` may touch the store's APIs (a second writer T4
  does not trust). And no field may hold a collection (state kept in
  memory). Which failures count is a choice with two options:
  - **by the store's own error class — chosen.** SQLSTATE class
    `08` (the connection failed or was lost) and `57P01`–`57P03`
    (the store ending the session, or not accepting it). Nothing
    else: any other failure stays a `500`, a defect.
  - **by the framework's exception types** (a data-access resource
    failure, a transaction-system failure). Rejected: the second of
    those also wraps a refusal at commit, migration V3 saying no.
    That would then read "outcome unknown", though the store
    answered, and undid it.
- **The store:** nothing. No migration, no table, no index.
- **The ledger:** one wrapper around its transactions, and the
  classification (revised 2026-10-10, above). No statement changes.
- **Under test:**
  - `ForkedLedger` gains a kill outright (the process killed, not
    asked to stop).
  - A hold, from outside every instance: the store's superuser
    takes a row the consume needs, and keeps it until the test lets
    go. Two points:
    - the item's row: the consume waits with its receipt written and
      the count not yet moved;
    - R's row: the consume waits with both moves written, at the end
      of its statement, where the receipt's reference to R is
      checked.

    The test knows the consume waits there by reading the store's
    own list of who waits on whom, never by sleeping. The second
    point rests on when the store checks a reference inside a
    `WITH`. The build confirms it. If it does not hold, E1 holds at
    one point, and the evidence as delivered says so.
  - `ThrowawayStore` gains a freeze and a thaw (the container
    paused and resumed, ADR-0004's way). It also gains ending one
    session as the superuser: the way the ADR-0016 check makes an
    instance lose the store.
  - The witness gains one reading of the item's numbers together
    with R's ending, in one statement, so the two cannot be read at
    different moments.
  - E4: a fifth rule in `NoSecondWayOutTest`.
- **Not added:** a time limit anywhere, a repair, a retry, a read
  endpoint.

### The red, planned

The wall stands already, so it is taken away on the working tree,
never in history.

*As signed:* consume split into two transactions (the receipt and
the units held made final, then the count), and each of E1–E3
holding the second transaction back.

*As revised, 2026-10-10:* that split stayed green. Both hold points
sit inside the first transaction: it moves the item's row, and its
receipt is checked against R's row. So the hold caught the consume
before anything was final. What shows red, tried on the working
tree, from actual output:

- **The moves split in either order, and migration V3 absent.**
  Migration V3 refuses each split at its first commit. With
  migration V3 standing, nothing half-done is ever left (G1's
  backstop, seen). With both taken away:
  - *the receipt first:* the hold at the item's row lands between
    the two, and the kill leaves R consumed with the count unmoved;
  - *the numbers first:* the hold at R's row lands between, and the
    kill leaves the count fallen with R still holding: more held
    than on hand.

  Each hold point shows one of F16's two half-states. E2 and E3 use
  the same two red trees.
- **What no hold reaches:** the split migration V3 lets through. That
  is the receipt with the units held, then the count. Both touch the
  item's row, so no hold lands between them. E4 sees it red, by
  planting.

E4 is seen red by planting a half-consume statement. A race whose
red needs its window held open gets it on the red tree only: the
hold is the window, and it is the same on both trees.

### Deviations and provisionals, so the close can see them

- **The plan's surface revised during the build, 2026-10-10.**
  ADR-0016's answer is classified in `Ledger`, not in
  `DoorProblems`. The door touching the store's error types broke
  SL-2's one-writer rule. A set of error codes held in a field broke
  SL-1's no-state rule. Both rules stand unchanged. The design moved
  to fit them.
- **The plan revised during the build, 2026-10-10.** The red as signed
  stayed green: both hold points sit inside the first of two
  transactions. On the working tree only, the red takes away two walls
  instead of one: the single transaction, and migration V3 behind it.
  The evidence as delivered says so for each run. G1's backstop,
  signed as unproven, is now seen. The split migration V3 lets through
  is named as beyond E1's holds, and E4's to catch. No wall, option or
  guarantee changed.
- **G2 reaches past the registry's adversity:** a reader, not a
  death. It was signed in the specification. Its evidence uses the
  same hold as E1's, so it adds no new kind of adversity to the
  harness.
- **The hold at R's row is provisional** on the store's behaviour,
  confirmed in the build. Confirmed 2026-10-09 by the harness's own
  check: while R's row is held, the consume holds the item's row
  already, so it waits with both moves written.

- **The plan revised during the build, 2026-10-09.** G3's owner and
  its chosen option said a killed instance's consume is undone "at
  once". The harness showed the store notices a dead connection only
  when it next speaks to it. A killed instance's consume that is
  waiting on a row stays at the store until its wait ends, and is
  undone then. Nothing about the outcome changed: nothing committed,
  and no half-done state was readable at any moment. Only when the
  store learns of it changed, and that is W3's. No wall, option or
  evidence changed.

## Evidence, as delivered
All of it runs under `./mvnw test`: 100 tests (83 at the branch
point), 0 failures, nothing exported, and the ground need not be
up. Every interruption runs through a forked instance's real door.
The witness is read from the store as `runtime`, from outside every
instance, in one statement: the item's numbers with R's ending. So
no reading can fall either side of a commit. The door's answer is
checked last.

Beside them, and not evidence, each saying so on itself:
- `StoreOutOfReachIT.aConsumeFrozenOutWithItsInstanceAliveFinishesOnItsOwn`:
  the freeze with the instance alive, which cannot go red for the
  invariant;
- `StoreOutOfReachIT.anInstanceThatLosesTheStoreMidConsumeAnswersOutcomeUnknown`:
  a check of ADR-0016's answer;
- `StoreOutOfReachClassifierTest`: a tripwire on the plan's choice,
  ADR-0016 decided by the store's error class, not the framework's
  type;
- `InterruptionHarnessIT`: five checks of the harness itself, before
  any evidence relied on it.

SL-1's, SL-2's and SL-3's evidence ran green unchanged beside all of
it, with every decision's transaction now passing through the
ledger's one wrapper.

**Not run on the real ground.** Nothing in this slice changes the
store: there is no migration. Its adversity is staged by the harness
alone. The ground stayed down.

### Red before green, from actual output

Every wall was made absent on the working tree for one run, then
restored. None landed in history. The wall here is two: the single
transaction, and the store's check on the units held (migration V3)
behind it.

- **The red as signed stayed green:** consume split into the
  receipt with the units held, then the count. Both hold points sit
  inside the first transaction, and the hold caught the consume
  before anything was final (the plan, revised 2026-10-10).
- **E1, the moves split with migration V3 standing:** the store
  refused the first transaction each way, and the consume never
  reached the hold: *nothing came to wait on the row of item …
  within PT30S*. G1's backstop, seen.
- **E1, receipt first, migration V3 absent, held at the item's row:**
  *R ended by consume exactly when the count fell by its 3:
  Numbers[onHandCount=10, held=8, activeSum=5, reservations=2],
  ending=consumed*.
- **E1, numbers first, migration V3 absent, held at R's row:** *the
  invariant: Numbers[onHandCount=7, held=5, activeSum=8,
  reservations=2], ending=null*. More held than on hand.
- **E2, numbers first, migration V3 absent,** both runs on the
  mid-consume reading: *the invariant, mid-consume:
  Numbers[onHandCount=7, held=5, activeSum=8, reservations=2],
  ending=null*. The test stops there, so the reserve's own half is not
  reached on the red tree. The reader that breaks first is the
  witness.
- **E3, receipt first, migration V3 absent:** *R ended by consume
  exactly when the count fell by its 3: Numbers[onHandCount=10,
  held=8, activeSum=5, reservations=2], ending=consumed*. The freeze
  with the instance alive stayed green on the same tree, as its own
  words say.
- **E4, each violation planted in the ledger,** the rule naming the
  statement:
  - a consume freeing R's units and leaving the count;
  - a release also lowering the count;
  - the count lowered alone, which rule three names too;
  - a consume lowering the count by 1: *expected:
    Optional[r.quantity]*.
- **ADR-0016's check,** against the door before its handler:
  *expected: 503 but was: 500*, with the framework's default body.
  **Its tripwire,** with the rejected option planted, deciding by
  the wrapper's type: *aRefusalAtCommitIsNotOutOfReach: Expecting
  value to be false but was true*.

All green, unchanged, with every wall standing: 100 tests.

### What the build found that the specification had not

- **The planned red could not show.** Holds are row locks, and the
  split signed in the plan put both of its first transaction's rows
  under them. Red needs the two moves split *and* migration V3 taken
  away. That migration V3 refused every split it could see is the
  build's best news: the units-held half of G1 has a wall behind it
  in the store.
- **One split no hold reaches.** The receipt with the units held made
  final, then the count. Both touch the item's row, so no hold lands
  between them, and migration V3 does not see it. E4 is all that
  stands there: a rule over the ledger's statements, read at build
  time, under T4. In TODO's known issues.
- **A killed instance's waiting session lingers.** The store learns
  a connection is dead when it next speaks to it. A session waiting
  on a row stays until the wait ends, then is undone. The outcome
  holds, but the words in the plan did not (revised 2026-10-09).
- **The door could not classify the store's failures.** SL-1's and
  SL-2's structural rules refused `DoorProblems` touching the
  store's types or keeping a set of codes. So the ledger classifies,
  and the door maps `StoreOutOfReach` (the plan's surface, revised
  2026-10-10).
- **The harness, twice.**
  - The superuser does not find the ledger's tables without their
    schema named.
  - The container engine closes an idle connection that the client
    then reuses. So a freeze after another failed with *Broken pipe*
    until the request was sent once more.

## Standing guards

What could break this slice later, and what watches for it:

- **Consume split.** Any change that makes consume's two moves two
  statements, two transactions, or two paths.
  - `E4 · G4` fails at build time on the ledger's statements.
  - Migration V3 refuses at commit any split that separates the
    ending from the units held.
  - E1–E3 fail on the witness when both are gone.

  The one split only E4 sees, the count apart from the rest, is why
  E4 stays.
- **A retry or a repair in the ledger.** Anything that finishes,
  repeats or repairs a consume after an unknown outcome is a second
  path (G4's attack). `E4 · G4` names one that moves half. A whole
  second consume path is watched by rule three and E4 together.
- **The wrapper bypassed.** A new decision that opens its own
  transaction, not through `inOneTransaction`, answers a lost store
  with the framework's `500` again. ADR-0016's check covers consume
  only. A new door owes its own.
- **ADR-0016 decided by type.** `StoreOutOfReachClassifierTest`
  fails if a refusal at commit ever reads "outcome unknown".
- **A network between the instances and the store.** The wait an
  interrupted consume leaves grows from a moment to the operating
  system's keepalive. TODO's known issue says when to look again.

## Sign-offs

<!-- Dated lines, the reviewer's: the specification before the plan,
     the plan before the build. -->

- 2026-10-09 — §3's two decisions taken by the reviewer: an outcome
  the ledger cannot know answers `503` (ADR-0016); nothing
  converges, because nothing is half-done, the wait named outside
  the promise.
- 2026-10-09 — the specification (§1–§7) signed by the reviewer as
  written, G2 included: a reader in the middle of a consume, found
  by the attack on "readable", though the registry's adversity
  names only death and unknown outcomes.
- 2026-10-09 — the plan (§8) signed by the reviewer as written, its
  four face choices taken: one statement in one transaction, the
  store's default isolation and the item's row, the store undoing an
  ended session, a rule over the ledger's statements under T4; and
  ADR-0016's answer decided by the store's own error class, not the
  framework's exception types.
- 2026-10-10 — the evidence (§9) certified against the delivered
  files and the suite, 100 tests green; SL-4 closed.
- 2026-10-10 — read against the slice-record shape at the close: §8's
  owners for G2 and G4 now say whether their backstop was seen at the
  red run — G2's never reached, G4's seen. The same guarantees and
  owners; no decision changed.
- 2026-10-13 — three claims rewritten to say what happens, condition
  first (RETRO.md, D14), each checked against its block and its
  tests; G2's already did, and stands. The guarantees, owners and
  evidence are unchanged.
- 2026-10-14 — written plain, no decision changed: one idea per
  sentence, dash and semicolon chains split, lists as lists. Metaphors
  and jargon out ("ran dry", "rot", "G2 dies", "unmakes", "movements",
  "faces", "shape" where it meant a design). The attack's closing line
  says "no fifth guarantee", since four more answers follow.
  "Discharges no kill" cut: the line already says "not evidence". A
  glossary line under the title. The text before this pass is at
  fec6652. The guarantees, owners and evidence are unchanged.
- 2026-10-14 — read against the definition and the registry, no
  decision changed. A reserve's possible orphan is stopped by V5
  alone, as the definition's kill 16 says; "W2, V5" was the
  registry's slip, corrected there too. The registry's flag is
  quoted in its current words. "The trust line" is named, T4.
- 2026-10-14 — read a third time, no decision changed: migration
  V3 reads an item's history at each commit that touches the item,
  not at every commit.
