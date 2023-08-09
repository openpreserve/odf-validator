package org.openpreservation.odf.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class MetadataImpl_UserDefinedFieldImplTest {
    @Test
    public void testOf() {
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    MetadataImpl.UserDefinedFieldImpl.of(null, "valueType", "value");
                });
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(MetadataImpl.UserDefinedFieldImpl.class).verify();
    }

    @Test
    public void testGetName() {
        Metadata.UserDefinedField udf = MetadataImpl.UserDefinedFieldImpl.of("name", "valueType", "value");
        assertEquals("Error with retrieved name value", "name", udf.getName());
    }

    @Test
    public void testGetValue() {
        Metadata.UserDefinedField udf = MetadataImpl.UserDefinedFieldImpl.of("name", "valueType", "value");
        assertEquals("Error with retrieved name value", "value", udf.getValue());
    }

    @Test
    public void testGetValueType() {
        Metadata.UserDefinedField udf = MetadataImpl.UserDefinedFieldImpl.of("name", "valueType", "value");
        assertEquals("Error with retrieved name value", "valueType", udf.getValueType());
    }
}
