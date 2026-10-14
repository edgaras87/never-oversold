# The shape of a kill

**Governs:** how a kill is written in the definition's collisions
layer: one way the promise can break, found by setting one fact
against one possession. Its form, never its content: which fact
meets which possession, and what dies, come from the collision.

<!-- A shape: what a kind of output looks like here, its form and
     never its content. Read at framing's closing gate. How shapes
     live is .claude/rules/shapes-lifecycle.md, and not repeated
     here. How a kill reads sentence by sentence is the writing
     rule's (.claude/rules/writing.md). -->

## What this document controls

**The form of one kill in the numbered list of kills,** and nothing
else. A kill this shape accepts can still be the wrong kill; that
is the collision's business. The concerns the kills are grouped
into, the folds, and what the fences stopped are their own.

**Two layers, marked apart.** *Skeleton:* what any project's kills
would want. *Illustration:* this project's form of it, with its
tags and its numbers.

## The shape

```markdown
<n>. <the fact> × <the possession it attacks> → <what happens>:
   **<what dies, in its own words>**. <optional: "kill <m>'s death,
   <how this case differs>.">
   *Say:* <real numbers from this system, from the state before to
   the state that breaks>
```

A kill with more than one case:

```markdown
<n>. <the fact> × <the possession> → <what happens>, two ways:
   (a) <the first case>: **<what dies>**.
   (b) <the second case>: stopped by <fence>: <why, optional>.
   *Say:* <numbers for each case that dies>
```

A collision that breaks nothing:

```markdown
<n>. <the fact> × <the possession> → <what happens>: **nothing
   dies** — <why the promise holds>. Stopped by <fence>.
```

*Skeleton:*

- **It starts with the fact and the possession it attacks,** by
  their tags, then what happens when they meet.
- **Every kill states its own death, in bold, in its own words.** A
  kill whose death is another kill's says so after its own:
  "kill 1's death, the racers our own instances". A reader landing
  on any kill sees what breaks without reading another.
- **A kill may have cases,** labelled (a), (b), when one fact meets
  one possession in more than one way. Each case ends in its death,
  in bold, or in the fence that stops it.
- **A fenced case says "stopped by"** and the fence, or fences:
  "stopped by W1", "stopped by W2 and V5". A reason may follow
  after a colon.
- **A collision that breaks nothing stays in the list,** saying
  "**nothing dies**", why, and the fence. It shows the pair was
  tried, and the count of kills stays honest.
- **Every kill that dies carries a `*Say:*` line** with real
  numbers from this system: the state before, the collision, the
  state that breaks the promise.

*Illustration here:* the tags are the definition's: facts F1 to
F24, possessions P1 to P4, fences W1 to W7, verdicts V1 to V5.

```markdown
1. F1 × P3 → both racing admits succeed on units that fit only
   once: **reserved exceeds the on-hand-count.**
   *Say:* 10 on hand, 8 held. Two reserves of 2 arrive together;
   both see 2 free, both are admitted: 12 held of 10.

9. F15 × P2, P3 → we die between deciding, recording and
   replying, two ways:
   (a) an admit decided but not yet recorded is a decision no
   other admit can see: **two decisions on one unit**, kill 1's
   death from our own half-done work.
   (b) recorded but never replied: an orphaned hold. Stopped by W2
   and V5: expiry is its exit.
   *Say:* (a) 10 on hand, 9 held. A reserve of 1 is decided, not
   yet recorded; a second reserve of 1 sees 1 free and is admitted;
   the first is recorded: 11 held of 10.

20. F2, F12 × P2 → a retried or abandoned reserve makes a second
    or orphaned hold: **nothing dies.** It over-holds; it cannot
    oversell. Stopped by W2 and V5.
```

## What is exempt, and why

- **The concerns, the folds, and what the fences stopped,** which
  follow the kills: they group and count the kills; they are not
  kills.

## What to check at a close

Read the kills and ask:

1. Does every kill start with the fact and the possession, by
   their tags?
2. Does every kill state its own death, in bold, or say "nothing
   dies"?
3. Is every case labelled, and does each end in a death or "stopped
   by" a fence?
4. Does every kill that dies have a `*Say:*` line with real
   numbers?
5. Where a kill differs from this shape: did it drift, did it find
   something better, or is it a different kind? The shapes rule
   says how each ends.

## Revisions

<!-- Dated lines: what changed, what taught it, and what was read
     from elsewhere. -->

- 2026-10-13 — written from run 3's 20 kills, after a look at where
  they differed and what they all lacked, each settled by the
  reviewer (RETRO.md, D29): every kill states its own death; cases
  labelled; "stopped by" for a fence; "nothing dies" kept; a
  worked example with numbers in every kill that dies; the tags
  left to the definition. Taught by D7: four kills had lost what
  dies when they were copied into the definition. Nothing read from
  elsewhere.
- 2026-10-14 — the example's kill 20 takes the definition's form,
  "**nothing dies.** It over-holds; it cannot oversell.", without
  the dash the writing rule splits. Found at Step 10's closing
  reading. Nothing read from elsewhere.
