# Retro: what run 4 does differently

Started 2026-10-11.

## Goal

Run 3 stays as it is. We go through it part by part and decide
what could be done better. The lessons go to run 4: the same
project, rebuilt from the start.

A lesson counts only if it says where it lands, because run 4
starts from the bundle (skills, rules, playbook). A lesson that
stays in our heads is repeated in run 4.

Considered and rejected: fixing run 3 in place, as a walk-through
or the polish's passes 2 and 3 (each rewrite risks changing what a
record means, and the result is still text written badly the first
time); writing the change into TODO and PLAN on main first (the
reviewer chose a note on a branch, so main stays untouched until
the retro lands).

## Rules

- All work happens on the branch `retrospective`. Main is not
  touched.
- This note lives on `retrospective`.
- An experiment gets its own branch, cut from `retrospective`:
  `try/<what>`. If it works, it merges back. If not, it is deleted,
  and this note is untouched.
- Commits are free on `retrospective` and `try/` branches.
  Before anything lands on main, they are regrouped by kind.
- Each part is decided one decision at a time, each with a
  recommendation.

## Parts

Walked in the order run 3 made them, so each lesson lands where
run 4 will meet it.

| # | Part | Files | Status |
|---|---|---|---|
| 1 | The set-up | `.claude/CLAUDE.md`, `PLAN.md`, `TODO.md`, `.claude/decisions.md` | done (2026-10-11) |
| 2 | Framing | `docs/system/` (intent, definition, registry, derivation), ADR-0002 | done (2026-10-11) |
| 3 | Identity | ADR-0003, README's first lines | done (2026-10-11) |
| 4 | The ground | `compose.yaml`, `infrastructure/`, `docs/infrastructure/`, ADR-0004–0006 | done (2026-10-11) |
| 5 | The skeleton | `docs/construction/bootstrap-requirements.md`, `pom.xml`, ADR-0007–0009 | done (2026-10-11) |
| 6 | The slices | `docs/construction/sl-1` to `sl-4`, `.claude/shapes/slice-record.md`, `.claude/rules/evidence-test.md`, ADR-0010–0016 | done (2026-10-11) |
| 7 | Code and tests | `src/main/`, `src/test/`, the migrations | done (2026-10-11) |
| 8 | The front door | `README.md`, `ARCHITECTURE.md`, `CHANGELOG.md` | done (2026-10-11) |
| 9 | History | `devlog/`, `.claude/polish/` | done (2026-10-12) |
| 10 | The method | `.claude/skills/`, `.claude/rules/`, `docs/concept/` | done (2026-10-12) |

Status: not started → in progress → done (date).

## Do differently

<!-- One block per part: what we decided, why, and where it lands
     (a skill, a rule, the playbook, a shape, or run 4's own
     records). -->

### 1. The set-up

**D1. Less paperwork about the work.** (2026-10-11)
Run 3: ~150,000 words of Markdown for 768 lines of production
code; one step's story told in PLAN, the devlog, the slice record
and the commits.
- Each fact has one home. Others link to it, never retell it.
- As a rule, always loaded: `.claude/rules/one-home.md`.
- A PLAN step's gate says what, not how (D4). A closed step
  shrinks to one line.
- A devlog entry is what happened, the dead ends, where to resume.
  Test output lives in the slice record.
- The decisions log keeps its three-line entries, and a "rules in
  force now" list on top.
Considered and rejected: a gate keeping one short line per skill
stage ("Stage 0 passed … Stage 4 passed"), the agent's first
version; the reviewer saw it still copied the skill, and D4 put
goal facts in its place.
Lands in: the bundle (the one-home rule); the run playbook (step
form); the kit's recording convention (devlog, decisions log).

**D2. A plain voice from the first record.** (2026-10-11)
Run 3's records are dense: long chained sentences, coined words
never defined ("explain simpler" asked again and again). Fixing
the voice afterwards cost a polish pass and still was not done.
- A one-page writing rule: short sentences, one idea each, worked
  examples with real numbers, parts labelled not chained, defined
  words and references kept.
- Always loaded (no paths), for chat and records alike;
  `CLAUDE.local.md` points at it rather than repeating it.
- The method's own texts — skills, references, rules, concept —
  are written in it; the agent copies the voice it reads. Run 3
  showed it: "explain plainly" sat in memory from 2026-09-20, and
  the records still came out in the skills' voice.
- Each step's closing gate reads new and changed text against it.
- A glossary born at framing: each coined word defined once, in
  one line, when it first appears.
- A word is coined only when plain English cannot say it.
- Said plainly, not by metaphor: no "costume", "weather", "stage".
- Each kill carries one worked example with numbers: it is where a
  reader most needs to see the failure happen.
Considered and rejected:
- A rule loaded only with Markdown files (`paths:`), the first
  draft: it would miss chat.
- The voice kept only in `CLAUDE.local.md` or the agent's memory:
  run 3 had it in memory, and the records still copied the skills.
- A rule without plain sources: the agent copies what it reads.
- One rule for how a fact reads and where it lives: two questions,
  so two rules (writing, one-home).
- Choosing other styles now (specification, one kind per
  document): judged later on plain text (D14; part 8).
Lands in: the bundle (the writing rule; the skills, references,
rules and concept rewritten plain); cbc-framing (the glossary as a
framing output); the run playbook (the closing gate item).

**D3. The reviewer's way of working, in force from day one.**
(2026-10-11)
Run 3 learned five of the reviewer's preferences one correction at
a time, into the agent's memory — which is kept per project folder,
so run 4 would start without them.
- This repo's `CLAUDE.local.md` holds them: commit on the word;
  free commits only where granted, regrouped before landing;
  explain plainly; decisions one at a time with a recommendation;
  a plain manual beside any agent instructions.
- The reviewer moves that file to the deliverer, which delivers it
  to run 4.
- The agent's memory holds project facts, not the reviewer's ways.
Considered and rejected: a user-level `~/.claude/CLAUDE.md`, the
agent's first proposal (the reviewer chose `CLAUDE.local.md`,
carried through the deliverer, so it travels with the bundle);
leaving the preferences in memory (kept per folder; run 4 would
start without them).
Lands in: `CLAUDE.local.md`, here; then the deliverer, by the
reviewer's hand.

**D4. A trial names when it is judged; a gate says what, not
how.** (2026-10-11)
Run 3 put six arrangement pieces on trial, each "held until the
retrospective", so none was judged while the work ran. And its
step form said a step's own items derive "from the goal, the named
skill and the registry", which invited copying the skill: Step 8's
gate ran ~150 lines, most of it `cbc-slice` retold.
- Verdicts: one branch per step — keep. The reviewer's pace in
  `CLAUDE.local.md` — keep. The entry file under `.claude/` —
  keep. Gate items ticked as they come true — keep. The step
  form's standard opening and closing items — keep. The
  slice-record shape and the test rule — part 6.
- The gate answers "is it done?" The skill answers "how, and in
  what order?" Like an inspector's checklist beside a builder's
  schedule: the reviewer signs the checklist.
- The step form gains, between opening and closing, an instruction
  for a step's own items: ask of the goal "how would I check that's
  true?"; each answer one item — a fact, checkable by looking,
  what not how, together equal to the goal. The skill's stages are
  not copied in.
- The items are not an order of work. The skill sets the order; an
  item is ticked when it comes true, whenever that is.
- A step carries one "Now:" line, overwritten as the work moves,
  saying where it stands (e.g. "Now: Stage 2, the plan. Spec signed
  2026-10-09.").
- An instruction in the step form, not a rule: it is used once per
  step, when the gate is written, by the agent drafting and the
  reviewer approving, and the step form is where that happens.
- In run 4, a trial says at which step's close it is judged, and
  the verdict is one line: kept, changed, or dropped.
- (2026-10-13, at Step 10's build: "Now:" changes at a new stage,
  not at every commit; inside a commit plan, the plan and the
  branch's log show progress.)
Considered and rejected:
- Gate items as skill stages, one line each: they show where the
  work is, but can all be ticked while the goal is missed.
- Stage lines kept only for progress: they bring the how back; the
  "Now:" line gives progress without it.
- The instruction as a rule in `.claude/rules/`: used once per
  step, and a rule reaches only the agent, not the reviewer reading
  PLAN. Nor in PLAN's Standing rules, which hold daily practice.
Lands in: the run playbook (the step form; the kept pieces as
standard; the trial rule); the bundle's delivered-copies rule (how
a trial is marked).

### 2. Framing

Already landed: the three things framing missed in run 3 — where
the system runs, who may write the store (T4), the size it is
built for — were written back into `cbc-framing` on 2026-10-08
(372a1a5). Run 4 asks them at framing.

**D5. The exports hold the system, not how framing ran.**
(2026-10-11)
Run 3's registry carries ~55 lines of what SL-3 and SL-4 settled,
told again from their records; the definition carries how its
census reached saturation and the order its layers were derived in.
- The registry: slice rows, one "Now / next" line, the kill table,
  the revision log. A slice's close adds one line and a link to its
  record.
- The definition: the layers and their content. How framing ran
  stays in the derivation, linked.
- A slice row's own judgment and flags stay: they are about the
  slice, and its spec reads them.
- (2026-10-13, at Step 10's second look under D28: the saturation
  table stays in the definition. cbc-framing asks for the probe
  log in L1 so the census's completeness can be checked; it is
  evidence, not the story of how framing ran. Only the note on the
  order the layers were derived in goes.)
Considered and rejected: a D5 on the commit plan's cost, raised
while comparing it with PLAN; the reviewer had asked only how the
two relate, so it was parked for part 10 (it became D19).
Lands in: cbc-framing (the export templates); cbc-slice (what a
close writes into the registry).

**D6. One name per thing, in full words.** (2026-10-11)
Run 3 uses about twelve numbering schemes, mostly single letters
(F, W, T, P, G, E) a reader must learn. Three problems besides.
"V3" is a scope verdict in the definition and a migration in the
code. "G1" means a different guarantee in each slice record.
Concerns A–D are SL-1 to SL-4 under a second name. Example, from
the devlog: "V3 is G1's backstop" means migration V3 and SL-4's
G1; a reader of the definition reads a scope verdict.
- Tags are full words in capitals: the kind first, then a domain
  where it helps, then the number. Example: FACT-STORE-19.
- Layers put the number before the name: LAYER-4-KILL. A layer is
  a depth, and the depth is what a reader looks for.
- One spelling everywhere. Never short at the source and long
  elsewhere: a search must find every use.
- A number local to one record carries its owner outside it, owner
  first: "SLICE-4 GUARANTEE-1", never a bare "GUARANTEE-1".
- Numbers run once through a record, never restarting per section.
- A concern that becomes exactly one slice is called by the
  slice's tag. Letters only where it does not.
- Every tag kind is in the glossary (D2), one plain sentence each.

| Run 3 | Run 4 | What it is |
|---|---|---|
| F19 | FACT-STORE-19 | Something the world does that can hurt us, with its actor |
| T4 | ASSUMPTION-4 | Something taken as true without defending it |
| W2 | NOT-OURS-2 | Something that exists, which we neither own nor defend |
| V5 (verdict) | SYSTEM-BOUNDARY-5 | A boundary question, decided in or out |
| P4 | POSSESSION-4 | Something the system must own |
| kill 15 | KILL-15 | One way the promise can break |
| FC1 | FOLD-1 | A rule no danger attacks, folded into the first slice that needs it |
| concern A | the slice's tag | A group of kills proven together |
| L1 | LAYER-1-ENVIRONMENT | The world the system lives in: actors, facts, assumptions |
| L2 | LAYER-2-POSSESSION | What must be ours, and what we refuse |
| L3 | LAYER-3-OWNER | Who owns each possession inside the system |
| L4 | LAYER-4-KILL | Where facts collide with the promise, and what breaks |
| L5 | LAYER-5-BETWEEN-OWNERS | What passes between owners |
| SL-4 | SLICE-4 | One guarantee set proven against one danger |
| G1 | GUARANTEE-1 | A guarantee inside a slice |
| E1 | EVIDENCE-1 | An evidence test inside a slice |
| R5 | READINESS-5 | A check run before a slice starts |
| Q1 | QUESTION-1 | A reviewer's question during framing |
| PL0 | PROBE-LENS-1 | A way of searching for facts not yet listed |
| V3 (migration) | MIGRATION-V3 | A database migration |
| C1 | CONSTRAINT-STORE-1 | A rule the store enforces on itself |
| ADR-0014 | ADR-0014 | An architecture decision |

"Tripwire" stays the name of a test that pins a decision; the
glossary defines it.
Considered and rejected, the scheme:
- Letters everywhere (F, W, G): needs the glossary after a month
  away.
- Full words for framing tags, letters for G and E (the agent's
  recommendation): the reviewer chose full words everywhere, for
  reading.
- Short at the source, full when cited elsewhere: two spellings,
  and a search finds only one.
- Abbreviations (GRNT, EVDC): still to be learned, and harder to
  read than either form.
- The owner written "SL-4/G1" or "G1 of SL-4": the space reads
  and searches best.
Considered and rejected, per tag:
- TRUST: also means security trust. ASSUMPTION instead.
- FENCE: sounds like a protective test, and "tripwire" keeps that
  meaning. EXCLUDED: sounds as if the thing does not exist. OUT,
  OURS: unclear in a sentence. NOT-OURS and POSSESSION instead.
- SCOPE: "scope of what?". PROJECT-BOUNDARY: the verdicts are
  about the system, not the repo. SYSTEM-BOUNDARY instead.
- DOOR: "door" already means the HTTP API. DEFINITION: names the
  heading, not the thing. FOLD kept; the glossary explains it.
- FAILURE for KILL, NEVER for GUARANTEE, PROOF for EVIDENCE: the
  reviewer kept the method's words.
- FLYWAY-V3: names the tool. MIGRATION-V3 names what it is.
- SL: the only abbreviation left. SLICE instead.
- Layers by name only (ENVIRONMENT-LAYER): hides the depth.
  LAYER-ENVIRONMENT-1: matches FACT, but buries the depth. L4-KILL:
  a letter again. LAYER-4-KILL instead.
- QUESTION and PROBE-LENS left as run 3 wrote them: renamed, with
  numbers running once through the record.
Lands in: cbc-framing (the tags in its export templates; the
glossary); cbc-slice (GUARANTEE, EVIDENCE, READINESS, the owner
rule); the concept chapters (the layer tags); the writing rule
(the owner rule; its examples move to these tags when it lands in
the bundle).

**D7. Framing writes its results once, into the exports.**
(2026-10-11)
Run 3 wrote each framing result twice: first in the derivation,
then again in the exports at close. 6,673 of the derivation's
8,811 words are results copied this way. The copies drift. Kill 17
in the derivation (line 579): "on-hand-count lowered by units the
caller never held; reserved exceeds on-hand-count for the others."
In the definition (line 396): "the count lowered by units the
caller never held." The definition lost half the consequence, and
only comparing the two files shows it.
- Each step's result goes straight into its export, as a draft,
  when the reviewer gives the verdict. Example, in the definition:
  "FACT-CALLER-7. A caller consumes a reservation that is not
  theirs, or claims a quantity other than the reservation's → a
  lie at the door."
- The derivation keeps how each step ran, and links to the result.
  Example: "Step 2, 2026-09-10. Considered 'a caller sends a
  negative quantity'; folded into FACT-CALLER-7. Reviewer asked:
  is a wrong reservation id the same fact as a wrong quantity?
  Yes: both are a caller lying at the door. Verdict: accepted.
  Results: definition, LAYER-1-ENVIRONMENT."
- A return trip changes the export and adds one line to the
  derivation saying what changed and why.
- Close checks the exports read alone, then ends their draft state.
  Nothing is composed.
- (2026-10-13, at Step 10's comparison: six places, not one. Four
  kills had lost what dies (4, 5, 17, 18), F16 its link to the
  promise, and L2's edge changed meaning: "a reservation not ours"
  became "not the caller's own", a check the system never makes.
  Copying does not only shorten; it rewords, and a rewording can
  turn true into false.)
Lands in: cbc-framing (the derivation's shape; the steps write to
the exports; the close); the concept's framing chapter (where a
step's result lives).

**D8. The briefing is kept in the repo.** (2026-10-11)
Run 3's briefing, the input that started framing, sat in
`temp/briefing-run-3.md`, untracked. The folder is now empty.
Five records cite it: ADR-0002, ADR-0003, the registry, the
derivation, PLAN. None can be checked against it. Example: the
registry says "Divergences from the briefing: none", and nothing
is left to compare.
- The briefing is committed when framing opens, unchanged:
  `docs/system/framing/briefing.md`, beside the derivation.
- It is history: never edited after that commit.
- Anything private is removed before the commit, and the file's
  first line says what was removed.
Considered, as the fallback if a briefing must stay private: the
intent quotes its fixed points instead. Not needed unless the
reviewer says so.
Lands in: cbc-framing (framing opens by committing the briefing);
the run playbook (the framing step's opening item).

### 3. Identity

**Kept as it is.** (2026-10-11) The name came after the promise
and from it: never-oversold is the promise's negation. ADR-0003
held each candidate against a four-point bar set before naming,
and wrote down why each one lost, so "why not safe-reservations?"
has an answer. One step, one ADR, 321 words of gate. Its voice is
D2's to fix; nothing else changes.

### 4. The ground

**D9. The ground proves what the store promises; the harness proves
its own levers.** (2026-10-11)
Run 3's ground step (Step 3) proved three levers with containers:
start several (`--scale`), kill one (`podman kill`), freeze one
(`podman pause`). The harness came a step later (ADR-0009) and
chose another way: it forks the ledger's instances as processes and
kills them as processes. So two of the three proofs were never
used. The operator manual still describes them, under "The levers
the evidence pulls", with a note that the evidence pulls them its
own way.
- The ground step proves what the store itself promises: the
  CONSTRAINT-STORE rules, each seen refused, and the facility a
  slice will need (two writers on one row, one made to wait).
- A lever the evidence pulls is proven where the harness is built
  (the bootstrap), in the form the harness uses. Run 3's freeze is
  the example that worked: the harness pauses its throwaway store,
  and that is the form that was needed.
- What run 3 did well, kept: one home for the role split and the
  migrations, read by both the compose ground and the tests'
  throwaway store (`ThrowawayStore.java` reads `bootstrap.sql` and
  the migrations folder).
Lands in: infra-establish (what its verify step proves);
cbc-bootstrap (the harness proves its levers).

### 5. The skeleton

**D10. A step's contract becomes history at its close.**
(2026-10-11)
Run 3's bootstrap requirements were the bootstrap's contract,
certified at its close and "kept as it was certified". But the
records table still sends a reader there for "what a slice may rely
on from the bootstrap", so it is read as current. It is not: its
banner lists what SL-1 changed since, and the reader applies those
changes in their head. Example: §3 says the migrations home is
"honestly empty"; it holds V1 to V4. Meanwhile ARCHITECTURE already
describes the harness and the store as they are now. And §3 tells
again what ADR-0007, ADR-0009 and the infrastructure contract
already say: one database identity, no schema control in the app,
a self-contained test suite.
- The requirements are written before the code, and certified
  against it at the close. After that they are history, like a
  closed ADR: never updated, and no banner.
- What a slice may rely on now is ARCHITECTURE's: the store, the
  ledger, the harness, as they stand. The records table points
  there.
- The requirements link to the ADRs and the contract for each
  constraint, and state only what is new: the capabilities, the
  exclusions, the evidence owed.
- What run 3 did well, kept: the decisions came before the code
  (ADR-0007 to ADR-0009), and ADR-0009 left the harness's form
  (processes or containers) to the build, where it was cheap to
  see. (2026-10-12: but the why reached only the devlog; D26.) The
  probe the harness first raced died at SL-1 as planned;
  the harness itself survived and grew.
Lands in: cbc-bootstrap (the requirements' status after close;
what they restate); the kit's records table (who answers "what may
a slice rely on").

### 6. The slices

**D11. A slice record keeps each guarantee in one block.**
(2026-10-11)
Run 3's slice records are laid out by stage: guarantees (§4),
evidence criteria (§5), owners (§8), faces (§8), evidence as
delivered (§9). So one guarantee is spread over five places.
Example, SL-4 GUARANTEE-2, "a reader sees a consume whole or not at
all": the claim and the attack at line 180, its test at 291, the
claim again and what holds it at 427, the options weighed at 547,
the test names at 790. To answer "how is this held, and how was it
proven?", a reader reads all five.
- One block per guarantee, holding, in order: the claim; what could
  go wrong and what we guarantee, with numbers; the evidence
  criterion; what holds it and its backstop; the options weighed;
  the tests that proved it.
- The block grows by stage. The specification writes the first
  three parts, the plan adds the next two, the close adds the
  tests. The reviewer signs a stage by reading the parts it added.
- What spans guarantees keeps its own section: invariant,
  adversity, what the slice does not claim, the shape the walls
  need, escape hatches, the surface, deviations, the red run,
  standing guards, sign-offs.
- The claim is written once, at the top of its block. No section
  restates it.
Considered and rejected: cutting the record's length. The sections
are not copies: the attack, the wall and the test say different
things. The cost is the reader's hunting, not the words.
Lands in: cbc-slice (the record's layout, and what each stage
adds); the slice-record shape (its three shapes become parts of
one block).

**D12. The slice-record shape becomes one block per guarantee, and
the close gate checks against it.** (2026-10-11)
The verdict D4 left to this part. In run 3 the shape was read only
at a slice's close, never while writing: it was being learned. It
was written from SL-2's record and revised three times, each from a
real miss. Example: SL-3 GUARANTEE-5's backstop was named and did
not hold, and nothing in the record said it had never been seen
working. Since 2026-10-08 a backstop says "seen at the red run" or
"unproven". SL-3 and SL-4 read alike because of it.
- The shape is rewritten to D11: one block per guarantee, its parts
  in order (claim; what could go wrong and what we guarantee; the
  evidence criterion; what holds it and its backstop; the options
  weighed; the tests). Run 3's three separate shapes (a guarantee,
  an owner, a face) become parts of that block.
- Its rules for the parts stay: the chosen option first and marked;
  a cost says the size it assumes; a backstop says "seen" or
  "unproven"; an option changed after signing keeps its dates.
- Labelled parts and worked numbers leave the shape: the writing
  rule says them (rules 3 and 4).
- It stays a gate check: each slice's closing gate reads the record
  against it. What changes is that run 4 starts with it. It is
  delivered with cbc-slice, so the first slice is checked against
  what run 3 learned, not against nothing.
- The skill and the shape split the work. cbc-slice carries the
  record's outline: its sections, one block per guarantee, and
  which stage writes which part (headings only). The shape says
  how each part reads, and is checked at the close. So the first
  slice is written grouped by guarantee, and checked in detail at
  its close.
Considered and rejected: shipping the shape as cbc-slice's
template, used while writing (the agent's first version). The
reviewer pointed out that it is a gate check, and that the skill
needs its own outline so the first slice is grouped from the
start.
Lands in: the slice-record shape, rewritten (delivered beside
cbc-slice); cbc-slice (the record's outline); cbc-slice's close
(the gate reads the record against it); the writing rule (it keeps
what the shape shared with it).
The shapes convention around it (`shapes-lifecycle.md`, 735 words,
for one shape) is part 10's.

**D13. The test rule is kept, and loads from the first slice.**
(2026-10-11)
The other verdict D4 left to this part. `evidence-test.md` loads
whenever a file under `src/test/` is touched. It was written at
SL-3, because a lesson written in a slice record was not read while
writing tests. Example: SL-2's first red failed on a 500, not on
the numbers, so it proved nothing. SL-2's record said so. SL-3's
first red failed on a 500 again, and four of its tests lacked the
promise check. After the rule: SL-4's tests followed it, and its
close added two lessons rather than fixing old ones.
- Kept as it is: the witness asserted before the door's answer; the
  promise on every reading after the adversity; the slice's own
  invariant next; exact numbers after; numbers in the labels; each
  test says on its first line what it is.
- Kept: the split between skeleton (any project) and illustration
  (this one's classes and numbers), so another project can take
  the skeleton.
- Run 4 has it from the first slice, not from the third.
- Its labels follow D6 and carry their owner, since a test is
  outside the record: "SLICE-4 EVIDENCE-3 · GUARANTEE-2 · KILL-12",
  not "E3 · G2 — kill 12".
Lands in: the bundle (the test rule, delivered with cbc-slice);
D6's tags in its labels.

**D14. No specification style; a guarantee's claim says what
happens, condition first.** (2026-10-11)
Judges the parked question. Run 3's 22 guarantee claims are short,
but some are titles rather than claims. The body under each is
concrete; the claim line above it is not. Example, SL-1:
- GUARANTEE-6, as written: "Nonsense never reaches the decision."
- Plain: "A request with an unknown item, or a quantity that is not
  a whole number from 1 to the bound, is refused before the ledger
  decides, and moves nothing."
- Specification style: "If a request names an unknown item or a
  quantity outside 1 to the bound, the ledger shall refuse it
  before deciding."
The plain version is as exact. "Shall" adds a word and nothing
else, and most guarantees are "never" properties, which have no
trigger to put after "when". Claims that already read well put the
condition first: SL-3 GUARANTEE-1, "When exits race on one
reservation, exactly one of them takes effect."
- Plain stays the style for guarantees and gate items.
- A guarantee's claim says what happens to what, in this system's
  nouns: the condition first where there is one (when, while, if),
  then the outcome a test can see. No abstract nouns standing for
  the mechanism ("nonsense", "the truth", "a decision exists only
  as a record").
- The question is asked once more at run 4's first slice close, on
  claims written this way.
Lands in: the slice-record shape (the claim line's form); the
writing rule's "Other styles" (no new style named).

### 7. Code and tests

**D15. A code comment says what the code relies on, and links to
the argument.** (2026-10-11)
In `src/main`, 257 of 668 lines are comments. Much of it retells
the slice record. Example: `Ledger.reserve`'s comment (20 lines)
repeats SL-1's owner block for GUARANTEE-1 nearly word for word:
"two racers update one row; the store serializes them and the
second re-evaluates the condition against the first's result".
And the code cites 21 bare local tags ("G4", "E5 · G2", "F21")
with no slice: `NoInstanceStateOrClockTest` says "G2" and never
says it is SL-1's.
- A comment says what the code relies on that the code cannot
  show, in a few lines, and names the guarantee with its owner.
  The argument stays in the slice record.
- Example, `Ledger.reserve`, 20 lines to 5: "Admits a reservation
  if its units still fit, else refuses. The check and the write
  are one UPDATE, so the store runs racing admits on one item one
  after another, and only those that still fit change the row.
  Holds SLICE-1 GUARANTEE-1."
- Tests keep their story with numbers (D13): a test is read on its
  own when it goes red.
- What run 3 did well, kept: migrations never edited once
  committed (V1 to V4, one commit each); absence guards read the
  bytecode (ArchUnit), so a reformat or a wrapper cannot slip
  past; one migrations folder for the ground and the tests.
Lands in: the one-home rule (a comment links to the argument);
D6's owner rule, applied to code and tests; cbc-slice (what the
build writes in comments).

### 8. The front door

**D16. ARCHITECTURE describes the system by rule, not by slice.**
(2026-10-11)
ARCHITECTURE says it describes "the system as it is now", in one or
two pages. It runs to 2,031 words. Its "Invariants" section (634
words) tells each slice's story again: "SL-2 ... enforced by the
same two owners SL-1 built, justified here against a different
adversity ... No structure was added: the slice's record §8 says
why." That is how the work went, and the slice records hold it.
Below it, the "Responsibilities" table (304 words) already says
what is true now, one row per rule. Example row: "never more held
than on hand | computed by the ledger's conditional update |
refused by the store's `item_never_oversold` | guarded by the
reserve storms".
- The table is the invariants section: one row per rule, with what
  computes it, what refuses a wrong result, and the test that goes
  red. Each row links to the slice that proved it.
- The per-slice story leaves ARCHITECTURE; it is the slice record's.
- A slice's close adds or changes rows. It never adds a paragraph.
- This matters more after D10: ARCHITECTURE answers "what may a
  slice rely on now?", so it must stay short and current.
- What run 3 did well, kept: README summarises each invariant in
  two lines and links to its record; CHANGELOG writes for users
  ("answers 503, outcome unknown"), and its versions count closed
  slices (0.4 = four closed).
Lands in: the kit's ARCHITECTURE template (the invariants as a
table); cbc-slice (what a close writes into ARCHITECTURE).

### 9. History

**D17. Each close checks what the polish swept for, so no sweep is
needed.** (2026-10-11)
After SL-4, run 3 ran a clean-up pass over the whole repo: 8
agents, 720,000 tokens, 20 fixes. Most fixes were drift a step's
close could have caught, because that step caused it:
- `pom.xml` still said `0.3-SNAPSHOT` after SL-4 closed. The version
  had moved at every close but that one. Found only by comparing
  facts across files.
- ARCHITECTURE said the ledger holds "three of its four"
  invariants. All four were closed.
- SL-4's record said "Today nothing at the door handles a lost
  store". The 503 was built.
- SL-3's evidence table called consume × release kill 14. It is
  kill 13.
And 11 of the devlog's 28 entries are housekeeping: writing passes,
error checks, re-pins, the polish. That is work on the records, not
on the system.
- Mechanical checks become a script, run at every close: links and
  paths resolve; every ADR, slice and kill number cited exists; each
  pair of copies agrees (the version in `pom.xml` and CHANGELOG).
  This is the "check" the one-home rule's rule 5 asks for.
- Judgment checks become one closing gate item, on the records the
  step touched and the living records that mention its subject: no
  line still says "now", "today" or "not yet" about something the
  step changed.
- The writing rule's check (D2) runs at the same close.
- A whole-repo sweep is then a sign that a close missed something,
  not a planned phase.
Lands in: the run playbook (the closing items); the bundle (the
check script, with the one-home rule).

### 10. The method

**D18. The shapes rule shrinks to what was used.** (2026-10-12)
Takes up the parked question. `shapes-lifecycle.md` is 735 words
and run 3 had two shapes. What it holds, and what run 3 used:

| Part of the rule | Used in run 3? |
|---|---|
| A shape is written from work that exists, never in advance | Yes: both shapes came from slices already written |
| Two places: read at a gate (`.claude/shapes/`), or loaded when matching files are touched (`.claude/rules/` with `paths:`) | Yes: the slice record at the gate, the test rule loaded |
| The reviewer moves a shape between the two | Yes: the test rule went straight to loaded, on the reviewer's word |
| A difference ends one of three ways: the shape was wrong, the output drifted, or each had something | Yes: each revision line names which |
| At each close, look in `temp/` for shapes the deliverer staged | No: checked at two closes, nothing there either time |
| Offer shapes back, mark skeleton apart from illustration | Once: the test rule's skeleton |

- Kept, in under a page: written from work that exists; the two
  places and what each does; the reviewer moves a shape; the three
  ways a difference ends.
- Dropped: the `temp/` staging step and its closing gate item.
  With D12 and D13, run 4's two shapes arrive in the delivery
  itself, like any other rule.
- Offering back stays the delivered-copies rule's job, which
  already handles edits handed back to the deliverer.
- (2026-10-12, at Step 10's triage: all shapes in one folder, read
  at the gate only; a fourth way a difference ends, a new shape;
  the test file's red-run rule left to cbc-slice. The reviewer's
  reason: a test written without the shape in view can teach the
  shape, or show a new one.)
- (2026-10-13, at Step 10's commit 5: a gate-only shape is safe
  for form, not for work. A part the work must produce, like a
  backstop "seen at the red run", cannot be added at the gate if no
  one looked during the run. So when a shape names such a part, the
  skill that runs the work must ask for it too.)
Lands in: the bundle (the shapes rule, shortened; the step form
loses the `temp/` item).

**D19. The commit plan stays, and gains small steps and a look
back.** (2026-10-12)
Takes up the parked question. The commit plan worked: it gives the
target up front, and is revised as the work shows more, the way a
Proposed ADR is drafted early and edited during the run. Run 3's
17 revisions are that adjusting, not a fault. What it lacked was
room to try. Each planned commit landed as one commit, so trying
two ways meant reworking a commit in place. And nobody looked at
the finished set as a whole: the polish did, after the fact, and
regrouped 36 small commits into 7.
- A planned commit may be built as several small commits: tries,
  fixes, a way abandoned. Before the planned commit's boundary,
  they are squashed into the one commit the plan names, and the
  reviewer reviews that.
- The small commits are kept on a local `<branch>-detail` branch,
  as run 3's polish did, so nothing tried is lost.
- Before the plan closes, look back at the landed commits as a
  whole. Where a better grouping shows (two that are one change, or
  one that is two), propose it to the reviewer, regroup on the
  word, and revise the plan to match what landed.
- So grouping happens twice. At each boundary, small steps fold into
  their planned commit. At the end, the planned commits are grouped
  again if the whole shows a better split. The end look sees only a
  few planned commits, never the pile of small ones.
- Unchanged: the plan up front; the plan revised as the work
  moves; its open, revise and close commits; the reviewer's pace.
- (2026-10-15, at Step 10's close: the small steps were lived, and
  three things the decision did not foresee. Commits inserted after
  the plan was written took letters (8b, 22b, E2.1 to E2.12), so
  the numbers already cited stayed true. One planned commit could
  repeat over a set of records, with an order and a stop rule: the
  plain pass ran three times. And the small commits must follow
  commit-messages when written: sixteen subjects over 50
  characters were found only at the close. All three went into
  `commit-plan`.)
Considered and rejected:
- Retiring the commit plan for free commits regrouped at the end
  (the agent's first version). The reviewer kept it: the up-front
  target works, and its revisions are the adjusting, as with a
  Proposed ADR edited during the run.
- Squashing only at the end: a pile to review at once.
- Squashing only at each boundary: nobody looks at the whole.
  Both are kept.
Lands in: commit-plan (small steps inside a planned commit; the
look back before close).

**D20. Run 3's own hand-off lists close into this note.**
(2026-10-12)
Run 3 kept two lists meant for "the retrospective": TODO's "To the
deliverer" (9 items) and the decisions log, whose header says each
of its 38 entries "graduates to the deliverer, stays local, or
dies" at the retrospective. Left as they are, they are a second
home for lessons this note now holds. And the log misled this
retro once: its entry of 2026-09-07 says delivered copies change
"only by copying anew", a rule a later entry replaced, so it was
read as still in force.

| TODO "To the deliverer" item | Answered by |
|---|---|
| Four arrangement pieces on trial | D4 (kept) |
| Gate items ticked as they come true | D4 (kept) |
| Should a rule be deliverable on trial? | D4 (a trial names the close that judges it) |
| The slice-record shape | D12 |
| The evidence-test shape | D13 |
| The method has no glossary | D2, D6 |
| PLAN's Release step, reshaped at birth | open: run 3's Release was not run |
| `infra-establish` silent on the store's facility | open: held until a second run meets it; run 4 is that run |
| The absence rung | open: the same |

- When the retro lands, each answered item in TODO is replaced by
  one line naming its decision here. The three open ones stay.
- The decisions log is read top to bottom once, and gets the "rules
  in force now" list on top that D1 asks for. An entry whose rule
  is in that list, or in a D here, has graduated; one whose rule
  was replaced is marked so in the list. The entries stay as
  history.
Considered and rejected: leaving both lists as they are, with this
note the only place that knows what is answered. The log had
already misled this retro once.
Lands in: TODO and `.claude/decisions.md`, here, when the retro
lands; D1's "rules in force now" list, first used here.

**D21. A skill says each thing once: SKILL.md or its reference,
not both.** (2026-10-12)
The one-home rule, applied to the method's own texts. cbc-slice
tells its stages twice: in SKILL.md, and again in
`references/cbc-slice-workflow.md`. 18% of SKILL.md's text appears
word for word in the workflow reference, and more is the same
paragraph reworded. Example, in both: "One recorded as an ADR is
Proposed until the slice's closing records, then Accepted: the
build can still reshape it, and a Proposed record is corrected in
place where an Accepted one could only be superseded." So when a
lesson changes a stage, two files must change, and the delivered-
copies log records two edits for one lesson. cbc-framing shows it
can be done: 1% shared.
- SKILL.md holds what the agent does at each stage, and each gate,
  short.
- A reference holds only what SKILL.md does not: the longer why,
  worked examples, templates. It links back to the stage it serves
  and does not retell it.
- The concept chapters explain the method to a reader. They are
  not checked against the skills, and they are allowed to say the
  same idea in their own words: they are written for a different
  reader.
- (2026-10-13, measured again at Step 10 as phrases of 8 words or
  more shared between SKILL.md and its references: cbc-slice 33%,
  the others 2–5%. The 18% above was counted another way; whole
  matching sentences give 1%.)
Lands in: the bundle (cbc-slice's SKILL.md and workflow reference,
when D2's plain rewrite reaches them; the same check for
cbc-bootstrap and infra-establish).

### Delivering the retro

**D22. What must happen during run 4 is a gate item in run 4's
PLAN, at the step that judges it.** (2026-10-12)
Run 4 starts with no memory of run 3 and does not read this note.
Most decisions here change the bundle's text, so run 4 follows them
without needing the reason. A few cannot: they are checks that only
run 4's own work can answer. Left only in this note, run 4 never
meets them.

| Decision | What run 4 must do | At which step |
|---|---|---|
| D4 | Each trial names the close that judges it | Wherever a trial starts |
| D14 | Read the guarantee claims once more for specification style | The first slice's close |
| D20 | Note whether `infra-establish` leaves the store's facility unsaid, met unprompted | The ground's close |
| D20 | Note whether a guarantee is held by an absence (the absence rung) | Each slice's close |
| D20 | Release's gate in run 3's reshaped form (a Goal line, the step form) | Release |
| D18 | Shapes are read at the gate only: did that cost rework, or teach something? | The second slice's close |
| D28 | The copies edited untried in run 3 (cbc-framing, cbc-slice, cbc-bootstrap, infra-establish) run for the first time: note where their new wording did not hold | Each step that runs one |
| D29 | The kill and registry-entry shapes are read for the first time at a gate: did they find anything? | Framing's close; the first slice's close |

- The deliverer writes each into the playbook's PLAN, as a gate
  item at its step, with one line of reason and a pointer back to
  this note. Example, at the first slice's close: "The guarantee
  claims are read once more for specification style. Run 3 said no
  (its RETRO.md, D14); this is the first chance to judge on claims
  written plainly."
- Everything else lands in the bundle's text, and run 4 meets it
  there.
- (2026-10-15, read once more against every decision at Step 10's
  close: two rows added. D28's untried edits are first used by run
  4, and D29's two shapes are first read at a gate there.)
Considered and rejected: leaving these checks only in this note.
Run 4 does not read it.
Lands in: the run playbook (the PLAN that run 4 is born with).

**D23. The retrospective becomes a skill and a PLAN step.**
(2026-10-12)
Run 3's PLAN ends on "Retrospective (fill at project end)": seven
questions and "fold lessons into the playbook". This retro did far
more, and none of it was written down as a way of working: walk the
run part by part in the order it was built; back each finding with
a real example; decide one at a time with a recommendation; say
where each decision lands; keep what was considered and rejected;
park questions to the part that owns them; try changes on `try/`
branches; close the run's own hand-off lists; turn the next run's
checks into its gate items (D22). And run 3 stopped before
Release, so its retro happened only because the reviewer asked.
- A skill, `retrospective`, holds the how: the steps above, in
  order. A plain manual for the reviewer sits beside it: what the
  retro is, how it runs, how it decides.
- A PLAN step names the skill and holds the gate, as every other
  step does. Example items: every part has a status; every decision
  says where it lands; the run's hand-off lists point to the
  decisions; the next run's checks are its gate items.
- The step comes after the last slice and before Release, so it
  happens even when Release is skipped. Release gains one closing
  item: its own lessons are added to the retrospective's note.
- It replaces the "Retrospective (fill at project end)" section.
- The skill and the manual are drafted from this retro, on
  `try/retro-skill`, so the deliverer gets a working text.
- The step, drafted in run 4's step form (D4: goal facts, no
  order, one "Now:" line; D18: no `temp/` item):

```markdown
## Step <n>: Retrospective  (retrospective)    [ ]

Goal: every lesson of this run that the next run should act on is
a decision in RETRO.md, with an example from the run and the place
it lands; the run's own hand-off lists point to those decisions;
and what only the next run can check is listed for its PLAN.
Now: <where the work stands, one line>
Gate:
- [ ] The branch `retrospective` is cut from main, and the step
      has not been worked anywhere else.
- [ ] This gate was written before the work started.
- [ ] RETRO.md's parts table lists every part of the run, each
      marked done with its date.
- [ ] Every decision has an example from the run and a "Lands in"
      that names a skill, a rule, the playbook, a template or the
      reviewer's local file.
- [ ] Every Parked item is judged, or names the part or the next
      run's step that takes it.
- [ ] Every `try/` branch is kept or dropped, and the note's
      Experiments line says which.
- [ ] Every item in TODO's "To the deliverer" that a decision
      answers is one line naming it; one line asks the deliverer
      to apply the note.
- [ ] The decisions log opens on "rules in force now", with what
      each rule replaced.
- [ ] What only the next run can check is listed in the note, each
      with the step that judges it.
- [ ] The commits are regrouped by kind before landing; the small
      ones are kept on `retrospective-detail`.
- [ ] <the closing items, as every step>
Notes: opens when the last slice closes, before Release, so it runs
even if Release is skipped. The reviewer decides each decision,
each experiment, and when the branch lands.
```

- Release gains one closing item:

```markdown
- [ ] Release's own lessons are added to RETRO.md, as decisions in
      the same form.
```

Considered and rejected:
- Only a PLAN step: a gate says what is true at the end, not how
  to walk the run; the how would be lost, or copied into the gate
  (against D4).
- Only a skill: nothing says when it runs. Run 3 shows the risk.
- The step after Release: it covers Release, but a run that stops
  before Release gets no retro at all.
Lands in: the bundle (the `retrospective` skill and its manual);
the run playbook (the step, its place before Release, and
Release's closing item).

**D24. A retrospective is followed by an apply step.**
(2026-10-12)
This retro was told to find what could be better, never to change
anything; the reviewer kept looking and changing apart on purpose,
and the look stayed clear. But run 3 would then be released while
its records still read in a way the retro showed could be better:
the dense voice (D2), facts told twice (D1, D5, D7, D10, D15, D16),
slice records spread over five sections per guarantee (D11).
- After the retrospective, a step applies it to the run itself,
  before Release.
- Triage first, one decision at a time: apply here, next run only,
  or partly. The test: apply what improves what the run hands over
  without changing what it proved. What changes how a step runs
  waits for the next run, since the run's steps are done. History
  is never touched; the exports change by a dated revision.
- Delivered skills and rules may be among what is applied, under
  `delivered-copies.md` rule 2, each with its TODO line asking the
  deliverer to compare with the bundle. Concept chapters never.
- Then a commit plan, ordered by how the changes affect each
  other. Example: remove repeats before rewriting (no polishing
  text that is about to go); regroup slice records before
  rewriting them; rename tags before the words around them change;
  the plain rewrite last; the drift check (D17) at the end.
- Built with D19's small steps inside each planned commit, the plan
  checked after each and revised when it no longer holds.
- The deliverer reads the run once, after this step, so the
  decisions it takes have been tried on real text.
- (2026-10-15, at Step 10's close: what applying taught. Owners
  before summaries: a record that summarizes others is revised
  after them, README last. A plain pass is repeated until it
  changes nothing that matters: pass 2 reads each record against
  the revised ones without rewriting, and pass 3 runs only over
  what pass 2 changed. Each rewrite is checked by a fresh reader,
  a second only if the fixes were more than a few. Concept chapters
  may now be edited from a lived mistake (D28). The way is kept as
  a skill, `apply-retrospective`, its own and not a part of
  `retrospective`, since one changes the run's records and the
  other must not.)
- The step, drafted for the next run's PLAN in its step form,
  beside D23's:

```markdown
## Step <n>: Apply the retrospective  (apply-retrospective)    [ ]

Goal: the run hands over what its retrospective showed could be
better, wherever that changes how its records read and not what it
proved; what happened to each decision is recorded.
Now: <where the work stands, one line>
Gate:
- [ ] The step's branch is cut from main, and the step has not been
      worked anywhere else.
- [ ] This gate was written before the work started.
- [ ] RETRO.md's "Applied to <run>" has one row for every decision:
      applied here, next run only, or partly, each with its reason.
- [ ] Every row applied here, or partly, names the commits that
      applied it.
- [ ] What the run proved is unchanged: the test suite is green,
      with as many tests as at the last slice's close.
- [ ] History keeps its words: no commit edits a devlog entry, a
      closed ADR's body, a closed step's gate, a released CHANGELOG
      version or a migration.
- [ ] The exports change only through dated revision entries.
- [ ] Every delivered copy edited here has its decisions-log entry
      and its TODO line, marked untried where the copy was finished.
- [ ] Every link and path in the records resolves.
- [ ] No living line still says "now", "today" or "not yet" about
      something that has since changed.
- [ ] What trying a decision taught is written back into RETRO.md,
      dated, on the decision it changed.
- [ ] What the step produced is read against every shape that
      governs it, and every difference is settled.
- [ ] Every commit follows commit-messages, and none straddles agent
      and project paths.
- [ ] The commits are regrouped by kind before landing; the small
      ones are kept on `<branch>-detail`.
- [ ] The step reached main by fast-forward, on the reviewer's word.
Notes: opens after the retrospective, before Release. The reviewer
decides each triage row and approves the commit plan before any
change.
```
Considered and rejected:
- Applying during the retro: looking and changing at once loses
  the clear look.
- Releasing without applying: ships what is known to read worse
  than it has to.
- The deliverer first: the decisions reach the bundle untried, and
  the deliverer reads the run twice.
Lands in: the retrospective skill and its manual (the step after
the retro); D23's PLAN step (a step after it, before Release); the
run playbook.

**D25. A new kind of work starts from a provisional manual.**
(2026-10-12)
Run 3 did two things it had never done before: the retrospective
(Step 9) and applying it (Step 10). Neither had a skill. The
retro's skill was drafted at its close, from what had happened
(D23). For Step 10 the agent first proposed updating the skill
before doing the step. The reviewer stopped it and asked for a
provisional manual, changed as the work goes, with the skill built
at the close.
- The steps: `CLAUDE.local.md`, "New kinds of work".
- Why: a skill written before the work describes a guess. Written
  after, it describes what worked.
Considered and rejected:
- The skill first: a guess, written down before it is tried.
- A rule in `.claude/rules/`: a second home, since
  `CLAUDE.local.md` already loads every session.
- No record: the file is not tracked, so the deliverer would not
  know the way exists.
Lands in: `CLAUDE.local.md`; the deliverer, by the reviewer's hand,
who may fold it into the playbook.

**D26. The evidence runs the system in the form it ships, and a
living record says so.** (2026-10-12)
Asked during Step 10's triage whether the tests should run in
containers, the agent could not answer from the living records.
ARCHITECTURE points to ADR-0009, and ADR-0009 left the choice open
("decided at Stage 4 from what the build makes cheap"). The why is
only in the devlog (2026-09, Step 4, commit 6): the fork of
`target/classes` is the real application with no test code,
started with the environment a real instance gets. The choice was
right: the ledger ships as a plain process (README, `./mvnw
spring-boot:run`), and the evidence forks plain processes. The
agent also suspected a gap, a dropped store connection never
tested, and was wrong: `StoreOutOfReachIT` ends the session at the
store and sees the 503. Both came from the same thing: the reason
was not where a reader looks.
- What the evidence was checked against, adversity by adversity:

| Adversity | In the world | In the evidence | Same? |
|---|---|---|---|
| Instances with no shared memory | separate JVMs | forked JVMs | yes |
| Death mid-work | the process killed | SIGKILL (`ForkedLedger.kill`) | yes |
| The store hangs | no answer | the store's container paused | yes |
| The connection drops | an error at once | the session ended at the store | yes |
| A network cut, the instance alive | the store waits to notice | not staged; said in SL-4's record, §7; the wait is a known issue | said |

- The rule: the harness starts the system in the form it ships. A
  plain process ships: the evidence forks processes. An image
  ships: the evidence starts the image.
- The bootstrap's records say which form ships and that the
  evidence matches it, in a living record (ARCHITECTURE), not only
  in the devlog.
Considered and rejected:
- Containers for every test: they add an image build to every run
  and make the proof no stronger when a process is what ships.
  `podman kill` sends the same SIGKILL.
- A new test for the dropped connection: one exists.
Lands in: cbc-bootstrap (the harness decision states the shipped
form); the run playbook (the bootstrap's close writes it to
ARCHITECTURE).

**D27. Tests are placed by the kind of check they are.**
(2026-10-12)
Run 3's tests sit in one package: 18 test classes side by side,
beside `testsupport/` (the helpers) and `reservation/` (one unit
test that must reach a package-private method). A reader can tell
them apart only by name. Some names say what they check
(`NoSecondWayOutTest`); others do not (`ReceiptGuardIT`,
`ExitDoorIT`). The reviewer asked during Step 10's triage whether a
better layout exists.
- Tests are grouped by kind, in packages named for it:
  - `adversity/`: creates the danger for real (races, storms,
    kills, freezes). Example: `ExitStormIT`.
  - `structure/`: reads the source; a guarantee held because
    something is absent. Example: `NoSecondWayOutTest`.
  - `store/`: the store's own refusals and its migrations.
    Example: `MigrationPathIT`.
  - `door/`: the HTTP contract, the ADRs' answers. Example:
    `ReservationDoorIT`.
  - `testsupport/`: the helpers, and the test of the harness
    itself.
  - A unit test that needs package access stays beside its class.
- A test that mixes kinds goes by its main job.
- Which test proves which guarantee is the slice record's, in that
  guarantee's block (D11). The folder answers "show me every race
  test" or "I changed the door: what checks it?".
- (2026-10-13, at Step 10's commit 9: a fifth kind, `folds/`.
  OneClockIT fits none of the four: it shows a fold's fact (FC3,
  one clock) by running the system, and stages no attack. The
  definition already splits its work this way, concerns with an
  adversity and folds checked without one. Named `folds/`, the word
  the registry, the slice records and the skill use, not the
  definition's one heading "Definitions".)
Considered and rejected:
- By slice (`sl3/`): several tests serve more than one slice
  (`NoSecondWayOutTest` serves SL-2, SL-3 and SL-4), and a slice is
  the order of building, not the system (D16).
- Mirroring the main packages: main has one feature,
  `reservation`, so every test lands in one folder again.
- Leaving it to the slice records: they answer "which test proves
  this", not "which tests are of this kind".
Lands in: cbc-bootstrap (the skeleton creates the packages); the
test rule (which kind goes where).

**D28. A delivered copy is fixed here when the evidence is here,
chapters included; the hand-off marks it untried.** (2026-10-13)
During Step 10's triage the agent kept skill fixes for "next run
only" whenever run 3 could not run the skill again: D7's framing,
D12's slice outline, D26's and D27's bootstrap, D9's ground, D10's
requirements. No rule asked for that. `delivered-copies.md` rule 7
already lets a run edit a copy it has finished with, and prefers it:
the deliverer takes or declines an exact change instead of
translating a note. The reviewer pointed it out at commit 7, when
the definition's six losses (D7) were traced to cbc-framing's
close, which copies results into the exports. The reviewer then
asked why the concept chapters are blocked at all, since the
deliverer decides either way.
- A copy this run has finished with is fixed here when run 3 has
  the evidence: something happened that the copy did not foresee,
  and the fix is one any project would want (rule 2's test).
- Its hand-off line says so: "untried here: written from run 3's
  evidence, never run". The deliverer reads those hunks hardest.
  Nothing about it is written into the copy itself (rule 2).
- The concept chapters are treated the same way. Rule 1's reason,
  "nothing in a chapter is run, so nothing in it can fail a step",
  is true but not enough: skills are written from the chapters,
  and readers learn the method from them, so lived work can show a
  chapter wrong. A chapter is edited only from such a mistake,
  never from a preference, and marked untried.
- This reverses the chapter clause of the bundle's decision behind
  rule 1 (CBC ADR-0036). The edit to rule 1 is an exact change the
  deliverer can decline; the TODO line names the reversal plainly.
- No chapter is edited in Step 10. D7's copying lives in the
  cbc-framing skill; the framing chapter says nothing about
  composing the exports. The other chapter lines in this note (D2's
  plain voice, D6's layer tags, D7's line on where results live)
  are preferences, not mistakes.
Considered and rejected:
- Keeping skill fixes for the next run when the evidence is here
  (the agent's practice through the triage): the deliverer gets a
  note to translate, and the run that saw the mistake does not
  write the fix.
- A note asking the deliverer to reconsider the chapter clause, and
  nothing more: the deliverer would have to write the change it is
  asked to judge.
- Chapters editable for a plainer voice too: a preference is not
  evidence, and the chapters are the method's statement, shared by
  every run.
Lands in: `delivered-copies.md` (rule 1: chapters as any copy;
rule 4: the "untried here" mark); the entry file's line on copies;
TODO's hand-off; the triage rows that kept a skill fix for the next
run, each looked at again.

**D29. Framing's records get shapes where run 3 missed: a kill,
and a slice's registry entry.** (2026-10-13)
Run 3 had shapes only for the slice records and the tests. Two
forms in framing's records recurred and went wrong, and nothing at
a gate could see it. Four of the definition's 20 kills lost what
dies when composed (D7: kills 4, 5, 17, 18). The registry's entries
grew ~70 lines of story at two slice closes (D5). The reviewer
asked, at Step 10's second look at D7, whether framing's records
could have shapes too.
- Two shapes, written from run 3's records:
  `.claude/shapes/kill.md` and `.claude/shapes/registry-entry.md`.
- The kill shape is read at framing's closing gate; the registry
  entry's at each slice's close.
- Tried here on real text: the kill shape against the definition
  as it was before commit 7 (it should find kills 4, 5, 17 and 18)
  and after (it should find none); the registry entry's against
  the registry after commit 8 shortens it.
- Both pass the shapes rule's §4: cbc-framing's Step 3 gate already
  asks that each kill states what dies; cbc-slice's close asks for
  one registry line once D5's fix lands.
- Each shape is written after a look at its run-3 examples: where
  they differ, and what they all could do better (against the
  writing rule, this note's decisions, and what went wrong). The
  reviewer settles what the look finds. Example: none of the 20
  kills has a worked example with numbers, which D2 asks for; a
  shape copied from them would never ask. The shapes rule's §1
  gains the same step once the kill shape has lived it (D25).
- (2026-10-13, the kill shape tried at Step 10's commit 8b: on the
  definition before commit 7 it finds kills 4 and 5, which had lost
  their whole death, but not 17 and 18, which still had a bold
  death, only weaker. A shape catches a part that is missing, not a
  part that lost half its meaning; only the source shows that, which
  is why D7's fix is writing each result once.)
Considered and rejected:
- Shapes for facts, assumptions, fences and verdicts: their form
  held in run 3, and a shape with no miss behind it is written in
  advance (§1). A later gate can start one (the fourth way).
- A shape for the intent: one piece, and nothing went wrong.
Lands in: `.claude/shapes/` (offered to the deliverer as this
project's shapes, skeleton apart from illustration); framing's
closing gate and each slice's close, in run 4's PLAN.

## Applied to run 3

<!-- Step 10's triage: what happened to each decision in run 3
     itself. One row per decision, decided by the reviewer one at a
     time. "Here": applied, partly, or next run only. Commits are
     the small commits on `step-10-apply-retro-detail`, kept by a tag
     of the same name; each commit that landed on main names the
     small commits it holds. -->

| Decision | Here | What, and why | Commits |
|---|---|---|---|
| D1 | partly | In force from Step 9: the one-home rule (new and changed text) and the "rules in force now" list. Older text not swept: swept under D17's row: a full one-home pass with D2's rewrite. The known repeats: D5, D7, D10, D15, D16. Next run only: short gates, one-line closed steps, short devlog and log entries; run 3's are history. | with D17's |
| D2 | partly | Applied: the writing rule (Step 9); the records a reader uses rewritten plain: README, ARCHITECTURE, the exports by dated revision, the slice records with a revision line pointing at the text as signed, the two infrastructure manuals; a glossary; a worked example with numbers per kill; cbc-slice, `delivered-copies.md` and `shapes-lifecycle.md` plain, each with its TODO line; the two shapes plain, the slice-record shape in its rewrite (D12), the test shape after the labels (D13). Next run only: the closing gate check of the voice. Never: history, the bootstrap requirements (history by D10), concept chapters. | d088c03, f91ff4c, 9e1f370, baaa013, 6fc3a87, 54480a3, 000ef34, adf4ca0, efc6ba3, 6e20e9c, fec6652, 40bfc15, 24ffd2c, 8cc81e3, c9a4e14, 76fe5d4, 7d29733, 9bda31b, 854810c, 2794ac6, 128801b; fix-ups 712bfb5, 83ead3a, 62262f2, fa7d321, 365eb26, e4b738a |
| D3 | applied | `CLAUDE.local.md` written at the retro. Added in Step 10: the steps for a new kind of work (D25). Memory holds no preferences (its index is empty). The hand carry to the deliverer comes after this step (TODO). | Step 9; `CLAUDE.local.md` (not tracked); d138acc |
| D4 | partly | PLAN's "on trial" notes say kept (RETRO.md, D4). The step form asks of the goal "how would I check that's true?", one item per answer, and gains the "Now:" line: Release still opens from it (Steps 9 and 10 already wrote their gates this way). One decisions-log entry for the step form. Next run only: a trial names the close that judges it; the playbook's step form. Never: closed gates, Step 8's included. | 8d30cb0, 0ba881f, a291fd9, 416d232, 2a1ae3f |
| D5 | partly | The registry's ordering section shortened to the order and a link to each slice record (~70 lines to ~5); each fact checked for its home first. The definition's derivation-order note dropped; its saturation table kept, as the evidence the census is complete (cbc-framing asks for it in L1, on purpose). Each by a dated revision entry. Fixed here, untried (D28): cbc-slice's close writes one registry line and leaves the reasons to the slice record; the registry template's ordering section says one line per close. | e4277f1, 8db57c0, a291147 |
| D6 | partly | In run 3's living text: "migration V3"; local numbers carry their owner ("SL-4 G1"); concerns A–D become their slice tags; the glossary maps every short tag to its run-4 name. Run 3's records keep the short tags (history uses them ~940 times). Fixed here, untried (D28): the full-word tags in cbc-framing and cbc-slice (skills, references, templates, worked examples: 9 uses in cbc-framing's and 17 in cbc-slice's texts, the count corrected at the close, where ~120 had been guessed), layers excepted. Next run only: the layer tags, with the concept chapter that names them (no mistake came from them). | 2325249, 55887bd, d023336, f91ff4c, 9e1f370 |
| D7 | partly | Done: the definition compared once against the derivation; six places restored by a dated revision entry. Fixed here, untried (D28): cbc-framing writes each step's result straight into its export as a draft at the verdict; the derivation keeps how the step ran and links to the result; a return trip edits the export and adds a line to the derivation; the close checks the exports read alone and ends their draft state, copying nothing. Never: the derivation (history). | 80d70e3, 07cf9ab |
| D8 | next run only | The briefing is lost: never committed, and no transcript holds it (the earliest is 2026-09-11; framing ran 2026-09-10). Here: the registry's "Divergences from the briefing" says so by a revision line. | e4277f1 |
| D9 | partly | The operator manual's "The levers the evidence pulls" says what is true: the harness pulls its own levers, as plain processes and by pausing its throwaway store; the operator does nothing. Step 3's container proofs stay in the devlog, linked. Fixed here, untried (D28): infra-establish's step 1 lets the adversities' demands choose the environment but proves only the engine and the store's promises; each lever is proven by the harness that pulls it (cbc-bootstrap for the first, cbc-slice's R4 for the rest). | 36f8090, 1cadda1 |
| D10 | partly | The records tables (CLAUDE.md, README) send "what a slice may rely on" to ARCHITECTURE; the requirements are named history. Their banner, stale (it names V1 and SL-1 only), becomes a plain history marker pointing to ARCHITECTURE; the certified text is untouched. With D16, so ARCHITECTURE answers first. D10's "kept" bullet notes D26 (done with this row). Fixed here, untried (D28): cbc-bootstrap's §3 points to the ADR or contract for each constraint; its close makes the requirements history and writes ARCHITECTURE's components. Next run only: the kit's records table. Never: the certified text. | e1baa9a, b2d9f6c, a9ea214, 7007337 |
| D11 | applied | The four slice records regrouped, one block per guarantee (22 blocks, ~27,700 words); sections that span guarantees kept. Moved, not reworded: a script checks the same lines before and after; D2's rewrite comes after. Living links (~240) switch from section numbers to labels ("SL-4 G2", "SL-4 E3"); history's 72 keep theirs, covered by one revision line per record naming the signed commit. The plan's restated claim headings are dropped (the block has one claim); the move-check allows exactly those lines. After D12's shape. | 55d8d6a, 23573c2, 59a7112, c6ae21a, 6192f5d, b21c069, 48ede9c |
| D12 | partly | The slice-record shape rewritten to one block per guarantee, its parts in order; its rules for the parts kept; what the writing rule says, removed (commit 5). Step 10's closing gate reads the four records against it. Fixed here, untried (D28): cbc-slice carries the record's outline (its sections, one block per guarantee, which stage writes which part), replacing Stage 4's by-stage line; the shape gives the outline up and keeps how each part reads, so the first slice is written grouped, not regrouped at the gate. | 55d8d6a, 0a8b1a9; at the close e4c72be, a993ae3 |
| D13 | partly | The test shape stands (in force since SL-3, now read at the gate, D18). Test labels carry their slice: "SL-1 E3 · G3 — kill 5" (53 first lines, comments only). SL-1's and SL-2's tests, written before the rule, read against its close check: first lines and labels fixed; an assertion missing or out of order is not changed (never seen red), but listed in the slice record, and the reviewer decides whether it needs a review step. Fixed here, untried (D28): cbc-slice's build stage says a test is evidence only if every reading after the adversity checks the promise, and the slice's invariant, before any number (SL-3's four misses). Next run only: the test shape delivered with the bundle; full-word labels. | b37c989, d088c03, f49a71e; at the close c52606d, 83f57f2, a6c4987 |
| D14 | partly | The claims that read as titles (16 of 22 in the end, where about 8 had been counted: e.g. SL-1 G6 "Nonsense never reaches the decision") rewritten to say what happens, condition first, each checked against its block's body and tests; shown per slice. The claims that already read well stay. The claim's form goes into the new shape (D12). After D11's regroup, before D2's rewrite. Next run only: written this way at specification; asked again at run 4's first slice close. | 55d8d6a, e2b169e |
| D15 | partly | The main code's comments (257 of 668 lines) cut to what the code relies on and cannot show, each linking its guarantee with the owner ("Holds SL-1 G1"); what a removed comment said is checked to be in the record first. The 21 bare tags in code carry their owner. Comments only: no code line changes; tests keep their stories (D13's labels only). Never: the migrations, comments included (Flyway's checksum). After D11 and D27. Next run only: cbc-slice's build. | fefc362 |
| D16 | applied | ARCHITECTURE's "Invariants" stories (634 words) removed; the Responsibilities table becomes the invariants section, one row per rule, each linking its slice ("SL-1 G1"); each fact checked for its home in a slice record first. The components answer "what may a slice rely on" (D10), with D26's harness line; the codemap gains D27's test folders. ~2,031 to ~1,400 words. After D11, D15, D27; with D10; before D2's rewrite. Fixed here, untried (D28): cbc-slice's close adds or changes ARCHITECTURE's rows, never a paragraph (no skill said anything, and each close wrote a story). Next run only: the kit's ARCHITECTURE template. | e1baa9a, a291147 |
| D17 | applied | The sweep held at D1, settled: C, a one-home pass over the living records (README, ARCHITECTURE, exports, slice records, manuals, TODO, CLAUDE.md, PLAN's living parts), done in the same pass as D2's rewrite: each paragraph first checked for its home (linked if elsewhere), then rewritten. Plus B: a check script (links, paths, cited numbers and names, the version pair), run before the build, after D11 and D27, and at the close; offered to the deliverer with the one-home rule. The "now/today/not yet" check: one closing gate item. Never: history. Next run only: both at every close. | 5bb7fec, 8d30cb0, 6fc3a87, 442da9f; pass 2: 132c380, 377d32f, a73b1e6, 1e87a2d, 769c9de, 7ad220d, 5296346, a526905, 5f57033, a6cfb1e, e87f870, 941fd69, 3cf14b7, 2a1ae3f, 1a84f5f, 71e7945; pass 3: 9f9d5cd, 6bc33eb, f5bb8b9, 94062e7, a393b7f, 196d022, 4c80a1b, 10584b0, ac27434, b72d738; the checks e874436 |
| D18 | applied | `shapes-lifecycle.md` shortened and written plain: every shape in `.claude/shapes/`, read at the gate only, no proven/provisional split, no shape loaded while writing; a difference at the gate ends four ways (the output drifted; the shape was wrong; each had something; a different kind, so a new shape starts from it). The test file split: its shape moves to `.claude/shapes/evidence-test.md`; the red-run rule it repeated is left to cbc-slice's build gate, where it is loaded with the build; `.claude/rules/evidence-test.md` goes. The `temp/` staging dropped. A delivered copy: one log entry and one TODO line. PLAN's step form loses the `temp/` item, with D4's change; Step 10's own gate keeps it. Next run only: the bundle's rule, the playbook's step form, and the gate-only trial (D22's table, judged at the second slice's close). | 8d30cb0, d194038, e0169dd, f49a71e, 9bda31b |
| D19 | applied | Step 10's commit plan is the first built this way: small commits inside each planned commit, squashed at its boundary and shown to the reviewer there (commits are free on the branch, but the boundary is a stop), kept on `step-10-apply-retro-detail`; a look back after the last. The plan's header says so. The `commit-plan` skill is edited at Step 10's close from what happened (D25), with its log entry and TODO line. | 3daf0fb |
| D20 | applied | Done in Step 9: TODO's answered items point to their decisions, the three open ones stay; the decisions log has "Rules in force now" on top. What Step 10 adds (TODO lines for each edited delivered copy; "Rules in force now" for D4 and D18) lands with those commits and is checked by Step 10's gate item on TODO's hand-off. | Step 9; Close 5, `docs: TODO hands the changed copies over` |
| D21 | partly | cbc-slice's SKILL.md and workflow reference (~4,200 words) made to say each thing once, in the same pass as D2's plain rewrite: each paragraph checked for the other file first, then rewritten; the red-run rule and every stage's gate kept in meaning. A finished copy (delivered-copies rule 7): one log entry, one TODO line. Measured again (runs of 8 words): cbc-slice shares 33% of SKILL.md with its workflow; cbc-bootstrap 2%, infra-establish 5%, cbc-framing 5%, nothing to fix. Never: the concept chapters. | 2794ac6 |
| D22 | next run only | Every row is a run-4 step. The table stays open while Step 10 runs: D18 added a row (shapes at the gate only, judged at the second slice's close); D25 to D27 added none, since they land in text run 4 meets. Read once more at Step 10's close against every decision. | |
| D23 | applied | Done in Step 9: the `retrospective` skill and its manual (this project's own), Step 9 run as the PLAN step, Release's closing item for its own lessons, the old section replaced. What the skill gains from Step 10 is D24's row. | Step 9 |
| D24 | applied | Step 10 is the step: triaged one decision at a time (it also produced D25 to D27), then a commit plan ordered by influence, built with D19's small steps; the deliverer reads after. At the close: the apply skill built from the manual (its own or part of `retrospective`, decided then); the `retrospective` skill and manual name the step after; a draft of the apply step for run 4's PLAN, beside D23's. | bbe42b8, 1f8b593, 01d67f1, 9c3e8dd, e283fd3, 8d5e53f, e874436, fe18881 |
| D25 | applied | Step 10 runs this way: its provisional manual, `.claude/apply-retro/manual.md`, becomes a skill at the close. | d138acc, 3daf0fb, fe18881 |
| D26 | partly | ARCHITECTURE's harness section says why processes: the ledger ships as a plain process, and the fork is the real application with no test code (devlog, 2026-09). D9's new manual text says the same in one line. No new test: both ways of losing the store are proven. Fixed here, untried (D28): cbc-bootstrap's Stage 1 decides the harness's form by what ships, not by cost; its close writes that form, and why, into ARCHITECTURE's harness component (one edit with D10's). | 61a8b99, e1baa9a, 36f8090, a2a776e |
| D27 | applied | The tests moved into `adversity/`, `structure/`, `store/` and `door/`; each test's kind confirmed by reading it before it moves. Moved, not changed: only the package line and the folder; `./mvnw test` green with the same count. One living path link updated; history cites none. Before D13's labels, which touch the same files. Fixed here, untried (D28): cbc-bootstrap's `app-structure.md` shows the test packages by kind and puts the first evidence test in `adversity/`, written after commit 9 from what it found. | 3443228, e1baa9a, 2a05394 |
| D28 | applied | `delivered-copies.md` rule 1 treats a chapter as any copy, fixed from a lived mistake only; rule 4's hand-off line marks a finished copy's edit "untried here"; the entry file's line follows. TODO names the reversal of CBC ADR-0036's chapter clause. The rows that kept a skill fix for the next run (D5, D6, D7, D9, D10, D12, D13, D16, D21, D26, D27) looked at again, one at a time. No chapter edited here. | c6d21b8, 1a84f5f, 86ce30e |
| D29 | applied | The kill shape and the registry-entry shape written from run 3's records, after commit 8; tried on the definition before and after commit 7, and on commit 8's registry. | 8782a78, 00eabbd, e061d44, cdfc432, 8db57c0, baaa013, efb216a; at the close 402ccf8, b56a590, 3364d9e |

## Experiments

<!-- One line each: the try/ branch, what it tried, how it went,
     kept or dropped. -->

- `try/writing-rule` (opened 2026-10-11): D2's writing rule and
  D1's one-home rule, written now and used for the rest of the
  retro, to see whether answers and notes come out plainer and
  facts stay in one place. Kept (2026-10-12): both rules stay,
  always loaded; `retrospective` fast-forwards to the branch.
- `try/retro-skill` (opened 2026-10-12): D23's skill and its manual,
  `.claude/skills/retrospective/`, drafted from this retro. A skill
  this project wrote, not a delivered copy. Kept (2026-10-12),
  with a decisions log entry; `retrospective` fast-forwards to the
  branch.

## Parked

<!-- Questions raised in one part that belong to a later one. Each
     line names the part that takes it up. -->

- Another style where plain falls short. Specification style for
  guarantees and gate items: judged in part 6 (D14: no); asked
  again at run 4's first slice close. One kind per document:
  judged in part 8, not needed. Each document already serves one
  reader, and the records tables say what each answers. Nothing
  went wrong to fix. A decision for it ("one reader per document",
  with an "Its reader" column in README's records table) was
  drafted and dropped: the column would repeat "What it answers".
