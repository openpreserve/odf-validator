package org.openpreservation.odf.pkg;

public class FileEntryImpl implements FileEntry {
    private final String fullPath;
    private final String mediaType;
    private final long size;
    private final String version;

    private FileEntryImpl(final String fullPath, final String mediaType, final long size, final String version) {
        this.fullPath = fullPath;
        this.mediaType = mediaType;
        this.size = size;
        this.version = version;
    }

    @Override
    public String getFullPath() {
        return this.fullPath;
    }

    @Override
    public String getMediaType() {
        return this.mediaType;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public String getVersion() {
        return this.version;
    }
    
    static final FileEntryImpl from(final String fullPath, final String mediaType, final long size, final String version) {
        return new FileEntryImpl(fullPath, mediaType, size, version);
    }
}
