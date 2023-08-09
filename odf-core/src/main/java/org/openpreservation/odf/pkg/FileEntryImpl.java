package org.openpreservation.odf.pkg;

import java.util.Objects;

import org.openpreservation.utils.Checks;

final class FileEntryImpl implements FileEntry {
    static final FileEntryImpl of(final String fullPath) {
        return FileEntryImpl.of(fullPath, null);
    }
    static final FileEntryImpl of(final String fullPath, final String mediaType) {
        return FileEntryImpl.of(fullPath, mediaType, -1, null);
    }
    static final FileEntryImpl of(final String fullPath, final String mediaType, final long size,
            final String version) {
        Objects.requireNonNull(fullPath, String.format(Checks.NOT_NULL, "fullPath", "String"));
        if (fullPath.isBlank()) {
            throw new IllegalArgumentException(String.format(Checks.NOT_EMPTY, "fullPath"));
        }
        return new FileEntryImpl(fullPath, mediaType, size, version);
    }
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

    @Override
    public boolean isDocument() {
        return this.fullPath.endsWith("/");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fullPath == null) ? 0 : fullPath.hashCode());
        result = prime * result + ((mediaType == null) ? 0 : mediaType.hashCode());
        result = prime * result + (int) (size ^ (size >>> 32));
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileEntryImpl other = (FileEntryImpl) obj;
        if (fullPath == null) {
            if (other.fullPath != null)
                return false;
        } else if (!fullPath.equals(other.fullPath))
            return false;
        if (mediaType == null) {
            if (other.mediaType != null)
                return false;
        } else if (!mediaType.equals(other.mediaType))
            return false;
        if (size != other.size)
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FileEntryImpl [fullPath=" + fullPath + ", mediaType=" + mediaType + ", size=" + size + ", version="
                + version + "]";
    }
}
