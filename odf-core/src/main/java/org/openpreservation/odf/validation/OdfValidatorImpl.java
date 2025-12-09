package org.openpreservation.odf.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.xml.XmlParsers;
import org.openpreservation.format.xml.XmlValidator;
import org.openpreservation.odf.Source;
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;
import org.openpreservation.odf.xml.OdfNamespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.Version;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class OdfValidatorImpl implements OdfValidator {
    private static final String TAG_DOC = "office:document";
    private static final String TO_VAL_STRING = "toValidate";
    private static final String ODF_SPECS = "ODF Specification";
    private static final MessageFactory FACTORY = Messages.getInstance();

    private static final ValidationResult notOdf(final Path toValidate) {
        final ValidationResult result = ValidationResultImpl.of("ODF Specification Validation");
        result.getMessageLog().add(toValidate.toString(), FACTORY.getError("DOC-1"));
        return result;
    }

    static final OdfValidator getInstance(final boolean isExtended) {
        return new OdfValidatorImpl(isExtended);
    }

    private final boolean isExtended;

    private OdfValidatorImpl(final boolean isExtended) {
        super();
        this.isExtended = isExtended;
    }

    @Override
    public ValidationReport validate(final Path toValidate, final Formats legal)
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

    @Override
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

    @Override
    public ValidationReport profile(final Path toProfile, final Profile profile)
            throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toProfile, String.format(Checks.NOT_NULL, "Path", TO_VAL_STRING));
        Objects.requireNonNull(profile, String.format(Checks.NOT_NULL, "Profile", TO_VAL_STRING));
        // Check if the path exists and is not a directory
        Checks.existingFileCheck(toProfile);
        return profile.check(Documents.openDocumentFrom(toProfile));
    }

    @Override
    public ValidationResult validate(final OpenDocument toValidate)
            throws IOException {
        if (toValidate.getFormat() == Formats.UNKNOWN) {
            return notOdf(toValidate.getPath());
        }
        final ParseResult parseResult = toValidate.getDocument().getXmlDocument().getParseResult();
        List<Message> messages = new ArrayList<>(OdfXmlValidator.getDocInfoMessages(
            OdfXmlDocuments.odfXmlDocumentOf(parseResult)));
        messages.addAll(OdfXmlValidator.getInstance(this.isExtended).validate(toValidate.getPath(), parseResult));
        return ValidationResultImpl.of(ODF_SPECS, Messages.messageLogInstance(toValidate.getPath().toString(),
                messages));
    }

    private ValidationReport validatePackage(final Path toValidate)
            throws ParserConfigurationException, SAXException, ParseException, FileNotFoundException {
        final OdfPackage pckg = OdfPackages.getPackageParser().parsePackage(toValidate);
        return ValidationReportImpl.of(toValidate.toString(), pckg,
                Collections.singletonList(OdfValidators.getValidatingParser(this.isExtended).validatePackage(pckg)));
    }

    private ValidationReport validateOpenDocumentXml(final Path toValidate)
            throws ParserConfigurationException, SAXException, IOException {
        final XmlParser checker = XmlParsers.getNonValidatingParser();
        final ParseResult parseResult = checker.parse(toValidate);
        List<Message> messages = new ArrayList<>(OdfXmlValidator.getDocInfoMessages(
                OdfXmlDocuments.odfXmlDocumentOf(parseResult)));
        messages.addAll(OdfXmlValidator.getInstance(this.isExtended).validate(toValidate, parseResult));
        return ValidationReportImpl.of(toValidate.toString(),
                OdfXmlDocuments.odfXmlDocumentOf(parseResult), Collections.singletonList(
                        ValidationResultImpl.of(ODF_SPECS, Messages.messageLogInstance(toValidate.toString(),
                                messages))));
    }
}