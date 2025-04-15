package org.openpreservation.odf.validation.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ParameterImpl implements Parameter {
    static class ParameterListImpl implements ParameterList {
        static final ParameterList of(final List<Parameter> parameters) {
            return new ParameterListImpl(parameters);
        }

        @Override
        public String toString() {
            return "ParameterListImpl [parameters=" + parameters + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ParameterListImpl other = (ParameterListImpl) obj;
            if (parameters == null) {
                if (other.parameters != null)
                    return false;
            } else if (!parameters.equals(other.parameters))
                return false;
            return true;
        }

        private final List<Parameter> parameters;

        private ParameterListImpl() {
            this(new ArrayList<>());
        }

        private ParameterListImpl(final List<Parameter> parameters) {
            super();
            this.parameters = parameters;
        }

        @Override
        public ParameterList add(final String name, final String value) {
            this.parameters.add(ParameterImpl.of(name, value));
            return this;
        }

        @Override
        public int size() {
            return this.parameters.size();
        }

        @Override
        public Parameter[] toArray() {
            return this.parameters.toArray(new Parameter[0]);
        }

        @Override
        public List<Parameter> toList() {
            return Collections.unmodifiableList(this.parameters);
        }

        @Override
        public ParameterList clear() {
            this.parameters.clear();
            return this;
        }
    }
    public static final Parameter of(final String name, final String value) {
        return new ParameterImpl(name, value);
    }

    private final String name;

    private final String value;

    private ParameterImpl(final String name, final String value) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ParameterImpl other = (ParameterImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ParameterImpl [name=" + name + ", value=" + value + "]";
    }
}
