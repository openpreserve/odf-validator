package org.openpreservation.odf.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamespacesTest {
    static final String MANIFEST_ID = "urn:oasis:names:tc:opendocument:xmlns:manifest:1.0";
    static final String DSIG_ID = "urn:oasis:names:tc:opendocument:xmlns:digitalsignature:1.0";
    static final String PKG = "http://docs.oasis-open.org/ns/office/1.2/meta/pkg#";
    static final String DS = "http://www.w3.org/2000/09/xmldsig#";

    @Test
    public void testFromPrefix() {
        assertEquals(MANIFEST_ID, Namespaces.fromPrefix("manifest").id.toString());
        assertEquals(DSIG_ID, Namespaces.fromPrefix("dsig").id.toString());
        assertEquals(PKG, Namespaces.fromPrefix("pkg").id.toString());
        assertEquals(DS, Namespaces.fromPrefix("ds").id.toString());
        assertNull(Namespaces.fromPrefix("foo"));
    }

    @Test
    public void testFromId() {
        assertEquals("manifest", Namespaces.fromId(MANIFEST_ID).prefix);
        assertEquals("dsig", Namespaces.fromId(DSIG_ID).prefix);
        assertEquals("pkg", Namespaces.fromId(PKG).prefix);
        assertEquals("ds", Namespaces.fromId(DS).prefix);
        assertNull(Namespaces.fromId("foo"));
    }
}
