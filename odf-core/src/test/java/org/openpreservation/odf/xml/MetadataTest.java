package org.openpreservation.odf.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.openpreservation.odf.xml.Metadata.UserDefinedField;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class MetadataTest {
    private static final String VERSION = "1.2";
    private Metadata metadata;

    @Before
    public void setUp() {
        Map<String, String> stringValues = stringValues();
        List<UserDefinedField> userDefinedFields = userDefinedFields();
        this.metadata = MetadataImpl.of(VERSION, stringValues, userDefinedFields);
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(MetadataImpl.class).verify();
        EqualsVerifier.forClass(MetadataImpl.UserDefinedFieldImpl.class).verify();
    }

    @Test
    public void testOf() {
        Map<String, String> stringValues = new HashMap<>();
        List<UserDefinedField> userDefinedFields = new ArrayList<>();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    MetadataImpl.of(VERSION, null, userDefinedFields);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    MetadataImpl.of(VERSION, stringValues, null);
                });
    }

    @Test
    public void testGetStringValue() {
        assertEquals("dc:date value should equal the example value.", "2023-02-15T14:36:57.946037020",
                this.metadata.getStringValue("dc:date"));
        assertEquals("meta:generator value should equal the example value.",
                "LibreOffice/7.4.4.2$Linux_X86_64 LibreOffice_project/40$Build-2",
                this.metadata.getStringValue("meta:generator"));
    }

    @Test
    public void testGetStringValue2() {
        assertEquals("dc:date value should equal the example value.", "2023-02-15T14:36:57.946037020",
                this.metadata.getStringValue("dc", "date"));
        assertEquals("meta:generator value should equal the example value.",
                "LibreOffice/7.4.4.2$Linux_X86_64 LibreOffice_project/40$Build-2",
                this.metadata.getStringValue("meta", "generator"));
    }

    @Test
    public void testGetStringValueMap() {
        assertEquals("Generated example Map should equal the example map", stringValues(),
                this.metadata.getStringValueMap());
        assertNotSame("Generated example Map should not be same object as example map.", stringValues(),
                this.metadata.getStringValueMap());
    }

    @Test
    public void testGetUserDefinedFields() {
        assertEquals("", userDefinedFields(), this.metadata.getUserDefinedFields());
        assertNotSame("", userDefinedFields(), this.metadata.getUserDefinedFields());
    }

    @Test
    public void testGetVersion() {
        assertEquals("", VERSION, this.metadata.getVersion());
    }

    @Test
    public void testFromStream() throws ParserConfigurationException, SAXException, IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream("org/openpreservation/odf/pkg/meta.xml");
        Metadata metadata = MetadataImpl.from(is);
        assertEquals("Manifest version should be 1.3", "1.3", metadata.getVersion());
        assertEquals("Generator key should give expected value",
                "LibreOffice/7.0.4.2$Linux_X86_64 LibreOffice_project/00$Build-2",
                metadata.getStringValue("meta:generator"));
    }

    private static final Map<String, String> stringValues() {
        Map<String, String> stringValues = new HashMap<>();
        stringValues.put("dc:date", "2023-02-15T14:36:57.946037020");
        stringValues.put("meta:generator", "LibreOffice/7.4.4.2$Linux_X86_64 LibreOffice_project/40$Build-2");
        return stringValues;
    }

    private static final List<UserDefinedField> userDefinedFields() {
        List<UserDefinedField> userDefinedFields = new ArrayList<>();
        UserDefinedField udf = MetadataImpl.UserDefinedFieldImpl.of(":date", "dateTime",
                "2023-02-15T14:36:57.946037020");
        userDefinedFields.add(udf);
        return userDefinedFields;
    }
}
