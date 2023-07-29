package org.openpreservation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Checks {
    private static final String NOT_NULL = "%s parameter: %s must not be null.";
    private static final int BUFFER_SIZE = 4096;

    private Checks() {
        throw new AssertionError("Should not be instantiated.");
    }

    public static final void notNull(final Object toCheck, final String type, final String name) {
        if (toCheck == null) {
            throw new IllegalArgumentException(String.format(NOT_NULL, type, name));
        }
    }

    /**
     * see https://stackoverflow.com/a/140861
     * 
     * @param s
     * @return
     */
    public static final byte[] hexStringToByteArray(final String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static int copyStream(final InputStream source, final OutputStream dest) throws IOException {
        int len = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((len = source.read(buffer)) > 0) {
            dest.write(buffer, 0, len);
        }
        return len;
    }

}
