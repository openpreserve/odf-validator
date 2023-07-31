package org.openpreservation.odf.xml;

import java.util.Objects;

import org.openpreservation.format.xml.Namespace;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.utils.Checks;

public class OdfDocumentImpl implements OdfDocument {
    private static final String STRING = "String";
    public static final OdfDocument of(final ParseResult parseResult) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, "parseResult", "ParseResult"));
        final String version = parseResult.getRootAttributeValue("office:version") != null
                ? parseResult.getRootAttributeValue("office:version")
                : "";
        final String mime = parseResult.getRootAttributeValue("office:mimetype") != null
                ? parseResult.getRootAttributeValue("office:mimetype")
                : "";

        return OdfDocumentImpl.of(parseResult.getRootNamespace(), parseResult.getRootName(),
                mime, version);
    }
    static final OdfDocument of(final Namespace rootNamespace, final String rootName, final String mimeType,
            final String version) {
        Objects.requireNonNull(rootNamespace, String.format(Checks.NOT_NULL, "rootNamespace", "Namespace"));
        Objects.requireNonNull(rootName, String.format(Checks.NOT_NULL, "rootElementName", STRING));
        Objects.requireNonNull(mimeType, String.format(Checks.NOT_NULL, "mimeType", STRING));
        Objects.requireNonNull(version, String.format(Checks.NOT_NULL, "version", STRING));
        return new OdfDocumentImpl(rootNamespace, rootName, mimeType, version);
    }
    private final Namespace rootNamespace;
    private final String rootName;

    private final String mimeType;

    private final String version;

    private OdfDocumentImpl(final Namespace rootNamespace, final String rootName, final String mimeType,
            final String version) {
        super();
        this.rootNamespace = rootNamespace;
        this.rootName = rootName;
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
        return this.rootNamespace;
    }

    @Override
    public String getRootName() {
        return this.rootName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rootNamespace == null) ? 0 : rootNamespace.hashCode());
        result = prime * result + ((rootName == null) ? 0 : rootName.hashCode());
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
        final OdfDocumentImpl other = (OdfDocumentImpl) obj;
        if (rootNamespace == null) {
            if (other.rootNamespace != null)
                return false;
        } else if (!rootNamespace.equals(other.rootNamespace))
            return false;
        if (rootName == null) {
            if (other.rootName != null)
                return false;
        } else if (!rootName.equals(other.rootName))
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
        return "OdfDocumentImpl [rootNamespace=" + rootNamespace + ", rootName=" + rootName + ", mimeType=" + mimeType
                + ", version=" + version + "]";
    }
}
