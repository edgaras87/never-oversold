---
paths:
  - "src/test/**"
---

# The shape of an evidence test

**Governs:** how a test that stands as a slice's evidence is
written — what it says on itself, the order it asserts in, and how
its assertions read. Its form, never its content: which adversity it
creates and which guarantee it answers come from the slice record.

<!-- A shape: what a kind of output looks like here, its form and
     never its content. This one is exposed — it loads whenever a test
     is touched — because what it carries had been written down before,
     inside slice records nobody opens while writing a test, and was
     repeated anyway. How shapes live is
     .claude/rules/shapes-lifecycle.md, and not repeated here. -->

## What this document controls

**The form of an evidence test, and nothing else.** A test this
document accepts can still create the wrong adversity; that is the
slice record's business. It covers the tests that stand as evidence
or beside it — the ones that create an adversity at the door and read
the witness, the structural ones that read the code, and the
tripwires. A test of a wall on its own (a catalog check, the store's
refusals shown directly) says so at its class and is otherwise its
own.

**Two layers, marked apart.** *Skeleton* — what any project proving
an invariant by tests would want, whatever its stack. *Illustration*
— this project's form of it: its classes, its numbers, its door. A
reader from elsewhere takes the skeleton and writes their own
illustration; the code blocks below are all illustration.

## The shape

### What it says on itself

```java
/**
 * E3 · G2 — kill 12: the reply is lost, the caller consumes again.
 *
 * <p>Consume R, then the same consume again. The count moves once: 7
 * on hand, 5 held, after both. <what the door answers, and why>
 * This fails if <what would trip it, in the same numbers>.
 */
```

or, for a test that guards a decided face rather than the promise:

```java
/**
 * Not evidence — a tripwire for §3's choice, at the reserve: <the
 * decision it pins>. It guards the decided face, not the promise,
 * and discharges no kill.
 * ...
 */
```

*Skeleton:*

- **The first line names what it is:** the criterion, the guarantee
  and the kill it answers — or that it is not evidence but a
  tripwire, and which decision it pins.
- **Then the story in plain words,** with the numbers it runs on,
  and what would trip it.
- **The class says where the record is** and which numbers its
  tests start from.

*Illustration here:* the label reads `E<n> · G<n> — kill <k>`, the
criteria and guarantees numbered as in the slice record.

### The order it asserts in

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

1. **The witness first,** read from where the truth persists, never
   from replies.
2. **The promise on every reading taken after the adversity,**
   before any exact number, sampled readings included. A reading
   that only confirms the scene is set, before the adversity, is
   not evidence and owes it nothing.
3. **Then the exact numbers,** then the state of each thing the
   adversity touched.
4. **The door's answer last.**

*Illustration here:* the witness is `Witness.read`, plain JDBC as
`runtime` from outside every instance; the promise is
`Witness.Numbers.holds()`; each reservation's state is
`Witness.endingOf`; the answer is the status, then the body by JSON
path.

### How its assertions read

*Skeleton:*

- **A label carries the numbers,** so a red prints the whole reading.
- **An answer's fields by their path, never by substring;** a field's
  absence by its name, never by a null.
- **The record's example numbers** when the test tells the record's
  story, so the test and the record can be read side by side.
- **A helper that may be refused returns the answer and asserts
  nothing;** the helper a scene relies on calls it and asserts.

*Illustration here:* `.as("<what this number means>: %s", after)`;
`Body.of(…).intAt("$.quantity")` and `Body.names("endedBy")`; 10 on
hand, R holding 3, S holding 5 (SL-3's §4); `tryReserve` beside
`reserve`.

Four things the skeleton encodes, so a reader checking a test knows
what is being checked:

- **The witness before the status,** because a test that stops on a
  status code proves it reads status codes. Its red must fail on the
  numbers; a red that fails on a status, a `500` or a setup step is
  not the evidence's red.
- **The promise before the numbers,** because exact numbers can be
  right for the story and the state still be wrong elsewhere.
- **Every test says what it is,** so a reader never has to ask what
  it is for, and a tripwire can never be counted as a kill.
- **The numbers in the labels,** so a red read from the output alone
  says what the state was.

## What is exempt, and why

- **A wall's own checks** — a catalog test, the store's refusals sent
  directly. They say *not evidence* at their class; they discharge no
  kill and need no witness.
- **Infrastructure tests** — health, the migration path, the harness's
  own machinery.

## What to check at a close

Read the slice's tests and ask:

1. Does every test's first line say what it is?
2. Is the witness asserted before the answer, and the promise before
   the numbers, on every reading taken after the adversity?
3. Does every number's label print the reading?
4. Did every red fail on the witness, not on a status or a crash?
5. Where a test diverges from this shape — did it find something
   better? If so, this document changes and the change is dated.

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
