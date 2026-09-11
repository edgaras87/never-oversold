# Bootstrap requirements — what is being brought to life, and under what constraints

<!-- The contract for PLAN Step 4: what the bootstrap delivers and
     refuses, decided before any code exists (ADR-0007–0009) and
     certified against the delivered files at the step's close.
     It names no dependency, no class, no file layout — that gap is
     deliberate; the implementation closes it, and on any conflict
     between this document and the method's walkthrough, this
     document wins and the conflict is reported. Ground facts are
     cited by pointer to the two manuals, never restated. -->

## §1 Identity

- Group `io.github.edgaras87`; artifact `never-oversold`; base
  package `io.github.edgaras87.neveroversold`; packaging jar;
  language version Java 21 (ADR-0007).
- Structure: package by feature, package-private boundaries, depth
  earned per feature (ADR-0008).

## §2 Capabilities

Four, and no more.

1. **An HTTP door.** The system answers HTTP on a port set from its
   environment. *Why:* adversity arrives through the real door —
   the evidence attacks what a caller reaches, not an inner method;
   and a port from the environment lets several instances coexist
   on one machine (ADR-0009).
2. **Operational health.** A cheap, unauthenticated world-is-up
   check that reports the store as a named component, up or down.
   *Why:* every evidence run first needs to know the world is up;
   the store is the one dependency, so its state is part of "up".
3. **Connectivity to the ground as `runtime`.** The system connects
   to the store as the runtime identity and can execute plain SQL
   inside a transaction. *Why:* the promise is a property of
   persisted state; the identity is the contract's only one for the
   running ledger (§4).
4. **The evidence harness.** Test-scope machinery that can, from the
   one standard test command:
   - stand up its own throwaway store of the ground's major
     version, migrate it harness-side from the one migrations home,
     and prove the migration path with a zero-applied assertion —
     the history table born, nothing applied, because the home is
     honestly empty;
   - run integration tests through the real door against that
     store, under the same command as the unit tests — no second
     command, no test that quietly never runs;
   - start more than one instance of the system as separate
     processes against that store, address each through its own
     door, release requests at one instant across them, assert
     every response, and read the witness from the store directly
     (ADR-0009);
   - prove all of this on **one named adversity**: contention, in
     SL-1's shape — concurrent requests on one probe, aimed at
     nothing yet. Every other adversity class is staged at its own
     slice.

   *Why:* cbc-slice must have somewhere to land; the first slice
   should spend its effort on the invariant, not on building the
   stage.

## §3 Constraints

1. **One identity.** `runtime` is the only database identity in any
   configuration or profile; its password is read from the
   environment (§4) and appears nowhere in code or committed
   configuration; `migrator` credentials exist in no application
   configuration. *Why:* the contract's identity rule; a running
   ledger that could change structure would make every refusal the
   ground proved a formality.
2. **Nothing in-app controls schema.** No in-app migration, no
   object mapping, no schema generation, no validation of structure
   at start-up by the application; migration tooling is present at
   test scope only, for the harness. *Why:* the one DDL path is
   Flyway as `migrator` from the compose profile (contract, *How
   schema changes are made*); the application's role is to run.
3. **One migrations home, honestly empty.** The harness migrates
   from `infrastructure/flyway/migrations/` on the filesystem —
   never from a copy — and the home holds no placeholder migration.
   *Why:* a table exists only when a slice's invariant earns it; a
   dummy baseline would be structure ahead of need.
4. **Nothing enters ahead of need.** Plain SQL access; every
   dependency enters at the step that earns it with its reason
   written beside it; deliberate absences are commented. *Why:*
   the reader must see decisions, not a starter's habit.
5. **The suite is self-contained.** The one standard test command
   is green from a clean clone with only the README's prerequisites
   in hand; the compose ground does not need to be up. *Why:* a
   stranger's machine is part of the runtime ground (definition,
   L1); evidence that needs a hand-started world is evidence the
   reader cannot re-run.
6. **Instances are processes the harness owns.** Started before a
   race, stopped after, none left running on failure. *Why:*
   ADR-0009; a stranded instance holding a port turns the next run
   red for the wrong reason.

## §4 The ground, by pointer

Everything about the store is the two manuals':
`docs/infrastructure/infrastructure-contract.md` (identities,
reachability, refusals, the DDL path) and
`docs/infrastructure/operator-manual.md` (standing up, verifying,
resetting). Only what the code needs in hand is carried here,
stated notation-neutrally:

- **Identity:** `runtime`. Password: env var
  `NEVER_OVERSOLD_RUNTIME_PASSWORD`, no default.
- **Endpoint from the host** (where the system runs during the
  bootstrap and where the evidence reads the witness): host
  `localhost`; port: env var `POSTGRES_PORT`, default `5432`;
  database `never_oversold`; schema `never_oversold`, which is both
  roles' search path.
- **Endpoint inside the compose network**, should the system ever
  run there: host `postgres`, port `5432`. Not used by the
  bootstrap.
- **The system's own listen port:** from its environment, default
  `8080`; the harness sets it per instance. The variable's name is
  the implementation's, recorded where it lands.
- **Container runtime for the harness's throwaway store:** rootless
  podman on this host; the once-per-machine setup is the operator
  manual's, not the code's.

## §5 Evidence owed at the close

1. The step plan as executed, with every deviation stated
   honestly, in the devlog.
2. The run proof, from actual output: the system started on the
   real ground as `runtime`, the language version announced, health
   `UP` with the store's component `UP`.
3. The one standard test command green end to end, with the
   integration tests shown structurally to run under it; the
   zero-applied migration assertion passing; the plural-instance
   race passing with every response asserted and the witness read
   from the store.
4. The commits standing on the step's branch, each following the
   commit convention, none straddling agent and project paths.
5. Certification member by member against §1, §2, §3 and §6 from
   the delivered files, never from a report alone.

## §6 Exclusions

- **No business behavior.** No item, no reservation, no admit. The
  probe the harness attacks does one round-trip to the store as
  `runtime` and carries no meaning; it is documented as scaffolding
  and dies when SL-1 lands. *Why:* behavior enters only as a slice
  with its invariant and its evidence.
- **No persistence schema.** No table, no sequence, no migration.
  *Why:* no table before its invariant.
- **No other adversity class.** No duplicate delivery, no kill
  mid-work, no frozen store, no skewed clock at bootstrap. *Why:*
  each is created by the slice that owns it; kill 10's clock shape
  is SL-1's decision by name (registry, SL-1's flag).
- **No authentication or authorization** at the door. *Why:* no
  slice demands identity at the door; the callers are the weather,
  not the audience.
- **No delivery machinery** beyond locally runnable and testable —
  no image publishing, no deployment descriptor, no pipeline. *Why:*
  the intent scopes deploy to a clean machine and the README's
  commands; nothing hosted exists to deliver to.
