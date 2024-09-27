package org.openpreservation.odf.validation.rules;

import java.io.FileNotFoundException;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.ValidatingParser;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validators;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.Version;
import org.xml.sax.SAXException;


class ValidPackageRule extends AbstractRule {
    private static final String VER_MESS = "Package version: %s detected. ";
    private static final String INV_MESS = "Package does not comply with specification. ";

    static final ValidPackageRule getInstance(final Severity severity)
            throws ParserConfigurationException, SAXException {
        return new ValidPackageRule("POL_2", "Standard Compliance",
                "The file MUST comply with the standard \"OASIS Open Document Format for Office Applications (OpenDocument) v1.3\".",
                severity, false);
    }

    private final ValidatingParser validatingParser = Validators.getValidatingParser();

    private ValidationReport validationReport;

    private ValidPackageRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) throws ParserConfigurationException, SAXException {
        super(id, name, description, severity, isPrerequisite);
    }

    @Override
    public MessageLog check(final OdfXmlDocument document) {
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }

    @Override
    public MessageLog check(final OdfPackage odfPackage) throws ParseException {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        try {
            this.validationReport = this.validatingParser.validatePackage(odfPackage);
        } catch (FileNotFoundException e) {
            throw new ParseException("File not found exception when processing package.", e);
        }
        if (!this.validationReport.isValid() || !odfPackage.getDetectedVersion().equals(Version.ODF_13)) {
            String message = "";
            if (!odfPackage.getDetectedVersion().equals(Version.ODF_13)) {
                message = String.format(VER_MESS, odfPackage.getDetectedVersion().version);
            }
            if (!this.validationReport.isValid()) {
                message += INV_MESS;
            }
            messageLog.add(odfPackage.getName(), Messages.getMessageInstance(this.id, Message.Severity.ERROR,
                    this.getName(), message + this.getDescription()));
        }
        return messageLog;
    }

    public final ValidationReport getValidationReport() {
        return this.validationReport;
    }
}
