package org.openpreservation.odf.fmt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.openpreservation.utils.Checks;

public final class FormatSniffer {
    private static final int MAX_BOM_LENGTH = 4;
    private static final int MAX_MIME_LENGTH = 90;
    private static final String TEST_VAR = "toSniff";

    public static Formats sniff(final String toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "String", TEST_VAR));
        return sniff(Paths.get(toSniff));
    }

    public static Formats sniff(final Path toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "Path", TEST_VAR));
        return sniff(toSniff.toFile());
    }

    public static Formats sniff(final File toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "File", TEST_VAR));
        if (!toSniff.exists()) {
            throw new FileNotFoundException("File " + toSniff.getAbsolutePath() + " does not exist.");
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(toSniff))) {
            return sniff(bis);
        }
    }

    public static Formats sniff(final BufferedInputStream toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "InputStream", TEST_VAR));
        final Encodings bom = skipBom(toSniff);
        final Formats format = Formats.identify(Utils.readAndReset(toSniff, MAX_MIME_LENGTH));
        if (bom == Encodings.NONE) {
            return format;
        }
        return format.isText() ? format : Formats.UNKNOWN;
    }

    public static Encodings sniffEncoding(final String toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "String", TEST_VAR));
        return sniffEncoding(Paths.get(toSniff));
    }

    public static Encodings sniffEncoding(final Path toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "Path", TEST_VAR));
        return sniffEncoding(toSniff.toFile());
    }

    public static Encodings sniffEncoding(final File toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "File", TEST_VAR));
        try (InputStream bis = new FileInputStream(toSniff)) {
            return sniffEncoding(bis);
        }
    }

    public static Encodings sniffEncoding(final InputStream toSniff) throws IOException {
        Objects.requireNonNull(toSniff, String.format(Checks.NOT_NULL, "InputStream", TEST_VAR));
        try (BufferedInputStream bis = new BufferedInputStream(toSniff)) {
            return skipBom(bis);
        }
    }

    private static Encodings skipBom(final BufferedInputStream toSkip) throws IOException {
        final byte[] bom = Utils.readAndReset(toSkip, MAX_BOM_LENGTH);
        final Encodings enc = Encodings.fromRepresentation(bom);
        try {
            final long skipped = toSkip.skip(enc.getLength());
            if (skipped != enc.getLength()) {
                throw new IOException(
                        String.format("BOM %s detected but failed to skip %d bytes.", enc, enc.getLength()));
            }
            return enc;
        } catch (final IOException e) {
            throw new IOException("Could not skip BOM.", e);
        }
    }

    private FormatSniffer() {
        throw new AssertionError("Utility class 'FormatSniffer' should not be instantiated");
    }
}
