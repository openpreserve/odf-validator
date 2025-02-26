package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.validation.messages.MessageLog;

final class ProfileResultImpl implements ProfileResult {
    private final String filename;
    private final String profile;
    private final MessageLog messageLog;

    private ProfileResultImpl(final String filename, final String profile, final MessageLog messageLog) {
        super();
        this.filename = filename;
        this.profile = profile;
        this.messageLog = messageLog;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public String getProfile() {
        return this.profile;
    }

    @Override
    public MessageLog getMessageLog() {
        return this.messageLog;
    }

    @Override
    public List<Check> getChecks() {
        return this.messageLog.getChecks();
    }

    @Override
    public boolean isValid() {
        return !this.getMessageLog().hasErrors();
    }

    static final ProfileResult of(final String filename, final String profile, final MessageLog messageLog) {
        return new ProfileResultImpl(filename, profile, messageLog);
    }
}
