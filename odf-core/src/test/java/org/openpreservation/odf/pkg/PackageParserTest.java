package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.odf.fmt.TestFiles;
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
                    parser.parsePackage(file);
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
    public void testParseNullStream() throws ParserConfigurationException, SAXException, IOException {
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
    public void testParsePackagePath() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        Path path = Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath());
        OdfPackage pkg = parser.parsePackage(path);
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet", pkg.getMimeType());
    }

    @Test
    public void testParsePackageFile() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        File file = new File(TestFiles.EMPTY_ODS.toURI());
        OdfPackage pkg = parser.parsePackage(file);
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet", pkg.getMimeType());
    }

    @Test
    public void testParsePackageStream() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        InputStream is = TestFiles.EMPTY_ODS.openStream();
        OdfPackage pkg = parser.parsePackage(is, TestFiles.EMPTY_ODS.toString());
        assertNotNull("Parsed package should not be null", pkg);
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        assertEquals("Mimetype should be Spreadsheet", "application/vnd.oasis.opendocument.spreadsheet", pkg.getMimeType());
    }

    @Test
    public void testDsigValidation() throws ParserConfigurationException, SAXException, IOException {
        PackageParser parser = OdfPackages.getPackageParser();
        InputStream is = TestFiles.DSIG_EXAMPLE.openStream();
        OdfPackage pkg = parser.parsePackage(is, TestFiles.DSIG_EXAMPLE.toString());
        ParseResult result = pkg.getEntryXmlParseResult("META-INF" + File.separator + "documentsignatures.xml");
        assertTrue("Package should have a well formed dsig", result.isWellFormed());
    }
}
