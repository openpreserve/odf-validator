package org.openpreservation.format.xml;

import java.util.Arrays;
import java.util.Objects;

import org.openpreservation.utils.Checks;

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
     * The encoding name for the Byte Order Mark
     */
    public final String encoding;
    /**
     * A byte array representing the Byte Order Mark
     */
    final byte[] representation;

    private Encodings(final String encoding, final byte[] representation) {
        this.encoding = encoding;
        this.representation = representation;
    }

    /**
     * Returns the length of the Byte Order Mark in bytes
     *
     * @return the length of the Byte Order Mark in bytes
     */
    public int getLength() {
        return representation.length;
    }

    /**
     * Get a Byte Order Mark from the String name
     *
     * @param encoding the name of the encoding to retrieve
     *
     * @return the Endcoding with the given name, or Encoding.NONE if no match
     * @throws NullPointerException if encoding is null
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
     * Get a Byte Order Mark from the byte array representation
     *
     * @param bytes the byte array representation of the encoding to retrieve
     *
     * @return the Endcoding with the given representation, or Encoding.NONE if no match
     * @throws NullPointerException if bytes is null
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

}
