# ScheduleFlow tests

Root AGENTS.md applies to tests, including loading the Java coding standard
and Git standard and invoking the repository-local test-ui skill after each
coherent code update.

Tests mirror source packages and belong to the role that owns the tested
type. Printing owns cli tests and the process UI plan/fixtures; Save and
Printing coordinate rollback tests. Optimizer owns PlanningRecordsTest.

Use JUnit 5, fixed local time inputs, Clock.fixed with an explicit zone where
a clock is required, temporary storage paths and purposeful fake Storage.
Never depend on the current date, the developer's saved data or timezone.
Keep assertions focused on behavior and contract invariants. No disabled
placeholder tests or expected-stub-exception acceptance tests.

Foundation tests deliberately do not certify deferred services, planning,
persistence or CLI acceptance cases. Add those tests with the owning feature.
