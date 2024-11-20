package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OdfDocument;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;

final class ExtensionMimeTypeRule extends AbstractRule {

    static final ExtensionMimeTypeRule getInstance(final Severity severity) {
        return new ExtensionMimeTypeRule("POL_4", "Extension and MIME type",
                "The MIME type value MUST be: \"application/vnd.oasis.opendocument.spreadsheet\" and the file extension MUST be \".ods\"."
                        + //
                        "",
                severity, false);
    }

    private ExtensionMimeTypeRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super(id, name, description, severity, isPrerequisite);
    }

    @Override
    public MessageLog check(final OpenDocument document) {
        Objects.requireNonNull(document, "document must not be null");
        if (document.isPackage()) {
            return this.check(document.getPackage());
        }
        return this.check(document.getDocument());
    }

    public MessageLog check(final OdfDocument document) {
        Objects.requireNonNull(document, "document must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        messageLog.add(OdfPackages.MIMETYPE, Messages.getMessageInstance(this.id, this.severity, this.getName(),
                this.getDescription()));
        return messageLog;
    }

    public MessageLog check(final OdfPackage odfPackage) {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        if (!odfPackage.hasMimeEntry()
                || !"application/vnd.oasis.opendocument.spreadsheet".equals(odfPackage.getMimeType())
                || !odfPackage.getName().endsWith(".ods")) {
            messageLog.add(OdfPackages.MIMETYPE, Messages.getMessageInstance(this.id, this.severity, this.getName(),
                    this.getDescription()));
        }
        return messageLog;
    }
}
