package org.openpreservation.format.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openpreservation.odf.validation.messages.Message;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ParsingHandler extends DefaultHandler {
    private Namespace rootNamespace = null;
    private Set<Namespace> declaredNamespaces = new HashSet<>();
    private Set<Namespace> usedNamespaces = new HashSet<>();
    private String rootPrefix = "";
    private String rootLocalName = "";
    private List<Attribute> attributes = new ArrayList<>();

    public ParsingHandler() {
        super();
    }

    public ParseResult getResult(final boolean isWellFormed, final List<Message> messages) {
        return ParseResultImpl.of(isWellFormed, this.rootNamespace, this.declaredNamespaces, this.usedNamespaces,
                this.rootPrefix,
                this.rootLocalName, this.attributes, messages);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (this.rootLocalName.isEmpty()) {
            this.rootLocalName = localName;
            this.rootPrefix = splitNamespace(qName);
            this.attributes = AttributeImpl.of(attributes);
            this.rootNamespace = NamespaceImpl.of(uri, this.rootPrefix);
            this.usedNamespaces.add(NamespaceImpl.of(uri, this.rootPrefix));
        } else {
            this.usedNamespaces.add(NamespaceImpl.of(uri, splitNamespace(qName)));
        }
        for (int index = 0; index < attributes.getLength(); index++) {
            this.usedNamespaces
                    .add(NamespaceImpl.of(attributes.getURI(index), splitNamespace(attributes.getQName(index))));
        }
    }

    private static final String splitNamespace(final String qName) {
        return qName.contains(":") ? qName.split(":")[0] : "";
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        this.declaredNamespaces.add(NamespaceImpl.of(uri, prefix));
    }
}
