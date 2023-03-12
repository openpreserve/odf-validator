package org.openpreservation.odf.fmt;

import java.util.EnumSet;
import java.util.Set;

import org.openpreservation.utils.Checks;

public enum Formats {
    ZIP("application/zip", EnumSet.of(Signatures.ZIP, Signatures.ZIP_EMPTY, Signatures.ZIP_SPANNED)),
    XML("text/xml", EnumSet.of(Signatures.XML_UTF_8, Signatures.XML_UTF_16_LE, Signatures.XML_UTF_16_BE,
            Signatures.XML_UTF_32_LE, Signatures.XML_UTF_32_BE)),
    ODS("application/vnd.oasis.opendocument.spreadsheet", EnumSet.of(Signatures.ODS)),
    OTS("application/vnd.oasis.opendocument.spreadsheet-template", EnumSet.of(Signatures.ODT)),
    UNKNOWN("application/octet-stream", EnumSet.of(Signatures.NOMATCH));

    public final String mime;
    public final String extension;
    final Set<Signatures> signatures;

    private Formats(final String mime, final Set<Signatures> signatures) {
        this.mime = mime;
        this.extension = this.name().replace("UNKNOWN", "").toLowerCase();
        this.signatures = EnumSet.copyOf(signatures);
    }

    static final Set<Formats> ODF_PACKAGES = EnumSet.of(ODS, OTS);
    static final Set<Formats> ODF = EnumSet.of(XML, ODS, OTS);
    static final Set<Formats> ZIPS = EnumSet.of(ZIP, ODS, OTS);

    public boolean isText() {
        return this == XML;
    }

    public boolean isPackage() {
        return ODF_PACKAGES.contains(this);
    }

    public boolean isOdf() {
        return ODF.contains(this);
    }

    public boolean isZip() {
        return ZIPS.contains(this);
    }

    public static Formats identify(final byte[] bytes) {
        Signatures sig = Signatures.match(bytes);
        for (Formats f : Formats.values()) {
            if (f.signatures.contains(sig)) {
                return f;
            }
        }
        return UNKNOWN;
    }

    public static Formats fromMime(final String mime) {
        Checks.notNull(mime, "String", "mime");
        for (Formats f : Formats.values()) {
            if (f.mime.equals(mime.toLowerCase())) {
                return f;
            }
        }
        return UNKNOWN;
    }

    public static Formats fromExtension(final String ext) {
        for (Formats f : Formats.values()) {
            if (f.extension.equals(ext.toLowerCase())) {
                return f;
            }
        }
        return UNKNOWN;
    }
}