package org.openpreservation.format.xml;

import java.net.URI;

/**
 * An interface defining the behaviour of XML attributes.
 */
public interface Attribute {
    /**
     * Get the index of the <code>Attribute</code> in the element
     * <code>Array</code>.
     * 
     * @return the <code>int</code> index of the <code>Attribute</code>
     */
    public int getIndex();

    /**
     * Get the qualified name of the <code>Attribute</code>.
     * 
     * @return the <code>String</code> qualified name of the <code>Attribute</code>
     */
    public String getQualifiedName();

    /**
     * Get the <code>String</code> value of the <code>Attribute</code>.
     * 
     * @return the <code>String</code> value of the <code>Attribute</code>
     */
    public String getValue();

    /**
     * Get the <code>String</code> local name of the <code>Attribute</code>.
     * 
     * @return the <code>String</code> local name of the <code>Attribute</code>
     */
    public String getLocalName();

    /**
     * Get the <code>String</code> namespace prefix of the <code>Attribute</code>.
     * 
     * @return the <code>String</code> namespace prefix of the
     *         <code>Attribute</code>
     */
    public String getPrefix();

    /**
     * Get the <code>String</code> namespace URI of the <code>Attribute</code>.
     * 
     * @return the <code>String</code> namespace URI of the <code>Attribute</code>
     */
    public URI getURI();

    /**
     * Get the <code>String</code> type of the <code>Attribute</code>.
     * 
     * @return the <code>String</code> type of the <code>Attribute</code>
     */
    public String getType();
}
