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

    public static final Rule odf3() throws ParserConfigurationException, SAXException {
        return PackageMimeTypeRule.getInstance();
    }
}
