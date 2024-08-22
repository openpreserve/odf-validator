package org.openpreservation.format.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.openpreservation.messages.Message;
import org.openpreservation.utils.Checks;

final class ValidationResultImpl implements ValidationResult {
    private static final String PARSE_RESULT_NAME = "parseResult";
    private static final String PARSE_RESULT_TYPE = "ParseResult";
    private static final String MESSAGES_NAME = "messages";
    private static final String MESSAGES_TYPE = "List<Message>";
    static final ValidationResult of(final ParseResult parseResult, final boolean valid, final List<Message> messages) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, PARSE_RESULT_NAME, PARSE_RESULT_TYPE));
        Objects.requireNonNull(messages, String.format(Checks.NOT_NULL, MESSAGES_NAME, MESSAGES_TYPE));
        return new ValidationResultImpl(parseResult, valid, messages);
    }
    static final ValidationResult valid(final ParseResult parseResult, final List<Message> messages) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, PARSE_RESULT_NAME, PARSE_RESULT_TYPE));
        Objects.requireNonNull(messages, String.format(Checks.NOT_NULL, MESSAGES_NAME, MESSAGES_TYPE));
        return of(parseResult, true, messages);
    }
    static final ValidationResult notValid(final ParseResult parseResult, final List<Message> messages) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, PARSE_RESULT_NAME, PARSE_RESULT_TYPE));
        Objects.requireNonNull(messages, String.format(Checks.NOT_NULL, MESSAGES_NAME, MESSAGES_TYPE));
        return of(parseResult, false, messages);
    }

    private static List<Message> combineMessages(final ParseResult result, final List<Message> messages) {
        final List<Message> combined = new ArrayList<>(result.getMessages());
        combined.addAll(messages);
        return combined;
    }

    private final ParseResult parseResult;

    private final boolean valid;

    private final List<Message> messages;

    private ValidationResultImpl(final ParseResult parseResult, final boolean valid, final List<Message> messages) {
        this.parseResult = parseResult;
        this.valid = valid;
        this.messages = Collections.unmodifiableList(combineMessages(parseResult, messages));
    }

    @Override
    public boolean isWellFormed() {
        return this.parseResult.isWellFormed();
    }

    @Override
    public Namespace getRootNamespace() {
        return this.parseResult.getRootNamespace();
    }

    @Override
    public Set<Namespace> getDeclaredNamespaces() {
        return this.parseResult.getDeclaredNamespaces();
    }

    @Override
    public Set<Namespace> getUsedNamespaces() {
        return this.parseResult.getUsedNamespaces();
    }

    @Override
    public String getRootPrefix() {
        return this.parseResult.getRootPrefix();
    }

    @Override
    public String getRootName() {
        return this.parseResult.getRootName();
    }

    @Override
    public boolean isRootName(final String name) {
        return this.parseResult.isRootName(name);
    }

    @Override
    public List<Attribute> getRootAttributes() {
        return this.parseResult.getRootAttributes();
    }

    @Override
    public String getRootAttributeValue(final String qName) {
        return this.parseResult.getRootAttributeValue(qName);
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parseResult == null) ? 0 : parseResult.hashCode());
        result = prime * result + (valid ? 1231 : 1237);
        result = prime * result + ((messages == null) ? 0 : messages.hashCode());
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
        if (parseResult == null) {
            if (other.parseResult != null)
                return false;
        } else if (!parseResult.equals(other.parseResult))
            return false;
        if (valid != other.valid)
            return false;
        if (messages == null) {
            if (other.messages != null)
                return false;
        } else if (!messages.equals(other.messages))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ValidationResultImpl [parseResult=" + parseResult + ", valid=" + valid + ", messages=" + messages + "]";
    }
}
