package org.openpreservation.odf.pkg;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class FileEntryTest {
    @Test
    public void testInstantiation() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FileEntryImpl.of(null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FileEntryImpl.of(null, null);
                });
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FileEntryImpl.of("", null);
                });
    }
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(FileEntryImpl.class).verify();
    }

}
