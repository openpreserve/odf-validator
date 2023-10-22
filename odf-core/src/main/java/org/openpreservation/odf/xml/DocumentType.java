package org.openpreservation.odf.xml;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import org.openpreservation.odf.fmt.Formats;

public enum DocumentType {
    TEXT(EnumSet.of(Formats.ODT, Formats.OTT, Formats.ODM, Formats.OTM), Constants.ELENAME_TEXT),
    SPREADSHEET(EnumSet.of(Formats.ODS, Formats.OTS), Constants.ELENAME_SPREADSHEET),
    PRESENTATION(EnumSet.of(Formats.ODP, Formats.OTP), Constants.ELENAME_PRESENTATION),
    DRAWING(EnumSet.of(Formats.ODG, Formats.OTG), Constants.ELENAME_DRAWING),
    CHART(EnumSet.of(Formats.ODC, Formats.OTC), Constants.ELENAME_CHART),
    IMAGE(EnumSet.of(Formats.ODI, Formats.OTI), Constants.ELENAME_IMAGE),
    FORMULA(EnumSet.of(Formats.ODF, Formats.OTH), Constants.ELENAME_FORMULA),
    DATABASE(EnumSet.of(Formats.ODB), Constants.ELENAME_DATABASE);

    private final EnumSet<Formats> formats;
    private final String bodyElementName;

    private DocumentType(final EnumSet<Formats> formats, final String bodyElementName) {
        this.formats = formats;
        this.bodyElementName = bodyElementName;
    }

    public Set<Formats> getFormats() {
        return this.formats;
    }

    public String getBodyElementName() {
        return this.bodyElementName;
    }

    public static DocumentType getTypeByFormat(final Formats format) {
        Objects.requireNonNull(format, "format must not be null");
        for (final DocumentType type : DocumentType.values()) {
            if (type.getFormats().contains(format)) {
                return type;
            }
        }
        return null;
    }

    public static DocumentType getTypeByBodyElement(final String bodyElementName) {
        Objects.requireNonNull(bodyElementName, "bodyElementName must not be null");
        final String searchTerm = (bodyElementName.toLowerCase().startsWith("office:") ? bodyElementName
                : Constants.officePrefix(bodyElementName));
        for (final DocumentType type : DocumentType.values()) {
            if (type.bodyElementName.equals(searchTerm)) {
                return type;
            }
        }
        return null;
    }

    public static DocumentType getTypeByMimeString(final String mimeString) {
        Objects.requireNonNull(mimeString, "mimeString must not be null");
        for (final DocumentType type : DocumentType.values()) {
            for (final Formats format : type.getFormats()) {
                if (format.mime.equals(mimeString)) {
                    return type;
                }
            }
        }
        return null;
    }
}
