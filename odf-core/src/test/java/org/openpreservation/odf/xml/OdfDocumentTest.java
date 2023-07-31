package org.openpreservation.odf.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.xml.XmlTestUtils;
import org.openpreservation.odf.fmt.TestFiles;
import org.xml.sax.SAXException;

public class OdfDocumentTest {
    private XmlParser xmlParser = new XmlParser();

    public OdfDocumentTest() throws ParserConfigurationException, SAXException {
    }

    @Test
    public void testInstatiaion() throws IOException {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(null, "rootName", "mimeType", "version");
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(XmlTestUtils.exampleNamespace, null, "mimeType", "version");
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(XmlTestUtils.exampleNamespace, "rootName", null, "version");
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(XmlTestUtils.exampleNamespace, "rootName", "mimeType", null);
                });
    }

    @Test
    public void testParseValid() throws IOException {
        ParseResult parseResult = xmlParser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfDocument odfDocument = OdfDocumentImpl.of(parseResult);
        assertNotNull(odfDocument);
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", odfDocument.getMimeType());
        assertEquals("1.3", odfDocument.getVersion());
        assertEquals("office", odfDocument.getRootNamespace().getPrefix());
        assertEquals("document", odfDocument.getRootName());
    }
}
