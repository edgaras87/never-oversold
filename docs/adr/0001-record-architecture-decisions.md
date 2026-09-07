# 0001. Record architecture decisions

Date: 2026-09-07
Status: Accepted

## Context

Significant decisions — what to use, where a boundary falls, what to
build and what to take, what a term means — will be made throughout
this project. Without a record, the *why* evaporates:
debates recur, and future maintainers can't distinguish deliberate choices
from accidents.

## Options considered

1. Keep decisions in heads / chat history — free, but unsearchable and lossy.
2. One big design document — goes stale as a whole; nobody updates it.
3. One small immutable record per decision (ADRs) — cheap to write, cheap
   to read, history preserved.

## Decision

We will record every significant architecture decision as a numbered ADR in
`docs/adr/`, using this template (context / options / decision /
consequences). ADRs are immutable: a reversed decision gets a *new* ADR
marked "Supersedes NNNN", and the old one's status becomes
"Superseded by NNNN".

Threshold: if a future developer would ask "why on earth is it done this
way?", it deserves an ADR.

## Consequences

Good: decisions and their rejected alternatives are searchable forever;
"we already considered that" ends recurring debates.
Bad: small ongoing discipline cost; requires honesty about downsides at
decision time.

<!-- Template for the next ADR: copy this file, bump the number,
     replace every section. Keep it under one page. -->
