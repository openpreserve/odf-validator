package org.openpreservation.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

final class MessageLogImpl implements MessageLog {
    static final MessageLog of() {
        return new MessageLogImpl();
    }

    static final MessageLog of(final String path, final List<Message> messages) {
        final Map<String, List<Message>> messageMap = new HashMap<>();
        messageMap.put(path, messages);
        return new MessageLogImpl(messageMap);
    }

    private final Map<String, List<Message>> messageMap;

    private MessageLogImpl() {
        this(new HashMap<>());
    }

    private MessageLogImpl(final Map<String, List<Message>> messageMap) {
        super();
        this.messageMap = new HashMap<>(messageMap);
    }

    @Override
    public int size() {
        return this.messageMap.values().stream().mapToInt(List::size).sum();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public int add(final String path, final Message message) {
        messageMap.putIfAbsent(path, new ArrayList<>());
        this.messageMap.get(path).add(message);
        return this.size();
    }

    @Override
    public int add(final String path, final Collection<? extends Message> messages) {
        if (!messages.isEmpty()) {
            messageMap.putIfAbsent(path, new ArrayList<>());
            this.messageMap.get(path).addAll(messages);
        }
        return this.size();
    }

    @Override
    public Map<String, List<Message>> getErrors() {
        return getMessagesBySeverity(Message.Severity.ERROR);
    }

    @Override
    public Map<String, List<Message>> getWarnings() {
        return getMessagesBySeverity(Message.Severity.WARNING);
    }

    @Override
    public Map<String, List<Message>> getInfos() {
        return getMessagesBySeverity(Message.Severity.INFO);
    }

    @Override
    public Map<String, List<Message>> getMessagesBySeverity(final Message.Severity severity) {
        return this.messageMap.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey,
                        e -> e.getValue().stream().filter(m -> m.getSeverity().equals(severity))
                                .collect(Collectors.toUnmodifiableList())))
                .entrySet().stream().filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    public Map<String, List<Message>> getMessages() {
        return Collections.unmodifiableMap(this.messageMap);
    }

    @Override
    public Map<String, List<Message>> getMessagesById(final String id) {
        return this.messageMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey,
                e -> e.getValue().stream().filter(m -> m.getId().equals(id)).collect(Collectors.toUnmodifiableList())));
    }

    @Override
    public boolean hasFatals() {
        return containsSeverity(Message.Severity.FATAL);
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

    @Override
    public List<Message> getMessagesForPath(final String path) {
        return Collections.unmodifiableList(this.messageMap.getOrDefault(path, new ArrayList<>()));
    }

    private boolean containsSeverity(final Message.Severity severity) {
        return this.getMessagesBySeverity(severity).size() > 0;
    }

    @Override
    public int add(Map<String, List<Message>> messages) {
        messages.forEach(this::add);
        return this.size();
    }

    @Override
    public int getErrorCount() {
        return this.getErrors().values().stream().mapToInt(List::size).sum();
    }

    @Override
    public int getWarningCount() {
        return this.getWarnings().values().stream().mapToInt(List::size).sum();
    }

    @Override
    public int getInfoCount() {
        return this.getInfos().values().stream().mapToInt(List::size).sum();
    }
}
