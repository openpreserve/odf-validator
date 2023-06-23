package org.openpreservation.odf.fmt;

import java.util.Arrays;

public enum Encodings {
    NONE("NONE", new byte[] {}),
    UTF_8("UTF-8", new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }),
    UTF_16_BE("UTF-16(BE)", new byte[] { (byte) 0xFE, (byte) 0xFF }),
    UTF_16_LE("UTF-16(LE)", new byte[] { (byte) 0xFF, (byte) 0xFE }),
    UTF_32_BE("UTF-32(BE)", new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF }),
    UTF_32_LE("UTF-32(LE)", new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00 });

    public final String encoding;
    final byte[] representation;

    private Encodings(final String encoding, final byte[] representation) {
        this.encoding = encoding;
        this.representation = representation;
    }

    public int getLength() {
        return representation.length;
    }

    public static Encodings fromEncoding(final String encoding) {
        for (Encodings e : Encodings.values()) {
            if (e.encoding.equals(encoding)) {
                return e;
            }
        }
        return NONE;
    }

    public static Encodings fromRepresentation(final byte[] bytes) {
        Encodings result = NONE;
        for (Encodings e : Encodings.values()) {
            if (e.representation.length <= (bytes.length)
                    && (Arrays.equals(e.representation, Arrays.copyOfRange(bytes, 0, e.representation.length)))) {
                result = e;
            }
        }
        return result;
    }

}
