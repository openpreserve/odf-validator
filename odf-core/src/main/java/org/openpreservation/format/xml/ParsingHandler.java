package org.openpreservation.format.xml;

import java.util.ArrayList;
import java.util.List;

import org.openpreservation.messages.Message;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ParsingHandler extends DefaultHandler {
    private Namespace rootNamespace = null;
    private List<Namespace> namespaces = new ArrayList<>();
    private String rootPrefix = "";
    private String rootLocalName = "";
    private List<Attribute> attributes = new ArrayList<>();

    public ParsingHandler() {
        super();
    }

    public ParseResult getResult(final boolean isWellFormed, final List<Message> messages) {
        return ParseResultImpl.of(isWellFormed, this.rootNamespace, this.namespaces, this.rootPrefix,
                this.rootLocalName, this.attributes, messages);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (this.rootLocalName.isEmpty()) {
            this.rootLocalName = localName;
            this.rootPrefix = qName.contains(":") ? qName.split(":")[0] : "";
            this.attributes = AttributeImpl.of(attributes);
            this.rootNamespace = NamespaceImpl.of(uri, this.rootPrefix);
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        this.namespaces.add(NamespaceImpl.of(uri, prefix));
    }
}
