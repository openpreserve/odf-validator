package org.openpreservation.odf.validation.rules;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Message.Severity;

import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;

final class SchematronRule extends AbstractRule {
    static final SchematronRule getInstance(final String id, final String name, final String description,
            final Severity severity, final boolean isPrerequisite, final URL schematronLoc) {
        return new SchematronRule(id, name, description, severity, isPrerequisite, schematronLoc);
    }

    final SchematronResourcePure schematron;

    private SchematronRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite, final URL schematronLoc) {
        super(id, name, description, severity, isPrerequisite);
        Objects.requireNonNull(schematronLoc, "schematronLoc must not be null");
        this.schematron = SchematronResourcePure.fromURL(schematronLoc);
    }

    @Override
    public MessageLog check(final OpenDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        if (document.isPackage()) {
            return this.check(document.getPackage());
        }
        try (final InputStream is = new FileInputStream(document.getPath().toFile())) {
            return check(document.getPath().toString(), is);
        } catch (IOException e) {
            throw new ParseException("Unexpected Exception caught when executing Schematron checks.", e);
        }
    }

    public MessageLog check(final OdfPackage odfPackage) throws ParseException {
        final MessageLog messageLog = Messages.messageLogInstance();
        for (final FileEntry entry : odfPackage.getXmlEntries()) {
            if (!OdfPackages.isOdfXml(entry.getFullPath()) || entry.isEncrypted()) {
                continue;
            }
            try (InputStream is = odfPackage.getEntryStream(entry)) {
                messageLog.add(check(entry.getFullPath(), is).getMessages());
            } catch (IOException e) {
                // No need to catch stream close exception
            }
        }
        return messageLog;
    }

    private MessageLog check(final String entryName, final InputStream is) throws ParseException {
        SchematronOutputType schResult;
        Source source = new StreamSource(is);
        try {
            schResult = this.schematron
                    .applySchematronValidationToSVRL(source);
        } catch (IllegalArgumentException e) {
            // Ignore unparsable schematron
            return Messages.messageLogInstance();
        } catch (Exception e) {
            throw new ParseException("Unexpected Exception caught when executing Schematron checks.", e);
        }
        final MessageLog messageLog = Messages.messageLogInstance();
        for (final AbstractSVRLMessage result : SVRLHelper
                .getAllFailedAssertionsAndSuccessfulReports(schResult)) {
            messageLog.add(entryName,
                    Messages.getMessageInstance(this.id, Message.Severity.valueOf(result.getRole()),
                            this.getName(),
                            result.getText()));
        }
        return messageLog;
    }
}
