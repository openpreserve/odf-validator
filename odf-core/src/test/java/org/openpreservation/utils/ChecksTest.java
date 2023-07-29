package org.openpreservation.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class ChecksTest {
    final static String TEST_STRING = "test";

    @Test
    public void testNotNull() {
        assertThrows(IllegalArgumentException.class, () -> Checks.notNull(null, "String", "toSniff"));
    }

    @Test
    public void testCopyStream() throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(TEST_STRING.getBytes(StandardCharsets.UTF_8));
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Checks.copyStream(bis, bos);
            final String test = new String(bos.toByteArray(), StandardCharsets.UTF_8);
            assertEquals("Expected copied stream result to be equal", TEST_STRING, test);
        }
    }
}
