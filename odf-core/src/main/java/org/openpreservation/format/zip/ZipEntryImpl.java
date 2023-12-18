package org.openpreservation.format.zip;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.openpreservation.utils.Checks;

public final class ZipEntryImpl implements ZipEntry {
    static final ZipEntryImpl of(final String name, final long size, final long compressedSize, final long crc,
            final int method, final boolean isDirectory, final byte[] extra) {
        Objects.requireNonNull(name, String.format(Checks.NOT_NULL, name, "String"));
        return new ZipEntryImpl(name, size, compressedSize, crc, method, isDirectory, extra);
    }

    static final ZipEntryImpl of(final ZipArchiveEntry entry) {
        Objects.requireNonNull(entry, String.format(Checks.NOT_NULL, entry, "ZipArchiveEntry"));
        return ZipEntryImpl.of(entry.getName(), entry.getSize(), entry.getCompressedSize(), entry.getCrc(),
                entry.getMethod(), entry.isDirectory(), entry.getExtra());
    }

    static final ZipEntryImpl of(final java.util.zip.ZipEntry entry) {
        Objects.requireNonNull(entry, String.format(Checks.NOT_NULL, entry, "ZipArchiveEntry"));
        return ZipEntryImpl.of(entry.getName(), entry.getSize(), entry.getCompressedSize(), entry.getCrc(),
                entry.getMethod(), entry.isDirectory(), entry.getExtra());
    }

    static final ZipEntryImpl of(final String name) {
        Objects.requireNonNull(name, String.format(Checks.NOT_NULL, name, "String"));
        return new ZipEntryImpl(name, 0, 0, 0, 0, false, null);
    }

    private final String name;
    private final long size;
    private final long compressedSize;
    private final long crc;
    private final int method;
    private final byte[] extra;

    private final boolean isDirectory;

    private ZipEntryImpl(final String name, final long size, final long compressedSize, final long crc,
            final int method, final boolean isDirectory, final byte[] extra) {
        super();
        this.name = name;
        this.size = size;
        this.compressedSize = compressedSize;
        this.crc = crc;
        this.method = method;
        this.isDirectory = isDirectory;
        this.extra = extra;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public long getCompressedSize() {
        return this.compressedSize;
    }

    @Override
    public long getCrc() {
        return this.crc;
    }

    @Override
    public boolean isStored() {
        return this.getMethod() == java.util.zip.ZipEntry.STORED;
    }

    @Override
    public int getMethod() {
        return this.method;
    }

    @Override
    public boolean isDirectory() {
        return this.isDirectory;
    }

    public byte[] getExtra() {
        return this.extra;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (size ^ (size >>> 32));
        result = prime * result + (int) (compressedSize ^ (compressedSize >>> 32));
        result = prime * result + (int) (crc ^ (crc >>> 32));
        result = prime * result + method;
        result = prime * result + Arrays.hashCode(extra);
        result = prime * result + (isDirectory ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ZipEntryImpl other = (ZipEntryImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size != other.size)
            return false;
        if (compressedSize != other.compressedSize)
            return false;
        if (crc != other.crc)
            return false;
        if (method != other.method)
            return false;
        if (!Arrays.equals(extra, other.extra))
            return false;
        if (isDirectory != other.isDirectory)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ZipEntryImpl [name=" + name + ", size=" + size + ", compressedSize=" + compressedSize + ", crc=" + crc
                + ", method=" + method + ", isDirectory=" + isDirectory + "]";
    }
}
