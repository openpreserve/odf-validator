package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.zip.ZipArchive;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.odf.fmt.Formats;

/**
 * An interface defining the behaviour of ODF packages.
 * 
 * Most of the methods in this interface concern themselves
 * with the properties of the package needed for validation.
 */
public interface OdfPackage {
    /**
     * Get the name or identifier of the package, often the path to the file parsed.
     *
     * @return the <code>String</code> name of the package
     */
    public String getName();

    /**
     * Get the detected {@link Formats} of the package.
     * 
     * This is the format detected by format sniffing and may not be the actual
     * ODF type depending on any <code>mimetype</code> zip entry in the package.
     *
     * @return the detected <code>Formats</code> of the package
     */
    public Formats getDetectedFormat();

    /**
     * Is the item parsed a well formed <code>ZipArchive</code>.
     *
     * @return <code>true</code> if the package is well formed, otherwise
     *         <code>false</code>
     */
    public boolean isWellFormedZip();

    /**
     * Get the declared <code>mimetype</code> of the package.
     * 
     * This is the mimetype declared in the <code>mimetype</code> {@link ZipEntry}
     * and/or the <code>manifest</code>'s root("/") {@link FileEntry}.
     * This may differ from the detected format.
     *
     * @return the declared mimetype of the package
     */
    public String getMimeType();

    /**
     * Does the package have a <code>mimetype</code> {@link ZipEntry}?
     *
     * @return <code>true</code> if the package has a <code>mimetype</code>
     * <code>ZipEntry</code>, otherwise <code>false</code>
     */
    public boolean hasMimeEntry();

    /**
     * Is the <code>mimetype</code> <code>ZipEntry</code> the first <code>ZipEntry</code> in the package?
     *
     * @return <code>true</code> if the <code>mimetype</code> <code>ZipEntry</code>
     *         is the first entry in the package's <code>ZipArchive</code>,
     *         otherwise <code>false</code>
     */
    public boolean isMimeFirst();

    /**
     * Does the package have a manifest, that is a <code>ZipEntry</code> with the
     * path <code>META-INF/manifest.xml</code>?
     *
     * @return <code>true</code> if the package has a manifest, otherwise
     *         <code>false</code>
     */
    public boolean hasManifest();

    /**
     * Does the package have a <code>ZipEntry</code> with the path
     * <code>Thumbnails/thumbnail.png</code>?
     *
     * @return <code>true</code> if the package has a thumbnail preview
     *         <code>ZipEntry</code>, otherwise <code>false</code>
     */
    public boolean hasThumbnail();

    /**
     * Returns the details of the underlying {@link ZipArchive} for the package.
     *
     * @return the package <code>ZipAchive</code>
     */
    public ZipArchive getZipArchive();

    /**
     * Get the {@link Manifest} object for the package, this is parsed as long as
     * the
     * <code>META-INF/manifest.xml</code> file exists and is a well formed XML
     * document.
     *
     * @return the package <code>Manifest</code>
     */
    public Manifest getManifest();

    /**
     * Get the <code>InputStream</code> to any of an identified ODF XML document in
     * the package
     *
     * @param path the internal <code>String</code> path to the XML document
     * @return the <code>InputStream</code> to the XML document
     * @throws IOException when there's a problem reading the entry with the
     *                     supplied <code>path</code>
     */
    public InputStream getEntryXmlStream(final String path) throws IOException;

    /**
     * Get the XML {@link ParseResult} of and identified ODF XML documents in the
     * package
     *
     * @param path the internal <code>String</code> package path to the XML document
     * @return the <code>ParseResult</code> returned from parsing the XML document
     *         with the supplied <code>path</code>.
     * @throws IOException when there's a problem reading the entry with the
     *                     supplied <code>path</code>.
     */
    public ParseResult getEntryXmlParseResult(final String path);

    /**
     * Get a <code>List<String></code> paths of the package's identified ODF XML
     * documents.
     * 
     * These will be all paths to files with the following names:
     * <ul>
     * <li>content.xml</li>
     * <li>styles.xml</li>
     * <li>meta.xml</li>
     * <li>settings.xml</li>
     * <li>manifest.rdf</li>
     * </ul>
     *
     * @return a <code>List<String></code> of all of the String internal paths to
     *         the packge ODF XML
     *         documents
     */
    public List<String> getXmlEntryPaths();

    /**
     * Get the <code>Set</code> of {@link FileEntry} to all of the identified ODF
     * XML documents in the package.
     * 
     * See {@link #getXmlEntryPaths()} for the list of XML documents.
     *
     * @return a <code>Set<FileEntry></code> of all of the identified ODF XML
     *         documents in the package.
     */
    public Set<FileEntry> getXmlEntries();

    /**
     * Get the <code>InputStream</code> for any item from the document's
     * <code>Manifest</code>
     *
     * @param entry the <code>Manifest</code> {@link FileEntry} to get the
     *              <code>InputStream</code> for
     * @return the <code>InputStream</code> for the FileEntry</code> supplied
     * @throws IOException when there's a problem reading the
     *                     <code>FileEntry</code>.
     */
    public InputStream getEntryStream(final FileEntry entry) throws IOException;

    /**
     * Get the {@link OdfPackageDocument} for the package, this is the root document
     * of the ODF
     * Package
     *
     * @return the package's root <code>OdfPackageDocument</code>
     */
    public OdfPackageDocument getDocument();

    /**
     * Get the <code>Map</code> of the package's {@link OdfPackageDocument} objects
     * by <code>String</code> path key.
     *
     * @return the <code>Map</code> of <code>OdfPackageDocument</code> Sub-Documents
     *         for the package.
     */
    public Map<String, OdfPackageDocument> getDocumentMap();

    /**
     * Get an <code>OdfPackageDocument</code> for a sub-document identified by the
     * passed <code>String</code> path.
     *
     * @param path the internal <code>String</code> path to the sub-document
     * @return the <code>OdfPackageDocument</code> for the sub-document
     */
    public OdfPackageDocument getSubDocument(final String path);

    /**
     * Retrieve the <code>Map</code> of <code>String</code> path keys and
     * {@link ParseResult} values for any
     * <code>ZipEntry</code> below the <code>META-INF</code> directory.
     *
     * @return the <code>Map</code> of META-INF <code>String</code> paths and XML
     *         <code>ParseResults</code>.
     */
    public Map<String, ParseResult> getMetaInfMap();

    /**
     * Does the META-INF directory or sub-directories contain a
     * <code>ZipeEntry</code> with the term "signatures" in its name.
     *
     * @return <code>true</code> if the package has Digital Signature entries,
     *         otherwise <code>false</code>.
     */
    public boolean hasDsigEntries();
}
