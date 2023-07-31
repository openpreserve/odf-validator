package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ZipArchiveCache extends ZipArchive {
    public List<String> getCachedEntryNames();

    public InputStream getEntryInputStream(final String entryName) throws IOException;
}
