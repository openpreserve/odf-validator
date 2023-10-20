package org.openpreservation.format.zip;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.openpreservation.utils.Checks;

final class ZipArchiveImpl implements ZipArchive {
    static final ZipArchiveImpl from(final List<ZipEntry> entries) {
        Objects.requireNonNull(entries, String.format(Checks.NOT_NULL, "List<ZipEntry>", "entries"));
        if (entries.contains(null)) {
            throw new IllegalArgumentException("List<ZipEntry> entries cannot contain null entries");
        }
        return new ZipArchiveImpl(entries);
    }

    private final List<ZipEntry> entries;

    private ZipArchiveImpl(final List<ZipEntry> entries) {
        this.entries = Collections.unmodifiableList(entries);
    }

    @Override
    public ZipEntry getFirstEntry() {
        return this.entries.stream().findFirst().orElse(null);
    }

    @Override
    public List<ZipEntry> getZipEntries() {
        return this.entries;
    }

    @Override
    public ZipEntry getZipEntry(final String entryName) {
        Objects.requireNonNull(entryName, String.format(Checks.NOT_NULL, "entryName", "String"));
        return this.entries.stream().filter(e -> e.getName().equals(entryName)).findFirst().orElse(null);
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public String toString() {
        return "ZipArchiveImpl [entries=" + entries + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entries == null) ? 0 : entries.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ZipArchiveImpl other = (ZipArchiveImpl) obj;
        if (entries == null) {
            if (other.entries != null)
                return false;
        } else if (!entries.equals(other.entries))
            return false;
        return true;
    }
}
