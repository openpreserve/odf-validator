package org.openpreservation.odf.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.format.zip.Zips;
import org.openpreservation.odf.fmt.TestFiles;
import org.xml.sax.SAXException;

public class ValidatorsTest {
    @Test
    public void testInstantiation() throws ParserConfigurationException, SAXException {
        ValidatingParser parser = Validators.getValidatingParser();
        assertNotNull("Validating parser should not be null", parser);
    }

    @Test
    public void testIsStoredValid() {
        ZipEntry entry = Zips.zipEntryInstance("name", 0, 0, 0, java.util.zip.ZipEntry.STORED, false, null);
        assertTrue("Stored compression method should be valid", Validators.isCompressionValid(entry));
    }

    @Test
    public void testIsDefaltedValid() {
        ZipEntry entry = Zips.zipEntryInstance("name", 0, 0, 0, java.util.zip.ZipEntry.DEFLATED, false, null);
        assertTrue("Deflated compression method should be valid", Validators.isCompressionValid(entry));
    }

    @Test
    public void testInValidCompression() {
        ZipEntry entry = Zips.zipEntryInstance("name", 0, 0, 0, java.util.zip.ZipEntry.CENATT, false, null);
        assertFalse("CENATT should NOT be valid", Validators.isCompressionValid(entry));
    }

    @Test
    public void validateSpecificFormat() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validateSpreadsheet(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertTrue("Package should be valid." , report.isValid());
        report = validator.validateSpreadsheet(new File(TestFiles.DSIG_INVALID.toURI()).toPath());
        assertFalse("Package should NOT be valid, spreadsheets only." , report.isValid());
        assertEquals(1, report.getMessages().stream().filter(m -> m.getId().equals("DOC-7")).count());
    }
}
