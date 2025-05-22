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
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * Class to wrap the checking of XML files for well-formedness and validity.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
final class XmlParserImpl implements XmlParser {
    private static final class DummyEntityResolver implements EntityResolver {
        public InputSource resolveEntity(final String publicID, final String systemID)
                throws SAXException {

            return new InputSource(new StringReader(""));
        }
    }
    private static final MessageFactory FACTORY = Messages.getInstance();
    private final SAXParserFactory nonValidatingFactory;

    private final SAXParser parser;

    private final XMLReader reader;

    /**
     * Default constructor for the <code>XmlParser</code> class.
     * 
     * @throws ParserConfigurationException if the parser can not be configured
     * @throws SAXException                 if the parser can not be created
     */
    XmlParserImpl() throws ParserConfigurationException, SAXException {
        this.nonValidatingFactory = XmlUtils.getNonValidatingFactory();
        this.parser = nonValidatingFactory.newSAXParser();
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "false");
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "false");
        this.reader = this.parser.getXMLReader();
        this.reader.setEntityResolver(new DummyEntityResolver());
    }

    @Override
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
            messages.add(FACTORY.getError("XML-3", XmlUtils.excepToParameterList(e)));
        } catch (final SAXException e) {
            messages.add(FACTORY.getError("XML-1", Messages.parameterListInstance().add("message", e.getMessage())));
        }
        return handler.getResult(false, messages);
    }

    @Override
    public ParseResult parse(final Path toTest)
            throws IOException {
        Objects.requireNonNull(toTest, "Parameter toTest can not be null.");
        return parse(Files.newInputStream(toTest));
    }
}
