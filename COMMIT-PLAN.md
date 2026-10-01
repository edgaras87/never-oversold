# Commit plan: the writing sweep's remainder

## Summary — the state after all commits

The shape covers the whole of a slice record's argument: guarantees,
owners, and now the faces weighed for a guarantee, each face a block
with labelled parts and a rule between faces. No comparison table is
left undecided.

SL-1's record reads like SL-2's: its guarantees and owners in the
shape, its faces as blocks, and its §8 counting the unknown item
that §4 already names. SL-2's two face tables are blocks. Each record
carries a dated line saying what changed and that no decision did.

README and the infrastructure contract lose the passages that carry
an argument in one block: README's Status paragraph and invariant
bullets, the contract's "What the store offers". The contract no
longer says a check constraint waits for commit. The operator manual
is untouched; it is procedure, one claim per line.

TODO's item is gone, and the devlog says what the sweep did.

## Commits

**1. `docs(agent): the shape takes the faces as blocks`**
The shape gains a third skeleton, a face in "the faces chosen, and
the ones not", and comparison tables leave its exempt list. A dated
line under its Revisions. One decisions entry: the requirements, the
four candidates, why each lost, and that the verdict was the
reviewer's without the render step. The draft in `temp/` is deleted
after this commit; it is untracked, so no commit shows that.

**2. `docs: the faces compared in blocks`**
SL-1 §7's five faces and SL-2 §8's G4 and G5, rewritten from tables
into the new skeleton. Word for word where the words survive; only
the form and the labels change. A dated line in each record's
sign-offs.

**3. `docs: SL-1's guarantees and owners in the shape`**
§3's six guarantees and §7's owners, each a block with its labelled
parts and a *Say:* line where a mechanism is involved, the numbers
taken from SL-1's own evidence runs. No guarantee, owner or kill
changes. A dated line in §10.

**4. `docs: SL-1's §8 counts the unknown item`**
§4 names an unknown item among E6's shapes; §8's row does not, and
counts its shapes loosely. The row is brought to what the tests
send. A content fix, kept apart from the form.

**5. `docs: README and the contract say one thing at a time`**
README's Status paragraph and the bracketed notes on its invariants;
the contract's "What the store offers". Same claims, split so a
reader holds one at a time.

**6. `docs: the contract says when a check constraint fires`**
Added in revision, after commit 5. The contract lists "unique and
check constraints checked at commit". PostgreSQL can defer a unique
constraint to commit; a check constraint it checks at every write,
and cannot defer. SL-1's wall is a check constraint and holds
because of that. A content fix, kept apart from commit 5's form.
ADR-0005 says only "constraints checked at commit", true of the
deferrable kinds, and is not touched.

**7. `docs: devlog and TODO close the writing sweep`**
TODO's item leaves; the devlog's entry and its Resume line.

## Decisions taken inside this plan

- **Faces as blocks, decided without rendering.** The reviewer chose
  B on the argument that it is the owners' answer for the owners'
  reason. `visual-comparison` asks for the render first; the
  decisions entry says it was skipped and whose call that was.
- **Recorded in `.claude/decisions.md`, not a `docs/adr/` ADR.** The
  shape is agent-side, and the writing pass recorded its own
  comparison there. `visual-comparison` says ADR; the precedent wins
  here because the decision is about how records are written, not
  about the system.
- **Closed records are edited.** The shapes rule says outputs closed
  before a change are not reopened. The reviewer decided otherwise
  for this sweep: SL-1 was written before any shape existed, and the
  sweep was queued to fix exactly these records. Each gets a dated
  line, as SL-1's faces did when restated after its close.
- **SL-1's §8 table keeps its form.** It is a lookup, criterion to
  test, and the shape exempts those. Only its E6 row's content moves.
