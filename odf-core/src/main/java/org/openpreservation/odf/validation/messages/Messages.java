package org.openpreservation.odf.validation.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;
import org.openpreservation.odf.validation.messages.ParameterImpl.ParameterListImpl;

/**
 * Utility class that handles creation of Message type instances, etc.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public enum Messages {
    /**
     * Ensure Messages is a singleton
     */
    INSTANCE;

    public static final String NO_ID = "NO-ID";
    public static final String EMPTY_MESSAGE = "";
    public static final Message DEFAULT_MESSAGE = getMessageInstance(NO_ID,
            Severity.INFO, EMPTY_MESSAGE, parameterListInstance());

    /**
     * Create a new message instance with a DEFAULT_ID
     *
     * @param message
     *                the message of the new message
     * @return the new message instance
     * @throws IllegalArgumentException
     *                                  if the id or message is null or empty
     */
    public static Message getMessageInstance(final String message)
            throws IllegalArgumentException {
        return getMessageInstance(NO_ID, Severity.INFO, message, parameterListInstance());
    }

    /**
     * Create a Message instance with the give id and message value
     *
     * @param id
     *                the id of the new message
     * @param message
     *                the message of the new message
     * @return the new message instance
     * @throws IllegalArgumentException
     *                                  if the id or message is null or empty
     */
    public static Message getMessageInstance(final String id, final Severity severity,
            final String message) throws IllegalArgumentException {
        return getMessageInstance(id, severity, message, EMPTY_MESSAGE, parameterListInstance());
    }

    /**
     * Create a Message instance with the give id and message value
     *
     * @param id
     *                the id of the new message
     * @param message
     *                the message of the new message
     * @return the new message instance
     * @throws IllegalArgumentException
     *                                  if the id or message is null or empty
     */
    public static Message getMessageInstance(final String id, final Severity severity,
            final String message, final ParameterList parameters) throws IllegalArgumentException {
        return getMessageInstance(id, severity, message, EMPTY_MESSAGE, parameters);
    }

    /**
     * Create a new Message instance with the given id, message and
     * sub-message
     *
     * @param id
     *                   the id of the new message
     * @param message
     *                   the message of the new message
     * @param subMessage
     *                   the sub-message of the new message
     * @return the new message instance
     * @throws IllegalArgumentException
     *                                  if the id or message is null or empty
     */
    public static Message getMessageInstance(final String id, final Severity severity,
            final String message, final String subMessage)
            throws IllegalArgumentException {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException(
                    "id cannot be null or an empty string.");
        if (message == null)
            throw new IllegalArgumentException(
                    "message cannot be null.");
        return MessageImpl.getInstance(id, severity, message, subMessage, parameterListInstance());
    }

    /**
     * Create a new Message instance with the given id, message and
     * sub-message
     *
     * @param id
     *                   the id of the new message
     * @param message
     *                   the message of the new message
     * @param subMessage
     *                   the sub-message of the new message
     * @return the new message instance
     * @throws IllegalArgumentException
     *                                  if the id or message is null or empty
     */
    public static Message getMessageInstance(final String id, final Severity severity,
            final String message, final String subMessage, final ParameterList parameters)
            throws IllegalArgumentException {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException(
                    "id cannot be null or an empty string.");
        if (message == null)
            throw new IllegalArgumentException(
                    "message cannot be null.");
        return MessageImpl.getInstance(id, severity, message, subMessage, parameters);
    }

    /**
     * Get a MessageFactory instance with the default property based
     * bundle name and the default user locale
     *
     * @return a new MessageFactory instance backed by the bundle property
     *         file.
     * @throws IllegalArgumentException
     *                                  if the bundle name is empty or the message
     *                                  bundle can't be
     *                                  located
     */
    public static MessageFactory getInstance()
            throws IllegalArgumentException {
        return getInstance("org.openpreservation.odf.messages.Messages");
    }

    /**
     * Get a MessageFactory instance with the requested property based
     * bundle name and the default user locale
     *
     * @param bundleName
     *                   the fully qualified resource path for the message bundle
     *                   property file
     * @return a new MessageFactory instance backed by the bundle property
     *         file.
     * @throws IllegalArgumentException
     *                                  if the bundle name is empty or the message
     *                                  bundle can't be
     *                                  located
     */
    public static MessageFactory getInstance(final String bundleName)
            throws IllegalArgumentException {
        final String odfLanguage = System.getProperty("odf.language");
        final Locale local = odfLanguage == null || odfLanguage.trim().length() == 0 ? Locale.getDefault()
                : Locale.forLanguageTag(odfLanguage);

        return getInstance(bundleName, local);
    }

    /**
     * Get a MessageFactory instance with the requested property based
     * bundle name and a specific locale
     *
     * @param bundleName
     *                   the fully qualified resource path for the message bundle
     *                   property file
     * @param locale
     *                   the locale for the message bundle
     * @return a new MessageFactory instance backed by the bundle property
     *         file.
     * @throws IllegalArgumentException
     *                                  if the bundle name is empty or the message
     *                                  bundle can't be
     *                                  located
     */
    public static MessageFactory getInstance(final String bundleName,
            final Locale locale) throws IllegalArgumentException {
        if (bundleName == null || bundleName.isEmpty())
            throw new IllegalArgumentException(
                    "bundleName cannot be null or empty");
        if (locale == null)
            throw new IllegalArgumentException("locale cannot be null.");
        final ResourceBundle messageBundle = ResourceBundle.getBundle(bundleName,
                locale);
        return MessageFactoryImpl.getInstance(messageBundle);
    }

    /**
     * Get the new MessageLog instance
     *
     * @return a new message log instance with the default path and no messages
     */
    public static MessageLog messageLogInstance() {
        return MessageLogImpl.of();
    }

    /**
     * Create a new MessageLog instance with the given values
     *
     * @param path the path reference for the message log, i.e. the path to the entity that the log is associated with
     * @param messages the list of messages to be added to the log
     * @return the new MessageLog instance created with the given values
     */
    public static MessageLog messageLogInstance(final String path, final List<Message> messages) {
        return MessageLogImpl.of(path, messages);
    }

    public static Parameter parameterOf(final String name, final String value) {
        return ParameterImpl.of(name, value);
    }

    public static ParameterList parameterListInstance() {
        return ParameterListImpl.of(new ArrayList<>());
    }
}
