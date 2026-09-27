package scheduleflow.cli;

import java.util.List;
import scheduleflow.model.Commitment;
import scheduleflow.model.Task;
import scheduleflow.planning.Plan;
import scheduleflow.planning.ScheduleView;

/**
 * Formats output without a trailing newline, clock reads, console I/O or state mutation.
 */
public final class TextRenderer {
    /**
     * Creates the TextRenderer dependencies without performing I/O.
     */
    public TextRenderer() {
    }

    /**
     * Returns command help.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String help() {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.help");
    }

    /**
     * Formats the added task.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String taskAdded(Task task) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.taskAdded");
    }

    /**
     * Formats the removed task.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String taskDeleted(Task task) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.taskDeleted");
    }

    /**
     * Formats the already sorted task list.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String tasks(List<Task> tasks) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.tasks");
    }

    /**
     * Formats the added commitment, including midnight as 24:00.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String commitmentAdded(Commitment commitment) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.commitmentAdded");
    }

    /**
     * Formats the removed commitment.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String commitmentDeleted(Commitment commitment) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.commitmentDeleted");
    }

    /**
     * Formats the already sorted weekly commitments.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String commitments(List<Commitment> commitments) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.commitments");
    }

    /**
     * Formats complete or partial allocation results with whole-plan totals.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String plan(Plan plan) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.plan");
    }

    /**
     * Formats the frozen schedule and whole-plan unallocated footer.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String schedule(ScheduleView view) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.schedule");
    }

    /**
     * Returns the absent-plan message.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String noPlan() {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.noPlan");
    }

    /**
     * Formats a one-line user error.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String error(String message) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.error");
    }

    /**
     * Formats a syntax error and its expected usage.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String parseError(ParseException exception) {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.parseError");
    }

    /**
     * Returns the normal goodbye message.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String exit() {
        throw new UnsupportedOperationException("TODO(Printing): implement TextRenderer.exit");
    }
}
