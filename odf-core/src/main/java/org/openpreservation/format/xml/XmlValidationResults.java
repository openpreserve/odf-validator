package org.openpreservation.format.xml;

import java.util.List;

import org.openpreservation.odf.validation.messages.Message;

/**
 * A factory for creating {@link ParseResult} and {@link XmlValidationResult}
 * objects.
 */
public final class XmlValidationResults {
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
     * Creates a new {@link XmlValidationResult} object from the supplied values.
     * 
     * @param parseResult
     *                    the {@link ParseResult} to be used for the
     *                    {@link XmlValidationResult}
     * @param valid
     *                    <code>true</code> if the result is for a valid XML
     *                    document, otherwise <code>false</code>
     * @param messages
     *                    the <code>List</code> of <code>Message</code>s to be used
     *                    for the <code>ValidationResult</code>
     * @return a new <code>ValidationResult</code> object created from the supplied
     *         values
     */
    public static final XmlValidationResult of(final ParseResult parseResult, final boolean valid,
            final List<Message> messages) {
        return XmlValidationResultImpl.of(parseResult, valid, messages);
    }

    private XmlValidationResults() {
        throw new AssertionError("No instances of ValidationResults");
    }
}
