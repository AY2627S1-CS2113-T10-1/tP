package scheduleflow.planning;

import java.time.LocalDate;

/**
 * Projects an existing frozen plan into one day's complete study window.
 */
public final class ScheduleService {
    /**
     * Creates the ScheduleService dependencies without performing I/O.
     */
    public ScheduleService() {
    }

    /**
     * Returns a merged projection without replanning or reading the current clock.
     * This feature is intentionally unfinished in the shared starter.
     */
    public ScheduleView forDate(Plan plan, LocalDate date) {
        throw new UnsupportedOperationException("TODO(Optimizer): implement ScheduleService.forDate");
    }
}
