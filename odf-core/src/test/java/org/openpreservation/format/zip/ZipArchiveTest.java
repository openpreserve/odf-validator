package org.openpreservation.format.zip;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

import org.junit.Test;

public class ZipArchiveTest {
    @Test
    public void testInstantiation() {
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    ZipArchiveImpl.from(null);
                });

        ZipArchiveImpl archive = ZipArchiveImpl.from(new ArrayList<>());
        assertNotNull("Instantiated archive should not be null", archive);
        List<ZipEntry> entries = new ArrayList<>();
        entries.add(null);
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    ZipArchiveImpl.from(entries);
                });
    }

    @Test
    public void testSize() {
        List<ZipEntry> entries = new ArrayList<>();
        ZipArchiveImpl archive = ZipArchiveImpl.from(new ArrayList<>());
        assertTrue("Instantiated archive size should be zero", archive.size() == 0);
        entries.add(new ZipEntry(""));
        archive = ZipArchiveImpl.from(entries);
        assertTrue("Instantiated archive size should be one", archive.size() == 1);
    }

    @Test
    public void testGetZipEntries() {
        List<ZipEntry> entries = new ArrayList<>();
        ZipArchiveImpl archive = ZipArchiveImpl.from(new ArrayList<>());
        assertTrue("Retrieved ZipEntry List should be empty", archive.getZipEntries().isEmpty());
        entries.add(new ZipEntry("test"));
        archive = ZipArchiveImpl.from(entries);
        assertFalse("Retrieved ZipEntry List should NOT be empty", archive.getZipEntries().isEmpty());
    }

    @Test
    public void testGetZipEntry() {
        List<ZipEntry> entries = new ArrayList<>();
        ZipArchiveImpl archive = ZipArchiveImpl.from(new ArrayList<>());
        assertNull("Retrieved test entry should be null", archive.getZipEntry("test"));
        entries.add(new ZipEntry("test"));
        archive = ZipArchiveImpl.from(entries);
        assertNotNull("Retrieved test entry should NOT be null", archive.getZipEntry("test"));
    }
}
