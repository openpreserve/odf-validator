package org.openpreservation.odf.validation;

import org.openpreservation.messages.MessageLog;

public interface ProfileResult {
    public String getId();

    public String getName();

    public ValidationResult getValidationResult();

    public MessageLog getMessageLog();

    public boolean isValid();
}
