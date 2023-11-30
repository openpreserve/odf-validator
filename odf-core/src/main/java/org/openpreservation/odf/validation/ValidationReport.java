package org.openpreservation.odf.validation;

import java.util.Collection;
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
    public final MessageLog documentMessages;

    private ValidationReport(final String name) {
        this(name, null);
    }

    private ValidationReport(final String name, final OpenDocument document) {
        this(name, document, Messages.messageLogInstance());
    }

    private ValidationReport(final String name, final OpenDocument document, final MessageLog documentMessages) {
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

    static final ValidationReport of(final String name, final MessageLog documentMessages) {
        return new ValidationReport(name, null, documentMessages);
    }

    static final ValidationReport of(final String name, final OpenDocument document, final MessageLog documentMessages) {
        return new ValidationReport(name, document, documentMessages);
    }

    @Override
    public String toString() {
        return "ValidationReport [name=" + name + ", documentMessages=" + documentMessages + "]";
    }

    public boolean isValid() {
        return !documentMessages.hasErrors();
    }

    public int add(final String name, final Message message) {
        return this.documentMessages.add(name, message);
    }

    public int add(final String name, final Collection<? extends Message> messages) {
        return this.documentMessages.add(name, messages);
    }

    public void addAll(final Map<String, List<Message>> messages) {
        messages.entrySet().stream().forEach(e -> add(e.getKey(), e.getValue()));
    }

    public List<Message> getErrors() {
        return documentMessages.getErrors().values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
    public List<Message> getMessages() {
        return documentMessages.getMessages().values().stream().flatMap(List::stream).collect(Collectors.toList());
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
