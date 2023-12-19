package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Interface for processing {@link ZipEntry} objects from a {@link ZipArchive}
 */
public interface ZipEntryProcessor {
    /**
     * Returns <code>true</code> if the processor accepts the supplied
     * <code>ZipEntry</code> for processing.
     * 
     * @param entry The <code>ZipEntry</code> to evaluate.
     * @return <code>true<code> if the processor accepts the entry, otherwise <code>false</code>.
     */
    public boolean accepts(final ZipEntry entry);

    /**
     * Process the <code>ZipEntry</code> and it's <code>InputStream</code>.
     * 
     * @param entry The <code>ZipEntry<code> to process.
     * @param is The <code>InputStream<code> for the <code>ZipEntry<code> <code>entry</code>.
     * @throws IOException when an IO error occurs reading the
     *                     <code>InputStream</code>.
     */
    public void process(final ZipEntry entry, final InputStream is) throws IOException;
}
