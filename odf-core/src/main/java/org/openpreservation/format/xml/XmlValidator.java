package org.openpreservation.format.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.Messages;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Simple class to wrap XML schema vaidaton.
 */
public final class XmlValidator {
    private static final MessageFactory FACTORY = Messages.getInstance();
    private static boolean isValid(final List<Message> messages) {
        for (final Message message : messages) {
            if (message.isError()) {
                return false;
            }
        }
        return true;
    }

    private boolean isWellFormed = false;

    /**
     * Validate the supplied InputStream against the supplied schema.
     *
     * @param parseResult the {@link ParseResult} obtained form parsign the file
     *                    using {@link XmlParser}
     * @param toValidate  an <code>InputStream</code> to validate
     * @param schema      the {@link Schema} to validate against
     * @return a {@link XmlValidationResult} containing the result of the validation
     * @throws IOException if there is an error reading supplied
     *                     <code>InputStream</code>.
     */
    public XmlValidationResult validate(final ParseResult parseResult, final InputStream toValidate, final Schema schema)
            throws IOException {
        if (!parseResult.isWellFormed()) {
            return XmlValidationResultImpl.of(parseResult, false, new ArrayList<>());
        }
        final List<Message> messages = validateSource(new StreamSource(toValidate), schema);
        if (!this.isWellFormed) {
            return XmlValidationResultImpl.of(ParseResultImpl.invertWellFormed(parseResult), false, messages);
        }
        return XmlValidationResultImpl.of(parseResult, isValid(messages), messages);
    }

    private List<Message> validateSource(final StreamSource toValidate, final Schema schema) throws IOException {
        final List<Message> messages = new ArrayList<>();
        final Validator validator = schema.newValidator();
        final MessageHandler handler = new MessageHandler("XML-4");
        validator.setErrorHandler(handler);
        try {
            validator.validate(toValidate);
            this.isWellFormed = true;
        } catch (final SAXParseException e) {
            messages.add(FACTORY.getError("XML-3", e.getLineNumber(),
                    e.getColumnNumber(), e.getMessage()));
        } catch (final SAXException e) {
            messages.add(FACTORY.getError("XML-1", e.getMessage()));
        }
        messages.addAll(handler.messages);
        return messages;
    }
}
