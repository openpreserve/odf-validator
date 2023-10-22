package org.openpreservation.odf.pkg;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.openpreservation.odf.xml.DocumentType;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.Version;

final class OdfPackageDocumentImpl implements OdfPackageDocument {
    static final class Builder {
        public static Builder builder() {
            return new Builder();
        }

        public static Builder of(final FileEntry fileEntry) {
            Objects.requireNonNull(fileEntry, "fileEntry cannot be null");
            return builder().fileEntry(fileEntry);
        }

        private FileEntry entry;
        private Map<String, OdfXmlDocument> xmlDocumentMap = new HashMap<>();
        private Metadata metadata;

        private Builder() {
        }

        public Builder fileEntry(final FileEntry fileEntry) {
            Objects.requireNonNull(fileEntry, "FileEntry cannot be null");
            this.entry = fileEntry;
            return this;
        }

        public Builder metadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder xmlDocumentMap(Map<String, OdfXmlDocument> xmlDocumentMap) {
            this.xmlDocumentMap = xmlDocumentMap;
            return this;
        }

        public Builder xmlDocument(final String name, final OdfXmlDocument document) {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(document, "document cannot be null");
            this.xmlDocumentMap.put(name, document);
            return this;
        }

        public OdfPackageDocument build() {
            return new OdfPackageDocumentImpl(this.entry, this.xmlDocumentMap, this.metadata);
        }
    }

    private final FileEntry entry;
    private final Map<String, OdfXmlDocument> xmlDocumentMap;
    private final Metadata metadata;

    private OdfPackageDocumentImpl(final FileEntry entry, final Map<String, OdfXmlDocument> xmlDocumentMap) {
        this(entry, xmlDocumentMap, null);
    }

    private OdfPackageDocumentImpl(final FileEntry entry, final Map<String, OdfXmlDocument> xmlDocumentMap,
            Metadata metadata) {
        super();
        this.entry = entry;
        this.xmlDocumentMap = Collections.unmodifiableMap(xmlDocumentMap);
        this.metadata = metadata;
    }

    static final OdfPackageDocumentImpl of(final FileEntry entry, final Map<String, OdfXmlDocument> xmlDocumentMap) {
        return new OdfPackageDocumentImpl(entry, xmlDocumentMap);
    }

    @Override
    public Map<String, OdfXmlDocument> getXmlDocumentMap() {
        return this.xmlDocumentMap;
    }

    @Override
    public OdfXmlDocument getXmlDocument(String path) {
        return this.xmlDocumentMap.get(path);
    }

    @Override
    public Version getVersion() {
        return Version.fromVersion(this.entry.getVersion());
    }

    @Override
    public DocumentType getDocumentType() {
        return DocumentType.getTypeByMimeString(this.entry.getMediaType());
    }

    @Override
    public FileEntry getFileEntry() {
        return this.entry;
    }

    @Override
    public OdfXmlDocument getXmlDocument() {
        if (this.xmlDocumentMap.containsKey(Constants.NAME_CONTENT)) {
            return this.xmlDocumentMap.get(Constants.NAME_CONTENT);
        } else if (this.xmlDocumentMap.containsKey(Constants.NAME_STYLES)) {
            return this.xmlDocumentMap.get(Constants.NAME_STYLES);
        }
        return null;
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }
}
