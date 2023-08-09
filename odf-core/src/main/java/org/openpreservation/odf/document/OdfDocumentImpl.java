package org.openpreservation.odf.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.odf.xml.DocumentType;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.Version;
import org.xml.sax.SAXException;

final class OdfDocumentImpl implements OdfDocument {
    private final OdfXmlDocument xmlDocument;
    private final Metadata metadata;

    private OdfDocumentImpl(final OdfXmlDocument xmlDocument, final Metadata metadata) {
        super();
        this.xmlDocument = xmlDocument;
        this.metadata = metadata;
    }

    static final OdfDocument of(final OdfXmlDocument xmlDocument, final Metadata metadata) {
        return new OdfDocumentImpl(xmlDocument, metadata);
    }

    static final OdfDocument of(final ParseResult parseResult, final Metadata metadata) {
        return new OdfDocumentImpl(OdfXmlDocuments.odfXmlDocumentOf(parseResult), metadata);
    }

    static final OdfDocument from(final InputStream docStream)
            throws IOException, ParserConfigurationException, SAXException {
        byte[] bytes = null;
        OdfXmlDocument xmlDocument = null;
        Metadata metadata = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            docStream.transferTo(bos);
            bytes = bos.toByteArray();
        }
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            xmlDocument = OdfXmlDocuments.xmlDocumentFrom(is);
        }
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            metadata = OdfXmlDocuments.metadataFrom(is);
        }
        return OdfDocumentImpl.of(xmlDocument, metadata);
    }

    @Override
    public Version getVersion() {
        return Version.fromVersion(this.getXmlDocument().getVersion());
    }

    @Override
    public DocumentType getDocumentType() {
        return DocumentType.getTypeByMimeString(this.getXmlDocument().getMimeType());
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

    @Override
    public OdfXmlDocument getXmlDocument() {
        return this.xmlDocument;
    }
}
