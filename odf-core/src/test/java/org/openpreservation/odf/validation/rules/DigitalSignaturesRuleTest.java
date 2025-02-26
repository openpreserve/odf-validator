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

public class DigitalSignaturesRuleTest {
    private final Rule rule = Rules.odf9();

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(DigitalSignaturesRule.class).verify();
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
    public void testCheckNotZipPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_FODS, rule);
        assertFalse("Document XML should NOT return errors", messages.hasErrors());
    }

    @Test
    public void testCheckNotWellFormedPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.BADLY_FORMED_PKG, rule);
        assertFalse("Badly formed package does not contain digital signatures.", messages.hasErrors());
    }

    @Test
    public void testCheckInvalidPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.MIME_EXTRA_ODS, rule);
        assertFalse("Invalid extra headers for mimetype is OK.", messages.hasErrors());
    }

    @Test
    public void testCheckValidDsigPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.DSIG_VALID, rule);
        assertTrue("File contains valid digital signatures.", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-9")).count() > 0).count());
    }
}
