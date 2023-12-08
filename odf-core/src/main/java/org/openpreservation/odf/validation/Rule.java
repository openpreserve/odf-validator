package org.openpreservation.odf.validation;

import java.io.IOException;

import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.xml.OdfXmlDocument;

public interface Rule {
    public String getId();
    public String getName();
    public String getDescription();
    public Severity getSeverity();
    public boolean isPrerequisite();
    public MessageLog check(final OdfXmlDocument document) throws IOException;
    public MessageLog check(final OdfPackage odfPackage) throws IOException;
}
