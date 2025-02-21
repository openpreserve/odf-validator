package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Message.Severity;

final class PackageMimeTypeRule extends AbstractRule {

    static final PackageMimeTypeRule getInstance(final Severity severity) {
        return new PackageMimeTypeRule("POL-3", "Package mimetype entry",
                "An ODF package MUST have a mimetype entry as specified in the Section 3.3 of the ODF specification v1.3.",
                severity, false);
    }

    private PackageMimeTypeRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super(id, name, description, severity, isPrerequisite);
    }

    @Override
    public MessageLog check(final OpenDocument document) {
        Objects.requireNonNull(document, "document must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        if (document.isPackage() && !document.getPackage().hasMimeEntry()) {
            messageLog.add(OdfPackages.MIMETYPE, Messages.getMessageInstance(this.id, this.severity, this.getName(),
                    this.getDescription()));
        }
        return messageLog;
    }
}
