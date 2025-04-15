package org.openpreservation.odf.validation.messages;

import java.util.List;

public interface Parameter {
    public String getName();

    public String getValue();

    public interface ParameterList {
        public ParameterList add(String name, String value);

        public Parameter[] toArray();

        public List<Parameter> toList();

        public ParameterList clear();

        public int size();
    }
}
