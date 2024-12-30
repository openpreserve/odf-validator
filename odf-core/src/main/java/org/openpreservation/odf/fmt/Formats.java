package org.openpreservation.odf.fmt;

import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import org.openpreservation.utils.Checks;

/**
 * The {@code Formats} enum represents the different formats supported by the ODF validator.
 * Each enum constant corresponds to a specific format, defined by its MIME type, file extension,
 * and a set of signatures, for identification.
 */
public enum Formats {
    /**
     * The ODF Text Document format.
     */
    ODT(Constants.MIME_ODT, Constants.EXT_ODT, EnumSet.of(Signatures.ODT)),

    /**
     * The ODF Spreadsheet format.
     */
    ODS(Constants.MIME_ODS, Constants.EXT_ODS, EnumSet.of(Signatures.ODS)),

    /**
     * The ODF Presentation format.
     */
    ODP(Constants.MIME_ODP, Constants.EXT_ODP, EnumSet.of(Signatures.ODP)),

    /**
     * The ODF Graphics format.
     */
    ODG(Constants.MIME_ODG, Constants.EXT_ODG, EnumSet.of(Signatures.ODG)),

    /**
     * The ZIP format, used for ODF packages.
     */
    ZIP(Constants.MIME_ZIP, Constants.EXT_ZIP,
            EnumSet.of(Signatures.ZIP, Signatures.ZIP_EMPTY, Signatures.ZIP_SPANNED)),

    /**
     * The XML format, used for ODF documents and metadata.
     */
    XML(Constants.MIME_XML, Constants.EXT_XML,
            EnumSet.of(Signatures.XML_UTF_8, Signatures.XML_UTF_16_LE, Signatures.XML_UTF_16_BE,
                    Signatures.XML_UTF_32_LE, Signatures.XML_UTF_32_BE)),

    /**
     * The ODF Database format.
     */
    ODB(Constants.MIME_ODB, Constants.EXT_ODB, EnumSet.of(Signatures.ODB)),

    /**
     * The ODF Chart format.
     */
    ODC(Constants.MIME_ODC, Constants.EXT_ODC, EnumSet.of(Signatures.ODC)),

    /**
     * The ODF Formula format.
     */
    ODF(Constants.MIME_ODF, Constants.EXT_ODF, EnumSet.of(Signatures.ODF)),

    /**
     * The ODF Image format.
     */
    ODI(Constants.MIME_ODI, Constants.EXT_ODI, EnumSet.of(Signatures.ODI)),

    /**
     * The ODF Master Document format.
     */
    ODM(Constants.MIME_ODM, Constants.EXT_ODM, EnumSet.of(Signatures.ODM)),

    /**
     * The ODF Template Chart format.
     */
    OTC(Constants.MIME_OTC, Constants.EXT_OTC, EnumSet.of(Signatures.OTC)),

    /**
     * The ODF Template Formula format.
     */
    OTF(Constants.MIME_OTF, Constants.EXT_OTF, EnumSet.of(Signatures.OTF)),

    /**
     * The ODF Template Graphics format.
     */
    OTG(Constants.MIME_OTG, Constants.EXT_OTG, EnumSet.of(Signatures.OTG)),

    /**
     * The ODF Template HTML format.
     */
    OTH(Constants.MIME_OTH, Constants.EXT_OTH, EnumSet.of(Signatures.OTH)),

    /**
     * The ODF Template Image format.
     */
    OTI(Constants.MIME_OTI, Constants.EXT_OTI, EnumSet.of(Signatures.OTI)),

    /**
     * The ODF Template Master Document format.
     */
    OTM(Constants.MIME_OTM, Constants.EXT_OTM, EnumSet.of(Signatures.OTM)),

    /**
     * The ODF Template Presentation format.
     */
    OTP(Constants.MIME_OTP, Constants.EXT_OTP, EnumSet.of(Signatures.OTP)),

    /**
     * The ODF Template Spreadsheet format.
     */
    OTS(Constants.MIME_OTS, Constants.EXT_OTS, EnumSet.of(Signatures.OTS)),

    /**
     * The ODF Template Text Document format.
     */
    OTT(Constants.MIME_OTT, Constants.EXT_OTT, EnumSet.of(Signatures.OTT)),

    /**
     * Unknown format.
     */
    UNKNOWN(Constants.MIME_OCTET, "bin", EnumSet.of(Signatures.NOMATCH));

    static final Set<Formats> ODF_PACKAGES = EnumSet.of(ZIP, ODB, ODT, OTT, ODG, OTG, ODP, OTP, ODS, OTS, ODC, OTC, ODI,
            OTI, ODF,
            OTF, ODM, OTM, OTH);
    static final Set<Formats> ODF_DOCUMENTS = EnumSet.of(XML, ODB, ODT, OTT, ODG, OTG, ODP, OTP, ODS, OTS, ODC, OTC,
            ODI, OTI, ODF,
            OTF, ODM, OTM, OTH);

    /**
     * Identifies the format based on the provided byte array.
     *
     * @param bytes the byte array to identify
     * @return the identified format, or {@code UNKNOWN} if no match is found
     * @throws NullPointerException if {@code bytes} is null
     */
    public static Formats identify(final byte[] bytes) {
        Objects.requireNonNull(bytes, String.format(Checks.NOT_NULL, "byte[]", "bytes"));
        final Signatures sig = Signatures.match(bytes);
        for (final Formats f : Formats.values()) {
            if (f.signatures.contains(sig)) {
                return f;
            }
        }
        return UNKNOWN;
    }

    /**
     * Identifies the format based on the provided MIME type.
     *
     * @param mime the MIME type to identify
     * @return the identified format, or {@code UNKNOWN} if no match is found
     * @throws NullPointerException if {@code mime} is null
     */
    public static Formats fromMime(final String mime) {
        Objects.requireNonNull(mime, String.format(Checks.NOT_NULL, "String", "mime"));
        for (final Formats f : Formats.values()) {
            if (f.mime.equals(mime)) {
                return f;
            }
        }
        return UNKNOWN;
    }

    /**
     * Identifies the format based on the provided file extension.
     *
     * @param ext the file extension to identify
     * @return the identified format, or {@code UNKNOWN} if no match is found
     * @throws NullPointerException if {@code ext} is null
     */
    public static Formats fromExtension(final String ext) {
        Objects.requireNonNull(ext, String.format(Checks.NOT_NULL, "String", "ext"));
        for (final Formats f : Formats.values()) {
            if (f.extension.equals(ext)) {
                return f;
            }
        }
        return UNKNOWN;
    }

    /**
     * The String MIME type identifier
     */
    public final String mime;

    /**
     * The String extension associated with the MIME type
     */
    public final String extension;

    private final Set<Signatures> signatures;

    private Formats(final String mime, final String extension, final Set<Signatures> signatures) {
        this.mime = mime;
        this.extension = extension;
        this.signatures = EnumSet.copyOf(signatures);
    }

    /**
     * Is the format a text format?
     *
     * @return {@code true} if the format is a text format, {@code false} otherwise
     */
    public boolean isText() {
        return this == XML;
    }

    /**
     * Is the format a package format?
     *
     * @return {@code true} if the format is a package format, {@code false} otherwise
     */
    public boolean isPackage() {
        return ODF_PACKAGES.contains(this);
    }

    /**
     * Is the format a OpenDocument format?
     *
     * @return {@code true} if the format is an OpenDocument format, {@code false} otherwise
     */
    public boolean isOdf() {
        return ODF_DOCUMENTS.contains(this);
    }

    /**
     * Get the number of signatures for the format.
     *
     * @return the number of signatures for the format
     */
    public int getSignatureCount() {
        return signatures.size();
    }

    /**
     * Get the maximum length of a signature for the format.
     *
     * @return the maximum length of a signature for the format
     */
    public int getMaxSignatureLength() {
        int max = 0;
        for (final Signatures s : signatures) {
            max = Math.max(max, s.getLength());
        }
        return max;
    }
    /**
     * Get the MIME type as an ASCII encoded byte array.
     *
     * @return the ASCII encoded MIME type id as a byte array
     */
    public byte[] getBytes() {
        return mime.getBytes(StandardCharsets.US_ASCII);
    }
}