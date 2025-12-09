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
        }
        if (dt != null) {
          return dt.getFormats().iterator().next();
        }
        dt = DocumentType.getTypeByBodyElement(search);
        if (dt != null) {
          return dt.getFormats().iterator().next();
        }
        dt = DocumentType.getTypeByMimeString(search);
        if (dt != null) {
          return dt.getFormats().iterator().next();
        }
        return Formats.UNKNOWN;
    }
}
