package org.openpreservation.format.xml;

import java.util.List;

import org.openpreservation.messages.Message;

public final class ValidationResults {
    private ValidationResults() {
        throw new AssertionError("No instances of ValidationResults");
    }
    public static final ValidationResult of(final ParseResult parseResult, final boolean valid,
            final List<Message> messages) {
        return ValidationResultImpl.of(parseResult, valid, messages);
    }
}
