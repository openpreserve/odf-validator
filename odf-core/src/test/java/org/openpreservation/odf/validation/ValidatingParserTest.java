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
import org.openpreservation.odf.pkg.PackageParser.ParseException;
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
    public void testParsePath() throws ParseException, URISyntaxException, IOException, ParserConfigurationException, SAXException {
        ValidatingParser parser = Validators.getValidatingParser();
        URL resourceUrl = TestFiles.EMPTY_ODS;
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
    public void testParseFile() throws ParseException, IOException, URISyntaxException, ParserConfigurationException, SAXException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()));
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
    public void testParseNullName() throws ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = TestFiles.EMPTY_ODS.openStream();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(is, null);
                });
    }

    @Test
    public void testParseStream() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.EMPTY_ODS.openStream(), TestFiles.EMPTY_ODS.toString());
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet", pkg.getMimeType());
    }

    @Test
    public void testValidPackage() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.EMPTY_ODS.openStream(), TestFiles.EMPTY_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("Empty ODS IS valid", report.isValid());
    }

    @Test
    public void testInValidZip() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.FAKEMIME_TEXT.openStream(), TestFiles.FAKEMIME_TEXT.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("FAKEMIME should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-1")).count() > 0);
    }

    @Test
    public void testBadlyFormedPackage() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.BADLY_FORMED_PKG.openStream(), TestFiles.BADLY_FORMED_PKG.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("BADLY_FORMED_PKG should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-4")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-3")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-7")).count() > 0);
    }

    @Test
    public void testNoManifest() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.NO_MANIFEST_ODS.openStream(), TestFiles.NO_MANIFEST_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("NO_MANIFEST_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-3")).count() > 0);
    }

    @Test
    public void testManifestRootNoMime() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_ROOT_NO_MIME_ODS.openStream(), TestFiles.MANIFEST_ROOT_NO_MIME_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_ROOT_NO_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-4")).count() > 0);
    }

    @Test
    public void testManifestRootRandMimetype() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_RAND_MIMETYPE_ODS.openStream(), TestFiles.MANIFEST_RAND_MIMETYPE_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_RAND_MIMETYPE_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-5")).count() > 0);
    }

    @Test
    public void testManifestRandRootMime() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_RAND_ROOT_MIME_ODS.openStream(), TestFiles.MANIFEST_RAND_ROOT_MIME_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_RAND_ROOT_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-5")).count() > 0);
    }

    @Test
    public void testManifestRootDiffMime() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_DIFF_MIME_ODS.openStream(), TestFiles.MANIFEST_DIFF_MIME_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_DIFF_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-5")).count() > 0);
    }

    @Test
    public void testManifestEmptyRootMime() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_EMPTY_ROOT_MIME_ODS.openStream(), TestFiles.MANIFEST_EMPTY_ROOT_MIME_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_EMPTY_ROOT_MIME_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-5")).count() > 0);
    }

    @Test
    public void testManifestEntry() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_ENTRY_ODS.openStream(), TestFiles.MANIFEST_ENTRY_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-2")).count() > 0);
    }

    @Test
    public void testMimetypeEntry() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MIMETYPE_ENTRY_ODS.openStream(), TestFiles.MIMETYPE_ENTRY_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIMETYPE_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-3")).count() > 0);
    }

    @Test
    public void testMetainfEntry() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.METAINF_ENTRY_ODT.openStream(), TestFiles.METAINF_ENTRY_ODT.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("METAINF_ENTRY_ODT should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-6")).count() > 0);
    }

    @Test
    public void testMissingManifestEntry() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_MISSING_ENTRY_ODS.openStream(), TestFiles.MANIFEST_MISSING_ENTRY_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_MISSING_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-1")).count() > 0);
    }

    @Test
    public void testMissingXmlEntry() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_MISSING_XML_ENTRY_ODS.openStream(), TestFiles.MANIFEST_MISSING_XML_ENTRY_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_MISSING_XML_ENTRY_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-1")).count() > 0);
    }

    @Test
    public void testMissingFile() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MISSING_FILE_ODS.openStream(), TestFiles.MISSING_FILE_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MISSING_FILE_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-4")).count() > 0);
    }

    @Test
    public void testNoMimeWithRoot() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.NO_MIME_ROOT_ODS.openStream(), TestFiles.NO_MIME_ROOT_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("NO_MIME_ROOT_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-4")).count() > 0);
    }

    @Test
    public void testNoRootMimeTyoe() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_NO_ROOT_MIMETYPE_ODS.openStream(), TestFiles.MANIFEST_NO_ROOT_MIMETYPE_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MANIFEST_NO_ROOT_MIMETYPE_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-5")).count() > 0);
    }

    @Test
    public void testNoMimeNoRoot() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.NO_MIME_NO_ROOT_ODS.openStream(), TestFiles.NO_MIME_NO_ROOT_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("NO_MIME_NO_ROOT_ODS should be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-4")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MAN-7")).count() > 0);
    }

    @Test
    public void testMimeLast() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MIME_LAST_ODS.openStream(), TestFiles.MIME_LAST_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_LAST_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-1")).count() > 0);
    }

    @Test
    public void testMimeCompressed() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MIME_COMPRESSED_ODS.openStream(), TestFiles.MIME_COMPRESSED_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_COMPRESSED_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-2")).count() > 0);
    }

    @Test
    public void testMimeCompressedLast() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MIME_COMPRESSED_LAST_ODS.openStream(), TestFiles.MIME_COMPRESSED_LAST_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_COMPRESSED_LAST_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-1")).count() > 0);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-2")).count() > 0);
    }

    @Test
    public void testMimeExtra() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MIME_EXTRA_ODS.openStream(), TestFiles.MIME_EXTRA_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("MIME_EXTRA_ODS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("MIM-3")).count() > 0);
    }

    @Test
    public void testNoThumbnail() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.NO_THUMBNAIL_ODS.openStream(), TestFiles.NO_THUMBNAIL_ODS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("PKG-7")).count() > 0);
    }

    @Test
    public void testNoEmbeddedWord() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.EMBEDDED_WORD.openStream(), TestFiles.EMBEDDED_WORD.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("EMBEDDED_WORD IS valid", report.isValid());
    }

    @Test
    public void testPasswordEncrypted() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.ENCRYPTED_PASSWORDS.openStream(), TestFiles.ENCRYPTED_PASSWORDS.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("ENCRYPTED_PASSWORDS should NOT be valid", report.isValid());
        assertTrue(report.getMessages().stream().filter(m -> m.getId().equals("XML-3")).count() > 0);
    }

    @Test
    public void testDsigValid() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = TestFiles.DSIG_VALID.openStream();
        OdfPackage pkg = parser.parsePackage(is, TestFiles.DSIG_VALID.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertTrue("Package is not valid" , report.isValid());
    }

    @Test
    public void testDsigInvalid() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = TestFiles.DSIG_INVALID.openStream();
        OdfPackage pkg = parser.parsePackage(is, TestFiles.DSIG_INVALID.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("Package should be NOT be valid, dsig file has bad version" , report.isValid());
    }

    @Test
    public void testDsigInvalidBadName() throws ParserConfigurationException, SAXException, IOException, ParseException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = TestFiles.DSIG_BADNAME.openStream();
        OdfPackage pkg = parser.parsePackage(is, TestFiles.DSIG_BADNAME.toString());
        ValidationReport report = parser.validatePackage(pkg);
        assertFalse("Package should be NOT be valid, badly named META-INF file." , report.isValid());
        assertEquals(1, report.getMessages().stream().filter(m -> m.getId().equals("PKG-5")).count());
    }
}
