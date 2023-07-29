package org.openpreservation.odf.fmt;

import java.nio.charset.StandardCharsets;

import org.openpreservation.utils.Checks;

public enum MimeTypes {
    ZIP("application/zip", "zip"),
    XML("text/xml", "xml"),
    ODS("application/vnd.oasis.opendocument.spreadsheet", "ods"),
    OTS("application/vnd.oasis.opendocument.spreadsheet-template", "ots"),
    UNKNOWN("application/octet-stream", "bin");

    private final static String STRING = "String";

    /**
     * The String MIME type identifier
     */
    public final String mime;
    /**
     * The String extension associated with the MIME type
     */
    public final String extension;

    private MimeTypes(final String mime, final String extension) {
        this.mime = mime;
        this.extension = extension;
    }

    /**
     * Get the MIME type as an ASCII encoded byte array.
     * 
     * @return the ASCII encoded MIME type id as a byte array
     */
    public byte[] getBytes() {
        return this.mime.getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Get the MimeType associated with the given extension.
     * 
     * @param ext the String extension to match the MimeType for
     * 
     * @return The MimeType associated with the given extension, or UNKNOWN if not
     *         matched
     */
    public static MimeTypes fromExtension(final String ext) {
        Checks.notNull(ext, STRING, "ext");
        final String match = ext.toLowerCase();
        for (MimeTypes m : MimeTypes.values()) {
            if (m.extension.equals(match)) {
                return m;
            }
        }
        return UNKNOWN;
    }

    public static MimeTypes fromMime(final String mime) {
        Checks.notNull(mime, STRING, "mime");
        final String match = mime.toLowerCase();
        for (MimeTypes m : MimeTypes.values()) {
            if (m.mime.equals(match)) {
                return m;
            }
        }
        return UNKNOWN;
    }

    public static boolean isTemplate(final String mime) {
        Checks.notNull(mime, STRING, "mime");
        return mime.toLowerCase().equals(OTS.mime);
    }

    public static boolean isDocument(final String mime) {
        Checks.notNull(mime, STRING, "mime");
        return mime.toLowerCase().equals(ODS.mime);
    }
}
