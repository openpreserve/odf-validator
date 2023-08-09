package org.openpreservation.odf.document;

import org.openpreservation.odf.xml.DocumentType;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.Version;

public interface OdfDocument {
    /**
     * Gets the version of the ODF document
     *
     * @return the ODF document version
     */
    public Version getVersion();

    /**
     * Gets the parsed type of the ODF document.
     *
     * @return
     */
    public DocumentType getDocumentType();

    /**
     * Returns the ODF Xml Document instance with the details of the parsed
     * document.
     *
     * @return the ODF Xml Document instance
     */
    public OdfXmlDocument getXmlDocument();

    /**
     * Gets the Metadata object parsed from the main ODF XML document for single
     * file XML documents or from the meta.xml file for documents parsed from a
     * package.
     *
     * @return the ODF document metadata.
     */
    public Metadata getMetadata();

}
