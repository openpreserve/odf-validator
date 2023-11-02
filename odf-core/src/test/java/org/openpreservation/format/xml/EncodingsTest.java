package org.openpreservation.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class EncodingsTest {
    private static final String UTF8 = "UTF-8";
    private static final String UTF16BE = "UTF-16(BE)";
    private static final String UTF16LE = "UTF-16(LE)";
    private static final String UTF32BE = "UTF-32(BE)";
    private static final String UTF32LE = "UTF-32(LE)";
    private static final String NONE = "NONE";
    private static final String NOT = "NOT";

    @Test
    public void testFromEncoding() {
        assertSame(Encodings.UTF_8, Encodings.fromEncoding(UTF8));
        assertSame(Encodings.UTF_16_BE, Encodings.fromEncoding(UTF16BE));
        assertSame(Encodings.UTF_16_LE, Encodings.fromEncoding(UTF16LE));
        assertSame(Encodings.UTF_32_BE, Encodings.fromEncoding(UTF32BE));
        assertSame(Encodings.UTF_32_LE, Encodings.fromEncoding(UTF32LE));
        assertSame(Encodings.NONE, Encodings.fromEncoding(NONE));
        assertSame(Encodings.NONE, Encodings.fromEncoding(NOT));
    }

    @Test
    public void testUtf8() throws IOException {
        assertSame(Encodings.UTF_8, Encodings.fromRepresentation(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
        assertNotSame(Encodings.UTF_8, Encodings.fromRepresentation(new byte[] { (byte) 0xEF, (byte) 0xBB }));
        assertNotSame(Encodings.UTF_8, Encodings.fromRepresentation(new byte[] { (byte) 0xEF }));
        assertNotSame(Encodings.UTF_8, Encodings.fromRepresentation(new byte[] {}));
        InputStream is = XmlTestFiles.UTF8_BOM.openStream();
        byte[] bytes = new byte[100];
        int read = is.read(bytes);
        assertEquals(3, read);
        assertSame(Encodings.UTF_8, Encodings.fromRepresentation(bytes));
        is = XmlTestFiles.UTF8_BOM_PI.openStream();
        read = is.read(bytes);
        assertEquals(41, read);
        assertSame(Encodings.UTF_8, Encodings.fromRepresentation(bytes));
    }

    @Test
    public void testUtf16Le() throws IOException {
        assertSame(Encodings.UTF_16_LE, Encodings.fromRepresentation(new byte[] { (byte) 0xFF, (byte) 0xFE }));
        assertNotSame(Encodings.UTF_16_LE,
                Encodings.fromRepresentation(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
        assertNotSame(Encodings.UTF_16_LE, Encodings.fromRepresentation(new byte[] { (byte) 0xFE, (byte) 0xFF }));
        assertNotSame(Encodings.UTF_16_LE, Encodings.fromRepresentation(new byte[] { (byte) 0xFF }));
        assertNotSame(Encodings.UTF_16_LE, Encodings.fromRepresentation(new byte[] {}));
        InputStream is = XmlTestFiles.UTF16LE_BOM.openStream();
        byte[] bytes = new byte[100];
        int read = is.read(bytes);
        byte[] testBytes = new byte[read];
        System.arraycopy(bytes, 0, testBytes, 0, read);
        assertSame(Encodings.UTF_16_LE, Encodings.fromRepresentation(testBytes));
        is = XmlTestFiles.UTF16LE_BOM_PI.openStream();
        read = is.read(bytes);
        testBytes = new byte[read];
        System.arraycopy(bytes, 0, testBytes, 0, read);
        assertSame(Encodings.UTF_16_LE, Encodings.fromRepresentation(testBytes));
    }

    @Test
    public void testUtf16Be() throws IOException {
        assertSame(Encodings.UTF_16_BE, Encodings.fromRepresentation(new byte[] { (byte) 0xFE, (byte) 0xFF }));
        assertNotSame(Encodings.UTF_16_BE,
                Encodings.fromRepresentation(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
        assertNotSame(Encodings.UTF_16_BE, Encodings.fromRepresentation(new byte[] { (byte) 0xFE, (byte) 0xFE }));
        assertNotSame(Encodings.UTF_16_BE, Encodings.fromRepresentation(new byte[] { (byte) 0xEF }));
        assertNotSame(Encodings.UTF_16_BE, Encodings.fromRepresentation(new byte[] {}));
        InputStream is = XmlTestFiles.UTF16BE_BOM.openStream();
        byte[] bytes = new byte[100];
        int read = is.read(bytes);
        byte[] testBytes = new byte[read];
        System.arraycopy(bytes, 0, testBytes, 0, read);
        assertSame(Encodings.UTF_16_BE, Encodings.fromRepresentation(testBytes));
        is = XmlTestFiles.UTF16BE_BOM_PI.openStream();
        read = is.read(bytes);
        testBytes = new byte[read];
        System.arraycopy(bytes, 0, testBytes, 0, read);
        assertSame(Encodings.UTF_16_BE, Encodings.fromRepresentation(testBytes));
    }

}
