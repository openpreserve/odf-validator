package org.openpreservation.odf.validation;

import org.openpreservation.messages.MessageLog;

final class ProfileResultImpl implements ProfileResult {
    private final String id;
    private final String name;
    private final MessageLog messageLog;

    private ProfileResultImpl(final String id, final String name, final MessageLog messageLog) {
        super();
        this.id = id;
        this.name = name;
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
    public MessageLog getMessageLog() {
        return this.messageLog;
    }

    @Override
    public boolean isValid() {
        return !this.getMessageLog().hasErrors();
    }

    static final ProfileResult of(final String id, final String name, final MessageLog messageLog) {
        return new ProfileResultImpl(id, name, messageLog);
    }
}
