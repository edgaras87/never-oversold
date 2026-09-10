# Change-plan: Step 3, the ground

## Summary — the state after all commits

The ground stands: the execution environment decided against the
lived default and running; the services the registry's adversities
require stood up under `infrastructure/`, constrained to need with
every constraint enforced by the service itself; verified both
ways from actual output, T2's tool among the refusals seen; the
two manuals standing, the operator manual proven by a clean
re-stand; the ground's decisions as ADRs, the establishment log as
the walk's lived record. The definition carries the runtime ground
it had left unstated. README has its Prerequisites, ARCHITECTURE
names what runs, the records tables list the new records, PLAN's
Step 3 gate is ticked.

## Commits

**1. `docs(agent): add change-plan for the ground`**
This plan, agreed.

**2. `docs: definition — the runtime ground`**
A dated revision entry in the definition: L1 gains the runtime
facts Stage 0 requires — one local machine, the reviewer's; a
stranger's clean machine by the README; more than one instance
runnable on it. Triggered by this step's readiness check; the
framing's own return-trip discipline, extended past close.

**3. `docs: decide the execution environment`**
ADR-0004, Proposed: the slices' demands derived (race plural
instances, kill mid-write, inject an unknowable outcome, control a
clock), the lived default checked against them, kept or defeated by
name. `infrastructure/establishment-log.md` opened: its first entry
maps the skill's default records to this repo's, records Stage 0's
pass, and logs the decision. No ground file is born here.

**4. `docs: operator manual — the environment`**
`operator-manual.md` begun from the lived stand-up, verified by
execution: engine version, compose provider, host OS and arch,
cgroups, rootless and its implications. Written at the moment it
happens.

**5. `docs: evaluate the services from the registry`**
ADR-0005, Proposed: slice by slice, what capability each
invariant's evidence requires; the service set that survives; the
not-provisioned list with each exclusion's why. Log entry.

**6. `docs: name the constraints and their enforcement`**
Log entry: each service's constraints, each naming its enforcement
mechanism; standing knowledge as the rebuttable default where it
exists, deviations naming their defeater.

**7. `build(infra): land the ground files`**
Under `infrastructure/`: the compose declaration, bootstrap
scripts, tool configs; `.env.example` committed and `.env`
ignored. The files realize commits 3, 5 and 6 and are younger than
all three.

**8. `test(infra): verify the ground both ways`**
The verify suite: the catalog check with expected results beside
each query; the behavioral check with each constraint attempted and
refused live, T2's tool among them. Log entry with actual output.
The operator manual grows its service sections from this lived
work in the same commit — the manual and the work are one change.

**9. `docs: write the infrastructure contract`**
`infrastructure-contract.md`: one section per service — identities
to connect as and never to use, reachability inside and outside,
refusals as contract terms, how schema changes are made.

**10. `docs: records catch up on the ground`**
The exit test run and its answers logged; the clean re-stand from
the operator manual alone (volumes dropped on the reviewer's yes)
recorded; README's Prerequisites from the template; ARCHITECTURE
naming the ground's services; README's records table gaining the
log and the manuals; CHANGELOG; devlog entry; TODO triaged, the T2
item closed; PLAN's gate ticked and the step done, the decision
index gaining ADR-0004 and ADR-0005; both ADRs flipped to Accepted.

**11. `docs(agent): list the ground's records in the entry file`**
The records-table rows. Agent-scoped.

**12. `docs(agent): close change-plan for the ground`**
Deletes this file; the body records what diverged.

Then, outside the set: the branch fast-forwarded into main on the
reviewer's word; the reviewer pushes.

**Provisional throughout:** deviations the ground forces (a second
service, a constraint family without standing knowledge) land as
their own logged commits where they fall; touch-ups from the
reviewer's reading, zero or more, one commit each. The close body
lists both.

## Decisions taken inside this plan

- **The runtime-ground revision is this step's first work commit,**
  not a reopening of Step 1: the definition is a living record and
  the skill's Stage 0 is exactly the downstream trigger its
  revision rule names. Framing work is not done here; a fact the
  framing left unstated is written where it belongs.
- **The skill's default record paths are kept** — the log under
  `infrastructure/`, both manuals at the root — because the repo
  has no convention on manuals and the defaults are lived; the
  log's first entry says so.
- **The ground's decisions are ADRs and the log is the walk.** The
  records table sends decisions to `docs/adr/`; the skill's log is
  the lived-result record the walk needs. Two records, two jobs, no
  duplication: the ADR holds the why and the options, the log the
  commands and what actually happened.
- **Ground files land in one commit after the three decisions,**
  so no file exists before the decision that defines it — the
  walk's own rule.
- **Verification and the manual's service sections share a
  commit:** the manual is written from lived work, contemporaneously,
  and that work is the verification.
