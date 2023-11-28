package org.openpreservation.odf.validation;

import java.util.List;
import java.util.Map;

import org.openpreservation.messages.MessageLog;

public interface ProfileResult {
    public ValidationReport getValidationReport();
    public Map<String, MessageLog> getPackageMessageLogMap();
    public MessageLog getMessageLogForPath(final String path);
    public List<String> getLogPaths();
}
