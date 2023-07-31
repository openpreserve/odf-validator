package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;

import org.openpreservation.utils.Checks;

public final class Zips {
    private Zips() {
        throw new AssertionError("Utility class 'Zips' should not be instantiated");
    }

    public static final ZipEntryProcessor extractorInstance(final Path root, final boolean extractDirectories) {
        Objects.requireNonNull(root, "Path root must not be null");
        return new ZipEntryProcessor() {
            @Override
            public void process(final java.util.zip.ZipEntry entry, final InputStream is) throws IOException {
                if (!this.accepts(entry)) {
                    return;
                }
                final Path target = root.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(target);
                } else {
                    Files.createDirectories(target.getParent());
                    try (OutputStream os = Files.newOutputStream(target)) {
                        is.transferTo(os);
                    }
                }
            }

            @Override
            public boolean accepts(final java.util.zip.ZipEntry entry) {
                return !entry.isDirectory() || extractDirectories;
            }

        };
    }

    public static final ZipProcessor.Factory factoryInstance() {
        return (processor -> (is -> {
            Objects.requireNonNull(is, "null");
            final List<ZipEntry> entries = new ArrayList<>();
            try (ZipInputStream zis = new ZipInputStream(is)) {
                java.util.zip.ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    entries.add(ZipEntryImpl.of(entry));
                    processor.process(entry, zis);
                }
            }
            return ZipArchiveImpl.from(entries);
        }));
    }

    public static final ZipArchiveCache zipArchiveCacheInstance(final ZipArchive archive,
            final Map<String, byte[]> cache) {
        Objects.requireNonNull(archive, String.format(Checks.NOT_NULL, "ZipArchive", "archive"));
        Objects.requireNonNull(cache, String.format(Checks.NOT_NULL, "Map<String, byte[]>", "cache"));
        return ZipArchiveCacheImpl.of(archive, cache);
    }

    public static final ZipArchiveCache zipArchiveCacheInstance(final Path path) throws IOException {
        Objects.requireNonNull(path, String.format(Checks.NOT_NULL, "Path", "path"));
        if (!Files.exists(path) || Files.isDirectory(path)) {
            throw new IllegalArgumentException(String.format("Path %s does not exist", path));
        }
        return ZipFileProcessor.of(path);
    }
}
