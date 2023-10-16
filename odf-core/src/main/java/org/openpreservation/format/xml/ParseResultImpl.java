package org.openpreservation.format.xml;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.utils.Checks;

final class ParseResultImpl implements ParseResult {
    static ParseResult of(final boolean isWellFormed, final Namespace rootNamespace, final List<Namespace> namespaces,
            final String rootPrefix, final String rootName,
            final List<Attribute> rootAttributes, final List<Message> messages) {
        Objects.requireNonNull(namespaces, String.format(Checks.NOT_NULL, "namespaces", "List<Namespace>"));
        Objects.requireNonNull(messages, String.format(Checks.NOT_NULL, "messages", "List<Message>"));
        return new ParseResultImpl(isWellFormed, rootNamespace, namespaces, rootPrefix, rootName, rootAttributes,
                messages);
    }

    static ParseResult of(final Namespace rootNamespace, final List<Namespace> namespaces,
            final String rootPrefix, final String rootName,
            final List<Attribute> rootAttributes, final List<Message> messages) {
        Objects.requireNonNull(messages, String.format(Checks.NOT_NULL, "messages", "List<Message>"));
        return ParseResultImpl.of(isWellFormed(messages), rootNamespace, namespaces, rootPrefix, rootName,
                rootAttributes,
                messages);
    }

    private static final boolean isWellFormed(final List<Message> messages) {
        for (final Message message : messages) {
            if (message.getSeverity() == Severity.ERROR) {
                return false;
            }
        }
        return true;
    }

    static ParseResult invertWellFormed(final ParseResult parseResult) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, "parseResult", "ParseResult"));
        return new ParseResultImpl(!parseResult.isWellFormed(), parseResult.getRootNamespace(),
                parseResult.getNamespaces(), parseResult.getRootPrefix(), parseResult.getRootName(),
                parseResult.getRootAttributes(),
                parseResult.getMessages());
    }

    private final boolean isWF;
    private final Namespace rootNamespace;
    private final List<Namespace> namespaces;
    private final String rootPrefix;
    private final String rootName;
    private final List<Attribute> rootAttributes;

    private final List<Message> messages;

    private ParseResultImpl(final boolean isWellFormed, final Namespace rootNamespace, final List<Namespace> namespaces,
            final String rootPrefix, final String rootName, final List<Attribute> rootAttributes,
            final List<Message> messages) {
        this.isWF = isWellFormed;
        this.rootNamespace = rootNamespace;
        this.namespaces = Collections.unmodifiableList(namespaces);
        this.rootPrefix = rootPrefix;
        this.rootName = rootName;
        this.rootAttributes = Collections.unmodifiableList(rootAttributes);
        this.messages = Collections.unmodifiableList(messages);
    }

    @Override
    public boolean isWellFormed() {
        return this.isWF;
    }

    @Override
    public Namespace getRootNamespace() {
        return this.rootNamespace;
    }

    @Override
    public List<Namespace> getNamespaces() {
        return this.namespaces;
    }

    @Override
    public String getRootPrefix() {
        return this.rootPrefix;
    }

    @Override
    public boolean isRootName(final String name) {
        return this.rootName != null && !this.rootName.isBlank() && this.rootName.equals(name);
    }

    @Override
    public String getRootName() {
        return this.rootName;
    }

    @Override
    public List<Attribute> getRootAttributes() {
        return this.rootAttributes;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isWF ? 1231 : 1237);
        result = prime * result + ((rootNamespace == null) ? 0 : rootNamespace.hashCode());
        result = prime * result + ((namespaces == null) ? 0 : namespaces.hashCode());
        result = prime * result + ((rootPrefix == null) ? 0 : rootPrefix.hashCode());
        result = prime * result + ((rootName == null) ? 0 : rootName.hashCode());
        result = prime * result + ((rootAttributes == null) ? 0 : rootAttributes.hashCode());
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
        final ParseResultImpl other = (ParseResultImpl) obj;
        if (isWF != other.isWF)
            return false;
        if (rootNamespace == null) {
            if (other.rootNamespace != null)
                return false;
        } else if (!rootNamespace.equals(other.rootNamespace))
            return false;
        if (namespaces == null) {
            if (other.namespaces != null)
                return false;
        } else if (!namespaces.equals(other.namespaces))
            return false;
        if (rootPrefix == null) {
            if (other.rootPrefix != null)
                return false;
        } else if (!rootPrefix.equals(other.rootPrefix))
            return false;
        if (rootName == null) {
            if (other.rootName != null)
                return false;
        } else if (!rootName.equals(other.rootName))
            return false;
        if (rootAttributes == null) {
            if (other.rootAttributes != null)
                return false;
        } else if (!rootAttributes.equals(other.rootAttributes))
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
        return "ParseResultImpl [isWF=" + isWF + ", rootNamespace=" + rootNamespace + ", namespaces=" + namespaces
                + ", rootPrefix=" + rootPrefix + ", rootName=" + rootName + ", rootAttributes=" + rootAttributes
                + ", messages=" + messages + "]";
    }

    @Override
    public String getRootAttributeValue(final String qName) {
        for (final Attribute attribute : this.rootAttributes) {
            if (attribute.getQualifiedName().equals(qName)) {
                return attribute.getValue();
            }
        }
        return null;
    }
}
