package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.validation.messages.Message;

/**
 * Interface for a check, the application of a validation rule and message to an ODF document.
 */
public interface Check {
    /**
     * Get the message associated with the check.
     *
     * @return the message
     */
    public Message getMessage();

    /**
     * Get the path to the element in the ODF document that the check applies to.
     *
     * @return the path
     */
    public String getPath();

    /**
     * Get the parameters associated with the check.
     *
     * @return the parameters
     */
    public List<Parameter> getParameters();
}
