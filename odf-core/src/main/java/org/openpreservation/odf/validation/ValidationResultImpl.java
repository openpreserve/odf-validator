package org.openpreservation.odf.validation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.Formats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ValidationResult")
public final class ValidationResultImpl implements ValidationResult {
    static final ValidationResult of(final String name) {
        return ValidationResultImpl.of(name, Formats.UNKNOWN);
    }
    static final ValidationResult of(final String name, final Formats detectedFormat) {
        return ValidationResultImpl.of(name, detectedFormat, false);
    }
    static final ValidationResult of(final String name, final Formats detectedFormat, final boolean isEncrypted) {
        return ValidationResultImpl.of(name, detectedFormat, isEncrypted, Messages.messageLogInstance());
    }
    static final ValidationResult of(final String name, final Formats detectedFormat, final boolean isEncrypted, final MessageLog documentMessages) {
        return new ValidationResultImpl(name, detectedFormat, isEncrypted, documentMessages);
    }

    static final ValidationResult of(final String name, final OpenDocument document) {
        return ValidationResultImpl.of(name, document.getFormat(), (document.getPackage() != null) ? document.getPackage().isEncrypted() : false);
    }

    private final String name;
    private final Formats detectedFormat;
    private final boolean isEncrypted;
    private final MessageLog documentMessages;

    private ValidationResultImpl(final String name, final Formats detectedFormat, final boolean isEncrypted, final MessageLog documentMessages) {
        super();
        this.name = name;
        this.detectedFormat = detectedFormat;
        this.isEncrypted = isEncrypted;
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
    public List<Message> getErrors() {
        return documentMessages.getErrors().values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessages() {
        return documentMessages.getMessages().values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public MessageLog getMessageLog() {
        return this.documentMessages;
    }

    @Override
    public Formats getDetectedFormat() {
        return this.detectedFormat;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @JsonIgnore
    @Override
    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((detectedFormat == null) ? 0 : detectedFormat.hashCode());
        result = prime * result + ((isEncrypted) ? 1231 : 1237);
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
        if (detectedFormat == null) {
            if (other.detectedFormat != null)
                return false;
        } else if (!detectedFormat.equals(other.detectedFormat))
            return false;
        if (isEncrypted != other.isEncrypted)
            return false;
        if (documentMessages == null) {
            if (other.documentMessages != null)
                return false;
        } else if (!documentMessages.equals(other.documentMessages))
            return false;
        return true;
    }
}
