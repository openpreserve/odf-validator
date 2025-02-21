package org.openpreservation.odf.pkg;

import javax.xml.parsers.ParserConfigurationException;

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
