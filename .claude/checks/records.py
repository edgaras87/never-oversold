#!/usr/bin/env python3
"""Mechanical checks on the living records.

Finds what a step's close should not leave behind: a link or path
that leads nowhere, a cited ADR, slice, kill or test that does not
exist, and two copies of one fact that disagree. It judges nothing
else: whether a line is still true is the closing gate's question.

Run from the repository root:  python3 .claude/checks/records.py
Exit code 0 when nothing is found, 1 otherwise.

History is not checked: the devlog, the ADRs, the derivation, the
bootstrap requirements and CHANGELOG's released versions record what
was true then, and keep their words (.claude/rules/one-home.md).
Nor are PLAN.md and RETRO.md: PLAN's closed steps are history, and
RETRO's decisions name files the next run will have. Both are read
at the gate instead.
"""

import re
import sys
from pathlib import Path

ROOT = Path.cwd()

# The living records, and the code whose comments cite them.
LIVING = [
    "README.md", "ARCHITECTURE.md", "TODO.md",
    "docs/system/intent.md", "docs/system/definition.md",
    "docs/system/registry.md",
    "docs/construction/sl-*.md",
    "docs/infrastructure/*.md",
    ".claude/CLAUDE.md", ".claude/rules/*.md", ".claude/shapes/*.md",
    ".claude/skills/retrospective/*.md", ".claude/apply-retro/*.md",
]
CODE = ["src/**/*.java", "src/main/resources/**/*.yaml"]

# A path worth checking starts at one of the repo's own top folders.
PATH_ROOTS = ("docs/", ".claude/", "src/", "devlog/", "infrastructure/")


def files(patterns):
    seen = []
    for pattern in patterns:
        for path in sorted(ROOT.glob(pattern)):
            if path.is_file() and path not in seen:
                seen.append(path)
    return seen


def slug(heading):
    """GitHub's anchor for a heading."""
    text = heading.strip().lower()
    text = re.sub(r"[^\w\- ]", "", text)
    return text.replace(" ", "-")


def anchors(path):
    found = set()
    for line in path.read_text(encoding="utf-8").splitlines():
        if line.startswith("#"):
            found.add(slug(line.lstrip("#")))
    return found


def known_adrs():
    return {int(p.name[:4]) for p in (ROOT / "docs/adr").glob("[0-9]*.md")}


def known_slices():
    text = (ROOT / "docs/system/registry.md").read_text(encoding="utf-8")
    return {int(n) for n in re.findall(r"^### SL-(\d+)", text, re.M)}


def known_kills():
    text = (ROOT / "docs/system/definition.md").read_text(encoding="utf-8")
    section = text.split("### Kills", 1)[1].split("\n### ", 1)[0]
    return {int(n) for n in re.findall(r"^(\d+)\. ", section, re.M)}


def known_tests():
    return {p.stem for p in (ROOT / "src/test").rglob("*.java")}


def outside_code(lines):
    """The lines a reader follows: a fenced code block is an example."""
    fenced = False
    for number, line in enumerate(lines, 1):
        if line.lstrip().startswith("```"):
            fenced = not fenced
            continue
        if not fenced:
            yield number, line


def check_links(path, lines, report):
    for number, line in outside_code(lines):
        for target in re.findall(r"\]\(([^)\s]+)\)", line):
            if re.match(r"[a-z]+:", target):
                continue  # a URL
            file_part, _, anchor = target.partition("#")
            dest = (path.parent / file_part).resolve() if file_part else path
            if not dest.exists():
                report(path, number, f"link leads nowhere: {target}")
            elif anchor and dest.suffix == ".md" and anchor not in anchors(dest):
                report(path, number, f"no such heading: {target}")


def check_paths(path, lines, report):
    for number, line in enumerate(lines, 1):
        for cited in re.findall(r"`([^`\s]+)`", line):
            if not cited.startswith(PATH_ROOTS) or any(c in cited for c in "<*…"):
                continue  # not a path, or a pattern standing for many
            cited = cited.rstrip(".,;:")
            if not (ROOT / cited.split("#")[0]).exists():
                report(path, number, f"path does not exist: {cited}")


def check_numbers(path, lines, report, adrs, slices, kills, tests):
    for number, line in enumerate(lines, 1):
        for n in re.findall(r"(?<!CBC )\bADR-(\d{4})\b", line):
            if int(n) not in adrs:
                report(path, number, f"no such ADR: ADR-{n}")
        for n in re.findall(r"\bSL-(\d+)\b", line):
            if int(n) not in slices:
                report(path, number, f"no such slice: SL-{n}")
        for n in re.findall(r"\b[Kk]ill (\d+)\b", line):
            if int(n) not in kills:
                report(path, number, f"no such kill: kill {n}")
        # A test class: capitalised, with lower case in it; not an
        # annotation (@SpringBootTest), nor a pattern (*StormIT).
        for name in re.findall(r"(?<![@*\w])([A-Z][a-z][A-Za-z]*(?:IT|Test))\b", line):
            if name not in tests:
                report(path, number, f"no such test: {name}")


def check_version(report):
    pom = (ROOT / "pom.xml").read_text(encoding="utf-8")
    project = pom.split("<parent>")[0] + pom.split("</parent>")[-1]
    version = re.search(r"<version>([^<]+)</version>", project).group(1)
    log = (ROOT / "CHANGELOG.md").read_text(encoding="utf-8")
    released = re.search(r"^## \[(\d[^\]]*)\]", log, re.M).group(1)
    if version.removesuffix("-SNAPSHOT") != released:
        report(ROOT / "pom.xml", 0,
               f"version {version} does not follow CHANGELOG's latest "
               f"release, {released}")


def main():
    findings = []

    def report(path, line, message):
        findings.append(f"{path.relative_to(ROOT)}:{line}: {message}")

    adrs, slices = known_adrs(), known_slices()
    kills, tests = known_kills(), known_tests()

    for path in files(LIVING):
        lines = path.read_text(encoding="utf-8").splitlines()
        check_links(path, lines, report)
        check_paths(path, lines, report)
        check_numbers(path, lines, report, adrs, slices, kills, tests)
    for path in files(CODE):
        # Only the comments: the compiler already checks the code.
        lines = [line if line.lstrip().startswith(("*", "/", "#")) else ""
                 for line in path.read_text(encoding="utf-8").splitlines()]
        check_paths(path, lines, report)
        check_numbers(path, lines, report, adrs, slices, kills, tests)
    check_version(report)

    for finding in findings:
        print(finding)
    print(f"{len(findings)} found")
    return 1 if findings else 0


if __name__ == "__main__":
    sys.exit(main())
