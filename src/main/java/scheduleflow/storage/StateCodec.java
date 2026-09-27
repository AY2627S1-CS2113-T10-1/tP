package scheduleflow.storage;

import scheduleflow.model.Snapshot;

/**
 * Converts snapshots to and from the versioned text format without file I/O.
 */
public final class StateCodec {
    /**
     * Creates the StateCodec dependencies without performing I/O.
     */
    public StateCodec() {
    }

    /**
     * Encodes a complete snapshot in canonical version 1 form.
     * This feature is intentionally unfinished in the shared starter.
     */
    public String encode(Snapshot state) {
        throw new UnsupportedOperationException("TODO(Save): implement StateCodec.encode");
    }

    /**
     * Decodes and validates the complete text, translating structural failures to StorageException.
     * This feature is intentionally unfinished in the shared starter.
     */
    public Snapshot decode(String text) throws StorageException {
        throw new UnsupportedOperationException("TODO(Save): implement StateCodec.decode");
    }
}
