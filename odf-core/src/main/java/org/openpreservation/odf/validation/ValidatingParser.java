package org.openpreservation.odf.validation;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser;

public interface ValidatingParser extends PackageParser {
    /**
     * Validates the given ODF package.
     * 
     * @param odfPackage the ODF Package to validate, this must not be null
     * @return a ValidationReport containing the results of the validation
     * @throws IOException if there's a problem reading package elements from the
     *                     zip file.
     */
    public ValidationReport validatePackage(final OdfPackage odfPackage)
            throws ParseException, FileNotFoundException;
}
