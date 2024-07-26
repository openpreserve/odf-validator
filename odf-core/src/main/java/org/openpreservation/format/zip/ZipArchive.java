package org.openpreservation.format.zip;

import java.util.List;

/**
 * An interface for accessing the contents of a Zip archive.
 */
public interface ZipArchive {
    /**
     * Returns the first physical {@link ZipEntry} in the archive.
     *
     * @return the first physical <code>ZipEntry</code> in the archive
     */
    public ZipEntry getFirstEntry();

    /**
     * Get a <code>List<ZipEntry></code> of all of the zip entries in the archive
     *
     * @return an ordered <code>List<ZipEntry></code> of all of the zip entries in
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
