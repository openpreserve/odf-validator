package org.openpreservation.format.zip;

/**
 * An interface for recording and accessing the properties of an entry from a
 * {@link ZipArchive}.
 */
public interface ZipEntry {
    /**
     * Get the name of the entry
     * @return the <code>String</code> name of the entry
     */
    public String getName();

    /**
     * Get the size of the entry in bytes
     * @return the <code>long</code> size of the entry in bytes
     */
    public long getSize();

    /**
     * Get the compressed size of the entry in bytes
     * @return the <code>long</code> compressed size of the entry in bytes
     */
    public long getCompressedSize();

    /**
     * Get the CRC of the entry
     * @return the <code>long</code> CRC of the entry
     */
    public long getCrc();

    /**
     * Is the entry <code>STORED</code>, e.g. not compressed?
     *
     * @return <code>true</code> if the entry is uncompressed, <code>false</code> otherwise
     */
    public boolean isStored();

    /**
     * Get the integer code for the compression method used for the entry.
     * @return the <code>int</code> compression method code
     */
    public int getMethod();

    /**
     * Is the entry a directory?
     *
     * @return <code>true</code> if the entry is a directory, <code>false</code> otherwise
     */
    public boolean isDirectory();

    /**
     * Get the extra field data for the entry
     * 
     * @return the <code>byte[]</code> extra field data for the entry.
     */
    public byte[] getExtra();
}
