package org.openpreservation.messages;

/**
 * Defines behaviour of validation messages.
 * These messages have a unique string identifier as well as
 * the previous message and sub-message strings.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public interface Message {
    /**
     * Get the unique, persistent message identifier.
     * 
     * @return the String message id.
     */
    public String getId();

    /**
     * Get the message severity
     *
     * @return the Severity of the message
     */
    public Severity getSeverity();

    /**
     * Get the main message
     * 
     * @return the String message
     */
    public String getMessage();

    /**
     * Test whether the message has a sub-message
     * 
     * @return true if the message has a sub-message
     */
    public boolean hasSubMessage();

    /**
     * Get the sub-message
     * 
     * @return the String sub-message
     */
    public String getSubMessage();

    /**
     * Test whether the message is an error.
     * 
     * @return true if the message is an error
     */
    public boolean isError();

    /**
     * Test whether the message is a fatal
     * 
     * @return true if the message is a fatal
     */
    public boolean isFatal();

    /**
     * Test whether the message is an info message.
     * 
     * @return true if the message is an info message
     */
    public boolean isInfo();

    /**
     * Test whether the message is a warning.
     * 
     * @return true if the message is a warning
     */
    public boolean isWarning();

    public enum Severity {
        INFO, WARNING, ERROR, FATAL;

        public final String label;

        private Severity() {
            this.label = this.name().toLowerCase();
        }
    }
}
