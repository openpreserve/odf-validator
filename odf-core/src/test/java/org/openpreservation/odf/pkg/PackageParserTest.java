package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.Version;
import org.xml.sax.SAXException;

public class PackageParserTest {
    @Test
    public void testInstantiation() throws ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        assertNotNull("Instantiated parser should not be null", parser);
    }

    @Test
    public void testParseNullFile() throws ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        File file = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(file.toPath());
                });
    }

    @Test
    public void testParseMissingFile() throws ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        File file = new File("missingFileRubbishdhbna");
        assertThrows("FileNotFoundException expected",
                FileNotFoundException.class,
                () -> {
                    parser.parsePackage(file.toPath());
                });
    }

    @Test
    public void testParseDirFile() throws ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        File file = new File(".");
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    parser.parsePackage(file.toPath());
                });
    }

    @Test
    public void testParseNullPath() throws ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        Path path = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(path);
                });
    }

    @Test
    public void testParseMissingPath() throws ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        Path path = Path.of(".", "missingFileRubbishdhbna");
        assertThrows("FileNotFoundException expected",
                FileNotFoundException.class,
                () -> {
                    parser.parsePackage(path);
                });
    }

    @Test
    public void testParseDirPath() throws ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        Path path = Path.of(".");
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    parser.parsePackage(path);
                });
    }

    @Test
    public void testParseNullStream() throws ParseException, SAXException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(null, "name");
                });
        InputStream is = TestFiles.EMPTY_ODS.openStream();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parser.parsePackage(is, null);
                });
    }

    @Test
    public void testParsePackagePath()
            throws ParseException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath()));
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet",
                pkg.getMimeType());
    }

    @Test
    public void testParsePackageFile()
            throws ParseException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet",
                pkg.getMimeType());
    }

    @Test
    public void testParsePackageStream()
            throws ParseException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.EMPTY_ODS.openStream(), TestFiles.EMPTY_ODS.toString());
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet",
                pkg.getMimeType());
    }

    @Test
    public void testDsigParsing() throws IOException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.DSIG_INVALID.openStream(), TestFiles.DSIG_INVALID.toString());
        ParseResult result = pkg.getEntryXmlParseResult("META-INF/documentsignatures.xml");
        assertNotNull("Dsig file META-INF/documentsignatures.xml result should not be null", result);
        assertTrue("Package should have a well formed dsig for META-INF/documentsignatures.xml", result.isWellFormed());
    }

    @Test
    public void testManifestNotWF() throws IOException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.MANIFEST_NOT_WF.openStream(),
                TestFiles.MANIFEST_NOT_WF.toString());
        ParseResult result = pkg.getEntryXmlParseResult("META-INF/manifest.xml");
        assertNotNull("Dsig file META-INF/documentsignatures.xml result should not be null", result);
        assertFalse("Package should NOT have a well formed META-INF/manifest.xml", result.isWellFormed());
    }

    @Test
    public void testVersionDetection() throws IOException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(TestFiles.VER_1_3_ODS.openStream(),
                TestFiles.VER_1_3_ODS.toString());
        assertEquals("Package should be version 1.3", Version.ODF_13, pkg.getDetectedVersion());
        pkg = parser.parsePackage(TestFiles.VER_1_2_ODS.openStream(),
                TestFiles.VER_1_2_ODS.toString());
        assertEquals("Package should be version 1.2", Version.ODF_12, pkg.getDetectedVersion());
        pkg = parser.parsePackage(TestFiles.VER_1_1_ODS.openStream(),
                TestFiles.VER_1_1_ODS.toString());
        assertEquals("Package should be version 1.1", Version.ODF_11, pkg.getDetectedVersion());
    }
}
