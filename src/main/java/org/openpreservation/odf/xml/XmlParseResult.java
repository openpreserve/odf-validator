package org.openpreservation.odf.xml;

import java.util.Collections;
import java.util.List;

import org.openpreservation.messages.Message;

public class XmlParseResult {
    public final boolean isWellFormed;
    public final boolean isValid;
    public final String rootName;
    public final String version;
    public final String mimeType;
    public final List<Message> messages;

    private XmlParseResult(final boolean isWellFormed, final boolean isValid, final String rootName,
            final String version,
            final String mimeType, final List<Message> messages) {
        this.isWellFormed = isWellFormed;
        this.isValid = isValid;
        this.rootName = rootName;
        this.version = version;
        this.mimeType = mimeType;
        this.messages = Collections.unmodifiableList(messages);
    }

    public boolean hasRootName() {
        return rootName != null && !rootName.isEmpty();
    }

    public boolean isRootName(final String name) {
        return rootName != null && rootName.equals(name);
    }

    public boolean hasVersion() {
        return version != null && !version.isEmpty();
    }

    public boolean hasMimeType() {
        return mimeType != null && !mimeType.isEmpty();
    }

    public static XmlParseResult of(final List<Message> messages) {
        return new XmlParseResult(false, false, "", "", "", messages);
    }

    public static XmlParseResult invalid(final List<Message> messages) {
        return new XmlParseResult(true, false, "", "", "", messages);
    }

    public static XmlParseResult valid(final List<Message> messages) {
        return new XmlParseResult(true, true, "", "", "", messages);
    }

    public static XmlParseResult of(final boolean isWellFormed, final boolean isValid, final String rootName,
            final String version,
            final String mimeType, final List<Message> messages) {
        return new XmlParseResult(isWellFormed, isValid, rootName, version, mimeType, messages);
    }
}
