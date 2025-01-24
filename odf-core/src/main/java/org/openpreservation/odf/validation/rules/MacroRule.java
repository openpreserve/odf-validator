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
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.xml.sax.SAXException;

final class MacroRule extends AbstractRule {
    static final String ODF8_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-8.sch";
    static final String NS_SCRIPTS = "http://openoffice.org/2000/script";

    static final MacroRule getInstance(final Severity severity) {
        return new MacroRule("POL-8", "Macros check",
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
    public MessageLog check(final OpenDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        if (document.isPackage()) {
            return this.check(document.getPackage());
        }
        if (document.getDocument() == null) {
            return Messages.messageLogInstance();
        }
        MessageLog messageLog = checkOdfScriptParseResult(document.getPath().toString(),
                document.getDocument().getXmlDocument().getParseResult());
        messageLog.add(this.schematron.check(document).getMessages());
        return messageLog;
    }

    private MessageLog check(final OdfPackage odfPackage) throws ParseException {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        MessageLog messageLog = checkOdfScriptXml(odfPackage);
        messageLog.add(schematron.check(odfPackage).getMessages());
        return messageLog;
    }

    private MessageLog checkOdfScriptXml(final OdfPackage odfPackage) {
        MessageLog messageLog = Messages.messageLogInstance();
        for (FileEntry entry : odfPackage.getXmlEntries()) {
            final ParseResult result = getParseResult(odfPackage, entry);
            if (result != null) {
                messageLog.add(checkOdfScriptParseResult(entry.getFullPath(), result).getMessages());
            }
        }
        return messageLog;
    }

    private ParseResult getParseResult(final OdfPackage odfPackage, final FileEntry entry) {
        if (entry.isEncrypted()) {
            return null;
        }
        ParseResult result = odfPackage.getEntryXmlParseResult(entry.getFullPath());
        if (result == null) {
            try (final InputStream entryStream = odfPackage.getEntryStream(entry)) {
                if (entryStream == null) {
                    return null;
                }
                result = new XmlParser().parse(entryStream);
            } catch (IOException | ParserConfigurationException | SAXException e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }

    private MessageLog checkOdfScriptParseResult(final String fullPath, final ParseResult result) {
        MessageLog messageLog = Messages.messageLogInstance();
        if (result == null) {
            return messageLog;
        }
        if (NS_SCRIPTS.equals(result.getRootNamespace().getId().toASCIIString())
                && "module".equals(result.getRootName())) {
            messageLog.add(fullPath, Messages.getMessageInstance(id, severity, name, description));
        }
        return messageLog;

    }
}
