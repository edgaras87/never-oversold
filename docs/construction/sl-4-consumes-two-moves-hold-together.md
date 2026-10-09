# SL-4 — consume's two moves hold together

<!-- The slice's record, grown in three movements: the correctness
     specification (what must hold, against what, and what proof
     looks like — no mechanism), then the plan (one structural owner
     per guarantee, and why it beats the named adversity), then the
     evidence as delivered (which test creates which adversity, and
     what it read from the store). Invariant and adversity are the
     registry's and the definition's, as written; guarantees are
     derived by attack, never looked up. Sign-offs are the
     reviewer's, dated, at the end of each movement.

     §3 is this slice's own section: two things the framing does
     not decide, decided before the specification that depends on
     them. -->

## §1 Invariant

No readable state holds one of consume's two moves — ending the
reservation, lowering the on-hand-count — without the other.
(`docs/system/registry.md`, SL-4; concern D in the definition's L4.)

The words, as the truth set has them:

- **Consume** is P4's: ending a reservation and lowering the
  on-hand-count together, once per reservation — the one act that
  moves both numbers. The "once" is SL-3's, closed; this slice
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

## §2 Adversity

Our own failure in the middle of a consume. Pulled from the
definition's L1 as written:

- we die between consume's two moves — one move done: the count
  lowered while the reservation still counts, or the reservation
  ended while consumed units still count (F16, kill 15);
- a write's outcome is unknowable — a timeout after sending — and
  we fall silent: the consume neither confirmed nor redone, so
  whatever it left is what stays (F19, kill 16's silence half).

Kills covered: 15, 16 (our silence).

The registry's flag, carried here: the evidence is kill-mid-work and
unknown-outcome injection, not hammering. No race is created; the
adversity is one consume, interrupted.

What this slice does not face: our own retry after an unknown
outcome (kill 16's retry half — SL-3, closed: a repeated consume
moves nothing twice); a reserve whose outcome is unknown (F19 × P2
— the caller's view refused at L2, the possible orphan W2 and V5);
how long anyone waits while a consume is interrupted (W3).

## §3 Decided before the specification

Two questions the framing leaves open, found at this slice's
opening (PLAN, Step 8) and decided by the reviewer on 2026-10-09,
each with its options.

### What a caller is told when the outcome is unknown — ADR-0016

When an instance loses the store mid-consume, the consume may have
committed or not, and the instance cannot tell. The framing refuses
the caller's view (L2), so what the caller reads is the door's
convention, not the promise. Today nothing at the door handles a
lost store, and the framework's default error reads as "failed".

*Say:* 10 on hand, R holding 3. The caller consumes R. The store
commits — 7 on hand, R consumed — and the connection breaks before
the instance hears. Today the caller reads an error, and believes R
is still open.

**Chosen:** the instance answers `503`, Problem Details, "outcome
unknown": the request may or may not have taken effect. The caller
sends the consume again, and ADR-0013 makes that safe — `200` if
the first landed, the consume done now if it did not, refused if
another exit ended R meanwhile.
*Say:* the caller reads `503`, sends consume R again, and reads
`200` with R consumed. The item still reads 7 on hand: one consume,
not two.

**Rejected:** leaving the default error (it says "failed" for a
consume that may have landed); `500` (it would read the same as a
defect in the ledger); and a limit on how long an instance waits
for the store (the numbers are right however long the wait; a limit
brings the unknown sooner but does not remove it; and the number is
a policy the framing never gives — W3). The options and reasons in
full are ADR-0016's.

### Whether anything is left to converge

The registry kept SL-4 apart from SL-3 because the proof
obligations differ: duplicates collapse there, and here "the
half-done converges". That was written at framing, before any
consume existed, when the two moves could have been two writes,
with a half-done state left for something to repair.

SL-3 built consume as one write: the reservation's ending and the
count's fall are committed together, or not at all (SL-3 §8). An
interrupted consume leaves the store as it was before, or as it is
after — never between.

**Chosen: nothing converges, because nothing is half-done.** The
spec says so, and the evidence shows it: an interrupted consume
reads as both moves or neither. Convergence is met at its strongest
— there is never anything to converge from. The registry's
judgment still stands, because the evidence differs from SL-3's:
an interruption, not duplicates.

**Named, outside the promise: the wait an interrupted consume can
leave.** A consume whose instance is cut off from the store — not
killed, cut off — can stay open at the store until the store gives
up on that connection. Until then, other decisions on that item
wait. None reads a wrong number; they only wait, and how long is
W3's, fenced.
*Say:* an instance has run its consume of R on item X, and loses
its network to the store before its commit arrives. A reserve on
item X from another instance waits. When the store gives up on the lost connection, it undoes
the open consume — 10 on hand, R holding 3 — and the reserve
decides against those numbers.

What the decision asks of the evidence: after the interruption, the
next decision on the same item goes through, against the numbers
the witness reads. Nothing is stuck.

**Rejected: limiting that wait in this slice.** It would be a
setting on the store, a change to the ground, made for no
guarantee. Our ground cannot readily stage it either: the long wait
needs a network cut while the instance lives on, and a killed
instance's connection is closed at once. Recorded in the backlog
as a known issue, with when to revisit.

## §4 Guarantees — derived by attack

Each is one answer to: *what would let §1 hold on paper yet break
in fact?* The attack runs until no new answer comes. Every
guarantee is a property; none names how it is held.

The numbers in the examples start from one item unless they say
otherwise: 10 on hand; reservation R holds 3, reservation S holds
5, so 8 are held and 2 are free. A consume of R should leave 7 on
hand, 5 held.

**G1. A consume's two moves become final together, or neither
does.**

*What could go wrong (the attack):* the two moves are made final
one after the other, and we die in between (F16).
*Say:* R's ending is final, and we die before the count falls.
The item reads 10 on hand and 5 held, so 5 look free — but 3 of
the 10 were sold and have left the shelf. Five more are admitted:
10 held against the 7 really there. Or the other way round: the
count falls to 7 and we die before R ends. R still holds 3 and has
no ending, so the caller's retry consumes it again: 4 on hand, one
hold's units taken off the shelf twice.

*What we guarantee:* there is no moment at which one of a consume's
moves is final and the other is not. However the consume is
interrupted, and wherever, what remains is both moves or neither.
*Say:* we die mid-consume. The item reads either 10 on hand, R
holding 3, 8 held — or 7 on hand, R ended by consume, 5 held.
Nothing else.

*Kills:* 15.

---

**G2. While a consume is under way, everyone who reads sees it
whole or not at all.**

*What could go wrong (the attack):* both moves become final
together, but a reader looks while the consume is under way, and
sees one move and not yet the other. "Readable" is every state
readable from outside, not only the states left behind — and the
reader that matters most is another decision on the same item.
*Say:* a consume of R is under way. A reserve of 5 on the same
item reads R as ended, 5 held, but the count still at 10: 5 look
free, and it is admitted. Then the consume finishes: 7 on hand, 10
held. Oversold by 3, though no one died.

*What we guarantee:* a reader — a decision, the witness, anyone —
sees a consume either before it or after it, never in the middle.
A decision taken on the same item while a consume is under way
decides against one whole state, before or after, never a mix.
*Say:* the reserve of 5 decides against 10 on hand and 8 held, and
is refused; or against 7 on hand and 5 held, and is refused. Either
way the item ends at 7 on hand, 5 held.

*Kills:* 15 (the readable half-state, met by a reader rather than
left by a death).

---

**G3. An interrupted consume settles on its own.**

*What could go wrong (the attack):* the consume's outcome is
unknown (F19), and then nothing more happens: the caller does not
send it again, and the instance that sent it is gone or has given
up. If anything were still needed — a retry, a repair, a person —
to bring the consume to both moves or neither, then in our silence
it would stay half-done.
*Say:* the store stops answering mid-consume. The caller's request
times out, and the caller walks away. Nobody ever sends consume R
again.

*What we guarantee:* an interrupted consume reaches both moves or
neither with no further request, retry, repair or person. Once it
has, the next decision on the item goes through against that
state; nothing is left stuck (§3).
*Say:* the store answers again. With no one sending anything, it
reads 10 on hand with R holding 3, or 7 on hand with R consumed.
A reserve of 2 then decides against those numbers, and fits
either way: 10 held of 10 on hand, or 7 held of 7.

*Kills:* 16 (our silence).

---

**G4. Nothing makes one of consume's moves without the other.**

*What could go wrong (the attack):* the consume itself holds
together, but another path makes one move alone — a recovery step
that redoes the count's fall after an unknown outcome, a repair
that ends reservations it thinks were consumed, a later feature
that lowers the count "for a sale".
*Say:* after an unknown outcome, a well-meant retry inside the
ledger lowers the count by R's 3 again, without looking at R. 4 on
hand, R consumed once.

*What we guarantee:* the count falls for a consume only together
with that reservation's ending by consume, and a reservation ends
by consume only together with the count's fall — on every path,
including paths this slice never anticipated.
*Say:* there is one way to consume R, and it does both.

*Kills:* none by number. The attack is on the walls themselves: a
second way in that makes one move alone, bringing kill 15's
half-state back without any death.

---

**Inherited, not re-owned.** The attack's answers that SL-1, SL-2
and SL-3 already hold are named, not re-derived. A consume moves
exactly what its reservation holds, once, and never after the
reservation ended (SL-3's G2 and G4, kills 11, 12, 17). Exits
racing on one reservation collapse to one (SL-3's G1, kills 13,
14). Only the ledger writes the store's data (T4).

**The attack ran dry at four.** A fifth answer — "the store loses
one move after saying both were final" — is below the store's
acknowledgment: trusted (T1), its complement fenced (W4). A sixth —
"the caller is told the wrong thing about an unknown outcome" — is
the caller's view, refused at L2; ADR-0016 decides what the reply
says. A seventh — "an interrupted consume makes others on the item
wait" — is waiting, W3 (§3). An eighth — "a reserve's outcome is
unknown" — is F19 × P2, the orphan W2 and V5.

## §5 Evidence criteria

Each guarantee's evidence *creates* its adversity through the real
door and reads the witness from the store, from outside every
instance. The witness is §1's, read in one reading for the item and
for each reservation on it: the on-hand-count; the units held, and
the sum of active reservations; and how each reservation ended, if
it has. From these the evidence checks two things: the on-hand-count
has fallen by exactly the units of the reservations that ended by
consume, once each; and no more is held than is on hand.

The registry's flag decides the shape: no hammering. The adversity
is one consume, interrupted, and the harness interrupts it on
purpose — it holds the consume in the middle of its work, so the
interruption lands there and not before or after by luck.

- **E1 (G1).** An instance killed outright — not stopped — while
  its consume is held in the middle of its work. Repeated at every
  point the harness can hold it. After each: the witness reads both
  moves or neither; never R ended with the count unmoved, never the
  count lowered with R unended.
- **E2 (G2).** A consume held in the middle of its work. While it
  is held: the witness reads the item, and a reserve on the same
  item is sent from another instance. Then the consume is let go.
  The witness, read while held, shows the before-state whole; the
  reserve decides against a whole state; after, the witness shows
  the after-state whole, and no more held than on hand.
- **E3 (G3).** The store frozen while a consume is in the middle
  of its work. The caller gives up, and nothing else is sent —
  no retry, no repair. The store is let go; with no request since,
  the witness reads both moves or neither. Then a reserve on the
  same item goes through, decided against the numbers the witness
  read. Beside it, not evidence: an instance that loses the store
  mid-consume answers as ADR-0016 says — `503`, "outcome unknown".
  That checks the door's convention, not the promise; it says so
  on itself and discharges no kill.
- **E4 (G4).** A structural check, reading the code: no path lowers
  the count for a consume without ending the reservation in the
  same act, or ends one by consume without lowering the count —
  seen red by planting a path that makes one move alone.
- **SL-1's, SL-2's and SL-3's evidence, unchanged and green.**
- **The harness can fail (R5).** Each of E1–E4 is run with its wall
  absent — the two moves made final apart, on the working tree,
  never in history — and seen red, failing on the witness and not
  on a status or a crash, recorded from actual output; then green,
  unchanged, with the wall standing.

All of it under the one standard test command, nothing exported,
the ground not required up.

## §6 Folds and flags — the written zeros

**Folds: none.** SL-4's row carries none. The three folds are
SL-1's (FC1, FC3) and SL-3's (FC2); a consume relies on FC2 through
SL-3's G4.

**Flags: one, answered.** The registry's flag says the evidence is
kill-mid-work and unknown-outcome injection, not hammering. Both
are staged as their own evidence; neither is removed by a
definition.

- **Kill-mid-work** is E1: an instance killed outright while its
  consume is held mid-work. E2 uses the same hold to put a reader in
  the middle instead of a death.
- **Unknown-outcome injection** is E3: the store frozen mid-consume,
  the caller walking away, the consume left to settle on its own.

## §7 What this slice does not claim

- **That nobody waits.** An interrupted consume can keep its item
  waiting until the store gives up on it (§3). Waiting is W3's.
- **That a network cut with the instance alive is staged.** The
  evidence kills the instance (E1) and freezes the store (E3). A
  cut with the instance alive leaves the store the same thing — an
  unfinished consume no final word reaches — and differs only in
  how long the store takes to notice, which is the wait above.
- **That what the store acknowledged survives the store.** T1,
  W4.
- **That the caller is told what happened.** ADR-0016's `503` says
  only that the outcome is unknown, even when the instance could
  know nothing happened; the caller's view stays refused at L2.
- **That a reserve with an unknown outcome is safe from a second
  hold.** W2, V5.
- **That release and expiry hold together.** Each makes one move —
  the units held fall — and P4 names consume alone as the act that
  moves both numbers.

## §8 Plan — one structural owner per guarantee

<!-- The second movement: mechanisms are allowed here and nowhere
     above. Each guarantee gets exactly one owner from the
     enforcement hierarchy — database constraint → type system →
     single validated entry path → runtime check → code review →
     hope — the strongest available, justified against the named
     adversity, not in general. -->

### What stands already

This slice builds almost no wall. SL-3 made consume one statement
in one transaction (SL-3 §8, "How each path runs"), and SL-1 made
every decision on an item one guarded update of its row. The plan's
work is to say which of those walls owns which guarantee here, and
to build the harness that can interrupt a consume on purpose.

What a consume is, as the store sees it today, all in one
transaction on the one entry path (`Ledger.consume`, through
`Ledger.exit`):

1. **One statement.** Its `WITH` writes R's `consumed` receipt;
   the same statement's `UPDATE item` lowers `on_hand_count` and
   `reserved` by R's units, for the receipt it wrote. Move one is
   the receipt and the units held falling; move two is the count
   falling. Both of the item's numbers are columns of one row, and
   one update moves them.
2. **The answer's read** — the reservation as persisted.
3. **The commit.** The store's deferred check on the units held
   (V3) runs here, and then every write of the transaction becomes
   final at one instant.

*Say:* 10 on hand, R holding 3, 8 held. The statement writes R's
receipt and turns the item row from 10 on hand and 8 held into 7
on hand and 5 held. Until the commit, nobody but this transaction
sees either.

### Owners

**G1. A consume's two moves become final together, or neither
does.**

*The wall:* the transaction — one statement, one commit. The store
makes all of a transaction's writes final at its commit, and a
transaction that never commits leaves nothing (T1; ADR-0005 chose
the store for exactly this).

*Why it beats this attack:* the attack needs a moment at which one
move is final and the other is not. Inside one transaction there
is no such moment: before the commit neither is final, after it
both are. A death at any point before the commit — mid-statement,
between the statement and the commit — ends the session, and the
store undoes every write in it.
*Say:* the instance is killed while the receipt is written and the
item row still waits. The store undoes the receipt: 10 on hand, R
holding 3, 8 held.

*If the wall were ever wrong:* partly. The check on the units held
(V3) refuses a receipt made final without the units held falling.
Nothing stands behind the count's half: a count lowered with no
receipt, or a receipt with the count unmoved, passes every check
the store has. E4's rule is the early warning there (G4). Whether
V3 is seen catching anything at the red run is recorded in §9;
until then it is unproven here.

---

**G2. While a consume is under way, everyone who reads sees it
whole or not at all.**

*The wall:* the store shows no reader a write before its commit,
and a decision on an item waits for the item's row while a consume
holds it. The decision reads the two numbers it compares from that
one row, in one update (SL-1's guarded update).

*Why it beats this attack:* the attack needs a reader to see one
move and not the other. A reader that is not deciding sees the
store as of before the consume's commit, or after it. A reserve on
the same item cannot read past a consume at all: its update waits
for the row, and when the row is free, the store re-reads it and
the reserve decides against what is there then — the before-state
if the consume was undone, the after-state if it committed.
*Say:* a consume of R is under way, holding the row at 7 on hand,
5 held, uncommitted. A reserve of 5 waits. The consume commits; the
reserve reads 7 and 5, 2 free, and is refused. Or the consume is
undone; the reserve reads 10 and 8, 2 free, and is refused.

*If the wall were ever wrong:* `item_never_oversold`, the store's
check that the units held never pass the count, refuses a decision
that would hold more than is on hand — the promise's own half, not
the consume's. A reserve that saw half a consume could fit and pass
it. Unproven until the red run.

---

**G3. An interrupted consume settles on its own.**

*The wall:* the store itself. A session that ends without a commit
is undone by the store; a commit the store acknowledged stays
(T1). No path in the ledger finishes, repeats or repairs a consume,
and G4's rule keeps it so.

*Why it beats this attack:* the attack needs something to be owed
after the interruption — a retry, a repair, a person. Nothing is.
The store decides alone, and only between two answers: the commit
arrived, or it did not. When the instance is killed, its
connection closes and the store undoes the consume at once. When
the store was frozen, it resumes the moment it is let go, and does
the same.
*Say:* the store freezes mid-consume; the caller walks away. The
store is let go. Whatever it decides — the commit arrived, 7 on
hand and R consumed; or the instance is gone, 10 on hand and R
holding 3 — no request since has been needed.

*If the wall were ever wrong:* nothing. This is T1, trusted.

---

**G4. Nothing makes one of consume's moves without the other.**

*The wall:* the trust line and a rule over the ledger's code. Only
the ledger writes the store's data (T4). SL-3's structural check
(`NoSecondWayOutTest`, E7) already fails the build if a number
falls in a statement that writes no receipt. A fifth rule, this
slice's E4: a statement writes a `consumed` receipt if and only if
it lowers `on_hand_count` by the same units; and nothing else
lowers `on_hand_count` relative to itself.

*Why it beats this attack:* the attack is a second path written
into the ledger — a retry that resends only the count, a repair
that writes only receipts. Under T4 every writer the system trusts
is the ledger, so a rule over the ledger's statements reaches
every path the definition allows. A path that makes one move alone
fails the build, naming the file, before it can run.
*Say:* a planted retry lowers the count by R's 3 with no receipt:
rule 3 names it. A planted consume that lowers only `reserved`:
the new rule names it — a `consumed` receipt with the count
unmoved.

*If the wall were ever wrong:* V3, for the units-held half only, as
under G1.

---

### The faces chosen, and the ones not

**The two moves, made final (G1)**

**One statement in one transaction** — *chosen*

*How it holds G1:* both moves are one update of one row, beside
the receipt, all made final by one commit.

*Cost:* nothing new — SL-3 built it. A consume holds its item's
row from its update to its commit: one statement, one read, and
V3's read of the item's history.
*Assumes:* the definition's size — up to a few thousand
reservations on one item over its life, which V3 reads at every
commit through V4's index. Revisit with V3's known issue: when an
item's history passes that size, or a decision's speed is
measured.

---

**Two statements in one transaction**

*How it holds G1:* the same commit makes both final.

*Why not:* no gain, and it unmakes SL-3's wall: rule 3 of E7 fails
any statement that lowers a number without writing the receipt.
And the row is held longer.

---

**Two transactions, and a repair that finishes the half-done**

*How it holds G1:* it does not; it converges afterwards.

*Why not:* the half-done is readable between the two (G2 dies),
and the repair is a second path that makes one move alone (G4).
§3 decided there is nothing to converge.

---

**What a reader sees mid-consume (G2)**

**The store's default isolation, and the item's row** — *chosen*

*How it holds G2:* no uncommitted write is seen; a decision waits
for the row and re-reads it.

*Cost:* nothing new. A reserve on an item waits while a consume of
the same item is under way, for as long as the consume runs.
*Assumes:* a consume's run is short — one statement and a commit;
an interrupted one can keep the row longer (§3, W3, the known
issue).

---

**Serializable isolation on every decision**

*How it holds G2:* the store refuses any decision whose reads a
concurrent commit has made stale.

*Why not:* it holds nothing more here — the numbers a decision
compares are one row — and a refused decision has to be sent
again, which is a retry path in the ledger (G4's attack).

---

**An interrupted consume settles (G3)**

**The store undoes an ended session** — *chosen*

*How it holds G3:* nothing has to happen after the interruption;
the store decides between committed and undone by itself.

*Cost:* nothing to build. The wait until the store notices the
session has ended.
*Assumes:* the end reaches the store promptly — at once for a
killed process on the same machine, as on this ground. Revisit
when a network sits between the instances and the store (the known
issue: a cut connection can stay open for hours).

---

**A limit at the store on a session left open mid-transaction**

*How it holds G3:* the same, with the wait bounded.

*Why not:* §3 — a ground change made for no guarantee; waiting is
W3's.

---

**A repair in the ledger**

*How it holds G3:* something finds half-done consumes and finishes
them.

*Why not:* there are none to find, and it would be a second path
(G4).

---

**One move never alone (G4)**

**A rule over the ledger's statements, under T4** — *chosen*

*How it holds G4:* every writer the definition trusts is the
ledger; the rule reads every statement it has.

*Cost:* one more rule in E7, and the build parsing the ledger's
statements, as it already does.
*Assumes:* T4. Revisit if anything but the ledger is ever trusted
to write data.

---

**A check in the store at every commit**

*How it holds G4:* the store would refuse a `consumed` receipt
whose item's count did not fall by its units, whoever wrote it.

*Why not:* the store keeps no history of the count to compare
against — an adjustment sets the count outright. The check would
need a new record of every movement of the count, written by every
path, against writers T4 already trusts.

---

### Escape hatches hunted, against the trust list

- **The ledger's own retries.** None exist: no retry in the
  application, and the driver and the pool resend nothing. A
  consume whose statement fails is answered (ADR-0016), never
  redone. E4 names any that is ever written with half a consume.
- **The expiry branch inside an exit.** When a consume meets an
  expired hold, it writes the `expired` receipt and frees the units
  — one move. That is expiry, not consume: P4 is not involved.
- **An adjustment.** It sets the count outright, under SL-2's
  rules, and writes no receipt. It is the operator's count, not a
  consume, and E4's rule names relative lowering only.
- **A migration.** `migrator` writes structure only (T4); V1–V4
  write no rows. A migration that wrote data would be outside T4 —
  a question for the definition before it is a wall.
- **The store's superuser.** Trusted (T4). The harness uses it to
  hold a row, to end a session, and to read who waits; it writes
  no data.
- **Anyone else holding `runtime`'s password.** W7, fenced: T4
  assumes nobody else writes.

### The surface, at its minimum

- **The door: ADR-0016's answer.** One handler in `DoorProblems`:
  the store out of reach answers `503`, "outcome unknown". Which
  failures count is a choice with two faces:
  - **by the store's own error class — chosen.** SQLSTATE class
    `08` (the connection failed or was lost) and `57P01`–`57P03`
    (the store ending the session, or not accepting it). Nothing
    else: any other failure stays a `500`, a defect.
  - **by the framework's exception types** (a data-access resource
    failure, a transaction-system failure). Rejected: the second of
    those also wraps a refusal at commit — V3 saying no — which
    would then read "outcome unknown" though the store answered,
    and undid it.
- **The store:** nothing. No migration, no table, no index.
- **The ledger:** nothing.
- **Under test:**
  - `ForkedLedger` gains a kill outright (the process killed, not
    asked to stop).
  - A hold, from outside every instance: the store's superuser
    takes a row the consume needs, and keeps it until the test lets
    go. Two points: the item's row — the consume waits with its
    receipt written and the count not yet moved; and R's row — the
    consume waits with both moves written, at the end of its
    statement, where the receipt's reference to R is checked. The
    test knows the consume waits there by reading the store's own
    list of who waits on whom, never by sleeping. The second point
    rests on when the store checks a reference inside a `WITH`; the
    build confirms it, and if it does not hold, E1 holds at one
    point and §9 says so.
  - `ThrowawayStore` gains a freeze and a thaw (the container
    paused and resumed, ADR-0004's way), and ending one session as
    the superuser — the way the ADR-0016 check makes an instance
    lose the store.
  - The witness gains one reading of the item's numbers together
    with R's ending, in one statement, so the two cannot be read at
    different moments.
  - E4: a fifth rule in `NoSecondWayOutTest`.
- **Not added:** a time limit anywhere, a repair, a retry, a read
  endpoint.

### The red, planned

The wall stands already, so it is taken away on the working tree,
never in history: consume split into two transactions — the
receipt and the units held made final, then the count. Each of
E1–E3 then holds the second transaction back and lands its
interruption there; the witness reads R consumed with the count
unmoved. E4 is seen red by planting a half-consume statement. A
race whose red needs its window held open gets it on the red tree
only: the hold is the window, and it is the same on both trees.

### Deviations and provisionals, so the close can see them

- **G2 reaches past the registry's adversity** — a reader rather
  than a death. Signed in the specification; its evidence uses the
  same hold as E1's, so it adds no new kind of adversity to the
  harness.
- **The hold at R's row is provisional** on the store's behaviour,
  confirmed in the build.

## §11 Sign-offs

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
