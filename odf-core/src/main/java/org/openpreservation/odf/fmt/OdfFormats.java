package org.openpreservation.odf.fmt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.openpreservation.utils.Checks;

public final class OdfFormats {
    private static final String TO_TEST = "toTest";

    public static boolean isPackage(final String toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "String", TO_TEST));
        return OdfFormats.isPackage(Paths.get(toTest).toFile());
    }

    public static boolean isPackage(final Path toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "Path", TO_TEST));
        return OdfFormats.isPackage(toTest.toFile());
    }

    public static boolean isPackage(final File toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "File", TO_TEST));
        try (InputStream is = new FileInputStream(toTest)) {
            return OdfFormats.isPackage(is);
        }
    }

    public static boolean isPackage(final InputStream toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "InputStream", TO_TEST));
        try (BufferedInputStream bis = new BufferedInputStream(toTest)) {
            return FormatSniffer.sniff(bis).isPackage();
        }
    }

    public static boolean isXml(final String toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "String", TO_TEST));
        return OdfFormats.isXml(Paths.get(toTest));
    }

    public static boolean isXml(final Path toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "Path", TO_TEST));
        return OdfFormats.isXml(toTest.toFile());
    }

    public static boolean isXml(final File toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "File", TO_TEST));
        try (InputStream is = new java.io.FileInputStream(toTest)) {
            return OdfFormats.isXml(is);
        }
    }

    public static boolean isXml(final InputStream toTest) throws IOException {
        Objects.requireNonNull(toTest, String.format(Checks.NOT_NULL, "InputStream", TO_TEST));
        try (BufferedInputStream bis = new BufferedInputStream(toTest)) {
            return FormatSniffer.sniff(bis) == Formats.XML;
        }
    }

    private OdfFormats() {
        throw new AssertionError("Should not be instantiated.");
    }
}
