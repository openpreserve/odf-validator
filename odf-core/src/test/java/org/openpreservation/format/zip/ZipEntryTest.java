package org.openpreservation.format.zip;

import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ZipEntryTest {
    @Test
    public void testInstantiation() {
        final String name = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ZipEntryImpl.of(name);
                });
        final ZipArchiveEntry entry = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ZipEntryImpl.of(entry);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ZipEntryImpl.of(null, 0, 0, 0, 0, false, null);
                });
    }
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ZipEntryImpl.class).verify();
    }

}
