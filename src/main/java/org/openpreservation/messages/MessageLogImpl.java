package org.openpreservation.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class MessageLogImpl implements MessageLog {
    private final List<Message> messages = new ArrayList<>();
    @Override
    public int size() {
        return this.messages.size();
    }

    @Override
    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

    @Override
    public int add(Message message) {
        this.messages.add(message);
        return this.size();
    }

    @Override
    public int add(Collection<? extends Message> messages) {
        this.messages.addAll(messages);
        return this.size();
    }

    @Override
    public List<Message> getErrors() {
        return getMessages(Message.Severity.ERROR);
    }

    @Override
    public List<Message> getWarnings() {
        return getMessages(Message.Severity.WARNING);
    }

    @Override
    public List<Message> getInfos() {
        return getMessages(Message.Severity.INFO);
    }

    @Override
    public List<Message> getMessages(Message.Severity severity) {
        List<Message> filteredList = new ArrayList<>();
        for (Message message : this.messages) {
            if (message.getSeverity() == severity) {
                filteredList.add(message);
            }
        }
        return Collections.unmodifiableList(filteredList);
    }

    @Override
    public List<Message> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }

    @Override
    public List<Message> getMessages(String id) {
        List<Message> filteredList = new ArrayList<>();
        for (Message message : this.messages) {
            if (message.getId().equals(id)) {
                filteredList.add(message);
            }
        }
        return Collections.unmodifiableList(filteredList);
    }

    @Override
    public boolean hasErrors() {
        return containsSeverity(Message.Severity.ERROR);
    }

    @Override
    public boolean hasWarnings() {
        return containsSeverity(Message.Severity.WARNING);
    }

    @Override
    public boolean hasInfos() {
        return containsSeverity(Message.Severity.INFO);
    }

    private boolean containsSeverity(Message.Severity severity) {
        for (Message message : this.messages) {
            if (message.getSeverity() == severity) {
                return true;
            }
        }
        return false;
    }
}
