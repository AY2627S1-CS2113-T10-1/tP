package scheduleflow.cli;

import java.time.Clock;
import java.util.Objects;
import scheduleflow.commitment.CommitmentService;
import scheduleflow.planning.Planner;
import scheduleflow.planning.ScheduleService;
import scheduleflow.storage.Storage;
import scheduleflow.storage.StorageException;
import scheduleflow.task.TaskService;

/**
 * Routes parsed commands and owns the candidate, save, commit, response transaction order.
 */
public final class CommandDispatcher {
    private final TaskService tasks;
    private final CommitmentService commitments;
    private final Storage storage;
    private final Planner planner;
    private final ScheduleService schedules;
    private final TextRenderer renderer;
    private final Clock clock;

    /**
     * Creates the CommandDispatcher dependencies without performing I/O.
     */
    public CommandDispatcher(TaskService tasks, CommitmentService commitments, Storage storage,
            Planner planner, ScheduleService schedules, TextRenderer renderer, Clock clock) {
        this.tasks = Objects.requireNonNull(tasks, "tasks");
        this.commitments = Objects.requireNonNull(commitments, "commitments");
        this.storage = Objects.requireNonNull(storage, "storage");
        this.planner = Objects.requireNonNull(planner, "planner");
        this.schedules = Objects.requireNonNull(schedules, "schedules");
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /**
     * Captures time once and executes the command while preserving state on validation or save failure.
     * This feature is intentionally unfinished in the shared starter.
     */
    public CommandResponse execute(Command command, AppState state) throws StorageException {
        throw new UnsupportedOperationException("TODO(Printing): implement CommandDispatcher.execute");
    }
}
