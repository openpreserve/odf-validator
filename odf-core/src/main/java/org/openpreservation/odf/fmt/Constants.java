package org.openpreservation.odf.fmt;

public final class Constants {
    static final String APPLICATION = "application";
    static final String MIMETYPE = "mimetype";
    static final String TEXT = "text";
    static final String VND_ODF = "vnd.oasis.opendocument";
    static final String EXT_ODB = "odb";
    static final String EXT_ODC = "odc";
    static final String EXT_ODF = "odf";
    static final String EXT_ODG = "odg";
    static final String EXT_ODI = "odi";
    static final String EXT_ODM = "odm";
    static final String EXT_ODP = "opd";
    static final String EXT_ODS = "ods";
    static final String EXT_ODT = "odt";
    static final String EXT_OTC = "otc";
    static final String EXT_OTF = "otf";
    static final String EXT_OTG = "otg";
    static final String EXT_OTH = "oth";
    static final String EXT_OTI = "oti";
    static final String EXT_OTM = "otm";
    static final String EXT_OTP = "otp";
    static final String EXT_OTS = "ots";
    static final String EXT_OTT = "ott";
    static final String EXT_XML = "xml";
    static final String EXT_ZIP = "zip";
    static final String MIME_ODB = odfIdentifier("database");
    static final String MIME_ODC = odfIdentifier("chart");
    static final String MIME_ODF = odfIdentifier("formula");
    static final String MIME_ODG = odfIdentifier("graphics");
    static final String MIME_ODI = odfIdentifier("image");
    static final String MIME_ODM = odfIdentifier("text-master");
    static final String MIME_ODP = odfIdentifier("presentation");
    static final String MIME_ODS = odfIdentifier("spreadsheet");
    static final String MIME_ODT = odfIdentifier("text");
    static final String MIME_OTC = odfIdentifier("chart-template");
    static final String MIME_OTF = odfIdentifier("formula-template");
    static final String MIME_OTG = odfIdentifier("graphics-template");
    static final String MIME_OTH = odfIdentifier("text-web");
    static final String MIME_OTI = odfIdentifier("image-template");
    static final String MIME_OTM = odfIdentifier("text-master-template");
    static final String MIME_OTP = odfIdentifier("presentation-template");
    static final String MIME_OTS = odfIdentifier("spreadsheet-template");
    static final String MIME_OTT = odfIdentifier("text-template");
    static final String MIME_ZIP = appIdentifier(EXT_ZIP);
    static final String MIME_XML = mimeIdentiier(TEXT, EXT_XML);
    static final String MIME_OCTET = appIdentifier("octet-stream");

    private Constants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    private static final String appIdentifier(final String type) {
        return mimeIdentiier(APPLICATION, type);
    }

    private static final String odfIdentifier(final String ext) {
        return mimeIdentiier(APPLICATION, String.format("%s.%s", VND_ODF, ext));
    }

    private static final String mimeIdentiier(final String group, final String type) {
        return String.format("%s/%s", group, type);
    }
}
