package org.openpreservation.format.xml;

import java.net.URI;
import java.net.URL;

public interface Namespace {
    public URI getId();

    public String getPrefix();

    public URL getSchemalocation();
}
