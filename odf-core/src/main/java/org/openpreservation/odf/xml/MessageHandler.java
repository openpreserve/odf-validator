package org.openpreservation.odf.xml;

import java.util.ArrayList;
import java.util.List;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class MessageHandler implements ErrorHandler {
    private static final String MESSAGE_ID = "XML-3";
    private static final MessageFactory FACTORY = Messages.getInstance();
    public final List<Message> messages = new ArrayList<>();

    @Override
    public void warning(SAXParseException e) {
        this.messages.add(FACTORY.getWarning(MESSAGE_ID, e.getLineNumber(), e.getColumnNumber(), e.getMessage()));
    }

    @Override
    public void error(SAXParseException e) {
        this.messages.add(FACTORY.getError(MESSAGE_ID, e.getLineNumber(), e.getColumnNumber(), e.getMessage()));
    }

    @Override
    public void fatalError(SAXParseException e) {
        this.messages.add(FACTORY.getError(MESSAGE_ID, e.getLineNumber(), e.getColumnNumber(), e.getMessage()));
    }
}
