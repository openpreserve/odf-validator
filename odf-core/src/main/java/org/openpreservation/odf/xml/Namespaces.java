package org.openpreservation.odf.xml;

import java.net.URI;
import java.util.Objects;

import org.openpreservation.utils.Checks;

public enum Namespaces {
    ANIM("urn:oasis:names:tc:opendocument:xmlns:animation:1.0",
            "Elements and attributes that describe animation content."),
    CHART("urn:oasis:names:tc:opendocument:xmlns:chart:1.0",
            "Elements and attributes that describe chart content."),
    CONFIG("urn:oasis:names:tc:opendocument:xmlns:config:1.0",
            "Elements and attributes that describe application specific settings."),
    DB("urn:oasis:names:tc:opendocument:xmlns:database:1.0",
            "For elements and attributes that describe database specific objects."),
    DR3D("urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0",
            "Elements and attributes that describe 3D graphic content."),
    DRAW("urn:oasis:names:tc:opendocument:xmlns:drawing:1.0",
            "Elements and attributes that describe graphic content."),
    FORM("urn:oasis:names:tc:opendocument:xmlns:form:1.0",
            "Elements and attributes that forms and controls."),
    FO("urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0",
            "Attributes that are compatible to attributes defined in https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-xsl"),
    GRDDL("http://www.w3.org/2003/g/data-view#",
            "GRDDL attributes (see https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-grddl)."),
    MANIFEST("urn:oasis:names:tc:opendocument:xmlns:manifest:1.0",
            "Elements and attributes contained in the package manifest."),
    META("urn:oasis:names:tc:opendocument:xmlns:meta:1.0",
            "Elements and attributes that describe meta information."),
    NUMBER("urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0",
            "Elements and attributes that describe data style information."),
    OFFICE("urn:oasis:names:tc:opendocument:xmlns:office:1.0",
            "All common pieces of information not contained in another, more specific namespace."),
    PRESENTATION("urn:oasis:names:tc:opendocument:xmlns:presentation:1.0",
            "Elements and attributes that describe presentation content."),
    SCRIPT("urn:oasis:names:tc:opendocument:xmlns:script:1.0",
            "Elements and attributes that represent scripts or events."),
    STYLE("urn:oasis:names:tc:opendocument:xmlns:style:1.0",
            "Elements and attributes that describe the style and inheritance model used by the OpenDocument format."),
    TABLE("urn:oasis:names:tc:opendocument:xmlns:table:1.0",
            "Elements and attributes that may occur in spreadsheets or in table in a text document."),
    TEXT("urn:oasis:names:tc:opendocument:xmlns:text:1.0",
            "Elements and attributes that may occur within text documents and text parts of other document types."),
    DSIG("urn:oasis:names:tc:opendocument:xmlns:digitalsignature:1.0",
            "Elements and attributes contained in digital signature files."),
    PKG("http://docs.oasis-open.org/ns/office/1.2/meta/pkg#",
            "OWL classes and properties contained in metadata manifest files."),
    DS("http://www.w3.org/2000/09/xmldsig#",
            "XML Digital Signature Syntax and Processing namespace (see [xmldsig-core])"),
    XFORMS("http://www.w3.org/2002/xforms",
            "The XForms namespace (see https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-xforms)."),
    XHTML("http://www.w3.org/1999/xhtml",
            "RDFa attributes (see https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-rdfa)."),
    XMLNS("http://www.w3.org/XML/1998/namespace",
            "The XML namespace."),
    XLINK("http://www.w3.org/1999/xlink",
            "The XLink namespace (see https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-xlink)."),
    ODF("http://docs.oasis-open.org/ns/office/1.2/meta/odf#",
            "RDF node property and node elements for OpenDocument package entities."),
    OOO("http://openoffice.org/2004/office",
            "OpenOffice.org namespace.");

    public static final Namespaces fromPrefix(final String prefix) {
        Objects.requireNonNull(prefix, String.format(Checks.NOT_NULL, "String", "prefix"));
        for (final Namespaces ns : Namespaces.values()) {
            if (ns.prefix.equals(prefix)) {
                return ns;
            }
        }
        return null;
    }

    public static final Namespaces fromId(final String id) {
        Objects.requireNonNull(id, String.format(Checks.NOT_NULL, "String", "id"));
        final URI uri = URI.create(id);
        return fromId(uri);
    }

    public static final Namespaces fromId(final URI id) {
        for (final Namespaces ns : Namespaces.values()) {
            if (ns.id.equals(id)) {
                return ns;
            }
        }
        return null;
    }

    public final String prefix;

    public final URI id;

    public final String description;

    private Namespaces(final String id, final String description) {
        this.prefix = name().toLowerCase();
        this.id = URI.create(id);
        this.description = description;
    }
}
