package org.openpreservation.odf.xml;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

public class OdfSchemaFactory {
    public enum Version {
        ODF_10("1.0"), ODF_11("1.1"), ODF_12("1.2"), ODF_13("1.3");

        private final String verString;

        private Version(final String version) {
            this.verString = version;
        }

        public static Version fromVersion(final String version) {
            for (final Version v : Version.values()) {
                if (v.verString.equals(version)) {
                    return v;
                }
            }
            return ODF_13;
        }
    }

    private static final String SCHEMA_ROOT = "org/openpreservation/odf/schema/";
    private static final String ODF_ROOT = SCHEMA_ROOT + "odf/";
    private static final String ODF_13_ROOT = ODF_ROOT + "1.3/";
    private static final String ODF_12_ROOT = ODF_ROOT + "1.2/";
    private static final String ODF_11_ROOT = ODF_ROOT + "1.1/";
    private static final String ODF_10_ROOT = ODF_ROOT + "1.0/";
    private static final String SCHEMA_PATH_DSIG_13 = ODF_13_ROOT + "dsig-schema.rng";
    private static final String SCHEMA_PATH_MANIFEST_13 = ODF_13_ROOT + "manifest-schema.rng";
    private static final String SCHEMA_PATH_ODF_13 = ODF_13_ROOT + "schema.rng";
    private static final String SCHEMA_PATH_METADATA_13 = ODF_13_ROOT + "metadata.owl";
    private static final String SCHEMA_PATH_PACKAGE_METADATA_13 = ODF_13_ROOT + "package-metadata.owl";
    private static final String SCHEMA_PATH_DSIG_12 = ODF_12_ROOT + "dsig-schema.rng";
    private static final String SCHEMA_PATH_MANIFEST_12 = ODF_12_ROOT + "manifest-schema.rng";
    private static final String SCHEMA_PATH_ODF_12 = ODF_12_ROOT + "schema.rng";
    private static final String SCHEMA_PATH_METADATA_12 = ODF_12_ROOT + "metadata.owl";
    private static final String SCHEMA_PATH_PACKAGE_METADATA_12 = ODF_12_ROOT + "package-metadata.owl";
    private static final String SCHEMA_PATH_MANIFEST_11 = ODF_11_ROOT + "manifest-schema.rng";
    private static final String SCHEMA_PATH_ODF_11 = ODF_11_ROOT + "schema.rng";
    private static final String SCHEMA_PATH_STRICT_ODF_11 = ODF_11_ROOT + "strict-schema.rng";
    private static final String SCHEMA_PATH_MANIFEST_10 = ODF_10_ROOT + "manifest-schema.rng";
    private static final String SCHEMA_PATH_ODF_10 = ODF_10_ROOT + "schema.rng";
    private static final String SCHEMA_PATH_STRICT_ODF_10 = ODF_10_ROOT + "strict-schema.rng";
    private static final String JAXP_RNG_FACTORY = "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory";
    private static final Map<Version, Map<Namespaces, String>> SCHEMA_LOCATION_MAP = schemaMap();

    private static final Map<Version, Map<Namespaces, String>> schemaMap() {
        final Map<Version, Map<Namespaces, String>> map = new EnumMap<>(Version.class);
        map.put(Version.ODF_13, schemaMap13());
        map.put(Version.ODF_12, schemaMap12());
        map.put(Version.ODF_11, schemaMap11());
        map.put(Version.ODF_10, schemaMap10());
        return map;
    }

    private static final Map<Namespaces, String> schemaMap13() {
        final Map<Namespaces, String> map = new EnumMap<>(Namespaces.class);
        map.put(Namespaces.DSIG, SCHEMA_PATH_DSIG_13);
        map.put(Namespaces.MANIFEST, SCHEMA_PATH_MANIFEST_13);
        map.put(Namespaces.ODF, SCHEMA_PATH_METADATA_13);
        map.put(Namespaces.PKG, SCHEMA_PATH_PACKAGE_METADATA_13);
        map.put(Namespaces.OFFICE, SCHEMA_PATH_ODF_13);
        return map;
    }

    private static final Map<Namespaces, String> schemaMap12() {
        final Map<Namespaces, String> map = new EnumMap<>(Namespaces.class);
        map.put(Namespaces.DSIG, SCHEMA_PATH_DSIG_12);
        map.put(Namespaces.MANIFEST, SCHEMA_PATH_MANIFEST_12);
        map.put(Namespaces.ODF, SCHEMA_PATH_METADATA_12);
        map.put(Namespaces.PKG, SCHEMA_PATH_PACKAGE_METADATA_12);
        map.put(Namespaces.OFFICE, SCHEMA_PATH_ODF_12);
        return map;
    }

    private static final Map<Namespaces, String> schemaMap11() {
        final Map<Namespaces, String> map = new EnumMap<>(Namespaces.class);
        map.put(Namespaces.MANIFEST, SCHEMA_PATH_MANIFEST_11);
        map.put(Namespaces.OFFICE, SCHEMA_PATH_ODF_11);
        return map;
    }

    private static final Map<Namespaces, String> schemaMap10() {
        final Map<Namespaces, String> map = new EnumMap<>(Namespaces.class);
        map.put(Namespaces.MANIFEST, SCHEMA_PATH_MANIFEST_10);
        map.put(Namespaces.OFFICE, SCHEMA_PATH_ODF_10);
        return map;
    }

    private static final SchemaFactory getSchemaFactory() {
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI,
                JAXP_RNG_FACTORY);
        return SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
    }

    private final SchemaFactory rngSchemaFactory = getSchemaFactory();

    public final Schema getSchema(final Namespaces namespace) {
        return getSchemas(EnumSet.of(namespace), Version.ODF_13);
    }

    public final Schema getSchema(final Namespaces namespace, final Version version) {
        Objects.requireNonNull(namespace, String.format(Checks.NOT_NULL, "namespace", "Namspaces"));
        Objects.requireNonNull(version, String.format(Checks.NOT_NULL, "version", "Version"));
        return getSchemas(EnumSet.of(namespace), version);
    }

    public final Schema getSchemas(final Set<Namespaces> namespaces, final Version version) {
        Objects.requireNonNull(namespaces, String.format(Checks.NOT_NULL, "namespace", "Namspaces"));
        Objects.requireNonNull(version, String.format(Checks.NOT_NULL, "version", "Version"));
        if (namespaces.isEmpty()) {
            throw new IllegalArgumentException("At least one namespace must be specified");
        }
        final Source[] sources = getSources(namespaces, version);
        if (sources.length < 1) {
            throw new NoSuchElementException("No schema found for the specified namespace and version.");
        }
        try {
            return rngSchemaFactory.newSchema(getSources(namespaces, version));
        } catch (final SAXException e) {
            throw new IllegalStateException("Problem loading internal schema document", e);
        }
    }

    private final Source[] getSources(final Set<Namespaces> namespaces, final Version version) {
        final List<Source> sources = new ArrayList<>();
        for (final Namespaces namespace : namespaces) {
            final String schemaPath = SCHEMA_LOCATION_MAP.get(version).get(namespace);
            if (schemaPath == null) {
                continue;
            }
            sources.add(new StreamSource(ClassLoader.getSystemResourceAsStream(schemaPath)));
        }
        return sources.toArray(new Source[sources.size()]);
    }
}
