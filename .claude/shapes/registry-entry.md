# The shape of a registry entry

**Governs:** how one slice's entry is written in the slice
registry: the fields framing gives it, and the line each close
adds. Its form, never its content: what the invariant and the
adversity are comes from framing, and what a close answered comes
from the slice's record.

<!-- A shape: what a kind of output looks like here, its form and
     never its content. Read at framing's closing gate, and at each
     slice's close. How shapes live is
     .claude/rules/shapes-lifecycle.md, and not repeated here. -->

## What this document controls

**The form of one slice's entry,** and nothing else. An entry this
shape accepts can still cut the wrong slice; that is framing's
business. The registry's other sections (its state, the count of
kills against slices, the order) are their own.

**Two layers, marked apart.** *Skeleton:* what any project's
registry would want. *Illustration:* this project's form of it.

## The shape

```markdown
### SL-<n> — <the invariant, in a few words>  `<status>`

- **Invariant:** <one statement, true in every readable state>
- **Adversity to create:** <each attack the evidence must stage,
  with the fact it comes from>
- **Area:** <the owner>. **Kills covered:** <numbers>
- **Folds in:** <each definition this slice consumes, or "none">
- **Flag riding:** <what cannot be staged the normal way, and what
  the evidence does instead; or "none">
- **Presumes:** <the slices this one stands on, and what it takes
  from each; or "nothing">
- **Judgment logged:** <why the slice is cut this way; or "none">
- **Record:** <a link to the slice's record>
- **Answered at close:** <one line per flag or judgment the slice
  settled: the answer in a sentence, and where in the record>
```

*Skeleton:*

- **Every field is present.** An empty one says "none" (or
  "nothing", for what a slice presumes), so a reader can tell
  empty from forgotten.
- **The status sits in the heading,** one of the registry's status
  values, and a closed slice gives its date.
- **The record is linked** from the entry, so a reader of the
  registry reaches the slice in one step.
- **At a close, one line per flag or judgment the slice settled,**
  under *Answered at close*. One sentence and a pointer; how the
  slice got there is its record's. An entry with nothing to answer
  says "none".
- **Framing's words stay as they were written.** A judgment the
  slice proved wrong is not rewritten; its answer sits beside it,
  so a reader sees both.

*Illustration here:*

```markdown
### SL-4 — consume's two moves hold together  `closed (2026-10-10, evidence)`

- **Invariant:** no readable state holds one of consume's two moves
  without the other. The two moves are ending the reservation and
  lowering the on-hand-count.
- **Adversity to create:** our own death between the two moves
  (F16); an unknowable outcome of the consume write (F19).
- **Area:** the reservation ledger. **Kills covered:** 15, 16
  (silence).
- **Folds in:** none.
- **Flag riding:** the evidence is not hammering. It kills an
  instance mid-work and injects an unknown outcome. Named here so
  the slice plans for it.
- **Presumes:** SL-3. Each exit leaves its own record, its receipt,
  so a half-done consume can be told from a done one.
- **Judgment logged:** kept apart from SL-3 because the proof
  obligations differ: "duplicates collapse" (SL-3) against "the
  half-done converges" (SL-4). The evidence differs with them.
- **Record:** [SL-4](../construction/sl-4-consumes-two-moves-hold-together.md)
- **Answered at close:**
  - The judgment: nothing converges. One commit leaves nothing
    half-done, and the evidence shows both moves or neither
    (record, "Whether anything is left to converge").
  - The flag: both were staged. An instance was killed mid-consume,
    and the store was frozen (record, the evidence as delivered).
```

## What to check at a close

Read the entry and ask:

1. Is every field present, an empty one saying "none"?
2. Does the heading carry the status, and a closed slice its date?
3. Is the record linked?
4. Does a closed slice's entry answer each flag and judgment it
   settled, one line each, with a pointer?
5. Where the entry differs from this shape: did it drift, did it
   find something better, or is it a different kind? The shapes
   rule says how each ends.

## Revisions

<!-- Dated lines: what changed, what taught it, and what was read
     from elsewhere. -->

- 2026-10-13 — written from run 3's four slice entries, after a
  look at where they differed and what they all lacked, each
  settled by the reviewer (RETRO.md, D29): every field present,
  "none" when empty; the record linked; one "Answered at close"
  line per flag or judgment settled. Taught by D5: two closes wrote
  ~70 lines of story into the registry, and once that story was
  removed, SL-4's entry still read its framing guess, "the half-done
  converges", as if it were open. Nothing read from elsewhere.
- 2026-10-14 — the example is the real SL-4 entry as it now reads,
  its answers as sub-bullets ("The judgment:", "The flag:"); the
  example still read the entry as it was before Step 10's pass 1.
  Found at Step 10's closing reading. Nothing read from elsewhere.
