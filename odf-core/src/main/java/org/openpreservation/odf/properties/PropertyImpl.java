package org.openpreservation.odf.properties;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;

public class PropertyImpl implements Property {
    private static final MessageFactory FACTORY = Messages.getInstance();
    private final String name;
    private final String errorCode;

    private PropertyImpl(final String name, final String errorCode) {
        this.name = name;
        this.errorCode = errorCode;
    }

    public static PropertyImpl create(final String name, final String errorCode) {
        return new PropertyImpl(name, errorCode);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public Message getMessage() {
        return FACTORY.getError(this.errorCode);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
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
        PropertyImpl other = (PropertyImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (errorCode == null) {
            if (other.errorCode != null)
                return false;
        } else if (!errorCode.equals(other.errorCode))
            return false;
        return true;
    }

}
