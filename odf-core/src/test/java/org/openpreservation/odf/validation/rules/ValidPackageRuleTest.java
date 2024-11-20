package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ValidPackageRuleTest {
    private final Rule rule = Rules.odf2();

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ValidPackageRule.class).verify();
    }

    @Test
    public void testGetInstance() throws ParserConfigurationException, SAXException {
        assertNotNull("Returned Rule should not be null", rule);
    }

    @Test
    public void testCheckNullXmlDoc() throws ParserConfigurationException, SAXException {
        OpenDocument nullDoc = null;
        assertThrows("NullPointerException expected",
        NullPointerException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }

    @Test
    public void testCheckValidPackage()
            throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_ODS, rule);
        assertFalse("Package should NOT return errors", messages.hasErrors());
    }

    @Test
    public void testCheckNotZipPackage()
            throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_FODS, rule);
        assertTrue("Document XML should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL_2")).count() > 0).count());
    }

    @Test
    public void testCheckNotWellFormedPackage()
            throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.BADLY_FORMED_PKG, rule);
        assertTrue("Document XML should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL_2")).count() > 0).count());
    }

    @Test
    public void testCheckInvalidPackage()
            throws URISyntaxException, ParseException, FileNotFoundException {
        MessageLog messages = Utils.getMessages(TestFiles.MIME_EXTRA_ODS, rule);
        assertTrue("Document XML should return errors", messages.hasErrors());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL_2")).count() > 0).count());
    }
}
