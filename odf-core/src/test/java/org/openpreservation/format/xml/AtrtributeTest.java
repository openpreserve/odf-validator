package org.openpreservation.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AtrtributeTest {
    @Test
    public void testInstantiation() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    AttributeImpl.of(null);
                });
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    AttributeImpl.of(-1, "qName", "value", XmlTestUtils.exampleUri, "type");
                });
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    AttributeImpl.of(0, "", "value", XmlTestUtils.exampleUri, "type");
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    AttributeImpl.of(0, null, "value", XmlTestUtils.exampleUri, "type");
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    AttributeImpl.of(0, "qName", null, XmlTestUtils.exampleUri, "type");
                });
    }
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(AttributeImpl.class).verify();
    }


    @Test
    public void testGetIndex() {
        Attribute attribute = AttributeImpl.of(0, "qName", "value", XmlTestUtils.exampleUri, "type");
        assertEquals("Atrribute index should be 0", 0, attribute.getIndex());
    }

    @Test
    public void testGetQualifiedName() {
        Attribute attribute = AttributeImpl.of(0, "prefix:name", "value", XmlTestUtils.exampleUri, "type");
        assertEquals("Qualified name should be \"prefix:name\"", "prefix:name", attribute.getQualifiedName());
    }

    @Test
    public void testGetQualifiedNameNoPrefix() {
        Attribute attribute = AttributeImpl.of(0, "name", "value", XmlTestUtils.exampleUri, "type");
        assertEquals("Qualified name should be \"name\"", "name", attribute.getQualifiedName());
    }

    @Test
    public void testGetLocalName() {
        Attribute attribute = AttributeImpl.of(0, "prefix:name", "value", XmlTestUtils.exampleUri, "type");
        assertEquals("Local name should be \"name\"", "name", attribute.getLocalName());
    }

    @Test
    public void testGetLocalNameNoPrefix() {
        Attribute attribute = AttributeImpl.of(0, "name", "value", XmlTestUtils.exampleUri, "type");
        assertEquals("Local name should be \"name\"", "name", attribute.getLocalName());
    }

    @Test
    public void testGetPrefix() {
        Attribute attribute = AttributeImpl.of(0, "prefix:name", "value", XmlTestUtils.exampleUri, "type");
        assertEquals("Prefix should be \"prefix\"", "prefix", attribute.getPrefix());
    }

    @Test
    public void testGetPrefixNoPrefix() {
        Attribute attribute = AttributeImpl.of(0, "name", "value", XmlTestUtils.exampleUri, "type");
        assertEquals("Prefix should be \"\"", "", attribute.getPrefix());
    }
}
