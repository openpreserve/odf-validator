package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.TestFiles;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OdfPackageTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

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
    public void testHasMimeEntry() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        assertTrue("Package should have a mimetype entry", pkg.hasMimeEntry());
        pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.NO_MIME_ODS).toURI()));
        assertFalse("Package should NOT have a mimetype entry", pkg.hasMimeEntry());
    }

    @Test
    public void testGetFormat() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        assertEquals(Formats.ODS, pkg.getFormat());
        pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.NO_MIME_ODS).toURI()));
        assertEquals(Formats.ZIP, pkg.getFormat());
    }

    @Test
    public void testHasManifest() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        assertTrue(pkg.hasManifest());
    }

    @Test
    public void testGetManifest() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        Manifest manifest = pkg.getManifest();
        Manifest expected = ManifestImpl.from(ClassLoader.getSystemResourceAsStream("org/openpreservation/odf/pkg/manifest.xml"));
        assertEquals(expected, manifest);
    }

    @Test
    public void testGetMetadata() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        PackageParser parser = PackageParserImpl.getInstance();
        OdfPackage pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        Metadata metadata = pkg.getMetadata();
        Metadata expected = MetadataImpl.from(ClassLoader.getSystemResourceAsStream("org/openpreservation/odf/pkg/meta.xml"));
        assertEquals(expected, metadata);
    }

}
