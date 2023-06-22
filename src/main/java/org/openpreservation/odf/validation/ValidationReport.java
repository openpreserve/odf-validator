package org.openpreservation.odf.validation;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;

public class ValidationReport {
    final Path path;
    public final Map<Path, MessageLog> documentMessages;

    public ValidationReport(final Path path) {
        this(path, new HashMap<>());
    }

    public ValidationReport(final Path path, final Map<Path, MessageLog> documentMessages) {
        super();
        this.path = path;
        this.documentMessages = documentMessages;
    }

    public boolean isValid() {
        return documentMessages.values().stream().allMatch(m -> m.getErrors().isEmpty());        
    }

    public int add(final Path path, final Message message) {
        this.documentMessages.putIfAbsent(path, Messages.messageLogInstance());
        return this.documentMessages.get(path).add(message);
    }

    public int add(final Path path, final Collection<? extends Message> messages) {
        this.documentMessages.putIfAbsent(path, Messages.messageLogInstance());
        return this.documentMessages.get(path).add(messages);
    }

    public List<Message> getErrors() {
        return documentMessages.values().stream().flatMap(m -> m.getErrors().stream()).collect(Collectors.toList());
    }

    public List<Message> getErrors(final Path path) {
        return documentMessages.get(path).getErrors();
    }

    public List<Message> getWarnings() {
        return documentMessages.values().stream().flatMap(m -> m.getWarnings().stream()).collect(Collectors.toList());
    }

    public List<Message> getWarnings(final Path path) {
        return documentMessages.get(path).getWarnings();
    }

    public List<Message> getInfos() {
        return documentMessages.values().stream().flatMap(m -> m.getInfos().stream()).collect(Collectors.toList());
    }

    public List<Message> getInfos(final Path path) {
        return documentMessages.get(path).getInfos();
    }

    public List<Message> getMessages() {
        return documentMessages.values().stream().flatMap(m -> m.getMessages().stream()).collect(Collectors.toList());
    }

    public List<Message> getMessages(final Path path) {
        return documentMessages.get(path).getMessages();
    }

    public List<Message> getMessages(final Path path, final Message.Severity severity) {
        return documentMessages.get(path).getMessages(severity);
    }

    public List<Message> getMessages(final Path path, final String id) {
        return documentMessages.get(path).getMessages(id);
    }

    public boolean hasErrors() {
        return documentMessages.values().stream().anyMatch(m -> m.hasErrors());
    }

    public boolean hasErrors(final Path path) {
        return documentMessages.get(path).hasErrors();
    }

    public boolean hasWarnings() {
        return documentMessages.values().stream().anyMatch(m -> m.hasWarnings());
    }

    public boolean hasWarnings(final Path path) {
        return documentMessages.get(path).hasWarnings();
    }

    public boolean hasInfos() {
        return documentMessages.values().stream().anyMatch(m -> m.hasInfos());
    }

    public boolean hasInfos(final Path path) {
        return documentMessages.get(path).hasInfos();
    }
}
