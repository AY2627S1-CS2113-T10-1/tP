package scheduleflow.commitment;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import scheduleflow.model.Commitment;
import scheduleflow.model.Snapshot;

/**
 * Creates weekly commitment candidates without saving or changing live state.
 */
public final class CommitmentService {
    /**
     * Creates the CommitmentService dependencies without performing I/O.
     */
    public CommitmentService() {
    }

    /**
     * Creates a candidate after checking overlap and counter overflow.
     * This feature is intentionally unfinished in the shared starter.
     */
    public Snapshot add(Snapshot state, String name, DayOfWeek day, LocalTime start, int minutes) {
        throw new UnsupportedOperationException("TODO(Commitment): implement CommitmentService.add");
    }

    /**
     * Removes a known weekly series from a candidate without reusing its ID.
     * This feature is intentionally unfinished in the shared starter.
     */
    public Snapshot delete(Snapshot state, int commitmentId) {
        throw new UnsupportedOperationException("TODO(Commitment): implement CommitmentService.delete");
    }

    /**
     * Returns an immutable view ordered by weekday, start and numeric ID.
     * This feature is intentionally unfinished in the shared starter.
     */
    public List<Commitment> list(Snapshot state) {
        throw new UnsupportedOperationException("TODO(Commitment): implement CommitmentService.list");
    }
}
