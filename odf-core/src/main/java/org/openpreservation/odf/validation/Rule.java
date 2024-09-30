package org.openpreservation.odf.validation;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.OdfXmlDocument;

public interface Rule {
    public String getId();

    public String getName();

    public String getDescription();

    public Severity getSeverity();

    public boolean isPrerequisite();

    public MessageLog check(final OdfXmlDocument document) throws ParseException;

    public MessageLog check(final OdfPackage odfPackage) throws ParseException;

    public MessageLog check(final ValidationReport report) throws ParseException;
}
