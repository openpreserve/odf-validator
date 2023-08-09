package org.openpreservation.odf.xml;

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
        return null;
    }
}
