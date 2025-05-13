package org.openpreservation.odf.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.Namespace;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class OdfXmlDocumentImpl implements OdfXmlDocument {
    static final String[] extendedDocTypes = { "office:document-content", "office:document-styles", "office:styles",
            "office:document-settings" };

    static final OdfXmlDocumentImpl of(final ParseResult parseResult) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, "parseResult", "ParseResult"));
        final Version version = parseResult.getRootAttributeValue("office:version") != null
                ? Version.fromVersion(parseResult.getRootAttributeValue("office:version"))
                : Version.UNKNOWN;
        final Formats format = parseResult.getRootAttributeValue(Constants.ELENAME_MIME) != null
                ? Formats.fromMime(parseResult.getRootAttributeValue(Constants.ELENAME_MIME))
                : Formats.UNKNOWN;

        return new OdfXmlDocumentImpl(parseResult, format, version);
    }

    static final OdfXmlDocumentImpl from(final InputStream docStream)
            throws ParserConfigurationException, SAXException, IOException {
        Objects.requireNonNull(docStream, String.format(Checks.NOT_NULL, "docStream", "InputStream"));
        final XmlParser checker = new XmlParser();
        ParseResult result = checker.parse(docStream);
        return OdfXmlDocumentImpl.of(result);
    }

    private final ParseResult parseResult;
    private final Formats format;
    private final Version version;
    private final Set<Namespace> foreignNamespaces;

    private OdfXmlDocumentImpl(final ParseResult parseResult, final Formats format,
            final Version version) {
        super();
        this.parseResult = parseResult;
        this.format = format;
        this.version = version;
        this.foreignNamespaces = Collections.unmodifiableSet(getForeignNamespaces(parseResult));
    }

    @Override
    public boolean hasVersion() {
        return this.version != null && this.version != Version.UNKNOWN;
    }

    @Override
    public Version getVersion() {
        return this.version;
    }

    @Override
    public boolean hasFormat() {
        return this.format != null && this.format != Formats.UNKNOWN;
    }

    @Override
    public Formats getFormat() {
        return this.format;
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
        return getQualifedName(this.getRootNamespace().getPrefix(), this.getLocalRootName());
    }

    @Override
    public Set<Namespace> getForeignNamespaces() {
        return this.foreignNamespaces;
    }

    @Override
    public boolean isExtended() {
        return !this.foreignNamespaces.isEmpty();
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
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((foreignNamespaces == null) ? 0 : foreignNamespaces.hashCode());
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
        if (format == null) {
            if (other.format != null)
                return false;
        } else if (!format.equals(other.format))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        if (foreignNamespaces == null) {
            if (other.foreignNamespaces != null)
                return false;
        } else if (!foreignNamespaces.equals(other.foreignNamespaces))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OdfDocumentImpl [parseResult=" + this.parseResult + ", rootName=" + this.getRootName() + ", format="
                + format.mime
                + ", version=" + version + "]";
    }

    static final Set<Namespace> getForeignNamespaces(final ParseResult parseResult) {
        Set<Namespace> foreignNamespaces = new HashSet<>();
        if (isExtendedRoot(getQualifedName(parseResult.getRootPrefix(), parseResult.getRootName()))) {
            for (final Namespace usedNamespace : parseResult.getUsedNamespaces()) {
                if (OdfNamespaces.fromId(usedNamespace.getId()) == null) {
                    foreignNamespaces.add(usedNamespace);
                }
            }
        }
        return foreignNamespaces;
    }

    static boolean isExtendedRoot(final String rootName) {
        return Arrays.stream(extendedDocTypes).anyMatch(rootName::equals);
    }

    private static final String getQualifedName(final String prefix, final String name) {
        return String.format("%s:%s", prefix, name);
    }
}
