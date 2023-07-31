package org.openpreservation.format.xml;

import java.net.URI;

public interface Attribute {
    public int getIndex();
    public String getQualifiedName();
    public String getValue();
    public String getLocalName();
    public String getPrefix();
    public URI getURI();
    public String getType();
}
