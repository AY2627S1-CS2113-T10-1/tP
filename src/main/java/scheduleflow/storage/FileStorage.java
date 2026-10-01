package scheduleflow.storage;

import java.nio.file.Path;
import java.util.Objects;
import scheduleflow.model.Snapshot;

/**
 * Owns UTF-8 loading and atomic file replacement at a fixed absolute path.
 */
public final class FileStorage implements Storage {
    private final Path file;
    private final StateCodec codec;

    /**
     * Creates the FileStorage dependencies without performing I/O.
     */
    public FileStorage(Path file, StateCodec codec) {
        this.file = Objects.requireNonNull(file, "file").toAbsolutePath().normalize();
        this.codec = Objects.requireNonNull(codec, "codec");
    }

    @Override
    public Snapshot load() throws StorageException {
        throw new UnsupportedOperationException("TODO(Save): implement FileStorage.load");
    }

    @Override
    public void save(Snapshot state) throws StorageException {
        throw new UnsupportedOperationException("TODO(Save): implement FileStorage.save");
    }
}
