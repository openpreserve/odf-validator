package org.openpreservation.odf.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.odf.fmt.TestFiles;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OdfXmlDocumentTest {
    private XmlParser xmlParser = new XmlParser();

    public OdfXmlDocumentTest() throws ParserConfigurationException, SAXException {
    }

    @Test
    public void testInstatiaion() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfXmlDocumentImpl.of(null);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(OdfXmlDocumentImpl.class).verify();
    }

    @Test
    public void testParseValid() throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.EMPTY_FODS.openStream());
        OdfXmlDocument odfDocument = OdfXmlDocumentImpl.of(parseResult);
        assertNotNull(odfDocument);
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", odfDocument.getMimeType());
        assertTrue(odfDocument.hasVersion());
        assertTrue(odfDocument.hasMimeType());
        assertEquals("1.3", odfDocument.getVersion());
        assertEquals("office", odfDocument.getRootNamespace().getPrefix());
        assertEquals("document", odfDocument.getLocalRootName());
    }

    @Test
    public void testGetRootName() throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.EMPTY_FODS.openStream());
        OdfXmlDocument odfDocument = OdfXmlDocumentImpl.of(parseResult);
        assertEquals("office:document", odfDocument.getRootName());
    }


    @Test
    public void testParseInvalid() throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.FLAT_NOT_VALID.openStream());
        OdfXmlDocument odfDocument = OdfXmlDocumentImpl.of(parseResult);
        assertNotNull(odfDocument);
        assertTrue("File should have a MIME type", odfDocument.hasMimeType());
        assertTrue("File should have a version", odfDocument.hasVersion());
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", odfDocument.getMimeType());
        assertEquals("1.3", odfDocument.getVersion());
        assertEquals("office", odfDocument.getRootNamespace().getPrefix());
        assertEquals("document", odfDocument.getLocalRootName());
    }
   
    @Test
    public void testParseNoMimetype() throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.FLAT_NO_MIME.openStream());
        OdfXmlDocument odfDocument = OdfXmlDocumentImpl.of(parseResult);
        assertNotNull(odfDocument);
        assertFalse("File should NOT have a mimetype", odfDocument.hasMimeType());
        assertEquals("", odfDocument.getMimeType());
        assertTrue("File should have a version", odfDocument.hasVersion());
        assertEquals("1.3", odfDocument.getVersion());
        assertEquals("office", odfDocument.getRootNamespace().getPrefix());
        assertEquals("document", odfDocument.getLocalRootName());
    }

    @Test
    public void testParseNoVersion() throws IOException {
        ParseResult parseResult = xmlParser.parse(TestFiles.FLAT_NO_VERSION.openStream());
        OdfXmlDocument odfDocument = OdfXmlDocumentImpl.of(parseResult);
        assertNotNull(odfDocument);
        assertTrue("File should have a MIME type", odfDocument.hasMimeType());
        assertFalse("File should NOT have a version", odfDocument.hasVersion());
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", odfDocument.getMimeType());
        assertEquals("", odfDocument.getVersion());
        assertEquals("office", odfDocument.getRootNamespace().getPrefix());
        assertEquals("document", odfDocument.getLocalRootName());
    }
}
