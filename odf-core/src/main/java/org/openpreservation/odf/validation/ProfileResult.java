package org.openpreservation.odf.validation;

import org.openpreservation.messages.MessageLog;

public interface ProfileResult {
    public String getId();
    public ValidationReport getValidationReport();
    public MessageLog getProfileMessages();
    public boolean isValid();
}
