package org.openpreservation.odf.fmt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openpreservation.utils.Checks;

public class FormatSniffer {
    private static final int MAX_BOM_LENGTH = 4;
    private static final int MAX_MIME_LENGTH = 90;
    private static final String TEST_VAR = "toSniff";

    public static Formats sniff(final String toSniff) throws IOException {
        Checks.notNull(toSniff, "String", TEST_VAR);
        return sniff(Paths.get(toSniff));
    }

    public static Formats sniff(final Path toSniff) throws IOException {
        Checks.notNull(toSniff, "Path", TEST_VAR);
        return sniff(toSniff.toFile());
    }

    public static Formats sniff(final File toSniff) throws IOException {
        Checks.notNull(toSniff, "File", TEST_VAR);
        if (!toSniff.exists()) {
            throw new FileNotFoundException("File " + toSniff.getAbsolutePath() + " does not exist.");
        }
        try (InputStream is = new FileInputStream(toSniff)) {
            return sniff(is);
        }
    }

    public static Formats sniff(final InputStream toSniff) throws IOException {
        Checks.notNull(toSniff, "InputStream", TEST_VAR);
        try (BufferedInputStream bis = new BufferedInputStream(toSniff)) {
            skipBom(bis);
            return Formats.identify(readAndReset(bis, MAX_MIME_LENGTH));
        }
    }

    public static Encodings sniffEncoding(final String toSniff) throws IOException {
        Checks.notNull(toSniff, "String", TEST_VAR);
        return sniffEncoding(Paths.get(toSniff));
    }

    public static Encodings sniffEncoding(final Path toSniff) throws IOException {
        Checks.notNull(toSniff, "Path", TEST_VAR);
        return sniffEncoding(toSniff.toFile());
    }

    public static Encodings sniffEncoding(final File toSniff) throws IOException {
        Checks.notNull(toSniff, "File", TEST_VAR);
        try (InputStream bis = new FileInputStream(toSniff)) {
            return sniffEncoding(bis);
        }
    }

    public static Encodings sniffEncoding(final InputStream toSniff) throws IOException {
        Checks.notNull(toSniff, "InputStream", TEST_VAR);
        try (BufferedInputStream bis = new BufferedInputStream(toSniff)) {
            return skipBom(bis);
        }
    }

    private static byte[] readAndReset(final BufferedInputStream toRead, final int numBytes) throws IOException {
        byte[] retBytes = new byte[0];
        toRead.mark(numBytes);
        byte[] bytes = new byte[numBytes];
        int read = toRead.read(bytes, 0, numBytes);
        toRead.reset();
        if (read > 0) {
            retBytes = new byte[read];
            System.arraycopy(bytes, 0, retBytes, 0, read);
        }
        return retBytes;
    }

    private static Encodings skipBom(final BufferedInputStream toSkip) throws IOException {
        byte[] bom = readAndReset(toSkip, MAX_BOM_LENGTH);
        Encodings enc = Encodings.fromRepresentation(bom);
        try {
            long skipped = toSkip.skip(enc.getLength());
            if (skipped != enc.getLength()) {
                throw new IOException(
                        String.format("BOM %s detected but failed to skip %d bytes.", enc, enc.getLength()));
            }
            return enc;
        } catch (IOException e) {
            throw new IOException("Could not skip BOM.", e);
        }
    }
}
