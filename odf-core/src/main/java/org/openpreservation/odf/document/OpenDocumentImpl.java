package org.openpreservation.odf.document;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackageDocument;
import org.openpreservation.odf.xml.Version;

final class OpenDocumentImpl implements OpenDocument {
    private final Path path;
    private final OdfDocument document;
    private final OdfPackage pkg;

    private OpenDocumentImpl(final Path path, OdfDocument document, OdfPackage pkg) {
        super();
        this.path = path;
        this.document = document;
        this.pkg = pkg;
    }

    static final OpenDocument of(final Path path, OdfDocument document) {
        Objects.requireNonNull(document, "OdfDocument parameter document cannot be null");
        return new OpenDocumentImpl(path, document, null);
    }

    static final OpenDocument of(final Path path, OdfPackage pkg) {
        Objects.requireNonNull(pkg, "OdfPackage pkg document cannot be null");
        return new OpenDocumentImpl(path, pkg.getDocument(), pkg);
    }

    @Override
    public Path getPath() {
        return this.path;
    }

    @Override
    public FileType getFileType() {
        return this.pkg != null ? FileType.PACKAGE : FileType.FLAT_XML;
    }

    @Override
    public boolean isPackage() {
        return this.pkg != null;
    }

    @Override
    public OdfDocument getDocument() {
        return this.document;
    }

    @Override
    public Collection<OdfDocument> getSubDocuments() {
        List<OdfDocument> subDocs = new ArrayList<>();
        for (OdfPackageDocument doc : this.pkg.getDocumentMap().values()) {
            if (!doc.equals(this.document)) {
                subDocs.add(doc);
            }
        }
        return subDocs;
    }

    @Override
    public OdfPackage getPackage() {
        return this.pkg;
    }

    @Override
    public Formats getFormat() {
        return (this.isPackage()) ? this.pkg.getDetectedFormat()
                : Formats.fromMime(this.document.getXmlDocument().getMimeType());
    }

    @Override
    public Version getVersion() {
        return (this.isPackage()) ? this.pkg.getDetectedVersion() : this.document.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, document, pkg);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OpenDocumentImpl))
            return false;
        OpenDocumentImpl other = (OpenDocumentImpl) obj;
        return Objects.equals(path, other.path) && Objects.equals(document, other.document) && Objects.equals(pkg, other.pkg);
    }

}
