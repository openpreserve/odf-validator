package org.openpreservation.utils;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ChecksTest {
    @Test
    public void testNotNull() {
        assertThrows(NullPointerException.class, () -> Checks.notNull(null, "String", "toSniff"));
    }
}
