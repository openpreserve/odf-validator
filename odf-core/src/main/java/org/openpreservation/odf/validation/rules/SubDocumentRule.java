package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;

final class SubDocumentRule extends AbstractRule {

    static final SubDocumentRule getInstance(final Severity severity) {
        return new SubDocumentRule("POL_10", "Sub Documents",
                "The package MUST NOT contain sub documents.", severity, false);
    }

    private SubDocumentRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super(id, name, description, severity, isPrerequisite);
    }

    @Override
    public MessageLog check(final OpenDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        if (document.isPackage() && document.getPackage().hasManifest() && document.getPackage().getManifest().getDocumentEntries().size() > 1) {
            messageLog.add(OdfPackages.PATH_MANIFEST,
                    Messages.getMessageInstance(this.id, this.severity, this.getName(),
                            this.getDescription()));
        }
        return messageLog;
    }
}
