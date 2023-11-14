package org.openpreservation.odf.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OpenDocument;

public final class ValidationReport {
    final String name;
    public final OpenDocument document;
    public final Map<String, MessageLog> documentMessages;

    private ValidationReport(final String name) {
        this(name, null);
    }

    private ValidationReport(final String name, final OpenDocument document) {
        this(name, document, new HashMap<>());
    }

    private ValidationReport(final String name, final OpenDocument document, final Map<String, MessageLog> documentMessages) {
        super();
        this.name = name;
        this.document = document;
        this.documentMessages = documentMessages;
    }

    static final ValidationReport of(final String name) {
        return new ValidationReport(name);
    }
    static final ValidationReport of(final String name, final OpenDocument document) {
        return new ValidationReport(name, document);
    }

    static final ValidationReport of(final String name, final Map<String, MessageLog> documentMessages) {
        return new ValidationReport(name, null, documentMessages);
    }

    static final ValidationReport of(final String name, final OpenDocument document, final Map<String, MessageLog> documentMessages) {
        return new ValidationReport(name, document, documentMessages);
    }

    @Override
    public String toString() {
        return "ValidationReport [name=" + name + ", documentMessages=" + documentMessages + "]";
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

    public void addAll(final Map<String, List<Message>> messages) {
        messages.entrySet().stream().forEach(e -> add(e.getKey(), e.getValue()));
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((documentMessages == null) ? 0 : documentMessages.hashCode());
        result = prime * result + ((document == null) ? 0 : document.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ValidationReport other = (ValidationReport) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (documentMessages == null) {
            if (other.documentMessages != null)
                return false;
        } else if (!documentMessages.equals(other.documentMessages))
            return false;
        if (document == null) {
            if (other.document != null)
                return false;
        } else if (!document.equals(other.document))
            return false;
        return true;
    }
}
