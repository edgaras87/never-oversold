<!-- A reference on CBC ADR-0008's imitated side: a decision surface,
     nothing to paste or fill. -->

# App structure — the decision surface at bootstrap

How the application's packages are shaped is a **bootstrap
decision**, made and recorded exactly as the stack decision is: in
the run's own log, with its why, before the code that lives under
it. The recorded decision governs **all later code — slice work
included**: slices enter the structure decided here, they do not
re-decide it. This doc carries the lived default, the decision
rule, and the vocabulary of the named alternatives — so the choice
is conscious, recallable months later, and means the same thing to
the person and the agent.

**Masters nothing else.** What classes exist is the slices'
outcome; what enters the build is the pom convention's; this doc
owns only the *shape they land in*.

## The lived default

**Package-by-feature, package-private boundaries, structural depth
earned per feature.** Lived across nine slices:

```
<base-package>                main
├── <feature-a>/     FeatureAController, FeatureADomainClass
└── <feature-b>/     ...

<base-package>                test
├── adversity/       creates the danger for real: races, storms, kills
├── folds/           a fold's fact shown by running the system, no attack
├── structure/       reads the code: a guarantee held by an absence
├── store/           the store's own refusals, and the migration path
├── door/            the HTTP contract: each answer and its status
├── testsupport/     the harness bases (harness reference), and its own test
└── <feature-a>/     a unit test that needs the feature's package access
```

- **One package per problem area**, holding everything that area
  needs — the package list reads as the problem's table of
  contents, not as a list of technical roles.
- **Classes are package-private by default** (`class Foo`, not
  `public class Foo`); only what another feature genuinely calls
  becomes public. The compiler enforces the boundary — enforcement
  over convention, the same stance as the ground's grant split.
- **Depth is earned per feature, never stamped.** A feature starts
  as few classes as its slice demands (one lived feature: a
  controller and a domain class); an internal service or persistence split
  appears inside one feature when that feature's own complexity
  earns it, and appears nowhere else. Layers inside a feature are
  legal; a repo-wide layer template is provisioning ahead of need.
- **Tests are placed by the kind of check they are,** never by
  slice: a slice is the order of building, and one test may serve
  several. A test that mixes kinds goes by its main job. The folder
  answers "show me every race test" or "I changed the door: what
  checks it?"; which test proves which guarantee is the slice
  record's. The bootstrap's first tests go in from the start: the
  contention probe in `adversity/`, the migration path in `store/`.
  These test packages were laid out after one run's slices, not yet
  lived from a bootstrap.

This is the shape a run gets when its log says nothing else.

## The decision rule

- **Decided at bootstrap, logged with its why.** As with the stack:
  the slices rarely discriminate between structures, so the
  deciding inputs are the run's own — fluency, audience, and a
  stated intent (*"this run practices pattern X"* is a legitimate
  deciding input when it is written down).
- **Deviating from the default is off-default**, in the templates'
  sense: derive from the chosen pattern's own logic, record the
  decision in the run's log, and expect what you learn to harvest
  back into this doc.
- **The first run to live an alternative makes it lived** — its
  entry below stops being vocabulary and gains a harvest line.

## The vocabulary — named alternatives, none lived here yet

Each entry is what the name *means*, in two lines — not an
endorsement. Deeper trade-off talk is a conversation, not this doc.

- **Classic layered** *(unlived here)* — packages by technical role
  (`controller/`, `service/`, `repository/`); a request flows down
  the layers. The long-standing enterprise default; every class
  public across layer packages, so the boundaries are convention.
- **Hexagonal / onion / clean** *(unlived here)* — domain logic in
  the center with no framework imports; every external thing (DB,
  HTTP, queues) behind a port interface with an adapter at the
  edge. Pays when many integrations need swapping or faking; costs
  interfaces and mapping everywhere.
- **Vertical slice** *(unlived here)* — package-by-feature taken to
  the use-case grain: each use case its own top-to-bottom slice,
  sharing almost nothing. The default's nearest kin.
- **Modular monolith — Spring Modulith** *(unlived here)* — the
  default plus tooling: explicit per-module API surfaces, verified
  boundaries, per-module tests. The step to consider when features
  multiply and package-private stops being enough.

## What this doc does not carry

- **Pros/cons essays** — re-derivable in conversation at the moment
  of choice, and they would stale; the two lines above are for
  recall and shared vocabulary only.
- **Application code** — the executor's, always (CBC ADR-0008); the
  harness reference is the only code-bearing exception, and it
  earned that.
