package org.openpreservation.format.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.Messages;
import org.xml.sax.EntityResolver;
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
public final class XmlParser {
    private static final class DummyEntityResolver implements EntityResolver {
        public InputSource resolveEntity(final String publicID, final String systemID)
                throws SAXException {

            return new InputSource(new StringReader(""));
        }
    }
    private static final String SAX_FEATURE_PREFIX = "http://xml.org/sax/features/";
    private static final String SAX_FEATURE_EXT_GEN_ENTITIES = SAX_FEATURE_PREFIX + "external-general-entities";

    private static final String SAX_FEATURE_EXT_PARAM_ENTITIES = SAX_FEATURE_PREFIX + "external-parameter-entities";
    private static final MessageFactory FACTORY = Messages.getInstance();
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
    private final SAXParserFactory nonValidatingFactory;

    private final SAXParser parser;

    private final XMLReader reader;

    /**
     * Default constructor for the <code>XmlParser</code> class.
     * 
     * @throws ParserConfigurationException if the parser can not be configured
     * @throws SAXException                 if the parser can not be created
     */
    public XmlParser() throws ParserConfigurationException, SAXException {
        this.nonValidatingFactory = getNonValidatingFactory();
        this.parser = nonValidatingFactory.newSAXParser();
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "false");
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "false");
        this.reader = this.parser.getXMLReader();
        this.reader.setEntityResolver(new DummyEntityResolver());
    }

    /**
     * Parses the supplied <code>InputStream</code> for well-formedness and returns
     * a {@link ParseResult} object.
     *
     * @param toTest an <code>InputStream</code> to be parsed.
     * @return a <code>ParseResult</code> object containing the result of the parse.
     * @throws IOException if the supplied <code>InputStream</code> can not be read.
     */
    public ParseResult parse(final InputStream toTest)
            throws IOException {
        Objects.requireNonNull(toTest, "Parameter toTest can not be null.");
        final List<Message> messages = new ArrayList<>();
        final ParsingHandler handler = new ParsingHandler();
        final MessageHandler messageHandler = new MessageHandler();
        reader.setContentHandler(handler);
        reader.setErrorHandler(messageHandler);
        try {
            reader.parse(new InputSource(toTest));
            messages.addAll(messageHandler.messages);
            return handler.getResult(true, messages);
        } catch (final SAXParseException e) {
            messages.add(FACTORY.getError("XML-3", e.getLineNumber(),
                    e.getColumnNumber(), e.getMessage()));
        } catch (final SAXException e) {
            messages.add(FACTORY.getError("XML-1", e.getMessage()));
        }
        return handler.getResult(false, messages);
    }

    /**
     * Parses the supplied <code>Path</code> for well-formedness and returns a
     * {@link ParseResult} object.
     *
     * @param toTest a <code>Path</code> to be parsed.
     * @return a <code>ParseResult</code> object containing the result of the parse.
     * @throws IOException if the supplied <code>Path</code> can not be read.
     */
    public ParseResult parse(final Path toTest)
            throws IOException {
        Objects.requireNonNull(toTest, "Parameter toTest can not be null.");
        return parse(Files.newInputStream(toTest));
    }
}
