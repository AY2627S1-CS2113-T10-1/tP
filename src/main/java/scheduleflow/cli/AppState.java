package scheduleflow.cli;

import java.util.Objects;
import java.util.Optional;
import scheduleflow.model.Snapshot;
import scheduleflow.planning.Plan;

/**
 * Holds the saved snapshot and optional temporary plan for one running application.
 */
public final class AppState {
    private Snapshot snapshot;
    private Optional<Plan> currentPlan = Optional.empty();

    /**
     * Creates live state from a loaded snapshot with no restored plan.
     */
    public AppState(Snapshot initial) {
        snapshot = Objects.requireNonNull(initial, "initial");
    }

    /**
     * Returns the last successfully loaded or saved snapshot.
     */
    public Snapshot snapshot() {
        return snapshot;
    }

    /**
     * Returns the current temporary plan, if one has been generated since the last mutation.
     */
    public Optional<Plan> currentPlan() {
        return currentPlan;
    }

    /**
     * Publishes a previously validated, successfully saved nonnull candidate and clears the plan.
     * The caller establishes this precondition before saving; no domain validation or I/O occurs here.
     */
    public void commit(Snapshot saved) {
        snapshot = Objects.requireNonNull(saved, "saved");
        currentPlan = Optional.empty();
    }

    /**
     * Replaces only the plan; callers guarantee that its source equals the current snapshot.
     */
    public void replacePlan(Plan plan) {
        currentPlan = Optional.of(Objects.requireNonNull(plan, "plan"));
    }
}
