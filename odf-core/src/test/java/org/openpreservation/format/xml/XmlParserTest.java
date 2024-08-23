package org.openpreservation.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.xml.sax.SAXException;

public class XmlParserTest {
    public void testParseNull() throws ParserConfigurationException, SAXException {
        XmlParser xmlChecker = new XmlParser();
        InputStream nullStream = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    xmlChecker.parse(nullStream);
                });
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    xmlChecker.parse(nullPath);
                });
    }

    @Test
    public void testParseWF() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(TestFiles.EMPTY_FODS.openStream());
        assertNotNull("Parse result is not null", result);
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have multiple root attributes", result.getRootAttributes().size() > 1);
        assertFalse("Parse result should have version attribute",
                result.getRootAttributeValue("office:version").isBlank());
        assertTrue("Parse result should have multiple namespace declarations",
                result.getDeclaredNamespaces().size() > 1);
    }

    @Test
    public void testParseNotWF() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(TestFiles.FLAT_NOT_WF.openStream());
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should NOT be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have multiple root attributes", result.getRootAttributes().size() > 1);
        assertFalse("Parse result should have version attribute",
                result.getRootAttributeValue("office:version").isBlank());
        assertTrue("Parse result should have multiple namespace declarations",
                result.getDeclaredNamespaces().size() > 1);
    }

    @Test
    public void testParseNotValid() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(TestFiles.FLAT_NOT_VALID.openStream());
        assertNotNull("Parse result is not null", result);
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have multiple root attributes", result.getRootAttributes().size() > 1);
        assertFalse("Parse result should have version attribute",
                result.getRootAttributeValue("office:version").isBlank());
        assertTrue("Parse result should have multiple namespace declarations",
                result.getDeclaredNamespaces().size() > 1);
    }

    @Test
    public void testParseNotXML() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(TestFiles.FAKEMIME_TEXT.openStream());
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should NOT be well formed", result.isWellFormed());
        assertTrue("Parse result should NOT have root name", result.getRootName().isBlank());
        assertNull("Parse result should NOT have root namespace", result.getRootNamespace());
        assertTrue("Parse result should NOT have root prefix", result.getRootPrefix().isBlank());
        assertEquals("Parse result should have no root attributes", 0, result.getRootAttributes().size());
        assertNull("Parse result should have no version attribute", result.getRootAttributeValue("office:version"));
        assertTrue("Parse result should have NO namespace declarations", result.getDeclaredNamespaces().size() < 1);
    }

    @Test
    public void testParseNoVersion() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(TestFiles.FLAT_NO_VERSION.openStream());
        assertNotNull("Parse result is not null", result);
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isEmpty());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertTrue("Parse result should have a root attribute", result.getRootAttributes().size() > 0);
        assertNull("Parse result should have NO version attribute", result.getRootAttributeValue("office:version"));
        assertNotNull("Parse result should have a mimetype attribute", result.getRootAttributeValue("office:mimetype"));
        assertTrue("Parse result should have multiple namespace declarations",
                result.getDeclaredNamespaces().size() > 1);
    }

    @Test
    public void testParseNoMime() throws ParserConfigurationException, SAXException, IOException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(TestFiles.FLAT_NO_MIME.openStream());
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertNotNull("Parse result should have root prefix", result.getRootPrefix());
        assertNotNull("Parse result should have a version attribute", result.getRootAttributeValue("office:version"));
        assertTrue("Parse result should have a root attribute", result.getRootAttributes().size() > 0);
        assertNull("Parse result should NOT have a mimetype attribute",
                result.getRootAttributeValue("office:mimetype"));
        assertTrue("Parse result should have multiple namespace declarations",
                result.getDeclaredNamespaces().size() > 1);
    }

    @Test
    public void testParseEmptyByPath()
            throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(new File(TestFiles.EMPTY.toURI()).toPath());
        assertNotNull("Parse result is not null", result);
        assertFalse("Parse result should NOT be well formed", result.isWellFormed());
        assertTrue("Parse result should NOT have root name", result.getRootName().isBlank());
        assertNull("Parse result should NOT have root namespace", result.getRootNamespace());
        assertTrue("Parse result should NOT have root prefix", result.getRootPrefix().isBlank());
        assertEquals("Parse result should have no root attributes", 0, result.getRootAttributes().size());
        assertNull("Parse result should have no version attribute", result.getRootAttributeValue("office:version"));
        assertTrue("Parse result should have NO namespace declarations", result.getDeclaredNamespaces().size() < 1);
    }

    @Test
    public void testParseLoExtExtended()
            throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        XmlParser xmlChecker = new XmlParser();
        ParseResult result = xmlChecker.parse(new File(TestFiles.LOEXT_EXTENDED_CONFORMANCE.toURI()).toPath());
        assertNotNull("Parse result is not null", result);
        assertTrue("Parse result should be well formed", result.isWellFormed());
        assertFalse("Parse result should have root name", result.getRootName().isBlank());
        assertNotNull("Parse result should have root namespace", result.getRootNamespace());
        assertFalse("Parse result should have root prefix", result.getRootPrefix().isBlank());
        assertEquals("Parse result should have a root attributes", 1, result.getRootAttributes().size());
        assertEquals("Parse result should have multiple namespace declarations", 35,
                result.getDeclaredNamespaces().size());
        assertEquals("Parse result should have multiple used namespaces", 10, result.getUsedNamespaces().size());
    }
}
