# Change-plan: the bundle re-pin @ c3a3d97, both halves as one copy

## Summary — the state after all commits

The five method skills and the four convention skills are pinned at
the bundle's `c3a3d97`, taken from the staging in `temp/`, and the
staging is gone. The method half is the copy this run already held
minus every provenance and harvest header — no rule changed, verified
line by line. The convention half is the handbook's `ba7eaa4` arriving
through the bundle's channel instead of the handbook's: the rules
kept, the explanation behind them moved to a manual that never ships.
`docs/concept/` is unchanged and is not re-copied.

One decisions entry carries the new hash, the bundle's named with the
handbook's inside it, and settles the three calls this take forces:
one source for both halves, no receipt branch for a pristine copy, and
what the kit's next delivery no longer carries.

Three citations of `convention-lifecycle §8 step 4` — in the rules
file's header, in `.claude/CLAUDE.md`, and in `TODO.md` — name §3 step
4, which is where the renumbering put that text. The rules file's rule
2 speaks of the header-less copy in the past tense, because it has
arrived. `TODO.md` carries one line back to the bundle naming where
this delivery's note fell short.

What this gives us that we did not have: one source, one procedure and
one hash to carry, and a rules file that describes files this run
actually holds.

## Commits

**1. `docs(agent): add change-plan for the bundle re-pin`**
The plan, agreed before any of the work, so the series can be read
against it afterwards.

**2. `chore(agent): update the bundle's skills @ c3a3d97`**
The take itself, agent-side and whole: 28 method files and 4
convention files copied from `temp/bundle-c3a3d97/`, the decisions
entry, and the two §8 → §3 citations that live on this side of the
line — the rules file's header and `.claude/CLAUDE.md`. Compare-first
is empty on both halves — `git diff kit-9e28143 main` on the
conventions and `git log housekeeping-bundle-7bbf49a..main` on the
method skills and the concept both return nothing, so no local edit is
overwritten.

The two citations ride here rather than in a step of their own because
they are made false by the copy. Reverting this step alone must land
the repo coherent: §8 citations against an eight-section skill, or §3
against a three-section one, never one against the other.

**3. `docs(agent): rule 2 names the copy that arrived`**
Rule 2's "from the next copy on there is no header block to join" was
written in the future tense against a copy that had not come. It has.
The mechanism does not change — the block is still this run's alone,
still taken away by rule 5 at each re-pin — only the tense, and the
parenthetical date that pointed forward. Its own step because it is a
judgment about our rule, not a consequence of the renumbering.

**4. `docs: TODO cites convention-lifecycle §3 step 4`**
The third stale citation. It is a separate step from 2 only because
`TODO.md` is a project record and steps 2 and 3 are agent-side, and no
commit straddles the two histories (`commit-messages`, scope note).
That rule costs one boundary's coherence here — between 2 and 4 the
TODO cites a section number the skill no longer has — and the cost is
accepted rather than argued, because the separation is what keeps a
filtered log readable.

**5. `docs: hand the note's two gaps to the bundle`**
One TODO line back, since they asked to hear it: the note named two
stale citations and there were three — it did not see
`.claude/CLAUDE.md` — and it left the receipt-branch question
unanswered while making it live. Their first run of the procedure as
note-and-copy together; both gaps are things a diff could not tell us
and the note was the only thing that could have. Its own step, not
folded into 4: a stale citation is bookkeeping this delivery forced, a
hand-off is a request outward.

**6. `docs: TODO carries the subject-style deviation`**
The decision below says this set matches the repo's declarative
subjects rather than the convention's imperative test. Added as a
step, not left in this file, because this file is deleted at the
close and the retrospective reads `TODO.md`. One line in Later, where
the other retrospective items are.

**7. `docs(agent): close change-plan for the re-pin`**
Deletes this file. Body records what diverged.

Then the branch `housekeeping-bundle-c3a3d97` fast-forwards into main,
on the reviewer's word.

## Decisions taken inside this plan

**Both halves come from the bundle now, one pin.** The four convention
files are byte-identical to the handbook at `ba7eaa4` — their claim,
which this repo cannot check: we hold no handbook checkout, and the
receipt `kit-9e28143` only proves we have not edited ours. Taken
anyway, because our own `convention-lifecycle` permits it in as many
words — the handbook may be "a checkout on disk or the payload a
handoff carries" — and because the alternative is two channels, two
procedures and two hashes for files that arrive together. The entry
names `c3a3d97` with `ba7eaa4` inside it, so the handbook's hash is
still on record and the unverified claim is attributed, not adopted.

**No receipt branch for this delivery.** The receipt exists for the
kit because the kit ships stubs, whose local content is the record and
cannot be diffed against a master. A skill copy is pristine by rule 1;
its compare is `git diff <delivery-commit> -- <path>`, which is what
rule 5 already names and what was used above. The `kit-<hash>` line
continues for the stubs when the kit next delivers — carrying four
fewer files, which the entry says so the next update does not read
their absence as a deletion.

**The rules file stays, and stays seven rules.** The bundle took these
rules as this run wrote them (2026-09-17); nothing in this delivery
reopens them. Step 3 changes a tense, not a rule.

**`docs/concept/` is not re-copied.** `diff -rq` against the staging
is empty. Copying it anyway would put five unchanged files in the
commit and make the diff lie about what the delivery carried.

**The first-edit-through-a-re-pin report stays unfired.** We had no
edit to carry through, so this re-pin does not discharge it. Its TODO
line is untouched.

**Subjects follow this repo's lived style, not the convention's
letter.** `commit-messages` sets an imperative test; every record
commit in this log since Step 1 is declarative instead ("the bundle
takes the in-place rule"). The deviation is the repo's, not this
set's, and this set matches it rather than leaving six commits that
read unlike their neighbours. Worth a retrospective line — either the
convention bends or the log does — and step 6 is that line. Judged
the other way when this plan was written, and reversed at the close:
a note that lives only in a deleted file's history is a note nobody
finds.
