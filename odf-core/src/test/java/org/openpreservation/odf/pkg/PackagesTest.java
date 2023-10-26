package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;

public class PackagesTest {
    @Test
    public void testInstantiation() {
        PackageParser parser = OdfPackages.getPackageParser();
        assertNotNull("Parser instantiation should not be null.", parser);
    }

    @Test
    public void testIsValidZipNullPath() {
        Path path = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfPackages.isValidZip(path);
                });
    }

    @Test
    public void testIsZip() throws URISyntaxException, IOException {
        URL resourceUrl = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        Path path = Paths.get(resourceUrl.toURI());
        assertTrue("Empty test file should be a zip file.", OdfPackages.isZip(path));
    }

    @Test
    public void testIsFakeZip() throws URISyntaxException, IOException {
        URL resourceUrl = ClassLoader.getSystemResource(TestFiles.FAKEMIME_TEXT);
        Path path = Paths.get(resourceUrl.toURI());
        assertTrue("Fake MIME test file should be a valid zip file.", OdfPackages.isZip(path));
    }

    @Test
    public void testIsNotZip() throws URISyntaxException, IOException {
        URL resourceUrl = ClassLoader.getSystemResource(TestFiles.FLAT_NOT_WF);
        Path path = Paths.get(resourceUrl.toURI());
        assertFalse("Flat XML test file should NOT be a zip file.", OdfPackages.isZip(path));
    }

    @Test
    public void testIsValidZip() throws URISyntaxException, IOException {
        URL resourceUrl = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        Path path = Paths.get(resourceUrl.toURI());
        assertTrue("Empty test file should be a valid zip.", OdfPackages.isValidZip(path));
    }

    @Test
    public void testInValidZip() throws URISyntaxException, IOException {
        URL resourceUrl = ClassLoader.getSystemResource(TestFiles.FAKEMIME_TEXT);
        Path path = Paths.get(resourceUrl.toURI());
        assertFalse("Fake MIME test file should NOT be a valid zip.", OdfPackages.isValidZip(path));
    }
}
