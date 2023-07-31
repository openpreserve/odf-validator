package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;

public interface ZipCachingProcessor {
    public ZipArchiveCache process(final InputStream toProcess) throws IOException;

    public interface Factory {
        public ZipCachingProcessor from(final ZipEntryProcessor processor);
    }
}
