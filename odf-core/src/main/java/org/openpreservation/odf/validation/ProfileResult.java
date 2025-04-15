package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageLog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize( as = ProfileResultImpl.class )
public interface ProfileResult {
    /**
     * Get the id of the profile
     *
     * @return the id of the profile
     */
    public String getFilename();

    /**
     * Get the name of the profile
     *
     * @return the name of the profile
     */
    public String getProfile();

    /**
     * Get the profile message log
     *
     * @see MessageLog
     * @return the MessageLog associated with the profile result
     */
    @JsonIgnore
    public MessageLog getMessageLog();

    /**
     * Get the profile message list
     *
     * @see Message
     * @return the Messages associated with the profile result
     */
    @JsonProperty("checks")
    public List<Check> getChecks();

    /**
     * Check if the profile is valid
     *
     * @return <code>true</code> if the profile is valid, <code>false</code> otherwise
     */
    public boolean isValid();
}
