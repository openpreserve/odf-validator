package org.openpreservation.odf.validation.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openpreservation.odf.validation.Check;
import org.openpreservation.odf.validation.CheckImpl;

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
    public List<Check> getErrors() {
        return getChecksBySeverity(Message.Severity.ERROR);
    }

    @Override
    public List<Check> getWarnings() {
        return getChecksBySeverity(Message.Severity.WARNING);
    }

    @Override
    public List<Check> getInfos() {
        return getChecksBySeverity(Message.Severity.INFO);
    }

    @Override
    public List<Check> getChecksBySeverity(final Message.Severity severity) {
        final List<Check> checks = new ArrayList<>();
        for (final Entry<String, List<Message>> entry : this.messageMap.entrySet()) {
            for (final Message message : entry.getValue()) {
                if (message.getSeverity().equals(severity)) {
                    checks.add(CheckImpl.of(message, entry.getKey()));
                }
            }
        }
        return checks;
    }

    @Override
    public List<Check> getChecks() {
        final List<Check> checks = new ArrayList<>();
        for (final Entry<String, List<Message>> entry : this.messageMap.entrySet()) {
            for (final Message message : entry.getValue()) {
                checks.add(CheckImpl.of(message, entry.getKey()));
            }
        }
        return checks;
    }

    @Override
    public List<Check> getChecksById(final String id) {
        final List<Check> checks = new ArrayList<>();
        for (final Entry<String, List<Message>> entry : this.messageMap.entrySet()) {
            for (final Message message : entry.getValue()) {
                if (message.getId().equals(id)) {
                    checks.add(CheckImpl.of(message, entry.getKey()));
                }
            }
        }
        return checks;
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
    public List<Check> getChecksForPath(final String path) {
        final List<Check> checks = new ArrayList<>();
        for (final Message message : this.messageMap.getOrDefault(path, Collections.emptyList())) {
            checks.add(CheckImpl.of(message, path));
        }
        return checks;
    }

    @Override
    public Map<String, List<Message>> getMessages() {
        return this.messageMap;
    }

    private boolean containsSeverity(final Message.Severity severity) {
        return this.getChecksBySeverity(severity).size() > 0;
    }

    @Override
    public int add(Map<String, List<Message>> messages) {
        messages.forEach(this::add);
        return this.size();
    }

    @Override
    public int getErrorCount() {
        return this.getErrors().size();
    }

    @Override
    public int getWarningCount() {
        return this.getWarnings().size();
    }

    @Override
    public int getInfoCount() {
        return this.getInfos().size();
    }
}
