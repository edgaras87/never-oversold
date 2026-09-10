# 0003. Name the project

Date: 2026-09-10
Status: Accepted

## Context

The briefing brought a working name, safe-reservations, and the
rule that the naming decision confirms or overturns it once the
promise is chosen: the name follows from the sentence. The promise
is now fixed (`docs/system/intent.md`): *for any item, the
reserved quantity never exceeds the on-hand-count, however many
reservations race for the same units.* The project is real —
framing did not end in "no project" — so it needs a public
identity: a name, a one-line description, and a remote under that
name.

The bar a name must clear, set in PLAN's Step 2 gate:

1. It points at the claim, not at a feature or a component.
2. It does not overclaim: it promises what the intent promises,
   no more.
3. A stranger can guess what the project proves from it.
4. It works as a repository and directory name.

## Options considered

Candidates derived from the sentence — its negation, its two
numbers, its adversity, its owner — each against the bar:

1. **safe-reservations** (the working name). Points at the
   territory, not the claim; "safe" does not say from what, and
   promises more than one invariant — safe from loss, from
   duplication, from lies — where the intent refuses two of those.
   A stranger guesses the topic, not the guarantee. Fails 1 and 2.
2. **never-oversold.** The promise's negation, ruled out. Says
   exactly what the intent says and nothing more; a stranger reads
   inventory and the one thing that cannot happen. Repository-
   friendly. Clears all four.
3. **no-oversell.** The same claim, shorter, but it reads as a
   flag or a slogan rather than a property that holds; "no" is
   weaker than "never". Clears the bar; second to 2.
4. **oversell-proof.** The claim, with a pun on proof. The pun
   reads as marketing before the evidence exists, and "proof"
   claims done at the naming. Fails 2 until the release.
5. **reserved-within-count.** The invariant literally. Honest, but
   a stranger cannot guess it; it names the ledger's arithmetic,
   not the event it prevents. Fails 3.
6. **reservations-under-contention.** Names the adversity, not the
   claim; a stranger guesses the difficulty, not the guarantee.
   Fails 1.
7. **reservation-ledger.** Names the component. Fails 1.

## Decision

**never-oversold** — the reviewer's verdict, 2026-09-10, on the
recommendation, no candidate added or reordered. The working name
safe-reservations is overturned: it named the
territory and overclaimed; the promise names one event, and the
name should be that event, ruled out.

The description, one line derived from the intent's why, used
verbatim on the remote:

> An inventory-reservation ledger built to keep one promise under
> contention: for any item, reserved never exceeds the
> on-hand-count. Correctness-first, with the evidence.

## Consequences

Good: the name states the claim, so the reader meets the promise
before opening a file, and the name cannot drift from what the
evidence proves. Bad: a second claim, if ever added by a dated
revision, would not be in the name; the name is the first promise's.
The folder on disk keeps its old name until the reviewer renames it;
records name the project, not the path.
