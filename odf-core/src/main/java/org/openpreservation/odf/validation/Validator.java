package org.openpreservation.odf.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.xml.XmlValidator;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.ValidatingParser;
import org.openpreservation.odf.xml.Namespaces;
import org.openpreservation.odf.xml.OdfDocument;
import org.openpreservation.odf.xml.OdfDocumentImpl;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.openpreservation.odf.xml.OdfSchemaFactory.Version;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

public class Validator {
    private static final String TAG_DOC = "office:document";
    private static final MessageFactory FACTORY = Messages.getInstance();

    private static final boolean isFile(final Path toCheck) {
        // Check that the supplied path is an existing, regular file
        return (toCheck != null && Files.exists(toCheck) && !Files.isDirectory(toCheck));
    }

    public Validator() {
        super();
    }

    public ValidationReport validate(final Path toValidate)
            throws ParserConfigurationException, IOException, SAXException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "String", "toValidate"));
        if (!isFile(toValidate)) {
            throw new FileNotFoundException("Path parameter is not a file: " + toValidate);
        }
        if (OdfPackages.isZip(toValidate)) {
            ValidatingParser parser = OdfPackages.getValidatingParser();
            OdfPackage pckg = parser.parsePackage(toValidate);
            return parser.validatePackage(pckg);
        }
        final ValidationReport report = new ValidationReport(toValidate.toString());
        final XmlParser checker = new XmlParser();
        ParseResult parseResult = checker.parse(toValidate);
        if (parseResult.isWellFormed()) {
            Version version = Version.ODF_13;
            final XmlValidator validator = new XmlValidator();
            if (parseResult.isRootName(TAG_DOC)) {
                final OdfDocument doc = OdfDocumentImpl.of(parseResult);
                version = Version.fromVersion(doc.getVersion());
                report.add(toValidate.toString(), FACTORY.getInfo("DOC-2", doc.getVersion()));
                if (Formats.fromMime(doc.getMimeType()).isPackage()) {
                    report.add(toValidate.toString(), FACTORY.getInfo("DOC-3", doc.getMimeType()));
                } else {
                    report.add(toValidate.toString(), FACTORY.getError("DOC-4", doc.getMimeType()));
                }
            }
            Schema schema = new OdfSchemaFactory().getSchema(Namespaces.OFFICE, version);
            parseResult = validator.validate(parseResult, Files.newInputStream(toValidate), schema);
        } else {
            report.add(toValidate.toString(), FACTORY.getError("DOC-1"));
        }
        report.add(toValidate.toString(), parseResult.getMessages());
        return report;
    }
}