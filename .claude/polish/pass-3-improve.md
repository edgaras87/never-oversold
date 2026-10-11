# Pass 3: Improve

<!-- The rules every pass follows are in 00-overview.md; this brief
     holds only what is pass 3's own. A sketch: finished when the
     pass opens. -->

Status: sketch
Branch: `polish-3-improve`

## Goal

The repo reads well to a stranger. Not only short sentences: the
explanations are clear, things are easy to find, references lead
somewhere useful, and every test says what it proves. The meaning
of every record stays the same.

## Inputs

- Pass 1's ideas list: improvements its readers noticed and did not
  act on.
- The reviewer's standing feedback: write to be read once — short
  sentences, one idea per sentence, worked examples with real
  numbers, the defined words and references kept.

## Lenses: one agent per kind of improvement

Each agent looks at the same files through one lens only, so each
catches what the others would miss:

| Lens | Looks for |
|---|---|
| Wording | Long or tangled sentences, words a stranger would not know |
| Explanation | Places a reader gets lost; where a worked example would help |
| Structure | Section order; what lives where; how a reader gets from the front door to the system, the slices and the evidence |
| References | Links a reader would want and does not get; citations (ADR, guarantee, kill numbers) that are hard to follow |
| Tests and code comments | Does each test's header say what it attacks and what it proves; does a comment say why, not what |

## How the lenses work together

The agents do not chat freely. They work in rounds:

1. **Propose.** Each lens reads the area and writes its proposals.
2. **Answer each other, once.** Each lens reads the others'
   proposals and marks the ones it agrees with, and the ones that
   clash with its own. Example: Structure wants to move a section
   that References just linked to.
3. **Merge.** The proposals are combined; agreements become one
   proposal, clashes go to the reviewer side by side.
4. **Choose.** The reviewer picks.
5. **Apply.** One writer, one commit per file, meaning unchanged.

The Wording lens's first job is a short style guide, agreed with
the reviewer before anything is applied. Where it lives once agreed
(a project rule beside `evidence-test.md`?) is decided then.

## Guarding tokens

Five lenses and a round of answers cost about ten agent runs per
area. So:
- **Pilot on one small area first,** e.g. README and one slice
  record, and see what the lenses find and what it costs.
- **One round of answers only,** never a second.
- Lenses that find little in the pilot are dropped for the rest.

## Fences

As in every pass: history keeps its words, the exports change only
by a dated revision entry, behaviour does not change. A delivered
copy improved here follows pass 2's rule-2 handling.

## Open

- Code names (classes, methods, tests): this pass, or a pass of
  their own?
- Which lenses survive the pilot.
