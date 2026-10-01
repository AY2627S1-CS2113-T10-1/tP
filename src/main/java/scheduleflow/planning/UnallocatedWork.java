package scheduleflow.planning;

import java.time.LocalDateTime;
import scheduleflow.common.InputRules;
import scheduleflow.common.ValidationException;

/**
 * Describes a task's positive unscheduled remainder; source matching belongs to the planner.
 */
public record UnallocatedWork(int taskId, String taskName, LocalDateTime deadline,
        int minutes, UnallocatedReason reason) {
    /**
     * Validates the remainder's own fields and normalizes its task name.
     */
    public UnallocatedWork {
        if (taskId <= 0 || reason == null) {
            throw new ValidationException("Unallocated work requires a positive task ID and reason.");
        }
        taskName = InputRules.normalizeName(taskName);
        InputRules.requireDuration(minutes);
        if (deadline == null || deadline.getSecond() != 0 || deadline.getNano() != 0) {
            throw new ValidationException("Unallocated deadline must have minute precision.");
        }
    }
}
