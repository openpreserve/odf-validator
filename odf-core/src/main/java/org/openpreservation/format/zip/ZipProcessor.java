package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;

public interface ZipProcessor {
    public ZipArchive process(final InputStream toProcess) throws IOException;

    public interface Factory {
        public ZipProcessor from(ZipEntryProcessor processor);
    }
}
