package org.openpreservation.odf.pkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

import org.openpreservation.format.zip.ZipArchive;

/**
 * An interface defining the behaviour of ODF package parsers.
 */
public interface PackageParser {
    /**
     * Resove and parse a system <code>Path</code> and return an {@link OdfPackage}
     * instance.
     * The returned package may not be valid but is parsed leniently.
     *
     * @param is a system path to parse, this must not be null
     * @return the parsed <code>ODFPackage</code>
     * @throws IOException          when there is an issue reading entries from the
     *                              package {@link ZipArchive}.
     * @throws NullPointerException when <code>toParse</code> is null
     */
    public OdfPackage parsePackage(final Path is) throws ParseException, FileNotFoundException;

    /**
     * Parse a Java File instance and return an {@link OdfPackage} instance.
     * The returned package may not be valid but is parsed leniently.
     *
     * @param toParse a system <code>Path</code> to parse, this must not be
     *                <code>null</code>
     * @return the parsed <code>ODFPackage</code>
     * @throws IOException          when there is an issue reading entries from the
     *                              package {@link ZipArchive}
     * @throws NullPointerException when <code>toParse</code> is null
     */
    public OdfPackage parsePackage(final File toParse) throws ParseException, FileNotFoundException;

    /**
     * Parse an <code>InputStream</code> and return an {@link OdfPackage} instance.
     * The returned package may not be valid but is parsed leniently.
     *
     * @param toParse an <code>InputStream</code> to parse, this must not be
     *                <code>null</code>
     * @param name    an identifier of some kind for the package, this must not be
     *                <code>null</code>
     * @return the parsed <code>ODFPackage</code>
     * @throws IOException          when there is an issue reading entries from the
     *                              package {@link ZipArchive}
     * @throws NullPointerException when <code>toParse</code> or <code>name</code>
     *                              is <code>null</code>
     */
    public OdfPackage parsePackage(final InputStream toParse, final String name)
            throws ParseException;

    public static class ParseException extends Exception {
        private static final long serialVersionUID = 1L;

        public ParseException(final String message) {
            super(message);
        }

        public ParseException(final Map<String, String> badEntries) {
            super(messageFromEntryMap(badEntries));
        }

        public ParseException(final String message, final Throwable cause) {
            super(message, cause);
        }

        private static final String messageFromEntryMap(final Map<String, String> badEntries) {
            StringBuilder sb = new StringBuilder();
            sb.append("The following zip entries could not be read: ");
            badEntries.forEach((k, v) -> {
                sb.append(k);
                sb.append(": ");
                sb.append(v);
                sb.append("\n");
            });
            return sb.toString();
        }
    }
}
