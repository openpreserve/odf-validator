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

    private static final String SCHEMA_ROOT = "org/openpreservation/odf/schema/";
    private static final String ODF_ROOT = SCHEMA_ROOT + "odf/";
    private static final String ODF_14_ROOT = ODF_ROOT + "1.4/";
    private static final String ODF_13_ROOT = ODF_ROOT + "1.3/";
    private static final String ODF_12_ROOT = ODF_ROOT + "1.2/";
    private static final String ODF_11_ROOT = ODF_ROOT + "1.1/";
    private static final String ODF_10_ROOT = ODF_ROOT + "1.0/";
    private static final String SCHEMA_NAME_MANIFEST = "manifest-schema.rng";
    private static final String SCHEMA_NAME_DSIG = "dsig-schema.rng";
    private static final String SCHEMA_NAME = "schema.rng";
    private static final String SCHEMA_NAME_STRICT = "strict-schema.rng";
    private static final String SCHEMA_NAME_METADATA = "metadata.owl";
    private static final String SCHEMA_NAME_PACKAGE_METADATA = "package-metadata.owl";
    private static final String SCHEMA_PATH_DSIG_14 = ODF_14_ROOT + SCHEMA_NAME_DSIG;
    private static final String SCHEMA_PATH_MANIFEST_14 = ODF_14_ROOT + SCHEMA_NAME_MANIFEST;
    private static final String SCHEMA_PATH_ODF_14 = ODF_14_ROOT + SCHEMA_NAME;
    private static final String SCHEMA_PATH_METADATA_14 = ODF_14_ROOT + SCHEMA_NAME_METADATA;
    private static final String SCHEMA_PATH_PACKAGE_METADATA_14 = ODF_14_ROOT + SCHEMA_NAME_PACKAGE_METADATA;
    private static final String SCHEMA_PATH_DSIG_13 = ODF_13_ROOT + SCHEMA_NAME_DSIG;
    private static final String SCHEMA_PATH_MANIFEST_13 = ODF_13_ROOT + SCHEMA_NAME_MANIFEST;
    private static final String SCHEMA_PATH_ODF_13 = ODF_13_ROOT + SCHEMA_NAME;
    private static final String SCHEMA_PATH_METADATA_13 = ODF_13_ROOT + SCHEMA_NAME_METADATA;
    private static final String SCHEMA_PATH_PACKAGE_METADATA_13 = ODF_13_ROOT + SCHEMA_NAME_PACKAGE_METADATA;
    private static final String SCHEMA_PATH_DSIG_12 = ODF_12_ROOT + SCHEMA_NAME_DSIG;
    private static final String SCHEMA_PATH_MANIFEST_12 = ODF_12_ROOT + SCHEMA_NAME_MANIFEST;
    private static final String SCHEMA_PATH_ODF_12 = ODF_12_ROOT + SCHEMA_NAME;
    private static final String SCHEMA_PATH_METADATA_12 = ODF_12_ROOT + SCHEMA_NAME_METADATA;
    private static final String SCHEMA_PATH_PACKAGE_METADATA_12 = ODF_12_ROOT + SCHEMA_NAME_PACKAGE_METADATA;
    private static final String SCHEMA_PATH_MANIFEST_11 = ODF_11_ROOT + SCHEMA_NAME_MANIFEST;
    private static final String SCHEMA_PATH_ODF_11 = ODF_11_ROOT + SCHEMA_NAME;
    private static final String SCHEMA_PATH_STRICT_ODF_11 = ODF_11_ROOT + SCHEMA_NAME_STRICT;
    private static final String SCHEMA_PATH_MANIFEST_10 = ODF_10_ROOT + SCHEMA_NAME_MANIFEST;
    private static final String SCHEMA_PATH_ODF_10 = ODF_10_ROOT + SCHEMA_NAME;
    private static final String SCHEMA_PATH_STRICT_ODF_10 = ODF_10_ROOT + SCHEMA_NAME_STRICT;
    private static final String JAXP_RNG_FACTORY = "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory";
    private static final Map<Version, Map<OdfNamespaces, String>> SCHEMA_LOCATION_MAP = schemaMap();

    private static final Map<Version, Map<OdfNamespaces, String>> schemaMap() {
        final Map<Version, Map<OdfNamespaces, String>> map = new EnumMap<>(Version.class);
        map.put(Version.ODF_14, schemaMap14());
        map.put(Version.ODF_13, schemaMap13());
        map.put(Version.ODF_12, schemaMap12());
        map.put(Version.ODF_11, schemaMap11());
        map.put(Version.ODF_10, schemaMap10());
        return map;
    }

    private static final Map<OdfNamespaces, String> schemaMap14() {
        final Map<OdfNamespaces, String> map = new EnumMap<>(OdfNamespaces.class);
        map.put(OdfNamespaces.DSIG, SCHEMA_PATH_DSIG_14);
        map.put(OdfNamespaces.MANIFEST, SCHEMA_PATH_MANIFEST_14);
        map.put(OdfNamespaces.ODF, SCHEMA_PATH_METADATA_14);
        map.put(OdfNamespaces.PKG, SCHEMA_PATH_PACKAGE_METADATA_14);
        map.put(OdfNamespaces.OFFICE, SCHEMA_PATH_ODF_14);
        return map;
    }

    private static final Map<OdfNamespaces, String> schemaMap13() {
        final Map<OdfNamespaces, String> map = new EnumMap<>(OdfNamespaces.class);
        map.put(OdfNamespaces.DSIG, SCHEMA_PATH_DSIG_13);
        map.put(OdfNamespaces.MANIFEST, SCHEMA_PATH_MANIFEST_13);
        map.put(OdfNamespaces.ODF, SCHEMA_PATH_METADATA_13);
        map.put(OdfNamespaces.PKG, SCHEMA_PATH_PACKAGE_METADATA_13);
        map.put(OdfNamespaces.OFFICE, SCHEMA_PATH_ODF_13);
        return map;
    }

    private static final Map<OdfNamespaces, String> schemaMap12() {
        final Map<OdfNamespaces, String> map = new EnumMap<>(OdfNamespaces.class);
        map.put(OdfNamespaces.DSIG, SCHEMA_PATH_DSIG_12);
        map.put(OdfNamespaces.MANIFEST, SCHEMA_PATH_MANIFEST_12);
        map.put(OdfNamespaces.ODF, SCHEMA_PATH_METADATA_12);
        map.put(OdfNamespaces.PKG, SCHEMA_PATH_PACKAGE_METADATA_12);
        map.put(OdfNamespaces.OFFICE, SCHEMA_PATH_ODF_12);
        return map;
    }

    private static final Map<OdfNamespaces, String> schemaMap11() {
        final Map<OdfNamespaces, String> map = new EnumMap<>(OdfNamespaces.class);
        map.put(OdfNamespaces.MANIFEST, SCHEMA_PATH_MANIFEST_11);
        map.put(OdfNamespaces.OFFICE, SCHEMA_PATH_ODF_11);
        return map;
    }

    private static final Map<OdfNamespaces, String> schemaMap10() {
        final Map<OdfNamespaces, String> map = new EnumMap<>(OdfNamespaces.class);
        map.put(OdfNamespaces.MANIFEST, SCHEMA_PATH_MANIFEST_10);
        map.put(OdfNamespaces.OFFICE, SCHEMA_PATH_ODF_10);
        return map;
    }

    private static final SchemaFactory getSchemaFactory() {
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI,
                JAXP_RNG_FACTORY);
        return SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
    }

    private final SchemaFactory rngSchemaFactory = getSchemaFactory();

    public final Schema getSchema(final OdfNamespaces namespace) {
        return getSchemas(EnumSet.of(namespace), Version.ODF_13);
    }

    public final Schema getSchema(final OdfNamespaces namespace, final Version version) {
        Objects.requireNonNull(namespace, String.format(Checks.NOT_NULL, "namespace", "Namspaces"));
        Objects.requireNonNull(version, String.format(Checks.NOT_NULL, "version", "Version"));
        return getSchemas(EnumSet.of(namespace), version);
    }

    public final Schema getSchemas(final Set<OdfNamespaces> namespaces, final Version version) {
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

    private final Source[] getSources(final Set<OdfNamespaces> namespaces, final Version version) {
        final List<Source> sources = new ArrayList<>();
        for (final OdfNamespaces namespace : namespaces) {
            Map <OdfNamespaces, String> schemaMap = SCHEMA_LOCATION_MAP.get(version);
            if (schemaMap == null) {
                throw new IllegalArgumentException("No schemas found for ODF version: " + version.version + ", supported versions are: " + Version.supportedVersions());
            }
            final String schemaPath = schemaMap.get(namespace);
            if (schemaPath == null) {
                continue;
            }
            sources.add(new StreamSource(ClassLoader.getSystemResourceAsStream(schemaPath)));
        }
        return sources.toArray(new Source[sources.size()]);
    }
}
