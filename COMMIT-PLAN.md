# Commit plan: the writing pass — the shape, its gate, its hand-offs

<!-- The convention: .claude/skills/commit-plan. Deleted at the close. -->

## Summary — the state after all commits

SL-2's record reads without being unpacked aloud, and the shape it
arrived at is written down as a model of its own — controlling two
sections and saying so, leaving the rest to each slice. Every slice
step from Step 7 on carries a gate item reading its record against
that model before the merge. The slice skill gains the step in
general form, carrying no shape of its own, so the mechanism can
travel while this project's answer stays here. Two hand-offs stand
in TODO: the shape offered to the bundle as a baseline, and a
request that baselines be delivered at a gate rather than shipped.

Written **after** the work rather than before it, which the
convention asks the other way round. The set grew out of a
conversation that kept turning up the next question, and the plan
is being opened now because the reviewer wants each remaining
boundary to be a place to object. Steps 2 onward are unlanded and
may change; step 1 is the only one whose content is settled.

## Commits

**1. `docs: reshape SL-2's guarantees and owners`**
The two sections that could not be read without help. Labelled
parts, a `*Say:*` line with real numbers, a rule between blocks,
and the owners table gone — a four-column row measured 435
characters on one line. This one stands on its own: it is the
material every later step describes.

**2. `docs: model the shape a slice record's two sections take`**
`docs/construction/slice-record-shape.md`, alone. Derived from
step 1's result, not from a guess. Controls the guarantees and the
owners and says why only those two — they are the two that were
written, read, found wanting and rebuilt. Carries the two
comparisons and the baseline lifecycle.

**3. `docs: gate the shape check, and default the gate items`**
PLAN, twice. The gate item that every slice step from Step 7
carries, and the retrospective item narrowed to the giving-back
direction, since reading other projects' baselines happens after
the first slice rather than at the end. And a block of default gate
items beside the standing rules — the lines that have appeared in
every step's gate written by hand, now with their placeholders, to
be copied into each step when it opens. Copied, not shared: a step
needs its own checkbox or there is nothing to tick.

**4. `chore(agent): move the shapes under .claude/, as workshop`**
Steps 2 and 3 landed the shape record under `docs/` and the glob
with it, and both were wrong about where this belongs. Its
vocabulary gives it away — it says "the bundle" and "the reviewer",
which a reader of `docs/` cannot decode — and its reader is
whoever writes the next record, not whoever judges the system. So
`docs/construction/slice-record-shape.md` becomes
`.claude/shapes/slice-record.md`, the gate item's glob follows, and
the entry file's records-table row moves with it.

`.claude/shapes/` and not `.claude/rules/` **while the shape is
still provisional**, and the difference is mechanical rather than a
note anyone must remember: a file under `rules/` with `paths:`
frontmatter loads whenever a matching path is touched, and one
under `shapes/` is inert until someone opens it. A provisional
shape must not be in front of the writer, because what the writer
invents without it is the only thing that can still change it.

A fixed shape is the opposite and moves to `rules/` for exactly
that reason. Once it has stopped changing there is nothing left to
learn from divergence, conformance is what is wanted, and hiding it
only makes the next record re-derive it badly. The gate survives
the move and asks a different question: not "did this find
something better" but "did this drift".

So the lifecycle is renamed and given its stages, each with a
place: **provisional** in `.claude/shapes/`, inert; **offered as a
baseline**, held at the bundle and requested into `temp/` at the
one moment a shape is first written, deleted after; **fixed** in
`.claude/rules/` with its `paths:`, loading while the matching
artifact is written, and shipping with the bundle — which ships
fixed shapes only, those being the ones meant to be in front of a
writer. The promotion is countable: a shape is fixed once two
closes in a row take nothing up into it.

**5. `chore(agent): write the shape lifecycle as a rule`**
The lifecycle currently sits inside `slice-record-shape.md`, in
three sections. That was tolerable with one shape and is wrong
now: it governs every shape, and burying it in one of them leaves
the second shape either duplicating it or silently lacking it.
Worse, it does not describe `.claude/shapes/` — it describes
movement between three places, `shapes/`, the bundle, and
`rules/`, so no file inside one of them has the right scope.

It becomes `.claude/rules/shapes-lifecycle.md`, path-scoped to
`.claude/shapes/**` so it loads exactly when a shape is opened and
never while a record is being written. The precedent is this
project's own `skills-changed-in-place.md`: a rule about how a
class of artifact is handled, scoped to the paths it governs,
written here from lived work.

What it carries: the three stages and their places; that the place
decides whether a shape loads during the work; the promotion
counter, two closes taking nothing up; that a fixed shape which
changes is not fixed any more and the counter resets; how a
provisional baseline is requested into `temp/` and deleted; and
that the bundle ships fixed shapes only.

**6. `chore(agent): add the shape kind to artifact-kinds`**
The kinds vocabulary has no word for a document that describes
while the work happens and binds at one moment, so naming this one
took "model" plus a paragraph saying model is wrong — which is the
symptom artifact-kinds names as earning an entry. Edited in our
copy under convention-lifecycle §3: the entry, its axes, and the
exemplar. The decisions entry and the TODO line asking the bundle
to evaluate ride with it; the `Governs:` line stays prose, since
nothing parses it and no record under `docs/` carries frontmatter.

**7. `chore(agent): check a slice record against its project's shape`**
The skill edit, the decisions entry, and the records-table row.
Agent paths only. The step the skill gains is general; the shape it
compares against is the project's own, which is what lets this
travel upstream without handing anyone an answer.

**8. `docs: hand the shapes and their lifecycle to the bundle`**
TODO's hand-offs, written last because two of the three describe
what step 5 defines and would otherwise be written twice. Three of
them. One: this project's slice-record shape offered — as a
*provisional shape*, not an example, the distinction mattering
because what the bundle does with it is a design question of its
own. It has to decide where a project's shapes land on its side,
how it notices that `.claude/shapes/` here holds a new or changed
finding, whether it reconciles by reading the records of change or
by tracking a hash. Ours is to offer and to say what changed, not
to specify their side. Two: ship fixed shapes, request provisional
ones. Three: the lifecycle itself offered as a convention
candidate, carrying the question of whether the arrangement
convention should learn about shapes at all.

**9. `docs(agent): the drafts behind the writing pass`**
`temp/`: the decide-first questions, the option comparison on the
rule's wording, the visual comparison on the Owners shape, and the
four rendered shapes. Kept because the reasoning is the evidence
that the choices were built rather than argued.

**10. `docs(agent): close the commit plan for the writing pass`**
Deletes this file; the body records what diverged.

## Revision — 2026-09-22, at step 3's boundary

Steps 3 and 4 grow; the count is unchanged. What forced it: the
reviewer, reading step 3's gate item, saw that the lines it sits
beside — the branch pair, the commit hygiene line — are written by
hand in every step and should be defaults with placeholders. That
is the same shape of finding as the slice record's, one level up:
the thing that recurs belongs to whatever defines the steps, which
is the playbook PLAN names at its top. Adopted here first, on
trial, as the branch rule was, rather than proposed upward from an
idea. Step 4 grows with it, since where the lifecycle and the
defaults should land is one question for the bundle rather than
three.

## Revision — 2026-09-22, at step 3's boundary, second

A step added, now nine with the close. What forced it: naming the
shape document's kind needed a word the vocabulary does not have,
and the file says so in its own header — "model" plus a paragraph
explaining that model is wrong. artifact-kinds calls that the
symptom that earns an entry, so the entry is written rather than
the misfit left standing. Step 3 also grew inside its own boundary
while the reviewer read it: the gate item split in three, the
shape check made one pair per shape, the shapes found by a
`docs/**/*-shape.md` glob and a `Governs:` line rather than a list,
and a condition added for when the bundle's baselines are asked
for — once, when a shape is first written.

## Revision — 2026-09-22, at step 3's boundary, third

A step added, ten with the close, and two landed steps corrected by
it rather than rewritten. The reviewer asked why shapes live under
`docs/` at all, and the answer is that they should not: the shape
record is workshop furniture, written for whoever writes the next
record, and it reads as such — it uses arrangement words a `docs/`
reader cannot decode. Step 5 moves it, moves the glob and the
records-table row with it, and renames the lifecycle from the
middle stage it was named after.

Also settled there, and recorded because it is the kind of thing a
later hand undoes: not `.claude/rules/`, whose files load whenever
a matching path is touched. That would hand the shape to whoever is
writing a record, which is exactly what the close-only rule exists
to prevent.

Left as it is: step 4's hand-off grows one line, asking the bundle
whether the arrangement convention should learn about shapes, since
this project holds that convention as stubs and has no copy to
edit.

## Revision — 2026-09-22, at step 3's boundary, fourth

A step added, eleven with the close. The lifecycle was written
inside the one shape that exists, and the reviewer asked where it
would live once a second shape appeared — and further, whether it
is a `.claude/shapes/` matter at all, since a shape ends its life
in `rules/` and passes through the bundle on the way. It is not:
it governs movement between three places, so it becomes a rule
scoped to the shapes it governs, on the model of this project's
`skills-changed-in-place.md`.

Step 4's hand-off grows again with it: the lifecycle offered to
the bundle as a convention candidate, by the same route those
seven rules took — written here from lived work, handed up after,
theirs to take, reshape or decline.

## Revision — 2026-09-22, at step 4's boundary

The hand-offs move from step 4 to step 8, after everything they
describe exists. What forced it: the reviewer read them and found
two of the three were restating the lifecycle that step 5 has not
written yet, which is the same thing said twice and corrected
later. The third was framed as offering an example, and it is not
— it offers a provisional shape, and how a bundle notices, stores
and reconciles a project's shapes is that bundle's design rather
than something this repo can specify for it.

Left open deliberately, to be settled inside step 5 rather than
guessed here: whether a requested provisional shape is deleted
after the gate or moved into `.claude/shapes/` and kept. The
reviewer's argument for keeping it is that by then this project's
later records are already influenced by its own earlier ones, so
hiding it buys little — which the lifecycle is the right place to
weigh.

## Decisions taken inside this plan

Named here so each can be objected to at its own boundary rather
than all at once.

- **The model controls two sections, not the record.** SL-1 and
  SL-2 do not carry the same sections and should not.
- **The shape stays in this project; only the step travels.** A
  shape shipped with the skill would be inherited rather than
  derived, and would make the step it belongs to decorative.
- **Baselines are read after the first slice, not at the end.**
  Inside a project the records inherit from each other anyway, so
  the first record is the only independent sample and the rest of
  the project may as well have the benefit.
- **A provisional baseline is requested at a gate and deleted
  after.** A file in the working tree is read during the work
  whether or not anyone meant it to be.
- **The temp drafts are committed rather than discarded.** The
  `decide-first` and comparison playbooks both say to delete the
  draft once the answers are recorded; kept here because the
  reviewer asked to be able to diff the reasoning, and because this
  set's answers live in several places rather than one ADR.
