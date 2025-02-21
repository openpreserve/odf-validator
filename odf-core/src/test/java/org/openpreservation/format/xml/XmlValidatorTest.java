package org.openpreservation.format.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.xml.OdfNamespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.xml.sax.SAXException;

public class XmlValidatorTest {
    private XmlParser xmlParser = new XmlParser();
    private XmlValidator xmlValidator = new XmlValidator();
    private OdfSchemaFactory odfSchemaFactory = new OdfSchemaFactory();
    private Schema schema = odfSchemaFactory.getSchema(OdfNamespaces.OFFICE);

    public XmlValidatorTest() throws ParserConfigurationException, SAXException {
    }

    @Test
    public void testValidateValid()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.EMPTY_FODS.openStream());
        assertNotNull("Parse result is not null", parseResult);
        XmlValidationResult validationResult = xmlValidator.validate(parseResult,
                TestFiles.EMPTY_FODS.openStream(), schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertTrue("Validation result should be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNotWF()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.FLAT_NOT_WF.openStream());
        assertNotNull("Parse result is not null", parseResult);
        assertFalse("Parse result should NOT be well formed", parseResult.isWellFormed());
        XmlValidationResult validationResult = xmlValidator.validate(parseResult,
                TestFiles.FLAT_NOT_WF.openStream(),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertFalse("Validation result should NOT be well formed", validationResult.isWellFormed());
    }

    @Test
    public void testValidateNotWFBadParseResult()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.EMPTY_FODS.openStream());
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        XmlValidationResult validationResult = xmlValidator.validate(parseResult,
                TestFiles.FLAT_NOT_WF.openStream(),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertFalse("Validation result should NOT be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNotValid()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.FLAT_NOT_VALID.openStream());
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        XmlValidationResult validationResult = xmlValidator.validate(parseResult,
                TestFiles.FLAT_NOT_VALID.openStream(),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNoVersion()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.FLAT_NO_VERSION.openStream());
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        XmlValidationResult validationResult = xmlValidator.validate(parseResult,
                TestFiles.FLAT_NO_VERSION.openStream(),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }

    @Test
    public void testValidateNoMime()
            throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.FLAT_NO_MIME.openStream());
        assertNotNull("Parse result is not null", parseResult);
        assertTrue("Parse result should be well formed", parseResult.isWellFormed());
        XmlValidationResult validationResult = xmlValidator.validate(parseResult,
                TestFiles.FLAT_NO_MIME.openStream(),
                schema);
        assertNotNull("Validation result is not null", validationResult);
        assertTrue("Validation result should be well formed", validationResult.isWellFormed());
        assertFalse("Validation result should NOT be valid", validationResult.isValid());
    }
}
