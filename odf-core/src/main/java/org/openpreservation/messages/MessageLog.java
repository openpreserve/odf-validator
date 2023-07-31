package org.openpreservation.messages;

import java.util.Collection;
import java.util.List;

public interface MessageLog {
    public int size();
    public boolean isEmpty();
    public int add(final Message message);
    public int add(final Collection<? extends Message> messages);
    public List<Message> getErrors();
    public List<Message> getWarnings();
    public List<Message> getInfos();
    public List<Message> getMessages();
    public List<Message> getMessages(Message.Severity severity);
    public List<Message> getMessages(String id);
    public boolean hasErrors();
    public boolean hasWarnings();
    public boolean hasInfos();
}
