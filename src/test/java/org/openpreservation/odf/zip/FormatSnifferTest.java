package org.openpreservation.odf.zip;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;

public class FormatSnifferTest {
    private final static String TEST_ROOT = "org/openpreservation/odf/";
    private final static String ZIP_TEST_ROOT = TEST_ROOT + "zip/";
    private final static String XML_TEST_ROOT = TEST_ROOT + "xml/";
    private final static String EMPTY_ODS = ZIP_TEST_ROOT + "empty.ods";
    private final static String NO_MIME_ODS = ZIP_TEST_ROOT + "no_mime.ods";
    private final static String EMPTY_FODS = XML_TEST_ROOT + "empty.fods";

    @Test
    public void testIsPackageStream() {
        InputStream is = ClassLoader.getSystemResourceAsStream(EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", EMPTY_ODS), FormatSniffer.isPackage(is));
    }

    @Test
    public void testIsXMLStream() {
        InputStream is = ClassLoader.getSystemResourceAsStream(EMPTY_FODS);
        assertTrue(String.format("%s IS a flat XML file.", EMPTY_FODS), FormatSniffer.isXml(is));
    }

    @Test
    public void testIsPackageStreamNoMime() {
        InputStream is = ClassLoader.getSystemResourceAsStream(NO_MIME_ODS);
        assertFalse(String.format("%s IS NOT a package.", NO_MIME_ODS), FormatSniffer.isPackage(is));
    }
}
