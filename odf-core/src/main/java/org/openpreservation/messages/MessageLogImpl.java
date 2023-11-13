package org.openpreservation.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

final class MessageLogImpl implements MessageLog {
    private final List<Message> messages;

    private MessageLogImpl() {
        this(new ArrayList<>());
    }

    private MessageLogImpl(final List<Message> messages) {
        super();
        this.messages = new ArrayList<>(messages);
    }

    static final MessageLog of() {
        return new MessageLogImpl();
    }

    static final MessageLog of(final List<Message> messages) {
        return new MessageLogImpl(messages);
    }
    
    @Override
    public int size() {
        return this.messages.size();
    }

    @Override
    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

    @Override
    public int add(final Message message) {
        this.messages.add(message);
        return this.size();
    }

    @Override
    public int add(final Collection<? extends Message> messages) {
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
    public List<Message> getMessages(final Message.Severity severity) {
        return this.messages.stream().filter(m -> m.getSeverity() == severity).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Message> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }

    @Override
    public List<Message> getMessages(final String id) {
        return this.messages.stream().filter(m -> m.getId().equals(id)).collect(Collectors.toUnmodifiableList());
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

    private boolean containsSeverity(final Message.Severity severity) {
        return this.messages.stream().anyMatch((m -> m.getSeverity() == severity));
    }
}
