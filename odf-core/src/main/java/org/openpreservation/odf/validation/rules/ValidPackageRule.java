package org.openpreservation.odf.validation.rules;

import java.io.FileNotFoundException;
import java.util.Objects;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validator;
import org.openpreservation.odf.xml.Version;

class ValidPackageRule extends AbstractRule {
    private static final String PACK_MESS = "File is not an ODF package. ";
    private static final String VER_MESS = "Package version: %s detected. ";
    private static final String INV_MESS = "Package does not comply with specification. ";

    static final ValidPackageRule getInstance(final Severity severity) {
        return new ValidPackageRule("POL_2", "Standard Compliance",
                "The file MUST comply with the standard \"OASIS Open Document Format for Office Applications (OpenDocument) v1.3\".",
                severity, false);
    }

    private ValidPackageRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super(id, name, description, severity, isPrerequisite);
    }

    @Override
    public MessageLog check(final OpenDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        try {
            Validator validator = new Validator();
            ValidationReport report = validator.validate(document.getPath());
            return check(report);
        } catch (FileNotFoundException e) {
            throw new ParseException("File not found exception when processing package.", e);
        }
    }

    @Override
    public MessageLog check(final ValidationReport report) {
        Objects.requireNonNull(report, "report must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        if (report.document == null) {
            messageLog.add(report.name,
                    Messages.getMessageInstance(this.id, Message.Severity.ERROR,
                            this.getName(), PACK_MESS + this.getDescription()));
            return messageLog;
        }
        if (!report.isValid() || !report.document.getVersion().equals(Version.ODF_13) || !report.document.isPackage()) {
            String message = (!report.document.isPackage()) ? PACK_MESS : "";
            if (report.document != null && !report.document.getVersion().equals(Version.ODF_13)) {
                message = String.format(VER_MESS, report.document.getVersion());
            }
            if (!report.isValid()) {
                message += INV_MESS;
            }
            messageLog.add(report.name,
                    Messages.getMessageInstance(this.id, Message.Severity.ERROR,
                            this.getName(), message + this.getDescription()));
        }
        return messageLog;
    }
}
