package org.openpreservation.odf.xml;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class VersionTest {
    @Test
    public void testFromVersion() {
        assertSame(Version.ODF_10, Version.fromVersion("1.0"));
        assertSame(Version.ODF_11, Version.fromVersion("1.1"));
        assertSame(Version.ODF_12, Version.fromVersion("1.2"));
        assertSame(Version.ODF_13, Version.fromVersion("1.3"));
        assertSame(Version.UNKNOWN, Version.fromVersion("1.4"));
    }
}
