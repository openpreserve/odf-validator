package org.openpreservation.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;

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
    public void testInstantiationIllegalUrl() throws MalformedURLException {
        URL illegalUrl = new URL("http://www.example.com/?Type=Type A&Name=Name&Char=!");
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    NamespaceImpl.of(XmlTestUtils.exampleUri, "example", illegalUrl);
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

    @Test
    public void testGetSchemaLocation() {
        Namespace ns = NamespaceImpl.of(XmlTestUtils.exampleUri, "example", XmlTestUtils.exampleUrl);
        assertEquals("ID should equal example URI", XmlTestUtils.exampleUrl, ns.getSchemalocation());
    }
}
