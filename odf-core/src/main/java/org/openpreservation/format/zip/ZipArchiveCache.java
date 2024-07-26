package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * An extension of {@link ZipArchive} that caches the contents of the archive
 * and provides access to the <code>InputStream</code>s.
 */
public interface ZipArchiveCache extends ZipArchive {
    /**
     * Get a <code>List<String></code> of all of the cached entries in the archive
     *
     * @return
     */
    public List<String> getCachedEntryNames();

    /**
     * Get the <code>InputStream</code> for the entry with the passed name,
     * equivalent to the path.
     *
     * @return the <code>InputStream</code> for the entry with the passed
     *         <code>name</code>, or <code>null</code> if no match
     */
    public InputStream getEntryInputStream(final String entryName) throws IOException;
}
