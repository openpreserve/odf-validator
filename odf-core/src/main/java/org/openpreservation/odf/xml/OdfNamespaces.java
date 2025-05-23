package org.openpreservation.odf.xml;

import java.net.URI;
import java.net.URL;
import java.util.Objects;

import org.openpreservation.format.xml.Namespace;
import org.openpreservation.utils.Checks;

/**
 * Enum representing the XML namespaces used by ODF (OpenDocument Format).
 * The enum implements the {@link Namespace} interface, so that it plays nicely with the XML classes in the ODF library.
 *
 * Each instance ties the namespace URI to a prefix and a description of the namespace.
 * The instance names are the official prefix values in uppercase.
 * Read the descriptions of each instance for more info.
 */
public enum OdfNamespaces implements Namespace {
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
    DC("http://purl.org/dc/elements/1.1/",
            "The Dublin Core Namespace (see https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-dcmi)"),
    MATH("http://www.w3.org/1998/Math/MathML",
            "MathML Namespace (see https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-mathml)"),
    SVG("urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0",
            "Elements and attributes that are derived from elements or attributes defined in https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-svg."),
    SMIL("urn:oasis:names:tc:opendocument:xmlns:smil-compatible:1.0",
            "Attributes that are derived from to attributes defined in https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#nref-smil20");

    /**
     * Get an instance of OdfNamespaces from a prefix.
     *
     * @param prefix the prefix to search for
     * @return the OdfNamespaces instance with the given prefix, or null if not found
     */
    public static final OdfNamespaces fromPrefix(final String prefix) {
        Objects.requireNonNull(prefix, String.format(Checks.NOT_NULL, "String", "prefix"));
        for (final OdfNamespaces ns : OdfNamespaces.values()) {
            if (ns.prefix.equals(prefix)) {
                return ns;
            }
        }
        return null;
    }

    /**
     * Get an instance of OdfNamespaces from a namespace ID string.
     *
     * @param id the URI string to search for
     * @return the OdfNamespaces instance with the given URI, or null if not found
     */
    public static final OdfNamespaces fromId(final String id) {
        Objects.requireNonNull(id, String.format(Checks.NOT_NULL, "String", "id"));
        final URI uri = URI.create(id);
        return fromId(uri);
    }

    /**
     * Get an instance of OdfNamespaces from a namespace ID URI.
     *
     * @param id the URI to search for
     * @return the OdfNamespaces instance with the given URI, or null if not found
     */
    public static final OdfNamespaces fromId(final URI id) {
        for (final OdfNamespaces ns : OdfNamespaces.values()) {
            if (ns.id.equals(id)) {
                return ns;
            }
        }
        return null;
    }

    private final String prefix;

    private final URI id;

    private final String description;

    private OdfNamespaces(final String id, final String description) {
        this.prefix = name().toLowerCase();
        this.id = URI.create(id);
        this.description = description;
    }

    /**
     * Get a description of the namespace.
     *
     * @return a String description of the namespace
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public URI getId() {
        return this.id;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public URL getSchemalocation() {
        return null;
    }
}
