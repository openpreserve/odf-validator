package org.openpreservation.odf.fmt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import org.junit.Test;

public class OdfFormatsTest {
    @Test
    public void testIsPackageStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestData.EMPTY_ODS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageNullStream() {
        InputStream nullIs = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullIs);
                });
    }

    @Test
    public void testIsPackageStreamNoMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.NO_MIME_ODS);
        assertFalse(String.format("%s IS NOT a package.", TestData.NO_MIME_ODS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamFlat() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.EMPTY_FODS);
        assertFalse(String.format("%s IS NOT a package.", TestData.EMPTY_FODS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamEmpty() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.EMPTY_FILE);
        assertFalse(String.format("%s IS NOT a package.", TestData.EMPTY_FILE), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamP() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.P_TEXT_FILE);
        assertFalse(String.format("%s IS NOT a package.", TestData.P_TEXT_FILE), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamK() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.K_TEXT_FILE);
        assertFalse(String.format("%s IS NOT a package.", TestData.K_TEXT_FILE), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamPK() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.PK_TEXT_FILE);
        assertFalse(String.format("%s IS NOT a package.", TestData.PK_TEXT_FILE), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamFakeMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.FAKEMIME_TEXT_FILE);
        assertFalse(String.format("%s IS NOT a package.", TestData.FAKEMIME_TEXT_FILE), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamOdsMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.MIMESID_ODS_FILE);
        assertTrue(String.format("%s IS an ods package.", TestData.MIMESID_ODS_FILE), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamOtsMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.MIMESID_OTS_FILE);
        assertTrue(String.format("%s IS an ots package.", TestData.MIMESID_OTS_FILE), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageString() throws IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestData.EMPTY_ODS), OdfFormats.isPackage(empty.getPath()));
    }

    @Test
    public void testIsPackageNullString() {
        String nullStr = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullStr);
                });
    }

    @Test
    public void testIsPackageStringNoMime() throws IOException {
        URL empty = ClassLoader.getSystemResource(TestData.NO_MIME_ODS);
        assertFalse(String.format("%s IS NOT a package.", TestData.NO_MIME_ODS), OdfFormats.isPackage(empty.getPath()));
    }

    @Test
    public void testIsPackageFile() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestData.EMPTY_ODS),
                OdfFormats.isPackage(new File(empty.toURI())));
    }

    @Test
    public void testIsPackageNullFile() {
        File nullFile = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullFile);
                });
    }

    @Test
    public void testIsPackageFileNoMime() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestData.NO_MIME_ODS);
        assertFalse(String.format("%s IS NOT a package.", TestData.NO_MIME_ODS),
                OdfFormats.isPackage(new File(empty.toURI())));
    }

    @Test
    public void testIsPackagePath() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestData.EMPTY_ODS),
                OdfFormats.isPackage(new File(empty.toURI()).toPath()));
    }

    @Test
    public void testIsPackageNullPath() {
        Path nullPath = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullPath);
                });
    }

    @Test
    public void testIsPackagePathNoMime() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestData.NO_MIME_ODS);
        assertFalse(String.format("%s IS NOT a package.", TestData.NO_MIME_ODS),
                OdfFormats.isPackage(new File(empty.toURI()).toPath()));
    }

    @Test
    public void testIsXMLStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.EMPTY_FODS);
        assertTrue(String.format("%s IS a flat XML file.", TestData.EMPTY_FODS), OdfFormats.isXml(is));
    }

    @Test
    public void testIsXMLEmptyStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.EMPTY_FILE);
        assertFalse(String.format("%s IS NOT a flat XML file.", TestData.EMPTY_FILE), OdfFormats.isXml(is));
    }

    @Test
    public void testIsXMLBomStream() throws IOException {
        /**
         * Work currently parked here to work out what's going on with reading a UTF-8
         * BOM.
         * This is edge case stuff, but it's worth understanding.
         * This may well be the answer:
         * 
         */
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.UTF8_BOM_PI_FILE);
        assertTrue(String.format("%s IS a flat XML file.", TestData.UTF8_BOM_PI_FILE), OdfFormats.isXml(is));
    }

    @Test
    public void testIsXMLNullStream() {
        InputStream nullPath = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isXml(nullPath);
                });
    }

    @Test
    public void testIsXMLString() throws IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_FODS);
        assertTrue(String.format("%s IS a flat XML file.", TestData.EMPTY_FODS), OdfFormats.isXml(empty.getPath()));
    }

    @Test
    public void testIsXMLNullString() {
        String nullStr = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isXml(nullStr);
                });
    }
}
