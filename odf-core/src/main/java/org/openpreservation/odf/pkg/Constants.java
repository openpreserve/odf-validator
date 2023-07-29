package org.openpreservation.odf.pkg;

public class Constants {
    static final String MIMETYPE = "mimetype";
    static final String NAME_META_INF = "META-INF/";
    static final String NAME_MANIFEST = "manifest.xml";
    static final String PATH_MANIFEST = NAME_META_INF + NAME_MANIFEST;
    static final String NAME_CONTENT = "content.xml";
    static final String TAG_MANIFEST = "manifest:manifest";

    private Constants() {
        throw new AssertionError("Constants class is not instantiable");
    }
}
