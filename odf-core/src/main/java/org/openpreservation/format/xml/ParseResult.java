package org.openpreservation.format.xml;

import java.util.List;

import org.openpreservation.messages.Message;

public interface ParseResult {
    /**
     * Is the XML document well formed?
     *
     * @return true if the result is for a well formed XML document, otherwise false
     */
    public boolean isWellFormed();

    /**
     * Get the namespace associated with the document's root element
     *
     * @return the Namespace of the document's root element
     */
    public Namespace getRootNamespace();

    /**
     * Get all of the declared namespaces in the document
     *
     * @return the Namespace of the document's root element
     */
    public List<Namespace> getNamespaces();

    /**
     * Get the root element namespace prefix
     *
     * @return The String namesapce prefix of the document's root element, or empty
     *         String if there is no root element NS prefix
     */
    public String getRootPrefix();

    /**
     * Get the unqualified name of the document's root element
     *
     * @return The String name of the document's root element, or empty String if
     *         there is no root element name
     */
    public String getRootName();

    /**
     * Establish whether the document's root element name matches the supplied name
     *
     * @param name the name to compare with the document's root element name
     * @return true if the document's root element name matches the supplied name,
     *         otherwise false
     */
    public boolean isRootName(final String name);

    /**
     * Get the root element attributes
     *
     * @return the root element attributes
     */
    public List<Attribute> getRootAttributes();

    /**
     * Get the value of the root element attribute with the supplied name
     *
     * @param name the name of the attribute
     * @return the value of the attribute, or null if the attribute does not exist
     */
    public String getRootAttributeValue(final String qName);

    /**
     * The list of document parsing messages, which may be empty
     *
     * @return the list of document parsing messages
     */
    public List<Message> getMessages();
}
