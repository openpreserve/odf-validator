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
import org.openpreservation.format.xml.XmlTestFiles;

public class OdfFormatsTest {
    @Test
    public void testIsPackageStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.EMPTY_ODS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageNullStream() {
        InputStream nullIs = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullIs);
                });
    }

    @Test
    public void testIsPackageStreamNoMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.NO_MIME_ROOT_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.NO_MIME_ROOT_ODS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamFlat() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS);
        assertFalse(String.format("%s IS NOT a package.", TestFiles.EMPTY_FODS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamEmpty() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY);
        assertFalse(String.format("%s IS NOT a package.", TestFiles.EMPTY), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamP() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.P_TEXT);
        assertFalse(String.format("%s IS NOT a package.", TestFiles.P_TEXT), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamK() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.K_TEXT);
        assertFalse(String.format("%s IS NOT a package.", TestFiles.K_TEXT), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamPK() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.PK_TEXT);
        assertFalse(String.format("%s IS NOT a package.", TestFiles.PK_TEXT), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamFakeMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.FAKEMIME_TEXT);
        assertFalse(String.format("%s IS NOT a package.", TestFiles.FAKEMIME_TEXT), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamOdsMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.MIMESID_ODS);
        assertTrue(String.format("%s IS an ods package.", TestFiles.MIMESID_ODS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageStreamOtsMime() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.MIMESID_OTS);
        assertTrue(String.format("%s IS an ots package.", TestFiles.MIMESID_OTS), OdfFormats.isPackage(is));
    }

    @Test
    public void testIsPackageString() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.EMPTY_ODS),
                OdfFormats.isPackage(new File(empty.toURI()).getAbsolutePath()));
    }

    @Test
    public void testIsPackageNullString() {
        String nullStr = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullStr);
                });
    }

    @Test
    public void testIsPackageStringNoMime() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestFiles.NO_MIME_ROOT_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.NO_MIME_ROOT_ODS),
                OdfFormats.isPackage(new File(empty.toURI()).getAbsolutePath()));
    }

    @Test
    public void testIsPackageFile() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.EMPTY_ODS),
                OdfFormats.isPackage(new File(empty.toURI())));
    }

    @Test
    public void testIsPackageNullFile() {
        File nullFile = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullFile);
                });
    }

    @Test
    public void testIsPackageFileNoMime() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.NO_MIME_ROOT_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.NO_MIME_ROOT_ODS),
                OdfFormats.isPackage(new File(empty.toURI())));
    }

    @Test
    public void testIsPackagePath() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.EMPTY_ODS),
                OdfFormats.isPackage(new File(empty.toURI()).toPath()));
    }

    @Test
    public void testIsPackageNullPath() {
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isPackage(nullPath);
                });
    }

    @Test
    public void testIsPackagePathNoMime() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.NO_MIME_ROOT_ODS);
        assertTrue(String.format("%s IS a package.", TestFiles.NO_MIME_ROOT_ODS),
                OdfFormats.isPackage(new File(empty.toURI()).toPath()));
    }

    @Test
    public void testIsXMLStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS);
        assertTrue(String.format("%s IS a flat XML file.", TestFiles.EMPTY_FODS), OdfFormats.isXml(is));
    }

    @Test
    public void testIsXMLEmptyStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY);
        assertFalse(String.format("%s IS NOT a flat XML file.", TestFiles.EMPTY), OdfFormats.isXml(is));
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
        InputStream is = ClassLoader.getSystemResourceAsStream(XmlTestFiles.UTF8_BOM_PI);
        assertTrue(String.format("%s IS a flat XML file.", XmlTestFiles.UTF8_BOM_PI), OdfFormats.isXml(is));
    }

    @Test
    public void testIsXMLNullStream() {
        InputStream nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isXml(nullPath);
                });
    }

    @Test
    public void testIsXMLString() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_FODS);
        assertTrue(String.format("%s IS a flat XML file.", TestFiles.EMPTY_FODS),
                OdfFormats.isXml(new File(empty.toURI()).getAbsolutePath()));
    }

    @Test
    public void testIsXMLNullString() {
        String nullStr = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isXml(nullStr);
                });
    }
}
