package org.openpreservation.format.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.utils.Checks;

final class ParseResultImpl implements ParseResult {
    private static final String MESSAGES_NAME = "messages";
    private static final String MESSAGES_TYPE = "List<Message>";

    static ParseResult of(final boolean isWellFormed, final Namespace rootNamespace,
            final Collection<Namespace> declaredNamespaces, final Collection<Namespace> usedNamespaces,
            final String rootPrefix, final String rootName,
            final List<Attribute> rootAttributes, final List<Message> messages) {
        Objects.requireNonNull(declaredNamespaces,
                String.format(Checks.NOT_NULL, "declaredNamespaces", "List<Namespace>"));
        Objects.requireNonNull(usedNamespaces, String.format(Checks.NOT_NULL, "usedNamespaces", "List<Namespace>"));
        Objects.requireNonNull(messages, String.format(Checks.NOT_NULL, MESSAGES_NAME, MESSAGES_TYPE));
        return new ParseResultImpl(isWellFormed, rootNamespace, declaredNamespaces, usedNamespaces, rootPrefix,
                rootName, rootAttributes,
                messages);
    }

    static ParseResult of(final Namespace rootNamespace, final Collection<Namespace> declaredNamespaces,
            final Collection<Namespace> usedNamespaces,
            final String rootPrefix, final String rootName,
            final List<Attribute> rootAttributes, final List<Message> messages) {
        Objects.requireNonNull(messages, String.format(Checks.NOT_NULL, MESSAGES_NAME, MESSAGES_TYPE));
        return ParseResultImpl.of(isWellFormed(messages), rootNamespace, declaredNamespaces, usedNamespaces, rootPrefix,
                rootName,
                rootAttributes,
                messages);
    }

    static ParseResult invertWellFormed(final ParseResult parseResult) {
        Objects.requireNonNull(parseResult, String.format(Checks.NOT_NULL, "parseResult", "ParseResult"));
        return new ParseResultImpl(!parseResult.isWellFormed(), parseResult.getRootNamespace(),
                parseResult.getDeclaredNamespaces(), parseResult.getUsedNamespaces(), parseResult.getRootPrefix(),
                parseResult.getRootName(),
                parseResult.getRootAttributes(),
                parseResult.getMessages());
    }

    private static final boolean isWellFormed(final List<Message> messages) {
        for (final Message message : messages) {
            if (message.getSeverity() == Severity.ERROR) {
                return false;
            }
        }
        return true;
    }

    private final boolean isWF;
    private final Namespace rootNamespace;
    private final Set<Namespace> usedNamespaces;
    private final Set<Namespace> declaredNamespaces;
    private final String rootPrefix;
    private final String rootName;
    private final List<Attribute> rootAttributes;

    private final List<Message> messages;

    private ParseResultImpl(final boolean isWellFormed, final Namespace rootNamespace,
            final Collection<Namespace> declaredNamespaces, final Collection<Namespace> usedNamespaces,
            final String rootPrefix, final String rootName, final List<Attribute> rootAttributes,
            final List<Message> messages) {
        this.isWF = isWellFormed;
        this.rootNamespace = rootNamespace;
        this.declaredNamespaces = Collections.unmodifiableSet(new HashSet<>(declaredNamespaces));
        this.usedNamespaces = Collections.unmodifiableSet(new HashSet<>(usedNamespaces));
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
    public Set<Namespace> getDeclaredNamespaces() {
        return this.declaredNamespaces;
    }

    @Override
    public Set<Namespace> getUsedNamespaces() {
        return this.usedNamespaces;
    }

    @Override
    public String getRootPrefix() {
        return this.rootPrefix;
    }

    @Override
    public boolean isRootName(final String name) {
        Objects.requireNonNull(name, "String parameter name cannot be null.");
        if (this.rootName == null || (name.contains(":") && this.rootPrefix == null)) {
            return false;
        }
        final String match = (name.contains(":")) ? String.format("%s:%s", this.rootPrefix, this.rootName)
                : this.rootName;
        return match.equals(name);
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
        result = prime * result + ((declaredNamespaces == null) ? 0 : declaredNamespaces.hashCode());
        result = prime * result + ((usedNamespaces == null) ? 0 : usedNamespaces.hashCode());
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
        if (declaredNamespaces == null) {
            if (other.declaredNamespaces != null)
                return false;
        } else if (!declaredNamespaces.equals(other.declaredNamespaces))
            return false;
        if (usedNamespaces == null) {
            if (other.usedNamespaces != null)
                return false;
        } else if (!usedNamespaces.equals(other.usedNamespaces))
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
        return "ParseResultImpl [isWF=" + isWF + ", rootNamespace=" + rootNamespace + ", namespaces="
                + declaredNamespaces
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
