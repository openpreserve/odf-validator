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
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;

import nl.jqno.equalsverifier.EqualsVerifier;

public class SubDocumentRuleTest {
    private final Rule rule = Rules.odf10();

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(SubDocumentRule.class).verify();
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
        assertTrue("Valid Package should not return errors", messages.hasWarnings());
        assertEquals(1, messages.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_10")).count() > 0).count());
    }

    @Test
    public void testCheckNotZipPackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_FODS, rule);
        assertFalse("Document XML should NOT return errors", messages.hasErrors());
    }

    @Test
    public void testCheckNotWellFormedPackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.BADLY_FORMED_PKG, rule);
        assertFalse("Badly formed package does not contain digital signatures.", messages.hasErrors());
    }

    @Test
    public void testCheckInvalidPackage() throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.MIME_EXTRA_ODS, rule);
        assertFalse("Invalid extra headers for contains an empty subdocument.", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_10")).count() > 0).count());
    }

    @Test
    public void testCheckValidDsigPackage() throws IOException, URISyntaxException, ParseException {
        MessageLog messages = Utils.getMessages(TestFiles.DSIG_VALID, rule);
        assertTrue("File contains an empty sub document.", messages.hasWarnings());
        assertEquals(1, messages.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_10")).count() > 0).count());
    }
}
