package org.openpreservation.odf.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.xml.XmlParsers;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.DocumentType;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.xml.sax.SAXException;

public class DocumentsTest {
    private Utils utils = new Utils();

    public DocumentsTest() throws IOException, ParserConfigurationException, SAXException {
    }

    @Test
    public void testOpenDocInstantiationOfNullDocument() {
        OdfDocument nullDoc = null;
        Path path = Paths.get("");
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.openDocumentOf(path, nullDoc);
                });
    }

    @Test
    public void testOpenDocInstantiationOfDocument() throws ParseException, IOException {
        OdfDocument doc = Documents.odfDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        OpenDocument openDoc = Documents.openDocumentOf(Paths.get(""), doc);
        assertNotNull("Parsed OpenDocument should not be null.", openDoc);
        assertFalse("Parsed document should be a package", openDoc.isPackage());
        assertEquals("Document should be a spreadsheet.", Formats.ODS, openDoc.getFormat());
    }

    @Test
    public void testOpenDocInstantiationOfNullPath() throws FileNotFoundException, ParseException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.openDocumentOf(nullPath, pkg);
                });
    }

    @Test
    public void testOpenDocInstantiationOfNullPackage() {
        OdfPackage nullPkg = null;
        Path path = Paths.get("");
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.openDocumentOf(path, nullPkg);
                });
    }

    @Test
    public void testOpenDocInstantiationOfPackage()
            throws ParseException, URISyntaxException, FileNotFoundException {
        PackageParser parser = OdfPackages.getPackageParser();
        File file = new File(TestFiles.EMPTY_ODS.toURI());
        OdfPackage pkg = parser.parsePackage(file);
        OpenDocument openDoc = Documents.openDocumentOf(file.toPath(), pkg);
        assertNotNull("Parsed OpenDocument should not be null.", openDoc);
        assertTrue("Parsed document should be a package", openDoc.isPackage());
        assertEquals("Document should be a spreadsheet.", Formats.ODS, openDoc.getFormat());
    }

    @Test
    public void testOdfDocInstantiationOfNullXmlDocument() {
        OdfXmlDocument nullDoc = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.odfDocumentOf(nullDoc, utils.metadata);
                });
    }

    @Test
    public void testOdfDocInstantiationOfNullXmlDocMetadata()
            throws IOException, ParserConfigurationException, SAXException {
        OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.odfDocumentOf(xmlDoc, null);
                });
    }

    @Test
    public void testOdfDocInstantiationOfXmlDocument() throws IOException, ParserConfigurationException, SAXException {
        OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        OdfDocument odfDoc = Documents.odfDocumentOf(xmlDoc, utils.metadata);
        assertNotNull("Parsed OdfDocument should not be null.", odfDoc);
        assertEquals("Parsed document should same as original", xmlDoc, odfDoc.getXmlDocument());
        assertEquals("Document should be a spreadsheet.", DocumentType.SPREADSHEET, odfDoc.getDocumentType());
    }

    @Test
    public void testOdfDocInstantiationOfNullParseResultWithMetadata() {
        ParseResult nullResult = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.odfDocumentOf(nullResult, utils.metadata);
                });
    }

    @Test
    public void testOdfDocInstantiationOfNullParseResultMetadata()
            throws IOException, ParserConfigurationException, SAXException {
        XmlParser parser = XmlParsers.getNonValidatingParser();
        ParseResult result = parser.parse(TestFiles.EMPTY_FODS.openStream());
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.odfDocumentOf(result, null);
                });
    }

    @Test
    public void testOdfDocInstantiationOfParseResultWithMetadata() throws IOException, ParserConfigurationException, SAXException {
        XmlParser parser = XmlParsers.getNonValidatingParser();
        ParseResult result = parser.parse(TestFiles.EMPTY_FODS.openStream());
        OdfDocument odfDoc = Documents.odfDocumentOf(result, utils.metadata);
        assertNotNull("Parsed OdfDocument should not be null.", odfDoc);
        assertEquals("Parse result of document should same as original", result, odfDoc.getXmlDocument().getParseResult());
        assertEquals("Document should be a spreadsheet.", DocumentType.SPREADSHEET, odfDoc.getDocumentType());
    }

    @Test
    public void testOdfDocInstantiationOfNullParseResult()
            throws IOException, ParserConfigurationException, SAXException {
        ParseResult nullResult = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.odfDocumentOf(nullResult);
                });
    }

    @Test
    public void testOdfDocInstantiationOfParseResult() throws IOException, ParserConfigurationException, SAXException {
        XmlParser parser = XmlParsers.getNonValidatingParser();
        ParseResult result = parser.parse(TestFiles.EMPTY_FODS.openStream());
        OdfDocument odfDoc = Documents.odfDocumentOf(result);
        assertNotNull("Parsed OdfDocument should not be null.", odfDoc);
        assertEquals("Parse result of document should same as original", result, odfDoc.getXmlDocument().getParseResult());
        assertEquals("Document should be a spreadsheet.", DocumentType.SPREADSHEET, odfDoc.getDocumentType());
    }

    @Test
    public void testFrom() throws IOException, ParserConfigurationException, SAXException, ParseException {
        Path path = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.odfDocumentFrom(path);
                });
        Metadata md = OdfXmlDocuments.metadataFrom(TestFiles.EMPTY_FODS.openStream());
        OdfDocument doc = Documents.odfDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", md, doc.getMetadata());
    }

    @Test
    public void testOfDocument() throws IOException, ParserConfigurationException, SAXException, ParseException {
        Metadata md = OdfXmlDocuments.metadataFrom(TestFiles.EMPTY_FODS.openStream());
        OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docFrom = Documents.odfDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docOf = OdfDocumentImpl.of(xmlDoc, md);
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", docOf.getMetadata(), docFrom.getMetadata());
        assertEquals("Expectd synthetic document and parsed document to be equal", xmlDoc, docFrom.getXmlDocument());
    }

    @Test
    public void testOfResult() throws IOException, ParserConfigurationException, SAXException, ParseException {
        XmlParser parser = XmlParsers.getNonValidatingParser();
        Metadata md = OdfXmlDocuments.metadataFrom(TestFiles.EMPTY_FODS.openStream());
        ParseResult result = parser.parse(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docFrom = Documents.odfDocumentFrom(TestFiles.EMPTY_FODS.openStream());
        OdfDocument docOf = OdfDocumentImpl.of(result, md);
        assertEquals("Expected synthetic metadata parsed copy and parsed from document ", docOf.getMetadata(), docFrom.getMetadata());
    }
}
