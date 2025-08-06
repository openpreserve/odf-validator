package org.openpreservation.odf.xml;

/**
 * The {@code Version} enum represents the different versions of the ODF specification.
 * Each enum constant corresponds to a specific version.
 */
public enum Version {
    /**
     * ODF version 1.0.
     */
    ODF_10("1.0"),

    /**
     * ODF version 1.1.
     */
    ODF_11("1.1"),

    /**
     * ODF version 1.2.
     */
    ODF_12("1.2"),

    /**
     * ODF version 1.3.
     */
    ODF_13("1.3"),

    /**
     * ODF version 1.4.
     */
    ODF_14("1.4"),

    /**
     * Unknown version.
     */
    UNKNOWN("unknown");

    /**
     * The version string.
     */
    public final String version;

    public static final String supportedVersions() {
        StringBuilder sb = new StringBuilder();
        String prepend = "";
        for (Version v : Version.values()) {
            if (v != UNKNOWN) {
                sb.append(prepend).append(v.version);
                prepend = ", ";
            }
        }
        return sb.toString().trim();
    }

    /**
     * Constructs a {@code Version} enum constant with the specified version string.
     *
     * @param version the version string
     */
    private Version(final String version) {
        this.version = version;
    }

    /**
     * Returns the {@code Version} enum constant based on the provided version string.
     *
     * @param version the version string to identify
     * @return the identified {@code Version} enum constant, or {@code UNKNOWN} if no match is found
     */
    public static Version fromVersion(final String version) {
        for (final Version v : Version.values()) {
            if (v.version.equals(version)) {
                return v;
            }
        }
        return UNKNOWN;
    }
}
