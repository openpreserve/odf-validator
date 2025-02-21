package org.openpreservation.odf.validation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.ValidatingParser;
import org.openpreservation.odf.validation.ValidationResult;
import org.openpreservation.odf.validation.Validators;
import org.xml.sax.SAXException;

public class Utilities {

    static ValidationResult getValidationReport(final URL resource)
            throws URISyntaxException, ParseException, ParserConfigurationException, SAXException, IOException {
        ValidatingParser parser = Validators.getValidatingParser();
        InputStream is = resource.openStream();
        OdfPackage pkg = parser.parsePackage(is, resource.toString());
        return parser.validatePackage(pkg);
    }
}
