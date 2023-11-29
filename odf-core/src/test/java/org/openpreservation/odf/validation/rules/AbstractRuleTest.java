package org.openpreservation.odf.validation.rules;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AbstractRuleTest {
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(AbstractRule.class).verify();
    }
}
