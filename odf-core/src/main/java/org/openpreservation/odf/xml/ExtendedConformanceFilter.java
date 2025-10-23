package org.openpreservation.odf.xml;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

public class ExtendedConformanceFilter extends XMLFilterImpl {
    private static final String TRUE = "true";

    private Set<OdfNamespaces> odfNamespaces;
    private Stack<Boolean> foreignProcContElements = new Stack<>();
    private Stack<Boolean> paraAncestorElements = new Stack<>();

    private final Version version;

    /** Creates a new instance of NamespaceFilter */
    public ExtendedConformanceFilter(final Version version) {
        super();
        this.version = version;
        this.odfNamespaces = (this.version.compareTo(Version.ODF_12) >= 0) ? OdfNamespaces.ODF_NAMESPACES_1_2
                : OdfNamespaces.ODF_NAMESPACES_1_1;
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        OdfNamespaces namespace = OdfNamespaces.fromId(uri);
        if (isForeignNamespace(namespace)) {
            this.foreignProcContElements.pop();
        } else {
            if (isProcessContent()) {
                this.paraAncestorElements.pop();
                super.endElement(uri, localName, qName);
            }
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        boolean processContent = isProcessContent();
        OdfNamespaces namespace = OdfNamespaces.fromId(uri);
        if (isForeignNamespace(namespace)) {
            processForeignNamespace(processContent, atts);
        } else if (processContent) {
            super.startElement(uri, localName, qName, processContent(namespace, localName, atts));
        }
    }

    private boolean isForeignNamespace(final OdfNamespaces namespace) {
        return !this.odfNamespaces.contains(namespace);
    }

    private final void processForeignNamespace(final boolean processContent, final Attributes atts) {
        boolean processContentLocal = processContent;
        if (processContent) {
            String procContAttVal = atts.getValue(OdfNamespaces.OFFICE.getId().toString(), "process-content");
            if (this.version.compareTo(Version.ODF_12) >= 0) {
                processContentLocal = procContAttVal != null
                        ? procContAttVal.equals(TRUE)
                        : hasParagraphAncestorElement();
            } else {
                processContentLocal = procContAttVal == null || procContAttVal.equals(TRUE);
            }
        }
        this.foreignProcContElements.push(processContentLocal);
    }

    private final Attributes processContent(final OdfNamespaces namespace,
            final String localName, final Attributes atts) {
        AttributesImpl localAttributes = new AttributesImpl(atts);
        int i = localAttributes.getLength();
        while (i-- > 0) {
            String aAttrUri = localAttributes.getURI(i);
            if (isForeignNamespace(OdfNamespaces.fromId(aAttrUri))) {
                localAttributes.removeAttribute(i);
            }
        }

        boolean paraAncestor = hasParagraphAncestorElement();
        if (isParagraphAncestor(namespace, localName))
            paraAncestor = true;
        else if (isParagraphAncestorException(namespace, localName)) {
            paraAncestor = false;
        }
        paraAncestorElements.push(paraAncestor);
        return localAttributes;
    }

    private boolean isProcessContent() {
        return this.foreignProcContElements.empty()
                ? true
                : this.foreignProcContElements.peek();
    }

    private boolean hasParagraphAncestorElement() {
        return this.paraAncestorElements.empty()
                ? false
                : this.paraAncestorElements.peek();
    }

    private final boolean isParagraphAncestor(final OdfNamespaces namespace, final String localName) {
        return ((localName.equals("p") || localName.equals("h")) && namespace == OdfNamespaces.TEXT);
    }

    @Override
    public void characters(final char[] chars, final int start, final int length) throws SAXException {
        if (isProcessContent())
            super.characters(chars, start, length);
    }

    // elements that do not contain character data - by ODF 1.2
    // part 1 3.17, the character data in foreign elements
    // below these should be ignored by default
    private static final Map<OdfNamespaces, Set<String>> PARAGRAPH_ANCESTOR_EXCEPTIONS = Map.of(
            // Text namespace elements that do not contain character data
            OdfNamespaces.TEXT, Set.of("s", "tab", "line-break", "soft-page-break", "bookmark", 
                    "bookmark-start", "bookmark-end", "reference-mark", "reference-mark-start", 
                    "reference-mark-end", "note", "change", "change-start", "change-end", 
                    "toc-mark", "toc-mark-start", "toc-mark-end", "alphabetical-index-mark", 
                    "alphabetical-index-mark-start", "alphabetical-index-mark-end", 
                    "user-index-mark", "user-index-mark-start", "user-index-mark-end"),
            // Office namespace elements that do not contain character data
            OdfNamespaces.OFFICE, Set.of("annotation", "annotation-end"),
            // Draw namespace elements that do not contain character data
            OdfNamespaces.DRAW, Set.of("a", "rect", "line", "polyline", "polygon", "regular-polygon", 
                    "path", "circle", "g", "page-thumbnail", "frame", "measure", "caption", 
                    "connector", "control", "custom-shape"),
            // 3D namespace elements that do not contain character data
            OdfNamespaces.DR3D, Set.of("scene"),
            // Presentation namespace elements that do not contain character data
            OdfNamespaces.PRESENTATION, Set.of("header", "footer", "date-time"));

    private boolean isParagraphAncestorException(final OdfNamespaces namespace, final String localName) {
        return PARAGRAPH_ANCESTOR_EXCEPTIONS.containsKey(namespace)
                && PARAGRAPH_ANCESTOR_EXCEPTIONS.get(namespace).contains(localName);
    }
}
