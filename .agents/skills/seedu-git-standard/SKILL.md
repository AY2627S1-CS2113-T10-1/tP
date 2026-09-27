---
name: seedu-git-standard
description: Apply SE Education Git conventions before every ScheduleFlow commit and when naming project branches.
---

# Git standard

Source: [SE Education Git conventions](https://se-education.org/guides/conventions/git.html), read 2026-09-27.

Required: write a meaningful imperative subject, capitalize its first letter,
and omit a final period. Keep it at most 72 characters. Use meaningful kebab-case
branch names; for an issue branch, prefix the issue number.

Recommendations: aim for at most 50 subject characters. An applicable scope
prefix is optional. Give nontrivial changes a body, separated by a blank line,
wrapped at 72 columns, with paragraphs as needed. Explain what and why rather
than narrating the diff. Describe the existing problem in present tense, why it
matters, the proposed change and why that approach helps. Avoid redundant
wording such as "currently". Split changes if the explanation becomes unwieldy.

## Project workflow

1. Read this skill before every commit. Inspect status and both diffs first.
2. Preserve unrelated index/worktree changes. Stage named intended files only;
   inspect the complete staged diff and run `git diff --cached --check`.
3. Commit each significant verified change locally. Each quality refactoring
   gets its own commit and explanatory body. Include material checks/blockers.
4. Active UI failures must be fixed and rerun. Intended starter stubs may be
   committed with passing foundation checks and a recorded BLOCKED UI run.
5. Never stage saved data, build output, transcripts, secrets or temporary
   directories. Do not push, amend, merge or rewrite history without a new
   explicit instruction. Use lightweight tags if tags are requested.

These workflow steps are project requirements, stronger than the source's
recommendations about when to include a commit body.
