package org.openpreservation.messages;

import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import org.openpreservation.messages.Message.Severity;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 *
 *          Created 1 Mar 2019:16:52:25
 */

final class MessageFactoryImpl implements MessageFactory {
    private static final String MSG_NO_SUCH_MESSAGE = "Message id %s not found in bundle %s.";
    private final ResourceBundle messageBundle;

    private MessageFactoryImpl(final ResourceBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    @Override
    public Message getMessage(final String id, final Severity severity) throws NoSuchElementException {
        return getMessage(id, severity, (Object[]) null);
    }

    @Override
    public Message getError(final String id) throws NoSuchElementException {
        return getMessage(id, Severity.ERROR);
    }

    @Override
    public Message getFatal(final String id) throws NoSuchElementException {
        return getMessage(id, Severity.FATAL);
    }

    @Override
    public Message getInfo(final String id) throws NoSuchElementException {
        return getMessage(id, Severity.INFO);
    }

    @Override
    public Message getWarning(final String id) throws NoSuchElementException {
        return getMessage(id, Severity.WARNING);
    }

    @Override
    public Message getMessage(final String id, final Severity severity,
            final Object... args) throws NoSuchElementException {
        try {
            final String message = (args == null || args.length < 1) ? this.messageBundle.getString(id)
                    : String.format(this.messageBundle.getString(id), args);
            return Messages.getMessageInstance(id, severity, message);
        } catch (final MissingResourceException ex) {
            throw createEleException(id, ex);
        }
    }

    @Override
    public Message getError(final String id, final Object... args) throws NoSuchElementException {
        return getMessage(id, Severity.ERROR, args);
    }

    @Override
    public Message getFatal(final String id, final Object... args) throws NoSuchElementException {
        return getMessage(id, Severity.FATAL, args);
    }

    @Override
    public Message getInfo(final String id, final Object... args) throws NoSuchElementException {
        return getMessage(id, Severity.INFO, args);
    }

    @Override
    public Message getWarning(final String id, final Object... args) throws NoSuchElementException {
        return getMessage(id, Severity.WARNING, args);
    }

    private NoSuchElementException createEleException(final String id, final Throwable cause) {
        return (NoSuchElementException) new NoSuchElementException(
                String.format(MSG_NO_SUCH_MESSAGE, id, this.messageBundle.getBaseBundleName())).initCause(cause);
    }

    static MessageFactoryImpl getInstance(
            final ResourceBundle messageBundle) throws IllegalArgumentException {
        if (messageBundle == null)
            throw new IllegalArgumentException("messageBundle cannot be null");
        return new MessageFactoryImpl(messageBundle);
    }
}
