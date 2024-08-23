package org.openpreservation.odf.xml;

import java.util.Set;

import org.openpreservation.format.xml.Namespace;
import org.openpreservation.format.xml.ParseResult;

public interface OdfXmlDocument {
    /**
     * Get the default namespace associated with the root element of the document
     *
     * @return the root element namespace
     */
    public Namespace getRootNamespace();

    /**
     * Get the name of the local/unqualified of the document's root element.
     *
     * @return the unqualified name of the root element
     */
    public String getLocalRootName();

    /**
     * Get the namespace prefixed/qualfied name of the document's root element.
     *
     * @return the unqualified name of the root element
     */
    public String getRootName();

    /**
     * Does the document declare a MIME type?
     *
     * @return true if the document declares a MIME type, otherwise false
     */
    public boolean hasMimeType();

    /**
     * Get the declared ODF MIME type of the document, parsed from a root element
     * attribute
     *
     * @return the value of the mimetype attribute or null if no attribute found.
     */
    public String getMimeType();

    /**
     * Does the document declare a version?
     *
     * @return true if the document declares a version, otherwise false
     */
    public boolean hasVersion();

    /**
     * Get the ODF version of the XML document, parsed from a root element attribute
     *
     * @return The value of the version attribute or null if no attribute found
     */
    public String getVersion();

    /**
     * Establish whether the document uses a namespace outside of the declared ODF namespaces.
     *
     * @return true if the document uses a namespace outside of the declared ODF namespaces
     */
    public boolean isExtended();
    /**
     * Get the parse result of the XML document
     *
     * @return the parse result of the XML document
     */
    public ParseResult getParseResult();
}
