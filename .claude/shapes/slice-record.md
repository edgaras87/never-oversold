# The shape of a slice record

**Governs:** how a slice record is laid out, and how each
guarantee's block is written. Their form, never their content: what
a guarantee says comes from the attack, and what holds it comes from
the plan.

<!-- A shape: what a kind of output looks like here, its form and
     never its content. Read at a slice's closing gate. How shapes
     live is .claude/rules/shapes-lifecycle.md, and not repeated
     here. How a record reads sentence by sentence is the writing
     rule's (.claude/rules/writing.md). -->

## What this document controls

**The record's order, and the form of a guarantee's block.** A
block this shape accepts can still hold the wrong guarantee; that
is the attack's business, and the specification's. Every other
section is the slice's own, as long as the slice needs.

## The record's order

1. **Invariant.** What the slice protects, in one statement.
2. **Adversity.** What attacks it, and how the evidence creates it,
   for every guarantee alike.
3. **Decided before the specification,** where something was.
4. **Guarantees.** One block per guarantee, below.
5. **Folds and flags:** what the registry handed this slice, each
   answered.
6. **What this slice does not claim.**
7. **The plan, across guarantees:** what the walls need (or
   what stands already), the escape hatches hunted, the surface,
   the red as planned, the deviations and provisionals.
8. **Evidence, as delivered:** the suite as a whole, the tests
   beside the evidence that are not evidence, the red runs, and
   what the build found that the specification had not.
9. **Standing guards:** what keeps the guarantees true after the
   slice closes.
10. **Sign-offs.**

What spans every guarantee has its own section. What belongs to one
guarantee is in its block, and nowhere else.

## A guarantee's block

The block grows by stage. The specification writes the claim, the
attack and the evidence criterion. The plan adds what holds it and
the options weighed. The close adds the tests. The reviewer signs a
stage by reading the parts it added.

```markdown
### SL-<n> G<k>. <the claim, condition first (a when, while or if
clause, or the condition as the subject), then what a test can see
happen>

*What could go wrong (the attack):* <what would let the invariant
hold on paper and break in fact>
*Say:* <real numbers from this system: 8 held, the operator
asserts 7>

*What we guarantee:* <the property that answers it>
*Say:* <the same numbers, carried through>

*Kills:* <n>

- **E<m> (G<k>).** <the adversity the test creates, and what the
  witness must read after it>

*The wall:* <the wall, named: a constraint, a statement, an
absence with the test that guards it>
*Why it beats this attack:* <against the named adversity, not
adversity in general>
*Say:* <real numbers>
*If the wall were ever wrong:* <what stands behind it, or nothing,
said so; and "seen at the red run" or "unproven">

*Options weighed:*

**<the option chosen>** — *chosen*
*How it holds it:* <how>
*Cost:* <what choosing it costs>
*Assumes:* <the size that cost is true at, and what would make the
option worth revisiting>

**<an option not chosen>**
*How it would hold it:* <how>
*Why not:* <why it lost, against the named adversity>

*Proven by:*

| Criterion | Test | The adversity it creates |
|---|---|---|
| E<m> · G<k> | `<Class.method>` | <what it does, with the kill it answers> |

---
```

### Rules for the parts

- **The claim is written once,** at the top of its block. No other
  section restates it.
- **The claim says what happens to what,** in this system's nouns:
  the condition first where there is one, as a clause or as the
  subject ("A refused change moves nothing"), then the outcome a
  test can see. No abstract noun standing for the mechanism ("nonsense",
  "the truth").
- **A `*Say:*` line is owed wherever a mechanism is involved,** and
  not forced where the claim is concrete without one. An option
  carries none: the chosen one's numbers are in *What holds it*, and
  a rejected one was never run.
- **Options are weighed where this slice made the choice.** Where
  the wall already stood, the block names where its options were
  weighed ("in SL-1 G1's block") instead of weighing them again.
- **The chosen option comes first and says so.** Every option
  answers the same questions under the same labels, so options are
  weighed by reading down, not across.
- **A cost says the size it is true at.** An option chosen at one
  size can lose at another; *Assumes:* is where a later reader finds
  when to look again.
- **An evidence criterion is a list item,** so it can hold
  sub-points and name more than one guarantee ("E1 (G1, G2)"). The
  table names the criterion beside each test, since one criterion
  can serve two guarantees.
- **A backstop says whether it was seen.** The red run removes the
  wall, and that is the one moment the backstop meets the attack. A
  backstop never seen there is written "unproven".
- **An option changed after the plan was signed keeps its dates.**
  The new choice comes first, marked `*chosen <date>*`. The one it
  replaced moves below, marked `*as signed on <date>*`, and answers
  under `*Why not, on revision:*`.
- **Blocks are separated by a rule** (`---`).

## What is exempt, and why

- **Lookup tables:** kill to slice, criterion to test, a records
  index. A table row is one line in the file, so its cells stay
  short. A row of prose 435 characters long cannot be read in an
  editor, and a diff marks it whole when one word changes.
- **Lists of single claims:** what the slice does not claim, the
  standing guards.

## What to check at a close

Read the record and ask:

1. Does it follow the record's order, with what spans guarantees in
   its own sections?
2. Is each guarantee one block, its parts in order, and no part of
   it elsewhere?
3. Is each claim written once, condition first, saying what a test
   can see?
4. Does every chosen option come first, with its cost and the size
   that cost assumes?
5. Does every backstop say whether it was seen at the red run?
6. Where the record differs from this shape: did it drift, did it
   find something better, or is it a different kind? The shapes rule
   says how each ends.

## Revisions

<!-- Dated lines: what changed, what taught it, and what was read
     from elsewhere. -->

- 2026-09-22 — written, from SL-2's record. Nothing read from
  elsewhere yet: no shape for this kind has been delivered into
  `temp/`. First use: SL-3's close, which is also its first test.
- 2026-10-01 — the faces weighed for a guarantee join, as blocks,
  and leave the exempt list, where they stood undecided. Taught by
  SL-2 §8's G5 table, whose rows ran to 374 characters on one line.
  Weighed against the table as it was, a short table with blocks
  below, and a numbered list (decisions log, this date). Nothing
  read from elsewhere.
- 2026-10-07 — a face revised after signing carries its dates.
  Taught by SL-3's close, the shape's first reading: §8 had dated
  both faces when the store's check on the units held replaced the
  structural test as G6's guard for the numbers (2026-10-05), and
  the shape had no line for a decision changing mid-build. Each had
  something: the record keeps its labels, and the shape takes them.
  The same reading found G6's kills line and two owners' backstop
  lines missing; there the output had drifted, and the shape
  stands. Nothing read from elsewhere.
- 2026-10-08 — the chosen face's cost gains *Assumes:*, the size it
  is true at and when to revisit; an owner's backstop says whether
  it was seen at the red run, or is unproven. Taught by SL-3 and the
  review after it: G5's backstop, `item_never_oversold`, was named
  and did not hold — seen only because E6's red happened to show it;
  and the check on the units held carried a cost true at the
  evidence's size, unstated beyond it, until the definition gave the
  system a size. From SL-4's record on; closed records are not
  reopened (shapes-lifecycle §5). Nothing read from elsewhere.
- 2026-10-13 — one block per guarantee (RETRO.md, D11 and D12): the
  three shapes (a guarantee, an owner, a face) become parts of one
  block, in the order the stages write them, with the evidence
  criterion and the tests added. The record's order is here too,
  since run 3 has no slice skill outline to carry it. The claim's
  form is condition first (D14). The labels' and the worked
  numbers' explanations leave: the writing rule says them (rules 3
  and 4). Taught by the retrospective: one guarantee was spread over
  five sections. Nothing read from elsewhere.
- 2026-10-14 — cbc-slice now carries the record's skeleton, which
  every run needs before any shape exists. This shape keeps the full
  order anyway: it is the form this project repeats, read at the
  close, and complete without the skill. Nothing read from
  elsewhere.
- 2026-10-14 — "the shape the walls need" becomes "what the walls
  need", the slice records' section name since Step 10's pass 2:
  a shape is a page here, not a design. Nothing read from
  elsewhere.
- 2026-10-14 — the shape was wrong here, found at Step 10's closing
  reading of all four slice records, which agree with each other:
  the wall's label is *The wall:*, matching *If the wall were ever
  wrong*; an evidence criterion is a list item, and the table names
  the criterion beside each test; the chosen option is marked with
  a dash; options are weighed only where the slice made the choice,
  else the block names where; a claim's condition may be its
  subject. Nothing read from elsewhere.
