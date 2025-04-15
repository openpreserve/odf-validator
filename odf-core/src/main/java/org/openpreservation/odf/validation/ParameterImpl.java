package org.openpreservation.odf.validation;

public class ParameterImpl implements Parameter {
    private final String name;
    private final String value;

    public ParameterImpl(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

}
