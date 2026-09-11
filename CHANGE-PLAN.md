# Change-plan: Step 4, the bootstrap

## Summary — the state after all commits

The system is alive and empty: a skeleton on the decided stack
(ADR-0007), in the decided shape (ADR-0008), standing in the
repository beside the ground; it starts on the real ground as
`runtime` and nothing else, answers health with the store's
component up, and carries no business behavior — one probe,
documented to die at SL-1. The evidence harness stands at test
scope: its own throwaway store of the ground's major version,
migrated harness-side from the one honestly empty home and proven
by a zero-applied assertion; integration tests under the one
standard test command; and the machinery proven able to create
contention — requests released at one instant across more than one
instance of the system running as separate processes (ADR-0009),
every response asserted, the witness read from the store. The
requirements (`docs/construction/bootstrap-requirements.md`) are
certified member by member against the delivered files. README has
Run and Test and the stack's prerequisite; ARCHITECTURE shows the
ledger running beside the store; CHANGELOG, devlog and TODO are
caught up; PLAN's Step 4 gate is ticked.

## Commits

**1. `docs(agent): add change-plan for the bootstrap`**
This plan, agreed.

**2. `build: stand the skeleton in the repository`**
The walkthrough's step 2. Initialized to §1's identity and
extracted into the repository root beside `infrastructure/`:
plumbing merged, never overwritten (`.gitignore` grown,
`.gitattributes` merged for the wrapper's line endings, README
kept); generated cruft removed; the build file per the pom
convention with two capabilities and no more — the HTTP door and
operational health — every dependency carrying its earning reason,
the Boot minor pinned here; configuration carrying the application
name only. Verified: the context test green with no store; the
application starts announcing Java 21; health UP.

**3. `feat: connect to the ground as runtime`**
The walkthrough's step 3, §2's third capability. The datasource
wired to the real ground as `runtime` from §4's facts, the password
from the environment; plain SQL access, the driver at runtime
scope; the three deliberate absences commented in the
configuration with their whys — no schema pinning, no migrator
credentials in any profile, no test profile. Verified: ground up,
the application runs, health UP with the store's component UP;
recorded from actual output.

**4. `test: stand the evidence harness, migration path proven`**
The walkthrough's step 4, the first half of §2's fourth
capability. Test-scope only: the throwaway store of the ground's
major version, one per test JVM, started once; migrations run
harness-side from `infrastructure/flyway/migrations/` on the
filesystem before any context boots; the two layered test bases;
the migration-path test asserting the history table born with
zero applied; the build's test includes widened so integration
tests run under the one standard test command. The operator manual
gains the test runtime's once-per-machine section from the lived
setup — the manual and the work are one change. Verified: the
standard test command green, the integration tests shown to run
under it.

**5. `feat: the probe, scaffolding that dies at SL-1`**
The walkthrough's step 5 as lived: one endpoint doing one
round-trip to the store as `runtime`, carrying no meaning, its
death scheduled in its own documentation; and the in-process burst
— many requests held at a barrier, released at one instant through
the real door, every response asserted — as the harness's cheap
first check that the door and the store round-trip survive a
burst. Verified: the standard test command green. This commit does
not close the gate's plural-instance item; the next does.

**6. `test: the race across instances` — provisional**
ADR-0009's shape: the harness starts N ≥ 2 instances of the system
as separate processes against its throwaway store, each on its own
port from the environment, releases requests at one instant across
all of them, asserts every response, reads the witness from the
store, and stops every process it started. The realization —
processes forked from the build's own output, or containers of the
system's image — is decided at this boundary from what the build
makes cheap, and the devlog records the choice with its why and
the traps met. Provisional: the intent is firm, the wording and
the exact split arrive when the shape is seen. Verified: the
standard test command green end to end.

**7. `docs: records catch up on the bootstrap`**
Certification against the delivered files, member by member
against §1, §2, §3 and §6, and §5's evidence from actual output —
the exit test's answers — in the devlog; README's Run and Test
sections and the JDK prerequisite line from the skill's template,
Run ending in the proof of life; README's records table gaining
the requirements document; ARCHITECTURE showing the ledger running
beside the store; CHANGELOG's Added line, the version still 0.0;
TODO triaged — Step 4's items closed or moved to SL-1 by name, Now
pointing at Step 5; PLAN's gate ticked and the step done.

**8. `docs(agent): list the requirements in the entry file`**
The records-table row. Agent-scoped.

**9. `docs(agent): close change-plan for the bootstrap`**
Deletes this file; the body records what diverged.

Then, outside the set: the branch fast-forwarded into main on the
reviewer's word; the reviewer pushes.

**Provisional throughout:** a missing *what* found while building
returns to Stage 1 as a logged re-decision, landing where it falls
with the plan revised; touch-ups from the reviewer's reading, zero
or more, one commit each. The close body lists both.

## Decisions taken inside this plan

- **The skill's step 1 is already done.** "The stack decision
  recorded" landed before this set as ADR-0007–0009, confirmed at
  Stage 1's gate; the skill requires decisions logged before any
  code and the plan starts at the walkthrough's step 2. The three
  ADRs stand Accepted, not Proposed: their decisions were settled
  in conversation and confirmed by the reviewer. ADR-0009 leaves
  one realization open by design; if commit 6 contradicts its
  decision rather than realizes it, that is a divergence — the
  ADR gains a dated revision, the plan is revised, and the close
  body says so.
- **The walkthrough's step 5 is split in two.** Commit 5 lands the
  reference's lived probe pair — the endpoint and the in-process
  burst — because it is cheap, proven, and shows the door and the
  store round-trip under concurrency before the harder shape is
  attempted. Commit 6 is where this run leaves the reference
  (ADR-0009) and is the only commit that closes the gate's
  plural-instance item. The in-process burst stays afterwards as
  the cheap first check, beside the real race.
- **The operator manual's test-runtime section shares commit 4,**
  as Step 3's verification shared the manual's service sections:
  the manual is written from lived work, contemporaneously. The
  host already carries the setup from an earlier project; the
  section is written for a stranger's machine and checked against
  what this host actually has.
- **Commit types:** `build` for the skeleton, whose substance is
  the build file; `feat` for a capability the running system gains;
  `test` for harness work. No scope: the project has one area and
  it is not built yet.
- **The requirements document joins the records tables** (README,
  the entry file) as the builder's record of what the skeleton
  delivers and refuses; certified, it stays as the contract SL-1
  builds on, never edited in place.
