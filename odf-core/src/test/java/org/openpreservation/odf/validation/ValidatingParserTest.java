package org.openpreservation.odf.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.xml.sax.SAXException;

public class ValidatingParserTest {
    @Test
    public void testParseNullPath() throws ParserConfigurationException, SAXException {
        ValidatingParser parser = Validators.getValidatingParser();
        Path path = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(path);
                });
    }

    @Test
    public void testParsePath() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        URL resourceUrl = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        Path path = Paths.get(resourceUrl.toURI());
        OdfPackage pkg = parser.parsePackage(path);
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet", pkg.getMimeType());
    }

    @Test
    public void testParseNullFile() throws ParserConfigurationException, SAXException {
        ValidatingParser parser = Validators.getValidatingParser();
        File file = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(file);
                });
    }

    @Test
    public void testParseFile() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        ValidatingParser parser = Validators.getValidatingParser();
        URL resourceUrl = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        File file = new File(resourceUrl.toURI());
        OdfPackage pkg = parser.parsePackage(file);
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet", pkg.getMimeType());
    }

    @Test
    public void testParseNullStream() throws ParserConfigurationException, SAXException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(is, "test");
                });
    }

    @Test
    public void testParseNullName() throws ParserConfigurationException, SAXException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_ODS);
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(is, null);
                });
    }

    @Test
    public void testParseStream() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_ODS);
        OdfPackage pkg = parser.parsePackage(is, TestFiles.EMPTY_ODS);
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet", pkg.getMimeType());
    }

    @Test
    public void testValidPackage() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_ODS), TestFiles.EMPTY_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("Empty ODS should be valid", report.isValid());
    }

    @Test
    public void testInValidZip() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.FAKEMIME_TEXT), TestFiles.FAKEMIME_TEXT);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("FAKEMIME should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-9")).count() > 0);
    }

    @Test
    public void testBadlyFormedPackage() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.BADLY_FORMED_PKG), TestFiles.BADLY_FORMED_PKG);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("BADLY_FORMED_PKG should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-2")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-4")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-18")).count() > 0);
    }

    @Test
    public void testNoManifest() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.NO_MANIFEST_ODS), TestFiles.NO_MANIFEST_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("NO_MANIFEST_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-4")).count() > 0);
    }

    @Test
    public void testManifestRootNoMime() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_ROOT_NO_MIME_ODS), TestFiles.MANIFEST_ROOT_NO_MIME_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_ROOT_NO_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-10")).count() > 0);
    }

    @Test
    public void testManifestRootRandMimetype() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_RAND_MIMETYPE_ODS), TestFiles.MANIFEST_RAND_MIMETYPE_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_RAND_MIMETYPE_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-12")).count() > 0);
    }

    @Test
    public void testManifestRandRootMime() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_RAND_ROOT_MIME_ODS), TestFiles.MANIFEST_RAND_ROOT_MIME_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_RAND_ROOT_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-12")).count() > 0);
    }

    @Test
    public void testManifestRootDiffMime() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_DIFF_MIME_ODS), TestFiles.MANIFEST_DIFF_MIME_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_DIFF_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-12")).count() > 0);
    }

    @Test
    public void testManifestEmptyRootMime() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_EMPTY_ROOT_MIME_ODS), TestFiles.MANIFEST_EMPTY_ROOT_MIME_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_EMPTY_ROOT_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-12")).count() > 0);
    }

    @Test
    public void testManifestEntry() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_ENTRY_ODS), TestFiles.MANIFEST_ENTRY_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-14")).count() > 0);
    }

    @Test
    public void testMimetypeEntry() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MIMETYPE_ENTRY_ODS), TestFiles.MIMETYPE_ENTRY_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIMETYPE_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-15")).count() > 0);
    }

    @Test
    public void testMetainfEntry() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.METAINF_ENTRY_ODT), TestFiles.METAINF_ENTRY_ODT);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("METAINF_ENTRY_ODT should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-13")).count() > 0);
    }

    @Test
    public void testMissingManifestEntry() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_MISSING_ENTRY_ODS), TestFiles.MANIFEST_MISSING_ENTRY_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_MISSING_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-17")).count() > 0);
    }

    @Test
    public void testMissingXmlEntry() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_MISSING_XML_ENTRY_ODS), TestFiles.MANIFEST_MISSING_XML_ENTRY_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_MISSING_XML_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-17")).count() > 0);
    }

    @Test
    public void testMissingFile() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MISSING_FILE_ODS), TestFiles.MISSING_FILE_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MISSING_FILE_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-16")).count() > 0);
    }

    @Test
    public void testNoMimeWithRoot() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.NO_MIME_ROOT_ODS), TestFiles.NO_MIME_ROOT_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("NO_MIME_ROOT_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-10")).count() > 0);
    }

    @Test
    public void testNoRootMimeTyoe() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MANIFEST_NO_ROOT_MIMETYPE_ODS), TestFiles.MANIFEST_NO_ROOT_MIMETYPE_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_NO_ROOT_MIMETYPE_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-11")).count() > 0);
    }

    @Test
    public void testNoMimeNoRoot() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.NO_MIME_NO_ROOT_ODS), TestFiles.NO_MIME_NO_ROOT_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("NO_MIME_NO_ROOT_ODS should be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-2")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-19")).count() > 0);
    }

    @Test
    public void testMimeLast() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MIME_LAST_ODS), TestFiles.MIME_LAST_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_LAST_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-7")).count() > 0);
    }

    @Test
    public void testMimeCompressed() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MIME_COMPRESSED_ODS), TestFiles.MIME_COMPRESSED_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_COMPRESSED_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-6")).count() > 0);
    }

    @Test
    public void testMimeCompressedLast() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MIME_COMPRESSED_LAST_ODS), TestFiles.MIME_COMPRESSED_LAST_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_COMPRESSED_LAST_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-7")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-6")).count() > 0);
    }

    @Test
    public void testMimeExtra() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.MIME_EXTRA_ODS), TestFiles.MIME_EXTRA_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_EXTRA_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-8")).count() > 0);
    }

    @Test
    public void testNoThumbnail() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.NO_THUMBNAIL_ODS), TestFiles.NO_THUMBNAIL_ODS);
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("NO_THUMBNAIL_ODS should be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-18")).count() > 0);
    }

    @Test
    public void testNoEmbeddedWord() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.EMBEDDED_WORD), TestFiles.EMBEDDED_WORD);
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("EMBEDDED_WORD should be valid", report.isValid());
    }

    @Test
    public void testPasswordEncrypted() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(ClassLoader.getSystemResourceAsStream(TestFiles.ENCRYPTED_PASSWORDS), TestFiles.ENCRYPTED_PASSWORDS);
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("ENCRYPTED_PASSWORDS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("XML-3")).count() > 0);
    }
}
