package org.openpreservation.format.zip;

import java.util.List;

public interface ZipArchive {
    /**
     * Get the list of ZipEntries in the archive
     *
     * @return an ordered list of the the ZipEntries in the archive
     */
    public List<ZipEntry> getZipEntries();

    /**
     * Retrieve a ZipEntry by name.
     *
     * @param entryName the name of the ZipEntry to retrieve
     *
     * @return the ZipEntry with the given name, or null if no match
     */
    public ZipEntry getZipEntry(final String entryName);

    /**
     * Get the number of ZipEntries in the archive
     *
     * @return number of ZipEntries in the archive
     */
    public int size();
}
