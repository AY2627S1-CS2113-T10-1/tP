package scheduleflow.model;

import java.time.LocalDateTime;
import scheduleflow.common.InputRules;
import scheduleflow.common.ValidationException;

/**
 * Stores an unfinished task's whole estimate, independent of any generated plan.
 * A past deadline remains structurally valid so saved tasks can be restored.
 */
public record Task(int id, String name, LocalDateTime deadline, int remainingMinutes) {
    /**
     * Creates a structurally valid task and normalizes its name.
     */
    public Task {
        if (id <= 0) {
            throw new ValidationException("Task ID must be positive.");
        }
        name = InputRules.normalizeName(name);
        if (deadline == null || deadline.getYear() < 1 || deadline.getYear() > 9999
                || deadline.getSecond() != 0 || deadline.getNano() != 0) {
            throw new ValidationException("Deadline must have minute precision and a year from 0001 to 9999.");
        }
        InputRules.requireDuration(remainingMinutes);
    }

    /**
     * Returns the stable task identifier with no zero padding.
     */
    public String displayId() {
        return "T" + id;
    }
}
