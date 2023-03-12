package org.openpreservation.odf.fmt;

import static org.junit.Assert.assertEquals;
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
    public void testSniffXMLString() throws IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(empty.getPath());
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestData.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    public void testSniffXMLNullString() {
        String nullPath = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    OdfFormats.isXml(nullPath);
                });
    }

    @Test
    public void testSniffXMLPath() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(new File(empty.toURI()).toPath());
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestData.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullPath() throws IOException {
        Path nullPath = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniff(nullPath);
                });
    }

    @Test
    public void testSniffXMLFile() throws URISyntaxException, IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(new File(empty.toURI()));
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestData.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullFile() throws IOException {
        File nullFile = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniff(nullFile);
                });
    }

    @Test
    public void testSniffXMLNoSuchFile() throws IOException, URISyntaxException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_FODS);
        File resFile = new File(empty.toURI().getPath());
        File resDir = resFile.isDirectory() ? new File(resFile, "no-such-file.none")
                : new File(resFile.getParentFile(), "no-such-file.none");

        assertThrows("Null pointer exception expected",
                FileNotFoundException.class,
                () -> {
                    FormatSniffer.sniff(resDir);
                });
    }

    @Test
    public void testSniffXMLStream() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(TestData.EMPTY_FODS);
        Formats fmt = FormatSniffer.sniff(is);
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestData.EMPTY_FODS, Formats.XML, fmt),
                Formats.XML, fmt);
    }

    @Test
    public void testSniffXMLNullStream() {
        InputStream nullStream = null;
        assertThrows("Null pointer exception expected",
                NullPointerException.class,
                () -> {
                    FormatSniffer.sniff(nullStream);
                });
    }

    @Test
    public void testSniffPackageString() throws IOException {
        URL empty = ClassLoader.getSystemResource(TestData.EMPTY_ODS);
        Formats fmt = FormatSniffer.sniff(new File(empty.getPath()));
        assertEquals(String.format("%s SHOULD sniff as format %s, not %s.", TestData.EMPTY_ODS, Formats.ODS, fmt),
                Formats.ODS, fmt);
    }
}
