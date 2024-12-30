package org.openpreservation.odf.validation;

import org.openpreservation.messages.MessageLog;

public interface ProfileResult {
    public String getId();

    public String getName();

    public ValidationResult getValidationReport();

    public MessageLog getMessageLog();

    public boolean isValid();
}
