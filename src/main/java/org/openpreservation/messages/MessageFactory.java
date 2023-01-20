package org.openpreservation.messages;

import java.util.NoSuchElementException;

import org.openpreservation.messages.Message.Severity;

/**
 * Factory interface for Message creation
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public interface MessageFactory {

    /**
     * Retrieve Message by unique persistent id
     * 
     * @param id
     *                 the id of the message to be retrieved
     * @param severity
     *                 the the severity of the message
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getMessage(final String id, final Severity severity)
            throws NoSuchElementException;

    /**
     * Retrieve error Message by unique persistent id
     * 
     * @param id
     *           the id of the message to be retrieved
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getError(final String id)
            throws NoSuchElementException;

    /**
     * Retrieve fatal Message by unique persistent id
     * 
     * @param id
     *           the id of the message to be retrieved
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getFatal(final String id)
            throws NoSuchElementException;

    /**
     * Retrieve info Message by unique persistent id
     * 
     * @param id
     *           the id of the message to be retrieved
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getInfo(final String id)
            throws NoSuchElementException;

    /**
     * Retrieve warning Message by unique persistent id
     * 
     * @param id
     *           the id of the message to be retrieved
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getWarning(final String id)
            throws NoSuchElementException;

    /**
     * 
     * Retrieve Message by unique persistent id and format with the given
     * arguments
     * 
     * @param id
     *                 the id of the message to be retrieved
     * @param severity
     *                 the the severity of the message
     * @param args
     *                 the arguments to be used in formatting the message
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getMessage(final String id, final Severity severity, final Object... args)
            throws NoSuchElementException;

    /**
     * 
     * Retrieve error Message by unique persistent id and format with the given
     * arguments
     * 
     * @param id
     *             the id of the message to be retrieved
     * @param args
     *             the arguments to be used in formatting the message
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getError(final String id, final Object... args)
            throws NoSuchElementException;

    /**
     * 
     * Retrieve error Message by unique persistent id and format with the given
     * arguments
     * 
     * @param id
     *             the id of the message to be retrieved
     * @param args
     *             the arguments to be used in formatting the message
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getFatal(final String id, final Object... args)
            throws NoSuchElementException;

    /**
     * 
     * Retrieve info Message by unique persistent id and format with the given
     * arguments
     * 
     * @param id
     *             the id of the message to be retrieved
     * @param args
     *             the arguments to be used in formatting the message
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getInfo(final String id, final Object... args)
            throws NoSuchElementException;

    /**
     * 
     * Retrieve warning Message by unique persistent id and format with the given
     * arguments
     * 
     * @param id
     *             the id of the message to be retrieved
     * @param args
     *             the arguments to be used in formatting the message
     * @return the message with persistent id equal to id
     * @throws NoSuchElementException
     *                                if no message with id can be retrieved
     */
    public Message getWarning(final String id, final Object... args)
            throws NoSuchElementException;
}
