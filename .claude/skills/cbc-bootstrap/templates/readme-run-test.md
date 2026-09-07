<!-- Template — master copy in correctness-by-construction (ADR-0008);
     checked against concept v1 (ADR-0003, ADR-0005 — practice-born).
     Extracted 2026-09-03 from checkout-system's lived README Run and
     Test sections plus its Prerequisites stack line (ADR-0013).
     Changes on extraction: the JDK version generalized to a
     placeholder. Merge into the run's README at Stage 5, when the
     harness is real, and fill from the certified commands; the
     filled sections are the run's own (ADR-0008). Only the material
     below travels — this header stays with the master. The stack
     line joins the Prerequisites section infra-establish opened; the
     command bodies are the lived Spring/Maven shape — a different
     stack writes its own, keeping the shape: Run stands the ground
     up and ends in a proof of life; Test is the one standard test
     command, with what it really drives stated beside it. -->

<!-- Into the existing Prerequisites section: -->

- JDK <version> (Maven rides in via the committed wrapper)

## Run

```bash
# stand the ground up (first time: creates roles/schema; see the
# operator manual for the two-way verification)
cp .env.example .env
podman compose up -d

# run the system against it, as the runtime identity
set -a; . ./.env; set +a
./mvnw spring-boot:run
# proof of life: curl localhost:8080/actuator/health → status UP, db UP
```

## Test

```bash
# the one standard test command — unit and integration tests together;
# integration tests drive a real throwaway PostgreSQL (Testcontainers,
# rootless podman), so the ground does not need to be up
./mvnw test
```
