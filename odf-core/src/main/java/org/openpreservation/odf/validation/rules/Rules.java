package org.openpreservation.odf.validation.rules;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.validation.Rule;
import org.xml.sax.SAXException;

public class Rules {
    private Rules() {
        throw new AssertionError("Utility class must not be instantiated");
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

    public static final Rule odf9() {
        return DigitalSignaturesRule.getInstance();
    }
}
