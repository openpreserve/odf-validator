package org.openpreservation.format.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlValidator {
    private static final MessageFactory FACTORY = Messages.getInstance();

    public ValidationResult validate(final ParseResult parseResult, final InputStream toValidate, Schema schema)
            throws IOException {
        if (!parseResult.isWellFormed()) {
            return ValidationResultImpl.of(parseResult, false, new ArrayList<>());
        }
        final List<Message> messages = new ArrayList<>();
        Validator validator = schema.newValidator();
        MessageHandler handler = new MessageHandler("XML-4");
        validator.setErrorHandler(handler);
        boolean isWellFormed = false;
        boolean isValid = true;
        try {
            validator.validate(new StreamSource(toValidate));
            isWellFormed = true;
        } catch (SAXParseException e) {
            messages.add(FACTORY.getError("XML-3", e.getLineNumber(),
                    e.getColumnNumber(), e.getMessage()));
        } catch (SAXException e) {
            messages.add(FACTORY.getError("XML-1", e.getMessage()));
        }
        messages.addAll(handler.messages);
        for (Message message : messages) {
            if (message.isError()) {
                isValid = false;
                break;
            }
        }
        if (!isWellFormed) {
            return ValidationResultImpl.of(ParseResultImpl.invertWellFormed(parseResult), false, messages);
        }
        return ValidationResultImpl.of(parseResult, isValid, messages);
    }
}
