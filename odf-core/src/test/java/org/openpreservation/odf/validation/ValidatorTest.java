package org.openpreservation.odf.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.xml.sax.SAXException;

public class ValidatorTest {

    @Test
    public void validateNullPath() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        Validator validator = new Validator();
        Path nullPath = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    validator.validate(nullPath);
                });
    }

    @Test
    public void validateNoSuchPath()
            throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        Validator validator = new Validator();
        Path noSuchFile = Paths.get("n0SuchDF1l3");
        assertThrows("FileNotFoundException expected",
                FileNotFoundException.class,
                () -> {
                    validator.validate(noSuchFile);
                });
    }

    @Test
    public void validateDirPath() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        Validator validator = new Validator();
        Path noSuchFile = Paths.get(".");
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    validator.validate(noSuchFile);
                });
    }

    @Test
    public void validatePath() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validate(new File(TestFiles.EMPTY_ODS.toURI()).toPath());
        assertTrue("Package should be valid.", report.getValidationResult().isValid());
    }

    @Test
    public void validateEmpty() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validate(new File(TestFiles.EMPTY.toURI()).toPath());
        assertFalse("Package should NOT be valid, spreadsheets only.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("DOC-1")).count());
    }

    @Test
    public void validateNoMimeNoRoot() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validate(new File(TestFiles.NO_MIME_NO_ROOT_ODS.toURI()).toPath());
        assertTrue("Package should be valid.", report.getValidationResult().isValid());
    }

    @Test
    public void validateDocXml() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validate(new File(TestFiles.EMPTY_FODS.toURI()).toPath());
        assertTrue("Package should be valid.", report.getValidationResult().isValid());
    }

    @Test
    public void validateDocInvalidXml() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validate(new File(TestFiles.FLAT_NOT_VALID.toURI()).toPath());
        assertFalse("Document should NOT be valid.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("XML-4")).count());
    }

    @Test
    public void validateDocNotWellFormedXml() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validate(new File(TestFiles.FLAT_NOT_WF.toURI()).toPath());
        assertFalse("Document should NOT be valid.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("DOC-1")).count());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("XML-3")).count());
    }

    @Test
    public void validateSingleFormatInvalid() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validateSingleFormat(new File(TestFiles.DSIG_INVALID.toURI()).toPath(), Formats.ODS);
        assertFalse("Package should NOT be valid, spreadsheets only.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("DOC-7")).count());
    }

    @Test
    public void validateSingleFormatEmpty() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validateSingleFormat(new File(TestFiles.EMPTY.toURI()).toPath(), Formats.ODS);
        assertFalse("Package should NOT be valid, spreadsheets only.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("DOC-6")).count());
    }

    @Test
    public void validateSingleFormatNoMimeNoRoot() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator
                .validateSingleFormat(new File(TestFiles.NO_MIME_NO_ROOT_ODS.toURI()).toPath(), Formats.ODS);
        assertFalse("Package should NOT be valid, spreadsheets only.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("DOC-7")).count());
    }

    @Test
    public void validateSingleFormatDocXml() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validateSingleFormat(new File(TestFiles.EMPTY_FODS.toURI()).toPath(), Formats.ODS);
        assertTrue("Package should be valid.", report.getValidationResult().isValid());
    }

    @Test
    public void validateSpreadsheetDocInvalidXml() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validateSingleFormat(new File(TestFiles.FLAT_NOT_VALID.toURI()).toPath(), Formats.ODS);
        assertFalse("Document should NOT be valid.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("XML-4")).count());
    }

    @Test
    public void validateSpreadsheetDocNotWellFormedXml() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validateSingleFormat(new File(TestFiles.FLAT_NOT_WF.toURI()).toPath(), Formats.ODS);
        assertFalse("Document should NOT be valid.", report.getValidationResult().isValid());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("DOC-1")).count());
        assertEquals(1, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("XML-3")).count());
    }

    @Test
    public void validateExtendedSpreadsheet() throws ParseException, IOException, URISyntaxException {
        Validator validator = new Validator();
        ValidationReport report = validator.validateSingleFormat(new File(TestFiles.EXTENDED_SPREADSHEET.toURI()).toPath(), Formats.ODS);
        assertFalse("Document should NOT be valid.", report.getValidationResult().isValid());
        assertEquals(2, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("DOC-8")).count());
        assertEquals(0, report.getValidationResult().getMessages().stream().filter(m -> m.getId().equals("XML-3")).count());
    }
}
