package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OdfPackageTest {

    @Test
    public void testInstantiation() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfPackageImpl.Builder.fromFormat(null);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(OdfPackageImpl.class).verify();
    }

    @Test
    public void testWellFormedZip() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertTrue("Package should be a well formed zip", pkg.isWellFormedZip());
    }

    @Test
    public void testGetName() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertEquals("Package name should be passed String", new File(TestFiles.EMPTY_ODS.toURI()).toPath().getFileName().toString(), pkg.getName());
    }

    @Test
    public void testHasMimeEntry() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        pkg = parser.parsePackage(new File(TestFiles.NO_MIME_ROOT_ODS.toURI()).toPath());
        assertFalse("Package should NOT have a mimetype entry", pkg.hasMimeEntry());
    }

    @Test
    public void testGetFormat() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertEquals(Formats.ODS, pkg.getDetectedFormat());
        pkg = parser.parsePackage(new File(TestFiles.NO_MIME_ROOT_ODS.toURI()).toPath());
        assertEquals(Formats.ZIP, pkg.getDetectedFormat());
    }

    @Test
    public void testHasManifest() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertTrue(pkg.hasManifest());
        pkg = parser.parsePackage(new File(TestFiles.NO_MANIFEST_ODS.toURI()).toPath());
        assertFalse(pkg.hasManifest());
    }

    @Test
    public void testGetManifest() throws ParserConfigurationException, SAXException, IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        Manifest manifest = pkg.getManifest();
        Manifest expected = ManifestImpl
                .from(ClassLoader.getSystemResourceAsStream("org/openpreservation/odf/pkg/manifest.xml"));
        assertEquals(expected, manifest);
    }

    @Test
    public void testGetManifestNoMime()
            throws ParserConfigurationException, SAXException, IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser
                .parsePackage(new File(TestFiles.NO_MIME_ROOT_ODS.toURI()).toPath());
        Manifest manifest = pkg.getManifest();
        Manifest expected = ManifestImpl
                .from(ClassLoader.getSystemResourceAsStream("org/openpreservation/odf/pkg/manifest.xml"));
        assertEquals(expected, manifest);
    }

    @Test
    public void testGetMetadata() throws ParserConfigurationException, SAXException, IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        Metadata metadata = pkg.getDocument().getMetadata();
        Metadata expected = OdfXmlDocuments
                .metadataFrom(ClassLoader.getSystemResourceAsStream("org/openpreservation/odf/pkg/meta.xml"));
        assertEquals(expected, metadata);
    }

    @Test
    public void testIsMimeFirst() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertTrue("MIME entry should be first", pkg.isMimeFirst());
        pkg = parser.parsePackage(new File(TestFiles.MIME_LAST_ODS.toURI()).toPath());
        assertFalse("MIME entry should NOT be first", pkg.isMimeFirst());
        pkg = parser.parsePackage(new File(TestFiles.MIME_COMPRESSED_LAST_ODS.toURI()).toPath());
        assertFalse("MIME entry should NOT be first", pkg.isMimeFirst());
    }

    @Test
    public void testIsMimeCompressed() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        ZipEntry entry = pkg.getZipArchive().getZipEntry(OdfPackages.MIMETYPE);
        assertEquals("MIME entry should be stored", java.util.zip.ZipEntry.STORED, entry.getMethod());
        pkg = parser.parsePackage(new File(TestFiles.MIME_COMPRESSED_ODS.toURI()).toPath());
        entry = pkg.getZipArchive().getZipEntry(OdfPackages.MIMETYPE);
        assertNotEquals("MIME entry should NOT be stored", java.util.zip.ZipEntry.STORED, entry.getMethod());
        pkg = parser.parsePackage(new File(TestFiles.MIME_COMPRESSED_LAST_ODS.toURI()).toPath());
        entry = pkg.getZipArchive().getZipEntry(OdfPackages.MIMETYPE);
        assertNotEquals("MIME entry should be stored", java.util.zip.ZipEntry.STORED, entry.getMethod());
        pkg = parser.parsePackage(new File(TestFiles.MIME_LAST_ODS.toURI()).toPath());
        entry = pkg.getZipArchive().getZipEntry(OdfPackages.MIMETYPE);
        assertEquals("MIME entry should be stored", java.util.zip.ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testHasThumbnail() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertTrue("Package should have a thumbnail", pkg.hasThumbnail());
        pkg = parser.parsePackage(new File(TestFiles.NO_THUMBNAIL_ODS.toURI()).toPath());
        assertFalse("Package should NOT have a thumbnail", pkg.hasThumbnail());
    }

    @Test
    public void testXmlEntryPaths() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.DSIG_INVALID.toURI()).toPath());
        final List<String> entryPaths = Collections.unmodifiableList(pkg.getXmlEntryPaths());
        assertEquals("Package should have 7 XML entries", 7, entryPaths.size());
        assertTrue("Package should have a styles.xml entry", entryPaths.contains("styles.xml"));
        assertTrue("Package should have a content.xml entry", entryPaths.contains("content.xml"));
        assertTrue("Package should have a meta.xml entry", entryPaths.contains("meta.xml"));
        assertTrue("Package should have a settings.xml entry", entryPaths.contains("settings.xml"));
        assertTrue("Package should have a settings.xml entry", entryPaths.contains("manifest.rdf"));
        assertTrue("Package should have a settings.xml entry", entryPaths.contains("META-INF/manifest.xml"));
        assertTrue("Package should have a settings.xml entry", entryPaths.contains("META-INF/documentsignatures.xml"));
    }

    @Test
    public void testXmlParseResults() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        for (String entryPath: pkg.getXmlEntryPaths()) {
            assertNotNull("Package should have a parse result for " + entryPath, pkg.getEntryXmlParseResult(entryPath));
            assertNotNull("Package should have an XML input stream for " + entryPath, pkg.getEntryXmlStream(entryPath));
        }
    }

    @Test
    public void testgetEntryStream() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        for (FileEntry manifestEntry: pkg.getManifest().getEntries()) {
            if (!manifestEntry.isDocument())
                assertNotNull("Package should have an entry stream for " + manifestEntry.getFullPath(), pkg.getEntryStream(manifestEntry));
        }
    }
}
