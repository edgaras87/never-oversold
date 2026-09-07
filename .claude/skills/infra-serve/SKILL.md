---
name: infra-serve
description: Add, change, or remove an infrastructure service or capability on an ALREADY-ESTABLISHED ground - a new dependency discovered during the project's build (a cache, a queue, a second datastore, a broker, an extension, a new port or capability on an existing service) re-enters the original need-constrained evaluation as one logged decision, with the manuals grown from the lived setup. Use whenever the user wants to add infrastructure to a project whose ground already stands - "we need Redis now", "add a message queue", "the new slice needs X", "open another port", "bump the postgres version" - even if it sounds like a small compose edit. Re-entry is the lived normal, never a failure of the first pass. Do NOT use for the first establishment (infra-establish) or for application/schema work (that is building, not ground).
---

<!-- Checked against concept v1 of correctness-by-construction
     (ADR-0003, ADR-0005 — practice-born). Provenance —
     archive/cbc/system-design-method agents-from-practice/
     Infrastructure-establishment/.claude/skills/infra-serve/SKILL.md
     @ fe0075d (imported 2026-08-28, PLAN Step 4). Changes on
     import: none — verbatim below this header. -->

# Infra serve — keeping an established ground

The ground stays governed after it stands: **every new dependency
re-enters the evaluation** — the same constrained-by-need question the
establishment asked, one logged decision, the manuals grown. Nothing
joins the ground because it was easy to add.

## Stage 0 — the ground stands?

Verify: the establishment log exists with the original service
evaluation and its not-provisioned list; the infrastructure contract
and operator manual exist; the ground currently comes up
(`podman compose ps` or the project's equivalent — read the actual
output). Any missing → this is not a re-entry; run infra-establish,
or repair the record first.

Check the original evaluation: if the wanted service is **on the
not-provisioned list**, the old exclusion's why must be answered by
the new need — name what changed. If it isn't answered, the exclusion
stands and the request is refused with that reasoning.

## The re-entry, one pass

1. **Need.** What slice, invariant, or environment fact demands this —
   concretely, what must the evidence or the system now be able to
   *do* that the current ground refuses? "We'll probably need it" is
   not a need; refuse anticipation even when cheap. Removals and
   version changes take the same question in reverse: what need does
   the current shape no longer serve?
2. **Decision.** One entry in the establishment log: the need, the
   service (or change) that answers it, what was considered and
   excluded with each exclusion's why. The user's confirmation is the
   verdict.
3. **Constraints.** The new service gets its constraints named with
   enforcement mechanisms, standing knowledge as the rebuttable
   default where it exists (for a datastore: the role-split model in
   infra-establish's references). A changed service gets its existing
   constraints re-checked against the change.
4. **Stand up and verify both ways.** Ground files as complete files
   (never fragments); secrets split out; then the catalog check *and*
   the behavioral check for the new capability — expected result
   stated before running, outcome read from actual output. A reused
   service family may have a lived walkthrough in infra-establish's
   references — consult it; never force its shape on a different
   service.
5. **Grow the manuals.** The infrastructure contract gains (or
   amends) its section — identities, reachability, refusals as
   contract terms, how changes are made; the operator manual gains
   the lived setup and any new reset path. If the project keeps a
   public setup guide, re-derive it from the operator manual — the
   manual stays the sole master.

## Hard lines (same as establishment)

Never re-frame the problem; never build the system (the ground ends
where application code and schema content begin); destructive acts
need explicit confirmation; host-level installs are the user's own
acts; project files speak the project's language. One logical change
per commit-sized delivery, commit message drafted for each.
