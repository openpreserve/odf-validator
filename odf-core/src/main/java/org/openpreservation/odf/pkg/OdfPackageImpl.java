package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.zip.ZipArchive;
import org.openpreservation.format.zip.ZipArchiveCache;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.OdfFormats;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.Version;

final class OdfPackageImpl implements OdfPackage {

    static final class Builder {
        public static Builder builder() {
            return new Builder();
        }

        public static Builder fromFormat(final Formats format) {
            Objects.requireNonNull(format, "format cannot be null");
            return builder().format(format);
        }

        private ZipArchiveCache archive;
        private String name;
        private String mimetype = OdfFormats.MIME_UKNOWN;
        private Formats format = Formats.UNKNOWN;
        private Manifest manifest;
        private Version version = Version.UNKNOWN;

        private Map<String, OdfPackageDocument> documentMap = new HashMap<>();
        private Map<String, ParseResult> metaInfMap = new HashMap<>();

        private Builder() {
        }

        public Builder archive(final ZipArchiveCache archive) {
            Objects.requireNonNull(archive, "ZipArchiveCache cannot be null");
            this.archive = archive;
            return this;
        }

        public Builder format(final Formats format) {
            Objects.requireNonNull(format, "Format cannot be null");
            this.format = format;
            return this;
        }

        public Builder mimetype(final String mimetype) {
            this.mimetype = mimetype;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder manifest(final Manifest manifest) {
            this.manifest = manifest;
            return this;
        }

        public Builder version(final Version version) {
            this.version = version;
            return this;
        }

        public Builder document(final String path, final OdfPackageDocument document) {
            Objects.requireNonNull(path, "path cannot be null");
            Objects.requireNonNull(document, "document cannot be null");
            this.documentMap.put(path, document);
            return this;
        }

        public Builder metaInf(final String path, final ParseResult parseResult) {
            Objects.requireNonNull(path, "path cannot be null");
            Objects.requireNonNull(parseResult, "parseResult cannot be null");
            this.metaInfMap.put(path, parseResult);
            return this;
        }

        public OdfPackage build() {
            return new OdfPackageImpl(this.name, this.archive, this.format, this.version, this.mimetype, this.manifest,
                    this.documentMap, this.metaInfMap);
        }
    }

    private final ZipArchiveCache archive;
    private final Formats format;
    private final String mimetype;
    private final Manifest manifest;
    private final String name;
    private final Version version;

    private final Map<String, OdfPackageDocument> documentMap;
    private final Map<String, ParseResult> metaInfMap;

    private OdfPackageImpl(final String name, final ZipArchiveCache archive, final Formats format,
            final Version version,
            final String mimetype, final Manifest manifest,
            final Map<String, OdfPackageDocument> documentMap,
            final Map<String, ParseResult> metaInfMap) {
        super();
        this.archive = archive;
        this.format = format;
        this.mimetype = mimetype;
        this.manifest = manifest;
        this.documentMap = Collections.unmodifiableMap(documentMap);
        this.metaInfMap = Collections.unmodifiableMap(metaInfMap);
        this.name = name;
        this.version = version;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getMimeType() {
        return this.mimetype;
    }

    @Override
    public boolean hasMimeEntry() {
        return (this.archive != null) && this.archive.getZipEntry(OdfFormats.MIMETYPE) != null;
    }

    @Override
    public boolean isMimeFirst() {
        return (this.archive != null) && this.archive.getFirstEntry().getName().equals(OdfFormats.MIMETYPE);
    }

    @Override
    public boolean hasManifest() {
        return (this.archive != null) && this.archive.getZipEntry(Constants.PATH_MANIFEST) != null
                && this.manifest != null;
    }

    @Override
    public boolean hasThumbnail() {
        return (this.archive != null) && this.archive.getZipEntry(Constants.PATH_THUMBNAIL) != null;
    }

    @Override
    public ZipArchive getZipArchive() {
        return this.archive;
    }

    @Override
    public boolean isWellFormedZip() {
        return this.archive != null;
    }

    @Override
    public Manifest getManifest() {
        return this.manifest;
    }

    @Override
    public Formats getDetectedFormat() {
        return this.format;
    }

    @Override
    public Version getDetectedVersion() {
        return this.version;
    }

    @Override
    public InputStream getEntryXmlStream(final String path) throws IOException {
        return (this.archive != null) ? this.archive.getEntryInputStream(path) : null;
    }

    @Override
    public ParseResult getEntryXmlParseResult(final String path) {
        Path filePath = Paths.get(path);
        if (PackageParserImpl.isMetaInf(path)) {
            return this.metaInfMap.get(path);
        }
        Path parent = filePath.getParent();
        OdfPackageDocument doc = this.documentMap.get((parent == null) ? "/" : parent.toString());
        return (doc == null) ? null
                : doc.getXmlDocument(filePath.getFileName().toString())
                        .getParseResult();
    }

    @Override
    public List<String> getXmlEntryPaths() {
        List<String> paths = new ArrayList<>();
        for (Entry<String, ParseResult> metaEntry : this.metaInfMap.entrySet()) {
            paths.add(metaEntry.getKey());
        }
        for (Entry<String, OdfPackageDocument> docEntry : this.documentMap.entrySet()) {
            final String docKey = "/".equals(docEntry.getKey()) ? "" : docEntry.getKey();
            for (Entry<String, OdfXmlDocument> xmlEntry : docEntry.getValue().getXmlDocumentMap().entrySet()) {
                paths.add(docKey + xmlEntry.getKey());
            }
        }
        return paths;
    }

    @Override
    public Set<FileEntry> getXmlEntries() {
        Set<FileEntry> entries = new HashSet<>();
        for (FileEntry entry : this.manifest.getEntriesByMediaType("text/xml")) {
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public InputStream getEntryStream(final FileEntry entry) throws IOException {
        return (this.archive != null) ? this.archive.getEntryInputStream(entry.getFullPath()) : null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((archive == null) ? 0 : archive.hashCode());
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((mimetype == null) ? 0 : mimetype.hashCode());
        result = prime * result + ((manifest == null) ? 0 : manifest.hashCode());
        result = prime * result + ((documentMap == null) ? 0 : documentMap.hashCode());
        result = prime * result + ((metaInfMap == null) ? 0 : metaInfMap.hashCode());
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
        final OdfPackageImpl other = (OdfPackageImpl) obj;
        if (archive == null) {
            if (other.archive != null)
                return false;
        } else if (!archive.equals(other.archive))
            return false;
        if (format != other.format)
            return false;
        if (version != other.version)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (mimetype == null) {
            if (other.mimetype != null)
                return false;
        } else if (!mimetype.equals(other.mimetype))
            return false;
        if (manifest == null) {
            if (other.manifest != null)
                return false;
        } else if (!manifest.equals(other.manifest))
            return false;
        if (documentMap == null) {
            if (other.documentMap != null)
                return false;
        } else if (!documentMap.equals(other.documentMap))
            return false;
        if (metaInfMap == null) {
            if (other.metaInfMap != null)
                return false;
        } else if (!metaInfMap.equals(other.metaInfMap))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OdfPackageImpl [archive=" + archive + ", format=" + format + ", mimetype=" + mimetype + ", manifest="
                + manifest + "]";
    }

    @Override
    public OdfPackageDocument getDocument() {
        return this.documentMap.get("/");
    }

    @Override
    public Map<String, OdfPackageDocument> getDocumentMap() {
        return this.documentMap;
    }

    @Override
    public OdfPackageDocument getSubDocument(String path) {
        return this.documentMap.get(path);
    }

    @Override
    public Map<String, ParseResult> getMetaInfMap() {
        return Collections.unmodifiableMap(this.metaInfMap);
    }

    @Override
    public boolean hasDsigEntries() {
        if (this.archive == null) {
            return false;
        }
        for (ZipEntry entry : this.archive.getZipEntries()) {
            if (PackageParserImpl.isDsig(entry.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isExtended() {
        for (OdfPackageDocument doc : this.documentMap.values()) {
            if (doc.getXmlDocument().isExtended()) {
                return true;
            }
        }
        return false;
    }
}
