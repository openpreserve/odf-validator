package org.openpreservation.odf.pkg;

import java.util.Set;

public interface Manifest {
    public String getVersion();

    public int getEntryCount();

    public Set<FileEntry> getEntries();

    public Set<FileEntry> getEntriesByMediaType(final String mediaType);

    public FileEntry getEntry(final String entryName);

    public String getEntryMediaType(final String entryName);
}
