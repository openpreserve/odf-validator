package org.openpreservation.format.xml;

import java.util.List;

import org.openpreservation.messages.Message;

/**
 * A factory for creating {@link ParseResult} and {@link ValidationResult}
 * objects.
 */
public final class ValidationResults {
    /**
     * Create a new {@link ParseResult} object from the supplied values.
     * 
     * @param valid
     *                 <code>true</code> if the result is for a valid XML document,
     *                 otherwise <code>false</code>
     * @param messages
     *                 the <code>List</code> of <code>Message</code>s to be used for
     *                 the <code>ValidationResult</code>
     * @return a new <code>ParseResult</code> object created from the supplied
     *         values
     */
    public static final ParseResult parseResultOf(final boolean valid, final Namespace namespace,
            final List<Namespace> declareNamespaces, final List<Namespace> usedNamespaces,
            final String prefix, final String name, final List<Attribute> attributes, final List<Message> messages) {
        return ParseResultImpl.of(valid, namespace, declareNamespaces, usedNamespaces, prefix, name, attributes,
                messages);
    }

    /**
     * Creates a new {@link ValidationResult} object from the supplied values.
     * 
     * @param parseResult
     *                    the {@link ParseResult} to be used for the
     *                    {@link ValidationResult}
     * @param valid
     *                    <code>true</code> if the result is for a valid XML
     *                    document, otherwise <code>false</code>
     * @param messages
     *                    the <code>List</code> of <code>Message</code>s to be used
     *                    for the <code>ValidationResult</code>
     * @return a new <code>ValidationResult</code> object created from the supplied
     *         values
     */
    public static final ValidationResult of(final ParseResult parseResult, final boolean valid,
            final List<Message> messages) {
        return ValidationResultImpl.of(parseResult, valid, messages);
    }

    private ValidationResults() {
        throw new AssertionError("No instances of ValidationResults");
    }
}
