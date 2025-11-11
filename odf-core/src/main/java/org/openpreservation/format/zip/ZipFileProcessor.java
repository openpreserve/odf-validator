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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.openpreservation.utils.Checks;

/**
 * An implementation of {@link ZipArchiveCache} that caches the contents of the
 * archive and provides access to the <code>InputStream</code>s.
 */
public final class ZipFileProcessor implements ZipArchiveCache, AutoCloseable {
    private final Path path;
    private final Map<String, ZipArchiveEntry> entryCache;
    private final Map<String, byte[]> byteCache = new HashMap<>();
    private long currentCacheSize = 0;
    private String firstEntryName = null;
    private static final long MAX_CACHE_SIZE = Runtime.getRuntime().maxMemory() / 4;
    private static final long MAX_ITEM_SIZE = MAX_CACHE_SIZE / 20;
    private final ZipFile zipFile;

    private ZipFileProcessor(final Path path) throws IOException {
        super();
        this.path = path;
        this.zipFile = new ZipFile.Builder().setSeekableByteChannel(Files.newByteChannel(path)).get();
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
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntriesInPhysicalOrder();
        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            if (firstEntryName == null) {
                firstEntryName = entry.getName();
            }
            result.put(entry.getName(), entry);
        }
        return result;
    }

    @Override
    public Path getPath() {
        return this.path;
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
    public InputStream getEntryInputStream(String entryName) throws IOException, NoSuchElementException {
        Objects.requireNonNull(entryName, String.format(Checks.NOT_NULL, "entryName", "String"));
        if (!this.entryCache.containsKey(entryName)) {
            throw new NoSuchElementException(String.format("Entry %s not found in zip file", entryName));
        }
        return retrieveEntryInputStream(entryName);
    }

    private final InputStream retrieveEntryInputStream(final String entryName) throws IOException, NoSuchElementException {
        if (!this.byteCache.containsKey(entryName)) {
            ZipArchiveEntry entry = zipFile.getEntry(entryName);
            if (entry == null) {
                throw new NoSuchElementException(String.format("Entry %s not found in zip file", entryName));
            }
            return cacheEntryInputStream(entry);
        }
        return new ByteArrayInputStream(this.byteCache.get(entryName));
    }

    private final InputStream cacheEntryInputStream(final ZipArchiveEntry entry)
            throws IOException {
        InputStream is = zipFile.getInputStream(entry);
        if (entry.getSize() > MAX_ITEM_SIZE || (this.currentCacheSize + entry.getSize()) > MAX_CACHE_SIZE) {
            return is;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            is.transferTo(bos);
            this.byteCache.put(entry.getName(),
                    bos.toByteArray());
            this.currentCacheSize += bos.size();
        }
        return new ByteArrayInputStream(this.byteCache.get(entry.getName()));
    }

    @Override
    public void close() throws Exception {
        this.zipFile.close();
    }
}
