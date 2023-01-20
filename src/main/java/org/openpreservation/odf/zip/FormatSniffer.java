package org.openpreservation.odf.zip;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Arrays;

public class FormatSniffer {
    private static final char[] XML_HEADER_1 = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20};
    private static final char[] XML_HEADER_2 = {0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C, 0x00, 0x20};
    private static final char[] XML_HEADER_3 = {0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C, 0x00, 0x20};
    private static final char[] XML_HEADER_4 = {0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D, 0x00, 0x00, 0x00, 0x6C, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00};
    private static final char[] XML_HEADER_5 = {0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D, 0x00, 0x00, 0x00, 0x6C, 0x00, 0x00, 0x00, 0x20};
    private static final char[][] XML_HEADERS = {XML_HEADER_1, XML_HEADER_2, XML_HEADER_3, XML_HEADER_4, XML_HEADER_5};
    public static Format sniff(final Path pathToTest) {
        return sniff(pathToTest.toFile());
    }

    public static Format sniff(final File fileToTest) {
        try (Reader reader = new FileReader(fileToTest)) {
            return sniff(reader);
        } catch (IOException e) {
            return Format.UNKNOWN;
        }
    }

    public static Format sniff(final InputStream streamToTest) {
        try (Reader reader = new InputStreamReader(streamToTest)) {
            return sniff(reader);
        } catch (IOException e) {
            return Format.UNKNOWN;
        }
    }

    public static Format sniff(final Reader readerToTest) throws IOException {
        int firstChar = readerToTest.read();
        readerToTest.reset();
        if (firstChar == 'P') {
            return (isPackage(readerToTest)) ? Format.PACKAGE : Format.UNKNOWN;
        }
        if (firstChar == 0x00 || firstChar == 0x32) {
            return Format.FLAT;
        }
        return Format.UNKNOWN;
    }

    public static boolean isPackage(final Path pathToTest) {
        return isPackage(pathToTest.toFile());
    }

    public static boolean isPackage(final File fileToTest) {
        try (Reader reader = new FileReader(fileToTest)) {
            return isPackage(reader);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isPackage(final InputStream streamToTest) {
        try (Reader reader = new InputStreamReader(streamToTest)) {
            return isPackage(reader);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isPackage(final Reader readerToTest) throws IOException {
        if (hasPK(readerToTest)) {
            return hasMimeType(readerToTest);
        }
        return false;
    }

    public static boolean isXml(final Path pathToTest) {
        return isXml(pathToTest.toFile());
    }

    public static boolean isXml(final File fileToTest) {
        try (Reader reader = new FileReader(fileToTest)) {
            return isXml(reader);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isXml(final InputStream streamToTest) {
        try (Reader reader = new InputStreamReader(streamToTest)) {
            return isXml(reader);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isXml(final Reader readerToTest) throws IOException {
        char[] xmlTest = new char[30];
        readerToTest.read(xmlTest);
        int bomLenth = calcBOM(xmlTest);
        for (char[] header : XML_HEADERS) {
            if (Arrays.equals(header, Arrays.copyOfRange(xmlTest, bomLenth, header.length + bomLenth))) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasPK(final Reader readerToTest) throws IOException {
        if ((readerToTest.read() == 'P') && (readerToTest.read() == 'K')) {
            return true;
        }
        return false;
    }

    private static int calcBOM(final char[] toTest) {
        if ((toTest[0] == 0xff && toTest[1] == 0xfe) || (toTest[0] == 0xfe && toTest[0] == 0xff)) {
            return 2;
        } else if (toTest[0] == 0xef && toTest[1] == 0xbb && toTest[2] == 0xbf) {
            return 3;
        }
        return 0;
    }

    private static boolean hasMimeType(final Reader readerToTest) throws IOException {
        if (readerToTest.skip(28) != 28) {
            return false;
        }
        char[] mimeTest = new char[8];
        readerToTest.read(mimeTest, 0, 8);
        return "mimetype".equals(new String(mimeTest));
    }

    public enum Format {
        PACKAGE, FLAT, UNKNOWN
    }
}
