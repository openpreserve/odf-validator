package org.openpreservation.format.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ZipArchiveTest {
    Path path = Paths.get("");
    @Test
    public void testInstantiation() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ZipArchiveImpl.from(path, null);
                });

        ZipArchiveImpl archive = ZipArchiveImpl.from(path, new ArrayList<>());
        assertNotNull("Instantiated archive should not be null", archive);
        List<ZipEntry> entries = new ArrayList<>();
        entries.add(null);
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    ZipArchiveImpl.from(path, entries);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ZipArchiveImpl.class).verify();
    }

    @Test
    public void testSize() {
        List<ZipEntry> entries = new ArrayList<>();
        ZipArchiveImpl archive = ZipArchiveImpl.from(path, new ArrayList<>());
        assertEquals("Instantiated archive size should be zero", archive.size(), 0);
        entries.add(ZipEntryImpl.of(""));
        archive = ZipArchiveImpl.from(path, entries);
        assertEquals("Instantiated archive size should be one", archive.size(), 1);
    }

    @Test
    public void testGetZipEntries() {
        List<ZipEntry> entries = new ArrayList<>();
        ZipArchiveImpl archive = ZipArchiveImpl.from(path, new ArrayList<>());
        assertTrue("Retrieved ZipEntry List should be empty", archive.getZipEntries().isEmpty());
        entries.add(ZipEntryImpl.of("test"));
        archive = ZipArchiveImpl.from(path, entries);
        assertFalse("Retrieved ZipEntry List should NOT be empty", archive.getZipEntries().isEmpty());
    }

    @Test
    public void testGetZipEntryNull() {
        List<ZipEntry> entries = new ArrayList<>();
        entries.add(ZipEntryImpl.of("test"));
        ZipArchiveImpl archive = ZipArchiveImpl.from(path, entries);
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    archive.getZipEntry(null);
                });
    }

    @Test
    public void testGetZipEntry() {
        List<ZipEntry> entries = new ArrayList<>();
        ZipArchiveImpl archive = ZipArchiveImpl.from(path, new ArrayList<>());
        assertNull("Retrieved test entry should be null", archive.getZipEntry("test"));
        entries.add(ZipEntryImpl.of("test"));
        archive = ZipArchiveImpl.from(path, entries);
        assertNotNull("Retrieved test entry should NOT be null", archive.getZipEntry("test"));
    }
}
