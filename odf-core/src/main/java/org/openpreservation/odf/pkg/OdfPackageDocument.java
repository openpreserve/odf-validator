package org.openpreservation.odf.pkg;

import java.util.Map;

import org.openpreservation.odf.document.OdfDocument;
import org.openpreservation.odf.xml.OdfXmlDocument;

public interface OdfPackageDocument extends OdfDocument {
    /**
     * Returns the map of all XML documents in the package document.
     *
     * @return the map of all of the XML documents in the package document, the key
     *         is the file name.
     */
    public Map<String, OdfXmlDocument> getXmlDocumentMap();

    /**
     * Returns the XML document for the supplied path.
     *
     * @param path the path to the XML document.
     * @return the XML document for the supplied path.
     */
    public OdfXmlDocument getXmlDocument(String path);

    /**
     * Returns the manifest file entry for the package document.
     *
     * @return the documents manifest file entry.
     */
    public FileEntry getFileEntry();
}
