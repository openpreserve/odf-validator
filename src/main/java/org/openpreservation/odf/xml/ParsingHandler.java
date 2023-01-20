package org.openpreservation.odf.xml;

import java.util.List;

import org.openpreservation.messages.Message;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ParsingHandler extends DefaultHandler {
    private String firstTag = "";
    private String version = "";
    private String mime = "";

    public ParsingHandler() {
        super();
    }

    public XmlParseResult getResult(final boolean isWellFormed, final List<Message> messages) {
        return XmlParseResult.of(isWellFormed, false, this.firstTag, this.version, this.mime, messages);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (this.firstTag.isEmpty()) {
            this.firstTag = qName;
            this.version = attributes.getValue("office:version") != null ? attributes.getValue("office:version")
                    : "";
            this.mime = attributes.getValue("office:mimetype") != null ? attributes.getValue("office:mimetype")
                    : "";
        }
    }
}
