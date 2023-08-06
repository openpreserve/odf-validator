package org.openpreservation.odf.properties;

import org.openpreservation.messages.Message;

public interface Property {
    public String getName();
    public String getErrorCode();
    public Message getMessage();
}
