package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.util.Objects;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.xml.OdfXmlDocument;

final class EncryptionRule extends AbstractRule {

    static final EncryptionRule getInstance() {
        return new EncryptionRule("ODF_1", "Encryption",
                "The package MUST NOT contain any encrypted entries.", true);
    }

    private EncryptionRule(final String id, final String name, final String description, final boolean isPrerequisite) {
        super(id, name, description, isPrerequisite);
    }

    @Override
    public MessageLog check(final OdfXmlDocument document) {
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }

    @Override
    public MessageLog check(final OdfPackage odfPackage) throws IOException {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        if (!odfPackage.hasManifest()) {
            return messageLog;
        }
        for (final FileEntry entry : odfPackage.getManifest().getEntries()) {
            if (entry.isEncrypted()) {
                messageLog.add(entry.getFullPath(), Messages.getMessageInstance(this.id, Message.Severity.ERROR, this.getName(),
                        this.getDescription()));
            }
        }
        return messageLog;
    }
}
