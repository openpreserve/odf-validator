package org.openpreservation.odf.apps;

import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.xml.DocumentType;

import picocli.CommandLine.ITypeConverter;

public class FormatsConverter implements ITypeConverter<Formats> {
    @Override
    public Formats convert(final String value) throws Exception {
        final String search = value.toLowerCase().trim();
        Formats format = Formats.fromExtension(search);
        if (format != Formats.UNKNOWN) {
            return format;
        }
        format = Formats.fromMime(search);
        if (format != Formats.UNKNOWN) {
            return format;
        }
        DocumentType dt = null;
        try {
            dt = DocumentType.valueOf(search.toUpperCase());
        } catch (final IllegalArgumentException e) {
            /**
             * Simply means that the document type was not found by name, continue searching.
             */
        }
        if (dt != null) {
            return getFirstFormatFromDocumentType(dt);
        }
        dt = DocumentType.getTypeByBodyElement(search);
        if (dt != null) {
            return getFirstFormatFromDocumentType(dt);
        }
        dt = DocumentType.getTypeByMimeString(search);
        if (dt != null) {
            return getFirstFormatFromDocumentType(dt);
        }
        return Formats.UNKNOWN;
    }

    final static private Formats getFirstFormatFromDocumentType(final DocumentType dt) {
        // NOTE: If the DocumentType has multiple associated formats, this will select the first format in the set,
        // which may not align with user expectations. To ensure a specific format is chosen, specify the format
        // extension or MIME type directly instead of the document type name.
        return dt.getFormats().iterator().next();
    }
}
