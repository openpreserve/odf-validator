package org.openpreservation.odf.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.DocumentType;
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
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.openDocumentOf(nullDoc);
                });
    }

    @Test
    public void testOpenDocInstantiationOfDocument() throws IOException, ParserConfigurationException, SAXException {
        OdfDocument doc = OdfDocumentImpl.from(TestFiles.EMPTY_FODS.openStream());
        OpenDocument openDoc = Documents.openDocumentOf(doc);
        assertNotNull("Parsed OpenDocument should not be null.", openDoc);
        assertFalse("Parsed document should be a package", openDoc.isPackage());
        assertEquals("Document should be a spreadsheet.", Formats.ODS, openDoc.getFormat());
    }

    @Test
    public void testOpenDocInstantiationOfNullPackage() {
        OdfPackage nullPkg = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.openDocumentOf(nullPkg);
                });
    }

    @Test
    public void testOpenDocInstantiationOfPackage()
            throws IOException, ParseException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
        OpenDocument openDoc = Documents.openDocumentOf(pkg);
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
        XmlParser parser = new XmlParser();
        ParseResult result = parser.parse(TestFiles.EMPTY_FODS.openStream());
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Documents.odfDocumentOf(result, null);
                });
    }

    @Test
    public void testOdfDocInstantiationOfParseResultWithMetadata() throws IOException, ParserConfigurationException, SAXException {
        XmlParser parser = new XmlParser();
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
        XmlParser parser = new XmlParser();
        ParseResult result = parser.parse(TestFiles.EMPTY_FODS.openStream());
        OdfDocument odfDoc = Documents.odfDocumentOf(result);
        assertNotNull("Parsed OdfDocument should not be null.", odfDoc);
        assertEquals("Parse result of document should same as original", result, odfDoc.getXmlDocument().getParseResult());
        assertEquals("Document should be a spreadsheet.", DocumentType.SPREADSHEET, odfDoc.getDocumentType());
    }
}
