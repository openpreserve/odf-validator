package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.util.Objects;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.xml.OdfXmlDocument;

final class SubDocumentRule extends AbstractRule {

    private SubDocumentRule(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public MessageLog check(OdfXmlDocument document) {
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }

    @Override
    public MessageLog check(OdfPackage odfPackage) throws IOException {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        MessageLog messageLog = Messages.messageLogInstance();
        if (odfPackage.hasManifest() && odfPackage.getManifest().getDocumentEntries().size() > 1) {
            messageLog.add(Messages.getMessageInstance(this.id, Message.Severity.ERROR, this.getName(),
                    this.getDescription()));
        }
        return messageLog;
    }

    static final SubDocumentRule getInstance() {
        return new SubDocumentRule("ODF_10", "Sub Documents",
                "The package MUST NOT contain sub documents.");
    }
}
