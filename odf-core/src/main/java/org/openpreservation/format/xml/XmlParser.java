package org.openpreservation.format.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * Class to wrap the checking of XML files for well-formedness and validity.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class XmlParser {
    private static final String SAX_FEATURE_PREFIX = "http://xml.org/sax/features/";
    private static final String SAX_FEATURE_EXT_GEN_ENTITIES = SAX_FEATURE_PREFIX + "external-general-entities";
    private static final String SAX_FEATURE_EXT_PARAM_ENTITIES = SAX_FEATURE_PREFIX + "external-parameter-entities";

    private static final MessageFactory FACTORY = Messages.getInstance();
    private final SAXParserFactory nonValidatingFactory;
    private final SAXParser parser;
    private final XMLReader reader;

    public XmlParser() throws ParserConfigurationException, SAXException {
        this.nonValidatingFactory = getNonValidatingFactory();
        this.parser = nonValidatingFactory.newSAXParser();
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        this.reader = this.parser.getXMLReader();
    }

    public ParseResult parse(final InputStream toTest)
            throws IOException {
        Objects.requireNonNull(toTest, "Parameter toTest can not be null.");
        final List<Message> messages = new ArrayList<>();
        ParsingHandler handler = new ParsingHandler();
        MessageHandler messageHandler = new MessageHandler();
        reader.setContentHandler(handler);
        reader.setErrorHandler(messageHandler);
        try {
            reader.parse(new InputSource(toTest));
            messages.addAll(messageHandler.messages);
            return handler.getResult(true, messages);
        } catch (SAXParseException e) {
            messages.add(FACTORY.getError("XML-3", e.getLineNumber(),
                    e.getColumnNumber(), e.getMessage()));
        } catch (SAXException e) {
            messages.add(FACTORY.getError("XML-1", e.getMessage()));
        }
        return handler.getResult(false, messages);
    }

    public ParseResult parse(Path toTest)
            throws IOException {
        Objects.requireNonNull(toTest, "Parameter toTest can not be null.");
        return parse(Files.newInputStream(toTest));
    }

    public static final SAXParserFactory getNonValidatingFactory()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance(
                "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", ClassLoader.getSystemClassLoader());
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setFeature(SAX_FEATURE_EXT_GEN_ENTITIES, false);
        factory.setFeature(SAX_FEATURE_EXT_PARAM_ENTITIES, false);
        return factory;
    }
}
