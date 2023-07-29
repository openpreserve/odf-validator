package org.openpreservation.odf.pkg;

public interface FileEntry {
    public String getFullPath();

    public String getMediaType();

    public long getSize();

    public String getVersion();
}
