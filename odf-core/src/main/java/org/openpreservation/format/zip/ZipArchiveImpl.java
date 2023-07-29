package org.openpreservation.format.zip;

import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

import org.openpreservation.utils.Checks;

public class ZipArchiveImpl implements ZipArchive {
    private final List<ZipEntry> entries;

    private ZipArchiveImpl(final List<ZipEntry> entries) {
        this.entries = Collections.unmodifiableList(entries);
    }

    @Override
    public List<ZipEntry> getZipEntries() {
        return this.entries;
    }

    @Override
    public ZipEntry getZipEntry(String entryName) {
        return this.entries.stream().filter(e -> e.getName().equals(entryName)).findFirst().orElse(null);
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    static final ZipArchiveImpl from(final List<ZipEntry> entries) {
        Checks.notNull(entries, "List<ZipEntry>", "entries");
        if (entries.contains(null)) {
            throw new IllegalArgumentException("List<ZipEntry> entries cannot contain null entries");
        }
        return new ZipArchiveImpl(entries);
    }
}
