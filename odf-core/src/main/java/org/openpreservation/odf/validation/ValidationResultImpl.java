package org.openpreservation.odf.validation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "validation_result")
public final class ValidationResultImpl implements ValidationResult {

    private final String name;
    private final MessageLog documentMessages;

    static final ValidationResult of(final String name) {
        return ValidationResultImpl.of(name, Messages.messageLogInstance());
    }
    static final ValidationResult of(final String name, final MessageLog documentMessages) {
        return new ValidationResultImpl(name, documentMessages);
    }

    private ValidationResultImpl(final String name, final MessageLog documentMessages) {
        super();
        this.name = name;
        this.documentMessages = documentMessages;
    }

    @Override
    public String toString() {
        return "ValidationReport [name=" + name + ", documentMessages=" + documentMessages + "]";
    }

    @Override
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

    @Override
    public MessageLog getMessageLog() {
        return this.documentMessages;
    }

    @Override
    public List<Check> getChecks() {
        return documentMessages.getChecks();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((documentMessages == null) ? 0 : documentMessages.hashCode());
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
        final ValidationResultImpl other = (ValidationResultImpl) obj;
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
        return true;
    }
}
