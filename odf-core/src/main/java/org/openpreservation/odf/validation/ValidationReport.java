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

public final class ValidationReport {
    public final String name;
    public final Formats detectedFormat;
    public final boolean isEncrypted;
    public final MessageLog documentMessages;

    private ValidationReport(final String name, final Formats detectedFormat, final boolean isEncrypted, final MessageLog documentMessages) {
        super();
        this.name = name;
        this.detectedFormat = detectedFormat;
        this.isEncrypted = isEncrypted;
        this.documentMessages = documentMessages;
    }

    static final ValidationReport of(final String name) {
        return ValidationReport.of(name, Formats.UNKNOWN);
    }

    static final ValidationReport of(final String name, final Formats detectedFormat) {
        return ValidationReport.of(name, detectedFormat, false);
    }

    static final ValidationReport of(final String name, final Formats detectedFormat, final boolean isEncrypted) {
        return ValidationReport.of(name, detectedFormat, isEncrypted, Messages.messageLogInstance());
    }

    static final ValidationReport of(final String name, final Formats detectedFormat, final boolean isEncrypted, final MessageLog documentMessages) {
        return new ValidationReport(name, detectedFormat, isEncrypted, documentMessages);
    }

    static final ValidationReport of(final String name, final OpenDocument document) {
        return ValidationReport.of(name, document.getFormat(), (document.getPackage() != null) ? document.getPackage().isEncrypted() : false);
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

    public Formats getDetectedFormat() {
        return this.detectedFormat;
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
        final ValidationReport other = (ValidationReport) obj;
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
