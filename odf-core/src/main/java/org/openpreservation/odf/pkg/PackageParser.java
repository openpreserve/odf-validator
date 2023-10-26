package org.openpreservation.odf.pkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface PackageParser {
    /**
     * Parse a system path and return an OdfPackage instance. The returned package
     * may not be valid but is parsed leniently.
     *
     * @param is a system path to parse, this must not be null
     * @return the parsed ODF Package
     * @throws IOException when there is an issue reading entries from the package zip archive
     * @throws NullPointerException when toParse is null
     */
    public OdfPackage parsePackage(final Path is) throws IOException;

    /**
     * Parse a Java File instance and return an OdfPackage instance. The returned package
     * may not be valid but is parsed leniently.
     *
     * @param toParse a system path to parse, this must not be null
     * @return the parsed ODF Package
     * @throws IOException when there is an issue reading entries from the package zip archive
     * @throws NullPointerException when toParse is null
     */
    public OdfPackage parsePackage(final File toParse) throws IOException;

    /**
     * Parse an InputStream and return an OdfPackage instance. The returned package
     * may not be valid but is parsed leniently.
     *
     * @param toParse a system path to parse, this must not be null
     * @param name an identifier of some kind for the package, this must not be null
     * @return the parsed ODF Package
     * @throws IOException when there is an issue reading entries from the package zip archive
     * @throws NullPointerException when toParse or name is null
     */
    public OdfPackage parsePackage(final InputStream toParse, final String name)
            throws IOException;
}
