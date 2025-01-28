package org.openpreservation.odf.validation;

import org.openpreservation.messages.MessageLog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize( as = ProfileResultImpl.class )
public interface ProfileResult {
    /**
     * Get the id of the profile
     *
     * @return the id of the profile
     */
    public String getId();

    /**
     * Get the name of the profile
     *
     * @return the name of the profile
     */
    public String getName();

    /**
     * Get the profile message log
     *
     * @see MessageLog
     * @return the MessageLog associated with the profile
     */
    @JsonProperty("messages")
    public MessageLog getMessageLog();

    /**
     * Check if the profile is valid
     *
     * @return <code>true</code> if the profile is valid, <code>false</code> otherwise
     */
    public boolean isValid();
}
