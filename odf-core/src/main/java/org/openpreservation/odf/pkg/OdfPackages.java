package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.fmt.FormatSniffer;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.OdfFormats;
import org.xml.sax.SAXException;

public final class OdfPackages {
    /**
     * The ODF mimetype entry name.
     */
    public static final String MIMETYPE = OdfFormats.MIMETYPE;
    public static final String PATH_MANIFEST = Constants.PATH_MANIFEST;
    public static final String NAME_META_INF = Constants.NAME_META_INF;
    public static final String PATH_THUMBNAIL = Constants.PATH_THUMBNAIL;

    /**
     * get a package parser instance.
     *
     * @return a new package parser instance
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static final PackageParser getPackageParser() {
        return PackageParserImpl.getInstance();
    }

    /**
     * Check if a file is a valid zip file
     *
     * @param toCheck a path to test
     * @return true if the supplied path resolves to a valid zip file, else false.
     * @throws IOException
     */
    public static final boolean isValidZip(final Path toCheck) throws IOException {
        try (ZipFile zipFile = new ZipFile(toCheck.toFile())) {
            return true;
        } catch (final ZipException e) {
            /**
             * No need to report this as an error as it simply means that the file is not a
             * ZIP
             */
        }
        return false;
    }

    /**
     * Uses the format sniffer to see if the supplied path is a ZIP instance. This
     * only uses the file header bytes and doesn't parse the path to ensure the zip
     * is valid.
     *
     * @param toCheck a path to sniff as a zip file
     * @return true if the supplied path has a zip signature, false otherwise.
     * @throws IOException if there's an issue reading the file
     */
    public static final boolean isZip(final Path toCheck) throws IOException {
        return FormatSniffer.sniff(toCheck, Formats.ZIP.getMaxSignatureLength()) == Formats.ZIP;
    }

    /**
     * Check if the given path is an ODF spec complian dsig path.
     *
     * @param path the path to check
     * @return true if the path is a valid dsig path, else false
     */
    public static final boolean isDsig(final String path) {
        return PackageParserImpl.isDsig(path);
    }

    public static final boolean isOdfXml(final String entrypath) {
        return PackageParserImpl.isOdfXml(entrypath);
    }

    private OdfPackages() {
        throw new AssertionError("Utility class 'OdfPackages' should not be instantiated");
    }
}
