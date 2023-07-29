package org.openpreservation.format.zip;

import java.util.List;
import java.util.zip.ZipEntry;

public interface ZipArchive {
    public List<ZipEntry> getZipEntries();

    public ZipEntry getZipEntry(final String entryName);

    public int size();
}
