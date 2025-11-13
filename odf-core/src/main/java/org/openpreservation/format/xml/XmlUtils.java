package org.openpreservation.format.xml;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

final class XmlUtils {
    private static final String SAX_FEATURE_PREFIX = "http://xml.org/sax/features/";
    private static final String SAX_FEATURE_EXT_GEN_ENTITIES = SAX_FEATURE_PREFIX + "external-general-entities";
    private static final String SAX_FEATURE_EXT_PARAM_ENTITIES = SAX_FEATURE_PREFIX + "external-parameter-entities";

    private XmlUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static final SAXParserFactory getNonValidatingFactory()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        final SAXParserFactory factory = SAXParserFactory.newInstance(
                "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", ClassLoader.getSystemClassLoader());
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(false);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature(SAX_FEATURE_EXT_GEN_ENTITIES, false);
        factory.setFeature(SAX_FEATURE_EXT_PARAM_ENTITIES, false);
        return factory;
    }

    static final XMLReader getSAXReader()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, SAXException {
        final SAXParser parser = XmlUtils.getNonValidatingFactory().newSAXParser();
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "false");
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "false");
        return parser.getXMLReader();
    }
    
    static final XMLReader getFilteredSAXReader(XMLFilter filter)
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, SAXException {
        final SAXParser parser = XmlUtils.getNonValidatingFactory().newSAXParser();
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "false");
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "false");
        filter.setParent(parser.getXMLReader());
        return filter;
    }

    static ParameterList excepToParameterList(SAXParseException e) {
        return Messages.parameterListInstance().clear().add("line", Integer.toString(e.getLineNumber()))
                .add("column", Integer.toString(e.getColumnNumber()))
                .add("message", e.getMessage());
    }
}
