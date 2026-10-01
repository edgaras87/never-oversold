# Commit plan: the error check's four open decisions

## Summary — the state after all commits

The last four findings of the error check of 2026-10-01 are
settled, as the reviewer chose:

- ADR-0010's "201 for a new record" meets the adjustment that
  creates an item and answers 200: a new ADR-0012 records that an
  adjustment answers 200 whether or not the item existed, and why.
  The code, the tests and README already do this; nothing in them
  changes.
- ADR-0005's two outdated reasons stay as written, ADRs being
  immutable, and TODO's Known issues says so: the decision stands,
  the reasons do not, and when to revisit.
- The bootstrap requirements say at the top that they describe
  2026-09-12, and what SL-1 changed since.
- PLAN's retrospective points at the three tests where they are
  written, CLAUDE.md's own comment, not at a convention this repo
  does not hold.

TODO's Later also carries the two structure ideas the check
raised. The devlog says what was decided.

## Commits

**1. `docs: ADR-0012, an adjustment answers 200`**
Opens Proposed. Context: ADR-0010's rule and ADR-0011's first
adjustment meet. Decision: 200 either way. Why: an adjustment
asserts a count, and the same assertion should get the same answer;
with 201 on creation, a resent first adjustment would be answered
201 then 200. Rejected: 201 on creation.

**2. `docs: the bootstrap requirements say they are dated`**
One paragraph under the title: true as of 2026-09-12; SL-1 has since
added V1 and the ledger's behaviour, as §6 intended, and the probe
is gone.

**3. `docs: PLAN's retrospective points at the three tests`**
Item 6's "(agent-arrangement §2)" points at the comment in
CLAUDE.md that holds the three tests.

**4. `docs: TODO carries ADR-0005 and two structure ideas`**
Known issues: ADR-0005's reasons, what, why accepted, when to
revisit. Later: a mark for "true as of a date" on records that
describe a moment, and the habit of sweeping a fact's other copies
when one changes.

**5. `docs: ADR-0012 accepted, and the records catch up`**
ADR-0012 to Accepted; PLAN's decision index and ARCHITECTURE's
ADR list gain it; the devlog's entry and its Resume line.

## Decisions taken inside this plan

- **ADR-0012 amends ADR-0010 without superseding it.** ADR-0001
  knows only "Supersedes"; ADR-0010's other decisions all stand, so
  0010 stays Accepted and 0012 says which one line it narrows.
- **ADR-0005 gets no new ADR.** Its decision did not change, only
  two reasons; a known issue is the honest size of that.
- **The bootstrap requirements keep their body.** A dated note on
  top, not edits through the certified text.
