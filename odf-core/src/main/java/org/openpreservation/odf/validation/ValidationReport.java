package org.openpreservation.odf.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;

public class ValidationReport {
    final String name;
    public final Map<String, MessageLog> documentMessages;

    public ValidationReport(final String name) {
        this(name, new HashMap<>());
    }

    public ValidationReport(final String name, final Map<String, MessageLog> documentMessages) {
        super();
        this.name = name;
        this.documentMessages = documentMessages;
    }

    public boolean isValid() {
        return documentMessages.values().stream().allMatch(m -> m.getErrors().isEmpty());        
    }

    public int add(final String name, final Message message) {
        this.documentMessages.putIfAbsent(name, Messages.messageLogInstance());
        return this.documentMessages.get(name).add(message);
    }

    public int add(final String name, final Collection<? extends Message> messages) {
        this.documentMessages.putIfAbsent(name, Messages.messageLogInstance());
        return this.documentMessages.get(name).add(messages);
    }

    public List<Message> getErrors() {
        return documentMessages.values().stream().flatMap(m -> m.getErrors().stream()).collect(Collectors.toList());
    }

    public List<Message> getErrors(final String name) {
        return documentMessages.get(name).getErrors();
    }

    public List<Message> getWarnings() {
        return documentMessages.values().stream().flatMap(m -> m.getWarnings().stream()).collect(Collectors.toList());
    }

    public List<Message> getWarnings(final String name) {
        return documentMessages.get(name).getWarnings();
    }

    public List<Message> getInfos() {
        return documentMessages.values().stream().flatMap(m -> m.getInfos().stream()).collect(Collectors.toList());
    }

    public List<Message> getInfos(final String name) {
        return documentMessages.get(name).getInfos();
    }

    public List<Message> getMessages() {
        return documentMessages.values().stream().flatMap(m -> m.getMessages().stream()).collect(Collectors.toList());
    }

    public List<Message> getMessages(final String name) {
        return documentMessages.get(name).getMessages();
    }

    public List<Message> getMessages(final String name, final Message.Severity severity) {
        return documentMessages.get(name).getMessages(severity);
    }

    public List<Message> getMessages(final String name, final String id) {
        return documentMessages.get(name).getMessages(id);
    }

    public boolean hasErrors() {
        return documentMessages.values().stream().anyMatch(m -> m.hasErrors());
    }

    public boolean hasErrors(final String name) {
        return documentMessages.get(name).hasErrors();
    }

    public boolean hasWarnings() {
        return documentMessages.values().stream().anyMatch(m -> m.hasWarnings());
    }

    public boolean hasWarnings(final String name) {
        return documentMessages.get(name).hasWarnings();
    }

    public boolean hasInfos() {
        return documentMessages.values().stream().anyMatch(m -> m.hasInfos());
    }

    public boolean hasInfos(final String name) {
        return documentMessages.get(name).hasInfos();
    }
}
