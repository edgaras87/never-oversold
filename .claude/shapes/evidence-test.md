# The shape of an evidence test

**Governs:** how an evidence test is written: the first line of its
comment, the order of its assertions, and the labels on them. Its
form only. Which adversity a test creates, and which guarantee it
answers, come from the slice record.

<!-- A shape: what a kind of output looks like here, its form and
     never its content. Read at a slice's closing gate. How shapes
     live is .claude/rules/shapes-lifecycle.md, and not repeated
     here. When a red run counts is cbc-slice's build gate, not this
     shape's. -->

## What this document controls

**Three kinds of test.** Each sits beside a slice's evidence:
- a test that creates an adversity at the door and reads the
  witness;
- a structural test, which reads the code;
- a tripwire, which pins a decision and is not evidence.

A test this shape accepts can still create the wrong adversity.
That is the slice record's business.

**Not covered: a test of one wall on its own,** such as a catalog
check, or the store refusing a statement sent to it directly. It
says so at its class, and its form is its own.

**Two layers, marked apart.**
- *Skeleton:* what any project proving an invariant by tests would
  want, whatever its stack.
- *Illustration:* this project's form of it: its classes, its
  numbers, its door. Every code block below is illustration.

A reader from another project takes the skeleton and writes their
own illustration.

## The shape

### The first line says what the test is

```java
/**
 * SL-3 E3 · G2 — kill 12: the reply is lost, the caller consumes again.
 *
 * <p>Consume R, then the same consume again. The count moves once: 7
 * on hand, 5 held, after both. <what the door answers, and why>
 * This fails if <what would trip it, in the same numbers>.
 */
```

A tripwire says it is not evidence, and names the decision it pins:

```java
/**
 * Not evidence — a tripwire for the choice in SL-3's record, "When an
 * expired hold's units are free", at the reserve: <the decision it
 * pins>. It guards the decided face, not the promise, and discharges
 * no kill.
 */
```

*Skeleton:*

- **The first line names what the test is.** For evidence: the
  slice, the criterion, the guarantee, and the kill where it answers
  one (a test for a fold or a structural rule may answer none). For
  a tripwire: "not evidence", and the decision it pins.
- **Then the story in plain words,** with the numbers it runs on,
  and what would make it fail.
- **The class says where its slice record is,** and which numbers
  its tests start from.

*Illustration here:* `SL-<n> E<m> · G<k> — kill <j>`, numbered as
in the slice record. The slice is named because a test sits
outside its record (the writing rule, rule 6).

### The order of its assertions

```java
ResponseEntity<String> answer = consume(scene.r());

Witness.Numbers after = Witness.read(scene.item());
assertThat(after.holds()).as("the invariant: %s", after).isTrue();
assertThat(after.onHandCount()).as("moved once, not twice: %s", after).isEqualTo(7);
assertThat(after.held()).as("moved once, not twice: %s", after).isEqualTo(5);
assertThat(Witness.endingOf(scene.r())).isEqualTo("consumed");

assertThat(answer.getStatusCode()).isEqualTo(HttpStatus.OK);
assertThat(Body.of(answer.getBody()).stringAt("$.endedAt")).isEqualTo(...);
```

*Skeleton:*

1. **The witness first.** Read it from where the truth is stored,
   never from the replies. When two of its numbers must agree with
   each other, read them in one read.
2. **Then the promise,** on every reading taken after the
   adversity, sampled readings included. A reading taken before the
   adversity only checks the scene is set; it owes nothing.
3. **Then the slice's own invariant,** where it differs from the
   promise.
4. **Then the exact numbers,** then the state of each thing the
   adversity touched.
5. **The door's answer last.**

*Illustration here:*
- **The witness:** `Witness.read`, plain JDBC as `runtime`, from
  outside every instance. `Witness.read(item, reservation)` reads
  the item's numbers and one reservation's ending in one statement.
- **The promise:** `Witness.Numbers.holds()`.
- **A slice's own invariant:** SL-4's is "R ended by consume exactly
  when the count fell by its units", asserted as two booleans that
  must be equal.
- **Each reservation's state:** `Witness.endingOf`.
- **The answer:** the status, then the body by JSON path.

### How its assertions read

*Skeleton:*

- **A label prints the reading,** so a failure shows the whole
  state, not one number. Where the reading is the one value the
  assertion prints anyway, the label names it and adds nothing.
- **An answer's fields by their path, never by a substring.** A
  missing field by its name, never by a null.
- **The record's example numbers,** when the test tells the
  record's story, so the two can be read side by side.
- **A helper that may be refused returns the answer and asserts
  nothing.** The helper a scene relies on calls it, and asserts.

*Illustration here:*
- **A label:** `.as("<what this number means>: %s", after)`.
- **Fields:** `Body.of(…).intAt("$.quantity")`, and
  `Body.names("endedBy")` for a missing one.
- **The example numbers:** 10 on hand, R holding 3, S holding 5
  (SL-3's record, "Guarantees").
- **A helper:** `tryReserve` beside `reserve`.

### Why each rule is there

- **The witness before the answer.** A test that fails on a status
  code proves it reads status codes. SL-2's first red failed on
  "expected 409 CONFLICT but was 200 OK", and never read the store.
- **The promise before the exact numbers.** The numbers can be
  right for the story while the state is wrong somewhere else.
- **The slice's invariant beside the promise.** A slice's invariant
  can be narrower than the promise. R ended with the count unmoved
  holds no more than is on hand: the promise holds, and SL-4's
  invariant is broken.
- **One read for what must agree.** Two reads can each be true and
  still not belong together: the item read before a commit, the
  reservation after it.
- **Every test says what it is,** so nobody has to ask what it is
  for, and a tripwire is never counted as a kill.
- **The reading in the labels,** so a failure read from the output
  alone says what the state was.

## What is exempt, and why

- **A wall's own checks:** a catalog test, or the store's refusals
  sent directly. They say *not evidence* at their class. They
  discharge no kill and need no witness.
- **Infrastructure tests:** health, the migration path, the
  harness's own checks.

## What to check at a close

Read the slice's tests and ask:

1. Does every test's first line say what it is?
2. On every reading after the adversity: is the witness asserted
   before the answer, and the promise (then the slice's invariant,
   where it differs) before the exact numbers? Is what must agree
   read in one read?
3. Does every label print the reading?
4. Where a test differs from this shape: did it drift, did it find
   something better, or is it a different kind of test? The shapes
   rule says how each ends.

## Revisions

<!-- Dated lines: what changed, what taught it, and what was read
     from elsewhere. -->

- 2026-10-07 — written from SL-2's `CorrectionIT` and SL-3's
  `ExitDoorIT`, `ExitStormIT` and `NoSecondWayOutTest`, and exposed
  at once on the reviewer's word (2026-10-03, SL-3's third commit):
  there, four tests lacked the promise check and two the ending,
  and the first red failed on a `500` — SL-2's "worthless red"
  repeated, its lesson written in SL-2's record where nobody looks
  while writing a test. SL-1's assertion convention (bodies by JSON
  path, the witness by plain JDBC) was likewise in SL-1's record
  only. The skeleton is marked apart from this project's
  illustration (shapes-lifecycle §4), so it can be offered. Nothing
  read from elsewhere.
- 2026-10-07 — the promise is owed on every reading taken after the
  adversity, where it said every reading that can move numbers.
  Taught by SL-3's close, the shape's first reading: five readings
  in `ExitDoorIT` and `ExitStormIT` confirm the scene before the
  adversity — `T took the units R's expiry freed` — and check no
  promise. A setup reading is not evidence; the shape was wrong
  here, and the tests stand. Nothing read from elsewhere.
- 2026-10-10 — the slice's own invariant is asserted after the
  promise and before the exact numbers, where it is not the promise
  itself; and a reading whose parts must agree is taken in one read.
  Taught by SL-4's close, the shape's second reading: its E1–E3
  assert "R ended exactly when the count fell", which the promise
  alone cannot see — R ended with the count at 10 holds no more than
  is on hand — and read the item's numbers and R's ending in one
  statement, so no reading straddles the commit they test. Each had
  something: the tests stand, and the shape takes both. Nothing read
  from elsewhere.
- 2026-10-13 — moved from `.claude/rules/` to `.claude/shapes/`: read
  at the gate only, no longer loaded while a test is written
  (RETRO.md, D18). The red-run rule it repeated, and its close
  question, are left to cbc-slice's build gate, which says them
  already. Nothing read from elsewhere.
- 2026-10-13 — written plain (RETRO.md, D2), and its illustration
  follows the labels the tests now carry (D13): the first line names
  its slice, "SL-3 E3 · G2 — kill 12", and links go by label, never
  by section number. The rules are unchanged. One reason gained its
  example (SL-2's first red). Nothing read from elsewhere.
- 2026-10-14 — the first line names the kill where the test answers
  one, since a test for a fold or a structural rule may answer none;
  a label prints the reading where it holds more than the asserted
  value. Found at Step 10's closing reading of the tests it touched.
  Nothing read from elsewhere.
