# Change-plan: finish the birth — the seed assembled, Step 0 closed

## Summary — the state after all commits

Main carries everything the seed delivered, tracked, in an order
that reads as the birth rather than as the delivery: the branch
`birth-seed` stays the receipt (kit @ c670fe5, bundle @ 57cf22f)
and is never merged; main's commits are the assembly. The pinned
copies — `docs/concept/` and the nine skills under
`.claude/skills/` — are byte-identical to the receipt, checkable
with one diff. The agent side is registered: `.claude/decisions.md`
names the kit's conventions at their pin and, in a new entry, the
correctness-by-construction bundle at its pin, so every skill's
version is answerable from the log. The records stand and each is
either true or honestly stubbed: PLAN is titled, Step 0 carries a
derived gate and is closed on it with today's date; the devlog
holds the birth session with a Resume line; TODO is triaged;
CHANGELOG and ARCHITECTURE keep their stubs, whose fill moments
they already name; README is unchanged, its paragraph already true.
The entry file is unchanged: read line by line, every line passes
its three tests. Nothing names the problem — no `docs/system/`, no
source, no build file. `CHANGE-PLAN.md` is gone; its retrospective
is the close commit's body.

## Commits

**1. `docs(agent): add change-plan for the birth`**
The agreement, timestamped before the work. The conventions it is
written under are on disk (delivered, receipted on `birth-seed`)
but not yet on main — they enter as this set's work, not before it.

**2. `chore(agent): install kit conventions @ c670fe5`**
The container's agent side: the four kit skills (artifact-kinds,
change-plans, commit-messages, convention-lifecycle) and
`.claude/decisions.md` with its birth entry, exactly as delivered.
Own step because it is one source at one pin, and the registry
entry that names these conventions lands with them
(convention-lifecycle §8.5: copy and entry, one commit). References
nothing in-repo, so it can land first.

**3. `docs: add the CbC concept, pinned @ 57cf22f`**
The five chapters under `docs/concept/`, verbatim. Project side —
method text is not part of the agent arrangement. Lands before the
records because README and the entry file point at it.

**4. `docs: add the project records, Step 0 open`**
The record system as one change: README, PLAN, TODO, CHANGELOG,
ARCHITECTURE, devlog, ADR-0001 — the kit's stubs with the bundle's
fills, plus the birth's one change to them: PLAN titled with the
working name and Step 0 carrying its derived gate, still `[~]`.
The gate is written here, not in a later commit, because a PLAN
committed with a placeholder title and an underived gate would lie
at its first commit; the gate's content is agreed in this plan
(commit 1) before PLAN exists, which is the decision-first order.
Every link in README's table resolves within this commit.

**5. `feat(agent): install the CbC skills @ 57cf22f`**
The five method skills (cbc-framing, cbc-slice, cbc-bootstrap,
infra-establish, infra-serve) with their references and templates,
verbatim, and the registry entry appended to `.claude/decisions.md`:
the bundle named as a whole — concept, skills, PLAN's steps, the
entry-file fills — at its pin, with why and what was rejected.
`feat(agent)` because these are new skills; separate from commit 2
because it is a different source at a different pin, and the
convention binds each injection's copy to its own entry. After
commit 3 and 4 so the entry names only what exists.

**6. `chore(agent): add the entry file`**
`CLAUDE.md`, unchanged from delivery. Last on the agent side
because it references the concept, the records, the skills and the
log — nothing it names may be missing when it lands.

**7. `docs: close Step 0 in PLAN, devlog, TODO`**
The step's gate checked item by item and closed `[x] 2026-09-07`;
the devlog's 2026-09-07 entry rewritten from the placeholders into
what actually happened (seed, assembly, the briefing explicitly not
yet brought, an Open line, a Resume line); PLAN's "Discovered along
the way" triaged into TODO. One commit because it is one moment —
"closing a step's gate" in the records table — and the three files
are that moment's three records.

**8. `docs(agent): close change-plan for the birth`**
Deletes this file. Body: what diverged from plan, or that nothing
did.

## Decisions taken inside this plan

- **The seed is not replayed.** Main takes the assembled state;
  `birth-seed` is the receipt. The pinned copies are still
  verifiable byte-for-byte against it (`git diff birth-seed --
  docs/concept .claude/skills` is empty after commit 6). Reproducing
  stub-then-fill on main would re-enact the delivery, which the
  receipt already records.
- **Three agent commits, not one.** Two pins (kit, bundle) and one
  entry file that depends on both sides. The revert test cannot be
  fully satisfied across the agent/project line — reverting commit
  5 with commit 6 present leaves the entry file naming missing
  skills — and the commit-messages scope rule takes precedence
  over it; noted, not fixed.
- **Step 0's gate, as it will read in PLAN** (verifiable facts,
  derived from the goal "the container exists — repo, records,
  arrangement — before content"):
  1. Every delivered file is tracked on main; `git status
     --porcelain` is empty; the pinned copies match the receipt.
  2. The nine skills load from `.claude/skills/`; each is
     registered in `.claude/decisions.md` at its pin; no placeholder
     remains in the log.
  3. Every record exists and is true or honestly stubbed: PLAN
     titled and this gate written; the devlog carries this session
     with a Resume line; TODO triaged; README's table links all
     resolve; CHANGELOG and ARCHITECTURE stubs name their own fill
     moment.
  4. The entry file passes its three tests, line by line.
  5. Nothing names the problem: no `docs/system/`, no source, no
     build file; the devlog's briefing line says so explicitly.
  6. Every commit on main follows commit-messages: subject ≤ 50,
     imperative, and no commit straddles agent and project paths.
- **No new ADR at birth.** The only project-side decision the birth
  could record — building by correctness-by-construction — is the
  seed's given, already stated in CLAUDE.md and README; the first
  decision the method itself asks for (the framing's adoption) is
  Step 1's. ADR-0001 stays as delivered.
- **CHANGELOG, ARCHITECTURE, README untouched.** Nothing users can
  see changed; the system has no shape; README's paragraph is true
  now and is re-derived at Step 1 by the framing skill's projection
  law. The CHANGELOG versioning placeholder stays: it names Framing
  as its decider.
- **The devlog's briefing line stays unfilled**, and says so.
  "Nothing before the briefing names the problem" is a local rule;
  a devlog line that guessed at the problem would break it.
- **TODO triage content** (commit 7): Next — Step 1 decides what a
  version is (CHANGELOG header), and adds the `docs/system/` row to
  the records tables when the framing artifacts exist. Later —
  Step N's two `(CbC)` gate items carry another run's recorded
  exclusions as caveats; re-decide them for this run at Release.
  Later — the worked-example twin in cbc-framing/cbc-slice claims
  a byte-identical copy but differs in its provenance path line;
  fold back to the source at the retrospective, never edit here.
- **No `.claude/settings.json`.** Nothing to configure yet; nothing
  enters ahead of need.
- **All steps are firm.** No result outside this repo shapes a
  later step, so nothing is provisional.
