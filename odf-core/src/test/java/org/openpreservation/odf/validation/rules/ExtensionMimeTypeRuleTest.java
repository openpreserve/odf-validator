package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.Test;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ExtensionMimeTypeRuleTest {
    private final Rule rule = Rules.odf4();

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ExtensionMimeTypeRule.class).verify();
    }

    @Test
    public void testGetInstance() {
        assertNotNull("Returned Rule should not be null", rule);
    }

    @Test
    public void testCheckNullXmlDoc() {
        OpenDocument nullDoc = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }

    @Test
    public void testCheckValidPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_ODS, rule);
        assertFalse("Valid Package should not return errors", messages.hasErrors());
    }

    @Test
    public void testCheckNotZipPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_FODS, rule);
        assertTrue("Document XML should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-4")).count() > 0).count());
    }

    @Test
    public void testCheckNotWellFormedPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.BADLY_FORMED_PKG, rule);
        assertTrue("Badly formed package should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-4")).count() > 0).count());
    }

    @Test
    public void testCheckInvalidPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.MIME_EXTRA_ODS, rule);
        assertFalse("Invalid extra headers for mimetype is OK.", messages.hasErrors());
    }

    @Test
    public void testCheckNotOdsPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.DSIG_INVALID, rule);
        assertTrue("DSIG file has wrong MIME and extension", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-4")).count() > 0).count());
    }

    @Test
    public void testCheckBadExtPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OpenDocument doc = Documents.openDocumentOf(
                Paths.get(new File(TestFiles.ODF4_BAD_EXT.toURI()).getAbsolutePath()),
                parser.parsePackage(Paths.get(new File(TestFiles.ODF4_BAD_EXT.toURI()).getAbsolutePath())));
        assertEquals("Package should have spreadsheet MIME value", Formats.ODS.mime, doc.getPackage().getMimeType());
        MessageLog results = rule.check(doc);
        assertTrue("Bad extension only but should be invalid", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-4")).count() > 0).count());
    }

    @Test
    public void testCheckBadMimePackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OpenDocument doc = Documents.openDocumentOf(Paths.get(new File(TestFiles.ODF4_BAD_MIME.toURI()).getAbsolutePath()), 
                parser.parsePackage(Paths.get(new File(TestFiles.ODF4_BAD_MIME.toURI()).getAbsolutePath())));
        assertEquals("Package should have template MIME value", Formats.OTS.mime, doc.getPackage().getMimeType());
        assertTrue("Package should have spreadsheet extension.",
                doc.getPackage().getName().endsWith(Formats.ODS.extension));
        MessageLog results = rule.check(doc);
        assertTrue("Bad extension only but should be invalid", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-4")).count() > 0).count());
    }
}
