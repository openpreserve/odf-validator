package org.openpreservation.odf.validation.rules;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.validation.Rule;
import org.xml.sax.SAXException;

public class Rules {
    static final String ODF5_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-5.sch";
    static final String ODF7_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-7.sch";
    static final String ODF8_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-8.sch";

    public static final Rule odf1() {
        return EncryptionRule.getInstance();
    }

    public static final Rule odf2() throws ParserConfigurationException, SAXException {
        return ValidPackageRule.getInstance();
    }

    public static final Rule odf3() {
        return PackageMimeTypeRule.getInstance();
    }

    public static final Rule odf4() {
        return ExtensionMimeTypeRule.getInstance();
    }

    public static final Rule odf5() {
        return SchematronRule.getInstance("ODF_5", "External data check",
                "The file MUST NOT have any references to external data.", false,
                ClassLoader.getSystemResource(ODF5_SCHEMATRON));
    }

    public static final Rule odf7() {
        return SchematronRule.getInstance("ODF_7", "Content check",
                "The file MUST have values or objects in at least one cell.", false,
                ClassLoader.getSystemResource(ODF7_SCHEMATRON));
    }

    public static final Rule odf8() {
        return SchematronRule.getInstance("ODF_8", "Macros check",
                "The file MUST NOT contain any macros.", false,
                ClassLoader.getSystemResource(ODF8_SCHEMATRON));
    }

    public static final Rule odf9() {
        return DigitalSignaturesRule.getInstance();
    }

    public static final Rule odf10() {
        return SubDocumentRule.getInstance();
    }

    private Rules() {
        throw new AssertionError("Utility class must not be instantiated");
    }
}
