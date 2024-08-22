package org.openpreservation.odf.validation.rules;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import javax.xml.transform.stream.StreamSource;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.OdfXmlDocument;

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
    public MessageLog check(final OdfXmlDocument document) throws ParseException {
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }

    @Override
    public MessageLog check(final OdfPackage odfPackage) throws ParseException {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        final MessageLog messageLog = Messages.messageLogInstance();
        for (final FileEntry entry : odfPackage.getXmlEntries()) {
            if (!OdfPackages.isOdfXml(entry.getFullPath())) {
                continue;
            }
            try (InputStream is = odfPackage.getEntryStream(entry)) {
                final SchematronOutputType schResult = this.schematron
                        .applySchematronValidationToSVRL(new StreamSource(is));
                for (final AbstractSVRLMessage result : SVRLHelper
                        .getAllFailedAssertionsAndSuccessfulReports(schResult)) {
                    messageLog.add(entry.getFullPath(),
                            Messages.getMessageInstance(this.id, Message.Severity.valueOf(result.getRole()),
                                    this.getName(),
                                    result.getText()));
                }
            } catch (final Exception e) {
                throw new ParseException("Unexpected Exception caught when executing Schematron checks.", e);
            }
        }
        return messageLog;
    }
}
