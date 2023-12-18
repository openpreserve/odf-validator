package org.openpreservation.messages;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface for a log of {@link Message} objects.
 */
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
     * @param messages the <code>Collection<Message></code> of <code>Messages</code> to be added
     * @return the <code>int</code> number of messages in the log
     */
    public int add(final String path, final Collection<? extends Message> messages);

    public int add(final Map<String, List<Message>> messages);

    public Map<String, List<Message>> getErrors();

    public Map<String, List<Message>> getWarnings();

    public Map<String, List<Message>> getInfos();

    public Map<String, List<Message>> getMessages();

    public Map<String, List<Message>> getMessagesBySeverity(Message.Severity severity);

    public Map<String, List<Message>> getMessagesById(String id);

    public List<Message> getMessagesForPath(String path);

    public boolean hasErrors();

    public boolean hasWarnings();

    public boolean hasInfos();

    public int getErrorCount();

    public int getWarningCount();

    public int getInfoCount();
}
