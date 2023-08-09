package org.openpreservation.odf.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.Namespace;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class OdfXmlDocumentImpl implements OdfXmlDocument {
    static final OdfXmlDocumentImpl of(final ParseResult parseResult) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, "parseResult", "ParseResult"));
        final String version = parseResult.getRootAttributeValue("office:version") != null
                ? parseResult.getRootAttributeValue("office:version")
                : "";
        final String mime = parseResult.getRootAttributeValue("office:mimetype") != null
                ? parseResult.getRootAttributeValue("office:mimetype")
                : "";

        return new OdfXmlDocumentImpl(parseResult, mime, version);
    }

    static final OdfXmlDocumentImpl from(final InputStream docStream)
            throws ParserConfigurationException, SAXException, IOException {
        Objects.requireNonNull(docStream, String.format(Checks.NOT_NULL, "docStream", "InputStream"));
        final XmlParser checker = new XmlParser();
        ParseResult result = checker.parse(docStream);
        return OdfXmlDocumentImpl.of(result);
    }

    private final ParseResult parseResult;
    private final String mimeType;
    private final String version;

    private OdfXmlDocumentImpl(final ParseResult parseResult, final String mimeType,
            final String version) {
        super();
        this.parseResult = parseResult;
        this.mimeType = mimeType;
        this.version = version;
    }

    @Override
    public boolean hasVersion() {
        return this.version != null && !this.version.isEmpty();
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public boolean hasMimeType() {
        return this.mimeType != null && !this.mimeType.isEmpty();
    }

    @Override
    public String getMimeType() {
        return this.mimeType;
    }

    @Override
    public Namespace getRootNamespace() {
        return this.parseResult.getRootNamespace();
    }

    @Override
    public String getLocalRootName() {
        return this.parseResult.getRootName();
    }

    @Override
    public String getRootName() {
        return String.format("%s:%s", this.getRootNamespace().getPrefix(), this.getLocalRootName());
    }

    @Override
    public ParseResult getParseResult() {
        return this.parseResult;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parseResult == null) ? 0 : parseResult.hashCode());
        result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        final OdfXmlDocumentImpl other = (OdfXmlDocumentImpl) obj;
        if (parseResult == null) {
            if (other.parseResult != null)
                return false;
        } else if (!parseResult.equals(other.parseResult))
            return false;
        if (mimeType == null) {
            if (other.mimeType != null)
                return false;
        } else if (!mimeType.equals(other.mimeType))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OdfDocumentImpl [parseResult=" + this.parseResult + ", rootName=" + this.getRootName() + ", mimeType="
                + mimeType
                + ", version=" + version + "]";
    }
}
