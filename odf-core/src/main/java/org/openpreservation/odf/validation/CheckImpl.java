package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.validation.messages.Message;

public class CheckImpl implements Check {
    private final Message message;
    private final String path;
    private final List<Parameter> parameters;

    public CheckImpl(final Message message, final String path, final List<Parameter> parameters) {
        this.message = message;
        this.path = path;
        this.parameters = parameters;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

}
