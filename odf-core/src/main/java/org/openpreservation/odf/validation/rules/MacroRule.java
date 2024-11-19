package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.xml.sax.SAXException;

final class MacroRule extends AbstractRule {
    static final String ODF8_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-8.sch";
    static final String NS_SCRIPTS = "http://openoffice.org/2000/script";

    static final MacroRule getInstance(final Severity severity) {
        return new MacroRule("POL_8", "Macros check",
                "The file MUST NOT contain any macros.", severity, false);
    }

    final SchematronRule schematron;

    private MacroRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super(id, name, description, severity, isPrerequisite);
        this.schematron = SchematronRule.getInstance(id, name,
                description, severity, isPrerequisite,
                ClassLoader.getSystemResource(ODF8_SCHEMATRON));
    }

    @Override
    public MessageLog check(final OdfXmlDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        return this.schematron.check(document);
    }

    @Override
    public MessageLog check(final OdfPackage odfPackage) throws ParseException {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        MessageLog messageLog;
        try {
            messageLog = checkOdfScriptXml(odfPackage);
        } catch (IOException e) {
            throw new ParseException("IOException when checking for macros.", e);
        }
        messageLog.add(schematron.check(odfPackage).getMessages());
        return messageLog;
    }

    private MessageLog checkOdfScriptXml(final OdfPackage odfPackage) throws IOException {
        MessageLog messageLog = Messages.messageLogInstance();
        XmlParser checker;
        try {
            checker = new XmlParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException(e);
        }
        for (FileEntry entry : odfPackage.getXmlEntries()) {
            if (entry.isEncrypted()) {
                continue;
            }
            try (final InputStream entryStream = odfPackage.getEntryStream(entry)) {
                if (entryStream == null) {
                    continue;
                }
                ParseResult result = checker.parse(odfPackage.getEntryStream(entry));
                if (NS_SCRIPTS.equals(result.getRootNamespace().getId().toASCIIString())
                        && "module".equals(result.getRootName())) {
                    messageLog.add(entry.getFullPath(), Messages.getMessageInstance(id, severity, name, description));

                }
            }
        }
        return messageLog;
    }
}
