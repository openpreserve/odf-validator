package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.messages.MessageLog;

import nl.jqno.equalsverifier.EqualsVerifier;

public class PackageMimeTypeRuleTest {
    private final Rule rule = Rules.odf3();

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(PackageMimeTypeRule.class).verify();
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
    public void testCheckValidPackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_ODS, rule);
        assertFalse("Valid Package should not return errors", messages.hasErrors());
    }

    @Test
    public void testCheckNotZipPackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_FODS, rule);
        assertTrue("Document XML should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-3")).count() > 0).count());
    }

    @Test
    public void testCheckNotWellFormedPackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.BADLY_FORMED_PKG, rule);
        assertTrue("Badly formed package should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-3")).count() > 0).count());
    }

    @Test
    public void testCheckInvalidPackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.MIME_EXTRA_ODS, rule);
        assertFalse("Invalid extra headers for mimetype is OK.", messages.hasErrors());
    }

    @Test
    public void testCheckNoMimePackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.NO_MIME_ROOT_ODS, rule);
        assertTrue("No mimetype with root entry (invalid) return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-3")).count() > 0).count());
    }

    @Test
    public void testCheckNoMimeNoRootPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.NO_MIME_NO_ROOT_ODS, rule);
        assertTrue("No mimetype with no root entry (valid) should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-3")).count() > 0).count());
    }
}
