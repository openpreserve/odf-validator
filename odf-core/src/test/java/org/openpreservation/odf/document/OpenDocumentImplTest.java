package org.openpreservation.odf.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OpenDocumentImplTest {

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(OpenDocumentImpl.class).verify();
    }

    @Test
    public void testOfNullDoc() {
        OdfDocument nullDoc = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OpenDocumentImpl.of(Paths.get(""), nullDoc);
                });
    }

    @Test
    public void testOfNullPkg() {
        OdfPackage nullPkg = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OpenDocumentImpl.of(Paths.get(""), nullPkg);
                });
    }

    @Test
    public void testGetDocumentSingle() throws IOException, ParserConfigurationException, SAXException {
        OdfDocument odfDoc = OdfDocumentImpl.from(TestFiles.EMPTY_FODS.openStream());
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), odfDoc);
        assertEquals("Expected instantiating and return document to be equal", odfDoc, openDoc.getDocument());
    }

    @Test
    public void testGetDocumentPackage() throws IOException, ParseException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), pkg);
        assertEquals("Expected instantiating and return document to be equal", pkg.getDocument(), openDoc.getDocument());
    }

    @Test
    public void testGetFileTypeSingle() throws IOException, ParserConfigurationException, SAXException {
        OdfDocument odfDoc = OdfDocumentImpl.from(TestFiles.EMPTY_FODS.openStream());
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), odfDoc);
        assertEquals(FileType.SINGLE, openDoc.getFileType());
    }

    @Test
    public void testGetFileTypePackage() throws IOException, ParseException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), pkg);
        assertEquals(FileType.PACKAGE, openDoc.getFileType());
    }

    @Test
    public void testGetPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), pkg);
        assertEquals("Expected instantiating and return package to be equal", pkg, openDoc.getPackage());
    }

    @Test
    public void testGetSubDocuments() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), pkg);
        assertEquals("Expected instantiating and return package sub-docs to be equal", pkg.getSubDocument("Configurations2/"), openDoc.getSubDocuments().toArray()[0]);
        assertEquals("Expected one sub-document", 1, openDoc.getSubDocuments().size());
    }

    @Test
    public void testIsPackageSingle() throws IOException, ParserConfigurationException, SAXException {
        OdfDocument odfDoc = OdfDocumentImpl.from(TestFiles.EMPTY_FODS.openStream());
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), odfDoc);
        assertFalse(openDoc.isPackage());
    }

    @Test
    public void testIsPackagePackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
        OpenDocument openDoc = OpenDocumentImpl.of(Paths.get(""), pkg);
        assertTrue(openDoc.isPackage());
    }
}
