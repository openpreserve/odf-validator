package org.openpreservation.format.zip;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ZipsTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testFactoryInstance() {
        assertNotNull("Factory should not return a null instance", Zips.getFactoryInstance());
    }

    @Test
    public void testExtractorInstance() throws IOException {
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    Zips.getExtractorInstance(null, false);
                });
        Path tempFolder = temporaryFolder.newFolder("extInstTest").toPath();
        assertNotNull("Extractor should not return a null instance", Zips.getExtractorInstance(tempFolder, false));
        assertNotNull("Extractor should not return a null instance", Zips.getExtractorInstance(tempFolder, true));
    }

    @Test
    public void testAccepts() throws IOException {
        Path tempFolder = temporaryFolder.newFolder("acceptsTest").toPath();
        ZipEntry fileEntry = new ZipEntry("file");
        ZipEntry dirEntry = new ZipEntry("dir/");
        assertFalse(fileEntry.isDirectory());
        assertTrue(dirEntry.isDirectory());
        ZipEntryProcessor dirProcessor = Zips.getExtractorInstance(tempFolder, true);
        assertTrue(dirProcessor.accepts(fileEntry));
        assertTrue(dirProcessor.accepts(dirEntry));
        ZipEntryProcessor notDirProcessor = Zips.getExtractorInstance(tempFolder, false);
        assertTrue(notDirProcessor.accepts(fileEntry));
        assertFalse(notDirProcessor.accepts(dirEntry));
    }

    @Test
    public void testProcess() throws IOException, URISyntaxException {
        Path tempFolder = temporaryFolder.newFolder("emptyTestNoDirs").toPath();
        ZipEntryProcessor notDirProcessor = Zips.getExtractorInstance(tempFolder, false);
        ZipProcessor processor = Zips.getFactoryInstance().from(notDirProcessor);
        InputStream is = new FileInputStream(new File(ClassLoader.getSystemResource(TestFiles.EMPTY_ODS).toURI()));
        assertNotNull(is);
        ZipArchive archive = processor.process(is);
        assertNotNull("Archive should not be null", archive);
        assertTrue("Archive should not be empty", archive.size() > 0);
    }

}
