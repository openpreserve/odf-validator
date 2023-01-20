package org.openpreservation.odf.xml;

import java.net.URI;

public enum Namespaces {
    MANIFEST("urn:oasis:names:tc:opendocument:xmlns:manifest:1.0", "Elements and attribute contained in the package manifest."),
    DSIG("urn:oasis:names:tc:opendocument:xmlns:digitalsignature:1.0", "Elements and attribute contained in digital signature files."),
    PKG("http://docs.oasis-open.org/ns/office/1.2/meta/pkg#", "OWL classes and properties contained in metadata manifest files."),
    DS("http://www.w3.org/2000/09/xmldsig#", "XML Digital Signature Syntax and Processing namespace (see [xmldsig-core])");

    public final String prefix;
    public final URI id;
    public final String description;

    private Namespaces(final String id, final String description) {
        this.prefix = name().toLowerCase();
        this.id = URI.create(id);
        this.description = description;
    }

    public static final Namespaces fromPrefix(final String prefix) {
        for (Namespaces ns : Namespaces.values()) {
            if (ns.prefix.equals(prefix)) {
                return ns;
            }
        }
        return null;
    }

    public static final Namespaces fromId(final String id) {
        final URI uri = URI.create(id);
        for (Namespaces ns : Namespaces.values()) {
            if (ns.id.equals(uri)) {
                return ns;
            }
        }
        return null;
    }
}
