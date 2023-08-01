package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.zip.ZipArchive;
import org.openpreservation.odf.fmt.Formats;

public interface OdfPackage {
    /**
     * Get the name/identifier of the package, often the path to the file parsed.
     * 
     * @return the name of the package
     */
    public String getName();

    /**
     * Get the detected format of the package. This is the format detected by format
     * sniffing and may not be the actual ODF type depending on any MIMETYPE entry
     * in the package.
     * 
     * @return the detected format of the package
     */
    public Formats getFormat();

    /**
     * Is the item parsed a well formed ODF Package, i.e. is is a ZIP archive with
     * wellformed XML documents.
     * 
     * @return true if the package is well formed, otherwise false
     */
    public boolean isWellFormedZip();

    /**
     * Get the declared mimetype of the package. This is the mimetype declared in
     * the MIMETYPE entry and/or manifest root entry. This may differ from the
     * detected format.
     * 
     * @return the declared mimetype of the package
     */
    public String getMimeType();

    /**
     * Does the package have a MIMETYPE entry?
     * 
     * @return true if the package has a MIMETYPE entry, otherwise false
     */
    public boolean hasMimeEntry();

    /**
     * Does the package have a manifest?
     * 
     * @return true if the package has a manifest, otherwise false
     */
    public boolean hasManifest();

    /**
     * Does the package have a preview image thumbnail?
     * 
     * @return true if the package has a thumbnail preview, otherwise false
     */
    public boolean hasThumbnail();

    /**
     * Returns the details of the underlying ZIP archive for the package as a
     * ZipArchive instance.
     * 
     * @return the package ZIP archive
     */
    public ZipArchive getZipArchive();

    /**
     * Get the Manifest object for the package, this is parsed as long as the
     * Manifest file is a well formed XML document.
     * 
     * @return the package Manifest
     */
    public Manifest getManifest();

    /**
     * Gets the Metadata object parsed from the package root document if it is a
     * well formed XML document.
     * 
     * @return the root document metadata.
     */
    public Metadata getMetadata();

    /**
     * Get the InputStream to any of an identified ODF XML document in the package
     * 
     * @param path the internal path to the XML document
     * @return the InputStream to the XML document
     * @throws IOException when there's a problem reading the entry
     */
    public InputStream getEntryXmlStream(final String path) throws IOException;

    /**
     * Get the XML ParseResult of and identified ODF XML documents in the package
     * 
     * @param path the internal path to the XML document
     * @return the InputStream to the XML document
     * @throws IOException when there's a problem reading the entry
     */
    public ParseResult getEntryXmlParseResult(final String path);

    /**
     * Get the paths to all of the identified ODF XML documents in the package,
     * including content, styles, metadata and the manifest
     * 
     * @return a List of all of the String internal paths to the packge ODF XML
     *         documents
     */
    public List<String> getXmlEntryPaths();

    /**
     * Get the InputStream for any item from the document's Manifest
     * 
     * @param entry the Manifest FileEntry to get the InputStream for
     * @return the InputStream for the entry supplied
     * @throws IOException when there's a problem reading the entry
     */
    public InputStream getEntryStream(final FileEntry entry) throws IOException;
}
