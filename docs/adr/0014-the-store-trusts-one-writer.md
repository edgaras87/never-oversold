# 0014. The store trusts one writer: the ledger

Date: 2026-10-08
Status: Accepted

## Context

Until 2026-10-08 the definition did not say who may write the store.
Its picture was plain — every request crosses at the door, and the
door is the ledger's — but nothing said whether anything else may
reach the stored data around it. Silence read as "anyone might", so
the slices defended against writers no one had named. SL-3's sixth
guarantee is the clearest case: *no path ends a reservation… including
paths this slice never anticipated*, defended against "a script, a
migration, an admin path", which is part of why its store carries a
guard on receipts and a check on the units held.

Meanwhile the ground said the opposite of a closed door. The
infrastructure contract and the operator manual tell a person to
inspect the store, and the evidence to read its witness, as `runtime`
— the identity the ledger writes with, which may insert, update and
delete every table. So the one identity that writes the data was the
one handed to people for looking.

Say: an operator opens a console as `runtime` to look at an item, and
at 2 a.m. "fixes" its units held by hand. Nothing in the framing says
whether that is an attack the system must survive or a writer it
trusts. SL-3 answered it alone, by walling it; every later slice
would have to answer it again.

## Options considered

1. **Trust one writer, and say so.** A trust assumption, T4: only the
   ledger writes the data, the identity it writes with is its alone,
   only the migration tool writes structure, the superuser is trusted
   as in any store; everything else is fenced out (W7). Nothing on
   the ground changes. Chosen.
2. **Lock it: enforce one writer with roles.** A read-only identity
   for people and for the witness; `runtime`'s password only in the
   ledger's environment; its rights cut to what its statements use.
   Enforced rather than trusted — but a new role needs the superuser,
   the infrastructure contract and the operator manual are rewritten,
   and the harness changes how it reads the witness. On one machine,
   where the person running the ledger holds its password anyway, the
   lock guards against the same hands that hold the key. Rejected for
   a system the intent says nobody operates; named below as what a
   real deployment does.
3. **Defend the open world.** Name anyone holding a writing identity
   as an actor, and make every slice wall against them, as SL-3 did.
   Honest, but without end: every future guarantee owes walls against
   arbitrary writers, and the superuser can remove any wall anyway.
   Rejected.
4. **Stay silent.** Each slice keeps guessing, and pays as SL-3 did.
   Rejected.

## Decision

- **The definition carries T4 and W7** (its revision of 2026-10-08).
  Only the ledger writes the store's data; only the migration tool
  writes its structure; the superuser is trusted; anything else that
  writes is outside.
- **Inside the application, the one writer is guarded at build time**:
  only the ledger class may reach the store, by any database API, and
  the ledger's own statements are read — none writes a receipt or the
  units held but an exit's, and none updates or deletes a
  reservation. Code in one process can always get around a build
  rule on purpose; the rules catch the honest mistake, which is the
  one that happens.
- **The store's refusing walls stay.** They never depended on who
  writes: the receipts' key, their guard and the check on the units
  held refuse a wrong result from any writer, the ledger included —
  and the ledger is where SL-3's one real double free came from. What
  T4 removes is the duty to build *new* walls against writers outside
  the ledger.
- **A slice hunting escape hatches aims at the trust list**: a writer
  T4 trusts is named, not walled.
- **People read with `runtime`, and do not write with it.** The
  contract and the manual say so; T4 is the trust that they do not.

## What a real deployment would do instead

This system runs one data identity and trusts it. A deployment that
others operate would replace that trust with enforcement:

- an application role holding only the rights its statements use —
  here, no update or delete on reservations, no update or delete on
  receipts;
- the migration role used only by the deployment pipeline;
- a read-only role for people — support, reports, debugging — often
  on a replica;
- an audited emergency login for the rare repair by hand, each use
  logged and justified;
- operations an administrator needs — cancel a reservation, retire
  an item — built as authenticated features at the door, through the
  same ledger and the same walls, never as a database role around
  them.

None of it is built here: the intent excludes operating the system.

## Consequences

Good: the framing finally says what was always true in practice, and
later slices stop defending against writers no one named — they say
they rely on T4. The ground and the code change nothing. The walls
already standing keep their full value against the ledger's own bugs.

Bad: T4 is trust, not enforcement. A console session as `runtime`, a
script with the password, or the superuser can still write anything
the store's rules allow, and nothing reports it. What those rules
refuse — an oversell, a second receipt, units held out of step — they
still refuse; what they allow — an open hold deleted with its units,
a hold's expiry moved — is only kept out by trust. Neither can
oversell.

Changes one export: the definition, by the dated revision named
above.
