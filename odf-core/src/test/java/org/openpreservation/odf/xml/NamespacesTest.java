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
        assertEquals(MANIFEST_ID, OdfNamespaces.fromPrefix("manifest").getId().toString());
        assertEquals(DSIG_ID, OdfNamespaces.fromPrefix("dsig").getId().toString());
        assertEquals(PKG, OdfNamespaces.fromPrefix("pkg").getId().toString());
        assertEquals(DS, OdfNamespaces.fromPrefix("ds").getId().toString());
        assertNull(OdfNamespaces.fromPrefix("foo"));
    }

    @Test
    public void testFromId() {
        assertEquals("manifest", OdfNamespaces.fromId(MANIFEST_ID).getPrefix());
        assertEquals("dsig", OdfNamespaces.fromId(DSIG_ID).getPrefix());
        assertEquals("pkg", OdfNamespaces.fromId(PKG).getPrefix());
        assertEquals("ds", OdfNamespaces.fromId(DS).getPrefix());
        assertNull(OdfNamespaces.fromId("foo"));
    }
}
