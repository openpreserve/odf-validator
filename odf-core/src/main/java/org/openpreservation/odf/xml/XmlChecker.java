package org.openpreservation.odf.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * Class to wrap the checking of XML files for well-formedness and validity.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class XmlChecker {
    private static final String SAX_FEATURE_PREFIX = "http://xml.org/sax/features/";
    private static final String SAX_FEATURE_EXT_GEN_ENTITIES = SAX_FEATURE_PREFIX + "external-general-entities";
    private static final String SAX_FEATURE_EXT_PARAM_ENTITIES = SAX_FEATURE_PREFIX + "external-parameter-entities";
    private static final String JAXP_RNG_FACTORY = "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory";
    private static final String SCHEMA_RES_PATH_ODF_13 = "org/openpreservation/odf/schema/OpenDocument-v1.3-schema.rng";

    private static final MessageFactory FACTORY = Messages.getInstance();
    private final SchemaFactory rngSchemaFactory = getSchemaFactory();
    private final SAXParserFactory nonValidatingFactory;

    public XmlChecker() throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        this.nonValidatingFactory = getNonValidatingFactory();
    }

    public XmlParseResult parse(final InputStream toTest, final Path streamName)
            throws ParserConfigurationException, SAXException, IOException {
        final List<Message> messages = new ArrayList<>();
        ParsingHandler handler = new ParsingHandler();
        MessageHandler messageHandler = new MessageHandler();
        SAXParser parser;
        XMLReader reader;
        parser = nonValidatingFactory.newSAXParser();
        reader = parser.getXMLReader();
        reader.setContentHandler(handler);
        reader.setErrorHandler(messageHandler);
        try {
            reader.parse(new InputSource(toTest));
            messages.addAll(messageHandler.messages);
            return handler.getResult(true, messages);
        } catch (SAXParseException e) {
            if (e.getLineNumber() == 1 && e.getColumnNumber() == 1) {
                messages.add(FACTORY.getError("DOC-1", streamName));
            } else {
                messages.add(FACTORY.getError("XML-3", e.getLineNumber(),
                        e.getColumnNumber(), e.getMessage()));
            }
        } catch (SAXException e) {
            messages.add(FACTORY.getError("XML-1", streamName, e.getMessage()));
        }
        return XmlParseResult.of(messages);
    }

    public XmlParseResult validate(Path toValidate) throws IOException, SAXException {
        final List<Message> messages = new ArrayList<>();
        Schema schema = rngSchemaFactory
                .newSchema(new StreamSource(ClassLoader.getSystemResourceAsStream(SCHEMA_RES_PATH_ODF_13)));
        Validator validator = schema.newValidator();
        MessageHandler handler = new MessageHandler();
        validator.setErrorHandler(handler);
        boolean isValid = true;
        validator.validate(new StreamSource(toValidate.toFile()));
        messages.addAll(handler.messages);
        for (Message message : messages) {
            if (message.isError()) {
                isValid = false;
                break;
            }
        }
        return (isValid) ? XmlParseResult.valid(messages) : XmlParseResult.invalid(messages);
    }

    public XmlParseResult parse(Path toTest)
            throws IOException, ParserConfigurationException, SAXException {
        return parse(Files.newInputStream(toTest), toTest);
    }

    private static final SAXParserFactory getNonValidatingFactory()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setFeature(SAX_FEATURE_EXT_GEN_ENTITIES, false);
        factory.setFeature(SAX_FEATURE_EXT_PARAM_ENTITIES, false);
        return factory;
    }

    private static final SchemaFactory getSchemaFactory() {
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI,
                JAXP_RNG_FACTORY);
        return SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
    }
}
