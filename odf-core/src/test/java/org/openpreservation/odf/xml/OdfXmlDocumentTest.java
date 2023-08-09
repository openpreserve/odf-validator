package org.openpreservation.odf.xml;

import static org.junit.Assert.assertEquals;
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
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfXmlDocument odfDocument = OdfXmlDocumentImpl.of(parseResult);
        assertNotNull(odfDocument);
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", odfDocument.getMimeType());
        assertTrue(odfDocument.hasVersion());
        assertTrue(odfDocument.hasMimeType());
        assertEquals("1.3", odfDocument.getVersion());
        assertEquals("office", odfDocument.getRootNamespace().getPrefix());
        assertEquals("document", odfDocument.getLocalRootName());
    }
}
