package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.fmt.FormatSniffer;
import org.openpreservation.odf.fmt.OdfFormats;
import org.xml.sax.SAXException;

public final class OdfPackages {
    /**
     * The ODF mimetype entry name.
     */
    public static final String MIMETYPE = OdfFormats.MIMETYPE;

    /**
     * Get a validating parser instance.
     *
     * @return a validating parser instance
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static final ValidatingParser getValidatingParser() throws ParserConfigurationException, SAXException {
        return ValidatingParserImpl.getInstance();
    }

    /**
     * get a package parser instance.
     *
     * @return a new package parser instance
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static final PackageParser getPackageParser() throws ParserConfigurationException, SAXException {
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
            e.printStackTrace();
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
        return FormatSniffer.sniff(toCheck).isPackage();
    }

    private OdfPackages() {
        throw new AssertionError("Utility class 'OdfPackages' should not be instantiated");
    }
}
