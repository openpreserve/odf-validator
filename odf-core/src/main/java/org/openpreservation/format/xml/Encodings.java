package org.openpreservation.format.xml;

import java.util.Arrays;
import java.util.Objects;

import org.openpreservation.utils.Checks;

/**
 * An enum defining the Byte Order Marks for UTF-8, UTF-16 and UTF-32.
 * 
 * See <a href="https://en.wikipedia.org/wiki/Byte_order_mark">Byte Order Mark</a>
 */
public enum Encodings {
    /**
     * Empty Byte String to match no encoding
     */
    NONE("NONE", new byte[] {}),
    /**
     * UTF-8 Byte Order Mark
     */
    UTF_8("UTF-8", new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }),
    /**
     * UTF-16 Big Endian Byte Order Mark
     */
    UTF_16_BE("UTF-16(BE)", new byte[] { (byte) 0xFE, (byte) 0xFF }),
    /**
     * UTF-16 Little Endian Byte Order Mark
     */
    UTF_16_LE("UTF-16(LE)", new byte[] { (byte) 0xFF, (byte) 0xFE }),
    /**
     * UTF-32 Big Endian Byte Order Mark
     */
    UTF_32_BE("UTF-32(BE)", new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF }),
    /**
     * UTF-32 Little Endian Byte Order Mark
     */
    UTF_32_LE("UTF-32(LE)", new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00 });

    /**
     * Get an <code>Encodings</code> instance from the String name or {@link NONE} if no match
     *
     * @param encoding the name of the encoding to retrieve
     *
     * @return the Endcoding with the given name, or <code>Encoding.NONE</code> if no match
     * @throws NullPointerException if <code>encoding</code> is <code>null</code>
     */
    public static Encodings fromEncoding(final String encoding) {
        Objects.requireNonNull(encoding, String.format(Checks.NOT_NULL, "encoding", "String"));
        final String match = encoding.toUpperCase();
        for (final Encodings e : Encodings.values()) {
            if (e.encoding.equals(match)) {
                return e;
            }
        }
        return NONE;
    }

    /**
     * Get an <code>Encodings</code> instance from the byte array representation, or {@link NONE} if no match.
     *
     * @param bytes the <code>byte[]</code> representation of the encoding to retrieve
     *
     * @return the <code>Endcodings</code> instance with the given representation, or <code>Encoding.NONE</code> if no match
     * @throws NullPointerException if <code>bytes</code> is <code>null</code>.
     */
    public static Encodings fromRepresentation(final byte[] bytes) {
        Objects.requireNonNull(bytes, String.format(Checks.NOT_NULL, "bytes", "byte[]"));
        Encodings result = NONE;
        for (final Encodings e : Encodings.values()) {
            if (e.representation.length <= (bytes.length)
                    && (Arrays.equals(e.representation, Arrays.copyOfRange(bytes, 0, e.representation.length)))) {
                result = e;
            }
        }
        return result;
    }

    /**
     * The <code>String</code> encoding name for the Byte Order Mark
     */
    public final String encoding;

    /**
     * A <code>byte[]</code> representing the Byte Order Mark
     */
    final byte[] representation;

    private Encodings(final String encoding, final byte[] representation) {
        this.encoding = encoding;
        this.representation = representation;
    }

    /**
     * Returns the length of the Byte Order Mark in bytes
     *
     * @return the <code>int</code> length of the Byte Order Mark in bytes
     */
    public int getLength() {
        return representation.length;
    }

}
