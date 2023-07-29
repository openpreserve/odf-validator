package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Interface for processing Zip entries.
 */
public interface ZipEntryProcessor {
    /**
     * Returns true if the processor accepts the entry.
     * @param entry The ZipEntry to evaluate.
     * @return true if the processor accepts the entry, otherwise false.
     */
    public boolean accepts(final ZipEntry entry);

    /**
     * Process the ZipEntry.
     * @param entry The ZipEntry to process.
     * @param is The InputStream for the ZipEntry.
     * @throws IOException when an IO error occurs reading the input stream.
     */
    public void process(final ZipEntry entry, final InputStream is) throws IOException;
}
