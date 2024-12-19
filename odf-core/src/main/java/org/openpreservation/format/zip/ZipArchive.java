package org.openpreservation.format.zip;

import java.nio.file.Path;
import java.util.List;

/**
 * An interface for accessing the contents of a Zip archive.
 */
public interface ZipArchive {
    /**
     * Get the path to the archive
     *
     * @return the <code>Path</code> to the archive
     */
    public Path getPath();

    /**
     * Returns the first physical {@link ZipEntry} in the archive. Used to ensure that the MIMETYPE entry is the first in the archive.
     *
     * @return the first physical <code>ZipEntry</code> in the archive
     */
    public ZipEntry getFirstEntry();

    /**
     * Get a <code>List<ZipEntry></code> of all of the zip entries in the archive
     *
     * @return a <code>List<ZipEntry></code> of all of the zip entries in
     *         the archive
     */
    public List<ZipEntry> getZipEntries();

    /**
     * Retrieve a <code>ZipEntry</code> by entry name, equivalent to the path.
     *
     * @param entryName the name of the <code>ZipEntry</code> to retrieve
     *
     * @return the <code>ZipEntry</code> with the given <code>entryName</code>, or
     *         <code>null</code> if no match
     */
    public ZipEntry getZipEntry(final String entryName);

    /**
     * Get the count of entries in the archive
     *
     * @return <code>int</code> number of <code>ZipEntries<code> in the archive
     */
    public int size();
}
