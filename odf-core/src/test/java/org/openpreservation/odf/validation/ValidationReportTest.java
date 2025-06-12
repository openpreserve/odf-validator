package org.openpreservation.odf.validation;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ValidationReportTest {
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ValidationResultImpl.class).verify();
    }

    @Test
    public void testJsonSerialisation() throws JsonProcessingException, FileNotFoundException, ParseException, URISyntaxException {
        var mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        OdfValidator validator = OdfValidators.getOdfValidator();
        ValidationReport report = validator.validate(new File(TestFiles.NO_MIME_NO_ROOT_ODS.toURI()).toPath());
        String json = mapper.writeValueAsString(report);
        assertFalse(json.isEmpty());
    }

    @Test
    public void testXmlSerialisation() throws FileNotFoundException, ParseException, URISyntaxException, JsonProcessingException {
        OdfValidator validator = OdfValidators.getOdfValidator();
        ValidationReport report = validator.validate(new File(TestFiles.NO_MIME_NO_ROOT_ODS.toURI()).toPath());
        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(report);
        assertFalse(xml.isEmpty());
    }
}
