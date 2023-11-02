package org.openpreservation.format.xml;

import java.net.URL;

public class XmlTestFiles {
    final static String TEST_ROOT = "org/openpreservation/format/xml/";
    public final static URL NOBOM_TEXT = ClassLoader.getSystemResource(TEST_ROOT + "no-bom-test.txt");
    public final static URL UTF8_BOM = ClassLoader.getSystemResource(TEST_ROOT + "utf8-bom-only.txt");
    public final static URL UTF16LE_BOM = ClassLoader.getSystemResource(TEST_ROOT + "utf16le-bom-only.txt");
    public final static URL UTF16BE_BOM = ClassLoader.getSystemResource(TEST_ROOT + "utf16be-bom-only.txt");
    public final static URL UTF8_BOM_PI = ClassLoader.getSystemResource(TEST_ROOT + "utf8-bom-pi.txt");
    public final static URL UTF16LE_BOM_PI = ClassLoader.getSystemResource(TEST_ROOT + "utf16le-bom-pi.txt");
    public final static URL UTF16BE_BOM_PI = ClassLoader.getSystemResource(TEST_ROOT + "utf16be-bom-pi.txt");
    public final static URL UTF8_BOM_ODS = ClassLoader.getSystemResource(TEST_ROOT + "utf8-bom-ods.txt");
}
