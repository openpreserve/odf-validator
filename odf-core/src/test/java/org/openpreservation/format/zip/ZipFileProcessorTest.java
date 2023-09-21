package org.openpreservation.format.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;

public class ZipFileProcessorTest {
    private static final URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
    // private static final String emptyPath = empty.getPath();

    @Test
    public void testInstantiation() throws IOException, URISyntaxException {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ZipFileProcessor.of(null);
                });
        Path path = Paths.get("test");
        assertFalse("Path should not exist", Files.exists(path));
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    ZipFileProcessor.of(path);
                });
        ZipFileProcessor processor = ZipFileProcessor.of(Paths.get(empty.toURI()));
        assertNotNull("Instantiated processor should not be null.", processor);
    }

    @Test
    public void testInstantiationZips() throws IOException, URISyntaxException {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    Zips.zipArchiveCacheInstance(null);
                });
        Path path = Paths.get("test");
        assertFalse("Path should not exist", Files.exists(path));
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    Zips.zipArchiveCacheInstance(path);
                });
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(Paths.get(empty.toURI()));
        assertNotNull("Instantiated processor should not be null.", processor);
    }

    @Test
    public void testSize() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(Paths.get(empty.toURI()));
        assertNotNull("Instantiated processor should not be null.", processor);
        assertTrue("Size should be greater than 0", processor.size() > 0);
    }

    @Test
    public void testGetZipEntries() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(Paths.get(empty.toURI()));
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
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(Paths.get(empty.toURI()));
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    processor.getZipEntry(null);
                });
    }

    @Test
    public void testGetZipEntry() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(Paths.get(empty.toURI()));
        assertNull("Retrieved test entry should be null", processor.getZipEntry("test"));
        assertNotNull("Retrieved settings.xml entry should NOT be null", processor.getZipEntry("settings.xml"));
    }

    @Test
    public void testGetCachedEntryNames() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(Paths.get(empty.toURI()));
        List<String> entries = processor.getCachedEntryNames();
        assertNotNull("Cached entries should be an empty list", entries);
        assertTrue("Cached entries should be an empty list", entries.isEmpty());
        processor.getEntryInputStream("settings.xml");
        entries = processor.getCachedEntryNames();
        assertEquals("Cached entries should have a single entry empty list", 1, entries.size());
    }

    @Test
    public void testGetCachedEntry() throws IOException, URISyntaxException {
        ZipArchiveCache processor = Zips.zipArchiveCacheInstance(Paths.get(empty.toURI()));
        InputStream is = processor.getEntryInputStream("mimetype");
        assertNotNull("Cached entry stream should be not be null.", is);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            is.transferTo(bos);
            String mimetype = bos.toString();
            assertEquals("Mimetype string should id a spreadsheet.", "application/vnd.oasis.opendocument.spreadsheet",
                    mimetype);
        }
    }
}
