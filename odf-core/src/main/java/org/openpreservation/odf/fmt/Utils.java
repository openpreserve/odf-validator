package org.openpreservation.odf.fmt;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Utils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * see https://stackoverflow.com/a/140861
     * 
     * @param s
     * @return
     */
    static final byte[] hexStringToByteArray(final String s) {
        final int len = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    static byte[] readAndReset(final BufferedInputStream toRead, final int numBytes) throws IOException {
        byte[] retBytes = new byte[0];
        toRead.mark(numBytes);
        final byte[] bytes = new byte[numBytes];
        final int read = toRead.read(bytes, 0, numBytes);
        toRead.reset();
        if (read > 0) {
            retBytes = new byte[read];
            System.arraycopy(bytes, 0, retBytes, 0, read);
        }
        return retBytes;
    }

    /**
     * see https://mkyong.com/java/how-to-convert-hex-to-ascii-in-java/
     * @param str
     * @return
     */
    static String stringToHex(final String str) {
        final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        // We need two bytes per String character
        final char[] hex = new char[bytes.length * 2];
        for (int iLoop = 0; iLoop < bytes.length; iLoop++) {
            // 1 byte = 8 bits,
            // upper 4 bits is the first half of hex
            // lower 4 bits is the second half of hex
            // combine both and we will get the hex value, 0A, 0B, 0C
            final int v = bytes[iLoop] & 0xFF;                // byte widened to int, need mask 0xff
                                                        // prevent sign extension for negative number
            hex[iLoop * 2] = HEX_ARRAY[v >>> 4];        // get upper 4 bits
            hex[iLoop * 2 + 1] = HEX_ARRAY[v & 0x0F];   // get lower 4 bits
        }
        return new String(hex);
    }

    static byte[] stringToHexBytes(final String str) {
        return hexStringToByteArray(stringToHex(str));
    }

    private Utils() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
