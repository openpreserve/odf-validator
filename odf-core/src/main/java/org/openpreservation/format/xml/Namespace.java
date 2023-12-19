package org.openpreservation.format.xml;

import java.net.URI;
import java.net.URL;

/**
 * An interface defining the properties of XML namespaces.
 */
public interface Namespace {
    /**
     * Get the <code>URI</code> id of the <code>Namespace</code>.
     * 
     * @return the <code>URI</code> id of the <code>Namespace</code>
     */
    public URI getId();

    /**
     * Get the <code>String</code> prefix of the <code>Namespace</code>.
     * 
     * @return the <code>String</code> prefix of the <code>Namespace</code>
     */
    public String getPrefix();

    /**
     * Get the <code>String</code> location of the <code>Namespace</code>.
     * 
     * @return the <code>String</code> location of the <code>Namespace</code>
     */
    public URL getSchemalocation();
}
