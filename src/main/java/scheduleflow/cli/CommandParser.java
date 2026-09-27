package scheduleflow.cli;

/**
 * Parses the complete case-sensitive command grammar without inspecting application state.
 */
public final class CommandParser {
    /**
     * Creates the CommandParser dependencies without performing I/O.
     */
    public CommandParser() {
    }

    /**
     * Parses one nonblank line, translating syntax, number and date errors to ParseException.
     * This feature is intentionally unfinished in the shared starter.
     */
    public Command parse(String line) throws ParseException {
        throw new UnsupportedOperationException("TODO(Printing): implement CommandParser.parse");
    }
}
