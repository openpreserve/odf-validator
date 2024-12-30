package org.openpreservation.format.xml;

/**
 * Simple extension of {@link ParseResult} to indicate whether the result of XML
 * validation is valid or not.
 */
public interface XmlValidationResult extends ParseResult {
    /**
     * Is the XML document valid according to the supplied schema?
     * 
     * @return <code>true</code> if the result is for a valid XML document,
     *         otherwise <code>false</code>
     */
    public boolean isValid();
}
