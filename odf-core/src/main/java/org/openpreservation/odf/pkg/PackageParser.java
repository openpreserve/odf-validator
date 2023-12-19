package org.openpreservation.odf.pkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

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
    public OdfPackage parsePackage(final Path is) throws IOException;

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
    public OdfPackage parsePackage(final File toParse) throws IOException;

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
            throws IOException;
}
