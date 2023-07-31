package org.openpreservation.format.zip;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class ZipArchiveCacheImpl implements ZipArchiveCache {
    private final ZipArchive archive;
    private final Map<String, byte[]> cache;
    
    private ZipArchiveCacheImpl(final ZipArchive archive, final Map<String, byte[]> cache) {
        super();
        this.archive = archive;
        this.cache = Collections.unmodifiableMap(cache);
    }

    @Override
    public List<String> getCachedEntryNames() {
        return this.cache.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public InputStream getEntryInputStream(String entryName) {
        return new ByteArrayInputStream(this.cache.get(entryName));
    }

    @Override
    public List<ZipEntry> getZipEntries() {
        return this.archive.getZipEntries();
    }

    @Override
    public ZipEntry getZipEntry(String entryName) {
        return this.archive.getZipEntry(entryName);
    }

    @Override
    public int size() {
        return this.archive.size();
    }

    static final ZipArchiveCacheImpl of(final ZipArchive archive, final Map<String, byte[]> cache) {
        return new ZipArchiveCacheImpl(archive, cache);
    } 
}
