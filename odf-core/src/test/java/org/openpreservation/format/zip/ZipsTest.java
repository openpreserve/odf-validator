package org.openpreservation.format.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackages;

public class ZipsTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testFactoryInstance() {
        assertNotNull("Factory should not return a null instance", Zips.factoryInstance());
    }

    @Test
    public void testExtractorInstance() throws IOException {
        assertThrows("IllegalArgumentException expected",
                NullPointerException.class,
                () -> {
                    Zips.extractorInstance(null, false);
                });
        Path tempFolder = temporaryFolder.newFolder("extInstTest").toPath();
        assertNotNull("Extractor should not return a null instance", Zips.extractorInstance(tempFolder, false));
        assertNotNull("Extractor should not return a null instance", Zips.extractorInstance(tempFolder, true));
    }

    @Test
    public void testZipEntryInstance() {
    final String name = null;
    byte[] extra = new byte[0];
    assertThrows("NullPointerException expected",
            NullPointerException.class,
            () -> {
                ZipEntryImpl.of(name, 0, 0, 0, 0, false, extra);
            });
    }

    @Test
    public void testAccepts() throws IOException {
        Path tempFolder = temporaryFolder.newFolder("acceptsTest").toPath();
        ZipEntry fileEntry = new ZipEntry("file");
        ZipEntry dirEntry = new ZipEntry("dir/");
        assertFalse(fileEntry.isDirectory());
        assertTrue(dirEntry.isDirectory());
        ZipEntryProcessor dirProcessor = Zips.extractorInstance(tempFolder, true);
        assertTrue(dirProcessor.accepts(fileEntry));
        assertTrue(dirProcessor.accepts(dirEntry));
        ZipEntryProcessor notDirProcessor = Zips.extractorInstance(tempFolder, false);
        assertTrue(notDirProcessor.accepts(fileEntry));
        assertFalse(notDirProcessor.accepts(dirEntry));
    }

    @Test
    public void testProcessNoDir() throws IOException, URISyntaxException {
        Path tempFolder = temporaryFolder.newFolder("emptyTestNoDirs").toPath();
        ZipEntryProcessor notDirProcessor = Zips.extractorInstance(tempFolder, false);
        ZipProcessor processor = Zips.factoryInstance().from(notDirProcessor);
        InputStream is = new FileInputStream(new File(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        assertNotNull(is);
        ZipArchive archive = processor.process(is);
        assertNotNull("Archive should not be null", archive);
        assertTrue("Archive should not be empty", archive.size() > 0);
        assertTrue(Files.isRegularFile(tempFolder.resolve("mimetype")));
        assertFalse(Files.isDirectory(tempFolder.resolve("Configurations2")));
    }

    @Test
    public void testProcessDir() throws IOException, URISyntaxException {
        Path tempFolder = temporaryFolder.newFolder("emptyTestDirs").toPath();
        ZipEntryProcessor notDirProcessor = Zips.extractorInstance(tempFolder, true);
        ZipProcessor processor = Zips.factoryInstance().from(notDirProcessor);
        InputStream is = new FileInputStream(new File(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        assertNotNull(is);
        ZipArchive archive = processor.process(is);
        assertNotNull("Archive should not be null", archive);
        assertTrue("Archive should not be empty", archive.size() > 0);
        assertTrue(Files.isRegularFile(tempFolder.resolve("mimetype")));
        assertTrue(Files.isDirectory(tempFolder.resolve("Configurations2")));
        assertEquals("First entry name should be mimetype", OdfPackages.MIMETYPE, archive.getFirstEntry().getName());
    }
}
