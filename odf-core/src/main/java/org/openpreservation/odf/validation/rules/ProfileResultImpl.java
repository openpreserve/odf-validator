package org.openpreservation.odf.validation.rules;

import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.validation.ProfileResult;
import org.openpreservation.odf.validation.ValidationReport;

final class ProfileResultImpl implements ProfileResult {
    private final String id;
    private final ValidationReport validationReport;
    private final MessageLog profileMessages;

    private ProfileResultImpl(final String id, final ValidationReport validationReport, final MessageLog profileMessages) {
        super();
        this.id = id;
        this.validationReport = validationReport;
        this.profileMessages = profileMessages;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public ValidationReport getValidationReport() {
        return this.validationReport;
    }

    @Override
    public MessageLog getProfileMessages() {
        return this.profileMessages;
    }

    @Override
    public boolean isValid() {
        return !this.getProfileMessages().hasErrors();
    }

    static final ProfileResultImpl of(final String id, final ValidationReport validationReport, final MessageLog profileMessages) {
        return new ProfileResultImpl(id, validationReport, profileMessages);
    }
}
