package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.fmt.Formats;

public interface ValidationResult {
    /**
     * Get the name of the document being validated.
     *
     * @return the name of the document being validated
     */
    public String getName();
    /**
     * Determine if the OpenDocument is encrypted.
     *
     * @return <code>true</code> if the OpenDocument is encrypted, otherwise <code>false</code>
     */
    public boolean isEncrypted();
    /**
     * Get the detected format of the OpenDocument.
     *
     * @return the detected format of the OpenDocument
     */
    Formats getDetectedFormat();
    /**
     * Determine if the OpenDocument is valid.
     *
     * @return <code>true</code> if the OpenDocument is valid, otherwise <code>false</code>
     */
    public boolean isValid();
    /**
     * Get all of the validation messages for the OpenDocument.
     *
     * @return a list of all of the validation messages for the OpenDocument
     */
    List<Message> getMessages();
    /**
     * Get all of the validation errors for the OpenDocument.
     *
     * @return a list of all of the validation errors for the OpenDocument
     */
    List<Message> getErrors();
    /**
     * Get the message log instance for the report
     *
     * @return the message log instance for the report
     */
    MessageLog getMessageLog();
}