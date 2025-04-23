package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;

final class EncryptionRule extends AbstractRule {

    static final EncryptionRule getInstance(final Severity severity) {
        return new EncryptionRule("POL-1", "Encryption",
                "The package MUST NOT contain any encrypted entries.", severity, false);
    }

    private EncryptionRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super(id, name, description, severity, isPrerequisite);
    }

    @Override
    public MessageLog check(final OpenDocument document) {
        Objects.requireNonNull(document, "document must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        if (!document.isPackage() || !document.getPackage().hasManifest()) {
            return messageLog;
        }
        for (final FileEntry entry : document.getPackage().getManifest().getEntries()) {
            if (entry.isEncrypted()) {
                messageLog.add(entry.getFullPath(), Messages.getMessageInstance(this.id, this.severity, this.getName(),
                        this.getDescription(), Messages.parameterListInstance().add("entryPath", entry.getFullPath())));
            }
        }
        return messageLog;
    }
}
