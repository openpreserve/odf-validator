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
        assertNotNull(doc.getXmlDocument("styles.xml"));
        assertNotNull(doc.getXmlDocument("content.xml"));
        assertEquals(doc.getXmlDocument("content.xml"), doc.getXmlDocument());
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
        assertNull(subDoc.getXmlDocument("styles.xml"));
        assertNull(subDoc.getXmlDocument("content.xml"));
        assertNull(subDoc.getXmlDocument());
    }
}
