package org.openpreservation.odf.validation.messages;

import java.util.Date;
import java.util.List;

import org.openpreservation.odf.validation.messages.Parameter.ParameterList;

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
    private final ParameterList parameters;
    private final Date timestamp = new Date();

    private MessageImpl(final String id, final Severity severity, final String message, final String subMessage, final ParameterList parameters) {
        this.id = id;
        this.severity = severity;
        this.message = message;
        this.subMessage = subMessage;
        this.parameters = ParameterImpl.ParameterListImpl.of(parameters.toList());
    }

    static Message getInstance(final String id, final Severity severity, final String message,
            final String subMessage, final ParameterList parameters) {
        return new MessageImpl(id, severity, message, subMessage, parameters);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Date getTimestamp() {
        return this.timestamp;
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
    public List<Parameter> getParameters() {
        return this.parameters.toList();
    }

    @Override
    public boolean hasSubMessage() {
        return !(this.subMessage == null || this.subMessage.isEmpty());
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
                + ", subMessage=" + this.subMessage + ", parameters" + this.parameters + ", severity=" + this.severity + ", timestamp=" + this.timestamp + "]";
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.severity == null) ? 0 : this.severity.hashCode());
        result = prime * result
                + ((this.message == null) ? 0 : this.message.hashCode());
        result = prime * result
                + ((this.subMessage == null) ? 0 : this.subMessage.hashCode());
        result = prime * result + ((this.timestamp == null) ? 0 : this.timestamp.hashCode());
        result = prime * result + ((this.parameters == null) ? 0 : this.parameters.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MessageImpl)) {
            return false;
        }
        final MessageImpl other = (MessageImpl) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.severity == null) {
            if (other.severity != null) {
                return false;
            }
        } else if (!this.severity.equals(other.severity)) {
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
        if (this.timestamp == null) {
            if (other.timestamp != null) {
                return false;
            }
        } else if (!this.timestamp.equals(other.timestamp)) {
            return false;
        }
        if (this.parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!this.parameters.equals(other.parameters)) {
            return false;
        }
        return true;
    }
}
