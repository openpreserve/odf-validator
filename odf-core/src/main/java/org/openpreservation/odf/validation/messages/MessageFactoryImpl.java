package org.openpreservation.odf.validation.messages;

import java.util.Collections;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;

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
        return getMessage(id, severity, ParameterImpl.ParameterListImpl.of(Collections.emptyList()));
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
            final ParameterList parameters) throws NoSuchElementException {
        try {
            final String message = (parameters == null || parameters.size() < 1) ? this.messageBundle.getString(id)
                    : String.format(this.messageBundle.getString(id), getArgs(parameters));
            return Messages.getMessageInstance(id, severity, message, parameters);
        } catch (final MissingResourceException ex) {
            throw createEleException(id, ex);
        }
    }

    private final Object[] getArgs(final ParameterList parameters) {
        return parameters.toList().stream().map(Parameter::getValue).toArray();
    }

    @Override
    public Message getError(final String id, final ParameterList parameters) throws NoSuchElementException {
        return getMessage(id, Severity.ERROR, parameters);
    }

    @Override
    public Message getFatal(final String id, final ParameterList parameters) throws NoSuchElementException {
        return getMessage(id, Severity.FATAL, parameters);
    }

    @Override
    public Message getInfo(final String id, final ParameterList parameters) throws NoSuchElementException {
        return getMessage(id, Severity.INFO, parameters);
    }

    @Override
    public Message getWarning(final String id, final ParameterList parameters) throws NoSuchElementException {
        return getMessage(id, Severity.WARNING, parameters);
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
