package org.openpreservation.odf.properties;

import java.io.IOException;
import java.util.List;

import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.validation.ValidationReport;

public interface PropertyExtractor {
    List<Property> extractProperties(final OdfPackage odfPackage, final ValidationReport report) throws IOException;
    List<Property> supportedProperties();
}
