package org.openpreservation.odf.xml;

import java.util.List;
import java.util.Map;

import org.openpreservation.odf.validation.ValidationResultImpl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface for retrieving any useful metadata from an ODF package meta.xml
 * file.
 */
@JsonDeserialize(as = MetadataImpl.class)
public interface Metadata {
    /**
     * Interface that handles basic user defined field information.
     */
    public interface UserDefinedField {
        /**
         * Get the name of the user defined field.
         *
         * @return The name of the user defined field.
         */
        public String getName();

        /**
         * The defined data type of the user defined field's value.
         *
         * @return
         */
        public String getValueType();

        /**
         * The String representation of the user defined field's value.
         *
         * @return the String value for the user defined field.
         */
        public String getValue();
    }

    /**
     * Return the office version of the Metadata data.
     *
     * @return the office version of the Metadata data, or null if not present
     */
    public String getVersion();

    /**
     * The qualified Name value Map of all of the qualifiedName:string pairs found
     * in the metadata block.
     *
     * @return The name value Map, may be an empty Map but never null
     */
    public Map<String, String> getStringValueMap();

    /**
     * Get the String value for the supplied qualified name.
     *
     * @param qualifiedName The qualified name of the value to retrieve
     * @return The String value for the supplied qualified name or null if no key
     *         exists
     */
    public String getStringValue(final String qualifiedName);

    /**
     * Look up the string value by prefix and local name.
     *
     * @param prefix    The prefix of the value to retrieve, usuall "meta" or "dc".
     * @param localName The local name of the value to retrieve.
     * @return The String value for the supplied prefix and local name or null if no
     *         key
     */
    public String getStringValue(final String prefix, final String localName);

    /**
     * Get the user defined fields from the metadata block
     *
     * @return a List of any user defined fields, which is empty if there are none.
     */
    public List<UserDefinedField> getUserDefinedFields();
}
