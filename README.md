# never-oversold

An inventory-reservation ledger built to keep one promise: **for
any item, the reserved quantity never exceeds the on-hand-count,
however many reservations race for the same units.** It exists as
a demonstration for a reader judging correctness-first
construction — the claim stated before any code, the system
derived from it, and evidence that stages the contention for real.
Why this claim, what it excludes, and what done means:
[docs/system/intent.md](docs/system/intent.md).

The ledger owns, per item, its on-hand-count and its reservations,
and decides three things: whether a reservation is admitted,
whether a change to the count is admitted, and whether an exit —
release, expiry, consume — is a reservation's first and only. It
refuses what cannot make the promise false: whether a repeated
request is the same one, how long a hold lasts, which items exist,
what a warehouse actually holds, who may act. The environment it
stands in, what dies without it, and who owns what:
[docs/system/definition.md](docs/system/definition.md).

Two words the records use throughout: *the store* is where the
ledger's numbers persist — PostgreSQL here — and *the door* is
where every request crosses — HTTP here.

## The invariants

- **No over-admission under contention.** The sum of active
  reservations never exceeds the on-hand-count, however many admits
  race.

  Closed 2026-09-14, on evidence. The wall, its guarantees and the
  storms that proved it:
  [docs/construction/sl-1-no-over-admission.md](docs/construction/sl-1-no-over-admission.md)

- **The correction never undercuts the holds.** No admitted change
  to the on-hand-count leaves it under the reserved sum.

  Closed 2026-09-21, on evidence. A correction that does not fit is
  refused, and one resent or arriving out of order is proved too:
  [docs/construction/sl-2-correction-never-undercuts.md](docs/construction/sl-2-correction-never-undercuts.md)

- **A reservation exits once.** Its numbers move at most once on
  exit, and never after it has ended.

  Closed 2026-10-07, on evidence. A reservation is consumed,
  released, or ends at its expiry instant, and each ending is
  recorded once — exits retried, racing each other or the clock, or
  arriving late are proved too:
  [docs/construction/sl-3-a-reservation-exits-once.md](docs/construction/sl-3-a-reservation-exits-once.md)

- **Consume's two moves hold together.** No readable state has the
  reservation ended without the count lowered, or the reverse.

  Closed 2026-10-10, on evidence. A consume interrupted mid-work —
  its instance killed, the store frozen, a reader beside it — leaves
  both moves or neither, and settles with no one sending anything:
  [docs/construction/sl-4-consumes-two-moves-hold-together.md](docs/construction/sl-4-consumes-two-moves-hold-together.md)

Each with the adversity its evidence must create, and its status:
[docs/system/registry.md](docs/system/registry.md).

Built by correctness-by-construction: what must never happen first,
features last. The method: [docs/concept/](docs/concept/), start
with [00-cbc.md](docs/concept/00-cbc.md).

**Status:** version 0.4 — all four invariants closed on evidence.
What each version brought: [CHANGELOG.md](CHANGELOG.md). What comes
next: [PLAN.md](PLAN.md).

## Prerequisites

- podman with a compose provider (`podman compose` answers) — the
  infrastructure ground; details in
  [docs/infrastructure/operator-manual.md](docs/infrastructure/operator-manual.md).
  The tests also need podman's user socket, set up once per machine
  (see Test below)
- JDK 21 (Maven rides in via the committed wrapper)

## Run

```bash
# stand the ground up (first time: creates roles/schema; see the
# operator manual for the two-way verification)
cp .env.example .env
podman compose up -d

# apply the schema as the migrator — the first time, and after every
# new migration; the running ledger cannot and must not
podman compose run --rm flyway migrate

# run the ledger against it, as the runtime identity
set -a; . ./.env; set +a
./mvnw spring-boot:run
# proof of life: curl localhost:8080/actuator/health → status UP, db UP
```

If health answers with `db` DOWN, the environment was not exported:
the ledger starts anyway and only health tells. Export `.env` in
the same shell and start again.

## Use

An item becomes known to the ledger by its first adjustment; a
reservation is admitted only if its units still fit, and ends once
— consumed, released, or at its expiry instant.
Every answer carries the record as stored; every refusal or
invalid request comes back as an RFC 9457 problem with its reason.

```bash
# an operator states an item's on-hand-count (creates the item on first sight)
curl -s -H 'Content-Type: application/json' \
     -d '{"onHandCount":3}' localhost:8080/items/sku-42/adjustments
# {"id":"sku-42","onHandCount":3,"reserved":0}

# a caller reserves 2 units for 5 minutes → 201, the reservation as stored
curl -s -H 'Content-Type: application/json' \
     -d '{"quantity":2,"hold":"PT5M"}' localhost:8080/items/sku-42/reservations
# {"id":"…","item":"sku-42","quantity":2,"expiresAt":"…"}

# a second caller asks for 2 more; only 1 is left → 409, refused
curl -s -H 'Content-Type: application/json' \
     -d '{"quantity":2,"hold":"PT5M"}' localhost:8080/items/sku-42/reservations
# {"title":"refused","status":409,"detail":"2 units of sku-42 do not fit: 2 held of 3 on hand",…}
```

A reservation ends once, by one of three exits. **Consume** means
the units were sold: they leave the on-hand-count. **Release**
means the caller gave up: they are free again. **Expiry** needs no
request: when the hold runs out by the store's clock, its units are
free for the next decision. An exit names its reservation and
carries no body.

```bash
# the caller consumes reservation R → 200, R as ended; sku-42 now has 1 on hand
curl -s -X POST localhost:8080/reservations/<R>/consume
# {"id":"…","item":"sku-42","quantity":2,"expiresAt":"…","endedBy":"consume","endedAt":"…"}

# the reply was lost and the caller sends it again → 200, the same answer; nothing moves twice
curl -s -X POST localhost:8080/reservations/<R>/consume

# a release after the consume → 409, refused: R has already ended
curl -s -X POST localhost:8080/reservations/<R>/release
# {"title":"refused","status":409,"detail":"reservation … has already ended by consume, at …",…}
```

A reservation the ledger does not know is answered `404`; one that
has already run out is refused, its units long free.

If the ledger loses the store mid-request, it cannot know whether
the request took effect, and says so: `503`, titled "outcome
unknown". A consume, a release or an adjustment can then be sent
again — the same request gets the same answer. A reserve sent again
may hold a second time, until that hold's expiry ends it.

```bash
# {"title":"outcome unknown","status":503,"detail":"the store was out of reach mid-request: the request may or may not have taken effect",…}
```

Quantities are whole numbers from 1 to 1 000 000; a hold is an
ISO-8601 duration from one second to seven days.

## Test

```bash
# the one standard test command — unit and integration tests together;
# integration tests drive a real throwaway PostgreSQL (Testcontainers,
# rootless podman) and fork real instances of the ledger against it,
# so the ground does not need to be up
./mvnw test
```

The first time on a machine, the tests need podman's user socket on,
and `~/.testcontainers.properties` pointing at it — two steps, in
[the operator manual's test runtime section](docs/infrastructure/operator-manual.md#the-test-runtime--once-per-machine).
Without them the integration tests cannot start their store.

## Project records

| Record | Where | What it answers |
|---|---|---|
| System | [docs/system/](docs/system/) | What it promises, owns, refuses; what to work next |
| Operator manual | [docs/infrastructure/operator-manual.md](docs/infrastructure/operator-manual.md) | How to stand the ground up, verify it, reset it |
| Infrastructure contract | [docs/infrastructure/infrastructure-contract.md](docs/infrastructure/infrastructure-contract.md) | What the builder may rely on: identities, reachability, refusals |
| Bootstrap requirements | [docs/construction/bootstrap-requirements.md](docs/construction/bootstrap-requirements.md) | What the skeleton delivers and refuses, certified at bootstrap |
| Slice records | [docs/construction/](docs/construction/) | Per invariant: what it guarantees, what holds each guarantee, how it was proven |
| Plan | [PLAN.md](PLAN.md) | Where are we, what's next, what does *done* mean |
| Decisions | [docs/adr/](docs/adr/) — cited from other repos as `NEVER-OVERSOLD ADR-nnnn` | Why is it built this way |
| Architecture | [ARCHITECTURE.md](ARCHITECTURE.md) | What is the current shape of the system |
| Backlog | [TODO.md](TODO.md) | What's known but not done |
| Changelog | [CHANGELOG.md](CHANGELOG.md) | What changed per version (for users) |
| Devlog | [devlog/](devlog/) | Day-to-day work, dead ends, open questions |

<!-- A line here is true now, and meant for someone arriving from
     outside. What changes weekly is PLAN.md's; why is the ADRs'; how
     it went is the devlog's. A missing section is not an omission: it
     arrives when a step's gate makes it true — projection follows
     truth. The paragraphs above are derived from docs/system/ and
     never authored here: a fact the reader needs that is not in an
     export marks a gap in the export — fix it there, re-derive. -->
