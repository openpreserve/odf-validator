package org.openpreservation.odf.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.ValidationResults;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.xml.XmlTestUtils;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.Metadata.UserDefinedField;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OdfDocumentTest {
    private static ParseResult parseResult = ValidationResults.parseResultOf(true, XmlTestUtils.exampleNamespace,
            new ArrayList<>(),
            "prefix", "name", new ArrayList<>(), new ArrayList<>());
    Map<String, String> stringValues = new HashMap<>();
    List<UserDefinedField> userDefinedFields = new ArrayList<>();
    Metadata metadata = OdfXmlDocuments.metadataOf("", stringValues, userDefinedFields);
    OdfXmlDocument odfDocument = OdfXmlDocuments
            .xmlDocumentFrom(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));

    public OdfDocumentTest() throws IOException, ParserConfigurationException, SAXException {
    }

    @Test
    public void testInstantiation() {
        OdfXmlDocument nullDoc = null;
        ParseResult nullResult = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(nullDoc, metadata);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(odfDocument, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(nullResult, metadata);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(parseResult, null);
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
        Metadata md = OdfXmlDocuments.metadataFrom(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfDocument doc = OdfDocumentImpl.from(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", md, doc.getMetadata());
    }

    @Test
    public void testOfDocument() throws IOException, ParserConfigurationException, SAXException {
        Metadata md = OdfXmlDocuments.metadataFrom(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfDocument docFrom = OdfDocumentImpl.from(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfDocument docOf = OdfDocumentImpl.of(xmlDoc, md);
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", docOf.getMetadata(), docFrom.getMetadata());
        assertEquals("Expectd synthetic document and parsed document to be equal", xmlDoc, docFrom.getXmlDocument());
    }

    @Test
    public void testOfResult() throws IOException, ParserConfigurationException, SAXException {
        XmlParser parser = new XmlParser();
        Metadata md = OdfXmlDocuments.metadataFrom(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        ParseResult result = parser.parse(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfDocument docFrom = OdfDocumentImpl.from(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS));
        OdfDocument docOf = OdfDocumentImpl.of(result, md);
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", docOf.getMetadata(), docFrom.getMetadata());
    }
}
