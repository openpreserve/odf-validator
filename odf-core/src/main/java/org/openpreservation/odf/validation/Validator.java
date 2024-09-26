package org.openpreservation.odf.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.openpreservation.format.xml.Namespace;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.xml.XmlValidator;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.OdfNamespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.Version;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

public class Validator {
    private static final String TAG_DOC = "office:document";
    private static final String TO_VAL_STRING = "toValidate";
    private static final MessageFactory FACTORY = Messages.getInstance();

    public Validator() {
        super();
    }

    public ValidationReport validateSpreadsheet(final Path toValidate) throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        return validateSingleFormat(toValidate, Formats.ODS);
    }

    public ValidationReport validateSpreadsheet(final File toValidate) throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        return validateSingleFormat(toValidate, Formats.ODS);
    }

    public ValidationReport validateSingleFormat(final File toValidate, final Formats legal)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "File", TO_VAL_STRING));
        Objects.requireNonNull(legal, String.format(Checks.NOT_NULL, "Formats", "legal"));
        return validateSingleFormat(toValidate.toPath(), legal);
    }

    public ValidationReport validateSingleFormat(final Path toValidate, final Formats legal)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        Objects.requireNonNull(legal, String.format(Checks.NOT_NULL, "Formats", "legal"));
        Checks.existingFileCheck(toValidate);
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

    public ValidationReport validate(final File toValidate) throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "File", TO_VAL_STRING));
        return validate(toValidate.toPath());
    }

    public ValidationReport validate(final Path toValidate)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));

        // Check if the path exists and is not a directory
        Checks.existingFileCheck(toValidate);

        try {
            if (OdfPackages.isZip(toValidate)) {
                return validatePackage(toValidate);
            } else if (OdfXmlDocuments.isXml(toValidate)) {
                return validateOpenDocumentXml(toValidate);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParseException("Exception thrown when validating ODF document.", e);
        }

        return notOdf(toValidate);
    }

    private static final ValidationReport notOdf(final Path toValidate) {
        final ValidationReport report = ValidationReport.of(toValidate.toString());
        report.add(toValidate.toString(), FACTORY.getError("DOC-1"));
        return report;
    }

    private ValidationReport validatePackage(final Path toValidate)
            throws ParserConfigurationException, SAXException, ParseException, FileNotFoundException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pckg = parser.parsePackage(toValidate);
        return parser.validatePackage(pckg);
    }

    private ValidationReport validateOpenDocumentXml(final Path toValidate)
            throws ParserConfigurationException, SAXException, IOException {
        final XmlParser checker = new XmlParser();
        ParseResult parseResult = checker.parse(toValidate);
        final ValidationReport report = (parseResult.isWellFormed())
                ? ValidationReport.of(toValidate.toString(),
                        Documents.openDocumentOf(Documents.odfDocumentOf(parseResult)))
                : ValidationReport.of(toValidate.toString());
        if (parseResult.isWellFormed()) {
            Version version = Version.ODF_13;
            final OdfXmlDocument doc = OdfXmlDocuments.odfXmlDocumentOf(parseResult);
            final XmlValidator validator = new XmlValidator();
            if (parseResult.isRootName(TAG_DOC)) {
                version = Version.fromVersion(doc.getVersion());
                report.add(toValidate.toString(), FACTORY.getInfo("DOC-2", doc.getVersion()));
                if (Formats.fromMime(doc.getMimeType()).isPackage()) {
                    report.add(toValidate.toString(), FACTORY.getInfo("DOC-3", doc.getMimeType()));
                } else {
                    report.add(toValidate.toString(), FACTORY.getError("DOC-4", doc.getMimeType()));
                }
            }
            if (doc.isExtended()) {
                report.add(toValidate.toString(),
                        FACTORY.getError("DOC-8", Utils.collectNsPrefixes(
                                OdfXmlDocuments.odfXmlDocumentOf(parseResult).getForeignNamespaces())));
            } else {
                Schema schema = new OdfSchemaFactory().getSchema(OdfNamespaces.OFFICE, version);
                parseResult = validator.validate(parseResult, Files.newInputStream(toValidate), schema);
            }
        } else {
            report.add(toValidate.toString(), FACTORY.getError("DOC-1"));
        }
        report.add(toValidate.toString(), parseResult.getMessages());
        return report;
    }
}