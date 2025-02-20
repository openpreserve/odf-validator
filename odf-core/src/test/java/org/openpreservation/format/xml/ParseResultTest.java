package org.openpreservation.format.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Message.Severity;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ParseResultTest {
    private static final Message errorMessage = Messages.getMessageInstance("err", Severity.ERROR, "ERROR");
    private static final Message warningMessage = Messages.getMessageInstance("wrn", Severity.WARNING, "WARNING");
    private static final Message infoMessage = Messages.getMessageInstance("inf", Severity.INFO, "INFO");

    private static final List<Message> errorList = new ArrayList<>(
            Arrays.asList(errorMessage, warningMessage, infoMessage));
    private static final List<Message> noErrorList = new ArrayList<>(Arrays.asList(warningMessage, infoMessage));

    @Test
    public void testIInstantiation() {
        List<Attribute> attributes = new ArrayList<>();
        List<Namespace> namespaces = new ArrayList<>();
        List<Message> messages = new ArrayList<>();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, null, namespaces, "rootPrefix", "rootName",
                            attributes, messages);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, namespaces, null, "rootPrefix", "rootName",
                            attributes, messages);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, namespaces, namespaces, "rootPrefix",
                            "rootName",
                            attributes, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ParseResultImpl.of(XmlTestUtils.exampleNamespace, namespaces, namespaces, "rootPrefix", "rootName",
                            attributes, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    ParseResultImpl.invertWellFormed(null);
                });
    }

    @Test
    public void testDetectWellFormed() {
        ParseResult parseResult = ParseResultImpl.of(XmlTestUtils.exampleNamespace, new ArrayList<>(),
                new ArrayList<>(), "rootPrefix",
                "rootName", new ArrayList<>(), errorList);
        assertFalse("ParseResult should NOT be well-formed.", parseResult.isWellFormed());
        parseResult = ParseResultImpl.of(XmlTestUtils.exampleNamespace, new ArrayList<>(), new ArrayList<>(),
                "rootPrefix", "rootName",
                new ArrayList<>(), noErrorList);
        assertTrue("ParseResult should be well-formed.", parseResult.isWellFormed());
    }

    @Test
    public void testInvertWellFormed() {
        ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(),
                new ArrayList<>(),
                "rootPrefix", "rootName", new ArrayList<>(), new ArrayList<>());
        ParseResult inverted = ParseResultImpl.invertWellFormed(parseResult);
        assertTrue("Original parse result should be well-formed.", parseResult.isWellFormed());
        assertFalse("Inverted parse result should NOT be well-formed.", inverted.isWellFormed());
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ParseResultImpl.class).verify();
    }

    @Test
    public void testIsRootNameNull() {
        ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(),
                new ArrayList<>(),
                "rootPrefix", null, new ArrayList<>(), new ArrayList<>());
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    parseResult.isRootName(null);
                });
    }

    @Test
    public void testIsRootNameEmpty() {
        ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(),
                new ArrayList<>(),
                "rootPrefix", "", new ArrayList<>(), new ArrayList<>());
        assertTrue("Empty String should match root name.", parseResult.isRootName(""));
    }

    @Test
    public void testIsRootName() {
        ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(),
                new ArrayList<>(),
                "rootPrefix", "rootName", new ArrayList<>(), new ArrayList<>());
        assertTrue("Empty String should not match root name.", parseResult.isRootName("rootName"));
        assertFalse("Empty String should not match root name.", parseResult.isRootName("notName"));
    }
}
