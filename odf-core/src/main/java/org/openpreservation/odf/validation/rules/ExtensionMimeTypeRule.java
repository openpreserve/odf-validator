package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.util.Objects;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.OdfXmlDocument;

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
    public MessageLog check(final OdfXmlDocument document) {
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }

    @Override
    public MessageLog check(final OdfPackage odfPackage) throws ParseException {
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
