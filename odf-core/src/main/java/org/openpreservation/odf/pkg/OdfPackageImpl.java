package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.zip.ZipArchive;
import org.openpreservation.format.zip.ZipArchiveCache;
import org.openpreservation.odf.fmt.Formats;

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
        private String mimetype;
        private Formats format;
        private Manifest manifest;

        private Metadata metadata;

        private Map<String, ParseResult> parseResults;

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

        public Builder metadata(final Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder parseResults(final Map<String, ParseResult> parseResults) {
            Objects.requireNonNull(parseResults, "parseResults cannot be null");
            this.parseResults = parseResults;
            return this;
        }

        public OdfPackage build() {
            return new OdfPackageImpl(this.name, this.archive, this.format, this.mimetype, this.manifest, this.metadata,
                    parseResults);
        }
    }

    private final ZipArchiveCache archive;
    private final Formats format;
    private final String mimetype;
    private final Manifest manifest;
    private final Metadata metadata;
    private final String name;

    private final Map<String, ParseResult> parseResults;

    private OdfPackageImpl(final String name, final ZipArchiveCache archive, final Formats format,
            final String mimetype,
            final Manifest manifest, final Metadata metadata, final Map<String, ParseResult> parseResults) {
        super();
        this.archive = archive;
        this.format = format;
        this.mimetype = mimetype;
        this.manifest = manifest;
        this.metadata = metadata;
        this.parseResults = Collections.unmodifiableMap(parseResults);
        this.name = name;
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
        return this.archive.getZipEntry(Constants.MIMETYPE) != null;
    }

    @Override
    public boolean hasManifest() {
        return this.archive.getZipEntry(Constants.PATH_MANIFEST) != null;
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
    public Metadata getMetadata() {
        return this.metadata;
    }

    @Override
    public Formats getFormat() {
        return this.format;
    }

    @Override
    public InputStream getEntryXmlStream(final String path) throws IOException {
        return this.archive.getEntryInputStream(path);
    }

    @Override
    public ParseResult getEntryXmlParseResult(final String path) {
        return this.parseResults.get(path);
    }

    @Override
    public List<String> getXmlEntryPaths() {
        return this.parseResults.keySet().stream().collect(java.util.stream.Collectors.toList());
    }

    @Override
    public InputStream getEntryStream(final FileEntry entry) throws IOException {
        return this.archive.getEntryInputStream(entry.getFullPath());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((archive == null) ? 0 : archive.hashCode());
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((mimetype == null) ? 0 : mimetype.hashCode());
        result = prime * result + ((manifest == null) ? 0 : manifest.hashCode());
        result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result + ((parseResults == null) ? 0 : parseResults.hashCode());
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
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        if (parseResults == null) {
            if (other.parseResults != null)
                return false;
        } else if (!parseResults.equals(other.parseResults))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OdfPackageImpl [archive=" + archive + ", format=" + format + ", mimetype=" + mimetype + ", manifest="
                + manifest + ", metadata=" + metadata + "]";
    }
}
