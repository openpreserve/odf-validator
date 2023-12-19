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

/**
 * A static utility class for general ODF package parsing.
 * 
 * The class doubles as a lightweight factory as well as
 * providing useful constants and utility methods.
 */
public final class OdfPackages {
    /**
     * The ODF specified name of the mimetype zip entry.
     */
    public static final String MIMETYPE = OdfFormats.MIMETYPE;
    /**
     * The ODF specified name of the package META-INF directory.
     */
    public static final String NAME_META_INF = Constants.NAME_META_INF;
    /**
     * The ODF specified path to the package manifest.
     */
    public static final String PATH_MANIFEST = Constants.PATH_MANIFEST;
    /**
     * The ODF specified path to a PNG thumbnail file.
     */
    public static final String PATH_THUMBNAIL = Constants.PATH_THUMBNAIL;

    /**
     * A factory method for {@link PackageParser} instances.
     *
     * @return a new <code>PackageParser</code> instance
     * @throws ParserConfigurationException if there's an issue configuring the underlying XML parser
     * @throws SAXException if there's an issue creating the underlying XML parser
     */
    public static final PackageParser getPackageParser() {
        return PackageParserImpl.getInstance();
    }

    /**
     * Uses the {@link FormatSniffer} to see if the supplied path resolves to a ZIP instance.
     * 
     * This check only tests the file header bytes and doesn't parse
     * the resolved path to ensure the zip is valid. See {@link #isValidZip(Path)} for that.
     *
     * @param toCheck a <code>Path<code> to sniff as a zip file
     * @return <code>true</code> if the supplied path has a zip signature, <code>false</code> otherwise.
     * @throws IOException if there's an issue reading the resolved <code>Path</code>
     */
    public static final boolean isZip(final Path toCheck) throws IOException {
        return FormatSniffer.sniff(toCheck, Formats.ZIP.getMaxSignatureLength()) == Formats.ZIP;
    }

    /**
     * Check if the supplied <code>Path</code> resolves to a valid zip archive
     * instance.
     * 
     * This is a full library parse of the resolved <code>Path<code>.
     *
     * @param toCheck a <code>Path</code> to resolve and test
     * @return <code>true</code> if the supplied path resolves to a valid zip file, else <code>false</code>.
     * @throws IOException if there's an issue reading the resolved <code>Path<code>
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
     * Check if the given <code>String</code> is an ODF spec compliant Digital Signature path.
     *
     * @param path the <code>String</code> path to check
     * @return <code>true</code> if <code>path</code> is a valid dsig path, else <code>false</code>.
     */
    public static final boolean isDsig(final String path) {
        return PackageParserImpl.isDsig(path);
    }

    /**
     * Check if the passed <code>String</code> path is an ODF specified XML file name.
     * 
     * This is the list of ODF specified XML files:
     * <ul>
     * <li>content.xml</li>
     * <li>styles.xml</li>
     * <li>meta.xml</li>
     * <li>settings.xml</li>
     * <li>manifest.rdf</li>
     * </ul>    
     * 
     * @param entrypath a <code>String</code> file path to check
     * @return <code>true</code> if <code>entryPath</code> is an ODF specified XML file name, else <code>false</code>.
     */
    public static final boolean isOdfXml(final String entrypath) {
        return PackageParserImpl.isOdfXml(entrypath);
    }

    private OdfPackages() {
        throw new AssertionError("Utility class 'OdfPackages' should not be instantiated");
    }
}
