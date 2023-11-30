package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.validation.ValidatingParser;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validators;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.xml.sax.SAXException;

class ValidPackageRule extends AbstractRule {
    private final ValidatingParser validatingParser = Validators.getValidatingParser();

    private ValidPackageRule(String id, String name, String description, final boolean isPrerequisite) throws ParserConfigurationException, SAXException {
        super(id, name, description, isPrerequisite);
    }

    @Override
    public MessageLog check(OdfXmlDocument document) {
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }

    @Override
    public MessageLog check(OdfPackage odfPackage) throws IOException {
        Objects.requireNonNull(odfPackage, "odfPackage must not be null");
        MessageLog messageLog = Messages.messageLogInstance();
        ValidationReport validationReport = validatingParser.validatePackage(odfPackage);
        if (!validationReport.isValid()) {
            messageLog.add(odfPackage.getName(), validationReport.getMessages());
            messageLog.add(odfPackage.getName(), Messages.getMessageInstance(this.id, Message.Severity.ERROR, this.getName(), this.getDescription()));
        }
        return messageLog;
    }

    static final ValidPackageRule getInstance() throws ParserConfigurationException, SAXException {
        return new ValidPackageRule("ODF_2", "Standard Compliance", "The file MUST comply with the standard \"OASIS Open Document Format for Office Applications (OpenDocument) v1.3\".", true);
    }
}
