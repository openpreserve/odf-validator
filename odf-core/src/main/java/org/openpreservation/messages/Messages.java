package org.openpreservation.messages;

import java.util.Locale;
import java.util.ResourceBundle;

import org.openpreservation.messages.Message.Severity;

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
            Severity.INFO, EMPTY_MESSAGE);

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
        return getMessageInstance(NO_ID, Severity.INFO, message);
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
        return getMessageInstance(id, severity, message, EMPTY_MESSAGE);
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
        return MessageImpl.getInstance(id, severity, message, subMessage);
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

    public static MessageLog messageLogInstance() {
        return new MessageLogImpl();
    }
}
