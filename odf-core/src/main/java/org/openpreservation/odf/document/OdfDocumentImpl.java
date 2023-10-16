package org.openpreservation.odf.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.odf.xml.DocumentType;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.Version;
import org.xml.sax.SAXException;

final class OdfDocumentImpl implements OdfDocument {
    static final OdfDocument of(final OdfXmlDocument xmlDocument, final Metadata metadata) {
        Objects.requireNonNull(xmlDocument, "OdfXmlDocument parameter xmlDocument cannot be null");
        Objects.requireNonNull(metadata, "Metadata parameter metadata cannot be null");
        return new OdfDocumentImpl(xmlDocument, metadata);
    }
    static final OdfDocument of(final ParseResult parseResult, final Metadata metadata) {
        Objects.requireNonNull(parseResult, "ParseResult parameter parseResult cannot be null");
        Objects.requireNonNull(metadata, "Metadata parameter metadata cannot be null");
        return new OdfDocumentImpl(OdfXmlDocuments.odfXmlDocumentOf(parseResult), metadata);
    }

    static final OdfDocument from(final InputStream docStream)
            throws IOException, ParserConfigurationException, SAXException {
        Objects.requireNonNull(docStream, "InputStream parameter docStream cannot be null");
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

    private final OdfXmlDocument xmlDocument;

    private final Metadata metadata;

    private OdfDocumentImpl(final OdfXmlDocument xmlDocument, final Metadata metadata) {
        super();
        this.xmlDocument = xmlDocument;
        this.metadata = metadata;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((xmlDocument == null) ? 0 : xmlDocument.hashCode());
        result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OdfDocumentImpl other = (OdfDocumentImpl) obj;
        if (xmlDocument == null) {
            if (other.xmlDocument != null)
                return false;
        } else if (!xmlDocument.equals(other.xmlDocument))
            return false;
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OdfDocumentImpl [xmlDocument=" + xmlDocument + ", metadata=" + metadata + "]";
    }

}
