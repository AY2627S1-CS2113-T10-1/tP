package scheduleflow.cli;

import java.time.Clock;
import java.util.Objects;
import scheduleflow.commitment.CommitmentService;
import scheduleflow.planning.EarliestDeadlinePlanner;
import scheduleflow.planning.ScheduleService;
import scheduleflow.storage.Storage;
import scheduleflow.task.TaskService;

/**
 * Owns startup, the command loop and recoverable user errors; returns a process status.
 */
public final class App {
    private final Storage storage;
    private final ConsoleUi ui;
    private final CommandParser parser;
    private final TextRenderer renderer;
    private final CommandDispatcher dispatcher;

    /**
     * Creates the App dependencies without performing I/O.
     */
    public App(Storage storage, Clock clock, ConsoleUi ui) {
        this.storage = Objects.requireNonNull(storage, "storage");
        this.ui = Objects.requireNonNull(ui, "ui");
        parser = new CommandParser();
        renderer = new TextRenderer();
        dispatcher = new CommandDispatcher(new TaskService(), new CommitmentService(), storage,
                new EarliestDeadlinePlanner(), new ScheduleService(), renderer, Objects.requireNonNull(clock, "clock"));
    }

    /**
     * Loads once and runs commands, returning zero for normal exit or one for startup/input failure.
     * This feature is intentionally unfinished in the shared starter.
     */
    public int run() {
        throw new UnsupportedOperationException("TODO(Printing): implement App.run");
    }
}
