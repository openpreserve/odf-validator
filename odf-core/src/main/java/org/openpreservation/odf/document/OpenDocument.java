package org.openpreservation.odf.document;

import java.util.Collection;

import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;

public interface OpenDocument {
    /**
     * Indicates the type of OpenDocument, a zip archive package or a single XML
     * file
     *
     * @return an enumerated FileType valued indicating the type of OpenDocument
     */
    public FileType getFileType();

    /**
     * Is the OpenDocument an ODF package, i.e. a zip archive
     *
     * @return true if the OpenDocument is a package
     */
    public boolean isPackage();

    /**
     * Get the main ODF Document details, this will be the only document for a
     * single XML file, a package may have sub-documents
     *
     * @return the main ODF Document for the OpenDocument file
     */
    public OdfDocument getDocument();

    /**
     * For and ODF Package get the sub-documents, these will be the documents in the
     * package other than the main document. Single file OpenDocuments return an
     * empty collection.
     *
     * @return the Collection of OdfDocument sub-documents.
     */
    public Collection<OdfDocument> getSubDocuments();

    /**
     * Get the ODF Package for the OpenDocument, this will be null for a single XML
     * file.
     *
     * @return the ODF Package for the OpenDocument
     */
    public OdfPackage getPackage();

    /**
     * Get the format of the OpenDocument, this will be the declared format of the
     * package
     * or the parsed format of a single document.
     *
     * @return the format of the OpenDocument
     */
    public Formats getFormat();
}
