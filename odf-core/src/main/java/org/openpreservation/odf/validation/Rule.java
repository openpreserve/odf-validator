package org.openpreservation.odf.validation;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Message.Severity;

public interface Rule {
    public String getId();

    public String getName();

    public String getDescription();

    public Severity getSeverity();

    public boolean isPrerequisite();

    public MessageLog check(final OpenDocument document) throws ParseException;
}
