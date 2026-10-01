package scheduleflow.task;

import java.time.LocalDateTime;
import java.util.List;
import scheduleflow.model.Snapshot;
import scheduleflow.model.Task;

/**
 * Creates task mutation candidates without saving or changing live state.
 */
public final class TaskService {
    /**
     * Creates the TaskService dependencies without performing I/O.
     */
    public TaskService() {
    }

    /**
     * Creates a candidate with the next ID after validating the deadline against now.
     * This feature is intentionally unfinished in the shared starter.
     */
    public Snapshot add(Snapshot state, String name, LocalDateTime deadline, int minutes,
            LocalDateTime now) {
        throw new UnsupportedOperationException("TODO(Task): implement TaskService.add");
    }

    /**
     * Removes a known task from a candidate while preserving both counters.
     * This feature is intentionally unfinished in the shared starter.
     */
    public Snapshot delete(Snapshot state, int taskId) {
        throw new UnsupportedOperationException("TODO(Task): implement TaskService.delete");
    }

    /**
     * Returns an immutable view in ascending numeric task ID order.
     * This feature is intentionally unfinished in the shared starter.
     */
    public List<Task> list(Snapshot state) {
        throw new UnsupportedOperationException("TODO(Task): implement TaskService.list");
    }
}
