# Commit plan: the documentation says what is true

## Summary — the state after all commits

Fourteen of the eighteen findings from the error check of
2026-10-01 are fixed: every false number, broken pointer and
out-of-date sentence in a document this project may edit. The
truth set changes only by dated revision entries, as its own rule
says; each slice record gets a dated line for what changed.

Not in this set, each waiting on the reviewer: the two ADRs
(0005's stale reasons, 0010's 201 against the code's 200), the
bootstrap requirements as a snapshot, PLAN's pointer to a
convention this repo does not hold, and the two structure ideas
for TODO.

## Commits

**1. `docs: the truth set corrects a count and a list`**
Definition L1's census table: the second lens found six new
facts, not five. Registry: SL-1's adversity names F21 and F18,
which the definition places in concern A; the revision log in date
order. A dated revision entry in each file.

**2. `docs: SL-1's record agrees with itself and the code`**
§7's pointer to "§6" goes to "Deviations and provisionals", where
the cost actually is. The migrations rule stops pointing at a
README that does not exist; it is stated where it stands. "Refused
by the constraint" becomes refused by the statement's condition,
the constraint behind it, as the code does it. §8's provisionals
say SL-2 decided the correction. A dated line in §10.

**3. `docs: SL-2's §9 says E5 grew to cover G5`**
§5 signed E5 for G6 alone; the delivered test carries G5's
structural half too. One sentence in §9, not a change to the
signed §5. A dated line in §11.

**4. `docs: the operator manual catches up with the contract`**
A slice chooses its face in its plan, not its specification, as
the contract has said since 2026-09-11. The Flyway check's
expected output is what a ground shows now that V1 exists; the
dated "seen here" line stays, marked as before V1.

**5. `docs: ARCHITECTURE describes two closed invariants`**
The overview's "first invariant" and the codemap's tests, brought
to SL-2's close.

**6. `docs: README migrates before it runs the ledger`**
The schema step moves ahead of starting the ledger, so a first
run top to bottom has tables.

**7. `docs: CHANGELOG's 0.1 has one Added`**
The two Added sections under 0.1 merged into one, SL-1's entries
first. No 0.0 section is invented: 0.0 was never released as such,
and writing one now would claim a release that did not happen.

**8. `docs: devlog carries the error check`**
The session's entry and its Resume line.

## Decisions taken inside this plan

- **The Flyway expectation is not re-lived.** A fresh ground could
  not be stood up beside the real one without removing the real
  container: the compose file pins the container name. The new
  expected line is Flyway's documented behaviour, and the manual
  keeps its dated lived line as history.
- **The migrations rule is not added to V1.** Editing a migration
  that has run changes its checksum and fails validation on every
  ground that applied it. The rule is stated in SL-1's record
  instead of pointing elsewhere.
- **CHANGELOG merges, rather than inventing 0.0.** The header's
  definition of 0.0 stands; the section it would describe was never
  cut, and the history does not claim it was.
