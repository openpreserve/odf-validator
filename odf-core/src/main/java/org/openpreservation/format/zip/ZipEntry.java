package org.openpreservation.format.zip;

public interface ZipEntry {
    public String getName();
    public long getSize();
    public long getCompressedSize();
    public long getCrc();
    public boolean isStored();
    public int getMethod();
    public boolean isDirectory();
    public byte[] getExtra();
}
