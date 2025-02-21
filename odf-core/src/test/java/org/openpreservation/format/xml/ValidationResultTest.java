package org.openpreservation.format.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openpreservation.odf.validation.messages.Message;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ValidationResultTest {
    private static ParseResult parseResult = ParseResultImpl.of(true, XmlTestUtils.exampleNamespace, new ArrayList<>(),
            new ArrayList<>(),
            "prefix", "name", new ArrayList<>(), new ArrayList<>());

    @Test
    public void testInstantiation() {
        List<Message> messages = new ArrayList<>();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    XmlValidationResultImpl.of(null, true, messages);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    XmlValidationResultImpl.of(parseResult, false, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    XmlValidationResultImpl.valid(null, messages);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    XmlValidationResultImpl.valid(parseResult, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    XmlValidationResultImpl.notValid(null, messages);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    XmlValidationResultImpl.notValid(parseResult, null);
                });
    }

    @Test
    public void testValid() {
        XmlValidationResult result = XmlValidationResultImpl.valid(parseResult, new ArrayList<>());
        assertTrue("Validation result should be valid.", result.isValid());
    }

    @Test
    public void testNotValid() {
        XmlValidationResult result = XmlValidationResultImpl.notValid(parseResult, new ArrayList<>());
        assertFalse("Validation result should not be valid.", result.isValid());
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(XmlValidationResultImpl.class).verify();
    }
}
