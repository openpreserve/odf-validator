package org.openpreservation.odf.fmt;

import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import org.openpreservation.utils.Checks;

public enum Formats {
    ZIP(Constants.MIME_ZIP, Constants.EXT_ZIP,
            EnumSet.of(Signatures.ZIP)),
    XML(Constants.MIME_XML, Constants.EXT_XML,
            EnumSet.of(Signatures.XML_UTF_8, Signatures.XML_UTF_16_LE, Signatures.XML_UTF_16_BE,
                    Signatures.XML_UTF_32_LE, Signatures.XML_UTF_32_BE)),
    ODB(Constants.MIME_ODB, Constants.EXT_ODB, EnumSet.of(Signatures.ODB)),
    ODC(Constants.MIME_ODC, Constants.EXT_ODC, EnumSet.of(Signatures.ODC)),
    ODF(Constants.MIME_ODF, Constants.EXT_ODF, EnumSet.of(Signatures.ODF)),
    ODG(Constants.MIME_ODG, Constants.EXT_ODG, EnumSet.of(Signatures.ODG)),
    ODI(Constants.MIME_ODI, Constants.EXT_ODI, EnumSet.of(Signatures.ODI)),
    ODM(Constants.MIME_ODM, Constants.EXT_ODM, EnumSet.of(Signatures.ODM)),
    ODP(Constants.MIME_ODP, Constants.EXT_ODP, EnumSet.of(Signatures.ODP)),
    ODS(Constants.MIME_ODS, Constants.EXT_ODS, EnumSet.of(Signatures.ODS)),
    ODT(Constants.MIME_ODT, Constants.EXT_ODT, EnumSet.of(Signatures.ODT)),
    OTC(Constants.MIME_OTC, Constants.EXT_OTC, EnumSet.of(Signatures.OTC)),
    OTF(Constants.MIME_OTF, Constants.EXT_OTF, EnumSet.of(Signatures.OTF)),
    OTG(Constants.MIME_OTG, Constants.EXT_OTG, EnumSet.of(Signatures.OTG)),
    OTH(Constants.MIME_OTH, Constants.EXT_OTH, EnumSet.of(Signatures.OTH)),
    OTI(Constants.MIME_OTI, Constants.EXT_OTI, EnumSet.of(Signatures.OTI)),
    OTM(Constants.MIME_OTM, Constants.EXT_OTM, EnumSet.of(Signatures.OTM)),
    OTP(Constants.MIME_OTP, Constants.EXT_OTP, EnumSet.of(Signatures.OTP)),
    OTS(Constants.MIME_OTS, Constants.EXT_OTS, EnumSet.of(Signatures.OTS)),
    OTT(Constants.MIME_OTT, Constants.EXT_OTT, EnumSet.of(Signatures.OTT)),
    UNKNOWN(Constants.MIME_OCTET, "bin", EnumSet.of(Signatures.NOMATCH));

    static final Set<Formats> ODF_PACKAGES = EnumSet.of(ZIP, ODB, ODT, OTT, ODG, OTG, ODP, OTP, ODS, OTS, ODC, OTC, ODI, OTI, ODF,
            OTF, ODM, OTM, OTH);
    static final Set<Formats> ODF_DOCUMENTS = EnumSet.of(XML, ODB, ODT, OTT, ODG, OTG, ODP, OTP, ODS, OTS, ODC, OTC, ODI, OTI, ODF,
            OTF, ODM, OTM, OTH);
    static final Set<Formats> ODF_SPREADSHEETS = EnumSet.of(ODS, OTS);
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

    public static Formats fromMime(final String mime) {
        Objects.requireNonNull(mime, String.format(Checks.NOT_NULL, "String", "mime"));
        for (final Formats f : Formats.values()) {
            if (f.mime.equals(mime)) {
                return f;
            }
        }
        return UNKNOWN;
    }

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

    final Set<Signatures> signatures;

    private Formats(final String mime, final String extension, final Set<Signatures> signatures) {
        this.mime = mime;
        this.extension = extension;
        this.signatures = EnumSet.copyOf(signatures);
    }

    public boolean isText() {
        return this == XML;
    }
    public boolean isPackage() {
        return ODF_PACKAGES.contains(this);
    }
    public boolean isSpreadsheet() {
        return ODF_SPREADSHEETS.contains(this);
    }

    public boolean isOdf() {
        return ODF_DOCUMENTS.contains(this);
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