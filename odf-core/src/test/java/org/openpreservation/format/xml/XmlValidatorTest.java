package org.openpreservation.format.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.xml.Namespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.xml.sax.SAXException;

public class XmlValidatorTest {
    private XmlParser xmlParser = new XmlParser();
    private XmlValidator xmlValidator = new XmlValidator();
    private OdfSchemaFactory odfSchemaFactory = new OdfSchemaFactory();
    private Schema schema = odfSchemaFactory.getSchema(Namespaces.OFFICE);

    public XmlValidatorTest() throws ParserConfigurationException, SAXException {
    }

    @Test
    public void testValidateValid()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        assertNotNull("Parse result is not null", parseResult);
        ValidationResult validationResult = xmlValidator.validate(parseResult,
                ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS), schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertTrue("Validation result should be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNotWF()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NOT_WF));
        assertNotNull("Parse result is not null", parseResult);
        assertFalse("Parse result should NOT be well formed", parseResult.isWellFormed());
        ValidationResult validationResult = xmlValidator.validate(parseResult,
                ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NOT_WF),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertFalse("Validation result should NOT be well formed", validationResult.isWellFormed());
    }

    @Test
    public void testValidateNotWFBadParseResult()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        ValidationResult validationResult = xmlValidator.validate(parseResult,
                ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NOT_WF),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertFalse("Validation result should NOT be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNotValid()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NOT_VALID));
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        ValidationResult validationResult = xmlValidator.validate(parseResult,
                ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NOT_VALID),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNoVersion()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NO_VERSION));
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        ValidationResult validationResult = xmlValidator.validate(parseResult,
                ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NO_VERSION),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNoMime()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NO_MIME));
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        ValidationResult validationResult = xmlValidator.validate(parseResult,
                ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NO_MIME),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }
}
