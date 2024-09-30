package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.xml.OdfXmlDocument;

final class DigitalSignaturesRule extends AbstractRule {

    static final DigitalSignaturesRule getInstance(final Severity severity) {
        return new DigitalSignaturesRule("POL_9", "Digital Signatures",
                "The package MUST NOT contain any digital signatures.", severity, false);
    }

    private DigitalSignaturesRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
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
        if (odfPackage.hasDsigEntries()) {
            for (final String path : odfPackage.getMetaInfMap().keySet()) {
                if (OdfPackages.isDsig(path)) {
                    messageLog.add(path, Messages.getMessageInstance(this.id, this.severity, this.getName(),
                            this.getDescription()));
                }
            }
        }
        return messageLog;
    }
}
