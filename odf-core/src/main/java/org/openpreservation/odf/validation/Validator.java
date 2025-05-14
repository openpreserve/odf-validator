package org.openpreservation.odf.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.xml.XmlValidator;
import org.openpreservation.odf.Source;
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.Messages;
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

    private static final ValidationResult notOdf(final Path toValidate) {
        final ValidationResult result = ValidationResultImpl.of("ODF Specification Validation");
        result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-1"));
        return result;
    }

    public Validator() {
        super();
    }

    /**
     * Validate a single document as a specific ODF format
     *.
     * @param toValidate the path to the document to validate
     * @param legal the expected format of the document
     *
     * @return the validation result
     * @throws ParseException if the document cannot be parsed
     * @throws FileNotFoundException if the document cannot be found
     */
    public ValidationReport validateSingleFormat(final Path toValidate, final Formats legal)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        Objects.requireNonNull(legal, String.format(Checks.NOT_NULL, "Formats", "legal"));
        Checks.existingFileCheck(toValidate);
        final ValidationReport report = validate(toValidate);
        final Formats detectedFmt = report.getDetectedFormat();
        if (detectedFmt == null || detectedFmt == Formats.UNKNOWN) {
            report.getValidationResults().get(0).getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-6"));
        } else {
            if (detectedFmt != legal) {
                report.getValidationResults().get(0).getMessageLog()
                        .add(toValidate.toString(),
                             FACTORY.getError("DOC-7",
                                              Messages.parameterListInstance()
                                              .add("expectedMime", legal.mime)
                                              .add("detectedMime", detectedFmt.mime)));
            }
        }
        return report;
    }

    /**
     * Validate a document.
     *
     * @param toValidate the path to the document to validate
     * @return the <code>ValidationResult</code> for the document
     * @throws ParseException if the document cannot be parsed
     * @throws FileNotFoundException if the document cannot be found
     */
    public ValidationReport validate(final Path toValidate)
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

        return ValidationReportImpl.of(toValidate.toString(), Collections.singletonList(notOdf(toValidate)));
    }

    /**
     * Profile check a document using a specific profile.
     *
     * @param toProfile the path to the document to profile
     * @param profile the profile to use
     * @return the <code>ProfileResult</code> for the document
     * @throws ParseException if the document cannot be parsed
     * @throws FileNotFoundException if the document cannot be found
     */
    public ValidationReport profile(final Path toProfile, final Profile profile)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toProfile, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        Objects.requireNonNull(profile, String.format(Checks.NOT_NULL, "Profile", TO_VAL_STRING));
        // Check if the path exists and is not a directory
        Checks.existingFileCheck(toProfile);
        return profile.check(Documents.openDocumentFrom(toProfile));
    }

    /**
     * Validate an OpenDocument.
     *
     * @param toValidate the OpenDocument to validate
     * @return the <code>ValidationResult</code> for the document
     * @throws IOException if the document cannot be read
     */
    public ValidationResult validateOpenDocument(final OpenDocument toValidate)
            throws IOException {
        if (toValidate.getFormat() == Formats.UNKNOWN) {
            return notOdf(toValidate.getPath());
        }
        final ParseResult parseResult = toValidate.getDocument().getXmlDocument().getParseResult();
        return validateParseResult(toValidate.getPath(), parseResult);
    }

    private ValidationReport validatePackage(final Path toValidate)
            throws ParserConfigurationException, SAXException, ParseException, FileNotFoundException {
        final OdfPackage pckg = OdfPackages.getPackageParser().parsePackage(toValidate);
        return ValidationReportImpl.of(toValidate.toString(), pckg, Collections.singletonList(Validators.getValidatingParser().validatePackage(pckg)));
    }

    private ValidationReport validateOpenDocumentXml(final Path toValidate)
            throws ParserConfigurationException, SAXException, IOException {
        final XmlParser checker = new XmlParser();
        final ParseResult parseResult = checker.parse(toValidate);
        return ValidationReportImpl.of(toValidate.toString(), OdfXmlDocuments.odfXmlDocumentOf(parseResult), Collections.singletonList(validateParseResult(toValidate, parseResult)));
    }

    private ValidationResult validateParseResult(final Path toValidate, ParseResult parseResult)
            throws IOException {
        final ValidationResult result = ValidationResultImpl.of("ODF Specification");
        if (parseResult.isWellFormed()) {
            Version version = Version.ODF_13;
            final OdfXmlDocument doc = OdfXmlDocuments.odfXmlDocumentOf(parseResult);
            final XmlValidator validator = new XmlValidator();
            if (parseResult.isRootName(TAG_DOC)) {
                result.getMessageLog().add(toValidate.toString(), FACTORY.getInfo("DOC-2", Messages.parameterListInstance().add("version", doc.getVersion().version)));
                if (doc.getFormat().isPackage()) {
                    result.getMessageLog().add(toValidate.toString(), FACTORY.getInfo("DOC-3", Messages.parameterListInstance().add("mimetype", doc.getFormat().mime)));
                } else {
                    result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-4", Messages.parameterListInstance().add("mimetype", doc.getFormat().mime)));
                }
            }
            if (doc.isExtended()) {
                result.getMessageLog().add(toValidate.toString(),
                        FACTORY.getError("DOC-8", Messages.parameterListInstance().add("namespaces", Utils.collectNsPrefixes(
                                OdfXmlDocuments.odfXmlDocumentOf(parseResult).getForeignNamespaces()))));
            } else {
                final Schema schema = new OdfSchemaFactory().getSchema(OdfNamespaces.OFFICE, version);
                parseResult = validator.validate(parseResult, Files.newInputStream(toValidate), schema);
            }
        } else {
            result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-1"));
        }
        result.getMessageLog().add(toValidate.toString(), parseResult.getMessages());
        return result;
    }
}