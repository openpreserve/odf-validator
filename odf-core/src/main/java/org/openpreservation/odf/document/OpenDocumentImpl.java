package org.openpreservation.odf.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackageDocument;

final class OpenDocumentImpl implements OpenDocument {
    private final OdfDocument document;
    private final OdfPackage pkg;

    private OpenDocumentImpl(OdfDocument document, OdfPackage pkg) {
        super();
        this.document = document;
        this.pkg = pkg;
    }

    static final OpenDocument of(OdfDocument document) {
        Objects.requireNonNull(document, "OdfDocument parameter document cannot be null");
        return new OpenDocumentImpl(document, null);
    }

    static final OpenDocument of(OdfPackage pkg) {
        Objects.requireNonNull(pkg, "OdfPackage pkg document cannot be null");
        return new OpenDocumentImpl(pkg.getDocument(), pkg);
    }

    @Override
    public FileType getFileType() {
        return this.pkg != null ? FileType.PACKAGE : FileType.SINGLE;
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
    public int hashCode() {
        return Objects.hash(document, pkg);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OpenDocumentImpl))
            return false;
        OpenDocumentImpl other = (OpenDocumentImpl) obj;
        return Objects.equals(document, other.document) && Objects.equals(pkg, other.pkg);
    }
    
}
