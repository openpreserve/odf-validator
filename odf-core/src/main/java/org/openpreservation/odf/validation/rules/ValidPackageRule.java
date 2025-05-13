package org.openpreservation.odf.validation.rules;

import java.io.FileNotFoundException;
import java.util.Objects;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validator;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;
import org.openpreservation.odf.xml.Version;

class ValidPackageRule extends AbstractRule {
    private static final String PACK_MESS = "File is not an ODF package. ";
    private static final String VER_MESS = "Package version: %s detected. ";
    private static final String INV_MESS = "Package does not comply with specification. ";

    static final ValidPackageRule getInstance(final Severity severity) {
        return new ValidPackageRule("POL-2", "Standard Compliance",
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
            final MessageLog messageLog = Messages.messageLogInstance();
            Validator validator = new Validator();
            ValidationReport report = validator.validate(document.getPath());
            ParameterList parameters = Messages.parameterListInstance();
            if (!report.getValidationResults().get(0).isValid() || !document.getVersion().equals(Version.ODF_13) || !document.isPackage()) {
                String message = (!document.isPackage()) ? PACK_MESS : "";
                if (document != null && !document.getVersion().equals(Version.ODF_13)) {
                    message = String.format(VER_MESS, document.getVersion());
                    parameters.add("version", document.getVersion().version);
                }
                if (!report.getValidationResults().get(0).isValid()) {
                    message += INV_MESS;
                }
                messageLog.add(report.getFilename(),
                        Messages.getMessageInstance(this.id, Message.Severity.ERROR,
                                this.getName(), message + this.getDescription(), parameters));
            }
            return messageLog;
        } catch (FileNotFoundException e) {
            throw new ParseException("File not found exception when processing package.", e);
        }
    }
}
