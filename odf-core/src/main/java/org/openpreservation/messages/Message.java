package org.openpreservation.messages;

import java.util.Date;

/**
 * Defines behaviour of validation messages.
 *
 * These messages have a unique string identifier as well as
 * the previous message and sub-message strings.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public interface Message {
    /**
     * An enum set that defines the severity of a message.
     */
    public enum Severity {
        /**
         * The message is informational, equivalent to MAY
         */
        INFO,
        /**
         * The message is a warning, equivalent to SHOULD
         */
        WARNING,
        /**
         * The message is an error, equivalent to MUST
         */
        ERROR,
        /**
         * The message is a fatal error, usually an system issue
         */
        FATAL;

        /**
         * The label for the severity, lower case
         */
        public final String label;

        private Severity() {
            this.label = this.name().toLowerCase();
        }
    }

    /**
     * Get the unique, persistent message identifier.
     *
     * @return the <code>String</code> message id.
     */
    public String getId();

    /**
     * Get the message timestamp
     *
     * @return the {@link Date} timestamp of the message, or {@code null} if not set
     */
    public Date getTimestamp();

    /**
     * Get the message severity
     *
     * @return the {@link Severity} of the message
     */
    public Severity getSeverity();

    /**
     * Get the main message
     *
     * @return the <code>String</code> message
     */
    public String getMessage();

    /**
     * Test whether the message has a sub-message
     *
     * @return <code>true</code> if the message has a sub-message,
     *         <code>false</code> otherwise
     */
    public boolean hasSubMessage();

    /**
     * Get the sub-message
     *
     * @return the <code>String</code> sub-message
     */
    public String getSubMessage();

    /**
     * Test whether the message has {@link Severity} ERROR.
     *
     * @return <code>true</code> if the message has <code>Severity.ERROR</code>,
     *         <code>false</code> otherwise
     */
    public boolean isError();

    /**
     * Test whether the message has {@link Severity} FATAL.
     *
     * @return <code>true</code> if the message has <code>Severity.FATAL</code>,
     *         <code>false</code> otherwise
     */
    public boolean isFatal();

    /**
     * Test whether the message has {@link Severity} INFO.
     *
     * @return <code>true</code> if the message has <code>Severity.INFO</code>,
     *         <code>false</code> otherwise
     */
    public boolean isInfo();

    /**
     * Test whether the message has {@link Severity} WARNING.
     *
     * @return <code>true</code> if the message has <code>Severity.WARNING</code>,
     *         <code>false</code> otherwise
     */
    public boolean isWarning();
}
