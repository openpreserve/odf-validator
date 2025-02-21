package org.openpreservation.odf.pkg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Constants {
    public static final String SIG_TERM = "signatures";
    public static final String NAME_META_INF = "META-INF/";
    public static final String NAME_MANIFEST = "manifest.xml";
    public static final String PATH_MANIFEST = NAME_META_INF + NAME_MANIFEST;
    public static final String NAME_CONTENT = "content.xml";
    public static final String NAME_STYLES = "styles.xml";
    public static final String NAME_SETTINGS = "settings.xml";
    public static final String NAME_META = "meta.xml";
    public static final String NAME_MANIFEST_RDF = "manifest.rdf";
    public static final String NAME_THUMBNAIL = "thumbnail.png";
    public static final String PATH_THUMBNAIL = "Thumbnails/" + NAME_THUMBNAIL;
    public static final String TAG_MANIFEST = "manifest";
    public static final Set<String> ODF_XML = odfXml();

    private Constants() {
        throw new AssertionError("Constants class is not instantiable");
    }

    private static final Set<String> odfXml() {
        final String[] odfXml = { NAME_CONTENT, NAME_STYLES, NAME_META, NAME_SETTINGS, NAME_MANIFEST_RDF };
        return new HashSet<>(Arrays.asList(odfXml));
    }
}
