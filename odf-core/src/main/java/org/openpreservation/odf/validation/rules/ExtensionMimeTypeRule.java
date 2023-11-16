package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.util.Objects;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.xml.OdfXmlDocument;

final class ExtensionMimeTypeRule extends AbstractRule {

    private ExtensionMimeTypeRule(String id, String name, String description) {
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
        if (!odfPackage.hasMimeEntry()
                || !"application/vnd.oasis.opendocument.spreadsheet".equals(odfPackage.getMimeType())
                || !odfPackage.getName().endsWith(".ods")) {
            messageLog.add(Messages.getMessageInstance(this.id, Message.Severity.ERROR, this.getName(),
                    this.getDescription()));
        }
        return messageLog;
    }

    static final ExtensionMimeTypeRule getInstance() {
        return new ExtensionMimeTypeRule("ODF_4", "Extension and MIME type",
                "The MIME type value MUST be: \"application/vnd.oasis.opendocument.spreadsheet\" and the file extension MUST be \".ods\"."
                        + //
                        "");
    }
}
