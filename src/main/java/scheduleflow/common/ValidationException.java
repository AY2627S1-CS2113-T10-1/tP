package scheduleflow.common;

/**
 * Reports a rejected domain value without changing application state.
 */
public final class ValidationException extends RuntimeException {
    /**
     * Creates a validation failure with a user-facing explanation.
     */
    public ValidationException(String message) {
        super(message);
    }
}
