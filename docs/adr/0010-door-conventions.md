# 0010. The door's conventions: resources, JSON, refusal versus invalid, errors

Date: 2026-09-12
Status: Accepted

## Context

The definition's edge says every request crosses at one door —
reserve(item, quantity), release, consume, adjust the
on-hand-count — parsed and checked there, and refused there for an
unknown item, a non-positive quantity, or a reservation not the
caller's own (`docs/system/definition.md`, L2). The bootstrap's
door is HTTP with JSON (ADR-0007) and carries one probe with no
meaning, marked to die here. TODO deferred the door's conventions
to this opening by name: resource naming, the JSON shape, the
error format, and how a refusal differs from an invalid request —
decided once, entered by every later slice.

Two of the definition's decisions shape the door before any
option is weighed. T3: a request's stated identity is what it
claims to be, authentication outside (W5) — so nothing here
carries credentials. And the refused caller's view (L2): the
ledger's answer reports the persisted outcome, never a computed
picture; a lie to the caller cannot break the promise, so the
answer is kept honest by carrying the record, not by promising
about it.

## Options considered

**Resource naming.**

1. **Action-shaped paths** — `POST /reserve`, `/consume`,
   `/release`, `/adjust`: the definition's verbs as URLs. Honest
   to the vocabulary, but every path is a procedure call and the
   reservation, the thing later requests name, has no address.
   Rejected.
2. **Resource-shaped paths, nouns from the definition** — a
   reservation is a record (P2) with an address; an exit is a
   transition on one reservation; an adjustment is a request
   against one item's count (P1). Chosen.
3. **A version prefix** (`/v1/...`). Nothing ahead of need: no
   hosted deployment, no second client. Rejected; a revision here
   adds one if a need arrives.

**Refusal versus invalid.**

1. **One status for every non-success.** Hides the distinction the
   definition draws: nonsense at the door (FC1, kill 18) is not the
   same event as a valid request the ledger cannot honour.
   Rejected.
2. **Invalid and refused are different statuses,** and the body
   says which in words. Chosen: the evidence must count refusals
   to show the storm was real, and a client retrying a refusal is
   doing something different from a client fixing a bad request.

**Error format.**

1. **Spring's default error body.** Framework-shaped, changes with
   the framework, names exceptions. Rejected.
2. **A home-grown `{error: {code, message}}`.** One more format
   for a stranger to learn. Rejected.
3. **Problem Details, RFC 9457** (`application/problem+json`):
   `type`, `title`, `status`, `detail`. A standard a stranger
   already reads; the framework produces it natively. Chosen.

## Decision

- **Paths are resource-shaped**, nouns from the definition:
  - `POST /items/{item}/reservations` — reserve; the response is
    the reservation as persisted, `201 Created`, its address in
    `Location`.
  - `POST /reservations/{reservation}/consume` and
    `POST /reservations/{reservation}/release` — the exits, as
    transitions on the one reservation they name (SL-3's).
  - `POST /items/{item}/adjustments` — a change to the
    on-hand-count (ADR-0011 says what it carries).
  Each slice births only the paths its guarantees need; the list
  above is the shape, not a promise that all exist.
- **Identifiers are opaque strings** in paths and bodies. An
  item's identifier is given from outside (the catalog is not
  ours). A reservation's identifier is the ledger's own.
- **JSON bodies are flat objects with the definition's terms as
  field names**, camel-cased: `quantity`, `onHandCount`,
  `expiresAt`. A response carries the record as persisted, never a
  derived view (no "available" number: the caller's view is
  refused, and a number computed at read time is stale by F22 the
  moment it is sent).
- **Three kinds of answer, three statuses:**
  - admitted: `201 Created` for a new record, `200 OK` for a
    transition on an existing one;
  - **invalid** — the request means nothing (FC1: a non-positive or
    unbounded quantity, a missing field, a body that does not
    parse): `400 Bad Request`; an unknown item or reservation named
    in the path: `404 Not Found`;
  - **refused** — the request is valid and the ledger cannot honour
    it as asked (the units do not fit; an exit on an ended
    reservation): `409 Conflict`.
- **Every non-success body is Problem Details** (RFC 9457):
  `title` names the kind in the definition's words ("refused",
  "invalid request", "unknown item"), `detail` says what did not
  fit or what was wrong, `status` repeats the code, `type` stays
  `about:blank` until a catalogue of types is earned.
- What a slice adds to a body — an expiry, a quantity's bound —
  enters through that slice's specification, under these rules.

## Consequences

Good: a stranger reads the API with what they already know; the
evidence can count refusals by status and prove the storm asked
for more than fit; the distinction the definition draws at the
door is visible on the wire; nothing here names a mechanism inside
the ledger.

Bad: `404` for an unknown item leaks nothing but does depend on
ADR-0011 — an item exists when it has a count — so "unknown" is
"never adjusted"; resource-shaped paths make the exits look like
sub-resources, which they are not, and the two `POST`s to a verb
are the honest exception, said here once.
