package org.openpreservation.messages;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Interface for a log of {@link Message} objects.
 */
@JsonSerialize(as = MessageLogImpl.class)
public interface MessageLog {
    /**
     * Get the number of messages in the log.
     *
     * @return the <code>int</code> number of messages in the log
     */
    public int size();

    /**
     * Is the log empty?
     *
     * @return <code>true</code> if the log is empty, <code>false</code> otherwise
     */
    @JsonIgnore
    public boolean isEmpty();

    /**
     * Add a <code>Message</code> to the log for a particular path.
     *
     * @param path    the <code>String</code> path for the message
     * @param message the {@link Message} to be added
     * @return the <code>int</code> number of messages in the log
     */
    public int add(final String path, final Message message);

    /**
     * Add a {@link Collection} of <code>Messages</code> to the log for a particular path.
     *
     * @param path    the <code>String</code> path for the message
     * @param messages the <code>Collection&lt;Message&gt;</code> of <code>Messages</code> to be added
     * @return the <code>int</code> number of messages in the log
     */
    public int add(final String path, final Collection<? extends Message> messages);

    /**
     * Add a map of messages to the log.
     *
     * @param messages the <code>Map&lt;String, List&lt;Message&gt;&gt;</code> of messages to be added
     * @return the <code>int</code> number of messages in the log
     */
    public int add(final Map<String, List<Message>> messages);

    /**
     * Get all error messages in the log.
     *
     * @return a <code>Map&lt;String, List&lt;Message&gt;&gt;</code> of error messages
     */
    @JsonIgnore
    public Map<String, List<Message>> getErrors();

    /**
     * Get all warning messages in the log.
     *
     * @return a <code>Map&lt;String, List&lt;Message&gt;&gt;</code> of warning messages
     */
    @JsonIgnore
    public Map<String, List<Message>> getWarnings();

    /**
     * Get all info messages in the log.
     *
     * @return a <code>Map&lt;String, List&lt;Message&gt;&gt;</code> of info messages
     */
    @JsonIgnore
    public Map<String, List<Message>> getInfos();

    /**
     * Get all messages in the log.
     *
     * @return a <code>Map&lt;String, List&lt;Message&gt;&gt;</code> of all messages
     */
    public Map<String, List<Message>> getMessages();

    /**
     * Does the log contain any error messages?
     *
     * @return <code>true</code> if the log contains error messages, <code>false</code> otherwise
     */
    @JsonIgnore
    public boolean hasErrors();

    /**
     * Does the log contain any warning messages?
     *
     * @return <code>true</code> if the log contains warning messages, <code>false</code> otherwise
     */
    @JsonIgnore
    public boolean hasWarnings();

    /**
     * Does the log contain any info messages?
     *
     * @return <code>true</code> if the log contains info messages, <code>false</code> otherwise
     */
    @JsonIgnore
    public boolean hasInfos();

    /**
     * Does the log contain any fatal error messages?
     *
     * @return <code>true</code> if the log contains fatal messages, <code>false</code> otherwise
     */
    @JsonIgnore
    public boolean hasFatals();

    /**
     * Get the number of error messages in the log.
     *
     * @return the <code>int</code> number of error messages in the log
     */
    @JsonProperty("error_count")
    public int getErrorCount();

    /**
     * Get the number of warning messages in the log.
     *
     * @return the <code>int</code> number of warning messages in the log
     */
    @JsonProperty("warning_count")
    public int getWarningCount();

    /**
     * Get the number of info messages in the log.
     *
     * @return
     */
    @JsonProperty("info_count")
    public int getInfoCount();

    /**
     * Get all messages in the log of a particular severity.
     *
     * @param severity the {@link Message.Severity} of the messages to be retrieved
     * @return a <code>Map&lt;String, List&lt;Message&gt;&gt;</code> of messages of the specified severity
     */
    public Map<String, List<Message>> getMessagesBySeverity(final Message.Severity severity);

    /**
     * Get all messages in the log for a particular ID
     *
     * @param id the <code>String</code> ID of the messages to be retrieved
     * @return a <code>Map&lt;String, List&lt;Message&gt;&gt;</code> of messages for the specified ID
     */
    public Map<String, List<Message>> getMessagesById(final String id);

    /**
     * Get all messages in the log for a particular path
     *
     * @param path the <code>String</code> path of the messages to be retrieved
     * @return a <code>List&lt;Message&gt;</code> of messages for the specified path
     */
    public List<Message> getMessagesForPath(final String path);
}
