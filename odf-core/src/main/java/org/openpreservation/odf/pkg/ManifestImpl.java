package org.openpreservation.odf.pkg;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

final class ManifestImpl implements Manifest {
    private final String version;
    private final Map<String, FileEntry> entries;

    private ManifestImpl(final String version, final Map<String, FileEntry> entries) {
        this.version = version;
        this.entries = entries;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public int getEntryCount() {
        return this.entries.size();
    }

    @Override
    public Set<FileEntry> getEntries() {
        return this.entries.values().stream().collect(Collectors.toSet());
    }

    @Override
    public Set<FileEntry> getEntriesByMediaType(final String mediaType) {
        return this.entries.values().stream().filter(e -> e.getMediaType().equals(mediaType)).collect(Collectors.toSet());
    }

    @Override
    public FileEntry getEntry(final String entryName) {
        return this.entries.get(entryName);
    }

    @Override
    public String getEntryMediaType(final String entryName) {
        return this.entries.get(entryName).getMediaType();
    }

    static final ManifestImpl from(final String version, final Map<String, FileEntry> entries) {
        return new ManifestImpl(version, entries);
    }

    static final ManifestImpl from(final InputStream manifestStream) {
        return null;
    }
}
