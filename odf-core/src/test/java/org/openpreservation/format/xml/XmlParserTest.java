package org.openpreservation.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.xml.sax.SAXException;

public class XmlParserTest {

    @Test
    public void testParseWF() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        assertNotNull("Parse result is not null", result);
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have multiple root attributes", result.getRootAttributes().size() > 1);
        assertFalse("Parse result should have version attribute", result.getRootAttributeValue("office:version").isBlank());
        assertTrue("Parse result should have multiple namespace declarations", result.getNamespaces().size() > 1);
    }

    @Test
    public void testParseNotWF() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NOT_WF));
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should NOT be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have multiple root attributes", result.getRootAttributes().size() > 1);
        assertFalse("Parse result should have version attribute", result.getRootAttributeValue("office:version").isBlank());
        assertTrue("Parse result should have multiple namespace declarations", result.getNamespaces().size() > 1);
    }

    @Test
    public void testParseNotValid() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NOT_VALID));
        assertNotNull("Parse result is not null", result);
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have multiple root attributes", result.getRootAttributes().size() > 1);
        assertFalse("Parse result should have version attribute", result.getRootAttributeValue("office:version").isBlank());
        assertTrue("Parse result should have multiple namespace declarations", result.getNamespaces().size() > 1);
    }

    @Test
    public void testParseNotXML() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FAKEMIME_TEXT));
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should NOT be well formed", result.isWellFormed());
        assertTrue("Parse result should NOT have root name", result.getRootName().isBlank());
        assertNull("Parse result should NOT have root namespace", result.getRootNamespace());
        assertTrue("Parse result should NOT have root prefix", result.getRootPrefix().isBlank());
        assertEquals("Parse result should have no root attributes", 0, result.getRootAttributes().size());
        assertNull("Parse result should have no version attribute", result.getRootAttributeValue("office:version"));
        assertTrue("Parse result should have NO namespace declarations", result.getNamespaces().size() < 1);
    }

    @Test
    public void testParseNoVersion() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NO_VERSION));
        assertNotNull("Parse result is not null", result);
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isEmpty());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have a root attribute", result.getRootAttributes().size() > 0);
        assertNull("Parse result should have NO version attribute", result.getRootAttributeValue("office:version"));
        assertNotNull("Parse result should have a mimetype attribute", result.getRootAttributeValue("office:mimetype"));
        assertTrue("Parse result should have multiple namespace declarations", result.getNamespaces().size() > 1);
    }

    @Test
    public void testParseNoMime() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(ClassLoader.getSystemResourceAsStream(TestFiles.FLAT_NO_MIME));
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertNotNull("Parse result should have a version attribute", result.getRootAttributeValue("office:version"));
        assertTrue("Parse result should have a root attribute", result.getRootAttributes().size() > 0);
        assertNull("Parse result should NOT have a mimetype attribute", result.getRootAttributeValue("office:mimetype"));
        assertTrue("Parse result should have multiple namespace declarations", result.getNamespaces().size() > 1);
    }

    @Test
    public void testParseEmpty() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY));
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should NOT be well formed", result.isWellFormed());
        assertTrue("Parse result should NOT have root name", result.getRootName().isBlank());
        assertNull("Parse result should NOT have root namespace", result.getRootNamespace());
        assertTrue("Parse result should NOT have root prefix", result.getRootPrefix().isBlank());
        assertEquals("Parse result should have no root attributes", 0, result.getRootAttributes().size());
        assertNull("Parse result should have no version attribute", result.getRootAttributeValue("office:version"));
        assertTrue("Parse result should have NO namespace declarations", result.getNamespaces().size() < 1);
    }
}
