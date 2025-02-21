package org.openpreservation.odf.xml;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import org.openpreservation.odf.fmt.Formats;

/**
 * Enum representing the type of ODF document.
 * The enum allows easy lookup/matching of the body element name, and associated MIME types.
 */
public enum DocumentType {
    /**
     * Text document type, e.g. ODF Text, masters and associated templates.
     */
    TEXT(EnumSet.of(Formats.ODT, Formats.OTT, Formats.ODM, Formats.OTM), Constants.ELENAME_TEXT),
    /**
     * Spreadsheet document type, e.g. ODF Spreadsheet and template.
     */
    SPREADSHEET(EnumSet.of(Formats.ODS, Formats.OTS), Constants.ELENAME_SPREADSHEET),
    /**
     * Presentation document type, e.g. ODF Presentation and template.
     */
    PRESENTATION(EnumSet.of(Formats.ODP, Formats.OTP), Constants.ELENAME_PRESENTATION),
    /**
     * Drawing document type, e.g. ODF Drawing and template.
     */
    DRAWING(EnumSet.of(Formats.ODG, Formats.OTG), Constants.ELENAME_DRAWING),
    /**
     * Chart document type, e.g. ODF Chart and template.
     */
    CHART(EnumSet.of(Formats.ODC, Formats.OTC), Constants.ELENAME_CHART),
    /**
     * Image document type, e.g. ODF Image and template.
     */
    IMAGE(EnumSet.of(Formats.ODI, Formats.OTI), Constants.ELENAME_IMAGE),
    /**
     * Formula document type, e.g. ODF Formula and template.
     */
    FORMULA(EnumSet.of(Formats.ODF, Formats.OTH), Constants.ELENAME_FORMULA),
    /**
     * Database document type, e.g. ODF Database.
     */
    DATABASE(EnumSet.of(Formats.ODB), Constants.ELENAME_DATABASE);

    private final EnumSet<Formats> formats;
    private final String bodyElementName;

    private DocumentType(final EnumSet<Formats> formats, final String bodyElementName) {
        this.formats = formats;
        this.bodyElementName = bodyElementName;
    }

    /**
     * Get the set of formats associated with this document type.
     *
     * @return the set of formats
     */
    public Set<Formats> getFormats() {
        return this.formats;
    }

    /**
     * Get the body element name associated with this document type.
     *
     * @return the body element name
     */
    public String getBodyElementName() {
        return this.bodyElementName;
    }

    /**
     * Get the document type by the format.
     *
     * @param format the format
     * @return the document type
     */
    public static DocumentType getTypeByFormat(final Formats format) {
        Objects.requireNonNull(format, "format must not be null");
        for (final DocumentType type : DocumentType.values()) {
            if (type.getFormats().contains(format)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get the document type by the body element name.
     *
     * @param bodyElementName the body element name
     * @return the document type
     */
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

    /**
     * Get the document type by the MIME type.
     *
     * @param mimeString the MIME type
     * @return the document type
     */
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
