package org.openpreservation.odf.validation.rules;

import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.validation.ProfileResult;
import org.openpreservation.odf.validation.ValidationResult;

final class ProfileResultImpl implements ProfileResult {
    private final String id;
    private final String name;
    private final ValidationResult validationReport;
    private final MessageLog messageLog;

    private ProfileResultImpl(final String id, final String name, final ValidationResult validationReport, final MessageLog messageLog) {
        super();
        this.id = id;
        this.name = name;
        this.validationReport = validationReport;
        this.messageLog = messageLog;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ValidationResult getValidationResult() {
        return this.validationReport;
    }

    @Override
    public MessageLog getMessageLog() {
        return this.messageLog;
    }

    @Override
    public boolean isValid() {
        return !this.getMessageLog().hasErrors();
    }

    static final ProfileResultImpl of(final String id, final String name, final ValidationResult validationReport, final MessageLog messageLog) {
        return new ProfileResultImpl(id, name, validationReport, messageLog);
    }
}
