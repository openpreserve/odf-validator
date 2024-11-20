package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.OdfPackages;

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
    public MessageLog check(final OpenDocument document) {
        Objects.requireNonNull(document, "document must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        if (document.isPackage() && document.getPackage().hasDsigEntries()) {
            for (final String path : document.getPackage().getMetaInfMap().keySet()) {
                if (OdfPackages.isDsig(path)) {
                    messageLog.add(path, Messages.getMessageInstance(this.id, this.severity, this.getName(),
                            this.getDescription()));
                }
            }
        }
        return messageLog;
    }
}
