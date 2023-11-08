package org.openpreservation.odf.validation;

import java.io.File;
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
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.xml.Namespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.Version;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

public class Validator {
    private static final String TAG_DOC = "office:document";
    private static final MessageFactory FACTORY = Messages.getInstance();

    public Validator() {
        super();
    }

    public ValidationReport validateSpreadsheet(final Path toValidate) throws ParserConfigurationException, IOException, SAXException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", "toValidate"));
        return validateSingleFormat(toValidate, Formats.ODS);
    }

    public ValidationReport validateSpreadsheet(final File toValidate) throws ParserConfigurationException, IOException, SAXException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", "toValidate"));
        return validateSingleFormat(toValidate, Formats.ODS);
    }

    public ValidationReport validateSingleFormat(final File toValidate, final Formats legal) throws ParserConfigurationException, IOException, SAXException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "File", "toValidate"));
        Objects.requireNonNull(legal, String.format(Checks.NOT_NULL, "Formats", "legal"));
        return validateSingleFormat(toValidate.toPath(), legal);
    }

    public ValidationReport validateSingleFormat(final Path toValidate, final Formats legal) throws ParserConfigurationException, IOException, SAXException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", "toValidate"));
        Objects.requireNonNull(legal, String.format(Checks.NOT_NULL, "Formats", "legal"));
        ValidationReport report = validate(toValidate);
        if (report.document == null || report.document.getFormat() == null) {
            report.add(toValidate.toString(), FACTORY.getError("DOC-6"));
        } else {
            Formats detectedFmt = report.document.getFormat();
            if (detectedFmt != legal) {
                report.add(toValidate.toString(), FACTORY.getError("DOC-7", legal.mime, detectedFmt.mime));
            }
        }
        return report;
    }

    public ValidationReport validate(final File toValidate) throws ParserConfigurationException, IOException, SAXException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "File", "toValidate"));
        return validate(toValidate.toPath());
    }

    public ValidationReport validate(final Path toValidate)
            throws ParserConfigurationException, IOException, SAXException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", "toValidate"));

        // Check if the path exists and is not a directory
        existingFileCheck(toValidate);

        if (OdfPackages.isZip(toValidate)) {
            return validatePackage(toValidate);
        } else if (OdfXmlDocuments.isXml(toValidate)) {
            return validateOpenDocumentXml(toValidate);
        }

        return notOdf(toValidate);
    }

    private static final ValidationReport notOdf(final Path toValidate) {
        final ValidationReport report = ValidationReport.of(toValidate.toString());
        report.add(toValidate.toString(), FACTORY.getError("DOC-1"));
        return report;
    }

    static final void existingFileCheck(final Path toValidate) throws FileNotFoundException {
        if (!Files.exists(toValidate)) {
            throw new FileNotFoundException(errMessage(toValidate.toString(), " does not exist."));
        } else if (Files.isDirectory(toValidate)) {
            throw new IllegalArgumentException(errMessage(toValidate.toString(), " is a directory."));
        }
    }

    private ValidationReport validatePackage(final Path toValidate) throws ParserConfigurationException, SAXException, IOException {
            ValidatingParser parser = Validators.getValidatingParser();
            OdfPackage pckg = parser.parsePackage(toValidate);
            return parser.validatePackage(pckg);
    }
    private ValidationReport validateOpenDocumentXml(final Path toValidate) throws ParserConfigurationException, SAXException, IOException {
        final XmlParser checker = new XmlParser();
        ParseResult parseResult = checker.parse(toValidate);
        final ValidationReport report = (parseResult.isWellFormed()) ? ValidationReport.of(toValidate.toString(), Documents.openDocumentOf(Documents.odfDocumentOf(parseResult))) : ValidationReport.of(toValidate.toString());
        if (parseResult.isWellFormed()) {
            Version version = Version.ODF_13;
            final XmlValidator validator = new XmlValidator();
            if (parseResult.isRootName(TAG_DOC)) {
                final OdfXmlDocument doc = OdfXmlDocuments.odfXmlDocumentOf(parseResult);
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

    private static final String errMessage(final String toValidate, final String subMess) {
        return String.format("Supplied Path parameter: %s %s", toValidate, subMess);
    }
}