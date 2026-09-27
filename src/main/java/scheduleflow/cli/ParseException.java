package scheduleflow.cli;

import java.util.Objects;

/**
 * Reports invalid command syntax together with the recognized command's usage.
 */
public final class ParseException extends Exception {
    private final String usage;

    /**
     * Creates a parse failure with a nonnull usage hint.
     */
    public ParseException(String message, String usage) {
        super(message);
        this.usage = Objects.requireNonNull(usage, "usage");
    }

    public String usage() {
        return usage;
    }
}
