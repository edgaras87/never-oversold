# Commit plan: SL-1's tests say what each is for

## Summary — the state after all commits

Every test method SL-1 left behind opens with a header, as SL-2's
do: which evidence criterion, which guarantee and which kill it
serves — or that it is not evidence, and what it pins instead — and
then, in plain words, what it checks and what would make it fail.
A reader who lands on one method no longer has to scroll to the
class comment, or reconstruct its purpose from its assertions.

TODO's item for this is gone. TODO also carries the question owed
to the bundle since its take: which rule wins when a take covers
both `.claude/` and `docs/concept/`.

## Commits

**1. `test: each of SL-1's tests says what it is for`**
Fifteen headers across six files: `ReserveStormIT`,
`InstancesStormIT`, `AdjustmentRaceIT`, `OneClockIT`,
`NoInstanceStateOrClockTest`, `ReservationDoorIT`. Comments only;
nothing a test does changes. TODO's item leaves in the same commit,
since the item is this work and reverting one should bring back the
other.

**2. `docs: TODO asks the bundle which rule wins at a take`**
One line under *To the deliverer*. `delivered-copies.md` takes skills,
rules and the concept chapters as one act under one pin;
`commit-messages` says an agent commit touches nothing outside
`.claude/`. The take @ 0000855 followed the first and broke the
second.

**3. `docs: devlog carries SL-1's test headers`**
The session's entry and its Resume line.

## Decisions taken inside this plan

- **Where the numbers come from.** Criterion, guarantee and kills
  from SL-1's record (§3, §4, §8); every number in a header from the
  test's own code. Red-run results stay in the record, as SL-2's
  headers leave them there.
- **Four door tests are marked "not evidence — a tripwire".**
  `ReservationDoorIT`'s tests for a new item, an admitted answer and
  a refused answer cannot fail for the invariant; each pins a decided
  face, ADR-0010 or ADR-0011. `cbc-slice` asks such a test to say so.
- **The unknown-item test is marked E6.** SL-1 §4 names an unknown
  item among E6's shapes, answered with ADR-0010's status. §8's row
  counts nine shapes and leaves it out. The header follows §4; the
  record is not changed here.
- **The adjustment-under-holds test is marked as SL-2's.** Its
  inline comment already said so. The comment becomes the header.
- **Class comments are left alone.** They already say what each
  class is for; the item was the methods.
