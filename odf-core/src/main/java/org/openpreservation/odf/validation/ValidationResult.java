package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.validation.messages.MessageLog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ValidationResultImpl.class)
public interface ValidationResult {
    /**
     * Get the name of the document being validated.
     *
     * @return the name of the document being validated
     */
    public String getFilename();
    /**
     * Determine if the OpenDocument is encrypted.
     *
     * @return <code>true</code> if the OpenDocument is encrypted, otherwise <code>false</code>
     */
    @JsonProperty("is_encrypted")
    public boolean isEncrypted();
    /**
     * Get the detected format of the OpenDocument.
     *
     * @return the detected format of the OpenDocument
     */
    @JsonProperty("detected_format")
    Formats getDetectedFormat();
    /**
     * Determine if the OpenDocument is valid.
     *
     * @return <code>true</code> if the OpenDocument is valid, otherwise <code>false</code>
     */
    @JsonProperty
    public boolean isValid();
    /**
     * Get all of the validation messages for the OpenDocument.
     *
     * @return a list of all of the validation messages for the OpenDocument
     */
    @JsonProperty("checks")
    List<Check> getChecks();

    /**
     * Get the profile message log
     *
     * @see MessageLog
     * @return the MessageLog associated with the profile result
     */
    @JsonIgnore
    public MessageLog getMessageLog();
}