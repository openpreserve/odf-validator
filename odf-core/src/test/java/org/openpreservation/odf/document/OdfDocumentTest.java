package org.openpreservation.odf.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OdfDocumentTest {
    private Utils utils = new Utils();
    public OdfDocumentTest() throws IOException, ParserConfigurationException, SAXException {
    }

    @Test
    public void testInstantiation() {
        OdfXmlDocument nullDoc = null;
        ParseResult nullResult = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(nullDoc, utils.metadata);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(utils.odfDocument, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(nullResult, utils.metadata);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(utils.parseResult, null);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(OdfDocumentImpl.class).verify();
    }

    @Test
    public void testFrom() throws IOException, ParserConfigurationException, SAXException {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.from(null);
                });
        Metadata md = OdfXmlDocuments.metadataFrom(TestFiles.EMPTY_FODS.openStream());
        OdfDocument doc = OdfDocumentImpl.from(TestFiles.EMPTY_FODS.openStream());
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", md, doc.getMetadata());
    }

    @Test
    public void testOfDocument() throws IOException, ParserConfigurationException, SAXException {
        Metadata md = OdfXmlDocuments.metadataFrom(TestFiles.EMPTY_FODS.openStream());
        OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docFrom = OdfDocumentImpl.from(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docOf = OdfDocumentImpl.of(xmlDoc, md);
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", docOf.getMetadata(), docFrom.getMetadata());
        assertEquals("Expectd synthetic document and parsed document to be equal", xmlDoc, docFrom.getXmlDocument());
    }

    @Test
    public void testOfResult() throws IOException, ParserConfigurationException, SAXException {
        XmlParser parser = new XmlParser();
        Metadata md = OdfXmlDocuments.metadataFrom(TestFiles.EMPTY_FODS.openStream());
        ParseResult result = parser.parse(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docFrom = OdfDocumentImpl.from(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docOf = OdfDocumentImpl.of(result, md);
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", docOf.getMetadata(), docFrom.getMetadata());
    }
}
