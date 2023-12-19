package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for a processor that processes an {@link InputStream} and a factory
 * for the processor.
 */

public interface ZipProcessor {
    /**
     * Factory for creating a {@link ZipProcessor}.
     */
    public interface Factory {
        /**
         * Factory for creating a {@link ZipProcessor} from a {@link ZipEntryProcessor}.
         * 
         * @param processor the <code>ZipEntryProcessor</code> to use
         * @return a <code>ZipProcessor</code> created from the
         *         <code>ZipEntryProcessor</code>
         */
        public ZipProcessor from(final ZipEntryProcessor processor);
    }

    /**
     * Process the <code>InputStream</code> and return a {@link ZipArchive}
     * containing parsed from the stream.
     * 
     * @param toProcess the <code>InputStream</code> to process
     * @return a <code>ZipArchive</code> parsed from the stream
     * @throws IOException when an IO error occurs reading the
     *                     <code>InputStream</code>
     */
    public ZipArchive process(final InputStream toProcess) throws IOException;
}
