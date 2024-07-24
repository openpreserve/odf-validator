package org.openpreservation.format.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.openpreservation.utils.Checks;

/**
 * An implementation of {@link ZipArchiveCache} that caches the contents of the
 * archive and provides access to the <code>InputStream</code>s.
 */
public final class ZipFileProcessor implements ZipArchiveCache {
    private final Path path;
    private final Map<String, ZipArchiveEntry> entryCache;
    private final Map<String, byte[]> byteCache = new HashMap<>();
    private String firstEntryName = null;

    private ZipFileProcessor(final Path path) throws IOException {
        super();
        this.path = path;
        this.entryCache = cache(path);
    }

    static ZipFileProcessor of(final File file) throws IOException {
        Objects.requireNonNull(file, String.format(Checks.NOT_NULL, "File", "file"));
        return new ZipFileProcessor(file.toPath());
    }

    static ZipFileProcessor of(final Path path) throws IOException {
        Objects.requireNonNull(path, String.format(Checks.NOT_NULL, "Path", "path"));
        if (!Files.exists(path) || Files.isDirectory(path)) {
            throw new IllegalArgumentException(String.format("Path %s does not exist", path));
        }
        return new ZipFileProcessor(path);
    }

    private final Map<String, ZipArchiveEntry> cache(final Path path) throws IOException {
        Map<String, ZipArchiveEntry> result = new HashMap<>();
        try (ZipFile zipFile = new ZipFile(path)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntriesInPhysicalOrder();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (firstEntryName == null) {
                    firstEntryName = entry.getName();
                }
                result.put(entry.getName(), entry);
            }
        }
        return result;
    }

    @Override
    public List<ZipEntry> getZipEntries() {
        return this.entryCache.values().stream().map(new Function<ZipArchiveEntry, ZipEntry>() {
            @Override
            public ZipEntry apply(ZipArchiveEntry t) {
                return ZipEntryImpl.of(t);
            }
        }).collect(Collectors.toList());
    }

    /**
     * Returns the first physical {@link ZipEntry} in the archive.
     *
     * @return the first physical <code>ZipEntry</code> in the archive
     */
    public ZipEntry getFirstEntry() {
        return ZipEntryImpl.of(this.entryCache.get(this.firstEntryName));
    }

    @Override
    public ZipEntry getZipEntry(String entryName) {
        Objects.requireNonNull(entryName, String.format(Checks.NOT_NULL, "entryName", "String"));
        if (!this.entryCache.containsKey(entryName)) {
            return null;
        }
        return ZipEntryImpl.of(this.entryCache.get(entryName));
    }

    @Override
    public int size() {
        return this.entryCache.size();
    }

    @Override
    public List<String> getCachedEntryNames() {
        return this.byteCache.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public InputStream getEntryInputStream(String entryName) throws IOException {
        Objects.requireNonNull(entryName, String.format(Checks.NOT_NULL, "entryName", "String"));
        if (!this.entryCache.containsKey(entryName)) {
            return null;
        }
        if (!this.byteCache.containsKey(entryName)) {
            try (ZipFile zipFile = new ZipFile(this.path)) {
                ZipArchiveEntry entry = zipFile.getEntry(entryName);
                if (entry == null) {
                    return null;
                }
                InputStream is = zipFile.getInputStream(entry);
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    is.transferTo(bos);
                    this.byteCache.put(entryName,
                            bos.toByteArray());
                }
            }
        }
        return new ByteArrayInputStream(this.byteCache.get(entryName));
    }

}
