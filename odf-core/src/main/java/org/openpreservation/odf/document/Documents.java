package org.openpreservation.odf.document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.odf.Source;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.xml.sax.SAXException;

public class Documents {
    private Documents() {
        throw new AssertionError("Utility class 'Documents' should not be instantiated");
    }

    public static final OpenDocument openDocumentOf(final Path path, final OdfDocument document) {
        Objects.requireNonNull(document, "OdfDocument parameter document cannot be null");
        return OpenDocumentImpl.of(path, document);
    }

    public static final OpenDocument openDocumentOf(final Path path, final OdfPackage pkg) {
        Objects.requireNonNull(path, "Path path document cannot be null");
        Objects.requireNonNull(pkg, "OdfPackage pkg document cannot be null");
        return OpenDocumentImpl.of(path, pkg);
    }

    public static final OpenDocument openDocumentOf(final Path toParse) throws ParseException {
        Objects.requireNonNull(toParse, "Path path document cannot be null");
        try {
            if (Source.isZip(toParse)) {
                return openDocumentOf(toParse, OdfPackages.getPackageParser().parsePackage(toParse));
            } else if (Source.isXml(toParse)) {
                return openDocumentOf(toParse, from(toParse));
            }
        } catch (IOException e) {
            throw new ParseException("IOException thrown when validating ODF document.", e);
        }
        return OpenDocumentImpl.of(toParse);
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

    static final OdfDocument from(final Path path) throws ParseException {
        try (final InputStream is = Files.newInputStream(path)) {
            return OdfDocumentImpl.from(is);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParseException("Exception thrown when validating ODF document.", e);
        }
    }
}
