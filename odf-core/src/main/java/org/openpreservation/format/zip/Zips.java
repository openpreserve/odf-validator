package org.openpreservation.format.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipInputStream;

import org.openpreservation.utils.Checks;

/**
 * Utility class for working with {@link ZipArchive}s.
 */
public final class Zips {
    /**
     * Create a {@link ZipEntryProcessor} instance that processes the entries and
     * extracts them to the supplied root <code>Path</code>.
     * 
     * @param root               the root <code>Path</code> to extract the entries
     *                           to
     * @param extractDirectories <code>true</code> to extract directories,
     *                           <code>false</code> to ignore them
     * @return a <code>ZipEntryProcessor</code> that processes the entries and
     *         extracts them to the supplied root <code>Path</code>
     */
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

    /**
     * Create a {@link ZipProcessor.Factory} instance that can be used to create
     * {@link ZipProcessor} instances.
     * 
     * @return a <code>ZipProcessor.Factory</code>
     */
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
            return ZipArchiveImpl.from(Paths.get(""),  entries);
        }));
    }

    /**
     * Create a {@link ZipArchiveCache} instance that caches the contents of the
     * archive and provides access to the <code>InputStream</code>s.
     * 
     * @param file the <code>File</code> to cache
     * @return a <code>ZipArchiveCache</code> instance that caches the contents of
     *         the archive and provides access to the <code>InputStream</code>s
     */
    public static final ZipArchiveCache zipArchiveCacheInstance(final File file) throws IOException {
        Objects.requireNonNull(file, String.format(Checks.NOT_NULL, "File", "file"));
        return ZipFileProcessor.of(file.toPath());
    }

    /**
     * Create a {@link ZipArchiveCache} instance that caches the contents of the
     * archive and provides access to the <code>Path</code>s.
     * 
     * @param path the <code>Path</code> to cache
     * @return a <code>ZipArchiveCache</code> instance that caches the contents of
     *         the archive and provides access to the <code>InputStream</code>s
     */
    public static final ZipArchiveCache zipArchiveCacheInstance(final Path path) throws IOException {
        Objects.requireNonNull(path, String.format(Checks.NOT_NULL, "Path", "path"));
        if (!Files.exists(path) || Files.isDirectory(path)) {
            throw new IllegalArgumentException(String.format("Path %s does not exist", path));
        }
        return ZipFileProcessor.of(path);
    }

    public static final ZipEntry zipEntryInstance(final String name, final int size, final int compressedSize,
            final int crc32,
            final int method, final boolean isDirectory, final byte[] extra) {
        Objects.requireNonNull(name, String.format(Checks.NOT_NULL, "String", "name"));
        return ZipEntryImpl.of(name, size, compressedSize, crc32, method, isDirectory, extra);
    }

    private Zips() {
        throw new AssertionError("Utility class 'Zips' should not be instantiated");
    }
}
