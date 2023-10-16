package org.openpreservation.odf.xml;

import org.openpreservation.odf.fmt.OdfFormats;

public class Constants {
    private Constants() {
        throw new AssertionError("Constants class is not instantiable");
    }

    static final String PREFIX_OFFICE = "office";
    static final String ELENAME_MIME = officePrefix(OdfFormats.MIMETYPE);
    static final String ELENAME_TEXT = officePrefix("text");
    static final String ELENAME_SPREADSHEET = officePrefix("spreadsheet");
    static final String ELENAME_PRESENTATION = officePrefix("presentation");
    static final String ELENAME_DRAWING = officePrefix("drawing");
    static final String ELENAME_CHART = officePrefix("chart");
    static final String ELENAME_IMAGE = officePrefix("image");
    static final String ELENAME_FORMULA = officePrefix("formula");
    static final String ELENAME_DATABASE = officePrefix("database");

    static final String officePrefix(final String eleName) {
        return String.format("%s:%s", PREFIX_OFFICE, eleName);
    }
}
