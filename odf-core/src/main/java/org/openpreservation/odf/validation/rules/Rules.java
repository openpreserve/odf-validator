package org.openpreservation.odf.validation.rules;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.Rule;
import org.xml.sax.SAXException;

public class Rules {
    static final String ODF5_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-5.sch";
    static final String ODF7_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-7.sch";
    static final List<Rule> SET_RULES = Arrays.asList(odf1(), odf2(), odf3(), odf4(), odf5(), odf6(), odf7(), odf8(), odf9());
    static final Set<Rule> DNA_RULES = new LinkedHashSet<>(SET_RULES);

    public static final Rule odf1() {
        return EncryptionRule.getInstance(Severity.ERROR);
    }

    public static final Rule odf2() {
        try {
            return ValidPackageRule.getInstance(Severity.ERROR);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final Rule odf3() {
        return PackageMimeTypeRule.getInstance(Severity.ERROR);
    }

    public static final Rule odf4() {
        return ExtensionMimeTypeRule.getInstance(Severity.ERROR);
    }

    public static final Rule odf5() {
        return SchematronRule.getInstance("POL_5", "External data check",
                "The file MUST NOT have any references to external data.", Severity.INFO, false,
                ClassLoader.getSystemResource(ODF5_SCHEMATRON));
    }

    public static final Rule odf6() {
        return EmbeddedObjectsRule.getInstance(Severity.INFO);
    }

    public static final Rule odf7() {
        return SchematronRule.getInstance("POL_7", "Content check",
                "The file MUST have values or objects in at least one cell.", Severity.INFO, false,
                ClassLoader.getSystemResource(ODF7_SCHEMATRON));
    }

    public static final Rule odf8() {
        return MacroRule.getInstance(Severity.ERROR);
    }

    public static final Rule odf9() {
        return DigitalSignaturesRule.getInstance(Severity.ERROR);
    }

    public static final Rule odf10() {
        return SubDocumentRule.getInstance(Severity.WARNING);
    }

    public static final Profile getDnaProfile() {
        return ProfileImpl.of("DNA", "DNA ODF Spreadsheets Preservation Specification",
                "Extended validation for OpenDocument spreadsheets.", DNA_RULES);
    }

    private Rules() {
        throw new AssertionError("Utility class must not be instantiated");
    }
}
