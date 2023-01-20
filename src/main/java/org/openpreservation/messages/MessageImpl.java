package org.openpreservation.messages;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 *          Created 20 Feb 2019:16:46:19
 */

final class MessageImpl implements Message {
    private final String id;
    final Severity severity;
    private final String message;
    private final String subMessage;

    private MessageImpl(final String id, final Severity severity, final String message, final String subMessage) {
        this.id = id;
        this.severity = severity;
        this.message = message;
        this.subMessage = subMessage;
    }

    static Message getInstance(final String id, final Severity severity, final String message,
            final String subMessage) {
        return new MessageImpl(id, severity, message, subMessage);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Severity getSeverity() {
        return this.severity;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean hasSubMessage() {
        return (this.subMessage == null || this.subMessage.isEmpty());
    }

    @Override
    public String getSubMessage() {
        return this.subMessage;
    }

    @Override
    public boolean isError() {
        return this.severity == Severity.ERROR;
    }

    @Override
    public boolean isFatal() {
        return this.severity == Severity.FATAL;
    }

    @Override
    public boolean isInfo() {
        return this.severity == Severity.INFO;
    }

    @Override
    public boolean isWarning() {
        return this.severity == Severity.WARNING;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Messsage [id=" + this.id + ", message=" + this.message
                + ", subMessage=" + this.subMessage + "]";
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result
                + ((this.message == null) ? 0 : this.message.hashCode());
        result = prime * result
                + ((this.subMessage == null) ? 0 : this.subMessage.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MessageImpl)) {
            return false;
        }
        MessageImpl other = (MessageImpl) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!this.message.equals(other.message)) {
            return false;
        }
        if (this.subMessage == null) {
            if (other.subMessage != null) {
                return false;
            }
        } else if (!this.subMessage.equals(other.subMessage)) {
            return false;
        }
        return true;
    }
}
