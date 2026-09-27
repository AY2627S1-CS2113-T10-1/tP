package scheduleflow.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Owns line-oriented console I/O and prompt flushing.
 */
public final class ConsoleUi {
    private final BufferedReader input;
    private final PrintWriter output;

    /**
     * Creates the ConsoleUi dependencies without performing I/O.
     */
    public ConsoleUi(BufferedReader input, PrintWriter output) {
        this.input = Objects.requireNonNull(input, "input");
        this.output = Objects.requireNonNull(output, "output");
    }

    /**
     * Writes and flushes the prompt without a newline.
     * This feature is intentionally unfinished in the shared starter.
     */
    public void prompt() {
        throw new UnsupportedOperationException("TODO(Printing): implement ConsoleUi.prompt");
    }

    /**
     * Reads a line, returning null only for end of input.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String readLine() throws IOException {
        throw new UnsupportedOperationException("TODO(Printing): implement ConsoleUi.readLine");
    }

    /**
     * Writes text followed by a newline and flushes.
     * This feature is intentionally unfinished in the shared starter.
     */
    public void write(String text) {
        throw new UnsupportedOperationException("TODO(Printing): implement ConsoleUi.write");
    }
}
