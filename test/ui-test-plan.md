# ScheduleFlow UI test plan

The application is an unfinished starter. No feature scenario has passed.
`ui-scenarios.json` is the executable source of truth for IDs, aims, setup,
ordered commands, exact per-command output, exit codes and active/planned
status. Review it after every code update. The contract's A01-A23 remain full
release acceptance criteria; these initial CLI cases do not replace them.

Run with Java 25 and Node.js 22+:

```text
node .agents/skills/test-ui/scripts/run-ui-tests.mjs
node --test test/ui-runner.test.mjs
```

On Windows, the Gradle wrapper is `gradlew.bat`; on Unix it is `./gradlew`.
The runner invokes `shadowJar` itself and checks Java 25. If the environment's
default Gradle home is unwritable, set `GRADLE_USER_HOME` to your writable cache
before running. Build timeout: 180 seconds. Startup, each command and exit:
5 seconds each. The first failure terminates that application and the session.
Transcripts are generated in ignored `build/ui-transcripts/`.

## Exact comparison protocol

- UTF-8 stdin/stdout/stderr. Convert CRLF to LF only; retain all other spaces,
  punctuation, lines and ordering. No dynamic placeholders or regex matching.
- `startup` is literal text before the first `scheduleflow> ` prompt (normally
  empty). For startup failure, specify `exitCode` and no steps instead.
- Each `steps` entry sends its `input` plus LF, waits for `output` plus LF and
  the exact next prompt, and compares before sending any subsequent input.
  `input: null` closes stdin. An `exitCode` expects termination instead of a
  prompt; it must appear only on the last step. Empty lines need explicit
  prompt-only support if added later, since they produce no response newline.
- Stderr must be empty during feature scenarios. Unexpected output, stderr,
  exit or timeout fails the case. The transcript includes the failing position,
  expected/actual output, captured stderr and process exit.
- Every case starts in its own temporary directory. Optional `files` maps
  relative paths to exact UTF-8 initial file contents. Multiple `sessions`
  share that case directory for restart testing, but not a process; steps
  inside each session share one live process. Cleanup never touches real data.
- Lists supplied by `--commands` and `--expected-outputs` must have matching
  lengths. Their final command must exit with status 0 (or use null for EOF).

## Starter readiness

All maintained cases are **planned pending implementation**. Preflight lists
every ownership-marked stub and each unexecuted case and returns **BLOCKED**
(exit 2). A build failure is FAILED (exit 1). Do not treat scaffold exceptions
as successful feature checks. Once components exist, review and activate the
cases; active failures must be fixed before commits. Foundation-only commits
may proceed with passing Java tests/checks and recorded BLOCKED UI status.

| ID | Contract coverage | Readiness |
| --- | --- | --- |
| UI-001 | A01 empty lists and absent plan | Planned |
| UI-002 | A06 adjacency; A13 midnight; A02 commitment counters/restart | Planned |
| UI-003 | A20 EOF | Planned |
| UI-004 | A17 unsupported store version | Planned; error wording chosen for fixture |

UI-004's error wording is a proposed exact regression baseline, not a new
contract requirement; the Save/Printing owners may update it when agreeing
their error text. No broad error wildcard is permitted.

Time-sensitive A04-A13 and A18-A23 require fixed-clock JUnit tests through the
injected App/Planner APIs. Do not add a production clock flag or change Main's
contract to make these fixtures work. Add a test-only launcher and explicit
fixtures if fixed-clock process tests are later needed. A14/A22 require fake
storage or deterministic failure injection. Their release checks remain
unexecuted until the responsible modules are implemented.

Foundation checkpoint: all required types now compile; 32 feature methods
remain ownership-marked stubs. Review found no justified change to the four
planned scenarios or their expectations. The foundation UI session remains
BLOCKED; it does not establish any CLI acceptance behavior.
