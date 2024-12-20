package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.openpreservation.odf.Source;
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
                    Source.isValidZip(path);
                });
    }

    @Test
    public void testIsZip() throws URISyntaxException, IOException {
        Path path = Paths.get(new File(TestFiles.FAKEMIME_TEXT.toURI()).getAbsolutePath());
        assertTrue("Empty test file should be a zip file.", Source.isZip(path));
    }

    @Test
    public void testIsFakeZip() throws URISyntaxException, IOException {
        Path path = Paths.get(TestFiles.FAKEMIME_TEXT.toURI());
        assertTrue("Fake MIME test file should be a valid zip file.", Source.isZip(path));
    }

    @Test
    public void testIsNotZip() throws URISyntaxException, IOException {
        Path path = Paths.get(TestFiles.FLAT_NOT_WF.toURI());
        assertFalse("Flat XML test file should NOT be a zip file.", Source.isZip(path));
    }

    @Test
    public void testIsValidZip() throws URISyntaxException, IOException {
        Path path = Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath());
        assertTrue("Empty test file should be a valid zip.", Source.isValidZip(path));
    }

    @Test
    public void testInValidZip() throws URISyntaxException, IOException {
        Path path = Paths.get(TestFiles.FAKEMIME_TEXT.toURI());
        assertFalse("Fake MIME test file should NOT be a valid zip.", Source.isValidZip(path));
    }
}
