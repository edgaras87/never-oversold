<!-- Template — master copy in correctness-by-construction (ADR-0008);
     checked against concept v1 (ADR-0003, ADR-0005 — practice-born).
     Extracted 2026-09-03 from checkout-system's lived README
     Prerequisites section (ADR-0013). Changes on extraction: the
     stack line (JDK via the committed wrapper) removed — its fact is
     born at cbc-bootstrap, whose fragment carries it; the
     operator-manual path generalized to a placeholder. Merge into
     the run's README when the ground stands and fill; the filled
     section is the run's own (ADR-0008). Only the section below
     travels — this header stays with the master. A different ground
     writes different lines; the shape (each line one thing a
     stranger's machine needs, the manual linked) is what carries. -->

## Prerequisites

- podman with a compose provider (`podman compose` answers) — the
  infrastructure ground; details in
  [<path/to/operator-manual.md>](<path/to/operator-manual.md>)
