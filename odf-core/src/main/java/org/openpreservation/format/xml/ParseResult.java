package org.openpreservation.format.xml;

import java.util.List;
import java.util.Set;

import org.openpreservation.messages.Message;

/**
 * An interface defining the properties of XML document parsing results.
 * 
 * A <code>ParseResult</code> comprises various high level properties of the
 * parsed XML document, such as the root element name, namespace, etc.
 */
public interface ParseResult {
    /**
     * Is the XML document well formed?
     *
     * @return <code>true</code> if the result is for a well formed XML document,
     *         otherwise <code>false</code>
     */
    public boolean isWellFormed();

    /**
     * Get the {@link Namespace} associated with the document's root element
     *
     * @return the <code>Namespace</code> of the document's root element
     */
    public Namespace getRootNamespace();

    /**
     * Get all of the declared <code>Namespace</code>s in the document
     *
     * @return the <code>List<Namespace></code> of the document's namespaces
     */
    public Set<Namespace> getDeclaredNamespaces();

    /**
     * Get the root element namespace prefix
     *
     * @return The <code>String</code> namesapce prefix of the document's root
     *         element, or an empty
     *         <code>String</code> if there is no root element NS prefix
     */
    public String getRootPrefix();

    /**
     * Get the unqualified name of the document's root element
     *
     * @return The <code>String</code> unqualified name of the document's root
     *         element, or an empty <code>String</code> if
     *         there is no root element name
     */
    public String getRootName();

    /**
     * Establish whether the document's root element name matches the supplied name
     *
     * @param name the <code>String</code> name to compare with the document's root
     *             element name
     * @return <code>true</code> if the document's root element name matches the
     *         supplied <code>name</code>,
     *         otherwise <code>false</code>
     */
    public boolean isRootName(final String name);

    /**
     * Get the root element {@link Attribute}s
     *
     * @return the <code>List</code> root element <code>Attribute</code>s
     */
    public List<Attribute> getRootAttributes();

    /**
     * Get the value of the root element attribute with the supplied qualified name.
     *
     * @param qName the <code>String</code> qualified name of the
     *              <code>Attribute<code> that the value is required of.
     * @return the value of the <code>Attribute<code>, or <code>null</code> if the
     *              <code>Attribute</code> does not exist
     */
    public String getRootAttributeValue(final String qName);

    /**
     * The <code>List</code> of {@link Message} instances generated when parsing the
     * document, which may be empty
     *
     * @return the <code>List<Message></code> of document parsing messages
     */
    public List<Message> getMessages();
}
