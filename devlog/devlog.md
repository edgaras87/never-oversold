# Devlog

<!-- Newest entries on top. 2–5 minutes at the end of each session.
     For future-you: fragments fine, honesty mandatory. Never clean up.
     Mark dead ends loudly with "DEAD END:" so they're greppable.
     End every session with a "Resume:" line — cheapest save-point there is.
     When this file gets long, split into devlog/<YYYY-MM>.md per month. -->

## 2026-09-10 → 2026-09-11  (Step 3: the ground)

<!-- Grows as the walk runs — the lived-result record: for every
     executing step, the command, what was expected, what actually
     happened. Decisions with their options are ADRs. -->

- Opened on `step-3-ground`; the gate derived from the walk's own
  step gates. Readiness tripped on one thing: the definition had no
  runtime-ground facts. Added by a dated revision entry in L1
  (`44f256d`) — the framing's census never asks for them; hand-off
  filed in TODO.
- Readiness, on the actual repo: the three exports stand (ADR-0002),
  four slices, SL-1 chosen-next; the runtime ground now readable;
  the repo is git with `.gitignore`. Passed.
- The environment: ADR-0004, podman local containers, compose-
  driven, kept against every demand the slices make. Proven at the
  engine level before any ground file, each command with its
  expected result, then what happened:

  | Command | Expected | Actual |
  |---|---|---|
  | `podman version` | engine 5.x, client and API the same | 5.8.2 / API 5.8.2, linux/amd64 |
  | `podman compose version` | a provider answers | external provider `docker-compose`, Docker Compose v2.39.4-desktop.1 |
  | `podman info` | rootless, cgroups v2, crun, Fedora | rootless=true, cgroups v2, crun, fedora 42, amd64, kernel 6.19.14, SELinux enabled |
  | `podman pull docker.io/library/alpine:3.20` | digest, exit 0 | digest `bf8527eb…`, exit 0 |
  | `podman run --rm alpine:3.20 sh -c 'echo ground ok'` | `ground ok`, exit 0 | `ground ok`, exit 0 |
  | two `podman run -d` at once, `podman ps` | both Up | `ng-a Up`, `ng-b Up` |
  | `podman kill` on a running container | gone from `podman ps -a` | gone |
  | `podman pause`, then `podman unpause` | paused, then running | `paused`, then `running` |

  Eight of eight as expected; test containers removed. For the
  operator manual: rootless, ports above 1024; SELinux, `:Z` on
  bind mounts; the front door is `podman compose`, answered here by
  an external docker-compose binary.
- The skill's establishment log was opened at the decision and
  withdrawn one commit later, at the reviewer's question: what does
  it hold that the records do not? Nothing — this entry is the walk,
  ADRs are the decisions. Layout settled with it: `compose.yaml`
  and the env files at the root, the runnable ground under
  `infrastructure/`, the manuals under `docs/infrastructure/`.
  Hand-off filed: the skill should treat this as its normal shape
  in a repo with records, not a deviation.
- Operator manual begun (environment section) from the stand-up;
  found two compose providers on the host, podman choosing Docker's
  plugin — noted as "either works, the front door is `podman
  compose`".
- Services, slice by slice (ADR-0005, 09-11): every slice needs
  one shared store with a way to make two writers disagree, atomic
  commit of two writes, and a witness readable from the host;
  nothing needs anything else. PostgreSQL, one instance. Eight
  things not provisioned, each with its why; Flyway as tooling, not
  a service.
- Constraints (ADR-0006): the role-split model checked against the
  project's facts, nothing defeats it; nine constraints, each with
  the mechanism that enforces it — the grant system, the image's
  init hook, a named volume, a published port, the ignore file, the
  image tag. Names: `migrator`, `runtime`, database and schema
  `never_oversold`. T2's tool goes into the behavioral check as a
  capability shown, not a constraint governed.
- Ground files filled from the templates, this repo's names, one
  addition to the verify suite (query 6, the connect privilege,
  because C3 claims it). `podman compose config` clean; every bind
  mount labelled Z; Flyway only under its profile.
- Stand-up and verification, 2026-09-11, each with its expected
  result, then what happened:

  | Step | Expected | Actual |
  |---|---|---|
  | `podman pull postgres:17` | digest, exit 0 | `sha256:67f41722…`, PostgreSQL 17.11 |
  | `podman compose up -d` | created, started, bootstrap runs | started; log: `running …/bootstrap.sql`, `CREATE ROLE` ×2, `CREATE SCHEMA`; healthy after 6 s |
  | first query | answers | **failed once**: `the database system is shutting down` — the image's init restarts the server after the bootstrap and the health check saw the temporary one; answered 1 s later. Trap recorded in the manual |
  | catalog check, 6 queries | as the file's comments | all six as stated |
  | DDL as `runtime` | refused | `ERROR: permission denied for schema never_oversold` |
  | ungranted role connects | refused | `FATAL: permission denied for database "never_oversold"`; probe role dropped |
  | T2's tool: two `runtime` sessions on one advisory lock, `lock_timeout` 1.5 s | second refused | `ERROR: canceling statement due to lock timeout` |
  | `podman compose run --rm flyway info` | connects as migrator, empty schema, exit 0 | Flyway 11.20.3, `<< Empty Schema >>`, `No migrations found`, exit 0 |
  | witness read from the host via the published port | `1` | `1`, through a client container on the host network — the host has no `psql` |

  Both verifications passed; the manual's PostgreSQL section
  written from this run.
- The contract written; then the reviewer asked whether naming the
  server's serializing faces pre-empts the slice — it read that way
  in two phrases; both manuals now say inventory, not a choice, and
  that the advisory-lock probe was picked for needing no schema.
- Clean re-stand from the operator manual alone, on the reviewer's
  yes: `down --volumes` removed container, volume, network; `up -d`
  ran the bootstrap again; honest up after 7 s; all 16 catalog rows
  as stated; DDL as `runtime` refused; Flyway `<< Empty Schema >>`;
  witness read from the host `1`. Reproduced.
- Exit test: the ground runs — yes; every service tied to a stated
  need and every exclusion to a stated why — ADR-0005; both
  verifications passed from actual output — this entry; both
  manuals stand, written from lived work — yes; a stranger could
  stand it up from the operator manual alone — the re-stand followed
  it and nothing else. Passed. README gains Prerequisites;
  ARCHITECTURE names the ground and nothing that does not run.
- Version: still 0.0 — no slice closed; the ground is not a slice.
- Resume: the entry file's records rows, the plan's close, then
  fast-forward into main on the word and the reviewer pushes. Then
  Step 4 (bootstrap): cut its branch, derive its gate, run
  cbc-bootstrap — the stack at capability grain, the skeleton as
  `runtime` on this ground, the harness creating SL-1's adversity
  across instances (TODO's Step 4 items).

## 2026-09-10  (Step 2: identity)

- Named **never-oversold** (ADR-0003): the promise's negation,
  ruled out. Seven candidates from the sentence against a four-point
  bar; safe-reservations overturned — it named the territory and
  overclaimed; rejected beside it: no-oversell, oversell-proof,
  reserved-within-count, reservations-under-contention,
  reservation-ledger. The reviewer took the recommendation as it
  stood.
- The step was retitled from "Define (naming)" when it opened:
  a public identity is a name, a description, and a remote. The
  description is one line derived from the intent's why.
- The rename landed as one project commit across README, PLAN,
  the three exports (each with its first dated revision entry —
  the registry gained the log its header promised), CHANGELOG; the
  derivation record keeps the name it ran under, with a note; the
  entry file's title in its own agent commit. Closed steps' notes
  and earlier devlog entries stay as written: dated facts.
- The remote: github.com/edgaras87/never-oversold, created by the
  reviewer by hand with the ADR's description, main pushed by the
  reviewer at 0de75df (Step 1's close). This branch was not
  pushed; the reviewer pushes main again after the fast-forward.
- The folder on disk keeps `cbc-pure-run-3`; records name the
  project, not the path. The agent's memory is keyed to the path.
- Resume: close the plan, fast-forward `step-2-define` into main
  on the word, the reviewer pushes main. Then Step 3 (ground): cut
  its branch, derive its gate into PLAN, run infra-establish from
  the registry's adversity needs — T2's tool named and verified by
  refusal is the first thing the ground must show.

## 2026-09-09 → 2026-09-10  (Step 1: framing)

- Briefing: arrived 2026-09-09 in `temp/`, the reviewer's file,
  untracked. Inventory reservation under contention, for the
  portfolio reader; six bars a promise must clear; the sentence
  left open. The problem is named from this session on.
- Step 1 ran on `step-1-framing` under a change-plan. The seven
  framing steps each landed as a commit series — draft, one
  revision per reviewer question that changed something, the
  verdict — the reviewer's request before step 0's first commit,
  so a question's effect is a diff between commits. It held: 40
  commits from the gate to the ADR, every verdict the reviewer's.
- What the questions changed: the seller entered as who the
  promise protects, not the audience; the six bars split into
  settled-here and proven-later; the candidate sweep got written
  and each word of the sentence its commitment; refusal was
  restated as by-falsifiability, never by scope; the actors got
  one-line definitions; W2 states its cost; the reference number
  became the one token on-hand-count (a return trip through four
  frozen sections); possession tags kept P, probes became PL.
- Outcome: one promise; four possessions, six refusals; twenty-two
  facts, three trust lines, six fences, five scope verdicts,
  saturation by two empty lenses; twenty kills into four concerns
  and three folds; one area, five seams refused; L5 empty with
  reasons; four slices, SL-1 chosen-next. ADR-0002 adopts it.
- Two slips, both fixed at the reviewer's reading: the definition
  was appended in derivation order and had to be reordered L1→L5
  (the export section's "growing" read as append — hand-off filed
  in TODO); the registry's opening carried the template's own
  words — a skill name, a step number, the delegation slot — into
  a project artifact. The reviewer's rule from that: exports carry
  no agent language. Hand-off filed for the template's line.
- Review-driven touch-ups to committed exports each cost a plan
  revision until a provisional step named them; three plan
  revisions before that, one after. Worth a retrospective line:
  the plan wants a "touch-ups on reading" step from the start.
- DEAD END: none. The near one was concern B — the downward
  correction looked like a definition until the because was
  written (naive code accepts the honest request and the promise
  dies).
- Version decided: a state of the evidence, 0.N by slices closed,
  1.0.0 at all closed and released (CHANGELOG header).
- Resume: land the entry-file row and the plan's close, delete
  `temp/`, fast-forward `step-1-framing` into main on the word.
  Then Step 2 (Define): cut its branch, derive its gate into PLAN,
  decide the public name — safe-reservations to confirm or
  overturn.

## 2026-09-09  (pre-briefing: the working arrangement)

- Four arrangement pieces went in before the briefing, all on
  trial from Step 1. The branch rule landed on main on 09-07, as a
  Standing rules section in PLAN. The other three landed on the
  branch `prebriefing-arrangement` under a change-plan, seven
  commits, fast-forwarded to main on the reviewer's word.
- Kit updated c670fe5 → af16eb7 through the receipt branch
  `kit-af16eb7`: one commit cut from the seed, the kit copied over
  it. What the branch gave the compare: the whole kit diff at the
  two pins in one place, file by file, and compare-first became
  `git diff 27db35e main -- <file>` — every copy identical, so
  every overwrite was clean. Where it fell short: the four
  placeholder reversions are noise to read past; the receipt
  cannot say which convention a stub change belongs to (that is
  the starter README's table, read in the handbook checkout); the
  why is not in it either — ADR-0035, ADR-0036 and the
  agent-arrangement convention text are not kit files, and the
  rejection and the move both needed them; the entry file's stub
  diff had to be carried into the living file by hand, comment
  only; and the script's bare `git add -A` would have swept the
  untracked `temp/` into the receipt (excluded by hand).
- The kit's settings-file commit gate: rejected, not deferred. The
  reviewer gives the word once in chat; a prompt asking for it
  again is a second word. The handbook withdrew the same rule in
  its own checkout for the same reason. The receipt keeps the file
  so the compare stays honest.
- CLAUDE.md moved under .claude/; content untouched; the harness
  picks up the new address from the next session. CLAUDE.local.md
  exists at the root, ignored — the operator's, not quoted.
- Briefing: still not brought. Nothing names the problem.
- Resume: wait for the briefing. On its arrival cut Step 1's branch
  from main, derive Step 1's gate into PLAN first (the branch item
  among them), then run cbc-framing jointly.

## 2026-09-07  (Step 0: bootstrap)

- Project started. Seeded, not born whole: the kit's hygiene commit
  on main, then the branch `birth-seed` as the receipt — kit
  remainder @ c670fe5, the CbC bundle @ 57cf22f (concept, five
  skills, playbook steps, entry-file fills). Main held the same
  files untracked; the receipt is never merged.
- The birth ran as one change set, eight commits: the plan, kit
  conventions, concept, records with Step 0's gate, CbC skills
  with their registry entry, the entry file, this close, the
  plan's close. Every pinned copy byte-identical to the receipt.
- Briefing: not yet brought. It arrives as the prompt that opens
  Step 1; nothing before it names the problem. The title
  `cbc-pure-run-3` is a working name.
- Open: what a version is here (CHANGELOG header) — Framing
  decides.
- Resume: wait for the briefing. On its arrival open Step 1 with
  cbc-framing: derive Step 1's gate into PLAN first, then run the
  framing jointly.
