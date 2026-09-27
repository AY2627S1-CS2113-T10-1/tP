package scheduleflow.cli;

import java.util.Objects;

/**
 * Carries formatted output and whether command execution requests normal termination.
 */
public record CommandResponse(String output, boolean shouldExit) {
    /**
     * Requires actual output text; unfinished commands must never fabricate a response.
     */
    public CommandResponse {
        Objects.requireNonNull(output, "output");
    }
}
