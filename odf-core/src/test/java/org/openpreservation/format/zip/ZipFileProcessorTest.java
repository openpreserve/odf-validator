package org.openpreservation.format.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;

public class ZipFileProcessorTest {

    @Test
    public void testInstantiation() throws IOException, URISyntaxException {
        File nullFile = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ZipFileProcessor.of(nullFile);
                });
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ZipFileProcessor.of(nullPath);
                });
        Path missing = Paths.get("test");
        assertFalse("Path should not exist", Files.exists(missing));
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    ZipFileProcessor.of(missing);
                });
        Path dir = Paths.get(".");
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    ZipFileProcessor.of(dir);
                });
        ZipFileProcessor processor = ZipFileProcessor.of(new File(TestFiles.EMPTY_ODS.toURI()));
        assertNotNull("Instantiated processor should not be null.", processor);
    }

    @Test
    public void testInstantiationZips() throws IOException, URISyntaxException {
        File nullFile = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Zips.zipArchiveCacheInstance(nullFile);
                });
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Zips.zipArchiveCacheInstance(nullPath);
                });
        Path path = Paths.get("test");
        assertFalse("Path should not exist", Files.exists(path));
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    Zips.zipArchiveCacheInstance(path);
                });
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        assertNotNull("Instantiated processor should not be null.", processor);
    }

    @Test
    public void testSize() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        assertNotNull("Instantiated processor should not be null.", processor);
        assertTrue("Size should be greater than 0", processor.size() > 0);
    }

    @Test
    public void testGetZipEntries() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        List<ZipEntry> entries = processor.getZipEntries();
        assertNotNull("Retrieved entries should not be null", entries);
        assertFalse("Retrieved entries should not be empty", entries.isEmpty());
        for (ZipEntry entry : entries) {
            assertNotNull("Entry should not be null", entry);
            ZipEntry retrieved = processor.getZipEntry(entry.getName());
            assertNotNull("Retrieved Entry should not be null", retrieved);
        }
    }

    @Test
    public void testGetZipEntryNull() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    processor.getZipEntry(null);
                });
    }

    @Test
    public void testGetZipEntry() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        assertNull("Retrieved test entry should be null", processor.getZipEntry("test"));
        assertNotNull("Retrieved settings.xml entry should NOT be null", processor.getZipEntry("settings.xml"));
    }

    @Test
    public void testGetCachedEntry() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        InputStream is = processor.getEntryInputStream("mimetype");
        assertNotNull("Cached entry stream should be not be null.", is);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            is.transferTo(bos);
            String mimetype = bos.toString();
            assertEquals("Mimetype string should id a spreadsheet.", "application/vnd.oasis.opendocument.spreadsheet", mimetype);
        }
    }

    @Test
    public void testGetEntryInputStream() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        InputStream is = processor.getEntryInputStream("mimetype");
        assertNotNull("Cached entry stream should not be null.", is);
    }

    @Test
    public void testGetMisssingEntryInputStream() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(new File(TestFiles.EMPTY_ODS.toURI()));
        assertThrows("NoSuchElementException expected",
                NoSuchElementException.class,
                () -> {
                    processor.getEntryInputStream("notThere");
                });
    }
}
