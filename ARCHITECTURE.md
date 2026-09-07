# Architecture

<!-- Describes the system AS IT IS NOW — not the aspiration. 1–2 pages max.
     Update trigger: a plan step's gate closes and this no longer matches
     reality. For the WHY behind any shape, link the ADR. -->

## Overview

<!-- One paragraph: what kind of system this is, in one breath.
     Fill-comment: the paragraph replaces this comment. -->

```
┌──────────┐      ┌──────────┐      ┌──────────┐
│  <comp>  │ ───▶ │  <comp>  │ ───▶ │  <comp>  │
└──────────┘      └──────────┘      └──────────┘
```

## Components

### <component>

Responsibility: <one sentence — what it owns, what nothing else may do>.
Why shaped this way: ADR-000N.

## Invariants

<!-- What must NEVER happen to the data / system, and where each rule
     is enforced (DB constraint, module boundary, ...). -->
- <invariant> — enforced in <where>.

## Codemap

<!-- Where to find things. Directory → what lives there. -->
| Path | What lives there |
|---|---|
| `src/...` | |
