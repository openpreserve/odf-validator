package org.openpreservation.odf.validation.messages;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class MessageImplTest {
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(MessageImpl.class).verify();
    }
}
