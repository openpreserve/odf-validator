package org.openpreservation.messages;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MessageLog {
    public int size();
    public boolean isEmpty();
    public int add(final String path, final Message message);
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
