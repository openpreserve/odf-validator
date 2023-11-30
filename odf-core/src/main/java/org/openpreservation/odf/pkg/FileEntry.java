package org.openpreservation.odf.pkg;

public interface FileEntry {
    /**
     * Get the full internal path of the file entry.
     *
     * @return the entry internal path
     */
    public String getFullPath();

    /**
     * Get the declared media type of the file entry.
     *
     * @return the declared media type of the file entry, this may not be a legal
     *         MIME identifier
     */
    public String getMediaType();

    /**
     * The size of the file entry in bytes.
     *
     * @return the size of the file entry.
     */
    public long getSize();

    /**
     * Get's the version of the file entry if it's declared.
     *
     * @return the version of the file entry or null if not declared.
     */
    public String getVersion();

    /**
     * Is the file entry a document entry?
     *
     * @return true if the file entry is a document entry, otherwise false.
     */
    public boolean isDocument();

    /**
     * Is the file entry encrypted?
     *
     * @return true if the file entry is encrypted, otherwise false.
     */
    public boolean isEncrypted();
}
