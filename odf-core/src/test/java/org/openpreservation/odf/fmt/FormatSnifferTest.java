package org.openpreservation.odf.fmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import org.junit.Test;

public class FormatSnifferTest {
    @Test
    public void testSniffXMLString() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(new File(empty.toURI()).getAbsolutePath());
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullString() {
        String nullPath = null;
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FormatSniffer.sniff(nullPath);
                });
    }

    @Test
    public void testSniffXMLPath() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(new File(empty.toURI()).toPath());
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullPath() throws IOException {
        Path nullPath = null;
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FormatSniffer.sniff(nullPath);
                });
    }

    @Test
    public void testSniffXMLFile() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(new File(empty.toURI()));
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullFile() throws IOException {
        File nullFile = null;
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FormatSniffer.sniff(nullFile);
                });
    }

    @Test
    public void testSniffXMLNoSuchFile() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_FODS);
        File resFile = new File(empty.toURI().getPath());
        File resDir = resFile.isDirectory() ? new File(resFile, "no-such-file.none")
                : new File(resFile.getParentFile(), "no-such-file.none");

        assertThrows("IllegalArgumentException expected",
                FileNotFoundException.class,
                () -> {
                    FormatSniffer.sniff(resDir);
                });
    }

    @Test
    public void testSniffXMLStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXmlUtf8BomStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.UTF8_BOM_PI);
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.UTF8_BOM_PI, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXmlUtf16LEBomStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.UTF16LE_BOM_PI);
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.UTF16LE_BOM_PI, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXmlUtf16BEBomStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.UTF16BE_BOM_PI);
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.UTF16BE_BOM_PI, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullStream() {
        InputStream nullStream = null;
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FormatSniffer.sniff(nullStream);
                });
    }

    @Test
    public void testSniffPackageString() throws IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_ODS);
        Formats fmt = FormatSniffer.sniff(new File(empty.getPath()));
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_ODS, Formats.ODS, fmt),
                Formats.ODS, fmt);
    }

    @Test
    public void testSniffBomPkgMunge() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.UTF8_BOM_ODS);
        Formats fmt = FormatSniffer.sniff(is);
        assertNotEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.UTF8_BOM_ODS, Formats.ODS, fmt),
                Formats.ODS, fmt);
    }

    @Test
    public void testSniffEncodingString() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_FODS);
        Encodings enc = FormatSniffer.sniffEncoding(new File(empty.toURI()).getAbsolutePath());
        assertEquals(String.format("%s HAS encoding %s, not %s.", TestFiles.EMPTY_FODS, Encodings.NONE, enc),
                Encodings.NONE, enc);
    }

    @Test
    public void testSniffEncodingNullString() {
        String nullStr = null;
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FormatSniffer.sniffEncoding(nullStr);
                });
    }

    @Test
    public void testSniffEncodingPath() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.FAKEMIME_TEXT);
        Encodings enc = FormatSniffer.sniffEncoding(new File(empty.toURI()).toPath());
        assertEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.FAKEMIME_TEXT, Encodings.NONE, enc),
                Encodings.NONE, enc);
    }

    @Test
    public void testSniffEncodingNullPath() throws IOException {
        Path nullPath = null;
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FormatSniffer.sniffEncoding(nullPath);
                });
    }

    @Test
    public void testSniffEncodingFile() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestFiles.UTF16LE_BOM);
        Encodings enc = FormatSniffer.sniffEncoding(new File(empty.toURI()));
        assertEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.UTF16LE_BOM, Encodings.UTF_16_LE, enc),
                Encodings.UTF_16_LE, enc);
    }

    @Test
    public void testSniffEncodingNullFile() throws IOException {
        File nullFile = null;
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    FormatSniffer.sniffEncoding(nullFile);
                });
    }

    @Test
    public void testSniffEncodingNoSuchFile() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestFiles.EMPTY_FODS);
        File resFile = new File(empty.toURI().getPath());
        File resDir = resFile.isDirectory() ? new File(resFile, "no-such-file.none")
                : new File(resFile.getParentFile(), "no-such-file.none");

        assertThrows("IllegalArgumentException expected",
                FileNotFoundException.class,
                () -> {
                    FormatSniffer.sniffEncoding(resDir);
                });
    }

    @Test
    public void testSniffEncodingStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestFiles.UTF8_BOM_PI);
        Encodings enc = FormatSniffer.sniffEncoding(is);
        assertEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.UTF8_BOM_PI, Encodings.UTF_8, enc),
                Encodings.UTF_8, enc);
    }
}
