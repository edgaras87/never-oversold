# Glossary

The words this project's records coin, and the tags they number
things with. Each is one plain sentence here; its full meaning is in
the record named beside it. This page is a summary and owns nothing.

The records keep run 3's short tags (F19, SL-4 G1). From the next
run on, the method spells every tag in full words (FACT-STORE-19,
SLICE-4 GUARANTEE-1); each entry gives both. A number local to one
record carries its owner outside it: "SL-4 G1", never a bare "G1".

## The framing: what the system promises and owns

- **Promise.** The one claim the system exists to keep, stated
  before any code: for any item, the reserved quantity never exceeds
  the on-hand-count, however many reservations race for the same
  units. [intent](system/intent.md)
- **Invariant.** A statement true in every state anyone can read;
  each slice proves one. Example, SL-1's: for every item, the units
  of its active reservations are at most its on-hand-count.
- **Adversity.** What attacks an invariant in the real world:
  requests racing, an instance killed mid-work, a store that stops
  answering.
- **Layer** (L1 to L5; next run `LAYER-1-ENVIRONMENT` to
  `LAYER-5-BETWEEN-OWNERS`). One depth of the definition: the world
  (L1), what must be ours (L2), who owns it (L3), what breaks (L4),
  what passes between owners (L5). [definition](system/definition.md)
- **Fact** (`F19`; next run `FACT-STORE-19`, the actor named).
  Something the world does that can hurt the system, with who does
  it. Example, F1: many callers reserve one item's last units at
  the same instant. [definition, L1](system/definition.md)
- **Census.** The list of facts, grown until two search lenses in a
  row found nothing new.
- **Lens** (`PL0`; next run `PROBE-LENS-1`). One way of searching
  for facts not yet listed.
- **Assumption** (`T1`; next run `ASSUMPTION-1`). Something taken as
  true without defending it. Example, T1: the store keeps a write it
  acknowledged.
- **Fence** (`W1`; next run `NOT-OURS-1`). Something real that the
  system neither owns nor defends, written down so nobody takes it
  on quietly. Example, W1: what a warehouse physically holds.
- **Verdict** (`V1`; next run `SYSTEM-BOUNDARY-1`). A boundary
  question the reviewer decided in or out. Example, V1: a
  correction under the units held is in. Never a migration, which
  is "migration V1".
- **Possession** (`P1`; next run `POSSESSION-1`). Something the
  system must own, because anyone else owning it could break the
  promise. Example, P1: each item's on-hand-count.
- **Refusal.** Something the system declines to own, written with
  its **mirror**: what the refusal costs, and why the promise still
  holds. Example: reserve once-ness; a retried reserve makes a
  second hold, and the units still fit.
- **Seam.** A place where the system could split into two owners;
  each one probed here was refused. [definition,
  L3](system/definition.md)
- **Kill** (`kill 15`; next run `KILL-15`). One way the promise can
  break: a fact meeting a possession. Example, kill 1: two racing
  reserves both succeed on units that fit only once.
  [definition, L4](system/definition.md)
- **Concern** (A to D in the first framing). A group of kills
  proven together; each became one slice, and is now called by its
  slice's tag.
- **Proof obligation.** What a concern's evidence must show.
  Example, SL-4's: the half-done converges.
- **Area.** An owner in L3: something with its own state and its
  own decisions. This system has one, the reservation ledger.
- **Parked.** Seen during framing and left unchosen, for a slice to
  decide. Example: SL-2's two options, refuse or end reservations.
- **Fold** (`FC1`; next run `FOLD-1`). A rule no danger attacks
  beyond sending the request, checked in the first slice that needs
  it. Example, FC3: whether a reservation is active is judged by
  one clock.
- **Flag.** A note on a slice's registry entry: its evidence cannot
  be staged the usual way, and what it does instead.
  [registry](system/registry.md)
- **Hammering.** Many concurrent requests sent at one item, to
  create a race. A flag names evidence that cannot be staged this
  way.
- **The written zeros.** A zero stated in writing (no fold, no
  flag, no deferral), so the absence is checked, not assumed.
- **Question** (`Q1`; next run `QUESTION-1`). A reviewer's question
  during framing, with its answer.
  [derivation](system/framing/derivation.md)
- **Briefing.** The project idea the framing started from. It was
  not kept in the repository.

## The slices: how each promise is proven

- **Slice** (`SL-1`; next run `SLICE-1`). One invariant, taken from
  specification to evidence; four here.
  [the slice records](construction/)
- **Guarantee** (`SL-2 G1`; next run `SLICE-2 GUARANTEE-1`). One
  claim inside a slice, answering one attack. Example, SL-2 G1: a
  correction is admitted only if the count it sets is not under the
  units held at the moment it is written.
- **Wall.** What makes a guarantee hold by structure: a constraint,
  one statement, or something absent from the code. Example: reserve
  admits by one conditional update, and `item_never_oversold`
  refuses a row with more held than on hand.
- **Backstop.** What stands behind a wall and catches the attack if
  the wall is ever wrong; "seen" if a red run showed it catching,
  else "unproven".
- **Option** (first called **face** in the records). One way a wall
  could be built, weighed against the others; the chosen one comes
  first.
- **Escape hatch.** A path around a wall: an admin script, a console,
  a second writer.
- **Evidence** (`SL-1 E1`; next run `SLICE-1 EVIDENCE-1`). A test that
  creates the adversity for real and then reads the witness.
- **Evidence criterion.** What an evidence test must create, and what
  the witness must read after it.
- **Witness.** The state a test reads to judge the invariant, from
  the store, from outside every instance. Example: 20 on hand, 100
  reserves raced, the witness reads 20 held.
- **Red run.** The evidence run with its wall removed, to see the
  test fail on the witness; a test never seen red is not known to be
  watching.
- **Tripwire.** A test that pins a decision rather than the promise;
  it goes red only if someone changes that decision, and it never
  counts as evidence.
- **Early warning.** A test that reads the code and goes red when the
  violation it forbids is planted, such as SL-3 E7. It stands in
  front of a wall. Unlike a tripwire, it counts as evidence.
- **Red tree.** The working tree with a wall removed for a red run.
  It is never committed.
- **Kill-mid-work.** Evidence that kills an instance outright in the
  middle of its work. Example: SL-4 E1.
- **The wall's own check.** A test that sends a wall the refusal it
  exists for, or reads it where it is defined; it shows the wall
  stands, and is not evidence.
- **Readiness** (`R5`; next run `READINESS-5`). A check run before a
  slice starts. Example, R5: the evidence can be seen to fail.
- **Standing guard.** What keeps a guarantee true after its slice
  closes.
- **Provisional.** Decided for now, with what would reopen it.

## The system: the ledger and its ground

- **The store.** Where the ledger's numbers persist: PostgreSQL here.
- **The door.** Where every request crosses: HTTP here.
- **The ledger.** The application: any number of instances of one
  build, each holding no item state.
  [ARCHITECTURE](../ARCHITECTURE.md)
- **On-hand-count.** How many units of an item exist, as an operator
  last asserted it.
- **Units held** (the column `reserved`). The units of an item's
  reservations that have no receipt yet. Example: 10 on hand, 4
  held; a reserve of 7 is refused.
- **Reservation**, or **hold**. Units set aside for a caller, for a
  time the caller chooses, until the reservation exits.
- **Exit.** How a reservation ends: consume (the units leave stock),
  release (they return), or expiry (its time ran out). Each
  reservation exits once.
- **Receipt.** The one row that records a reservation's exit, how and
  when.
- **Tidy.** The first move of a reserve or an adjust on an item: it
  writes the expiry receipts of the item's run-out holds and frees
  their units.
- **Outcome unknown.** The answer `503` when the store was lost
  mid-request: the change may or may not have happened.
- **Migration** (`V3`; next run `MIGRATION-V3`). One versioned change
  to the store's structure, never edited once committed.
- **Constraint** (`C1`; next run `CONSTRAINT-STORE-1`). A rule the
  store enforces on itself. Example, C1: the running application
  cannot change the store's structure.
  [ADR-0006](adr/0006-store-constraints.md)
- **`runtime`, `migrator`.** The store's two identities: the ledger
  connects as `runtime`; only the migration tool connects as
  `migrator`.
- **The ground.** The services the system stands on, stood up and
  verified before any code: the store, in a container.
  [operator manual](infrastructure/operator-manual.md)
- **The harness.** The test machinery that creates each adversity:
  a throwaway store, the ledger forked as plain processes, the
  witness read from outside.
- **Lever.** One way the harness creates an adversity: kill an
  instance, pause the store, end a session.
- **The harness's hold** (`Hold`). A row lock the store's superuser
  takes, to pause a consume mid-work. Not a reservation.

## The work: how it is run and recorded

- **Step.** A numbered part of the plan, with its gate. Example:
  Step 10. Only PLAN's steps are called steps. [PLAN](../PLAN.md)
- **Stage.** One part of a step's work: one of its skill's stages,
  or, for a step with no skill, one its Notes list. Examples: a
  slice's specify, plan, build and document; Step 10's triage,
  commit plan, build and close.
- **Gate.** The check at the end of a step or a stage, signed by the
  reviewer before the next begins.
- **Commit.** One saved change in git, with its message.
- **Stage it.** Put a change in git's staging area, so it can be
  shown before it is committed. Unrelated to a stage of work.
- **Shape.** What a kind of output looks like here, read at a step's
  closing gate. Not SL-2's heading "The correction's shape —
  decided", which names a choice between two options.
  [`.claude/shapes/`](../.claude/shapes/)
- **The truth set**, or **the exports.** The intent, the
  definition and the registry: what the system is, changed only by
  a dated revision entry.
- **ADR** (`ADR-0014`). An architecture decision, with the options
  rejected. [docs/adr/](adr/)
- **Retrospective decision** (`D6`). One decision of the
  retrospective about what the next run does differently.
  [RETRO](../RETRO.md)
- **Delivered copy.** A skill, rule or concept chapter that came
  from the method's bundle, pinned at one of its commits. The
  **deliverer** is whoever keeps that bundle; the **pin** is the
  commit the copies equal. Until 2026-09-18 the rules came from the
  **kit**, the engineering-handbook starter kit this project was
  born from. Files filled from it still carry its pins, such as
  `kit @ af16eb7`.
- **Birth.** The project's creation on 2026-09-07, from the kit and
  the bundle: Step 0's work.
- **The arrangement.** The agent setup under `.claude/`: the entry
  file, the rules, the skills, the shapes and the decisions log.
- **The polish.** The records review planned in passes after the
  last slice. Pass 1 ran; Step 10 did the work of the other two. Its
  briefs and report are in git history (`.claude/polish/`, removed
  at Step 10's close).
- **The walk-through.** The review planned after the polish, which
  became Step 9's retrospective.
