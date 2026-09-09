---
name: convention-lifecycle
description: How conventions themselves are authored, delivered, vendored, and tracked. Use when creating or changing a convention, or when vendoring or injecting one into a project.
delivery: pushed
requires: artifact-kinds, change-plans, agent-arrangement
---

# Convention Lifecycle

The normative reference for the handbook's product: what a
convention is, what its file must declare, how it reaches a project,
and how a project knows which version it has. **Consolidation, not
new law** — every rule below cites the ADR that made it, and a
disagreement between this document and an ADR is a bug here
(ADR-0023).

The theory this document leans on is `models/agent.md`: the
channels (§4), the choosing table (§8), and the claims (§12). The
model describes; this convention prescribes. Nothing is restated
from it — read it there.

---

## 1. What a convention is

A document you can *violate* — it binds practice (artifact-kinds;
contrast a model, which merely describes). One convention per
directory: `conventions/<name>/CONVENTION.md`, plus whatever
copyable assets it owns (templates, stubs).

## 2. The frontmatter

Every `CONVENTION.md` opens with YAML frontmatter (ADR-0013):

```yaml
---
name: <directory name>
description: <when to read this file — the trigger>
delivery: <channel(s) — see §3>
requires: <conventions this one delegates to — see §5; omit if none>
---
```

- **`description` states when, never what.** It is the trigger a
  tool loads every session; a trigger cannot be lossy about a rule
  it does not contain (ADR-0013, model claim M1).
- **`name` equals the directory name** — it is how `requires` lines
  and registry entries refer to the convention.
- **The frontmatter is the skill shape.** `name` and `description`
  are exactly what a skill loader reads, so a verbatim copy at
  `.claude/skills/<name>/SKILL.md` loads without editing. That is a
  guarantee of this schema, relied on by §8 — not a property of how
  one file happened to be written (ADR-0030).

## 3. What `delivery` means

An **install instruction, read at vendoring time**: it names what
the convention *needs* — the channel semantics of model §4 — not
the mechanism any one tool provides (ADR-0012, ADR-0015). `pushed`
means "must be present at the moment of action"; how close a given
tool gets is that tool's business (in Claude Code, a skill is the
closest, and a hook closes the remainder — model §10).

## 4. How landing is evaluated

**Assign by what the convention is, not by what it is written as**
(ADR-0015):

- A rule that can ride inside the artifact it governs → `installed`,
  as embedded comments or template files. The strongest delivery;
  acting on the artifact is the trigger (ADR-0004, model §4).
- A rule bound to one identifiable moment → a skill, triggered by
  the `description`.
- A map of paths the agent must know exists → `ambient`, in the
  entry file (agent-arrangement §2). The records table is the one
  instance; a convention's own presence is not one (ADR-0034).
- A rule that must never be violated → additionally a gate; no text
  channel substitutes (model §7).

One convention may need several channels. The evaluation is
performed by whoever is landing the convention: the handbook agent
at vendoring time, the receiving agent at injection time — same
read, same decision, different evaluator (ADR-0022).

## 5. Requires-chains

`requires` names the conventions whose rules this one delegates to
(ADR-0017). Landing a convention means landing what it requires,
and what those require in turn — a convention installed without its
chain delegates to rules the project does not have.

## 6. The kit

The kit carries **copies, never symlinks** — a symlink into the
handbook does not survive copying (ADR-0007, ADR-0016). A copy is
verbatim except that it may omit handbook-only sections (the
commit-messages copy drops the handbook repo's scope note); the
omission is deliberate, and an audit treats it as expected rather
than drift. Copies can drift, which is why the install begins with
an audit against the handbook's current state, and why an update
compares before it overwrites (§8).

**The maintainer rule (ADR-0022):** a convention entering or
leaving the kit updates, in the same commit, the manual's file
table and the birth entry in the kit's decisions-log stub.

**A stub does not cite the handbook's decisions.** A born project
has its own `docs/adr/` sequence, so a bare `ADR-nnnn` in a stub's
comment points at that project's decision, not the handbook's; and
a prefixed one carries the why into a comment whose job is the rule
(ADR-0020's division of labor). The rule rides in the stub; the why
is the handbook's, reachable through the birth pin. The decisions
log is the one place that pointer lives (§7), so its own comment may
name the handbook decision it is provisional under.

## 7. Tracking: the registry and the hash

A project's convention registry is its agent decisions log
(`.claude/decisions.md`, ADR-0020) — entries, not a separate file
(ADR-0022):

- **The birth entry** names the conventions the project was born
  with and the copy-time handbook commit
  (`git rev-parse --short HEAD`, run in the handbook).
- **Every later injection or update appends an entry** with its own
  hash (§8, step 5).
- **A convention's version in a project** is the hash of the last
  entry that touched it. There are no per-convention version
  numbers: a number needs an ungated bump ritual and can silently
  lie; the hash is minted by git on every commit (ADR-0022).
- **"Is there newer, and what changed"** is answered in the
  handbook: `git diff <hash>..HEAD -- conventions/<name>/`.

## 8. Injection

Injecting a convention into a live project — a first copy, or an
update of one the project already holds — is the vendoring
evaluation performed by the receiving agent (§4), run against the
project's registry (§7). Written from the first lived injection
(ADR-0030); each step names the check it exists for.

1. **Position.** Read the registry: the birth entry, or the last
   entry touching this convention, gives the hash the project holds
   it at. In the handbook, `git diff <hash>..HEAD --
   conventions/<name>/` and the ADRs since say what changed; a first
   injection has no position and takes the whole convention. The
   handbook is a checkout on disk or the payload a handoff carries —
   either way the protocol is git, not a version number (§7).

2. **Evaluate.** Read the incoming frontmatter as at vendoring time:
   `delivery` decides which channels the landing needs (§4);
   `requires` names the chain that must be present *and current*
   (§5) — including a line added since the pin. An update can extend
   the chain (change-plans gained project-recording in the first
   lived run), and the project's copy of a newly required convention
   is checked in the same pass.

3. **Vehicle.** The landing's commits follow the ADR-0019 line. A
   skill delivery lands entirely agent-side — copy and registry
   entry — and is one commit (step 5). An installed
   delivery lands in project records too — stub comments, template
   files — and the two sides never share a commit, so that landing
   runs under a change-plan (change-plans §1). Not by definition: by
   which sides the delivery touches (ADR-0030, narrowing ADR-0022).

4. **Copy — compare first.** The master is
   `conventions/<name>/CONVENTION.md`; the installed copy is
   `.claude/skills/<name>/SKILL.md` — the copy is a rename. Before
   overwriting, diff the project's current copy against the
   handbook's file at the project's pinned hash. Identical:
   overwrite. Different: the project edited its copy, and each edit
   is re-applied, dropped, or promoted by decision, with the
   registry entry naming what happened. Nothing is clobbered
   silently. The copy loads as a skill because §2 guarantees the
   shape; there is no per-file check.

   An **installed** convention has no copy in the project to
   compare: what shipped was stub comments and template files, and
   the comments now sit inside filled, living records. The compare
   is kit against kit — `git diff <hash>..HEAD -- starter/kit/<stub>`
   for each stub the convention ships through, and the list of
   those stubs is the "Shipped conventions" table in
   `starter/README.md`, read there rather than remembered. What
   lands is the changed comment text, carried into the project's
   record; a template file is diffed as a file. The project's
   content around the comments is not a local edit, it is the
   record.

   Most of the time the diff carries nothing: a rule that changed
   reached the project through a reply before the update ran, and
   the record already says it. The update is then verification,
   and its product is the registry entry (step 5) — which is why
   the entry is never skipped. A handbook change absorbed through a
   reply without an entry leaves the pin lying: the registry names
   a hash the records have moved past, and the currency check in
   step 2 reads that lie as truth.

5. **Register — one commit.** Two things land together: the copy,
   and the registry entry appended to `.claude/decisions.md` — date,
   convention, injected or updated `@ <hash>`, why, what was
   rejected — after which that hash is the convention's version in
   this project (§7). The registry is the project's only list of
   its conventions; the entry file carries none (ADR-0034). The
   revert test binds the two: the copy without the entry leaves the
   registry lying — the same binding §6 puts on the kit's file table
   and birth entry. Splitting is allowed, never required; every piece
   is agent-scoped (ADR-0019 rule 3).

6. **Nothing edits the handbook.** Friction met here — a step this
   procedure left unstated, an edit the compare surfaced — goes up
   as a promotion-queue entry or a friction list (models/tiers.md
   §3), never as a side effect of the landing.

## 9. Adding a convention to the handbook

A new directory under `conventions/<name>/` with its
`CONVENTION.md`; a row in the handbook README's table; an ADR for
the decisions behind it; a changelog entry prefixed with the
convention name; a PLAN step, numbered by creation; and
registration in this repo's own arrangement (skill symlink and
entry-file line) in an agent-scoped commit. Whether it ships in the
kit is a decision the ADR takes — shipping triggers §6's maintainer
rule.

---

## Delivery

`pushed` — it fires when a convention is authored, changed,
vendored, or injected; outside those moments it is dead weight. In
the handbook that means a skill (symlink, like its siblings); in a
project, the kit's copy at `.claude/skills/convention-lifecycle/`.

**It ships in the kit** (ADR-0030, closing ADR-0023's deferral). Its
project-side reader is the injecting agent running §8 — and the
keeper of a project's copies, who needs §6 and §7 the moment a
convention arrives. Before §8 existed that role did not, and
shipping was dead weight (ADR-0023). A project born earlier holds
conventions this one governs without holding it, and takes it by §8
like any other.

**What this constrains.** Rules here delegate freely to
`artifact-kinds` (kind vocabulary) and `change-plans` (injection's
vehicle) — hence `requires`. And nothing here may restate the
model: citations only, or the two homes drift (ADR-0014, claim M2).
