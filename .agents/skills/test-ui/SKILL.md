---
name: test-ui
description: Run ScheduleFlow console scenarios after every code update, with per-command assertions, isolated saved data and honest scaffold readiness reporting.
---

# ScheduleFlow UI regression

Use this repository-local skill, not the unrelated global MoistBot skill.

1. Review `test/ui-test-plan.md` and its linked JSON fixtures after each code
   update. Update justified expectations and scenario status together.
2. From the repository root, with Java 25 and Node.js 22 or later, run:

   ```text
   node .agents/skills/test-ui/scripts/run-ui-tests.mjs
   ```

   The runner always builds current code. Set `GRADLE_USER_HOME` to a writable
   cache if needed. Supply `--fixtures path.json` for additional scenarios in
   the documented format, or matching JSON lists:

   ```text
   node .agents/skills/test-ui/scripts/run-ui-tests.mjs --commands '["task list","exit"]' --expected-outputs '["No tasks found.","Goodbye for now!"]'
   ```

3. Read the transcript path printed by the runner. Report actual passed,
   failed, blocked and planned IDs. A BLOCKED preflight is exit code 2, never a
   passed feature test. It lists missing components and unexecuted scenarios.
4. Stop at the first failure, fix it, and start a fresh test session. Do not
   edit expected text merely to hide a regression. Do not use a stub exception
   as evidence that a command works.

The runner sends one command, checks its exact response and prompt/exit, then
sends the next. Independent cases get separate temporary directories; sessions
within a case share saved data for restart checks. No dynamic output
placeholders are supported. See the plan for framing, timeout and comparison
rules. Transcripts include build output, actual input, stdout, stderr and exit
status under ignored `build/ui-transcripts/`.
