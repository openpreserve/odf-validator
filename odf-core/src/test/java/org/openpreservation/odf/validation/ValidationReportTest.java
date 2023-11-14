package org.openpreservation.odf.validation;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ValidationReportTest {

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ValidationReport.class).verify();
    }
}
