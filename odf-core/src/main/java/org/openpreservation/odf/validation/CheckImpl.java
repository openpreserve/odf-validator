package org.openpreservation.odf.validation;

import org.openpreservation.odf.validation.messages.Message;

public class CheckImpl implements Check {
    private final Message message;
    private final String path;

    private CheckImpl(final Message message, final String path) {
        this.message = message;
        this.path = path;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String getPath() {
        return path;
    }

    public static Check of(final Message message, final String path) {
        return new CheckImpl(message, path);
    }
}
