package org.openpreservation.odf.fmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.junit.Test;
import org.openpreservation.format.xml.Encodings;
import org.openpreservation.format.xml.XmlTestFiles;

public class FormatSnifferTest {
    @Test
    public void testSniffXMLString() throws IOException, URISyntaxException {
        Formats fmt = FormatSniffer.sniff(new File(TestFiles.EMPTY_FODS.toURI()).getAbsolutePath());
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullString() {
        String nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniff(nullPath);
                });
    }

    @Test
    public void testSniffXMLPath() throws URISyntaxException, IOException {
        Formats fmt = FormatSniffer.sniff(new File(TestFiles.EMPTY_FODS.toURI()).toPath());
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullPath() throws IOException {
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniff(nullPath);
                });
    }

    @Test
    public void testSniffXMLFile() throws URISyntaxException, IOException {
        Formats fmt = FormatSniffer.sniff(new File(TestFiles.EMPTY_FODS.toURI()));
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullFile() throws IOException {
        File nullFile = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniff(nullFile);
                });
    }

    @Test
    public void testSniffXMLNoSuchFile() throws IOException, URISyntaxException {
        File resFile = new File(TestFiles.EMPTY_ODS.toURI().getPath());
        File resDir = resFile.isDirectory() ? new File(resFile, "no-such-file.none")
                : new File(resFile.getParentFile(), "no-such-file.none");

        assertThrows("NullPointerException expected",
                FileNotFoundException.class,
                () -> {
                    FormatSniffer.sniff(resDir);
                });
    }

    @Test
    public void testSniffXMLStream() throws IOException {
        BufferedInputStream is = new BufferedInputStream(TestFiles.EMPTY_FODS.openStream());
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXmlUtf8BomStream() throws IOException {
        BufferedInputStream is = new BufferedInputStream(XmlTestFiles.UTF8_BOM_PI.openStream());
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", XmlTestFiles.UTF8_BOM_PI, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXmlUtf16LEBomStream() throws IOException {
        BufferedInputStream is = new BufferedInputStream(XmlTestFiles.UTF16LE_BOM_PI.openStream());
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", XmlTestFiles.UTF16LE_BOM_PI, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXmlUtf16BEBomStream() throws IOException {
        BufferedInputStream is = new BufferedInputStream(XmlTestFiles.UTF16BE_BOM_PI.openStream());
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", XmlTestFiles.UTF16BE_BOM_PI, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullStream() {
        BufferedInputStream nullStream = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniff(nullStream);
                });
    }

    @Test
    public void testSniffPackageString() throws IOException {
        Formats fmt = FormatSniffer.sniff(new File(TestFiles.EMPTY_ODS.getPath()));
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.EMPTY_ODS, Formats.ODS, fmt),
                Formats.ODS, fmt);
    }

    @Test
    public void testSniffBomPkgMunge() throws IOException {
        BufferedInputStream is = new BufferedInputStream(XmlTestFiles.UTF8_BOM_ODS.openStream());
        Formats fmt = FormatSniffer.sniff(is);
        assertNotEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", XmlTestFiles.UTF8_BOM_ODS, Formats.ODS, fmt),
                Formats.ODS, fmt);
    }

    @Test
    public void testSniffEncodingString() throws IOException, URISyntaxException {
        Encodings enc = FormatSniffer.sniffEncoding(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath());
        assertEquals(String.format("%s HAS encoding %s, not %s.", TestFiles.EMPTY_FODS, Encodings.NONE, enc),
                Encodings.NONE, enc);
    }

    @Test
    public void testSniffEncodingNullString() {
        String nullStr = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniffEncoding(nullStr);
                });
    }

    @Test
    public void testSniffEncodingPath() throws URISyntaxException, IOException {
        Encodings enc = FormatSniffer.sniffEncoding(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", TestFiles.FAKEMIME_TEXT, Encodings.NONE, enc),
                Encodings.NONE, enc);
    }

    @Test
    public void testSniffEncodingNullPath() throws IOException {
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniffEncoding(nullPath);
                });
    }

    @Test
    public void testSniffEncodingFile() throws URISyntaxException, IOException {
        Encodings enc = FormatSniffer.sniffEncoding(new File(XmlTestFiles.UTF16LE_BOM.toURI()));
        assertEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", XmlTestFiles.UTF16LE_BOM, Encodings.UTF_16_LE, enc),
                Encodings.UTF_16_LE, enc);
    }

    @Test
    public void testSniffEncodingNullFile() throws IOException {
        File nullFile = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniffEncoding(nullFile);
                });
    }

    @Test
    public void testSniffEncodingNoSuchFile() throws IOException, URISyntaxException {
        File resFile = new File(TestFiles.EMPTY_ODS.toURI().getPath());
        File resDir = resFile.isDirectory() ? new File(resFile, "no-such-file.none")
                : new File(resFile.getParentFile(), "no-such-file.none");

        assertThrows("NullPointerException expected",
                FileNotFoundException.class,
                () -> {
                    FormatSniffer.sniffEncoding(resDir);
                });
    }

    @Test
    public void testSniffEncodingStream() throws IOException {
        BufferedInputStream is = new BufferedInputStream(XmlTestFiles.UTF8_BOM_PI.openStream());
        Encodings enc = FormatSniffer.sniffEncoding(is);
        assertEquals(
                String.format("%s SHOULD sniff as format %s, not %s.", XmlTestFiles.UTF8_BOM_PI, Encodings.UTF_8, enc),
                Encodings.UTF_8, enc);
    }
}
