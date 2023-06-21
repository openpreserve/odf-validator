package org.openpreservation.odf.fmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openpreservation.utils.Checks;

public class OdfFormats {

    private OdfFormats() {
        throw new AssertionError("Should not be instantiated.");
    }

    public static boolean isPackage(final String toTest) throws IOException {
        Checks.notNull(toTest, "String", "toTest");
        return OdfFormats.isPackage(Paths.get(toTest).toFile());
    }

    public static boolean isPackage(final Path toTest) throws IOException {
        Checks.notNull(toTest, "Path", "toTest");
        return OdfFormats.isPackage(toTest.toFile());
    }

    public static boolean isPackage(final File toTest) throws IOException {
        Checks.notNull(toTest, "File", "toTest");
        try (InputStream is = new FileInputStream(toTest)) {
            return OdfFormats.isPackage(is);
        }
    }

    public static boolean isPackage(final InputStream toTest) throws IOException {
        Checks.notNull(toTest, "InputStream", "toTest");
        return FormatSniffer.sniff(toTest).isPackage();
    }

    public static boolean isXml(final String toTest) throws IOException {
        Checks.notNull(toTest, "String", "toTest");
        return OdfFormats.isXml(Paths.get(toTest));
    }

    public static boolean isXml(final Path toTest) throws IOException {
        Checks.notNull(toTest, "Path", "toTest");
        return OdfFormats.isXml(toTest.toFile());
    }

    public static boolean isXml(final File toTest) throws IOException {
        Checks.notNull(toTest, "File", "toTest");
        try (InputStream is = new java.io.FileInputStream(toTest)) {
            return OdfFormats.isXml(is);
        }
    }

    public static boolean isXml(final InputStream toTest) throws IOException {
        Checks.notNull(toTest, "InputStream", "toTest");
        return FormatSniffer.sniff(toTest) == Formats.XML;
    }
}
