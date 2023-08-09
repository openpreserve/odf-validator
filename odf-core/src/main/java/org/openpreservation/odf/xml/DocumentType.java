package org.openpreservation.odf.xml;

import java.util.EnumSet;
import java.util.Set;

import org.openpreservation.odf.fmt.Formats;

public enum DocumentType {
    TEXT(EnumSet.of(Formats.ODT, Formats.OTT, Formats.ODM, Formats.OTM), "office:text"),
    SPREADSHEET(EnumSet.of(Formats.ODS, Formats.OTS), "office:spreadsheet"),
    PRESENTATION(EnumSet.of(Formats.ODP, Formats.OTP), "office:presentation"),
    DRAWING(EnumSet.of(Formats.ODG, Formats.OTG), "office:drawing"),
    CHART(EnumSet.of(Formats.ODC, Formats.OTC), "office:chart"),
    IMAGE(EnumSet.of(Formats.ODI, Formats.OTI), "office:image"),
    FORMULA(EnumSet.of(Formats.ODF, Formats.OTH), "office:formula"),
    DATABASE(EnumSet.of(Formats.ODB), "office:database");

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
        for (final DocumentType type : DocumentType.values()) {
            if (type.getFormats().contains(format)) {
                return type;
            }
        }
        return null;
    }

    public static DocumentType getTypeByBodyElement(final String bodyElementName) {
        for (final DocumentType type : DocumentType.values()) {
            if (type.bodyElementName.equals(bodyElementName)) {
                return type;
            }
        }
        return null;
    }

    public static DocumentType getTypeByMimeString(final String mimeString) {
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
