package scheduleflow.storage;

/**
 * Reports a load or atomic-save failure without publishing partial state.
 */
public final class StorageException extends Exception {
    /**
     * Creates a storage failure with a user-facing explanation.
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Creates a storage failure preserving its underlying cause.
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
