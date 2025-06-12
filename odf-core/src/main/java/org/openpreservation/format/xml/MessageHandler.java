package org.openpreservation.format.xml;

import java.util.ArrayList;
import java.util.List;

import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.Messages;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public final class MessageHandler implements ErrorHandler {
    private static final String MESSAGE_ID = "XML-3";
    private static final MessageFactory FACTORY = Messages.getInstance();
    public final List<Message> messages = new ArrayList<>();
    private final String messageId;

    public MessageHandler() {
        this(MESSAGE_ID);
    }

    public MessageHandler(final String messageId) {
        super();
        this.messageId = messageId;
    }

    @Override
    public void warning(SAXParseException e) {
        this.messages.add(FACTORY.getWarning(messageId, XmlUtils.excepToParameterList(e)));
    }

    @Override
    public void error(SAXParseException e) {
        this.messages.add(FACTORY.getError(messageId, XmlUtils.excepToParameterList(e)));
    }

    @Override
    public void fatalError(SAXParseException e) {
        this.messages.add(FACTORY.getError(messageId, XmlUtils.excepToParameterList(e)));
    }
}
