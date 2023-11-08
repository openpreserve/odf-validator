package org.openpreservation.odf.document;

import java.util.Objects;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;

public class Documents {
    private Documents() {
        throw new AssertionError("Utility class 'Documents' should not be instantiated");
    }

    public static final OpenDocument openDocumentOf(OdfDocument document) {
        Objects.requireNonNull(document, "OdfDocument parameter document cannot be null");
        return OpenDocumentImpl.of(document);
    }

    public static final OpenDocument openDocumentOf(OdfPackage pkg) {
        Objects.requireNonNull(pkg, "OdfPackage pkg document cannot be null");
        return OpenDocumentImpl.of(pkg);
    }

    public static final OdfDocument odfDocumentOf(final OdfXmlDocument xmlDocument, final Metadata metadata) {
        Objects.requireNonNull(xmlDocument, "OdfXmlDocument parameter xmlDocument cannot be null");
        Objects.requireNonNull(metadata, "Metadata parameter metadata cannot be null");
        return OdfDocumentImpl.of(xmlDocument, metadata);
    }

    public static final OdfDocument odfDocumentOf(final ParseResult parseResult, final Metadata metadata) {
        Objects.requireNonNull(parseResult, "ParseResult parameter parseResult cannot be null");
        Objects.requireNonNull(metadata, "Metadata parameter metadata cannot be null");
        return OdfDocumentImpl.of(OdfXmlDocuments.odfXmlDocumentOf(parseResult), metadata);
    }

    public static final OdfDocument odfDocumentOf(final ParseResult parseResult) {
        Objects.requireNonNull(parseResult, "ParseResult parameter parseResult cannot be null");
        return OdfDocumentImpl.of(parseResult);
    }

}
