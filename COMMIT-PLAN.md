# Commit plan: the review after SL-3 — who writes, where rules live, what size

<!-- The convention: .claude/skills/commit-plan. The commits that
     exist are the steps done; this file plans them and is deleted
     at the close. -->

## Summary — the state after all commits

The questions every slice answered without naming are answered once,
in the records they belong to. The definition says who writes the
store — the ledger alone, its structure Flyway's, the superuser
trusted, everything else outside — and what size the system is built
for. Two ADRs record why the store trusts one writer rather than
locking it, and when an index is a need. `reservation(item_id)` is
indexed. The build fails if the ledger ever updates or deletes a
reservation, or if any class but the ledger reaches the store
through any database API. ARCHITECTURE maps each rule to who
computes it, who refuses a wrong result, and what guards it.

What the repo gains besides: the delivered copies corrected in place
where SL-3 and this review found them silent — the slice, framing,
ground and commit-plan skills — each edit logged with its why and
handed to the deliverer by a TODO line, with the method chapters'
lessons as prose lines beside them; this project's slice-record
shape asking each face what size it assumes and each wall whether
its backstop was seen. The ground migrated to V4. No version
change: no invariant closes here.

## Commits

**1. `docs(agent): open commit plan for the review`**
This file. Thirteen commits; the decisions were taken in discussion
on 2026-10-07 and 2026-10-08, before it.

**2. `docs: T4 and the scale join the definition`**
Two dated revisions of the definition. L1's trust list gains T4 —
only the ledger writes the store's data, the identity it writes with
being its alone; only the migration tool writes its structure; the
superuser is trusted, as in any store — and the fences gain its
complement, W7: writers that bypass the ledger. The runtime ground
gains the scale the system is built for: the evidence's size, beyond
which nothing is promised about speed (W3). Decision first: the
ADRs and the code below lean on it.

**3. `docs: ADR-0014, the store trusts one writer`**
The four options weighed for who may write the store — trust one
writer, lock it with roles, defend the open world, stay silent — and
why trust won. What a real deployment would do instead: an app role
with only the rights its statements use (no update or delete on
reservations or receipts among them), the migration role used only
by the pipeline, a read-only role for people, an audited emergency
login, admin operations as features at the door. Opens as Proposed.

**4. `docs: ADR-0015, an index every decision needs`**
How ADR-0007's "nothing enters ahead of need" reads for an index: a
column every decision searches by is a need known from the code, not
a guess waiting for a measurement. Opens as Proposed.

**5. `feat: index reservations by item`**
V4: an index on `reservation(item_id)`, which tidy and the check on
the units held search on nearly every request. Changes no answer. The
catalog test names it. Every test green unchanged.

**6. `test: the ledger never rewrites a reservation`**
E7 gains a fourth rule: no statement in the ledger updates or
deletes a reservation — its ending is a receipt. SL-2's rule that
only the ledger reaches the store widens from two Spring classes to
every database API — `java.sql`, `javax.sql`, Spring's JDBC and
transactions. Each seen red by planting what it forbids.

**7. `chore(agent): correct the slice skill from SL-3`**
`cbc-slice`, in place: Stage 2's hunt for escape hatches aims at the
definition's trust list — a writer it trusts is named, not walled; a
face's cost says the size it assumes and when to revisit; Stage 3's
red counts only if it fails on the witness; a wall's own check is a
third kind of test beside evidence and tripwire, discharging no
kill; a race whose red needs its window widened is widened on the
red tree only, and says so. One decisions entry per edit.

**8. `chore(agent): correct the framing skill from SL-3`**
`cbc-framing`, finished here, in place (delivered-copies rule 7):
its trust step asks who besides the system can write what the
system stores, and whether that is trusted or defended; its runtime
ground asks for the size the system is built for. The fix's first
use is the deliverer's; the entry says so.

**9. `chore(agent): correct infra-establish from SL-3`**
`infra-establish`, finished here, in place: the ground names the
identity people inspect with, and if it can write, says the
framing's trust list must cover it, or that a read-only identity is
owed. Its first use is the deliverer's.

**10. `chore(agent): correct commit-plan from SL-3`**
`commit-plan`, in place: planned subjects are counted when the plan
is written, not when each commit lands; a step that changes the
agent's files is its own step, never inside a records step.

**11. `chore(agent): the shape asks scale and backstops`**
This project's slice-record shape: a face's cost says what it
assumes and when to revisit; a wall's "if it were ever wrong" says
whether that backstop was seen at the red run, or that it is
unproven. From SL-4 on; closed records are not reopened.

**12. `docs: records catch up on the review`**
ARCHITECTURE's responsibility map, and V4 in its codemap. The
infrastructure contract and the operator manual: `runtime` is the
ledger's to write with, and people read with it under T4; V4's
migration with its before-check. SL-3's record: dated lines — its
escape hatch for an open hold deleted with its units closed by E7's
fourth rule under T4; E7 called an early warning, not a tripwire in
the skill's sense; the check on the units held's face gaining what
size it assumes. SL-2's record: a dated line on its rule widened.
ADR-0014 and ADR-0015 to Accepted. TODO: the review closed; V3's
cost item narrowed to retention; one line per copy edited, to the
deliverer; the chapters' lessons as prose lines. The devlog. The
ground migrated to V4, on the reviewer's word at that boundary.

**13. `docs(agent): close commit plan for the review`**
Deletes this file.

## Decisions taken inside this plan

- **Decision first, then code, then records.** The definition and the
  ADRs land before the index and the rules that lean on them; the
  records last, where every truth they cite exists.
- **Each delivered copy its own commit.** The deliverer takes or
  declines per copy, and one commit per copy keeps each diff
  readable against its pin. The TODO lines that hand them over are a
  project path, so they ride in the records commit.
- **The definition carries no agent language** (the entry file's
  local rule): T4 names the ledger, the migration tool and the
  superuser, never a skill or a step.
- **The finished skills' edits are questions, not this project's
  answers.** "Who writes the store besides the system?" — never "only
  the ledger". The deliverer filters what is too specific.
- **No version change.** The version counts invariants closed on
  evidence; none closes here.
- **The ground's migration to V4 waits for the reviewer's word** at
  commit 12, with the before-check and a dump, as V3's did.
