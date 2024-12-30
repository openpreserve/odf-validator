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
import org.openpreservation.odf.Source;
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.document.OpenDocument;
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

    public ValidationResult validateSingleFormat(final Path toValidate, final Formats legal)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        Objects.requireNonNull(legal, String.format(Checks.NOT_NULL, "Formats", "legal"));
        Checks.existingFileCheck(toValidate);
        ValidationResult result = validate(toValidate);
        Formats detectedFmt = result.getDetectedFormat();
        if (detectedFmt == null || detectedFmt == Formats.UNKNOWN) {
            result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-6"));
        } else {
            if (detectedFmt != legal) {
                result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-7", legal.mime, detectedFmt.mime));
            }
        }
        return result;
    }

    public ValidationResult validate(final Path toValidate)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));

        // Check if the path exists and is not a directory
        Checks.existingFileCheck(toValidate);

        try {
            if (Source.isZip(toValidate)) {
                return validatePackage(toValidate);
            } else if (Source.isXml(toValidate)) {
                return validateOpenDocumentXml(toValidate);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParseException("Exception thrown when validating ODF document.", e);
        }

        return notOdf(toValidate);
    }

    public ProfileResult profile(final Path toValidate, final Profile profile)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        Objects.requireNonNull(profile, String.format(Checks.NOT_NULL, "Profile", TO_VAL_STRING));
        // Check if the path exists and is not a directory
        Checks.existingFileCheck(toValidate);
        return profile.check(Documents.openDocumentOf(toValidate));
    }

    private static final ValidationResult notOdf(final Path toValidate) {
        final ValidationResult result = ValidationResultImpl.of(toValidate.toString());
        result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-1"));
        return result;
    }

    private ValidationResult validatePackage(final Path toValidate)
            throws ParserConfigurationException, SAXException, ParseException, FileNotFoundException {
        OdfPackage pckg = OdfPackages.getPackageParser().parsePackage(toValidate);
        return validatePackage(pckg);
    }

    private ValidationResult validatePackage(final OdfPackage toValidate)
            throws ParserConfigurationException, SAXException, ParseException, FileNotFoundException {
        ValidatingParser parser = Validators.getValidatingParser();
        return parser.validatePackage(toValidate);
    }

    private ValidationResult validateOpenDocumentXml(final Path toValidate)
            throws ParserConfigurationException, SAXException, IOException {
        final XmlParser checker = new XmlParser();
        ParseResult parseResult = checker.parse(toValidate);
        return validateParseResult(toValidate, parseResult);
    }

    public ValidationResult validateOpenDocument(final OpenDocument toValidate)
            throws IOException {
        if (toValidate.getFormat() == Formats.UNKNOWN) {
            return notOdf(toValidate.getPath());
        }
        ParseResult parseResult = toValidate.getDocument().getXmlDocument().getParseResult();
        return validateParseResult(toValidate.getPath(), parseResult);
    }

    private ValidationResult validateParseResult(final Path toValidate, ParseResult parseResult)
            throws IOException {
        final ValidationResult result = (parseResult.isWellFormed())
                ? ValidationResultImpl.of(toValidate.toString(),
                        Documents.openDocumentOf(toValidate, Documents.odfDocumentOf(parseResult)))
                : ValidationResultImpl.of(toValidate.toString());
        if (parseResult.isWellFormed()) {
            Version version = Version.ODF_13;
            final OdfXmlDocument doc = OdfXmlDocuments.odfXmlDocumentOf(parseResult);
            final XmlValidator validator = new XmlValidator();
            if (parseResult.isRootName(TAG_DOC)) {
                version = Version.fromVersion(doc.getVersion());
                result.getMessageLog().add(toValidate.toString(), FACTORY.getInfo("DOC-2", doc.getVersion()));
                if (Formats.fromMime(doc.getMimeType()).isPackage()) {
                    result.getMessageLog().add(toValidate.toString(), FACTORY.getInfo("DOC-3", doc.getMimeType()));
                } else {
                    result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-4", doc.getMimeType()));
                }
            }
            if (doc.isExtended()) {
                result.getMessageLog().add(toValidate.toString(),
                        FACTORY.getError("DOC-8", Utils.collectNsPrefixes(
                                OdfXmlDocuments.odfXmlDocumentOf(parseResult).getForeignNamespaces())));
            } else {
                Schema schema = new OdfSchemaFactory().getSchema(OdfNamespaces.OFFICE, version);
                parseResult = validator.validate(parseResult, Files.newInputStream(toValidate), schema);
            }
        } else {
            result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-1"));
        }
        result.getMessageLog().add(toValidate.toString(), parseResult.getMessages());
        return result;
    }
}