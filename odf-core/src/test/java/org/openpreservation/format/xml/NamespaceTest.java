package org.openpreservation.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class NamespaceTest {

    @Test
    public void testInstantiationNullId() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    NamespaceImpl.of(null, "example", XmlTestUtils.exampleUrl);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(NamespaceImpl.class).verify();
    }

    @Test
    public void testInstantiationNullPrefix() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    NamespaceImpl.of(XmlTestUtils.exampleUri, null, XmlTestUtils.exampleUrl);
                });
    }

    @Test
    public void testGetNamespace() {
        Namespace ns = NamespaceImpl.of(XmlTestUtils.exampleUri, "example", XmlTestUtils.exampleUrl);
        assertEquals("ID should equal example URI", XmlTestUtils.exampleUri, ns.getId());
    }

    @Test
    public void testGetPrefix() {
        Namespace ns = NamespaceImpl.of(XmlTestUtils.exampleUri, "example", XmlTestUtils.exampleUrl);
        assertEquals("Prefix should equal example", "example", ns.getPrefix());
    }
}
