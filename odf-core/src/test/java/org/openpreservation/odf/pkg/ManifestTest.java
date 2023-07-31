package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ManifestTest {
    @Test
    public void testInstantiation() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ManifestImpl.from(null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ManifestImpl.of("1.2", null);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ManifestImpl.class).verify();
    }

    @Test
    public void testGetVersion() {
        Manifest manifest = ManifestImpl.of("1.2", new HashMap<>());
        assertEquals("Version should be 1.2", "1.2", manifest.getVersion());
        manifest = ManifestImpl.of(null, new HashMap<>());
        assertNull("Version should be null", manifest.getVersion());
    }

    @Test
    public void testGetRootMediaTypeNoRoot() {
        Manifest manifest = ManifestImpl.of("1.2", new HashMap<>());
        assertNull(manifest.getRootMediaType());
    }

    @Test
    public void testGetNoRootMediaType() {
        FileEntry entry = FileEntryImpl.of("/");
        Map<String, FileEntry> entries = new HashMap<>();
        entries.put(entry.getFullPath(), entry);
        Manifest manifest = ManifestImpl.of("1.2", entries);
        assertNull(manifest.getRootMediaType());
    }

    @Test
    public void testGetRootMediaType() {
        FileEntry entry = FileEntryImpl.of("/", "application/vnd.oasis.opendocument.text");
        Map<String, FileEntry> entries = new HashMap<>();
        entries.put(entry.getFullPath(), entry);
        Manifest manifest = ManifestImpl.of("1.2", entries);
        assertEquals("application/vnd.oasis.opendocument.text", manifest.getRootMediaType());
    }

    @Test
    public void testGetEntriesEmpty() {
        Manifest manifest = ManifestImpl.of("1.2", new HashMap<>());
        assertTrue("Entries should be empty", manifest.getEntries().isEmpty());
    }

    @Test
    public void testGetEntries() {
        Map<String, FileEntry> entries = new HashMap<>();
        FileEntry entry = FileEntryImpl.of("/", "application/vnd.oasis.opendocument.text");
        entries.put(entry.getFullPath(), entry);
        entry = FileEntryImpl.of("mimetype", "plain/text");
        entries.put(entry.getFullPath(), entry);
        Manifest manifest = ManifestImpl.of("1.2", entries);
        assertFalse("Entries should NOT be empty", manifest.getEntries().isEmpty());
        assertEquals("Entries should contain two entries", 2, manifest.getEntries().size());
    }

    @Test
    public void testGetEntry() {
        Map<String, FileEntry> entries = new HashMap<>();
        FileEntry entry = FileEntryImpl.of("/", "application/vnd.oasis.opendocument.text");
        entries.put(entry.getFullPath(), entry);
        entry = FileEntryImpl.of("mimetype", "plain/text");
        entries.put(entry.getFullPath(), entry);
        Manifest manifest = ManifestImpl.of("1.2", entries);
        assertNotNull("Entry should not be null", manifest.getEntry("/"));
        assertNotNull("Entry should not be null", manifest.getEntry("mimetype"));
        assertNull("Entry should be null", manifest.getEntry("/mimetype"));
    }

    @Test
    public void testGetEntriesByMediaType() {
        Map<String, FileEntry> entries = new HashMap<>();
        FileEntry entry = FileEntryImpl.of("/", "application/vnd.oasis.opendocument.text");
        entries.put(entry.getFullPath(), entry);
        entry = FileEntryImpl.of("mimetype", "plain/text");
        entries.put(entry.getFullPath(), entry);
        Manifest manifest = ManifestImpl.of("1.2", entries);
        assertNotNull("Entries should not be null", manifest.getEntriesByMediaType("test/nothing"));
        assertTrue("Entries should be empty", manifest.getEntriesByMediaType("test/nothing").isEmpty());
        assertFalse("Entries should NOT be empty", manifest.getEntriesByMediaType("plain/text").isEmpty());
        assertFalse("Entries should NOT be empty",
                manifest.getEntriesByMediaType("application/vnd.oasis.opendocument.text").isEmpty());
    }

    @Test
    public void testFromInputStream() throws ParserConfigurationException, SAXException, IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream("org/openpreservation/odf/pkg/manifest.xml");
        Manifest manifest = ManifestImpl.from(is);
        assertEquals("Manifest version should be 1.3", "1.3", manifest.getVersion());
        assertEquals("The file should contain 8 file entries", 8, manifest.getEntryCount());
        assertEquals("The file should contain 4 XML file entries", 4,
                manifest.getEntriesByMediaType("text/xml").size());
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", manifest.getRootMediaType());
        assertEquals("Root version should be 1.3", "1.3", manifest.getRootVersion());
        assertEquals("Thumbnail should havem media type for PNG", "image/png",
                manifest.getEntryMediaType("Thumbnails/thumbnail.png"));
        assertNull("Media type for non existing entry should be null",
                manifest.getEntryMediaType("Thumbnails/thumbnail.pngx"));
    }
}
