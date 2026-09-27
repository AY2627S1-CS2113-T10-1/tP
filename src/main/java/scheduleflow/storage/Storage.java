package scheduleflow.storage;

import scheduleflow.model.Snapshot;

/**
 * Loads and atomically saves the sole persisted domain representation.
 */
public interface Storage {
    /**
     * Loads a complete valid snapshot, or an empty store when the file is definitely absent.
     *
     * @throws StorageException If the store cannot be read or validated.
     */
    Snapshot load() throws StorageException;

    /**
     * Saves a candidate completely before callers publish it in memory.
     *
     * @throws StorageException If atomic replacement cannot complete; the old target remains intact.
     */
    void save(Snapshot state) throws StorageException;
}
