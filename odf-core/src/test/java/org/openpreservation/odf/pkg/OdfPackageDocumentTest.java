package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.xml.sax.SAXException;
import org.openpreservation.odf.xml.DocumentType;
import org.openpreservation.odf.xml.Version;

public class OdfPackageDocumentTest {
    @Test
    public void testParseDocument() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_ODS);
        OdfPackage pkg = parser.parsePackage(is, TestFiles.EMPTY_ODS);
        OdfPackageDocument doc = pkg.getDocument();
        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have a root entry name", "/", doc.getFileEntry().getFullPath());
        assertEquals("Document mediatype should be equal to mimetype entry", pkg.getMimeType(), doc.getFileEntry().getMediaType());
        assertNotNull("Styles file should be present", doc.getXmlDocument("styles.xml"));
        assertNotNull("Content file should be present", doc.getXmlDocument("content.xml"));
        assertEquals("Retrieved content and document should be equal", doc.getXmlDocument("content.xml"), doc.getXmlDocument());
        assertEquals("Document should have version 1.3", Version.ODF_13, doc.getVersion());
        assertEquals("DocumentType should be spreadsheet", DocumentType.SPREADSHEET, doc.getDocumentType());
        assertEquals("Document should have 5 XML documents", 5, doc.getXmlDocumentMap().size());
    }

    @Test
    public void testParseSubDocument() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_ODS);
        OdfPackage pkg = parser.parsePackage(is, TestFiles.EMPTY_ODS);
        OdfPackageDocument subDoc = pkg.getSubDocument("Configurations2/");
        assertNotNull("Document should not be null", subDoc);
        assertEquals("Document should have a root entry name", "Configurations2/", subDoc.getFileEntry().getFullPath());
        assertEquals("Document mediatype should be equal to mimetype entry", "application/vnd.sun.xml.ui.configuration", subDoc.getFileEntry().getMediaType());
        assertNull("Styles file should NOT be present", subDoc.getXmlDocument("styles.xml"));
        assertNull("Content file should NOT be present", subDoc.getXmlDocument("content.xml"));
        assertNull("Sub-document should have no XML document", subDoc.getXmlDocument());
        assertNull("Sub-document should have null version", subDoc.getVersion());
        assertNull("Sub-document should have null DocumentType", subDoc.getDocumentType());
        assertEquals("Sub-document should have no  XML documents", 0, subDoc.getXmlDocumentMap().size());
    }

    @Test
    public void testParseStylesDocument() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.STYLES_ONLY_DOC);
        OdfPackage pkg = parser.parsePackage(is, TestFiles.STYLES_ONLY_DOC);
        OdfPackageDocument doc = pkg.getDocument();
        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have a root entry name", "/", doc.getFileEntry().getFullPath());
        assertEquals("Document mediatype should be equal to mimetype entry", pkg.getMimeType(), doc.getFileEntry().getMediaType());
        assertNotNull("Styles file should be present", doc.getXmlDocument("styles.xml"));
        assertNull("Content file should NOT be present", doc.getXmlDocument("content.xml"));
        assertEquals("Retrieved content and document should be equal", doc.getXmlDocument("styles.xml"), doc.getXmlDocument());
        assertEquals("Document should have version 1.3", Version.ODF_13, doc.getVersion());
        assertEquals("DocumentType should be spreadsheet", DocumentType.SPREADSHEET, doc.getDocumentType());
        assertEquals("Document should have 4 XML documents", 4, doc.getXmlDocumentMap().size());
    }
}
