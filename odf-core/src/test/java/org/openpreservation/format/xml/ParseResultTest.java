package org.openpreservation.format.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openpreservation.messages.Message;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ParseResultTest {
    @Test
    public void testIInstantiation() {
        List<Attribute> attributes = new ArrayList<>();
        List<Namespace> namespaces = new ArrayList<>();
        List<Message> messages = new ArrayList<>();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, null, "rootPrefix", "rootName",
                            attributes, messages);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, namespaces, "rootPrefix", "rootName",
                           attributes, null);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ParseResultImpl.class).verify();
    }

    @Test
    public void testIsRootNameNull() {
        ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(), "rootPrefix", null, new ArrayList<>(), new ArrayList<>());
        assertFalse("Null String should not match root name.", parseResult.isRootName(null));
    }

    @Test
    public void testIsRootNameEmpty() {
        ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(), "rootPrefix", "", new ArrayList<>(), new ArrayList<>());
        assertFalse("Empty String should not match root name.", parseResult.isRootName(""));
    }

    @Test
    public void testIsRootName() {
        ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(), "rootPrefix", "rootName", new ArrayList<>(), new ArrayList<>());
        assertTrue("Empty String should not match root name.", parseResult.isRootName("rootName"));
        assertFalse("Empty String should not match root name.", parseResult.isRootName("notName"));
    }
}
