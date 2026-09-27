package scheduleflow.planning;

import java.time.LocalDateTime;
import scheduleflow.model.Snapshot;

/**
 * Allocates the earliest free slots in deadline and numeric task ID order.
 */
public final class EarliestDeadlinePlanner implements Planner {
    /**
     * Creates the EarliestDeadlinePlanner dependencies without performing I/O.
     */
    public EarliestDeadlinePlanner() {
    }

    @Override
    public Plan generate(Snapshot state, LocalDateTime now) {
        throw new UnsupportedOperationException("TODO(Optimizer): implement EarliestDeadlinePlanner.generate");
    }
}
