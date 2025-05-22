package org.openpreservation.format.xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public final class XmlParsers {
    private XmlParsers() {
        throw new UnsupportedOperationException("XmlParsers is a utility class should not be instantiated");
    }
    /**
     * Convenience method to obtain a pre-configured SAXParserFactory that is not
     * validating and is namespace aware.
     * 
     * @return a pre-configured <code>SAXParserFactory</code> that is not validating
     *         and is namespace aware.
     * @throws SAXNotRecognizedException    When the underlying XMLReader does not
     *                                      recognize the property name.
     * @throws SAXNotSupportedException     When the underlying XMLReader recognizes
     *                                      the property name but doesn't support
     *                                      the property.
     * @throws ParserConfigurationException When the underlying XMLReader is not
     *                                      configured correctly.
     */
    public static final SAXParserFactory getNonValidatingFactory()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        return XmlUtils.getNonValidatingFactory();
    }

    /**
     * Obtains a pre-configured XmlParser that is non validating.
     * @return a pre-configured XmlParser that is non validating.
     * @throws ParserConfigurationException if there is a problem configuring the parser
     * @throws SAXException if there is a problem creating the parser
     * @see XmlParser
     */
    public static final XmlParser getNonValidatingParser()
            throws ParserConfigurationException, SAXException {
        return new XmlParserImpl();
    }
}
